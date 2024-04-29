package in.nucleusteq.plasma.service.serviceImpl;

import in.nucleusteq.plasma.configration.TokenConfig;
import in.nucleusteq.plasma.constant.Constants;
import in.nucleusteq.plasma.constant.ErrorConstants;
import in.nucleusteq.plasma.dao.EmployeeRepository;
import in.nucleusteq.plasma.dao.TokenRepository;
import in.nucleusteq.plasma.dto.commonDto.ValidateTokenResponse;
import in.nucleusteq.plasma.exception.ResourceNotFoundException;
import in.nucleusteq.plasma.exception.UnauthorizedAccessException;
import in.nucleusteq.plasma.entity.Employee;
import in.nucleusteq.plasma.entity.RefreshToken;
import in.nucleusteq.plasma.service.ValidateTokenService;
import in.nucleusteq.plasma.utils.TokenUtils;
import io.jsonwebtoken.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
@Service
public class ValidateTokenServiceImpl implements ValidateTokenService {
    /**
     * The Token Utils Object.
     */
    @Autowired
    private TokenUtils tokenUtils;

    /**
     * The Token Config Object.
     */
    @Autowired
    private TokenConfig tokenConfig;

    /**
     * The Token Repository Object.
     */
    @Autowired
    private TokenRepository tokenRepository;

    /**
     * The Individual Repository Object.
     */
    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * The Logger Object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateTokenService.class);
    /**
     * @param authToken
     * @return
     */
    @Override
    public ValidateTokenResponse validateToken(String authToken) {
        ValidateTokenResponse validateTokenResponseOutDTO = new ValidateTokenResponse();
        String token = tokenUtils.removeBearerPrefixFromToken(authToken);
        String email = getEmailFromToken(token);

        RefreshToken userRefTokenEmail = tokenRepository.findByEmail(email);
        if (Objects.isNull(userRefTokenEmail)) {
            LOGGER.error(ErrorConstants.RECORD_NOT_FOUND, email);
            throw new ResourceNotFoundException(String.format(ErrorConstants.RECORD_NOT_FOUND, email));
        }

        if (Objects.nonNull(userRefTokenEmail.getInvalidatedToken())
                && userRefTokenEmail.getInvalidatedToken().equals(token)) {
            LOGGER.error(ErrorConstants.INVALID_TOKEN_USER, email);
            throw new UnauthorizedAccessException(String.format(ErrorConstants.INVALID_TOKEN_USER, email));
        }

        Employee employeeDetails = employeeRepository.getByEmail(email);
        if (Objects.isNull(employeeDetails)) {
            LOGGER.error(ErrorConstants.USER_NOT_FOUND_MESSAGE, email);
            throw new ResourceNotFoundException(String.format(ErrorConstants.USER_NOT_FOUND_MESSAGE, email));
        }

        validateTokenResponseOutDTO.setMessage(Constants.TOKEN_VALIDATED_SUCCESSFULLY);
        return validateTokenResponseOutDTO;
    }

    /**
     * @param token
     * @return
     */
    @Override
    public String getEmailFromToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(tokenConfig.getTokenSecret().getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token).getBody();
           JSONObject jsonObject = new JSONObject(claims);
            return jsonObject.getJSONObject(Constants.DATA_KEY).getString(Constants.EMAIL_KEY);
        } catch (SignatureException signatureException) {
            LOGGER.error("Signature is not valid for token: {} , Throwing SignatureException", token);
            throw new UnauthorizedAccessException(ErrorConstants.INVALID_TOKEN);
        }  catch (MalformedJwtException e) {
            LOGGER.error("Invalid algorithm & token type in token: {}, Throwing MalformedJwtException", token);
            throw new UnauthorizedAccessException(ErrorConstants.MALFORMEND_EXCEPTION_MESSAGE);
        } catch (UnsupportedJwtException e) {
            LOGGER.error("Missing required fields in token: {}, Throwing UnsupportedJwtException", token);
            throw new UnauthorizedAccessException(ErrorConstants.UNSUPPORTED_EXCEPTION_MESSAGE);
        }
//        catch (ExpiredJwtException e) {
//            LOGGER.error("The token: {} is expired, Throwing ExpiredJwtException", token);
//            throw new UnauthorizedAccessException(ErrorConstants.EXPIRED_TOKEN);
//        }
    }
}

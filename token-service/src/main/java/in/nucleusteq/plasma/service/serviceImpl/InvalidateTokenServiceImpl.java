package in.nucleusteq.plasma.service.serviceImpl;

import in.nucleusteq.plasma.constant.Constants;
import in.nucleusteq.plasma.constant.ErrorConstants;
import in.nucleusteq.plasma.dto.inDto.InvalidateTokenInDto;
import in.nucleusteq.plasma.dto.outDto.InvalidateTokenOutDto;
import in.nucleusteq.plasma.exception.UnauthorizedAccessException;
import in.nucleusteq.plasma.service.InvalidateTokenService;
import in.nucleusteq.plasma.service.RefreshTokenService;
import in.nucleusteq.plasma.utils.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class InvalidateTokenServiceImpl implements InvalidateTokenService {
    /**
     * The RefreshTokenServiceImpl Object.
     */
    @Autowired
    private RefreshTokenService refreshTokenService;
    /**
     * The Token Utils Object.
     */
    @Autowired
    private TokenUtils tokenUtils;

    /**
     * The logger object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(InvalidateTokenService.class);


    @Override
    public InvalidateTokenOutDto invalidateToken(InvalidateTokenInDto invalidateTokenInDTO, Map<String, String> headers) {
        String email = invalidateTokenInDTO.getEmail();
        String authToken = headers.get(Constants.AUTHORIZATION);
        String userAgent = headers.get(Constants.USER_AGENT);

        String token = tokenUtils.removeBearerPrefixFromToken(authToken);
        String extractedEmailFromToken = tokenUtils.getEmailFromToken(token);

        if (!email.equals(extractedEmailFromToken)) {
            LOGGER.error(String.format("Provided token does not belong to the email id {}, throwing UnauthorizedException"),
                    invalidateTokenInDTO.getEmail());
            throw new UnauthorizedAccessException(
                    String.format(ErrorConstants.PROVIDED_TOKEN_INVALID, invalidateTokenInDTO.getEmail()));
        }
        refreshTokenService.saveRefreshTokenForInvalidate(token, email);

        InvalidateTokenOutDto invalidateTokenOutDTO = new InvalidateTokenOutDto();
        invalidateTokenOutDTO.setMessage(Constants.TOKEN_INVALIDATED);
        return invalidateTokenOutDTO;
    }
}

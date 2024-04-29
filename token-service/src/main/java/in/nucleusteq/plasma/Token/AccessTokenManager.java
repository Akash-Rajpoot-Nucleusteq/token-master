package in.nucleusteq.plasma.Token;

import com.google.gson.Gson;
import in.nucleusteq.plasma.configration.TokenConfig;
import in.nucleusteq.plasma.constant.Constants;
import in.nucleusteq.plasma.constant.ErrorConstants;
import in.nucleusteq.plasma.exception.UnauthorizedAccessException;
import in.nucleusteq.plasma.entity.EmployeeDetails;
import in.nucleusteq.plasma.entity.TokenData;
import in.nucleusteq.plasma.utils.TokenUtils;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@Component
public class AccessTokenManager implements TokenManager{
    /**
     * The logger variable to log information or errors related to
     * AccessTokenManagerImpl class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AccessTokenManager.class);

    @Autowired
    private TokenConfig tokenConfig;
    @Autowired
    private TokenUtils tokenUtils;
    private static final Gson GSON = new Gson();
    /**
     * @param employeeDetails - details of user.
     * @return it return generated token.
     */
    @Override
    public String generateToken(EmployeeDetails employeeDetails) {
        LocalDateTime tokenIssuedDate = tokenUtils.getCurrentDateTime();
        LocalDateTime tokenExpirationDate = tokenUtils.getTokenExpirationDate(tokenIssuedDate, tokenConfig.getTokenExpiration());
        Map<String,Object> claims = new HashMap<>();
        claims.put(Constants.DATA_KEY,employeeDetails);
        Key secretKey =new SecretKeySpec(tokenConfig.getTokenSecret().getBytes(StandardCharsets.UTF_8),
                SignatureAlgorithm.HS256.getJcaName());
        Map<String,Object> header = new HashMap<>();
        header.put(Constants.HEADER_TYPE_KEY,Constants.HEADER_JWT_VALUE);

        String token = Jwts.builder().setClaims(claims)
                .setSubject(employeeDetails.getUserId().toString())
                .setIssuedAt(tokenUtils.convertToDateObject(tokenIssuedDate))
                .setExpiration(tokenUtils.convertToDateObject(tokenExpirationDate))
                .signWith(SignatureAlgorithm.HS256,secretKey).setHeader(header).compact();

        return token;
    }
    /**
     * This function is used to fetch userEmail from token.
     *
     * @param token - token type.
     * @return - TokenData - contains data related to token.
     * @throws UnauthorizedAccessException
     */
    @Override
    public TokenData getDataFromToken(String token) throws UnauthorizedAccessException {
        try{
            Claims claims = Jwts.parser()
                    .setSigningKey(tokenConfig.getTokenSecret().getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token).getBody();
            String tokenData = claims.get(Constants.DATA_KEY).toString().replace(" ","");
            return GSON.fromJson(tokenData,TokenData.class);
        } catch (SignatureException signatureException) {
            LOGGER.error("Signature is not valid for token: {} , Throwing SignatureException", token);
            throw new UnauthorizedAccessException(ErrorConstants.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            LOGGER.error("The token: {} is expired, Throwing ExpiredJwtException", token);
            throw new UnauthorizedAccessException(ErrorConstants.EXPIRED_TOKEN);
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid algorithm & token type in token: {}, Throwing MalformedJwtException", token);
            throw new UnauthorizedAccessException(ErrorConstants.MALFORMEND_EXCEPTION_MESSAGE);
        } catch (UnsupportedJwtException e) {
            LOGGER.error("Missing required fields in token: {}, Throwing UnsupportedJwtException", token);
            throw new UnauthorizedAccessException(ErrorConstants.UNSUPPORTED_EXCEPTION_MESSAGE);
        }
    }
}

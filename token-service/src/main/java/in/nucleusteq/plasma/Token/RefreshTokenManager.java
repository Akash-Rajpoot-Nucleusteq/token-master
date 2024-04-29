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
import org.json.JSONObject;
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
public class RefreshTokenManager implements TokenManager {
    /**
     * The TokenConfig object.
     */
    @Autowired
    private TokenConfig tokenConfig;

    /**
     * The tokenUtils object.
     */
    @Autowired
    private TokenUtils tokenUtils;
    /**
     * The Gson Object.
     */
    private static final Gson GSON = new Gson();
    /**
     * @param employeeDetails - details of user.
     * @return it return generated token.
     */
    /**
     * The logger variable to log information or errors related to
     * RefreshTokenManagerImpl class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshTokenManager.class);

    @Override
    public String generateToken(EmployeeDetails employeeDetails) {
        LocalDateTime tokenIssuedDate = tokenUtils.getCurrentDateTime();
        LocalDateTime tokenExpirationDate = tokenUtils.getRefreshTokenExpirationDate(tokenIssuedDate,
                tokenConfig.getRefreshTokenExpiration());
        Map<String, Object> claims = new HashMap<>();
        Map<Object, Object> dataMap = new HashMap<>();

        dataMap.put(Constants.MODE_KEY, Constants.REFRESH);
        dataMap.put(Constants.EMAIL_KEY, employeeDetails.getEmail());
        claims.put(Constants.DATA_KEY, dataMap);

        Key secretKey = new SecretKeySpec(tokenConfig.getRefreshTokenSecretKey().getBytes(StandardCharsets.UTF_8),
                SignatureAlgorithm.HS256.getJcaName());
        Map<String, Object> header = new HashMap<>();
        header.put(Constants.HEADER_TYPE_KEY, Constants.HEADER_JWT_VALUE);

        String token = Jwts.builder().setClaims(claims)
                .setSubject(employeeDetails.getUserId().toString())
                .setIssuedAt(tokenUtils.convertToDateObject(tokenIssuedDate))
                .setExpiration(tokenUtils.convertToDateObject(tokenExpirationDate))
                .signWith(SignatureAlgorithm.HS256, secretKey).setHeader(header).compact();

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
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(tokenConfig.getRefreshTokenSecretKey().getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token).getBody();
            JSONObject jsonData = new JSONObject(claims);
            JSONObject tokenData = jsonData.getJSONObject(Constants.DATA_KEY);

            return GSON.fromJson(tokenData.toString(), TokenData.class);

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

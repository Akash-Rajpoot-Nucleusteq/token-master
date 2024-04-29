package in.nucleusteq.plasma.utils;


import in.nucleusteq.plasma.configration.TokenConfig;
import in.nucleusteq.plasma.constant.Constants;
import in.nucleusteq.plasma.constant.ErrorConstants;
import in.nucleusteq.plasma.exception.UnauthorizedAccessException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class TokenUtils {
    /**
     * The logger variable to log information or errors related to
     * TokenController class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenUtils.class);

    @Autowired
    private TokenConfig tokenConfig;
    /**
     * This method is used to return current date and time.
     * Required this method for testing purpose.
     *
     * @return LocalDateTime - contains current date and time.
     */
    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
    /**
     * This method is used to return date object converted from LocalDateTime.
     *
     * @param localDateTime - date that need to be converted date object
     * @return Date - contains current date and time.
     */
    public Date convertToDateObject(final LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneOffset.UTC).toInstant());
    }
    /**
     * This method returns token expiration date.
     *
     * @param tokenIssuedDate          - date at which token is issued
     * @param tokenExpirationInSeconds - time with which token time need to be
     *                                 increased.
     * @return date of token expiry
     */
    public LocalDateTime getTokenExpirationDate(final LocalDateTime tokenIssuedDate,
                                                final Long tokenExpirationInSeconds) {
        return tokenIssuedDate.plusSeconds(tokenExpirationInSeconds);
    }
    /**
     * This method return refresh token expiration date.
     *
     * @param tokenIssuedDate              - date at which token is issued
     * @param refreshTokenExpirationInDays - days with which refresh token need to
     *                                     be increased.
     * @return date of refresh token expiry
     */
    public LocalDateTime getRefreshTokenExpirationDate(final LocalDateTime tokenIssuedDate,
                                                       final Long refreshTokenExpirationInDays) {
        return tokenIssuedDate.plusDays(refreshTokenExpirationInDays);
    }
    /**
     * This method remove bearer prefix from the token.
     *
     * @param authToken - authorization token
     * @return authToken without bearer prefix.
     * @throws in.nucleusteq.plasma.exception.UnauthorizedAccessException
     */
    public String removeBearerPrefixFromToken(final String authToken) throws UnauthorizedAccessException {
        String token = authToken.split(" ")[1];
        if (token.length() > 1) {
            return token;
        } else {
            LOGGER.error("Invalid Token: {}, size must be greater one, Throwing UnauthorizedException", authToken);
            throw new UnauthorizedAccessException(String.format(ErrorConstants.INVALID_TOKEN));
        }
    }
    /**
     * This function helps to fetch the email from the token and authenticate a
     * token.
     *
     * @param token - authorization token.
     * @throws UnauthorizedAccessException
     * @return Email.
     */
    public String getEmailFromToken(final String token) throws UnauthorizedAccessException {
        try {
            Claims claims = Jwts.parser().setSigningKey(tokenConfig.getTokenSecret().getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token).getBody();
            JSONObject jsonObject = new JSONObject(claims);
            return jsonObject.getJSONObject(Constants.DATA_KEY).getString(Constants.EMAIL_KEY);
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

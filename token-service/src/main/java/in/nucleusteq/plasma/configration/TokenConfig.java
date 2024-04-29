package in.nucleusteq.plasma.configration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import in.nucleusteq.plasma.constant.Constants;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Configuration
@EnableWebMvc
@ToString
@EqualsAndHashCode
public class TokenConfig {

    /**
     * The secret token that needs to be provided with the JWT token.
     */
    @Value("${jwt.secret}")
    private String tokenSecret;

    /**
     * The expiration time of JWT tokens.
     */
    @Value("${jwt.expiration}")
    private Long tokenExpiration;

    /**
     * The expiration time of refresh tokens.
     */
    @Value("${jwt.refresh.token.expiration}")
    private Long refreshTokenExpiration;

    /**
     * Getter method for retrieving the token secret.
     *
     * @return The token secret.
     */
    public String getTokenSecret() {
        return tokenSecret;
    }

    /**
     * Generates the secret key for refresh tokens.
     *
     * @return The secret key for refresh tokens.
     */
    public String getRefreshTokenSecretKey() {
        return tokenSecret + Constants.HYPHEN + Constants.REFRESH;
    }

    /**
     * Getter method for retrieving the token expiration time.
     *
     * @return The token expiration time.
     */
    public Long getTokenExpiration() {
        return tokenExpiration;
    }

    /**
     * Getter method for retrieving the refresh token expiration time.
     *
     * @return The refresh token expiration time.
     */
    public Long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }
}
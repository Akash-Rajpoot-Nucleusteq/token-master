package in.nucleusteq.plasma.factory;

import in.nucleusteq.plasma.Token.AccessTokenManager;
import in.nucleusteq.plasma.Token.RefreshTokenManager;
import in.nucleusteq.plasma.Token.TokenManager;
import in.nucleusteq.plasma.enums.TokenType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TokenManagerFactory {
    /**
     * The AccessTokenManager object.
     */
    @Autowired
    private AccessTokenManager accessTokenManager;
    /**
     * The RefreshTokenManager object.
     */
    @Autowired
    private RefreshTokenManager refreshTokenManager;
    /**
     * @param tokenType - type of token
     * @return it return the instance of class according to the given parameter.
     */
    public TokenManager getTokenManager(final TokenType tokenType){
        if(tokenType.equals(TokenType.ACCESS_TOKEN)){
            return accessTokenManager;
        }else{
            return refreshTokenManager;
        }
    }
}

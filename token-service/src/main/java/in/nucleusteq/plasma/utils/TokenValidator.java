package in.nucleusteq.plasma.utils;

import in.nucleusteq.plasma.configration.TokenConfig;
import in.nucleusteq.plasma.constant.Constants;
import in.nucleusteq.plasma.constant.ErrorConstants;
import in.nucleusteq.plasma.dao.EmployeeRepository;
import in.nucleusteq.plasma.dto.inDto.GenerateRefreshTokenInDto;
import in.nucleusteq.plasma.dto.inDto.GenerateTokenInDto;
import in.nucleusteq.plasma.exception.BadRequestException;
import in.nucleusteq.plasma.exception.ResourceNotFoundException;
import in.nucleusteq.plasma.exception.UnauthorizedAccessException;
import in.nucleusteq.plasma.entity.Employee;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
public class TokenValidator {
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    private TokenConfig tokenConfig;

    /**
     * The logger variable to log information or errors related to
     * TokenValidator class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenValidator.class);
    /**
     * This method validates the custom token API. Check for email id of the user
     * with the status of account.
     *
     * @param generateCustomTokenInDto - contains the email id.
     * @throws ResourceNotFoundException
     */
    public void validateGenerateCustomToken(final GenerateTokenInDto generateCustomTokenInDto)
            throws ResourceNotFoundException {
        validateEmailId(generateCustomTokenInDto.getEmail());
    }
    /**
     * This method checks for emailId and status of an user.
     *
     * @param email - email id of user.
     * @throws ResourceNotFoundException - throws exception in case emailId not found or
     *                               does not have active/
     *                               inactive status
     */
    public void validateEmailId(final String email) throws ResourceNotFoundException {
        Employee userCount = employeeRepository.getByEmail(email);
        if (userCount == null) {
            LOGGER.error(
                    "User account with email id {} is not in valid status or does not exist... throwing UserNotFound Exception",
                    email);
            throw new ResourceNotFoundException(String.format(ErrorConstants.USER_NOT_FOUND_MESSAGE, email));
        }
    }
    /**
     * This method checks, token is null or not.
     *
     * @param token - authorization token
     * @throws in.nucleusteq.plasma.exception.BadRequestException
     */
    public void verifyToken(final String token) throws BadRequestException {

        if (Objects.isNull(token) || !token.startsWith(Constants.BEARER)) {
            LOGGER.error("The token is null or start without Bearer prefix: {}, Thorwing BadRequestException", token);
            throw new BadRequestException(ErrorConstants.TOKEN_MUST_NOT_BE_NULL_OR_START_WITH_BEARER);
        }
    }
    /**
     * This method is used for the authentication of refreshToken.
     *
     * @param generateRefreshTokenInDto - refreshToken for authentication.
     * @throws UnauthorizedAccessException
     * @throws BadRequestException
     */
    public void validateAndAuthenticateRefreshToken(final GenerateRefreshTokenInDto generateRefreshTokenInDto)
            throws UnauthorizedAccessException {
        String refreshSecret = tokenConfig.getRefreshTokenSecretKey();
        try {
            Jwts.parser().setSigningKey(refreshSecret.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(generateRefreshTokenInDto.getRefreshToken()).getBody();
        } catch (SignatureException e) {
            LOGGER.error("Signature is not valid for the token {}, throwing SignatureException",
                    generateRefreshTokenInDto.getRefreshToken());
            throw new UnauthorizedAccessException(ErrorConstants.INVALID_TOKEN);
        } catch (ExpiredJwtException exception) {
            LOGGER.error("The token {} is expired, throwing ExpiredJwtException",
                    generateRefreshTokenInDto.getRefreshToken());
            throw new UnauthorizedAccessException(ErrorConstants.EXPIRED_TOKEN);
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid algorithm & token type {}, throwing MalformedJwtException",
                    generateRefreshTokenInDto.getRefreshToken());
            throw new UnauthorizedAccessException(ErrorConstants.MALFORMEND_EXCEPTION_MESSAGE);
        } catch (UnsupportedJwtException e) {
            LOGGER.error("Missing required fields in token {}, throwing UnsupportedJwtException",
                    generateRefreshTokenInDto.getRefreshToken());
            throw new UnauthorizedAccessException(ErrorConstants.UNSUPPORTED_EXCEPTION_MESSAGE);
        }
    }
}

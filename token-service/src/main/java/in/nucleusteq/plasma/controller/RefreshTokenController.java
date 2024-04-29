package in.nucleusteq.plasma.controller;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.nucleusteq.plasma.dto.inDto.GenerateRefreshTokenInDto;
import in.nucleusteq.plasma.dto.outDto.GenerateTokenOutDto;
import in.nucleusteq.plasma.exception.ResourceNotFoundException;
import in.nucleusteq.plasma.exception.UnauthorizedAccessException;
import in.nucleusteq.plasma.service.RefreshTokenService;
import in.nucleusteq.plasma.utils.TokenValidator;

/**
 * RefreshTokenController handles endpoints related to refreshing tokens.
 */
@RestController
@RequestMapping("plasma/refresh-token")
public class RefreshTokenController {
    /**
     * The RefreshTokenService for handling refresh token operations.
     */
    @Autowired
    private RefreshTokenService refreshTokenService;
    /**
     * The TokenValidator for validating and authenticating tokens.
     */
    @Autowired
    private TokenValidator tokenValidator;

    /**
     * Logger for logging messages.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshTokenController.class);

    /**
     * Generates a new access token from a refresh token.
     *
     * @param generateRefreshTokenInDto The input DTO containing the refresh token
     *                                  details.
     * @param headers                   The HTTP headers containing the user agent.
     * @return The output DTO containing the new access token details.
     * @throws UnauthorizedAccessException If the refresh token is unauthorized.
     * @throws ResourceNotFoundException   If the refresh token is not found.
     * @throws IOException                 If an I/O error occurs during token
     *                                     generation.
     */
    @PostMapping("/generateFromRefreshToken")
    public GenerateTokenOutDto generateRefreshToken(
            @RequestBody final GenerateRefreshTokenInDto generateRefreshTokenInDto,
            @RequestHeader final Map<String, String> headers) throws UnauthorizedAccessException,
            ResourceNotFoundException, IOException {

        LOGGER.info("Request received to generate token from refresh token for token {} ",
                generateRefreshTokenInDto.toString());

        tokenValidator.validateAndAuthenticateRefreshToken(generateRefreshTokenInDto);
        LOGGER.info("Successfully generated Refresh token for {}", generateRefreshTokenInDto.toString());
        return refreshTokenService.generateAndValidateRefreshToken(generateRefreshTokenInDto,
                headers.get("user-agent"));
    }

}

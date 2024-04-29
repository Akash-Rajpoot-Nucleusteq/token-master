package in.nucleusteq.plasma.controller;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.nucleusteq.plasma.constant.Constants;
import in.nucleusteq.plasma.dto.commonDto.ValidateTokenResponse;
import in.nucleusteq.plasma.dto.inDto.GenerateTokenInDto;
import in.nucleusteq.plasma.dto.inDto.InvalidateTokenInDto;
import in.nucleusteq.plasma.dto.outDto.GenerateTokenOutDto;
import in.nucleusteq.plasma.dto.outDto.InvalidateTokenOutDto;
import in.nucleusteq.plasma.dto.outDto.TokenDataOutDto;
import in.nucleusteq.plasma.exception.BadRequestException;
import in.nucleusteq.plasma.exception.ResourceNotFoundException;
import in.nucleusteq.plasma.exception.UnauthorizedAccessException;
import in.nucleusteq.plasma.service.InvalidateTokenService;
import in.nucleusteq.plasma.service.TokenService;
import in.nucleusteq.plasma.service.ValidateTokenService;
import in.nucleusteq.plasma.utils.TokenValidator;

/**
 * TokenController handles endpoints related to token management.
 */
@RestController
@RequestMapping("plasma/token")
public class TokenController {
	/**
	 * Logger for logging messages.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(TokenController.class);
	/**
	 * The TokenService for generating custom tokens.
	 */
	@Autowired
	private TokenService tokenService;
	/**
	 * The TokenValidator for validating tokens.
	 */
	@Autowired
	private TokenValidator tokenValidator;
	/**
	 * The ValidateTokenService for validating tokens.
	 */
	@Autowired
	private ValidateTokenService validateTokenService;
	/**
	 * The InvalidateTokenService for invalidating tokens.
	 */
	@Autowired
	private InvalidateTokenService invalidateTokenService;

	/**
	 * Generates a custom token for the specified email.
	 *
	 * @param customTokenInDto The input DTO containing the email for token
	 *                         generation.
	 * @param headers          The HTTP headers containing the user agent.
	 * @return The output DTO containing the generated token details.
	 * @throws IOException If an I/O error occurs during token generation.
	 */
	@PostMapping("/generate-custom-token")
	public GenerateTokenOutDto getGenerateCustomToken(@RequestBody GenerateTokenInDto customTokenInDto,
			@RequestHeader final Map<String, String> headers) throws IOException {
		LOGGER.info("start to get generated token {} ", customTokenInDto.getEmail());
		GenerateTokenOutDto generateTokenOutDto = tokenService.generateCustomToken(customTokenInDto,
				headers.get("user-agent"));
		LOGGER.info("end to get generated token {} ", customTokenInDto.getEmail());
		return generateTokenOutDto;
	}

	/**
	 * Validates the given token.
	 *
	 * @param headers The HTTP headers containing the token.
	 * @return The response DTO containing the token validation result.
	 * @throws BadRequestException         If the request is invalid.
	 * @throws ResourceNotFoundException   If the token is not found.
	 * @throws UnauthorizedAccessException If the token is unauthorized.
	 */
	@GetMapping(path = "/validate")
	public ValidateTokenResponse validateToken(@RequestHeader final Map<String, String> headers)
			throws BadRequestException, ResourceNotFoundException, UnauthorizedAccessException {

		String token = headers.get(Constants.AUTHORIZATION);
		LOGGER.info("Request received to validate the token {}", token);

		tokenValidator.verifyToken(token);

		ValidateTokenResponse validateTokenResponseOutDTO = validateTokenService.validateToken(token);
		LOGGER.info("Successfully validated the token {} ", token);
		return validateTokenResponseOutDTO;
	}

	/**
	 * Invalidates the given token.
	 *
	 * @param invalidateTokenInDTO The input DTO containing the token and email for
	 *                             token invalidation.
	 * @param headers              The HTTP headers containing the token.
	 * @return The output DTO containing the token invalidation result.
	 * @throws ResourceNotFoundException   If the token is not found.
	 * @throws UnauthorizedAccessException If the token is unauthorized.
	 * @throws BadRequestException         If the request is invalid.
	 * @throws IOException                 If an I/O error occurs during token
	 *                                     invalidation.
	 */
	@PostMapping("/invalidate")
	public InvalidateTokenOutDto invalidateToken(@RequestBody final InvalidateTokenInDto invalidateTokenInDTO,
			@RequestHeader final Map<String, String> headers)
			throws ResourceNotFoundException, UnauthorizedAccessException, BadRequestException, IOException {
		LOGGER.info("Request received to invalidate token {}", invalidateTokenInDTO.toString());

		tokenValidator.verifyToken(headers.get(Constants.AUTHORIZATION));
		tokenValidator.validateEmailId(invalidateTokenInDTO.getEmail());

		InvalidateTokenOutDto invalidateTokenOutDto = invalidateTokenService
				.invalidateToken(invalidateTokenInDTO, headers);
		LOGGER.info("Successfully invalidate token for {}", invalidateTokenInDTO.toString());
		return invalidateTokenOutDto;
	}

	/**
	 * /**
	 * Fetches data using the provided headers.
	 *
	 * @param headers The headers containing authentication token.
	 * @return The token data output DTO.
	 * @throws BadRequestException         If the request is malformed.
	 * @throws ResourceNotFoundException   If the requested resource is not found.
	 * @throws UnauthorizedAccessException If the request lacks proper
	 *                                     authorization.
	 */
	@GetMapping(path = "/fetchdata")
	public TokenDataOutDto fetchData(@RequestHeader final Map<String, String> headers)
			throws BadRequestException, ResourceNotFoundException, UnauthorizedAccessException {

		String token = headers.get(Constants.AUTHORIZATION);
		LOGGER.info("Request recived to fetch the data from the token: {}", token);
		tokenValidator.verifyToken(token);
		TokenDataOutDto tokenDataOutDto = tokenService.fetchDataFromToken(token);
		LOGGER.info("Successfully fetched the data from the token: {}", token);
		return tokenDataOutDto;
	}
}

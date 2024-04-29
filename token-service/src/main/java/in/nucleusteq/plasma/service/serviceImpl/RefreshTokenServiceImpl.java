package in.nucleusteq.plasma.service.serviceImpl;

import in.nucleusteq.plasma.Token.TokenManager;
import in.nucleusteq.plasma.configration.TokenConfig;
import in.nucleusteq.plasma.constant.Constants;
import in.nucleusteq.plasma.constant.ErrorConstants;
import in.nucleusteq.plasma.dao.EmployeeRepository;
import in.nucleusteq.plasma.dao.TokenRepository;
import in.nucleusteq.plasma.dto.inDto.GenerateRefreshTokenInDto;
import in.nucleusteq.plasma.dto.outDto.GenerateTokenOutDto;
import in.nucleusteq.plasma.enums.TokenType;
import in.nucleusteq.plasma.exception.ResourceNotFoundException;
import in.nucleusteq.plasma.exception.UnauthorizedAccessException;
import in.nucleusteq.plasma.factory.TokenManagerFactory;
import in.nucleusteq.plasma.mapper.ModelMapperAdapter;
import in.nucleusteq.plasma.entity.Employee;
import in.nucleusteq.plasma.entity.EmployeeDetails;
import in.nucleusteq.plasma.entity.RefreshToken;
import in.nucleusteq.plasma.service.RefreshTokenService;
import in.nucleusteq.plasma.utils.TokenUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    /**
     * tokenConfig object.
     */
    @Autowired
    private TokenConfig tokenConfig;
    /**
     * tokenUtils object.
     */
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private TokenManagerFactory tokenManagerFactory;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ModelMapperAdapter mapper;

    /**
     * The LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshTokenService.class);

    /**
     * @param employeeDetails
     * @param tokenIssuedAt
     * @return
     */
    @Override
    public String generateRefreshToken(EmployeeDetails employeeDetails, LocalDateTime tokenIssuedAt) {
        Map<Object, Object> dataMap = new HashMap<>();

        // dataMap.put(Constants.USER_KEY, employeeDetails);
        dataMap.put(Constants.MODE_KEY, Constants.REFRESH);
        dataMap.put(Constants.EMAIL_KEY, employeeDetails.getEmail());

        Map<String, Object> claims = new HashMap<>();

        claims.put(Constants.DATA_KEY, dataMap);

        String refreshTokenSecret = tokenConfig.getTokenSecret() + Constants.HYPHEN + Constants.REFRESH;

        Key refreshTokenKey = new SecretKeySpec(refreshTokenSecret.getBytes(StandardCharsets.UTF_8),
                SignatureAlgorithm.HS256.getJcaName());

        Map<String, Object> header = new HashMap<>();
        header.put(Constants.HEADER_TYPE_KEY, Constants.HEADER_JWT_VALUE);

        LocalDateTime refreshTokenExpiration = tokenUtils.getRefreshTokenExpirationDate(tokenIssuedAt,
                tokenConfig.getRefreshTokenExpiration());

        return Jwts.builder().setClaims(claims)
                .setSubject(employeeDetails.getUserId().toString())
                .setIssuedAt(tokenUtils.convertToDateObject(tokenIssuedAt))
                .setExpiration(tokenUtils.convertToDateObject(refreshTokenExpiration))
                .signWith(SignatureAlgorithm.HS256, refreshTokenKey).setHeader(header).compact();
    }

    /**
     * @param refreshToken
     * @param email
     * @param tokenIssuedDt
     */
    @Override
    public void saveRefreshToken(String refreshToken, String email, LocalDateTime tokenIssuedDt) {
        RefreshToken userRefreshToken = tokenRepository.findByEmail(email);
        if (Objects.isNull(userRefreshToken)) {
            userRefreshToken = new RefreshToken();
        }
        userRefreshToken.setEmail(email);
        userRefreshToken.setRefreshToken(refreshToken);
        userRefreshToken.setRefreshTokenDt(tokenIssuedDt.toInstant(ZoneOffset.UTC));

        tokenRepository.save(userRefreshToken);
    }

    /**
     * @param token
     * @param email
     */
    @Override
    public void saveRefreshTokenForInvalidate(String token, String email) {
        RefreshToken userRefreshToken = tokenRepository.findByEmail(email);
        userRefreshToken.setInvalidatedToken(token);
        tokenRepository.save(userRefreshToken);
    }

    /**
     * @param generateRefreshTokenInDto
     * @param userAgent
     * @return
     */
    @Override
    public GenerateTokenOutDto generateAndValidateRefreshToken(GenerateRefreshTokenInDto generateRefreshTokenInDto,
            String userAgent) throws UnauthorizedAccessException, ResourceNotFoundException, IOException {
        GenerateTokenOutDto generateTokenOutDto = new GenerateTokenOutDto();

        TokenManager refreshTokenManager = tokenManagerFactory.getTokenManager(TokenType.REFRESH_TOKEN);
        String userEmail = refreshTokenManager.getDataFromToken(generateRefreshTokenInDto.getRefreshToken()).getEmail();
        Employee employee = employeeRepository.getByEmail(userEmail);

        if (Objects.isNull(employee)) {
            LOGGER.info("User is not found for email {} ", userEmail);
            throw new ResourceNotFoundException(String.format(ErrorConstants.USER_NOT_FOUND_MESSAGE, userEmail));
        }

        matchRefreshToken(generateRefreshTokenInDto.getRefreshToken(), userEmail);

        LocalDateTime tokenIssuedDate = tokenUtils.getCurrentDateTime();
        TokenManager accessTokenManager = tokenManagerFactory.getTokenManager(TokenType.ACCESS_TOKEN);
        String jwtToken = accessTokenManager.generateToken(mapper.map(employee, EmployeeDetails.class));
        String getRefreshToken = refreshTokenManager.generateToken(mapper.map(employee, EmployeeDetails.class));
        ;
        saveRefreshToken(getRefreshToken, userEmail, tokenIssuedDate);

        generateTokenOutDto.setAccessCode(jwtToken);
        generateTokenOutDto.setAccessCodeExpiry(tokenConfig.getTokenExpiration());
        generateTokenOutDto.setRefreshToken(getRefreshToken);
        generateTokenOutDto.setEmail(userEmail);

        return generateTokenOutDto;
    }

    /**
     * @param refreshToken
     * @param userEmail
     */
    @Override
    public void matchRefreshToken(String refreshToken, String userEmail) {
        RefreshToken userRefreshToken = tokenRepository.findByEmail(userEmail);
        if (!refreshToken.equals(userRefreshToken.getRefreshToken())) {
            LOGGER.error("Token does not belong to the user %s.", userEmail);
            throw new UnauthorizedAccessException(String.format("Token does not belong to the user %s.", userEmail));
        }
    }
}

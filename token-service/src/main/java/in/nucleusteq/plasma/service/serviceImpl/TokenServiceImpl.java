package in.nucleusteq.plasma.service.serviceImpl;

import in.nucleusteq.plasma.configration.TokenConfig;
import in.nucleusteq.plasma.constant.Constants;
import in.nucleusteq.plasma.constant.ErrorConstants;
import in.nucleusteq.plasma.dao.EmployeeRepository;
import in.nucleusteq.plasma.dto.inDto.GenerateTokenInDto;
import in.nucleusteq.plasma.dto.outDto.GenerateTokenOutDto;
import in.nucleusteq.plasma.dto.outDto.TokenDataOutDto;
import in.nucleusteq.plasma.entity.*;
import in.nucleusteq.plasma.enums.TokenType;
import in.nucleusteq.plasma.exception.ResourceNotFoundException;
import in.nucleusteq.plasma.factory.TokenManagerFactory;
import in.nucleusteq.plasma.mapper.ModelMapperAdapter;
import in.nucleusteq.plasma.service.RefreshTokenService;
import in.nucleusteq.plasma.service.TokenService;
import in.nucleusteq.plasma.utils.RoleUtils;
import in.nucleusteq.plasma.utils.TokenUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
public class TokenServiceImpl implements TokenService {
        @Autowired
        private EmployeeRepository employeeRepository;

        @Autowired
        private TokenConfig tokenConfig;

        @Autowired
        private TokenUtils tokenUtils;

        @Autowired
        private RefreshTokenService refreshTokenService;

        @Autowired
        private TokenManagerFactory tokenManagerFactory;

        @Autowired
        private ModelMapperAdapter modelMapperAdapter;

        @Autowired
        private RoleUtils roleUtils;

        /**
         * The logger variable to log information or errors related to TokenService
         * class.
         */
        private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);

        /**
         * @param generateCustomTokenInDto
         * @param userAgent
         * @return
         */
        @Override
        public GenerateTokenOutDto generateCustomToken(GenerateTokenInDto generateCustomTokenInDto, String userAgent) {
                Employee employee = employeeRepository.getByEmail(generateCustomTokenInDto.getEmail());
                LocalDateTime tokenIssuedDate = tokenUtils.getCurrentDateTime();
                UserWorkDetail workDetail = employee.getUserWorkDetail();
                Set<Role> roles = workDetail.getRoles();

                String finalrole = roleUtils.getHighestWeightRole(roles);

                String role = roleUtils.getHighestWeightRole(employee.getUserWorkDetail().getRoles());
                EmployeeDetails employeeDetails = modelMapperAdapter.map(employee, EmployeeDetails.class);
                employeeDetails.setRole(finalrole);
                String jwtToken = getJwtToken(employeeDetails, tokenIssuedDate);

                String refreshToken = refreshTokenService.generateRefreshToken(modelMapperAdapter
                                .map(employee, EmployeeDetails.class), tokenIssuedDate);
                refreshTokenService.saveRefreshToken(refreshToken, generateCustomTokenInDto.getEmail(),
                                tokenIssuedDate);
                GenerateTokenOutDto generateTokenOutDto = GenerateTokenOutDto.builder()
                                .accessCode(jwtToken)
                                .accessCodeExpiry(tokenConfig.getTokenExpiration())
                                .refreshToken(refreshToken)
                                .email(employee.getEmail()).build();
                generateTokenOutDto.setAccessCode(jwtToken);
                return generateTokenOutDto;
        }

        /**
         * @param employeeDetails
         * @param tokenIssuedDate
         * @return
         */
        @Override
        public String getJwtToken(EmployeeDetails employeeDetails, LocalDateTime tokenIssuedDate) {
                Map<String, Object> claims = new HashMap<>();
                claims.put(Constants.DATA_KEY, employeeDetails);

                Key tokenKey = new SecretKeySpec(tokenConfig.getTokenSecret().getBytes(StandardCharsets.UTF_8),
                                SignatureAlgorithm.HS256.getJcaName());

                Map<String, Object> header = new HashMap<>();
                header.put(Constants.HEADER_TYPE_KEY, Constants.HEADER_JWT_VALUE);

                LocalDateTime tokenExpirationDate = tokenUtils.getTokenExpirationDate(tokenIssuedDate,
                                tokenConfig.getTokenExpiration());

                return Jwts.builder().setClaims(claims).setSubject(employeeDetails.getUserId().toString())
                                .setIssuedAt(tokenUtils.convertToDateObject(tokenIssuedDate))
                                .setExpiration(tokenUtils.convertToDateObject(tokenExpirationDate))
                                .signWith(SignatureAlgorithm.HS256, tokenKey).setHeader(header).compact();
        }

        /**
         * @param authToken
         * @return
         */
        @Override
        public TokenDataOutDto fetchDataFromToken(String authToken) {
                String token = tokenUtils.removeBearerPrefixFromToken(authToken);
                TokenData tokenData = tokenManagerFactory.getTokenManager(TokenType.ACCESS_TOKEN)
                                .getDataFromToken(token);
                TokenDataOutDto tokenDataOutDto = buildTokenDataOutDto(tokenData);
                Employee employeeDetails = employeeRepository.getByEmail(tokenDataOutDto.getEmail());
                if (Objects.isNull(employeeDetails)) {
                        LOGGER.error(ErrorConstants.USER_NOT_FOUND_MESSAGE, tokenDataOutDto.getEmail());
                        throw new ResourceNotFoundException(
                                        String.format(ErrorConstants.USER_NOT_FOUND_MESSAGE,
                                                        tokenDataOutDto.getEmail()));
                }
                return tokenDataOutDto;
        }

        /**
         * @param tokenData
         * @return
         */
        @Override
        public TokenDataOutDto buildTokenDataOutDto(TokenData tokenData) {
                TokenDataOutDto tokenDataOutDto = new TokenDataOutDto();
                tokenDataOutDto.setEmail(tokenData.getEmail());
                tokenDataOutDto.setRole(tokenData.getRole());

                return tokenDataOutDto;
        }
}

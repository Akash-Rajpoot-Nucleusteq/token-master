package in.nucleusteq.plasma.service;

import in.nucleusteq.plasma.dto.inDto.GenerateRefreshTokenInDto;
import in.nucleusteq.plasma.dto.outDto.GenerateTokenOutDto;
import in.nucleusteq.plasma.entity.EmployeeDetails;

import java.io.IOException;
import java.time.LocalDateTime;

public interface RefreshTokenService {
    String generateRefreshToken(final EmployeeDetails employeeDetails,
                                final LocalDateTime tokenIssuedAt);

    void saveRefreshToken(final String refreshToken, final String email,
                          final LocalDateTime tokenIssuedDt);

    void saveRefreshTokenForInvalidate(final String token, final String email);

    GenerateTokenOutDto generateAndValidateRefreshToken(final GenerateRefreshTokenInDto generateRefreshTokenInDto,
                                                        final String userAgent) throws IOException;

    void matchRefreshToken(final String refreshToken, final String userEmail);
}

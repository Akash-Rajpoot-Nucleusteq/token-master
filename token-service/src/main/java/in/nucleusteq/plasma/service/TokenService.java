package in.nucleusteq.plasma.service;

import in.nucleusteq.plasma.dto.inDto.GenerateTokenInDto;
import in.nucleusteq.plasma.dto.outDto.GenerateTokenOutDto;
import in.nucleusteq.plasma.dto.outDto.TokenDataOutDto;
import in.nucleusteq.plasma.entity.EmployeeDetails;
import in.nucleusteq.plasma.entity.TokenData;

import java.time.LocalDateTime;

public interface TokenService {
    GenerateTokenOutDto generateCustomToken(GenerateTokenInDto generateCustomTokenInDto, String userAgent);
    String getJwtToken(final EmployeeDetails employeeDetails, final LocalDateTime tokenIssuedDate);
    TokenDataOutDto fetchDataFromToken(final String authToken);
    TokenDataOutDto buildTokenDataOutDto(final TokenData tokenData);
}

package in.nucleusteq.plasma.service;

import in.nucleusteq.plasma.dto.inDto.InvalidateTokenInDto;
import in.nucleusteq.plasma.dto.outDto.InvalidateTokenOutDto;

import java.util.Map;

public interface InvalidateTokenService {
    InvalidateTokenOutDto invalidateToken(final InvalidateTokenInDto invalidateTokenInDTO,
                                          final Map<String, String> headers);
}

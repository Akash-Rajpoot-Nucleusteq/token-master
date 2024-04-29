package in.nucleusteq.plasma.service;

import in.nucleusteq.plasma.dto.commonDto.ValidateTokenResponse;

public interface ValidateTokenService {
    ValidateTokenResponse validateToken(final String authToken);
    String getEmailFromToken(final String token);
}

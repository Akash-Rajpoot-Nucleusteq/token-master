package in.nucleusteq.plasma.dto.outDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RefreshTokenOutDto {
    private int tokenId;
    private String refreshToken;
    private Instant expiry;
    private String userName;
}

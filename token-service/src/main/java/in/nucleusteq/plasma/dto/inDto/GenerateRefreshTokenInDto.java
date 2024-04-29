package in.nucleusteq.plasma.dto.inDto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class GenerateRefreshTokenInDto {
    /**
     * The refreshToken.
     */
    private String refreshToken;
}

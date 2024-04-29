package in.nucleusteq.plasma.dto.outDto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class GenerateTokenOutDto {
    /**
     * The access token.
     */
    private String accessCode;

    /**
     * The access token expiry.
     */
    private Long accessCodeExpiry;

    /**
     * The email id.
     */
    private String email;

    /**
     * The refresh token.
     */
    private String refreshToken;
}

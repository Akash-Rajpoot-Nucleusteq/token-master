package in.nucleusteq.plasma.dto.commonDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponce {
    private String JwtToken;
    private String refreshToken;
    private String userName;
}

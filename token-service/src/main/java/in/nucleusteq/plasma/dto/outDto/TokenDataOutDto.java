package in.nucleusteq.plasma.dto.outDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenDataOutDto {
    /**
     * unique email.
     */
    private String email;
    /**
     * user role in the system.
     */
    private String role;
}

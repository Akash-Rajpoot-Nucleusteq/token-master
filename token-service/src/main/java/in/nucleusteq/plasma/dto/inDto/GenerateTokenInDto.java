package in.nucleusteq.plasma.dto.inDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenerateTokenInDto {
    /**
     * user email.
     */
    private String email;
}

package in.nucleusteq.plasma.dto.inDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class InvalidateTokenInDto {
    /**
     * The email id.
     */
    private String email;
}

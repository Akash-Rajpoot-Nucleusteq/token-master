package in.nucleusteq.plasma.dto.commonDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ValidateTokenResponse {
    /**
     * The Message.
     */
    private String message;
}

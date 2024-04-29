package in.nucleusteq.plasma.dto.outDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class InvalidateTokenOutDto {
    /**
     * The success message.
     */
    private String message;
}

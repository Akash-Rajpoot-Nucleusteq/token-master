package in.nucleusteq.plasma.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ErrorResponce {
    /**
     * It stores response error code.
     */
    private Integer errorCode;
    /**
     * It stores list of error messages.
     */
    private List<String> errorMessages;

    private boolean success;
}

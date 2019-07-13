package revolut.transfer.domain.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationFailure {
    private String failureReason;
}

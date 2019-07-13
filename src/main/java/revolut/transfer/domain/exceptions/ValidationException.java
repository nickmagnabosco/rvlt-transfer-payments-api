package revolut.transfer.domain.exceptions;

import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException {
    private List<ValidationFailure> failures = Lists.newArrayList();

    public ValidationException() {
    }

    public ValidationException(List<ValidationFailure> failures) {
        this.failures.addAll(failures);
    }

}

package revolut.transfer.domain.models.accounts;

import revolut.transfer.domain.exceptions.ValidationException;
import revolut.transfer.domain.exceptions.ValidationFailure;
import spark.utils.StringUtils;

public enum UserTitle {
    MR, MS, MISS, MX, DOC;

    public static UserTitle fromString(String value) {
        try {
            if (StringUtils.isBlank(value)) {
                throw new ValidationException(new ValidationFailure("User type is empty"));
            }
            return UserTitle.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new ValidationException(new ValidationFailure("Unsupported user type"));
        }
    }
}

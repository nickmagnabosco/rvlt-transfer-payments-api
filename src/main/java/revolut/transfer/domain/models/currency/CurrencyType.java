package revolut.transfer.domain.models.currency;

import revolut.transfer.domain.exceptions.ValidationException;
import revolut.transfer.domain.exceptions.ValidationFailure;
import spark.utils.StringUtils;

public enum CurrencyType {
    GBP,
    EUR,
    USD;

    public static CurrencyType fromString(String value) {
        try {
            if (StringUtils.isBlank(value)) {
                throw new ValidationException(new ValidationFailure("Currency type is empty"));
            }
            return CurrencyType.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new ValidationException(new ValidationFailure("Unsupported type"));
        }
    }
}

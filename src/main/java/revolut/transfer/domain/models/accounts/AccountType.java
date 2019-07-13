package revolut.transfer.domain.models.accounts;

import revolut.transfer.domain.exceptions.ValidationException;
import revolut.transfer.domain.exceptions.ValidationFailure;
import spark.utils.StringUtils;

public enum AccountType {
    UK, // Account with sort code and account number
    IBAN,
    USD; // routing number, account number, account type

    public static AccountType fromString(String value) {
        try {
            if (StringUtils.isBlank(value)) {
                throw new ValidationException(new ValidationFailure("Account type is empty"));
            }
            return AccountType.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new ValidationException(new ValidationFailure("Unsupported account type"));
        }
    }
}

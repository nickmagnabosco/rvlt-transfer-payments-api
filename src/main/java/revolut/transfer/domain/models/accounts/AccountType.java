package revolut.transfer.domain.models.accounts;

import revolut.transfer.domain.exceptions.ValidationException;
import revolut.transfer.domain.exceptions.ValidationFailure;
import revolut.transfer.domain.models.MonetaryAmount;
import revolut.transfer.domain.models.currency.CurrencyType;
import spark.utils.StringUtils;

public enum AccountType {
    UK(CurrencyType.GBP, MonetaryAmount.ZERO_GBP), // Account with sort code and account number
    EUR(CurrencyType.EUR, MonetaryAmount.ZERO_EUR),
    USD(CurrencyType.USD, MonetaryAmount.ZERO_USD); // routing number, account number, account type

    private final CurrencyType accountCurrency;
    private final MonetaryAmount initialAmount;

    public CurrencyType getAccountCurrency() {
        return accountCurrency;
    }

    public MonetaryAmount getInitialAmount() {
        return initialAmount;
    }

    AccountType(CurrencyType accountCurrency, MonetaryAmount initialAmount) {
        this.accountCurrency = accountCurrency;
        this.initialAmount = initialAmount;
    }

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

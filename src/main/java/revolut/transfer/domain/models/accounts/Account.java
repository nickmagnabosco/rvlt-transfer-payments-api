package revolut.transfer.domain.models.accounts;

import lombok.Data;
import revolut.transfer.domain.commands.CreateAccountHolderCommand;
import revolut.transfer.domain.models.MonetaryAmount;
import revolut.transfer.domain.models.currency.CurrencyType;

import java.util.UUID;

@Data
public class Account {

    private final String id;
    private final String accountHolderId;
    private final String name;
    private final AccountType accountType;
    private final BankAccountDetails bankAccountDetails;

    private final CurrencyType currencyType;
    private final MonetaryAmount balance;

    public static Account createInitialAccount(CreateAccountHolderCommand createAccountHolderCommand, AccountDetailsFactory accountDetailsFactory) {
        AccountType defaultAccountType = createAccountHolderCommand.getDefaultAccountType();
        CurrencyType currencyTypeForAccount = accountDetailsFactory.getCurrencyTypeForAccount(defaultAccountType);
        return new Account(UUID.randomUUID().toString(),
                createAccountHolderCommand.getId(),
                "Primary Account",
                defaultAccountType,
                accountDetailsFactory.createBankAccountDetails(),
                currencyTypeForAccount,
                getInitialAvailableFound(currencyTypeForAccount));
    }

    public static MonetaryAmount getInitialAvailableFound(CurrencyType currencyType) {
        switch (currencyType) {
            case USD:
                return MonetaryAmount.ZERO_USD;
            case EUR:
                return MonetaryAmount.ZERO_EUR;
            case GBP:
                return MonetaryAmount.ZERO_GBP;
            default:
                throw new IllegalArgumentException("Unsupported currency");
        }
    }
}

package revolut.transfer.domain.models.accounts;

import lombok.*;
import lombok.experimental.NonFinal;
import revolut.transfer.domain.models.MonetaryAmount;
import revolut.transfer.domain.models.currency.CurrencyType;

@Value
@NonFinal
public class Account {

    private final String id;
    private final String accountHolderId;
    private final AccountType accountType;
    private final BankAccountDetails bankAccountDetails;

    private final CurrencyType currencyType;
    private final MonetaryAmount balance;
    private final MonetaryAmount availableBalance;

    public Account depositAmount(MonetaryAmount amount) {
        return new Account(
                id,
                accountHolderId,
                accountType,
                bankAccountDetails,
                currencyType,
                balance,
                balance.add(amount));
    }

//    public Account withdrawAmount(MonetaryAmount amount) {
//        return new Account(
//                id,
//                accountHolderId,
//                accountType,
//                bankAccountDetails,
//                currencyType,
//                balance.subtract(amount));
//    }
}

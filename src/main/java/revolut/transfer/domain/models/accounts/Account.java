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

}

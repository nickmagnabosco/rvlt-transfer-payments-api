package revolut.transfer.domain.models.accounts;

import lombok.Data;
import revolut.transfer.domain.models.MonetaryAmount;
import revolut.transfer.domain.models.currency.CurrencyType;

@Data
public class Account {

    private final String id;
    private final String accountHolderId;
    private final AccountType accountType;
    private final AccountDetails accountDetails;

    private final CurrencyType currencyType;
    private final MonetaryAmount availableFunds;

}

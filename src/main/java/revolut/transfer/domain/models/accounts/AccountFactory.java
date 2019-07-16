package revolut.transfer.domain.models.accounts;

import revolut.transfer.domain.models.currency.CurrencyType;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class AccountFactory {

    @Inject
    public AccountFactory() {

    }

    public Account createAccount(String id, String accountHolderId, AccountType accountType, BankAccountDetails bankAccountDetails) {
        CurrencyType accountCurrency = accountType.getAccountCurrency();
        return new Account(
                id,
                accountHolderId,
                accountType,
                bankAccountDetails,
                accountCurrency,
                accountType.getInitialAmount(),
                accountType.getInitialAmount());
    }

}

package revolut.transfer.domain.models.accounts;

import revolut.transfer.domain.models.currency.CurrencyType;
import revolut.transfer.domain.service.BankAccountService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class AccountFactory {

    private static final String PRIMARY_ACCOUNT = "Primary Account";

    private final BankAccountService bankDetailsService;

    @Inject
    public AccountFactory(BankAccountService bankDetailsService) {
        this.bankDetailsService = bankDetailsService;
    }

    public Account createAccount(String accountHolderId, AccountType accountType) {
        CurrencyType accountCurrency = accountType.getAccountCurrency();
        return new Account(
                UUID.randomUUID().toString(),
                accountHolderId,
                PRIMARY_ACCOUNT,
                accountType,
                bankDetailsService.createBankAccountDetails(accountType),
                accountCurrency,
                accountType.getInitialAmount());
    }

}

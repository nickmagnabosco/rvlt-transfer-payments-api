package revolut.transfer.domain.models.accounts;

import revolut.transfer.domain.models.currency.CurrencyType;
import revolut.transfer.domain.repositories.TransactionRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AccountFactory {
    private final TransactionRepository transactionRepository;

    @Inject
    public AccountFactory(TransactionRepository transactionRepository) {

        this.transactionRepository = transactionRepository;
    }

    public Account createAccount(String id, String accountHolderId, AccountType accountType, BankAccountDetails bankAccountDetails) {
        CurrencyType accountCurrency = accountType.getAccountCurrency();
        return new Account(
                id,
                accountHolderId,
                accountType,
                bankAccountDetails,
                accountCurrency,
                transactionRepository);
    }

}

package revolut.transfer.integration.repositories;

import revolut.transfer.domain.models.accounts.Account;
import revolut.transfer.domain.repositories.AccountRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class StubAccountRepositoryImpl implements AccountRepository {
    @Inject
    public StubAccountRepositoryImpl() {
    }

    @Override
    public String createAccount(Account account) {
        return null;
    }

    @Override
    public Account getAccountById(String accountId) {
        return null;
    }

    @Override
    public List<Account> getAllAccountsByHolderId(String accountHolderId) {
        return null;
    }

    @Override
    public List<Account> getAllAccounts() {
        return null;
    }
}

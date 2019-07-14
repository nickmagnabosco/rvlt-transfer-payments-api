package revolut.transfer.integration.repositories;

import com.google.common.collect.Lists;
import revolut.transfer.domain.models.accounts.Account;
import revolut.transfer.domain.repositories.AccountRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class StubAccountRepositoryImpl implements AccountRepository {
    public static List<Account> accounts = Lists.newArrayList();

    @Inject
    public StubAccountRepositoryImpl() {
    }

    @Override
    public Account createAccount(Account account) {
        accounts.add(account);
        return account;
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

package revolut.transfer.integration.repositories;

import org.jdbi.v3.core.Handle;
import revolut.transfer.domain.exceptions.ResourceNotFoundException;
import revolut.transfer.domain.models.accounts.Account;
import revolut.transfer.domain.repositories.AccountRepository;
import revolut.transfer.integration.mappers.AccountMapper;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class StubAccountRepositoryImpl implements AccountRepository {
    private final JDBIProvider jdbiProvider;
    private final AccountMapper accountMapper;

    @Inject
    public StubAccountRepositoryImpl(JDBIProvider jdbiProvider, AccountMapper accountMapper) {
        this.jdbiProvider = jdbiProvider;
        this.accountMapper = accountMapper;
    }

    @Override
    public String createAccount(Handle handle, Account account) {
        handle.createUpdate("INSERT INTO ACCOUNT (id, account_holder_id, account_type, currency_type) "
                + "VALUES (:id, :accountHolderId, :accountType, :currencyType)")
                .bind("id", account.getId())
                .bind("accountHolderId", account.getAccountHolderId())
                .bind("accountType", account.getAccountType())
                .bind("currencyType", account.getCurrencyType())
                .execute();
        return account.getId();
    }

    @Override
    public void updateAccount(Handle handle, Account updatedAccount) {

    }

    @Override
    public List<Account> getAllAccountsByHolderId(String accountHolderId) {
        return jdbiProvider.getJdbi().withHandle(handle -> handle.createQuery(
                "SELECT id, account_holder_id, account_type, currency_type "
                        + "FROM ACCOUNT "
                        + "WHERE account_holder_id=:accountHolderId")
                .bind("accountHolderId", accountHolderId)
                .map(accountMapper)
                .list());
    }

    @Override
    public Optional<Account> getAccountByHolderIdAndAccountId(String accountHolderId, String accountId) {
        return jdbiProvider.getJdbi().withHandle(handle -> handle.createQuery(
                "SELECT id, account_holder_id, account_type, currency_type "
                        + "FROM ACCOUNT "
                        + "WHERE id=:accountId AND account_holder_id=:accountHolderId")
                .bind("accountId", accountId)
                .bind("accountHolderId", accountHolderId)
                .map(accountMapper)
                .findFirst());
    }

}

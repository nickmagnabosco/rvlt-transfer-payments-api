package revolut.transfer.integration.repositories;

import org.jdbi.v3.core.Handle;
import revolut.transfer.domain.commands.CreateAccountHolderCommand;
import revolut.transfer.domain.models.accounts.AccountHolder;
import revolut.transfer.domain.repositories.AccountHolderRepository;
import revolut.transfer.integration.mappers.AccountHolderMapper;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class StubAccountHolderRepositoryImpl implements AccountHolderRepository {
    private final JDBIProvider jdbiProvider;
    private final AccountHolderMapper accountHolderMapper;

    @Inject
    public StubAccountHolderRepositoryImpl(JDBIProvider jdbiProvider, AccountHolderMapper accountHolderMapper) {
        this.jdbiProvider = jdbiProvider;
        this.accountHolderMapper = accountHolderMapper;
    }

    @Override
    public Optional<AccountHolder> getAccountHolderById(Handle handle, String accountHolderId) {
        return handle.createQuery(
                "SELECT id, title, first_name, last_name, email_address "
                        + "FROM ACCOUNT_HOLDER "
                        + "WHERE id=:accountHolderId")
                .bind("accountHolderId", accountHolderId)
                .map(accountHolderMapper)
                .findFirst();
    }

    @Override
    public Optional<AccountHolder> getAccountHolderById(String accountHolderId) {
        return jdbiProvider.getJdbi().withHandle(handle -> getAccountHolderById(handle, accountHolderId));
    }

    @Override
    public String createAccountHolder(Handle handle, CreateAccountHolderCommand accountHolder) {
        handle.createUpdate("INSERT INTO ACCOUNT_HOLDER (id, title, first_name, last_name, email_address)"
                + "VALUES (:id, :title, :firstName, :lastName, :emailAddress)")
                .bind("id", accountHolder.getId())
                .bind("title", accountHolder.getTitle())
                .bind("firstName", accountHolder.getFirstName())
                .bind("lastName", accountHolder.getLastName())
                .bind("emailAddress", accountHolder.getEmailAddress())
                .execute();
        return accountHolder.getId();
    }

}

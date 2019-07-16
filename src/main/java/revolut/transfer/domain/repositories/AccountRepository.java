package revolut.transfer.domain.repositories;

import org.jdbi.v3.core.Handle;
import revolut.transfer.domain.models.accounts.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    String createAccount(Handle handle, Account account);
    List<Account> getAllAccountsByHolderId(String accountHolderId);
    List<Account> getAllAccountsByHolderId(Handle handle, String accountHolderId);
    Optional<Account> getAccountByAccountId(String accountId);
    Optional<Account> getAccountByAccountId(Handle handle, String accountId);
    Optional<Account> getAccountByHolderIdAndAccountId(String accountHolderId, String accountId);
    Optional<Account> getAccountByHolderIdAndAccountId(Handle handle, String accountHolderId, String accountId);

}

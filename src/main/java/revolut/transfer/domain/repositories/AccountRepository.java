package revolut.transfer.domain.repositories;

import org.jdbi.v3.core.Handle;
import revolut.transfer.domain.models.accounts.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    String createAccount(Handle handle, Account account);
    void updateAccount(Handle handle, Account updatedAccount);
    List<Account> getAllAccountsByHolderId(String accountHolderId);
    Optional<Account> getAccountByHolderIdAndAccountId(String accountHolderId, String accountId);

}

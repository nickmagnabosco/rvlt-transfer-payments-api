package revolut.transfer.domain.repositories;

import revolut.transfer.domain.models.accounts.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    String createAccount(Account account);
    void updateAccount(Account updatedAccount);
    List<Account> getAllAccountsByHolderId(String accountHolderId);
    Optional<Account> getAccountByHolderIdAndAccountId(String accountHolderId, String accountId);

}

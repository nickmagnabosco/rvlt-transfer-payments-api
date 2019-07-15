package revolut.transfer.domain.repositories;

import revolut.transfer.domain.models.accounts.Account;

import java.util.List;

public interface AccountRepository {

    Account createAccount(Account account);
    Account getAccountById(String accountId);
    List<Account> getAllAccountsByHolderId(String accountHolderId);
    Account getAllAccountsByHolderIdAndAccountId(String accountHolderId, String accountId);
    List<Account> getAllAccounts();

}

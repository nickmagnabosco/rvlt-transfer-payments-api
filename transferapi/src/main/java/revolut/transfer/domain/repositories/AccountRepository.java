package revolut.transfer.domain.repositories;

import revolut.transfer.domain.accounts.Account;

import java.util.List;

public interface AccountRepository {

    String createAccount(Account account);
    Account getAccountById(String accountId);
    List<Account> getAllAccounts();

}

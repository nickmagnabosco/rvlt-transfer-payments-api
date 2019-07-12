package revolut.transfer.domain.repositories;

import revolut.transfer.domain.accounts.AccountHolder;

import java.util.List;

public interface AccountHolderRepository {

    String createAccountHolder(AccountHolder accountHolder);
    AccountHolder getAccountHolderById(String accountHolderId);
    List<AccountHolder> getAllAccountHolders();

}

package revolut.transfer.domain.repositories;

import revolut.transfer.domain.commands.CreateAccountHolderCommand;
import revolut.transfer.domain.models.accounts.AccountHolder;

import java.util.List;

public interface AccountHolderRepository {

    String createAccountHolder(CreateAccountHolderCommand accountHolder);
    AccountHolder getAccountHolderById(String accountHolderId);
    List<AccountHolder> getAllAccountHolders();

}

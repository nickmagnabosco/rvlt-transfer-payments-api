package revolut.transfer.domain.repositories;

import revolut.transfer.domain.commands.CreateAccountHolderCommand;
import revolut.transfer.domain.models.accounts.AccountHolder;

import java.util.Optional;

public interface AccountHolderRepository {

    String createAccountHolder(CreateAccountHolderCommand accountHolder);
    Optional<AccountHolder> getAccountHolderById(String accountHolderId);

}

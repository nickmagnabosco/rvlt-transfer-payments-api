package revolut.transfer.domain.repositories;

import org.jdbi.v3.core.Handle;
import revolut.transfer.domain.commands.CreateAccountHolderCommand;
import revolut.transfer.domain.models.accounts.AccountHolder;

import java.util.Optional;

public interface AccountHolderRepository {

    String createAccountHolder(Handle handle, CreateAccountHolderCommand accountHolder);
    Optional<AccountHolder> getAccountHolderById(String accountHolderId);

}

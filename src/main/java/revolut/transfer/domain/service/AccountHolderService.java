package revolut.transfer.domain.service;

import revolut.transfer.domain.commands.CreateAccountHolderCommand;
import revolut.transfer.domain.models.accounts.AccountHolder;
import revolut.transfer.domain.repositories.AccountHolderRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class AccountHolderService {
    private final AccountHolderRepository accountHolderRepository;

    @Inject
    public AccountHolderService(AccountHolderRepository accountHolderRepository) {
        this.accountHolderRepository = accountHolderRepository;
    }

    public AccountHolder getAccountHolderById(String accountHolderId) {
        return accountHolderRepository.getAccountHolderById(accountHolderId);
    }

    public List<AccountHolder> getAllAccountHolders() {
        return accountHolderRepository.getAllAccountHolders();
    }

    public String createAccountHolder(CreateAccountHolderCommand createAccountHolderCommand) {
        return createAccountHolderCommand.execute();
    }
}

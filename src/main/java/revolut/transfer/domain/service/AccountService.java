package revolut.transfer.domain.service;

import revolut.transfer.domain.commands.CreateAccountCommand;
import revolut.transfer.domain.commands.CreateAccountHolderCommand;
import revolut.transfer.domain.models.accounts.Account;
import revolut.transfer.domain.models.accounts.AccountHolder;
import revolut.transfer.domain.repositories.AccountHolderRepository;
import revolut.transfer.domain.repositories.AccountRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class AccountService {

    private final AccountHolderRepository accountHolderRepository;
    private final AccountRepository accountRepository;

    @Inject
    public AccountService(AccountHolderRepository accountHolderRepository, AccountRepository accountRepository) {
        this.accountHolderRepository = accountHolderRepository;
        this.accountRepository = accountRepository;
    }

    public AccountHolder getAccountHolderById(String accountHolderId) {
        return accountHolderRepository.getAccountHolderById(accountHolderId);
    }

    public AccountHolder createAccountHolder(CreateAccountHolderCommand createAccountHolderCommand) {
        return createAccountHolderCommand.execute();
    }

    public List<Account> getAccountsByHolderId(String accountHolderId) {
        return accountRepository.getAllAccountsByHolderId(accountHolderId);
    }

    public Account getAccountByHolderIdAndAccountId(String accountHolderId, String accountId) {
        return accountRepository.getAllAccountsByHolderIdAndAccountId(accountHolderId, accountId);
    }

    public Account createAccount(CreateAccountCommand createAccountCommand) {
        return createAccountCommand.execute();
    }
}

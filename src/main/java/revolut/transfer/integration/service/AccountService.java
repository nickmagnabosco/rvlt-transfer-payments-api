package revolut.transfer.integration.service;

import revolut.transfer.domain.commands.CreateAccountHolderCommand;
import revolut.transfer.integration.dto.Account;
import revolut.transfer.integration.dto.AccountHolderDetails;
import revolut.transfer.integration.dto.command.CreateAccountCommand;
import revolut.transfer.integration.dto.command.CreateAccountHolder;
import revolut.transfer.integration.transformers.AccountTransformer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class AccountService {

    private final revolut.transfer.domain.service.AccountService domainService;
    private final AccountTransformer accountTransformer;

    @Inject
    public AccountService(revolut.transfer.domain.service.AccountService domainService, AccountTransformer accountTransformer) {
        this.domainService = domainService;
        this.accountTransformer = accountTransformer;
    }

    public AccountHolderDetails getAccountHolderDetailsByHolderId(String accountHolderId) {
        return accountTransformer.transformAccountHolderDetails(domainService.getAccountHolderById(accountHolderId));
    }

    public AccountHolderDetails createAccountHolder(CreateAccountHolder createAccountHolder) {
        CreateAccountHolderCommand createAccountHolderCommand = accountTransformer.transform(createAccountHolder);
        return accountTransformer.transformAccountHolderDetails(domainService.createAccountHolder(createAccountHolderCommand));
    }

    public List<Account> getAccountsByHolderId(String accountHolderId) {
        return domainService.getAccountsByHolderId(accountHolderId).stream()
                .map(accountTransformer::transformAccount)
                .collect(Collectors.toList());
    }

    public Account getAccountByAccountHolderAndAccountId(String accountHolderId, String accountId) {
        return accountTransformer.transformAccount(domainService.getAccountByHolderIdAndAccountId(accountHolderId, accountId));
    }

    public Account createAccount(String accountHolderId, CreateAccountCommand createAccountCommand) {
        revolut.transfer.domain.commands.CreateAccountCommand createAccount = accountTransformer.transformCreateAccount(accountHolderId, createAccountCommand);
        return accountTransformer.transformAccount(domainService.createAccount(createAccount));
    }
}

package revolut.transfer.integration.transformers;

import revolut.transfer.domain.commands.CreateAccountCommand;
import revolut.transfer.domain.commands.CreateAccountHolderCommand;
import revolut.transfer.domain.models.accounts.*;
import revolut.transfer.domain.repositories.AccountHolderRepository;
import revolut.transfer.domain.repositories.AccountRepository;
import revolut.transfer.integration.dto.AccountHolderDetails;
import revolut.transfer.integration.dto.BankAccountDetails;
import revolut.transfer.integration.dto.command.CreateAccountHolder;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class AccountTransformer {

    private final AccountHolderRepository accountHolderRepository;
    private final AccountRepository accountRepository;
    private final AccountFactory accountFactory;

    @Inject
    public AccountTransformer(AccountHolderRepository accountHolderRepository, AccountRepository accountRepository,
            AccountFactory accountFactory) {
        this.accountHolderRepository = accountHolderRepository;
        this.accountRepository = accountRepository;
        this.accountFactory = accountFactory;
    }

    public CreateAccountHolderCommand transform(CreateAccountHolder createAccountHolder) {
        return new CreateAccountHolderCommand(
                UUID.randomUUID().toString(),
                UserTitle.fromString(createAccountHolder.getTitle()),
                createAccountHolder.getFirstName(),
                createAccountHolder.getLastName(),
                createAccountHolder.getEmailAddress(),
                AccountType.fromString(createAccountHolder.getDefaultAccountType()),
                accountHolderRepository,
                accountRepository,
                accountFactory
        );
    }

    public AccountHolderDetails transformAccountHolderDetails(AccountHolder accountHolder) {
        return new AccountHolderDetails(accountHolder.getId(),
                accountHolder.getTitle().name(),
                accountHolder.getFirstName(),
                accountHolder.getLastName(),
                accountHolder.getEmailAddress(),
                accountHolder.getAccounts().stream().map(this::transformAccount).collect(Collectors.toList()));
    }

    public revolut.transfer.integration.dto.Account transformAccount(revolut.transfer.domain.models.accounts.Account account) {
        return new revolut.transfer.integration.dto.Account(account.getId(),
                account.getAccountHolderId(),
                account.getAccountType().name(),
                account.getCurrencyType().name(),
                account.getBalance(),
                transformBankAccountDetails(account.getBankAccountDetails()));
    }

    public BankAccountDetails transformBankAccountDetails(revolut.transfer.domain.models.accounts.BankAccountDetails domain) {
        return new BankAccountDetails(domain.getIban(), domain.getSortCode(), domain.getAccountNumber());
    }

    public CreateAccountCommand transformCreateAccount(String accountHolderId, revolut.transfer.integration.dto.command.CreateAccountCommand dto) {
        return new CreateAccountCommand(
                accountHolderId,
                AccountType.fromString(dto.getAccountType()),
                accountRepository,
                accountFactory);
    }
}

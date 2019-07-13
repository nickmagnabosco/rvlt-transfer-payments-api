package revolut.transfer.integration.transformers;

import revolut.transfer.domain.commands.CreateAccountHolderCommand;
import revolut.transfer.domain.models.accounts.*;
import revolut.transfer.domain.repositories.AccountHolderRepository;
import revolut.transfer.domain.repositories.AccountRepository;
import revolut.transfer.integration.dto.AccountDetails;
import revolut.transfer.integration.dto.AccountHolderDetails;
import revolut.transfer.integration.dto.BankAccountDetails;
import revolut.transfer.integration.dto.command.CreateAccountHolder;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class AccountHolderTransformer {

    private final AccountHolderRepository accountHolderRepository;
    private final AccountRepository accountRepository;
    private final AccountDetailsFactory accountDetailsFactory;

    @Inject
    public AccountHolderTransformer(AccountHolderRepository accountHolderRepository, AccountRepository accountRepository,
            AccountDetailsFactory accountDetailsFactory) {
        this.accountHolderRepository = accountHolderRepository;
        this.accountRepository = accountRepository;
        this.accountDetailsFactory = accountDetailsFactory;
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
                accountDetailsFactory
        );
    }

    public AccountHolderDetails transformAccountHolderDetails(AccountHolder accountHolder) {
        return new AccountHolderDetails(accountHolder.getId(),
                accountHolder.getTitle().name(),
                accountHolder.getFirstName(),
                accountHolder.getLastName(),
                accountHolder.getEmailAddress(),
                accountHolder.getAccounts().stream().map(this::transformAccountDetails).collect(Collectors.toList()));
    }

    public AccountDetails transformAccountDetails(Account account) {
        return new AccountDetails(account.getId(),
                account.getAccountHolderId(),
                account.getAccountType().name(),
                transformBankAccountDetails(account.getBankAccountDetails()),
                account.getCurrencyType().name(),
                account.getBalance());
    }

    public BankAccountDetails transformBankAccountDetails(revolut.transfer.domain.models.accounts.BankAccountDetails domain) {
        return new BankAccountDetails(domain.getIban(), domain.getSortCode(), domain.getAccountNumber());
    }
}

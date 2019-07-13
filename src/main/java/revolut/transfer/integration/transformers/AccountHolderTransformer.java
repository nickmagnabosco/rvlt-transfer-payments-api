package revolut.transfer.integration.transformers;

import revolut.transfer.domain.commands.CreateAccountHolderCommand;
import revolut.transfer.domain.models.accounts.AccountType;
import revolut.transfer.domain.models.accounts.UserTitle;
import revolut.transfer.domain.repositories.AccountHolderRepository;
import revolut.transfer.integration.dto.command.CreateAccountHolder;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class AccountHolderTransformer {

    private final AccountHolderRepository accountHolderRepository;

    @Inject
    public AccountHolderTransformer(AccountHolderRepository accountHolderRepository) {
        this.accountHolderRepository = accountHolderRepository;
    }

    public CreateAccountHolderCommand transform(CreateAccountHolder createAccountHolder) {
        return new CreateAccountHolderCommand(
                UUID.randomUUID().toString(),
                UserTitle.valueOf(createAccountHolder.getTitle()),
                createAccountHolder.getFirstName(),
                createAccountHolder.getLastName(),
                createAccountHolder.getEmailAddress(),
                AccountType.valueOf(createAccountHolder.getDefaultAccountType()),
                accountHolderRepository
        );
    }
}

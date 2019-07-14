package revolut.transfer.domain.commands;

import lombok.Value;
import lombok.experimental.NonFinal;
import revolut.transfer.domain.exceptions.ValidationException;
import revolut.transfer.domain.exceptions.ValidationFailure;
import revolut.transfer.domain.models.accounts.Account;
import revolut.transfer.domain.models.accounts.AccountFactory;
import revolut.transfer.domain.models.accounts.AccountType;
import revolut.transfer.domain.repositories.AccountRepository;

@Value
@NonFinal
public class CreateAccountCommand {

    private final String accountHolderId;
    private final AccountType accountType;
    private final AccountRepository accountRepository;
    private final AccountFactory accountFactory;

    public Account execute() {
        validate();
        Account account = accountFactory.createAccount(accountHolderId, accountType);
        accountRepository.createAccount(account);
        return account;
    }

    public void validate() {
        Account account = accountRepository.getAllAccountsByHolderId(accountHolderId)
                .stream()
                .filter(a -> a.getAccountType().equals(accountType))
                .findFirst()
                .orElse(null);

        if (account != null) {
            throw new ValidationException(new ValidationFailure(String.format("Cannot add multiple account with type %s", accountType)));
        }
    }
}

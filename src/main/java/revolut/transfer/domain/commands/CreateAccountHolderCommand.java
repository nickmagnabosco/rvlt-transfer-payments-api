package revolut.transfer.domain.commands;

import com.google.common.collect.Lists;
import lombok.Value;
import lombok.experimental.NonFinal;
import revolut.transfer.domain.exceptions.ValidationException;
import revolut.transfer.domain.exceptions.ValidationFailure;
import revolut.transfer.domain.models.accounts.*;
import revolut.transfer.domain.repositories.AccountHolderRepository;
import revolut.transfer.domain.repositories.AccountRepository;
import spark.utils.StringUtils;

import java.util.List;

@Value
@NonFinal
public class CreateAccountHolderCommand {
    private final String id;
    private final UserTitle title;
    private final String firstName;
    private final String lastName;
    private final String emailAddress;
    private final AccountHolderRepository accountHolderRepository;
    private final AccountRepository accountRepository;

    public AccountHolder execute() {
        validate();
        accountHolderRepository.createAccountHolder(this);
        return this.toAccountHolder();
    }

    public void validate() {
        List<ValidationFailure> failures = Lists.newArrayList();

        if (StringUtils.isBlank(firstName)) {
            failures.add(new ValidationFailure("First name must be provided"));
        }

        if (StringUtils.isBlank(lastName)) {
            failures.add(new ValidationFailure("First name must be provided"));
        }

        if (StringUtils.isBlank(emailAddress)) {
            // TODO add more validation on the email address
            failures.add(new ValidationFailure("First name must be provided"));
        }

        if(!failures.isEmpty()) {
            throw new ValidationException(failures);
        }
    }

    public AccountHolder toAccountHolder() {
        return toAccountHolder(accountRepository.getAllAccountsByHolderId(id));
    }

    public AccountHolder toAccountHolder(List<Account> accounts) {
        return new AccountHolder(id,
                title,
                firstName,
                lastName,
                emailAddress,
                accounts);
    }
}

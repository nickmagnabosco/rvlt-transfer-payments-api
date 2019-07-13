package revolut.transfer.domain.commands;

import com.google.common.collect.Lists;
import lombok.Value;
import revolut.transfer.domain.exceptions.ValidationException;
import revolut.transfer.domain.exceptions.ValidationFailure;
import revolut.transfer.domain.models.accounts.AccountType;
import revolut.transfer.domain.models.accounts.UserTitle;
import revolut.transfer.domain.repositories.AccountHolderRepository;
import spark.utils.StringUtils;

import java.util.List;

@Value
public class CreateAccountHolderCommand {
    private final String id;
    private final UserTitle title;
    private final String firstName;
    private final String lastName;
    private final String emailAddress;
    private final AccountType defaultAccountType;
    private final AccountHolderRepository accountHolderRepository;

    public String execute() {
        validate();
        accountHolderRepository.createAccountHolder(this);
        return id;
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
            // TODO more validaiton on the email address
            failures.add(new ValidationFailure("First name must be provided"));
        }

        if(!failures.isEmpty()) {
            throw new ValidationException(failures);
        }

    }
}

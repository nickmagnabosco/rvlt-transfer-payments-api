package revolut.transfer.domain.models.accounts;

import lombok.Value;
import lombok.experimental.NonFinal;

import java.util.List;

@Value
@NonFinal
public class AccountHolder {

    private final String id;
    private final UserTitle title;
    private final String firstName;
    private final String lastName;
    private final String emailAddress;
    private final List<Account> accounts;

    public AccountHolder withAccounts(List<Account> accountList) {
        return new AccountHolder(id, title, firstName, lastName, emailAddress, accountList);
    }
}

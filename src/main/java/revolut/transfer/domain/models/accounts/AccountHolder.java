package revolut.transfer.domain.models.accounts;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.util.List;

@Value
@NonFinal
@AllArgsConstructor
public class AccountHolder {

    private final String id;
    private final UserTitle title;
    private final String firstName;
    private final String lastName;
    private final String emailAddress;
    private final List<Account> accounts;

    public AccountHolder(String id, UserTitle title, String firstName, String lastName, String emailAddress) {
        this.id = id;
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.accounts = Lists.newArrayList();
    }

    public AccountHolder withAccounts(List<Account> accountList) {
        return new AccountHolder(id, title, firstName, lastName, emailAddress, accountList);
    }
}

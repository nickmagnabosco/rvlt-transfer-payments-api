package revolut.transfer.domain.models.accounts;

import lombok.Value;

import java.util.List;

@Value
public class AccountHolder {

    private final String id;
    private final UserTitle title;
    private final String firstName;
    private final String lastName;
    private final String emailAddress;
    private final List<Account> accounts;

}

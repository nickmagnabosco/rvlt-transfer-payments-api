package revolut.transfer.domain.models.accounts;

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

}

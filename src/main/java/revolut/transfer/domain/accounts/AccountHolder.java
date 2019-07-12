package revolut.transfer.domain.accounts;

import lombok.Data;
import revolut.transfer.domain.currency.CurrencyType;

import java.util.List;
import java.util.Set;

@Data
public class AccountHolder {

    private final String id;
    private final UserTitle title;
    private final String firstName;
    private final String lastName;
    private final String emailAddress;
}

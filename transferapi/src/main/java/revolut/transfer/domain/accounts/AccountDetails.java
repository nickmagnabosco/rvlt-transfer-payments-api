package revolut.transfer.domain.accounts;

import lombok.Data;

@Data
public class AccountDetails {

    private final String sortCode;
    private final String accountNumber;

}

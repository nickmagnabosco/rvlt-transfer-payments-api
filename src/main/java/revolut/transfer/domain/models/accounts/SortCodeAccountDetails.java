package revolut.transfer.domain.models.accounts;

import lombok.Value;

@Value
public class SortCodeAccountDetails extends AccountDetails {

    private String sortCode;
    private String accountNumber;

}

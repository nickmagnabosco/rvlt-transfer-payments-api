package revolut.transfer.domain.models.accounts;

import lombok.Value;

@Value
public class USDAccountDetails extends AccountDetails {

    private String routingNumber;
    private String accountNumber;
    private String accountType;

}



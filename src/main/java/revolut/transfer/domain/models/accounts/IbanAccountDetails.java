package revolut.transfer.domain.models.accounts;

import lombok.Value;

@Value
public class IbanAccountDetails extends AccountDetails {

    private String iban;

}

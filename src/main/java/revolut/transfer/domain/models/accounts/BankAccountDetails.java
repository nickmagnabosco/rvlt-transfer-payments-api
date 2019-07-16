package revolut.transfer.domain.models.accounts;

import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonFinal
public class BankAccountDetails {

    private final String id;
    private final String accountId;
    private final String iban;
    private final String bic;
    private final String sortCode;
    private final String accountNumber;

}

package revolut.transfer.domain.models.accounts;

import lombok.Value;

@Value
public class BankAccountDetails {
    private final String iban;
    private final String sortCode;
    private final String accountNumber;
}

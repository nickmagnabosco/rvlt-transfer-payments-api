package revolut.transfer.integration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import revolut.transfer.domain.models.MonetaryAmount;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDetails {

    private String id;
    private String accountHolderId;
    private String accountType;
    private BankAccountDetails accountDetails;
    private String currencyType;
    private MonetaryAmount availableFunds;

}

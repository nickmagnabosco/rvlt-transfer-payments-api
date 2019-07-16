package revolut.transfer.integration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {

    private String id;
    private String accountHolderId;
    private String accountType;
    private String currencyType;
    private MonetaryAmount balance;
    private BankAccountDetails bankAccountDetails;

}

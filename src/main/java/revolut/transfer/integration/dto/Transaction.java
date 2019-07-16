package revolut.transfer.integration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {

    private String id;
    private String requestId;
    private String accountId;
    private String status;
    private String type;
    private MonetaryAmount amount;
    private String dateTime;

}

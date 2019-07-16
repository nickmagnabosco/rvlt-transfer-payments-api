package revolut.transfer.integration.dto.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import revolut.transfer.integration.dto.MonetaryAmount;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateTransfer {

    private String requestId;
    private String targetAccountId;
    private MonetaryAmount depositAmount;

}

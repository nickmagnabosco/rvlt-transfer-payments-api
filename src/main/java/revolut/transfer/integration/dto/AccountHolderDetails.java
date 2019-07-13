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
public class AccountHolderDetails {
    private String id;
    private String title;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private List<AccountDetails> accounts;
}

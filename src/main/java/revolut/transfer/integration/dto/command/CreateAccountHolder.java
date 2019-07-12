package revolut.transfer.integration.dto.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import revolut.transfer.domain.accounts.UserTitle;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateAccountHolder {

    private UserTitle title;
    private String firstName;
    private String lastName;
    private String emailAddress;

}

package revolut.transfer.integration.dto.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateAccountHolder {

    private String title;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String defaultAccountType;

}

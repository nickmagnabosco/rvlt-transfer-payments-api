package acceptance;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;
import revolut.transfer.integration.dto.Account;
import revolut.transfer.integration.dto.AccountHolderDetails;
import revolut.transfer.integration.dto.command.CreateAccountHolder;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.post;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class CreateAccountTest extends ComponentTest {
    @Test
    public void createAccountHolder() throws Exception {
        CreateAccountHolder createAccount = new CreateAccountHolder(
                "MR",
                "Jim",
                "Don",
                "myemail@address.com",
                "UK"
        );

        AccountHolderDetails response =
                given()
                    .body(createAccount)
                .when()
                    .post(getFullUrl( "/accountHolders"))
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .as(AccountHolderDetails.class);

        assertThat(response.getId()).isNotEmpty();
        assertThat(response.getEmailAddress()).isEqualTo("myemail@address.com");
        assertThat(response.getFirstName()).isEqualTo("Jim");
        assertThat(response.getLastName()).isEqualTo("Don");
        assertThat(response.getTitle()).isEqualTo("MR");
        assertThat(response.getAccounts()).hasSize(1);
        assertThat(response.getAccounts().get(0).getAccountType()).isEqualTo("UK");
    }
}

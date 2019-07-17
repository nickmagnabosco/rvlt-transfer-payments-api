package acceptance;


import org.junit.Test;
import revolut.transfer.domain.models.MonetaryAmount;
import revolut.transfer.integration.dto.Account;
import revolut.transfer.integration.dto.AccountHolderDetails;
import revolut.transfer.integration.dto.BankAccountDetails;
import revolut.transfer.integration.dto.command.CreateAccountHolder;
import revolut.transfer.integration.repositories.StubAccountHolderRepositoryImpl;

import static acceptance.TestUtility.givenAccountHolder;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class AccountHolderTest extends ComponentTest {

    @Test
    public void createAndRetrieveAccountHolder() throws Exception {
        CreateAccountHolder createAccount = new CreateAccountHolder(
                "MR",
                "Jim",
                "Don",
                "myemail@address.com"
        );

        AccountHolderDetails createdAccount =
                given()
                    .body(createAccount)
                .when()
                    .post(getFullUrl( "/accountHolders"))
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .as(AccountHolderDetails.class);

        assertThat(createdAccount.getId()).isNotEmpty();
        assertThat(createdAccount.getEmailAddress()).isEqualTo("myemail@address.com");
        assertThat(createdAccount.getFirstName()).isEqualTo("Jim");
        assertThat(createdAccount.getLastName()).isEqualTo("Don");
        assertThat(createdAccount.getTitle()).isEqualTo("MR");
        assertThat(createdAccount.getAccounts()).hasSize(0);

        AccountHolderDetails getAccountReponse = when()
                .get(getFullUrl("/accountHolders/" + createdAccount.getId()))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(AccountHolderDetails.class);

        assertThat(getAccountReponse.getId()).isNotEmpty();
        assertThat(getAccountReponse.getEmailAddress()).isEqualTo("myemail@address.com");
        assertThat(getAccountReponse.getFirstName()).isEqualTo("Jim");
        assertThat(getAccountReponse.getLastName()).isEqualTo("Don");
        assertThat(getAccountReponse.getTitle()).isEqualTo("MR");
        assertThat(getAccountReponse.getAccounts()).hasSize(0);
    }

    @Test
    public void getAccountHolderById() {
        AccountHolderDetails accountHolderDetails = givenAccountHolder(new CreateAccountHolder(
                "MR",
                "Clint",
                "Eastwood",
                "myemail@address.com"));

        AccountHolderDetails response = when()
                .get(getFullUrl("/accountHolders/" + accountHolderDetails.getId()))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(AccountHolderDetails.class);

        assertThat(response.getId()).isEqualTo(accountHolderDetails.getId());
        assertThat(response.getTitle()).isEqualTo("MR");
        assertThat(response.getFirstName()).isEqualTo("Clint");
        assertThat(response.getLastName()).isEqualTo("Eastwood");
        assertThat(response.getEmailAddress()).isEqualTo("myemail@address.com");
        assertThat(response.getAccounts()).isEqualTo(accountHolderDetails.getAccounts());
    }

    @Test
    public void getAccountHolderById_accountDoesNotExist_404() {
        givenAccountHolder(new CreateAccountHolder(
                "MR",
                "Clint",
                "Eastwood",
                "myemail@address.com"));

        when()
                .get(getFullUrl("/accountHolders/123"))
                .then()
                .statusCode(404);
    }

}

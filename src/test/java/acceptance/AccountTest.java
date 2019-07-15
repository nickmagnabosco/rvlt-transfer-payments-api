package acceptance;

import org.junit.Test;
import revolut.transfer.integration.dto.Account;
import revolut.transfer.integration.dto.AccountHolderDetails;
import revolut.transfer.integration.dto.MonetaryAmount;
import revolut.transfer.integration.dto.command.CreateAccountCommand;
import revolut.transfer.integration.dto.command.CreateAccountHolder;

import java.util.Arrays;
import java.util.List;

import static acceptance.TestUtility.givenAccountHolder;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class AccountTest extends ComponentTest {
    @Test
    public void createNewAccount() {
        AccountHolderDetails accountHolderDetails = givenAccountHolder(new CreateAccountHolder(
                "MR",
                "Clint",
                "Eastwood",
                "myemail@address.com",
                "UK"));

        CreateAccountCommand createAccountCommand = new CreateAccountCommand("IBAN");
        Account response = given()
                .body(createAccountCommand)
                .when()
                .post(getFullUrl("/accountHolders/" + accountHolderDetails.getId() + "/accounts"))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(Account.class);

        assertThat(response.getId()).isNotEmpty();
        assertThat(response.getAccountType()).isEqualTo("IBAN");
        assertThat(response.getAccountHolderId()).isEqualTo(accountHolderDetails.getId());
        assertThat(response.getCurrencyType()).isEqualTo("EUR");
        assertThat(response.getBalance()).isEqualTo(new MonetaryAmount(revolut.transfer.domain.models.MonetaryAmount.ZERO_EUR));
        assertThat(response.getBankAccountDetails()).isNotNull();
        assertThat(response.getBankAccountDetails().getBic()).isNotEmpty();
        assertThat(response.getBankAccountDetails().getSortCode()).isEmpty();
        assertThat(response.getBankAccountDetails().getAccountNumber()).isEmpty();
        assertThat(response.getBankAccountDetails().getIban()).isNotEmpty();

    }

    @Test
    public void createNewAccount_whenAccountTypeAlreadyExists_400() {
        AccountHolderDetails accountHolderDetails = givenAccountHolder(new CreateAccountHolder(
                "MR",
                "Clint",
                "Eastwood",
                "myemail@address.com",
                "UK"));

        CreateAccountCommand createAccountCommand = new CreateAccountCommand("UK");
        given()
                .body(createAccountCommand)
                .when()
                .post(getFullUrl("/accountHolders/" + accountHolderDetails.getId() + "/accounts"))
                .then()
                .statusCode(400);
    }

    @Test
    public void createNewAccount_invalidAccountType_400() {
        AccountHolderDetails accountHolderDetails = givenAccountHolder(new CreateAccountHolder(
                "MR",
                "Clint",
                "Eastwood",
                "myemail@address.com",
                "UK"));

        CreateAccountCommand createAccountCommand = new CreateAccountCommand("ETH");
        given()
                .body(createAccountCommand)
                .when()
                .post(getFullUrl("/accountHolders/" + accountHolderDetails.getId() + "/accounts"))
                .then()
                .statusCode(400);
    }


    @Test
    public void getAllAccounts_forAccountHolder() {
        AccountHolderDetails accountHolderDetails = givenAccountHolder(new CreateAccountHolder(
                "MR",
                "Clint",
                "Eastwood",
                "myemail@address.com",
                "UK"));

        Account firstAccount = accountHolderDetails.getAccounts().get(0);
        CreateAccountCommand createAccountCommand = new CreateAccountCommand("IBAN");
        Account secondAccount = given()
                .body(createAccountCommand)
                .when()
                .post(getFullUrl("/accountHolders/" + accountHolderDetails.getId() + "/accounts"))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(Account.class);

        Account[] accounts = when()
                .get(getFullUrl("/accountHolders/" + accountHolderDetails.getId() + "/accounts"))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(Account[].class);

        List<Account> accountList = Arrays.asList(accounts);
        assertThat(accountList).hasSize(2);
        assertThat(accountList).contains(firstAccount);
        assertThat(accountList).contains(secondAccount);
    }

    @Test
    public void getAccountById() {
        AccountHolderDetails accountHolderDetails = givenAccountHolder(new CreateAccountHolder(
                "MR",
                "Clint",
                "Eastwood",
                "myemail@address.com",
                "UK"));

        Account firstAccount = accountHolderDetails.getAccounts().get(0);

        Account account = when()
                .get(getFullUrl("/accountHolders/" + accountHolderDetails.getId() + "/accounts/" + firstAccount.getId()))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(Account.class);

        assertThat(account).isEqualTo(firstAccount);
    }

    @Test
    public void getAccountById_whenAccountDoesNotExist_404() {
        AccountHolderDetails accountHolderDetails = givenAccountHolder(new CreateAccountHolder(
                "MR",
                "Clint",
                "Eastwood",
                "myemail@address.com",
                "UK"));

        when()
                .get(getFullUrl("/accountHolders/" + accountHolderDetails.getId() + "/accounts/random123"))
                .then()
                .statusCode(404);
    }

}

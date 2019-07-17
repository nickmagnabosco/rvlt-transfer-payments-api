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
                "myemail@address.com"));

        CreateAccountCommand createAccountCommand = new CreateAccountCommand("EUR");
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
        assertThat(response.getAccountType()).isEqualTo("EUR");
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
                "myemail@address.com"));

        CreateAccountCommand createAccountCommand = new CreateAccountCommand("UK");
        given()
                .body(createAccountCommand)
                .when()
                .post(getFullUrl("/accountHolders/" + accountHolderDetails.getId() + "/accounts"))
                .then()
                .statusCode(200);

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
                "myemail@address.com"));

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
                "myemail@address.com"));

        CreateAccountCommand createAccountCommand = new CreateAccountCommand("UK");
        Account firstAccount = given()
                .body(createAccountCommand)
                .when()
                .post(getFullUrl("/accountHolders/" + accountHolderDetails.getId() + "/accounts"))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(Account.class);

        CreateAccountCommand createAccountCommand2 = new CreateAccountCommand("EUR");
        Account secondAccount = given()
                .body(createAccountCommand2)
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
    public void getAllAccounts_forAccountHolder_whenNoAccount_returnsEmptyList() {
        AccountHolderDetails accountHolderDetails = givenAccountHolder(new CreateAccountHolder(
                "MR",
                "Clint",
                "Eastwood",
                "myemail@address.com"));

        Account[] accounts = when()
                .get(getFullUrl("/accountHolders/" + accountHolderDetails.getId() + "/accounts"))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(Account[].class);

        List<Account> accountList = Arrays.asList(accounts);
        assertThat(accountList).hasSize(0);
    }


    @Test
    public void getAccountById() {
        AccountHolderDetails accountHolderDetails = givenAccountHolder(new CreateAccountHolder(
                "MR",
                "Clint",
                "Eastwood",
                "myemail@address.com"));

        CreateAccountCommand createAccountCommand = new CreateAccountCommand("UK");
        Account createdAccount = given()
                .body(createAccountCommand)
                .when()
                .post(getFullUrl("/accountHolders/" + accountHolderDetails.getId() + "/accounts"))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(Account.class);

        Account account = when()
                .get(getFullUrl("/accountHolders/" + accountHolderDetails.getId() + "/accounts/" + createdAccount.getId()))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(Account.class);

        assertThat(account).isEqualTo(createdAccount);
    }

    @Test
    public void getAccountById_whenAccountDoesNotExist_404() {
        AccountHolderDetails accountHolderDetails = givenAccountHolder(new CreateAccountHolder(
                "MR",
                "Clint",
                "Eastwood",
                "myemail@address.com"));

        when()
                .get(getFullUrl("/accountHolders/" + accountHolderDetails.getId() + "/accounts/random123"))
                .then()
                .statusCode(404);
    }

}

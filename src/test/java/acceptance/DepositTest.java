package acceptance;

import org.junit.Test;
import revolut.transfer.integration.dto.Account;
import revolut.transfer.integration.dto.AccountHolderDetails;
import revolut.transfer.integration.dto.MonetaryAmount;
import revolut.transfer.integration.dto.Transaction;
import revolut.transfer.integration.dto.command.CreateAccountCommand;
import revolut.transfer.integration.dto.command.CreateAccountHolder;
import revolut.transfer.integration.dto.command.CreateDeposit;

import java.math.BigDecimal;

import static acceptance.TestUtility.givenAccount;
import static acceptance.TestUtility.givenAccountHolder;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class DepositTest extends ComponentTest {
    @Test
    public void createDepositTransaction() {
        AccountHolderDetails accountHolderDetails = givenAccountHolder(new CreateAccountHolder(
                "MR",
                "Clint",
                "Eastwood",
                "myemail@address.com",
                "UK"));

        Account account = givenAccount(accountHolderDetails.getId(), new CreateAccountCommand("UK"));

        CreateDeposit createDeposit = new CreateDeposit("req123", account.getId(), new MonetaryAmount(BigDecimal.valueOf(10), "GBP"));
        Transaction transaction = given()
                .body(createDeposit)
                .when()
                .post(getFullUrl("/accountHolders/" + accountHolderDetails.getId() + "/accounts/" + account.getId() + "/deposits"))
                .then()
                .statusCode(200)
                .extract()
                .body().as(Transaction.class);

        assertThat(transaction.getId()).isNotEmpty();
        assertThat(transaction.getAccountId()).isEqualTo(account.getId());
        assertThat(transaction.getRequestId()).isEqualTo(createDeposit.getRequestId());
        assertThat(transaction.getDateTime()).isNotEmpty();
        assertThat(transaction.getAmount()).isEqualTo(new MonetaryAmount(BigDecimal.valueOf(10), "GBP"));
        assertThat(transaction.getType()).isEqualTo("DEPOSIT");
        assertThat(transaction.getStatus()).isEqualTo("IN_PROGRESS");

        Account getAccount = when()
                .get(getFullUrl("/accountHolders/" + accountHolderDetails.getId() + "/accounts/" + account.getId()))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(Account.class);

        assertThat(getAccount.getBalance()).isEqualTo(new MonetaryAmount(BigDecimal.valueOf(10), "GBP"));
    }

    @Test
    public void createMultipleDepositTransaction_sameRequestId_400() {
        AccountHolderDetails accountHolderDetails = givenAccountHolder(new CreateAccountHolder(
                "MR",
                "Clint",
                "Eastwood",
                "myemail@address.com",
                "UK"));

        Account account = givenAccount(accountHolderDetails.getId(), new CreateAccountCommand("UK"));

        CreateDeposit createDeposit = new CreateDeposit("req234", account.getId(), new MonetaryAmount(BigDecimal.valueOf(10), "GBP"));
        given()
                .body(createDeposit)
                .when()
                .post(getFullUrl("/accountHolders/" + accountHolderDetails.getId() + "/accounts/" + account.getId() + "/deposits"))
                .then()
                .statusCode(200)
                .extract()
                .body().as(Transaction.class);

        given()
                .body(createDeposit)
                .when()
                .post(getFullUrl("/accountHolders/" + accountHolderDetails.getId() + "/accounts/" + account.getId() + "/deposits"))
                .then()
                .statusCode(400);

        Account getAccount = when()
                .get(getFullUrl("/accountHolders/" + accountHolderDetails.getId() + "/accounts/" + account.getId()))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(Account.class);

        assertThat(getAccount.getBalance()).isEqualTo(new MonetaryAmount(BigDecimal.valueOf(10), "GBP"));
    }

    @Test
    public void createMultipleDepositTransaction_differentRequestId_200() {
        AccountHolderDetails accountHolderDetails = givenAccountHolder(new CreateAccountHolder(
                "MR",
                "Clint",
                "Eastwood",
                "myemail@address.com",
                "UK"));

        Account account = givenAccount(accountHolderDetails.getId(), new CreateAccountCommand("UK"));

        CreateDeposit createDeposit = new CreateDeposit("req345", account.getId(), new MonetaryAmount(BigDecimal.valueOf(10), "GBP"));
        given()
                .body(createDeposit)
                .when()
                .post(getFullUrl("/accountHolders/" + accountHolderDetails.getId() + "/accounts/" + account.getId() + "/deposits"))
                .then()
                .statusCode(200)
                .extract()
                .body().as(Transaction.class);

        CreateDeposit createDeposit2 = new CreateDeposit("req456", account.getId(), new MonetaryAmount(BigDecimal.valueOf(10), "GBP"));

        given()
                .body(createDeposit2)
                .when()
                .post(getFullUrl("/accountHolders/" + accountHolderDetails.getId() + "/accounts/" + account.getId() + "/deposits"))
                .then()
                .statusCode(200)
                .extract()
                .body().as(Transaction.class);

        Account getAccount = when()
                .get(getFullUrl("/accountHolders/" + accountHolderDetails.getId() + "/accounts/" + account.getId()))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(Account.class);

        assertThat(getAccount.getBalance()).isEqualTo(new MonetaryAmount(BigDecimal.valueOf(20), "GBP"));
    }
}

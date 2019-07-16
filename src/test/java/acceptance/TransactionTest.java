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
import static org.assertj.core.api.Java6Assertions.assertThat;

public class TransactionTest extends ComponentTest {
    @Test
    public void createTransaction() {
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
                .post(getFullUrl("/accountHolders/" + accountHolderDetails.getId() + "/accounts/" + account.getId() + "/deposit"))
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
    }
}

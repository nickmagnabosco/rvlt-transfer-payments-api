package acceptance;

import org.junit.Test;
import revolut.transfer.integration.dto.Account;
import revolut.transfer.integration.dto.AccountHolderDetails;
import revolut.transfer.integration.dto.MonetaryAmount;
import revolut.transfer.integration.dto.Transaction;
import revolut.transfer.integration.dto.command.CreateAccountCommand;
import revolut.transfer.integration.dto.command.CreateAccountHolder;
import revolut.transfer.integration.dto.command.CreateTransfer;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static acceptance.TestUtility.*;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class TransferTest extends ComponentTest {
    @Test
    public void transfer() {
        AccountHolderDetails receiver = givenAccountHolder(new CreateAccountHolder(
                "MR",
                "Clint",
                "Eastwood",
                "myemail@address.com",
                "UK"));

        AccountHolderDetails sender = givenAccountHolder(new CreateAccountHolder(
                "MR",
                "Clint",
                "Eastwood",
                "myemail@address.com",
                "UK"));
        Account receiverAccount = givenAccount(receiver.getId(), new CreateAccountCommand("UK"));

        Account senderAccount = givenAccountWithDeposit(sender.getId(),
                new CreateAccountCommand("UK"),
                "request123",
                new MonetaryAmount(BigDecimal.valueOf(100), "GBP"));

        CreateTransfer createDeposit = new CreateTransfer("transfer123", receiverAccount.getId(),
                new MonetaryAmount(BigDecimal.valueOf(10), "GBP"));

        Transaction transaction = given()
                .body(createDeposit)
                .when()
                .post(getFullUrl("/accountHolders/" + sender.getId() + "/accounts/" + senderAccount.getId() + "/transfers"))
                .then()
                .statusCode(200)
                .extract()
                .body().as(Transaction.class);

        assertThat(transaction.getId()).isNotEmpty();
        assertThat(transaction.getAccountId()).isEqualTo(senderAccount.getId());
        assertThat(transaction.getRequestId()).isEqualTo(createDeposit.getRequestId());
        assertThat(transaction.getDateTime()).isNotEmpty();
        assertThat(transaction.getAmount()).isEqualTo(new MonetaryAmount(BigDecimal.valueOf(10), "GBP"));
        assertThat(transaction.getType()).isEqualTo("OUTBOUND_PAYMENT");
        assertThat(transaction.getStatus()).isEqualTo("COMPLETED");

        Account updatedSenderAccount = when()
                .get(getFullUrl("/accountHolders/" + sender.getId() + "/accounts/" + senderAccount.getId()))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(Account.class);
        assertThat(updatedSenderAccount.getBalance()).isEqualTo(new MonetaryAmount(BigDecimal.valueOf(90), "GBP"));

        Account updatedReceiverAccount = when()
                .get(getFullUrl("/accountHolders/" + receiver.getId() + "/accounts/" + receiverAccount.getId()))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(Account.class);
        assertThat(updatedReceiverAccount.getBalance()).isEqualTo(new MonetaryAmount(BigDecimal.valueOf(10), "GBP"));
    }

    @Test
    public void transfer_differentCurrency() {
        AccountHolderDetails receiver = givenAccountHolder(new CreateAccountHolder(
                "MR",
                "Clint",
                "Eastwood",
                "myemail@address.com",
                "EUR"));

        AccountHolderDetails sender = givenAccountHolder(new CreateAccountHolder(
                "MR",
                "Clint",
                "Eastwood",
                "myemail@address.com",
                "UK"));
        Account receiverAccount = givenAccount(receiver.getId(), new CreateAccountCommand("EUR"));

        Account senderAccount = givenAccountWithDeposit(sender.getId(),
                new CreateAccountCommand("UK"),
                "request234",
                new MonetaryAmount(BigDecimal.valueOf(100), "GBP"));

        CreateTransfer createDeposit = new CreateTransfer("transfer234", receiverAccount.getId(),
                new MonetaryAmount(BigDecimal.valueOf(10), "GBP"));

        Transaction transaction = given()
                .body(createDeposit)
                .when()
                .post(getFullUrl("/accountHolders/" + sender.getId() + "/accounts/" + senderAccount.getId() + "/transfers"))
                .then()
                .statusCode(200)
                .extract()
                .body().as(Transaction.class);

        assertThat(transaction.getId()).isNotEmpty();
        assertThat(transaction.getAccountId()).isEqualTo(senderAccount.getId());
        assertThat(transaction.getRequestId()).isEqualTo(createDeposit.getRequestId());
        assertThat(transaction.getDateTime()).isNotEmpty();
        assertThat(transaction.getAmount()).isEqualTo(new MonetaryAmount(BigDecimal.valueOf(10), "GBP"));
        assertThat(transaction.getType()).isEqualTo("OUTBOUND_PAYMENT");
        assertThat(transaction.getStatus()).isEqualTo("COMPLETED");

        Account updatedSenderAccount = when()
                .get(getFullUrl("/accountHolders/" + sender.getId() + "/accounts/" + senderAccount.getId()))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(Account.class);
        assertThat(updatedSenderAccount.getBalance()).isEqualTo(new MonetaryAmount(BigDecimal.valueOf(90), "GBP"));

        Account updatedReceiverAccount = when()
                .get(getFullUrl("/accountHolders/" + receiver.getId() + "/accounts/" + receiverAccount.getId()))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(Account.class);
        assertThat(updatedReceiverAccount.getBalance().getCurrencyType()).isEqualTo("EUR");
    }

    @Test
    public void transfer_insufficientFunds_returnsFailedTransaction_doesNotAffectBalance() {
        AccountHolderDetails receiver = givenAccountHolder(new CreateAccountHolder(
                "MR",
                "Clint",
                "Eastwood",
                "myemail@address.com",
                "EUR"));

        AccountHolderDetails sender = givenAccountHolder(new CreateAccountHolder(
                "MR",
                "Clint",
                "Eastwood",
                "myemail@address.com",
                "UK"));
        Account receiverAccount = givenAccount(receiver.getId(), new CreateAccountCommand("EUR"));

        Account senderAccount = givenAccountWithDeposit(sender.getId(),
                new CreateAccountCommand("UK"),
                "deposit123",
                new MonetaryAmount(BigDecimal.valueOf(100), "GBP"));

        CreateTransfer createTransfer = new CreateTransfer("transfer456", receiverAccount.getId(),
                new MonetaryAmount(BigDecimal.valueOf(110), "GBP"));

        Transaction transferTransaction = given()
                .body(createTransfer)
                .when()
                .post(getFullUrl("/accountHolders/" + sender.getId() + "/accounts/" + senderAccount.getId() + "/transfers"))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(Transaction.class);

        assertThat(transferTransaction.getStatus()).isEqualTo("FAILED");

        Transaction[] transactions = when()
                .get(getFullUrl("/accountHolders/" + sender.getId() + "/accounts/" + senderAccount.getId() + "/transactions"))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(Transaction[].class);

        List<Transaction> transactionList = Arrays.asList(transactions);
        assertThat(transactionList).hasSize(2);
        assertThat(transactionList).contains(transferTransaction);

        Account updatedSenderAccount = when()
                .get(getFullUrl("/accountHolders/" + sender.getId() + "/accounts/" + senderAccount.getId()))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(Account.class);
        assertThat(updatedSenderAccount.getBalance()).isEqualTo(new MonetaryAmount(BigDecimal.valueOf(100), "GBP"));

        Account updatedReceiverAccount = when()
                .get(getFullUrl("/accountHolders/" + receiver.getId() + "/accounts/" + receiverAccount.getId()))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(Account.class);
        assertThat(updatedReceiverAccount.getBalance()).isEqualTo(new MonetaryAmount(BigDecimal.valueOf(0), "EUR"));
    }
}

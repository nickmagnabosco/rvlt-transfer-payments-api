package acceptance;

import revolut.transfer.integration.dto.Account;
import revolut.transfer.integration.dto.AccountHolderDetails;
import revolut.transfer.integration.dto.MonetaryAmount;
import revolut.transfer.integration.dto.Transaction;
import revolut.transfer.integration.dto.command.CreateAccountCommand;
import revolut.transfer.integration.dto.command.CreateAccountHolder;
import revolut.transfer.integration.dto.command.CreateDeposit;

import java.math.BigDecimal;

import static acceptance.ComponentTest.getFullUrl;
import static com.jayway.restassured.RestAssured.given;

public class TestUtility {

    public static AccountHolderDetails givenAccountHolder(CreateAccountHolder createAccountHolder) {
        return given()
                .body(createAccountHolder)
                .when()
                .post(getFullUrl( "/accountHolders"))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(AccountHolderDetails.class);
    }

    public static Account givenAccount(String accountHolderId, CreateAccountCommand createAccountCommand) {
        return given()
                .body(createAccountCommand)
                .when()
                .post(getFullUrl("/accountHolders/" + accountHolderId + "/accounts"))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(Account.class);
    }

    public static Account givenAccountWithDeposit(String accountHolderId,
            CreateAccountCommand createAccountCommand,
            String requestId,
            MonetaryAmount amount) {
        Account account = given()
                .body(createAccountCommand)
                .when()
                .post(getFullUrl("/accountHolders/" + accountHolderId + "/accounts"))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(Account.class);

        CreateDeposit createDeposit = new CreateDeposit(requestId, account.getId(), amount);

        given()
                .body(createDeposit)
                .when()
                .post(getFullUrl("/accountHolders/" + accountHolderId + "/accounts/" + account.getId() + "/deposits"))
                .then()
                .statusCode(200)
                .extract()
                .body().as(Transaction.class);

        return account;
    }
}

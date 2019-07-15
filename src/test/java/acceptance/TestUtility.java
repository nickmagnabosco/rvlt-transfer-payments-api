package acceptance;

import revolut.transfer.integration.dto.AccountHolderDetails;
import revolut.transfer.integration.dto.command.CreateAccountHolder;

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
}

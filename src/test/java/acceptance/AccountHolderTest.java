package acceptance;


import org.junit.Test;
import revolut.transfer.domain.models.MonetaryAmount;
import revolut.transfer.integration.dto.Account;
import revolut.transfer.integration.dto.AccountHolderDetails;
import revolut.transfer.integration.dto.BankAccountDetails;
import revolut.transfer.integration.dto.command.CreateAccountHolder;
import revolut.transfer.integration.repositories.StubAccountHolderRepositoryImpl;
import revolut.transfer.integration.repositories.StubAccountRepositoryImpl;

import static acceptance.TestUtility.givenAccountHolder;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class AccountHolderTest extends ComponentTest {

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

        assertThat(StubAccountHolderRepositoryImpl.accountHolders).hasSize(1);
        assertThat(StubAccountHolderRepositoryImpl.accountHolders.get(0).getId()).isEqualTo(response.getId());
    }

    @Test
    public void createAccountHolder_createsDefaultAccount() throws Exception {
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

        assertThat(response.getAccounts()).hasSize(1);
        Account account = response.getAccounts().get(0);
        assertThat(account.getId()).isNotEmpty();
        assertThat(account.getAccountType()).isEqualTo("UK");
        assertThat(account.getAccountHolderId()).isEqualTo(response.getId());
        assertThat(account.getCurrencyType()).isEqualTo("GBP");
        assertThat(account.getBalance()).isEqualTo(new revolut.transfer.integration.dto.MonetaryAmount(MonetaryAmount.ZERO_GBP));
        BankAccountDetails bankAccountDetails = account.getBankAccountDetails();
        assertThat(bankAccountDetails).isNotNull();
        assertThat(bankAccountDetails.getAccountNumber()).isNotEmpty();
        assertThat(bankAccountDetails.getIban()).isNotEmpty();
        assertThat(bankAccountDetails.getSortCode()).isNotEmpty();
        assertThat(bankAccountDetails.getBic()).isNotEmpty();

        assertThat(StubAccountRepositoryImpl.accounts).hasSize(1);
        revolut.transfer.domain.models.accounts.Account createdAccount = StubAccountRepositoryImpl.accounts.get(0);
        assertThat(createdAccount.getId()).isEqualTo(account.getId());
        assertThat(createdAccount.getAccountHolderId()).isEqualTo(account.getAccountHolderId());
        assertThat(createdAccount.getAccountType().name()).isEqualTo(account.getAccountType());
        assertThat(new revolut.transfer.integration.dto.MonetaryAmount(createdAccount.getBalance())).isEqualTo(account.getBalance());
        assertThat(createdAccount.getBankAccountDetails()).isEqualToComparingFieldByField(account.getBankAccountDetails());
    }

    @Test
    public void getAccountHolderById() {
        AccountHolderDetails accountHolderDetails = givenAccountHolder(new CreateAccountHolder(
                "MR",
                "Clint",
                "Eastwood",
                "myemail@address.com",
                "UK"));

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

}

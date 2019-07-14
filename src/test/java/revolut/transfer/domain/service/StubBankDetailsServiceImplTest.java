package revolut.transfer.domain.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import revolut.transfer.domain.models.accounts.AccountType;
import revolut.transfer.domain.models.accounts.BankAccountDetails;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class StubBankDetailsServiceImplTest {

    @InjectMocks
    private StubBankAccountServiceImpl subject;

    @Test
    public void createBankAccountDetails_UKAccountType() {
        BankAccountDetails bankAccountDetails = subject.createBankAccountDetails(AccountType.UK);

        assertThat(bankAccountDetails.getAccountNumber()).isNotEmpty();
        assertThat(bankAccountDetails.getAccountNumber()).hasSize(8);

        assertThat(bankAccountDetails.getSortCode()).isNotEmpty();
        assertThat(bankAccountDetails.getSortCode()).hasSize(6);

        assertThat(bankAccountDetails.getIban()).isNotEmpty();
        assertThat(bankAccountDetails.getIban()).hasSize(22);
        assertThat(bankAccountDetails.getIban()).startsWith("GB");

        assertThat(bankAccountDetails.getBic()).isNotEmpty();
        assertThat(bankAccountDetails.getBic()).hasSizeGreaterThanOrEqualTo(8);
        assertThat(bankAccountDetails.getBic()).hasSizeLessThanOrEqualTo(11);
    }
}
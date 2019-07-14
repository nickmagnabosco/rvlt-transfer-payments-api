package revolut.transfer.domain.service;

import com.github.javafaker.Faker;
import revolut.transfer.domain.models.accounts.AccountType;
import revolut.transfer.domain.models.accounts.BankAccountDetails;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class StubBankAccountServiceImpl implements BankAccountService {

    private static final String UK_SORT_CODE = "123456";
    public static final String DEFAULT_COUNTRY_CODE = "GB";

    @Inject
    public StubBankAccountServiceImpl() {
    }

    public BankAccountDetails createBankAccountDetails(AccountType accountType) {
        Faker faker = Faker.instance();
        long accountNumber = faker.number().randomNumber(8, true);
        String iban = faker.finance().iban(DEFAULT_COUNTRY_CODE);
        String bic = faker.finance().bic();

        return accountType.equals(AccountType.UK) ?
                new BankAccountDetails(iban, bic, UK_SORT_CODE, String.valueOf(accountNumber)) :
                new BankAccountDetails(iban, bic, "", "");
    }

}

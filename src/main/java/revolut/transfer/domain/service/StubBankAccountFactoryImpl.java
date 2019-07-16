package revolut.transfer.domain.service;

import com.github.javafaker.Faker;
import revolut.transfer.domain.models.accounts.AccountType;
import revolut.transfer.domain.models.accounts.BankAccountDetails;
import revolut.transfer.domain.repositories.BankDetailsRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class StubBankAccountFactoryImpl implements BankAccountFactory {

    private static final String UK_SORT_CODE = "123456";
    public static final String DEFAULT_COUNTRY_CODE = "GB";
    private final BankDetailsRepository bankDetailsRepository;

    @Inject
    public StubBankAccountFactoryImpl(BankDetailsRepository bankDetailsRepository) {
        this.bankDetailsRepository = bankDetailsRepository;
    }

    public BankAccountDetails createBankAccountDetails(String accountId, AccountType accountType) {
        Faker faker = Faker.instance();
        long accountNumber = faker.number().randomNumber(8, true);
        String iban = faker.finance().iban(DEFAULT_COUNTRY_CODE);
        String bic = faker.finance().bic();

        String id = UUID.randomUUID().toString();
        BankAccountDetails bankAccountDetails;

        if (accountType.equals(AccountType.UK)) {
            bankAccountDetails = new BankAccountDetails(id, accountId, iban, bic, UK_SORT_CODE, String.valueOf(accountNumber));
        } else {
            bankAccountDetails = new BankAccountDetails(id, accountId, iban, bic, "", "");
        }

        return bankAccountDetails;
    }
}

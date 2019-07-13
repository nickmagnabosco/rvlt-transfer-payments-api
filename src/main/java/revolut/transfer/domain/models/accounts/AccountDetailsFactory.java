package revolut.transfer.domain.models.accounts;

import com.github.javafaker.Faker;
import revolut.transfer.domain.models.currency.CurrencyType;
import revolut.transfer.domain.repositories.AccountRepository;
import spark.utils.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Random;

@Singleton
public class AccountDetailsFactory {

    private static final String UK_SORT_CODE = "123456";
    private final AccountRepository accountRepository;

    @Inject
    public AccountDetailsFactory(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public BankAccountDetails createBankAccountDetails() {
        long accountNumber = Faker.instance().number().randomNumber(8, true);
        String iban = Faker.instance().finance().iban("GB");
        return new BankAccountDetails(iban, UK_SORT_CODE, String.valueOf(accountNumber));
    }

    public CurrencyType getCurrencyTypeForAccount(AccountType accountType) {
        switch (accountType) {
            case UK:
                return CurrencyType.GBP;
            case USD:
                return CurrencyType.USD;
            case IBAN:
                return CurrencyType.EUR;
            default:
                throw new IllegalArgumentException("Account type not supported");
        }
    }
}

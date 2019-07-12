package revolut.transfer.domain.accounts;

import lombok.Data;
import revolut.transfer.domain.currency.CurrencyType;

import java.util.Set;

@Data
public class Account {

    private final String id;
    private final String accountHolderId;
    private final AccountType accountType;
    private final AccountDetails accountDetails;

    private final CurrencyType defaultCurrencyType;
    private final Set<CurrencyType> activeCurrencies;

    public void addCurrency(CurrencyType currencyType) {
        activeCurrencies.add(currencyType);
    }

    public void removeCurrency(CurrencyType currencyType) {
        if (currencyType.equals(defaultCurrencyType)) {
            throw new RuntimeException("Cannot remove default currency type.");
        }

        activeCurrencies.remove(currencyType);
    }

}

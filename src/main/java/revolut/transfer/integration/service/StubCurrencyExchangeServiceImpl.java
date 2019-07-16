package revolut.transfer.integration.service;

import revolut.transfer.domain.models.MonetaryAmount;
import revolut.transfer.domain.models.currency.CurrencyType;
import revolut.transfer.domain.service.CurrencyExchangeService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;

@Singleton
public class StubCurrencyExchangeServiceImpl implements CurrencyExchangeService {

    @Inject
    public StubCurrencyExchangeServiceImpl() {
    }

    @Override
    public MonetaryAmount exchange(MonetaryAmount amount, CurrencyType currencyType) {
        CurrencyType sourceCurrency = amount.getCurrencyType();
        if (sourceCurrency.equals(currencyType)) {
            return amount;
        }

        switch (sourceCurrency) {
            case GBP:
                return exchangeGbpTo(amount, currencyType);
            case EUR:
                return exchangeEurTo(amount, currencyType);
            case USD:
                return exchangeUsdTo(amount, currencyType);
            default:
                throw new IllegalArgumentException("Unsupported currency");
        }
    }

    private MonetaryAmount exchangeGbpTo(MonetaryAmount amount, CurrencyType currencyType) {
        switch (currencyType) {
            case EUR:
                return amount.multiply(BigDecimal.valueOf(1.11)).setCurrency(CurrencyType.EUR);
            case USD:
                return amount.multiply(BigDecimal.valueOf(1.24)).setCurrency(CurrencyType.USD);
            default:
                throw new IllegalArgumentException("Unsupported currency");
        }
    }

    private MonetaryAmount exchangeEurTo(MonetaryAmount amount, CurrencyType currencyType) {
        switch (currencyType) {
            case GBP:
                return amount.multiply(BigDecimal.valueOf(0.90)).setCurrency(CurrencyType.GBP);
            case USD:
                return amount.multiply(BigDecimal.valueOf(1.12)).setCurrency(CurrencyType.USD);
            default:
                throw new IllegalArgumentException("Unsupported currency");
        }
    }

    private MonetaryAmount exchangeUsdTo(MonetaryAmount amount, CurrencyType currencyType) {
        switch (currencyType) {
            case GBP:
                return amount.multiply(BigDecimal.valueOf(0.81)).setCurrency(CurrencyType.GBP);
            case EUR:
                return amount.multiply(BigDecimal.valueOf(0.89)).setCurrency(CurrencyType.EUR);
            default:
                throw new IllegalArgumentException("Unsupported currency");
        }
    }


}

package revolut.transfer.domain.models;

import lombok.Data;
import revolut.transfer.domain.models.currency.CurrencyType;

import java.math.BigDecimal;

@Data
public class MonetaryAmount {

    private final BigDecimal amount;
    private final CurrencyType currencyType;

    public static MonetaryAmount ZERO_GBP = new MonetaryAmount(BigDecimal.ZERO, CurrencyType.GBP);
    public static MonetaryAmount ZERO_USD = new MonetaryAmount(BigDecimal.ZERO, CurrencyType.GBP);
    public static MonetaryAmount ZERO_EUR = new MonetaryAmount(BigDecimal.ZERO, CurrencyType.GBP);

    public static MonetaryAmount ofGBP(double amount) {
        return new MonetaryAmount(BigDecimal.valueOf(amount), CurrencyType.GBP);
    }

    public static MonetaryAmount ofEUR(double amount) {
        return new MonetaryAmount(BigDecimal.valueOf(amount), CurrencyType.EUR);
    }

    public static MonetaryAmount ofUSD(double amount) {
        return new MonetaryAmount(BigDecimal.valueOf(amount), CurrencyType.USD);
    }
}

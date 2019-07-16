package revolut.transfer.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import revolut.transfer.domain.models.currency.CurrencyType;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonetaryAmount {
    private static final MathContext ROUND_UP = new MathContext(120, RoundingMode.HALF_UP);

    private BigDecimal amount;
    private CurrencyType currencyType;

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

    public MonetaryAmount add(MonetaryAmount amount) {
        validateCurrencyTypeForOperation(this, amount);
        return new MonetaryAmount(this.getAmount().add(amount.getAmount(), ROUND_UP), this.getCurrencyType());
    }

    public MonetaryAmount negate() {
        return new MonetaryAmount(this.getAmount().negate(), this.getCurrencyType());
    }

    public MonetaryAmount subtract(MonetaryAmount amount) {
        validateCurrencyTypeForOperation(this, amount);
//        if (amount.isGreaterThan(this)) {
//            throw new IllegalArgumentException("Result of operation cannot be less than 0.0");
//        }

        return new MonetaryAmount(this.getAmount().subtract(amount.getAmount(), ROUND_UP), this.getCurrencyType());
    }

    public boolean isGreaterThan(MonetaryAmount amount) {
        validateCurrencyTypeForOperation(this, amount);
        return this.getAmount().compareTo(amount.getAmount()) > 1;
    }

    public boolean isLessThan(MonetaryAmount amount) {
        validateCurrencyTypeForOperation(this, amount);
        return this.getAmount().compareTo(amount.getAmount()) < 0;
    }

    public boolean isEqualTo(MonetaryAmount amount) {
        validateCurrencyTypeForOperation(this, amount);
        return this.getAmount().compareTo(amount.getAmount()) == 0;
    }

    private static void validateCurrencyTypeForOperation(MonetaryAmount amount1, MonetaryAmount amount2) {
        if (!amount1.getCurrencyType().equals(amount2.getCurrencyType())) {
            throw new IllegalArgumentException("Cannot perform operation on different currency.");
        }
    }

}

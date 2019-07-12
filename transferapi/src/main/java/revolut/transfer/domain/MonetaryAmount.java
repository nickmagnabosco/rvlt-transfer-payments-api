package revolut.transfer.domain;

import lombok.Data;
import revolut.transfer.domain.currency.CurrencyType;

import java.math.BigDecimal;

@Data
public class MonetaryAmount {

    private final BigDecimal amount;
    private final CurrencyType currencyType;

}

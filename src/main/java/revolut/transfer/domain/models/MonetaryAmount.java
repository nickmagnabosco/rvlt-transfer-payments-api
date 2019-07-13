package revolut.transfer.domain.models;

import lombok.Data;
import revolut.transfer.domain.models.currency.CurrencyType;

import java.math.BigDecimal;

@Data
public class MonetaryAmount {

    private final BigDecimal amount;
    private final CurrencyType currencyType;

}

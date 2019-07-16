package revolut.transfer.integration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import revolut.transfer.domain.models.currency.CurrencyType;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MonetaryAmount {
    private BigDecimal amount;
    private String currencyType;

    public MonetaryAmount(revolut.transfer.domain.models.MonetaryAmount monetaryAmount) {
        amount = monetaryAmount == null ? null : monetaryAmount.getAmount();
        currencyType = monetaryAmount == null ? null : monetaryAmount.getCurrencyType().name();
    }

    public revolut.transfer.domain.models.MonetaryAmount toDomain() {
        return new revolut.transfer.domain.models.MonetaryAmount(
                amount,
                CurrencyType.fromString(currencyType)
        );
    }
}

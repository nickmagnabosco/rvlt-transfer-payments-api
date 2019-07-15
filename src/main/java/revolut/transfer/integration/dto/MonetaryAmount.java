package revolut.transfer.integration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MonetaryAmount {
    private BigDecimal amount;
    private String currencyType;

    public MonetaryAmount(revolut.transfer.domain.models.MonetaryAmount monetaryAmount) {
        amount = monetaryAmount.getAmount();
        currencyType = monetaryAmount.getCurrencyType().name();
    }
}

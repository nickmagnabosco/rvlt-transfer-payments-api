package revolut.transfer.domain.models.quotes;

import lombok.Value;
import revolut.transfer.domain.models.MonetaryAmount;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
public class Quote {

    private final String id;
    private final MonetaryAmount sourceValue;
    private final MonetaryAmount targetValue;
    private final BigDecimal rate;
    private final LocalDateTime createdDateTime;
    private final LocalDateTime expirationDateTime;
    private final MonetaryAmount fee;
    private final String requesterAccountId;

    public boolean isStillValid() {
        return LocalDateTime.now().isBefore(expirationDateTime);
    }

}

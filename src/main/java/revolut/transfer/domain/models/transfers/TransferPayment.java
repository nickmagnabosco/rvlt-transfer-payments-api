package revolut.transfer.domain.models.transfers;

import lombok.Value;
import revolut.transfer.domain.models.MonetaryAmount;

import java.time.LocalDateTime;

@Value
public class TransferPayment {

    private final String id;
    private final String sourceAccountId;
    private final String targetAccountId;
    private final String quoteId;
    private final String reference;
    private final TransferStatus status;
    private final TransferType transferType;
    private final LocalDateTime dateTimeCreated;
    private final MonetaryAmount sourceValue;
    private final MonetaryAmount targetValue;

}

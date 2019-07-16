package revolut.transfer.domain.models.transactions;

import lombok.Value;
import lombok.experimental.NonFinal;
import revolut.transfer.domain.models.MonetaryAmount;

import java.time.LocalDateTime;

@Value
@NonFinal
public class Transaction {

    private final String id;
    private final String requestId;
    private final String accountId;
    private final TransactionStatus status;
    private final TransactionType type;
    private final MonetaryAmount amount;
    private final LocalDateTime dateTime;

}

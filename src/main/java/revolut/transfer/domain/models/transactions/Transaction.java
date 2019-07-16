package revolut.transfer.domain.models.transactions;

import lombok.Value;
import lombok.experimental.NonFinal;
import revolut.transfer.domain.models.MonetaryAmount;

@Value
@NonFinal
public class Transaction {

    private final String id;
    private final String sourceAccountId;
    private final String targetAccountId;
    private final TransactionStatus status;
    private final TransactionType type;
    private final MonetaryAmount amount;

}

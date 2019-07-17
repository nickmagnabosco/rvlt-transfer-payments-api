package revolut.transfer.domain.models.transactions;

import revolut.transfer.domain.models.MonetaryAmount;
import revolut.transfer.domain.models.transactions.Transaction;
import revolut.transfer.domain.models.transactions.TransactionStatus;
import revolut.transfer.domain.models.transactions.TransactionType;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDateTime;
import java.util.UUID;

@Singleton
public class FundTransactionFactory {
    @Inject
    public FundTransactionFactory() {
    }

    public Transaction createTransaction(
            String id,
            String requestId,
            String sourceAccountId,
            TransactionStatus status,
            TransactionType transactionType,
            MonetaryAmount amount) {
        return new Transaction(
                id,
                requestId,
                sourceAccountId,
                status,
                transactionType,
                amount,
                LocalDateTime.now()
        );
    }

    public Transaction createTransactionNewId(
            String sourceAccountId,
            TransactionStatus status,
            TransactionType transactionType,
            MonetaryAmount amount) {
        return new Transaction(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                sourceAccountId,
                status,
                transactionType,
                amount,
                LocalDateTime.now()
        );
    }
}

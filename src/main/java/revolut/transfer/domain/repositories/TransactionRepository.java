package revolut.transfer.domain.repositories;

import org.jdbi.v3.core.Handle;
import revolut.transfer.domain.models.transactions.Transaction;
import revolut.transfer.domain.models.transactions.TransactionStatus;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    String createTransaction(Handle handle, Transaction transaction);

    void updateStatus(Handle handle, String transactionId, TransactionStatus status);

    Optional<Transaction> getTransactionByRequestId(String requestId);
    Optional<Transaction> getTransactionByRequestId(Handle handle, String requestId);

    Optional<Transaction> getTransactionByTransactionId(String transactionId);

    List<Transaction> getAllTransactionsByAccountId(String accountId);
    List<Transaction> getAllTransactionsByAccountId(Handle handle, String accountId);

}

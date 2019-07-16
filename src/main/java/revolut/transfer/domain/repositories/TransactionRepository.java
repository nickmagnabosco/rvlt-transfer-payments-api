package revolut.transfer.domain.repositories;

import org.jdbi.v3.core.Handle;
import revolut.transfer.domain.models.transactions.Transaction;
import revolut.transfer.domain.models.transactions.TransactionStatus;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    String createTransaction(Handle handle, Transaction transaction);
    void updateStatus(Handle handle, String transactionId, TransactionStatus status);
    Optional<Transaction> getTransactionById(String id);
    List<Transaction> getAllTransactionsByAccountId(String accountId);

}

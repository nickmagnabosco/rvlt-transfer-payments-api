package revolut.transfer.domain.repositories;

import revolut.transfer.domain.models.transactions.Transaction;
import revolut.transfer.domain.models.transactions.TransactionStatus;

import java.util.List;

public interface TransactionRepository {
    String createTransaction(Transaction transaction);
    void updateStatus(String transactionId, TransactionStatus status);
    Transaction getTransactionById(String id);
    List<Transaction> getAllTransactionsByAccountId(String accountId);

}

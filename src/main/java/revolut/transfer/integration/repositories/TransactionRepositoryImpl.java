package revolut.transfer.integration.repositories;

import org.jdbi.v3.core.Handle;
import revolut.transfer.domain.models.transactions.Transaction;
import revolut.transfer.domain.models.transactions.TransactionStatus;
import revolut.transfer.domain.repositories.TransactionRepository;
import revolut.transfer.integration.mappers.TransactionMapper;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
public class TransactionRepositoryImpl implements TransactionRepository {
    private final TransactionMapper transactionMapper;
    private final JDBIProvider jdbiProvider;

    @Override
    public String createTransaction(Handle handle, Transaction transaction) {
        handle.execute("INSERT INTO TRANSACTION (id, account_id, status, type, amount_value, amount_currency_type) "
                + "VALUES (?, ?, ?, ?, ?, ?)",
                transaction.getId(),
                transaction.getAccountId(),
                transaction.getStatus(),
                transaction.getType(),
                transaction.getAmount().getAmount(),
                transaction.getAmount().getCurrencyType());
        return transaction.getId();
    }

    @Override
    public void updateStatus(Handle handle, String transactionId, TransactionStatus status) {
        handle.execute("UPDATE TRANSACTION "
                + "WHERE id=? "
                + "SET status=?",transactionId, status);
    }

    @Inject
    public TransactionRepositoryImpl(TransactionMapper transactionMapper, JDBIProvider jdbiProvider) {
        this.transactionMapper = transactionMapper;
        this.jdbiProvider = jdbiProvider;
    }

    @Override
    public Optional<Transaction> getTransactionById(String id) {
        return jdbiProvider.getJdbi().withHandle(handle -> handle.createQuery(
                "SELECT id, account_id, status, type, amount_value, amount_currency_type "
                        + "FROM TRANSACTION "
                        + "WHERE id=:id")
                .bind("id", id)
                .map(transactionMapper)
                .findFirst());
    }

    @Override
    public List<Transaction> getAllTransactionsByAccountId(String accountId) {
        return jdbiProvider.getJdbi().withHandle(handle -> handle.createQuery(
                "SELECT id, account_id, status, type, amount_value, amount_currency_type "
                        + "FROM TRANSACTION "
                        + "WHERE account_id=:accountId")
                .bind("accountId", accountId)
                .map(transactionMapper)
                .list());
    }
}

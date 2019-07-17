package revolut.transfer.integration.repositories;

import org.jdbi.v3.core.Handle;
import revolut.transfer.domain.models.transactions.Transaction;
import revolut.transfer.domain.models.transactions.TransactionStatus;
import revolut.transfer.domain.repositories.TransactionRepository;
import revolut.transfer.domain.utility.DateTimeUtility;
import revolut.transfer.integration.mappers.TransactionMapper;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
public class TransactionRepositoryImpl implements TransactionRepository {
    public static final String SELECT_TRANSACTION = "SELECT id, request_id, account_id, status, type, amount_value, amount_currency_type, created_datetime ";
    private final TransactionMapper transactionMapper;
    private final JDBIProvider jdbiProvider;


    @Inject
    public TransactionRepositoryImpl(TransactionMapper transactionMapper, JDBIProvider jdbiProvider) {
        this.transactionMapper = transactionMapper;
        this.jdbiProvider = jdbiProvider;
    }

    @Override
    public String createTransaction(Handle handle, Transaction transaction) {
        handle.execute("INSERT INTO TRANSACTION (id, request_id, account_id, status, type, amount_value, amount_currency_type, created_datetime) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                transaction.getId(),
                transaction.getRequestId(),
                transaction.getAccountId(),
                transaction.getStatus(),
                transaction.getType(),
                transaction.getAmount().getAmount(),
                transaction.getAmount().getCurrencyType(),
                DateTimeUtility.toTimestamp(transaction.getDateTime()));
        return transaction.getId();
    }

    @Override
    public Optional<Transaction> getTransactionByRequestId(Handle handle, String requestId) {
        return handle.createQuery(
                SELECT_TRANSACTION
                        + "FROM TRANSACTION "
                        + "WHERE request_id=:requestId")
                .bind("requestId", requestId)
                .map(transactionMapper)
                .findFirst();
    }

    @Override
    public Optional<Transaction> getTransactionByRequestId(String requestId) {
        return jdbiProvider.getJdbi().withHandle(handle -> getTransactionByRequestId(handle, requestId));
    }

    @Override
    public List<Transaction> getAllTransactionsByAccountId(Handle handle, String accountId) {
        return handle.createQuery(
                SELECT_TRANSACTION
                        + "FROM TRANSACTION "
                        + "WHERE account_id=:accountId "
                        + "ORDER BY created_datetime ASC")
                .bind("accountId", accountId)
                .map(transactionMapper)
                .list();
    }

    @Override
    public List<Transaction> getAllTransactionsByAccountId(String accountId) {
        return jdbiProvider.getJdbi().withHandle(handle -> getAllTransactionsByAccountId(handle, accountId));
    }

    @Override
    public void updateStatus(Handle handle, String transactionId, TransactionStatus status) {
        handle.execute("UPDATE TRANSACTION "
                + "SET status=? "
                + "WHERE id=?", status, transactionId);
    }

    @Override
    public Optional<Transaction> getTransactionByTransactionId(String transactionId) {
        return jdbiProvider.getJdbi().withHandle(handle ->
                handle.createQuery(
                        SELECT_TRANSACTION
                                + "FROM TRANSACTION "
                                + "WHERE id=:transactionId ")
                        .bind("transactionId", transactionId)
                        .map(transactionMapper)
                        .findFirst());
    }
}

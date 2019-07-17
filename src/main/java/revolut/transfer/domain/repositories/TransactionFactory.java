package revolut.transfer.domain.repositories;

import org.jdbi.v3.core.Handle;

import java.util.function.Consumer;
import java.util.function.Function;

public interface TransactionFactory {
    void useTransaction(Consumer<Handle> callback);
    <T> T inTransaction(Function<Handle, T> callbackFn);
    <T> T inTransactionWithRollback(Function<Handle, T> callbackFn, Function<Handle, T>  rollbackAction);
}

package revolut.transfer.integration.repositories;

import com.google.common.base.Throwables;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleConsumer;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.transaction.SerializableTransactionRunner;
import revolut.transfer.domain.repositories.TransactionFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.jdbi.v3.core.transaction.TransactionIsolationLevel.SERIALIZABLE;

@Singleton
public class JDBIProvider implements TransactionFactory {
    private static Jdbi jdbi;

    @Inject
    public JDBIProvider() {
    }

    public Jdbi getJdbi() {
        if (jdbi == null) {
            jdbi = Jdbi.create("jdbc:h2:mem:test;INIT=create schema if not exists test;DB_CLOSE_DELAY=-1;user=sa");
            jdbi.setTransactionHandler(new SerializableTransactionRunner());
        }

        return jdbi;
    }

    @Override
    public void useTransaction(Consumer<Handle> callback) {
        try (Handle openHandle = getJdbi().open()){

            openHandle.useTransaction(SERIALIZABLE, handle -> {
                callback.accept(handle);
                handle.commit();
            });
        } catch (Exception e) {
            throw Throwables.propagate(e.getCause());
        }
    }

    @Override
    public <T> T inTransaction(Function<Handle, T> callbackFn) {
        try (Handle openHandle = getJdbi().open()) {
            return openHandle.inTransaction(SERIALIZABLE, handle -> {
                T result = callbackFn.apply(handle);
                handle.commit();
                return result;
            });
        } catch (Exception e) {
            throw Throwables.propagate(e.getCause());
        }
    }

}

package revolut.transfer.integration.repositories;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import revolut.transfer.domain.repositories.TransactionFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class JDBIProvider implements TransactionFactory {
    private final Jdbi jdbi;

    @Inject
    public JDBIProvider() {
        this.jdbi = Jdbi.create("jdbc:h2:mem:test;INIT=create schema if not exists test;DB_CLOSE_DELAY=-1;user=sa");
    }

    public Jdbi getJdbi() {
        return jdbi;
    }

    @Override
    public Handle openHandle() {
        return jdbi.open();
    }

}

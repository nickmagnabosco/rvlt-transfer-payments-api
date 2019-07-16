package revolut.transfer.domain.repositories;

import org.jdbi.v3.core.Handle;

public interface TransactionFactory {

    Handle openHandle();
}

package revolut.transfer.domain.commands;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.jdbi.v3.core.Handle;
import revolut.transfer.domain.models.accounts.Account;
import revolut.transfer.domain.models.transactions.Transaction;
import revolut.transfer.domain.models.transactions.TransactionStatus;
import revolut.transfer.domain.repositories.TransactionRepository;

@AllArgsConstructor
@Value
@NonFinal
public class PendingTransaction {
    private final Transaction outboundPaymentTransaction;
    private final Transaction inboundPaymentTransaction;
    private final Account sourceAccount;
    private final TransactionRepository transactionRepository;

    public void updatePendingTransactions(Handle handle, TransactionStatus status) {
        transactionRepository.updateStatus(handle, getOutboundPaymentTransaction().getId(), status);
        transactionRepository.updateStatus(handle, getInboundPaymentTransaction().getId(), status);
    }
}

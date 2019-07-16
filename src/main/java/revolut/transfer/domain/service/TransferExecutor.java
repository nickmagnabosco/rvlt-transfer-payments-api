package revolut.transfer.domain.service;

import org.h2.engine.ConnectionInfo;
import org.h2.jdbc.JdbcConnection;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.jdbcx.JdbcDataSource;
import revolut.transfer.domain.models.accounts.Account;
import revolut.transfer.domain.models.transactions.Transaction;
import revolut.transfer.domain.models.transactions.TransactionStatus;
import revolut.transfer.domain.models.transactions.TransactionType;
import revolut.transfer.domain.models.transfers.TransferPayment;
import revolut.transfer.domain.models.transfers.TransferType;
import revolut.transfer.domain.repositories.AccountRepository;
import revolut.transfer.domain.repositories.TransactionRepository;
import revolut.transfer.domain.repositories.TransferRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class TransferExecutor {
    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Inject
    public TransferExecutor(TransferRepository transferRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public synchronized void executeTransfer(TransferPayment transferPayment) {
//        Account account = accountRepository.getAccountByHolderIdAndAccountId(transferPayment.getSourceAccountId(), transferPayment.getSourceAccountId());
//
//        if (transferPayment.getTransferType().equals(TransferType.DEPOSIT)) {
//            Transaction transaction = new Transaction(
//                    UUID.randomUUID().toString(),
//                    transferPayment.getSourceAccountId(),
//                    transferPayment.getTargetAccountId(),
//                    TransactionStatus.IN_PROGRESS,
//                    TransactionType.DEPOSIT,
//                    transferPayment.getTransferAmount());
//
//            String transactionId = transactionRepository.createTransaction(transaction);
//            Account updatedAccount = account.depositAmount(transferPayment.getTransferAmount());
//            accountRepository.updateAccount(updatedAccount);
//            transactionRepository.updateStatus(transactionId, TransactionStatus.COMPLETED);
            // 1. create transaction / pending
            // 2. update available balance
            // 3. update balance
            // 4. Update transaction status
        }


//        if (transferPayment.getTransferType().equals(TransferType.PAYMENT)) {
//
//        }
//    }
}

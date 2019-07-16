package revolut.transfer.integration.service;

import revolut.transfer.domain.commands.CreateDepositCommand;
import revolut.transfer.domain.commands.CreateTransferCommand;
import revolut.transfer.integration.dto.Transaction;
import revolut.transfer.integration.dto.command.CreateDeposit;
import revolut.transfer.integration.dto.command.CreateTransfer;
import revolut.transfer.integration.transformers.TransactionTransformer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class TransactionService {

    private final revolut.transfer.domain.service.TransactionService domain;
    private final TransactionTransformer transactionTransformer;

    @Inject
    public TransactionService(revolut.transfer.domain.service.TransactionService domain, TransactionTransformer transactionTransformer) {
        this.domain = domain;
        this.transactionTransformer = transactionTransformer;
    }

    public Transaction createDeposit(String accountHolderId, String accountId, CreateDeposit createDeposit) {
        CreateDepositCommand createDepositCommand = transactionTransformer.transform(accountHolderId, accountId, createDeposit);
        return transactionTransformer.transformTransaction(domain.createDeposit(createDepositCommand));
    }

    public Transaction createTransfer(String accountHolderId, String accountId, CreateTransfer createTransfer) {
        CreateTransferCommand createTransferCommand = transactionTransformer.transform(accountHolderId, accountId, createTransfer);
        return transactionTransformer.transformTransaction(domain.createTransfer(createTransferCommand));
    }

    public Transaction getTransactionByAccountIdAndTransactionId(String accountHolderId, String accountId, String transactionId) {
        return transactionTransformer.transformTransaction(domain.getTransactionByAccountIdAndTransactionId(accountHolderId, accountId, transactionId));
    }

    public List<Transaction> getTransactionsByAccountId(String accountHolderId, String accountId) {
        return domain.getAllTransactionByAccountId(accountHolderId, accountId)
                .stream()
                .map(transactionTransformer::transformTransaction)
                .collect(Collectors.toList());
    }
}

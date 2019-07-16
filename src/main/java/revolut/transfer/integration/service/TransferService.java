package revolut.transfer.integration.service;

import revolut.transfer.domain.commands.CreateDepositCommand;
import revolut.transfer.domain.commands.CreateTransferCommand;
import revolut.transfer.domain.service.TransactionService;
import revolut.transfer.integration.dto.Transaction;
import revolut.transfer.integration.dto.command.CreateDeposit;
import revolut.transfer.integration.dto.command.CreateTransfer;
import revolut.transfer.integration.transformers.TransactionTransformer;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TransferService {

    private final TransactionService domain;
    private final TransactionTransformer transactionTransformer;

    @Inject
    public TransferService(TransactionService domain, TransactionTransformer transactionTransformer) {
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
}

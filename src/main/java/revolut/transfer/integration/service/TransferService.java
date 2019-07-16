package revolut.transfer.integration.service;

import revolut.transfer.domain.commands.CreateDepositCommand;
import revolut.transfer.domain.service.TransactionService;
import revolut.transfer.integration.dto.Transaction;
import revolut.transfer.integration.dto.command.CreateDeposit;
import revolut.transfer.integration.mappers.TransactionMapper;
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
        CreateDepositCommand transform = transactionTransformer.transform(accountHolderId, accountId, createDeposit);
        return transactionTransformer.transformTransaction(domain.createDeposit(transform));
    }
}

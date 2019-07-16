package revolut.transfer.domain.service;

import revolut.transfer.domain.commands.CreateDepositCommand;
import revolut.transfer.domain.commands.CreateTransferCommand;
import revolut.transfer.domain.exceptions.ResourceNotFoundException;
import revolut.transfer.domain.models.transactions.Transaction;
import revolut.transfer.domain.repositories.TransactionRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class TransactionService {
    private final TransactionRepository transactionRepository;
    @Inject
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction createDeposit(CreateDepositCommand createDepositCommand) {
        return createDepositCommand.execute();
    }

    public Transaction createTransfer(CreateTransferCommand createTransferCommand) {
        return createTransferCommand.execute();
    }

    public Transaction getTransactionByAccountIdAndTransactionId(String accountHolderId, String accountId, String transactionId) {
        return transactionRepository.getTransactionByTransactionId(transactionId).orElseThrow(ResourceNotFoundException::new);
    }

    public List<Transaction> getAllTransactionByAccountId(String accountHolderId, String accountId) {
        return transactionRepository.getAllTransactionsByAccountId(accountId);
    }
}

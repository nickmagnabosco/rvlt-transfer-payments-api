package revolut.transfer.domain.service;

import revolut.transfer.domain.commands.CreateDepositCommand;
import revolut.transfer.domain.commands.CreateTransferCommand;
import revolut.transfer.domain.models.transactions.Transaction;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TransactionService {
    @Inject
    public TransactionService() {
    }

    public Transaction createDeposit(CreateDepositCommand createDepositCommand) {
        return createDepositCommand.execute();
    }

    public Transaction createTransfer(CreateTransferCommand createTransferCommand) {
        return createTransferCommand.execute();
    }
}

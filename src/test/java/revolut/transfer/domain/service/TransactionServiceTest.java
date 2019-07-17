package revolut.transfer.domain.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import revolut.transfer.domain.commands.CreateDepositCommand;
import revolut.transfer.domain.commands.CreateTransferCommand;
import revolut.transfer.domain.exceptions.ResourceNotFoundException;
import revolut.transfer.domain.models.transactions.Transaction;
import revolut.transfer.domain.repositories.TransactionRepository;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService subject;

    @Test
    public void createDeposit_executeCommand() {
        CreateDepositCommand createDepositCommand = mock(CreateDepositCommand.class);

        subject.createDeposit(createDepositCommand);

        verify(createDepositCommand).execute();
    }

    @Test
    public void createTransfer_executeCommand() {
        CreateTransferCommand command = mock(CreateTransferCommand.class);

        subject.createTransfer(command);

        verify(command).execute();
    }

    @Test
    public void getTransactionByAccountIdAndTransactionId_getsViaRepository() {
        Transaction transaction = mock(Transaction.class);
        when(transactionRepository.getTransactionByTransactionId("transf123")).thenReturn(Optional.of(transaction));
        subject.getTransactionByAccountIdAndTransactionId("accHol123", "acc123", "transf123");

        verify(transactionRepository).getTransactionByTransactionId("transf123");
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getTransactionByAccountIdAndTransactionId_whenNotFound_throwsResourceNotFoundException() {
        when(transactionRepository.getTransactionByTransactionId("transf123")).thenReturn(Optional.empty());
        subject.getTransactionByAccountIdAndTransactionId("accHol123", "acc123", "transf123");

        verify(transactionRepository).getTransactionByTransactionId("transf123");
    }

    @Test
    public void getAllTransactionByAccountId_getsViaRepository() {
        subject.getAllTransactionByAccountId("accHol123", "acc123");

        verify(transactionRepository).getAllTransactionsByAccountId("acc123");
    }
}
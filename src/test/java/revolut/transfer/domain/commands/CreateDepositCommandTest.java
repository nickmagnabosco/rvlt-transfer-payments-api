package revolut.transfer.domain.commands;

import org.jdbi.v3.core.Handle;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import revolut.transfer.domain.exceptions.ValidationException;
import revolut.transfer.domain.models.MonetaryAmount;
import revolut.transfer.domain.models.accounts.Account;
import revolut.transfer.domain.models.currency.CurrencyType;
import revolut.transfer.domain.models.transactions.FundTransactionFactory;
import revolut.transfer.domain.models.transactions.Transaction;
import revolut.transfer.domain.models.transactions.TransactionStatus;
import revolut.transfer.domain.models.transactions.TransactionType;
import revolut.transfer.domain.repositories.AccountRepository;
import revolut.transfer.domain.repositories.TransactionFactory;
import revolut.transfer.domain.repositories.TransactionRepository;

import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doAnswer;

@RunWith(MockitoJUnitRunner.class)
public class CreateDepositCommandTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionFactory transactionFactory;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private FundTransactionFactory fundTransactionFactory;

    @Mock
    private Transaction depositTransaction;
    @Before
    public void setup() {
        Handle handle = mock(Handle.class);
        doAnswer(i -> {
            Function<Handle, Object> callback = (Function<Handle, Object>)i.getArguments()[0];
            callback.apply(handle);
            return depositTransaction;
        }).when(transactionFactory).inTransaction(any());
    }

    @Test
    public void validate_validCommand() {
        CreateDepositCommand createTransferCommand = new CreateDepositCommand(
                "id123",
                "accHol123",
                "123",
                "sourceAcc123",
                "sourceAcc123",
                MonetaryAmount.ofGBP(100),
                transactionRepository,
                transactionFactory,
                accountRepository,
                fundTransactionFactory
        );

        createTransferCommand.validate();
    }

    @Test(expected = ValidationException.class)
    public void validate_requestId() {
        CreateDepositCommand createTransferCommand = new CreateDepositCommand(
                "id123",
                "accHol123",
                "",
                "sourceAcc123",
                "sourceAcc123",
                MonetaryAmount.ofGBP(100),
                transactionRepository,
                transactionFactory,
                accountRepository,
                fundTransactionFactory
        );

        createTransferCommand.validate();
    }

    @Test(expected = ValidationException.class)
    public void validate_sourceAcc() {
        CreateDepositCommand createTransferCommand = new CreateDepositCommand(
                "id123",
                "accHol123",
                "req123",
                "",
                "targetAcc123",
                MonetaryAmount.ofGBP(100),
                transactionRepository,
                transactionFactory,
                accountRepository,
                fundTransactionFactory
        );

        createTransferCommand.validate();
    }

    @Test(expected = ValidationException.class)
    public void validate_targetAcc() {
        CreateDepositCommand createTransferCommand = new CreateDepositCommand(
                "id123",
                "accHol123",
                "req123",
                "source213",
                "",
                MonetaryAmount.ofGBP(100),
                transactionRepository,
                transactionFactory,
                accountRepository,
                fundTransactionFactory
        );

        createTransferCommand.validate();
    }

    @Test(expected = ValidationException.class)
    public void validate_sourceAccDifferentThanTargetAcc() {
        CreateDepositCommand createTransferCommand = new CreateDepositCommand(
                "id123",
                "accHol123",
                "req123",
                "sourceAcc123",
                "targetAcc123",
                MonetaryAmount.ofGBP(100),
                transactionRepository,
                transactionFactory,
                accountRepository,
                fundTransactionFactory
        );

        createTransferCommand.validate();
    }

    @Test(expected = ValidationException.class)
    public void validate_depositAmount() {
        CreateDepositCommand createTransferCommand = new CreateDepositCommand(
                "id123",
                "accHol123",
                "req123",
                "sourceAcc123",
                "sourceAcc123",
                null,
                transactionRepository,
                transactionFactory,
                accountRepository,
                fundTransactionFactory
        );

        createTransferCommand.validate();
    }

    @Test(expected = ValidationException.class)
    public void validate_negativeDepositAmount() {
        CreateDepositCommand createTransferCommand = new CreateDepositCommand(
                "id123",
                "accHol123",
                "req123",
                "sourceAcc123",
                "sourceAcc123",
                MonetaryAmount.ofGBP(-1),
                transactionRepository,
                transactionFactory,
                accountRepository,
                fundTransactionFactory
        );

        createTransferCommand.validate();
    }

    @Test(expected = ValidationException.class)
    public void validate_invalidAccount() {
        when(accountRepository.getAccountByAccountId(any(), eq("sourceAcc123"))).thenReturn(Optional.empty());
        CreateDepositCommand createTransferCommand = new CreateDepositCommand(
                "id123",
                "accHol123",
                "123",
                "sourceAcc123",
                "sourceAcc123",
                MonetaryAmount.ofGBP(100),
                transactionRepository,
                transactionFactory,
                accountRepository,
                fundTransactionFactory
        );

        createTransferCommand.execute();
    }

    @Test(expected = ValidationException.class)
    public void validate_invalidCurrencyFor_Account() {
        Account account = mock(Account.class);
        when(account.getCurrencyType()).thenReturn(CurrencyType.EUR);
        when(accountRepository.getAccountByAccountId(any(), eq("sourceAcc123"))).thenReturn(Optional.of(account));
        CreateDepositCommand createTransferCommand = new CreateDepositCommand(
                "id123",
                "accHol123",
                "123",
                "sourceAcc123",
                "sourceAcc123",
                MonetaryAmount.ofGBP(100),
                transactionRepository,
                transactionFactory,
                accountRepository,
                fundTransactionFactory
        );

        createTransferCommand.execute();
    }

    @Test(expected = ValidationException.class)
    public void validate_invalidAccountHolderFor_Account() {
        Account account = mock(Account.class);
        when(account.getCurrencyType()).thenReturn(CurrencyType.GBP);
        when(account.getAccountHolderId()).thenReturn("anotherAcc123");
        when(accountRepository.getAccountByAccountId(any(), eq("sourceAcc123"))).thenReturn(Optional.of(account));
        CreateDepositCommand createTransferCommand = new CreateDepositCommand(
                "id123",
                "accHol123",
                "123",
                "sourceAcc123",
                "sourceAcc123",
                MonetaryAmount.ofGBP(100),
                transactionRepository,
                transactionFactory,
                accountRepository,
                fundTransactionFactory
        );

        createTransferCommand.execute();
    }

    @Test(expected = ValidationException.class)
    public void validate_transactionAlreadyExists() {
        Account account = mock(Account.class);
        when(account.getCurrencyType()).thenReturn(CurrencyType.GBP);
        when(account.getAccountHolderId()).thenReturn("accHol123");
        when(accountRepository.getAccountByAccountId(any(), eq("sourceAcc123"))).thenReturn(Optional.of(account));

        Transaction transaction = mock(Transaction.class);
        when(transactionRepository.getTransactionByRequestId(any(), eq("123"))).thenReturn(Optional.of(transaction));
        CreateDepositCommand createTransferCommand = new CreateDepositCommand(
                "id123",
                "accHol123",
                "123",
                "sourceAcc123",
                "sourceAcc123",
                MonetaryAmount.ofGBP(100),
                transactionRepository,
                transactionFactory,
                accountRepository,
                fundTransactionFactory
        );

        createTransferCommand.execute();
    }

    @Test
    public void execute_validDeposit_createsTransactionViaFundTransactionFactory() {
        Account account = mock(Account.class);
        when(account.getCurrencyType()).thenReturn(CurrencyType.GBP);
        when(account.getAccountHolderId()).thenReturn("accHol123");
        when(accountRepository.getAccountByAccountId(any(), eq("sourceAcc123"))).thenReturn(Optional.of(account));

        when(transactionRepository.getTransactionByRequestId(any(), eq("123"))).thenReturn(Optional.empty());
        MonetaryAmount depositAmount = MonetaryAmount.ofGBP(100);
        CreateDepositCommand createTransferCommand = new CreateDepositCommand(
                "id123",
                "accHol123",
                "123",
                "sourceAcc123",
                "sourceAcc123",
                depositAmount,
                transactionRepository,
                transactionFactory,
                accountRepository,
                fundTransactionFactory
        );

        createTransferCommand.execute();

        verify(fundTransactionFactory).createTransaction("id123", "123", "sourceAcc123", TransactionStatus.COMPLETED, TransactionType.DEPOSIT, depositAmount);
    }

    @Test
    public void execute_validDeposit_createsTransactionViaRepository() {
        Account account = mock(Account.class);
        when(account.getCurrencyType()).thenReturn(CurrencyType.GBP);
        when(account.getAccountHolderId()).thenReturn("accHol123");
        when(accountRepository.getAccountByAccountId(any(), eq("sourceAcc123"))).thenReturn(Optional.of(account));

        when(transactionRepository.getTransactionByRequestId(any(), eq("123"))).thenReturn(Optional.empty());
        MonetaryAmount depositAmount = MonetaryAmount.ofGBP(100);
        Transaction transaction = mock(Transaction.class);
        when(fundTransactionFactory.createTransaction("id123", "123", "sourceAcc123", TransactionStatus.COMPLETED, TransactionType.DEPOSIT, depositAmount)).thenReturn(transaction);

        CreateDepositCommand createTransferCommand = new CreateDepositCommand(
                "id123",
                "accHol123",
                "123",
                "sourceAcc123",
                "sourceAcc123",
                depositAmount,
                transactionRepository,
                transactionFactory,
                accountRepository,
                fundTransactionFactory
        );

        createTransferCommand.execute();

        verify(transactionRepository).createTransaction(any(), eq(transaction));
    }

    @Test
    public void execute_validDeposit_returnsTransaction() {
        Account account = mock(Account.class);
        when(account.getCurrencyType()).thenReturn(CurrencyType.GBP);
        when(account.getAccountHolderId()).thenReturn("accHol123");
        when(accountRepository.getAccountByAccountId(any(), eq("sourceAcc123"))).thenReturn(Optional.of(account));

        when(transactionRepository.getTransactionByRequestId(any(), eq("123"))).thenReturn(Optional.empty());
        MonetaryAmount depositAmount = MonetaryAmount.ofGBP(100);
        Transaction transaction = mock(Transaction.class);
        when(fundTransactionFactory.createTransaction("id123", "123", "sourceAcc123", TransactionStatus.COMPLETED, TransactionType.DEPOSIT, depositAmount)).thenReturn(transaction);

        CreateDepositCommand createTransferCommand = new CreateDepositCommand(
                "id123",
                "accHol123",
                "123",
                "sourceAcc123",
                "sourceAcc123",
                depositAmount,
                transactionRepository,
                transactionFactory,
                accountRepository,
                fundTransactionFactory
        );

        Transaction result = createTransferCommand.execute();

        assertThat(result).isEqualTo(depositTransaction);
    }
}
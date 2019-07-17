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
import revolut.transfer.domain.service.CurrencyExchangeService;

import java.util.Optional;
import java.util.function.Function;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CreateTransferCommandTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionFactory transactionFactory;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CurrencyExchangeService currencyExchangeService;
    @Mock
    private FundTransactionFactory fundTransactionFactory;

    @Mock
    private PendingTransferTransaction pendingTransaction;

    @Mock
    private Transaction outboundTransaction;

    @Mock
    private Transaction inboundTransaction;

    @Before
    public void setup() {
        when(pendingTransaction.getOutboundPaymentTransaction()).thenReturn(outboundTransaction);
        when(pendingTransaction.getInboundPaymentTransaction()).thenReturn(inboundTransaction);

        Handle handle = mock(Handle.class);
        doAnswer(i -> {
            Function<Handle, Object> callback = (Function<Handle, Object>)i.getArguments()[0];
            callback.apply(handle);
            return pendingTransaction;
        }).when(transactionFactory).inTransaction(any());

        doAnswer(i -> {
            Function<Handle, Object> callback = (Function<Handle, Object>)i.getArguments()[0];
            Function<Handle, Object> callback2 = (Function<Handle, Object>)i.getArguments()[1];
            try {
                callback.apply(handle);
                return outboundTransaction;
            } catch (Exception e) {
                callback2.apply(handle);
                return outboundTransaction;
            }
        }).when(transactionFactory).inTransactionWithRollback(any(), any());
    }

    @Test
    public void validate_validCommand() {
        CreateTransferCommand createTransferCommand = new CreateTransferCommand(
                "id123",
                "accHol123",
                "123",
                "sourceAcc123",
                "targetAcc123",
                MonetaryAmount.ofGBP(100),
                transactionRepository,
                transactionFactory,
                accountRepository,
                currencyExchangeService,
                fundTransactionFactory
        );

        createTransferCommand.validate();
    }

    @Test(expected = ValidationException.class)
    public void validate_requestId() {
        CreateTransferCommand createTransferCommand = new CreateTransferCommand(
                "id123",
                "accHol123",
                "",
                "sourceAcc123",
                "targetAcc123",
                MonetaryAmount.ofGBP(100),
                transactionRepository,
                transactionFactory,
                accountRepository,
                currencyExchangeService,
                fundTransactionFactory
        );

        createTransferCommand.validate();
    }

    @Test(expected = ValidationException.class)
    public void validate_sourceAccount() {
        CreateTransferCommand createTransferCommand = new CreateTransferCommand(
                "id123",
                "accHol123",
                "123",
                "",
                "targetAcc123",
                MonetaryAmount.ofGBP(100),
                transactionRepository,
                transactionFactory,
                accountRepository,
                currencyExchangeService,
                fundTransactionFactory
        );

        createTransferCommand.validate();
    }

    @Test(expected = ValidationException.class)
    public void validate_targetAccount() {
        CreateTransferCommand createTransferCommand = new CreateTransferCommand(
                "id123",
                "accHol123",
                "123",
                "sourceAcc123",
                "",
                MonetaryAmount.ofGBP(100),
                transactionRepository,
                transactionFactory,
                accountRepository,
                currencyExchangeService,
                fundTransactionFactory
        );

        createTransferCommand.validate();
    }

    @Test(expected = ValidationException.class)
    public void validate_targetAccountAndSourceAcc_areEqual() {
        CreateTransferCommand createTransferCommand = new CreateTransferCommand(
                "id123",
                "accHol123",
                "123",
                "sourceAcc123",
                "sourceAcc123",
                MonetaryAmount.ofGBP(100),
                transactionRepository,
                transactionFactory,
                accountRepository,
                currencyExchangeService,
                fundTransactionFactory
        );

        createTransferCommand.validate();
    }

    @Test(expected = ValidationException.class)
    public void validate_transferAmount() {
        CreateTransferCommand createTransferCommand = new CreateTransferCommand(
                "id123",
                "accHol123",
                "123",
                "sourceAcc123",
                "targetAcc123",
                null,
                transactionRepository,
                transactionFactory,
                accountRepository,
                currencyExchangeService,
                fundTransactionFactory
        );

        createTransferCommand.validate();
    }

    @Test(expected = ValidationException.class)
    public void validate_negativeTransferAmount() {
        CreateTransferCommand createTransferCommand = new CreateTransferCommand(
                "id123",
                "accHol123",
                "123",
                "sourceAcc123",
                "targetAcc123",
                MonetaryAmount.ofGBP(-1),
                transactionRepository,
                transactionFactory,
                accountRepository,
                currencyExchangeService,
                fundTransactionFactory
        );

        createTransferCommand.validate();
    }

    @Test(expected = ValidationException.class)
    public void execute_validates() {
        CreateTransferCommand createTransferCommand = new CreateTransferCommand(
                "id123",
                "accHol123",
                "123",
                "sourceAcc123",
                "targetAcc123",
                MonetaryAmount.ofGBP(-1),
                transactionRepository,
                transactionFactory,
                accountRepository,
                currencyExchangeService,
                fundTransactionFactory
        );

        createTransferCommand.execute();
    }

    @Test(expected = ValidationException.class)
    public void execute_whenTransactionAlreadyExist_throwsValidationException() {
        Transaction transaction = mock(Transaction.class);
        when(transactionRepository.getTransactionByRequestId(any(), eq("req123"))).thenReturn(Optional.of(transaction));

        CreateTransferCommand createTransferCommand = new CreateTransferCommand(
                "id123",
                "accHol123",
                "req123",
                "sourceAcc123",
                "targetAcc123",
                MonetaryAmount.ofGBP(100),
                transactionRepository,
                transactionFactory,
                accountRepository,
                currencyExchangeService,
                fundTransactionFactory
        );

        createTransferCommand.execute();
    }

    @Test(expected = ValidationException.class)
    public void execute_whenSourceAccountDoesNotExist_throwsValidationException() {
        when(transactionRepository.getTransactionByRequestId(any(), eq("req123"))).thenReturn(Optional.empty());
        when(accountRepository.getAccountByHolderIdAndAccountId(any(), eq("accHol123"), eq("sourceAcc123"))).thenReturn(Optional.empty());

        CreateTransferCommand createTransferCommand = new CreateTransferCommand(
                "id123",
                "accHol123",
                "req123",
                "sourceAcc123",
                "targetAcc123",
                MonetaryAmount.ofGBP(100),
                transactionRepository,
                transactionFactory,
                accountRepository,
                currencyExchangeService,
                fundTransactionFactory
        );

        createTransferCommand.execute();
    }

    @Test(expected = ValidationException.class)
    public void execute_whenTargetAccountDoesNotExist_throwsValidationException() {
        when(transactionRepository.getTransactionByRequestId(any(), eq("req123"))).thenReturn(Optional.empty());
        Account sourceAccount = mock(Account.class);
        when(accountRepository.getAccountByHolderIdAndAccountId(any(), eq("accHol123"), eq("sourceAcc123"))).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.getAccountByAccountId(any(), eq("targetAcc123"))).thenReturn(Optional.empty());

        CreateTransferCommand createTransferCommand = new CreateTransferCommand(
                "id123",
                "accHol123",
                "req123",
                "sourceAcc123",
                "targetAcc123",
                MonetaryAmount.ofGBP(100),
                transactionRepository,
                transactionFactory,
                accountRepository,
                currencyExchangeService,
                fundTransactionFactory
        );

        createTransferCommand.execute();
    }

    @Test
    public void execute_whenTransferAmount_isDifferentThanTargetAccountCurrency_getTransferAmountFromCurrencyExchangeService() {
        when(transactionRepository.getTransactionByRequestId(any(), eq("req123"))).thenReturn(Optional.empty());
        Account sourceAccount = mock(Account.class);
        when(sourceAccount.getAvailableBalance()).thenReturn(MonetaryAmount.ofGBP(100));
        Account targetAccount = mock(Account.class);
        when(targetAccount.getCurrencyType()).thenReturn(CurrencyType.EUR);
        when(targetAccount.getAccountHolderId()).thenReturn("otherAccHolder123");
        when(accountRepository.getAccountByHolderIdAndAccountId(any(), eq("accHol123"), eq("sourceAcc123"))).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.getAccountByAccountId(any(), eq("targetAcc123"))).thenReturn(Optional.of(targetAccount));

        MonetaryAmount transferAmount = MonetaryAmount.ofGBP(100);
        CreateTransferCommand createTransferCommand = new CreateTransferCommand(
                "id123",
                "accHol123",
                "req123",
                "sourceAcc123",
                "targetAcc123",
                transferAmount,
                transactionRepository,
                transactionFactory,
                accountRepository,
                currencyExchangeService,
                fundTransactionFactory
        );

        createTransferCommand.execute();
        verify(currencyExchangeService).exchange(transferAmount, CurrencyType.EUR);
    }

    @Test
    public void execute_whenTransferAmount_isSameCurrencyAccountCurrency_doesNotCallExchangeService() {
        when(transactionRepository.getTransactionByRequestId(any(), eq("req123"))).thenReturn(Optional.empty());
        Account sourceAccount = mock(Account.class);
        when(sourceAccount.getAvailableBalance()).thenReturn(MonetaryAmount.ofGBP(100));
        Account targetAccount = mock(Account.class);
        when(targetAccount.getCurrencyType()).thenReturn(CurrencyType.GBP);
        when(targetAccount.getAccountHolderId()).thenReturn("otherAccHolder123");
        when(accountRepository.getAccountByHolderIdAndAccountId(any(), eq("accHol123"), eq("sourceAcc123"))).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.getAccountByAccountId(any(), eq("targetAcc123"))).thenReturn(Optional.of(targetAccount));

        MonetaryAmount transferAmount = MonetaryAmount.ofGBP(100);
        CreateTransferCommand createTransferCommand = new CreateTransferCommand(
                "id123",
                "accHol123",
                "req123",
                "sourceAcc123",
                "targetAcc123",
                transferAmount,
                transactionRepository,
                transactionFactory,
                accountRepository,
                currencyExchangeService,
                fundTransactionFactory
        );

        createTransferCommand.execute();
        verify(currencyExchangeService, never()).exchange(any(), any());
    }

    @Test
    public void execute_createOutboundPaymentTransaction_viaFundTransactionFactory_saveItToRepository() {
        when(transactionRepository.getTransactionByRequestId(any(), eq("req123"))).thenReturn(Optional.empty());
        Account sourceAccount = mock(Account.class);
        when(sourceAccount.getAvailableBalance()).thenReturn(MonetaryAmount.ofGBP(100));
        Account targetAccount = mock(Account.class);
        when(targetAccount.getCurrencyType()).thenReturn(CurrencyType.GBP);
        when(targetAccount.getAccountHolderId()).thenReturn("otherAccHolder123");
        when(accountRepository.getAccountByHolderIdAndAccountId(any(), eq("accHol123"), eq("sourceAcc123"))).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.getAccountByAccountId(any(), eq("targetAcc123"))).thenReturn(Optional.of(targetAccount));

        MonetaryAmount transferAmount = MonetaryAmount.ofGBP(100);

        when(fundTransactionFactory.createTransaction("id123", "req123", "sourceAcc123", TransactionStatus.IN_PROGRESS, TransactionType.OUTBOUND_PAYMENT, transferAmount)).thenReturn(outboundTransaction);

        CreateTransferCommand createTransferCommand = new CreateTransferCommand(
                "id123",
                "accHol123",
                "req123",
                "sourceAcc123",
                "targetAcc123",
                transferAmount,
                transactionRepository,
                transactionFactory,
                accountRepository,
                currencyExchangeService,
                fundTransactionFactory
        );

        createTransferCommand.execute();

        verify(fundTransactionFactory).createTransaction("id123", "req123", "sourceAcc123", TransactionStatus.IN_PROGRESS, TransactionType.OUTBOUND_PAYMENT, transferAmount);
        verify(transactionRepository).createTransaction(any(), eq(outboundTransaction));

    }

    @Test
    public void execute_createInboundPaymentTransaction_insufficientFund_createFailedTransaction() {
        when(transactionRepository.getTransactionByRequestId(any(), eq("req123"))).thenReturn(Optional.empty());
        Account sourceAccount = mock(Account.class);
        when(sourceAccount.getAvailableBalance()).thenReturn(MonetaryAmount.ofGBP(10));
        Account targetAccount = mock(Account.class);
        when(targetAccount.getCurrencyType()).thenReturn(CurrencyType.GBP);
        when(targetAccount.getAccountHolderId()).thenReturn("otherAccHolder123");
        when(accountRepository.getAccountByHolderIdAndAccountId(any(), eq("accHol123"), eq("sourceAcc123"))).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.getAccountByAccountId(any(), eq("targetAcc123"))).thenReturn(Optional.of(targetAccount));

        MonetaryAmount transferAmount = MonetaryAmount.ofGBP(100);

        when(fundTransactionFactory.createTransaction("id123", "req123", "sourceAcc123", TransactionStatus.FAILED, TransactionType.OUTBOUND_PAYMENT, transferAmount)).thenReturn(outboundTransaction);

        CreateTransferCommand createTransferCommand = new CreateTransferCommand(
                "id123",
                "accHol123",
                "req123",
                "sourceAcc123",
                "targetAcc123",
                transferAmount,
                transactionRepository,
                transactionFactory,
                accountRepository,
                currencyExchangeService,
                fundTransactionFactory
        );

        createTransferCommand.execute();
        verify(transactionRepository).createTransaction(any(), eq(outboundTransaction));
        verify(transactionRepository, never()).createTransaction(any(), eq(inboundTransaction));
    }

    @Test
    public void execute_createInboundPaymentTransaction_viaFundTransactionFactory_saveItToRepository() {
        when(transactionRepository.getTransactionByRequestId(any(), eq("req123"))).thenReturn(Optional.empty());
        Account sourceAccount = mock(Account.class);
        when(sourceAccount.getAvailableBalance()).thenReturn(MonetaryAmount.ofGBP(100));
        Account targetAccount = mock(Account.class);
        when(targetAccount.getCurrencyType()).thenReturn(CurrencyType.GBP);
        when(targetAccount.getAccountHolderId()).thenReturn("otherAccHolder123");
        when(accountRepository.getAccountByHolderIdAndAccountId(any(), eq("accHol123"), eq("sourceAcc123"))).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.getAccountByAccountId(any(), eq("targetAcc123"))).thenReturn(Optional.of(targetAccount));

        MonetaryAmount transferAmount = MonetaryAmount.ofGBP(100);

        when(fundTransactionFactory.createTransactionNewId("targetAcc123", TransactionStatus.IN_PROGRESS, TransactionType.INCOMING_PAYMENT, transferAmount)).thenReturn(inboundTransaction);

        CreateTransferCommand createTransferCommand = new CreateTransferCommand(
                "id123",
                "accHol123",
                "req123",
                "sourceAcc123",
                "targetAcc123",
                transferAmount,
                transactionRepository,
                transactionFactory,
                accountRepository,
                currencyExchangeService,
                fundTransactionFactory
        );

        createTransferCommand.execute();

        verify(fundTransactionFactory).createTransactionNewId("targetAcc123", TransactionStatus.IN_PROGRESS, TransactionType.INCOMING_PAYMENT, transferAmount);
        verify(transactionRepository).createTransaction(any(), eq(inboundTransaction));
    }

    @Test
    public void execute_whenInsufficientFunds_afterTransactionCreated_throwsValidationException_andUpdateStatusToFailed() {
        when(transactionRepository.getTransactionByRequestId(any(), eq("req123"))).thenReturn(Optional.empty());
        Account sourceAccount = mock(Account.class);
        when(sourceAccount.getAvailableBalance())
                .thenReturn(MonetaryAmount.ofGBP(100))
                .thenReturn(MonetaryAmount.ofGBP(10));
        when(pendingTransaction.getSourceAccount()).thenReturn(sourceAccount);
        when(pendingTransaction.canBeExecuted()).thenReturn(true);

        Account targetAccount = mock(Account.class);
        when(targetAccount.getCurrencyType()).thenReturn(CurrencyType.GBP);
        when(targetAccount.getAccountHolderId()).thenReturn("otherAccHolder123");
        when(accountRepository.getAccountByHolderIdAndAccountId(any(), eq("accHol123"), eq("sourceAcc123"))).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.getAccountByAccountId(any(), eq("targetAcc123"))).thenReturn(Optional.of(targetAccount));

        MonetaryAmount transferAmount = MonetaryAmount.ofGBP(100);

        when(fundTransactionFactory.createTransactionNewId("targetAcc123", TransactionStatus.IN_PROGRESS, TransactionType.INCOMING_PAYMENT, transferAmount)).thenReturn(inboundTransaction);

        CreateTransferCommand createTransferCommand = new CreateTransferCommand(
                "id123",
                "accHol123",
                "req123",
                "sourceAcc123",
                "targetAcc123",
                transferAmount,
                transactionRepository,
                transactionFactory,
                accountRepository,
                currencyExchangeService,
                fundTransactionFactory
        );

        createTransferCommand.execute();

        verify(pendingTransaction, never()).updatePendingTransactions(any(), eq(TransactionStatus.COMPLETED));
        verify(pendingTransaction).updatePendingTransactions(any(), eq(TransactionStatus.FAILED));
        verify(outboundTransaction).withNewStatus(TransactionStatus.FAILED);
    }

    @Test
    public void execute_validTransfer_andUpdateStatusToCOMPLETED() {
        when(transactionRepository.getTransactionByRequestId(any(), eq("req123"))).thenReturn(Optional.empty());
        Account sourceAccount = mock(Account.class);
        when(sourceAccount.getAvailableBalance()).thenReturn(MonetaryAmount.ofGBP(200));
        when(pendingTransaction.getSourceAccount()).thenReturn(sourceAccount);
        when(pendingTransaction.canBeExecuted()).thenReturn(true);

        Account targetAccount = mock(Account.class);
        when(targetAccount.getCurrencyType()).thenReturn(CurrencyType.GBP);
        when(targetAccount.getAccountHolderId()).thenReturn("otherAccHolder123");
        when(accountRepository.getAccountByHolderIdAndAccountId(any(), eq("accHol123"), eq("sourceAcc123"))).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.getAccountByAccountId(any(), eq("targetAcc123"))).thenReturn(Optional.of(targetAccount));

        MonetaryAmount transferAmount = MonetaryAmount.ofGBP(100);

        when(fundTransactionFactory.createTransactionNewId("targetAcc123", TransactionStatus.IN_PROGRESS, TransactionType.INCOMING_PAYMENT, transferAmount)).thenReturn(inboundTransaction);

        CreateTransferCommand createTransferCommand = new CreateTransferCommand(
                "id123",
                "accHol123",
                "req123",
                "sourceAcc123",
                "targetAcc123",
                transferAmount,
                transactionRepository,
                transactionFactory,
                accountRepository,
                currencyExchangeService,
                fundTransactionFactory
        );

        createTransferCommand.execute();

        verify(pendingTransaction).updatePendingTransactions(any(), eq(TransactionStatus.COMPLETED));
        verify(outboundTransaction).withNewStatus(TransactionStatus.COMPLETED);
    }
}
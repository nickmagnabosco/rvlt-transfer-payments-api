package revolut.transfer.domain.commands;

import com.google.common.collect.Lists;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.jdbi.v3.core.Handle;
import revolut.transfer.domain.exceptions.ValidationException;
import revolut.transfer.domain.exceptions.ValidationFailure;
import revolut.transfer.domain.models.MonetaryAmount;
import revolut.transfer.domain.models.accounts.Account;
import revolut.transfer.domain.models.transactions.Transaction;
import revolut.transfer.domain.models.transactions.FundTransactionFactory;
import revolut.transfer.domain.models.transactions.TransactionStatus;
import revolut.transfer.domain.repositories.AccountRepository;
import revolut.transfer.domain.repositories.TransactionFactory;
import revolut.transfer.domain.repositories.TransactionRepository;
import revolut.transfer.domain.service.CurrencyExchangeService;
import spark.utils.StringUtils;

import java.util.List;

import static revolut.transfer.domain.models.transactions.TransactionType.*;
import static revolut.transfer.domain.models.transactions.TransactionStatus.*;

@Value
@NonFinal
public class CreateTransferCommand {

    private final String id;
    private final String accountHolderId;
    private final String requestId;
    private final String sourceAccountId;
    private final String targetAccountId;
    private final MonetaryAmount transferAmount;
    private final TransactionRepository transactionRepository;
    private final TransactionFactory transactionFactory;
    private final AccountRepository accountRepository;
    private final CurrencyExchangeService currencyExchangeService;
    private final FundTransactionFactory fundTransactionFactory;

    public Transaction execute() {
        validate();
        PendingTransferTransaction pendingTransaction = transactionFactory.inTransaction(this::createPendingTransaction);

        if (!pendingTransaction.canBeExecuted()) {
            return pendingTransaction.getOutboundPaymentTransaction().withNewStatus(FAILED);
        }

        return transactionFactory.inTransactionWithRollback(handle -> {
            validateAmountForTransfer(pendingTransaction.getSourceAccount());
            pendingTransaction.updatePendingTransactions(handle, COMPLETED);
            return pendingTransaction.getOutboundPaymentTransaction().withNewStatus(COMPLETED);
        }, (handle -> {
            pendingTransaction.updatePendingTransactions(handle, FAILED);
            return pendingTransaction.getOutboundPaymentTransaction().withNewStatus(FAILED);
        }));
    }

    public void validate() {
        List<ValidationFailure> failures = Lists.newArrayList();
        if (StringUtils.isBlank(requestId)) {
            failures.add(new ValidationFailure("Request id must be provided"));
        }

        if (StringUtils.isBlank(sourceAccountId) || StringUtils.isBlank(targetAccountId)) {
            failures.add(new ValidationFailure("Source account and target account id must be provided"));
        } else if (sourceAccountId.equals(targetAccountId)) {
            failures.add(new ValidationFailure("Source account and target account id cannot be equal for transfer"));
        }

        if (transferAmount == null || transferAmount.getAmount() == null || transferAmount.getCurrencyType() == null ||
                transferAmount.getAmount().compareTo(MonetaryAmount.ZERO_GBP.getAmount()) <= 0) {
            failures.add(new ValidationFailure("Monetary amount not valid"));
        }

        if (!failures.isEmpty()) {
            throw new ValidationException(failures);
        }
    }

    private PendingTransferTransaction createPendingTransaction(Handle handle) {
        validateTransaction(handle);
        Account sourceAccount = getSourceAccountForTransfer(handle);
        Account targetAccount = getTargetAccountForTransfer(handle);

        boolean enoughBalanceForTransfer = hasEnoughBalanceForTransfer(sourceAccount);

        MonetaryAmount valueForTargetAccount = getCurrentValueForTargetAccount(targetAccount);
        TransactionStatus transactionStatus = enoughBalanceForTransfer ? IN_PROGRESS : FAILED;

        Transaction outboundPaymentTransaction = fundTransactionFactory.createTransaction(id, requestId, sourceAccountId, transactionStatus, OUTBOUND_PAYMENT, transferAmount);
        transactionRepository.createTransaction(handle, outboundPaymentTransaction);

        if (!enoughBalanceForTransfer) {
            // Skip creating recipient transaction and return un-executable pending transaction
            return new PendingTransferTransaction(outboundPaymentTransaction, null, sourceAccount, transactionRepository, false);
        }

        Transaction incomingPaymentTransaction = fundTransactionFactory.createTransactionNewId(targetAccountId, IN_PROGRESS, INCOMING_PAYMENT, valueForTargetAccount);
        transactionRepository.createTransaction(handle, incomingPaymentTransaction);

        return new PendingTransferTransaction(outboundPaymentTransaction, incomingPaymentTransaction, sourceAccount, transactionRepository, true);
    }

    private void validateTransaction(Handle handle) {
        if (transactionRepository.getTransactionByRequestId(handle, requestId).isPresent()) {
            throw new ValidationException(new ValidationFailure("Transaction with given request id already processed"));
        }
    }

    private Account getSourceAccountForTransfer(Handle handle) {
        return accountRepository.getAccountByHolderIdAndAccountId(handle, accountHolderId, sourceAccountId)
                .orElseThrow(() -> new ValidationException(new ValidationFailure("Source account id does not exist")));
    }

    private Account getTargetAccountForTransfer(Handle handle) {
        return accountRepository.getAccountByAccountId(handle, targetAccountId)
                .orElseThrow(() -> new ValidationException(new ValidationFailure("Target account id does not exist")));
    }

    private void validateAmountForTransfer(Account sourceAccount) {
        if (!hasEnoughBalanceForTransfer(sourceAccount)) {
            throw new ValidationException(new ValidationFailure("Unsufficient funds for transfer"));
        }
    }

    private boolean hasEnoughBalanceForTransfer(Account sourceAccount) {
        return !sourceAccount.getAvailableBalance().isLessThan(transferAmount);
    }

    private MonetaryAmount getCurrentValueForTargetAccount(Account targetAccount) {
        if (!transferAmount.getCurrencyType().equals(targetAccount.getCurrencyType())) {
            return currencyExchangeService.exchange(transferAmount, targetAccount.getCurrencyType());
        }

        return transferAmount;
    }

}

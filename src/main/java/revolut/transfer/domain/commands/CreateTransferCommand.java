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
import revolut.transfer.domain.models.transactions.TransactionStatus;
import revolut.transfer.domain.models.transactions.FundTransactionFactory;
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
        PendingTransaction pendingTransaction = transactionFactory.inTransaction((handle -> {
            validateTransaction(handle);
            Account sourceAccount = getSourceAccountForTransfer(handle);
            Account targetAccount = getTargetAccountForTransfer(handle);

            MonetaryAmount valueForTargetAccount = getCurrentValueForTargetAccount(targetAccount);

            Transaction outboundPaymentTransaction = fundTransactionFactory.createTransaction(id, requestId, sourceAccountId, IN_PROGRESS, OUTBOUND_PAYMENT, transferAmount);
            transactionRepository.createTransaction(handle, outboundPaymentTransaction);

            Transaction incomingPaymentTransaction = fundTransactionFactory.createTransactionNewId(targetAccountId, IN_PROGRESS, INCOMING_PAYMENT, valueForTargetAccount);
            transactionRepository.createTransaction(handle, incomingPaymentTransaction);

            return new PendingTransaction(outboundPaymentTransaction, incomingPaymentTransaction, sourceAccount, transactionRepository);
        }));

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

        if (StringUtils.isBlank(targetAccountId)) {
            failures.add(new ValidationFailure("Account id must be provided"));
        }

        if (transferAmount == null || transferAmount.getAmount() == null || transferAmount.getCurrencyType() == null ||
                transferAmount.getAmount().compareTo(MonetaryAmount.ZERO_GBP.getAmount()) <= 0) {
            failures.add(new ValidationFailure("Monetary amount not valid"));
        }


        if (!failures.isEmpty()) {
            throw new ValidationException(failures);
        }
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
        Account targetAccount = accountRepository.getAccountByAccountId(handle, targetAccountId)
                .orElseThrow(() -> new ValidationException(new ValidationFailure("Target account id does not exist")));

        if (targetAccount.getAccountHolderId().equals(accountHolderId)) {
            throw new ValidationException(new ValidationFailure("Cannot perform transfer for same account"));
        }
        return targetAccount;
    }

    private void validateAmountForTransfer(Account sourceAccount) {
        if (sourceAccount.getAvailableBalance().isLessThan(transferAmount)) {
            throw new ValidationException(new ValidationFailure("Unsufficient funds for transfer"));
        }
    }

    private MonetaryAmount getCurrentValueForTargetAccount(Account targetAccount) {
        if (!transferAmount.getCurrencyType().equals(targetAccount.getCurrencyType())) {
            return currencyExchangeService.exchange(transferAmount, targetAccount.getCurrencyType());
        }

        return transferAmount;
    }

}

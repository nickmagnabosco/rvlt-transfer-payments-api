package revolut.transfer.domain.commands;

import com.google.common.collect.Lists;
import lombok.Value;
import lombok.experimental.NonFinal;
import revolut.transfer.domain.exceptions.ValidationException;
import revolut.transfer.domain.exceptions.ValidationFailure;
import revolut.transfer.domain.models.MonetaryAmount;
import revolut.transfer.domain.models.accounts.Account;
import revolut.transfer.domain.models.transactions.Transaction;
import revolut.transfer.domain.models.transactions.TransactionStatus;
import revolut.transfer.domain.models.transactions.TransactionType;
import revolut.transfer.domain.models.transfers.TransferPayment;
import revolut.transfer.domain.models.transfers.TransferStatus;
import revolut.transfer.domain.models.transfers.TransferType;
import revolut.transfer.domain.repositories.AccountRepository;
import revolut.transfer.domain.repositories.TransactionFactory;
import revolut.transfer.domain.repositories.TransactionRepository;
import spark.utils.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Value
@NonFinal
public class CreateDepositCommand {
    public static final String DEPOSIT_REFERENCE = "DEPOSIT";

    private final String id;
    private final String accountHolderId;
    private final String requestId;
    private final String sourceAccountId;
    private final String accountId;
    private final MonetaryAmount depositAmount;
    private final TransactionRepository transactionRepository;
    private final TransactionFactory transactionFactory;
    private final AccountRepository accountRepository;

    public Transaction execute() {
        validate();
        return transactionFactory.openHandle().inTransaction(h -> {
            Transaction transaction = new Transaction(
                    id,
                    requestId,
                    accountId,
                    TransactionStatus.IN_PROGRESS,
                    TransactionType.DEPOSIT,
                    depositAmount,
                    LocalDateTime.now()
            );
            transactionRepository.createTransaction(h, transaction);
            h.commit();
            return transaction;
        });

    }

    public void validate() {
        List<ValidationFailure> failures = Lists.newArrayList();
        if (StringUtils.isBlank(requestId)) {
            failures.add(new ValidationFailure("Request id must be provided"));
        }

        if (StringUtils.isBlank(sourceAccountId) || StringUtils.isBlank(accountId)) {
            failures.add(new ValidationFailure("Source account and target account id must be provided"));
        } else if (!sourceAccountId.equals(accountId)) {
            failures.add(new ValidationFailure("Source account and target account id must be equal for deposit"));
        }

        if (StringUtils.isBlank(accountId)) {
            failures.add(new ValidationFailure("Account id must be provided"));
        }

        if (depositAmount == null || depositAmount.getAmount() == null || depositAmount.getCurrencyType() == null ||
                depositAmount.getAmount().compareTo(MonetaryAmount.ZERO_GBP.getAmount()) <= 0) {
            failures.add(new ValidationFailure("Monetary amount not valid"));
        }


        if (!failures.isEmpty()) {
            throw new ValidationException(failures);
        }

        Optional<Account> accountByAccountId = accountRepository.getAccountByAccountId(accountId);
        if (!accountByAccountId.isPresent()) {
            throw new ValidationException(new ValidationFailure("Account id does not exist"));
        } else if (!accountByAccountId.get().getCurrencyType().equals(depositAmount.getCurrencyType())) {
            throw new ValidationException(new ValidationFailure("Currency not supported for given account"));
        } else if (!accountByAccountId.get().getAccountHolderId().equals(accountHolderId)) {
            throw new ValidationException(new ValidationFailure("Cannot perform deposit for given account"));
        }

        if (transactionRepository.getTransactionByRequestId(requestId).isPresent()) {
            throw new ValidationException(new ValidationFailure("Transaction with given request id already processed"));
        }
    }
}

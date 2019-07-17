package revolut.transfer.domain.models.accounts;

import lombok.*;
import lombok.experimental.NonFinal;
import revolut.transfer.domain.models.MonetaryAmount;
import revolut.transfer.domain.models.currency.CurrencyType;
import revolut.transfer.domain.models.transactions.Transaction;
import revolut.transfer.domain.models.transactions.TransactionStatus;
import revolut.transfer.domain.repositories.TransactionRepository;

import java.util.List;

@Value
@NonFinal
public class Account {

    private final String id;
    private final String accountHolderId;
    private final AccountType accountType;
    private final BankAccountDetails bankAccountDetails;

    private final CurrencyType currencyType;
    private final TransactionRepository transactionRepository;

    public Account(String id, String accountHolderId, AccountType accountType, BankAccountDetails bankAccountDetails, CurrencyType currencyType,
            TransactionRepository transactionRepository) {
        this.id = id;
        this.accountHolderId = accountHolderId;
        this.accountType = accountType;
        this.bankAccountDetails = bankAccountDetails;
        this.currencyType = currencyType;
        this.transactionRepository = transactionRepository;
    }

    public MonetaryAmount getBalance() {
        return this.getTransactions().stream()
                .filter(transaction -> !transaction.getStatus().equals(TransactionStatus.FAILED))
                .map(transaction -> transaction.getType().isNegativeOperation() ? transaction.getAmount().negate() : transaction.getAmount())
                .reduce(accountType.getInitialAmount(), MonetaryAmount::add);
    }

    public MonetaryAmount getAvailableBalance() {
        return this.getTransactions().stream()
                .filter(transaction -> !transaction.getStatus().equals(TransactionStatus.FAILED))
                .filter(transaction -> !transaction.getStatus().equals(TransactionStatus.IN_PROGRESS))
                .map(transaction -> transaction.getType().isNegativeOperation() ? transaction.getAmount().negate() : transaction.getAmount())
                .reduce(accountType.getInitialAmount(), MonetaryAmount::add);
    }

    public List<Transaction> getTransactions() {
       return this.transactionRepository.getAllTransactionsByAccountId(id);
    }

}

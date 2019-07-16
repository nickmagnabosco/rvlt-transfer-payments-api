package revolut.transfer.domain.models.accounts;

import com.google.common.collect.Lists;
import lombok.*;
import lombok.experimental.NonFinal;
import revolut.transfer.domain.models.MonetaryAmount;
import revolut.transfer.domain.models.currency.CurrencyType;
import revolut.transfer.domain.models.transactions.Transaction;
import revolut.transfer.domain.models.transactions.TransactionStatus;
import revolut.transfer.domain.repositories.TransactionRepository;

import java.util.List;
import java.util.Map;

@Value
@NonFinal
public class Account {

    private final String id;
    private final String accountHolderId;
    private final AccountType accountType;
    private final BankAccountDetails bankAccountDetails;

    private final CurrencyType currencyType;
    private final TransactionRepository transactionRepository;
    private MonetaryAmount balance;
    private MonetaryAmount availableBalance;
    private List<Transaction> transactions;

    public Account(String id, String accountHolderId, AccountType accountType, BankAccountDetails bankAccountDetails, CurrencyType currencyType,
            TransactionRepository transactionRepository) {
        this.id = id;
        this.accountHolderId = accountHolderId;
        this.accountType = accountType;
        this.bankAccountDetails = bankAccountDetails;
        this.currencyType = currencyType;
        this.transactionRepository = transactionRepository;
        this.balance = null;
        this.availableBalance = null;
        this.transactions = Lists.newArrayList();
    }

    public MonetaryAmount getBalance() {
        return this.getTransactions().stream()
                .map(transaction -> transaction.getType().isNegativeOperation() ? transaction.getAmount().negate() : transaction.getAmount())
                .reduce(accountType.getInitialAmount(), MonetaryAmount::add);
    }

    public MonetaryAmount getAvailableBalance() {
        return this.getTransactions().stream()
                .filter(transaction -> transaction.getStatus().equals(TransactionStatus.COMPLETED))
                .map(transaction -> transaction.getType().isNegativeOperation() ? transaction.getAmount().negate() : transaction.getAmount())
                .reduce(accountType.getInitialAmount(), MonetaryAmount::add);
    }

    public List<Transaction> getTransactions() {
       return this.transactionRepository.getAllTransactionsByAccountId(id);
    }

}

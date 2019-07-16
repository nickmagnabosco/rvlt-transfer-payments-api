package revolut.transfer.domain.models.accounts;

import com.google.common.collect.Lists;
import lombok.*;
import lombok.experimental.NonFinal;
import org.jdbi.v3.core.transaction.TransactionIsolationLevel;
import revolut.transfer.domain.models.MonetaryAmount;
import revolut.transfer.domain.models.currency.CurrencyType;
import revolut.transfer.domain.repositories.TransactionRepository;
import revolut.transfer.integration.dto.Transaction;

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

    public MonetaryAmount balance() {
//        this.transactionRepository.getAllTransactionsByAccountId(id).stream().reduce()
        return null;
    }

//    public Account depositAmount(MonetaryAmount amount) {
//        return new Account(
//                id,
//                accountHolderId,
//                accountType,
//                bankAccountDetails,
//                currencyType,
//                balance,
//                balance.add(amount));
//    }

//    public Account withdrawAmount(MonetaryAmount amount) {
//        return new Account(
//                id,
//                accountHolderId,
//                accountType,
//                bankAccountDetails,
//                currencyType,
//                balance.subtract(amount));
//    }
}

package revolut.transfer.domain.commands;

import lombok.Value;
import lombok.experimental.NonFinal;
import revolut.transfer.domain.exceptions.ValidationException;
import revolut.transfer.domain.exceptions.ValidationFailure;
import revolut.transfer.domain.models.accounts.Account;
import revolut.transfer.domain.models.accounts.AccountFactory;
import revolut.transfer.domain.models.accounts.AccountType;
import revolut.transfer.domain.models.accounts.BankAccountDetails;
import revolut.transfer.domain.repositories.AccountRepository;
import revolut.transfer.domain.repositories.BankDetailsRepository;
import revolut.transfer.domain.repositories.TransactionFactory;
import revolut.transfer.domain.service.BankAccountFactory;

@Value
@NonFinal
public class CreateAccountCommand {

    private final String id;
    private final String accountHolderId;
    private final AccountType accountType;
    private final AccountRepository accountRepository;
    private final AccountFactory accountFactory;
    private final BankDetailsRepository bankDetailsRepository;
    private final BankAccountFactory bankAccountFactory;
    private final TransactionFactory transactionFactory;

    public Account execute() {
        validate();
        BankAccountDetails bankAccountDetails = bankAccountFactory.createBankAccountDetails(id, accountType);
        Account account = accountFactory.createAccount(id, accountHolderId, accountType, bankAccountDetails);
        transactionFactory.useTransaction(h -> {
            accountRepository.createAccount(h, account);
            bankDetailsRepository.createBankDetails(h, id, bankAccountDetails);
        });

        return account;
    }

    public void validate() {
        Account account = accountRepository.getAllAccountsByHolderId(accountHolderId)
                .stream()
                .filter(a -> a.getAccountType().equals(accountType))
                .findFirst()
                .orElse(null);

        if (account != null) {
            throw new ValidationException(new ValidationFailure(String.format("Cannot add multiple account with type %s", accountType)));
        }
    }
}

package revolut.transfer.domain.service;

import revolut.transfer.domain.models.accounts.AccountType;
import revolut.transfer.domain.models.accounts.BankAccountDetails;

public interface BankAccountFactory {
    BankAccountDetails createBankAccountDetails(String accountId, AccountType accountType);
}

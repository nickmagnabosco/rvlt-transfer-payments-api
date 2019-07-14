package revolut.transfer.domain.service;

import revolut.transfer.domain.models.accounts.AccountType;
import revolut.transfer.domain.models.accounts.BankAccountDetails;

public interface BankAccountService {
    BankAccountDetails createBankAccountDetails(AccountType accountType);
}

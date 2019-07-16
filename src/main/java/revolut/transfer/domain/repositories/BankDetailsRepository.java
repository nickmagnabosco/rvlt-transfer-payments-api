package revolut.transfer.domain.repositories;

import revolut.transfer.domain.models.accounts.BankAccountDetails;

public interface BankDetailsRepository {
    String createBankDetails(String accountId, BankAccountDetails bankAccountDetails);
    BankAccountDetails getBankDetailsByAccountId(String accountId);
}

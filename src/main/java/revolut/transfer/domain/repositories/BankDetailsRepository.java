package revolut.transfer.domain.repositories;

import org.jdbi.v3.core.Handle;
import revolut.transfer.domain.models.accounts.BankAccountDetails;

public interface BankDetailsRepository {
    String createBankDetails(Handle handle, String accountId, BankAccountDetails bankAccountDetails);
    BankAccountDetails getBankDetailsByAccountId(String accountId);
    BankAccountDetails getBankDetailsByAccountId(Handle handle, String accountId);
}

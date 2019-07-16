package revolut.transfer.integration.repositories;

import revolut.transfer.domain.models.accounts.BankAccountDetails;
import revolut.transfer.domain.repositories.BankDetailsRepository;
import revolut.transfer.integration.mappers.BankDetailsMapper;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BankDetailsRepositoryImpl implements BankDetailsRepository {
    private final JDBIProvider jdbiProvider;
    private final BankDetailsMapper bankDetailsMapper;

    @Inject
    public BankDetailsRepositoryImpl(JDBIProvider jdbiProvider, BankDetailsMapper bankDetailsMapper) {
        this.jdbiProvider = jdbiProvider;
        this.bankDetailsMapper = bankDetailsMapper;
    }

    @Override
    public String createBankDetails(String accountId, BankAccountDetails bankAccountDetails) {
        jdbiProvider.getJdbi().useHandle(handle -> handle.createUpdate("INSERT INTO BANK_DETAILS (id, account_id, iban, bic, sort_code, account_number) "
                    + "VALUES (:id, :accountId, :iban, :bic, :sortCode, :accountNumber)")
                    .bind("id", bankAccountDetails.getId())
                    .bind("accountId", accountId)
                    .bind("iban", bankAccountDetails.getIban())
                    .bind("bic", bankAccountDetails.getBic())
                    .bind("sortCode", bankAccountDetails.getSortCode())
                    .bind("accountNumber", bankAccountDetails.getAccountNumber())
                    .execute()
        );
        return bankAccountDetails.getId();
    }

    @Override
    public BankAccountDetails getBankDetailsByAccountId(String accountId) {
        return jdbiProvider.getJdbi().withHandle(handle -> handle.createQuery(
                "SELECT id, account_id, iban, bic, sort_code, account_number "
                        + "FROM BANK_DETAILS "
                        + "WHERE account_id=:accountId")
                .bind("accountId", accountId)
                .map(bankDetailsMapper)
                .findOnly());
    }
}

package revolut.transfer.integration.mappers;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import revolut.transfer.domain.models.accounts.Account;
import revolut.transfer.domain.models.accounts.AccountType;
import revolut.transfer.domain.models.currency.CurrencyType;
import revolut.transfer.domain.repositories.BankDetailsRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;

@Singleton
public class AccountMapper implements RowMapper<Account> {
    private final BankDetailsRepository bankDetailsRepository;

    @Inject
    public AccountMapper(BankDetailsRepository bankDetailsRepository) {
        this.bankDetailsRepository = bankDetailsRepository;
    }

    @Override
    public Account map(ResultSet rs, StatementContext ctx) throws SQLException {
        String accountId = rs.getString("id");
        AccountType accountType = AccountType.valueOf(rs.getString("account_type"));
        return new Account(
                accountId,
                rs.getString("account_holder_id"),
                accountType,
                bankDetailsRepository.getBankDetailsByAccountId(accountId),
                CurrencyType.valueOf(rs.getString("currency_type")),
                accountType.getInitialAmount(),
                accountType.getInitialAmount()
        );
    }
}

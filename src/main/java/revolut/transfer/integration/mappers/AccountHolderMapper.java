package revolut.transfer.integration.mappers;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import revolut.transfer.domain.models.accounts.Account;
import revolut.transfer.domain.models.accounts.AccountHolder;
import revolut.transfer.domain.models.accounts.UserTitle;
import revolut.transfer.domain.repositories.AccountRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Singleton
public class AccountHolderMapper implements RowMapper<AccountHolder> {
    private final AccountRepository accountRepository;

    @Inject
    public AccountHolderMapper(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountHolder map(ResultSet rs, StatementContext ctx) throws SQLException {
        String accountHolderId = rs.getString("id");
        List<Account> accounts = accountRepository.getAllAccountsByHolderId(accountHolderId);
        return new AccountHolder(
                rs.getString("id"),
                UserTitle.valueOf(rs.getString("title")),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email_address"),
                accounts
        );
    }
}

package revolut.transfer.integration.mappers;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import revolut.transfer.domain.models.accounts.BankAccountDetails;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;

@Singleton
public class BankDetailsMapper implements RowMapper<BankAccountDetails> {

    @Inject
    public BankDetailsMapper() {
    }

    @Override
    public BankAccountDetails map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new BankAccountDetails(
                rs.getString("id"),
                rs.getString("account_id"),
                rs.getString("iban"),
                rs.getString("bic"),
                rs.getString("sort_code"),
                rs.getString("account_number")
        );
    }
}

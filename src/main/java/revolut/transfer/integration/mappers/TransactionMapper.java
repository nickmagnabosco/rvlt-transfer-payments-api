package revolut.transfer.integration.mappers;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import revolut.transfer.domain.models.MonetaryAmount;
import revolut.transfer.domain.models.currency.CurrencyType;
import revolut.transfer.domain.models.transactions.Transaction;
import revolut.transfer.domain.models.transactions.TransactionStatus;
import revolut.transfer.domain.models.transactions.TransactionType;
import revolut.transfer.domain.utility.DateTimeUtility;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;

@Singleton
public class TransactionMapper implements RowMapper<Transaction> {

    @Inject
    public TransactionMapper() {
    }

    @Override
    public Transaction map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Transaction(
                rs.getString("id"),
                rs.getString("request_id"),
                rs.getString("account_id"),
                TransactionStatus.valueOf(rs.getString("status")),
                TransactionType.valueOf(rs.getString("type")),
                new MonetaryAmount(
                        rs.getBigDecimal("amount_value"),
                        CurrencyType.valueOf(rs.getString("amount_currency_type"))),
                DateTimeUtility.fromTimestamp(rs.getTimestamp("created_datetime")));
    }
}

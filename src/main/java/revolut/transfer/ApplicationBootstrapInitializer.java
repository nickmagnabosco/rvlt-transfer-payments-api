package revolut.transfer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.Flyway;
import revolut.transfer.domain.exceptions.ResourceNotFoundException;
import revolut.transfer.domain.exceptions.ValidationException;
import revolut.transfer.integration.adapters.*;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import static spark.Spark.*;
import static spark.Spark.exception;

@Singleton
public class ApplicationBootstrapInitializer {
    private final TransferAdapter transferAdapter;
    private final QuoteAdapter quoteAdapter;
    private final AccountHolderAdapter accountHolderAdapter;
    private final String databaseUrl;
    private final String databasePassword;
    private final String databaseUser;

    @Inject
    public ApplicationBootstrapInitializer(
            TransferAdapter transferAdapter,
            QuoteAdapter quoteAdapter,
            AccountHolderAdapter accountHolderAdapter,
            @Named("database.url") String databaseUrl,
            @Named("database.password") String databasePassword,
            @Named("database.user") String databaseUser)
    {
        this.transferAdapter = transferAdapter;
        this.quoteAdapter = quoteAdapter;
        this.accountHolderAdapter = accountHolderAdapter;
        this.databasePassword = databasePassword;
        this.databaseUser = databaseUser;
        this.databaseUrl = databaseUrl;
    }

    public void initialize() {
        Flyway flyway = Flyway.configure().dataSource("jdbc:h2:mem:test;INIT=create schema if not exists test;DB_CLOSE_DELAY=-1;", "sa", "").load();
        flyway.migrate();

        defaultResponseTransformer(new JsonTransformer());

        exception(ResourceNotFoundException.class, ((e, request, response) -> {
            response.status(404);
        }));

        exception(ValidationException.class, ((e, request, response) -> {
            response.status(400);
            try {
                response.body(new ObjectMapper().writeValueAsString(e.getFailures()));
            } catch (JsonProcessingException e1) {
                e1.printStackTrace();
            }
        }));

        transferAdapter.initialize();
        quoteAdapter.initialize();
        accountHolderAdapter.initialize();

        awaitInitialization();
    }
}

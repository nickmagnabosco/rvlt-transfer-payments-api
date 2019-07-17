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
    private final AccountAdapter accountHolderAdapter;
    private final String databaseUrl;
    private final String databasePassword;
    private final String databaseUser;

    @Inject
    public ApplicationBootstrapInitializer(
            TransferAdapter transferAdapter,
            AccountAdapter accountHolderAdapter,
            @Named("database.url") String databaseUrl,
            @Named("database.password") String databasePassword,
            @Named("database.user") String databaseUser)
    {
        this.transferAdapter = transferAdapter;
        this.accountHolderAdapter = accountHolderAdapter;
        this.databasePassword = databasePassword;
        this.databaseUser = databaseUser;
        this.databaseUrl = databaseUrl;
    }

    public void initialize() {
        Flyway flyway = Flyway.configure().dataSource(databaseUrl, databaseUser, databasePassword).load();
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
                throw new ValidationException();
            }
        }));

        transferAdapter.initialize();
        accountHolderAdapter.initialize();

        awaitInitialization();
    }
}

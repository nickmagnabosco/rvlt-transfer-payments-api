package revolut.transfer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.internal.codegen.DaggerComponentProcessor_ProcessorComponent;
import revolut.transfer.domain.exceptions.ResourceNotFoundException;
import revolut.transfer.domain.exceptions.ValidationException;
import revolut.transfer.integration.adapters.*;

import javax.inject.Inject;
import javax.inject.Singleton;

import static spark.Spark.*;
import static spark.Spark.exception;

@Singleton
public class ApplicationBootstrapInitializer {
    private final TransferAdapter transferAdapter;
    private final AccountAdapter accountAdapter;
    private final QuoteAdapter quoteAdapter;
    private final AccountHolderAdapter accountHolderAdapter;

    @Inject
    public ApplicationBootstrapInitializer(TransferAdapter transferAdapter, AccountAdapter accountAdapter, QuoteAdapter quoteAdapter,
            AccountHolderAdapter accountHolderAdapter)
    {
        this.transferAdapter = transferAdapter;
        this.accountAdapter = accountAdapter;
        this.quoteAdapter = quoteAdapter;
        this.accountHolderAdapter = accountHolderAdapter;
    }

    public void initialize() {
//        port(8080);
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
        accountAdapter.initialize();
        quoteAdapter.initialize();
        accountHolderAdapter.initialize();

        awaitInitialization();
    }
}

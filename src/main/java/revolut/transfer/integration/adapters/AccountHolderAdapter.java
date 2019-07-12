package revolut.transfer.integration.adapters;

import revolut.transfer.domain.accounts.AccountHolder;
import revolut.transfer.domain.repositories.AccountHolderRepository;
import revolut.transfer.integration.dto.command.CreateAccountHolder;
import revolut.transfer.integration.repositories.StubAccountHolderRepositoryImpl;

import java.util.UUID;

import static spark.Spark.*;

public class AccountHolderAdapter extends Adapter {

    AccountHolderRepository accountHolderAdapter = new StubAccountHolderRepositoryImpl();

    public void initialize() {

        get("/accountHolders", (req, response) -> {
            return accountHolderAdapter.getAllAccountHolders();
        }, jsonTransformer);

        get("/accountHolders/:holderId", (req, response) -> accountHolderAdapter.getAccountHolderById(req.params("holderId")), jsonTransformer);

        post("/accountHolders", (req, response) -> {
            CreateAccountHolder createAccountHolder = objectMapper.readValue(req.body(), CreateAccountHolder.class);
            accountHolderAdapter.createAccountHolder(new AccountHolder(UUID.randomUUID().toString(), createAccountHolder.getTitle(), createAccountHolder.getFirstName(), createAccountHolder.getLastName(), createAccountHolder.getEmailAddress()));
            return "";
        });
    }
}

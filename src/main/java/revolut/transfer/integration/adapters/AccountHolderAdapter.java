package revolut.transfer.integration.adapters;

import revolut.transfer.domain.repositories.AccountHolderRepository;
import revolut.transfer.integration.dto.command.CreateAccountHolder;
import revolut.transfer.integration.service.AccountHolderService;

import javax.inject.Inject;
import javax.inject.Singleton;

import static spark.Spark.*;

@Singleton
public class AccountHolderAdapter extends Adapter {

    private final AccountHolderService accountHolderService;

    @Inject
    public AccountHolderAdapter(AccountHolderService accountHolderService) {
        this.accountHolderService = accountHolderService;
    }

    public void initialize() {

//        get("/accountHolders", (req, response) -> {
//            return accountHolderAdapter.getAllAccountHolders();
//        }, jsonTransformer);

        get("/accountHolders/:holderId", (req, response) -> accountHolderService.getAccountHolderById(req.params("holderId")), jsonTransformer);

//        get("/accountHolders/:holderId/accounts", (req, response) -> accountHolderAdapter.getAccountHolderById(req.params("holderId")), jsonTransformer);
//
//        get("/accountHolders/:holderId/accounts/:accountId", (req, response) -> accountHolderAdapter.getAccountHolderById(req.params("holderId")), jsonTransformer);

        post("/accountHolders", (req, response) -> {
            CreateAccountHolder createAccountHolder = objectMapper.readValue(req.body(), CreateAccountHolder.class);
//            accountHolderAdapter.createAccountHolder(new AccountHolder(UUID.randomUUID().toString(), createAccountHolder.getTitle(), createAccountHolder.getFirstName(), createAccountHolder.getLastName(), createAccountHolder.getEmailAddress()));
            return "";
        });

        get("/accountHolders/:holderId/transfers", (req, response) -> {
            return "";
        });

        get("/accountHolders/:holderId/transfers/:transferId", (req, response) -> {
            return "";
        });

        post("/accountHolders/:holderId/transfers", (req, response) -> {
            return "";
        });
    }
}

package revolut.transfer.integration.adapters;

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

        get("/accountHolders/:holderId", (req, response) -> accountHolderService.getAccountHolderDetailsById(req.params("holderId")), jsonTransformer);

//        get("/accountHolders/:holderId/accounts", (req, response) -> accountHolderAdapter.getAccountHolderDetailsById(req.params("holderId")), jsonTransformer);
//
//        get("/accountHolders/:holderId/accounts/:accountId", (req, response) -> accountHolderAdapter.getAccountHolderDetailsById(req.params("holderId")), jsonTransformer);

        post("/accountHolders", (req, response) -> accountHolderService.createAccountHolder(objectMapper.readValue(req.body(), CreateAccountHolder.class)));

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

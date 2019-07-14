package revolut.transfer.integration.adapters;

import revolut.transfer.integration.dto.command.CreateAccountCommand;
import revolut.transfer.integration.dto.command.CreateAccountHolder;
import revolut.transfer.integration.service.AccountService;

import javax.inject.Inject;
import javax.inject.Singleton;

import static spark.Spark.*;

@Singleton
public class AccountHolderAdapter extends Adapter {

    private final AccountService accountService;

    @Inject
    public AccountHolderAdapter(AccountService accountService) {
        this.accountService = accountService;
    }

    public void initialize() {

        get("/accountHolders/:holderId", (req, response) -> accountService.getAccountHolderDetailsByHolderId(req.params("holderId")), jsonTransformer);

        post("/accountHolders", (req, response) ->
                accountService.createAccountHolder(objectMapper.readValue(req.body(), CreateAccountHolder.class)),
                jsonTransformer);

        get("/accountHolders/:holderId/accounts", (req, response) -> accountService.getAccountsByHolderId(req.params("holderId")), jsonTransformer);

        get("/accountHolders/:holderId/accounts/:accountId", (req, response) ->
                        accountService.getAccountByAccountHolderAndAccountId(req.params("holderId"), req.params("accountId")),
                jsonTransformer);

        post("/accountHolders/:holderId/accounts", (req, response) ->
                accountService.createAccount(req.params("holderId"), objectMapper.readValue(req.body(), CreateAccountCommand.class)),
                jsonTransformer);

        get("/accountHolders/:holderId/transfers/:transferId", (req, response) -> {
            return "";
        });

        get("/accountHolders/:holderId/transfers", (req, response) -> {
            return "";
        });
    }
}

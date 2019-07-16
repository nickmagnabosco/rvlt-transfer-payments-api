package revolut.transfer.integration.adapters;

import revolut.transfer.integration.dto.command.CreateDeposit;
import revolut.transfer.integration.dto.command.CreateTransfer;
import revolut.transfer.integration.service.TransactionService;

import javax.inject.Inject;
import javax.inject.Singleton;

import static spark.Spark.get;
import static spark.Spark.post;

@Singleton
public class TransferAdapter extends Adapter {
    private final TransactionService transferService;
    @Inject
    public TransferAdapter(TransactionService transferService) {
        this.transferService = transferService;
    }

    public void initialize() {
        get("/accountHolders/:holderId/accounts/:accountId/transactions/:transactionId", (req, response) ->
                transferService.getTransactionByAccountIdAndTransactionId(req.params("holderId"), req.params("accountId"), req.params("transactionId")), jsonTransformer);

        get("/accountHolders/:holderId/accounts/:accountId/transactions", (req, response) ->
                transferService.getTransactionsByAccountId(req.params("holderId"), req.params("accountId")), jsonTransformer);

        post("/accountHolders/:holderId/accounts/:accountId/transfers", (req, response) ->
                transferService.createTransfer(req.params("holderId"), req.params("accountId"), objectMapper.readValue(req.body(), CreateTransfer.class)),
                jsonTransformer);

        post("/accountHolders/:holderId/accounts/:accountId/deposits", (req, response) ->
                transferService.createDeposit(req.params("holderId"), req.params("accountId"), objectMapper.readValue(req.body(), CreateDeposit.class)),
                        jsonTransformer);
    }
}

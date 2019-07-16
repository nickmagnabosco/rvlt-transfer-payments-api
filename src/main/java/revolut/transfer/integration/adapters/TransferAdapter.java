package revolut.transfer.integration.adapters;

import revolut.transfer.integration.dto.command.CreateDeposit;
import revolut.transfer.integration.service.TransferService;

import javax.inject.Inject;
import javax.inject.Singleton;

import static spark.Spark.get;
import static spark.Spark.post;

@Singleton
public class TransferAdapter extends Adapter {
    private final TransferService transferService;
    @Inject
    public TransferAdapter(TransferService transferService) {
        this.transferService = transferService;
    }

    public void initialize() {
        get("/accountHolders/:holderId/transfers/:transferId", (req, response) -> {
            return "";
        }, jsonTransformer);

        get("/accountHolders/:holderId/transfers", (req, response) -> {
            return "";
        }, jsonTransformer);

        post("/accountHolders/:holderId/transfers", (req, response) -> {
            return "";
        }, jsonTransformer);

        post("/accountHolders/:holderId/accounts/:accountId/deposit", (req, response) ->
                transferService.createDeposit(req.params("holderId"), req.params("accountId"), objectMapper.readValue(req.body(), CreateDeposit.class)),
                        jsonTransformer);
    }
}

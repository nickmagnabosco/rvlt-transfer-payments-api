package revolut.transfer.integration.adapters;

import javax.inject.Inject;
import javax.inject.Singleton;

import static spark.Spark.get;
import static spark.Spark.post;

@Singleton
public class TransferAdapter extends Adapter {

    @Inject
    public TransferAdapter() {
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
    }
}

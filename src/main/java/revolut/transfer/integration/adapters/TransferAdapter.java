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
        get("/transfers", (req, response) -> {
            return "Hello";
        });

        get("/transfers/:transferId", (req, response) -> {
            return req.params("transferId");
        });

        post("/transfers", (req, response) -> {
           return null;
        });
    }
}

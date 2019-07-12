package revolut.transfer.integration.adapters;

import static spark.Spark.get;
import static spark.Spark.post;

public class TransferAdapter extends Adapter {

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

package revolut.transfer.integration.adapters;

import spark.Spark;

import static spark.Spark.post;
import static spark.route.HttpMethod.get;

public class AccountAdapter extends Adapter {
    public void initialize() {
        Spark.get("/accounts", (req, response) -> {
            return "Hello";
        });

        Spark.get("/accounts/:accountId", (req, response) -> {
            return req.params("accountId");
        });

        post("/accounts", (req, response) -> {
            return null;
        });
    }
}

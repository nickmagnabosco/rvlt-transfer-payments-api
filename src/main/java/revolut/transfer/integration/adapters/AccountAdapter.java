package revolut.transfer.integration.adapters;

import spark.Spark;

import javax.inject.Inject;
import javax.inject.Singleton;

import static spark.Spark.post;
import static spark.route.HttpMethod.get;

@Singleton
public class AccountAdapter extends Adapter {

    @Inject
    public AccountAdapter() {
    }

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

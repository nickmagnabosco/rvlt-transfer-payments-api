package revolut.transfer.integration.adapters;

import static spark.Spark.get;
import static spark.Spark.post;

public class QuoteAdapter extends Adapter {
    public void initialize() {
        get("/quotes", (req, response) -> {
            return "Hello";
        });

        get("/quotes/:quoteId", (req, response) -> {
            return req.params("quoteId");
        });

        post("/quotes", (req, response) -> {
            return null;
        });
    }
}

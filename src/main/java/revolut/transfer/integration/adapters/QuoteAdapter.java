package revolut.transfer.integration.adapters;

import javax.inject.Inject;
import javax.inject.Singleton;

import static spark.Spark.get;
import static spark.Spark.post;

@Singleton
public class QuoteAdapter extends Adapter {

    @Inject
    public QuoteAdapter() {
    }

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

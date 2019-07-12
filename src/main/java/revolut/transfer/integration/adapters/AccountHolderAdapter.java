package revolut.transfer.integration.adapters;

import spark.Spark;

import static spark.Spark.post;

public class AccountHolderAdapter {
    public void initialize() {
        Spark.get("/accountHolders", (req, response) -> {
            return "Hello";
        });

        Spark.get("/accountHolders/:holderId", (req, response) -> {
            return req.params("holderId");
        });

        post("/accountHolders", (req, response) -> {
            return null;
        });
    }
}

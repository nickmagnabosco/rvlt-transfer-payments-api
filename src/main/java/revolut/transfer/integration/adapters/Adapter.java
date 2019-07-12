package revolut.transfer.integration.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;

import static spark.Spark.before;

public abstract class Adapter {
    protected ObjectMapper objectMapper;

    protected static String CONTENT_JSON = "application/json";

    protected JsonTransformer jsonTransformer;

    public Adapter() {
        objectMapper = new ObjectMapper();
        jsonTransformer = new JsonTransformer();

        before((request, response) -> response.type(CONTENT_JSON));
    }

    abstract void initialize();
}

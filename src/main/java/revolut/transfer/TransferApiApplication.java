package revolut.transfer;

import revolut.transfer.domain.exceptions.ResourceNotFoundException;
import revolut.transfer.integration.adapters.JsonTransformer;

import static spark.Spark.*;

public class TransferApiApplication {

    public static void main(String[] args) {
//        port(ResourceBundle.getBundle("configuration/local.properties").getString("application.port"));
        port(8080);
        defaultResponseTransformer(new JsonTransformer());
        new ApplicationBootstrapInitializer().initialize();
        exception(ResourceNotFoundException.class, ((e, request, response) -> {
            response.status(404);
        }));

        awaitInitialization();
    }
}

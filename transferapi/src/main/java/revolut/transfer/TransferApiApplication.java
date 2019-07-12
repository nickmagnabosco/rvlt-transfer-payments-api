package revolut.transfer;

import revolut.transfer.integration.adapters.TransferAdapter;

import java.util.ResourceBundle;

import static spark.Spark.awaitInitialization;
import static spark.Spark.port;

public class TransferApiApplication {

    public static void main(String[] args) {
//        port(ResourceBundle.getBundle("configuration/local.properties").getString("application.port"));
        port(8080);
        new TransferAdapter().initialize();
//        awaitInitialization();
    }
}

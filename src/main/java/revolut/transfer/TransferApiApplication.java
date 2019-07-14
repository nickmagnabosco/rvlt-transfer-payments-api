package revolut.transfer;

import spark.Service;
import spark.Spark;
import spark.servlet.SparkApplication;

import static spark.Spark.port;

public class TransferApiApplication {
    private ApplicationComponent applicationComponent;

    public void start() {
        applicationComponent = DaggerApplicationComponent.create();
        applicationComponent.applicationBootstrapInitializer().initialize();
    }

    public static void main(String[] args) {
//        port(ResourceBundle.getBundle("configuration/local.properties").getString("application.port"));
        port(8080);
        new TransferApiApplication().start();
        Spark.awaitInitialization();
    }
}

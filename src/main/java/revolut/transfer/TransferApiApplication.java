package revolut.transfer;

import spark.Spark;

import java.util.ResourceBundle;

import static spark.Spark.port;

public class TransferApiApplication {
    private ApplicationComponent applicationComponent;

    public void start() {
        applicationComponent = DaggerApplicationComponent.create();
        applicationComponent.applicationBootstrapInitializer().initialize();
    }

    public static void main(String[] args) {
        String applicationPortString = ResourceBundle.getBundle("local").getString("application.port");
        port(Integer.parseInt(applicationPortString));
        new TransferApiApplication().start();
        Spark.awaitInitialization();
    }
}

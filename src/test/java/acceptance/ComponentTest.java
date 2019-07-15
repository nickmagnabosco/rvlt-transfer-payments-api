package acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import revolut.transfer.TransferApiApplication;
import revolut.transfer.integration.repositories.StubAccountHolderRepositoryImpl;
import revolut.transfer.integration.repositories.StubAccountRepositoryImpl;

import static spark.Spark.*;

public class ComponentTest {
    protected final static String BASE_URL = "http://localhost";
    protected final static int PORT = 4567;
    protected final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeClass
    public static void setup() {
//        port(PORT);
        new TransferApiApplication().start();
        awaitInitialization();
    }

    @Before
    public void clearDb() {
        StubAccountRepositoryImpl.accounts.clear();
        StubAccountHolderRepositoryImpl.accountHolders.clear();
    }

    @AfterClass
    public static void tearDown() {
        stop();
        awaitStop();
    }

    public static String getFullUrl(String url) {
        return String.format("%s:%s%s", BASE_URL, PORT, url);
    }
}

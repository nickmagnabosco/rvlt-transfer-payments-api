package acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import revolut.transfer.TransferApiApplication;
import spark.utils.StringUtils;

import java.io.IOException;

import static spark.Spark.*;

public class ComponentTest {
    protected HttpClient httpClient = HttpClientBuilder.create().build();
    protected final static String BASE_URL = "http://localhost";
    protected final static int PORT = 4567;
    protected final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setup() {
        port(PORT);
        new TransferApiApplication().start();
        awaitInitialization();
    }

    @After
    public void tearDown() {
        stop();
    }

    public static String getFullUrl(String url) {
        return String.format("%s:%s%s", BASE_URL, PORT, url);
    }
}

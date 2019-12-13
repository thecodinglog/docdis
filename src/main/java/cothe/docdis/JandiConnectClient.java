package cothe.docdis;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JandiConnectClient {
    private static Logger logger = LogManager.getLogger(JandiConnectClient.class);
    private final String uri;

    public JandiConnectClient(String uri) {
        this.uri = uri;
    }

    public void sendMessage(String message) {
        try {
            HttpPost post = new HttpPost(uri);

            post.setHeader("Accept", "application/vnd.tosslab.jandi-v2+json");
            post.setHeader("Content-Type", "application/json");

            String json = "{\n" +
                    "  \"body\" : \"" + message + "\"\n" +
                    "}";
            StringEntity stringEntity = new StringEntity(json);

            post.setEntity(stringEntity);

            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(post)) {
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}

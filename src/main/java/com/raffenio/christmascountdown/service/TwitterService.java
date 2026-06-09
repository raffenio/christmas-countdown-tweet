package com.raffenio.christmascountdown.service;

import com.raffenio.christmascountdown.config.TwitterProperties;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.v1.User;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TwitterService {

    private static final Logger log = LoggerFactory.getLogger(TwitterService.class);
    private static final String TWEET_V2_URL = "https://api.twitter.com/2/tweets";

    private final Twitter twitter;
    private final TwitterProperties props;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Value("${twitter.test-tweet.enabled:false}")
    private boolean testTweetEnabled;

    public TwitterService(Twitter twitter, TwitterProperties props) {
        this.twitter = twitter;
        this.props = props;
    }

    @PostConstruct
    public void verifyConnection() {
        try {
            User user = twitter.v1().users().verifyCredentials();
            log.info("Conexión con Twitter OK. Cuenta autenticada: @{} ({})", user.getScreenName(), user.getName());
        } catch (TwitterException e) {
            log.error("Error al verificar credenciales de Twitter: {} (código HTTP {})", e.getMessage(), e.getStatusCode());
            log.error("Revisá las keys en application.properties");
            return;
        }

        if (testTweetEnabled) {
            sendTestTweet();
        }
    }

    public void sendTestTweet() {
        log.info("Enviando tweet de prueba...");
        postTweet("Tweet de prueba - integracion con Twitter API funcionando correctamente.");
    }

    public void postTweet(String message) {
        try {
            String body = "{\"text\":\"" + escapeJson(message) + "\"}";
            String authHeader = buildOAuth1Header("POST", TWEET_V2_URL);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(TWEET_V2_URL))
                    .header("Authorization", authHeader)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                log.info("Tweet publicado correctamente. Respuesta: {}", response.body());
            } else {
                log.error("Error al publicar el tweet. Código {}: {}", response.statusCode(), response.body());
            }
        } catch (Exception e) {
            log.error("Error al publicar el tweet: {}", e.getMessage(), e);
        }
    }

    private String buildOAuth1Header(String method, String url) throws Exception {
        String nonce = UUID.randomUUID().toString().replace("-", "");
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);

        Map<String, String> params = new TreeMap<>();
        params.put("oauth_consumer_key", props.getApiKey());
        params.put("oauth_nonce", nonce);
        params.put("oauth_signature_method", "HMAC-SHA1");
        params.put("oauth_timestamp", timestamp);
        params.put("oauth_token", props.getAccessToken());
        params.put("oauth_version", "1.0");

        String paramString = params.entrySet().stream()
                .map(e -> encode(e.getKey()) + "=" + encode(e.getValue()))
                .collect(Collectors.joining("&"));

        String baseString = method + "&" + encode(url) + "&" + encode(paramString);
        String signingKey = encode(props.getApiSecret()) + "&" + encode(props.getAccessTokenSecret());

        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(signingKey.getBytes(StandardCharsets.UTF_8), "HmacSHA1"));
        String signature = Base64.getEncoder().encodeToString(
                mac.doFinal(baseString.getBytes(StandardCharsets.UTF_8)));

        params.put("oauth_signature", signature);

        return "OAuth " + params.entrySet().stream()
                .filter(e -> e.getKey().startsWith("oauth_"))
                .map(e -> encode(e.getKey()) + "=\"" + encode(e.getValue()) + "\"")
                .collect(Collectors.joining(", "));
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8)
                .replace("+", "%20")
                .replace("*", "%2A")
                .replace("%7E", "~");
    }

    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}

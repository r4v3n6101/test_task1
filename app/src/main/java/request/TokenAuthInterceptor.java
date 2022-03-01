package request;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

public class TokenAuthInterceptor implements RequestInterceptor {

    private static final String TOKEN_HEADER = "X-App-Token";
    private static final String SIGNATURE_HEADER = "X-App-Access-Sig";
    private static final String TIMESTAMP_HEADER = "X-App-Access-Ts";

    private final String appToken;
    private final String secret;

    public TokenAuthInterceptor(String appToken, String secret) {
        this.appToken = appToken;
        this.secret = secret;
    }

    @Override
    public void apply(RequestTemplate template) {
        long timestamp = Instant.now().getEpochSecond();
        try {
            template.header(TOKEN_HEADER, appToken);
            template.header(SIGNATURE_HEADER, hmacSign(timestamp, template.method(), template.url(), template.body()));
            template.header(TIMESTAMP_HEADER, String.valueOf(timestamp));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    private String hmacSign(long timestamp, String method, String url, byte[] body) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmac = Mac.getInstance("HmacSHA256");
        hmac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        hmac.update((timestamp + method + url).getBytes(StandardCharsets.UTF_8));
        byte[] result = body != null ? hmac.doFinal(body) : hmac.doFinal();
        return Hex.encodeHexString(result);
    }
}

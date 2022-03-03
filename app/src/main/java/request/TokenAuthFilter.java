package request;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.core.GenericType;
import org.apache.commons.codec.binary.Hex;
import org.glassfish.jersey.client.ClientRequest;
import org.glassfish.jersey.internal.PropertiesDelegate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

public class TokenAuthFilter implements ClientRequestFilter {

    private final String appToken;
    private final String secret;
    private static final String TOKEN_HEADER = "X-App-Token";
    private static final String SIGNATURE_HEADER = "X-App-Access-Sig";
    private static final String TIMESTAMP_HEADER = "X-App-Access-Ts";

    public TokenAuthFilter(String appToken, String secret) {
        this.appToken = appToken;
        this.secret = secret;
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        long timestamp = Instant.now().getEpochSecond();
        requestContext.getHeaders().putSingle(TIMESTAMP_HEADER, timestamp);
        requestContext.getHeaders().putSingle(TOKEN_HEADER, appToken);

        byte[] body = null;
        if (requestContext.getEntity() != null && requestContext instanceof ClientRequest) {
            body = writeBody((ClientRequest) requestContext);
        }
        try {
            String signature = hmacSign(timestamp, requestContext.getMethod(), requestContext.getUri().toURL().getFile(), body);
            requestContext.getHeaders().putSingle(SIGNATURE_HEADER, signature);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    private byte[] writeBody(ClientRequest request) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GenericType<?> entityType = new GenericType<>(request.getEntityType());
        request.getWorkers().writeTo(
                request.getEntity(),
                entityType.getRawType(),
                entityType.getType(),
                request.getEntityAnnotations(),
                request.getMediaType(),
                request.getHeaders(),
                new PropertiesDelegate() {
                    @Override
                    public Object getProperty(String name) {
                        return null;
                    }

                    @Override
                    public Collection<String> getPropertyNames() {
                        return null;
                    }

                    @Override
                    public void setProperty(String name, Object object) {

                    }

                    @Override
                    public void removeProperty(String name) {

                    }
                },
                baos,
                List.of()
        );
        return baos.toByteArray();
    }

    private String hmacSign(long timestamp, String method, String url, byte[] body) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmac = Mac.getInstance("HmacSHA256");
        hmac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        hmac.update((timestamp + method + url).getBytes(StandardCharsets.UTF_8));
        byte[] result = body != null ? hmac.doFinal(body) : hmac.doFinal();
        return Hex.encodeHexString(result);
    }
}

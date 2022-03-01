import feign.Feign;
import request.SumSubApi;
import request.TokenAuthInterceptor;

public class Main {
    public static void main(String[] args) {
        String appToken = System.getenv("APP_TOKEN");
        String secret = System.getenv("SECRET");

        SumSubApi api = Feign.builder()
                .requestInterceptor(new TokenAuthInterceptor(appToken, secret))
                .target(SumSubApi.class, "https://test-api.sumsub.com");
        System.out.println(api.health());
    }
}

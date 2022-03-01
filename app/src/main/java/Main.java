import feign.Feign;
import feign.Logger;
import feign.form.FormEncoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import request.TokenAuthInterceptor;
import request.api.SumSub;
import request.data.Applicant;
import request.data.ApplicantStatus;
import request.data.IdData;
import request.data.IdMetadata;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws IOException {
        String appToken = System.getenv("APP_TOKEN");
        String secret = System.getenv("SECRET");
        String testImage = System.getenv("TEST_IMAGE");
        String defaultLevel = "basic-kyc-level"; // from docs

        SumSub api = Feign.builder()
                .encoder(new FormEncoder(new JacksonEncoder()))
                .decoder(new JacksonDecoder())
                .requestInterceptor(new TokenAuthInterceptor(appToken, secret))
                .logLevel(Logger.Level.FULL)
                .target(SumSub.class, "https://test-api.sumsub.com");

        Applicant applicant = api.createApplicant(defaultLevel, UUID.randomUUID().toString());
        IdMetadata metadata = new IdMetadata(IdMetadata.DocType.PASSPORT, "RUS");
        byte[] imageContent = Files.readAllBytes(Path.of(testImage));
        api.addIdDoc(applicant.getId(), new IdData(metadata, imageContent));
        ApplicantStatus status = api.getStatus(applicant.getId());
        System.out.println(status.getReviewStatus());
    }
}

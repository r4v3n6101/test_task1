import feign.Feign;
import feign.form.FormData;
import feign.form.FormEncoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import request.TokenAuthInterceptor;
import request.api.SumSub;
import request.data.Applicant;
import request.data.ApplicantStatus;
import request.data.DocMetadata;

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
                .target(SumSub.class, "https://test-api.sumsub.com");

        Applicant applicant = api.createApplicant(defaultLevel, UUID.randomUUID().toString());
        FormData metadata = new DocMetadata(DocMetadata.DocType.PASSPORT, "RUS").toFormData();
        FormData content = new FormData("image/jpg", "test.jpg", Files.readAllBytes(Path.of(testImage)));
        api.addIdDoc(applicant.id, metadata, content);
        api.pendApplicant(applicant.id);
        ApplicantStatus status;
        while (!(status = api.getApplicantStatus(applicant.id)).reviewStatus.equals("completed")) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Still not ready: status=" + status.reviewStatus);
        }
        applicant = api.getApplicant(applicant.id);
        System.out.println("Name: " + applicant.info.firstName);
        System.out.println("Last name: " + applicant.info.lastName);
    }
}

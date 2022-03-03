import request.api.SumSub;
import request.data.Applicant;
import request.data.ApplicantStatus;
import request.data.DocMetadata;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        String appToken = System.getenv("APP_TOKEN");
        String secret = System.getenv("SECRET");
        String testImage = System.getenv("TEST_IMAGE");
        String defaultLevel = "basic-kyc-level"; // from docs

        SumSub api = new SumSub("https://test-api.sumsub.com", appToken, secret);
        Applicant applicant = api.createApplicant(defaultLevel, UUID.randomUUID().toString());
        byte[] content = Files.readAllBytes(Path.of(testImage));
        api.addIdDoc(applicant.id, new DocMetadata(DocMetadata.DocType.PASSPORT, "RUS"), content);
        api.pendApplicant(applicant.id);
        ApplicantStatus status;
        while (!(status = api.getApplicantStatus(applicant.id)).reviewStatus.equals("completed")) {
            Thread.sleep(3000);
            System.out.println("Still not ready, status=" + status.reviewStatus);
        }
        applicant = api.getApplicant(applicant.id);
        System.out.println("First name: " + applicant.info.firstName);
        System.out.println("Last name: " + applicant.info.lastName);
    }
}
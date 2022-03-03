package request.api;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import request.TokenAuthFilter;
import request.data.Applicant;
import request.data.ApplicantStatus;
import request.data.DocMetadata;

import java.util.Map;

public class SumSub {

    private final Client client;
    private final String entryPoint;

    public SumSub(String entryPoint, String appToken, String secret) {
        this.client = ClientBuilder.newBuilder()
                .register(MultiPartFeature.class)
                .register(new TokenAuthFilter(appToken, secret))
                .build();
        this.entryPoint = entryPoint;
    }

    public Applicant createApplicant(String level, String externalId) {
        return client.target(entryPoint)
                .path("/resources/applicants")
                .queryParam("levelName", level)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(Map.of("externalUserId", externalId)), Applicant.class);
    }

    public void addIdDoc(String applicantId, DocMetadata metadata, byte[] content) {
        FormDataMultiPart formData = new FormDataMultiPart();
        formData.field("metadata", metadata, MediaType.APPLICATION_JSON_TYPE);
        formData.field("content", content, MediaType.APPLICATION_OCTET_STREAM_TYPE);
        client.target(entryPoint)
                .path("/resources/applicants/" + applicantId + "/info/idDoc")
                .request(MediaType.APPLICATION_JSON)
                .header("X-Return-Doc-Warnings", false)
                .post(Entity.entity(formData, MediaType.MULTIPART_FORM_DATA_TYPE));
    }

    public void pendApplicant(String applicantId) {
        client.target(entryPoint)
                .path("/resources/applicants/" + applicantId + "/status/pending")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json("{}"));
    }

    public Applicant getApplicant(String applicantId) {
        return client.target(entryPoint)
                .path("/resources/applicants/" + applicantId + "/status/pending")
                .request(MediaType.APPLICATION_JSON)
                .get(Applicant.class);
    }

    public ApplicantStatus getApplicantStatus(String applicantId) {
        return client.target(entryPoint)
                .path("/resources/applicants/" + applicantId + "/status")
                .request(MediaType.APPLICATION_JSON)
                .get(ApplicantStatus.class);
    }
}

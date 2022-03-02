package request.api;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.form.FormData;
import request.data.Applicant;
import request.data.ApplicantStatus;

public interface SumSub {

    @RequestLine("POST /resources/applicants?levelName={levelName}")
    @Headers("Content-Type: application/json")
    Applicant createApplicant(@Param("levelName") String level, @Param("externalUserId") String uuid);

    @RequestLine("POST /resources/applicants/{applicantId}/info/idDoc")
    @Headers({"Content-Type: multipart/form-data", "X-Return-Doc-Warnings: false"})
    void addIdDoc(@Param("applicantId") String applicantId, @Param("metadata") FormData metadata, @Param("content") FormData content);

    @RequestLine("POST /resources/applicants/{applicantId}/status/pending")
    void pendApplicant(@Param("applicantId") String applicantId);

    @RequestLine("GET /resources/applicants/{applicantId}/one")
    Applicant getApplicant(@Param("applicantId") String applicantId);

    @RequestLine("GET /resources/applicants/{applicantId}/status")
    ApplicantStatus getApplicantStatus(@Param("applicantId") String applicantId);
}
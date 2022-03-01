package request.api;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import request.data.Applicant;
import request.data.ApplicantStatus;
import request.data.IdData;

public interface SumSub {

    @RequestLine("POST /resources/applicants?levelName={levelName}")
    @Headers("Content-Type: application/json")
    Applicant createApplicant(@Param("levelName") String level, @Param("externalUserId") String uuid);

    @RequestLine("POST /resources/applicants/{applicantId}/info/idDoc")
    @Headers({"Content-Type: multipart/form-data", "X-Return-Doc-Warnings: false"})
    void addIdDoc(@Param("applicantId") String applicantId, IdData data);

    @RequestLine("GET /resources/applicants/{applicantId}/status")
    ApplicantStatus getStatus(@Param("applicantId") String applicantId);
}


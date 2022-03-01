package request.data;

public class ApplicantStatus {
    private final String moderationComment;
    private final String clientComment;
    private final String reviewStatus;

    public ApplicantStatus(String moderationComment, String clientComment, String reviewStatus) {
        this.moderationComment = moderationComment;
        this.clientComment = clientComment;
        this.reviewStatus = reviewStatus;
    }

    private ApplicantStatus() {
        this("", "", "");
    }

    public String getModerationComment() {
        return moderationComment;
    }

    public String getClientComment() {
        return clientComment;
    }

    public String getReviewStatus() {
        return reviewStatus;
    }
}

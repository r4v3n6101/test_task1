package request.data;

public class Applicant {
    private final String id;

    public Applicant(String id) {
        this.id = id;
    }

    private Applicant() {
        this("");
    }

    public String getId() {
        return id;
    }
}

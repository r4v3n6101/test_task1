package request.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Applicant {
    public String id;
    public Info info;

    public static class Info {
        public String firstName;
        public String lastName;
    }
}

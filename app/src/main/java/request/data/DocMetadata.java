package request.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.form.FormData;

import java.nio.charset.StandardCharsets;

public class DocMetadata {
    public DocType idDocType;
    public String country;

    public DocMetadata(DocType idDocType, String country) {
        this.idDocType = idDocType;
        this.country = country;
    }

    public FormData toFormData() throws JsonProcessingException {
        String data = new ObjectMapper().writeValueAsString(this);
        return new FormData("application/json", null, data.getBytes(StandardCharsets.UTF_8));
    }

    public enum DocType {
        PASSPORT,
        // etc
    }
}
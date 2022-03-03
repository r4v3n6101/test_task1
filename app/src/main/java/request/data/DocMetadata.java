package request.data;

public class DocMetadata {
    public DocType idDocType;
    public String country;

    public DocMetadata(DocType idDocType, String country) {
        this.idDocType = idDocType;
        this.country = country;
    }

    public enum DocType {
        PASSPORT,
        // etc
    }
}
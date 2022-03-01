package request.data;

public class IdMetadata {
    private final DocType idDocType;
    private final String country;

    public IdMetadata(DocType idDocType, String country) {
        this.idDocType = idDocType;
        this.country = country;
    }

    private IdMetadata() {
        this(null, "");
    }

    public DocType getIdDocType() {
        return idDocType;
    }

    public String getCountry() {
        return country;
    }

    public enum DocType {
        PASSPORT,
        // etc
    }
}
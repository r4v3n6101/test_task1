package request.data;

public class IdData {

    private final IdMetadata metadata;
    private final byte[] content;

    public IdData(IdMetadata metadata, byte[] content) {
        this.metadata = metadata;
        this.content = content;
    }

    private IdData() {
        this(null, null);
    }

    public IdMetadata getMetadata() {
        return metadata;
    }

    public byte[] getContent() {
        return content;
    }
}

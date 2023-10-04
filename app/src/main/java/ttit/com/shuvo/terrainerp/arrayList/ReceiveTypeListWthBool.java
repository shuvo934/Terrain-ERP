package ttit.com.shuvo.terrainerp.arrayList;

public class ReceiveTypeListWthBool {
    private String id;
    private String type;
    private boolean updated;

    public ReceiveTypeListWthBool(String id, String type, boolean updated) {
        this.id = id;
        this.type = type;
        this.updated = updated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }
}

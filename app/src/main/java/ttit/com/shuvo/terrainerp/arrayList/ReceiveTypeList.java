package ttit.com.shuvo.terrainerp.arrayList;

public class ReceiveTypeList {
    private String id;
    private String type;
    private String extra_id;

    public ReceiveTypeList(String id, String type, String extra_id) {
        this.id = id;
        this.type = type;
        this.extra_id = extra_id;
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

    public String getExtra_id() {
        return extra_id;
    }

    public void setExtra_id(String extra_id) {
        this.extra_id = extra_id;
    }
}

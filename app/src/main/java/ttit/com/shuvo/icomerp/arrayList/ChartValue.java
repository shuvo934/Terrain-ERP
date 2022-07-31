package ttit.com.shuvo.icomerp.arrayList;

public class ChartValue {

    private String id;
    private String type;
    private String value;

    public ChartValue(String id, String type, String value) {
        this.id = id;
        this.type = type;
        this.value = value;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

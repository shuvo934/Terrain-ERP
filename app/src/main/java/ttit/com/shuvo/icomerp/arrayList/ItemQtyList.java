package ttit.com.shuvo.icomerp.arrayList;

public class ItemQtyList {
    private String id;
    private String qty;
    private String type;

    public ItemQtyList(String id, String qty, String type) {
        this.id = id;
        this.qty = qty;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

package ttit.com.shuvo.terrainerp.arrayList;

public class WoPoQtyLists {

    private String itemName;
    private String qty;
    private String rate;

    public WoPoQtyLists(String itemName, String qty, String rate) {
        this.itemName = itemName;
        this.qty = qty;
        this.rate = rate;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}

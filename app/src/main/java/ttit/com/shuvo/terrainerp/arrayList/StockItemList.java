package ttit.com.shuvo.terrainerp.arrayList;

public class StockItemList {

    private String itemId;
    private String itemName;
    private String unit;
    private String qty;
    private String value;
    private String stock_qty;

    public StockItemList(String itemId, String itemName, String unit, String qty, String value, String stock_qty) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.unit = unit;
        this.qty = qty;
        this.value = value;
        this.stock_qty = stock_qty;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStock_qty() {
        return stock_qty;
    }

    public void setStock_qty(String stock_qty) {
        this.stock_qty = stock_qty;
    }
}

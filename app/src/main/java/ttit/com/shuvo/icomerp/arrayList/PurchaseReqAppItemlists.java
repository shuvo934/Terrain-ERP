package ttit.com.shuvo.icomerp.arrayList;

public class PurchaseReqAppItemlists {

    private String itemId;
    private String itemName;
    private String unit;
    private String stockQty;
    private String itemCode;
    private String itemHSCode;
    private String itemPartNo;

    public PurchaseReqAppItemlists(String itemId, String itemName, String unit, String stockQty, String itemCode, String itemHSCode, String itemPartNo) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.unit = unit;
        this.stockQty = stockQty;
        this.itemCode = itemCode;
        this.itemHSCode = itemHSCode;
        this.itemPartNo = itemPartNo;
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

    public String getStockQty() {
        return stockQty;
    }

    public void setStockQty(String stockQty) {
        this.stockQty = stockQty;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemHSCode() {
        return itemHSCode;
    }

    public void setItemHSCode(String itemHSCode) {
        this.itemHSCode = itemHSCode;
    }

    public String getItemPartNo() {
        return itemPartNo;
    }

    public void setItemPartNo(String itemPartNo) {
        this.itemPartNo = itemPartNo;
    }
}

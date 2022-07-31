package ttit.com.shuvo.icomerp.arrayList;

public class PurchaseReqAppQtyLists {

    private String itemId;
    private String itemName;
    private String reqQty;
    private String approvedQty;
    private String prdId;

    public PurchaseReqAppQtyLists(String itemId, String itemName, String reqQty, String approvedQty, String prdId) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.reqQty = reqQty;
        this.approvedQty = approvedQty;
        this.prdId = prdId;
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

    public String getReqQty() {
        return reqQty;
    }

    public void setReqQty(String reqQty) {
        this.reqQty = reqQty;
    }

    public String getApprovedQty() {
        return approvedQty;
    }

    public void setApprovedQty(String approvedQty) {
        this.approvedQty = approvedQty;
    }

    public String getPrdId() {
        return prdId;
    }

    public void setPrdId(String prdId) {
        this.prdId = prdId;
    }
}

package ttit.com.shuvo.terrainerp.arrayList;

public class SalesOrderItemLists {

    private String itemName;
    private String hsCode;
    private String partNo;
    private String qty;
    private String unit;
    private String unitPrice;
    private String discountValueUnit;
    private String discountType;
    private String orderAmnt;
    private String deliverdQty;
    private String rtnQty;
    private String balanceQty;
    private String sampleQty;
    private String balancedSampQty;
    private String deliverdSampQty;

    public SalesOrderItemLists(String itemName, String hsCode, String partNo, String qty, String unit, String unitPrice, String discountValueUnit, String discountType, String orderAmnt, String deliverdQty, String rtnQty, String balanceQty, String sampleQty, String balancedSampQty, String deliverdSampQty) {
        this.itemName = itemName;
        this.hsCode = hsCode;
        this.partNo = partNo;
        this.qty = qty;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.discountValueUnit = discountValueUnit;
        this.discountType = discountType;
        this.orderAmnt = orderAmnt;
        this.deliverdQty = deliverdQty;
        this.rtnQty = rtnQty;
        this.balanceQty = balanceQty;
        this.sampleQty = sampleQty;
        this.balancedSampQty = balancedSampQty;
        this.deliverdSampQty = deliverdSampQty;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getHsCode() {
        return hsCode;
    }

    public void setHsCode(String hsCode) {
        this.hsCode = hsCode;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getDiscountValueUnit() {
        return discountValueUnit;
    }

    public void setDiscountValueUnit(String discountValueUnit) {
        this.discountValueUnit = discountValueUnit;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getOrderAmnt() {
        return orderAmnt;
    }

    public void setOrderAmnt(String orderAmnt) {
        this.orderAmnt = orderAmnt;
    }

    public String getDeliverdQty() {
        return deliverdQty;
    }

    public void setDeliverdQty(String deliverdQty) {
        this.deliverdQty = deliverdQty;
    }

    public String getRtnQty() {
        return rtnQty;
    }

    public void setRtnQty(String rtnQty) {
        this.rtnQty = rtnQty;
    }

    public String getBalanceQty() {
        return balanceQty;
    }

    public void setBalanceQty(String balanceQty) {
        this.balanceQty = balanceQty;
    }

    public String getSampleQty() {
        return sampleQty;
    }

    public void setSampleQty(String sampleQty) {
        this.sampleQty = sampleQty;
    }

    public String getBalancedSampQty() {
        return balancedSampQty;
    }

    public void setBalancedSampQty(String balancedSampQty) {
        this.balancedSampQty = balancedSampQty;
    }

    public String getDeliverdSampQty() {
        return deliverdSampQty;
    }

    public void setDeliverdSampQty(String deliverdSampQty) {
        this.deliverdSampQty = deliverdSampQty;
    }
}

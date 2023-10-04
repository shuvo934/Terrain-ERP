package ttit.com.shuvo.terrainerp.arrayList;

public class PurchaseOrderReqLists {

    private String reqNo;
    private String categoryName;
    private String womId;
    private String workOrderJobId;
    private String prmId;

    public PurchaseOrderReqLists(String reqNo, String categoryName, String womId, String workOrderJobId, String prmId) {
        this.reqNo = reqNo;
        this.categoryName = categoryName;
        this.womId = womId;
        this.workOrderJobId = workOrderJobId;
        this.prmId = prmId;
    }

    public String getReqNo() {
        return reqNo;
    }

    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getWomId() {
        return womId;
    }

    public void setWomId(String womId) {
        this.womId = womId;
    }

    public String getWorkOrderJobId() {
        return workOrderJobId;
    }

    public void setWorkOrderJobId(String workOrderJobId) {
        this.workOrderJobId = workOrderJobId;
    }

    public String getPrmId() {
        return prmId;
    }

    public void setPrmId(String prmId) {
        this.prmId = prmId;
    }
}

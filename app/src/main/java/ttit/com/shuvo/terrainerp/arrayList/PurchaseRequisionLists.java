package ttit.com.shuvo.terrainerp.arrayList;

public class PurchaseRequisionLists {

    private String prmId;
    private String reqNo;
    private String prDate;
    private String user;

    public PurchaseRequisionLists(String prmId, String reqNo, String prDate, String user) {
        this.prmId = prmId;
        this.reqNo = reqNo;
        this.prDate = prDate;
        this.user = user;
    }

    public String getPrmId() {
        return prmId;
    }

    public void setPrmId(String prmId) {
        this.prmId = prmId;
    }

    public String getReqNo() {
        return reqNo;
    }

    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }

    public String getPrDate() {
        return prDate;
    }

    public void setPrDate(String prDate) {
        this.prDate = prDate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}

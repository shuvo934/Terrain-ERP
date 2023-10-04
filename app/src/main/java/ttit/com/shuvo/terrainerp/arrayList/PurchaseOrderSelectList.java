package ttit.com.shuvo.terrainerp.arrayList;

public class PurchaseOrderSelectList {

    private String wom_id;
    private String wom_no;
    private String date;
    private String userName;
    private String supplierName;

    public PurchaseOrderSelectList(String wom_id, String wom_no, String date, String userName, String supplierName) {
        this.wom_id = wom_id;
        this.wom_no = wom_no;
        this.date = date;
        this.userName = userName;
        this.supplierName = supplierName;
    }

    public String getWom_id() {
        return wom_id;
    }

    public void setWom_id(String wom_id) {
        this.wom_id = wom_id;
    }

    public String getWom_no() {
        return wom_no;
    }

    public void setWom_no(String wom_no) {
        this.wom_no = wom_no;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}

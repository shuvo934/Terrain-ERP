package ttit.com.shuvo.terrainerp.arrayList;

public class PurchaseOrderRequisitionLists {
    private String prm_req_no;
    private String pr_date;
    private String prm_id;
    private String im_id;
    private String im_name;
    private String req_qty;
    private String app_qty;
    private String app_balance;

    public PurchaseOrderRequisitionLists(String prm_req_no, String pr_date, String prm_id, String im_id, String im_name, String req_qty, String app_qty, String app_balance) {
        this.prm_req_no = prm_req_no;
        this.pr_date = pr_date;
        this.prm_id = prm_id;
        this.im_id = im_id;
        this.im_name = im_name;
        this.req_qty = req_qty;
        this.app_qty = app_qty;
        this.app_balance = app_balance;
    }

    public String getPrm_req_no() {
        return prm_req_no;
    }

    public void setPrm_req_no(String prm_req_no) {
        this.prm_req_no = prm_req_no;
    }

    public String getPr_date() {
        return pr_date;
    }

    public void setPr_date(String pr_date) {
        this.pr_date = pr_date;
    }

    public String getPrm_id() {
        return prm_id;
    }

    public void setPrm_id(String prm_id) {
        this.prm_id = prm_id;
    }

    public String getIm_id() {
        return im_id;
    }

    public void setIm_id(String im_id) {
        this.im_id = im_id;
    }

    public String getIm_name() {
        return im_name;
    }

    public void setIm_name(String im_name) {
        this.im_name = im_name;
    }

    public String getReq_qty() {
        return req_qty;
    }

    public void setReq_qty(String req_qty) {
        this.req_qty = req_qty;
    }

    public String getApp_qty() {
        return app_qty;
    }

    public void setApp_qty(String app_qty) {
        this.app_qty = app_qty;
    }

    public String getApp_balance() {
        return app_balance;
    }

    public void setApp_balance(String app_balance) {
        this.app_balance = app_balance;
    }
}

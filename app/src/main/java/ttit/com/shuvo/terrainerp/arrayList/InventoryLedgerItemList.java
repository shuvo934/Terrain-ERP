package ttit.com.shuvo.terrainerp.arrayList;

public class InventoryLedgerItemList {

    private String sl_date;
    private String rcv_no;
    private String issue_no;
    private String trans_flag;
    private String trans_src;
    private String supplier;
    private String rcv_qty;
    private String issue_qty;
    private String balance;

    public InventoryLedgerItemList(String sl_date, String rcv_no, String issue_no, String trans_flag, String trans_src, String supplier, String rcv_qty, String issue_qty, String balance) {
        this.sl_date = sl_date;
        this.rcv_no = rcv_no;
        this.issue_no = issue_no;
        this.trans_flag = trans_flag;
        this.trans_src = trans_src;
        this.supplier = supplier;
        this.rcv_qty = rcv_qty;
        this.issue_qty = issue_qty;
        this.balance = balance;
    }

    public String getSl_date() {
        return sl_date;
    }

    public void setSl_date(String sl_date) {
        this.sl_date = sl_date;
    }

    public String getRcv_no() {
        return rcv_no;
    }

    public void setRcv_no(String rcv_no) {
        this.rcv_no = rcv_no;
    }

    public String getIssue_no() {
        return issue_no;
    }

    public void setIssue_no(String issue_no) {
        this.issue_no = issue_no;
    }

    public String getTrans_flag() {
        return trans_flag;
    }

    public void setTrans_flag(String trans_flag) {
        this.trans_flag = trans_flag;
    }

    public String getTrans_src() {
        return trans_src;
    }

    public void setTrans_src(String trans_src) {
        this.trans_src = trans_src;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getRcv_qty() {
        return rcv_qty;
    }

    public void setRcv_qty(String rcv_qty) {
        this.rcv_qty = rcv_qty;
    }

    public String getIssue_qty() {
        return issue_qty;
    }

    public void setIssue_qty(String issue_qty) {
        this.issue_qty = issue_qty;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}


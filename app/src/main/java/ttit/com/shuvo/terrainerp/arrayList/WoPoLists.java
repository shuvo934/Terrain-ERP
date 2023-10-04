package ttit.com.shuvo.terrainerp.arrayList;

public class WoPoLists {

    private String wo_id;
    private String wo_po_no;
    private String wo_amnt;
    private String rcv_amnt;
    private String bill_amnt;
    private String vat_amnt;
    private String total_bill;
    private String bill_paid;
    private String bill_payable;

    public WoPoLists(String wo_id, String wo_po_no, String wo_amnt, String rcv_amnt, String bill_amnt, String vat_amnt, String total_bill, String bill_paid, String bill_payable) {
        this.wo_id = wo_id;
        this.wo_po_no = wo_po_no;
        this.wo_amnt = wo_amnt;
        this.rcv_amnt = rcv_amnt;
        this.bill_amnt = bill_amnt;
        this.vat_amnt = vat_amnt;
        this.total_bill = total_bill;
        this.bill_paid = bill_paid;
        this.bill_payable = bill_payable;
    }

    public String getWo_id() {
        return wo_id;
    }

    public void setWo_id(String wo_id) {
        this.wo_id = wo_id;
    }

    public String getWo_po_no() {
        return wo_po_no;
    }

    public void setWo_po_no(String wo_po_no) {
        this.wo_po_no = wo_po_no;
    }

    public String getWo_amnt() {
        return wo_amnt;
    }

    public void setWo_amnt(String wo_amnt) {
        this.wo_amnt = wo_amnt;
    }

    public String getRcv_amnt() {
        return rcv_amnt;
    }

    public void setRcv_amnt(String rcv_amnt) {
        this.rcv_amnt = rcv_amnt;
    }

    public String getBill_amnt() {
        return bill_amnt;
    }

    public void setBill_amnt(String bill_amnt) {
        this.bill_amnt = bill_amnt;
    }

    public String getVat_amnt() {
        return vat_amnt;
    }

    public void setVat_amnt(String vat_amnt) {
        this.vat_amnt = vat_amnt;
    }

    public String getTotal_bill() {
        return total_bill;
    }

    public void setTotal_bill(String total_bill) {
        this.total_bill = total_bill;
    }

    public String getBill_paid() {
        return bill_paid;
    }

    public void setBill_paid(String bill_paid) {
        this.bill_paid = bill_paid;
    }

    public String getBill_payable() {
        return bill_payable;
    }

    public void setBill_payable(String bill_payable) {
        this.bill_payable = bill_payable;
    }
}

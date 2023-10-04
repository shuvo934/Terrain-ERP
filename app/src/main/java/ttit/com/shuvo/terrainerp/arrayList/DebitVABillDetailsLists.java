package ttit.com.shuvo.terrainerp.arrayList;

public class DebitVABillDetailsLists {

    private String vjID;
    private String vjAVDPID;
    private String invoiceAmnt;
    private String vjBRBID;
    private String vatAmnt;
    private String poWoNo;
    private String invoiceNo;

    public DebitVABillDetailsLists(String vjID, String vjAVDPID, String invoiceAmnt, String vjBRBID, String vatAmnt, String poWoNo, String invoiceNo) {
        this.vjID = vjID;
        this.vjAVDPID = vjAVDPID;
        this.invoiceAmnt = invoiceAmnt;
        this.vjBRBID = vjBRBID;
        this.vatAmnt = vatAmnt;
        this.poWoNo = poWoNo;
        this.invoiceNo = invoiceNo;
    }

    public String getVjID() {
        return vjID;
    }

    public void setVjID(String vjID) {
        this.vjID = vjID;
    }

    public String getVjAVDPID() {
        return vjAVDPID;
    }

    public void setVjAVDPID(String vjAVDPID) {
        this.vjAVDPID = vjAVDPID;
    }

    public String getInvoiceAmnt() {
        return invoiceAmnt;
    }

    public void setInvoiceAmnt(String invoiceAmnt) {
        this.invoiceAmnt = invoiceAmnt;
    }

    public String getVjBRBID() {
        return vjBRBID;
    }

    public void setVjBRBID(String vjBRBID) {
        this.vjBRBID = vjBRBID;
    }

    public String getVatAmnt() {
        return vatAmnt;
    }

    public void setVatAmnt(String vatAmnt) {
        this.vatAmnt = vatAmnt;
    }

    public String getPoWoNo() {
        return poWoNo;
    }

    public void setPoWoNo(String poWoNo) {
        this.poWoNo = poWoNo;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }
}

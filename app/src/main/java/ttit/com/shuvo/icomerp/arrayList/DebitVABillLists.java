package ttit.com.shuvo.icomerp.arrayList;

import java.util.ArrayList;

public class DebitVABillLists {

    private String avdpId;
    private String avdpVdId;
    private String remarks;
    private String avdpBrmId;
    private String billNo;
    private String billType;
    private ArrayList<DebitVABillDetailsLists> debitVABillDetailsLists;

    public DebitVABillLists(String avdpId, String avdpVdId, String remarks, String avdpBrmId, String billNo, String billType, ArrayList<DebitVABillDetailsLists> debitVABillDetailsLists) {
        this.avdpId = avdpId;
        this.avdpVdId = avdpVdId;
        this.remarks = remarks;
        this.avdpBrmId = avdpBrmId;
        this.billNo = billNo;
        this.billType = billType;
        this.debitVABillDetailsLists = debitVABillDetailsLists;
    }

    public String getAvdpId() {
        return avdpId;
    }

    public void setAvdpId(String avdpId) {
        this.avdpId = avdpId;
    }

    public String getAvdpVdId() {
        return avdpVdId;
    }

    public void setAvdpVdId(String avdpVdId) {
        this.avdpVdId = avdpVdId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAvdpBrmId() {
        return avdpBrmId;
    }

    public void setAvdpBrmId(String avdpBrmId) {
        this.avdpBrmId = avdpBrmId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public ArrayList<DebitVABillDetailsLists> getDebitVABillDetailsLists() {
        return debitVABillDetailsLists;
    }

    public void setDebitVABillDetailsLists(ArrayList<DebitVABillDetailsLists> debitVABillDetailsLists) {
        this.debitVABillDetailsLists = debitVABillDetailsLists;
    }
}

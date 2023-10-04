package ttit.com.shuvo.terrainerp.arrayList;

public class VoucherSDSList {
    private String voucherNo;
    private String voucherDate;
    private String voucherAmnt;

    public VoucherSDSList(String voucherNo, String voucherDate, String voucherAmnt) {
        this.voucherNo = voucherNo;
        this.voucherDate = voucherDate;
        this.voucherAmnt = voucherAmnt;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getVoucherAmnt() {
        return voucherAmnt;
    }

    public void setVoucherAmnt(String voucherAmnt) {
        this.voucherAmnt = voucherAmnt;
    }
}

package ttit.com.shuvo.terrainerp.arrayList;

public class DebitCreditVoucherLists {

    private String unitName;
    private String vmNo;
    private String approvedUser;
    private String billRefNo;
    private String billRefDate;
    private String vmDate;
    private String type;
    private String narration;
    private String adCode;
    private String adName;
    private String adDate;
    private String payeeName;
    private String debit;
    private String credit;
    private String billInfo;

    public DebitCreditVoucherLists(String unitName, String vmNo, String approvedUser, String billRefNo, String billRefDate, String vmDate, String type, String narration, String adCode, String adName, String adDate, String payeeName, String debit, String credit, String billInfo) {
        this.unitName = unitName;
        this.vmNo = vmNo;
        this.approvedUser = approvedUser;
        this.billRefNo = billRefNo;
        this.billRefDate = billRefDate;
        this.vmDate = vmDate;
        this.type = type;
        this.narration = narration;
        this.adCode = adCode;
        this.adName = adName;
        this.adDate = adDate;
        this.payeeName = payeeName;
        this.debit = debit;
        this.credit = credit;
        this.billInfo = billInfo;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getVmNo() {
        return vmNo;
    }

    public void setVmNo(String vmNo) {
        this.vmNo = vmNo;
    }

    public String getApprovedUser() {
        return approvedUser;
    }

    public void setApprovedUser(String approvedUser) {
        this.approvedUser = approvedUser;
    }

    public String getBillRefNo() {
        return billRefNo;
    }

    public void setBillRefNo(String billRefNo) {
        this.billRefNo = billRefNo;
    }

    public String getBillRefDate() {
        return billRefDate;
    }

    public void setBillRefDate(String billRefDate) {
        this.billRefDate = billRefDate;
    }

    public String getVmDate() {
        return vmDate;
    }

    public void setVmDate(String vmDate) {
        this.vmDate = vmDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public String getAdDate() {
        return adDate;
    }

    public void setAdDate(String adDate) {
        this.adDate = adDate;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getBillInfo() {
        return billInfo;
    }

    public void setBillInfo(String billInfo) {
        this.billInfo = billInfo;
    }
}

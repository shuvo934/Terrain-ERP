package ttit.com.shuvo.icomerp.arrayList;

public class AccountLedgerLists {
    private String lgId;
    private String date;
    private String voucherNo;
    private String particulars;
    private String lgDebit;
    private String debit;
    private String lgCredit;
    private String credit;
    private String openingDebit;
    private String openingCredit;
    private String ahCode;
    private String balance;

    public AccountLedgerLists(String lgId, String date, String voucherNo, String particulars, String lgDebit, String debit, String lgCredit, String credit, String openingDebit, String openingCredit, String ahCode, String balance) {
        this.lgId = lgId;
        this.date = date;
        this.voucherNo = voucherNo;
        this.particulars = particulars;
        this.lgDebit = lgDebit;
        this.debit = debit;
        this.lgCredit = lgCredit;
        this.credit = credit;
        this.openingDebit = openingDebit;
        this.openingCredit = openingCredit;
        this.ahCode = ahCode;
        this.balance = balance;
    }

    public String getLgId() {
        return lgId;
    }

    public void setLgId(String lgId) {
        this.lgId = lgId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public String getLgDebit() {
        return lgDebit;
    }

    public void setLgDebit(String lgDebit) {
        this.lgDebit = lgDebit;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getLgCredit() {
        return lgCredit;
    }

    public void setLgCredit(String lgCredit) {
        this.lgCredit = lgCredit;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getOpeningDebit() {
        return openingDebit;
    }

    public void setOpeningDebit(String openingDebit) {
        this.openingDebit = openingDebit;
    }

    public String getOpeningCredit() {
        return openingCredit;
    }

    public void setOpeningCredit(String openingCredit) {
        this.openingCredit = openingCredit;
    }

    public String getAhCode() {
        return ahCode;
    }

    public void setAhCode(String ahCode) {
        this.ahCode = ahCode;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}

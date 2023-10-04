package ttit.com.shuvo.terrainerp.arrayList;

public class CashTransactionLists {
    private String unitName;
    private String voucherNo;
    private String particulars;
    private String debit;
    private String credit;
    private String invPurNo;
    private String type;
    private String totalDebit;
    private String totalCredit;
    private String balance;
    private String preBalance;
    private String totalBalance;

    public CashTransactionLists(String unitName, String voucherNo, String particulars, String debit, String credit, String invPurNo, String type, String totalDebit, String totalCredit, String balance, String preBalance, String totalBalance) {
        this.unitName = unitName;
        this.voucherNo = voucherNo;
        this.particulars = particulars;
        this.debit = debit;
        this.credit = credit;
        this.invPurNo = invPurNo;
        this.type = type;
        this.totalDebit = totalDebit;
        this.totalCredit = totalCredit;
        this.balance = balance;
        this.preBalance = preBalance;
        this.totalBalance = totalBalance;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
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

    public String getInvPurNo() {
        return invPurNo;
    }

    public void setInvPurNo(String invPurNo) {
        this.invPurNo = invPurNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(String totalDebit) {
        this.totalDebit = totalDebit;
    }

    public String getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(String totalCredit) {
        this.totalCredit = totalCredit;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getPreBalance() {
        return preBalance;
    }

    public void setPreBalance(String preBalance) {
        this.preBalance = preBalance;
    }

    public String getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(String totalBalance) {
        this.totalBalance = totalBalance;
    }
}

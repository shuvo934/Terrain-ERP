package ttit.com.shuvo.icomerp.arrayList;

public class PartySummaryLedgerLists {
    private String lgAdId;
    private String adCode;
    private String adName;
    private String bf;
    private String debit;
    private String credit;
    private String dueBalance;
    private String adFlag;

    public PartySummaryLedgerLists(String lgAdId, String adCode, String adName, String bf, String debit, String credit, String dueBalance, String adFlag) {
        this.lgAdId = lgAdId;
        this.adCode = adCode;
        this.adName = adName;
        this.bf = bf;
        this.debit = debit;
        this.credit = credit;
        this.dueBalance = dueBalance;
        this.adFlag = adFlag;
    }

    public String getLgAdId() {
        return lgAdId;
    }

    public void setLgAdId(String lgAdId) {
        this.lgAdId = lgAdId;
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

    public String getBf() {
        return bf;
    }

    public void setBf(String bf) {
        this.bf = bf;
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

    public String getDueBalance() {
        return dueBalance;
    }

    public void setDueBalance(String dueBalance) {
        this.dueBalance = dueBalance;
    }

    public String getAdFlag() {
        return adFlag;
    }

    public void setAdFlag(String adFlag) {
        this.adFlag = adFlag;
    }
}

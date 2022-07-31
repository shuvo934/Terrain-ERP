package ttit.com.shuvo.icomerp.arrayList;

public class PayRcvSummAccountsList {

    private String slNo;
    private String lgAdId;
    private String partyName;
    private String balance;
    private String balanceType;
    private String accountNo;
    private String flag;

    public PayRcvSummAccountsList(String slNo, String lgAdId, String partyName, String balance, String balanceType, String accountNo, String flag) {
        this.slNo = slNo;
        this.lgAdId = lgAdId;
        this.partyName = partyName;
        this.balance = balance;
        this.balanceType = balanceType;
        this.accountNo = accountNo;
        this.flag = flag;
    }

    public String getSlNo() {
        return slNo;
    }

    public void setSlNo(String slNo) {
        this.slNo = slNo;
    }

    public String getLgAdId() {
        return lgAdId;
    }

    public void setLgAdId(String lgAdId) {
        this.lgAdId = lgAdId;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(String balanceType) {
        this.balanceType = balanceType;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}

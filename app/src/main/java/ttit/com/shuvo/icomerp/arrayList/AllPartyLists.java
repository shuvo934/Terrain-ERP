package ttit.com.shuvo.icomerp.arrayList;

public class AllPartyLists {
    private String sNo;
    private String ad_id;
    private String ad_name;
    private String ad_code;
    private String ad_address;
    private String ad_phone;
    private String ad_email;

    public AllPartyLists(String sNo, String ad_id, String ad_name, String ad_code, String ad_address, String ad_phone, String ad_email) {
        this.sNo = sNo;
        this.ad_id = ad_id;
        this.ad_name = ad_name;
        this.ad_code = ad_code;
        this.ad_address = ad_address;
        this.ad_phone = ad_phone;
        this.ad_email = ad_email;
    }

    public String getsNo() {
        return sNo;
    }

    public void setsNo(String sNo) {
        this.sNo = sNo;
    }

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }

    public String getAd_name() {
        return ad_name;
    }

    public void setAd_name(String ad_name) {
        this.ad_name = ad_name;
    }

    public String getAd_code() {
        return ad_code;
    }

    public void setAd_code(String ad_code) {
        this.ad_code = ad_code;
    }

    public String getAd_address() {
        return ad_address;
    }

    public void setAd_address(String ad_address) {
        this.ad_address = ad_address;
    }

    public String getAd_phone() {
        return ad_phone;
    }

    public void setAd_phone(String ad_phone) {
        this.ad_phone = ad_phone;
    }

    public String getAd_email() {
        return ad_email;
    }

    public void setAd_email(String ad_email) {
        this.ad_email = ad_email;
    }
}

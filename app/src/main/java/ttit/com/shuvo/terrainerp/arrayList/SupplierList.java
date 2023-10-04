package ttit.com.shuvo.terrainerp.arrayList;

public class SupplierList {
    private String ad_id;
    private String ad_code;
    private String ad_name;
    private String ad_short_name;
    private String ad_address;

    public SupplierList(String ad_id, String ad_code, String ad_name, String ad_short_name, String ad_address) {
        this.ad_id = ad_id;
        this.ad_code = ad_code;
        this.ad_name = ad_name;
        this.ad_short_name = ad_short_name;
        this.ad_address = ad_address;
    }

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }

    public String getAd_code() {
        return ad_code;
    }

    public void setAd_code(String ad_code) {
        this.ad_code = ad_code;
    }

    public String getAd_name() {
        return ad_name;
    }

    public void setAd_name(String ad_name) {
        this.ad_name = ad_name;
    }

    public String getAd_short_name() {
        return ad_short_name;
    }

    public void setAd_short_name(String ad_short_name) {
        this.ad_short_name = ad_short_name;
    }

    public String getAd_address() {
        return ad_address;
    }

    public void setAd_address(String ad_address) {
        this.ad_address = ad_address;
    }
}

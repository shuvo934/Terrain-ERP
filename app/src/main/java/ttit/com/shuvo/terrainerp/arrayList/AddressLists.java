package ttit.com.shuvo.terrainerp.arrayList;

public class AddressLists {
    private String coa_name;
    private String coa_address;
    private String coa_short_name;
    private String coa_short_code;
    private String coa_id;

    public AddressLists(String coa_name, String coa_address, String coa_short_name, String coa_short_code, String coa_id) {
        this.coa_name = coa_name;
        this.coa_address = coa_address;
        this.coa_short_name = coa_short_name;
        this.coa_short_code = coa_short_code;
        this.coa_id = coa_id;
    }

    public String getCoa_name() {
        return coa_name;
    }

    public void setCoa_name(String coa_name) {
        this.coa_name = coa_name;
    }

    public String getCoa_address() {
        return coa_address;
    }

    public void setCoa_address(String coa_address) {
        this.coa_address = coa_address;
    }

    public String getCoa_short_name() {
        return coa_short_name;
    }

    public void setCoa_short_name(String coa_short_name) {
        this.coa_short_name = coa_short_name;
    }

    public String getCoa_short_code() {
        return coa_short_code;
    }

    public void setCoa_short_code(String coa_short_code) {
        this.coa_short_code = coa_short_code;
    }

    public String getCoa_id() {
        return coa_id;
    }

    public void setCoa_id(String coa_id) {
        this.coa_id = coa_id;
    }
}

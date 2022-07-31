package ttit.com.shuvo.icomerp.arrayList;

public class VoucherAllSDSList {
    private String sm_id;
    private String v_id;
    private String v_no;
    private String v_date;
    private String v_amnt;

    public VoucherAllSDSList(String sm_id, String v_id, String v_no, String v_date, String v_amnt) {
        this.sm_id = sm_id;
        this.v_id = v_id;
        this.v_no = v_no;
        this.v_date = v_date;
        this.v_amnt = v_amnt;
    }

    public String getSm_id() {
        return sm_id;
    }

    public void setSm_id(String sm_id) {
        this.sm_id = sm_id;
    }

    public String getV_id() {
        return v_id;
    }

    public void setV_id(String v_id) {
        this.v_id = v_id;
    }

    public String getV_no() {
        return v_no;
    }

    public void setV_no(String v_no) {
        this.v_no = v_no;
    }

    public String getV_date() {
        return v_date;
    }

    public void setV_date(String v_date) {
        this.v_date = v_date;
    }

    public String getV_amnt() {
        return v_amnt;
    }

    public void setV_amnt(String v_amnt) {
        this.v_amnt = v_amnt;
    }
}

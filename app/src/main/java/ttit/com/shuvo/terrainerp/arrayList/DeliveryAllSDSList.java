package ttit.com.shuvo.terrainerp.arrayList;

public class DeliveryAllSDSList {
    private String som_id;
    private String sm_id;
    private String delivery_no;
    private String deliver_date;
    private String deliver_amnt;
    private String deliver_vat;
    private String deliver_total_amnt;
    private String deliver_paid;
    private String deliver_balance;

    public DeliveryAllSDSList(String som_id, String sm_id, String delivery_no, String deliver_date, String deliver_amnt, String deliver_vat, String deliver_total_amnt, String deliver_paid, String deliver_balance) {
        this.som_id = som_id;
        this.sm_id = sm_id;
        this.delivery_no = delivery_no;
        this.deliver_date = deliver_date;
        this.deliver_amnt = deliver_amnt;
        this.deliver_vat = deliver_vat;
        this.deliver_total_amnt = deliver_total_amnt;
        this.deliver_paid = deliver_paid;
        this.deliver_balance = deliver_balance;
    }

    public String getSom_id() {
        return som_id;
    }

    public void setSom_id(String som_id) {
        this.som_id = som_id;
    }

    public String getSm_id() {
        return sm_id;
    }

    public void setSm_id(String sm_id) {
        this.sm_id = sm_id;
    }

    public String getDelivery_no() {
        return delivery_no;
    }

    public void setDelivery_no(String delivery_no) {
        this.delivery_no = delivery_no;
    }

    public String getDeliver_date() {
        return deliver_date;
    }

    public void setDeliver_date(String deliver_date) {
        this.deliver_date = deliver_date;
    }

    public String getDeliver_amnt() {
        return deliver_amnt;
    }

    public void setDeliver_amnt(String deliver_amnt) {
        this.deliver_amnt = deliver_amnt;
    }

    public String getDeliver_vat() {
        return deliver_vat;
    }

    public void setDeliver_vat(String deliver_vat) {
        this.deliver_vat = deliver_vat;
    }

    public String getDeliver_total_amnt() {
        return deliver_total_amnt;
    }

    public void setDeliver_total_amnt(String deliver_total_amnt) {
        this.deliver_total_amnt = deliver_total_amnt;
    }

    public String getDeliver_paid() {
        return deliver_paid;
    }

    public void setDeliver_paid(String deliver_paid) {
        this.deliver_paid = deliver_paid;
    }

    public String getDeliver_balance() {
        return deliver_balance;
    }

    public void setDeliver_balance(String deliver_balance) {
        this.deliver_balance = deliver_balance;
    }
}

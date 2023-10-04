package ttit.com.shuvo.terrainerp.arrayList;

public class SalesOrderList {
    private String order_id;
    private String ad_id;
    private String ad_name;
    private String client_code;
    private String order_no;
    private String order_date;
    private String user;
    private String target_delivery;
    private String target_address;
    private String order_type;
    private String edd;
    private String discount_amnt;
    private String vat_amnt;
    private String contact_person;
    private String contact_number;
    private String person_email;
    private String total_order_amnt;
    private String advance_amnt;

    public SalesOrderList(String order_id, String ad_id, String ad_name, String client_code, String order_no, String order_date, String user, String target_delivery, String target_address, String order_type, String edd, String discount_amnt, String vat_amnt, String contact_person, String contact_number, String person_email, String total_order_amnt, String advance_amnt) {
        this.order_id = order_id;
        this.ad_id = ad_id;
        this.ad_name = ad_name;
        this.client_code = client_code;
        this.order_no = order_no;
        this.order_date = order_date;
        this.user = user;
        this.target_delivery = target_delivery;
        this.target_address = target_address;
        this.order_type = order_type;
        this.edd = edd;
        this.discount_amnt = discount_amnt;
        this.vat_amnt = vat_amnt;
        this.contact_person = contact_person;
        this.contact_number = contact_number;
        this.person_email = person_email;
        this.total_order_amnt = total_order_amnt;
        this.advance_amnt = advance_amnt;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
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

    public String getClient_code() {
        return client_code;
    }

    public void setClient_code(String client_code) {
        this.client_code = client_code;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTarget_delivery() {
        return target_delivery;
    }

    public void setTarget_delivery(String target_delivery) {
        this.target_delivery = target_delivery;
    }

    public String getTarget_address() {
        return target_address;
    }

    public void setTarget_address(String target_address) {
        this.target_address = target_address;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getEdd() {
        return edd;
    }

    public void setEdd(String edd) {
        this.edd = edd;
    }

    public String getDiscount_amnt() {
        return discount_amnt;
    }

    public void setDiscount_amnt(String discount_amnt) {
        this.discount_amnt = discount_amnt;
    }

    public String getVat_amnt() {
        return vat_amnt;
    }

    public void setVat_amnt(String vat_amnt) {
        this.vat_amnt = vat_amnt;
    }

    public String getContact_person() {
        return contact_person;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getPerson_email() {
        return person_email;
    }

    public void setPerson_email(String person_email) {
        this.person_email = person_email;
    }

    public String getTotal_order_amnt() {
        return total_order_amnt;
    }

    public void setTotal_order_amnt(String total_order_amnt) {
        this.total_order_amnt = total_order_amnt;
    }

    public String getAdvance_amnt() {
        return advance_amnt;
    }

    public void setAdvance_amnt(String advance_amnt) {
        this.advance_amnt = advance_amnt;
    }
}

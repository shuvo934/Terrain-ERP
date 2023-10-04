package ttit.com.shuvo.terrainerp.arrayList;

public class OrderAllSDSList {

    private String ad_id;
    private String som_id;
    private String order_no;
    private String order_date;
    private String order_amnt;
    private String adv_amnt;
    private String acc_paid;
    private String balance;

    public OrderAllSDSList(String ad_id, String som_id, String order_no, String order_date, String order_amnt, String adv_amnt, String acc_paid, String balance) {
        this.ad_id = ad_id;
        this.som_id = som_id;
        this.order_no = order_no;
        this.order_date = order_date;
        this.order_amnt = order_amnt;
        this.adv_amnt = adv_amnt;
        this.acc_paid = acc_paid;
        this.balance = balance;
    }

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }

    public String getSom_id() {
        return som_id;
    }

    public void setSom_id(String som_id) {
        this.som_id = som_id;
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

    public String getOrder_amnt() {
        return order_amnt;
    }

    public void setOrder_amnt(String order_amnt) {
        this.order_amnt = order_amnt;
    }

    public String getAdv_amnt() {
        return adv_amnt;
    }

    public void setAdv_amnt(String adv_amnt) {
        this.adv_amnt = adv_amnt;
    }

    public String getAcc_paid() {
        return acc_paid;
    }

    public void setAcc_paid(String acc_paid) {
        this.acc_paid = acc_paid;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}

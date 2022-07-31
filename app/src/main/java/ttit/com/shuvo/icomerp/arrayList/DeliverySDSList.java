package ttit.com.shuvo.icomerp.arrayList;

import java.util.ArrayList;

public class DeliverySDSList {
    private String deliveryNo;
    private String deliveryDate;
    private String deliveryAmnt;
    private String paidAmnt;
    private String deliveryBalance;
    private ArrayList<VoucherSDSList> voucherSDSLists;

    public DeliverySDSList(String deliveryNo, String deliveryDate, String deliveryAmnt, String paidAmnt, String deliveryBalance, ArrayList<VoucherSDSList> voucherSDSLists) {
        this.deliveryNo = deliveryNo;
        this.deliveryDate = deliveryDate;
        this.deliveryAmnt = deliveryAmnt;
        this.paidAmnt = paidAmnt;
        this.deliveryBalance = deliveryBalance;
        this.voucherSDSLists = voucherSDSLists;
    }

    public String getDeliveryNo() {
        return deliveryNo;
    }

    public void setDeliveryNo(String deliveryNo) {
        this.deliveryNo = deliveryNo;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryAmnt() {
        return deliveryAmnt;
    }

    public void setDeliveryAmnt(String deliveryAmnt) {
        this.deliveryAmnt = deliveryAmnt;
    }

    public String getPaidAmnt() {
        return paidAmnt;
    }

    public void setPaidAmnt(String paidAmnt) {
        this.paidAmnt = paidAmnt;
    }

    public String getDeliveryBalance() {
        return deliveryBalance;
    }

    public void setDeliveryBalance(String deliveryBalance) {
        this.deliveryBalance = deliveryBalance;
    }

    public ArrayList<VoucherSDSList> getVoucherSDSLists() {
        return voucherSDSLists;
    }

    public void setVoucherSDSLists(ArrayList<VoucherSDSList> voucherSDSLists) {
        this.voucherSDSLists = voucherSDSLists;
    }
}

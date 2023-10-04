package ttit.com.shuvo.terrainerp.arrayList;

import java.util.ArrayList;

public class OrderSDSList {
    private String orderNo;
    private String orderDate;
    private String orderAmnt;
    private String advanceAmnt;
    private String totalPaid;
    private String orderBalance;
    private ArrayList<DeliverySDSList> deliverySDSLists;

    public OrderSDSList(String orderNo, String orderDate, String orderAmnt, String advanceAmnt, String totalPaid, String orderBalance, ArrayList<DeliverySDSList> deliverySDSLists) {
        this.orderNo = orderNo;
        this.orderDate = orderDate;
        this.orderAmnt = orderAmnt;
        this.advanceAmnt = advanceAmnt;
        this.totalPaid = totalPaid;
        this.orderBalance = orderBalance;
        this.deliverySDSLists = deliverySDSLists;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderAmnt() {
        return orderAmnt;
    }

    public void setOrderAmnt(String orderAmnt) {
        this.orderAmnt = orderAmnt;
    }

    public String getAdvanceAmnt() {
        return advanceAmnt;
    }

    public void setAdvanceAmnt(String advanceAmnt) {
        this.advanceAmnt = advanceAmnt;
    }

    public String getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(String totalPaid) {
        this.totalPaid = totalPaid;
    }

    public String getOrderBalance() {
        return orderBalance;
    }

    public void setOrderBalance(String orderBalance) {
        this.orderBalance = orderBalance;
    }

    public ArrayList<DeliverySDSList> getDeliverySDSLists() {
        return deliverySDSLists;
    }

    public void setDeliverySDSLists(ArrayList<DeliverySDSList> deliverySDSLists) {
        this.deliverySDSLists = deliverySDSLists;
    }
}

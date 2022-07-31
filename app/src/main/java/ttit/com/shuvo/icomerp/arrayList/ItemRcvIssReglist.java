package ttit.com.shuvo.icomerp.arrayList;

import java.util.ArrayList;

public class ItemRcvIssReglist {

    private String slDate;
    private String rcvNo;
    private String issNo;
    private String transFlag;
    private String transSrc;
    private String supplier;
    private String customer;
    private ArrayList<ItemRcvIssItemDescList> itemRcvIssItemDescLists;

    public ItemRcvIssReglist(String slDate, String rcvNo, String issNo, String transFlag, String transSrc, String supplier, String customer, ArrayList<ItemRcvIssItemDescList> itemRcvIssItemDescLists) {
        this.slDate = slDate;
        this.rcvNo = rcvNo;
        this.issNo = issNo;
        this.transFlag = transFlag;
        this.transSrc = transSrc;
        this.supplier = supplier;
        this.customer = customer;
        this.itemRcvIssItemDescLists = itemRcvIssItemDescLists;
    }

    public String getSlDate() {
        return slDate;
    }

    public void setSlDate(String slDate) {
        this.slDate = slDate;
    }

    public String getRcvNo() {
        return rcvNo;
    }

    public void setRcvNo(String rcvNo) {
        this.rcvNo = rcvNo;
    }

    public String getIssNo() {
        return issNo;
    }

    public void setIssNo(String issNo) {
        this.issNo = issNo;
    }

    public String getTransFlag() {
        return transFlag;
    }

    public void setTransFlag(String transFlag) {
        this.transFlag = transFlag;
    }

    public String getTransSrc() {
        return transSrc;
    }

    public void setTransSrc(String transSrc) {
        this.transSrc = transSrc;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public ArrayList<ItemRcvIssItemDescList> getItemRcvIssItemDescLists() {
        return itemRcvIssItemDescLists;
    }

    public void setItemRcvIssItemDescLists(ArrayList<ItemRcvIssItemDescList> itemRcvIssItemDescLists) {
        this.itemRcvIssItemDescLists = itemRcvIssItemDescLists;
    }
}

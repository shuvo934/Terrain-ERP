package ttit.com.shuvo.terrainerp.arrayList;

import java.util.ArrayList;

public class ItemAllSubLists {

    private String subName;
    private String subId;
    private String totalQty;
    private String totalVal;
    private ArrayList<ItemAllLists> allLists;

    public ItemAllSubLists(String subName, String subId, String totalQty, String totalVal, ArrayList<ItemAllLists> allLists) {
        this.subName = subName;
        this.subId = subId;
        this.totalQty = totalQty;
        this.totalVal = totalVal;
        this.allLists = allLists;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public String getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        this.totalQty = totalQty;
    }

    public String getTotalVal() {
        return totalVal;
    }

    public void setTotalVal(String totalVal) {
        this.totalVal = totalVal;
    }

    public ArrayList<ItemAllLists> getAllLists() {
        return allLists;
    }

    public void setAllLists(ArrayList<ItemAllLists> allLists) {
        this.allLists = allLists;
    }
}

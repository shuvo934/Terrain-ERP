package ttit.com.shuvo.terrainerp.arrayList;

import java.util.ArrayList;

public class DeliveryChallanDetailsList {
    private String sm_id;
    private String sd_id;
    private String item_name;
    private String part_no;
    private String item_amnt;
    private String item_qty;
    private String item_id;
    private ArrayList<DCColorWiseItemList> dcColorWiseItemLists;
    private boolean updated;

    public DeliveryChallanDetailsList(String sm_id, String sd_id, String item_name, String part_no, String item_amnt, String item_qty, String item_id, ArrayList<DCColorWiseItemList> dcColorWiseItemLists, boolean updated) {
        this.sm_id = sm_id;
        this.sd_id = sd_id;
        this.item_name = item_name;
        this.part_no = part_no;
        this.item_amnt = item_amnt;
        this.item_qty = item_qty;
        this.item_id = item_id;
        this.dcColorWiseItemLists = dcColorWiseItemLists;
        this.updated = updated;
    }

    public String getSm_id() {
        return sm_id;
    }

    public void setSm_id(String sm_id) {
        this.sm_id = sm_id;
    }

    public String getSd_id() {
        return sd_id;
    }

    public void setSd_id(String sd_id) {
        this.sd_id = sd_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getPart_no() {
        return part_no;
    }

    public void setPart_no(String part_no) {
        this.part_no = part_no;
    }

    public String getItem_amnt() {
        return item_amnt;
    }

    public void setItem_amnt(String item_amnt) {
        this.item_amnt = item_amnt;
    }

    public String getItem_qty() {
        return item_qty;
    }

    public void setItem_qty(String item_qty) {
        this.item_qty = item_qty;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public ArrayList<DCColorWiseItemList> getDcColorWiseItemLists() {
        return dcColorWiseItemLists;
    }

    public void setDcColorWiseItemLists(ArrayList<DCColorWiseItemList> dcColorWiseItemLists) {
        this.dcColorWiseItemLists = dcColorWiseItemLists;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }
}

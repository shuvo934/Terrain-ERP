package ttit.com.shuvo.terrainerp.arrayList;

import java.util.ArrayList;

public class PurchaseRcvItemDetailsList {
    private String size_id;
    private String size_name;
    private String item;
    private String color_id;
    private String color_name;
    private String wod_qty;
    private String wod_item_id;
    private String rcv_qty;
    private String balance_qty;
    private String after_rcv_balance_qty;
    private String wod_rate;
    private String wod_actual_rate;
    private String wod_vat_pct;
    private String wod_vat_pct_amt;
    private String wod_id;
    private String item_unit;
    private String item_code;
    private String item_hs_code;
    private String item_part_number;
    private String item_wise_rcv_total_amnt;
    private String item_wise_rcv_total_vat;
    private ArrayList<PurchaseRcvWarehouseRcvLists> purchaseRcvWarehouseRcvLists;
    private boolean needToUpdate;
    private boolean updated;

    public PurchaseRcvItemDetailsList(String size_id, String size_name, String item, String color_id, String color_name,
                                      String wod_qty, String wod_item_id, String rcv_qty, String balance_qty, String after_rcv_balance_qty,
                                      String wod_rate, String wod_actual_rate, String wod_vat_pct, String wod_vat_pct_amt, String wod_id,
                                      String item_unit, String item_code, String item_hs_code, String item_part_number,
                                      String item_wise_rcv_total_amnt, String item_wise_rcv_total_vat,
                                      ArrayList<PurchaseRcvWarehouseRcvLists> purchaseRcvWarehouseRcvLists,
                                      boolean needToUpdate, boolean updated) {
        this.size_id = size_id;
        this.size_name = size_name;
        this.item = item;
        this.color_id = color_id;
        this.color_name = color_name;
        this.wod_qty = wod_qty;
        this.wod_item_id = wod_item_id;
        this.rcv_qty = rcv_qty;
        this.balance_qty = balance_qty;
        this.after_rcv_balance_qty = after_rcv_balance_qty;
        this.wod_rate = wod_rate;
        this.wod_actual_rate = wod_actual_rate;
        this.wod_vat_pct = wod_vat_pct;
        this.wod_vat_pct_amt = wod_vat_pct_amt;
        this.wod_id = wod_id;
        this.item_unit = item_unit;
        this.item_code = item_code;
        this.item_hs_code = item_hs_code;
        this.item_part_number = item_part_number;
        this.item_wise_rcv_total_amnt = item_wise_rcv_total_amnt;
        this.item_wise_rcv_total_vat = item_wise_rcv_total_vat;
        this.purchaseRcvWarehouseRcvLists = purchaseRcvWarehouseRcvLists;
        this.needToUpdate = needToUpdate;
        this.updated = updated;
    }

    public String getSize_id() {
        return size_id;
    }

    public void setSize_id(String size_id) {
        this.size_id = size_id;
    }

    public String getSize_name() {
        return size_name;
    }

    public void setSize_name(String size_name) {
        this.size_name = size_name;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getColor_id() {
        return color_id;
    }

    public void setColor_id(String color_id) {
        this.color_id = color_id;
    }

    public String getColor_name() {
        return color_name;
    }

    public void setColor_name(String color_name) {
        this.color_name = color_name;
    }

    public String getWod_qty() {
        return wod_qty;
    }

    public void setWod_qty(String wod_qty) {
        this.wod_qty = wod_qty;
    }

    public String getWod_item_id() {
        return wod_item_id;
    }

    public void setWod_item_id(String wod_item_id) {
        this.wod_item_id = wod_item_id;
    }

    public String getRcv_qty() {
        return rcv_qty;
    }

    public void setRcv_qty(String rcv_qty) {
        this.rcv_qty = rcv_qty;
    }

    public String getBalance_qty() {
        return balance_qty;
    }

    public void setBalance_qty(String balance_qty) {
        this.balance_qty = balance_qty;
    }

    public String getAfter_rcv_balance_qty() {
        return after_rcv_balance_qty;
    }

    public void setAfter_rcv_balance_qty(String after_rcv_balance_qty) {
        this.after_rcv_balance_qty = after_rcv_balance_qty;
    }

    public String getWod_rate() {
        return wod_rate;
    }

    public void setWod_rate(String wod_rate) {
        this.wod_rate = wod_rate;
    }

    public String getWod_actual_rate() {
        return wod_actual_rate;
    }

    public void setWod_actual_rate(String wod_actual_rate) {
        this.wod_actual_rate = wod_actual_rate;
    }

    public String getWod_vat_pct() {
        return wod_vat_pct;
    }

    public void setWod_vat_pct(String wod_vat_pct) {
        this.wod_vat_pct = wod_vat_pct;
    }

    public String getWod_vat_pct_amt() {
        return wod_vat_pct_amt;
    }

    public void setWod_vat_pct_amt(String wod_vat_pct_amt) {
        this.wod_vat_pct_amt = wod_vat_pct_amt;
    }

    public String getWod_id() {
        return wod_id;
    }

    public void setWod_id(String wod_id) {
        this.wod_id = wod_id;
    }

    public String getItem_unit() {
        return item_unit;
    }

    public void setItem_unit(String item_unit) {
        this.item_unit = item_unit;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public String getItem_hs_code() {
        return item_hs_code;
    }

    public void setItem_hs_code(String item_hs_code) {
        this.item_hs_code = item_hs_code;
    }

    public String getItem_part_number() {
        return item_part_number;
    }

    public void setItem_part_number(String item_part_number) {
        this.item_part_number = item_part_number;
    }

    public String getItem_wise_rcv_total_amnt() {
        return item_wise_rcv_total_amnt;
    }

    public void setItem_wise_rcv_total_amnt(String item_wise_rcv_total_amnt) {
        this.item_wise_rcv_total_amnt = item_wise_rcv_total_amnt;
    }

    public String getItem_wise_rcv_total_vat() {
        return item_wise_rcv_total_vat;
    }

    public void setItem_wise_rcv_total_vat(String item_wise_rcv_total_vat) {
        this.item_wise_rcv_total_vat = item_wise_rcv_total_vat;
    }

    public ArrayList<PurchaseRcvWarehouseRcvLists> getPurchaseRcvWarehouseRcvLists() {
        return purchaseRcvWarehouseRcvLists;
    }

    public void setPurchaseRcvWarehouseRcvLists(ArrayList<PurchaseRcvWarehouseRcvLists> purchaseRcvWarehouseRcvLists) {
        this.purchaseRcvWarehouseRcvLists = purchaseRcvWarehouseRcvLists;
    }

    public boolean isNeedToUpdate() {
        return needToUpdate;
    }

    public void setNeedToUpdate(boolean needToUpdate) {
        this.needToUpdate = needToUpdate;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }
}

package ttit.com.shuvo.terrainerp.arrayList;

import java.util.ArrayList;

public class InventoryLedgerList {

    private String item_id;
    private String item_name;
    private String item_code;
    private String cat_name;
    private String subCat_name;
    private String hs_code;
    private String unit;
    private String opening_date;
    private String opening_balance;
    private String total_rcv_qty;
    private String total_issue_qty;
    private String total_balance;
    private boolean updated;
    private ArrayList<InventoryLedgerItemList> inventoryLedgerItemLists;

    public InventoryLedgerList(String item_id, String item_name, String item_code, String cat_name, String subCat_name, String hs_code, String unit, String opening_date, String opening_balance, String total_rcv_qty, String total_issue_qty, String total_balance, boolean updated, ArrayList<InventoryLedgerItemList> inventoryLedgerItemLists) {
        this.item_id = item_id;
        this.item_name = item_name;
        this.item_code = item_code;
        this.cat_name = cat_name;
        this.subCat_name = subCat_name;
        this.hs_code = hs_code;
        this.unit = unit;
        this.opening_date = opening_date;
        this.opening_balance = opening_balance;
        this.total_rcv_qty = total_rcv_qty;
        this.total_issue_qty = total_issue_qty;
        this.total_balance = total_balance;
        this.updated = updated;
        this.inventoryLedgerItemLists = inventoryLedgerItemLists;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getSubCat_name() {
        return subCat_name;
    }

    public void setSubCat_name(String subCat_name) {
        this.subCat_name = subCat_name;
    }

    public String getHs_code() {
        return hs_code;
    }

    public void setHs_code(String hs_code) {
        this.hs_code = hs_code;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getOpening_date() {
        return opening_date;
    }

    public void setOpening_date(String opening_date) {
        this.opening_date = opening_date;
    }

    public String getOpening_balance() {
        return opening_balance;
    }

    public void setOpening_balance(String opening_balance) {
        this.opening_balance = opening_balance;
    }

    public String getTotal_rcv_qty() {
        return total_rcv_qty;
    }

    public void setTotal_rcv_qty(String total_rcv_qty) {
        this.total_rcv_qty = total_rcv_qty;
    }

    public String getTotal_issue_qty() {
        return total_issue_qty;
    }

    public void setTotal_issue_qty(String total_issue_qty) {
        this.total_issue_qty = total_issue_qty;
    }

    public String getTotal_balance() {
        return total_balance;
    }

    public void setTotal_balance(String total_balance) {
        this.total_balance = total_balance;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public ArrayList<InventoryLedgerItemList> getInventoryLedgerItemLists() {
        return inventoryLedgerItemLists;
    }

    public void setInventoryLedgerItemLists(ArrayList<InventoryLedgerItemList> inventoryLedgerItemLists) {
        this.inventoryLedgerItemLists = inventoryLedgerItemLists;
    }
}

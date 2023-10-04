package ttit.com.shuvo.terrainerp.arrayList;

import java.util.ArrayList;

public class ItemRcvIssItemDescList {

    private String itemId;
    private String itemName;
    private String unit;
    private String catName;
    private String subCatName;
    private boolean updated;
    private ArrayList<ItemRcvIssWarehouseList> itemRcvIssWarehouseLists;

    public ItemRcvIssItemDescList(String itemId, String itemName, String unit, String catName, String subCatName, boolean updated, ArrayList<ItemRcvIssWarehouseList> itemRcvIssWarehouseLists) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.unit = unit;
        this.catName = catName;
        this.subCatName = subCatName;
        this.updated = updated;
        this.itemRcvIssWarehouseLists = itemRcvIssWarehouseLists;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getSubCatName() {
        return subCatName;
    }

    public void setSubCatName(String subCatName) {
        this.subCatName = subCatName;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public ArrayList<ItemRcvIssWarehouseList> getItemRcvIssWarehouseLists() {
        return itemRcvIssWarehouseLists;
    }

    public void setItemRcvIssWarehouseLists(ArrayList<ItemRcvIssWarehouseList> itemRcvIssWarehouseLists) {
        this.itemRcvIssWarehouseLists = itemRcvIssWarehouseLists;
    }
}


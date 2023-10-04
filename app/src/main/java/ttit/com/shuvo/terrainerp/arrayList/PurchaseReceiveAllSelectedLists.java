package ttit.com.shuvo.terrainerp.arrayList;

import java.util.ArrayList;

public class PurchaseReceiveAllSelectedLists {
    private String cat_id;
    private String cat_name;
    private ArrayList<PurchaseRcvItemDetailsList> purchaseRcvItemDetailsLists;
    private boolean updated;

    public PurchaseReceiveAllSelectedLists(String cat_id, String cat_name, ArrayList<PurchaseRcvItemDetailsList> purchaseRcvItemDetailsLists,
                                           boolean updated) {
        this.cat_id = cat_id;
        this.cat_name = cat_name;
        this.purchaseRcvItemDetailsLists = purchaseRcvItemDetailsLists;
        this.updated = updated;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public ArrayList<PurchaseRcvItemDetailsList> getPurchaseRcvItemDetailsLists() {
        return purchaseRcvItemDetailsLists;
    }

    public void setPurchaseRcvItemDetailsLists(ArrayList<PurchaseRcvItemDetailsList> purchaseRcvItemDetailsLists) {
        this.purchaseRcvItemDetailsLists = purchaseRcvItemDetailsLists;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }
}

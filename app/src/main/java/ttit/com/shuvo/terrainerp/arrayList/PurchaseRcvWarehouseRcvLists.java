package ttit.com.shuvo.terrainerp.arrayList;

public class PurchaseRcvWarehouseRcvLists {
    private String wh_rack_name;
    private String whd_id;
    private String whm_id;
    private String rcv_qty;
    private String item_id;
    private boolean updated;

    public PurchaseRcvWarehouseRcvLists(String wh_rack_name, String whd_id, String whm_id, String rcv_qty, String item_id, boolean updated) {
        this.wh_rack_name = wh_rack_name;
        this.whd_id = whd_id;
        this.whm_id = whm_id;
        this.rcv_qty = rcv_qty;
        this.item_id = item_id;
        this.updated = updated;
    }

    public String getWh_rack_name() {
        return wh_rack_name;
    }

    public void setWh_rack_name(String wh_rack_name) {
        this.wh_rack_name = wh_rack_name;
    }

    public String getWhd_id() {
        return whd_id;
    }

    public void setWhd_id(String whd_id) {
        this.whd_id = whd_id;
    }

    public String getWhm_id() {
        return whm_id;
    }

    public void setWhm_id(String whm_id) {
        this.whm_id = whm_id;
    }

    public String getRcv_qty() {
        return rcv_qty;
    }

    public void setRcv_qty(String rcv_qty) {
        this.rcv_qty = rcv_qty;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }
}

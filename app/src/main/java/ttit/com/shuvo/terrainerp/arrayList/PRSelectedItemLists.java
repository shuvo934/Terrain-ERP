package ttit.com.shuvo.terrainerp.arrayList;

public class PRSelectedItemLists {

    private String im_name;
    private String item_reference_name;
    private String item_barcode_no;
    private String item_hs_code;
    private String item_part_number;
    private String item_id;
    private String im_id;
    private String item_unit;
    private String item_code;
    private String item_color_id;
    private String item_size_id;
    private String stock;
    private String ccppd_id;
    private String reqQty;
    private String prd_id;
    private boolean updated;

    public PRSelectedItemLists(String im_name, String item_reference_name, String item_barcode_no, String item_hs_code, String item_part_number, String item_id, String im_id, String item_unit, String item_code, String item_color_id, String item_size_id, String stock, String ccppd_id, String reqQty, String prd_id, boolean updated) {
        this.im_name = im_name;
        this.item_reference_name = item_reference_name;
        this.item_barcode_no = item_barcode_no;
        this.item_hs_code = item_hs_code;
        this.item_part_number = item_part_number;
        this.item_id = item_id;
        this.im_id = im_id;
        this.item_unit = item_unit;
        this.item_code = item_code;
        this.item_color_id = item_color_id;
        this.item_size_id = item_size_id;
        this.stock = stock;
        this.ccppd_id = ccppd_id;
        this.reqQty = reqQty;
        this.prd_id = prd_id;
        this.updated = updated;
    }

    public String getIm_name() {
        return im_name;
    }

    public void setIm_name(String im_name) {
        this.im_name = im_name;
    }

    public String getItem_reference_name() {
        return item_reference_name;
    }

    public void setItem_reference_name(String item_reference_name) {
        this.item_reference_name = item_reference_name;
    }

    public String getItem_barcode_no() {
        return item_barcode_no;
    }

    public void setItem_barcode_no(String item_barcode_no) {
        this.item_barcode_no = item_barcode_no;
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

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getIm_id() {
        return im_id;
    }

    public void setIm_id(String im_id) {
        this.im_id = im_id;
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

    public String getItem_color_id() {
        return item_color_id;
    }

    public void setItem_color_id(String item_color_id) {
        this.item_color_id = item_color_id;
    }

    public String getItem_size_id() {
        return item_size_id;
    }

    public void setItem_size_id(String item_size_id) {
        this.item_size_id = item_size_id;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getCcppd_id() {
        return ccppd_id;
    }

    public void setCcppd_id(String ccppd_id) {
        this.ccppd_id = ccppd_id;
    }

    public String getReqQty() {
        return reqQty;
    }

    public void setReqQty(String reqQty) {
        this.reqQty = reqQty;
    }

    public String getPrd_id() {
        return prd_id;
    }

    public void setPrd_id(String prd_id) {
        this.prd_id = prd_id;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }
}

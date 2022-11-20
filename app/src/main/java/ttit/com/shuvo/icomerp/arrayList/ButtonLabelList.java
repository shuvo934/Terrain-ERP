package ttit.com.shuvo.icomerp.arrayList;

public class ButtonLabelList {
    private int msma_id;
    private int android_id;
    private String label_name;

    public ButtonLabelList(int msma_id, int android_id, String label_name) {
        this.msma_id = msma_id;
        this.android_id = android_id;
        this.label_name = label_name;
    }

    public int getMsma_id() {
        return msma_id;
    }

    public void setMsma_id(int msma_id) {
        this.msma_id = msma_id;
    }

    public int getAndroid_id() {
        return android_id;
    }

    public void setAndroid_id(int android_id) {
        this.android_id = android_id;
    }

    public String getLabel_name() {
        return label_name;
    }

    public void setLabel_name(String label_name) {
        this.label_name = label_name;
    }
}

package ttit.com.shuvo.terrainerp.arrayList;

public class UserAccessList {
    private String msma_id;
    private String msma_label_name;
    private String msma_view;

    public UserAccessList(String msma_id, String msma_label_name, String msma_view) {
        this.msma_id = msma_id;
        this.msma_label_name = msma_label_name;
        this.msma_view = msma_view;
    }

    public String getMsma_id() {
        return msma_id;
    }

    public void setMsma_id(String msma_id) {
        this.msma_id = msma_id;
    }

    public String getMsma_label_name() {
        return msma_label_name;
    }

    public void setMsma_label_name(String msma_label_name) {
        this.msma_label_name = msma_label_name;
    }

    public String getMsma_view() {
        return msma_view;
    }

    public void setMsma_view(String msma_view) {
        this.msma_view = msma_view;
    }
}

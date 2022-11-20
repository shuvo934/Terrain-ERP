package ttit.com.shuvo.icomerp.arrayList;

public class UserGroupModuleList {
    private String msma_id;
    private String msma_name;
    private boolean active;

    public UserGroupModuleList(String msma_id, String msma_name, boolean active) {
        this.msma_id = msma_id;
        this.msma_name = msma_name;
        this.active = active;
    }

    public String getMsma_id() {
        return msma_id;
    }

    public void setMsma_id(String msma_id) {
        this.msma_id = msma_id;
    }

    public String getMsma_name() {
        return msma_name;
    }

    public void setMsma_name(String msma_name) {
        this.msma_name = msma_name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

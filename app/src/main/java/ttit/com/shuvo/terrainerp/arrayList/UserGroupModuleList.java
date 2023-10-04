package ttit.com.shuvo.terrainerp.arrayList;

public class UserGroupModuleList {
    private String msma_id;
    private String msma_name;
    private boolean active;
    private boolean updated;

    public UserGroupModuleList(String msma_id, String msma_name, boolean active,boolean updated) {
        this.msma_id = msma_id;
        this.msma_name = msma_name;
        this.active = active;
        this.updated = updated;
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

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }
}

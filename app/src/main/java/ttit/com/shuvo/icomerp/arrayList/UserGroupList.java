package ttit.com.shuvo.icomerp.arrayList;

import java.util.ArrayList;

public class UserGroupList {
    private String group_id;
    private String group_name;
    private boolean activated;
    private int activeValue;
    private ArrayList<UserGroupModuleList> userGroupModuleLists;

    public UserGroupList(String group_id, String group_name, boolean activated, int activeValue, ArrayList<UserGroupModuleList> userGroupModuleLists) {
        this.group_id = group_id;
        this.group_name = group_name;
        this.activated = activated;
        this.activeValue = activeValue;
        this.userGroupModuleLists = userGroupModuleLists;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public ArrayList<UserGroupModuleList> getUserGroupModuleLists() {
        return userGroupModuleLists;
    }

    public void setUserGroupModuleLists(ArrayList<UserGroupModuleList> userGroupModuleLists) {
        this.userGroupModuleLists = userGroupModuleLists;
    }


    public int getActiveValue() {
        return activeValue;
    }

    public void setActiveValue(int activeValue) {
        this.activeValue = activeValue;
    }
}

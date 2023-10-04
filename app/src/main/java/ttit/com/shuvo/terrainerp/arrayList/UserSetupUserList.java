package ttit.com.shuvo.terrainerp.arrayList;

public class UserSetupUserList {
    private String user_id;
    private String user_name;
    private String user_f_name;
    private String user_l_name;

    public UserSetupUserList(String user_id, String user_name, String user_f_name, String user_l_name) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_f_name = user_f_name;
        this.user_l_name = user_l_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_f_name() {
        return user_f_name;
    }

    public void setUser_f_name(String user_f_name) {
        this.user_f_name = user_f_name;
    }

    public String getUser_l_name() {
        return user_l_name;
    }

    public void setUser_l_name(String user_l_name) {
        this.user_l_name = user_l_name;
    }
}

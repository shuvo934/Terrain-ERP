package ttit.com.shuvo.terrainerp.arrayList;

public class DesignatedUserList {
    private String user_name;
    private String usr_name;
    private String usr_email;
    private String usr_contact;
    private String emp_name;
    private String emp_code;
    private String job_calling_title;
    private String dept_name;
    private String emp_id;
    private String jsm_dept_id;
    private String jsm_id;

    public DesignatedUserList(String user_name, String usr_name, String usr_email, String usr_contact, String emp_name, String emp_code, String job_calling_title, String dept_name, String emp_id, String jsm_dept_id, String jsm_id) {
        this.user_name = user_name;
        this.usr_name = usr_name;
        this.usr_email = usr_email;
        this.usr_contact = usr_contact;
        this.emp_name = emp_name;
        this.emp_code = emp_code;
        this.job_calling_title = job_calling_title;
        this.dept_name = dept_name;
        this.emp_id = emp_id;
        this.jsm_dept_id = jsm_dept_id;
        this.jsm_id = jsm_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUsr_name() {
        return usr_name;
    }

    public void setUsr_name(String usr_name) {
        this.usr_name = usr_name;
    }

    public String getUsr_email() {
        return usr_email;
    }

    public void setUsr_email(String usr_email) {
        this.usr_email = usr_email;
    }

    public String getUsr_contact() {
        return usr_contact;
    }

    public void setUsr_contact(String usr_contact) {
        this.usr_contact = usr_contact;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getEmp_code() {
        return emp_code;
    }

    public void setEmp_code(String emp_code) {
        this.emp_code = emp_code;
    }

    public String getJob_calling_title() {
        return job_calling_title;
    }

    public void setJob_calling_title(String job_calling_title) {
        this.job_calling_title = job_calling_title;
    }

    public String getDept_name() {
        return dept_name;
    }

    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getJsm_dept_id() {
        return jsm_dept_id;
    }

    public void setJsm_dept_id(String jsm_dept_id) {
        this.jsm_dept_id = jsm_dept_id;
    }

    public String getJsm_id() {
        return jsm_id;
    }

    public void setJsm_id(String jsm_id) {
        this.jsm_id = jsm_id;
    }
}

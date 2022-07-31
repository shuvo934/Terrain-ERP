package ttit.com.shuvo.icomerp.arrayList;

public class PartyContactList {
    private String contactPerson;
    private String designation;
    private String department;
    private String contactNo;
    private String emailAddress;

    public PartyContactList(String contactPerson, String designation, String department, String contactNo, String emailAddress) {
        this.contactPerson = contactPerson;
        this.designation = designation;
        this.department = department;
        this.contactNo = contactNo;
        this.emailAddress = emailAddress;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}

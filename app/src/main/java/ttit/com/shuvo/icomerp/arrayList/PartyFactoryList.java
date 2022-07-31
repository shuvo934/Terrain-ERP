package ttit.com.shuvo.icomerp.arrayList;

public class PartyFactoryList {
    private String factoryName;
    private String factoryAddress;
    private String factoryContact;

    public PartyFactoryList(String factoryName, String factoryAddress, String factoryContact) {
        this.factoryName = factoryName;
        this.factoryAddress = factoryAddress;
        this.factoryContact = factoryContact;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getFactoryAddress() {
        return factoryAddress;
    }

    public void setFactoryAddress(String factoryAddress) {
        this.factoryAddress = factoryAddress;
    }

    public String getFactoryContact() {
        return factoryContact;
    }

    public void setFactoryContact(String factoryContact) {
        this.factoryContact = factoryContact;
    }
}

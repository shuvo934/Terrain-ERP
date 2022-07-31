package ttit.com.shuvo.icomerp.arrayList;

import java.util.ArrayList;

public class ClientSDSList {
    private String clientName;
    private ArrayList<OrderSDSList> orderSDSLists;

    public ClientSDSList(String clientName, ArrayList<OrderSDSList> orderSDSLists) {
        this.clientName = clientName;
        this.orderSDSLists = orderSDSLists;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public ArrayList<OrderSDSList> getOrderSDSLists() {
        return orderSDSLists;
    }

    public void setOrderSDSLists(ArrayList<OrderSDSList> orderSDSLists) {
        this.orderSDSLists = orderSDSLists;
    }
}

package ttit.com.shuvo.icomerp.arrayList;

import java.util.ArrayList;

public class VoucherLists1 {
    private String vDate;
    private ArrayList<VoucherLists2> voucherLists2s;

    public VoucherLists1(String vDate, ArrayList<VoucherLists2> voucherLists2s) {
        this.vDate = vDate;
        this.voucherLists2s = voucherLists2s;
    }

    public String getvDate() {
        return vDate;
    }

    public void setvDate(String vDate) {
        this.vDate = vDate;
    }

    public ArrayList<VoucherLists2> getVoucherLists2s() {
        return voucherLists2s;
    }

    public void setVoucherLists2s(ArrayList<VoucherLists2> voucherLists2s) {
        this.voucherLists2s = voucherLists2s;
    }
}

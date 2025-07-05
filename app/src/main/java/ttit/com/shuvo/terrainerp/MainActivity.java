package ttit.com.shuvo.terrainerp;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import java.util.ArrayList;

import ttit.com.shuvo.terrainerp.arrayList.ButtonLabelList;
import ttit.com.shuvo.terrainerp.login.Login;
import ttit.com.shuvo.terrainerp.mainBoard.MainMenu;

public class MainActivity extends AppCompatActivity {

    private final Handler mHandler = new Handler();

    SharedPreferences sharedPreferences;
    boolean loginfile = false;

    public static final String LOGIN_ACTIVITY_FILE = "LOGIN_ACTIVITY_FILE_ERP";
    public static final String LOGIN_TF = "TRUE_FALSE";

    public static ArrayList<ButtonLabelList> buttonLabelLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(LOGIN_ACTIVITY_FILE, MODE_PRIVATE);
        loginfile = sharedPreferences.getBoolean(LOGIN_TF,false);

        buttonSetup();
        System.out.println(loginfile);

        goToActivityMap();
    }

    private void goToActivityMap() {
        mHandler.postDelayed(() -> {
            Intent intent;
            if (loginfile) {
                intent = new Intent(MainActivity.this, MainMenu.class);
            } else {
                intent = new Intent(MainActivity.this, Login.class);
            }
            startActivity(intent);
            finish();

        }, 2500);
    }

    public void buttonSetup() {
        buttonLabelLists = new ArrayList<>();

        buttonLabelLists.add(new ButtonLabelList(1,R.id.work_order,getString(R.string.msma_1)));
        buttonLabelLists.add(new ButtonLabelList(2,R.id.purchase_req_approval,getString(R.string.msma_2)));
        buttonLabelLists.add(new ButtonLabelList(3,R.id.purchase_order_approval,getString(R.string.msma_3)));
        buttonLabelLists.add(new ButtonLabelList(4,R.id.receive,getString(R.string.msma_4)));
        buttonLabelLists.add(new ButtonLabelList(5,R.id.stock_qty,getString(R.string.msma_5)));
        buttonLabelLists.add(new ButtonLabelList(6,R.id.stock_qty_item_wise,getString(R.string.msma_6)));
        buttonLabelLists.add(new ButtonLabelList(7,R.id.re_order,getString(R.string.msma_7)));
        buttonLabelLists.add(new ButtonLabelList(8,R.id.relations_of,getString(R.string.msma_8)));
        buttonLabelLists.add(new ButtonLabelList(9,R.id.stock_ledger,getString(R.string.msma_9)));
        buttonLabelLists.add(new ButtonLabelList(10,R.id.inventory_ledger_book,getString(R.string.msma_10)));
        buttonLabelLists.add(new ButtonLabelList(11,R.id.item_receive_issue_reg,getString(R.string.msma_11)));
        buttonLabelLists.add(new ButtonLabelList(12,R.id.all_items,getString(R.string.msma_12)));
        buttonLabelLists.add(new ButtonLabelList(13,R.id.dashboard,getString(R.string.msma_13)));
        buttonLabelLists.add(new ButtonLabelList(14,R.id.performance_report,getString(R.string.msma_14)));
        buttonLabelLists.add(new ButtonLabelList(15,R.id.user_setup,getString(R.string.msma_15)));
        buttonLabelLists.add(new ButtonLabelList(16,R.id.sales_order,getString(R.string.msma_16)));
        buttonLabelLists.add(new ButtonLabelList(17,R.id.deliver_challan,getString(R.string.msma_17)));
        buttonLabelLists.add(new ButtonLabelList(18,R.id.sales_order_delivery_challan,getString(R.string.msma_18)));
        buttonLabelLists.add(new ButtonLabelList(19,R.id.top_n_item,getString(R.string.msma_19)));
        buttonLabelLists.add(new ButtonLabelList(20,R.id.product_wise_assessment,getString(R.string.msma_20)));
        buttonLabelLists.add(new ButtonLabelList(21,R.id.graphical_summary,getString(R.string.msma_21)));
        buttonLabelLists.add(new ButtonLabelList(22,R.id.chart_of_acc,getString(R.string.msma_22)));
        buttonLabelLists.add(new ButtonLabelList(23,R.id.credit_voucher_approved,getString(R.string.msma_23)));
        buttonLabelLists.add(new ButtonLabelList(24,R.id.debit_voucher_approved,getString(R.string.msma_24)));
        buttonLabelLists.add(new ButtonLabelList(25,R.id.journal_voucher_approved,getString(R.string.msma_25)));
        buttonLabelLists.add(new ButtonLabelList(26,R.id.voucher_transaction,getString(R.string.msma_26)));
        buttonLabelLists.add(new ButtonLabelList(27,R.id.accnt_ledger,getString(R.string.msma_27)));
        buttonLabelLists.add(new ButtonLabelList(28,R.id.party_summary_ledger,getString(R.string.msma_28)));
        buttonLabelLists.add(new ButtonLabelList(29,R.id.party_pay_recv_summary_ledger,getString(R.string.msma_29)));
        buttonLabelLists.add(new ButtonLabelList(30,R.id.bank_balance,getString(R.string.msma_30)));
        buttonLabelLists.add(new ButtonLabelList(31,R.id.cash_trans,getString(R.string.msma_31)));
        buttonLabelLists.add(new ButtonLabelList(32,R.id.party_all_list,getString(R.string.msma_32)));
        buttonLabelLists.add(new ButtonLabelList(33,R.id.purchase_requisition,getString(R.string.msma_33)));
        buttonLabelLists.add(new ButtonLabelList(34,R.id.purchase_order,getString(R.string.msma_34)));
        buttonLabelLists.add(new ButtonLabelList(35,R.id.purchase_receive,getString(R.string.msma_35)));
    }
}
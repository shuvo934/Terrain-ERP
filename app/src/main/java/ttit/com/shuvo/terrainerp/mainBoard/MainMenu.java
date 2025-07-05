package ttit.com.shuvo.terrainerp.mainBoard;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ttit.com.shuvo.terrainerp.WaitProgress;
import ttit.com.shuvo.terrainerp.arrayList.UserAccessList;
import ttit.com.shuvo.terrainerp.arrayList.UserDesignation;
import ttit.com.shuvo.terrainerp.arrayList.UserInfoList;
import ttit.com.shuvo.terrainerp.fragments.AccountLedger;
import ttit.com.shuvo.terrainerp.fragments.AllItemList;
import ttit.com.shuvo.terrainerp.fragments.BankBalance;
import ttit.com.shuvo.terrainerp.fragments.CashTransaction;
import ttit.com.shuvo.terrainerp.fragments.ChartAccounts;
import ttit.com.shuvo.terrainerp.fragments.CreditVoucherApproved;
import ttit.com.shuvo.terrainerp.fragments.DebitVoucherApproved;
import ttit.com.shuvo.terrainerp.fragments.DeliveryFragement;
import ttit.com.shuvo.terrainerp.fragments.GraphicalSummary;
import ttit.com.shuvo.terrainerp.fragments.InventoryLedgerFragment;
import ttit.com.shuvo.terrainerp.fragments.ItemRCVISSUERegister;
import ttit.com.shuvo.terrainerp.fragments.ItemWiseStockFragment;
import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.fragments.DashboardFragment;
import ttit.com.shuvo.terrainerp.fragments.JournalVoucherApproved;
import ttit.com.shuvo.terrainerp.fragments.PurchaseReceive;
import ttit.com.shuvo.terrainerp.fragments.PartyAll;
import ttit.com.shuvo.terrainerp.fragments.PartyPayableReceivable;
import ttit.com.shuvo.terrainerp.fragments.PartySummaryLedger;
import ttit.com.shuvo.terrainerp.fragments.PerformanceFragment;
import ttit.com.shuvo.terrainerp.fragments.ProductWiseAssess;
import ttit.com.shuvo.terrainerp.fragments.PurchaseOrder;
import ttit.com.shuvo.terrainerp.fragments.PurchaseOrderApproved;
import ttit.com.shuvo.terrainerp.fragments.PurchaseReqApproved;
import ttit.com.shuvo.terrainerp.fragments.PurchaseRequisition;
import ttit.com.shuvo.terrainerp.fragments.ReOrderFragment;
import ttit.com.shuvo.terrainerp.fragments.ReceiveFragment;
import ttit.com.shuvo.terrainerp.fragments.RelationsFragment;
import ttit.com.shuvo.terrainerp.fragments.SalesOrderDeliverySummary;
import ttit.com.shuvo.terrainerp.fragments.SalesOrderFragment;
import ttit.com.shuvo.terrainerp.fragments.StockFragment;
import ttit.com.shuvo.terrainerp.fragments.StockLedgerFragment;
import ttit.com.shuvo.terrainerp.fragments.TopNItem;
import ttit.com.shuvo.terrainerp.fragments.UserSetupFragment;
import ttit.com.shuvo.terrainerp.fragments.VoucherTransaction;
import ttit.com.shuvo.terrainerp.fragments.WorkOrderFragment;
import ttit.com.shuvo.terrainerp.login.Login;

import static ttit.com.shuvo.terrainerp.MainActivity.buttonLabelLists;
import static ttit.com.shuvo.terrainerp.login.Login.CompanyName;
import static ttit.com.shuvo.terrainerp.login.Login.SoftwareName;
import static ttit.com.shuvo.terrainerp.login.Login.isApproved;
import static ttit.com.shuvo.terrainerp.login.Login.isLeaveApproved;
import static ttit.com.shuvo.terrainerp.login.Login.userAccessLists;
import static ttit.com.shuvo.terrainerp.login.Login.userDesignations;
import static ttit.com.shuvo.terrainerp.login.Login.userInfoLists;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    DrawerLayout drawerLayout;
    NavigationView navigationView;

    TextView appName;
    ImageView tool;
    ImageView logout;
    String emp_id = "";

    WaitProgress waitProgress = new WaitProgress();
//    private String message = null;
    private Boolean conn = false;
//    private Boolean infoConnected = false;
    private Boolean connected = false;

//    private Boolean getConn = false;
//    private Boolean getConnected = false;

//    private Connection connection;

    String android_id = "";
    String model = "";
    String brand = "";
    String ipAddress = "";
    String hostUserName = "";
    String osName = "";

    SharedPreferences sharedPreferences;
    public static final String LOGIN_ACTIVITY_FILE = "LOGIN_ACTIVITY_FILE_ERP";

    public static final String USER_NAME = "USER_NAME";
    public static final String USER_F_NAME = "USER_F_NAME";
    public static final String USER_L_NAME = "USER_L_NAME";
    public static final String EMAIL = "EMAIL";
    public static final String CONTACT = "CONTACT";
    public static final String EMP_ID_LOGIN = "EMP_ID";
    public static final String USER_ID = "USER_ID";

    public static final String JSM_CODE = "JSM_CODE";
    public static final String JSM_NAME = "JSM_NAME";
    public static final String JSD_ID_LOGIN = "JSD_ID";
    public static final String JSD_OBJECTIVE = "JSD_OBJECTIVE";
    public static final String DEPT_NAME = "DEPT_NAME";
    public static final String DIV_NAME = "DIV_NAME";
    public static final String DESG_NAME = "DESG_NAME";
    public static final String DESG_PRIORITY = "DESG_PRIORITY";
    public static final String JOINING_DATE = "JOINING_DATE";
    public static final String DIV_ID = "DIV_ID";
    public static final String LOGIN_TF = "TRUE_FALSE";

    public static final String IS_ATT_APPROVED = "IS_ATT_APPROVED";
    public static final String IS_LEAVE_APPROVED = "IS_LEAVE_APPROVED";
    public static final String COMPANY = "COMPANY";
    public static final String SOFTWARE = "SOFTWARE";
    public static final String LIVE_FLAG = "LIVE_FLAG";
    Bundle bundle = null;
    public static boolean dashboardFront = false;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_menu_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bundle = savedInstanceState;
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation);
        tool = findViewById(R.id.nav_icon_main_menu);
        logout = findViewById(R.id.log_out_icon_main_menu);
        appName = findViewById(R.id.thrm_app_name_main_menu);

        sharedPreferences = getSharedPreferences(LOGIN_ACTIVITY_FILE, MODE_PRIVATE);
        boolean loginfile = sharedPreferences.getBoolean(LOGIN_TF,false);

        if(loginfile) {
            System.out.println(loginfile +" PAISE");

            String userName = sharedPreferences.getString(USER_NAME, null);
            String userFname = sharedPreferences.getString(USER_F_NAME, null);
            String userLname = sharedPreferences.getString(USER_L_NAME,null);
            String email = sharedPreferences.getString(EMAIL,null);
            String contact = sharedPreferences.getString(CONTACT, null);
            String emp_id_login = sharedPreferences.getString(EMP_ID_LOGIN,null);
            String user_id = sharedPreferences.getString(USER_ID,null);

            userInfoLists = new ArrayList<>();
            userInfoLists.add(new UserInfoList(userName,userFname,userLname,email,contact,emp_id_login,user_id));

            String jsm_code = sharedPreferences.getString(JSM_CODE, null);
            String jsm_name = sharedPreferences.getString(JSM_NAME, null);
            String jsd_id = sharedPreferences.getString(JSD_ID_LOGIN,null);
            String jsd_obj = sharedPreferences.getString(JSD_OBJECTIVE,null);
            String dept_name = sharedPreferences.getString(DEPT_NAME, null);
            String div_name = sharedPreferences.getString(DIV_NAME, null);
            String desg_name = sharedPreferences.getString(DESG_NAME, null);
            String desg_priority = sharedPreferences.getString(DESG_PRIORITY,null);
            String joining = sharedPreferences.getString(JOINING_DATE, null);
            String div_id = sharedPreferences.getString(DIV_ID,null);

            userDesignations = new ArrayList<>();
            userDesignations.add(new UserDesignation(jsm_code,jsm_name,jsd_id,jsd_obj,dept_name,div_name,desg_name,desg_priority,joining,div_id));

            SoftwareName = sharedPreferences.getString(SOFTWARE, null);
            CompanyName = sharedPreferences.getString(COMPANY,null);

            isApproved = sharedPreferences.getInt(IS_ATT_APPROVED,0);
            isLeaveApproved = sharedPreferences.getInt(IS_LEAVE_APPROVED,0);

        }

        emp_id = userInfoLists.get(0).getEmp_id();

//        if (!userInfoLists.isEmpty()) {
//            String firstname = userInfoLists.get(0).getUser_fname();
//            String lastName = userInfoLists.get(0).getUser_lname();
//            if (firstname == null) {
//                firstname = "";
//            }
//            if (lastName == null) {
//                lastName = "";
//            }
//            String empFullName = firstname+" "+lastName;
//            userName.setText(empFullName);
//        }

//        if (!userDesignations.isEmpty()) {
//            String jsmName = userDesignations.get(0).getJsm_name();
//            if (jsmName == null) {
//                jsmName = "";
//            }
//            //designation.setText(jsmName);
//
//            String deptName = userDesignations.get(0).getDiv_name();
//            if (deptName == null) {
//                deptName = "";
//            }
//            //department.setText(deptName);
//        }

        appName.setText(SoftwareName);
        //comp.setText(CompanyName);

        model = android.os.Build.MODEL;

        brand = Build.BRAND;

        ipAddress = getIPAddress(true);

        hostUserName = getHostName("localhost");

        StringBuilder builder = new StringBuilder();
        builder.append("ANDROID: ").append(Build.VERSION.RELEASE);

        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException | IllegalAccessException | NullPointerException e) {
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                builder.append(": ").append(fieldName);
                //builder.append(" : ").append(fieldName).append(" : ");
                //builder.append("sdk=").append(fieldValue);
            }
        }

        System.out.println("OS: " + builder.toString());
        //Log.d(LOG_TAG, "OS: " + builder.toString());

        //System.out.println("HOSTTTTT: " + getHostName());

        osName = builder.toString();

        android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainMenu.this);
                builder.setTitle("LOG OUT!")
                        .setMessage("Do you want to Log Out?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                userInfoLists.clear();
                                userDesignations.clear();
                                userInfoLists = new ArrayList<>();
                                userDesignations = new ArrayList<>();
                                isApproved = 0;
                                isLeaveApproved = 0;

                                SharedPreferences.Editor editor1 = sharedPreferences.edit();
                                editor1.remove(USER_NAME);
                                editor1.remove(USER_F_NAME);
                                editor1.remove(USER_L_NAME);
                                editor1.remove(EMAIL);
                                editor1.remove(CONTACT);
                                editor1.remove(EMP_ID_LOGIN);
                                editor1.remove(USER_ID);

                                editor1.remove(JSM_CODE);
                                editor1.remove(JSM_NAME);
                                editor1.remove(JSD_ID_LOGIN);
                                editor1.remove(JSD_OBJECTIVE);
                                editor1.remove(DEPT_NAME);
                                editor1.remove(DIV_NAME);
                                editor1.remove(DESG_NAME);
                                editor1.remove(DESG_PRIORITY);
                                editor1.remove(JOINING_DATE);
                                editor1.remove(DIV_ID);
                                editor1.remove(LOGIN_TF);

                                editor1.remove(IS_ATT_APPROVED);
                                editor1.remove(IS_LEAVE_APPROVED);
                                editor1.remove(COMPANY);
                                editor1.remove(SOFTWARE);
                                editor1.remove(LIVE_FLAG);
                                editor1.apply();
                                editor1.commit();

                                Intent intent = new Intent(MainMenu.this, Login.class);
                                startActivity(intent);
                                finish();
                                //System.exit(0);
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.END)){
                    drawerLayout.closeDrawer(GravityCompat.END);
                }
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout , R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        View headerView = navigationView.getHeaderView(0);

        ImageView imageView = (ImageView) headerView.findViewById(R.id.draw_back_icon_main_menu1);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.END);
            }
        });



//                dataSet.setValueFormatter(new PercentFormatter(pieChart));
//        dataSet.setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value) {
//                return String.valueOf((int) Math.floor(value));
//            }
//        });
//        new CheckLogin().execute();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.closeDrawer(GravityCompat.END);
                } else {
                    if (!dashboardFront) {
                        if (bundle == null) {
                            dashboardFront = true;
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DashboardFragment()).commit();
                        }
                    }
                    else {
                        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(MainMenu.this)
                                .setTitle("EXIT!")
                                .setMessage("Do You Want to Exit?")
                                .setIcon(R.drawable.erp)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        System.exit(0);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Do nothing
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                }
            }
        });
        loginLog();

    }

    public void userCheck() {
        Menu menu = navigationView.getMenu();
        String dd = String.valueOf(R.string.msma_1);
        System.out.println(dd);
        //userAccessLists;
        for (int i = 0 ; i < buttonLabelLists.size(); i++) {
            menu.findItem(buttonLabelLists.get(i).getAndroid_id()).setVisible(false);
        }
        if (userAccessLists != null) {
            if (userAccessLists.size() != 0) {
                for (int i = 0 ; i < buttonLabelLists.size(); i++) {
                    for (int j = 0; j < userAccessLists.size(); j++) {
                        if (Integer.parseInt(userAccessLists.get(j).getMsma_id()) == buttonLabelLists.get(i).getMsma_id()) {
                            if (Integer.parseInt(userAccessLists.get(j).getMsma_view()) == 1) {
                                menu.findItem(buttonLabelLists.get(i).getAndroid_id()).setVisible(true);
                            }
                        }
                    }
                }
            }
            else {
                for (int i = 0 ; i < buttonLabelLists.size(); i++) {
                    menu.findItem(buttonLabelLists.get(i).getAndroid_id()).setVisible(true);
                }
            }
        }
        else {
            for (int i = 0 ; i < buttonLabelLists.size(); i++) {
                menu.findItem(buttonLabelLists.get(i).getAndroid_id()).setVisible(true);
            }
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dashboard:
                //Toast.makeText(getApplicationContext(),"Dashboard",Toast.LENGTH_SHORT).show();
                dashboardFront = true;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DashboardFragment()).commit();
                //navigationView.setCheckedItem(item.getItemId());
                //navigationView.setCheckedItem(item);
                item.setChecked(true);
                break;
            case R.id.work_order:
                //Toast.makeText(getApplicationContext(),"Work Order",Toast.LENGTH_SHORT).show();
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new WorkOrderFragment()).commit();
                //navigationView.setCheckedItem(item.getItemId());
                //navigationView.setCheckedItem(item);
                item.setChecked(true);
                break;
            case R.id.receive:
                //Toast.makeText(getApplicationContext(),"Receive",Toast.LENGTH_SHORT).show();
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ReceiveFragment()).commit();
                //navigationView.setCheckedItem(item.getItemId());
                item.setChecked(true);
                break;
            case R.id.stock_qty:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new StockFragment()).commit();
                item.setChecked(true);
                break;
            case R.id.re_order:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ReOrderFragment()).commit();
                item.setChecked(true);
                break;
            case R.id.stock_qty_item_wise:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ItemWiseStockFragment()).commit();
                item.setChecked(true);
                break;
            case R.id.stock_ledger:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new StockLedgerFragment()).commit();
                item.setChecked(true);
                break;
            case R.id.inventory_ledger_book:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new InventoryLedgerFragment()).commit();
                item.setChecked(true);
                break;
            case R.id.item_receive_issue_reg:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ItemRCVISSUERegister()).commit();
                item.setChecked(true);
                break;
            case R.id.relations_of:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new RelationsFragment()).commit();
                item.setChecked(true);
                break;
            case R.id.sales_order:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SalesOrderFragment()).commit();
                item.setChecked(true);
                break;
            case R.id.deliver_challan:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DeliveryFragement()).commit();
                item.setChecked(true);
                break;
            case R.id.sales_order_delivery_challan:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SalesOrderDeliverySummary()).commit();
                item.setChecked(true);
                break;
            case R.id.top_n_item:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new TopNItem()).commit();
                item.setChecked(true);
                break;
            case R.id.product_wise_assessment:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProductWiseAssess()).commit();
                item.setChecked(true);
                break;
            case R.id.graphical_summary:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new GraphicalSummary()).commit();
                item.setChecked(true);
                break;
            case R.id.chart_of_acc:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ChartAccounts()).commit();
                item.setChecked(true);
                break;
            case R.id.voucher_transaction:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new VoucherTransaction()).commit();
                item.setChecked(true);
                break;
            case R.id.accnt_ledger:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AccountLedger()).commit();
                item.setChecked(true);
                break;
            case R.id.party_summary_ledger:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PartySummaryLedger()).commit();
                item.setChecked(true);
                break;
            case R.id.party_pay_recv_summary_ledger:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PartyPayableReceivable()).commit();
                item.setChecked(true);
                break;
            case R.id.bank_balance:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new BankBalance()).commit();
                item.setChecked(true);
                break;
            case R.id.cash_trans:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new CashTransaction()).commit();
                item.setChecked(true);
                break;
            case R.id.credit_voucher_approved:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new CreditVoucherApproved()).commit();
                item.setChecked(true);
                break;
            case R.id.debit_voucher_approved:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DebitVoucherApproved()).commit();
                item.setChecked(true);
                break;
            case R.id.journal_voucher_approved:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new JournalVoucherApproved()).commit();
                item.setChecked(true);
                break;
            case R.id.purchase_req_approval:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PurchaseReqApproved()).commit();
                item.setChecked(true);
                break;
            case R.id.purchase_order_approval:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PurchaseOrderApproved()).commit();
                item.setChecked(true);
                break;
            case R.id.party_all_list:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PartyAll()).commit();
                item.setChecked(true);
                break;
            case R.id.all_items:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AllItemList()).commit();
                item.setChecked(true);
                break;
            case R.id.performance_report:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PerformanceFragment()).commit();
                item.setChecked(true);
                break;
            case R.id.user_setup:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new UserSetupFragment()).commit();
                item.setChecked(true);
                break;
            case R.id.purchase_requisition:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PurchaseRequisition()).commit();
                item.setChecked(true);
                break;
            case R.id.purchase_order:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PurchaseOrder()).commit();
                item.setChecked(true);
                break;
            case R.id.purchase_receive:
                dashboardFront = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PurchaseReceive()).commit();
                item.setChecked(true);
                break;
//            case R.id.supplier:
//                break;
//            case R.id.customer:
//                break;

            default:
                Toast.makeText(getApplicationContext(),"HELLO",Toast.LENGTH_SHORT).show();
        }
        drawerLayout.closeDrawer(GravityCompat.END);
        return true;
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }

    public static String getHostName(String defValue) {
        try {
            Method getString = Build.class.getDeclaredMethod("getString", String.class);
            getString.setAccessible(true);
            return getString.invoke(null, "net.hostname").toString();
        } catch (Exception ex) {
            return defValue;
        }
    }

//    public boolean isConnected () {
//        boolean connected = false;
//        boolean isMobile = false;
//        try {
//            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo nInfo = cm.getActiveNetworkInfo();
//            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
//            return connected;
//        } catch (Exception e) {
//            Log.e("Connectivity Exception", e.getMessage());
//        }
//        return connected;
//    }
//
//    public boolean isOnline () {
//
//        Runtime runtime = Runtime.getRuntime();
//        try {
//            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
//            int exitValue = ipProcess.waitFor();
//            return (exitValue == 0);
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }
//
//    public class CheckLogin extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            waitProgress.show(getSupportFragmentManager(), "WaitBar");
//            waitProgress.setCancelable(false);
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            if (isConnected() && isOnline()) {
//
//                LoginLog();
//                if (connected) {
//                    conn = true;
//                    message = "Internet Connected";
//                }
//
//            } else {
//                conn = false;
//                message = "Not Connected";
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//
//            waitProgress.dismiss();
//            if (conn) {
//
//
//                conn = false;
//                connected = false;
//                userCheck();
//
//                if (bundle == null) {
//                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DashboardFragment()).commit();
//                }
//
//
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(MainMenu.this)
//                        .setMessage("Please Check Your Internet Connection")
//                        .setPositiveButton("Retry", null)
//                        .setNegativeButton("Exit",null)
//                        .show();
//
//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
//                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                positive.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        dialog.dismiss();
//                        new CheckLogin().execute();
//
//                    }
//                });
//
//                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                negative.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                        finish();
//                    }
//                });
//            }
//        }
//    }
//
//    public void LoginLog () {
//
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//
//            String userName = userInfoLists.get(0).getUserName();
//            String userId = userInfoLists.get(0).getUserId();
//            userAccessLists = new ArrayList<>();
//
//            if (userId != null) {
//                if (!userId.isEmpty()) {
//                    System.out.println(userId);
//                } else {
//                    userId = "0";
//                }
//            } else {
//                userId = "0";
//            }
//
//            Statement stmt = connection.createStatement();
//            sessionId = "";
//
//            ResultSet resultSet2 = stmt.executeQuery("SELECT SYS_CONTEXT ('USERENV', 'SESSIONID') --INTO P_IULL_SESSION_ID\n" +
//                    "   FROM DUAL\n");
//
//            while (resultSet2.next()) {
//                System.out.println("SESSION ID: "+ resultSet2.getString(1));
//                sessionId = resultSet2.getString(1);
//            }
//
//            resultSet2.close();
//
//            CallableStatement callableStatement1 = connection.prepareCall("{call USERLOGINDTL(?,?,?,?,?,?,?,?,?)}");
//            callableStatement1.setString(1,userName);
//            callableStatement1.setString(2, brand+" "+model);
//            callableStatement1.setString(3,ipAddress);
//            callableStatement1.setString(4,hostUserName);
//            callableStatement1.setInt(5,Integer.parseInt(userId));
//            callableStatement1.setInt(6,Integer.parseInt(sessionId));
//            callableStatement1.setString(7,"1");
//            callableStatement1.setString(8,osName);
//            callableStatement1.setInt(9,3);
//            callableStatement1.execute();
//
//            callableStatement1.close();
//
//            ResultSet resultSet3 = stmt.executeQuery("Select distinct IMSA.MSMA_ID, IMSA.MSMA_LEBEL_NAME, IGPA.GPVA_VIEW\n" +
//                    "FROM ISP_GROUP_PRIVILEGE_ANDROAID IGPA, ISP_MODULE_SUB_MODULE_ANDROAID IMSA, ISP_USER_GROUP_MODULE_ANDROID IUGMA\n" +
//                    "WHERE IMSA.msma_id = IUGMA.USGRMA_MSMA_ID\n" +
//                    "and IGPA.gpva_group_id = IUGMA.USGRMA_GROUP_ID\n" +
//                    "AND IUGMA.USGRMA_USER_ID = "+userId+"\n" +
//                    "order by IMSA.MSMA_ID");
//
//            while (resultSet3.next()) {
//                userAccessLists.add(new UserAccessList(resultSet3.getString(1),resultSet3.getString(2),resultSet3.getString(3)));
//            }
//
//            resultSet3.close();
//
//
//            connected = true;
//
//            connection.close();
//
//        } catch (Exception e) {
//
//            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
//            Log.i("ERRRRR", e.getLocalizedMessage());
//            e.printStackTrace();
//        }
//
//    }

    public void loginLog() {
        waitProgress.show(getSupportFragmentManager(), "WaitBar");
        waitProgress.setCancelable(false);

        conn = false;
        connected = false;
        userAccessLists = new ArrayList<>();

        String userId = userInfoLists.get(0).getUserId();
        if (userId != null) {
            if (!userId.isEmpty()) {
                System.out.println(userId);
            } else {
                userId = "0";
            }
        } else {
            userId = "0";
        }

        String loginLogUrl = "http://103.56.208.123:8001/apex/tterp/login/loginLog";
        String accessUrl = "http://103.56.208.123:8001/apex/tterp/login/getUserAccess/"+userId+"";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest accessReq = new StringRequest(Request.Method.GET, accessUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        String msma_id = info.getString("msma_id");
                        String msma_lebel_name = info.getString("msma_lebel_name");
                        String gpva_view = info.getString("gpva_view");

                        userAccessLists.add(new UserAccessList(msma_id,msma_lebel_name,gpva_view));

                    }
                }
                connected = true;
                updateInterface();
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateInterface();
            }
        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateInterface();
        });

        StringRequest loginLogReq = new StringRequest(Request.Method.POST, loginLogUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                if (string_out.equals("Successfully Created")) {
                    requestQueue.add(accessReq);
                }
                else {
                    connected = false;
                    updateInterface();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateInterface();
            }
        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateInterface();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String userName = userInfoLists.get(0).getUserName();
                String user_iid = userInfoLists.get(0).getUserId();
                if (user_iid != null) {
                    if (!user_iid.isEmpty()) {
                        System.out.println(user_iid);
                    } else {
                        user_iid = "0";
                    }
                } else {
                    user_iid = "0";
                }
                headers.put("P_IULL_USER_ID",userName);
                headers.put("P_IULL_CLIENT_HOST_NAME",brand+" "+model);
                headers.put("P_IULL_CLIENT_IP_ADDR",ipAddress);
                headers.put("P_IULL_CLIENT_HOST_USER_NAME",hostUserName);
                headers.put("P_IULL_SESSION_USER_ID",user_iid);
                headers.put("P_IULL_OS_NAME",osName);
                return headers;
            }
        };

        requestQueue.add(loginLogReq);
    }

    private void updateInterface() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;
                userCheck();

                if (bundle == null) {
                    dashboardFront = true;
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DashboardFragment()).commit();
                }
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(MainMenu.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Exit",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    dialog.dismiss();
                    loginLog();

                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> {
                    dialog.dismiss();
                    finish();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(MainMenu.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Exit",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                dialog.dismiss();
                loginLog();

            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }
    }
}
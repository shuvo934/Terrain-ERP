package ttit.com.shuvo.icomerp.mainBoard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ttit.com.shuvo.icomerp.WaitProgress;
import ttit.com.shuvo.icomerp.arrayList.UserDesignation;
import ttit.com.shuvo.icomerp.arrayList.UserInfoList;
import ttit.com.shuvo.icomerp.fragments.AccountLedger;
import ttit.com.shuvo.icomerp.fragments.AllItemList;
import ttit.com.shuvo.icomerp.fragments.BankBalance;
import ttit.com.shuvo.icomerp.fragments.CashTransaction;
import ttit.com.shuvo.icomerp.fragments.ChartAccounts;
import ttit.com.shuvo.icomerp.fragments.CreditVoucherApproved;
import ttit.com.shuvo.icomerp.fragments.DebitVoucherApproved;
import ttit.com.shuvo.icomerp.fragments.DeliveryFragement;
import ttit.com.shuvo.icomerp.fragments.GraphicalSummary;
import ttit.com.shuvo.icomerp.fragments.InventoryLedgerFragment;
import ttit.com.shuvo.icomerp.fragments.ItemRCVISSUERegister;
import ttit.com.shuvo.icomerp.fragments.ItemWiseStockFragment;
import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.fragments.DashboardFragment;
import ttit.com.shuvo.icomerp.fragments.JournalVoucherApproved;
import ttit.com.shuvo.icomerp.fragments.PartyAll;
import ttit.com.shuvo.icomerp.fragments.PartyPayableReceivable;
import ttit.com.shuvo.icomerp.fragments.PartySummaryLedger;
import ttit.com.shuvo.icomerp.fragments.PerformanceFragment;
import ttit.com.shuvo.icomerp.fragments.ProductWiseAssess;
import ttit.com.shuvo.icomerp.fragments.PurchaseOrderApproved;
import ttit.com.shuvo.icomerp.fragments.PurchaseReqApproved;
import ttit.com.shuvo.icomerp.fragments.ReOrderFragment;
import ttit.com.shuvo.icomerp.fragments.ReceiveFragment;
import ttit.com.shuvo.icomerp.fragments.RelationsFragment;
import ttit.com.shuvo.icomerp.fragments.SalesOrderDeliverySummary;
import ttit.com.shuvo.icomerp.fragments.SalesOrderFragment;
import ttit.com.shuvo.icomerp.fragments.StockFragment;
import ttit.com.shuvo.icomerp.fragments.StockLedgerFragment;
import ttit.com.shuvo.icomerp.fragments.TopNItem;
import ttit.com.shuvo.icomerp.fragments.VoucherTransaction;
import ttit.com.shuvo.icomerp.fragments.WorkOrderFragment;
import ttit.com.shuvo.icomerp.login.Login;

import static ttit.com.shuvo.icomerp.OracleConnection.createConnection;
import static ttit.com.shuvo.icomerp.login.Login.CompanyName;
import static ttit.com.shuvo.icomerp.login.Login.SoftwareName;
import static ttit.com.shuvo.icomerp.login.Login.isApproved;
import static ttit.com.shuvo.icomerp.login.Login.isLeaveApproved;
import static ttit.com.shuvo.icomerp.login.Login.userDesignations;
import static ttit.com.shuvo.icomerp.login.Login.userInfoLists;

public class MainMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    DrawerLayout drawerLayout;
    NavigationView navigationView;

    TextView appName;
    ImageView tool;
    ImageView logout;
    String emp_id = "";

    WaitProgress waitProgress = new WaitProgress();
    private String message = null;
    private Boolean conn = false;
    private Boolean infoConnected = false;
    private Boolean connected = false;

    private Boolean getConn = false;
    private Boolean getConnected = false;

    private Connection connection;

    String android_id = "";
    String model = "";
    String brand = "";
    String ipAddress = "";
    String hostUserName = "";
    String sessionId = "";
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

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

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

        if (userInfoLists.size() != 0) {
            String firstname = userInfoLists.get(0).getUser_fname();
            String lastName = userInfoLists.get(0).getUser_lname();
            if (firstname == null) {
                firstname = "";
            }
            if (lastName == null) {
                lastName = "";
            }
            String empFullName = firstname+" "+lastName;
            //userName.setText(empFullName);
        }

        if (userDesignations.size() != 0) {
            String jsmName = userDesignations.get(0).getJsm_name();
            if (jsmName == null) {
                jsmName = "";
            }
            //designation.setText(jsmName);

            String deptName = userDesignations.get(0).getDiv_name();
            if (deptName == null) {
                deptName = "";
            }
            //department.setText(deptName);
        }

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


        new CheckLogin().execute();




    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dashboard:
                //Toast.makeText(getApplicationContext(),"Dashboard",Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DashboardFragment()).commit();
                //navigationView.setCheckedItem(item.getItemId());
                //navigationView.setCheckedItem(item);
                item.setChecked(true);
                break;
            case R.id.work_order:
                //Toast.makeText(getApplicationContext(),"Work Order",Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new WorkOrderFragment()).commit();
                //navigationView.setCheckedItem(item.getItemId());
                //navigationView.setCheckedItem(item);
                item.setChecked(true);
                break;
            case R.id.receive:
                //Toast.makeText(getApplicationContext(),"Receive",Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ReceiveFragment()).commit();
                //navigationView.setCheckedItem(item.getItemId());
                item.setChecked(true);
                break;
            case R.id.stock_qty:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new StockFragment()).commit();
                item.setChecked(true);
                break;
            case R.id.re_order:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ReOrderFragment()).commit();
                item.setChecked(true);
                break;
            case R.id.stock_qty_item_wise:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ItemWiseStockFragment()).commit();
                item.setChecked(true);
                break;
            case R.id.stock_ledger:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new StockLedgerFragment()).commit();
                item.setChecked(true);
                break;
            case R.id.inventory_ledger_book:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new InventoryLedgerFragment()).commit();
                item.setChecked(true);
                break;
            case R.id.item_receive_issue_reg:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ItemRCVISSUERegister()).commit();
                item.setChecked(true);
                break;
            case R.id.relations_of:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new RelationsFragment()).commit();
                item.setChecked(true);
                break;
            case R.id.sales_order:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SalesOrderFragment()).commit();
                item.setChecked(true);
                break;
            case R.id.deliver_challan:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DeliveryFragement()).commit();
                item.setChecked(true);
                break;
            case R.id.sales_order_delivery_challan:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SalesOrderDeliverySummary()).commit();
                item.setChecked(true);
                break;
            case R.id.top_n_item:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new TopNItem()).commit();
                item.setChecked(true);
                break;
            case R.id.product_wise_assessment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProductWiseAssess()).commit();
                item.setChecked(true);
                break;
            case R.id.graphical_summary:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new GraphicalSummary()).commit();
                item.setChecked(true);
                break;
            case R.id.chart_of_acc:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ChartAccounts()).commit();
                item.setChecked(true);
                break;
            case R.id.voucher_transaction:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new VoucherTransaction()).commit();
                item.setChecked(true);
                break;
            case R.id.accnt_ledger:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AccountLedger()).commit();
                item.setChecked(true);
                break;
            case R.id.party_summary_ledger:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PartySummaryLedger()).commit();
                item.setChecked(true);
                break;
            case R.id.party_pay_recv_summary_ledger:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PartyPayableReceivable()).commit();
                item.setChecked(true);
                break;
            case R.id.bank_balance:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new BankBalance()).commit();
                item.setChecked(true);
                break;
            case R.id.cash_trans:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new CashTransaction()).commit();
                item.setChecked(true);
                break;
            case R.id.credit_voucher_approved:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new CreditVoucherApproved()).commit();
                item.setChecked(true);
                break;
            case R.id.debit_voucher_approved:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DebitVoucherApproved()).commit();
                item.setChecked(true);
                break;
            case R.id.journal_voucher_approved:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new JournalVoucherApproved()).commit();
                item.setChecked(true);
                break;
            case R.id.purchase_req_approval:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PurchaseReqApproved()).commit();
                item.setChecked(true);
                break;
            case R.id.purchase_order_approval:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PurchaseOrderApproved()).commit();
                item.setChecked(true);
                break;
            case R.id.party_all_list:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PartyAll()).commit();
                item.setChecked(true);
                break;
            case R.id.all_items:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AllItemList()).commit();
                item.setChecked(true);
                break;
            case R.id.performance_report:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PerformanceFragment()).commit();
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



    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
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

    public boolean isConnected () {
        boolean connected = false;
        boolean isMobile = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    public boolean isOnline () {

        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    public class CheckLogin extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(getSupportFragmentManager(), "WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                LoginLog();
                if (connected) {
                    conn = true;
                    message = "Internet Connected";
                }

            } else {
                conn = false;
                message = "Not Connected";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            waitProgress.dismiss();
            if (conn) {


                conn = false;
                connected = false;

                if (bundle == null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DashboardFragment()).commit();
                }


            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(MainMenu.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Exit",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        new CheckLogin().execute();

                    }
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        finish();
                    }
                });
            }
        }
    }

    public void LoginLog () {

        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            String userName = userInfoLists.get(0).getUserName();
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

            Statement stmt = connection.createStatement();
            sessionId = "";

            ResultSet resultSet2 = stmt.executeQuery("SELECT SYS_CONTEXT ('USERENV', 'SESSIONID') --INTO P_IULL_SESSION_ID\n" +
                    "   FROM DUAL\n");

            while (resultSet2.next()) {
                System.out.println("SESSION ID: "+ resultSet2.getString(1));
                sessionId = resultSet2.getString(1);
            }

            resultSet2.close();

            CallableStatement callableStatement1 = connection.prepareCall("{call USERLOGINDTL(?,?,?,?,?,?,?,?,?)}");
            callableStatement1.setString(1,userName);
            callableStatement1.setString(2, brand+" "+model);
            callableStatement1.setString(3,ipAddress);
            callableStatement1.setString(4,hostUserName);
            callableStatement1.setInt(5,Integer.parseInt(userId));
            callableStatement1.setInt(6,Integer.parseInt(sessionId));
            callableStatement1.setString(7,"1");
            callableStatement1.setString(8,osName);
            callableStatement1.setInt(9,3);
            callableStatement1.execute();

            callableStatement1.close();


            connected = true;

            connection.close();

        } catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }

    }
}
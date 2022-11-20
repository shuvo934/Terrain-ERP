package ttit.com.shuvo.icomerp.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import oracle.sql.BLOB;
import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.WaitProgress;
import ttit.com.shuvo.icomerp.arrayList.UserAccessList;
import ttit.com.shuvo.icomerp.arrayList.UserDesignation;
import ttit.com.shuvo.icomerp.arrayList.UserInfoList;
import ttit.com.shuvo.icomerp.mainBoard.MainMenu;

import static ttit.com.shuvo.icomerp.OracleConnection.createConnection;

public class Login extends AppCompatActivity {

    TextInputEditText user;
    TextInputEditText pass;

    TextView login_failed;
    TextView softName;
    TextView contact;

    Button login;

    CheckBox checkBox;

    BLOB pic;
    ImageView imageView;

    String userName = "";
    String password = "";
    public static String CompanyName = "";
    public static String SoftwareName = "";
    public static int isApproved = 0;
    public static int isLeaveApproved = 0;

    WaitProgress waitProgress = new WaitProgress();
    private String message = null;
    private Boolean conn = false;
    private Boolean infoConnected = false;
    private Boolean connected = false;

    private Boolean getConn = false;
    private Boolean getConnected = false;

    private Connection connection;

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

    public static final String MyPREFERENCES = "UserPassERP" ;
    public static final String user_emp_code = "nameKey";
    public static final String user_password = "passKey";
    public static final String checked = "trueFalse";

    SharedPreferences sharedpreferences;

    SharedPreferences sharedLogin;

    String getUserName = "";
    String getPassword = "";
    boolean getChecked = false;

    String userId = "";
    public static ArrayList<UserInfoList> userInfoLists;
    public static ArrayList<UserDesignation> userDesignations;
    public static ArrayList<UserAccessList> userAccessLists;

    String emp_id = "";
    int live_loc_flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userInfoLists = new ArrayList<>();
        userDesignations = new ArrayList<>();
        userAccessLists = new ArrayList<>();

        softName = findViewById(R.id.name_of_soft_login);
        imageView = findViewById(R.id.erp_image);
        user = findViewById(R.id.user_name_given_log_in);
        pass = findViewById(R.id.password_given_log_in);
        checkBox = findViewById(R.id.remember_checkbox);

        login_failed = findViewById(R.id.email_pass_miss);
        contact = findViewById(R.id.contact_text);

        login = findViewById(R.id.log_in_button);

        sharedLogin = getSharedPreferences(LOGIN_ACTIVITY_FILE,MODE_PRIVATE);

        sharedpreferences = getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);
        getUserName = sharedpreferences.getString(user_emp_code,null);
        getPassword = sharedpreferences.getString(user_password,null);
        getChecked = sharedpreferences.getBoolean(checked,false);

        if (getUserName != null) {
            user.setText(getUserName);
        }
        if (getPassword != null) {
            pass.setText(getPassword);
        }
        checkBox.setChecked(getChecked);

        new Check().execute();

        pass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                        event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        Log.i("Let see", "Come here");
                        pass.clearFocus();
                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        closeKeyBoard();

                        return false; // consume.
                    }
                }
                return false;
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mmm = "info@techterrain-it.com";
                AlertDialog dialog = new AlertDialog.Builder(Login.this)
                        .setMessage("Do you want to send an email to "+mmm+" ?")
                        .setPositiveButton("Yes", null)
                        .setNegativeButton("No",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri data = Uri.parse("mailto:"+mmm);
                        intent.setData(data);
                        try {
                            startActivity(intent);

                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(Login.this, "There is no email app found.", Toast.LENGTH_SHORT).show();
                        }



                    }
                });
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeKeyBoard();

                login_failed.setVisibility(View.GONE);
                userName = user.getText().toString();
                password = pass.getText().toString();

                if (!userName.isEmpty() && !password.isEmpty()) {


                    new CheckLogin().execute();

                } else {
                    Toast.makeText(getApplicationContext(), "Please Give User Name and Password", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onBackPressed () {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("EXIT!")
                .setMessage("Do You Want to Exit?")
                .setIcon(R.drawable.erp)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        System.exit(0);
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

    private void closeKeyBoard () {
        View view = this.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onTouchEvent (MotionEvent event){
        closeKeyBoard();
        return super.onTouchEvent(event);
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

                LoginQuery();
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

                if (!userId.equals("-1")) {
                    if (infoConnected) {

                        if (checkBox.isChecked()) {
                            System.out.println("Remembered");
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.remove(user_emp_code);
                            editor.remove(user_password);
                            editor.remove(checked);
                            editor.putString(user_emp_code,userName);
                            editor.putString(user_password,password);
                            editor.putBoolean(checked,true);
                            editor.apply();
                            editor.commit();
                        } else {
                            System.out.println("Not Remembered");
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.remove(user_emp_code);
                            editor.remove(user_password);
                            editor.remove(checked);

                            editor.apply();
                            editor.commit();
                        }

//                        user.setText("");
//                        pass.setText("");
//                        checkBox.setChecked(false);



                        SharedPreferences.Editor editor1 = sharedLogin.edit();
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

                        editor1.putString(USER_NAME, userInfoLists.get(0).getUserName());
                        editor1.putString(USER_F_NAME, userInfoLists.get(0).getUser_fname());
                        editor1.putString(USER_L_NAME, userInfoLists.get(0).getUser_lname());
                        editor1.putString(EMAIL, userInfoLists.get(0).getEmail());
                        editor1.putString(CONTACT, userInfoLists.get(0).getContact());
                        editor1.putString(EMP_ID_LOGIN, userInfoLists.get(0).getEmp_id());
                        editor1.putString(USER_ID, userInfoLists.get(0).getUserId());

                        if (userDesignations.size() != 0) {
                            editor1.putString(JSM_CODE, userDesignations.get(0).getJsm_code());
                            editor1.putString(JSM_NAME, userDesignations.get(0).getJsm_name());
                            editor1.putString(JSD_ID_LOGIN, userDesignations.get(0).getJsd_id());
                            editor1.putString(JSD_OBJECTIVE, userDesignations.get(0).getJsd_objective());
                            editor1.putString(DEPT_NAME, userDesignations.get(0).getDept_name());
                            editor1.putString(DIV_NAME, userDesignations.get(0).getDiv_name());
                            editor1.putString(DESG_NAME, userDesignations.get(0).getDesg_name());
                            editor1.putString(DESG_PRIORITY, userDesignations.get(0).getDesg_priority());
                            editor1.putString(JOINING_DATE, userDesignations.get(0).getJoining_date());
                            editor1.putString(DIV_ID, userDesignations.get(0).getDiv_id());
                        } else {
                            editor1.putString(JSM_CODE, null);
                            editor1.putString(JSM_NAME, null);
                            editor1.putString(JSD_ID_LOGIN, null);
                            editor1.putString(JSD_OBJECTIVE, null);
                            editor1.putString(DEPT_NAME, null);
                            editor1.putString(DIV_NAME, null);
                            editor1.putString(DESG_NAME, null);
                            editor1.putString(DESG_PRIORITY, null);
                            editor1.putString(JOINING_DATE, null);
                            editor1.putString(DIV_ID, null);
                        }

                        editor1.putBoolean(LOGIN_TF,true);

                        editor1.putInt(IS_ATT_APPROVED, isApproved);
                        editor1.putInt(IS_LEAVE_APPROVED, isLeaveApproved);
                        editor1.putString(COMPANY, CompanyName);
                        editor1.putString(SOFTWARE,SoftwareName);
                        editor1.putInt(LIVE_FLAG,live_loc_flag);
                        editor1.apply();
                        editor1.commit();


                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                        startActivity(intent);
                        finish();

                    } else {
                        new CheckLogin().execute();
                    }

                } else {

                    login_failed.setVisibility(View.VISIBLE);
                }
                conn = false;
                connected = false;
                infoConnected = false;


            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(Login.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new CheckLogin().execute();
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public class Check extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(getSupportFragmentManager(), "WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                GettingData();
                if (getConnected) {
                    getConn = true;
                    message = "Internet Connected";
                }


            } else {
                getConn = false;
                message = "Not Connected";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            waitProgress.dismiss();
            if (getConn) {

                if (CompanyName == null) {
                    CompanyName = "No Name Found";
                }

                if (SoftwareName == null) {
                    SoftwareName = "Name of App";
                }

                SoftwareName = SoftwareName.toUpperCase();
                CompanyName = CompanyName.toUpperCase();
                softName.setText(SoftwareName);

                getConnected = false;
                getConn = false;


            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(Login.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new Check().execute();
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public void LoginQuery () {


        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            userInfoLists = new ArrayList<>();
            userDesignations = new ArrayList<>();
            userAccessLists = new ArrayList<>();
            isApproved = 0;
            isLeaveApproved = 0;

            Statement stmt = connection.createStatement();
            StringBuffer stringBuffer = new StringBuffer();


            ResultSet rs = stmt.executeQuery("select VALIDATE_USER_DB('" + userName + "',HAMID_ENCRYPT_DESCRIPTION_PACK.HEDP_ENCRYPT('" + password + "')) val from dual\n");


            while (rs.next()) {
                stringBuffer.append("USER ID: " + rs.getString(1) + "\n");
                userId = rs.getString(1);

            }
            rs.close();

            if (!userId.equals("-1")) {

                ResultSet resultSet = stmt.executeQuery("Select USR_NAME, USR_FNAME, USR_LNAME, USR_EMAIL, USR_CONTACT, USR_EMP_ID, USR_ID FROM ISP_USER\n" +
                        "where USR_ID = " + userId + "\n");

                while (resultSet.next()) {
                    emp_id = resultSet.getString(6);
                    userInfoLists.add(new UserInfoList(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6),resultSet.getString(7)));
                }
                resultSet.close();

                ResultSet resultSet1 = stmt.executeQuery("SELECT DISTINCT JOB_SETUP_MST.JSM_CODE, JOB_SETUP_MST.JSM_NAME TEMP_TITLE, \n" +
                        "JOB_SETUP_DTL.JSD_ID, JOB_SETUP_DTL.JSD_OBJECTIVE, DEPT_MST.DEPT_NAME, \n" +
                        "DIVISION_MST.DIVM_NAME, DESIG_MST.DESIG_NAME, DESIG_MST.DESIG_PRIORITY, (Select TO_CHAR(MAX(EMP_JOB_HISTORY.JOB_ACTUAL_DATE),'DD-MON-YYYY') from EMP_JOB_HISTORY) as JOININGDATE, DIVISION_MST.DIVM_ID \n" +
                        "FROM JOB_SETUP_MST, JOB_SETUP_DTL, DEPT_MST, DIVISION_MST, DESIG_MST, EMP_JOB_HISTORY\n" +
                        "WHERE ((JOB_SETUP_DTL.JSD_JSM_ID = JOB_SETUP_MST.JSM_ID)\n" +
                        " AND (JOB_SETUP_MST.JSM_DIVM_ID = DIVISION_MST.DIVM_ID)\n" +
                        " AND (DEPT_MST.DEPT_ID = JOB_SETUP_MST.JSM_DEPT_ID)\n" +
                        " AND (JOB_SETUP_MST.JSM_DESIG_ID = DESIG_MST.DESIG_ID))\n" +
                        " AND JOB_SETUP_DTL.JSD_ID = (SELECT JOB_JSD_ID\n" +
                        "                            FROM EMP_JOB_HISTORY\n" +
                        "                            WHERE JOB_ID = (SELECT MAX(JOB_ID) FROM EMP_JOB_HISTORY WHERE JOB_EMP_ID = " + emp_id + "))" +
                        "AND EMP_JOB_HISTORY.JOB_JSD_ID = JOB_SETUP_DTL.JSD_ID");

                while (resultSet1.next()) {

                    userDesignations.add(new UserDesignation(resultSet1.getString(1), resultSet1.getString(2), resultSet1.getString(3), resultSet1.getString(4), resultSet1.getString(5), resultSet1.getString(6), resultSet1.getString(7), resultSet1.getString(8), resultSet1.getString(9), resultSet1.getString(10)));
                }
                resultSet1.close();

                infoConnected = true;

            }
            System.out.println(stringBuffer);

            connected = true;

            connection.close();

        } catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

    public void GettingData () {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();


            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT CIM_NAME,CIM_LOGO FROM COMPANY_INFO_MST\n");

            while (rs.next()) {
                CompanyName = rs.getString(1);
                System.out.println(CompanyName);

//                pic = (BLOB) rs.getBlob(2);
//                System.out.println(String.valueOf(pic));
//
//                byte[] barr = pic.getBytes(1,(int)pic.length());
//                System.out.println(Arrays.toString(barr));
//                String encodedImageString = Base64.encodeToString(barr, Base64.DEFAULT);
//                byte[] bytes = Base64.decode(encodedImageString,Base64.DEFAULT);
//                System.out.println(Arrays.toString(bytes));

//                String bytes = rs.getString(2);

//                System.out.println(bytes);
//                byte[] b = bytes.getBytes();
//                System.out.println(Arrays.toString(b));

                // Blob image = rs.getBlob(2);
//                Bitmap bmp= BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
//                System.out.println(bmp);
//                imageView.setImageBitmap(bmp);

//                File myExternalFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"Logo.jpeg");
//                FileOutputStream fos = new FileOutputStream(myExternalFile);//Get OutputStream for NewFile Location
//                fos.write(barr);
//                fos.close();
//
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inMutable = true;
//                options.inJustDecodeBounds = true;
//                Bitmap bitmap = BitmapFactory.decodeByteArray(barr,0,barr.length);
//                System.out.println(bitmap);
//                imageView.setImageBitmap(bitmap);
//
//                InputStream inputStream = pic.getBinaryStream();
//                System.out.println(inputStream);
//
//                bitmap = BitmapFactory.decodeStream(inputStream);
//                System.out.println(bitmap);


            }


            ResultSet resultSet = stmt.executeQuery(" select LIC_SOFTWARE_NAME from isp_runauto where rownum=1\n");

            while (resultSet.next()) {
                SoftwareName = resultSet.getString(1);
                System.out.println(SoftwareName);
            }


//            CallableStatement callableStatement = connection.prepareCall("begin ? := GET_LEAVE_BALANCE(?,?,?); end;");
//            callableStatement.setInt(2,2008);
//            callableStatement.setString(3, "16-AUG-21");
//            callableStatement.setString(4,"EL");
//            callableStatement.registerOutParameter(1,Types.INTEGER);
//            callableStatement.execute();
//            int ddd = callableStatement.getInt(1);
//            System.out.println(ddd);
//            callableStatement.close();
//
//            CallableStatement callableStatement1 = connection.prepareCall("begin ? := GET_CONSUMED_LEAVE_BY_MONTH(?,?,?); end;");
//            callableStatement1.setInt(2,2008);
//            callableStatement1.setString(3, "01-AUG-21");
//            callableStatement1.setString(4,"EL");
//            callableStatement1.registerOutParameter(1,Types.INTEGER);
//            callableStatement1.execute();
//            int dddd = callableStatement1.getInt(1);
//            System.out.println(dddd);
//            callableStatement1.close();


            getConnected = true;
            connection.close();

        } catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}
package ttit.com.shuvo.icomerp.fragments;

import static android.content.Context.MODE_PRIVATE;
import static ttit.com.shuvo.icomerp.OracleConnection.createConnection;
import static ttit.com.shuvo.icomerp.login.Login.isApproved;
import static ttit.com.shuvo.icomerp.login.Login.isLeaveApproved;
import static ttit.com.shuvo.icomerp.login.Login.userDesignations;
import static ttit.com.shuvo.icomerp.login.Login.userInfoLists;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;

import oracle.jdbc.driver.OracleTypes;
import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.WaitProgress;
import ttit.com.shuvo.icomerp.adapters.CreditVoucherApprovedAdapter;
import ttit.com.shuvo.icomerp.adapters.UserGroupAdapter;
import ttit.com.shuvo.icomerp.arrayList.CreditVoucherApprovedLists;
import ttit.com.shuvo.icomerp.arrayList.UserGroupList;
import ttit.com.shuvo.icomerp.arrayList.UserGroupModuleList;
import ttit.com.shuvo.icomerp.arrayList.UserSetupUserList;
import ttit.com.shuvo.icomerp.dialogues.UserSelectionDialogue;
import ttit.com.shuvo.icomerp.login.Login;
import ttit.com.shuvo.icomerp.mainBoard.MainMenu;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserSetupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserSetupFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static TextInputEditText userSelectSpinner;
    public static ArrayList<UserSetupUserList> userSetupUserLists;
    public static String selected_user_id = "";
    LinearLayout afterSelecting;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
    Boolean isfiltered = false;
    private Connection connection;

    RecyclerView groupView;
    RecyclerView.LayoutManager layoutManager;
    UserGroupAdapter userGroupAdapter;

    public static ArrayList<UserGroupList> userGroupLists;
    String user_id_default = "";

    Button save;

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

    public UserSetupFragment() {
        // Required empty public constructor
    }

    Context mContext;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext=context;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserSetupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserSetupFragment newInstance(String param1, String param2) {
        UserSetupFragment fragment = new UserSetupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_setup, container, false);

        sharedPreferences = getActivity().getSharedPreferences(LOGIN_ACTIVITY_FILE, MODE_PRIVATE);
        userSelectSpinner = view.findViewById(R.id.select_user_setup);
        afterSelecting = view.findViewById(R.id.after_selecting_user);
        afterSelecting.setVisibility(View.GONE);

        groupView = view.findViewById(R.id.user_group_access_view);
        save = view.findViewById(R.id.save_user_access_button);
        save.setVisibility(View.GONE);

        groupView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        groupView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration2 = new DividerItemDecoration(groupView.getContext(),DividerItemDecoration.VERTICAL);
        groupView.addItemDecoration(dividerItemDecoration2);

        userSetupUserLists = new ArrayList<>();
        userGroupLists = new ArrayList<>();

        user_id_default = userInfoLists.get(0).getUserId();

        userSelectSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserSelectionDialogue userSelectionDialogue = new UserSelectionDialogue();
                userSelectionDialogue.show(getActivity().getSupportFragmentManager(),"USERSELECT");

            }
        });

        userSelectSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                System.out.println(selected_user_id);
                new GroupCheck().execute();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(mContext)
                        .setTitle("SAVE!")
                        .setMessage("Do you want to save this group access for this user?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new SaveCheck().execute();
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
        });

        new Check().execute();
        return view;
    }

    public boolean isConnected() {
        boolean connected = false;
        boolean isMobile = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    public boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException | InterruptedException e)          { e.printStackTrace(); }

        return false;
    }

    public class Check extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                userData();
                if (connected) {
                    conn = true;
                }

            } else {
                conn = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            if (conn) {

                conn = false;
                connected = false;

                waitProgress.dismiss();

            }else {
                waitProgress.dismiss();
                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
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

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public class GroupCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                userGroup();
                if (connected) {
                    conn = true;
                }

            } else {
                conn = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            if (conn) {

                conn = false;
                connected = false;

                afterSelecting.setVisibility(View.VISIBLE);
                save.setVisibility(View.VISIBLE);

                userGroupAdapter = new UserGroupAdapter(userGroupLists,mContext);
                groupView.setAdapter(userGroupAdapter);

                waitProgress.dismiss();

            }else {
                waitProgress.dismiss();
                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new GroupCheck().execute();
                        dialog.dismiss();
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
        }
    }

    public class SaveCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                saveGroupAccess();
                if (connected) {
                    conn = true;
                }

            } else {
                conn = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            if (conn) {

                conn = false;
                connected = false;

                Toast.makeText(getContext(),"SAVE SUCCESSFUL",Toast.LENGTH_SHORT).show();
                if (selected_user_id.equals(user_id_default)) {
                    MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(mContext)
                            .setTitle("LOG OUT!")
                            .setMessage("You have changed your Group Access, for this reason you will be automatically logged out.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
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

                                    Intent intent = new Intent(mContext, Login.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setCancelable(false);
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();
                }

                waitProgress.dismiss();

            }else {
                waitProgress.dismiss();
                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new SaveCheck().execute();
                        dialog.dismiss();
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
        }
    }

    public void userData() {
        try {
            this.connection = createConnection();

            userSetupUserLists = new ArrayList<>();

            Statement stmt = connection.createStatement();

            ResultSet resultSet = stmt.executeQuery("Select USR_ID, USR_NAME, USR_FNAME, USR_LNAME from ISP_USER WHERE NVL(USR_DEACTIVATION_FLAG,0)=0 AND USR_NAME != 'admin'\n" +
                    "order by USR_ID desc");

            while (resultSet.next()) {
                userSetupUserLists.add(new UserSetupUserList(resultSet.getString(1),resultSet.getString(2),
                        resultSet.getString(3),resultSet.getString(4)));
            }

            resultSet.close();
            stmt.close();

            connected = true;

            connection.close();

        }
        catch (Exception e) {

            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void userGroup() {
        try {
            this.connection = createConnection();

            userGroupLists = new ArrayList<>();

            Statement stmt = connection.createStatement();

            ResultSet resultSet = stmt.executeQuery("Select distinct IG.GRP_ID, IG.GRP_NAME,\n" +
                    "(Select count(*) from ISP_USER_GROUP_MODULE_ANDROID WHERE USGRMA_USER_ID = "+selected_user_id+" and USGRMA_GROUP_ID = IG.GRP_ID) activation\n" +
                    "from ISP_GROUP IG\n" +
                    "WHERE IG.GRP_ANDROIAID_FLAG = 1\n" +
                    "order by IG.GRP_ID");

            while (resultSet.next()) {

                boolean active;
                int val = resultSet.getInt(3);
                active = val >= 1;
                System.out.println(active);
//                System.out.println(resultSet.getString(3));
                userGroupLists.add(new UserGroupList(resultSet.getString(1),resultSet.getString(2), active,val,new ArrayList<>()));
            }


            resultSet.close();

            for (int i = 0; i < userGroupLists.size(); i++) {
                System.out.println(userGroupLists.get(i).getGroup_name() + " " + userGroupLists.get(i).isActivated());
                String group_id = userGroupLists.get(i).getGroup_id();

                ArrayList<UserGroupModuleList> userGroupModuleLists = new ArrayList<>();

                ResultSet resultSet1 = stmt.executeQuery("Select distinct IMSA.MSMA_ID, IMSA.MSMA_LEBEL_NAME,\n" +
                        "(Select count(*) from ISP_USER_GROUP_MODULE_ANDROID WHERE USGRMA_USER_ID = "+selected_user_id+" and USGRMA_GROUP_ID = "+group_id+" and USGRMA_MSMA_ID = IMSA.MSMA_ID) activation\n" +
                        "FROM ISP_MODULE_SUB_MODULE_ANDROAID IMSA,ISP_GROUP_PRIVILEGE_ANDROAID IGPA\n" +
                        "WHERE IMSA.msma_id = IGPA.gpva_msma_id\n" +
                        "and IGPA.gpva_group_id = "+group_id+" \n" +
                        "order by IMSA.MSMA_ID");
                while (resultSet1.next()) {
                    boolean active;
                    int val = resultSet1.getInt(3);
                    active = val >= 1;
                    userGroupModuleLists.add(new UserGroupModuleList(resultSet1.getString(1),resultSet1.getString(2),active));
                }
                resultSet1.close();

                userGroupLists.get(i).setUserGroupModuleLists(userGroupModuleLists);
            }

            stmt.close();

            connected = true;

            connection.close();

        }
        catch (Exception e) {

            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void saveGroupAccess() {
        try {
            this.connection = createConnection();

            Statement stmt = connection.createStatement();

            for (int i = 0; i < userGroupLists.size(); i++) {
                System.out.println(userGroupLists.get(i).getGroup_name() + " " + userGroupLists.get(i).isActivated());
                if (userGroupLists.get(i).isActivated()) {
                    int activeValue = userGroupLists.get(i).getActiveValue();
                    int val = 0;

                    ArrayList<UserGroupModuleList> userGroupModuleLists = userGroupLists.get(i).getUserGroupModuleLists();
                    for (int j = 0; j < userGroupModuleLists.size(); j++) {
                        if (userGroupModuleLists.get(j).isActive()) {
                            val++;
                        }
                    }
                    if (val != activeValue) {
                        for (int j = 0; j < userGroupModuleLists.size(); j++) {
                            if (userGroupModuleLists.get(j).isActive()) {
                                int msma_value = 1;
                                ResultSet resultSet = stmt.executeQuery("Select count(*) from ISP_USER_GROUP_MODULE_ANDROID WHERE USGRMA_USER_ID = "+selected_user_id+" and USGRMA_GROUP_ID = "+userGroupLists.get(i).getGroup_id()+" and USGRMA_MSMA_ID = "+userGroupModuleLists.get(j).getMsma_id()+"");
                                while (resultSet.next()) {
                                    msma_value = resultSet.getInt(1);
                                }
                                resultSet.close();
                                if (msma_value == 0) {
                                    stmt.executeUpdate("INSERT INTO ISP_USER_GROUP_MODULE_ANDROID(USGRMA_GROUP_ID, USGRMA_USER_ID,USGRMA_MSMA_ID) VALUES("+userGroupLists.get(i).getGroup_id()+","+selected_user_id+","+userGroupModuleLists.get(j).getMsma_id()+")");
                                }
                            }
                            else {
                                int msma_value = 0;
                                ResultSet resultSet = stmt.executeQuery("Select count(*) from ISP_USER_GROUP_MODULE_ANDROID WHERE USGRMA_USER_ID = "+selected_user_id+" and USGRMA_GROUP_ID = "+userGroupLists.get(i).getGroup_id()+" and USGRMA_MSMA_ID = "+userGroupModuleLists.get(j).getMsma_id()+"");
                                while (resultSet.next()) {
                                    msma_value = resultSet.getInt(1);
                                }
                                resultSet.close();
                                if (msma_value >= 1) {
                                    stmt.executeUpdate("DELETE FROM ISP_USER_GROUP_MODULE_ANDROID WHERE USGRMA_USER_ID = "+selected_user_id+" and USGRMA_GROUP_ID = "+userGroupLists.get(i).getGroup_id()+" and USGRMA_MSMA_ID = "+userGroupModuleLists.get(j).getMsma_id()+"");
                                }
                            }
                        }
                    }
                }
                else {
                    ArrayList<UserGroupModuleList> userGroupModuleLists = userGroupLists.get(i).getUserGroupModuleLists();
                    for (int j = 0; j < userGroupModuleLists.size(); j++) {
                        int msma_value = 0;
                        ResultSet resultSet = stmt.executeQuery("Select count(*) from ISP_USER_GROUP_MODULE_ANDROID WHERE USGRMA_USER_ID = "+selected_user_id+" and USGRMA_GROUP_ID = "+userGroupLists.get(i).getGroup_id()+" and USGRMA_MSMA_ID = "+userGroupModuleLists.get(j).getMsma_id()+"");
                        while (resultSet.next()) {
                            msma_value = resultSet.getInt(1);
                        }
                        resultSet.close();
                        if (msma_value >= 1) {
                            stmt.executeUpdate("DELETE FROM ISP_USER_GROUP_MODULE_ANDROID WHERE USGRMA_USER_ID = "+selected_user_id+" and USGRMA_GROUP_ID = "+userGroupLists.get(i).getGroup_id()+" and USGRMA_MSMA_ID = "+userGroupModuleLists.get(j).getMsma_id()+"");
                        }
                    }
                }
            }

            stmt.close();

            connected = true;

            connection.close();

        }
        catch (Exception e) {

            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}
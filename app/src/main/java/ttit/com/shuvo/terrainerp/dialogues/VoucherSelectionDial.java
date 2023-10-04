package ttit.com.shuvo.terrainerp.dialogues;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.WaitProgress;
import ttit.com.shuvo.terrainerp.adapters.VoucherSelectAdapter;
import ttit.com.shuvo.terrainerp.arrayList.VoucherSelectionList;
import ttit.com.shuvo.terrainerp.fragments.CreditVoucherApproved;
import ttit.com.shuvo.terrainerp.fragments.DebitVoucherApproved;
import ttit.com.shuvo.terrainerp.fragments.JournalVoucherApproved;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class VoucherSelectionDial extends AppCompatDialogFragment implements VoucherSelectAdapter.ClickedItem {

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
//    private Connection connection;

    RecyclerView accountsView;
    VoucherSelectAdapter accountsAdapter;
    RecyclerView.LayoutManager layoutManager;

    TextInputEditText search;

    AlertDialog alertDialog;

    String voucherType = "";

    String searchingName = "";

    ArrayList<VoucherSelectionList> voucherLists;
    ArrayList<VoucherSelectionList> filteredList;

    Boolean isfiltered = false;

    AppCompatActivity activity;

    Context mContext;

    public VoucherSelectionDial(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.voucher_select_list_view, null);

        activity = (AppCompatActivity) view.getContext();

        voucherLists = new ArrayList<>();
        filteredList = new ArrayList<>();
        accountsView = view.findViewById(R.id.select_voucher_list_view);
        search = view.findViewById(R.id.search_v_no_voucher_dialogue);

        builder.setView(view);

        alertDialog = builder.create();

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        if (CreditVoucherApproved.fromCVA == 1) {
            voucherType = "CV";
        }
        else if (DebitVoucherApproved.fromDVA == 1) {
            voucherType = "DV";
        }
        else if (JournalVoucherApproved.fromJVA == 1) {
            voucherType = "JV";
        }

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                searchingName = s.toString();
                filter(s.toString());
            }
        });

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                        event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        Log.i("Let see", "Come here");
                        search.clearFocus();
                        InputMethodManager mgr = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        closeKeyBoard();

                        return false; // consume.
                    }
                }
                return false;
            }
        });

        accountsView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        accountsView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(accountsView.getContext(),DividerItemDecoration.VERTICAL);
        accountsView.addItemDecoration(dividerItemDecoration);

        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                CreditVoucherApproved.fromCVA = 0;
                DebitVoucherApproved.fromDVA = 0;
                JournalVoucherApproved.fromJVA = 0;
                dialog.dismiss();
            }
        });

//        new Check().execute();
        getVoucherList();

        return alertDialog;
    }

    private void closeKeyBoard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void filter(String text) {

        filteredList = new ArrayList<>();
        for (VoucherSelectionList item : voucherLists) {
            if (item.getVm_no().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add((item));
                isfiltered = true;
            }
        }
        accountsAdapter.filterList(filteredList);
    }

//    public boolean isConnected() {
//        boolean connected = false;
//        boolean isMobile = false;
//        try {
//            ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo nInfo = cm.getActiveNetworkInfo();
//            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
//            return connected;
//        } catch (Exception e) {
//            Log.e("Connectivity Exception", e.getMessage());
//        }
//        return connected;
//    }
//
//    public boolean isOnline() {
//
//        Runtime runtime = Runtime.getRuntime();
//        try {
//            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
//            int     exitValue = ipProcess.waitFor();
//            return (exitValue == 0);
//        }
//        catch (IOException | InterruptedException e)          { e.printStackTrace(); }
//
//        return false;
//    }

    @Override
    public void onCategoryClicked(int CategoryPosition) {
        String name = "";
        String id = "";
        String status = "";
        String approvedUser = "";
        String approvedTime = "";
        if (isfiltered) {
            name = filteredList.get(CategoryPosition).getVm_no();
            id = filteredList.get(CategoryPosition).getVm_id();
            status = filteredList.get(CategoryPosition).getStatus();
            approvedUser = filteredList.get(CategoryPosition).getApprovedUser();
            approvedTime = filteredList.get(CategoryPosition).getApprovedTime();
        } else {
            name = voucherLists.get(CategoryPosition).getVm_no();
            id = voucherLists.get(CategoryPosition).getVm_id();
            status = voucherLists.get(CategoryPosition).getStatus();
            approvedUser = voucherLists.get(CategoryPosition).getApprovedUser();
            approvedTime = voucherLists.get(CategoryPosition).getApprovedTime();
        }

        System.out.println(id);

        if (CreditVoucherApproved.fromCVA == 1) {
            CreditVoucherApproved.cVm_id = id;
            CreditVoucherApproved.creditVoucherSelectSpinner.setText(name);
            CreditVoucherApproved.status_approved_CV = status;
            CreditVoucherApproved.appDisUser_CV = approvedUser;
            CreditVoucherApproved.appDisTime_CV = approvedTime;
        }
        else if (DebitVoucherApproved.fromDVA == 1) {
            DebitVoucherApproved.dVm_id = id;
            DebitVoucherApproved.debitVoucherSelectSpinner.setText(name);
            DebitVoucherApproved.status_approved_DV = status;
            DebitVoucherApproved.appDisUser_DV = approvedUser;
            DebitVoucherApproved.appDisTime_DV = approvedTime;
        }
        else if (JournalVoucherApproved.fromJVA == 1) {
            JournalVoucherApproved.jVm_id = id;
            JournalVoucherApproved.journalVoucherSelectSpinner.setText(name);
            JournalVoucherApproved.status_approved_JV = status;
            JournalVoucherApproved.appDisUser_JV = approvedUser;
            JournalVoucherApproved.appDisTime_JV = approvedTime;
        }

        CreditVoucherApproved.fromCVA = 0;
        DebitVoucherApproved.fromDVA = 0;
        JournalVoucherApproved.fromJVA = 0;
        alertDialog.dismiss();
    }

//    public class Check extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            waitProgress.show(activity.getSupportFragmentManager(),"WaitBar");
//            waitProgress.setCancelable(false);
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            if (isConnected() && isOnline()) {
//
//                ItemData();
//                if (connected) {
//                    conn = true;
//                }
//
//            } else {
//                conn = false;
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
//                conn = false;
//                connected = false;
//
//                accountsAdapter = new VoucherSelectAdapter(voucherLists,getContext(),VoucherSelectionDial.this);
//                accountsView.setAdapter(accountsAdapter);
//                accountsAdapter.notifyDataSetChanged();
//
//
//                //Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
//
//
//            }
//            else {
//                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog;
//                dialog = new AlertDialog.Builder(getContext())
//                        .setMessage("Please Check Your Internet Connection")
//                        .setPositiveButton("Retry", null)
//                        .setNegativeButton("Cancel",null)
//                        .show();
//
//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
//                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                positive.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        new Check().execute();
//                        dialog.dismiss();
//                    }
//                });
//
//                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                negative.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        CreditVoucherApproved.fromCVA = 0;
//                        DebitVoucherApproved.fromDVA = 0;
//                        JournalVoucherApproved.fromJVA = 0;
//                        dialog.dismiss();
//                        alertDialog.dismiss();
//
//                    }
//                });
//            }
//        }
//
//    }
//
//    public void ItemData() {
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//
//            voucherLists = new ArrayList<>();
//
//
//            Statement stmt = connection.createStatement();
//            ResultSet resultSet1 = stmt.executeQuery("SELECT\n" +
//                    "    acc_voucher_mst.vm_id,\n" +
//                    "    acc_voucher_mst.vm_no,\n" +
//                    "    TO_CHAR(acc_voucher_mst.vm_date,'dd-MON-yy') as vm_date,\n" +
//                    "    acc_voucher_mst.vm_type,\n" +
//                    "    acc_voucher_mst.vm_bill_ref_no,\n" +
//                    "    TO_CHAR(acc_voucher_mst.vm_bill_ref_date,'dd-MON-yy') as bill_date,\n" +
//                    "    acc_voucher_mst.vm_voucher_approved_flag,\n" +
//                    "    (select sum(NVL(acc_voucher_dtl.vd_dr_amt,0)) from acc_voucher_dtl where acc_voucher_dtl.vd_vm_id =  acc_voucher_mst.vm_id) as Amount,\n" +
//                    "    case\n" +
//                            "                        when acc_voucher_mst.vm_voucher_approved_flag = 0 OR acc_voucher_mst.vm_voucher_approved_flag is null then 'Pending'\n" +
//                            "                        when acc_voucher_mst.vm_voucher_approved_flag = 1 then 'Approved'\n" +
//                            "                        else null\n" +
//                            "                        end as STATUS,\n" +
//                    "(SELECT USR_FNAME || ' ' || USR_LNAME\n" +
//                    "                        FROM ISP_USER\n" +
//                    "                        WHERE USR_NAME = acc_voucher_mst.VM_VOUCHER_APPROVED_BY) as APPROVED_USER,\n" +
//                    "TO_CHAR(acc_voucher_mst.VM_VOUCHER_APPROVED_TIME,'dd-MON-yy, hh:mm am') as approved_date\n"+
//                    "FROM acc_voucher_mst\n" +
//                    "WHERE \n" +
//                    "    acc_voucher_mst.vm_type = '"+voucherType+"'\n" +
//                    "ORDER  BY acc_voucher_mst.vm_date DESC");
//
//
//            while (resultSet1.next()) {
//
//                voucherLists.add(new VoucherSelectionList(resultSet1.getString(1), resultSet1.getString(2), resultSet1.getString(3),
//                        resultSet1.getString(4),resultSet1.getString(5),resultSet1.getString(6),
//                        resultSet1.getString(7),resultSet1.getString(8),resultSet1.getString(9),
//                        resultSet1.getString(10),resultSet1.getString(11)));
//
//            }
//
//            resultSet1.close();
//
//            stmt.close();
//
//
//            connected = true;
//
//            connection.close();
//
//        }
//        catch (Exception e) {
//
//            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
//            Log.i("ERRRRR", e.getLocalizedMessage());
//            e.printStackTrace();
//        }
//    }

    public void getVoucherList() {
        waitProgress.show(activity.getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;
        voucherLists = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        String url = "http://103.56.208.123:8001/apex/tterp/dialogs/getVoucherSelectList?v_type="+voucherType+"";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        String vm_id = info.getString("vm_id")
                                .equals("null") ? "" : info.getString("vm_id");
                        String vm_no = info.getString("vm_no")
                                .equals("null") ? "" : info.getString("vm_no");
                        String vm_date = info.getString("vm_date")
                                .equals("null") ? "" : info.getString("vm_date");
                        String vm_type = info.getString("vm_type")
                                .equals("null") ? "" : info.getString("vm_type");
                        String vm_bill_ref_no = info.getString("vm_bill_ref_no")
                                .equals("null") ? "" : info.getString("vm_bill_ref_no");
                        String bill_date = info.getString("bill_date")
                                .equals("null") ? "" : info.getString("bill_date");
                        String vm_voucher_approved_flag = info.getString("vm_voucher_approved_flag")
                                .equals("null") ? "" : info.getString("vm_voucher_approved_flag");
                        String amount = info.getString("amount")
                                .equals("null") ? "0" : info.getString("amount");
                        String status = info.getString("status")
                                .equals("null") ? "" : info.getString("status");
                        String approved_user = info.getString("approved_user")
                                .equals("null") ? "" : info.getString("approved_user");
                        String approved_date = info.getString("approved_date")
                                .equals("null") ? "" : info.getString("approved_date");


                        voucherLists.add(new VoucherSelectionList(vm_id, vm_no, vm_date,
                                vm_type,vm_bill_ref_no,bill_date,
                                vm_voucher_approved_flag,amount,status,
                                approved_user,approved_date));
                    }
                }
                connected = true;
                updateFragment();
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateFragment();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateFragment();
        });

        requestQueue.add(stringRequest);
    }

    private void updateFragment() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                accountsAdapter = new VoucherSelectAdapter(voucherLists,getContext(),VoucherSelectionDial.this);
                accountsView.setAdapter(accountsAdapter);
                accountsAdapter.notifyDataSetChanged();
            }
            else {
                AlertDialog dialog;
                dialog = new AlertDialog.Builder(mContext)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getVoucherList();
                    dialog.dismiss();
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> {
                    CreditVoucherApproved.fromCVA = 0;
                    DebitVoucherApproved.fromDVA = 0;
                    JournalVoucherApproved.fromJVA = 0;
                    dialog.dismiss();
                    alertDialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog;
            dialog = new AlertDialog.Builder(mContext)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getVoucherList();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                CreditVoucherApproved.fromCVA = 0;
                DebitVoucherApproved.fromDVA = 0;
                JournalVoucherApproved.fromJVA = 0;
                dialog.dismiss();
                alertDialog.dismiss();
            });
        }
    }
}

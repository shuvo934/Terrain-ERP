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
import ttit.com.shuvo.terrainerp.adapters.PurchaseRequisitionSelectAdapter;
import ttit.com.shuvo.terrainerp.arrayList.PurchaseRequisionLists;
import ttit.com.shuvo.terrainerp.fragments.CreditVoucherApproved;
import ttit.com.shuvo.terrainerp.fragments.DebitVoucherApproved;
import ttit.com.shuvo.terrainerp.fragments.JournalVoucherApproved;
import ttit.com.shuvo.terrainerp.fragments.PurchaseReqApproved;
import ttit.com.shuvo.terrainerp.fragments.PurchaseRequisition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class PurchaseReqSelectDial extends AppCompatDialogFragment implements PurchaseRequisitionSelectAdapter.ClickedItem {

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
//    private Connection connection;

    RecyclerView accountsView;
    PurchaseRequisitionSelectAdapter purchaseRequisitionSelectAdapter;
    RecyclerView.LayoutManager layoutManager;

    TextInputEditText search;

    AlertDialog alertDialog;

    //String voucherType = "";

    String searchingName = "";

    ArrayList<PurchaseRequisionLists> purchaseRequisionLists;
    ArrayList<PurchaseRequisionLists> filteredList;

    Boolean isfiltered = false;

    AppCompatActivity activity;
    Context mContext;
    int selection;

    public PurchaseReqSelectDial(Context mContext, int selection) {
        this.mContext = mContext;
        this.selection = selection;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.purchase_requisition_list_view, null);

        activity = (AppCompatActivity) view.getContext();

        purchaseRequisionLists = new ArrayList<>();
        filteredList = new ArrayList<>();
        accountsView = view.findViewById(R.id.select_requisition_list_view);
        search = view.findViewById(R.id.search_req_no_pra_dialogue);

        builder.setView(view);

        alertDialog = builder.create();

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

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

                dialog.dismiss();
            }
        });

//        new Check().execute();
        getPurchaseReq();

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
        for (PurchaseRequisionLists item : purchaseRequisionLists) {
            if (item.getReqNo().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add((item));
                isfiltered = true;
            }
        }
        purchaseRequisitionSelectAdapter.filterList(filteredList);
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
        String pr_date = "";
        if (isfiltered) {
            name = filteredList.get(CategoryPosition).getReqNo();
            id = filteredList.get(CategoryPosition).getPrmId();
        } else {
            name = purchaseRequisionLists.get(CategoryPosition).getReqNo();
            id = purchaseRequisionLists.get(CategoryPosition).getPrmId();
        }

        System.out.println(id);

        if (selection == 1) {
            PurchaseReqApproved.prm_id = id;
            PurchaseReqApproved.purReqSelectSpinner.setText(name);
        }
        else if (selection == 2) {
            PurchaseRequisition.prm_id_to_update = id;
            PurchaseRequisition.req_no_to_show = name;
            PurchaseRequisition.purReqToUpdateSelectSpinner.setText(name);
        }


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
//                purchaseRequisitionSelectAdapter = new PurchaseRequisitionSelectAdapter(purchaseRequisionLists,getContext(),PurchaseReqSelectDial.this);
//                accountsView.setAdapter(purchaseRequisitionSelectAdapter);
//                purchaseRequisitionSelectAdapter.notifyDataSetChanged();
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

//    public void ItemData() {
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//
//            purchaseRequisionLists = new ArrayList<>();
//
//
//            Statement stmt = connection.createStatement();
//            ResultSet resultSet1 = stmt.executeQuery("select purchase_req_mst.prm_id, purchase_req_mst.prm_req_no, TO_CHAR(prm_req_date,'dd-MON-yy') pr_date,\n" +
//                    "                    (SELECT USR_FNAME || ' ' || USR_LNAME FROM ISP_USER \n" +
//                    "                    WHERE USR_NAME = purchase_req_mst.PRM_REQ_USER) user_name\n" +
//                    "                    from purchase_req_mst \n" +
//                    "                    where p_final_flag = 0 \n" +
//                    "                    and P_DISAPPROVED_FLAG = 1\n" +
//                    "                    and prm_req_flag = 3\n" +
//                    "                    and p_author_his is not NULL\n" +
//                    "                    order by prm_id desc");
//
//
//            while (resultSet1.next()) {
//
//                purchaseRequisionLists.add(new PurchaseRequisionLists(resultSet1.getString(1), resultSet1.getString(2), resultSet1.getString(3),
//                        resultSet1.getString(4)));
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

    public void getPurchaseReq() {
        waitProgress.show(activity.getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;
        purchaseRequisionLists = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        String url = "http://103.56.208.123:8001/apex/tterp/dialogs/getPurchaseReq";

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
                        String prm_id = info.getString("prm_id")
                                .equals("null") ? "" : info.getString("prm_id");
                        String prm_req_no = info.getString("prm_req_no")
                                .equals("null") ? "" : info.getString("prm_req_no");
                        String pr_date = info.getString("pr_date")
                                .equals("null") ? "" : info.getString("pr_date");
                        String user_name = info.getString("user_name")
                                .equals("null") ? "" : info.getString("user_name");

                        purchaseRequisionLists.add(new PurchaseRequisionLists(prm_id, prm_req_no, pr_date,
                                user_name));
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

                purchaseRequisitionSelectAdapter = new PurchaseRequisitionSelectAdapter(purchaseRequisionLists,getContext(),PurchaseReqSelectDial.this);
                accountsView.setAdapter(purchaseRequisitionSelectAdapter);
                purchaseRequisitionSelectAdapter.notifyDataSetChanged();
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
                    getPurchaseReq();
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
                getPurchaseReq();
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

package ttit.com.shuvo.icomerp.dialogues;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.WaitProgress;
import ttit.com.shuvo.icomerp.adapters.PurchaseOrderSelectAdapter;
import ttit.com.shuvo.icomerp.arrayList.PurchaseOrderSelectList;
import ttit.com.shuvo.icomerp.fragments.CreditVoucherApproved;
import ttit.com.shuvo.icomerp.fragments.DebitVoucherApproved;
import ttit.com.shuvo.icomerp.fragments.JournalVoucherApproved;
import ttit.com.shuvo.icomerp.fragments.PurchaseOrderApproved;

import static ttit.com.shuvo.icomerp.OracleConnection.createConnection;

public class PurchaseOrderSelectDial extends AppCompatDialogFragment implements PurchaseOrderSelectAdapter.ClickedItem{

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
    private Connection connection;

    RecyclerView accountsView;
    PurchaseOrderSelectAdapter purchaseOrderSelectAdapter;
    RecyclerView.LayoutManager layoutManager;

    TextInputEditText search;

    AlertDialog alertDialog;

    //String voucherType = "";

    String searchingName = "";

    ArrayList<PurchaseOrderSelectList> purchaseOrderSelectLists;
    ArrayList<PurchaseOrderSelectList> filteredList;

    Boolean isfiltered = false;

    AppCompatActivity activity;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.purchase_order_list_view, null);

        activity = (AppCompatActivity) view.getContext();

        purchaseOrderSelectLists = new ArrayList<>();
        filteredList = new ArrayList<>();
        accountsView = view.findViewById(R.id.select_order_list_view);
        search = view.findViewById(R.id.search_order_no_poa_dialogue);

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

        new Check().execute();

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

    public boolean isConnected() {
        boolean connected = false;
        boolean isMobile = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
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

    private void filter(String text) {

        filteredList = new ArrayList<>();
        for (PurchaseOrderSelectList item : purchaseOrderSelectLists) {
            if (item.getWom_no().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add((item));
                isfiltered = true;
            }
        }
        purchaseOrderSelectAdapter.filterList(filteredList);
    }

    @Override
    public void onCategoryClicked(int CategoryPosition) {
        String name = "";
        String id = "";
        if (isfiltered) {
            name = filteredList.get(CategoryPosition).getWom_no();
            id = filteredList.get(CategoryPosition).getWom_id();
        } else {
            name = purchaseOrderSelectLists.get(CategoryPosition).getWom_no();
            id = purchaseOrderSelectLists.get(CategoryPosition).getWom_id();
        }

        System.out.println(id);

        PurchaseOrderApproved.wom_id = id;
        PurchaseOrderApproved.purOrderSelectSpinner.setText(name);

        alertDialog.dismiss();
    }

    public class Check extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(activity.getSupportFragmentManager(),"WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                ItemData();
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

            waitProgress.dismiss();
            if (conn) {

                conn = false;
                connected = false;

                purchaseOrderSelectAdapter = new PurchaseOrderSelectAdapter(purchaseOrderSelectLists,getContext(),PurchaseOrderSelectDial.this);
                accountsView.setAdapter(purchaseOrderSelectAdapter);
                purchaseOrderSelectAdapter.notifyDataSetChanged();


                //Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();


            }else {
                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog;
                dialog = new AlertDialog.Builder(getContext())
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
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
                        CreditVoucherApproved.fromCVA = 0;
                        DebitVoucherApproved.fromDVA = 0;
                        JournalVoucherApproved.fromJVA = 0;
                        dialog.dismiss();
                        alertDialog.dismiss();

                    }
                });
            }
        }

    }

    public void ItemData() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            purchaseOrderSelectLists = new ArrayList<>();


            Statement stmt = connection.createStatement();
            ResultSet resultSet1 = stmt.executeQuery("select wom_id, wom_no, TO_CHAR(WOM_DATE,'dd MON, yy'),\n" +
                    "                    (SELECT USR_FNAME || ' ' || USR_LNAME FROM ISP_USER WHERE USR_NAME = WORK_ORDER_MST.WOM_USER) user_name,\n" +
                    "                    (select ad_name from acc_dtl where acc_dtl.ad_id = wom_ad_id)\n" +
                    "                    from WORK_ORDER_MST \n" +
                    "                    where work_order_mst.wom_type_flag = 2 \n" +
                    "                    and p_final_flag = 0\n" +
                    "                    and P_DISAPPROVED_FLAG = 1\n" +
                    "                    ORDER BY WOM_ID desc");


            while (resultSet1.next()) {

                purchaseOrderSelectLists.add(new PurchaseOrderSelectList(resultSet1.getString(1), resultSet1.getString(2), resultSet1.getString(3),
                        resultSet1.getString(4),resultSet1.getString(5)));

            }

            resultSet1.close();

            stmt.close();


            connected = true;

            connection.close();

        }
        catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}

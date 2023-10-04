package ttit.com.shuvo.terrainerp.dialogues;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.WaitProgress;
import ttit.com.shuvo.terrainerp.adapters.WoPoQtyAdapter;
import ttit.com.shuvo.terrainerp.arrayList.WoPoQtyLists;

import static ttit.com.shuvo.terrainerp.fragments.RelationsFragment.bill_amnt;
import static ttit.com.shuvo.terrainerp.fragments.RelationsFragment.firstDate;
import static ttit.com.shuvo.terrainerp.fragments.RelationsFragment.lastDate;
import static ttit.com.shuvo.terrainerp.fragments.RelationsFragment.rcv_amnt;
import static ttit.com.shuvo.terrainerp.fragments.RelationsFragment.selectedWOPO;
import static ttit.com.shuvo.terrainerp.fragments.RelationsFragment.supplier_ad_id_relation;
import static ttit.com.shuvo.terrainerp.fragments.RelationsFragment.wo_id;
import static ttit.com.shuvo.terrainerp.fragments.RelationsFragment.wo_po_amnt;
import static ttit.com.shuvo.terrainerp.fragments.RelationsFragment.wo_po_no;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WoPoDetails extends AppCompatDialogFragment {

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
//    private Connection connection;

    String wo_date = "";

    TextView woPoNo;
    TextView woDate;
    TextView noRcv;

    RecyclerView woItemView;
    RecyclerView.LayoutManager layoutManager;
    WoPoQtyAdapter woPoQtyAdapter;

    TextView woAmount;

    LinearLayout purchaseLay;

    RecyclerView poItemView;
    RecyclerView.LayoutManager layoutManager1;
    WoPoQtyAdapter woPoQtyAdapterPo;

    TextView poAmount;
    TextView poRcvble;

    TextView billAmount;
    TextView billRcvble;

    AlertDialog alertDialog;
    AppCompatActivity activity;

    ArrayList<WoPoQtyLists> woPoListsWo;
    ArrayList<WoPoQtyLists> woPoListsPo;

    Context mContext;

    public WoPoDetails(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.wo_po_bill_details_view, null);

        woPoListsPo = new ArrayList<>();
        woPoListsWo = new ArrayList<>();

        activity = (AppCompatActivity) view.getContext();

        purchaseLay = view.findViewById(R.id.purchase_layout);
        noRcv = view.findViewById(R.id.no_rcv_msg);
        woDate = view.findViewById(R.id.wo_po_date);
        noRcv.setVisibility(View.GONE);
        purchaseLay.setVisibility(View.GONE);
        woPoNo = view.findViewById(R.id.wo_po_no);

        woItemView = view.findViewById(R.id.wo_po_item_details);

        woAmount = view.findViewById(R.id.wo_po_total_amount);

        poItemView = view.findViewById(R.id.wo_po_rcv_item_details);

        poAmount = view.findViewById(R.id.wo_po_rcv_total_amount);
        poRcvble = view.findViewById(R.id.wo_po_rcvable_total_amount);
        billAmount = view.findViewById(R.id.wo_po_bill_total_amount);
        billRcvble = view.findViewById(R.id.wo_po_bill_bal_total_amount);


        woAmount.setText(wo_po_amnt+ " BDT");
        poAmount.setText(rcv_amnt + " BDT");

        int rcvble = Integer.parseInt(wo_po_amnt) - Integer.parseInt(rcv_amnt);

        poRcvble.setText(rcvble+ " BDT");

        billAmount.setText(bill_amnt + " BDT");

        int bal = Integer.parseInt(wo_po_amnt) - Integer.parseInt(bill_amnt);

        billRcvble.setText(bal+ " BDT");

        woPoNo.setText(wo_po_no);

        woItemView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        woItemView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(woItemView.getContext(),DividerItemDecoration.VERTICAL);
        woItemView.addItemDecoration(dividerItemDecoration);

        poItemView.setHasFixedSize(true);
        layoutManager1 = new LinearLayoutManager(getContext());
        poItemView.setLayoutManager(layoutManager1);

        DividerItemDecoration dividerItemDecoration1 =
                new DividerItemDecoration(poItemView.getContext(),DividerItemDecoration.VERTICAL);
        poItemView.addItemDecoration(dividerItemDecoration1);

        if (selectedWOPO.equals("2")) {
            purchaseLay.setVisibility(View.GONE);
            noRcv.setVisibility(View.VISIBLE);
        } else {
            purchaseLay.setVisibility(View.VISIBLE);
            noRcv.setVisibility(View.GONE);
        }


//        new CheckFORLISt().execute();
        getItems();

        builder.setView(view);

        alertDialog = builder.create();

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        return alertDialog;
    }

//    public boolean isConnected() {
//        boolean connected = false;
//        boolean isMobile = false;
//        try {
//            ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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
//
//    public class CheckFORLISt extends AsyncTask<Void, Void, Void> {
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
//                //Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
//                if (woPoListsPo.size() == 0) {
//                    woPoListsPo = new ArrayList<>();
//
//                    woPoQtyAdapterPo = new WoPoQtyAdapter(woPoListsPo,getContext());
//                    poItemView.setAdapter(woPoQtyAdapterPo);
//                    woPoQtyAdapterPo.notifyDataSetChanged();
//
//                    noRcv.setVisibility(View.VISIBLE);
//                    purchaseLay.setVisibility(View.GONE);
//                } else {
//
//                    woPoQtyAdapterPo = new WoPoQtyAdapter(woPoListsPo,getContext());
//                    poItemView.setAdapter(woPoQtyAdapterPo);
//                    woPoQtyAdapterPo.notifyDataSetChanged();
//
//                }
//
//                woPoQtyAdapter = new WoPoQtyAdapter(woPoListsWo,getContext());
//                woItemView.setAdapter(woPoQtyAdapter);
//                woPoQtyAdapter.notifyDataSetChanged();
//
//                woDate.setText(wo_date);
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
//                        new CheckFORLISt().execute();
//                        dialog.dismiss();
//                    }
//                });
//
//                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                negative.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                        alertDialog.dismiss();
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
//            woPoListsPo = new ArrayList<>();
//            woPoListsWo = new ArrayList<>();
//
//            Statement stmt = connection.createStatement();
//
//            if (firstDate.isEmpty()) {
//                firstDate = null;
//            }
//            if (lastDate.isEmpty()) {
//                lastDate = null;
//            }
//            if (supplier_ad_id_relation.isEmpty()) {
//                supplier_ad_id_relation = null;
//            }
//
//
//            CallableStatement callableStatement1 = connection.prepareCall("begin ? := GET_WO_PO_LIST(?,?,?,?,?,?,?); end;");
//            callableStatement1.setString(2,firstDate);
//            callableStatement1.setString(3,lastDate);
//            callableStatement1.setString(4,supplier_ad_id_relation);
//            callableStatement1.setString(5,"1");
//            callableStatement1.setString(6,selectedWOPO);
//            callableStatement1.setString(7,wo_id);
//            callableStatement1.setInt(8,3);
//            callableStatement1.registerOutParameter(1, OracleTypes.CURSOR);
//            callableStatement1.execute();
//
//            ResultSet resultSet1 = (ResultSet) callableStatement1.getObject(1);
//
//
//            while (resultSet1.next()) {
//
//                wo_date = resultSet1.getString(2);
//                System.out.println(resultSet1.getString(1));
//                woPoListsWo.add(new WoPoQtyLists(resultSet1.getString(4), resultSet1.getString(6)+" "+resultSet1.getString(5),resultSet1.getString(7)));
//
//            }
//
//            callableStatement1.close();
//
//            if (selectedWOPO.equals("1")) {
//
//                CallableStatement callableStatement2 = connection.prepareCall("begin ? := GET_WO_PO_LIST(?,?,?,?,?,?,?); end;");
//                callableStatement2.setString(2,firstDate);
//                callableStatement2.setString(3,lastDate);
//                callableStatement2.setString(4,supplier_ad_id_relation);
//                callableStatement2.setString(5,"1");
//                callableStatement2.setString(6,selectedWOPO);
//                callableStatement2.setString(7,wo_id);
//                callableStatement2.setInt(8,4);
//                callableStatement2.registerOutParameter(1, OracleTypes.CURSOR);
//                callableStatement2.execute();
//
//                ResultSet resultSet2 = (ResultSet) callableStatement2.getObject(1);
//
//
//                while (resultSet2.next()) {
//
//                    System.out.println(resultSet2.getString(1));
//                    woPoListsPo.add(new WoPoQtyLists(resultSet2.getString(3), resultSet2.getString(5)+" "+resultSet2.getString(4),resultSet2.getString(6)));
//
//                }
//
//                callableStatement2.close();
//            }
//
//
//            if (firstDate == null) {
//                firstDate = "";
//            }
//            if (lastDate == null) {
//                lastDate = "";
//            }
//            if (supplier_ad_id_relation == null) {
//                supplier_ad_id_relation = "";
//            }
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

    public void getItems() {
        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        String woUrl = "http://103.56.208.123:8001/apex/tterp/relations/getWOPOLists?st_date="+firstDate+"&end_date="+lastDate+"&ad_id="+supplier_ad_id_relation+"&approve_flag=1&wom_wo_type="+selectedWOPO+"&wom_id="+wo_id+"&options=3";
        String prUrl = "http://103.56.208.123:8001/apex/tterp/relations/getWOPOLists?st_date="+firstDate+"&end_date="+lastDate+"&ad_id="+supplier_ad_id_relation+"&approve_flag=1&wom_wo_type="+selectedWOPO+"&wom_id="+wo_id+"&options=4";

        StringRequest woPoReqPo = new StringRequest(Request.Method.GET, prUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        String wo_po_list = info.getString("wo_po_list");
                        JSONArray wopoArray = new JSONArray(wo_po_list);
                        for (int j = 0; j < wopoArray.length(); j++) {
                            JSONObject wo_po_info = wopoArray.getJSONObject(j);

//                            String rm_wom_id = wo_po_info.getString("rm_wom_id");
//                            String rd_item_id = wo_po_info.getString("rd_item_id");
                            String item_reference_name = wo_po_info.getString("item_reference_name");
                            String item_unit = wo_po_info.getString("item_unit");
                            String rd_qty = wo_po_info.getString("rd_qty")
                                    .equals("null") ? "0" : wo_po_info.getString("rd_qty");
                            String rd_rate = wo_po_info.getString("rd_rate")
                                    .equals("null") ? "0" : wo_po_info.getString("rd_rate");

                            woPoListsPo.add(new WoPoQtyLists(item_reference_name, rd_qty+" "+item_unit,rd_rate));

                        }
                    }
                }
                connected = true;
                updateLayout();
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateLayout();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateLayout();
        });

        StringRequest woPoReqWo = new StringRequest(Request.Method.GET, woUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        String wo_po_list = info.getString("wo_po_list");
                        JSONArray wopoArray = new JSONArray(wo_po_list);
                        for (int j = 0; j < wopoArray.length(); j++) {
                            JSONObject wo_po_info = wopoArray.getJSONObject(j);

//                            String wom_id = wo_po_info.getString("wom_id");
                            wo_date = wo_po_info.getString("wom_date");
//                            String wod_item_id = wo_po_info.getString("wod_item_id");
                            String item_reference_name = wo_po_info.getString("item_reference_name");
                            String item_unit = wo_po_info.getString("item_unit");
                            String wod_qty = wo_po_info.getString("wod_qty")
                                    .equals("null") ? "0" : wo_po_info.getString("wod_qty");
                            String wod_rate = wo_po_info.getString("wod_rate")
                                    .equals("null") ? "0" : wo_po_info.getString("wod_rate");

                            woPoListsWo.add(new WoPoQtyLists(item_reference_name, wod_qty+" "+item_unit,wod_rate));

                        }
                    }
                }
                if (selectedWOPO.equals("1")) {
                    requestQueue.add(woPoReqPo);
                }
                else {
                    connected = true;
                    updateLayout();
                }

            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateLayout();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateLayout();
        });

        requestQueue.add(woPoReqWo);
    }

    private void updateLayout() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                //Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                if (woPoListsPo.size() == 0) {
                    woPoListsPo = new ArrayList<>();

                    woPoQtyAdapterPo = new WoPoQtyAdapter(woPoListsPo,getContext());
                    poItemView.setAdapter(woPoQtyAdapterPo);
                    woPoQtyAdapterPo.notifyDataSetChanged();

                    noRcv.setVisibility(View.VISIBLE);
                    purchaseLay.setVisibility(View.GONE);
                } else {

                    woPoQtyAdapterPo = new WoPoQtyAdapter(woPoListsPo,getContext());
                    poItemView.setAdapter(woPoQtyAdapterPo);
                    woPoQtyAdapterPo.notifyDataSetChanged();

                }

                woPoQtyAdapter = new WoPoQtyAdapter(woPoListsWo,getContext());
                woItemView.setAdapter(woPoQtyAdapter);
                woPoQtyAdapter.notifyDataSetChanged();

                woDate.setText(wo_date);
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

                    getItems();
                    dialog.dismiss();
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> {
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

                getItems();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                alertDialog.dismiss();
            });
        }
    }

}

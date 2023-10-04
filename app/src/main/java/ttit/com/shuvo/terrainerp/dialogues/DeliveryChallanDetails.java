package ttit.com.shuvo.terrainerp.dialogues;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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

import java.text.DecimalFormat;
import java.util.ArrayList;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.WaitProgress;
import ttit.com.shuvo.terrainerp.adapters.DCItemDetailsAdapter;
import ttit.com.shuvo.terrainerp.adapters.VouchTrans2Adapter;
import ttit.com.shuvo.terrainerp.arrayList.DCColorWiseItemList;
import ttit.com.shuvo.terrainerp.arrayList.DeliveryChallanDetailsList;
import ttit.com.shuvo.terrainerp.fragments.CashTransaction;
import ttit.com.shuvo.terrainerp.fragments.DeliveryFragement;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DeliveryChallanDetails extends AppCompatDialogFragment {

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
//    private Connection connection;

    TextView invNo;
    TextView invDate;
    TextView salesOrderNo;
    TextView salesOrderDate;
    TextView clientName;
    TextView clientCode;
    TextView tDAddress;
    TextView eDDate;
    TextView contactPerson;
    TextView contactNo;
    TextView clientEmail;

    TextView totalAmount;
    TextView totalInvQty;
    TextView totalVat;
    TextView totalInvAmount;

    RecyclerView itemView;
    RecyclerView.LayoutManager layoutManager;
    DCItemDetailsAdapter dcItemDetailsAdapter;

    AlertDialog alertDialog;
    AppCompatActivity activity;
    String sm_id = "";

    double total_amnt = 0.0;
    double total_qty = 0.0;
    double total_vat = 0.0;
    double total_inv_amnt = 0.0;

    ArrayList<DeliveryChallanDetailsList> deliveryChallanDetailsLists;
    Context mContext;

    public DeliveryChallanDetails(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.delivery_challan_details_view, null);

        activity = (AppCompatActivity) view.getContext();

        invNo = view.findViewById(R.id.dc_no);
        invDate = view.findViewById(R.id.dc_date);
        salesOrderNo = view.findViewById(R.id.so_no_dc);
        salesOrderDate = view.findViewById(R.id.so_date_dc);
        clientName = view.findViewById(R.id.client_name_dc_from_list);
        clientCode = view.findViewById(R.id.client_code_dc);
        tDAddress = view.findViewById(R.id.target_delivery_address_dc);
        eDDate = view.findViewById(R.id.edd_dc);
        contactNo = view.findViewById(R.id.contact_no_dc);
        clientEmail = view.findViewById(R.id.contact_email_dc);
        contactPerson = view.findViewById(R.id.contact_person_dc);

        totalAmount = view.findViewById(R.id.total_item_wise_amnt_dc);
        totalInvQty = view.findViewById(R.id.total_item_wise_qty_dc);

        totalVat = view.findViewById(R.id.total_item_wise_vat_amnt_dc);
        totalInvAmount = view.findViewById(R.id.total_item_wise_inv_amnt_dc);

        itemView = view.findViewById(R.id.delivery_challan_details_report_view);

        deliveryChallanDetailsLists = new ArrayList<>();

        itemView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        itemView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(itemView.getContext(),DividerItemDecoration.VERTICAL);
        itemView.addItemDecoration(dividerItemDecoration);

        if (DeliveryFragement.fromDC == 1) {
            invNo.setText(DeliveryFragement.INV_NO);
            invDate.setText(DeliveryFragement.INV_DATE);
            salesOrderNo.setText(DeliveryFragement.SO_NO);
            salesOrderDate.setText(DeliveryFragement.SO_DATE);
            clientName.setText(DeliveryFragement.C_NAME);
            clientCode.setText(DeliveryFragement.C_CODE);
            tDAddress.setText(DeliveryFragement.ADDS);
            eDDate.setText(DeliveryFragement.EDD);
            contactNo.setText(DeliveryFragement.CONTACT);
            clientEmail.setText(DeliveryFragement.C_EMAIL);
            contactPerson.setText(DeliveryFragement.PERSON);

            sm_id = DeliveryFragement.SM_ID;
        } else if (VouchTrans2Adapter.fromVTSO == 1) {
            invNo.setText(VouchTrans2Adapter.INV_NO);
            invDate.setText(VouchTrans2Adapter.INV_DATE);
            salesOrderNo.setText(VouchTrans2Adapter.SO_NO);
            salesOrderDate.setText(VouchTrans2Adapter.SO_DATE);
            clientName.setText(VouchTrans2Adapter.C_NAME);
            clientCode.setText(VouchTrans2Adapter.C_CODE);
            tDAddress.setText(VouchTrans2Adapter.ADDS);
            eDDate.setText(VouchTrans2Adapter.EDD);
            contactNo.setText(VouchTrans2Adapter.CONTACT);
            clientEmail.setText(VouchTrans2Adapter.C_EMAIL);
            contactPerson.setText(VouchTrans2Adapter.PERSON);

            sm_id = VouchTrans2Adapter.SM_ID;
        } else if (CashTransaction.fromCS == 1) {
            invNo.setText(CashTransaction.INV_NO);
            invDate.setText(CashTransaction.INV_DATE);
            salesOrderNo.setText(CashTransaction.SO_NO);
            salesOrderDate.setText(CashTransaction.SO_DATE);
            clientName.setText(CashTransaction.C_NAME);
            clientCode.setText(CashTransaction.C_CODE);
            tDAddress.setText(CashTransaction.ADDS);
            eDDate.setText(CashTransaction.EDD);
            contactNo.setText(CashTransaction.CONTACT);
            clientEmail.setText(CashTransaction.C_EMAIL);
            contactPerson.setText(CashTransaction.PERSON);

            sm_id = CashTransaction.SM_ID;
        }


//        new CheckFORLISt().execute();
        getItemData();

        builder.setView(view);

        alertDialog = builder.create();

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CashTransaction.fromCS = 0;
                VouchTrans2Adapter.fromVTSO = 0;
                DeliveryFragement.fromDC = 0;
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
//                dcItemDetailsAdapter = new DCItemDetailsAdapter(deliveryChallanDetailsLists,getContext());
//                itemView.setAdapter(dcItemDetailsAdapter);
//                dcItemDetailsAdapter.notifyDataSetChanged();
//
//                DecimalFormat formatter = new DecimalFormat("##,##,##,###.##");
//                String formatted = formatter.format(total_amnt);
//
//                totalAmount.setText(formatted);
//
//                if ( DeliveryFragement.fromDC == 1) {
//                    if (DeliveryFragement.VAT_AMNT_DC != null) {
//                        if (!DeliveryFragement.VAT_AMNT_DC.isEmpty()) {
//                            total_vat = Double.parseDouble(DeliveryFragement.VAT_AMNT_DC);
//
//                        } else {
//                            total_vat = 0.0;
//                        }
//
//                    } else {
//                        total_vat = 0.0;
//
//                    }
//                }
//                else if (VouchTrans2Adapter.fromVTSO == 1) {
//                    if (VouchTrans2Adapter.VAT_AMNT_DC != null) {
//                        if (!VouchTrans2Adapter.VAT_AMNT_DC.isEmpty()) {
//                            total_vat = Double.parseDouble(VouchTrans2Adapter.VAT_AMNT_DC);
//
//                        } else {
//                            total_vat = 0.0;
//                        }
//
//                    } else {
//                        total_vat = 0.0;
//
//                    }
//                } else if (CashTransaction.fromCS == 1) {
//                    if (CashTransaction.VAT_AMNT_DC != null) {
//                        if (!CashTransaction.VAT_AMNT_DC.isEmpty()) {
//                            total_vat = Double.parseDouble(CashTransaction.VAT_AMNT_DC);
//
//                        } else {
//                            total_vat = 0.0;
//                        }
//
//                    } else {
//                        total_vat = 0.0;
//
//                    }
//                }
//
//                formatted = formatter.format(total_vat);
//                totalVat.setText(formatted);
//
//                total_inv_amnt = total_amnt + total_vat;
//                formatted = formatter.format(total_inv_amnt);
//                totalInvAmount.setText(formatted);
//
//                int qqqq = (int) total_qty;
//                totalInvQty.setText(String.valueOf(qqqq));
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
//                        new CheckFORLISt().execute();
//                        dialog.dismiss();
//                    }
//                });
//
//                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                negative.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        DeliveryFragement.fromDC = 0;
//                        VouchTrans2Adapter.fromVTSO = 0;
//                        CashTransaction.fromCS = 0;
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
//            deliveryChallanDetailsLists = new ArrayList<>();
//            total_amnt = 0.0;
//            total_qty = 0.0;
//            total_inv_amnt = 0.0;
//            total_vat = 0.0;
//
//            CallableStatement callableStatement1 = connection.prepareCall("begin ? := GET_INVOICE_DTL_REGISTER_LIST(?,?,?); end;");
//            callableStatement1.setInt(2,Integer.parseInt(sm_id));
//            callableStatement1.setString(3,null);
//            callableStatement1.setInt(4,2);
//
//            callableStatement1.registerOutParameter(1, OracleTypes.CURSOR);
//            callableStatement1.execute();
//
//            ResultSet resultSet1 = (ResultSet) callableStatement1.getObject(1);
//
//            while (resultSet1.next()) {
//
//
//                ArrayList<DCColorWiseItemList> dcColorWiseItemLists = new ArrayList<>();
//                String itemId = resultSet1.getString(7);
//
//                CallableStatement callableStatement = connection.prepareCall("begin ? := GET_INVOICE_DTL_REGISTER_LIST(?,?,?); end;");
//                callableStatement.setInt(2,Integer.parseInt(sm_id));
//                callableStatement.setString(3,itemId);
//                callableStatement.setInt(4,1);
//                callableStatement.registerOutParameter(1,OracleTypes.CURSOR);
//                callableStatement.execute();
//
//                ResultSet rs = (ResultSet) callableStatement.getObject(1);
//
//                while (rs.next()) {
//                    dcColorWiseItemLists.add(new DCColorWiseItemList(rs.getString(1),rs.getString(2),rs.getString(3),
//                            rs.getString(4),rs.getString(5),rs.getString(6),
//                            rs.getString(7),rs.getString(8),rs.getString(9),
//                            rs.getString(10),rs.getString(11),rs.getString(12),
//                            rs.getString(13),rs.getString(14),rs.getString(15),
//                            rs.getString(16),rs.getString(17),rs.getString(18),
//                            rs.getString(19),rs.getString(20),rs.getString(21)));
//                }
//
//                callableStatement.close();
//
//                System.out.println(resultSet1.getString(3));
//                deliveryChallanDetailsLists.add(new DeliveryChallanDetailsList(resultSet1.getString(1), resultSet1.getString(2), resultSet1.getString(3),
//                        resultSet1.getString(4),resultSet1.getString(5),resultSet1.getString(6),
//                        resultSet1.getString(7),dcColorWiseItemLists));
//
//
//                if (resultSet1.getString(5) != null) {
//                    total_amnt = total_amnt + Double.parseDouble(resultSet1.getString(5));
//                }
//                if (resultSet1.getString(6) != null) {
//                    total_qty = total_qty + Double.parseDouble(resultSet1.getString(6));
//                }
//
//
//            }
//
//            callableStatement1.close();
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

    public void getItemData() {
        waitProgress.show(activity.getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;
        deliveryChallanDetailsLists = new ArrayList<>();
        total_amnt = 0.0;
        total_qty = 0.0;
        total_inv_amnt = 0.0;
        total_vat = 0.0;

        String url = "http://103.56.208.123:8001/apex/tterp/dialogs/getInvoiceDetails?sm_id="+sm_id+"&item_id=&options=2";
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url ,response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        String invoice_dtl_register_list = info.getString("invoice_dtl_register_list");
                        JSONArray invArray = new JSONArray(invoice_dtl_register_list);
                        for (int j = 0; j < invArray.length(); j++) {
                            JSONObject inv_info = invArray.getJSONObject(j);
                            String sm_id_new = inv_info.getString("sm_id");
                            String sd_id = inv_info.getString("sd_id");
                            String item_reference_name = inv_info.getString("item_reference_name");
                            String item_part_number = inv_info.getString("item_part_number")
                                    .equals("null") ? "" : inv_info.getString("item_part_number");
                            String item_wise_amt = inv_info.getString("item_wise_amt")
                                    .equals("null") ? "0" : inv_info.getString("item_wise_amt");
                            String item_wise_qty = inv_info.getString("item_wise_qty")
                                    .equals("null") ? "0" : inv_info.getString("item_wise_qty");
                            String item_id = inv_info.getString("item_id")
                                    .equals("null") ? "" : inv_info.getString("item_id");

                            deliveryChallanDetailsLists.add(new DeliveryChallanDetailsList(sm_id_new, sd_id, item_reference_name,
                                    item_part_number,item_wise_amt,item_wise_qty,
                                    item_id,new ArrayList<>(),false));

                            total_amnt = total_amnt + Double.parseDouble(item_wise_amt);
                            total_qty = total_qty + Double.parseDouble(item_wise_qty);
                        }
                    }
                }

                checkToGetItemDetails();
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

        requestQueue.add(stringRequest);
    }

    private void checkToGetItemDetails() {
        if (deliveryChallanDetailsLists.size() != 0) {
            boolean allUpdated = false;
            for (int i = 0; i < deliveryChallanDetailsLists.size(); i++) {
                allUpdated = deliveryChallanDetailsLists.get(i).isUpdated();
                if (!deliveryChallanDetailsLists.get(i).isUpdated()) {
                    allUpdated = deliveryChallanDetailsLists.get(i).isUpdated();
                    String itm_id = deliveryChallanDetailsLists.get(i).getItem_id();

                    getItemDetails(itm_id, i);
                    break;
                }
            }
            if (allUpdated) {
                connected = true;
                updateLayout();
            }
        }
        else {
            connected = true;
            updateLayout();
        }
    }

    public void getItemDetails(String item_id, int index) {
        ArrayList<DCColorWiseItemList> dcColorWiseItemLists = new ArrayList<>();
        String url = "http://103.56.208.123:8001/apex/tterp/dialogs/getInvoiceDetails?sm_id="+sm_id+"&item_id="+item_id+"&options=1";
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url ,response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        String invoice_dtl_register_list = info.getString("invoice_dtl_register_list");
                        JSONArray invArray = new JSONArray(invoice_dtl_register_list);
                        for (int j = 0; j < invArray.length(); j++) {
                            JSONObject inv_info = invArray.getJSONObject(j);

                            String sm_id_new = inv_info.getString("sm_id");
                            String sd_id = inv_info.getString("sd_id");
                            String color_name = inv_info.getString("color_name")
                                    .equals("null") ? "" : inv_info.getString("color_name");
                            String size_name = inv_info.getString("size_name")
                                    .equals("null") ? "" : inv_info.getString("size_name");
                            String sd_qty = inv_info.getString("sd_qty")
                                    .equals("null") ? "0" : inv_info.getString("sd_qty");
                            String item_unit = inv_info.getString("item_unit")
                                    .equals("null") ? "" : inv_info.getString("item_unit");
                            String price = inv_info.getString("price")
                                    .equals("null") ? "0" : inv_info.getString("price");
                            String sd_discount_type = inv_info.getString("sd_discount_type")
                                    .equals("null") ? "" : inv_info.getString("sd_discount_type");
                            String sd_discount_value = inv_info.getString("sd_discount_value")
                                    .equals("null") ? "0" : inv_info.getString("sd_discount_value");
                            String sod_weighted_discount_type = inv_info.getString("sod_weighted_discount_type")
                                    .equals("null") ? "" : inv_info.getString("sod_weighted_discount_type");
                            String sod_weighted_discount_value = inv_info.getString("sod_weighted_discount_value")
                                    .equals("null") ? "0" : inv_info.getString("sod_weighted_discount_value");
                            String sd_tot_disc_amt = inv_info.getString("sd_tot_disc_amt")
                                    .equals("null") ? "0" : inv_info.getString("sd_tot_disc_amt");
                            String sd_rate = inv_info.getString("sd_rate")
                                    .equals("null") ? "0" : inv_info.getString("sd_rate");
                            String amt = inv_info.getString("amt")
                                    .equals("null") ? "0" : inv_info.getString("amt");
                            String sd_sample_qty = inv_info.getString("sd_sample_qty")
                                    .equals("null") ? "0" : inv_info.getString("sd_sample_qty");
                            String sd_vat_amt = inv_info.getString("sd_vat_amt")
                                    .equals("null") ? "0" : inv_info.getString("sd_vat_amt");
                            String vat_amt_item = inv_info.getString("vat_amt_item")
                                    .equals("null") ? "" : inv_info.getString("vat_amt_item");
                            String sd_return_mark = inv_info.getString("sd_return_mark")
                                    .equals("null") ? "" : inv_info.getString("sd_return_mark");
                            String ret_amt = inv_info.getString("ret_amt")
                                    .equals("null") ? "" : inv_info.getString("ret_amt");
                            String color_wise_qty = inv_info.getString("color_wise_qty")
                                    .equals("null") ? "" : inv_info.getString("color_wise_qty");
                            String color_wise_amt = inv_info.getString("color_wise_amt")
                                    .equals("null") ? "" : inv_info.getString("color_wise_amt");


                            dcColorWiseItemLists.add(new DCColorWiseItemList(sm_id_new,sd_id,color_name,
                                    size_name,sd_qty,item_unit,
                                    price,sd_discount_type,sd_discount_value,
                                    sod_weighted_discount_type,sod_weighted_discount_value,sd_tot_disc_amt,
                                    sd_rate,amt,sd_sample_qty,
                                    sd_vat_amt,vat_amt_item,sd_return_mark,
                                    ret_amt,color_wise_qty,color_wise_amt));
                        }
                    }
                }
                deliveryChallanDetailsLists.get(index).setDcColorWiseItemLists(dcColorWiseItemLists);
                deliveryChallanDetailsLists.get(index).setUpdated(true);
                checkToGetItemDetails();
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

        requestQueue.add(stringRequest);
    }

    private void updateLayout() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                dcItemDetailsAdapter = new DCItemDetailsAdapter(deliveryChallanDetailsLists,getContext());
                itemView.setAdapter(dcItemDetailsAdapter);
                dcItemDetailsAdapter.notifyDataSetChanged();

                DecimalFormat formatter = new DecimalFormat("##,##,##,###.##");
                String formatted = formatter.format(total_amnt);

                totalAmount.setText(formatted);

                if ( DeliveryFragement.fromDC == 1) {
                    if (DeliveryFragement.VAT_AMNT_DC != null) {
                        if (!DeliveryFragement.VAT_AMNT_DC.isEmpty()) {
                            total_vat = Double.parseDouble(DeliveryFragement.VAT_AMNT_DC);

                        } else {
                            total_vat = 0.0;
                        }

                    } else {
                        total_vat = 0.0;

                    }
                }
                else if (VouchTrans2Adapter.fromVTSO == 1) {
                    if (VouchTrans2Adapter.VAT_AMNT_DC != null) {
                        if (!VouchTrans2Adapter.VAT_AMNT_DC.isEmpty()) {
                            total_vat = Double.parseDouble(VouchTrans2Adapter.VAT_AMNT_DC);

                        } else {
                            total_vat = 0.0;
                        }

                    } else {
                        total_vat = 0.0;

                    }
                } else if (CashTransaction.fromCS == 1) {
                    if (CashTransaction.VAT_AMNT_DC != null) {
                        if (!CashTransaction.VAT_AMNT_DC.isEmpty()) {
                            total_vat = Double.parseDouble(CashTransaction.VAT_AMNT_DC);

                        } else {
                            total_vat = 0.0;
                        }

                    } else {
                        total_vat = 0.0;

                    }
                }

                formatted = formatter.format(total_vat);
                totalVat.setText(formatted);

                total_inv_amnt = total_amnt + total_vat;
                formatted = formatter.format(total_inv_amnt);
                totalInvAmount.setText(formatted);

                int qqqq = (int) total_qty;
                totalInvQty.setText(String.valueOf(qqqq));
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

                    getItemData();
                    dialog.dismiss();
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> {
                    DeliveryFragement.fromDC = 0;
                    VouchTrans2Adapter.fromVTSO = 0;
                    CashTransaction.fromCS = 0;
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

                getItemData();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                DeliveryFragement.fromDC = 0;
                VouchTrans2Adapter.fromVTSO = 0;
                CashTransaction.fromCS = 0;
                dialog.dismiss();
                alertDialog.dismiss();
            });
        }
    }
}

package ttit.com.shuvo.icomerp.dialogues;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import ttit.com.shuvo.icomerp.adapters.SalesOrderItemAdapter;
import ttit.com.shuvo.icomerp.adapters.VouchTrans2Adapter;
import ttit.com.shuvo.icomerp.adapters.WoPoQtyAdapter;
import ttit.com.shuvo.icomerp.arrayList.SalesOrderItemLists;
import ttit.com.shuvo.icomerp.arrayList.WoPoQtyLists;
import ttit.com.shuvo.icomerp.fragments.CashTransaction;
import ttit.com.shuvo.icomerp.fragments.SalesOrderFragment;

import static ttit.com.shuvo.icomerp.OracleConnection.createConnection;
import static ttit.com.shuvo.icomerp.fragments.RelationsFragment.firstDate;
import static ttit.com.shuvo.icomerp.fragments.RelationsFragment.lastDate;
import static ttit.com.shuvo.icomerp.fragments.RelationsFragment.selectedWOPO;
import static ttit.com.shuvo.icomerp.fragments.RelationsFragment.supplier_ad_id_relation;
import static ttit.com.shuvo.icomerp.fragments.RelationsFragment.wo_id;


public class SalesOrderDetails extends AppCompatDialogFragment {

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
    private Connection connection;

    TextView salesOrderNo;
    TextView salesOrderDate;
    TextView clientName;
    TextView clientCode;
    TextView tDAddress;
    TextView eDDate;
    TextView contactPerson;
    TextView contactNo;
    TextView clientEmail;
    TextView totalOrderAmount;
    TextView advanceAmnt;
    TextView remainAmnt;
    TextView vatAmnt;

    RecyclerView itemView;
    RecyclerView.LayoutManager layoutManager;
    SalesOrderItemAdapter salesOrderItemAdapter;

    AlertDialog alertDialog;
    AppCompatActivity activity;
    String som_id = "";

    double total_amnt = 0.0;
    double advance_amnt = 0.0;
    double remain_amnt = 0.0;
    double vat_amnt = 0.0;

    ArrayList<SalesOrderItemLists> salesOrderItemLists;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.sales_order_details_view, null);

        activity = (AppCompatActivity) view.getContext();

        salesOrderNo = view.findViewById(R.id.so_no);
        salesOrderDate = view.findViewById(R.id.so_date);
        clientName = view.findViewById(R.id.so_client_name);
        clientCode = view.findViewById(R.id.so_client_code);
        tDAddress = view.findViewById(R.id.target_delivery_address_so);
        eDDate = view.findViewById(R.id.so_expected_delivery_date);
        contactNo = view.findViewById(R.id.so_contact_no);
        clientEmail = view.findViewById(R.id.so_email);
        contactPerson = view.findViewById(R.id.so_contact_person);
        totalOrderAmount = view.findViewById(R.id.item_wise_total_amount);
        advanceAmnt = view.findViewById(R.id.item_wise_advance_paid_amnt);
        remainAmnt = view.findViewById(R.id.item_wise_remaining_amnt);
        vatAmnt = view.findViewById(R.id.item_wise_vat_amnt);

        itemView = view.findViewById(R.id.sales_order_details_report_view);

        salesOrderItemLists = new ArrayList<>();

        itemView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        itemView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(itemView.getContext(),DividerItemDecoration.VERTICAL);
        itemView.addItemDecoration(dividerItemDecoration);

        if (SalesOrderFragment.fromSO == 1) {
            salesOrderNo.setText(SalesOrderFragment.ORDER_NO);
            salesOrderDate.setText(SalesOrderFragment.ORDER_DATE);
            clientName.setText(SalesOrderFragment.CLIENT_NAME);
            clientCode.setText(SalesOrderFragment.CLIENT_CODE);
            tDAddress.setText(SalesOrderFragment.TARGET_ADDRESS);
            eDDate.setText(SalesOrderFragment.ED_DATE);
            contactNo.setText(SalesOrderFragment.CONTACT_NO);
            clientEmail.setText(SalesOrderFragment.CLIENT_EMAIL);
            contactPerson.setText(SalesOrderFragment.CONTACT_PERSON);

            som_id = SalesOrderFragment.SOM_ID;
        } else if (VouchTrans2Adapter.fromVTSO == 1) {
            salesOrderNo.setText(VouchTrans2Adapter.ORDER_NO);
            salesOrderDate.setText(VouchTrans2Adapter.ORDER_DATE);
            clientName.setText(VouchTrans2Adapter.CLIENT_NAME);
            clientCode.setText(VouchTrans2Adapter.CLIENT_CODE);
            tDAddress.setText(VouchTrans2Adapter.TARGET_ADDRESS);
            eDDate.setText(VouchTrans2Adapter.ED_DATE);
            contactNo.setText(VouchTrans2Adapter.CONTACT_NO);
            clientEmail.setText(VouchTrans2Adapter.CLIENT_EMAIL);
            contactPerson.setText(VouchTrans2Adapter.CONTACT_PERSON);

            som_id = VouchTrans2Adapter.SOM_ID;
        } else if (CashTransaction.fromCS == 1) {
            salesOrderNo.setText(CashTransaction.ORDER_NO);
            salesOrderDate.setText(CashTransaction.ORDER_DATE);
            clientName.setText(CashTransaction.CLIENT_NAME);
            clientCode.setText(CashTransaction.CLIENT_CODE);
            tDAddress.setText(CashTransaction.TARGET_ADDRESS);
            eDDate.setText(CashTransaction.ED_DATE);
            contactNo.setText(CashTransaction.CONTACT_NO);
            clientEmail.setText(CashTransaction.CLIENT_EMAIL);
            contactPerson.setText(CashTransaction.CONTACT_PERSON);

            som_id = CashTransaction.SOM_ID;
        }
        System.out.println(som_id);



        new CheckFORLISt().execute();

        builder.setView(view);

        alertDialog = builder.create();

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SalesOrderFragment.fromSO = 0;
                VouchTrans2Adapter.fromVTSO = 0;
                CashTransaction.fromCS = 0;
                alertDialog.dismiss();
            }
        });

        return alertDialog;


    }

    public boolean isConnected() {
        boolean connected = false;
        boolean isMobile = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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

    public class CheckFORLISt extends AsyncTask<Void, Void, Void> {

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

                salesOrderItemAdapter = new SalesOrderItemAdapter(salesOrderItemLists,getContext());
                itemView.setAdapter(salesOrderItemAdapter);
                salesOrderItemAdapter.notifyDataSetChanged();
                DecimalFormat formatter = new DecimalFormat("##,##,##,###.##");
                String formatted = formatter.format(total_amnt);

                totalOrderAmount.setText(formatted);

                if (SalesOrderFragment.fromSO == 1) {
                    if (SalesOrderFragment.ADVANCE_AMOUNT != null) {
                        if (!SalesOrderFragment.ADVANCE_AMOUNT.isEmpty()) {
                            advance_amnt = Double.parseDouble(SalesOrderFragment.ADVANCE_AMOUNT);

                        } else {
                            advance_amnt = 0.0;
                        }

                    } else {
                        advance_amnt = 0.0;

                    }
                    if (SalesOrderFragment.VAT_AMNT != null) {
                        if (!SalesOrderFragment.VAT_AMNT.isEmpty()) {
                            vat_amnt = Double.parseDouble(SalesOrderFragment.VAT_AMNT);

                        } else {
                            vat_amnt = 0.0;
                        }

                    } else {
                        vat_amnt = 0.0;

                    }
                } else if (VouchTrans2Adapter.fromVTSO == 1) {
                    if (VouchTrans2Adapter.ADVANCE_AMOUNT != null) {
                        if (!VouchTrans2Adapter.ADVANCE_AMOUNT.isEmpty()) {
                            advance_amnt = Double.parseDouble(VouchTrans2Adapter.ADVANCE_AMOUNT);

                        } else {
                            advance_amnt = 0.0;
                        }

                    } else {
                        advance_amnt = 0.0;

                    }
                    if (VouchTrans2Adapter.VAT_AMNT != null) {
                        if (!VouchTrans2Adapter.VAT_AMNT.isEmpty()) {
                            vat_amnt = Double.parseDouble(VouchTrans2Adapter.VAT_AMNT);

                        } else {
                            vat_amnt = 0.0;
                        }

                    } else {
                        vat_amnt = 0.0;

                    }
                } else if (CashTransaction.fromCS == 1) {
                    if (CashTransaction.ADVANCE_AMOUNT != null) {
                        if (!CashTransaction.ADVANCE_AMOUNT.isEmpty()) {
                            advance_amnt = Double.parseDouble(CashTransaction.ADVANCE_AMOUNT);

                        } else {
                            advance_amnt = 0.0;
                        }

                    } else {
                        advance_amnt = 0.0;

                    }
                    if (CashTransaction.VAT_AMNT != null) {
                        if (!CashTransaction.VAT_AMNT.isEmpty()) {
                            vat_amnt = Double.parseDouble(CashTransaction.VAT_AMNT);

                        } else {
                            vat_amnt = 0.0;
                        }

                    } else {
                        vat_amnt = 0.0;

                    }
                }

                formatted = formatter.format(advance_amnt);
                advanceAmnt.setText(formatted);

                formatted = formatter.format(vat_amnt);
                vatAmnt.setText(formatted);

                total_amnt = total_amnt + vat_amnt;
                remain_amnt = total_amnt - advance_amnt;
                formatted = formatter.format(remain_amnt);
                remainAmnt.setText(formatted);

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

                        new CheckFORLISt().execute();
                        dialog.dismiss();
                    }
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SalesOrderFragment.fromSO = 0;
                        VouchTrans2Adapter.fromVTSO = 0;
                        CashTransaction.fromCS = 0;
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

            salesOrderItemLists = new ArrayList<>();
            total_amnt = 0.0;
            advance_amnt = 0.0;
            remain_amnt = 0.0;
            vat_amnt = 0.0;

            CallableStatement callableStatement1 = connection.prepareCall("begin ? := GET_SALES_ORDER_DTL_LIST(?); end;");
            callableStatement1.setInt(2,Integer.parseInt(som_id));

            callableStatement1.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement1.execute();

            ResultSet resultSet1 = (ResultSet) callableStatement1.getObject(1);

            while (resultSet1.next()) {

                System.out.println(resultSet1.getString(1));
                salesOrderItemLists.add(new SalesOrderItemLists(resultSet1.getString(1), resultSet1.getString(2), resultSet1.getString(3),
                        resultSet1.getString(4),resultSet1.getString(5),resultSet1.getString(6),
                        resultSet1.getString(7),resultSet1.getString(8),resultSet1.getString(9),
                        resultSet1.getString(10),resultSet1.getString(11),resultSet1.getString(12),
                        resultSet1.getString(13),resultSet1.getString(14),resultSet1.getString(15)));

                if (resultSet1.getString(9) != null) {
                    total_amnt = total_amnt + Double.parseDouble(resultSet1.getString(9));
                }


            }

            callableStatement1.close();

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

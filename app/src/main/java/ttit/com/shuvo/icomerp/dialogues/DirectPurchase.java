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
import java.text.DecimalFormat;
import java.util.ArrayList;

import oracle.jdbc.driver.OracleTypes;
import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.WaitProgress;
import ttit.com.shuvo.icomerp.adapters.DebitCreditVoucherAdapter;
import ttit.com.shuvo.icomerp.adapters.DirectPurchaseAdapter;
import ttit.com.shuvo.icomerp.adapters.SalesOrderItemAdapter;
import ttit.com.shuvo.icomerp.adapters.VouchTrans2Adapter;
import ttit.com.shuvo.icomerp.arrayList.DebitCreditVoucherLists;
import ttit.com.shuvo.icomerp.arrayList.DirectPurchaseLists;
import ttit.com.shuvo.icomerp.fragments.CashTransaction;

import static ttit.com.shuvo.icomerp.OracleConnection.createConnection;

public class DirectPurchase extends AppCompatDialogFragment {

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
    private Connection connection;

    TextView dprNo;
    TextView supplier;
    TextView supplierCode;
    TextView invoiceNo;
    TextView invoiceDate;
    TextView rcvUser;
    TextView rcvDate;

    TextView total;
    TextView vat;
    TextView expense;
    TextView discount;
    TextView grandTotal;
    TextView dueAmnt;
    TextView paidAmnt;

    RecyclerView itemView;
    RecyclerView.LayoutManager layoutManager;
    DirectPurchaseAdapter directPurchaseAdapter;

    AlertDialog alertDialog;
    AppCompatActivity activity;
    String dpr_No = "";
    double total_amnt = 0.0;
    ArrayList<DirectPurchaseLists> directPurchaseLists;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.direct_purchase_receive_view, null);

        activity = (AppCompatActivity) view.getContext();

        dprNo = view.findViewById(R.id.dpr_no);
        supplier = view.findViewById(R.id.supplier_name_dpr);
        supplierCode = view.findViewById(R.id.supplier_code_dpr);
        invoiceNo = view.findViewById(R.id.invoice_no_dpr);
        invoiceDate = view.findViewById(R.id.invoice_date_dpr);
        rcvUser = view.findViewById(R.id.received_by_dpr);
        rcvDate = view.findViewById(R.id.received_date_dpr);

        total = view.findViewById(R.id.total_dpr);
        vat = view.findViewById(R.id.total_vat_dpr);
        expense = view.findViewById(R.id.total_expense_dpr);
        discount = view.findViewById(R.id.total_discount_dpr);
        grandTotal = view.findViewById(R.id.total_grand_amount_dpr);
        dueAmnt = view.findViewById(R.id.total_due_dpr);
        paidAmnt = view.findViewById(R.id.total_paid_dpr);

        itemView = view.findViewById(R.id.dpr_report_view);

        directPurchaseLists = new ArrayList<>();

        itemView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        itemView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(itemView.getContext(),DividerItemDecoration.VERTICAL);
        itemView.addItemDecoration(dividerItemDecoration);

        if (CashTransaction.fromCS == 1) {
            dpr_No = CashTransaction.VM_NO;
        }
        else {
            dpr_No = VouchTrans2Adapter.VM_NO;
        }

        dprNo.setText(dpr_No);

        new CheckFORLISt().execute();

        builder.setView(view);

        alertDialog = builder.create();

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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


            if (conn) {

                conn = false;
                connected = false;

                directPurchaseAdapter = new DirectPurchaseAdapter(directPurchaseLists,getContext());
                itemView.setAdapter(directPurchaseAdapter);
                directPurchaseAdapter.notifyDataSetChanged();

                DecimalFormat formatter = new DecimalFormat("##,##,##,###.##");
                String formatted = formatter.format(total_amnt);

                total.setText(formatted);

//                formatted = formatter.format();
//                totalCredit.setText(formatted);

                vat.setText(directPurchaseLists.get(0).getVat());
                expense.setText(directPurchaseLists.get(0).getExpense());
                discount.setText(directPurchaseLists.get(0).getDiscount());
                grandTotal.setText(directPurchaseLists.get(0).getGrandTotal());
                paidAmnt.setText(directPurchaseLists.get(0).getPaidAmnt());
                dueAmnt.setText(directPurchaseLists.get(0).getDueAmnt());

                supplier.setText(directPurchaseLists.get(0).getSupplier());
                supplierCode.setText(directPurchaseLists.get(0).getSupplierCode());
                rcvDate.setText(directPurchaseLists.get(0).getRcvDate());
                rcvUser.setText(directPurchaseLists.get(0).getRcvUser());
                invoiceNo.setText(directPurchaseLists.get(0).getChallanNo());
                invoiceDate.setText(directPurchaseLists.get(0).getChallanDate());

                waitProgress.dismiss();

            }else {
                waitProgress.dismiss();
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

            directPurchaseLists = new ArrayList<>();
            total_amnt = 0.0;


            CallableStatement callableStatement1 = connection.prepareCall("begin ? := GET_DIRECT_PURCHASE_LIST(?); end;");
            callableStatement1.setString(2,dpr_No);

            callableStatement1.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement1.execute();

            ResultSet resultSet1 = (ResultSet) callableStatement1.getObject(1);

            while (resultSet1.next()) {


                directPurchaseLists.add(new DirectPurchaseLists(resultSet1.getString(1), resultSet1.getString(2), resultSet1.getString(3),
                        resultSet1.getString(4),resultSet1.getString(5),resultSet1.getString(6),
                        resultSet1.getString(7),resultSet1.getString(8),resultSet1.getString(9),
                        resultSet1.getString(10),resultSet1.getString(11),resultSet1.getString(12),
                        resultSet1.getString(13),resultSet1.getString(14),resultSet1.getString(15),
                        resultSet1.getString(16),resultSet1.getString(17),resultSet1.getString(18),
                        resultSet1.getString(19),resultSet1.getString(20),resultSet1.getString(21),
                        resultSet1.getString(22),resultSet1.getString(23),resultSet1.getString(24),
                        resultSet1.getString(25),resultSet1.getString(26),resultSet1.getString(27),
                        resultSet1.getString(28),resultSet1.getString(29)));

                if (resultSet1.getString(23) != null) {
                    total_amnt = total_amnt + Double.parseDouble(resultSet1.getString(23));
                }

            }

            resultSet1.close();

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

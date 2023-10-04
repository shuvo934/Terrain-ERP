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
import ttit.com.shuvo.terrainerp.adapters.DebitCreditVoucherAdapter;
import ttit.com.shuvo.terrainerp.adapters.VouchTrans2Adapter;
import ttit.com.shuvo.terrainerp.arrayList.DebitCreditVoucherLists;
import ttit.com.shuvo.terrainerp.fragments.CashTransaction;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DebitCreditVoucher extends AppCompatDialogFragment {

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
//    private Connection connection;

    TextView debitVoucherNo;
    TextView unitName;
    TextView approverName;
    TextView billRefNo;
    TextView billRefDate;
    TextView vmDate;
    TextView totalDebit;
    TextView totalCredit;
    TextView narration;
    TextView debitCreditText;

    RecyclerView itemView;
    RecyclerView.LayoutManager layoutManager;
    DebitCreditVoucherAdapter debitCreditVoucherAdapter;

    AlertDialog alertDialog;
    AppCompatActivity activity;
    String vm_no = "";

    double total_debit = 0.0;
    double total_credit = 0.0;

    ArrayList<DebitCreditVoucherLists> debitCreditVoucherLists;

    Context mContext;
    public DebitCreditVoucher(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.debit_credit_voucher_view, null);

        activity = (AppCompatActivity) view.getContext();

        debitVoucherNo = view.findViewById(R.id.debit_voucher_no);
        unitName = view.findViewById(R.id.unit_name_dv);
        approverName = view.findViewById(R.id.approved_by_dv);
        billRefNo = view.findViewById(R.id.bill_ref_no_dv);
        billRefDate = view.findViewById(R.id.bill_ref_date_dv);
        vmDate = view.findViewById(R.id.date_dv);
        totalDebit = view.findViewById(R.id.total_debit_dv);
        totalCredit = view.findViewById(R.id.total_credit_dv);
        itemView = view.findViewById(R.id.debit_voucher_details_view);
        narration = view.findViewById(R.id.narration_item_dv);
        debitCreditText = view.findViewById(R.id.debit_or_credit_text);

        debitCreditVoucherLists = new ArrayList<>();

        itemView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        itemView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(itemView.getContext(),DividerItemDecoration.VERTICAL);
        itemView.addItemDecoration(dividerItemDecoration);

        if (CashTransaction.fromCS == 1) {
            vm_no = CashTransaction.VM_NO;
        } else {
            vm_no = VouchTrans2Adapter.VM_NO;
        }
        if (vm_no.contains("DV")) {
            debitCreditText.setText("Debit Voucher");
        }
        else if (vm_no.contains("CV")) {
            debitCreditText.setText("Credit Voucher");
        } else if (vm_no.contains("JV")) {
            debitCreditText.setText("Journal Voucher");
        }
        debitVoucherNo.setText(vm_no);
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
//
//            if (conn) {
//
//                conn = false;
//                connected = false;
//
//                debitCreditVoucherAdapter = new DebitCreditVoucherAdapter(debitCreditVoucherLists,getContext());
//                itemView.setAdapter(debitCreditVoucherAdapter);
//                debitCreditVoucherAdapter.notifyDataSetChanged();
//                DecimalFormat formatter = new DecimalFormat("##,##,##,###.##");
//                String formatted = formatter.format(total_debit);
//
//                totalDebit.setText(formatted);
//
//                formatted = formatter.format(total_credit);
//                totalCredit.setText(formatted);
//
//                unitName.setText(debitCreditVoucherLists.get(0).getUnitName());
//                approverName.setText(debitCreditVoucherLists.get(0).getApprovedUser());
//                billRefNo.setText(debitCreditVoucherLists.get(0).getBillRefNo());
//                billRefDate.setText(debitCreditVoucherLists.get(0).getBillRefDate());
//                vmDate.setText(debitCreditVoucherLists.get(0).getVmDate());
//
//                String totalNarration = "";
//                if (debitCreditVoucherLists.get(0).getNarration() != null) {
//                    totalNarration =  debitCreditVoucherLists.get(0).getNarration()+"\n"+ debitCreditVoucherLists.get(0).getBillInfo();
//                } else {
//                    totalNarration = debitCreditVoucherLists.get(0).getBillInfo();
//                }
//
//                narration.setText(totalNarration);
//
//                waitProgress.dismiss();
//
//            }
//            else {
//                waitProgress.dismiss();
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
//                        CashTransaction.fromCS = 0;
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
//            debitCreditVoucherLists = new ArrayList<>();
//            total_debit = 0.0;
//            total_credit = 0.0;
//
//            System.out.println("pai naki");
//            System.out.println(vm_no);
//            CallableStatement callableStatement1 = connection.prepareCall("begin ? := GET_VOUCHER_DETAILS(?); end;");
//            callableStatement1.setString(2,vm_no);
//
//            callableStatement1.registerOutParameter(1, OracleTypes.CURSOR);
//            callableStatement1.execute();
//
//            ResultSet resultSet1 = (ResultSet) callableStatement1.getObject(1);
//
//            while (resultSet1.next()) {
//
//                System.out.println(resultSet1.getString(1));
//                System.out.println("pai naki");
//                debitCreditVoucherLists.add(new DebitCreditVoucherLists(resultSet1.getString(1), resultSet1.getString(2), resultSet1.getString(3),
//                        resultSet1.getString(4),resultSet1.getString(5),resultSet1.getString(6),
//                        resultSet1.getString(7),resultSet1.getString(8),resultSet1.getString(9),
//                        resultSet1.getString(10),resultSet1.getString(11),resultSet1.getString(12),
//                        resultSet1.getString(13),resultSet1.getString(14),resultSet1.getString(22)));
//
//                if (resultSet1.getString(13) != null) {
//                    total_debit = total_debit + Double.parseDouble(resultSet1.getString(13));
//                }
//                if (resultSet1.getString(14) != null) {
//                    total_credit = total_credit + Double.parseDouble(resultSet1.getString(14));
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
        debitCreditVoucherLists = new ArrayList<>();
        total_debit = 0.0;
        total_credit = 0.0;
        String url = "http://103.56.208.123:8001/apex/tterp/dialogs/getVoucherDetails?vm_no="+vm_no+"";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

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
                        String voucher_details = info.getString("voucher_details");
                        JSONArray voucherArray = new JSONArray(voucher_details);
                        for (int j = 0; j < voucherArray.length(); j++) {
                            JSONObject voucher_info = voucherArray.getJSONObject(j);

                            String cid_name = voucher_info.getString("cid_name");
//                            String vm_no_new = voucher_info.getString("vm_no")
//                                    .equals("null") ? "" : voucher_info.getString("vm_no");
                            String apv_user = voucher_info.getString("apv_user")
                                    .equals("null") ? "" : voucher_info.getString("apv_user");
                            String vm_bill_ref_no = voucher_info.getString("vm_bill_ref_no")
                                    .equals("null") ? "" : voucher_info.getString("vm_bill_ref_no");
                            String bill_ref_date = voucher_info.getString("bill_ref_date")
                                    .equals("null") ? "" : voucher_info.getString("bill_ref_date");
                            String vm_date = voucher_info.getString("vm_date")
                                    .equals("null") ? "" : voucher_info.getString("vm_date");
                            String vm_type = voucher_info.getString("vm_type")
                                    .equals("null") ? "" : voucher_info.getString("vm_type");
                            String vm_naration = voucher_info.getString("vm_naration")
                                    .equals("null") ? "" : voucher_info.getString("vm_naration");
                            String ad_code = voucher_info.getString("ad_code")
                                    .equals("null") ? "" : voucher_info.getString("ad_code");
                            String ad_name = voucher_info.getString("ad_name")
                                    .equals("null") ? "" : voucher_info.getString("ad_name");
                            String vd_cheque_date = voucher_info.getString("vd_cheque_date")
                                    .equals("null") ? "" : voucher_info.getString("vd_cheque_date");
                            String payee_name = voucher_info.getString("payee_name")
                                    .equals("null") ? "" : voucher_info.getString("payee_name");
                            String vd_dr_amt = voucher_info.getString("vd_dr_amt")
                                    .equals("null") ? "0" : voucher_info.getString("vd_dr_amt");
                            String vd_cr_amt = voucher_info.getString("vd_cr_amt")
                                    .equals("null") ? "0" : voucher_info.getString("vd_cr_amt");
//                            String vm_post_flag = voucher_info.getString("vm_post_flag")
//                                    .equals("null") ? "" : voucher_info.getString("vm_post_flag");
//                            String vm_id = voucher_info.getString("vm_id")
//                                    .equals("null") ? "" : voucher_info.getString("vm_id");
//                            String vm_voucher_approved_by = voucher_info.getString("vm_voucher_approved_by")
//                                    .equals("null") ? "" : voucher_info.getString("vm_voucher_approved_by");
//                            String vm_voucher_approved_flag = voucher_info.getString("vm_voucher_approved_flag")
//                                    .equals("null") ? "" : voucher_info.getString("vm_voucher_approved_flag");
//                            String vd_party_emp_id = voucher_info.getString("vd_party_emp_id")
//                                    .equals("null") ? "" : voucher_info.getString("vd_party_emp_id");
//                            String vd_party_emp_flag = voucher_info.getString("vd_party_emp_flag")
//                                    .equals("null") ? "" : voucher_info.getString("vd_party_emp_flag");
//                            String vd_party_emp_job_id = voucher_info.getString("vd_party_emp_job_id")
//                                    .equals("null") ? "" : voucher_info.getString("vd_party_emp_job_id");
                            String job_no_nar = voucher_info.getString("job_no_nar")
                                    .equals("null") ? "" : voucher_info.getString("job_no_nar");

                            debitCreditVoucherLists.add(new DebitCreditVoucherLists(cid_name, vm_no, apv_user,
                                    vm_bill_ref_no,bill_ref_date,vm_date,
                                    vm_type,vm_naration,ad_code,
                                    ad_name,vd_cheque_date,payee_name,
                                    vd_dr_amt,vd_cr_amt,job_no_nar));

                            total_debit = total_debit + Double.parseDouble(vd_dr_amt);

                            total_credit = total_credit + Double.parseDouble(vd_cr_amt);
                        }
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
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                debitCreditVoucherAdapter = new DebitCreditVoucherAdapter(debitCreditVoucherLists,getContext());
                itemView.setAdapter(debitCreditVoucherAdapter);
                debitCreditVoucherAdapter.notifyDataSetChanged();
                DecimalFormat formatter = new DecimalFormat("##,##,##,###.##");
                String formatted = formatter.format(total_debit);

                totalDebit.setText(formatted);

                formatted = formatter.format(total_credit);
                totalCredit.setText(formatted);

                unitName.setText(debitCreditVoucherLists.get(0).getUnitName());
                approverName.setText(debitCreditVoucherLists.get(0).getApprovedUser());
                billRefNo.setText(debitCreditVoucherLists.get(0).getBillRefNo());
                billRefDate.setText(debitCreditVoucherLists.get(0).getBillRefDate());
                vmDate.setText(debitCreditVoucherLists.get(0).getVmDate());

                String totalNarration = "";
                if (debitCreditVoucherLists.get(0).getNarration() != null) {
                    totalNarration =  debitCreditVoucherLists.get(0).getNarration()+"\n"+ debitCreditVoucherLists.get(0).getBillInfo();
                } else {
                    totalNarration = debitCreditVoucherLists.get(0).getBillInfo();
                }

                narration.setText(totalNarration);

                waitProgress.dismiss();
            }
            else {
                waitProgress.dismiss();
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
                    CashTransaction.fromCS = 0;
                    dialog.dismiss();
                    alertDialog.dismiss();

                });
            }
        }
        else {
            waitProgress.dismiss();
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
                CashTransaction.fromCS = 0;
                dialog.dismiss();
                alertDialog.dismiss();

            });
        }
    }
}

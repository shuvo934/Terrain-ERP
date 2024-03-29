package ttit.com.shuvo.terrainerp.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.WaitProgress;
import ttit.com.shuvo.terrainerp.adapters.DebitVoucherApprovedAdapter;
import ttit.com.shuvo.terrainerp.arrayList.DebitVABillDetailsLists;
import ttit.com.shuvo.terrainerp.arrayList.DebitVABillLists;
import ttit.com.shuvo.terrainerp.arrayList.DebitVoucherApprovedList;
import ttit.com.shuvo.terrainerp.dialogues.VoucherSelectionDial;
import static ttit.com.shuvo.terrainerp.login.Login.userInfoLists;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DebitVoucherApproved#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DebitVoucherApproved extends Fragment implements DebitVoucherApprovedAdapter.ClickedItem{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
//    Boolean isfiltered = false;
//    private Connection connection;

    String emp_code = "";

    public static TextInputEditText debitVoucherSelectSpinner;

    public static String dVm_id = "";

    public static int fromDVA = 0;

    public static String status_approved_DV = "";

    public static String appDisUser_DV = "";
    public static String appDisTime_DV = "";

    LinearLayout afterVoucherSelect;

    TextInputEditText vDate;
    TextInputEditText vTime;
    TextInputEditText preparedBy;
    TextInputEditText billRefNo;
    TextInputEditText billRefDate;
    TextInputEditText remarks;

    TextInputEditText statusReview;
    TextInputEditText appOrDisAppUser;
    TextInputEditText appOrDisAppTime;
    TextInputLayout appOrDisAppUserLay;
    TextInputLayout appOrDisAppTimeLay;

    TextView totalDebit;
    TextView totalCredit;

    Button approve;
    Button reject;

    double total_debit = 0.0;
    double total_credit = 0.0;
    double total_inv_amnt = 0.0;
    String approve_flag = "";

    RecyclerView itemView;
    DebitVoucherApprovedAdapter debitVoucherApprovedAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<DebitVoucherApprovedList> debitVoucherApprovedLists;

    public DebitVoucherApproved() {
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
     * @return A new instance of fragment DebitVoucherApproved.
     */
    // TODO: Rename and change types and number of parameters
    public static DebitVoucherApproved newInstance(String param1, String param2) {
        DebitVoucherApproved fragment = new DebitVoucherApproved();
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
        View view = inflater.inflate(R.layout.fragment_debit_voucher_approved, container, false);

        debitVoucherSelectSpinner = view.findViewById(R.id.debit_voucher_search_text);
        afterVoucherSelect = view.findViewById(R.id.debit_voucher_approved_details_card);
        afterVoucherSelect.setVisibility(View.GONE);

        vDate = view.findViewById(R.id.date_debit_approved);
        vTime = view.findViewById(R.id.time_debit_approved);
        preparedBy = view.findViewById(R.id.prepared_by_debit_approved);
        billRefNo = view.findViewById(R.id.bill_ref_no_debit_approved);
        billRefDate = view.findViewById(R.id.bill_ref_date_debit_approved);
        remarks = view.findViewById(R.id.remarks_debit_approved);
        statusReview = view.findViewById(R.id.status_debit_approved);
        appOrDisAppUser = view.findViewById(R.id.app_or_disapp_debit_approved);
        appOrDisAppUserLay = view.findViewById(R.id.app_or_disapp_debit_approved_layout);
        appOrDisAppTime = view.findViewById(R.id.app_or_disapp_time_debit_approved);
        appOrDisAppTimeLay = view.findViewById(R.id.app_or_disapp_time_debit_approved_layout);

        totalDebit = view.findViewById(R.id.total_debit_dva);
        totalCredit = view.findViewById(R.id.total_credit_dva);

        approve = view.findViewById(R.id.approve_button_dva);
        reject = view.findViewById(R.id.reject_button_dva);

        itemView = view.findViewById(R.id.debit_voucher_approved_transaction_view);

        itemView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        itemView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(itemView.getContext(),DividerItemDecoration.VERTICAL);
        itemView.addItemDecoration(dividerItemDecoration);

        emp_code = userInfoLists.get(0).getUserName();

        debitVoucherApprovedLists = new ArrayList<>();

        debitVoucherSelectSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDVA = 1;
                VoucherSelectionDial voucherSelectionDial = new VoucherSelectionDial(mContext);
                voucherSelectionDial.show(getActivity().getSupportFragmentManager(),"DVS");
            }
        });

        debitVoucherSelectSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!dVm_id.isEmpty()) {
                    afterVoucherSelect.setVisibility(View.GONE);
//                    new Check().execute();
                    getAccData();
                }

            }
        });

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (total_debit != total_credit) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Warning!")
                            .setMessage("Total Debit Amount is not equal to Total Credit Amount")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else if (total_inv_amnt > total_debit){
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Warning!")
                            .setMessage("Total Invoice Amount is greater than Total Debit Amount")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Approve!")
                            .setMessage("Do you want to approve this voucher?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    approve_flag = "1";
//                                    new ApproveCheck().execute();
                                    approverProcess();
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

            }
        });


        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Disapprove!")
                        .setMessage("Do you want to disapprove this voucher?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                approve_flag = "0";
//                                new DisApproveCheck().execute();
                                approverProcess();
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
        });

        return view;
    }

//    public boolean isConnected() {
//        boolean connected = false;
//        boolean isMobile = false;
//        try {
//            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
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

    }

//    public class Check extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
//            waitProgress.setCancelable(false);
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            if (isConnected() && isOnline()) {
//
//                AccData();
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
//                afterVoucherSelect.setVisibility(View.VISIBLE);
//
//                if (status_approved_DV.equals("Approved")) {
//                    System.out.println(status_approved_DV);
//                    approve.setVisibility(View.GONE);
//                    reject.setVisibility(View.VISIBLE);
//                    if (appDisUser_DV != null) {
//                        if (!appDisUser_DV.isEmpty()) {
//                            appOrDisAppUserLay.setHint("Approved By:");
//                            appOrDisAppUser.setText(appDisUser_DV);
//                            appOrDisAppTimeLay.setHint("Approved Time:");
//                            appOrDisAppTime.setText(appDisTime_DV);
//                        } else {
//                            appOrDisAppUserLay.setHint("Approved By:");
//                            appOrDisAppUser.setText("No Name found");
//                            appOrDisAppTimeLay.setHint("Approved Time:");
//                            appOrDisAppTime.setText(appDisTime_DV);
//                        }
//                    }
//                    else {
//                        appOrDisAppUserLay.setHint("Approved By:");
//                        appOrDisAppUser.setText("No Name found");
//                        appOrDisAppTimeLay.setHint("Approved Time:");
//                        appOrDisAppTime.setText(appDisTime_DV);
//                    }
//                } else if (status_approved_DV.equals("Pending")) {
//                    System.out.println(status_approved_DV);
//                    approve.setVisibility(View.VISIBLE);
//                    reject.setVisibility(View.GONE);
//
//                    if (appDisUser_DV != null) {
//                        if (!appDisUser_DV.isEmpty()) {
//                            appOrDisAppUserLay.setHint("Disapproved By:");
//                            appOrDisAppUser.setText(appDisUser_DV);
//                            appOrDisAppTimeLay.setHint("Disapproved Time:");
//                            appOrDisAppTime.setText(appDisTime_DV);
//                        } else {
//                            appOrDisAppUserLay.setHint("Approved By / Disapproved By:");
//                            appOrDisAppUser.setText(appDisUser_DV);
//                            appOrDisAppTimeLay.setHint("Approved / Disapproved Time:");
//                            appOrDisAppTime.setText(appDisTime_DV);
//                        }
//                    } else {
//                        appOrDisAppUserLay.setHint("Approved By / Disapproved By:");
//                        appOrDisAppUser.setText(appDisUser_DV);
//                        appOrDisAppTimeLay.setHint("Approved / Disapproved Time:");
//                        appOrDisAppTime.setText(appDisTime_DV);
//                    }
//                }
//
//                statusReview.setText(status_approved_DV);
//
//                debitVoucherApprovedAdapter = new DebitVoucherApprovedAdapter(debitVoucherApprovedLists,getContext(),DebitVoucherApproved.this);
//                itemView.setAdapter(debitVoucherApprovedAdapter);
//
//                if (debitVoucherApprovedLists.size() != 0) {
//                    vDate.setText(debitVoucherApprovedLists.get(0).getVmDate());
//                    vTime.setText(debitVoucherApprovedLists.get(0).getVmTime());
//                    preparedBy.setText(debitVoucherApprovedLists.get(0).getVmUser());
//                    billRefNo.setText(debitVoucherApprovedLists.get(0).getVmBillRefNo());
//                    billRefDate.setText(debitVoucherApprovedLists.get(0).getVmBillRefDate());
//                    remarks.setText(debitVoucherApprovedLists.get(0).getVmNarration());
//                } else {
//                    vDate.setText("");
//                    vTime.setText("");
//                    preparedBy.setText("");
//                    billRefNo.setText("");
//                    billRefDate.setText("");
//                    remarks.setText("");
//                }
//
//                DecimalFormat formatter = new DecimalFormat("##,##,##,###.##");
//                String formatted = formatter.format(total_debit);
//                totalDebit.setText(formatted);
//
//                formatted = formatter.format(total_credit);
//                totalCredit.setText(formatted);
//
//
//                System.out.println(total_debit);
//                System.out.println(total_credit);
//                System.out.println(total_inv_amnt);
//
//                waitProgress.dismiss();
//
//            }
//            else {
//                waitProgress.dismiss();
//                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(getContext())
//                        .setMessage("Please Check Your Internet Connection")
//                        .setPositiveButton("Retry", null)
//                        .setNegativeButton("Cancel", null)
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
//                        dialog.dismiss();
//                    }
//                });
//            }
//        }
//    }

//    public class ApproveCheck extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
//            waitProgress.setCancelable(false);
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            if (isConnected() && isOnline()) {
//
//                AppeoveVoucher();
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
//            waitProgress.dismiss();
//            if (conn) {
//
//                conn = false;
//                connected = false;
//
//                Toast.makeText(getContext(),"APPROVED",Toast.LENGTH_SHORT).show();
//                afterVoucherSelect.setVisibility(View.GONE);
//                dVm_id = "";
//                debitVoucherSelectSpinner.setText("");
//
//
//
//
//            }
//            else {
//                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(getContext())
//                        .setMessage("Please Check Your Internet Connection")
//                        .setPositiveButton("Retry", null)
//                        .setNegativeButton("Cancel", null)
//                        .show();
//
//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
//                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                positive.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        new ApproveCheck().execute();
//                        dialog.dismiss();
//                    }
//                });
//
//                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                negative.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//            }
//        }
//    }
//
//    public class DisApproveCheck extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
//            waitProgress.setCancelable(false);
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            if (isConnected() && isOnline()) {
//
//                DisApproveVoucher();
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
//            waitProgress.dismiss();
//            if (conn) {
//
//                conn = false;
//                connected = false;
//
//                Toast.makeText(getContext(),"DISAPPROVED",Toast.LENGTH_SHORT).show();
//                afterVoucherSelect.setVisibility(View.GONE);
//                dVm_id = "";
//                debitVoucherSelectSpinner.setText("");
//
//
//
//
//            }else {
//                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(getContext())
//                        .setMessage("Please Check Your Internet Connection")
//                        .setPositiveButton("Retry", null)
//                        .setNegativeButton("Cancel", null)
//                        .show();
//
//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
//                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                positive.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        new DisApproveCheck().execute();
//                        dialog.dismiss();
//                    }
//                });
//
//                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                negative.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//            }
//        }
//    }

//    public void AccData() {
//        try {
//            this.connection = createConnection();
//
//
//
//            debitVoucherApprovedLists = new ArrayList<>();
//
//            total_debit = 0.0;
//            total_credit = 0.0;
//            total_inv_amnt = 0.0;
//
//            CallableStatement callableStatement1 = connection.prepareCall("begin ? := GET_VOUCHER_LIST(?); end;");
//            callableStatement1.setInt(2,Integer.parseInt(dVm_id));
//            callableStatement1.registerOutParameter(1, OracleTypes.CURSOR);
//            callableStatement1.execute();
//
//            ResultSet resultSet = (ResultSet) callableStatement1.getObject(1);
//
//            while (resultSet.next()) {
//                String vd_id = resultSet.getString(17);
////                System.out.println("VD_ID: "+vd_id);
//                CallableStatement callableStatement2 = connection.prepareCall("begin ? := GET_VOUCHER_BILL_LIST(?,?,?); end;");
//                callableStatement2.setString(2,vd_id);
//                callableStatement2.setString(3,null);
//                callableStatement2.setInt(4,1);
//                callableStatement2.registerOutParameter(1, OracleTypes.CURSOR);
//                callableStatement2.execute();
//
//                ResultSet resultSet1 = (ResultSet) callableStatement2.getObject(1);
//
//                ArrayList<DebitVABillLists> debitVABillLists = new ArrayList<>();
//
//                while (resultSet1.next()) {
//
//                    String avdp_id = resultSet1.getString(1);
//                    System.out.println("AVDP_ID: "+avdp_id);
//                    CallableStatement callableStatement3 = connection.prepareCall("begin ? := GET_VOUCHER_BILL_LIST(?,?,?); end;");
//                    callableStatement3.setString(2,null);
//                    callableStatement3.setString(3,avdp_id);
//                    callableStatement3.setInt(4,2);
//                    callableStatement3.registerOutParameter(1, OracleTypes.CURSOR);
//                    callableStatement3.execute();
//
//                    ResultSet resultSet2 = (ResultSet) callableStatement3.getObject(1);
//
//                    ArrayList<DebitVABillDetailsLists> debitVABillDetailsLists = new ArrayList<>();
//
//                    while (resultSet2.next()) {
//
//                        if (resultSet2.getString(3) != null) {
//                            total_inv_amnt  = total_inv_amnt + Double.parseDouble(resultSet2.getString(3));
//                        }
//
//                        debitVABillDetailsLists.add(new DebitVABillDetailsLists(resultSet2.getString(1),resultSet2.getString(2),resultSet2.getString(3),
//                                resultSet2.getString(4),resultSet2.getString(5),resultSet2.getString(6),resultSet2.getString(7)));
//
//                    }
//                    resultSet2.close();
//                    callableStatement3.close();
//
//                    debitVABillLists.add(new DebitVABillLists(resultSet1.getString(1),resultSet1.getString(2),resultSet1.getString(3),
//                            resultSet1.getString(4),resultSet1.getString(5),resultSet1.getString(6),debitVABillDetailsLists));
//                }
//                resultSet1.close();
//                callableStatement2.close();
//
//
//
//                debitVoucherApprovedLists.add(new DebitVoucherApprovedList(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),
//                        resultSet.getString(4),resultSet.getString(5),resultSet.getString(6),
//                        resultSet.getString(7),resultSet.getString(8),resultSet.getString(9),
//                        resultSet.getString(10),resultSet.getString(11),resultSet.getString(12),
//                        resultSet.getString(13),resultSet.getString(14),resultSet.getString(15),
//                        resultSet.getString(16),resultSet.getString(17),resultSet.getString(18),
//                        resultSet.getString(19),resultSet.getString(20),resultSet.getString(21),
//                        resultSet.getString(22),resultSet.getString(23),resultSet.getString(24),
//                        resultSet.getString(25),resultSet.getString(26),debitVABillLists));
//            }
//
//            resultSet.close();
//            callableStatement1.close();
//
//            if (debitVoucherApprovedLists.size() != 0) {
//                for (int i = 0; i < debitVoucherApprovedLists.size() ; i++) {
//                    if (debitVoucherApprovedLists.get(i).getVdDebit() != null) {
//                        total_debit = total_debit + Double.parseDouble(debitVoucherApprovedLists.get(i).getVdDebit());
//                    }
//                    if (debitVoucherApprovedLists.get(i).getVdCredit() != null) {
//                        total_credit = total_credit + Double.parseDouble(debitVoucherApprovedLists.get(i).getVdCredit());
//                    }
//                }
//            }
//
//            connected = true;
//
//            connection.close();
//
//        }
//        catch (Exception e) {
//
//            Log.i("ERRRRR", e.getLocalizedMessage());
//            e.printStackTrace();
//        }
//    }

    public void getAccData() {
        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;
        debitVoucherApprovedLists = new ArrayList<>();

        total_debit = 0.0;
        total_credit = 0.0;
        total_inv_amnt = 0.0;

        String url = "http://103.56.208.123:8001/apex/tterp/voucherApprove/getVoucherList/"+dVm_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        String voucher_list = info.getString("voucher_list");
                        JSONArray voucherArray = new JSONArray(voucher_list);
                        for (int j = 0; j < voucherArray.length(); j++) {
                            JSONObject voucher_info = voucherArray.getJSONObject(j);

                            String vm_id = voucher_info.getString("vm_id")
                                    .equals("null") ? "" : voucher_info.getString("vm_id");
                            String vm_no = voucher_info.getString("vm_no")
                                    .equals("null") ? "" : voucher_info.getString("vm_no");
                            String vm_date = voucher_info.getString("vm_date")
                                    .equals("null") ? "" : voucher_info.getString("vm_date");
                            String vm_time = voucher_info.getString("vm_time")
                                    .equals("null") ? "" : voucher_info.getString("vm_time");
                            String vm_naration = voucher_info.getString("vm_naration")
                                    .equals("null") ? "" : voucher_info.getString("vm_naration");
                            String vm_user = voucher_info.getString("vm_user")
                                    .equals("null") ? "" : voucher_info.getString("vm_user");
                            String vm_bill_ref_no = voucher_info.getString("vm_bill_ref_no")
                                    .equals("null") ? "" : voucher_info.getString("vm_bill_ref_no");
                            String vm_bill_ref_date = voucher_info.getString("vm_bill_ref_date")
                                    .equals("null") ? "" : voucher_info.getString("vm_bill_ref_date");
                            String vm_no_id = voucher_info.getString("vm_no_id")
                                    .equals("null") ? "" : voucher_info.getString("vm_no_id");
                            String vm_cid_id = voucher_info.getString("vm_cid_id")
                                    .equals("null") ? "" : voucher_info.getString("vm_cid_id");
                            String vm_proj_id = voucher_info.getString("vm_proj_id")
                                    .equals("null") ? "" : voucher_info.getString("vm_proj_id");
                            String vm_voucher_approved_flag = voucher_info.getString("vm_voucher_approved_flag")
                                    .equals("null") ? "" : voucher_info.getString("vm_voucher_approved_flag");
                            String vm_voucher_approved_by = voucher_info.getString("vm_voucher_approved_by")
                                    .equals("null") ? "" : voucher_info.getString("vm_voucher_approved_by");
                            String vm_voucher_approved_time = voucher_info.getString("vm_voucher_approved_time")
                                    .equals("null") ? "" : voucher_info.getString("vm_voucher_approved_time");
                            String vm_der_id = voucher_info.getString("vm_der_id")
                                    .equals("null") ? "" : voucher_info.getString("vm_der_id");
                            String vm_party_emp_flag = voucher_info.getString("vm_party_emp_flag")
                                    .equals("null") ? "" : voucher_info.getString("vm_party_emp_flag");
                            String vd_id = voucher_info.getString("vd_id")
                                    .equals("null") ? "" : voucher_info.getString("vd_id");
                            String vd_ad_id = voucher_info.getString("vd_ad_id")
                                    .equals("null") ? "" : voucher_info.getString("vd_ad_id");
                            String ad_code = voucher_info.getString("ad_code")
                                    .equals("null") ? "" : voucher_info.getString("ad_code");
                            String ad_name = voucher_info.getString("ad_name")
                                    .equals("null") ? "" : voucher_info.getString("ad_name");
                            String party_code = voucher_info.getString("party_code")
                                    .equals("null") ? "" : voucher_info.getString("party_code");
                            String party_name = voucher_info.getString("party_name")
                                    .equals("null") ? "" : voucher_info.getString("party_name");
                            String vd_cheque_no = voucher_info.getString("vd_cheque_no")
                                    .equals("null") ? "" : voucher_info.getString("vd_cheque_no");
                            String vd_cheque_date = voucher_info.getString("vd_cheque_date")
                                    .equals("null") ? "" : voucher_info.getString("vd_cheque_date");
                            String vd_dr_amt = voucher_info.getString("vd_dr_amt")
                                    .equals("null") ? "" : voucher_info.getString("vd_dr_amt");
                            String vd_cr_amt = voucher_info.getString("vd_cr_amt")
                                    .equals("null") ? "" : voucher_info.getString("vd_cr_amt");

                            debitVoucherApprovedLists.add(new DebitVoucherApprovedList(vm_id,vm_no,vm_date,
                                    vm_time,vm_naration,vm_user,
                                    vm_bill_ref_no,vm_bill_ref_date,vm_no_id,
                                    vm_cid_id,vm_proj_id,vm_voucher_approved_flag,
                                    vm_voucher_approved_by,vm_voucher_approved_time,vm_der_id,
                                    vm_party_emp_flag,vd_id,vd_ad_id,
                                    ad_code,ad_name,party_code,
                                    party_name,vd_cheque_no,vd_cheque_date,
                                    vd_dr_amt,vd_cr_amt,false,new ArrayList<>()));
                        }
                    }
                }
                if (debitVoucherApprovedLists.size() != 0) {
                    for (int i = 0; i < debitVoucherApprovedLists.size() ; i++) {
                        if (debitVoucherApprovedLists.get(i).getVdDebit() != null) {
                            if (!debitVoucherApprovedLists.get(i).getVdDebit().isEmpty()) {
                                total_debit = total_debit + Double.parseDouble(debitVoucherApprovedLists.get(i).getVdDebit());
                            }
                        }
                        if (debitVoucherApprovedLists.get(i).getVdCredit() != null) {
                            if (!debitVoucherApprovedLists.get(i).getVdCredit().isEmpty()) {
                                total_credit = total_credit + Double.parseDouble(debitVoucherApprovedLists.get(i).getVdCredit());
                            }
                        }
                    }
                }
                checkToGetBillListFirst();
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

        requestQueue.add(request);

    }

    public void checkToGetBillListFirst() {
        if (debitVoucherApprovedLists.size() != 0) {
            boolean allUpdated = false;
            for (int i = 0; i < debitVoucherApprovedLists.size(); i++) {
                allUpdated = debitVoucherApprovedLists.get(i).isUpdated();
                if (!debitVoucherApprovedLists.get(i).isUpdated()) {
                    allUpdated = debitVoucherApprovedLists.get(i).isUpdated();
                    String vd_id = debitVoucherApprovedLists.get(i).getVdid();
                    getBillListsFirst(vd_id, i);
                    break;
                }
            }
            if (allUpdated) {
                connected = true;
                updateFragment();
            }
        }
        else {
            connected = true;
            updateFragment();
        }
    }

    public void getBillListsFirst(String vd_id, int firstIndex) {
        String url = "http://103.56.208.123:8001/apex/tterp/voucherApprove/getVoucherBillList?vd_id="+vd_id+"&avdp_id=&choice_flag=1";
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        ArrayList<DebitVABillLists> debitVABillLists = new ArrayList<>();

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        String voucher_bill_list = info.getString("voucher_bill_list");
                        JSONArray voucherArray = new JSONArray(voucher_bill_list);
                        for (int j = 0; j < voucherArray.length(); j++) {
                            JSONObject bill_info = voucherArray.getJSONObject(j);

                            String avdp_id = bill_info.getString("avdp_id");
                            String avdp_vd_id = bill_info.getString("avdp_vd_id");
                            String avdp_remarks= bill_info.getString("avdp_remarks");
                            String avdp_brm_id = bill_info.getString("avdp_brm_id");
                            String brm_no = bill_info.getString("brm_no");
                            String brm_type_flag = bill_info.getString("brm_type_flag");

                            debitVABillLists.add(new DebitVABillLists(avdp_id,avdp_vd_id,avdp_remarks,
                                    avdp_brm_id,brm_no,brm_type_flag,false,new ArrayList<>()));
                        }
                    }
                }
                checkToGetBillListSecond(debitVABillLists, firstIndex);
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

        requestQueue.add(request);
    }

    public void checkToGetBillListSecond(ArrayList<DebitVABillLists> debitVABillLists, int firstIndex) {
        if (debitVABillLists.size() != 0) {
            boolean allUpdated = false;
            for (int i = 0; i < debitVABillLists.size(); i++) {
                allUpdated = debitVABillLists.get(i).isUpdated();
                if (!debitVABillLists.get(i).isUpdated()) {
                    allUpdated = debitVABillLists.get(i).isUpdated();
                    String avdp_id = debitVABillLists.get(i).getAvdpId();
                    getBillListsSecond(debitVABillLists, avdp_id, i,firstIndex);
                    break;
                }
            }
            if (allUpdated) {
                debitVoucherApprovedLists.get(firstIndex).setDebitVABillLists(debitVABillLists);
                debitVoucherApprovedLists.get(firstIndex).setUpdated(true);
                checkToGetBillListFirst();
            }
        }
        else {
            debitVoucherApprovedLists.get(firstIndex).setDebitVABillLists(debitVABillLists);
            debitVoucherApprovedLists.get(firstIndex).setUpdated(true);
            checkToGetBillListFirst();
        }
    }

    public void getBillListsSecond(ArrayList<DebitVABillLists> debitVABillLists, String avdp_id, int secondIndex,int firstIndex) {
        String url = "http://103.56.208.123:8001/apex/tterp/voucherApprove/getVoucherBillList?vd_id=&avdp_id="+avdp_id+"&choice_flag=2";
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        ArrayList<DebitVABillDetailsLists> debitVABillDetailsLists = new ArrayList<>();

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        String voucher_bill_list = info.getString("voucher_bill_list");
                        JSONArray voucherArray = new JSONArray(voucher_bill_list);
                        for (int j = 0; j < voucherArray.length(); j++) {
                            JSONObject bill_info = voucherArray.getJSONObject(j);

                            String vj_id = bill_info.getString("vj_id");
                            String vj_avdp_id = bill_info.getString("vj_avdp_id");
                            String vj_amt= bill_info.getString("vj_amt")
                                    .equals("null") ? "" : bill_info.getString("vj_amt");
                            String vj_brb_id = bill_info.getString("vj_brb_id");
                            String vj_vat_paid_amt = bill_info.getString("vj_vat_paid_amt");
                            String wom_no = bill_info.getString("wom_no");
                            String invoice_no = bill_info.getString("invoice_no");

                            if (!vj_amt.isEmpty()) {
                                total_inv_amnt  = total_inv_amnt + Double.parseDouble(vj_amt);
                            }

                            debitVABillDetailsLists.add(new DebitVABillDetailsLists(vj_id,vj_avdp_id,vj_amt,
                                    vj_brb_id,vj_vat_paid_amt,wom_no,invoice_no));

                        }
                    }
                }
                debitVABillLists.get(secondIndex).setDebitVABillDetailsLists(debitVABillDetailsLists);
                debitVABillLists.get(secondIndex).setUpdated(true);
                checkToGetBillListSecond(debitVABillLists, firstIndex);
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

        requestQueue.add(request);
    }

    private void updateFragment() {
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                afterVoucherSelect.setVisibility(View.VISIBLE);

                if (status_approved_DV.equals("Approved")) {
                    System.out.println(status_approved_DV);
                    approve.setVisibility(View.GONE);
                    reject.setVisibility(View.VISIBLE);
                    if (appDisUser_DV != null) {
                        if (!appDisUser_DV.isEmpty()) {
                            appOrDisAppUserLay.setHint("Approved By:");
                            appOrDisAppUser.setText(appDisUser_DV);
                            appOrDisAppTimeLay.setHint("Approved Time:");
                            appOrDisAppTime.setText(appDisTime_DV);
                        } else {
                            appOrDisAppUserLay.setHint("Approved By:");
                            appOrDisAppUser.setText("No Name found");
                            appOrDisAppTimeLay.setHint("Approved Time:");
                            appOrDisAppTime.setText(appDisTime_DV);
                        }
                    }
                    else {
                        appOrDisAppUserLay.setHint("Approved By:");
                        appOrDisAppUser.setText("No Name found");
                        appOrDisAppTimeLay.setHint("Approved Time:");
                        appOrDisAppTime.setText(appDisTime_DV);
                    }
                } else if (status_approved_DV.equals("Pending")) {
                    System.out.println(status_approved_DV);
                    approve.setVisibility(View.VISIBLE);
                    reject.setVisibility(View.GONE);

                    if (appDisUser_DV != null) {
                        if (!appDisUser_DV.isEmpty()) {
                            appOrDisAppUserLay.setHint("Disapproved By:");
                            appOrDisAppUser.setText(appDisUser_DV);
                            appOrDisAppTimeLay.setHint("Disapproved Time:");
                            appOrDisAppTime.setText(appDisTime_DV);
                        } else {
                            appOrDisAppUserLay.setHint("Approved By / Disapproved By:");
                            appOrDisAppUser.setText(appDisUser_DV);
                            appOrDisAppTimeLay.setHint("Approved / Disapproved Time:");
                            appOrDisAppTime.setText(appDisTime_DV);
                        }
                    } else {
                        appOrDisAppUserLay.setHint("Approved By / Disapproved By:");
                        appOrDisAppUser.setText(appDisUser_DV);
                        appOrDisAppTimeLay.setHint("Approved / Disapproved Time:");
                        appOrDisAppTime.setText(appDisTime_DV);
                    }
                }

                statusReview.setText(status_approved_DV);

                debitVoucherApprovedAdapter = new DebitVoucherApprovedAdapter(debitVoucherApprovedLists,getContext(),DebitVoucherApproved.this);
                itemView.setAdapter(debitVoucherApprovedAdapter);

                if (debitVoucherApprovedLists.size() != 0) {
                    vDate.setText(debitVoucherApprovedLists.get(0).getVmDate());
                    vTime.setText(debitVoucherApprovedLists.get(0).getVmTime());
                    preparedBy.setText(debitVoucherApprovedLists.get(0).getVmUser());
                    billRefNo.setText(debitVoucherApprovedLists.get(0).getVmBillRefNo());
                    billRefDate.setText(debitVoucherApprovedLists.get(0).getVmBillRefDate());
                    remarks.setText(debitVoucherApprovedLists.get(0).getVmNarration());
                } else {
                    vDate.setText("");
                    vTime.setText("");
                    preparedBy.setText("");
                    billRefNo.setText("");
                    billRefDate.setText("");
                    remarks.setText("");
                }

                DecimalFormat formatter = new DecimalFormat("##,##,##,###.##");
                String formatted = formatter.format(total_debit);
                totalDebit.setText(formatted);

                formatted = formatter.format(total_credit);
                totalCredit.setText(formatted);


                System.out.println(total_debit);
                System.out.println(total_credit);
                System.out.println(total_inv_amnt);

                waitProgress.dismiss();
            }
            else {
                waitProgress.dismiss();
                AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getAccData();
                    dialog.dismiss();
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> dialog.dismiss());
            }
        }
        else {
            waitProgress.dismiss();
            AlertDialog dialog = new AlertDialog.Builder(mContext)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getAccData();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }

//    public void AppeoveVoucher() {
//        try {
//            this.connection = createConnection();
//
//
//            CallableStatement callableStatement1 = connection.prepareCall("{call SET_VOUCHER_APPROVED(?,?,?)}");
//            callableStatement1.setInt(1,Integer.parseInt(dVm_id));
//            callableStatement1.setInt(2, 1);
//            callableStatement1.setString(3,emp_code);
//            callableStatement1.execute();
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
//            Log.i("ERRRRR", e.getLocalizedMessage());
//            e.printStackTrace();
//        }
//    }
//
//    public void DisApproveVoucher() {
//        try {
//            this.connection = createConnection();
//
//
//            CallableStatement callableStatement1 = connection.prepareCall("{call SET_VOUCHER_APPROVED(?,?,?)}");
//            callableStatement1.setInt(1,Integer.parseInt(dVm_id));
//            callableStatement1.setInt(2, 0);
//            callableStatement1.setString(3,emp_code);
//            callableStatement1.execute();
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
//            Log.i("ERRRRR", e.getLocalizedMessage());
//            e.printStackTrace();
//        }
//    }

    public void approverProcess() {
        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        String url = "http://103.56.208.123:8001/apex/tterp/voucherApprove/approveProcess";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                connected = string_out.equals("Successfully Created");
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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_VM_ID",dVm_id);
                headers.put("P_APPROVE_FLAG",approve_flag);
                headers.put("P_USER_ID",emp_code);
                return headers;
            }
        };

        requestQueue.add(request);
    }

    private void updateLayout() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                if (approve_flag.equals("1")) {
                    Toast.makeText(getContext(),"APPROVED",Toast.LENGTH_SHORT).show();
                }
                else if (approve_flag.equals("0")){
                    Toast.makeText(getContext(),"DISAPPROVED",Toast.LENGTH_SHORT).show();
                }
                afterVoucherSelect.setVisibility(View.GONE);
                dVm_id = "";
                debitVoucherSelectSpinner.setText("");
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setMessage("Failed to update Debit Voucher. Please Try Again.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    approverProcess();
                    dialog.dismiss();
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> dialog.dismiss());
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(mContext)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                approverProcess();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }
}
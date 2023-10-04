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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.rosemaryapp.amazingspinner.AmazingSpinner;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.WaitProgress;
import ttit.com.shuvo.terrainerp.adapters.PurchaseOrderAppItemAdapter;
import ttit.com.shuvo.terrainerp.arrayList.PurchaseOrderAppItemDetailsLists;
import ttit.com.shuvo.terrainerp.arrayList.PurchaseOrderReqLists;
import ttit.com.shuvo.terrainerp.arrayList.ReceiveTypeList;
import ttit.com.shuvo.terrainerp.dialogues.PurchaseOrderSelectDial;
import static ttit.com.shuvo.terrainerp.login.Login.userInfoLists;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PurchaseOrderApproved#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PurchaseOrderApproved extends Fragment {

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

    public static TextInputEditText purOrderSelectSpinner;

    public static String wom_id = "";

    LinearLayout afterOrderSelect;

    TextInputEditText oDate;
    TextInputEditText edDate;
    TextInputEditText supplierName;
    TextInputEditText supplierCode;
    TextInputEditText supplierAddress;
    TextInputEditText contactPerson;
    TextInputEditText category;
    TextInputEditText remarks;

    AmazingSpinner requisitionSpinner;

    TextView totalQty;
    TextView totalAmount;

    String wom_date = "";
    String wom_edDate = "";
    String supplier_name = "";
    String supplier_code = "";
    String supplier_address = "";
    String contact_person = "";
    String remarks_info = "";
    String category_name = "";

    String woj_id = "";

    LinearLayout afterRequisitionSelect;

    int total_qty = 0;
    double total_amount = 0.0;
    String author_his = "";

    RecyclerView itemView;
    PurchaseOrderAppItemAdapter purchaseOrderAppItemAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<PurchaseOrderReqLists> purchaseOrderReqLists;
    ArrayList<ReceiveTypeList> requisitionSpinnerLists;
    ArrayList<PurchaseOrderAppItemDetailsLists> purchaseOrderAppItemDetailsLists;

    Button approve;
    Button reject;
    String approve_flag = "";


    public PurchaseOrderApproved() {
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
     * @return A new instance of fragment PurchaseOrderApproved.
     */
    // TODO: Rename and change types and number of parameters
    public static PurchaseOrderApproved newInstance(String param1, String param2) {
        PurchaseOrderApproved fragment = new PurchaseOrderApproved();
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
        View view = inflater.inflate(R.layout.fragment_purchase_order_approved, container, false);

        purOrderSelectSpinner = view.findViewById(R.id.purchase_order_search_text);

        afterOrderSelect = view.findViewById(R.id.purchase_order_approved_details_card);
        afterOrderSelect.setVisibility(View.GONE);

        oDate = view.findViewById(R.id.date_pr_order_approved);
        edDate = view.findViewById(R.id.ed_date_pr_order_approved);
        supplierName = view.findViewById(R.id.supplier_name_pr_order_approved);
        supplierCode = view.findViewById(R.id.supplier_code_pr_order_approved);
        supplierAddress = view.findViewById(R.id.supplier_address_pr_order_approved);
        contactPerson = view.findViewById(R.id.contact_person_pr_order_approved);
        remarks = view.findViewById(R.id.remarks_pr_order_approved);

        requisitionSpinner = view.findViewById(R.id.requisition_no_spinner);
        afterRequisitionSelect = view.findViewById(R.id.after_requisition_selection);
        afterRequisitionSelect.setVisibility(View.GONE);

        category = view.findViewById(R.id.category_pr_order_approved);
        totalQty = view.findViewById(R.id.total_quantity_poa);
        totalAmount = view.findViewById(R.id.total_amount_poa);

        approve = view.findViewById(R.id.approve_button_poa);
        reject = view.findViewById(R.id.reject_button_poa);

        itemView = view.findViewById(R.id.purchase_order_approved_transaction_view);

        itemView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        itemView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(itemView.getContext(),DividerItemDecoration.VERTICAL);
        itemView.addItemDecoration(dividerItemDecoration);

        emp_code = userInfoLists.get(0).getUserName();

        purchaseOrderReqLists = new ArrayList<>();
        requisitionSpinnerLists = new ArrayList<>();
        purchaseOrderAppItemDetailsLists = new ArrayList<>();

        requisitionSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                System.out.println(position+": "+name);
                for (int i = 0; i < purchaseOrderReqLists.size(); i++) {
                    if (name.equals(purchaseOrderReqLists.get(i).getReqNo())) {
                        woj_id = (purchaseOrderReqLists.get(i).getWorkOrderJobId());
                        category_name = purchaseOrderReqLists.get(i).getCategoryName();
                    }
                }


                System.out.println(woj_id);
                category.setText(category_name);
                afterRequisitionSelect.setVisibility(View.GONE);
//                new ReqCheck().execute();
                getRequisitionData();

            }
        });


        purOrderSelectSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PurchaseOrderSelectDial purchaseReqSelectDial = new PurchaseOrderSelectDial(mContext,1);
                purchaseReqSelectDial.show(getActivity().getSupportFragmentManager(),"pos");
            }
        });

        purOrderSelectSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!wom_id.isEmpty()) {
                    afterOrderSelect.setVisibility(View.GONE);
//                    new Check().execute();
                    getAccData();
                }

            }
        });

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Approve!")
                        .setMessage("Do you want to approve this Purchase Order?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                approve_flag = "1";
//                                new ApproveCheck().execute();
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

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Approve!")
                        .setMessage("Do you want to reject this Purchase Order?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                approve_flag = "2";
//                                new RejectCheck().execute();
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
//                afterOrderSelect.setVisibility(View.VISIBLE);
//
//
//
//                oDate.setText(wom_date);
//                edDate.setText(wom_edDate);
//                supplierName.setText(supplier_name);
//                supplierCode.setText(supplier_code);
//                supplierAddress.setText(supplier_address);
//                contactPerson.setText(contact_person);
//                remarks.setText(remarks_info);
//
//                requisitionSpinner.setText(purchaseOrderReqLists.get(0).getReqNo());
//                category.setText(purchaseOrderReqLists.get(0).getCategoryName());
//
//                afterRequisitionSelect.setVisibility(View.VISIBLE);
//
//                ArrayList<String> type = new ArrayList<>();
//                for(int i = 0; i < purchaseOrderReqLists.size(); i++) {
//                    type.add(purchaseOrderReqLists.get(i).getReqNo());
//                }
//
//                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);
//
//                requisitionSpinner.setAdapter(arrayAdapter);
//
//
//                purchaseOrderAppItemAdapter = new PurchaseOrderAppItemAdapter(purchaseOrderAppItemDetailsLists,getContext());
//                itemView.setAdapter(purchaseOrderAppItemAdapter);
//
//                totalQty.setText(String.valueOf(total_qty));
//
//                DecimalFormat formatter = new DecimalFormat("##,##,##,###.##");
//                String formatted = formatter.format(total_amount);
//                totalAmount.setText(formatted);
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

//    public class ReqCheck extends AsyncTask<Void, Void, Void> {
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
//                RequisitionData();
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
//                afterRequisitionSelect.setVisibility(View.VISIBLE);
//
//                purchaseOrderAppItemAdapter = new PurchaseOrderAppItemAdapter(purchaseOrderAppItemDetailsLists,getContext());
//                itemView.setAdapter(purchaseOrderAppItemAdapter);
//
//                totalQty.setText(String.valueOf(total_qty));
//
//                DecimalFormat formatter = new DecimalFormat("##,##,##,###.##");
//                String formatted = formatter.format(total_amount);
//                totalAmount.setText(formatted);
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
//                        new ReqCheck().execute();
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
//                AppeovePurReq();
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
//                afterRequisitionSelect.setVisibility(View.GONE);
//                afterOrderSelect.setVisibility(View.GONE);
//                wom_id = "";
//                woj_id = "";
//                purOrderSelectSpinner.setText("");
//
//                wom_date = "";
//                wom_edDate = "";
//                supplier_name = "";
//                supplier_code = "";
//                supplier_address = "";
//                contact_person = "";
//                remarks_info = "";
//                category_name = "";
//
//                total_qty = 0;
//                total_amount = 0.0;
//                author_his = "";
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
//    public class RejectCheck extends AsyncTask<Void, Void, Void> {
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
//                RejectPurReq();
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
//                Toast.makeText(getContext(),"REJECTED",Toast.LENGTH_SHORT).show();
//                afterRequisitionSelect.setVisibility(View.GONE);
//                afterOrderSelect.setVisibility(View.GONE);
//                wom_id = "";
//                woj_id = "";
//                purOrderSelectSpinner.setText("");
//
//                wom_date = "";
//                wom_edDate = "";
//                supplier_name = "";
//                supplier_code = "";
//                supplier_address = "";
//                contact_person = "";
//                remarks_info = "";
//                category_name = "";
//
//                total_qty = 0;
//                total_amount = 0.0;
//                author_his = "";
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
//                        new RejectCheck().execute();
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
//            purchaseOrderReqLists = new ArrayList<>();
//            purchaseOrderAppItemDetailsLists = new ArrayList<>();
//
//            total_qty = 0;
//            total_amount = 0.0;
//
//            Statement statement = connection.createStatement();
//
//            ResultSet resultSet = statement.executeQuery("SELECT\n" +
//                    "    to_char(work_order_mst.wom_date, 'dd-MON-yy')     work_date,\n" +
//                    "    to_char(work_order_mst.wom_edd_date, 'dd-MON-yy') delivery_date,\n" +
//                    "    acc_dtl.ad_name,\n" +
//                    "    acc_dtl.ad_code,\n" +
//                    "    acc_dtl.ad_address,\n" +
//                    "    work_order_mst.wom_remarks,\n" +
//                    "    work_order_mst.wom_id,\n" +
//                    "    acc_attention_dtl.aad_contact_person, work_order_mst.P_AUTHOR_HIS\n" +
//                    "FROM\n" +
//                    "    work_order_mst,\n" +
//                    "    acc_dtl,\n" +
//                    "    acc_attention_dtl\n" +
//                    "WHERE\n" +
//                    "        work_order_mst.wom_ad_id = acc_dtl.ad_id\n" +
//                    "    AND work_order_mst.wom_aad_id = acc_attention_dtl.aad_id\n" +
//                    "    AND work_order_mst.wom_id = "+wom_id+"");
//
//            while (resultSet.next()) {
//
//                wom_date = resultSet.getString(1);
//                wom_edDate = resultSet.getString(2);
//                supplier_name = resultSet.getString(3);
//                supplier_code = resultSet.getString(4);
//                supplier_address = resultSet.getString(5);
//                contact_person = resultSet.getString(8);
//                remarks_info = resultSet.getString(6);
//                author_his = resultSet.getString(9);
//            }
//
//            resultSet.close();
//
//            ResultSet resultSet1 = statement.executeQuery("SELECT\n" +
//                    "    icom.purchase_req_mst.prm_req_no,\n" +
//                    "    icom.item_mst.im_name,\n" +
//                    "    icom.work_order_job.woj_wom_id,\n" +
//                    "    icom.work_order_job.woj_id,\n" +
//                    "    icom.work_order_job.woj_prm_id\n" +
//                    "FROM\n" +
//                    "         icom.work_order_job\n" +
//                    "    INNER JOIN icom.purchase_req_mst ON icom.work_order_job.woj_prm_id = icom.purchase_req_mst.prm_id\n" +
//                    "    INNER JOIN icom.item_mst ON icom.purchase_req_mst.prm_im_id = icom.item_mst.im_id\n" +
//                    "WHERE\n" +
//                    "    icom.work_order_job.woj_wom_id = "+wom_id+"\n" +
//                    "ORDER BY\n" +
//                    "    icom.work_order_job.woj_id");
//
//            while (resultSet1.next()) {
//                purchaseOrderReqLists.add(new PurchaseOrderReqLists(resultSet1.getString(1),resultSet1.getString(2),resultSet1.getString(3),
//                        resultSet1.getString(4),resultSet1.getString(5)));
//            }
//
//            resultSet1.close();
//
//            if (purchaseOrderReqLists.size() != 0) {
//                ResultSet resultSet2 = statement.executeQuery("SELECT WOD.WOD_ID,\n" +
//                        "       WOD.WOD_WOJ_ID,\n" +
//                        "       WOD.WOD_QTY,\n" +
//                        "       WOD.WOD_RATE,\n" +
//                        "       WOD.WOD_REMARKS,\n" +
//                        "       WOD.WOD_ITEM_ID,\n" +
//                        "       WOD.APP_QTY,\n" +
//                        "       WOD.WOD_PR_BALANCE_QTY,\n" +
//                        "       (SELECT PURCHASE_REQ_DTL.PRD_QTY\n" +
//                        "          FROM PURCHASE_REQ_DTL\n" +
//                        "         WHERE PURCHASE_REQ_DTL.PRD_ID = WOD.WOD_PRD_ID)\n" +
//                        "          REQ_QTY,\n" +
//                        "       ROUND (NVL (WOD.WOD_QTY, 0) * NVL (WOD.WOD_RATE, 0), 2) AMOUNT,\n" +
//                        "       ITEM.ITEM_REFERENCE_NAME,\n" +
//                        "       ITEM.ITEM_CODE,\n" +
//                        "       ITEM.ITEM_UNIT,\n" +
//                        "       ITEM.ITEM_BARCODE_NO,\n" +
//                        "       ITEM.ITEM_HS_CODE,\n" +
//                        "       ITEM.ITEM_PART_NUMBER\n" +
//                        "  FROM WORK_ORDER_DTL WOD, ITEM_DTL ITEM\n" +
//                        " WHERE WOD.WOD_ITEM_ID = ITEM.ITEM_ID\n" +
//                        " and WOD.WOD_WOJ_ID = "+purchaseOrderReqLists.get(0).getWorkOrderJobId()+"");
//
//                while (resultSet2.next()) {
//                    purchaseOrderAppItemDetailsLists.add(new PurchaseOrderAppItemDetailsLists(resultSet2.getString(1),resultSet2.getString(2),resultSet2.getString(3),
//                            resultSet2.getString(4),resultSet2.getString(5),resultSet2.getString(6),
//                            resultSet2.getString(7),resultSet2.getString(8),resultSet2.getString(9),
//                            resultSet2.getString(10),resultSet2.getString(11),resultSet2.getString(12),
//                            resultSet2.getString(13),resultSet2.getString(14),resultSet2.getString(15),
//                            resultSet2.getString(16)));
//                }
//                resultSet2.close();
//
//                if (purchaseOrderAppItemDetailsLists.size() != 0) {
//                    for (int i = 0; i < purchaseOrderAppItemDetailsLists.size() ; i++) {
//                        if (purchaseOrderAppItemDetailsLists.get(i).getWodQty() != null) {
//                            total_qty = total_qty + Integer.parseInt(purchaseOrderAppItemDetailsLists.get(i).getWodQty());
//                        }
//                        if (purchaseOrderAppItemDetailsLists.get(i).getAmount() != null) {
//                            total_amount = total_amount + Double.parseDouble(purchaseOrderAppItemDetailsLists.get(i).getAmount());
//                        }
//                    }
//                }
//            }
//
//            statement.close();
//
//
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

        wom_date = "";
        wom_edDate = "";
        supplier_name = "";
        supplier_code = "";
        supplier_address = "";
        contact_person = "";
        remarks_info = "";
        author_his = "";

        purchaseOrderReqLists = new ArrayList<>();

        String orderDataUrl = "http://103.56.208.123:8001/apex/tterp/purchaseOrderApproved/getOrderData?wom_id="+wom_id+"";
        String orderReqList = "http://103.56.208.123:8001/apex/tterp/purchaseOrderApproved/orderReqList?wom_id="+wom_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest orderReq = new StringRequest(Request.Method.GET, orderReqList, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);

                        String prm_req_no = info.getString("prm_req_no");
                        String im_name = info.getString("im_name");
                        String woj_wom_id = info.getString("woj_wom_id");
                        String woj_id_api = info.getString("woj_id");
                        String woj_prm_id = info.getString("woj_prm_id");

                        purchaseOrderReqLists.add(new PurchaseOrderReqLists(prm_req_no,im_name,woj_wom_id,
                                woj_id_api,woj_prm_id));

                    }
                }
                if (purchaseOrderReqLists.size() != 0) {
                    String woj_id_New = purchaseOrderReqLists.get(0).getWorkOrderJobId();
                    getOrderItemDetails(woj_id_New,1);
                }
                else {
                    connected = true;
                    updateFragment();
                }
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

        StringRequest orderDataReq = new StringRequest(Request.Method.GET, orderDataUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);

                        wom_date = info.getString("work_date");
                        wom_edDate = info.getString("delivery_date");
                        supplier_name = info.getString("ad_name");
                        supplier_code = info.getString("ad_code");
                        supplier_address = info.getString("ad_address");
                        contact_person = info.getString("aad_contact_person");
                        remarks_info = info.getString("wom_remarks");
                        author_his = info.getString("p_author_his");

                    }
                }

                requestQueue.add(orderReq);
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

        requestQueue.add(orderDataReq);

    }

    private void updateFragment() {
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                afterOrderSelect.setVisibility(View.VISIBLE);



                oDate.setText(wom_date);
                edDate.setText(wom_edDate);
                supplierName.setText(supplier_name);
                supplierCode.setText(supplier_code);
                supplierAddress.setText(supplier_address);
                contactPerson.setText(contact_person);
                remarks.setText(remarks_info);

                requisitionSpinner.setText(purchaseOrderReqLists.get(0).getReqNo());
                category.setText(purchaseOrderReqLists.get(0).getCategoryName());

                afterRequisitionSelect.setVisibility(View.VISIBLE);

                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < purchaseOrderReqLists.size(); i++) {
                    type.add(purchaseOrderReqLists.get(i).getReqNo());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                requisitionSpinner.setAdapter(arrayAdapter);


                purchaseOrderAppItemAdapter = new PurchaseOrderAppItemAdapter(purchaseOrderAppItemDetailsLists,getContext());
                itemView.setAdapter(purchaseOrderAppItemAdapter);

                totalQty.setText(String.valueOf(total_qty));

                DecimalFormat formatter = new DecimalFormat("##,##,##,###.##");
                String formatted = formatter.format(total_amount);
                totalAmount.setText(formatted);

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

    public void getRequisitionData() {
        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;
        getOrderItemDetails(woj_id,2);
    }

    private void updateLayout() {
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                afterRequisitionSelect.setVisibility(View.VISIBLE);

                purchaseOrderAppItemAdapter = new PurchaseOrderAppItemAdapter(purchaseOrderAppItemDetailsLists,getContext());
                itemView.setAdapter(purchaseOrderAppItemAdapter);

                totalQty.setText(String.valueOf(total_qty));

                DecimalFormat formatter = new DecimalFormat("##,##,##,###.##");
                String formatted = formatter.format(total_amount);
                totalAmount.setText(formatted);

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

                    getRequisitionData();
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

                getRequisitionData();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }

    public void getOrderItemDetails(String woj_id_new, int parser) {
        purchaseOrderAppItemDetailsLists = new ArrayList<>();

        total_qty = 0;
        total_amount = 0.0;

        String url = "http://103.56.208.123:8001/apex/tterp/purchaseOrderApproved/getOrderItemDetails?woj_id="+woj_id_new+"";

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

                        String wod_id = info.getString("wod_id");
                        String wod_woj_id = info.getString("wod_woj_id");
                        String wod_qty = info.getString("wod_qty")
                                .equals("null") ? "" : info.getString("wod_qty");
                        String wod_rate = info.getString("wod_rate")
                                .equals("null") ? "" : info.getString("wod_rate");
                        String wod_remarks = info.getString("wod_remarks")
                                .equals("null") ? "" : info.getString("wod_remarks");
                        String wod_item_id = info.getString("wod_item_id")
                                .equals("null") ? "" : info.getString("wod_item_id");
                        String app_qty = info.getString("app_qty")
                                .equals("null") ? "" : info.getString("app_qty");
                        String wod_pr_balance_qty = info.getString("wod_pr_balance_qty")
                                .equals("null") ? "" : info.getString("wod_pr_balance_qty");
                        String req_qty = info.getString("req_qty")
                                .equals("null") ? "" : info.getString("req_qty");
                        String amount = info.getString("amount")
                                .equals("null") ? "" : info.getString("amount");
                        String item_reference_name = info.getString("item_reference_name")
                                .equals("null") ? "" : info.getString("item_reference_name");
                        String item_code = info.getString("item_code")
                                .equals("null") ? "" : info.getString("item_code");
                        String item_unit = info.getString("item_unit")
                                .equals("null") ? "" : info.getString("item_unit");
                        String item_barcode_no = info.getString("item_barcode_no")
                                .equals("null") ? "" : info.getString("item_barcode_no");
                        String item_hs_code = info.getString("item_hs_code")
                                .equals("null") ? "" : info.getString("item_hs_code");
                        String item_part_number = info.getString("item_part_number")
                                .equals("null") ? "" : info.getString("item_part_number");

                        purchaseOrderAppItemDetailsLists.add(new PurchaseOrderAppItemDetailsLists(wod_id,wod_woj_id,wod_qty,
                                wod_rate,wod_remarks,wod_item_id,app_qty,wod_pr_balance_qty,req_qty,amount,
                                item_reference_name,item_code, item_unit,item_barcode_no,item_hs_code,item_part_number));

                    }
                }

                if (purchaseOrderAppItemDetailsLists.size() != 0) {
                    for (int i = 0; i < purchaseOrderAppItemDetailsLists.size() ; i++) {
                        if (purchaseOrderAppItemDetailsLists.get(i).getWodQty() != null) {
                            if (!purchaseOrderAppItemDetailsLists.get(i).getWodQty().isEmpty()) {
                                total_qty = total_qty + Integer.parseInt(purchaseOrderAppItemDetailsLists.get(i).getWodQty());
                            }
                        }
                        if (purchaseOrderAppItemDetailsLists.get(i).getAmount() != null) {
                            if (!purchaseOrderAppItemDetailsLists.get(i).getAmount().isEmpty()) {
                                total_amount = total_amount + Double.parseDouble(purchaseOrderAppItemDetailsLists.get(i).getAmount());
                            }
                        }
                    }
                }
                connected = true;
                if (parser == 1) {
                    updateFragment();
                }
                else if (parser == 2) {
                    updateLayout();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                if (parser == 1) {
                    updateFragment();
                }
                else if (parser == 2) {
                    updateLayout();
                }
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            if (parser == 1) {
                updateFragment();
            }
            else if (parser == 2) {
                updateLayout();
            }

        });

        requestQueue.add(stringRequest);
    }

//    public void RequisitionData() {
//        try {
//            this.connection = createConnection();
//
//            purchaseOrderAppItemDetailsLists = new ArrayList<>();
//
//            total_qty = 0;
//            total_amount = 0.0;
//
//            Statement statement = connection.createStatement();
//                ResultSet resultSet2 = statement.executeQuery("SELECT WOD.WOD_ID,\n" +
//                        "       WOD.WOD_WOJ_ID,\n" +
//                        "       WOD.WOD_QTY,\n" +
//                        "       WOD.WOD_RATE,\n" +
//                        "       WOD.WOD_REMARKS,\n" +
//                        "       WOD.WOD_ITEM_ID,\n" +
//                        "       WOD.APP_QTY,\n" +
//                        "       WOD.WOD_PR_BALANCE_QTY,\n" +
//                        "       (SELECT PURCHASE_REQ_DTL.PRD_QTY\n" +
//                        "          FROM PURCHASE_REQ_DTL\n" +
//                        "         WHERE PURCHASE_REQ_DTL.PRD_ID = WOD.WOD_PRD_ID)\n" +
//                        "          REQ_QTY,\n" +
//                        "       ROUND (NVL (WOD.WOD_QTY, 0) * NVL (WOD.WOD_RATE, 0), 2) AMOUNT,\n" +
//                        "       ITEM.ITEM_REFERENCE_NAME,\n" +
//                        "       ITEM.ITEM_CODE,\n" +
//                        "       ITEM.ITEM_UNIT,\n" +
//                        "       ITEM.ITEM_BARCODE_NO,\n" +
//                        "       ITEM.ITEM_HS_CODE,\n" +
//                        "       ITEM.ITEM_PART_NUMBER\n" +
//                        "  FROM WORK_ORDER_DTL WOD, ITEM_DTL ITEM\n" +
//                        " WHERE WOD.WOD_ITEM_ID = ITEM.ITEM_ID\n" +
//                        " and WOD.WOD_WOJ_ID = "+ woj_id +"");
//
//                while (resultSet2.next()) {
//                    purchaseOrderAppItemDetailsLists.add(new PurchaseOrderAppItemDetailsLists(resultSet2.getString(1),resultSet2.getString(2),resultSet2.getString(3),
//                            resultSet2.getString(4),resultSet2.getString(5),resultSet2.getString(6),
//                            resultSet2.getString(7),resultSet2.getString(8),resultSet2.getString(9),
//                            resultSet2.getString(10),resultSet2.getString(11),resultSet2.getString(12),
//                            resultSet2.getString(13),resultSet2.getString(14),resultSet2.getString(15),
//                            resultSet2.getString(16)));
//                }
//                resultSet2.close();
//
//                if (purchaseOrderAppItemDetailsLists.size() != 0) {
//                    for (int i = 0; i < purchaseOrderAppItemDetailsLists.size() ; i++) {
//                        if (purchaseOrderAppItemDetailsLists.get(i).getWodQty() != null) {
//                            total_qty = total_qty + Integer.parseInt(purchaseOrderAppItemDetailsLists.get(i).getWodQty());
//                        }
//                        if (purchaseOrderAppItemDetailsLists.get(i).getAmount() != null) {
//                            total_amount = total_amount + Double.parseDouble(purchaseOrderAppItemDetailsLists.get(i).getAmount());
//                        }
//                    }
//                }
//
//
//            statement.close();
//
//
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

//    public void AppeovePurReq() {
//        try {
//            this.connection = createConnection();
//
//            CallableStatement callableStatement1 = connection.prepareCall("{call AUTHORIZATION_PAC.AUTH_NEXT(?,?,?,?,?)}");
//            callableStatement1.setString(1,"PURCHASE_WORK_ORDER_BULK_AUT");
//            callableStatement1.setString(2, author_his);
//            callableStatement1.setString(3,emp_code);
//            callableStatement1.setInt(4,Integer.parseInt(wom_id));
//            callableStatement1.setInt(5,1);
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
//    public void RejectPurReq() {
//        try {
//            this.connection = createConnection();
//
//
//            CallableStatement callableStatement1 = connection.prepareCall("{call AUTHORIZATION_PAC.AUTH_NEXT(?,?,?,?,?)}");
//            callableStatement1.setString(1,"PURCHASE_WORK_ORDER_BULK_AUT");
//            callableStatement1.setString(2, author_his);
//            callableStatement1.setString(3,emp_code);
//            callableStatement1.setInt(4,Integer.parseInt(wom_id));
//            callableStatement1.setInt(5,2);
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

        String url = "http://103.56.208.123:8001/apex/tterp/purchaseOrderApproved/OrderApprovalProcess";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                connected = string_out.equals("Successfully Created");
                updateApprovalPrs();
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateApprovalPrs();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateApprovalPrs();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_AUTHOR",author_his);
                headers.put("P_EMP_CODE",emp_code);
                headers.put("P_WOM_ID",wom_id);
                headers.put("P_APPROVE_FLAG",approve_flag);
                return headers;
            }
        };

        requestQueue.add(request);
    }

    private void updateApprovalPrs() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;
                if (approve_flag.equals("1")) {
                    Toast.makeText(getContext(),"APPROVED",Toast.LENGTH_SHORT).show();
                }
                else if (approve_flag.equals("2")){
                    Toast.makeText(getContext(),"REJECTED",Toast.LENGTH_SHORT).show();
                }
                afterRequisitionSelect.setVisibility(View.GONE);
                afterOrderSelect.setVisibility(View.GONE);
                wom_id = "";
                woj_id = "";
                purOrderSelectSpinner.setText("");

                wom_date = "";
                wom_edDate = "";
                supplier_name = "";
                supplier_code = "";
                supplier_address = "";
                contact_person = "";
                remarks_info = "";
                category_name = "";

                total_qty = 0;
                total_amount = 0.0;
                author_his = "";
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setMessage("Failed to update Purchase Order. Please Try Again.")
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
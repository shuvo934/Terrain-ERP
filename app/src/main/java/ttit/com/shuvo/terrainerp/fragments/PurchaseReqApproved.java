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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.WaitProgress;
import ttit.com.shuvo.terrainerp.adapters.PurchaseReqAppQtyAdapter;
import ttit.com.shuvo.terrainerp.arrayList.PurchaseReqAppItemlists;
import ttit.com.shuvo.terrainerp.arrayList.PurchaseReqAppQtyLists;
import ttit.com.shuvo.terrainerp.dialogues.PurReqAppItemDial;
import ttit.com.shuvo.terrainerp.dialogues.PurchaseReqSelectDial;
import static ttit.com.shuvo.terrainerp.login.Login.userInfoLists;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PurchaseReqApproved#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PurchaseReqApproved extends Fragment {

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

    public static TextInputEditText purReqSelectSpinner;

    public static String prm_id = "";


    LinearLayout afterRequisitionSelect;

    TextInputEditText rDate;
    TextInputEditText erDate;
    TextInputEditText category;
    TextInputEditText remarks;

    TextView totalReqQty;
    public static TextView totalAppReqQty;

    Button showDetails;
    Button approve;
    Button reject;

    String req_Date = "";
    String er_Date = "";
    String remarks_Info = "";
    String category_Name = "";
    String author_his = "";

    RecyclerView itemView;
    PurchaseReqAppQtyAdapter purchaseReqAppQtyAdapter;
    RecyclerView.LayoutManager layoutManager;

    int total_qty = 0;
    public static int total_app_qty = 0;
    String approve_flag = "";

    ArrayList<PurchaseReqAppQtyLists> purchaseReqAppQtyLists;
    public static ArrayList<PurchaseReqAppItemlists> purchaseReqAppItemlists;

    public PurchaseReqApproved() {
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
     * @return A new instance of fragment PurchaseReqApproved.
     */
    // TODO: Rename and change types and number of parameters
    public static PurchaseReqApproved newInstance(String param1, String param2) {
        PurchaseReqApproved fragment = new PurchaseReqApproved();
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
        View view = inflater.inflate(R.layout.fragment_purchase_req_approved, container, false);

        purReqSelectSpinner = view.findViewById(R.id.purchase_requisition_search_text);

        afterRequisitionSelect = view.findViewById(R.id.purchase_req_approved_details_card);
        afterRequisitionSelect.setVisibility(View.GONE);

        rDate = view.findViewById(R.id.date_pr_req_approved);
        erDate = view.findViewById(R.id.er_date_pr_req_approved);
        category = view.findViewById(R.id.category_pr_req_approved);
        remarks = view.findViewById(R.id.remarks_pr_req_approved);

        totalReqQty = view.findViewById(R.id.total_req_qty_pra);
        totalAppReqQty = view.findViewById(R.id.total_app_req_qty_pra);

        showDetails = view.findViewById(R.id.show_item_details_button_pra);
        approve = view.findViewById(R.id.approve_button_pra);
        reject = view.findViewById(R.id.reject_button_pra);

        itemView = view.findViewById(R.id.pur_req_approved_transaction_view);

        itemView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        itemView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(itemView.getContext(),DividerItemDecoration.VERTICAL);
        itemView.addItemDecoration(dividerItemDecoration);

        emp_code = userInfoLists.get(0).getUserName();

        purchaseReqAppQtyLists = new ArrayList<>();
        purchaseReqAppItemlists = new ArrayList<>();

        purReqSelectSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PurchaseReqSelectDial purchaseReqSelectDial = new PurchaseReqSelectDial(mContext,1);
                purchaseReqSelectDial.show(getActivity().getSupportFragmentManager(),"prs");
            }
        });

        purReqSelectSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!prm_id.isEmpty()) {
                    afterRequisitionSelect.setVisibility(View.GONE);
//                    new Check().execute();
                    getRequisitionData();
                }

            }
        });

        showDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PurReqAppItemDial purReqAppItemDial = new PurReqAppItemDial();
                purReqAppItemDial.show(getActivity().getSupportFragmentManager(),"PRAIDD");
            }
        });

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean empty = false;
                for (int i = 0; i < purchaseReqAppQtyLists.size(); i++) {
                    System.out.println("Number: "+ i);
                    if (purchaseReqAppQtyLists.get(i).getApprovedQty() == null) {
                        empty = true;
                        System.out.println("NULL PAISE");
                        break;
                    }else if (purchaseReqAppQtyLists.get(i).getApprovedQty().isEmpty()) {
                        empty = true;
                        System.out.println("EMPTY PAISE");
                        break;
                    }else {
                        empty = false;
                    }
                }

                if (empty) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Approve!")
                            .setMessage("Some of your Item's Approved Quantity is empty. if you still want to approve then empty approved quantity will be equal to your requisition quantity. Do you want to approve this requisition?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

//                                    new ApproveCheck().execute();
                                    ReqOrderApproveProcess(1);
                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Approve!")
                            .setMessage("Do you want to approve this requisition?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

//                                    new ApproveCheck().execute();
                                    ReqOrderApproveProcess(1);
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
                builder.setTitle("Reject!")
                        .setMessage("Do you want to reject this requisition?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

//                                new RejectCheck().execute();
                                ReqOrderApproveProcess(2);
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

        return  view;
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
//                afterRequisitionSelect.setVisibility(View.VISIBLE);
//
//                purchaseReqAppQtyAdapter = new PurchaseReqAppQtyAdapter(purchaseReqAppQtyLists,getContext());
//                itemView.setAdapter(purchaseReqAppQtyAdapter);
//
//                erDate.setText(er_Date);
//                rDate.setText(req_Date);
//                category.setText(category_Name);
//                remarks.setText(remarks_Info);
//
//
//                totalReqQty.setText(String.valueOf(total_qty));
//                totalAppReqQty.setText(String.valueOf(total_app_qty));
//
////                DecimalFormat formatter = new DecimalFormat("##,##,##,###.##");
////                String formatted = formatter.format(total_debit);
////                totalDebit.setText(formatted);
////
////                formatted = formatter.format(total_credit);
////                totalCredit.setText(formatted);
//
//
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
//                prm_id = "";
//                purReqSelectSpinner.setText("");
//
//                req_Date = "";
//                er_Date = "";
//                remarks_Info = "";
//                category_Name = "";
//                author_his = "";
//
//                total_qty = 0;
//                total_app_qty = 0;
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
//                prm_id = "";
//                purReqSelectSpinner.setText("");
//
//                req_Date = "";
//                er_Date = "";
//                remarks_Info = "";
//                category_Name = "";
//                author_his = "";
//
//                total_qty = 0;
//                total_app_qty = 0;
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
//            purchaseReqAppQtyLists = new ArrayList<>();
//            purchaseReqAppItemlists = new ArrayList<>();
//
//            total_qty = 0;
//            total_app_qty = 0;
//
//            Statement statement = connection.createStatement();
//
////            ResultSet resultSet = statement.executeQuery("select prm_id, \n" +
////                    "prm_req_no,\n" +
////                    "TO_CHAR(prm_req_date, 'dd-MON-yy'),\n" +
////                    "TO_CHAR(prm_erd, 'dd-MON-yy'),\n" +
////                    "prm_req_remarks,\n" +
////                    "(select item_mst.im_name from item_mst where im_id = purchase_req_mst.prm_im_id) as category, P_AUTHOR_HIS\n" +
////                    "from purchase_req_mst\n" +
////                    "where prm_id = "+prm_id+"");
////
////            while (resultSet.next()) {
////
////                req_Date = resultSet.getString(3);
////                er_Date = resultSet.getString(4);
////                category_Name = resultSet.getString(6);
////                remarks_Info = resultSet.getString(5);
////                author_his = resultSet.getString(7);
////            }
////
////            resultSet.close();
//
////            ResultSet resultSet1 = statement.executeQuery("select \n" +
////                    "item_dtl.item_id, \n" +
////                    "item_dtl.item_reference_name,\n" +
////                    "purchase_req_dtl.prd_qty, \n" +
////                    "purchase_req_dtl.app_qty, purchase_req_dtl.prd_id\n" +
////                    "from purchase_req_dtl,item_dtl\n" +
////                    "where item_dtl.item_id = purchase_req_dtl.prd_item_id\n" +
////                    "AND prd_prm_id = "+prm_id+"");
////
////            while (resultSet1.next()) {
////                purchaseReqAppQtyLists.add(new PurchaseReqAppQtyLists(resultSet1.getString(1),resultSet1.getString(2),resultSet1.getString(3),
////                        resultSet1.getString(4),resultSet1.getString(5)));
////            }
////
////            resultSet1.close();
//
////            ResultSet resultSet2 = statement.executeQuery("select \n" +
////                    "purchase_req_dtl.prd_id,item_dtl.item_id, item_dtl.item_reference_name, item_dtl.item_unit, item_dtl.item_stock,item_dtl.item_code, item_dtl.item_hs_code,item_dtl.item_part_number \n" +
////                    "from purchase_req_dtl,item_dtl \n" +
////                    "where item_dtl.item_id = purchase_req_dtl.prd_item_id \n" +
////                    "and purchase_req_dtl.prd_prm_id = "+prm_id+"");
////
////            while (resultSet2.next()) {
////                purchaseReqAppItemlists.add(new PurchaseReqAppItemlists(resultSet2.getString(2),resultSet2.getString(3),resultSet2.getString(4),
////                        resultSet2.getString(5),resultSet2.getString(6),resultSet2.getString(7),resultSet2.getString(8)));
////            }
////            resultSet2.close();
//            statement.close();
//
////            if (purchaseReqAppQtyLists.size() != 0) {
////                for (int i = 0; i < purchaseReqAppQtyLists.size() ; i++) {
////                    if (purchaseReqAppQtyLists.get(i).getReqQty() != null) {
////                        total_qty = total_qty + Integer.parseInt(purchaseReqAppQtyLists.get(i).getReqQty());
////                    }
////                    if (purchaseReqAppQtyLists.get(i).getApprovedQty() != null) {
////                        total_app_qty = total_app_qty + Integer.parseInt(purchaseReqAppQtyLists.get(i).getApprovedQty());
////                    }
////                }
////            }
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

    public void getRequisitionData() {
        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        purchaseReqAppQtyLists = new ArrayList<>();
        purchaseReqAppItemlists = new ArrayList<>();

        req_Date = "";
        er_Date = "";
        category_Name = "";
        remarks_Info = "";
        author_his = "";

        total_qty = 0;
        total_app_qty = 0;

        String reqDataUrl = "http://103.56.208.123:8001/apex/tterp/purchReqApproved/getRequisitionData?prm_id="+prm_id+"";
        String reqItemDataUrl = "http://103.56.208.123:8001/apex/tterp/purchReqApproved/reqItemDetails?prm_id="+prm_id+"";
        String itemsUrl = "http://103.56.208.123:8001/apex/tterp/purchReqApproved/getReqItem?prm_id="+prm_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest itemReq = new StringRequest(Request.Method.GET, itemsUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);

//                        String prd_id_new = info.getString("prd_id");
                        String item_id_new = info.getString("item_id");
                        String item_reference_name = info.getString("item_reference_name")
                                .equals("null") ? "" : info.getString("item_reference_name");
                        String item_unit = info.getString("item_unit")
                                .equals("null") ? "" : info.getString("item_unit");
                        String item_stock = info.getString("item_stock")
                                .equals("null") ? "" : info.getString("item_stock");
                        String item_code = info.getString("item_code")
                                .equals("null") ? "" : info.getString("item_code");
                        String item_hs_code = info.getString("item_hs_code")
                                .equals("null") ? "" : info.getString("item_hs_code");
                        String item_part_number = info.getString("item_part_number")
                                .equals("null") ? "" : info.getString("item_part_number");

                        purchaseReqAppItemlists.add(new PurchaseReqAppItemlists(item_id_new,item_reference_name,item_unit,
                                item_stock,item_code,item_hs_code,item_part_number));

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

        StringRequest itemDetailsReq = new StringRequest(Request.Method.GET, reqItemDataUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);

                        String item_id_new = info.getString("item_id");
                        String item_reference_name = info.getString("item_reference_name")
                                .equals("null") ? "" : info.getString("item_reference_name");
                        String prd_qty = info.getString("prd_qty")
                                .equals("null") ? "" : info.getString("prd_qty");
                        String app_qty = info.getString("app_qty")
                                .equals("null") ? "" : info.getString("app_qty");
                        String prd_id_new = info.getString("prd_id")
                                .equals("null") ? "" : info.getString("prd_id");

                        purchaseReqAppQtyLists.add(new PurchaseReqAppQtyLists(item_id_new,item_reference_name,prd_qty,
                                app_qty,prd_id_new,false));
                    }
                }

                if (purchaseReqAppQtyLists.size() != 0) {
                    for (int i = 0; i < purchaseReqAppQtyLists.size() ; i++) {
                        if (purchaseReqAppQtyLists.get(i).getReqQty() != null) {
                            if (!purchaseReqAppQtyLists.get(i).getReqQty().isEmpty()) {
                                total_qty = total_qty + Integer.parseInt(purchaseReqAppQtyLists.get(i).getReqQty());
                            }
                        }
                        if (purchaseReqAppQtyLists.get(i).getApprovedQty() != null) {
                            if (!purchaseReqAppQtyLists.get(i).getApprovedQty().isEmpty()) {
                                total_app_qty = total_app_qty + Integer.parseInt(purchaseReqAppQtyLists.get(i).getApprovedQty());
                            }
                        }
                    }
                }

                requestQueue.add(itemReq);
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

        StringRequest requiDataReq = new StringRequest(Request.Method.GET, reqDataUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);

                        req_Date = info.getString("req_date");
                        er_Date = info.getString("er_date");
                        category_Name = info.getString("category");
                        remarks_Info = info.getString("prm_req_remarks");
                        author_his = info.getString("p_author_his");
                    }
                }
                requestQueue.add(itemDetailsReq);
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

        requestQueue.add(requiDataReq);
    }

    private void updateFragment() {
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                afterRequisitionSelect.setVisibility(View.VISIBLE);

                purchaseReqAppQtyAdapter = new PurchaseReqAppQtyAdapter(purchaseReqAppQtyLists,getContext());
                itemView.setAdapter(purchaseReqAppQtyAdapter);

                erDate.setText(er_Date);
                rDate.setText(req_Date);
                category.setText(category_Name);
                remarks.setText(remarks_Info);


                totalReqQty.setText(String.valueOf(total_qty));
                totalAppReqQty.setText(String.valueOf(total_app_qty));

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

//    public void AppeovePurReq() {
//        try {
//            this.connection = createConnection();
//
//
//            Statement statement = connection.createStatement();
//
//            for (int i = 0; i < purchaseReqAppQtyLists.size(); i++) {
//                System.out.println("Number: "+ i);
//                if (purchaseReqAppQtyLists.get(i).getApprovedQty() == null) {
//                    System.out.println("NULL PAISE");
//
//                    statement.executeUpdate("UPDATE PURCHASE_REQ_DTL\n" +
//                            "SET APP_QTY = "+purchaseReqAppQtyLists.get(i).getReqQty()+"\n" +
//                            "WHERE PRD_ID = "+purchaseReqAppQtyLists.get(i).getPrdId()+"\n");
//                }else if (purchaseReqAppQtyLists.get(i).getApprovedQty().isEmpty()) {
//                    statement.executeUpdate("UPDATE PURCHASE_REQ_DTL\n" +
//                            "SET APP_QTY = "+purchaseReqAppQtyLists.get(i).getReqQty()+"\n" +
//                            "WHERE PRD_ID = "+purchaseReqAppQtyLists.get(i).getPrdId()+"\n");
//                    System.out.println("EMPTY PAISE");
//                }else {
//                    statement.executeUpdate("UPDATE PURCHASE_REQ_DTL\n" +
//                            "SET APP_QTY = "+purchaseReqAppQtyLists.get(i).getApprovedQty()+"\n" +
//                            "WHERE PRD_ID = "+purchaseReqAppQtyLists.get(i).getPrdId()+"\n");
//                }
//            }
//
//            statement.close();
//
//
//
//
//            CallableStatement callableStatement1 = connection.prepareCall("{call AUTHORIZATION_PAC.AUTH_NEXT(?,?,?,?,?)}");
//            callableStatement1.setString(1,"PURCHASE_REQISITION_GI_AUT");
//            callableStatement1.setString(2, author_his);
//            callableStatement1.setString(3,emp_code);
//            callableStatement1.setInt(4,Integer.parseInt(prm_id));
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

//    public void RejectPurReq() {
//        try {
//            this.connection = createConnection();
//
//
//            CallableStatement callableStatement1 = connection.prepareCall("{call AUTHORIZATION_PAC.AUTH_NEXT(?,?,?,?,?)}");
//            callableStatement1.setString(1,"PURCHASE_REQISITION_GI_AUT");
//            callableStatement1.setString(2, author_his);
//            callableStatement1.setString(3,emp_code);
//            callableStatement1.setInt(4,Integer.parseInt(prm_id));
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

    public void ReqOrderApproveProcess(int parser) {
        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        approve_flag = String.valueOf(parser);
        if (approve_flag.equals("1")) {
            getApprovedQty();
        }
        else if (approve_flag.equals("2")) {
            approverProcess();
        }

    }

    public void getApprovedQty() {
        if (purchaseReqAppQtyLists.size() != 0) {
            boolean allUpdated = false;
            for (int i = 0; i < purchaseReqAppQtyLists.size(); i++) {
                allUpdated = purchaseReqAppQtyLists.get(i).isUpdated();
                if (!purchaseReqAppQtyLists.get(i).isUpdated()) {
                    if (purchaseReqAppQtyLists.get(i).getApprovedQty() == null) {
                        String app_qty = purchaseReqAppQtyLists.get(i).getReqQty();
                        String prd_id_for_update = purchaseReqAppQtyLists.get(i).getPrdId();
                        updateAppQty(app_qty, prd_id_for_update,i);
                        break;
                    }
                    else if (purchaseReqAppQtyLists.get(i).getApprovedQty().isEmpty()) {
                        String app_qty = purchaseReqAppQtyLists.get(i).getReqQty();
                        String prd_id_for_update = purchaseReqAppQtyLists.get(i).getPrdId();
                        updateAppQty(app_qty, prd_id_for_update,i);
                        break;
                    }
                    else {
                        String app_qty = purchaseReqAppQtyLists.get(i).getApprovedQty();
                        String prd_id_for_update = purchaseReqAppQtyLists.get(i).getPrdId();
                        updateAppQty(app_qty, prd_id_for_update,i);
                        break;
                    }
                }
            }
            if (allUpdated) {
                approverProcess();
            }
        }
        else {
            approverProcess();
        }
    }

    public void updateAppQty(String app_qt, String prd_id_new, int index) {
        String url = "http://103.56.208.123:8001/apex/tterp/purchReqApproved/updateItemAppQty";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                if (string_out.equals("Successfully Created")) {
                    purchaseReqAppQtyLists.get(index).setUpdated(true);
                    getApprovedQty();
                }
                else {
                    connected = false;
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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_APP_QTY",app_qt);
                headers.put("P_PRD_ID",prd_id_new);
                return headers;
            }
        };

        requestQueue.add(request);
    }

    public void approverProcess() {
        String url = "http://103.56.208.123:8001/apex/tterp/purchReqApproved/approverProcess";

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
                headers.put("P_AUTHOR",author_his);
                headers.put("P_EMP_CODE",emp_code);
                headers.put("P_PRM_ID",prm_id);
                headers.put("P_APPROVE_FLAG",approve_flag);
                return headers;
            }
        };

        requestQueue.add(request);
    }

    private void updateLayout() {
        waitProgress.dismiss();
        if (conn) {
            if (connected)  {
                conn = false;
                connected = false;

                if (approve_flag.equals("1")) {
                    Toast.makeText(getContext(),"APPROVED",Toast.LENGTH_SHORT).show();
                }
                else if (approve_flag.equals("2")){
                    Toast.makeText(getContext(),"REJECTED",Toast.LENGTH_SHORT).show();
                }
                afterRequisitionSelect.setVisibility(View.GONE);
                prm_id = "";
                purReqSelectSpinner.setText("");

                req_Date = "";
                er_Date = "";
                remarks_Info = "";
                category_Name = "";
                author_his = "";

                total_qty = 0;
                total_app_qty = 0;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setMessage("Failed to update Purchase Requisition. Please Try Again.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    ReqOrderApproveProcess(Integer.parseInt(approve_flag));
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

                ReqOrderApproveProcess(Integer.parseInt(approve_flag));
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }
}
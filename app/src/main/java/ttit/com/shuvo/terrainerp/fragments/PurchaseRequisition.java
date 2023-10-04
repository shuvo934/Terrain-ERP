package ttit.com.shuvo.terrainerp.fragments;

import static ttit.com.shuvo.terrainerp.login.Login.userInfoLists;

import android.app.DatePickerDialog;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
import com.google.android.material.textfield.TextInputLayout;
import com.rosemaryapp.amazingspinner.AmazingSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.WaitProgress;
import ttit.com.shuvo.terrainerp.adapters.PurhcaseReqItemReqQtyAdapter;
import ttit.com.shuvo.terrainerp.arrayList.PRSelectedItemLists;
import ttit.com.shuvo.terrainerp.arrayList.ReceiveTypeList;
import ttit.com.shuvo.terrainerp.arrayList.StringWthBool;
import ttit.com.shuvo.terrainerp.dialogues.ItemSelectionForPRDial;
import ttit.com.shuvo.terrainerp.dialogues.PurchaseReqSelectDial;
import ttit.com.shuvo.terrainerp.mainBoard.MainMenu;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PurchaseRequisition#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PurchaseRequisition extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean master_conn = false;
    private Boolean dtl_conn = false;
    private Boolean master_connected = false;
    private Boolean dtl_connected = false;
    private Boolean connected = false;

    String emp_code = "";

    public static TextInputEditText purReqToUpdateSelectSpinner;

    public static String prm_id_to_update = "";
    public static String req_no_to_show = "";
    TextInputEditText reqNoToUp;
    TextInputLayout reqNoLayout;
    TextInputEditText requisitionDate;
    public static String requisition_date = "";
    TextInputEditText receiveDate;
    String receive_date = "";
    TextView receiveDateMissing;
    private int mYear, mMonth, mDay;
    TextInputEditText prRemarks;
    String pr_remarks = "";
    public static TextInputLayout categoryLay;
    AmazingSpinner categorySpinner;
    TextView categoryMiss;
    ArrayList<ReceiveTypeList> categoryLists;
    String categoryId = "";
    String category_Name = "";
    String searchingCate = "";
    Button itemSelection;
    public static LinearLayout itemSelectLay;

    RecyclerView selectedItemView;
    public static PurhcaseReqItemReqQtyAdapter purhcaseReqItemReqQtyAdapter;
    RecyclerView.LayoutManager layoutManager;
    public static TextView totalReqQty;

    Button save;
    Button update;

    public static int total_req_qty_pr = 0;
    public static ArrayList<PRSelectedItemLists> prSelectedItemLists;
    String inserter_req_no = "";
    String inserted_prm_id = "";
    String author_his = "";
    ArrayList<String> old_prd_id_Lists;
    ArrayList<String> updated_prd_id_Lists;
    ArrayList<String> need_to_delete_prd_Lists;
    ArrayList<StringWthBool> prd_to_delete_Lists;


    public PurchaseRequisition() {
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
     * @return A new instance of fragment PurchaseRequisition.
     */
    // TODO: Rename and change types and number of parameters
    public static PurchaseRequisition newInstance(String param1, String param2) {
        PurchaseRequisition fragment = new PurchaseRequisition();
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
        View view = inflater.inflate(R.layout.fragment_purchase_requisition, container, false);
        purReqToUpdateSelectSpinner = view.findViewById(R.id.purchase_requisition_search_text_to_update);
        reqNoLayout = view.findViewById(R.id.requisition_no_after_save_or_update_layout);
        reqNoLayout.setVisibility(View.GONE);
        reqNoToUp = view.findViewById(R.id.requisition_no_after_save_or_update);
        requisitionDate = view.findViewById(R.id.requisition_date_after_save);
        receiveDate = view.findViewById(R.id.expected_receive_date_purch_requisition);
        receiveDateMissing = view.findViewById(R.id.expected_receive_date_missing_msg);
        receiveDateMissing.setVisibility(View.GONE);
        prRemarks = view.findViewById(R.id.remarks_for_purchase_requisition);
        categoryLay = view.findViewById(R.id.spinner_layout_cat_purchase_requisition);
        categoryLay.setEnabled(true);
        categorySpinner = view.findViewById(R.id.cat_type_spinner_purchase_requisition);
        categoryMiss = view.findViewById(R.id.category_selection_missing_in_pr_req);
        categoryMiss.setVisibility(View.GONE);
        itemSelection = view.findViewById(R.id.select_item_for_purchase_requisition);
        itemSelection.setEnabled(false);
        itemSelectLay = view.findViewById(R.id.item_selection_for_pr_layout);
        itemSelectLay.setVisibility(View.GONE);
        totalReqQty = view.findViewById(R.id.total_req_qty_of_purchase_requisition);
        selectedItemView = view.findViewById(R.id.pur_req_items_save_update_view);

        save = view.findViewById(R.id.save_requisition_button);
        save.setVisibility(View.VISIBLE);
        update = view.findViewById(R.id.update_requisition_button);
        update.setVisibility(View.GONE);

        emp_code = userInfoLists.get(0).getUserName();
        prSelectedItemLists = new ArrayList<>();

        selectedItemView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        selectedItemView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(selectedItemView.getContext(),DividerItemDecoration.VERTICAL);
        selectedItemView.addItemDecoration(dividerItemDecoration);

        purhcaseReqItemReqQtyAdapter = new PurhcaseReqItemReqQtyAdapter(prSelectedItemLists,mContext);
        selectedItemView.setAdapter(purhcaseReqItemReqQtyAdapter);

        //Getting Date
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());

        requisition_date = df.format(c);
        requisitionDate.setText(requisition_date);

        purReqToUpdateSelectSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PurchaseReqSelectDial purchaseReqSelectDial = new PurchaseReqSelectDial(mContext,2);
                purchaseReqSelectDial.show(getActivity().getSupportFragmentManager(),"prsupdate");
            }
        });

        purReqToUpdateSelectSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!prm_id_to_update.isEmpty()) {
                    reqNoLayout.setVisibility(View.VISIBLE);
                    reqNoToUp.setText(req_no_to_show);
                    save.setVisibility(View.GONE);
                    update.setVisibility(View.VISIBLE);
                    getRequisitionData();
                }

            }
        });

        receiveDate.setOnClickListener(v -> {
            final Calendar c1 = Calendar.getInstance();
            mYear = c1.get(Calendar.YEAR);
            mMonth = c1.get(Calendar.MONTH);
            mDay = c1.get(Calendar.DAY_OF_MONTH);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, (view1, year, month, dayOfMonth) -> {

                    String monthName = "";
                    String dayOfMonthName = "";
                    String yearName = "";
                    month = month + 1;
                    if (month == 1) {
                        monthName = "JAN";
                    } else if (month == 2) {
                        monthName = "FEB";
                    } else if (month == 3) {
                        monthName = "MAR";
                    } else if (month == 4) {
                        monthName = "APR";
                    } else if (month == 5) {
                        monthName = "MAY";
                    } else if (month == 6) {
                        monthName = "JUN";
                    } else if (month == 7) {
                        monthName = "JUL";
                    } else if (month == 8) {
                        monthName = "AUG";
                    } else if (month == 9) {
                        monthName = "SEP";
                    } else if (month == 10) {
                        monthName = "OCT";
                    } else if (month == 11) {
                        monthName = "NOV";
                    } else if (month == 12) {
                        monthName = "DEC";
                    }

                    if (dayOfMonth <= 9) {
                        dayOfMonthName = "0" + String.valueOf(dayOfMonth);
                    } else {
                        dayOfMonthName = String.valueOf(dayOfMonth);
                    }
                    yearName  = String.valueOf(year);
                    yearName = yearName.substring(yearName.length()-2);
                    System.out.println(yearName);
                    System.out.println(dayOfMonthName);
                    String dateText = dayOfMonthName + "-" + monthName + "-" + yearName;
                    receiveDate.setText(dateText);
                    receiveDateMissing.setVisibility(View.GONE);
                    receive_date = receiveDate.getText().toString();

                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        prRemarks.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                        event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        Log.i("Let see", "Come here");
                        closeKeyBoard();

                        return false; // consume.
                    }
                }
                return false;
            }
        });

        categorySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                System.out.println(position+": "+name);
                for (int i = 0; i <categoryLists.size(); i++) {
                    if (name.equals(categoryLists.get(i).getType())) {
                        categoryId = categoryLists.get(i).getId();
                        searchingCate = categoryLists.get(i).getType();
                    }
                }

                categoryMiss.setVisibility(View.GONE);
                itemSelection.setEnabled(true);
            }
        });

        itemSelection.setOnClickListener(v -> {
            ItemSelectionForPRDial itemSelectionForPRDial = new ItemSelectionForPRDial(mContext, categoryId, requisition_date);
            itemSelectionForPRDial.show(getActivity().getSupportFragmentManager(),"prItemSelect");
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pr_remarks = Objects.requireNonNull(prRemarks.getText()).toString();
                if (!receive_date.isEmpty()) {
                    if (!categoryId.isEmpty()) {
                        if (prSelectedItemLists.size() != 0) {
                            boolean emptyQty = false;
                            for (int i = 0; i < prSelectedItemLists.size(); i++) {
                                if (prSelectedItemLists.get(i).getReqQty().isEmpty()) {
                                    emptyQty = true;
                                    break;
                                }
                            }
                            if (!emptyQty) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setTitle("SAVE REQUISITION!")
                                        .setMessage("Do you want to save this requisition?")
                                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                saveRequisition();
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
                            else {
                                Toast.makeText(mContext, "Please Set Item Requisition Quantity Correctly", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(mContext, "Please Select Items", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        categoryMiss.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    receiveDateMissing.setVisibility(View.VISIBLE);
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pr_remarks = Objects.requireNonNull(prRemarks.getText()).toString();
                if (!receive_date.isEmpty()) {
                    if (!categoryId.isEmpty()) {
                        if (prSelectedItemLists.size() != 0) {
                            boolean emptyQty = false;
                            for (int i = 0; i < prSelectedItemLists.size(); i++) {
                                if (prSelectedItemLists.get(i).getReqQty().isEmpty()) {
                                    emptyQty = true;
                                    break;
                                }
                            }
                            if (!emptyQty) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setTitle("UPDATE REQUISITION!")
                                        .setMessage("Do you want to update this requisition?")
                                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                updateRequisition();
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
                            else {
                                Toast.makeText(mContext, "Please Set Item Requisition Quantity Correctly", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(mContext, "Please Select Items", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        categoryMiss.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    receiveDateMissing.setVisibility(View.VISIBLE);
                }
            }
        });

        getCategory();

        return view;
    }

    private void closeKeyBoard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void getCategory() {
        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;
        categoryLists = new ArrayList<>();

        String url = "http://103.56.208.123:8001/apex/tterp/utility/getCategory";

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

                        String im_id = info.getString("im_id");
                        String im_name = info.getString("im_name");

                        categoryLists.add(new ReceiveTypeList(im_id,im_name,""));
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

        requestQueue.add(request);
    }

    private void updateFragment() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < categoryLists.size(); i++) {
                    type.add(categoryLists.get(i).getType());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                categorySpinner.setAdapter(arrayAdapter);

            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getCategory();
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

                getCategory();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }

    //getting requisition Information for update
    public void getRequisitionData() {
        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;
        categoryLay.setEnabled(true);
        itemSelection.setEnabled(false);
        itemSelectLay.setVisibility(View.GONE);

        prSelectedItemLists.clear();
        old_prd_id_Lists = new ArrayList<>();

        requisition_date = "";
        receive_date = "";
        category_Name = "";
        pr_remarks = "";
        author_his = "";
        categoryId = "";

        total_req_qty_pr = 0;

        String reqDataUrl = "http://103.56.208.123:8001/apex/tterp/purchReqApproved/getRequisitionData?prm_id="+prm_id_to_update+"";
        String itemsUrl = "http://103.56.208.123:8001/apex/tterp/purchReqApproved/getReqItem?prm_id="+prm_id_to_update+"";

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

                        String prd_id_new = info.getString("prd_id");
                        String item_id_new = info.getString("item_id");
                        String item_reference_name = info.getString("item_reference_name")
                                .equals("null") ? "" : info.getString("item_reference_name");
                        String item_unit = info.getString("item_unit")
                                .equals("null") ? "" : info.getString("item_unit");
//                        String item_stock = info.getString("item_stock")
//                                .equals("null") ? "" : info.getString("item_stock");
                        String item_code = info.getString("item_code")
                                .equals("null") ? "" : info.getString("item_code");
                        String item_hs_code = info.getString("item_hs_code")
                                .equals("null") ? "" : info.getString("item_hs_code");
                        String item_part_number = info.getString("item_part_number")
                                .equals("null") ? "" : info.getString("item_part_number");
                        String item_color_id = info.getString("item_color_id")
                                .equals("null") ? "" : info.getString("item_color_id");
                        String item_size_id = info.getString("item_size_id")
                                .equals("null") ? "" : info.getString("item_size_id");
                        String prd_qty = info.getString("prd_qty")
                                .equals("null") ? "" : info.getString("prd_qty");

                        prSelectedItemLists.add(new PRSelectedItemLists(category_Name,item_reference_name,"",
                                item_hs_code,item_part_number,item_id_new,categoryId,item_unit,item_code,item_color_id,
                                item_size_id,"","",prd_qty,prd_id_new,false));
                        old_prd_id_Lists.add(prd_id_new);

                    }
                }

                connected = true;
                updateInterface();
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateInterface();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateInterface();
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

                        requisition_date = info.getString("req_date");
                        receive_date = info.getString("er_date");
                        category_Name = info.getString("category");
                        pr_remarks = info.getString("prm_req_remarks");
                        author_his = info.getString("p_author_his");
                        categoryId = info.getString("prm_im_id");
                    }
                }
                requestQueue.add(itemReq);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateInterface();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateInterface();
        });

        requestQueue.add(requiDataReq);
    }

    private void updateInterface() {
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                requisitionDate.setText(requisition_date);
                receiveDate.setText(receive_date);
                prRemarks.setText(pr_remarks);
                categorySpinner.setText(category_Name);

                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < categoryLists.size(); i++) {
                    type.add(categoryLists.get(i).getType());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                categorySpinner.setAdapter(arrayAdapter);

                receiveDateMissing.setVisibility(View.GONE);
                categoryMiss.setVisibility(View.GONE);

                if (!categoryId.isEmpty()) {
                    itemSelection.setEnabled(true);
                    if (prSelectedItemLists.size() != 0) {
                        categoryLay.setEnabled(false);
                        itemSelectLay.setVisibility(View.VISIBLE);
                        for (int i = 0; i < prSelectedItemLists.size() ; i++) {
                            if (prSelectedItemLists.get(i).getReqQty() != null) {
                                if (!prSelectedItemLists.get(i).getReqQty().isEmpty()) {
                                    total_req_qty_pr = total_req_qty_pr + Integer.parseInt(prSelectedItemLists.get(i).getReqQty());
                                }
                            }
                        }
                        totalReqQty.setText(String.valueOf(total_req_qty_pr));
                        purhcaseReqItemReqQtyAdapter.notifyDataSetChanged();
                    }
                }

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

    //updating Requisition
    public void updateRequisition() {
        prd_to_delete_Lists = new ArrayList<>();
        updated_prd_id_Lists = new ArrayList<>();
        need_to_delete_prd_Lists = new ArrayList<>();
        need_to_delete_prd_Lists.addAll(old_prd_id_Lists);
        System.out.println(old_prd_id_Lists);
        System.out.println(need_to_delete_prd_Lists);
        for (int i = 0; i < prSelectedItemLists.size(); i++) {
            String prd = prSelectedItemLists.get(i).getPrd_id();
            if (!prd.isEmpty()) {
                updated_prd_id_Lists.add(prd);
            }
        }

        need_to_delete_prd_Lists.removeAll(updated_prd_id_Lists);
        System.out.println(need_to_delete_prd_Lists);
        for (int i = 0; i < need_to_delete_prd_Lists.size(); i++) {
            prd_to_delete_Lists.add(new StringWthBool(need_to_delete_prd_Lists.get(i),false));
        }

        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        master_conn = false;
        dtl_conn = false;
        master_connected = false;
        dtl_connected = false;

        String url = "http://103.56.208.123:8001/apex/tterp/purchaseReq/updateReqMst";
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            master_conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                if (string_out.equals("Successfully Created")) {
                   master_connected = true;
                   checkToDeletePRD();
                }
                else {
                    master_connected = false;
                    updatePage();
                }

            }
            catch (JSONException e) {
                e.printStackTrace();
                master_connected = false;
                updatePage();
            }
        }, error -> {
            error.printStackTrace();
            master_conn = false;
            master_connected = false;
            updatePage();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_PRM_ID",prm_id_to_update);
                headers.put("P_USER",emp_code);
                headers.put("P_REMARKS",pr_remarks);
                headers.put("RCV_DATE",receive_date);
                headers.put("P_CATEGORY_ID",categoryId);
                return headers;
            }
        };

        requestQueue.add(request);

    }

    public void checkToDeletePRD() {
        if (prd_to_delete_Lists.size() != 0) {
            boolean allUpdated = false;
            for (int i = 0; i < prd_to_delete_Lists.size(); i++) {
                allUpdated = prd_to_delete_Lists.get(i).isUpdated();
                if (!prd_to_delete_Lists.get(i).isUpdated()) {
                    allUpdated = prd_to_delete_Lists.get(i).isUpdated();
                    String prd_id_delete = prd_to_delete_Lists.get(i).getName();
                    deleteReqDtl(prd_id_delete,i);
                    break;
                }
            }
            if (allUpdated) {
                checkToUpdateReqDtl();
            }
        }
        else {
            checkToUpdateReqDtl();
        }
    }

    public void deleteReqDtl(String prd_id_delete, int index) {
        String url = "http://103.56.208.123:8001/apex/tterp/purchaseReq/deleteReqDtl";
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            dtl_conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                if (string_out.equals("Successfully Created")) {
                    prd_to_delete_Lists.get(index).setUpdated(true);
                    checkToDeletePRD();
                }
                else {
                    dtl_connected = false;
                    updatePage();
                }

            }
            catch (JSONException e) {
                e.printStackTrace();
                dtl_connected = false;
                updatePage();
            }
        }, error -> {
            error.printStackTrace();
            dtl_conn = false;
            dtl_connected = false;
            updatePage();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_PRD_ID",prd_id_delete);
                return headers;
            }
        };

        requestQueue.add(request);
    }

    public void checkToUpdateReqDtl() {
        if (prSelectedItemLists.size() != 0) {
            boolean allUpdated = false;
            for (int i = 0; i < prSelectedItemLists.size(); i++) {
                allUpdated = prSelectedItemLists.get(i).isUpdated();
                if (!prSelectedItemLists.get(i).isUpdated()) {
                    allUpdated = prSelectedItemLists.get(i).isUpdated();
                    String item_id = prSelectedItemLists.get(i).getItem_id();
                    String color_id = prSelectedItemLists.get(i).getItem_color_id();
                    if (color_id.isEmpty()) {
                        color_id = "1";
                    }
                    String size_id = prSelectedItemLists.get(i).getItem_size_id();
                    if (size_id.isEmpty()) {
                        size_id = "1";
                    }
                    String req_qty = prSelectedItemLists.get(i).getReqQty();
                    String prd_Id_update = prSelectedItemLists.get(i).getPrd_id();
                    updateRequisitionItemData(prm_id_to_update, item_id,color_id,size_id,req_qty,prd_Id_update,i);
                    break;
                }
            }
            if (allUpdated) {
                dtl_connected = true;
                updatePage();
            }
        }
        else {
            dtl_connected = true;
            updatePage();
        }
    }

    public void updateRequisitionItemData(String prm_id_old, String item_id, String color_id, String size_id, String req_qty, String prd_id_update, int index) {
        String url = "http://103.56.208.123:8001/apex/tterp/purchaseReq/updateReqDtl";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            dtl_conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                if (string_out.equals("Successfully Created")) {
                    prSelectedItemLists.get(index).setUpdated(true);
                    checkToUpdateReqDtl();
                }
                else {
                    dtl_connected = false;
                    updatePage();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                dtl_connected = false;
                updatePage();
            }
        }, error -> {
            error.printStackTrace();
            dtl_conn = false;
            dtl_connected = false;
            updatePage();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_PRM_ID",prm_id_old);
                headers.put("P_ITEM_ID",item_id);
                headers.put("P_COLOR_ID",color_id);
                headers.put("P_SIZE_ID",size_id);
                headers.put("REQ_QTY",req_qty);
                headers.put("P_CATEGORY_ID",categoryId);
                headers.put("P_USER",emp_code);
                headers.put("P_OLD_PRD_ID",prd_id_update);
                return headers;
            }
        };

        requestQueue.add(request);
    }

    private void updatePage() {
        waitProgress.dismiss();
        if (master_conn) {
            if (master_connected) {
                if (dtl_conn) {
                    if (dtl_connected) {

                        master_conn =false;
                        master_connected = false;
                        dtl_conn = false;
                        dtl_connected = false;

                        AlertDialog dialog = new AlertDialog.Builder(mContext)
                                .setMessage("Purchase Requisition Updated Successfully.")
                                .setPositiveButton("Ok", null)
                                .show();

                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        positive.setOnClickListener(v -> {

                            MainMenu.dashboardFront = false;
                            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,new PurchaseRequisition()).commit();
                            dialog.dismiss();
                        });

                    }
                    else {
                        AlertDialog dialog = new AlertDialog.Builder(mContext)
                                .setMessage("There is a network issue in the server. Please try again or data will be lost.")
                                .setPositiveButton("Retry", null)
                                .setNegativeButton("Cancel", null)
                                .show();

                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        positive.setOnClickListener(v -> {

                            checkToDeletePRD();
                            dialog.dismiss();
                        });

                        Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                        negative.setOnClickListener(v -> dialog.dismiss());
                    }
                }
                else {
                    AlertDialog dialog = new AlertDialog.Builder(mContext)
                            .setMessage("Failed to update data for Item Requisition. Please try again or data will be lost.")
                            .setPositiveButton("Retry", null)
                            .setNegativeButton("Cancel", null)
                            .show();

                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positive.setOnClickListener(v -> {

                        checkToDeletePRD();
                        dialog.dismiss();
                    });

                    Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    negative.setOnClickListener(v -> dialog.dismiss());
                }
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    updateRequisition();
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

                updateRequisition();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }

    //Saving New Requisition
    public void saveRequisition() {
        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        master_conn = false;
        dtl_conn = false;
        master_connected = false;
        dtl_connected = false;

        String url = "http://103.56.208.123:8001/apex/tterp/purchaseReq/insertReqMst";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            master_conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                String out_prm_id = jsonObject.getString("out_prm_id").equals("null") ? "" : jsonObject.getString("out_prm_id");
                String out_prm_req_no = jsonObject.getString("out_prm_req_no");
                if (string_out.equals("Successfully Created")) {
                    if (!out_prm_id.isEmpty()) {
                        inserter_req_no = out_prm_req_no;
                        inserted_prm_id = out_prm_id;
                        System.out.println("NEW PRM ID: "+ inserted_prm_id);
                        master_connected = true;
                        saveItemWiseData();
                    }
                    else {
                        master_connected = false;
                        updateLayout();
                    }
                }
                else {
                    master_connected = false;
                    updateLayout();
                }

            }
            catch (JSONException e) {
                e.printStackTrace();
                master_connected = false;
                updateLayout();
            }
        }, error -> {
            error.printStackTrace();
            master_conn = false;
            master_connected = false;
            updateLayout();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_PRM_REQ_DATE",requisition_date);
                headers.put("P_USER",emp_code);
                headers.put("P_REMARKS",pr_remarks);
                headers.put("RCV_DATE",receive_date);
                headers.put("P_CATEGORY_ID",categoryId);
                return headers;
            }
        };

        requestQueue.add(request);

    }

    private void saveItemWiseData() {
        if (prSelectedItemLists.size() != 0) {
            boolean allUpdated = false;
            for (int i = 0; i < prSelectedItemLists.size(); i++) {
                allUpdated = prSelectedItemLists.get(i).isUpdated();
                if (!prSelectedItemLists.get(i).isUpdated()) {
                    allUpdated = prSelectedItemLists.get(i).isUpdated();
                    String item_id = prSelectedItemLists.get(i).getItem_id();
                    String color_id = prSelectedItemLists.get(i).getItem_color_id();
                    if (color_id.isEmpty()) {
                        color_id = "1";
                    }
                    String size_id = prSelectedItemLists.get(i).getItem_size_id();
                    if (size_id.isEmpty()) {
                        size_id = "1";
                    }
                    String req_qty = prSelectedItemLists.get(i).getReqQty();
                    saveRequisitionItemData(inserted_prm_id, item_id,color_id,size_id,req_qty,i);
                    break;
                }
            }
            if (allUpdated) {
                dtl_connected = true;
                updateLayout();
            }
        }
        else {
            dtl_conn = true;
            dtl_connected = true;
            updateLayout();
        }
    }

    public void saveRequisitionItemData(String inserted_prm_id,String item_id,String color_id,String size_id,String req_qty,int index) {
        String url = "http://103.56.208.123:8001/apex/tterp/purchaseReq/insertReqDtl";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            dtl_conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                if (string_out.equals("Successfully Created")) {
                    prSelectedItemLists.get(index).setUpdated(true);
                    saveItemWiseData();
                }
                else {
                    dtl_connected = false;
                    updateLayout();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                dtl_connected = false;
                updateLayout();
            }
        }, error -> {
            error.printStackTrace();
            dtl_conn = false;
            dtl_connected = false;
            updateLayout();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_PRM_ID",inserted_prm_id);
                headers.put("P_ITEM_ID",item_id);
                headers.put("P_COLOR_ID",color_id);
                headers.put("P_SIZE_ID",size_id);
                headers.put("REQ_QTY",req_qty);
                headers.put("P_CATEGORY_ID",categoryId);
                headers.put("P_USER",emp_code);
                return headers;
            }
        };

        requestQueue.add(request);
    }

    private void updateLayout() {
        waitProgress.dismiss();
        if (master_conn) {
            if (master_connected) {
                if (dtl_conn) {
                    if (dtl_connected) {

                        master_conn =false;
                        master_connected = false;
                        dtl_conn = false;
                        dtl_connected = false;

                        AlertDialog dialog = new AlertDialog.Builder(mContext)
                                .setMessage("New Purchase Requisition Saved Successfully. Your New Purchase Requisition No is: "+inserter_req_no)
                                .setPositiveButton("Ok", null)
                                .show();

                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        positive.setOnClickListener(v -> {

                            MainMenu.dashboardFront = false;
                            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,new PurchaseRequisition()).commit();
                            dialog.dismiss();
                        });

                    }
                    else {
                        AlertDialog dialog = new AlertDialog.Builder(mContext)
                                .setMessage("There is a network issue in the server. Please try again or data will be lost.")
                                .setPositiveButton("Retry", null)
                                .setNegativeButton("Cancel", null)
                                .show();

                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        positive.setOnClickListener(v -> {

                            saveItemWiseData();
                            dialog.dismiss();
                        });

                        Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                        negative.setOnClickListener(v -> dialog.dismiss());
                    }
                }
                else {
                    AlertDialog dialog = new AlertDialog.Builder(mContext)
                            .setMessage("Failed to save data for Item Requisition. Please try again or data will be lost.")
                            .setPositiveButton("Retry", null)
                            .setNegativeButton("Cancel", null)
                            .show();

                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positive.setOnClickListener(v -> {

                        saveItemWiseData();
                        dialog.dismiss();
                    });

                    Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    negative.setOnClickListener(v -> dialog.dismiss());
                }
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    saveRequisition();
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

                saveRequisition();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }
}
package ttit.com.shuvo.terrainerp.fragments;

import static ttit.com.shuvo.terrainerp.login.Login.userInfoLists;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.WaitProgress;
import ttit.com.shuvo.terrainerp.adapters.PurRcvCategorySelectedAdapter;
import ttit.com.shuvo.terrainerp.adapters.PurchaseRcvItemDetailsAdapter;
import ttit.com.shuvo.terrainerp.adapters.PurchaseRcvWarehouseRcvAdapter;
import ttit.com.shuvo.terrainerp.arrayList.PurchaseRcvItemDetailsList;
import ttit.com.shuvo.terrainerp.arrayList.PurchaseRcvWarehouseRcvLists;
import ttit.com.shuvo.terrainerp.arrayList.PurchaseReceiveAllSelectedLists;
import ttit.com.shuvo.terrainerp.dialogues.CategoryWarehouseSelectionDial;
import ttit.com.shuvo.terrainerp.dialogues.PurchaseOrderSelectForPRCV;
import ttit.com.shuvo.terrainerp.mainBoard.MainMenu;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PurchaseReceive#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PurchaseReceive extends Fragment implements PurRcvCategorySelectedAdapter.ClickedItem, PurchaseRcvItemDetailsAdapter.ClickedItem {

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
    private Boolean master_conn = false;
    private Boolean master_connected = false;
    private Boolean rcvJob_conn = false;
    private Boolean rcvJob_connected = false;
    private Boolean rcvDtl_conn = false;
    private Boolean rcvDtl_connected = false;
    private Boolean rcvWare_conn = false;
    private Boolean rcvWare_connected = false;

    public static TextInputEditText purOrderSelection;
    public static String wom_id_for_p_rcv = "";
    RelativeLayout layoutVisibility;

    TextInputEditText purchaseRcvDate;
    TextInputEditText challanDate;
    private int mYear, mMonth, mDay;
    String p_rcv_date = "";
    String challan_date = "";
    TextView dateMissing;

    TextInputEditText challanNo;
    String challan_no = "";
    TextView challanMissing;

    TextInputEditText supplierName;
    public static String supplier_name = "";
    TextInputEditText supplierCode;
    public static String supplier_code = "";
    TextInputEditText supplierAddress;
    public static String supplier_address = "";
    TextInputEditText supplierType;
    public static String supplier_type = "";
    TextInputEditText paymentType;
    public static String payment_type = "";

    public static String ad_id_for_p_rcv = "";
    public static String aad_id_for_p_rcv = "";
    public static String p_type_id_for_p_rcv = "";
    public static String supplier_type_Id_for_p_rcv = "";
    public static String wom_type_flag_for_p_rcv = "";
    String emp_code = "";

    TextInputEditText remarks;
    String remarks_text = "";

    CheckBox replace_check;
    String replacement_flag = "";

    public static Button categorySelection;
    public static CardView prcvCatCardView;
    public static CardView prcvItemCardView;
    public static TextView catItemMissing;
    public static HorizontalScrollView catItemHsv;
    public static CardView prcvWareCardView;
    public static Button warehouseSelection;
    public static TextView warehouseSelectSugg;
    public static LinearLayout warehouseLay;

    public static ArrayList<PurchaseReceiveAllSelectedLists> purchaseReceiveAllSelectedLists;
    public static int selectedCategoryPosition = 0;
    public static String im_id_in_p_rcv = "";
    RecyclerView selectedCategoryView;
    public static PurRcvCategorySelectedAdapter purRcvCategorySelectedAdapter;
    RecyclerView.LayoutManager layoutManager;

    public static int selectedItemPosition = 0;
    public static ArrayList<PurchaseRcvItemDetailsList> purchaseRcvItemDetailsLists;
    static RecyclerView selectedItemView;
    public static PurchaseRcvItemDetailsAdapter purchaseRcvItemDetailsAdapter;
    RecyclerView.LayoutManager layoutManagerItems;
    public static ScrollView pRcvScroll;

    public static TextView totalRcvQtyPRCV;
    public static int total_rcv_qty_prcv = 0;
    public static TextView totalAmountPRCV;
    public static double total_amount_prcv = 0;
    public static TextView totalVatAmountPRCV;
    public static double total_vat_amnt_prcv = 0;

    static RecyclerView selectedWarehouseView;
    public static PurchaseRcvWarehouseRcvAdapter purchaseRcvWarehouseRcvAdapter;
    RecyclerView.LayoutManager layoutManagerWarehouse;
    public static ArrayList<PurchaseRcvWarehouseRcvLists> purchaseRcvWarehouseRcvLists;

    public static TextView totalWarehouseWiseRcvQty;
    public static int total_ware_rcv_qty = 0;

    Button save;

    String inserted_rm_id = "";
    String inserted_rm_no = "";
    int first_index_to_update = 0;
    String inserted_rj_id = "";
    int second_index_to_update = 0;
    String inserted_rd_id = "";
    ArrayList<PurchaseRcvItemDetailsList> rcvItemDetailsListsToUpdate;
    String item_rate_to_ware = "";
    String item_vat_to_ware = "";
    String item_vat_amt_to_ware = "";

    public PurchaseReceive() {
        // Required empty public constructor
    }

    static Context mContext;
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
     * @return A new instance of fragment MaterialReceive.
     */
    // TODO: Rename and change types and number of parameters
    public static PurchaseReceive newInstance(String param1, String param2) {
        PurchaseReceive fragment = new PurchaseReceive();
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
        View view = inflater.inflate(R.layout.fragment_purchase_receive, container, false);

        purOrderSelection = view.findViewById(R.id.purchase_order_search_text_for_p_rcv);
        layoutVisibility = view.findViewById(R.id.purchase_receive_details_layout);
        layoutVisibility.setVisibility(View.GONE);

        purchaseRcvDate = view.findViewById(R.id.purchase_receive_date_for_p_rcv);
        challanDate = view.findViewById(R.id.challan_date_for_p_rcv);
        dateMissing = view.findViewById(R.id.p_rcv_rcv_challan_date_missing_text);
        dateMissing.setVisibility(View.GONE);

        challanNo = view.findViewById(R.id.challan_no_for_p_rcv);
        challanMissing = view.findViewById(R.id.challan_no_missing_msg);
        challanMissing.setVisibility(View.GONE);

        supplierName = view.findViewById(R.id.supplier_name_p_rcv);
        supplierCode = view.findViewById(R.id.supplier_code_p_rcv);
        supplierAddress = view.findViewById(R.id.supplier_address_p_rcv);
        supplierType = view.findViewById(R.id.supplier_type_p_rcv);
        paymentType = view.findViewById(R.id.payment_type_p_rcv);

        remarks = view.findViewById(R.id.remarks_for_p_rcv);

        replace_check = view.findViewById(R.id.replacement_rcv_checkbox);

        categorySelection = view.findViewById(R.id.select_category_for_p_rcv);

        pRcvScroll = view.findViewById(R.id.purchase_rcv_layout_scroll);
        prcvCatCardView = view.findViewById(R.id.added_category_selection_for_p_rcv_layout);
        prcvCatCardView.setVisibility(View.GONE);
        prcvItemCardView = view.findViewById(R.id.item_selection_for_p_rcv_layout);
        prcvItemCardView.setVisibility(View.GONE);
        catItemMissing = view.findViewById(R.id.category_item_missing_for_p_rcv);
        catItemMissing.setVisibility(View.GONE);
        catItemHsv = view.findViewById(R.id.category_items_for_p_rcv_hsv);
        catItemHsv.setVisibility(View.GONE);
        prcvWareCardView = view.findViewById(R.id.added_warehouse_wise_rcv_for_p_rcv_layout);
        prcvWareCardView.setVisibility(View.GONE);

        warehouseSelection = view.findViewById(R.id.select_warehouse_for_p_rcv);
        warehouseSelectSugg = view.findViewById(R.id.no_warehouse_selected_msg);
        warehouseSelectSugg.setVisibility(View.VISIBLE);
        warehouseLay = view.findViewById(R.id.warehouse_select_layout_p_rcv);
        warehouseLay.setVisibility(View.GONE);

        totalRcvQtyPRCV = view.findViewById(R.id.category_all_item_rcv_qty_for_p_rcv);
        totalAmountPRCV = view.findViewById(R.id.category_all_item_total_amont_for_p_rcv);
        totalVatAmountPRCV = view.findViewById(R.id.category_all_item_vat_amnt_for_p_rcv);

        totalWarehouseWiseRcvQty = view.findViewById(R.id.warehouse_wise_total_rcv_qty_for_p_rcv);

        save = view.findViewById(R.id.save_purchase_receive_button);

        emp_code = userInfoLists.get(0).getUserName();

        // category adapter initialization
        selectedCategoryView = view.findViewById(R.id.added_category_for_p_rcv_view);
        purchaseReceiveAllSelectedLists = new ArrayList<>();

        selectedCategoryView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        selectedCategoryView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(selectedCategoryView.getContext(),DividerItemDecoration.VERTICAL);
        selectedCategoryView.addItemDecoration(dividerItemDecoration);

        purRcvCategorySelectedAdapter = new PurRcvCategorySelectedAdapter(purchaseReceiveAllSelectedLists,mContext,PurchaseReceive.this);
        selectedCategoryView.setAdapter(purRcvCategorySelectedAdapter);

        // Item adapter initialization
        selectedItemView = view.findViewById(R.id.added_item_for_p_rcv_view);
        purchaseRcvItemDetailsLists = new ArrayList<>();

        selectedItemView.setHasFixedSize(true);
        layoutManagerItems = new LinearLayoutManager(getContext());
        selectedItemView.setLayoutManager(layoutManagerItems);
        DividerItemDecoration dividerItemDecoration1 = new DividerItemDecoration(selectedItemView.getContext(),DividerItemDecoration.VERTICAL);
        selectedItemView.addItemDecoration(dividerItemDecoration1);

        purchaseRcvItemDetailsAdapter = new PurchaseRcvItemDetailsAdapter(purchaseRcvItemDetailsLists,mContext,PurchaseReceive.this);
        selectedItemView.setAdapter(purchaseRcvItemDetailsAdapter);

        // warehouse adapter initialization
        selectedWarehouseView = view.findViewById(R.id.added_warehouse_wise_rcv_for_p_rcv_view);
        purchaseRcvWarehouseRcvLists= new ArrayList<>();

        selectedWarehouseView.setHasFixedSize(true);
        layoutManagerWarehouse = new LinearLayoutManager(getContext());
        selectedWarehouseView.setLayoutManager(layoutManagerWarehouse);
        DividerItemDecoration dividerItemDecoration2 = new DividerItemDecoration(selectedWarehouseView.getContext(), DividerItemDecoration.VERTICAL);
        selectedWarehouseView.addItemDecoration(dividerItemDecoration2);

        purchaseRcvWarehouseRcvAdapter = new PurchaseRcvWarehouseRcvAdapter(purchaseRcvWarehouseRcvLists,getContext());
        selectedWarehouseView.setAdapter(purchaseRcvWarehouseRcvAdapter);

        // Selecting Purchase Order for Purchase Receive
        purOrderSelection.setOnClickListener(v -> {
            PurchaseOrderSelectForPRCV purchaseOrderSelectForPRCV = new PurchaseOrderSelectForPRCV(mContext);
            purchaseOrderSelectForPRCV.show(getActivity().getSupportFragmentManager(),"POPRCV");
        });
        purOrderSelection.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!wom_id_for_p_rcv.isEmpty()) {
                    supplierName.setText(supplier_name);
                    supplierCode.setText(supplier_code);
                    supplierAddress.setText(supplier_address);
                    supplierType.setText(supplier_type);
                    paymentType.setText(payment_type);
                    layoutVisibility.setVisibility(View.VISIBLE);
                }
                else {
                    layoutVisibility.setVisibility(View.GONE);
                }
            }
        });

        // Selecting Purchase Rcv Date
        purchaseRcvDate.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, (view14, year, month, dayOfMonth) -> {

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
                    purchaseRcvDate.setText(dateText);
                    p_rcv_date = purchaseRcvDate.getText().toString();
                    dateMissing.setVisibility(View.GONE);

                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        // Selecting Challan Date
        challanDate.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, (view15, year, month, dayOfMonth) -> {

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
                    challanDate.setText(dateText);
                    challan_date = challanDate.getText().toString();
                    dateMissing.setVisibility(View.GONE);

                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        // Writing Challan No
        challanNo.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    challanNo.clearFocus();
                    InputMethodManager mgr = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    closeKeyBoard();

                    return false; // consume.
                }
            }
            return false;
        });

        challanNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    challanMissing.setVisibility(View.GONE);
                }
            }
        });

        // Writing Remarks
        remarks.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    remarks.clearFocus();
                    InputMethodManager mgr = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    closeKeyBoard();

                    return false; // consume.
                }
            }
            return false;
        });

        // Selecting Category
        categorySelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryWarehouseSelectionDial categoryWarehouseSelectionDial = new CategoryWarehouseSelectionDial(mContext,1,wom_id_for_p_rcv);
                categoryWarehouseSelectionDial.show(getActivity().getSupportFragmentManager(),"CWSPRCV");
            }
        });
        categorySelection.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getCatItemDetails();
            }
        });

        // Selecting Warehouse
        warehouseSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item_id = purchaseRcvItemDetailsLists.get(selectedItemPosition).getWod_item_id();
                CategoryWarehouseSelectionDial categoryWarehouseSelectionDial = new CategoryWarehouseSelectionDial(mContext,2,item_id);
                categoryWarehouseSelectionDial.show(getActivity().getSupportFragmentManager(),"CWSPRCV");
            }
        });

        warehouseSelection.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pRcvScroll.post(new Runnable() {
                    @Override
                    public void run() {
                        View lastChild = pRcvScroll.getChildAt(pRcvScroll.getChildCount() - 1);
                        int bottom = lastChild.getBottom() + pRcvScroll.getPaddingBottom();
                        int sy = pRcvScroll.getScrollY();
                        int sh = pRcvScroll.getHeight();
                        int delta = bottom - (sy + sh);

                        pRcvScroll.smoothScrollBy(0, delta);

                    }
                });
            }
        });

        // Saving Purchase Order
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                challan_no = Objects.requireNonNull(challanNo.getText()).toString();
                remarks_text = Objects.requireNonNull(remarks.getText()).toString();
                if (replace_check.isChecked()) {
                    replacement_flag = "1";
                }
                else {
                    replacement_flag = "0";
                }
                if (!p_rcv_date.isEmpty()) {
                    if (!challan_date.isEmpty()) {
                        if (!challan_no.isEmpty()) {
                            if (purchaseReceiveAllSelectedLists.size() != 0) {
                                boolean noItemData = false;
                                boolean noRcvInCat = true;
                                boolean noRcvInWarehouse = false;
                                String im_name = "";
                                String item_name = "";
                                for (int i = 0; i < purchaseReceiveAllSelectedLists.size(); i++) {
                                    im_name = purchaseReceiveAllSelectedLists.get(i).getCat_name();
                                    ArrayList<PurchaseRcvItemDetailsList> pppiii = purchaseReceiveAllSelectedLists.get(i).getPurchaseRcvItemDetailsLists();
                                    if (pppiii.size() == 0) {
                                        noItemData = true;
                                        break;
                                    }
                                    else {
                                        for (int j = 0; j < pppiii.size(); j++) {
                                            String rcv_qty = pppiii.get(j).getRcv_qty();
                                            if (!rcv_qty.isEmpty()) {
                                                if (!rcv_qty.equals("0")) {
                                                    pppiii.get(j).setNeedToUpdate(true);
                                                    noRcvInCat = false;
                                                    ArrayList<PurchaseRcvWarehouseRcvLists> wwwrrrr = pppiii.get(j).getPurchaseRcvWarehouseRcvLists();
                                                    for (int k = 0; k < wwwrrrr.size(); k++) {
                                                        if (wwwrrrr.get(k).getRcv_qty().isEmpty() || wwwrrrr.get(k).getRcv_qty().equals("0")) {
                                                            item_name = pppiii.get(j).getItem();
                                                            noRcvInWarehouse = true;
                                                            break;
                                                        }
                                                    }
                                                }
                                                else {
                                                    pppiii.get(j).setNeedToUpdate(false);
                                                }
                                            }
                                            else {
                                                pppiii.get(j).setNeedToUpdate(false);
                                            }
                                        }
                                        purchaseReceiveAllSelectedLists.get(i).setPurchaseRcvItemDetailsLists(pppiii);
                                        if (noRcvInCat) {
                                            break;
                                        }
                                    }

//                                    for (int j = 0; j < pppiii.size(); j++) {
//                                        System.out.println("ITEM NAME: " + pppiii.get(j).getItem());
//                                        System.out.println("ITEM WISE RCV QTY: "+pppiii.get(j).getRcv_qty());
//                                        System.out.println("ITEM NEED TO UPDATE: "+pppiii.get(j).isNeedToUpdate());
//
//                                        ArrayList<PurchaseRcvWarehouseRcvLists> wwwrrrr = pppiii.get(j).getPurchaseRcvWarehouseRcvLists();
//
//                                        for (int k = 0; k < wwwrrrr.size(); k++) {
//                                            System.out.println("WareHouse NAME: " + wwwrrrr.get(k).getWh_rack_name());
//                                            System.out.println("WareHouse WISE RCV QTY: "+wwwrrrr.get(k).getRcv_qty());
//                                        }
//
//                                    }

                                }
                                if (!noItemData) {
                                    if (!noRcvInCat) {
                                        if (!noRcvInWarehouse) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                            builder.setTitle("SAVE PURCHASE RECEIVE!")
                                                    .setMessage("Do you want to save this receive?")
                                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                            savePurchaseReceive();
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
                                            Toast.makeText(getContext(), "There is an empty Warehouse added in "+item_name+"'s Receive. Please remove this empty Warehouse.",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else {
                                        Toast.makeText(getContext(), "There is no Receive quantity added in "+im_name+" category. Please remove this category.",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(getContext(), "There is no Item in "+im_name+" category. Please remove this category.",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(getContext(), "Please Select Category to Receive",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            challanMissing.setVisibility(View.VISIBLE);
                            pRcvScroll.fullScroll(ScrollView.FOCUS_UP);
                        }
                    }
                    else {
                        dateMissing.setVisibility(View.VISIBLE);
                        pRcvScroll.fullScroll(ScrollView.FOCUS_UP);
                    }
                }
                else {
                    dateMissing.setVisibility(View.VISIBLE);
                    pRcvScroll.fullScroll(ScrollView.FOCUS_UP);
                }
            }
        });

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

    @Override
    public void onCatClicked(int Position) {
        selectedCategoryPosition = Position;
        selectedItemPosition = 0;
        im_id_in_p_rcv = purchaseReceiveAllSelectedLists.get(Position).getCat_id();
        System.out.println(im_id_in_p_rcv);
        catItemUpdate(im_id_in_p_rcv);
        purRcvCategorySelectedAdapter.notifyDataSetChanged();
    }

    // getting all data
    public void getCatItemDetails() {
        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;
        prcvItemCardView.setVisibility(View.GONE);
        prcvWareCardView.setVisibility(View.GONE);
        catItemHsv.setVisibility(View.GONE);
        catItemMissing.setVisibility(View.GONE);
        total_rcv_qty_prcv = 0;
        total_amount_prcv = 0;
        total_vat_amnt_prcv = 0;
        ArrayList<PurchaseRcvItemDetailsList> PurchaseRcvItemDetailsList = new ArrayList<>();
        String url = "http://103.56.208.123:8001/apex/tterp/purchaseReceive/getItemDetails?wom_id="+wom_id_for_p_rcv+"&im_id="+im_id_in_p_rcv+"&rj_id=&clcm_id=";
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

                        String size_id = info.getString("size_id")
                                .equals("null") ? "" : info.getString("size_id");
                        String size_name = info.getString("size_name")
                                .equals("null") ? "" : info.getString("size_name");
                        String item = info.getString("item")
                                .equals("null") ? "" : info.getString("item");
                        String color_id = info.getString("color_id")
                                .equals("null") ? "" : info.getString("color_id");
                        String color_name = info.getString("color_name")
                                .equals("null") ? "" : info.getString("color_name");
                        String wod_qty = info.getString("wod_qty")
                                .equals("null") ? "" : info.getString("wod_qty");
                        String wod_item_id = info.getString("wod_item_id")
                                .equals("null") ? "" : info.getString("wod_item_id");
                        String balance_qty = info.getString("balance_qty")
                                .equals("null") ? "" : info.getString("balance_qty");
                        String wod_rate = info.getString("wod_rate")
                                .equals("null") ? "" : info.getString("wod_rate");
                        String wod_actual_rate = info.getString("wod_actual_rate")
                                .equals("null") ? "" : info.getString("wod_actual_rate");
                        String wod_vat_pct = info.getString("wod_vat_pct")
                                .equals("null") ? "" : info.getString("wod_vat_pct");
                        String wod_vat_pct_amt = info.getString("wod_vat_pct_amt")
                                .equals("null") ? "" : info.getString("wod_vat_pct_amt");
                        String wod_id = info.getString("wod_id")
                                .equals("null") ? "" : info.getString("wod_id");
                        String item_unit = info.getString("item_unit")
                                .equals("null") ? "" : info.getString("item_unit");
                        String item_code = info.getString("item_code")
                                .equals("null") ? "" : info.getString("item_code");
                        String item_hs_code = info.getString("item_hs_code")
                                .equals("null") ? "" : info.getString("item_hs_code");
                        String item_part_number = info.getString("item_part_number")
                                .equals("null") ? "" : info.getString("item_part_number");


//                        int bal_qty = Integer.parseInt(balance_qty);
//                        double unit_price = Double.parseDouble(costprice);
//                        double item_per_amount = bal_qty * unit_price;
//
//                        double vat_amnt = (0.0 *  unit_price) / 100;
//
//                        double total_vat_per_item_amnt = vat_amnt *  bal_qty;
//                        DecimalFormat decimalFormat = new DecimalFormat("#.#");


                        PurchaseRcvItemDetailsList.add(new PurchaseRcvItemDetailsList(size_id,size_name,item,color_id,color_name,
                                wod_qty,wod_item_id,"0",balance_qty,balance_qty,wod_rate,wod_actual_rate,wod_vat_pct,wod_vat_pct_amt,
                                wod_id,item_unit,item_code,item_hs_code,item_part_number,"0","0",
                                new ArrayList<>(), false,false));

                    }
                }
                for (int i = 0; i < purchaseReceiveAllSelectedLists.size(); i++) {
                    if (im_id_in_p_rcv.equals(purchaseReceiveAllSelectedLists.get(i).getCat_id())) {
                        purchaseReceiveAllSelectedLists.get(i).setPurchaseRcvItemDetailsLists(PurchaseRcvItemDetailsList);
                    }
                }

                connected = true;
                updateItemDetails();

            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateItemDetails();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateItemDetails();
        });

        requestQueue.add(stringRequest);
    }

    private void updateItemDetails() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;
                selectedItemPosition = 0;
                catItemUpdate(im_id_in_p_rcv);
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

                    getCatItemDetails();
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

                getCatItemDetails();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }

    public static void catItemUpdate(String new_im_id) {
        ArrayList<PurchaseRcvItemDetailsList> lis = new ArrayList<>();
        for (int i = 0; i < purchaseReceiveAllSelectedLists.size(); i++) {
            if (new_im_id.equals(purchaseReceiveAllSelectedLists.get(i).getCat_id())) {
                lis = purchaseReceiveAllSelectedLists.get(i).getPurchaseRcvItemDetailsLists();
            }
        }
        prcvItemCardView.setVisibility(View.VISIBLE);
        if (lis.size() == 0) {
            catItemMissing.setVisibility(View.VISIBLE);
            catItemHsv.setVisibility(View.GONE);
            prcvWareCardView.setVisibility(View.GONE);
            pRcvScroll.post(new Runnable() {
                @Override
                public void run() {
                    View lastChild = pRcvScroll.getChildAt(pRcvScroll.getChildCount() - 1);
                    int bottom = lastChild.getBottom() + pRcvScroll.getPaddingBottom();
                    int sy = pRcvScroll.getScrollY();
                    int sh = pRcvScroll.getHeight();
                    int delta = bottom - (sy + sh);

                    pRcvScroll.smoothScrollBy(0, delta);

                }
            });
        }
        else {
            purchaseRcvItemDetailsLists.clear();
            purchaseRcvItemDetailsLists.addAll(lis);
            catItemMissing.setVisibility(View.GONE);
            catItemHsv.setVisibility(View.VISIBLE);
            purchaseRcvItemDetailsAdapter.notifyDataSetChanged();
            total_rcv_qty_prcv = 0;
            total_amount_prcv = 0.0;
            total_vat_amnt_prcv = 0.0;
            for (int i = 0; i < purchaseRcvItemDetailsLists.size(); i++) {
                if (!purchaseRcvItemDetailsLists.get(i).getRcv_qty().isEmpty()) {
                    total_rcv_qty_prcv = total_rcv_qty_prcv + Integer.parseInt(purchaseRcvItemDetailsLists.get(i).getRcv_qty());
                }
                if (!purchaseRcvItemDetailsLists.get(i).getItem_wise_rcv_total_amnt().isEmpty()) {
                    total_amount_prcv = total_amount_prcv + Double.parseDouble(purchaseRcvItemDetailsLists.get(i).getItem_wise_rcv_total_amnt());
                }
                if (!purchaseRcvItemDetailsLists.get(i).getItem_wise_rcv_total_vat().isEmpty()) {
                    total_vat_amnt_prcv = total_vat_amnt_prcv + Double.parseDouble(purchaseRcvItemDetailsLists.get(i).getItem_wise_rcv_total_vat());
                }
            }

            DecimalFormat decimalFormat = new DecimalFormat("#.#");

            totalRcvQtyPRCV.setText(String.valueOf(total_rcv_qty_prcv));
            totalAmountPRCV.setText(decimalFormat.format(total_amount_prcv));
            totalVatAmountPRCV.setText(decimalFormat.format(total_vat_amnt_prcv));

            System.out.println("ITEM POS: "+selectedItemPosition);
            System.out.println("ITEM SIZE: "+purchaseRcvItemDetailsLists.size());
            String item_id = lis.get(selectedItemPosition).getWod_item_id();
            System.out.println("ITEM ID: "+item_id);
            wareItemUpdate(item_id);
        }

    }

    @Override
    public void onItemClicked(int Position) {
        selectedItemPosition = Position;
        String id = purchaseRcvItemDetailsLists.get(Position).getWod_item_id();
        System.out.println("Clicked ITEM POS: "+selectedItemPosition);
        System.out.println("Clicked ITEM SIZE: "+purchaseRcvItemDetailsLists.size());
        System.out.println("Clicked ITEM ID: "+id);
        wareItemUpdate(id);
        purchaseRcvItemDetailsAdapter.notifyDataSetChanged();
    }

    public static void wareItemUpdate(String new_item_id) {
        ArrayList<PurchaseRcvWarehouseRcvLists> lis = new ArrayList<>();
        for (int i = 0; i < purchaseRcvItemDetailsLists.size(); i++) {
            if (new_item_id.equals(purchaseRcvItemDetailsLists.get(i).getWod_item_id())) {
                lis = purchaseRcvItemDetailsLists.get(i).getPurchaseRcvWarehouseRcvLists();
            }
        }
        prcvWareCardView.setVisibility(View.VISIBLE);
        total_ware_rcv_qty = 0;
        if (lis.size() == 0) {
            warehouseSelectSugg.setVisibility(View.VISIBLE);
            warehouseLay.setVisibility(View.GONE);
            purchaseRcvWarehouseRcvLists.clear();
        }
        else {
            warehouseSelectSugg.setVisibility(View.GONE);
            warehouseLay.setVisibility(View.VISIBLE);
            purchaseRcvWarehouseRcvLists.clear();
            purchaseRcvWarehouseRcvLists.addAll(lis);


            for (int i = 0; i < purchaseRcvWarehouseRcvLists.size(); i++) {
                if (!purchaseRcvWarehouseRcvLists.get(i).getRcv_qty().isEmpty()) {
                    total_ware_rcv_qty = total_ware_rcv_qty + Integer.parseInt(purchaseRcvWarehouseRcvLists.get(i).getRcv_qty());
                }
            }


        }
        purchaseRcvWarehouseRcvAdapter.notifyDataSetChanged();
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        totalWarehouseWiseRcvQty.setText(decimalFormat.format(total_ware_rcv_qty));

        pRcvScroll.post(new Runnable() {
            @Override
            public void run() {
                View lastChild = pRcvScroll.getChildAt(pRcvScroll.getChildCount() - 1);
                int bottom = lastChild.getBottom() + pRcvScroll.getPaddingBottom();
                int sy = pRcvScroll.getScrollY();
                int sh = pRcvScroll.getHeight();
                int delta = bottom - (sy + sh);

                pRcvScroll.smoothScrollBy(0, delta);

            }
        });
    }

    // purchase receive process
    public void savePurchaseReceive() {
        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        master_conn = false;
        master_connected = false;

        String url = "http://103.56.208.123:8001/apex/tterp/purchaseReceive/insertReceiveMst";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            master_conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                String out_rm_id = jsonObject.getString("out_rm_id").equals("null") ? "" : jsonObject.getString("out_rm_id");
                String out_rm_no = jsonObject.getString("out_rm_no");
                if (string_out.equals("Successfully Created")) {
                    if (!out_rm_id.isEmpty()) {
                        inserted_rm_no = out_rm_no;
                        inserted_rm_id = out_rm_id;
                        System.out.println("NEW RM ID: "+ inserted_rm_id);
                        master_connected = true;
                        checkToSaveReceiveJobData();
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
                headers.put("P_AAD_ID",aad_id_for_p_rcv);
                headers.put("P_AD_ID",ad_id_for_p_rcv);
                headers.put("P_CHALLAN_DATE",challan_date);
                headers.put("P_CHALLAN_NO",challan_no);
                headers.put("P_PAYMENT_TYPE",p_type_id_for_p_rcv);
                headers.put("P_REMARKS",remarks_text);
                headers.put("P_REPLACE_FLAG",replacement_flag);
                headers.put("P_RM_DATE",p_rcv_date);
                headers.put("P_RM_WOM_ID",wom_id_for_p_rcv);
                headers.put("P_SUPPLIER_TYPE",supplier_type_Id_for_p_rcv);
                headers.put("P_USER",emp_code);
                headers.put("P_WOM_TYPE_FLAG",wom_type_flag_for_p_rcv);

                return headers;
            }
        };

        requestQueue.add(request);
    }

    public void checkToSaveReceiveJobData() {
        if (purchaseReceiveAllSelectedLists.size() != 0) {
            boolean allUpdated = false;
            for (int i = 0; i < purchaseReceiveAllSelectedLists.size(); i++) {
                allUpdated = purchaseReceiveAllSelectedLists.get(i).isUpdated();
                if (!purchaseReceiveAllSelectedLists.get(i).isUpdated()) {
                    allUpdated = purchaseReceiveAllSelectedLists.get(i).isUpdated();
                    String im_id = purchaseReceiveAllSelectedLists.get(i).getCat_id();
                    first_index_to_update = i;
                    saveRcvJobData(im_id);
                    break;
                }
            }
            if (allUpdated) {
                rcvJob_conn = true;
                rcvJob_connected = true;
                updateLayout();
            }
        }
        else {
            rcvJob_conn = true;
            rcvJob_connected = true;
            rcvDtl_conn = true;
            rcvDtl_connected = true;
            rcvWare_conn = true;
            rcvWare_connected = true;
            updateLayout();
        }
    }

    public void saveRcvJobData(String im_id) {
        rcvJob_conn = false;
        rcvJob_connected = false;
        String url = "http://103.56.208.123:8001/apex/tterp/purchaseReceive/insertRcvJob";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            rcvJob_conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                String out_rj_id = jsonObject.getString("out_rj_id").equals("null") ? "" : jsonObject.getString("out_rj_id");
                if (string_out.equals("Successfully Created")) {
                    if (!out_rj_id.isEmpty()) {
                        inserted_rj_id = out_rj_id;
                        System.out.println("NEW RJ ID: "+ inserted_rj_id);
                        rcvJob_connected = true;
                        checkToSaveRcvDtlData();
                    }
                    else {
                        rcvJob_connected = false;
                        updateLayout();
                    }
                }
                else {
                    rcvJob_connected = false;
                    updateLayout();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                rcvJob_connected = false;
                updateLayout();
            }
        }, error -> {
            error.printStackTrace();
            rcvJob_conn = false;
            rcvJob_connected = false;
            updateLayout();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_IM_ID",im_id);
                headers.put("P_RM_ID",inserted_rm_id);
                return headers;
            }
        };

        requestQueue.add(request);
    }

    private void checkToSaveRcvDtlData() {
        rcvItemDetailsListsToUpdate = new ArrayList<>();
        rcvItemDetailsListsToUpdate = purchaseReceiveAllSelectedLists.get(first_index_to_update).getPurchaseRcvItemDetailsLists();
        if (rcvItemDetailsListsToUpdate.size() != 0) {
            boolean allUpdated = false;
            for (int i = 0; i < rcvItemDetailsListsToUpdate.size(); i++) {
                if (rcvItemDetailsListsToUpdate.get(i).isNeedToUpdate()) {
                    allUpdated = rcvItemDetailsListsToUpdate.get(i).isUpdated();
                    if (!rcvItemDetailsListsToUpdate.get(i).isUpdated()) {
                        allUpdated = rcvItemDetailsListsToUpdate.get(i).isUpdated();
                        String item_id = rcvItemDetailsListsToUpdate.get(i).getWod_item_id();
                        String color_id = rcvItemDetailsListsToUpdate.get(i).getColor_id();
                        String item_rate = rcvItemDetailsListsToUpdate.get(i).getWod_rate();
                        if (color_id.isEmpty()) {
                            color_id = "1";
                        }
                        String size_id = rcvItemDetailsListsToUpdate.get(i).getSize_id();
                        if (size_id.isEmpty()) {
                            size_id = "1";
                        }
                        String wod_id = rcvItemDetailsListsToUpdate.get(i).getWod_id();
                        String vat_pct = rcvItemDetailsListsToUpdate.get(i).getWod_vat_pct();
                        String vat_pct_amnt = rcvItemDetailsListsToUpdate.get(i).getWod_vat_pct_amt();

                        second_index_to_update = i;
                        saveRcvDtlData(color_id, item_id,item_rate,size_id,vat_pct,vat_pct_amnt,wod_id);
                        break;
                    }
                }
            }
            if (allUpdated) {
                rcvDtl_conn = true;
                rcvDtl_connected = true;
                purchaseReceiveAllSelectedLists.get(first_index_to_update).setUpdated(true);
                checkToSaveReceiveJobData();
            }
        }
        else {
            rcvDtl_connected = true;
            rcvDtl_conn = true;
            purchaseReceiveAllSelectedLists.get(first_index_to_update).setUpdated(true);
            checkToSaveReceiveJobData();
        }
    }

    public void saveRcvDtlData(String color_id, String item_id, String item_rate, String size_id, String vat_pct,
                               String vat_pct_amnt, String wod_id) {
        rcvDtl_conn = false;
        rcvDtl_connected = false;
        String url = "http://103.56.208.123:8001/apex/tterp/purchaseReceive/insertRcvDtl";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            rcvDtl_conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                String out_rd_id = jsonObject.getString("out_rd_id").equals("null") ? "" : jsonObject.getString("out_rd_id");
                if (string_out.equals("Successfully Created")) {
                    if (!out_rd_id.isEmpty()) {
                        inserted_rd_id = out_rd_id;
                        System.out.println("NEW RD ID: "+ inserted_rd_id);
                        rcvDtl_connected = true;
                        item_rate_to_ware = item_rate;
                        item_vat_to_ware = vat_pct;
                        item_vat_amt_to_ware = vat_pct_amnt;
                        checkToSaveRcvWarehouseData();
                    }
                    else {
                        rcvDtl_connected = false;
                        updateLayout();
                    }
                }
                else {
                    rcvDtl_connected = false;
                    updateLayout();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                rcvDtl_connected = false;
                updateLayout();
            }
        }, error -> {
            error.printStackTrace();
            rcvDtl_conn = false;
            rcvDtl_connected = false;
            updateLayout();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_COLOR_ID",color_id);
                headers.put("P_ITEM_ID",item_id);
                headers.put("P_RATE",item_rate);
                headers.put("P_RJ_ID",inserted_rj_id);
                headers.put("P_RM_DATE",p_rcv_date);
                headers.put("P_SIZE_ID",size_id);
                headers.put("P_VAT_PCT",vat_pct);
                headers.put("P_VAT_PCT_AMT",vat_pct_amnt);
                headers.put("P_WOD_ID",wod_id);

                return headers;
            }
        };

        requestQueue.add(request);
    }

    private void checkToSaveRcvWarehouseData() {
        ArrayList<PurchaseRcvWarehouseRcvLists> purchaseRcvWarehouseRcvLists1 = rcvItemDetailsListsToUpdate.get(second_index_to_update).getPurchaseRcvWarehouseRcvLists();
        if (purchaseRcvWarehouseRcvLists1.size() != 0) {
            boolean allUpdated = false;
            for (int i = 0; i < purchaseRcvWarehouseRcvLists1.size(); i++) {
                allUpdated = purchaseRcvWarehouseRcvLists1.get(i).isUpdated();
                if (!purchaseRcvWarehouseRcvLists1.get(i).isUpdated()) {
                    allUpdated = purchaseRcvWarehouseRcvLists1.get(i).isUpdated();

                    String whm_id = purchaseRcvWarehouseRcvLists1.get(i).getWhm_id();
                    String whd_id = purchaseRcvWarehouseRcvLists1.get(i).getWhd_id();
                    String qty = purchaseRcvWarehouseRcvLists1.get(i).getRcv_qty();

                    saveWarehouseData(purchaseRcvWarehouseRcvLists1,whm_id, whd_id,qty,i);
                    break;
                }
            }
            if (allUpdated) {
                rcvWare_conn = true;
                rcvWare_connected = true;
                rcvItemDetailsListsToUpdate.get(second_index_to_update).setUpdated(true);
                checkToSaveRcvDtlData();
            }
        }
        else {
            rcvWare_conn = true;
            rcvWare_connected = true;
            rcvItemDetailsListsToUpdate.get(second_index_to_update).setUpdated(true);
            checkToSaveRcvDtlData();
        }
    }

    public void saveWarehouseData(ArrayList<PurchaseRcvWarehouseRcvLists> purchaseRcvWarehouseRcvLists1, String whm_id, String whd_id, String qty, int lastIndex) {
        rcvWare_conn = false;
        rcvWare_connected = false;
        String url = "http://103.56.208.123:8001/apex/tterp/purchaseReceive/insertRcvDtlWarehouse";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            rcvWare_conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                if (string_out.equals("Successfully Created")) {
                    purchaseRcvWarehouseRcvLists1.get(lastIndex).setUpdated(true);
                    rcvWare_connected = true;
                    checkToSaveRcvWarehouseData();
                }
                else {
                    rcvWare_connected = false;
                    updateLayout();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                rcvWare_connected = false;
                updateLayout();
            }
        }, error -> {
            error.printStackTrace();
            rcvWare_conn = false;
            rcvWare_connected = false;
            updateLayout();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_QTY",qty);
                headers.put("P_RD_ID",inserted_rd_id);
                headers.put("P_RWD_RATE",item_rate_to_ware);
                headers.put("P_VAT_PCT",item_vat_to_ware);
                headers.put("P_VAT_PCT_AMT",item_vat_amt_to_ware);
                headers.put("P_WHD_ID",whd_id);
                headers.put("P_WHM_ID",whm_id);
                return headers;
            }
        };

        requestQueue.add(request);
    }

    private void updateLayout() {
        waitProgress.dismiss();
        if (master_conn) {
            if (master_connected) {
                if (rcvJob_conn) {
                    if (rcvJob_connected) {
                        if (rcvDtl_conn) {
                            if (rcvDtl_connected) {
                                if (rcvWare_conn) {
                                    if (rcvWare_connected) {
                                        master_conn =false;
                                        master_connected = false;
                                        rcvJob_conn = false;
                                        rcvJob_connected = false;
                                        rcvDtl_conn = false;
                                        rcvDtl_connected = false;
                                        rcvWare_conn = false;
                                        rcvWare_connected = false;

                                        AlertDialog dialog = new AlertDialog.Builder(mContext)
                                                .setMessage("New Purchase Receive Saved Successfully. Your New Purchase Receive No is: "+inserted_rm_no)
                                                .setPositiveButton("Ok", null)
                                                .show();

                                        dialog.setCancelable(false);
                                        dialog.setCanceledOnTouchOutside(false);
                                        Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                        positive.setOnClickListener(v -> {

                                            MainMenu.dashboardFront = false;
                                            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,new PurchaseReceive()).commit();
                                            dialog.dismiss();
                                        });
                                    }
                                    else {
                                        AlertDialog dialog = new AlertDialog.Builder(mContext)
                                                .setMessage("There is a network issue in the server. Please retry or data will be lost.")
                                                .setPositiveButton("Retry", null)
                                                .setNegativeButton("Cancel", null)
                                                .show();

                                        dialog.setCancelable(false);
                                        dialog.setCanceledOnTouchOutside(false);
                                        Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                        positive.setOnClickListener(v -> {

                                            waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
                                            waitProgress.setCancelable(false);
                                            checkToSaveRcvWarehouseData();
                                            dialog.dismiss();
                                        });

                                        Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                                        negative.setOnClickListener(v -> dialog.dismiss());
                                    }
                                }
                                else {
                                    AlertDialog dialog = new AlertDialog.Builder(mContext)
                                            .setMessage("Failed to save Warehouse Receive Data for Purchase Receive. Please retry or data will be lost.")
                                            .setPositiveButton("Retry", null)
                                            .setNegativeButton("Cancel", null)
                                            .show();

                                    dialog.setCancelable(false);
                                    dialog.setCanceledOnTouchOutside(false);
                                    Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                    positive.setOnClickListener(v -> {

                                        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
                                        waitProgress.setCancelable(false);
                                        checkToSaveRcvWarehouseData();
                                        dialog.dismiss();
                                    });

                                    Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                                    negative.setOnClickListener(v -> dialog.dismiss());
                                }
                            }
                            else {
                                AlertDialog dialog = new AlertDialog.Builder(mContext)
                                        .setMessage("There is a network issue in the server. Please retry or data will be lost.")
                                        .setPositiveButton("Retry", null)
                                        .setNegativeButton("Cancel", null)
                                        .show();

                                dialog.setCancelable(false);
                                dialog.setCanceledOnTouchOutside(false);
                                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                positive.setOnClickListener(v -> {

                                    waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
                                    waitProgress.setCancelable(false);
                                    checkToSaveRcvDtlData();
                                    dialog.dismiss();
                                });

                                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                                negative.setOnClickListener(v -> dialog.dismiss());
                            }
                        }
                        else {
                            AlertDialog dialog = new AlertDialog.Builder(mContext)
                                    .setMessage("Failed to save Item Data for Purchase Receive. Please retry or data will be lost.")
                                    .setPositiveButton("Retry", null)
                                    .setNegativeButton("Cancel", null)
                                    .show();

                            dialog.setCancelable(false);
                            dialog.setCanceledOnTouchOutside(false);
                            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                            positive.setOnClickListener(v -> {

                                waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
                                waitProgress.setCancelable(false);
                                checkToSaveRcvDtlData();
                                dialog.dismiss();
                            });

                            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                            negative.setOnClickListener(v -> dialog.dismiss());
                        }
                    }
                    else {
                        AlertDialog dialog = new AlertDialog.Builder(mContext)
                                .setMessage("There is a network issue in the server. Please retry or data will be lost.")
                                .setPositiveButton("Retry", null)
                                .setNegativeButton("Cancel", null)
                                .show();

                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        positive.setOnClickListener(v -> {

                            waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
                            waitProgress.setCancelable(false);
                            checkToSaveReceiveJobData();
                            dialog.dismiss();
                        });

                        Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                        negative.setOnClickListener(v -> dialog.dismiss());
                    }
                }
                else {
                    AlertDialog dialog = new AlertDialog.Builder(mContext)
                            .setMessage("Failed to save Category Data for Purchase Receive. Please retry or data will be lost.")
                            .setPositiveButton("Retry", null)
                            .setNegativeButton("Cancel", null)
                            .show();

                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positive.setOnClickListener(v -> {

                        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
                        waitProgress.setCancelable(false);
                        checkToSaveReceiveJobData();
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

                    savePurchaseReceive();
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

                savePurchaseReceive();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }
}
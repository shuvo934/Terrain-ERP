package ttit.com.shuvo.terrainerp.fragments;

import static ttit.com.shuvo.terrainerp.login.Login.userInfoLists;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.WaitProgress;
import ttit.com.shuvo.terrainerp.adapters.PurchaseOrderReqSelectedAdapter;
import ttit.com.shuvo.terrainerp.adapters.PurchaseOrderSelectedItemAdpater;
import ttit.com.shuvo.terrainerp.arrayList.DesignatedUserList;
import ttit.com.shuvo.terrainerp.arrayList.PurchaseOrderItemDetailsLists;
import ttit.com.shuvo.terrainerp.arrayList.PurchaseOrderSelectedRequisitionLists;
import ttit.com.shuvo.terrainerp.arrayList.ReceiveTypeList;
import ttit.com.shuvo.terrainerp.arrayList.SupplierList;
import ttit.com.shuvo.terrainerp.dialogues.DesignatedUserSelectDial;
import ttit.com.shuvo.terrainerp.dialogues.PurchaseOrderSelectDial;
import ttit.com.shuvo.terrainerp.dialogues.PurchaseReqSelectionForPODial;
import ttit.com.shuvo.terrainerp.dialogues.SupplierDialogue;
import ttit.com.shuvo.terrainerp.mainBoard.MainMenu;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PurchaseOrder#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PurchaseOrder extends Fragment implements PurchaseOrderReqSelectedAdapter.ClickedItem {

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
    private Boolean woj_conn = false;
    private Boolean master_connected = false;
    private Boolean woj_connected = false;
    private Boolean wod_conn = false;
    private Boolean wod_connected = false;

    public static TextInputEditText purOrderToUpdateSelectSpinner;
    public static String wom_id_to_update = "";
    public static String po_no_to_show = "";

    TextInputLayout poDateLay;
    TextInputEditText poDate;
    TextInputLayout expDelDateLay;
    TextInputEditText expDeliveryDate;
    private int mYear, mMonth, mDay;
    String po_date = "";
    String exp_del_date = "";
    TextView dateMissing;

    TextInputLayout woPoTypeLay;
    AmazingSpinner woPoType;
    TextView woPoTypeMissing;
    String selected_wo_po = "";

    public static TextInputLayout supplierPOLay;
    public static TextInputEditText supplierPO;
    TextInputEditText supplierCode;
    TextInputEditText supplierAddress;
    TextView supplierMissing;
    TextInputLayout contactPersonLay;
    AmazingSpinner contactPerson;
    TextView contactPersonMissing;
    public static boolean contact_person_needed = false;
    public static int fromforPO_supp = 0;
    public static String selected_ad_id_for_po = "";
    public static String supplier_code = "";
    public static String supplier_address = "";
    String supplier_name = "";
    String aad_id = "";
    String contact_person_name = "";
    public static ArrayList<SupplierList> supplierforPOLists;
    ArrayList<ReceiveTypeList> contactPersonLists;

    TextInputLayout supplierTypeLay;
    AmazingSpinner supplierType;
    String supplier_type_Id = "";
    TextInputLayout paymentTypeLay;
    AmazingSpinner paymentType;
    String payment_type_Id = "";
    ArrayList<ReceiveTypeList> supplierTypeLists;
    ArrayList<ReceiveTypeList> paymentTypeLists;

    TextInputLayout designatedUserLay;
    public static TextInputEditText designatedUser;
    TextView designatedUserMissing;
    public static String designated_user_id = "";
    public static String designated_user_name = "";
    ArrayList<DesignatedUserList> designatedUserLists;

    TextInputLayout deliveryAddressLay;
    AppCompatAutoCompleteTextView deliveryAddress;
    TextView delAddMissing;
    String del_add_id = "";
    String del_address = "";

    TextInputLayout billingAddressLay;
    AppCompatAutoCompleteTextView billingAddress;
    TextView billAddMissing;
    String bill_add_id = "";
    String bill_address = "";
    ArrayList<ReceiveTypeList> deliveryAddressLists;
    ArrayList<ReceiveTypeList> billingAddressLists;

    TextInputLayout remarksLay;
    AppCompatAutoCompleteTextView remarks;
    String remarks_text = "";
    TextView remarksMissing;
    ArrayList<String> remarksLists;

    public static Button purchaseReqSelection;
    public static CardView prCardView;
    public static CardView itemCardView;
    public static TextView reqItemMissing;
    public static HorizontalScrollView reqItemHsv;

    public static ArrayList<PurchaseOrderSelectedRequisitionLists> purchaseOrderSelectedRequisitionLists;
    public static int selectedRequisition = 0;
    RecyclerView selectedReqView;
    public static PurchaseOrderReqSelectedAdapter purchaseOrderReqSelectedAdapter;
    RecyclerView.LayoutManager layoutManager;

    static RecyclerView selectedItemsView;
    public static PurchaseOrderSelectedItemAdpater purchaseOrderSelectedItemAdpater;
    RecyclerView.LayoutManager layoutManagerItems;
    public static String prm_id_in_po = "";
    public static String im_id_in_po = "";
    public static ScrollView poScroll;

    public static TextView totalOrderQtyPO;
    public static int total_order_qty_po = 0;
    public static TextView totalAmountPO;
    public static double total_amount_po = 0;
    public static TextView totalVatAmountPO;
    public static double total_vat_amnt_po = 0;

    Button save;
    Button update;

    String emp_code = "";
    String inserter_wom_no = "";
    String inserted_wom_id = "";
    String inserted_woj_id = "";
    int first_index_to_update = 0;

    public PurchaseOrder() {
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
     * @return A new instance of fragment PurchaseOrder.
     */
    // TODO: Rename and change types and number of parameters
    public static PurchaseOrder newInstance(String param1, String param2) {
        PurchaseOrder fragment = new PurchaseOrder();
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
        View view = inflater.inflate(R.layout.fragment_purchase_order, container, false);

        AppCompatActivity activity = (AppCompatActivity) mContext;
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        purOrderToUpdateSelectSpinner = view.findViewById(R.id.purchase_order_search_text_for_po);

        poDateLay = view.findViewById(R.id.purchase_order_date_for_po_layout);
        poDate = view.findViewById(R.id.purchase_order_date_for_po);
        expDelDateLay = view.findViewById(R.id.expected_delivery_date_for_po_layout);
        expDeliveryDate = view.findViewById(R.id.expected_delivery_date_for_po);
        dateMissing = view.findViewById(R.id.po_del_date_missing_text);
        dateMissing.setVisibility(View.GONE);
        woPoTypeLay = view.findViewById(R.id.spinner_layout_wo_po_type_for_po);
        woPoType = view.findViewById(R.id.wo_po_type_spinner_for_po);
        woPoTypeMissing = view.findViewById(R.id.wo_po_type_for_po_missing_msg);
        woPoTypeMissing.setVisibility(View.GONE);

        save = view.findViewById(R.id.save_purchase_order_button);
        save.setVisibility(View.VISIBLE);
        update = view.findViewById(R.id.update_purchase_order_button);
        update.setVisibility(View.GONE);

        supplierPO = view.findViewById(R.id.select_supplier_for_po);
        supplierPOLay = view.findViewById(R.id.select_supplier_layout_for_po);
        supplierMissing = view.findViewById(R.id.supplier_name_for_po_missing_msg);
        supplierMissing.setVisibility(View.GONE);
        supplierCode = view.findViewById(R.id.supplier_code_for_po);
        supplierAddress = view.findViewById(R.id.supplier_address_for_po);
        contactPersonLay = view.findViewById(R.id.spinner_layout_contact_person_for_po);
        contactPerson = view.findViewById(R.id.contact_person_for_po_spinner);
        contactPersonLay.setEnabled(false);
        contactPersonMissing = view.findViewById(R.id.contact_person_for_po_missing_msg);
        contactPersonMissing.setVisibility(View.GONE);

        supplierTypeLay = view.findViewById(R.id.supplier_type_for_po_spinner_layout);
        supplierType = view.findViewById(R.id.supplier_type_for_po_spinner);
        paymentTypeLay = view.findViewById(R.id.payment_type_for_po_spinner_layout);
        paymentType = view.findViewById(R.id.payment_type_for_po_spinner);

        designatedUserLay = view.findViewById(R.id.designated_user_for_po_spinner_layout);
        designatedUser = view.findViewById(R.id.designated_user_for_po_spinner);
        designatedUserMissing = view.findViewById(R.id.designated_user_for_po_missing_msg);
        designatedUserMissing.setVisibility(View.GONE);

        deliveryAddressLay = view.findViewById(R.id.delivery_address_for_po_spinner_layout);
        deliveryAddress = view.findViewById(R.id.delivery_address_for_po_spinner);
        delAddMissing = view.findViewById(R.id.delivery_address_for_po_missing_msg);
        delAddMissing.setVisibility(View.GONE);

        billingAddressLay = view.findViewById(R.id.billing_address_for_po_spinner_layout);
        billingAddress = view.findViewById(R.id.billing_address_for_po_spinner);
        billAddMissing = view.findViewById(R.id.billing_address_for_po_missing_msg);
        billAddMissing.setVisibility(View.GONE);

        remarksLay = view.findViewById(R.id.spinner_layout_remarks_for_po);
        remarks = view.findViewById(R.id.remarks_for_po_spinner);
        remarksMissing = view.findViewById(R.id.remarks_for_po_missing_msg);
        remarksMissing.setVisibility(View.GONE);

        purchaseReqSelection = view.findViewById(R.id.select_purchase_requisition_for_po);

        prCardView = view.findViewById(R.id.added_pr_selection_for_po_pr_layout);
        prCardView.setVisibility(View.GONE);
        itemCardView = view.findViewById(R.id.added_item_selection_for_po_pr_layout);
        itemCardView.setVisibility(View.GONE);
        reqItemHsv = view.findViewById(R.id.requisition_items_for_po_hsv);
        reqItemHsv.setVisibility(View.GONE);
        reqItemMissing = view.findViewById(R.id.requisition_item_missing_for_po);
        reqItemMissing.setVisibility(View.GONE);

        poScroll = view.findViewById(R.id.purchase_order_layout_scroll_view);
        totalOrderQtyPO = view.findViewById(R.id.req_all_item_order_qty_for_po);
        totalAmountPO = view.findViewById(R.id.req_all_item_total_amont_for_po);
        totalVatAmountPO = view.findViewById(R.id.req_all_item_vat_amnt_for_po);

        selectedReqView = view.findViewById(R.id.added_pur_req_for_po_view);
        purchaseOrderSelectedRequisitionLists = new ArrayList<>();

        selectedReqView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        selectedReqView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(selectedReqView.getContext(),DividerItemDecoration.VERTICAL);
        selectedReqView.addItemDecoration(dividerItemDecoration);

        purchaseOrderReqSelectedAdapter = new PurchaseOrderReqSelectedAdapter(purchaseOrderSelectedRequisitionLists,mContext,PurchaseOrder.this);
        selectedReqView.setAdapter(purchaseOrderReqSelectedAdapter);

        selectedItemsView = view.findViewById(R.id.added_item_po_for_po_view);

        selectedItemsView.setHasFixedSize(true);
        layoutManagerItems = new LinearLayoutManager(getContext());
        selectedItemsView.setLayoutManager(layoutManagerItems);
        DividerItemDecoration dividerItemDecoration1 = new DividerItemDecoration(selectedItemsView.getContext(),DividerItemDecoration.VERTICAL);
        selectedItemsView.addItemDecoration(dividerItemDecoration1);

        emp_code = userInfoLists.get(0).getUserName();

        supplierforPOLists = new ArrayList<>();
        contactPersonLists = new ArrayList<>();

        supplierTypeLists = new ArrayList<>();
        paymentTypeLists = new ArrayList<>();

        designatedUserLists = new ArrayList<>();

        deliveryAddressLists = new ArrayList<>();
        billingAddressLists = new ArrayList<>();

        remarksLists = new ArrayList<>();

        // Selecting Purchase Order to Update
        purOrderToUpdateSelectSpinner.setOnClickListener(v -> {
            PurchaseOrderSelectDial purchaseReqSelectDial = new PurchaseOrderSelectDial(mContext,2);
            purchaseReqSelectDial.show(getActivity().getSupportFragmentManager(),"pos");
        });

        purOrderToUpdateSelectSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!wom_id_to_update.isEmpty()) {
                    getOrderData();
                }
            }
        });

        // Selecting PO Date
        poDate.setOnClickListener(v -> {
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
                    poDate.setText(dateText);
                    po_date = poDate.getText().toString();
                    dateMissing.setVisibility(View.GONE);

                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        // Selecting Expected Delivery Date
        expDeliveryDate.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view15, int year, int month, int dayOfMonth) {

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
                        expDeliveryDate.setText(dateText);
                        exp_del_date = expDeliveryDate.getText().toString();
                        dateMissing.setVisibility(View.GONE);

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        // WO/PO Type
        selected_wo_po = "1";
        woPoType.setText("Purchase Order");
        String[] type1 = new String[] {"Work Order", "Purchase Order"};

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type1);

        woPoType.setAdapter(arrayAdapter1);
        woPoType.setOnItemClickListener((parent, view13, position, id) -> {
            String name = parent.getItemAtPosition(position).toString();
            System.out.println(position+": "+name);
            if (name.equals("Work Order")) {
                selected_wo_po = "2";
            } else if (name.equals("Purchase Order")){
                selected_wo_po = "1";
            }
            woPoTypeMissing.setVisibility(View.GONE);

        });

        // Selecting Supplier
        supplierPO.setOnClickListener(v -> {
            fromforPO_supp = 1;
            SupplierDialogue supplierDialogue = new SupplierDialogue();
            supplierDialogue.show(getActivity().getSupportFragmentManager(),"SS" );
        });

        supplierPO.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (contact_person_needed) {
                    supplierMissing.setVisibility(View.GONE);
                    System.out.println(supplier_code);
                    String code = supplierCode.getText().toString();
                    if (!code.equals(supplier_code)) {
                        supplierAddress.setText(supplier_address);
                        supplierCode.setText(supplier_code);
                        getContactPerson();
                    }
                }

            }
        });

        // Selecting Contact Person
        contactPerson.setOnItemClickListener((parent, view12, position, id) -> {
            String name = parent.getItemAtPosition(position).toString();
            System.out.println(position+": "+name);
            for (int i = 0; i <contactPersonLists.size(); i++) {
                if (name.equals(contactPersonLists.get(i).getType())) {
                    aad_id = contactPersonLists.get(i).getId();
                }
            }

            contactPersonMissing.setVisibility(View.GONE);
        });

        // Selecting Supplier Type
        supplier_type_Id = "1";
        supplierType.setText("Supplier");

        supplierTypeLists.add(new ReceiveTypeList("1","Supplier",""));
        supplierTypeLists.add(new ReceiveTypeList("2","Trader's",""));
        supplierTypeLists.add(new ReceiveTypeList("3","Manufacturer",""));

        ArrayList<String> type = new ArrayList<>();
        for(int i = 0; i < supplierTypeLists.size(); i++) {
            type.add(supplierTypeLists.get(i).getType());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, R.layout.dropdown_menu_popup_item, R.id.drop_down_item, type);

        supplierType.setAdapter(arrayAdapter);
        supplierType.setOnItemClickListener((parent, view1, position, id) -> {
            String name = parent.getItemAtPosition(position).toString();
            System.out.println(position+": "+name);
            for (int i = 0; i <supplierTypeLists.size(); i++) {
                if (name.equals(supplierTypeLists.get(i).getType())) {
                    supplier_type_Id = supplierTypeLists.get(i).getId();
                }
            }
        });

        // Selecting Payment Type
        payment_type_Id = "1";
        paymentType.setText("Cash/Cheque");

        paymentTypeLists.add(new ReceiveTypeList("1","Cash/Cheque",""));
        paymentTypeLists.add(new ReceiveTypeList("2","Credit/LC",""));

        ArrayList<String> type2 = new ArrayList<>();
        for(int i = 0; i < paymentTypeLists.size(); i++) {
            type2.add(paymentTypeLists.get(i).getType());
        }

        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(mContext, R.layout.dropdown_menu_popup_item, R.id.drop_down_item, type2);

        paymentType.setAdapter(arrayAdapter2);
        paymentType.setOnItemClickListener((parent, view1, position, id) -> {
            String name = parent.getItemAtPosition(position).toString();
            System.out.println(position+": "+name);
            for (int i = 0; i <paymentTypeLists.size(); i++) {
                if (name.equals(paymentTypeLists.get(i).getType())) {
                    payment_type_Id = paymentTypeLists.get(i).getId();
                }
            }
        });

        // Selecting Designated User
        designatedUser.setOnClickListener(v -> {
            DesignatedUserSelectDial designatedUserSelectDial = new DesignatedUserSelectDial(designatedUserLists,mContext);
            designatedUserSelectDial.show(getActivity().getSupportFragmentManager(),"DUSL" );
        });

        designatedUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                designatedUserMissing.setVisibility(View.GONE);
            }
        });

        // Selecting Delivery Address
        deliveryAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                System.out.println(position+": "+name);
                for (int i = 0; i <deliveryAddressLists.size(); i++) {
                    if (name.equals(deliveryAddressLists.get(i).getType())) {
                        del_add_id = deliveryAddressLists.get(i).getId();
                        del_address = deliveryAddressLists.get(i).getType();
                    }
                }

                delAddMissing.setVisibility(View.GONE);
                closeKeyBoard();
            }
        });

        deliveryAddress.setOnTouchListener((v, event) -> {

            if (v.hasFocus()) {
                v.clearFocus();
                closeKeyBoard();
            }
            else {
                v.clearFocus();
                closeKeyBoard();
            }
            v.performClick();
            return true;
        });

        // Selecting Billing Address
        billingAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                System.out.println(position+": "+name);
                for (int i = 0; i <billingAddressLists.size(); i++) {
                    if (name.equals(billingAddressLists.get(i).getType())) {
                        bill_add_id = billingAddressLists.get(i).getId();
                        bill_address = billingAddressLists.get(i).getType();
                    }
                }

                billAddMissing.setVisibility(View.GONE);
            }
        });

        billingAddress.setOnTouchListener((v, event) -> {

            if (v.hasFocus()) {
                v.clearFocus();
                closeKeyBoard();
            }
            else {
                v.clearFocus();
                closeKeyBoard();
            }
            v.performClick();
            return true;
        });

        // Remarks
        remarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    remarksMissing.setVisibility(View.GONE);
                }
            }
        });

        // Selecting Purchase Requisition
        purchaseReqSelection.setOnClickListener(v -> {
            PurchaseReqSelectionForPODial purchaseReqSelectionForPODial = new PurchaseReqSelectionForPODial(mContext);
            purchaseReqSelectionForPODial.show(getActivity().getSupportFragmentManager(),"PRSPO" );
        });

        purchaseReqSelection.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("HOI KISU ?");
                getReqItemDetails();

            }
        });

        // Saving Purchase Order
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remarks_text = remarks.getText().toString();
                if (!po_date.isEmpty()) {
                    if (!exp_del_date.isEmpty()) {
                        if (!selected_wo_po.isEmpty()) {
                            if (!selected_ad_id_for_po.isEmpty()) {
                                if (!aad_id.isEmpty()) {
                                    if (!designated_user_id.isEmpty()) {
                                        if (!del_add_id.isEmpty()) {
                                            if (!bill_add_id.isEmpty()) {
                                                if (!remarks_text.isEmpty()) {
                                                    if (purchaseOrderSelectedRequisitionLists.size() != 0) {
                                                        boolean noItemList = false;
                                                        String prm_req_no = "";
                                                        boolean noQty = false;
                                                        boolean noPrice = false;
                                                        for (int i = 0; i < purchaseOrderSelectedRequisitionLists.size(); i++) {
                                                            prm_req_no = purchaseOrderSelectedRequisitionLists.get(i).getPrm_req_no();
                                                            ArrayList<PurchaseOrderItemDetailsLists> purchaseOrderItemDetailsLists = purchaseOrderSelectedRequisitionLists.get(i).getPurchaseOrderItemDetailsLists();
                                                            if (purchaseOrderItemDetailsLists.size() == 0 ) {
                                                                noItemList = true;
                                                                break;
                                                            }
                                                            else {
                                                                for (int j = 0; j < purchaseOrderItemDetailsLists.size(); j++) {
                                                                    if (purchaseOrderItemDetailsLists.get(j).getOrder_qty().isEmpty() || purchaseOrderItemDetailsLists.get(j).getOrder_qty().equals("0")) {
                                                                        noQty = true;
                                                                        break;
                                                                    }
                                                                    if (purchaseOrderItemDetailsLists.get(j).getCostprice().isEmpty()) {
                                                                        noPrice = true;
                                                                        break;
                                                                    }
                                                                    else {
                                                                        if (Double.parseDouble(purchaseOrderItemDetailsLists.get(j).getCostprice()) == 0.0) {
                                                                            noPrice = true;
                                                                            break;
                                                                        }
                                                                    }
                                                                }
                                                                if (noQty) {
                                                                    break;
                                                                }
                                                                if (noPrice) {
                                                                    break;
                                                                }
                                                            }

                                                        }
                                                        if (!noItemList) {
                                                            if (!noQty) {
                                                                if (!noPrice) {
                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                                                    builder.setTitle("SAVE PURCHASE ORDER!")
                                                                            .setMessage("Do you want to save this order?")
                                                                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {

                                                                                    savePurchaseOrder();
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
                                                                    Toast.makeText(getContext(),"Some Items Unit Price are missing",Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                            else {
                                                                Toast.makeText(getContext(),"Some Items Order Quantity are missing",Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                        else {
                                                            Toast.makeText(getContext(),"No Items for this "+prm_req_no+" No. Please Delete this Requisition",Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                    else {
                                                        Toast.makeText(getContext(),"Please Select Requisition to Save Purchase Order",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                else {
                                                    remarksMissing.setVisibility(View.VISIBLE);
                                                }
                                            }
                                            else {
                                                billAddMissing.setVisibility(View.VISIBLE);
                                            }
                                        }
                                        else {
                                            delAddMissing.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    else {
                                        designatedUserMissing.setVisibility(View.VISIBLE);
                                    }
                                }
                                else {
                                    contactPersonMissing.setVisibility(View.VISIBLE);
                                }
                            }
                            else {
                                supplierMissing.setVisibility(View.VISIBLE);
                            }
                        }
                        else {
                            woPoTypeMissing.setVisibility(View.VISIBLE);
                        }
                    }
                    else {
                        dateMissing.setVisibility(View.VISIBLE);
                        dateMissing.setText("Please Provide Expected Delivery Date");
                    }
                }
                else {
                    dateMissing.setVisibility(View.VISIBLE);
                    dateMissing.setText("Please Provide Purchase Order Date");
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (purchaseOrderSelectedRequisitionLists.size() != 0) {
                    boolean noItemList = false;
                    String prm_req_no = "";
                    boolean noQty = false;
                    boolean noPrice = false;
                    for (int i = 0; i < purchaseOrderSelectedRequisitionLists.size(); i++) {
                        prm_req_no = purchaseOrderSelectedRequisitionLists.get(i).getPrm_req_no();
                        ArrayList<PurchaseOrderItemDetailsLists> purchaseOrderItemDetailsLists = purchaseOrderSelectedRequisitionLists.get(i).getPurchaseOrderItemDetailsLists();
                        if (purchaseOrderItemDetailsLists.size() == 0 ) {
                            noItemList = true;
                            break;
                        }
                        else {
                            for (int j = 0; j < purchaseOrderItemDetailsLists.size(); j++) {
                                if (purchaseOrderItemDetailsLists.get(j).getOrder_qty().isEmpty() || purchaseOrderItemDetailsLists.get(j).getOrder_qty().equals("0")) {
                                    noQty = true;
                                    break;
                                }
                                if (purchaseOrderItemDetailsLists.get(j).getCostprice().isEmpty()) {
                                    noPrice = true;
                                    break;
                                }
                                else {
                                    if (Double.parseDouble(purchaseOrderItemDetailsLists.get(j).getCostprice()) == 0.0) {
                                        noPrice = true;
                                        break;
                                    }
                                }
                            }
                            if (noQty) {
                                break;
                            }
                            if (noPrice) {
                                break;
                            }
                        }

                    }
                    if (!noItemList) {
                        if (!noQty) {
                            if (!noPrice) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setTitle("UPDATE PURCHASE ORDER!")
                                        .setMessage("Do you want to update this order?")
                                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                updatePurchaseOrder();
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
                                Toast.makeText(getContext(),"Some Items Unit Price are missing",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(getContext(),"Some Items Order Quantity are missing",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getContext(),"No Items for this "+prm_req_no+" No. Please Delete this Requisition",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getContext(),"Please Select Requisition to update Purchase Order",Toast.LENGTH_SHORT).show();
                }
            }
        });

        getData();
        
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
    public void onReqClicked(int Position) {
        selectedRequisition = Position;
        prm_id_in_po = purchaseOrderSelectedRequisitionLists.get(Position).getPrm_id();
        itemUpdate(prm_id_in_po);
        purchaseOrderReqSelectedAdapter.notifyDataSetChanged();
    }

    // getting interface data
    public void getData() {
        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;
        supplierforPOLists = new ArrayList<>();
        designatedUserLists = new ArrayList<>();
        deliveryAddressLists = new ArrayList<>();
        billingAddressLists = new ArrayList<>();

        String suppUrl = "http://103.56.208.123:8001/apex/tterp/utility/getSupplier";
        String desigUserUrl = "http://103.56.208.123:8001/apex/tterp/utility/getDesignatedUser";
        String addressUrl = "http://103.56.208.123:8001/apex/tterp/utility/getAddress";
        String remarksUrl = "http://103.56.208.123:8001/apex/tterp/utility/getRemarks?wom_wo_type="+selected_wo_po+"";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest remarksReq = new StringRequest(Request.Method.GET, remarksUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);

                        String instrd_name = info.getString("instrd_name")
                                .equals("null") ? "" : info.getString("instrd_name");

                        remarksLists.add(instrd_name);

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

        StringRequest addressReq = new StringRequest(Request.Method.GET, addressUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);

//                        String coa_name = info.getString("coa_name")
//                                .equals("null") ? "" : info.getString("coa_name");
                        String coa_address = info.getString("coa_address")
                                .equals("null") ? "" : info.getString("coa_address");
//                        String coa_short_name = info.getString("coa_short_name")
//                                .equals("null") ? "" : info.getString("coa_short_name");
//                        String coa_short_code = info.getString("coa_short_code")
//                                .equals("null") ? "" : info.getString("coa_short_code");
                        String coa_id = info.getString("coa_id")
                                .equals("null") ? "" : info.getString("coa_id");

                        deliveryAddressLists.add(new ReceiveTypeList(coa_id, coa_address,""));
                        billingAddressLists.add(new ReceiveTypeList(coa_id, coa_address,""));


                    }
                }

                requestQueue.add(remarksReq);

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

        StringRequest desigUserReq = new StringRequest(Request.Method.GET, desigUserUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);

                        String user_name = info.getString("user_name")
                                .equals("null") ? "" : info.getString("user_name");
                        String usr_name = info.getString("usr_name")
                                .equals("null") ? "" : info.getString("usr_name");
                        String usr_email = info.getString("usr_email")
                                .equals("null") ? "" : info.getString("usr_email");
                        String usr_contact = info.getString("usr_contact")
                                .equals("null") ? "" : info.getString("usr_contact");
                        String emp_name = info.getString("emp_name")
                                .equals("null") ? "" : info.getString("emp_name");
                        String emp_code = info.getString("emp_code")
                                .equals("null") ? "" : info.getString("emp_code");
                        String job_calling_title = info.getString("job_calling_title")
                                .equals("null") ? "" : info.getString("job_calling_title");
                        String dept_name = info.getString("dept_name")
                                .equals("null") ? "" : info.getString("dept_name");
                        String emp_id = info.getString("emp_id")
                                .equals("null") ? "" : info.getString("emp_id");
                        String jsm_dept_id = info.getString("jsm_dept_id")
                                .equals("null") ? "" : info.getString("jsm_dept_id");
                        String jsm_id = info.getString("jsm_id")
                                .equals("null") ? "" : info.getString("jsm_id");

                        designatedUserLists.add(new DesignatedUserList(user_name,usr_name,usr_email,usr_contact,emp_name,
                                emp_code,job_calling_title,dept_name,emp_id,jsm_dept_id,jsm_id));

                    }
                }

                requestQueue.add(addressReq);

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

        StringRequest suppReq = new StringRequest(Request.Method.GET, suppUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);

                        String ad_id = info.getString("ad_id");
                        String ad_code = info.getString("ad_code");
                        String ad_name = info.getString("ad_name");
                        String ad_short_name = info.getString("ad_short_name");
                        String ad_address = info.getString("ad_address")
                                .equals("null") ? "N/A" : info.getString("ad_address");

                        supplierforPOLists.add(new SupplierList(ad_id,ad_code,ad_name,ad_short_name,ad_address));
                    }
                }

                requestQueue.add(desigUserReq);

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

        requestQueue.add(suppReq);
    }

    private void updateFragment() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < deliveryAddressLists.size(); i++) {
                    type.add(deliveryAddressLists.get(i).getType());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                deliveryAddress.setAdapter(arrayAdapter);

                ArrayList<String> type1 = new ArrayList<>();
                for(int i = 0; i < billingAddressLists.size(); i++) {
                    type1.add(billingAddressLists.get(i).getType());
                }

                ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type1);

                billingAddress.setAdapter(arrayAdapter1);


                ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,remarksLists);

                remarks.setAdapter(arrayAdapter2);
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

                    getData();
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

                getData();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }

    // getting contact person after selecting supplier
    public void getContactPerson() {
        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;
        contactPersonLists = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        String url = "http://103.56.208.123:8001/apex/tterp/utility/getContactPerson?ad_id="+selected_ad_id_for_po+"";

        StringRequest contactReq = new StringRequest(Request.Method.GET, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);

                        String aad_id = info.getString("aad_id");
                        String aad_contact_person = info.getString("aad_contact_person");

                        contactPersonLists.add(new ReceiveTypeList(aad_id,aad_contact_person,""));
                    }
                }

                connected = true;
                updateContactPerson();
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateContactPerson();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateContactPerson();
        });

        requestQueue.add(contactReq);
    }

    private void updateContactPerson() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                contactPersonMissing.setVisibility(View.GONE);
                contactPersonLay.setEnabled(true);
                contactPerson.setText("");
                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < contactPersonLists.size(); i++) {
                    type.add(contactPersonLists.get(i).getType());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                contactPerson.setAdapter(arrayAdapter);
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

                    getContactPerson();
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

                getContactPerson();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }

    // getting requisition item after selecting purchase requisition
    public void getReqItemDetails() {
        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;
        itemCardView.setVisibility(View.GONE);
        reqItemMissing.setVisibility(View.GONE);
        reqItemHsv.setVisibility(View.GONE);
        total_order_qty_po = 0;
        total_amount_po = 0;
        total_vat_amnt_po = 0;
        ArrayList<PurchaseOrderItemDetailsLists> purchaseOrderItemDetailsLists = new ArrayList<>();
        String url = "http://103.56.208.123:8001/apex/tterp/purchaseOrder/getRequisitionsItemData?WOJ_PRM_ID="+prm_id_in_po+"&WOJ_IM_ID="+im_id_in_po+"";
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

                        String color_name = info.getString("color_name")
                                .equals("null") ? "" : info.getString("color_name");
                        String color_id = info.getString("color_id")
                                .equals("null") ? "" : info.getString("color_id");
                        String size_name = info.getString("size_name")
                                .equals("null") ? "" : info.getString("size_name");
                        String balance_qty = info.getString("balance_qty")
                                .equals("null") ? "" : info.getString("balance_qty");
                        String prd_qty = info.getString("prd_qty")
                                .equals("null") ? "" : info.getString("prd_qty");
                        String size_id = info.getString("size_id")
                                .equals("null") ? "" : info.getString("size_id");
                        String prd_id = info.getString("prd_id")
                                .equals("null") ? "" : info.getString("prd_id");
                        String item_reference_name = info.getString("item_reference_name")
                                .equals("null") ? "" : info.getString("item_reference_name");
                        String item_id = info.getString("item_id")
                                .equals("null") ? "" : info.getString("item_id");
                        String item_code = info.getString("item_code")
                                .equals("null") ? "" : info.getString("item_code");
                        String item_barcode_no = info.getString("item_barcode_no")
                                .equals("null") ? "" : info.getString("item_barcode_no");
                        String item_hs_code = info.getString("item_hs_code")
                                .equals("null") ? "" : info.getString("item_hs_code");
                        String item_part_number = info.getString("item_part_number")
                                .equals("null") ? "" : info.getString("item_part_number");
                        String item_unit = info.getString("item_unit")
                                .equals("null") ? "" : info.getString("item_unit");
//                        String cctps_id = info.getString("cctps_id")
//                                .equals("null") ? "" : info.getString("cctps_id");
//                        String ccqdd_item_service_rate = info.getString("ccqdd_item_service_rate")
//                                .equals("null") ? "" : info.getString("ccqdd_item_service_rate");
                        String costprice = info.getString("costprice")
                                .equals("null") ? "0" : info.getString("costprice");

                        int bal_qty = Integer.parseInt(balance_qty);
                        double unit_price = Double.parseDouble(costprice);
                        double item_per_amount = bal_qty * unit_price;

                        double vat_amnt = (0.0 *  unit_price) / 100;

                        double total_vat_per_item_amnt = vat_amnt *  bal_qty;
                        DecimalFormat decimalFormat = new DecimalFormat("#.#");


                        purchaseOrderItemDetailsLists.add(new PurchaseOrderItemDetailsLists("",color_name,color_id,size_name,balance_qty,prd_qty,balance_qty,"0",
                                size_id,prd_id,item_reference_name,item_id,item_code,item_barcode_no,item_hs_code,item_part_number,item_unit,
                                costprice,decimalFormat.format(item_per_amount),"0",decimalFormat.format(vat_amnt),
                                decimalFormat.format(total_vat_per_item_amnt),false));

                    }
                }
                for (int i = 0; i < purchaseOrderSelectedRequisitionLists.size(); i++) {
                    if (prm_id_in_po.equals(purchaseOrderSelectedRequisitionLists.get(i).getPrm_id())) {
                        purchaseOrderSelectedRequisitionLists.get(i).setPurchaseOrderItemDetailsLists(purchaseOrderItemDetailsLists);
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

                itemUpdate(prm_id_in_po);
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

                    getReqItemDetails();
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

                getReqItemDetails();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }

    public static void itemUpdate(String new_prm_id) {
        ArrayList<PurchaseOrderItemDetailsLists> purchaseOrderItemDetailsLists = new ArrayList<>();
        for (int i = 0; i < purchaseOrderSelectedRequisitionLists.size(); i++) {
            if (new_prm_id.equals(purchaseOrderSelectedRequisitionLists.get(i).getPrm_id())) {
                purchaseOrderItemDetailsLists = purchaseOrderSelectedRequisitionLists.get(i).getPurchaseOrderItemDetailsLists();
            }
        }
        itemCardView.setVisibility(View.VISIBLE);
        if (purchaseOrderItemDetailsLists.size() == 0) {
            reqItemMissing.setVisibility(View.VISIBLE);
            reqItemHsv.setVisibility(View.GONE);
        }
        else {
            reqItemMissing.setVisibility(View.GONE);
            reqItemHsv.setVisibility(View.VISIBLE);
            purchaseOrderSelectedItemAdpater = new PurchaseOrderSelectedItemAdpater(purchaseOrderItemDetailsLists,mContext);
            selectedItemsView.setAdapter(purchaseOrderSelectedItemAdpater);
            total_order_qty_po = 0;
            total_amount_po = 0.0;
            total_vat_amnt_po = 0.0;
            for (int i = 0; i < purchaseOrderItemDetailsLists.size(); i++) {
                if (!purchaseOrderItemDetailsLists.get(i).getBalance_qty().isEmpty()) {
                    total_order_qty_po = total_order_qty_po + Integer.parseInt(purchaseOrderItemDetailsLists.get(i).getOrder_qty());
                }
                if (!purchaseOrderItemDetailsLists.get(i).getAmount().isEmpty()) {
                    total_amount_po = total_amount_po + Double.parseDouble(purchaseOrderItemDetailsLists.get(i).getAmount());
                }
                if (!purchaseOrderItemDetailsLists.get(i).getItem_vat_amnt().isEmpty()) {
                    total_vat_amnt_po = total_vat_amnt_po + Double.parseDouble(purchaseOrderItemDetailsLists.get(i).getItem_vat_amnt());
                }
            }

            DecimalFormat decimalFormat = new DecimalFormat("#.#");

            totalOrderQtyPO.setText(String.valueOf(total_order_qty_po));
            totalAmountPO.setText(decimalFormat.format(total_amount_po));
            totalVatAmountPO.setText(decimalFormat.format(total_vat_amnt_po));
        }

        poScroll.post(new Runnable() {
            @Override
            public void run() {
                View lastChild = poScroll.getChildAt(poScroll.getChildCount() - 1);
                int bottom = lastChild.getBottom() + poScroll.getPaddingBottom();
                int sy = poScroll.getScrollY();
                int sh = poScroll.getHeight();
                int delta = bottom - (sy + sh);

                poScroll.smoothScrollBy(0, delta);

            }
        });
    }

    // saving purchase order
    public void savePurchaseOrder() {
        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        master_conn = false;
        master_connected = false;

        String url = "http://103.56.208.123:8001/apex/tterp/purchaseOrder/insertWorkOrderMst";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            master_conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                String out_wom_id = jsonObject.getString("out_wom_id").equals("null") ? "" : jsonObject.getString("out_wom_id");
                String out_wom_no = jsonObject.getString("out_wom_no");
                if (string_out.equals("Successfully Created")) {
                    if (!out_wom_id.isEmpty()) {
                        inserter_wom_no = out_wom_no;
                        inserted_wom_id = out_wom_id;
                        System.out.println("NEW WOM ID: "+ inserted_wom_id);
                        master_connected = true;
                        checkToSaveRequisitionData();
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
                headers.put("P_AAD_ID",aad_id);
                headers.put("P_AD_ID",selected_ad_id_for_po);
                headers.put("P_BILL_ADDS",bill_address);
                headers.put("P_BILL_COA_ID",bill_add_id);
                headers.put("P_DELIVERY_DATE",exp_del_date);
                headers.put("P_DEL_ADDS",del_address);
                headers.put("P_DEL_COA_ID",del_add_id);
                headers.put("P_DESIG_USER",designated_user_id);
                headers.put("P_PAYMENT_TYPE",payment_type_Id);
                headers.put("P_REMARKS",remarks_text);
                headers.put("P_SUPPLIER_TYPE",supplier_type_Id);
                headers.put("P_USER",emp_code);
                headers.put("P_WOM_DATE",po_date);
                headers.put("P_WOM_WO_TYPE",selected_wo_po);

                return headers;
            }
        };

        requestQueue.add(request);

    }

    private void checkToSaveRequisitionData() {
        if (purchaseOrderSelectedRequisitionLists.size() != 0) {
            boolean allUpdated = false;
            for (int i = 0; i < purchaseOrderSelectedRequisitionLists.size(); i++) {
                allUpdated = purchaseOrderSelectedRequisitionLists.get(i).isUpdated();
                if (!purchaseOrderSelectedRequisitionLists.get(i).isUpdated()) {
                    allUpdated = purchaseOrderSelectedRequisitionLists.get(i).isUpdated();
                    String im_id = purchaseOrderSelectedRequisitionLists.get(i).getIm_id();
                    String prm_id = purchaseOrderSelectedRequisitionLists.get(i).getPrm_id();
                    first_index_to_update = i;
                    saveRequisitionData(im_id, prm_id);
                    break;
                }
            }
            if (allUpdated) {
                woj_conn = true;
                woj_connected = true;
                updateLayout();
            }
        }
        else {
            woj_conn = true;
            woj_connected = true;
            wod_conn = true;
            wod_connected = true;
            updateLayout();
        }
    }

    public void saveRequisitionData(String im_id, String prm_Id) {
        woj_conn = false;
        woj_connected = false;
        String url = "http://103.56.208.123:8001/apex/tterp/purchaseOrder/insertWorkOrderJob";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            woj_conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                String out_woj_id = jsonObject.getString("out_woj_id").equals("null") ? "" : jsonObject.getString("out_woj_id");
                if (string_out.equals("Successfully Created")) {
                    if (!out_woj_id.isEmpty()) {
                        inserted_woj_id = out_woj_id;
                        System.out.println("NEW WOJ ID: "+ inserted_woj_id);
                        woj_connected = true;
                        checkToSaveItemData();
                    }
                    else {
                        woj_connected = false;
                        updateLayout();
                    }
                }
                else {
                    woj_connected = false;
                    updateLayout();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                woj_connected = false;
                updateLayout();
            }
        }, error -> {
            error.printStackTrace();
            woj_conn = false;
            woj_connected = false;
            updateLayout();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_IM_ID",im_id);
                headers.put("P_PRM_ID",prm_Id);
                headers.put("P_WOM_ID",inserted_wom_id);
                headers.put("P_USER",emp_code);
                return headers;
            }
        };

        requestQueue.add(request);
    }

    private void checkToSaveItemData() {
        ArrayList<PurchaseOrderItemDetailsLists> purchaseOrderItemDetailsLists = purchaseOrderSelectedRequisitionLists.get(first_index_to_update).getPurchaseOrderItemDetailsLists();
        if (purchaseOrderItemDetailsLists.size() != 0) {
            boolean allUpdated = false;
            for (int i = 0; i < purchaseOrderItemDetailsLists.size(); i++) {
                allUpdated = purchaseOrderItemDetailsLists.get(i).isUpdated();
                if (!purchaseOrderItemDetailsLists.get(i).isUpdated()) {
                    allUpdated = purchaseOrderItemDetailsLists.get(i).isUpdated();
                    String item_id = purchaseOrderItemDetailsLists.get(i).getItem_id();
                    String color_id = purchaseOrderItemDetailsLists.get(i).getColor_id();
                    if (color_id.isEmpty()) {
                        color_id = "1";
                    }
                    String size_id = purchaseOrderItemDetailsLists.get(i).getSize_id();
                    if (size_id.isEmpty()) {
                        size_id = "1";
                    }
                    String prd_id = purchaseOrderItemDetailsLists.get(i).getPrd_id();
                    String vat_pct = purchaseOrderItemDetailsLists.get(i).getItem_vat_percentage();
                    String vat_pct_amnt = purchaseOrderItemDetailsLists.get(i).getItem_vat();
                    String order_qty = purchaseOrderItemDetailsLists.get(i).getOrder_qty();
                    String unit_price = purchaseOrderItemDetailsLists.get(i).getCostprice();

                    saveItemData(purchaseOrderItemDetailsLists,color_id, item_id,prd_id,size_id,vat_pct,vat_pct_amnt,order_qty,unit_price,i);
                    break;
                }
            }
            if (allUpdated) {
                wod_conn = true;
                wod_connected = true;
                purchaseOrderSelectedRequisitionLists.get(first_index_to_update).setUpdated(true);
                checkToSaveRequisitionData();
            }
        }
        else {
            wod_conn = true;
            wod_connected = true;
            purchaseOrderSelectedRequisitionLists.get(first_index_to_update).setUpdated(true);
            checkToSaveRequisitionData();
        }
    }

    public void saveItemData(ArrayList<PurchaseOrderItemDetailsLists> purchaseOrderItemDetailsLists,String color_id, String item_id, String prd_id, String size_id, String vat_pct, String vat_pct_amnt, String order_qty, String unit_price, int lastIndex) {
        wod_conn = false;
        wod_connected = false;
        String url = "http://103.56.208.123:8001/apex/tterp/purchaseOrder/insertWorkOrderDtl";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            wod_conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                if (string_out.equals("Successfully Created")) {
                    purchaseOrderItemDetailsLists.get(lastIndex).setUpdated(true);
                    wod_connected = true;
                    checkToSaveItemData();
                }
                else {
                    wod_connected = false;
                    updateLayout();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                wod_connected = false;
                updateLayout();
            }
        }, error -> {
            error.printStackTrace();
            wod_conn = false;
            wod_connected = false;
            updateLayout();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_PRD_ID",prd_id);
                headers.put("P_ITEM_ID",item_id);
                headers.put("P_COLOR_ID",color_id);
                headers.put("P_SIZE_ID",size_id);
                headers.put("P_VAT_PCT",vat_pct);
                headers.put("P_VAT_PCT_AMT",vat_pct_amnt);
                headers.put("P_USER",emp_code);
                headers.put("P_WOD_QTY",order_qty);
                headers.put("P_WOD_RATE",unit_price);
                headers.put("P_WOJ_ID",inserted_woj_id);
                headers.put("P_WOM_ID",inserted_wom_id);
                return headers;
            }
        };

        requestQueue.add(request);
    }

    private void updateLayout() {
        waitProgress.dismiss();
        if (master_conn) {
            if (master_connected) {
                if (woj_conn) {
                    if (woj_connected) {
                        if (wod_conn) {
                            if (wod_connected) {
                                master_conn =false;
                                master_connected = false;
                                woj_conn = false;
                                woj_connected = false;
                                wod_conn = false;
                                wod_connected = false;

                                AlertDialog dialog = new AlertDialog.Builder(mContext)
                                        .setMessage("New Purchase Order Saved Successfully. Your New Purchase Order No is: "+inserter_wom_no)
                                        .setPositiveButton("Ok", null)
                                        .show();

                                dialog.setCancelable(false);
                                dialog.setCanceledOnTouchOutside(false);
                                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                positive.setOnClickListener(v -> {

                                    MainMenu.dashboardFront = false;
                                    getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,new PurchaseOrder()).commit();
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
                                    checkToSaveItemData();
                                    dialog.dismiss();
                                });

                                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                                negative.setOnClickListener(v -> dialog.dismiss());
                            }
                        }
                        else {
                            AlertDialog dialog = new AlertDialog.Builder(mContext)
                                    .setMessage("Failed to save Item Data for Purchase Order. Please retry or data will be lost.")
                                    .setPositiveButton("Retry", null)
                                    .setNegativeButton("Cancel", null)
                                    .show();

                            dialog.setCancelable(false);
                            dialog.setCanceledOnTouchOutside(false);
                            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                            positive.setOnClickListener(v -> {

                                waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
                                waitProgress.setCancelable(false);
                                checkToSaveItemData();
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
                            checkToSaveRequisitionData();
                            dialog.dismiss();
                        });

                        Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                        negative.setOnClickListener(v -> dialog.dismiss());
                    }
                }
                else {
                    AlertDialog dialog = new AlertDialog.Builder(mContext)
                            .setMessage("Failed to save Requisition Data for Purchase Order. Please retry or data will be lost.")
                            .setPositiveButton("Retry", null)
                            .setNegativeButton("Cancel", null)
                            .show();

                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positive.setOnClickListener(v -> {

                        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
                        waitProgress.setCancelable(false);
                        checkToSaveRequisitionData();
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

                    savePurchaseOrder();
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

                savePurchaseOrder();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }

    // getting purchase order data to update
    public void getOrderData() {
        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;
        purchaseOrderSelectedRequisitionLists.clear();

        String orderDataUrl = "http://103.56.208.123:8001/apex/tterp/purchaseOrder/getOrderMasterData?wom_id="+wom_id_to_update+"";
        String requisitionUrl = "http://103.56.208.123:8001/apex/tterp/purchaseOrder/getSavedRequisitions?wom_id="+wom_id_to_update+"";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest requisitionReq = new StringRequest(Request.Method.GET, requisitionUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);

                        String woj_id = info.getString("woj_id");
                        String prm_req_no = info.getString("prm_req_no");
                        String pr_date = info.getString("pr_date")
                                .equals("null") ? "" : info.getString("pr_date");
                        String prm_id = info.getString("prm_id")
                                .equals("null") ? "" : info.getString("prm_id");
                        String im_id = info.getString("im_id")
                                .equals("null") ? "" : info.getString("im_id");
                        String im_name = info.getString("im_name")
                                .equals("null") ? "" : info.getString("im_name");

                        purchaseOrderSelectedRequisitionLists.add(new PurchaseOrderSelectedRequisitionLists(woj_id,prm_req_no,pr_date,prm_id,
                                im_id,im_name,"","","",
                                new ArrayList<>(),false,false,false));

                    }
                }

                checkToGetRequisitionData();
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updatePOData();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updatePOData();
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

                        po_date = info.getString("work_date");
                        exp_del_date = info.getString("delivery_date");
                        selected_wo_po = info.getString("wom_wo_type")
                                .equals("null") ? "" : info.getString("wom_wo_type");
                        selected_ad_id_for_po = info.getString("wom_ad_id")
                                .equals("null") ? "" : info.getString("wom_ad_id");
                        supplier_name = info.getString("ad_name")
                                .equals("null") ? "" : info.getString("ad_name");
                        supplier_code = info.getString("ad_code")
                                .equals("null") ? "" : info.getString("ad_code");
                        supplier_address = info.getString("ad_address")
                                .equals("null") ? "" : info.getString("ad_address");
                        aad_id = info.getString("wom_aad_id")
                                .equals("null") ? "" : info.getString("wom_aad_id");
                        contact_person_name = info.getString("aad_contact_person")
                                .equals("null") ? "" : info.getString("aad_contact_person");
                        supplier_type_Id = info.getString("wom_supplier_type")
                                .equals("null") ? "" : info.getString("wom_supplier_type");
                        payment_type_Id = info.getString("wom_payment_type")
                                .equals("null") ? "" : info.getString("wom_payment_type");
                        designated_user_id = info.getString("wom_designated_rm_user")
                                .equals("null") ? "" : info.getString("wom_designated_rm_user");
                        designated_user_name = info.getString("user_name")
                                .equals("null") ? "" : info.getString("user_name");

                        if (designated_user_name.equals(" ")) {
                            designated_user_name = "";
                        }

                        del_address = info.getString("wom_delivery_address")
                                .equals("null") ? "" : info.getString("wom_delivery_address");
                        del_add_id = info.getString("wom_delivery_add_coa_id")
                                .equals("null") ? "" : info.getString("wom_delivery_add_coa_id");
                        bill_address = info.getString("wom_billing_address")
                                .equals("null") ? "" : info.getString("wom_billing_address");
                        bill_add_id = info.getString("wom_billing_add_coa_id")
                                .equals("null") ? "" : info.getString("wom_billing_add_coa_id");
                        remarks_text = info.getString("wom_remarks")
                                .equals("null") ? "" : info.getString("wom_remarks");

                    }
                }

                requestQueue.add(requisitionReq);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updatePOData();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updatePOData();
        });

        requestQueue.add(orderDataReq);
    }

    public void checkToGetRequisitionData() {
        if (purchaseOrderSelectedRequisitionLists.size() != 0) {
            boolean allUpdated = false;
            for (int i = 0; i < purchaseOrderSelectedRequisitionLists.size(); i++) {
                allUpdated = purchaseOrderSelectedRequisitionLists.get(i).isUpdatedToGetVal();
                if (!purchaseOrderSelectedRequisitionLists.get(i).isUpdatedToGetVal()) {
                    allUpdated = purchaseOrderSelectedRequisitionLists.get(i).isUpdatedToGetVal();
                    String woj_id = purchaseOrderSelectedRequisitionLists.get(i).getWoj_id();
                    getRequisitionItemData(woj_id,i);
                    break;
                }
            }
            if (allUpdated) {
                connected = true;
                updatePOData();
            }
        }
        else {
            connected = true;
            updatePOData();
        }
    }

    public void getRequisitionItemData(String woj_id, int index) {
        itemCardView.setVisibility(View.GONE);
        reqItemMissing.setVisibility(View.GONE);
        reqItemHsv.setVisibility(View.GONE);
        total_order_qty_po = 0;
        total_amount_po = 0;
        total_vat_amnt_po = 0;
        ArrayList<PurchaseOrderItemDetailsLists> purchaseOrderItemDetailsLists = new ArrayList<>();
        String url = "http://103.56.208.123:8001/apex/tterp/purchaseOrder/getSavedRequistionItemData?woj_id="+woj_id+"";
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
                        String color_name = info.getString("color_name")
                                .equals("null") ? "" : info.getString("color_name");
                        String color_id = info.getString("color_id")
                                .equals("null") ? "" : info.getString("color_id");
                        String size_name = info.getString("size_name")
                                .equals("null") ? "" : info.getString("size_name");
                        String balance_qty = info.getString("balance_qty")
                                .equals("null") ? "" : info.getString("balance_qty");
                        String prd_qty = info.getString("prd_qty")
                                .equals("null") ? "" : info.getString("prd_qty");
                        String wod_qty = info.getString("wod_qty")
                                .equals("null") ? "" : info.getString("wod_qty");
                        String after_order_qty = info.getString("after_order_qty")
                                .equals("null") ? "" : info.getString("after_order_qty");
                        String size_id = info.getString("size_id")
                                .equals("null") ? "" : info.getString("size_id");
                        String prd_id = info.getString("prd_id")
                                .equals("null") ? "" : info.getString("prd_id");
                        String item_reference_name = info.getString("item_reference_name")
                                .equals("null") ? "" : info.getString("item_reference_name");
                        String item_id = info.getString("item_id")
                                .equals("null") ? "" : info.getString("item_id");
                        String item_code = info.getString("item_code")
                                .equals("null") ? "" : info.getString("item_code");
                        String item_barcode_no = info.getString("item_barcode_no")
                                .equals("null") ? "" : info.getString("item_barcode_no");
                        String item_hs_code = info.getString("item_hs_code")
                                .equals("null") ? "" : info.getString("item_hs_code");
                        String item_part_number = info.getString("item_part_number")
                                .equals("null") ? "" : info.getString("item_part_number");
                        String item_unit = info.getString("item_unit")
                                .equals("null") ? "" : info.getString("item_unit");
                        String costprice = info.getString("costprice")
                                .equals("null") ? "0" : info.getString("costprice");
                        String wod_vat_pct = info.getString("wod_vat_pct")
                                .equals("null") ? "0" : info.getString("wod_vat_pct");
                        String wod_vat_pct_amt = info.getString("wod_vat_pct_amt")
                                .equals("null") ? "0" : info.getString("wod_vat_pct_amt");

                        int order_qty = Integer.parseInt(wod_qty);
                        double unit_price = Double.parseDouble(costprice);
                        double item_per_amount = order_qty * unit_price;

                        double total_vat_per_item_amnt = Double.parseDouble(wod_vat_pct_amt) *  order_qty;
                        DecimalFormat decimalFormat = new DecimalFormat("#.#");


                        purchaseOrderItemDetailsLists.add(new PurchaseOrderItemDetailsLists(wod_id,color_name,color_id,size_name,
                                balance_qty, prd_qty,wod_qty,after_order_qty,size_id,prd_id,item_reference_name,item_id,
                                item_code,item_barcode_no,item_hs_code,item_part_number,item_unit,
                                costprice,decimalFormat.format(item_per_amount),wod_vat_pct,wod_vat_pct_amt,
                                decimalFormat.format(total_vat_per_item_amnt),false));

                    }
                }
                purchaseOrderSelectedRequisitionLists.get(index).setPurchaseOrderItemDetailsLists(purchaseOrderItemDetailsLists);
                purchaseOrderSelectedRequisitionLists.get(index).setUpdatedToGetVal(true);

                checkToGetRequisitionData();

            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updatePOData();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updatePOData();
        });

        requestQueue.add(stringRequest);

    }

    private void updatePOData() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                save.setVisibility(View.GONE);
                update.setVisibility(View.VISIBLE);

                poDate.setText(po_date);
                poDateLay.setEnabled(false);
                expDeliveryDate.setText(exp_del_date);
                expDelDateLay.setEnabled(false);
                dateMissing.setVisibility(View.GONE);

                if (selected_wo_po.equals("1")) {
                    woPoType.setText("Purchase Order");
                }
                else if (selected_wo_po.equals("2")){
                    woPoType.setText("Work Order");
                }
                woPoTypeLay.setEnabled(false);
                woPoTypeMissing.setVisibility(View.GONE);

                contact_person_needed = false;
                supplierPOLay.setHint("Supplier");
                supplierPOLay.setEnabled(false);
                supplierMissing.setVisibility(View.GONE);
                supplierPO.setText(supplier_name);
                supplierCode.setText(supplier_code);
                supplierAddress.setText(supplier_address);

                contactPersonLay.setEnabled(false);
                contactPersonMissing.setVisibility(View.GONE);
                contactPerson.setText(contact_person_name);

                String text = "";
                for (int i = 0; i < supplierTypeLists.size(); i++) {
                    if (!supplier_type_Id.isEmpty()) {
                        if (supplier_type_Id.equals(supplierTypeLists.get(i).getId())) {
                            text = supplierTypeLists.get(i).getType();
                        }
                    }
                }
                supplierType.setText(text);
                supplierTypeLay.setEnabled(false);

                for (int i = 0; i < paymentTypeLists.size(); i++) {
                    if (!payment_type_Id.isEmpty()) {
                        if (payment_type_Id.equals(paymentTypeLists.get(i).getId())) {
                            text = paymentTypeLists.get(i).getType();
                        }
                    }
                }
                paymentType.setText(text);
                paymentTypeLay.setEnabled(false);

                designatedUser.setText(designated_user_name);
                designatedUserLay.setEnabled(false);
                designatedUserMissing.setVisibility(View.GONE);

                deliveryAddress.setText(del_address);
                deliveryAddressLay.setEnabled(false);
                delAddMissing.setVisibility(View.GONE);

                billingAddress.setText(bill_address);
                billingAddressLay.setEnabled(false);
                billAddMissing.setVisibility(View.GONE);

                remarks.setText(remarks_text);
                remarksLay.setEnabled(false);
                remarksMissing.setVisibility(View.GONE);

                prCardView.setVisibility(View.GONE);
                String prm_id = "";
                if (purchaseOrderSelectedRequisitionLists.size() > 0) {
                    prCardView.setVisibility(View.VISIBLE);
                    prm_id = purchaseOrderSelectedRequisitionLists.get(0).getPrm_id();
                    selectedRequisition = 0;
                }
                purchaseOrderReqSelectedAdapter.notifyDataSetChanged();
                itemUpdate(prm_id);

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

                    getOrderData();
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

                getOrderData();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }

    // updating Purchase Order
    public void updatePurchaseOrder() {
        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        checkToUpdateRequisition();
    }
    private void checkToUpdateRequisition() {
        if (purchaseOrderSelectedRequisitionLists.size() != 0) {
            boolean allUpdated = false;
            for (int i = 0; i < purchaseOrderSelectedRequisitionLists.size(); i++) {
                allUpdated = purchaseOrderSelectedRequisitionLists.get(i).isUpdated();
                if (!purchaseOrderSelectedRequisitionLists.get(i).isUpdated()) {
                    allUpdated = purchaseOrderSelectedRequisitionLists.get(i).isUpdated();
                    String im_id = purchaseOrderSelectedRequisitionLists.get(i).getIm_id();
                    String prm_id = purchaseOrderSelectedRequisitionLists.get(i).getPrm_id();
                    String woj_id = purchaseOrderSelectedRequisitionLists.get(i).getWoj_id();
                    first_index_to_update = i;
                    updateRequisitionData(woj_id, im_id, prm_id);
                    break;
                }
            }
            if (allUpdated) {
                woj_conn = true;
                woj_connected = true;
                afterUpdatePO();
            }
        }
        else {
            woj_conn = true;
            woj_connected = true;
            wod_conn = true;
            wod_connected = true;
            afterUpdatePO();
        }
    }

    public void updateRequisitionData(String woj_id, String im_id, String prm_id) {
        woj_conn = false;
        woj_connected = false;
        if (woj_id.isEmpty()) {
            String url = "http://103.56.208.123:8001/apex/tterp/purchaseOrder/insertWorkOrderJob";

            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
                woj_conn = true;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String string_out = jsonObject.getString("string_out");
                    String out_woj_id = jsonObject.getString("out_woj_id").equals("null") ? "" : jsonObject.getString("out_woj_id");
                    if (string_out.equals("Successfully Created")) {
                        if (!out_woj_id.isEmpty()) {
                            inserted_woj_id = out_woj_id;
                            System.out.println("NEW WOJ ID: "+ inserted_woj_id);
                            woj_connected = true;
                            checkToUpdateItemData();
                        }
                        else {
                            woj_connected = false;
                            afterUpdatePO();
                        }
                    }
                    else {
                        woj_connected = false;
                        afterUpdatePO();
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    woj_connected = false;
                    afterUpdatePO();
                }
            }, error -> {
                error.printStackTrace();
                woj_conn = false;
                woj_connected = false;
                afterUpdatePO();
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("P_IM_ID",im_id);
                    headers.put("P_PRM_ID",prm_id);
                    headers.put("P_WOM_ID",wom_id_to_update);
                    headers.put("P_USER",emp_code);
                    return headers;
                }
            };

            requestQueue.add(request);
        }
        else {
            inserted_woj_id = woj_id;
            woj_connected = true;
            checkToUpdateItemData();
        }
    }

    private void checkToUpdateItemData() {
        ArrayList<PurchaseOrderItemDetailsLists> purchaseOrderItemDetailsLists = purchaseOrderSelectedRequisitionLists.get(first_index_to_update).getPurchaseOrderItemDetailsLists();
        if (purchaseOrderItemDetailsLists.size() != 0) {
            boolean allUpdated = false;
            for (int i = 0; i < purchaseOrderItemDetailsLists.size(); i++) {
                allUpdated = purchaseOrderItemDetailsLists.get(i).isUpdated();
                if (!purchaseOrderItemDetailsLists.get(i).isUpdated()) {
                    allUpdated = purchaseOrderItemDetailsLists.get(i).isUpdated();
                    String item_id = purchaseOrderItemDetailsLists.get(i).getItem_id();
                    String color_id = purchaseOrderItemDetailsLists.get(i).getColor_id();
                    if (color_id.isEmpty()) {
                        color_id = "1";
                    }
                    String size_id = purchaseOrderItemDetailsLists.get(i).getSize_id();
                    if (size_id.isEmpty()) {
                        size_id = "1";
                    }
                    String prd_id = purchaseOrderItemDetailsLists.get(i).getPrd_id();
                    String vat_pct = purchaseOrderItemDetailsLists.get(i).getItem_vat_percentage();
                    String vat_pct_amnt = purchaseOrderItemDetailsLists.get(i).getItem_vat();
                    String order_qty = purchaseOrderItemDetailsLists.get(i).getOrder_qty();
                    String unit_price = purchaseOrderItemDetailsLists.get(i).getCostprice();
                    String wod_id = purchaseOrderItemDetailsLists.get(i).getWod_id();

                    updateItemData(purchaseOrderItemDetailsLists,color_id, item_id,prd_id,size_id,vat_pct,vat_pct_amnt,order_qty,unit_price,wod_id,i);
                    break;
                }
            }
            if (allUpdated) {
                wod_conn = true;
                wod_connected = true;
                purchaseOrderSelectedRequisitionLists.get(first_index_to_update).setUpdated(true);
                checkToUpdateRequisition();
            }
        }
        else {
            wod_conn = true;
            wod_connected = true;
            purchaseOrderSelectedRequisitionLists.get(first_index_to_update).setUpdated(true);
            checkToUpdateRequisition();
        }
    }

    public void updateItemData(ArrayList<PurchaseOrderItemDetailsLists> purchaseOrderItemDetailsLists,String color_id, String item_id, String prd_id, String size_id, String vat_pct, String vat_pct_amnt, String order_qty, String unit_price, String wod_id, int lastIndex) {
        wod_conn = false;
        wod_connected = false;
        if (wod_id.isEmpty()) {
            String url = "http://103.56.208.123:8001/apex/tterp/purchaseOrder/insertWorkOrderDtl";

            RequestQueue requestQueue = Volley.newRequestQueue(mContext);

            StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
                wod_conn = true;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String string_out = jsonObject.getString("string_out");
                    if (string_out.equals("Successfully Created")) {
                        purchaseOrderItemDetailsLists.get(lastIndex).setUpdated(true);
                        wod_connected = true;
                        checkToUpdateItemData();
                    }
                    else {
                        wod_connected = false;
                        afterUpdatePO();
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    wod_connected = false;
                    afterUpdatePO();
                }
            }, error -> {
                error.printStackTrace();
                wod_conn = false;
                wod_connected = false;
                afterUpdatePO();
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("P_PRD_ID",prd_id);
                    headers.put("P_ITEM_ID",item_id);
                    headers.put("P_COLOR_ID",color_id);
                    headers.put("P_SIZE_ID",size_id);
                    headers.put("P_VAT_PCT",vat_pct);
                    headers.put("P_VAT_PCT_AMT",vat_pct_amnt);
                    headers.put("P_USER",emp_code);
                    headers.put("P_WOD_QTY",order_qty);
                    headers.put("P_WOD_RATE",unit_price);
                    headers.put("P_WOJ_ID",inserted_woj_id);
                    headers.put("P_WOM_ID",wom_id_to_update);
                    return headers;
                }
            };

            requestQueue.add(request);
        }
        else {
            String url = "http://103.56.208.123:8001/apex/tterp/purchaseOrder/updateWorkOrderDtl";

            RequestQueue requestQueue = Volley.newRequestQueue(mContext);

            StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
                wod_conn = true;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String string_out = jsonObject.getString("string_out");
                    if (string_out.equals("Successfully Created")) {
                        purchaseOrderItemDetailsLists.get(lastIndex).setUpdated(true);
                        wod_connected = true;
                        checkToUpdateItemData();
                    }
                    else {
                        wod_connected = false;
                        afterUpdatePO();
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    wod_connected = false;
                    afterUpdatePO();
                }
            }, error -> {
                error.printStackTrace();
                wod_conn = false;
                wod_connected = false;
                afterUpdatePO();
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("P_VAT_PCT",vat_pct);
                    headers.put("P_VAT_PCT_AMT",vat_pct_amnt);
                    headers.put("P_WOD_QTY",order_qty);
                    headers.put("P_WOD_RATE",unit_price);
                    headers.put("P_WOD_ID",wod_id);
                    return headers;
                }
            };
            requestQueue.add(request);
        }
    }

    private void afterUpdatePO() {
        waitProgress.dismiss();
        if (woj_conn) {
            if (woj_connected) {
                if (wod_conn) {
                    if (wod_connected) {
                        woj_conn = false;
                        woj_connected = false;
                        wod_conn = false;
                        wod_connected = false;

                        AlertDialog dialog = new AlertDialog.Builder(mContext)
                                .setMessage("Purchase Order Updated Successfully.")
                                .setPositiveButton("Ok", null)
                                .show();

                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        positive.setOnClickListener(v -> {

                            MainMenu.dashboardFront = false;
                            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,new PurchaseOrder()).commit();
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
                            checkToUpdateItemData();
                            dialog.dismiss();
                        });

                        Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                        negative.setOnClickListener(v -> dialog.dismiss());
                    }
                }
                else {
                    AlertDialog dialog = new AlertDialog.Builder(mContext)
                            .setMessage("Failed to update Item Data for Purchase Order. Please retry or data will be lost.")
                            .setPositiveButton("Retry", null)
                            .setNegativeButton("Cancel", null)
                            .show();

                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positive.setOnClickListener(v -> {

                        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
                        waitProgress.setCancelable(false);
                        checkToUpdateItemData();
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

                    updatePurchaseOrder();
                    dialog.dismiss();
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> dialog.dismiss());
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(mContext)
                    .setMessage("Failed to update Requisition Data for Purchase Order. Please try again.")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                updatePurchaseOrder();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }

    }
}
package ttit.com.shuvo.terrainerp.fragments;

import android.app.DatePickerDialog;
import android.content.Context;

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
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rosemaryapp.amazingspinner.AmazingSpinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.WaitProgress;
import ttit.com.shuvo.terrainerp.adapters.StoreLedgerAdapter;
import ttit.com.shuvo.terrainerp.arrayList.ReceiveTypeList;
import ttit.com.shuvo.terrainerp.arrayList.StoreLedgerList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StockLedgerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StockLedgerFragment extends Fragment implements StoreLedgerAdapter.ClickedItem {

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
    Boolean isfiltered = false;

//    private Connection connection;

    TextInputEditText beginDate;
    TextInputEditText endDate;
    TextView daterange;

    AmazingSpinner categorySpinner;
    AmazingSpinner subCatSpinner;

    TextInputEditText searchItem;
    TextInputLayout searchItemLay;

    RecyclerView itemView;
    StoreLedgerAdapter storeLedgerAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<StoreLedgerList> stockItemLists;
    ArrayList<StoreLedgerList> filteredList;

    TextView opQty;
    TextView opVal;
    TextView rcvQty;
    TextView rcvVal;
    TextView transRcvQty;
    TextView transRcvVal;
    TextView saleRtnQty;
    TextView saleRtnVal;
    TextView issRtnQty;
    TextView issRtnVal;
    TextView genIssRtnQty;
    TextView genIssRtnVal;
    TextView proUpdQty;
    TextView proUpdVal;
    TextView transIssQty;
    TextView transIssVal;
    TextView saleQty;
    TextView saleVal;
    TextView rawIssQty;
    TextView rawIssVal;
    TextView purRtnQty;
    TextView purRtnVal;
    TextView strAdjQty;
    TextView strAdjVal;
    TextView genItemIssQty;
    TextView genItemIssVal;
    TextView genAssDisQty;
    TextView genAssDisVal;
    TextView closeQty;
    TextView closeVal;

    Double op_qty = 0.0;
    Double op_val = 0.0;
    Double rcv_qty = 0.0;
    Double rcv_val = 0.0;
    Double trans_rcv_qty = 0.0;
    Double trans_rcv_val = 0.0;
    Double sale_rtn_qty = 0.0;
    Double sale_rtn_val = 0.0;
    Double iss_rtn_qty = 0.0;
    Double iss_rtn_val = 0.0;
    Double gen_iss_rtn_qty = 0.0;
    Double gen_iss_rtn_val = 0.0;
    Double pro_upd_qty = 0.0;
    Double pro_upd_val = 0.0;
    Double trans_iss_qty = 0.0;
    Double trans_iss_val = 0.0;
    Double sale_qty = 0.0;
    Double sale_val = 0.0;
    Double raw_iss_qty = 0.0;
    Double raw_iss_val = 0.0;
    Double pur_rtn_qty = 0.0;
    Double pur_rtn_val = 0.0;
    Double str_adj_qty = 0.0;
    Double str_adj_val = 0.0;
    Double gen_item_iss_qty = 0.0;
    Double gen_item_iss_val = 0.0;
    Double gen_ass_qty = 0.0;
    Double gen_ass_val = 0.0;
    Double close_qty = 0.0;
    Double close_val = 0.0;



    //ArrayList<WareHouseQtyLists> wareHouseQtyLists;

    ArrayList<ReceiveTypeList> categoryLists;
    ArrayList<ReceiveTypeList> subCategoryLists;

    String firstDate = "";
    String lastDate = "";
    String categoryId = "";
    String subCategoryId = "";
    String searchingCate = "";
    String searchingSubCate = "";
    String searchingName = "";

    private int mYear, mMonth, mDay;


    Context mContext;
    public StockLedgerFragment() {
        // Required empty public constructor
    }

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
     * @return A new instance of fragment StockLedgerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StockLedgerFragment newInstance(String param1, String param2) {
        StockLedgerFragment fragment = new StockLedgerFragment();
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
        View view = inflater.inflate(R.layout.fragment_stock_ledger, container, false);

        beginDate = view.findViewById(R.id.begin_date_stock_ledger);
        endDate = view.findViewById(R.id.end_date_stock_ledger);
        daterange = view.findViewById(R.id.date_range_msg_stock_ledger);
        categorySpinner = view.findViewById(R.id.cat_type_spinner_item_stock_ledger);
        subCatSpinner = view.findViewById(R.id.sub_cat_type_spinner_item_stock_ledger);

        searchItem = view.findViewById(R.id.search_item_name_stock_item_ledger);
        searchItemLay = view.findViewById(R.id.search_item_name_lay_stock_item_ledger);

        itemView = view.findViewById(R.id.item_overview_relation_store_ledger);

        opQty = view.findViewById(R.id.total_op_qty);
        opVal = view.findViewById(R.id.total_op_val);
        rcvQty = view.findViewById(R.id.total_rcv_qty);
        rcvVal = view.findViewById(R.id.total_rcv_val);
        transRcvQty = view.findViewById(R.id.total_trans_rcv_qty);
        transRcvVal = view.findViewById(R.id.total_trans_rcv_val);
        saleRtnQty = view.findViewById(R.id.total_sales_rtn_qty);
        saleRtnVal = view.findViewById(R.id.total_sales_rtn_val);
        issRtnQty = view.findViewById(R.id.total_issue_rtn_qty);
        issRtnVal = view.findViewById(R.id.total_issue_rtn_val);
        genIssRtnQty = view.findViewById(R.id.total_gen_issue_rtn_qty);
        genIssRtnVal = view.findViewById(R.id.total_gen_issue_rtn_val);
        proUpdQty = view.findViewById(R.id.total_prod_upd_qty);
        proUpdVal = view.findViewById(R.id.total_prod_upd_val);
        transIssQty = view.findViewById(R.id.total_trans_issue_qty);
        transIssVal = view.findViewById(R.id.total_trans_issue_val);
        saleQty = view.findViewById(R.id.total_sales_qty);
        saleVal = view.findViewById(R.id.total_sales_val);
        rawIssQty = view.findViewById(R.id.total_raw_issue_qty);
        rawIssVal = view.findViewById(R.id.total_raw_issue_val);
        purRtnQty = view.findViewById(R.id.total_pur_rtn_qty);
        purRtnVal = view.findViewById(R.id.total_pur_rtn_val);
        strAdjQty = view.findViewById(R.id.total_str_adj_qty);
        strAdjVal = view.findViewById(R.id.total_str_adj_val);
        genItemIssQty = view.findViewById(R.id.total_gen_item_issue_qty);
        genItemIssVal = view.findViewById(R.id.total_gen_item_issue_val);
        genAssDisQty = view.findViewById(R.id.total_gen_asset_disp_qty);
        genAssDisVal = view.findViewById(R.id.total_gen_asset_disp_val);
        closeQty = view.findViewById(R.id.total_closing_qty);
        closeVal = view.findViewById(R.id.total_closing_val);

        categoryLists = new ArrayList<>();
        subCategoryLists = new ArrayList<>();
        stockItemLists = new ArrayList<>();
        filteredList = new ArrayList<>();

        itemView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        itemView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(itemView.getContext(),DividerItemDecoration.VERTICAL);
        itemView.addItemDecoration(dividerItemDecoration);

        // Selecting Category
        categorySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                System.out.println(position+": "+name);
                subCatSpinner.setText("");
                searchingSubCate = "";
                for (int i = 0; i <categoryLists.size(); i++) {
                    if (name.equals(categoryLists.get(i).getType())) {
                        categoryId = categoryLists.get(i).getId();
                        if (categoryId.isEmpty()) {
                            searchingCate = "";
                        } else {
                            searchingCate = categoryLists.get(i).getType();
                        }

                    }
                }
//                System.out.println(categoryId);
//                subCategoryId = "";
                filterCate(searchingCate);
//                new SubCategoryCheck().execute();
                getSubCategory();
//                afterSubCatSelect.setVisibility(View.GONE);
//                toSeCat.setText(toSeCatMsg);
//                toSeSubCat.setText(toSeSubCatMSG);
//                afterItem.setVisibility(View.GONE);
//                toSeItem.setText(toSeItemMSG);
            }
        });

        // Selecting Sub Category
        subCatSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                System.out.println(position+": "+name);
                for (int i = 0; i <subCategoryLists.size(); i++) {
                    if (name.equals(subCategoryLists.get(i).getType())) {
                        subCategoryId = subCategoryLists.get(i).getId();
                        if (subCategoryId.isEmpty()) {
                            searchingSubCate = "";
                        } else {
                            searchingSubCate = subCategoryLists.get(i).getType();
                        }
                    }
                }
                filterSubCate(searchingSubCate);
//                System.out.println(subCategoryId);
//                fromSub = true;
//                new ReOrderFragment.ReOrderLevelCheck().execute();
//                afterCatSelect.setVisibility(View.GONE);
//                afterSubCatSelect.setVisibility(View.GONE);
//                toSeCat.setText(toSeCatMsg);
//                toSeSubCat.setText(toSeSubCatMSG);
//                afterItem.setVisibility(View.GONE);
//                toSeItem.setText(toSeItemMSG);
            }
        });


        searchItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                searchingName = s.toString();
                filter(s.toString());
            }
        });

        searchItem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

        // Getting Date
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM-yy",Locale.getDefault());

        firstDate = simpleDateFormat.format(c);
        firstDate = "01-"+firstDate;
        lastDate = df.format(c);

        beginDate.setText(firstDate);
        endDate.setText(lastDate);

        beginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

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
                            beginDate.setText(dayOfMonthName + "-" + monthName + "-" + yearName);
                            firstDate = beginDate.getText().toString();
                            if (lastDate.isEmpty()) {
                                daterange.setVisibility(View.GONE);
//                                new ItemCheck().execute();
                                getItemData();
                            } else {
                                Date bDate = null;
                                Date eDate = null;

                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());

                                try {
                                    bDate = sdf.parse(firstDate);
                                    eDate = sdf.parse(lastDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                if (bDate != null && eDate != null) {
                                    if (eDate.after(bDate)|| eDate.equals(bDate)) {
                                        //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                                        daterange.setVisibility(View.GONE);

//                                        new ItemCheck().execute();
                                        getItemData();

                                    }
                                    else {
                                        daterange.setVisibility(View.VISIBLE);
                                        beginDate.setText("");
                                    }
                                }
                            }

                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

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
                            endDate.setText(dayOfMonthName + "-" + monthName + "-" + yearName);
                            lastDate = endDate.getText().toString();

                            if (firstDate.isEmpty()) {
                                daterange.setVisibility(View.GONE);
//                                new ItemCheck().execute();
                                getItemData();
                            } else {
                                Date bDate = null;
                                Date eDate = null;

                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());

                                try {
                                    bDate = sdf.parse(firstDate);
                                    eDate = sdf.parse(lastDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                if (bDate != null && eDate != null) {
                                    if (eDate.after(bDate)|| eDate.equals(bDate)) {
                                        //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                                        daterange.setVisibility(View.GONE);

//                                        new ItemCheck().execute();
                                        getItemData();


                                    }
                                    else {
                                        daterange.setVisibility(View.VISIBLE);
                                        endDate.setText("");

                                    }
                                }
                            }

                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });


//        new Check().execute();
        getCategory();
        return view;
    }

    private void declareValue() {
         op_qty = 0.0;
         op_val = 0.0;
         rcv_qty = 0.0;
         rcv_val = 0.0;
         trans_rcv_qty = 0.0;
         trans_rcv_val = 0.0;
         sale_rtn_qty = 0.0;
         sale_rtn_val = 0.0;
         iss_rtn_qty = 0.0;
         iss_rtn_val = 0.0;
         gen_iss_rtn_qty = 0.0;
         gen_iss_rtn_val = 0.0;
         pro_upd_qty = 0.0;
         pro_upd_val = 0.0;
         trans_iss_qty = 0.0;
         trans_iss_val = 0.0;
         sale_qty = 0.0;
         sale_val = 0.0;
         raw_iss_qty = 0.0;
         raw_iss_val = 0.0;
         pur_rtn_qty = 0.0;
         pur_rtn_val = 0.0;
         str_adj_qty = 0.0;
         str_adj_val = 0.0;
         gen_item_iss_qty = 0.0;
         gen_item_iss_val = 0.0;
         gen_ass_qty = 0.0;
         gen_ass_val = 0.0;
         close_qty = 0.0;
         close_val = 0.0;
    }

    private void totalOfAll() {
        declareValue();
        if (isfiltered) {
            for (StoreLedgerList item: filteredList) {

                op_qty = op_qty + Double.parseDouble(item.getOp_qty_9());
                op_val = op_val + Double.parseDouble(item.getOp_val_8());
                rcv_qty = rcv_qty + Double.parseDouble(item.getRcv_qty_10());
                rcv_val = rcv_val + Double.parseDouble(item.getRcv_val_11());
                trans_rcv_qty = trans_rcv_qty + Double.parseDouble(item.getTransfer_rcv_qty_18());
                trans_rcv_val = trans_rcv_val + Double.parseDouble(item.getTransfer_rcv_val_19());
                sale_rtn_qty = sale_rtn_qty + Double.parseDouble(item.getSales_rtn_qty_12());
                sale_rtn_val = sale_rtn_val + Double.parseDouble(item.getSales_rtn_val_13());
                iss_rtn_qty = iss_rtn_qty + Double.parseDouble(item.getIssue_rtn_qty_16());
                iss_rtn_val = iss_rtn_val + Double.parseDouble(item.getIssue_rtn_val_17());
                gen_iss_rtn_qty = gen_iss_rtn_qty + Double.parseDouble(item.getGen_issue_rtn_qty_20());
                gen_iss_rtn_val = gen_iss_rtn_val + Double.parseDouble(item.getGen_issue_rtn_val_21());
                pro_upd_qty = pro_upd_qty + Double.parseDouble(item.getPro_update_qty_14());
                pro_upd_val = pro_upd_val + Double.parseDouble(item.getPro_update_val_15());
                trans_iss_qty = trans_iss_qty + Double.parseDouble(item.getTransfer_issue_r_qty_36());
                trans_iss_val = trans_iss_val + Double.parseDouble(item.getTransfer_issue_r_val_37());
                sale_qty = sale_qty + Double.parseDouble(item.getSales_qty_22());
                sale_val = sale_val + Double.parseDouble(item.getSales_val_23());
                raw_iss_qty = raw_iss_qty + Double.parseDouble(item.getRaw_issue_qty_30());
                raw_iss_val = raw_iss_val + Double.parseDouble(item.getRaw_issue_val_31());
                pur_rtn_qty = pur_rtn_qty + Double.parseDouble(item.getPur_rtn_qty_26());
                pur_rtn_val = pur_rtn_val + Double.parseDouble(item.getPur_rtn_val_27());
                str_adj_qty = str_adj_qty + Double.parseDouble(item.getStore_adj_qty_28());
                str_adj_val = str_adj_val + Double.parseDouble(item.getStore_adj_val_29());
                gen_item_iss_qty = gen_item_iss_qty + Double.parseDouble(item.getGen_item_issue_qty_32());
                gen_item_iss_val = gen_item_iss_val + Double.parseDouble(item.getGen_item_issue_val_33());
                gen_ass_qty = gen_ass_qty + Double.parseDouble(item.getGen_asset_qty_34());
                gen_ass_val = gen_ass_val + Double.parseDouble(item.getGen_asset_val_35());
                close_qty = close_qty + Double.parseDouble(item.getClosing_qty_38());
                close_val = close_val + Double.parseDouble(item.getClosing_val_39());

            }
        } else {
            for (StoreLedgerList item : stockItemLists) {
                op_qty = op_qty + Double.parseDouble(item.getOp_qty_9());
                op_val = op_val + Double.parseDouble(item.getOp_val_8());
                rcv_qty = rcv_qty + Double.parseDouble(item.getRcv_qty_10());
                rcv_val = rcv_val + Double.parseDouble(item.getRcv_val_11());
                trans_rcv_qty = trans_rcv_qty + Double.parseDouble(item.getTransfer_rcv_qty_18());
                trans_rcv_val = trans_rcv_val + Double.parseDouble(item.getTransfer_rcv_val_19());
                sale_rtn_qty = sale_rtn_qty + Double.parseDouble(item.getSales_rtn_qty_12());
                sale_rtn_val = sale_rtn_val + Double.parseDouble(item.getSales_rtn_val_13());
                iss_rtn_qty = iss_rtn_qty + Double.parseDouble(item.getIssue_rtn_qty_16());
                iss_rtn_val = iss_rtn_val + Double.parseDouble(item.getIssue_rtn_val_17());
                gen_iss_rtn_qty = gen_iss_rtn_qty + Double.parseDouble(item.getGen_issue_rtn_qty_20());
                gen_iss_rtn_val = gen_iss_rtn_val + Double.parseDouble(item.getGen_issue_rtn_val_21());
                pro_upd_qty = pro_upd_qty + Double.parseDouble(item.getPro_update_qty_14());
                pro_upd_val = pro_upd_val + Double.parseDouble(item.getPro_update_val_15());
                trans_iss_qty = trans_iss_qty + Double.parseDouble(item.getTransfer_issue_r_qty_36());
                trans_iss_val = trans_iss_val + Double.parseDouble(item.getTransfer_issue_r_val_37());
                sale_qty = sale_qty + Double.parseDouble(item.getSales_qty_22());
                sale_val = sale_val + Double.parseDouble(item.getSales_val_23());
                raw_iss_qty = raw_iss_qty + Double.parseDouble(item.getRaw_issue_qty_30());
                raw_iss_val = raw_iss_val + Double.parseDouble(item.getRaw_issue_val_31());
                pur_rtn_qty = pur_rtn_qty + Double.parseDouble(item.getPur_rtn_qty_26());
                pur_rtn_val = pur_rtn_val + Double.parseDouble(item.getPur_rtn_val_27());
                str_adj_qty = str_adj_qty + Double.parseDouble(item.getStore_adj_qty_28());
                str_adj_val = str_adj_val + Double.parseDouble(item.getStore_adj_val_29());
                gen_item_iss_qty = gen_item_iss_qty + Double.parseDouble(item.getGen_item_issue_qty_32());
                gen_item_iss_val = gen_item_iss_val + Double.parseDouble(item.getGen_item_issue_val_33());
                gen_ass_qty = gen_ass_qty + Double.parseDouble(item.getGen_asset_qty_34());
                gen_ass_val = gen_ass_val + Double.parseDouble(item.getGen_asset_val_35());
                close_qty = close_qty + Double.parseDouble(item.getClosing_qty_38());
                close_val = close_val + Double.parseDouble(item.getClosing_val_39());
            }
        }

         opQty.setText(String.valueOf(op_qty));
         opVal.setText(String.valueOf(op_val));
         rcvQty.setText(String.valueOf(rcv_qty));
         rcvVal.setText(String.valueOf(rcv_val));
         transRcvQty.setText(String.valueOf(trans_rcv_qty));
         transRcvVal.setText(String.valueOf(trans_rcv_val));
         saleRtnQty.setText(String.valueOf(sale_rtn_qty));
         saleRtnVal.setText(String.valueOf(sale_rtn_val));
         issRtnQty.setText(String.valueOf(iss_rtn_qty));
         issRtnVal.setText(String.valueOf(iss_rtn_val));
         genIssRtnQty.setText(String.valueOf(gen_iss_rtn_qty));
         genIssRtnVal.setText(String.valueOf(gen_iss_rtn_val));
         proUpdQty.setText(String.valueOf(pro_upd_qty));
         proUpdVal.setText(String.valueOf(pro_upd_val));
         transIssQty.setText(String.valueOf(trans_iss_qty));
         transIssVal.setText(String.valueOf(trans_iss_val));
         saleQty.setText(String.valueOf(sale_qty));
         saleVal.setText(String.valueOf(sale_val));
         rawIssQty.setText(String.valueOf(raw_iss_qty));
         rawIssVal.setText(String.valueOf(raw_iss_val));
         purRtnQty.setText(String.valueOf(pur_rtn_qty));
         purRtnVal.setText(String.valueOf(pur_rtn_val));
         strAdjQty.setText(String.valueOf(str_adj_qty));
         strAdjVal.setText(String.valueOf(str_adj_val));
         genItemIssQty.setText(String.valueOf(gen_item_iss_qty));
         genItemIssVal.setText(String.valueOf(gen_item_iss_val));
         genAssDisQty.setText(String.valueOf(gen_ass_qty));
         genAssDisVal.setText(String.valueOf(gen_ass_val));
         closeQty.setText(String.valueOf(close_qty));
         closeVal.setText(String.valueOf(close_val));

    }

    private void closeKeyBoard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void filter(String text) {

        filteredList = new ArrayList<>();
        for (StoreLedgerList item : stockItemLists) {
            if (searchingSubCate.isEmpty()) {
                if (searchingCate.isEmpty()) {
                    if (item.getItem_name_2().toLowerCase().contains(text.toLowerCase()) || item.getItem_code_1().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add((item));
                        isfiltered = true;
                    }
                } else {
                    if (item.getCatName_42().toLowerCase().contains(searchingCate.toLowerCase())) {
                        if (item.getItem_name_2().toLowerCase().contains(text.toLowerCase()) || item.getItem_code_1().toLowerCase().contains(text.toLowerCase())) {
                            filteredList.add((item));
                            isfiltered = true;
                        }
                    }
                }
            } else {
                if (searchingCate.isEmpty()) {
                    if (item.getSubCateName_43().toLowerCase().contains(searchingSubCate.toLowerCase())) {
                        if (item.getItem_name_2().toLowerCase().contains(text.toLowerCase()) || item.getItem_code_1().toLowerCase().contains(text.toLowerCase())) {
                            filteredList.add((item));
                            isfiltered = true;

                        }
                    }
                } else {
                    if (item.getCatName_42().toLowerCase().contains(searchingCate.toLowerCase())) {
                        if (item.getSubCateName_43().toLowerCase().contains(searchingSubCate.toLowerCase())) {
                            if (item.getItem_name_2().toLowerCase().contains(text.toLowerCase()) || item.getItem_code_1().toLowerCase().contains(text.toLowerCase())) {
                                filteredList.add((item));
                                isfiltered = true;

                            }
                        }
                    }
                }

            }

        }
        storeLedgerAdapter.filterList(filteredList);
        totalOfAll();
    }

    private void filterCate(String text) {

        filteredList = new ArrayList<>();
        for (StoreLedgerList item : stockItemLists) {
            if (searchingSubCate.isEmpty()){
                if (searchingName.isEmpty()) {
                    if (item.getCatName_42().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add((item));
                        isfiltered = true;
                    }
                } else {
                    if (item.getItem_name_2().toLowerCase().contains(searchingName.toLowerCase()) || item.getItem_code_1().toLowerCase().contains(searchingName.toLowerCase())) {
                        if (item.getCatName_42().toLowerCase().contains(text.toLowerCase())) {
                            filteredList.add((item));
                            isfiltered = true;
                        }
                    }
                }
            } else {
                if (searchingName.isEmpty()) {
                    if (item.getSubCateName_43().toLowerCase().contains(searchingSubCate.toLowerCase())) {
                        if (item.getCatName_42().toLowerCase().contains(text.toLowerCase())) {
                            filteredList.add((item));
                            isfiltered = true;
                        }
                    }
                } else {
                    if (item.getSubCateName_43().toLowerCase().contains(searchingSubCate.toLowerCase())) {
                        if (item.getItem_name_2().toLowerCase().contains(searchingName.toLowerCase()) || item.getItem_code_1().toLowerCase().contains(searchingName.toLowerCase())) {
                            if (item.getCatName_42().toLowerCase().contains(text.toLowerCase())) {
                                filteredList.add((item));
                                isfiltered = true;
                            }
                        }
                    }
                }
            }
//            if (searchingName.isEmpty()) {
//                if (item.getDep_name().toLowerCase().contains(text.toLowerCase())) {
//                    filteredList.add((item));
//                }
//            } else {
//                if (item.getEmp_name().toLowerCase().contains(searchingName.toLowerCase())) {
//                    if (item.getDep_name().toLowerCase().contains(text.toLowerCase())) {
//                        filteredList.add((item));
//                    }
//                }
//            }

        }
        storeLedgerAdapter.filterList(filteredList);
        totalOfAll();
    }

    private void filterSubCate(String text) {

        filteredList = new ArrayList<>();
        for (StoreLedgerList item : stockItemLists) {
            if (searchingCate.isEmpty()) {
                if (searchingName.isEmpty()) {
                    if (item.getSubCateName_43().toLowerCase().contains(text.toLowerCase())){
                        filteredList.add((item));
                        isfiltered = true;
                    }
                } else {
                    if (item.getItem_name_2().toLowerCase().contains(searchingName.toLowerCase()) || item.getItem_code_1().toLowerCase().contains(searchingName.toLowerCase())){
                        if (item.getSubCateName_43().toLowerCase().contains(text.toLowerCase())){
                            filteredList.add((item));
                            isfiltered = true;
                        }
                    }
                }
            } else {
                if (searchingName.isEmpty()) {
                    if (item.getCatName_42().toLowerCase().contains(searchingCate.toLowerCase())){
                        if (item.getSubCateName_43().toLowerCase().contains(text.toLowerCase())){
                            filteredList.add((item));
                            isfiltered = true;
                        }
                    }
                } else {
                    if (item.getCatName_42().toLowerCase().contains(searchingCate.toLowerCase())){
                        if (item.getItem_name_2().toLowerCase().contains(searchingName.toLowerCase()) || item.getItem_code_1().toLowerCase().contains(searchingName.toLowerCase())){
                            if (item.getSubCateName_43().toLowerCase().contains(text.toLowerCase())){
                                filteredList.add((item));
                                isfiltered = true;
                            }
                        }
                    }
                }
            }

        }
        storeLedgerAdapter.filterList(filteredList);
        totalOfAll();
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
//                CategoryData();
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
//                //String[] type = new String[] {"Approved", "Pending","Both"};
//                ArrayList<String> type = new ArrayList<>();
//                for(int i = 0; i < categoryLists.size(); i++) {
//                    type.add(categoryLists.get(i).getType());
//                }
//
//                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);
//
//                categorySpinner.setAdapter(arrayAdapter);
//
//                new ItemCheck().execute();
//
////                new ReOrderFragment.ReOrderLevelCheck().execute();
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

//    public class SubCategoryCheck extends AsyncTask<Void, Void, Void> {
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
//                SubCategoryData();
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
//                //String[] type = new String[] {"Approved", "Pending","Both"};
//                ArrayList<String> type = new ArrayList<>();
//                for(int i = 0; i < subCategoryLists.size(); i++) {
//                    type.add(subCategoryLists.get(i).getType());
//                }
//
//                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);
//
//                subCatSpinner.setAdapter(arrayAdapter);
//
//                //new ReOrderLevelCheck().execute();
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
//                        new SubCategoryCheck().execute();
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

//    public class ItemCheck extends AsyncTask<Void, Void, Void> {
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
//                isfiltered = false;
//                categorySpinner.setText("");
//                subCatSpinner.setText("");
//                searchingCate = "";
//                searchingName = "";
//                searchingSubCate = "";
//
//                subCategoryLists = new ArrayList<>();
//
//                ArrayList<String> type = new ArrayList<>();
//                for(int i = 0; i < subCategoryLists.size(); i++) {
//                    type.add(subCategoryLists.get(i).getType());
//                }
//
//                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);
//
//                subCatSpinner.setAdapter(arrayAdapter);
//
//
//                storeLedgerAdapter = new StoreLedgerAdapter(stockItemLists, getContext(),StockLedgerFragment.this);
//                itemView.setAdapter(storeLedgerAdapter);
//                searchItem.setText("");
//                totalOfAll();
//
//
////                scrollView.fullScroll(View.FOCUS_DOWN);
////
//////                System.out.println(searchItem.getBaseline()+" : "+ searchItem.getBottom());
//////                scrollView.scrollTo(searchItem.getBottom(),searchItem.getBottom());
////
////                scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
////                    @Override
////                    public void onGlobalLayout() {
////                        scrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
////                        scrollView.fullScroll(View.FOCUS_DOWN);
////                        searchItem.clearFocus();
//////                        scrollView.scrollTo(searchItem.getBottom(),searchItem.getBottom());
////
//////                        scrollView.pageScroll(View.FOCUS_DOWN);
////                    }
////                });
//
//                searchItem.clearFocus();
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
//                        new ItemCheck().execute();
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

//    public void CategoryData() {
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//
//            Statement stmt = connection.createStatement();
//
//            categoryLists = new ArrayList<>();
//
//
//
//            ResultSet resultSet1 = stmt.executeQuery("SELECT DISTINCT V.IM_ID, V.IM_NAME\n" +
//                    "  FROM ITEM_DETAILS_V V");
//
//            while (resultSet1.next()) {
//                categoryLists.add(new ReceiveTypeList(resultSet1.getString(1),resultSet1.getString(2)));
//            }
//            categoryLists.add(new ReceiveTypeList("","All Categories"));
//
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
                    categoryLists.add(new ReceiveTypeList("","All Categories",""));
                }
                getItems(1);
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

                //String[] type = new String[] {"Approved", "Pending","Both"};
                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < categoryLists.size(); i++) {
                    type.add(categoryLists.get(i).getType());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                categorySpinner.setAdapter(arrayAdapter);

                updateItemLay();
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

    public void getItems(int parser) {
        stockItemLists = new ArrayList<>();
        String url = "http://103.56.208.123:8001/apex/tterp/storeLedger/getStoreLedger?st_date="+firstDate+"&end_date="+lastDate+"&cat_id=&sub_cat_id=&item_id=";
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
                        String store_ledger = info.getString("store_ledger");
                        JSONArray storeArray = new JSONArray(store_ledger);
                        for (int j = 0; j < storeArray.length(); j++) {
                            JSONObject store_info = storeArray.getJSONObject(j);

                            String item_code = store_info.getString("item_code");
                            String item_reference_name = store_info.getString("item_reference_name");
//                            String size_name = store_info.getString("size_name")
//                                    .equals("null") ? "" : store_info.getString("size_name");

//                            String color = store_info.getString("color")
//                                    .equals("null") ? "" : store_info.getString("color");

                            String hs_code = store_info.getString("hs_code")
                                    .equals("null") ? "" : store_info.getString("hs_code");

//                            String part_no = store_info.getString("part_no")
//                                    .equals("null") ? "" : store_info.getString("part_no");

                            String item_unit = store_info.getString("item_unit")
                                    .equals("null") ? "" : store_info.getString("item_unit");

                            String op_val = store_info.getString("op_val")
                                    .equals("null") ? "0" : store_info.getString("op_val");

                            String op_qty = store_info.getString("op_qty")
                                    .equals("null") ? "0" : store_info.getString("op_qty");

                            String receive_qty = store_info.getString("receive_qty")
                                    .equals("null") ? "0" : store_info.getString("receive_qty");

                            String receive_val = store_info.getString("receive_val")
                                    .equals("null") ? "0" : store_info.getString("receive_val");

                            String sales_rtn_qty = store_info.getString("sales_rtn_qty")
                                    .equals("null") ? "0" : store_info.getString("sales_rtn_qty");

                            String sales_rtn_val = store_info.getString("sales_rtn_val")
                                    .equals("null") ? "0" : store_info.getString("sales_rtn_val");

                            String prod_up_qty = store_info.getString("prod_up_qty")
                                    .equals("null") ? "0" : store_info.getString("prod_up_qty");

                            String prod_up_val = store_info.getString("prod_up_val")
                                    .equals("null") ? "0" : store_info.getString("prod_up_val");

                            String issue_ret_qty = store_info.getString("issue_ret_qty")
                                    .equals("null") ? "0" : store_info.getString("issue_ret_qty");

                            String issue_ret_val = store_info.getString("issue_ret_val")
                                    .equals("null") ? "0" : store_info.getString("issue_ret_val");

                            String transfer_ran_rcv_qty = store_info.getString("transfer_ran_rcv_qty")
                                    .equals("null") ? "0" : store_info.getString("transfer_ran_rcv_qty");

                            String transfer_ran_rcv_val = store_info.getString("transfer_ran_rcv_val")
                                    .equals("null") ? "0" : store_info.getString("transfer_ran_rcv_val");

                            String gi_issueret_rcv_qty = store_info.getString("gi_issueret_rcv_qty")
                                    .equals("null") ? "0" : store_info.getString("gi_issueret_rcv_qty");

                            String gi_issueret_rcv_val = store_info.getString("gi_issueret_rcv_val")
                                    .equals("null") ? "0" : store_info.getString("gi_issueret_rcv_val");

                            String issue_sales_qty = store_info.getString("issue_sales_qty")
                                    .equals("null") ? "0" : store_info.getString("issue_sales_qty");

                            String issue_sales_val = store_info.getString("issue_sales_val")
                                    .equals("null") ? "0" : store_info.getString("issue_sales_val");

//                            String trans_issue_qty = store_info.getString("trans_issue_qty")
//                                    .equals("null") ? "" : store_info.getString("trans_issue_qty");
//
//                            String trans_issue_val = store_info.getString("trans_issue_val")
//                                    .equals("null") ? "" : store_info.getString("trans_issue_val");

                            String pur_rtn_qty = store_info.getString("pur_rtn_qty")
                                    .equals("null") ? "0" : store_info.getString("pur_rtn_qty");

                            String pur_rtn_val = store_info.getString("pur_rtn_val")
                                    .equals("null") ? "0" : store_info.getString("pur_rtn_val");

                            String store_adj_qty = store_info.getString("store_adj_qty")
                                    .equals("null") ? "0" : store_info.getString("store_adj_qty");

                            String store_adj_val = store_info.getString("store_adj_val")
                                    .equals("null") ? "0" : store_info.getString("store_adj_val");

                            String issue_raw_qty = store_info.getString("issue_raw_qty")
                                    .equals("null") ? "0" : store_info.getString("issue_raw_qty");

                            String issue_raw_val = store_info.getString("issue_raw_val")
                                    .equals("null") ? "0" : store_info.getString("issue_raw_val");

                            String issue_gen_item_qty = store_info.getString("issue_gen_item_qty")
                                    .equals("null") ? "0" : store_info.getString("issue_gen_item_qty");

                            String issue_gen_item_val = store_info.getString("issue_gen_item_val")
                                    .equals("null") ? "0" : store_info.getString("issue_gen_item_val");

                            String issue_gen_asset_qty = store_info.getString("issue_gen_asset_qty")
                                    .equals("null") ? "0" : store_info.getString("issue_gen_asset_qty");

                            String issue_gen_asset_val = store_info.getString("issue_gen_asset_val")
                                    .equals("null") ? "0" : store_info.getString("issue_gen_asset_val");

                            String transfer_ran_issue_qty = store_info.getString("transfer_ran_issue_qty")
                                    .equals("null") ? "0" : store_info.getString("transfer_ran_issue_qty");

                            String transfer_ran_issue_val = store_info.getString("transfer_ran_issue_val")
                                    .equals("null") ? "0" : store_info.getString("transfer_ran_issue_val");

                            String cls_qty = store_info.getString("cls_qty")
                                    .equals("null") ? "0" : store_info.getString("cls_qty");

                            String cls_val = store_info.getString("cls_val")
                                    .equals("null") ? "0" : store_info.getString("cls_val");

//                            String item_id = store_info.getString("item_id")
//                                    .equals("null") ? "" : store_info.getString("item_id");
//
//                            String im_id = store_info.getString("im_id")
//                                    .equals("null") ? "" : store_info.getString("im_id");

                            String category = store_info.getString("category")
                                    .equals("null") ? "" : store_info.getString("category");

                            String subcatm_name = store_info.getString("subcatm_name")
                                    .equals("null") ? "" : store_info.getString("subcatm_name");


                            stockItemLists.add(new StoreLedgerList(String.valueOf(j+1),item_reference_name,item_code,
                                    hs_code,item_unit,op_qty,
                                    op_val,receive_qty,receive_val,
                                    transfer_ran_rcv_qty,transfer_ran_rcv_val,sales_rtn_qty,
                                    sales_rtn_val,issue_ret_qty,issue_ret_val,
                                    gi_issueret_rcv_qty,gi_issueret_rcv_val,prod_up_qty,
                                    prod_up_val,transfer_ran_issue_qty,transfer_ran_issue_val,
                                    issue_sales_qty,issue_sales_val,issue_raw_qty,
                                    issue_raw_val,pur_rtn_qty,pur_rtn_val,
                                    store_adj_qty,store_adj_val,issue_gen_item_qty,
                                    issue_gen_item_val,issue_gen_asset_qty,issue_gen_asset_val,
                                    cls_qty,cls_val,category,
                                    subcatm_name));

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

        requestQueue.add(request);
    }

    private void updateItemLay() {
        isfiltered = false;
        categorySpinner.setText("");
        subCatSpinner.setText("");
        searchingCate = "";
        searchingName = "";
        searchingSubCate = "";

        subCategoryLists = new ArrayList<>();

        ArrayList<String> type = new ArrayList<>();
        for(int i = 0; i < subCategoryLists.size(); i++) {
            type.add(subCategoryLists.get(i).getType());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

        subCatSpinner.setAdapter(arrayAdapter);

        storeLedgerAdapter = new StoreLedgerAdapter(stockItemLists, getContext(),StockLedgerFragment.this);
        itemView.setAdapter(storeLedgerAdapter);
        searchItem.setText("");
        totalOfAll();

        searchItem.clearFocus();
    }

//    public void SubCategoryData() {
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//
//            Statement stmt = connection.createStatement();
//
//            subCategoryLists = new ArrayList<>();
//
//            if (categoryId.isEmpty()) {
//                categoryId = null;
//            }
//
//            ResultSet resultSet1 = stmt.executeQuery("SELECT DISTINCT V.ISC_ID, V.SUBCATM_NAME\n" +
//                    "  FROM ITEM_DETAILS_V V where V.IM_ID = "+categoryId+"");
//
//            while (resultSet1.next()) {
//                subCategoryLists.add(new ReceiveTypeList(resultSet1.getString(1),resultSet1.getString(2)));
//            }
//            subCategoryLists.add(new ReceiveTypeList("","All Sub Categories"));
//
//            if (categoryId == null) {
//                subCategoryLists = new ArrayList<>();
//                categoryId = "";
//            }
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

    public void getSubCategory() {
        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        subCategoryLists = new ArrayList<>();

        String subCatUrl = "http://103.56.208.123:8001/apex/tterp/utility/getSubCategory?cat_id="+categoryId+"";
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest subCatReq = new StringRequest(Request.Method.GET, subCatUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);

                        String isc_id = info.getString("isc_id");
                        String subcatm_name = info.getString("subcatm_name");

                        subCategoryLists.add(new ReceiveTypeList(isc_id,subcatm_name,""));
                    }
                    subCategoryLists.add(new ReceiveTypeList("","All Sub Categories",""));
                }
                connected = true;
                updateSubLayout();
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateSubLayout();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateSubLayout();
        });

        requestQueue.add(subCatReq);

    }

    private void updateSubLayout() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                //String[] type = new String[] {"Approved", "Pending","Both"};
                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < subCategoryLists.size(); i++) {
                    type.add(subCategoryLists.get(i).getType());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                subCatSpinner.setAdapter(arrayAdapter);
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

                    getSubCategory();
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

                getSubCategory();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }

//    public void ItemData() {
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//
//            stockItemLists = new ArrayList<>();
//
////            Statement stmt = connection.createStatement();
////
////            if (firstDate.isEmpty()) {
////                firstDate = null;
////            }
////            if (lastDate.isEmpty()) {
////                lastDate = null;
////            }
////            if (categoryId.isEmpty()) {
////                categoryId = null;
////            }
////            if (subCategoryId.isEmpty()) {
////                subCategoryId = null;
////            }
//
//            CallableStatement callableStatement = connection.prepareCall("begin ? := GET_STORE_LEDGER(?,?,?,?,?); end;");
//            callableStatement.setString(2,firstDate);
//            callableStatement.setString(3,lastDate);
//            callableStatement.setString(4,null);
//            callableStatement.setString(5,null);
//            callableStatement.setString(6,null);
//            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
//            callableStatement.execute();
//
//            ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
//
//            int i = 0;
//            while (resultSet.next()) {
//                i++;
//
//                stockItemLists.add(new StoreLedgerList(String.valueOf(i),resultSet.getString(2),resultSet.getString(1),
//                        resultSet.getString(5),resultSet.getString(7),resultSet.getString(9),
//                        resultSet.getString(8),resultSet.getString(10),resultSet.getString(11),
//                        resultSet.getString(18),resultSet.getString(19),resultSet.getString(12),
//                        resultSet.getString(13),resultSet.getString(16),resultSet.getString(17),
//                        resultSet.getString(20),resultSet.getString(21),resultSet.getString(14),
//                        resultSet.getString(15),resultSet.getString(36),resultSet.getString(37),
//                        resultSet.getString(22),resultSet.getString(23),resultSet.getString(30),
//                        resultSet.getString(31),resultSet.getString(26),resultSet.getString(27),
//                        resultSet.getString(28),resultSet.getString(29),resultSet.getString(32),
//                        resultSet.getString(33),resultSet.getString(34),resultSet.getString(35),
//                        resultSet.getString(38),resultSet.getString(39),resultSet.getString(42),
//                        resultSet.getString(43)));
////                System.out.println(i);
//            }
//
//            callableStatement.close();
//            connected = true;
//
//            if (firstDate == null) {
//                firstDate = "";
//            }
//            if (lastDate == null) {
//                lastDate = "";
//            }
//            if (categoryId == null) {
//                categoryId = "";
//            }
//            if (subCategoryId == null) {
//                subCategoryId = "";
//            }
//
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
        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;
        getItems(2);
    }

    private void updateLayout() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;
                updateItemLay();
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

                    getItemData();
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

                getItemData();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }

}
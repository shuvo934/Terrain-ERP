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
import ttit.com.shuvo.terrainerp.adapters.ItemRcvIssRegAdapter;
import ttit.com.shuvo.terrainerp.arrayList.ItemRcvIssItemDescList;
import ttit.com.shuvo.terrainerp.arrayList.ItemRcvIssReglist;
import ttit.com.shuvo.terrainerp.arrayList.ItemRcvIssWarehouseList;
import ttit.com.shuvo.terrainerp.arrayList.ReceiveTypeList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemRCVISSUERegister#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemRCVISSUERegister extends Fragment implements ItemRcvIssRegAdapter.ClickedItem{

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

    AmazingSpinner supplierSpinner;
    AmazingSpinner customerSpinner;

    TextInputEditText searchItem;
    TextInputLayout searchItemLay;

    RecyclerView itemView;
    ItemRcvIssRegAdapter itemRcvIssRegAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<ItemRcvIssReglist> itemRcvIssReglists;
    ArrayList<ItemRcvIssReglist> filteredList;


    ArrayList<ReceiveTypeList> supplierLists;
    ArrayList<ReceiveTypeList> customerLists;

    Double rcv_qty = 0.0;
    Double rcv_amnt = 0.0;
    Double issue_qty = 0.0;
    Double issue_amnt = 0.0;

    TextView rcvQty;
    TextView rcvAmnt;
    TextView issueQty;
    TextView issueAmnt;

    String firstDate = "";
    String lastDate = "";
    String supplierId = "";
    String customerId = "";
    String searchingSupplier = "";
    String searchingCustomer = "";
    String searchingName = "";

    private int mYear, mMonth, mDay;


    public ItemRCVISSUERegister() {
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
     * @return A new instance of fragment ItemRCVISSUERegister.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemRCVISSUERegister newInstance(String param1, String param2) {
        ItemRCVISSUERegister fragment = new ItemRCVISSUERegister();
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
        View view = inflater.inflate(R.layout.fragment_item_r_c_v_i_s_s_u_e_register, container, false);

        beginDate = view.findViewById(R.id.begin_date_item_rcv_iss_reg);
        endDate = view.findViewById(R.id.end_date_item_rcv_iss_reg);
        daterange = view.findViewById(R.id.date_range_msg_item_rcv_iss_reg);
        supplierSpinner = view.findViewById(R.id.cat_type_spinner_item_rcv_iss_reg);
        customerSpinner = view.findViewById(R.id.sub_cat_type_spinner_item_rcv_iss_reg);

        searchItem = view.findViewById(R.id.search_item_name_stock_item_rcv_iss_reg);
        searchItemLay = view.findViewById(R.id.search_item_name_lay_stock_item_rcv_iss_reg);

        itemView = view.findViewById(R.id.item_overview_relation_item_rcv_iss_reg);

        rcvQty = view.findViewById(R.id.total_rcv_qty_irir);
        rcvAmnt = view.findViewById(R.id.total_rcv_amnt_irir);
        issueQty = view.findViewById(R.id.total_iss_qty_irir);
        issueAmnt = view.findViewById(R.id.total_iss_amnt_irir);

        supplierLists = new ArrayList<>();
        customerLists = new ArrayList<>();
        itemRcvIssReglists = new ArrayList<>();
        filteredList = new ArrayList<>();

        itemView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        itemView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(itemView.getContext(),DividerItemDecoration.VERTICAL);
        itemView.addItemDecoration(dividerItemDecoration);


        // Selecting Supplier
        supplierSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                System.out.println(position+": "+name);
                //subCatSpinner.setText("");
                //searchingSubCate = "";
                for (int i = 0; i < supplierLists.size(); i++) {
                    if (name.equals(supplierLists.get(i).getType())) {
                        supplierId = supplierLists.get(i).getId();
                        if (supplierId.isEmpty()) {
                            searchingSupplier = "";
                        } else {
                            searchingSupplier = supplierLists.get(i).getType();
                        }

                    }
                }
//                System.out.println(categoryId);
//                subCategoryId = "";

                //pore
                filterCate(searchingSupplier);

                //new SubCategoryCheck().execute();

//                afterSubCatSelect.setVisibility(View.GONE);
//                toSeCat.setText(toSeCatMsg);
//                toSeSubCat.setText(toSeSubCatMSG);
//                afterItem.setVisibility(View.GONE);
//                toSeItem.setText(toSeItemMSG);
            }
        });

        // Selecting Sub Category
        customerSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                System.out.println(position+": "+name);
                for (int i = 0; i < customerLists.size(); i++) {
                    if (name.equals(customerLists.get(i).getType())) {
                        customerId = customerLists.get(i).getId();
                        if (customerId.isEmpty()) {
                            searchingCustomer = "";
                        } else {
                            searchingCustomer = customerLists.get(i).getType();
                        }
                    }
                }

                System.out.println(searchingCustomer);
                filterSubCate(searchingCustomer);

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
                                getItemsInfo();
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
                                        getItemsInfo();
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
                                getItemsInfo();

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
                                        getItemsInfo();

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
        getSupplierAndCustomer();

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

    private void declareValue() {
         rcv_qty = 0.0;
         rcv_amnt = 0.0;
         issue_qty = 0.0;
         issue_amnt = 0.0;
    }

    private void totalOfAll() {

        declareValue();

        if (isfiltered) {

            for (ItemRcvIssReglist item: filteredList) {

                ArrayList<ItemRcvIssItemDescList> itemLists = new ArrayList<>();
                itemLists = item.getItemRcvIssItemDescLists();

                for (ItemRcvIssItemDescList item2: itemLists) {
                    ArrayList<ItemRcvIssWarehouseList> whList = new ArrayList<>();
                    whList = item2.getItemRcvIssWarehouseLists();

                    for (ItemRcvIssWarehouseList item3: whList) {

                        if (item3.getWhRcvQty() != null  ) {
                            if (!item3.getWhRcvQty().isEmpty()) {
                                rcv_qty = rcv_qty + Double.parseDouble(item3.getWhRcvQty());
                            }
                        }

                        if (item3.getWhIssQty() != null ) {
                            if (!item3.getWhIssQty().isEmpty()) {
                                issue_qty = issue_qty + Double.parseDouble(item3.getWhIssQty());
                            }

                        }

                        rcv_amnt = rcv_amnt + Double.parseDouble(item3.getWhRcvAmnt());

                        issue_amnt = issue_amnt + Double.parseDouble(item3.getWhIssAmnt());
                    }
                }



            }
        } else {
            for (ItemRcvIssReglist item : itemRcvIssReglists) {

                ArrayList<ItemRcvIssItemDescList> itemLists = new ArrayList<>();
                itemLists = item.getItemRcvIssItemDescLists();

                for (ItemRcvIssItemDescList item2: itemLists) {
                    ArrayList<ItemRcvIssWarehouseList> whList = new ArrayList<>();
                    whList = item2.getItemRcvIssWarehouseLists();

                    for (ItemRcvIssWarehouseList item3: whList) {

                        if (item3.getWhRcvQty() != null  ) {
                            if (!item3.getWhRcvQty().isEmpty()) {
                                rcv_qty = rcv_qty + Double.parseDouble(item3.getWhRcvQty());
                            }
                        }

                        if (item3.getWhIssQty() != null ) {
                            if (!item3.getWhIssQty().isEmpty()) {
                                issue_qty = issue_qty + Double.parseDouble(item3.getWhIssQty());
                            }

                        }

                        rcv_amnt = rcv_amnt + Double.parseDouble(item3.getWhRcvAmnt());

                        issue_amnt = issue_amnt + Double.parseDouble(item3.getWhIssAmnt());
                    }
                }

            }
        }

        rcvAmnt.setText(String.valueOf(rcv_amnt));
        issueQty.setText(String.valueOf(issue_qty));
        rcvQty.setText(String.valueOf(rcv_qty));
        issueAmnt.setText(String.valueOf(issue_amnt));

    }

    private void filter(String text) {

        filteredList = new ArrayList<>();
        for (ItemRcvIssReglist item : itemRcvIssReglists) {
            if (searchingCustomer.isEmpty()) {
                if (searchingSupplier.isEmpty()) {
                    if (item.getRcvNo().toLowerCase().contains(text.toLowerCase()) || item.getIssNo().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add((item));
                        isfiltered = true;
                    }
                } else {
                    if (item.getSupplier().toLowerCase().contains(searchingSupplier.toLowerCase())) {
                        if (item.getRcvNo().toLowerCase().contains(text.toLowerCase()) || item.getIssNo().toLowerCase().contains(text.toLowerCase())) {
                            filteredList.add((item));
                            isfiltered = true;
                        }
                    }
                }
            } else {
                if (searchingSupplier.isEmpty()) {
                    if (item.getCustomer() != null) {
                        if (item.getCustomer().toLowerCase().contains(searchingCustomer.toLowerCase())) {
                            if (item.getRcvNo().toLowerCase().contains(text.toLowerCase()) || item.getIssNo().toLowerCase().contains(text.toLowerCase())) {
                                filteredList.add((item));
                                isfiltered = true;

                            }
                        }
                    }

                } else {
                    if (item.getSupplier().toLowerCase().contains(searchingSupplier.toLowerCase())) {
                        if (item.getCustomer() != null) {
                            if (item.getCustomer().toLowerCase().contains(searchingCustomer.toLowerCase())) {
                                if (item.getRcvNo().toLowerCase().contains(text.toLowerCase()) || item.getIssNo().toLowerCase().contains(text.toLowerCase())) {
                                    filteredList.add((item));
                                    isfiltered = true;

                                }
                            }
                        }

                    }
                }

            }

        }
        itemRcvIssRegAdapter.filterList(filteredList);
        totalOfAll();
    }

    private void filterCate(String text) {

        filteredList = new ArrayList<>();
        for (ItemRcvIssReglist item : itemRcvIssReglists) {
            if (searchingCustomer.isEmpty()){
                if (searchingName.isEmpty()) {
                    if (item.getSupplier().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add((item));
                        isfiltered = true;
                    }
                } else {
                    if (item.getRcvNo().toLowerCase().contains(searchingName.toLowerCase()) || item.getIssNo().toLowerCase().contains(searchingName.toLowerCase())) {
                        if (item.getSupplier().toLowerCase().contains(text.toLowerCase())) {
                            filteredList.add((item));
                            isfiltered = true;
                        }
                    }
                }
            } else {
                if (searchingName.isEmpty()) {
                    if (item.getCustomer() != null) {
                        if (item.getCustomer().toLowerCase().contains(searchingCustomer.toLowerCase())) {
                            if (item.getSupplier().toLowerCase().contains(text.toLowerCase())) {
                                filteredList.add((item));
                                isfiltered = true;
                            }
                        }
                    }

                } else {
                    if (item.getCustomer() != null) {
                        if (item.getCustomer().toLowerCase().contains(searchingCustomer.toLowerCase())) {
                            if (item.getRcvNo().toLowerCase().contains(searchingName.toLowerCase()) || item.getIssNo().toLowerCase().contains(searchingName.toLowerCase())) {
                                if (item.getSupplier().toLowerCase().contains(text.toLowerCase())) {
                                    filteredList.add((item));
                                    isfiltered = true;
                                }
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
        itemRcvIssRegAdapter.filterList(filteredList);
        totalOfAll();
    }

    private void filterSubCate(String text) {

        filteredList = new ArrayList<>();
        for (ItemRcvIssReglist item : itemRcvIssReglists) {
            if (searchingSupplier.isEmpty()) {
                if (searchingName.isEmpty()) {
                    if (item.getCustomer() != null) {
                        if (item.getCustomer().toLowerCase().contains(text.toLowerCase())){
                            filteredList.add((item));
                            isfiltered = true;
                        }
                    } else if (text.isEmpty()) {
                        filteredList.add((item));
                        isfiltered = true;
                    }

                } else {
                    if (item.getRcvNo().toLowerCase().contains(searchingName.toLowerCase()) || item.getIssNo().toLowerCase().contains(searchingName.toLowerCase())){
                        if (item.getCustomer() != null) {
                            if (item.getCustomer().toLowerCase().contains(text.toLowerCase())){
                                filteredList.add((item));
                                isfiltered = true;
                            }
                        } else if (text.isEmpty()) {
                            filteredList.add((item));
                            isfiltered = true;
                        }

                    }
                }
            } else {
                if (searchingName.isEmpty()) {
                    if (item.getSupplier().toLowerCase().contains(searchingSupplier.toLowerCase())){
                        if (item.getCustomer() != null) {
                            if (item.getCustomer().toLowerCase().contains(text.toLowerCase())){
                                filteredList.add((item));
                                isfiltered = true;
                            }
                        } else if (text.isEmpty()) {
                            filteredList.add((item));
                            isfiltered = true;
                        }

                    }
                } else {
                    if (item.getSupplier().toLowerCase().contains(searchingSupplier.toLowerCase())){
                        if (item.getRcvNo().toLowerCase().contains(searchingName.toLowerCase()) || item.getIssNo().toLowerCase().contains(searchingName.toLowerCase())){
                            if (item.getCustomer() != null) {
                                if (item.getCustomer().toLowerCase().contains(text.toLowerCase())){
                                    filteredList.add((item));
                                    isfiltered = true;
                                }
                            } else if (text.isEmpty()) {
                                filteredList.add((item));
                                isfiltered = true;
                            }

                        }
                    }
                }
            }

        }
        itemRcvIssRegAdapter.filterList(filteredList);
        totalOfAll();
    }

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
//                for(int i = 0; i < supplierLists.size(); i++) {
//                    type.add(supplierLists.get(i).getType());
//                }
//
//                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);
//
//                supplierSpinner.setAdapter(arrayAdapter);
//
//                ArrayList<String> type1 = new ArrayList<>();
//                for(int i = 0; i < customerLists.size(); i++) {
//                    type1.add(customerLists.get(i).getType());
//                }
//
//                ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type1);
//
//                customerSpinner.setAdapter(arrayAdapter1);
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
//
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
//                for(int i = 0; i < customerLists.size(); i++) {
//                    type.add(customerLists.get(i).getType());
//                }
//
//                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);
//
//                customerSpinner.setAdapter(arrayAdapter);
//
//                //new ReOrderLevelCheck().execute();
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
//
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
//                supplierSpinner.setText("");
//                customerSpinner.setText("");
//                searchingSupplier = "";
//                searchingName = "";
//                searchingCustomer = "";
//
////                customerLists = new ArrayList<>();
////
////                ArrayList<String> type = new ArrayList<>();
////                for(int i = 0; i < customerLists.size(); i++) {
////                    type.add(customerLists.get(i).getType());
////                }
////
////                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);
////
////                customerSpinner.setAdapter(arrayAdapter);
//
//
//                itemRcvIssRegAdapter = new ItemRcvIssRegAdapter(itemRcvIssReglists, getContext(),ItemRCVISSUERegister.this);
//                itemView.setAdapter(itemRcvIssRegAdapter);
//
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
//            supplierLists = new ArrayList<>();
//            customerLists = new ArrayList<>();
//
//
//
//            ResultSet resultSet1 = stmt.executeQuery("SELECT ALL ACC_DTL.AD_ID,\n" +
//                    "           ACC_DTL.AD_CODE,\n" +
//                    "           ACC_DTL.AD_NAME,\n" +
//                    "           ACC_DTL.AD_SHORT_NAME\n" +
//                    "  FROM ACC_DTL\n" +
//                    " WHERE ACC_DTL.AD_FLAG = 7 AND AD_PHARMACY_FLAG = 0");
//
//            while (resultSet1.next()) {
//                supplierLists.add(new ReceiveTypeList(resultSet1.getString(1),resultSet1.getString(3)));
//            }
//            supplierLists.add(new ReceiveTypeList("","All Suppliers"));
//
//            ResultSet resultSet = stmt.executeQuery("SELECT AD_ID, AD_NAME FROM ACC_DTL WHERE AD_FLAG = 6\n");
//
//            while (resultSet.next()) {
//                customerLists.add(new ReceiveTypeList(resultSet.getString(1),resultSet.getString(2)));
//            }
//            customerLists.add(new ReceiveTypeList("","All Customers"));
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

    public void getSupplierAndCustomer() {
        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        supplierLists = new ArrayList<>();
        customerLists = new ArrayList<>();

        String suppUrl = "http://103.56.208.123:8001/apex/tterp/utility/getSupplier";
        String clientUrl = "http://103.56.208.123:8001/apex/tterp/utility/getClient";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest clientReq = new StringRequest(Request.Method.GET, clientUrl, response -> {
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
                        String ad_name = info.getString("ad_name");

                        customerLists.add(new ReceiveTypeList(ad_id,ad_name,""));
                    }
                    customerLists.add(new ReceiveTypeList("","All Customers",""));
                }
                getItemData(1);
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
                        String ad_name = info.getString("ad_name");

                        supplierLists.add(new ReceiveTypeList(ad_id,ad_name,""));
                    }
                    supplierLists.add(new ReceiveTypeList("","All Suppliers",""));
                }

                requestQueue.add(clientReq);
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

                //String[] type = new String[] {"Approved", "Pending","Both"};
                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < supplierLists.size(); i++) {
                    type.add(supplierLists.get(i).getType());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                supplierSpinner.setAdapter(arrayAdapter);

                ArrayList<String> type1 = new ArrayList<>();
                for(int i = 0; i < customerLists.size(); i++) {
                    type1.add(customerLists.get(i).getType());
                }

                ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type1);

                customerSpinner.setAdapter(arrayAdapter1);

                updateUI();
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

                    getSupplierAndCustomer();
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

                getSupplierAndCustomer();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }

//    public void SubCategoryData() {
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//
//            Statement stmt = connection.createStatement();
//
//            customerLists = new ArrayList<>();
//
//            if (supplierId.isEmpty()) {
//                supplierId = null;
//            }
//
//            ResultSet resultSet1 = stmt.executeQuery("SELECT AD_ID, AD_NAME FROM ACC_DTL WHERE AD_FLAG = 6\n");
//
//            while (resultSet1.next()) {
//                customerLists.add(new ReceiveTypeList(resultSet1.getString(1),resultSet1.getString(2)));
//            }
//            customerLists.add(new ReceiveTypeList("","All Sub Categories"));
//
//            if (supplierId == null) {
//                customerLists = new ArrayList<>();
//                supplierId = "";
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

//    public void ItemData() {
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//
//            itemRcvIssReglists = new ArrayList<>();
//            //declareValue();
//
//            Statement stmt = connection.createStatement();
//
//            if (firstDate.isEmpty()) {
//                firstDate = null;
//            }
//            if (lastDate.isEmpty()) {
//                lastDate = null;
//            }
//
//            CallableStatement callableStatement = connection.prepareCall("begin ? := GET_ITEM_ISSUE_RCV_REGISTER(?,?,?,?,?,?,?,?); end;");
//            callableStatement.setString(2,firstDate);
//            callableStatement.setString(3,lastDate);
//            callableStatement.setString(4,null);
//            callableStatement.setString(5,null);
//            callableStatement.setString(6,null);
//            callableStatement.setString(7,null);
//            callableStatement.setString(8,null);
//            callableStatement.setInt(9,1);
//            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
//            callableStatement.execute();
//
//            ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
//
//            int i = 0;
//            while (resultSet.next()) {
//                i++;
//                String rcv_no = resultSet.getString(4);
//                String issue_no = resultSet.getString(7);
//                String rm_id = resultSet.getString(1);
//                String issue_id = resultSet.getString(2);
//
////                if (rcv_no.equals("N/A") && !issue_no.isEmpty()) {
////                    rm_id = null;
////                } else if (issue_no.equals("N/A") && !rcv_no.isEmpty()){
////                    issue_id = null;
////                }
//
//                ArrayList<ItemRcvIssItemDescList> itemRcvIssItemDescLists = new ArrayList<>();
//
//                CallableStatement callableStatement1 = connection.prepareCall("begin ? := GET_ITEM_ISSUE_RCV_REGISTER(?,?,?,?,?,?,?,?); end;");
//                callableStatement1.setString(2,firstDate);
//                callableStatement1.setString(3,lastDate);
//                callableStatement1.setString(4,null);
//                callableStatement1.setString(5,null);
//                callableStatement1.setString(6,rm_id);
//                callableStatement1.setString(7,issue_id);
//                callableStatement1.setString(8,null);
//                callableStatement1.setInt(9,2);
//                callableStatement1.registerOutParameter(1, OracleTypes.CURSOR);
//                callableStatement1.execute();
//
//                ResultSet rs = (ResultSet) callableStatement1.getObject(1);
//
//                while (rs.next()) {
//
//                    String item_id = rs.getString(2);
//
//                    ArrayList<ItemRcvIssWarehouseList> itemRcvIssWarehouseLists = new ArrayList<>();
//
//                    CallableStatement callableStatement12 = connection.prepareCall("begin ? := GET_ITEM_ISSUE_RCV_REGISTER(?,?,?,?,?,?,?,?); end;");
//                    callableStatement12.setString(2,firstDate);
//                    callableStatement12.setString(3,lastDate);
//                    callableStatement12.setString(4,null);
//                    callableStatement12.setString(5,null);
//                    callableStatement12.setString(6,rm_id);
//                    callableStatement12.setString(7,issue_id);
//                    callableStatement12.setString(8,item_id);
//                    callableStatement12.setInt(9,3);
//                    callableStatement12.registerOutParameter(1, OracleTypes.CURSOR);
//                    callableStatement12.execute();
//
//                    ResultSet rs1 = (ResultSet) callableStatement12.getObject(1);
//
//                    while (rs1.next()) {
//
////                        itemRcvIssWarehouseLists.add(new ItemRcvIssWarehouseList(rs1.getString(2),rs1.getString(3),rs1.getString(4),
////                                rs1.getString(5),rs1.getString(6),rs1.getString(7),rs1.getString(8),rs1.getString(9)));
//
////                        if (rs1.getString(4) != null  ) {
////                            if (!rs1.getString(4).isEmpty()) {
////                                rcv_qty = rcv_qty + Double.parseDouble(rs1.getString(4));
////                            }
////                        }
////
////                        if (rs1.getString(7) != null ) {
////                            if (!rs1.getString(7).isEmpty()) {
////                                issue_qty = issue_qty + Double.parseDouble(rs1.getString(7));
////                            }
////
////                        }
////
////                        rcv_amnt = rcv_amnt + Double.parseDouble(rs1.getString(6));
////
////                        issue_amnt = issue_amnt + Double.parseDouble(rs1.getString(9));
//
//                    }
//                    callableStatement12.close();
//
////                    itemRcvIssItemDescLists.add(new ItemRcvIssItemDescList(rs.getString(2),rs.getString(1),rs.getString(3),
////                            "","",itemRcvIssWarehouseLists));
//
//
//
//                }
//
//                callableStatement1.close();
//
////                String dateC = resultSet.getString(3).substring(0, 10);
////
////
////                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
////                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy",Locale.getDefault());
////                Date date = null;
////                try {
////                    date = df.parse(dateC);
////                } catch (ParseException e) {
////                    e.printStackTrace();
////                }
////
////                if (date != null) {
////                    dateC = sdf.format(date);
////                }
////
////                itemRcvIssReglists.add(new ItemRcvIssReglist(dateC,resultSet.getString(4),resultSet.getString(7),
////                        resultSet.getString(10),resultSet.getString(11),resultSet.getString(12),
////                        resultSet.getString(13),itemRcvIssItemDescLists));
//
//
//
//
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

    public void getItemsInfo() {
        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;
        getItemData(2);
    }

    private void updateItemsRcvIssReg() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;
                updateUI();
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

                    getItemsInfo();
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

                getItemsInfo();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }

    //-----------------------

    public void getItemData(int parser) {
        itemRcvIssReglists = new ArrayList<>();

        String irirUrl = "http://103.56.208.123:8001/apex/tterp/itemRcvIssReg/item_iss_rcv_reg?st_date="+firstDate+"&end_date="+lastDate+"&user_id=&iss_user=&rm_id=&issue_id=&item_id=&options=1";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest request = new StringRequest(Request.Method.GET, irirUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        String item_issue_rcv_register = info.getString("item_issue_rcv_register");
                        JSONArray itemsArray = new JSONArray(item_issue_rcv_register);
                        for (int j = 0; j < itemsArray.length(); j++) {
                            JSONObject items_info = itemsArray.getJSONObject(j);

                            String sl_rm_id = items_info.getString("sl_rm_id")
                                    .equals("null") ? "" : items_info.getString("sl_rm_id");
                            String sl_issm_id = items_info.getString("sl_issm_id")
                                    .equals("null") ? "" : items_info.getString("sl_issm_id");
                            String sl_date = items_info.getString("sl_date");
                            String rm_no = items_info.getString("rm_no")
                                    .equals("null") ? "" : items_info.getString("rm_no");
//                            String rcv_user = items_info.getString("rcv_user")
//                                    .equals("null") ? "" : items_info.getString("rcv_user");
//                            String rcv_user_full = items_info.getString("rcv_user_full")
//                                    .equals("null") ? "" : items_info.getString("rcv_user_full");
                            String issm_no = items_info.getString("issm_no")
                                    .equals("null") ? "" : items_info.getString("issm_no");
//                            String issue_user = items_info.getString("issue_user")
//                                    .equals("null") ? "" : items_info.getString("issue_user");
//                            String issue_user_full = items_info.getString("issue_user_full")
//                                    .equals("null") ? "" : items_info.getString("issue_user_full");
                            String sl_trans_flag = items_info.getString("sl_trans_flag")
                                    .equals("null") ? "" : items_info.getString("sl_trans_flag");
                            String trans_source = items_info.getString("trans_source")
                                    .equals("null") ? "" : items_info.getString("trans_source");
                            String ad_short_name = items_info.getString("ad_short_name")
                                    .equals("null") ? "" : items_info.getString("ad_short_name");
                            String customer_name = items_info.getString("customer_name")
                                    .equals("null") ? "" : items_info.getString("customer_name");
//                            String customer_code = items_info.getString("customer_code")
//                                    .equals("null") ? "" : items_info.getString("customer_code");

                            String dateC = sl_date.substring(0, 10);


                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy",Locale.getDefault());
                            Date date = null;
                            try {
                                date = df.parse(dateC);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            if (date != null) {
                                dateC = sdf.format(date);
                            }

                            itemRcvIssReglists.add(new ItemRcvIssReglist(dateC,rm_no,issm_no,
                                    sl_trans_flag,trans_source,ad_short_name,
                                    customer_name,sl_rm_id,sl_issm_id,false, new ArrayList<>()));

                        }
                    }
                }
                checkToGetNextList(parser);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                if (parser == 1) {
                    updateFragment();
                }
                else if (parser == 2) {
                    updateItemsRcvIssReg();
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
                updateItemsRcvIssReg();
            }
        });

        requestQueue.add(request);
    }

    public void checkToGetNextList(int parser) {
        if (itemRcvIssReglists.size() != 0) {
            boolean allUpdated = false;
            for (int i = 0; i < itemRcvIssReglists.size(); i++) {
                allUpdated = itemRcvIssReglists.get(i).isUpdated();
                if (!itemRcvIssReglists.get(i).isUpdated()) {
                    allUpdated = itemRcvIssReglists.get(i).isUpdated();
                    String rm_id = itemRcvIssReglists.get(i).getRm_id();
                    String issm_id = itemRcvIssReglists.get(i).getIssm_id();
                    getItemDetailsList(rm_id, issm_id, i, parser);
                    break;
                }
            }
            if (allUpdated) {
                connected = true;
                if (parser == 1) {
                    updateFragment();
                }
                else if (parser == 2) {
                    updateItemsRcvIssReg();
                }
            }
        }
        else {
            connected = true;
            if (parser == 1) {
                updateFragment();
            }
            else if (parser == 2) {
                updateItemsRcvIssReg();
            }
        }
    }

    public void getItemDetailsList(String rm_id, String issm_id, int firstIndex, int parser) {
        ArrayList<ItemRcvIssItemDescList> itemRcvIssItemDescLists = new ArrayList<>();
        String irirUrl = "http://103.56.208.123:8001/apex/tterp/itemRcvIssReg/item_iss_rcv_reg?st_date="+firstDate+"&end_date="+lastDate+"&user_id=&iss_user=&rm_id="+rm_id+"&issue_id="+issm_id+"&item_id=&options=2";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest request = new StringRequest(Request.Method.GET, irirUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        String item_issue_rcv_register = info.getString("item_issue_rcv_register");
                        JSONArray itemsArray = new JSONArray(item_issue_rcv_register);
                        for (int j = 0; j < itemsArray.length(); j++) {
                            JSONObject items_info = itemsArray.getJSONObject(j);

                            String item_reference_name = items_info.getString("item_reference_name");
                            String item_id = items_info.getString("item_id");
                            String item_unit = items_info.getString("item_unit");

                            itemRcvIssItemDescLists.add(new ItemRcvIssItemDescList(item_id,item_reference_name,item_unit,
                                    "","",false, new ArrayList<>()));
                        }
                    }
                }
                checkToGetLastList(rm_id, issm_id, firstIndex, parser, itemRcvIssItemDescLists);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                if (parser == 1) {
                    updateFragment();
                }
                else if (parser == 2) {
                    updateItemsRcvIssReg();
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
                updateItemsRcvIssReg();
            }
        });

        requestQueue.add(request);
    }

    public void checkToGetLastList(String rm_id, String issm_id, int firstIndex, int parser, ArrayList<ItemRcvIssItemDescList> itemRcvIssItemDescLists) {
        if (itemRcvIssItemDescLists.size() != 0) {
            boolean allUpdated = false;
            for (int i = 0; i < itemRcvIssItemDescLists.size(); i++) {
                allUpdated = itemRcvIssItemDescLists.get(i).isUpdated();
                if (!itemRcvIssItemDescLists.get(i).isUpdated()) {
                    allUpdated = itemRcvIssItemDescLists.get(i).isUpdated();
                    String item_id = itemRcvIssItemDescLists.get(i).getItemId();
                    getItemLastDetails(itemRcvIssItemDescLists, rm_id, issm_id, item_id, firstIndex,parser,i);
                    break;
                }
            }
            if (allUpdated) {
                itemRcvIssReglists.get(firstIndex).setItemRcvIssItemDescLists(itemRcvIssItemDescLists);
                itemRcvIssReglists.get(firstIndex).setUpdated(true);
                checkToGetNextList(parser);
            }
        }
        else {
            itemRcvIssReglists.get(firstIndex).setItemRcvIssItemDescLists(itemRcvIssItemDescLists);
            itemRcvIssReglists.get(firstIndex).setUpdated(true);
            checkToGetNextList(parser);
        }
    }

    public void getItemLastDetails(ArrayList<ItemRcvIssItemDescList> itemRcvIssItemDescLists, String rm_id, String issm_id, String item_id, int firstIndex, int parser, int secondIndex) {
        ArrayList<ItemRcvIssWarehouseList> itemRcvIssWarehouseLists = new ArrayList<>();
        String irirUrl = "http://103.56.208.123:8001/apex/tterp/itemRcvIssReg/item_iss_rcv_reg?st_date="+firstDate+"&end_date="+lastDate+"&user_id=&iss_user=&rm_id="+rm_id+"&issue_id="+issm_id+"&item_id="+item_id+"&options=3";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest request = new StringRequest(Request.Method.GET, irirUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        String item_issue_rcv_register = info.getString("item_issue_rcv_register");
                        JSONArray itemsArray = new JSONArray(item_issue_rcv_register);
                        for (int j = 0; j < itemsArray.length(); j++) {
                            JSONObject items_info = itemsArray.getJSONObject(j);

                            String whm_name = items_info.getString("whm_name")
                                    .equals("null") ? "" : items_info.getString("whm_name");
                            String whd_reck = items_info.getString("whd_reck")
                                    .equals("null") ? "" : items_info.getString("whd_reck");
                            String sl_rcv_qty = items_info.getString("sl_rcv_qty")
                                    .equals("null") ? "" : items_info.getString("sl_rcv_qty");
                            String sl_rcv_rate = items_info.getString("sl_rcv_rate")
                                    .equals("null") ? "" : items_info.getString("sl_rcv_rate");
                            String rcv_amt = items_info.getString("rcv_amt")
                                    .equals("null") ? "" : items_info.getString("rcv_amt");
                            String sl_issue_qty = items_info.getString("sl_issue_qty")
                                    .equals("null") ? "" : items_info.getString("sl_issue_qty");
                            String sl_issue_rate = items_info.getString("sl_issue_rate")
                                    .equals("null") ? "" : items_info.getString("sl_issue_rate");
                            String issue_amt = items_info.getString("issue_amt")
                                    .equals("null") ? "" : items_info.getString("issue_amt");

                            itemRcvIssWarehouseLists.add(new ItemRcvIssWarehouseList(whm_name,whd_reck,sl_rcv_qty,
                                    sl_rcv_rate,rcv_amt,sl_issue_qty,sl_issue_rate,issue_amt));

                        }
                    }
                }

                itemRcvIssItemDescLists.get(secondIndex).setItemRcvIssWarehouseLists(itemRcvIssWarehouseLists);
                itemRcvIssItemDescLists.get(secondIndex).setUpdated(true);
                checkToGetLastList(rm_id, issm_id,firstIndex,parser,itemRcvIssItemDescLists);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                if (parser == 1) {
                    updateFragment();
                }
                else if (parser == 2) {
                    updateItemsRcvIssReg();
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
                updateItemsRcvIssReg();
            }
        });

        requestQueue.add(request);
    }

    public void updateUI() {
        isfiltered = false;
        supplierSpinner.setText("");
        customerSpinner.setText("");
        searchingSupplier = "";
        searchingName = "";
        searchingCustomer = "";

        itemRcvIssRegAdapter = new ItemRcvIssRegAdapter(itemRcvIssReglists, getContext(),ItemRCVISSUERegister.this);
        itemView.setAdapter(itemRcvIssRegAdapter);

        searchItem.setText("");
        totalOfAll();

        searchItem.clearFocus();
    }
}
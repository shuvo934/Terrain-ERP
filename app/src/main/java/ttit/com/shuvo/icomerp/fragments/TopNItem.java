package ttit.com.shuvo.icomerp.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rosemaryapp.amazingspinner.AmazingSpinner;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import oracle.jdbc.driver.OracleTypes;
import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.WaitProgress;
import ttit.com.shuvo.icomerp.adapters.InventoryLedgerAdapter;
import ttit.com.shuvo.icomerp.adapters.TopNItemAdapter;
import ttit.com.shuvo.icomerp.arrayList.InventoryLedgerItemList;
import ttit.com.shuvo.icomerp.arrayList.InventoryLedgerList;
import ttit.com.shuvo.icomerp.arrayList.ReceiveTypeList;
import ttit.com.shuvo.icomerp.arrayList.TopNItemLists;

import static ttit.com.shuvo.icomerp.OracleConnection.createConnection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TopNItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopNItem extends Fragment implements TopNItemAdapter.ClickedItem{

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

    private Connection connection;

    TextInputEditText beginDate;
    TextInputEditText endDate;
    TextView daterange;

    AmazingSpinner categorySpinner;
    AmazingSpinner subCatSpinner;
    AmazingSpinner listedSpinner;

    TextInputEditText searchItem;
    TextInputLayout searchItemLay;

    RecyclerView itemView;
    TopNItemAdapter topNItemAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<ReceiveTypeList> categoryLists;
    ArrayList<ReceiveTypeList> subCategoryLists;
    ArrayList<ReceiveTypeList> listedbyLists;

    String firstDate = "";
    String lastDate = "";
    String categoryId = "";
    String subCategoryId = "";
    String searchingCate = "";
    String searchingSubCate = "";
    String searchingName = "";

    double total_amnt = 0.0;
    int listedByNo = 1;

    TextView totalAmnt;

    private int mYear, mMonth, mDay;

    ArrayList<TopNItemLists> topNItemLists;
    ArrayList<TopNItemLists> filteredList;

    boolean firsttime = true;

    Context mContext;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext=context;
    }

    public TopNItem() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TopNItem.
     */
    // TODO: Rename and change types and number of parameters
    public static TopNItem newInstance(String param1, String param2) {
        TopNItem fragment = new TopNItem();
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
        View view = inflater.inflate(R.layout.fragment_top_n_item, container, false);

        beginDate = view.findViewById(R.id.begin_date_top_n_item);
        endDate = view.findViewById(R.id.end_date_top_n_item);
        daterange = view.findViewById(R.id.date_range_msg_top_n_item);
        categorySpinner = view.findViewById(R.id.cat_type_spinner_item_top_n_item);
        subCatSpinner = view.findViewById(R.id.sub_cat_type_spinner_item_top_n_item);
        listedSpinner = view.findViewById(R.id.listed_by_spinner);

        searchItem = view.findViewById(R.id.search_item_name_top_n_item);
        searchItemLay = view.findViewById(R.id.search_item_name_lay_top_n_item);

        totalAmnt = view.findViewById(R.id.total_top_n_item_amnt);

        itemView = view.findViewById(R.id.top_n_item_report_view);

        categoryLists = new ArrayList<>();
        subCategoryLists = new ArrayList<>();
        topNItemLists = new ArrayList<>();
        filteredList = new ArrayList<>();
        listedbyLists = new ArrayList<>();

        listedbyLists.add(new ReceiveTypeList("1","Quantity"));
        listedbyLists.add(new ReceiveTypeList("2","Amount"));

        firsttime = true;

        itemView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        itemView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(itemView.getContext(),DividerItemDecoration.VERTICAL);
        itemView.addItemDecoration(dividerItemDecoration);

        listedSpinner.setText("Quantity");
        ArrayList<String> type = new ArrayList<>();
        for(int i = 0; i < listedbyLists.size(); i++) {
            type.add(listedbyLists.get(i).getType());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

        listedSpinner.setAdapter(arrayAdapter);


        listedSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                System.out.println(position+": "+name);
                for (int i = 0; i < listedbyLists.size(); i++) {
                    if (name.equals(listedbyLists.get(i).getType())) {
                        listedByNo = Integer.parseInt(listedbyLists.get(i).getId());
                    }
                }

                new ItemCheck().execute();

            }
        });

//        listedSpinner.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (firsttime) {
//                    ArrayList<String> type = new ArrayList<>();
//                    for(int i = 0; i < listedbyLists.size(); i++) {
//                        type.add(listedbyLists.get(i).getType());
//                    }
//                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);
//
//                    listedSpinner.setAdapter(arrayAdapter);
//                    firsttime = false;
//                }
//
//
//            }
//        });

//        listedSpinner.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//            }
//        });



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
                new SubCategoryCheck().execute();
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
                                new ItemCheck().execute();
//                                afterCat.setVisibility(View.GONE);
//                                afterSubCat.setVisibility(View.GONE);
//                                toSeCat.setText(toSeCatMSG);
//                                toSeSubCat.setText(toSeSubCatMSG);
//                                afterItem.setVisibility(View.GONE);
//                                toSeItem.setText(toSeItemMSG);
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

                                        new ItemCheck().execute();
//                                        afterCat.setVisibility(View.GONE);
//                                        afterSubCat.setVisibility(View.GONE);
//                                        toSeCat.setText(toSeCatMSG);
//                                        toSeSubCat.setText(toSeSubCatMSG);
//                                        afterItem.setVisibility(View.GONE);
//                                        toSeItem.setText(toSeItemMSG);

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
                                new ItemCheck().execute();
//                                afterCat.setVisibility(View.GONE);
//                                afterSubCat.setVisibility(View.GONE);
//                                toSeCat.setText(toSeCatMSG);
//                                toSeSubCat.setText(toSeSubCatMSG);
//                                afterItem.setVisibility(View.GONE);
//                                toSeItem.setText(toSeItemMSG);
                            }
                            else {
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

                                        new ItemCheck().execute();
//                                        afterCat.setVisibility(View.GONE);
//                                        afterSubCat.setVisibility(View.GONE);
//                                        toSeCat.setText(toSeCatMSG);
//                                        toSeSubCat.setText(toSeSubCatMSG);
//                                        afterItem.setVisibility(View.GONE);
//                                        toSeItem.setText(toSeItemMSG);

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

        new Check().execute();

        return  view;
    }

    private void filter(String text) {

        filteredList = new ArrayList<>();
        for (TopNItemLists item : topNItemLists) {
            if (searchingSubCate.isEmpty()) {
                if (searchingCate.isEmpty()) {
                    if (item.getItem_name().toLowerCase().contains(text.toLowerCase()) || item.getItem_code().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add((item));
                        isfiltered = true;
                    }
                } else {
                    if (item.getCat_name().toLowerCase().contains(searchingCate.toLowerCase())) {
                        if (item.getItem_name().toLowerCase().contains(text.toLowerCase()) || item.getItem_code().toLowerCase().contains(text.toLowerCase())) {
                            filteredList.add((item));
                            isfiltered = true;
                        }
                    }
                }
            } else {
                if (searchingCate.isEmpty()) {
                    if (item.getSubCat_name().toLowerCase().contains(searchingSubCate.toLowerCase())) {
                        if (item.getItem_name().toLowerCase().contains(text.toLowerCase()) || item.getItem_code().toLowerCase().contains(text.toLowerCase())) {
                            filteredList.add((item));
                            isfiltered = true;

                        }
                    }
                } else {
                    if (item.getCat_name().toLowerCase().contains(searchingCate.toLowerCase())) {
                        if (item.getSubCat_name().toLowerCase().contains(searchingSubCate.toLowerCase())) {
                            if (item.getItem_name().toLowerCase().contains(text.toLowerCase()) || item.getItem_code().toLowerCase().contains(text.toLowerCase())) {
                                filteredList.add((item));
                                isfiltered = true;

                            }
                        }
                    }
                }

            }

        }
        total_amnt = 0.0;
        for (int i = 0; i < filteredList.size(); i++) {
            filteredList.get(i).setSl_no(String.valueOf(i+1));
            if (filteredList.get(i).getAmount() != null) {
                total_amnt = total_amnt + Double.parseDouble(filteredList.get(i).getAmount());
            }
        }
        DecimalFormat formatter = new DecimalFormat("##,##,##,###");
        String formatted = formatter.format(total_amnt);

        totalAmnt.setText(formatted);

        topNItemAdapter.filterList(filteredList);

        //totalOfAll();
    }

    private void filterCate(String text) {

        filteredList = new ArrayList<>();
        for (TopNItemLists item : topNItemLists) {
            if (searchingSubCate.isEmpty()){
                if (searchingName.isEmpty()) {
                    if (item.getCat_name().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add((item));
                        isfiltered = true;
                    }
                } else {
                    if (item.getItem_name().toLowerCase().contains(searchingName.toLowerCase()) || item.getItem_code().toLowerCase().contains(searchingName.toLowerCase())) {
                        if (item.getCat_name().toLowerCase().contains(text.toLowerCase())) {
                            filteredList.add((item));
                            isfiltered = true;
                        }
                    }
                }
            } else {
                if (searchingName.isEmpty()) {
                    if (item.getSubCat_name().toLowerCase().contains(searchingSubCate.toLowerCase())) {
                        if (item.getCat_name().toLowerCase().contains(text.toLowerCase())) {
                            filteredList.add((item));
                            isfiltered = true;
                        }
                    }
                } else {
                    if (item.getSubCat_name().toLowerCase().contains(searchingSubCate.toLowerCase())) {
                        if (item.getItem_name().toLowerCase().contains(searchingName.toLowerCase()) || item.getItem_code().toLowerCase().contains(searchingName.toLowerCase())) {
                            if (item.getCat_name().toLowerCase().contains(text.toLowerCase())) {
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
        total_amnt = 0.0;
        for (int i = 0; i < filteredList.size(); i++) {
            filteredList.get(i).setSl_no(String.valueOf(i+1));
            if (filteredList.get(i).getAmount() != null) {
                total_amnt = total_amnt + Double.parseDouble(filteredList.get(i).getAmount());
            }
        }

        DecimalFormat formatter = new DecimalFormat("##,##,##,###");
        String formatted = formatter.format(total_amnt);

        totalAmnt.setText(formatted);
        topNItemAdapter.filterList(filteredList);
        //totalOfAll();
    }

    private void filterSubCate(String text) {

        filteredList = new ArrayList<>();
        for (TopNItemLists item : topNItemLists) {
            if (searchingCate.isEmpty()) {
                if (searchingName.isEmpty()) {
                    if (item.getSubCat_name().toLowerCase().contains(text.toLowerCase())){
                        filteredList.add((item));
                        isfiltered = true;
                    }
                } else {
                    if (item.getItem_name().toLowerCase().contains(searchingName.toLowerCase()) || item.getItem_code().toLowerCase().contains(searchingName.toLowerCase())){
                        if (item.getSubCat_name().toLowerCase().contains(text.toLowerCase())){
                            filteredList.add((item));
                            isfiltered = true;
                        }
                    }
                }
            } else {
                if (searchingName.isEmpty()) {
                    if (item.getCat_name().toLowerCase().contains(searchingCate.toLowerCase())){
                        if (item.getSubCat_name().toLowerCase().contains(text.toLowerCase())){
                            filteredList.add((item));
                            isfiltered = true;
                        }
                    }
                } else {
                    if (item.getCat_name().toLowerCase().contains(searchingCate.toLowerCase())){
                        if (item.getItem_name().toLowerCase().contains(searchingName.toLowerCase()) || item.getItem_code().toLowerCase().contains(searchingName.toLowerCase())){
                            if (item.getSubCat_name().toLowerCase().contains(text.toLowerCase())){
                                filteredList.add((item));
                                isfiltered = true;
                            }
                        }
                    }
                }
            }

        }

        total_amnt = 0.0;
        for (int i = 0; i < filteredList.size(); i++) {
            filteredList.get(i).setSl_no(String.valueOf(i+1));
            if (filteredList.get(i).getAmount() != null) {
                total_amnt = total_amnt + Double.parseDouble(filteredList.get(i).getAmount());
            }
        }

        DecimalFormat formatter = new DecimalFormat("##,##,##,###");
        String formatted = formatter.format(total_amnt);

        totalAmnt.setText(formatted);
        topNItemAdapter.filterList(filteredList);
        //totalOfAll();
    }

    @Override
    public void onCategoryClicked(int CategoryPosition) {

    }

    public class Check extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                CategoryData();
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

            waitProgress.dismiss();
            if (conn) {

                conn = false;
                connected = false;

                //String[] type = new String[] {"Approved", "Pending","Both"};
                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < categoryLists.size(); i++) {
                    type.add(categoryLists.get(i).getType());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                categorySpinner.setAdapter(arrayAdapter);

                new ItemCheck().execute();

//                new ReOrderFragment.ReOrderLevelCheck().execute();

            }else {
                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new Check().execute();
                        dialog.dismiss();
                    }
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public class SubCategoryCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                SubCategoryData();
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

            waitProgress.dismiss();
            if (conn) {

                conn = false;
                connected = false;

                //String[] type = new String[] {"Approved", "Pending","Both"};
                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < subCategoryLists.size(); i++) {
                    type.add(subCategoryLists.get(i).getType());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                subCatSpinner.setAdapter(arrayAdapter);

                //new ReOrderLevelCheck().execute();

            }else {
                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new SubCategoryCheck().execute();
                        dialog.dismiss();
                    }
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public class ItemCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
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

            waitProgress.dismiss();
            if (conn) {

                conn = false;
                connected = false;
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


                topNItemAdapter = new TopNItemAdapter(topNItemLists, getContext(),TopNItem.this);
                itemView.setAdapter(topNItemAdapter);
                searchItem.setText("");


//                scrollView.fullScroll(View.FOCUS_DOWN);
//
////                System.out.println(searchItem.getBaseline()+" : "+ searchItem.getBottom());
////                scrollView.scrollTo(searchItem.getBottom(),searchItem.getBottom());
//
//                scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        scrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                        scrollView.fullScroll(View.FOCUS_DOWN);
//                        searchItem.clearFocus();
////                        scrollView.scrollTo(searchItem.getBottom(),searchItem.getBottom());
//
////                        scrollView.pageScroll(View.FOCUS_DOWN);
//                    }
//                });

                searchItem.clearFocus();

                DecimalFormat formatter = new DecimalFormat("##,##,##,###");
                String formatted = formatter.format(total_amnt);

                totalAmnt.setText(formatted);



            }else {
                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new ItemCheck().execute();
                        dialog.dismiss();
                    }
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    private void closeKeyBoard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public boolean isConnected() {
        boolean connected = false;
        boolean isMobile = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
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

    public void CategoryData() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            Statement stmt = connection.createStatement();

            categoryLists = new ArrayList<>();



            ResultSet resultSet1 = stmt.executeQuery("SELECT DISTINCT V.IM_ID, V.IM_NAME\n" +
                    "  FROM ITEM_DETAILS_V V");

            while (resultSet1.next()) {
                categoryLists.add(new ReceiveTypeList(resultSet1.getString(1),resultSet1.getString(2)));
            }
            categoryLists.add(new ReceiveTypeList("","All Categories"));


            connected = true;

            connection.close();

        }
        catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void SubCategoryData() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            Statement stmt = connection.createStatement();

            subCategoryLists = new ArrayList<>();

            if (categoryId.isEmpty()) {
                categoryId = null;
            }

            ResultSet resultSet1 = stmt.executeQuery("SELECT DISTINCT V.ISC_ID, V.SUBCATM_NAME\n" +
                    "  FROM ITEM_DETAILS_V V where V.IM_ID = "+categoryId+"");

            while (resultSet1.next()) {
                subCategoryLists.add(new ReceiveTypeList(resultSet1.getString(1),resultSet1.getString(2)));
            }
            subCategoryLists.add(new ReceiveTypeList("","All Sub Categories"));

            if (categoryId == null) {
                subCategoryLists = new ArrayList<>();
                categoryId = "";
            }

            connected = true;

            connection.close();

        }
        catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void ItemData() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            topNItemLists= new ArrayList<>();
            total_amnt = 0.0;

            //Statement stmt = connection.createStatement();

            if (firstDate.isEmpty()) {
                firstDate = null;
            }
            if (lastDate.isEmpty()) {
                lastDate = null;
            }


            CallableStatement callableStatement = connection.prepareCall("begin ? := GET_TOP_ITEM_LIST(?,?,?); end;");
            callableStatement.setString(2,firstDate);
            callableStatement.setString(3,lastDate);
            callableStatement.setInt(4,listedByNo);

            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement.execute();

            ResultSet resultSet = (ResultSet) callableStatement.getObject(1);

            int i = 0;
            while (resultSet.next()) {
                i++;

                topNItemLists.add(new TopNItemLists(String.valueOf(i),resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),
                        resultSet.getString(4),resultSet.getString(5),resultSet.getString(6),
                        resultSet.getString(7),resultSet.getString(8),resultSet.getString(9),
                        resultSet.getString(10),resultSet.getString(11),resultSet.getString(12)));


//                System.out.println(i);
            }

            for (int j = 0; j < topNItemLists.size(); j++) {
                if (topNItemLists.get(j).getAmount() != null) {
                    total_amnt = total_amnt + Double.parseDouble(topNItemLists.get(j).getAmount());
                }
            }

            callableStatement.close();
            connected = true;

            if (firstDate == null) {
                firstDate = "";
            }
            if (lastDate == null) {
                lastDate = "";
            }



            connection.close();

        }
        catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}
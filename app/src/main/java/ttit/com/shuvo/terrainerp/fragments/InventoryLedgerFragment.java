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
import ttit.com.shuvo.terrainerp.adapters.InventoryLedgerAdapter;
import ttit.com.shuvo.terrainerp.arrayList.InventoryLedgerItemList;
import ttit.com.shuvo.terrainerp.arrayList.InventoryLedgerList;
import ttit.com.shuvo.terrainerp.arrayList.ReceiveTypeList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InventoryLedgerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InventoryLedgerFragment extends Fragment implements InventoryLedgerAdapter.ClickedItem {

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
    InventoryLedgerAdapter inventoryLedgerAdapter;
    RecyclerView.LayoutManager layoutManager;

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

    ArrayList<InventoryLedgerList> inventoryLedgerLists;
    ArrayList<InventoryLedgerList> filteredList;

//    ArrayList<InventoryLedgerItemList> inventoryLedgerItemLists;

    public InventoryLedgerFragment() {
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
     * @return A new instance of fragment InventoryLedgerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InventoryLedgerFragment newInstance(String param1, String param2) {
        InventoryLedgerFragment fragment = new InventoryLedgerFragment();
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
        View view = inflater.inflate(R.layout.fragment_inventory_ledger, container, false);

        beginDate = view.findViewById(R.id.begin_date_inventory_ledger);
        endDate = view.findViewById(R.id.end_date_inventory_ledger);
        daterange = view.findViewById(R.id.date_range_msg_inventory_ledger);
        categorySpinner = view.findViewById(R.id.cat_type_spinner_item_inventory_ledger);
        subCatSpinner = view.findViewById(R.id.sub_cat_type_spinner_item_inventory_ledger);

        searchItem = view.findViewById(R.id.search_item_name_inventory_ledger);
        searchItemLay = view.findViewById(R.id.search_item_name_lay_inventory_ledger);

        itemView = view.findViewById(R.id.item_all_inventory_ledger);

        categoryLists = new ArrayList<>();
        subCategoryLists = new ArrayList<>();
        inventoryLedgerLists = new ArrayList<>();
//        inventoryLedgerItemLists = new ArrayList<>();
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
                                getItemsInventory();
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
                                        getItemsInventory();
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
                                getItemsInventory();
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
                                        getItemsInventory();

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

    private void filter(String text) {

        filteredList = new ArrayList<>();
        for (InventoryLedgerList item : inventoryLedgerLists) {
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
        inventoryLedgerAdapter.filterList(filteredList);
        //totalOfAll();
    }

    private void filterCate(String text) {

        filteredList = new ArrayList<>();
        for (InventoryLedgerList item : inventoryLedgerLists) {
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
        inventoryLedgerAdapter.filterList(filteredList);
        //totalOfAll();
    }

    private void filterSubCate(String text) {

        filteredList = new ArrayList<>();
        for (InventoryLedgerList item : inventoryLedgerLists) {
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
        inventoryLedgerAdapter.filterList(filteredList);
        //totalOfAll();
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
//                inventoryLedgerAdapter = new InventoryLedgerAdapter(inventoryLedgerLists, getContext(),InventoryLedgerFragment.this);
//                itemView.setAdapter(inventoryLedgerAdapter);
//                searchItem.setText("");
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

                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < categoryLists.size(); i++) {
                    type.add(categoryLists.get(i).getType());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                categorySpinner.setAdapter(arrayAdapter);

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
        });

        requestQueue.add(subCatReq);

    }

    private void updateLayout() {
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
//            inventoryLedgerLists= new ArrayList<>();
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
//
//            CallableStatement callableStatement = connection.prepareCall("begin ? := GET_ITEM_LIST(?,?); end;");
//            callableStatement.setString(2,firstDate);
//            callableStatement.setString(3,lastDate);
//
//            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
//            callableStatement.execute();
//
//            ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
//
//            int i = 0;
//            while (resultSet.next()) {
//                i++;
//
//                inventoryLedgerItemLists = new ArrayList<>();
//                int balance = 0;
//                balance = resultSet.getInt(10);
//
//                CallableStatement callableStatement1 = connection.prepareCall("begin ? := GET_ITEM_LEDGER_LIST(?,?,?); end;");
//                callableStatement1.setInt(2,resultSet.getInt(3));
//                callableStatement1.setString(3,firstDate);
//                callableStatement1.setString(4,lastDate);
//
//                callableStatement1.registerOutParameter(1, OracleTypes.CURSOR);
//                callableStatement1.execute();
//
//                ResultSet resultSet1 = (ResultSet) callableStatement1.getObject(1);
//
//                while (resultSet1.next()) {
//                    int rcvqty = resultSet1.getInt(5);
//                    int issuqty = resultSet1.getInt(6);
//
//                    System.out.println(rcvqty);
//                    System.out.println(issuqty);
//                    System.out.println(balance);
//
//                    balance = balance + rcvqty - issuqty;
//                    System.out.println("After Calc: "+balance);
//
//                    String rcv_QTY = "";
//                    if (rcvqty == 0) {
//                        rcv_QTY = "";
//                    } else {
//                        rcv_QTY = resultSet1.getString(5);
//                    }
//
//                    String iss_QTY = "";
//                    if (issuqty == 0) {
//                        iss_QTY = "";
//                    } else {
//                        iss_QTY = resultSet1.getString(6);
//                    }
//
//                    inventoryLedgerItemLists.add(new InventoryLedgerItemList(resultSet1.getString(1),resultSet1.getString(7),resultSet1.getString(8),
//                            resultSet1.getString(10),resultSet1.getString(9),resultSet1.getString(12),
//                            rcv_QTY,iss_QTY,String.valueOf(balance)));
//
//                }
//                callableStatement1.close();
//
//
//                String total_rcv_qty = "";
//                if (resultSet.getInt(13) == 0) {
//                    total_rcv_qty = "";
//                } else {
//                    total_rcv_qty = resultSet.getString(13);
//                }
//
//                String total_iss_qty = "";
//                if (resultSet.getInt(12) == 0) {
//                    total_iss_qty = "";
//                } else {
//                    total_iss_qty = resultSet.getString(12);
//                }
//                inventoryLedgerLists.add(new InventoryLedgerList(resultSet.getString(3),resultSet.getString(4),resultSet.getString(5),
//                        resultSet.getString(1),resultSet.getString(8),resultSet.getString(6),
//                        resultSet.getString(7),firstDate,resultSet.getString(10),total_rcv_qty,
//                        total_iss_qty,resultSet.getString(11),inventoryLedgerItemLists));
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

    public void getItemsInventory() {
        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;
        getItems(2);
    }

    private void updateInventory() {
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

                    getItemsInventory();
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

                getItemsInventory();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }

    public void getItems(int parser) {
        inventoryLedgerLists= new ArrayList<>();

        String inventoryUrl = "http://103.56.208.123:8001/apex/tterp/inventoryLedger/getItemList?st_date="+firstDate+"&end_date="+lastDate+"";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest inventoryReq = new StringRequest(Request.Method.GET, inventoryUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        String item_list = info.getString("item_list");
                        JSONArray itemsArray = new JSONArray(item_list);
                        for (int j = 0; j < itemsArray.length(); j++) {
                            JSONObject items_info = itemsArray.getJSONObject(j);

                            String im_name = items_info.getString("im_name");
//                            String im_id = items_info.getString("im_id");
                            String item_id = items_info.getString("item_id");
                            String item_reference_name = items_info.getString("item_reference_name");
                            String item_code = items_info.getString("item_code");
                            String item_hs_code = items_info.getString("item_hs_code")
                                    .equals("null") ? "" : items_info.getString("item_hs_code");
                            String item_unit = items_info.getString("item_unit");
                            String subcatm_name = items_info.getString("subcatm_name");
//                            String itm_status = items_info.getString("itm_status");
                            String opening_qty = items_info.getString("opening_qty");
                            String closing_qty = items_info.getString("closing_qty");
                            String total_issue_qty = items_info.getString("total_issue_qty")
                                    .equals("0") ? "" : items_info.getString("total_issue_qty");
                            String total_rcv_qty = items_info.getString("total_rcv_qty")
                                    .equals("0") ? "" : items_info.getString("total_rcv_qty");


                            inventoryLedgerLists.add(new InventoryLedgerList(item_id,item_reference_name,item_code,
                                    im_name,subcatm_name,item_hs_code,
                                    item_unit,firstDate,opening_qty,total_rcv_qty,
                                    total_issue_qty,closing_qty,false,new ArrayList<>()));
                        }
                    }
                }
                checkToGetItemLedger(parser);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                if (parser == 1) {
                    updateFragment();
                }
                else if (parser == 2) {
                    updateInventory();
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
                updateInventory();
            }
        });

        requestQueue.add(inventoryReq);
    }

    public void checkToGetItemLedger(int parser) {
        if (inventoryLedgerLists.size() != 0) {
            boolean allUpdated = false;
            for (int i = 0; i < inventoryLedgerLists.size(); i++) {
                allUpdated = inventoryLedgerLists.get(i).isUpdated();
                if (!inventoryLedgerLists.get(i).isUpdated()) {
                    double balance = Double.parseDouble(inventoryLedgerLists.get(i).getOpening_balance());
                    String item_id = inventoryLedgerLists.get(i).getItem_id();
                    getItemLedger(balance, item_id, parser,i);
                    break;
                }
            }
            if (allUpdated) {
                connected = true;
                if (parser == 1) {
                    updateFragment();
                }
                else if (parser == 2) {
                    updateInventory();
                }
            }
        }
        else {
            connected = true;
            if (parser == 1) {
                updateFragment();
            }
            else if (parser == 2) {
                updateInventory();
            }
        }
    }

    public void getItemLedger(double balance, String item_id, int parser, int index) {
        ArrayList<InventoryLedgerItemList> ledgerItemLists = new ArrayList<>();
        final double[] bal = {0};
        bal[0] = balance;

        String url = "http://103.56.208.123:8001/apex/tterp/inventoryLedger/getItemLedgerList?item_id="+item_id+"&st_date="+firstDate+"&end_date="+lastDate+"";
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
                        String item_ledger_list = info.getString("item_ledger_list");
                        JSONArray itemsArray = new JSONArray(item_ledger_list);
                        for (int j = 0; j < itemsArray.length(); j++) {
                            JSONObject items_info = itemsArray.getJSONObject(j);

                            String sl_date = items_info.getString("sl_date");
//                            String sl_rm_id = items_info.getString("sl_rm_id");
//                            String sl_issm_id = items_info.getString("sl_issm_id")
//                                    .equals("null") ? "" : items_info.getString("sl_issm_id");
//                            String sl_item_id = items_info.getString("sl_item_id");
                            String sl_rcv_qty = items_info.getString("sl_rcv_qty")
                                    .equals("0") ? "" : items_info.getString("sl_rcv_qty");
                            String sl_issue_qty = items_info.getString("sl_issue_qty")
                                    .equals("0") ? "" : items_info.getString("sl_issue_qty");
                            String sl_rm_no = items_info.getString("sl_rm_no");
                            String sl_issm_no = items_info.getString("sl_issm_no");
                            String trans_source = items_info.getString("trans_source");
                            String sl_trans_flag = items_info.getString("sl_trans_flag");
//                            String sl_supplier_id = items_info.getString("sl_supplier_id");
                            String ad_short_name = items_info.getString("ad_short_name");

                            double rcvqty = items_info.getDouble("sl_rcv_qty");
                            double issuqty = items_info.getDouble("sl_issue_qty");

                            bal[0] = bal[0] + rcvqty - issuqty;


                            ledgerItemLists.add(new InventoryLedgerItemList(sl_date,sl_rm_no,sl_issm_no,
                                    sl_trans_flag,trans_source,ad_short_name,
                                    sl_rcv_qty,sl_issue_qty,String.valueOf(bal[0])));
                        }
                    }
                }
                inventoryLedgerLists.get(index).setInventoryLedgerItemLists(ledgerItemLists);
                inventoryLedgerLists.get(index).setUpdated(true);
                checkToGetItemLedger(parser);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                if (parser == 1) {
                    updateFragment();
                }
                else if (parser == 2) {
                    updateInventory();
                }
            }
        },error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            if (parser == 1) {
                updateFragment();
            }
            else if (parser == 2) {
                updateInventory();
            }
        });

        requestQueue.add(request);
    }

    public void updateUI() {
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


        inventoryLedgerAdapter = new InventoryLedgerAdapter(inventoryLedgerLists, getContext(),InventoryLedgerFragment.this);
        itemView.setAdapter(inventoryLedgerAdapter);
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
    }
}
package ttit.com.shuvo.icomerp.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rosemaryapp.amazingspinner.AmazingSpinner;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.WaitProgress;
import ttit.com.shuvo.icomerp.adapters.AllItemSubAdapter;
import ttit.com.shuvo.icomerp.adapters.AllItemSubItemAdapter;
import ttit.com.shuvo.icomerp.adapters.ItemWiswStockAdapter;
import ttit.com.shuvo.icomerp.arrayList.ItemAllLists;
import ttit.com.shuvo.icomerp.arrayList.ItemAllSubLists;
import ttit.com.shuvo.icomerp.arrayList.ItemWiseStockLists;
import ttit.com.shuvo.icomerp.arrayList.ReceiveTypeList;

import static ttit.com.shuvo.icomerp.OracleConnection.createConnection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllItemList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllItemList extends Fragment {

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

    AmazingSpinner categorySpinner;
    AmazingSpinner subCatSpinner;

    TextInputEditText searchItem;
    TextInputLayout searchItemLay;

    RecyclerView itemView;
    AllItemSubAdapter allItemSubAdapter;
    RecyclerView.LayoutManager layoutManager;

    private final ArrayList<ItemAllSubLists> itemAllSubLists = new ArrayList<>();
    ArrayList<ItemAllSubLists> filteredList;
    ArrayList<ItemAllSubLists> anotherNewItemLists;

    CardView allItemsCard;
    CardView searchCard;

    String categoryId = "";
    String subCategoryId = "";
    String searchingCate = "";
    String searchingSubCate = "";
    String searchingName = "";

    ArrayList<ReceiveTypeList> categoryLists;
    ArrayList<ReceiveTypeList> subCategoryLists;
    ArrayList<ItemAllSubLists> newItemLists;

    int totalQty = 0;
    double totalValue = 0.0;

    int totalTotalQ = 0;
    double totalTotalV = 0.0;

    TextView totalQ;
    TextView totalV;

    public AllItemList() {
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
     * @return A new instance of fragment AllItemList.
     */
    // TODO: Rename and change types and number of parameters
    public static AllItemList newInstance(String param1, String param2) {
        AllItemList fragment = new AllItemList();
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
        View view = inflater.inflate(R.layout.fragment_all_item_list, container, false);

        categorySpinner = view.findViewById(R.id.cat_type_spinner_all_items);
        subCatSpinner = view.findViewById(R.id.sub_cat_type_spinner_all_items);

        searchItem = view.findViewById(R.id.search_item_name_all_items);
        searchItemLay = view.findViewById(R.id.search_item_name_lay_all_items);

        itemView = view.findViewById(R.id.all_item_list_report_view);
        allItemsCard = view.findViewById(R.id.all_items_card);
        allItemsCard.setVisibility(View.GONE);
        searchCard = view.findViewById(R.id.search_item_card_ail);
        searchCard.setVisibility(View.GONE);

        totalQ = view.findViewById(R.id.total_stock_qty_ail);
        totalV = view.findViewById(R.id.total_stock_value_ail);

        categoryLists = new ArrayList<>();
        subCategoryLists = new ArrayList<>();
        //itemAllSubLists = new ArrayList<>();
        filteredList = new ArrayList<>();
        newItemLists = new ArrayList<>();
        anotherNewItemLists = new ArrayList<>();

        itemView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        itemView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(itemView.getContext(),DividerItemDecoration.VERTICAL);
        itemView.addItemDecoration(dividerItemDecoration);

//        ArrayList<Integer> gfg = new ArrayList<>();
//
//        // adding elements to  first ArrayList
//        gfg.add(10);
//        gfg.add(21);
//        gfg.add(22);
//        gfg.add(35);
//
//        // passing in the constructor
//        ArrayList<Integer> gfg2 = new ArrayList<>(gfg);
//
//        // Iterating over  second ArrayList
//        System.out.println(
//                "-----Iterating over the second ArrayList----");
//        for (Integer value : gfg2) {
//            System.out.println(value);
//        }
//
//        // here we changed the third element to 23
//        // we changed in second list and you can
//        // here we will not see the same change in the first
//        gfg2.set(2, 23);
//        gfg2.set(3,567);
//
//        for (Integer value : gfg2) {
//            System.out.println(value);
//        }
//
//        for (Integer value : gfg) {
//            System.out.println(value);
//        }
//
//        System.out.println("third element of first list ="
//                + gfg.get(2));
//        System.out.println("third element of second list ="
//                + gfg2.get(2));

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

                allItemsCard.setVisibility(View.GONE);
                searchCard.setVisibility(View.GONE);
//                System.out.println(categoryId);
//                subCategoryId = "";
                //filterCate(searchingCate);
                new SubCategoryCheck().execute();
            }
        });

        // Selecting Sub Category
        subCatSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                System.out.println(position+": "+name);
                searchItem.setText("");
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
                searchCard.setVisibility(View.VISIBLE);
                filterSubCate(searchingSubCate);

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

        new Check().execute();


        return view;
    }

    private void filter(String text) {
        if (!text.isEmpty()) {
            filteredList = new ArrayList<>();
            // copying one arraylist to another
            newItemLists = new ArrayList<ItemAllSubLists>(itemAllSubLists);

            //newItemLists.addAll(itemAllSubLists);
            //newItemLists = (ArrayList<ItemAllSubLists>) itemAllSubLists.clone();
            //Collections.copy(newItemLists,itemAllSubLists);
            //newItemLists = new ArrayList<ItemAllSubLists>(itemAllSubLists);
            //newItemLists.add(new ItemAllSubLists("dad","ddd","dassd","dasds",new ArrayList<>()));

//        System.out.println(newItemLists.size());
//        System.out.println(itemAllSubLists.size());
//
//        System.out.println(newItemLists.size());
//        System.out.println(itemAllSubLists.size());

//        newItemLists = itemAllSubLists;
//        for (int i = 0; i < itemAllSubLists.size(); i++) {
//            if (itemAllSubLists.get(i).getSubName().toLowerCase().contains(searchingSubCate.toLowerCase())) {
//                ArrayList<ItemAllLists> itemAllLists = itemAllSubLists.get(i).getAllLists();
//                System.out.println(searchingSubCate +":old: "+ itemAllLists.size());
//
////                for (ItemAllLists item2 : itemAllLists) {
////                    if (item2.getItemName().toLowerCase().contains(text.toLowerCase())) {
////                        System.out.println("old STOCK: " + item2.getStock());
////                    }
////                }
//            }
//        }

//        for (int i = 0; i < newItemLists.size(); i++) {
//            if (newItemLists.get(i).getSubName().toLowerCase().contains(searchingSubCate.toLowerCase())) {
//                ArrayList<ItemAllLists> itemAllLists = newItemLists.get(i).getAllLists();
//                System.out.println(searchingSubCate +":new: "+ itemAllLists.size());
//
////                for (ItemAllLists item2 : itemAllLists) {
////                    if (item2.getItemName().toLowerCase().contains(text.toLowerCase())) {
////                        System.out.println("new STOCK: " + item2.getStock());
////                    }
////                }
//            }
//        }
            for (ItemAllSubLists newItem : newItemLists) {
                if (!searchingCate.isEmpty()) {
                    if (!searchingSubCate.isEmpty()) {
                        if (newItem.getSubName().toLowerCase().contains(searchingSubCate.toLowerCase())) {
                            String subName = newItem.getSubName();
                            String subId = newItem.getSubId();
                            String totalQty= newItem.getTotalQty();
                            String totalVal = newItem.getTotalVal();
                            ArrayList<ItemAllLists> itemAllLists = new ArrayList<ItemAllLists>(newItem.getAllLists());
                            System.out.println(itemAllLists.size());

                            ArrayList<ItemAllLists> filteredList1 = new ArrayList<>();

                            for (ItemAllLists item2 : itemAllLists) {
                                if (item2.getItemName().toLowerCase().contains(text.toLowerCase())) {
                                    filteredList1.add(item2);
                                    //item2.setStock("250");
                                }
                            }
                            ItemAllSubLists ddaass = new ItemAllSubLists(subName,subId,totalQty,totalVal,filteredList1);
                            System.out.println(filteredList1.size());
                            //newItem.setAllLists(filteredList1);
                            //newItem = ddaass;
                            filteredList.add(ddaass);
                        }
                    }
                }
            }

            //allItemSubAdapter.filterList(filteredList);

            totalTotalQ = 0;
            totalTotalV = 0.0;

            for (int i = 0 ; i < filteredList.size(); i++) {
                ArrayList<ItemAllLists> itemAllLists = filteredList.get(i).getAllLists();
                int totalQty = 0;
                double totalValue = 0.0;
                for (int j = 0; j < itemAllLists.size(); j++) {
                    if (itemAllLists.get(j).getStock() != null) {
                        if (!itemAllLists.get(j).getStock().isEmpty()) {
                            totalQty = totalQty + Integer.parseInt(itemAllLists.get(j).getStock());
                        }
                    }


                    if (itemAllLists.get(j).getStockVal() != null) {
                        if (!itemAllLists.get(j).getStockVal().isEmpty()) {
                            totalValue = totalValue + Double.parseDouble(itemAllLists.get(j).getStockVal());
                        }
                    }
                }
                filteredList.get(i).setTotalQty(String.valueOf(totalQty));
                filteredList.get(i).setTotalVal(String.valueOf(totalValue));
                totalTotalQ = totalTotalQ + totalQty;
                totalTotalV = totalTotalV + totalValue;
            }

            allItemSubAdapter.filterList(filteredList);

            DecimalFormat formatter = new DecimalFormat("##,##,##,###.##");
            String formatted = formatter.format(totalTotalV);

            totalQ.setText(String.valueOf(totalTotalQ));
            totalV.setText(formatted);
        } else {
            System.out.println("empty paise");
        }

    }

    private void filterSubCate(String text) {

        filteredList = new ArrayList<>();
        for (ItemAllSubLists item : itemAllSubLists) {
            if (!searchingCate.isEmpty()) {
                if (item.getSubName().toLowerCase().contains(text.toLowerCase())){
                    filteredList.add((item));
                }
            }
        }

        allItemSubAdapter.filterList(filteredList);

        totalTotalQ = 0;
        totalTotalV = 0.0;

        for (int i = 0 ; i < filteredList.size(); i++) {
            totalTotalQ = totalTotalQ + Integer.parseInt(filteredList.get(i).getTotalQty());
            totalTotalV = totalTotalV + Double.parseDouble(filteredList.get(i).getTotalVal());
        }

        DecimalFormat formatter = new DecimalFormat("##,##,##,###.##");
        String formatted = formatter.format(totalTotalV);

        totalQ.setText(String.valueOf(totalTotalQ));
        totalV.setText(formatted);



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

                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < categoryLists.size(); i++) {
                    type.add(categoryLists.get(i).getType());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                categorySpinner.setAdapter(arrayAdapter);


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

                allItemsCard.setVisibility(View.VISIBLE);
                //searchCard.setVisibility(View.VISIBLE);

                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < subCategoryLists.size(); i++) {
                    type.add(subCategoryLists.get(i).getType());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                subCatSpinner.setAdapter(arrayAdapter);

                totalTotalQ = 0;
                totalTotalV = 0.0;

                for (int i = 0 ; i < itemAllSubLists.size(); i++) {
                    totalTotalQ = totalTotalQ + Integer.parseInt(itemAllSubLists.get(i).getTotalQty());
                    totalTotalV = totalTotalV + Double.parseDouble(itemAllSubLists.get(i).getTotalVal());
                }

                DecimalFormat formatter = new DecimalFormat("##,##,##,###.##");
                String formatted = formatter.format(totalTotalV);

                totalQ.setText(String.valueOf(totalTotalQ));
                totalV.setText(formatted);

                allItemSubAdapter = new AllItemSubAdapter(getContext(),itemAllSubLists);
                itemView.setAdapter(allItemSubAdapter);
                allItemSubAdapter.notifyDataSetChanged();

//                System.out.println(anotherNewItemLists.hashCode());
//                System.out.println(newItemLists.hashCode());


//                newItemLists = new ArrayList<ItemAllSubLists>(itemAllSubLists);
////                //newItemLists.addAll(itemAllSubLists);
////
//////                Iterator<ItemAllSubLists> iterator = itemAllSubLists.iterator();
//////                while(iterator.hasNext()){
//////                    newItemLists.add((ItemAllSubLists) iterator.next());
//////                }
//////                newItemLists.add(new ItemAllSubLists("dad","ddd","dassd","dasds",new ArrayList<>()));
//////
//                System.out.println(newItemLists.get(1).getSubName());
//                System.out.println(itemAllSubLists.get(1).getSubName());
//
////                ItemAllSubLists dasdad = newItemLists.get(1);
////                dasdad.setSubName("JACK SPARROW");
//
//                ItemAllSubLists dasd = new ItemAllSubLists("JACK SPARROW","455555","45","23232",new ArrayList<>());
////
//                newItemLists.set(1,dasd);
////                //System.out.println(anotherNewItemLists.get(1).getSubName());
////
////                newItemLists.get(1).setSubName("JACK SPARROW");
////
//                System.out.println(newItemLists.get(1).getSubName());
//                System.out.println(itemAllSubLists.get(1).getSubName());
//
//                newItemLists = new ArrayList<ItemAllSubLists>(itemAllSubLists);
//
//                System.out.println(newItemLists.get(1).getSubName());
//                System.out.println(itemAllSubLists.get(1).getSubName());
//               // System.out.println(anotherNewItemLists.get(1).getSubName());
//
//                System.out.println(newItemLists.size());
//                System.out.println(itemAllSubLists.size());
//
////                anotherNewItemLists.get(1).setSubName("JACK FINISH");
////
////                System.out.println(newItemLists.get(1).getSubName());
////                System.out.println(itemAllSubLists.get(1).getSubName());
////                System.out.println(anotherNewItemLists.get(1).getSubName());
//
//                System.out.println(anotherNewItemLists.hashCode());
//                System.out.println(newItemLists.hashCode());




                //searchItem.setText("");


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
            //categoryLists.add(new ReceiveTypeList("","All Categories"));


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
            //itemAllSubLists = new ArrayList<>();
            itemAllSubLists.clear();
            newItemLists = new ArrayList<>();

            if (categoryId.isEmpty()) {
                categoryId = null;
            }

            ResultSet resultSet1 = stmt.executeQuery("SELECT DISTINCT V.ISC_ID, V.SUBCATM_NAME\n" +
                    "  FROM ITEM_DETAILS_V V where V.IM_ID = "+categoryId+"");

            while (resultSet1.next()) {

                subCategoryLists.add(new ReceiveTypeList(resultSet1.getString(1),resultSet1.getString(2)));

                String isc_id = resultSet1.getString(1);
                String name = resultSet1.getString(2);
                //System.out.println(isc_id);


            }

            resultSet1.close();

            for (int j = 0; j < subCategoryLists.size(); j++) {

                ArrayList<ItemAllLists> itemAllLists = new ArrayList<>();

                String isc_id = subCategoryLists.get(j).getId();
                String name = subCategoryLists.get(j).getType();
                ResultSet resultSet2 = stmt.executeQuery("SELECT DISTINCT ITEM_DTL.ITEM_CODE, ITEM_DTL.ITEM_REFERENCE_NAME, ITEM_DTL.ITEM_DES, \n" +
                        "    ITEM_DTL.ITEM_STOCK, ITEM_DTL.ITEM_SALES_PRICE, ITEM_DTL.ITEM_STOCK_AMT, \n" +
                        "    ITEM_MST.IM_NAME, 'Category: '||ITEM_MST.IM_NAME CATEGORY, SUBCAT_MST.SUBCATM_NAME, \n" +
                        "    SUBCAT_MST.SUBCATM_ID, \n" +
                        "    ITEM_PACK.GET_ITEM_STOCK_QTY(ITEM_DTL.ITEM_ID,NULL,NULL,SYSDATE) STOCK,\n" +
                        "    ITEM_PACK.GET_ITEM_STOCK_VAL(ITEM_DTL.ITEM_ID,NULL,NULL,SYSDATE) STOCK_VAL, \n" +
                        "    ITEM_DTL.ITEM_ID,\n" +
                        "    ITEM_PACK.GET_ITEM_HS_CODE(ITEM_ID) ITEM_HS_CODE,\n" +
                        "    ITEM_PACK.GET_ITEM_PART_NUMBER(ITEM_ID) ITEM_PART_NUMBER,\n" +
                        "    ITEM_PACK.GET_ITEM_UNIT(ITEM_ID) ITEM_UNIT,\n" +
                        "    ITEM_PACK.GET_ITEM_STATUS(ITEM_ID) ITEM_STATUS,\n" +
                        "    ITEM_PACK.GET_ITEM_COST_PRICE(ITEM_ID) ITEM_COST_PRICE,\n" +
                        "    ITEM_PACK.GET_ITEM_VAT_PCT_PURCHASE(ITEM_ID) ITEM_VAT_PERCENT_COST_PRICE,\n" +
                        "    ITEM_PACK.GET_ITEM_SELLING_PRICE(ITEM_ID) ITEM_SELLING_PRICE,\n" +
                        "    ITEM_PACK.GET_ITEM_VAT_PCT(ITEM_ID) ITEM_VAT_PERCENT_SELLING_PRICE,\n" +
                        "    ITEM_PACK.GET_ITEM_PROFILE_APPROVAL_DATE(ITEM_ID) ITEM_PROFILE_APPROVAL_DATE,\n" +
                        "    color_mst.color_name,\n" +
                        "    size_mst.size_name\n" +
                        "FROM ITEM_DTL, ITEM_MST, ITEM_SUBCAT, SUBCAT_MST, ORDER_DETAILES, COLOR_MST, SIZE_MST\n" +
                        "WHERE ((ITEM_DTL.ITEM_ISC_ID = ITEM_SUBCAT.ISC_ID)\n" +
                        " AND (ITEM_SUBCAT.ISC_IM_ID = ITEM_MST.IM_ID)\n" +
                        " AND (ITEM_SUBCAT.ISC_SUBCATM_ID = SUBCAT_MST.SUBCATM_ID)\n" +
                        " AND (ITEM_DTL.ITEM_ID = ORDER_DETAILES.ODR_ITEM_ID(+))\n" +
                        " AND (color_mst.color_id = item_dtl.item_color_id)\n" +
                        " AND (size_mst.size_id = item_dtl.item_size_id)\n" +
                        " AND ITEM_DTL.ITEM_ISC_ID = "+isc_id+")\n" +
                        "ORDER BY ITEM_DTL.ITEM_REFERENCE_NAME");

                int i = 0;
                while (resultSet2.next()) {
                    //System.out.println(resultSet2.getString(2));
                    i++;
                    itemAllLists.add(new ItemAllLists(String.valueOf(i),resultSet2.getString(1),resultSet2.getString(2),resultSet2.getString(3),
                            resultSet2.getString(4),resultSet2.getString(5),resultSet2.getString(6),
                            resultSet2.getString(7),resultSet2.getString(8),resultSet2.getString(9),
                            resultSet2.getString(10),resultSet2.getString(11),resultSet2.getString(12),
                            resultSet2.getString(13),resultSet2.getString(14),resultSet2.getString(15),
                            resultSet2.getString(16),resultSet2.getString(17),resultSet2.getString(18),
                            resultSet2.getString(19),resultSet2.getString(20),resultSet2.getString(21),
                            resultSet2.getString(22),resultSet2.getString(23),resultSet2.getString(24)));
                }

                resultSet2.close();

                totalQty = 0;
                totalValue = 0.0;
                System.out.println(isc_id);
                for (int a = 0; a < itemAllLists.size(); a++) {
                    if (itemAllLists.get(a).getStock() != null) {
                        if (!itemAllLists.get(a).getStock().isEmpty()) {
                            totalQty = totalQty + Integer.parseInt(itemAllLists.get(a).getStock());
                        }
                    }

                    if (itemAllLists.get(a).getStockVal() != null) {
                        if (!itemAllLists.get(a).getStockVal().isEmpty()) {
                            totalValue = totalValue + Double.parseDouble(itemAllLists.get(a).getStockVal());
                        }
                    }
                }

                System.out.println(totalQty);
                System.out.println(totalValue);

                itemAllSubLists.add(new ItemAllSubLists(name,isc_id,String.valueOf(totalQty),
                        String.valueOf(totalValue),itemAllLists));

            }


            subCategoryLists.add(new ReceiveTypeList("","All Sub Categories"));


            stmt.close();

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
}
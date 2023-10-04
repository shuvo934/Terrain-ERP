package ttit.com.shuvo.terrainerp.fragments;

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
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rosemaryapp.amazingspinner.AmazingSpinner;

import java.util.ArrayList;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.WaitProgress;
import ttit.com.shuvo.terrainerp.adapters.ItemWiswStockAdapter;
import ttit.com.shuvo.terrainerp.arrayList.ItemWiseStockLists;
import ttit.com.shuvo.terrainerp.arrayList.ReceiveTypeList;
import ttit.com.shuvo.terrainerp.arrayList.WareHouseQtyLists;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemWiseStockFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemWiseStockFragment extends Fragment implements ItemWiswStockAdapter.ClickedItem {

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
//
//    private Connection connection;

    AmazingSpinner categorySpinner;
    AmazingSpinner subCatSpinner;

    TextInputEditText searchItem;
    TextInputLayout searchItemLay;

    RecyclerView itemView;
    ItemWiswStockAdapter itemWiswStockAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<ItemWiseStockLists> stockItemLists;
    ArrayList<ItemWiseStockLists> filteredList;

//    ArrayList<WareHouseQtyLists> wareHouseQtyLists;

    ArrayList<ReceiveTypeList> categoryLists;
    ArrayList<ReceiveTypeList> subCategoryLists;

    String categoryId = "";
    String subCategoryId = "";
    String searchingCate = "";
    String searchingSubCate = "";
    String searchingName = "";



    Context mContext;
    public ItemWiseStockFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemWiseStockFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemWiseStockFragment newInstance(String param1, String param2) {
        ItemWiseStockFragment fragment = new ItemWiseStockFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext=context;
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
        View view = inflater.inflate(R.layout.fragment_item_wise_stock, container, false);

        categorySpinner = view.findViewById(R.id.cat_type_spinner_item_stock);
        subCatSpinner = view.findViewById(R.id.sub_cat_type_spinner_item_stock);

        searchItem = view.findViewById(R.id.search_item_name_stock_item);
        searchItemLay = view.findViewById(R.id.search_item_name_lay_stock_item);

        itemView = view.findViewById(R.id.item_overview_relation_stock_item);

        categoryLists = new ArrayList<>();
        subCategoryLists = new ArrayList<>();
        stockItemLists = new ArrayList<>();
//        wareHouseQtyLists = new ArrayList<>();
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

//        new Check().execute();
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

//    private void filter(String text) {
//
//        filteredList = new ArrayList<>();
//        for (ItemWiseStockLists item : stockItemLists) {
//            if (item.getItem_name().toLowerCase().contains(text.toLowerCase()) || item.getItem_code().toLowerCase().contains(text.toLowerCase())) {
//                filteredList.add((item));
//                isfiltered = true;
//            }
//        }
//        itemWiswStockAdapter.filterList(filteredList);
//    }

    private void filter(String text) {

        filteredList = new ArrayList<>();
        for (ItemWiseStockLists item : stockItemLists) {
            if (searchingSubCate.isEmpty()) {
                if (searchingCate.isEmpty()) {
                    if (item.getItem_name().toLowerCase().contains(text.toLowerCase()) || item.getItem_code().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add((item));
                    }
                } else {
                    if (item.getCate_name().toLowerCase().contains(searchingCate.toLowerCase())) {
                        if (item.getItem_name().toLowerCase().contains(text.toLowerCase()) || item.getItem_code().toLowerCase().contains(text.toLowerCase())) {
                            filteredList.add((item));
                        }
                    }
                }
            } else {
                if (searchingCate.isEmpty()) {
                    if (item.getSub_cate_name().toLowerCase().contains(searchingSubCate.toLowerCase())) {
                        if (item.getItem_name().toLowerCase().contains(text.toLowerCase()) || item.getItem_code().toLowerCase().contains(text.toLowerCase())) {
                            filteredList.add((item));

                        }
                    }
                } else {
                    if (item.getCate_name().toLowerCase().contains(searchingCate.toLowerCase())) {
                        if (item.getSub_cate_name().toLowerCase().contains(searchingSubCate.toLowerCase())) {
                            if (item.getItem_name().toLowerCase().contains(text.toLowerCase()) || item.getItem_code().toLowerCase().contains(text.toLowerCase())) {
                                filteredList.add((item));

                            }
                        }
                    }
                }

            }

        }
        itemWiswStockAdapter.filterList(filteredList);
    }

    private void filterCate(String text) {

        filteredList = new ArrayList<>();
        for (ItemWiseStockLists item : stockItemLists) {
            if (searchingSubCate.isEmpty()){
                if (searchingName.isEmpty()) {
                    if (item.getCate_name().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add((item));
                    }
                } else {
                    if (item.getItem_name().toLowerCase().contains(searchingName.toLowerCase()) || item.getItem_code().toLowerCase().contains(searchingName.toLowerCase())) {
                        if (item.getCate_name().toLowerCase().contains(text.toLowerCase())) {
                            filteredList.add((item));
                        }
                    }
                }
            } else {
                if (searchingName.isEmpty()) {
                    if (item.getSub_cate_name().toLowerCase().contains(searchingSubCate.toLowerCase())) {
                        if (item.getCate_name().toLowerCase().contains(text.toLowerCase())) {
                            filteredList.add((item));
                        }
                    }
                } else {
                    if (item.getSub_cate_name().toLowerCase().contains(searchingSubCate.toLowerCase())) {
                        if (item.getItem_name().toLowerCase().contains(searchingName.toLowerCase()) || item.getItem_code().toLowerCase().contains(searchingName.toLowerCase())) {
                            if (item.getCate_name().toLowerCase().contains(text.toLowerCase())) {
                                filteredList.add((item));
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
        itemWiswStockAdapter.filterList(filteredList);
    }

    private void filterSubCate(String text) {

        filteredList = new ArrayList<>();
        for (ItemWiseStockLists item : stockItemLists) {
            if (searchingCate.isEmpty()) {
                if (searchingName.isEmpty()) {
                    if (item.getSub_cate_name().toLowerCase().contains(text.toLowerCase())){
                        filteredList.add((item));
                    }
                } else {
                    if (item.getItem_name().toLowerCase().contains(searchingName.toLowerCase()) || item.getItem_code().toLowerCase().contains(searchingName.toLowerCase())){
                        if (item.getSub_cate_name().toLowerCase().contains(text.toLowerCase())){
                            filteredList.add((item));
                        }
                    }
                }
            } else {
                if (searchingName.isEmpty()) {
                    if (item.getCate_name().toLowerCase().contains(searchingCate.toLowerCase())){
                        if (item.getSub_cate_name().toLowerCase().contains(text.toLowerCase())){
                            filteredList.add((item));
                        }
                    }
                } else {
                    if (item.getCate_name().toLowerCase().contains(searchingCate.toLowerCase())){
                        if (item.getItem_name().toLowerCase().contains(searchingName.toLowerCase()) || item.getItem_code().toLowerCase().contains(searchingName.toLowerCase())){
                            if (item.getSub_cate_name().toLowerCase().contains(text.toLowerCase())){
                                filteredList.add((item));
                            }
                        }
                    }
                }
            }

        }
        itemWiswStockAdapter.filterList(filteredList);
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
//
//
//
//                itemWiswStockAdapter = new ItemWiswStockAdapter(stockItemLists, getContext(),ItemWiseStockFragment.this);
//                itemView.setAdapter(itemWiswStockAdapter);
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
                getItems();
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
//            stockItemLists = new ArrayList<>();
//
//            Statement stmt = connection.createStatement();
//
//
//
//            CallableStatement callableStatement = connection.prepareCall("begin ? := GET_WH_WISE_ITEM_STOCK(?,?); end;");
//            callableStatement.setString(2,null);
//            callableStatement.setString(3,null);
//            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
//            callableStatement.execute();
//
//            ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
//
//            int i = 0;
//            while (resultSet.next()) {
//                i++;
//
//                String item_id = resultSet.getString(3);
//
//                CallableStatement callableStatement1 = connection.prepareCall("begin ? := GET_ITEM_WISE_WH_STOCK(?); end;");
//                callableStatement1.setString(2,item_id);
//                callableStatement1.registerOutParameter(1,OracleTypes.CURSOR);
//                callableStatement1.execute();
//
//                ResultSet resultSet1 = (ResultSet) callableStatement1.getObject(1);
//
////                wareHouseQtyLists = new ArrayList<>();
//                int j = 0;
//                while (resultSet1.next()) {
//                    j++;
////                    wareHouseQtyLists.add(new WareHouseQtyLists(String.valueOf(j),resultSet1.getString(1),resultSet1.getString(3)));
//                }
//                callableStatement1.close();
////
////                stockItemLists.add(new ItemWiseStockLists(String.valueOf(i),resultSet.getString(10),resultSet.getString(11),resultSet.getString(4),resultSet.getString(8),resultSet.getString(5),
////                                    wareHouseQtyLists,resultSet.getString(6),resultSet.getString(2),resultSet.getString(7),resultSet.getString(9)));
//
//                System.out.println(i);
//            }
//
//            callableStatement.close();
//            connected = true;
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

//    public void getStockUpdate() {
//        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
//        waitProgress.setCancelable(false);
//        conn = false;
//        connected = false;
//        getItems(2);
//    }
//
//    private void updateInterface() {
//        waitProgress.dismiss();
//        if (conn) {
//            if (connected) {
//                conn = false;
//                connected = false;
//                updateUI();
//            }
//            else {
//                AlertDialog dialog = new AlertDialog.Builder(mContext)
//                        .setMessage("There is a network issue in the server. Please Try later.")
//                        .setPositiveButton("Retry", null)
//                        .setNegativeButton("Cancel", null)
//                        .show();
//
//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
//                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                positive.setOnClickListener(v -> {
//
//                    getStockUpdate();
//                    dialog.dismiss();
//                });
//
//                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                negative.setOnClickListener(v -> dialog.dismiss());
//            }
//        }
//        else {
//            AlertDialog dialog = new AlertDialog.Builder(mContext)
//                    .setMessage("Please Check Your Internet Connection")
//                    .setPositiveButton("Retry", null)
//                    .setNegativeButton("Cancel", null)
//                    .show();
//
//            dialog.setCancelable(false);
//            dialog.setCanceledOnTouchOutside(false);
//            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//            positive.setOnClickListener(v -> {
//
//                getStockUpdate();
//                dialog.dismiss();
//            });
//
//            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//            negative.setOnClickListener(v -> dialog.dismiss());
//        }
//    }

    public void getItems() {
        stockItemLists = new ArrayList<>();

        String url = "http://103.56.208.123:8001/apex/tterp/itemWiseStock/getStock?whm_id=&isc_id=";
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
                        String wh_wise_item_stock = info.getString("wh_wise_item_stock");
                        JSONArray itemsArray = new JSONArray(wh_wise_item_stock);
                        for (int j = 0; j < itemsArray.length(); j++) {
                            JSONObject stock_info = itemsArray.getJSONObject(j);

//                            String total_stock = stock_info.getString("total_stock")
//                                    .equals("null") ? "" : stock_info.getString("total_stock");
                            String total_stock_val = stock_info.getString("total_stock_val")
                                    .equals("null") ? "" : stock_info.getString("total_stock_val");
                            String item_id = stock_info.getString("item_id")
                                    .equals("null") ? "" : stock_info.getString("item_id");
                            String item_reference_name = stock_info.getString("item_reference_name")
                                    .equals("null") ? "" : stock_info.getString("item_reference_name");
                            String item_unit = stock_info.getString("item_unit")
                                    .equals("null") ? "" : stock_info.getString("item_unit");
                            String vat_pct = stock_info.getString("vat_pct")
                                    .equals("null") ? "" : stock_info.getString("vat_pct");
                            String selling_price = stock_info.getString("selling_price")
                                    .equals("null") ? "" : stock_info.getString("selling_price");
                            String item_code = stock_info.getString("item_code")
                                    .equals("null") ? "" : stock_info.getString("item_code");
                            String item_hs_code = stock_info.getString("item_hs_code")
                                    .equals("null") ? "" : stock_info.getString("item_hs_code");
                            String im_name = stock_info.getString("im_name")
                                    .equals("null") ? "" : stock_info.getString("im_name");
                            String subcatm_name = stock_info.getString("subcatm_name")
                                    .equals("null") ? "" : stock_info.getString("subcatm_name");

                            stockItemLists.add(new ItemWiseStockLists(String.valueOf(j+1),im_name,subcatm_name,item_reference_name,item_code,item_unit,
                                    new ArrayList<>(),vat_pct,total_stock_val,selling_price,item_hs_code,item_id,false));

                        }
                    }
                }
                checkToGetNextList();
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

    public void checkToGetNextList() {
        if (stockItemLists.size() != 0) {
            boolean allUpdated = false;
            for (int i = 0; i < stockItemLists.size(); i++) {
                allUpdated = stockItemLists.get(i).isUpdated();
                if (!stockItemLists.get(i).isUpdated()) {
                    allUpdated = stockItemLists.get(i).isUpdated();
                    String item_id = stockItemLists.get(i).getItem_id();
                    getStockDetailsList(item_id, i);
                    break;
                }
            }
            if (allUpdated) {
                connected = true;
                updateFragment();
            }
        }
        else {
            connected = true;
            updateFragment();
        }
    }

    public void getStockDetailsList(String item_id, int index) {
        ArrayList<WareHouseQtyLists> wareHouseQtyLists = new ArrayList<>();

        String url = "http://103.56.208.123:8001/apex/tterp/itemWiseStock/getItemWiseStock?item_id="+item_id+"";
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
                        String item_wise_wh_stock = info.getString("item_wise_wh_stock");
                        JSONArray itemsArray = new JSONArray(item_wise_wh_stock);
                        for (int j = 0; j < itemsArray.length(); j++) {
                            JSONObject stock_info = itemsArray.getJSONObject(j);

                            String whm_name = stock_info.getString("whm_name");
//                            String sl_item_id = stock_info.getString("sl_item_id");
                            String stock = stock_info.getString("stock").equals("null") ? "" : stock_info.getString("stock");

                            wareHouseQtyLists.add(new WareHouseQtyLists(String.valueOf(j+1),whm_name,stock));

                        }
                    }
                }
                stockItemLists.get(index).setItem_qty(wareHouseQtyLists);
                stockItemLists.get(index).setUpdated(true);
                checkToGetNextList();
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

    private void updateUI() {
        itemWiswStockAdapter = new ItemWiswStockAdapter(stockItemLists, getContext(),ItemWiseStockFragment.this);
        itemView.setAdapter(itemWiswStockAdapter);
        searchItem.setText("");

        searchItem.clearFocus();
    }
}
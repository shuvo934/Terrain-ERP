package ttit.com.shuvo.icomerp.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
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
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rosemaryapp.amazingspinner.AmazingSpinner;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import oracle.jdbc.driver.OracleTypes;
import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.WaitProgress;
import ttit.com.shuvo.icomerp.adapters.StockItemAdapter;
import ttit.com.shuvo.icomerp.arrayList.ReceiveTypeList;
import ttit.com.shuvo.icomerp.arrayList.StockItemList;

import static ttit.com.shuvo.icomerp.OracleConnection.createConnection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReOrderFragment extends Fragment implements StockItemAdapter.ClickedItem {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    AmazingSpinner categorySpinner;
    AmazingSpinner subCatSpinner;

    LineChart lineChart;

    ArrayList<String> reOrderLevelLists;
    ArrayList<ReceiveTypeList> categoryLists;
    ArrayList<ReceiveTypeList> subCategoryLists;
    ArrayList<Entry> dataValue;

    LinearLayout afterSubCatSelect;

    TextInputEditText searchItem;
    TextInputLayout searchItemLay;
    RecyclerView itemView;
    StockItemAdapter itemAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<StockItemList> reorderItemLists;
    ArrayList<StockItemList> filteredList;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
    private Boolean fromSub = false;
    private Boolean isfiltered = false;

    private Connection connection;

    String categoryId = "";
    String subCategoryId = "";

    Context mContext;
    public ReOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReOrderFragment newInstance(String param1, String param2) {
        ReOrderFragment fragment = new ReOrderFragment();
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
        View view = inflater.inflate(R.layout.fragment_re_order, container, false);

        afterSubCatSelect = view.findViewById(R.id.after_selecting_sub_category_re_order);
        afterSubCatSelect.setVisibility(View.GONE);

        categorySpinner = view.findViewById(R.id.cat_type_spinner_re_order);
        subCatSpinner = view.findViewById(R.id.sub_cat_type_spinner_re_order);

        searchItem = view.findViewById(R.id.search_item_name_re_order);
        searchItemLay = view.findViewById(R.id.search_item_name_lay_re_order);
        itemView = view.findViewById(R.id.item_overview_relation_re_order);

        lineChart = view.findViewById(R.id.re_order_level_chart);

        categoryLists = new ArrayList<>();
        subCategoryLists = new ArrayList<>();
        dataValue = new ArrayList<>();
        reorderItemLists = new ArrayList<>();
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
                for (int i = 0; i <categoryLists.size(); i++) {
                    if (name.equals(categoryLists.get(i).getType())) {
                        categoryId = categoryLists.get(i).getId();
                    }
                }
                System.out.println(categoryId);
                subCategoryId = "";
                new ReOrderLevelCheck().execute();
                afterSubCatSelect.setVisibility(View.GONE);
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
                    }
                }
                System.out.println(subCategoryId);
                fromSub = true;
                new ReOrderLevelCheck().execute();
//                afterCatSelect.setVisibility(View.GONE);
//                afterSubCatSelect.setVisibility(View.GONE);
//                toSeCat.setText(toSeCatMsg);
//                toSeSubCat.setText(toSeSubCatMSG);
//                afterItem.setVisibility(View.GONE);
//                toSeItem.setText(toSeItemMSG);
            }
        });

        LineChartInit();

        searchItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

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


        return  view;
    }

    public void LineChartInit() {

//        monthName = new ArrayList<>();
//        monthName.add("JAN");
//        monthName.add("FEB");
//        monthName.add("MAR");
//        monthName.add("APR");
//        monthName.add("MAY");
//        monthName.add("JUN");

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.GRAY);
        lineChart.getDescription().setEnabled(false);
        lineChart.setPinchZoom(false);

        lineChart.setDrawGridBackground(false);

        lineChart.getAxisLeft().setDrawGridLines(true);
        lineChart.setScaleEnabled(false);
        lineChart.setTouchEnabled(false);
        lineChart.setDoubleTapToZoomEnabled(false);

        lineChart.getAxisRight().setEnabled(false);
        //lineChart.getAxisLeft().setAxisMinValue(0);
        lineChart.getLegend().setEnabled(false);
        //lineChart.getAxisLeft().mAxisMinimum = 5000f;
        lineChart.getAxisLeft().setTextColor(Color.GRAY);
        lineChart.getAxisLeft().setXOffset(10f);
        lineChart.getAxisLeft().setTextSize(8);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisLeft().setGranularity(1);



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
        for (StockItemList item : reorderItemLists) {
            if (item.getItemName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add((item));
                isfiltered = true;
            }
        }
        itemAdapter.filterList(filteredList);
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

                new ReOrderLevelCheck().execute();

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

//            waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
//            waitProgress.setCancelable(false);
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

    public class ReOrderLevelCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                ReOrderLevelData();
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


            if (conn) {

                conn = false;
                connected = false;



//                reOrderLevelLists = new ArrayList<>();
//
//                reOrderLevelLists.add("45");
//                reOrderLevelLists.add("55");
//                reOrderLevelLists.add("145");
//                reOrderLevelLists.add("245");
//                reOrderLevelLists.add("345");
//                reOrderLevelLists.add("440");
//                reOrderLevelLists.add("540");
//                reOrderLevelLists.add("640");
//                reOrderLevelLists.add("700");
//                reOrderLevelLists.add("840");



                if (reOrderLevelLists.size() > 7) {

                    int first = Integer.parseInt(reOrderLevelLists.get(0));
                    int last = Integer.parseInt(reOrderLevelLists.get(reOrderLevelLists.size() - 1));

                    ArrayList<String> strReOrder = new ArrayList<>();

                    strReOrder = reOrderLevelLists;

                    strReOrder.remove(0);
                    strReOrder.remove(strReOrder.size()-1);

                    int arraySize = strReOrder.size() / 5;
                    int newArraySize = 0;
                    int defaultSize = arraySize;

                    ArrayList<String> firstArray = new ArrayList<>();
                    ArrayList<String> secondArray = new ArrayList<>();
                    ArrayList<String> thirdArray = new ArrayList<>();
                    ArrayList<String> fourthArray = new ArrayList<>();
                    ArrayList<String> fifthArray = new ArrayList<>();

                    for (int i = 0; i < arraySize; i++) {
                        firstArray.add(strReOrder.get(i));
                    }

                    newArraySize = arraySize;
                    arraySize = arraySize + defaultSize;

                    for (int i = newArraySize; i < arraySize; i++) {
                        secondArray.add(strReOrder.get(i));
                    }

                    newArraySize = arraySize;
                    arraySize = arraySize + defaultSize;

                    for (int i = newArraySize; i < arraySize; i++) {
                        thirdArray.add(strReOrder.get(i));
                    }

                    newArraySize = arraySize;
                    arraySize = arraySize + defaultSize;

                    for (int i = newArraySize; i < arraySize; i++) {
                        fourthArray.add(strReOrder.get(i));
                    }

                    newArraySize = arraySize;
                    arraySize = arraySize + defaultSize;

                    for (int i = newArraySize; i < strReOrder.size(); i++) {
                        fifthArray.add(strReOrder.get(i));
                    }

                    Random random = new Random();

                    int firstIndex = random.nextInt(firstArray.size());
                    int secondIndex = random.nextInt(secondArray.size());
                    int thirdIndex = random.nextInt(thirdArray.size());
                    int fourthIndex = random.nextInt(fourthArray.size());
                    int fifthIndex = random.nextInt(fifthArray.size());

                    dataValue = new ArrayList<>();

                    dataValue.add(new Entry(0,first,0));
                    dataValue.add(new Entry(1,Integer.parseInt(firstArray.get(firstIndex)),1));
                    dataValue.add(new Entry(2,Integer.parseInt(secondArray.get(secondIndex)),2));
                    dataValue.add(new Entry(3,Integer.parseInt(thirdArray.get(thirdIndex)),3));
                    dataValue.add(new Entry(4,Integer.parseInt(fourthArray.get(fourthIndex)),4));
                    dataValue.add(new Entry(5,Integer.parseInt(fifthArray.get(fifthIndex)),5));
                    dataValue.add(new Entry(6,last,6));



                }
                else if (reOrderLevelLists.size() == 1){

                    dataValue = new ArrayList<>();
                    for (int i = 0; i < reOrderLevelLists.size(); i++) {
                        dataValue.add(new Entry(i,Integer.parseInt(reOrderLevelLists.get(i)),i));
                    }
                    dataValue.add(new Entry(1,Integer.parseInt(reOrderLevelLists.get(0)),1));

                }
                else {
                    dataValue = new ArrayList<>();
                    for (int i = 0; i < reOrderLevelLists.size(); i++) {
                        dataValue.add(new Entry(i,Integer.parseInt(reOrderLevelLists.get(i)),i));
                    }
                }

                //lineChart.animateX(1000);
                lineChart.animateY(1000);
                LineDataSet lineDataSet = new LineDataSet(dataValue,"Re-Order Level");
                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(lineDataSet);

                LineData data1 = new LineData(lineDataSet);
                lineDataSet.setCircleColor(Color.parseColor("#b2bec3"));
                lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
                lineDataSet.setColor(Color.parseColor("#00cec9"));
                lineDataSet.setDrawFilled(true);
                Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.chart_fill_re_order);
                lineDataSet.setFillDrawable(drawable);

                lineDataSet.setDrawCircleHole(false);
                //lineChart.getXAxis().setValueFormatter(new DashboardFragment.MyAxisValueFormatter(monthName));
                lineChart.setData(data1);
                lineChart.invalidate();

                if (!fromSub) {
                    new SubCategoryCheck().execute();
                    subCatSpinner.setText("");
                    System.out.println("SUB CATE CLICKED");
                } else {
                    new ItemCheck().execute();
                    fromSub = false;
                }




            }else {
                waitProgress.dismiss();
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

                        new ReOrderLevelCheck().execute();
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

//            waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
//            waitProgress.setCancelable(false);
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

                afterSubCatSelect.setVisibility(View.VISIBLE);

                itemAdapter = new StockItemAdapter(reorderItemLists, getContext(),ReOrderFragment.this);
                itemView.setAdapter(itemAdapter);
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

    public void ReOrderLevelData() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            Statement stmt = connection.createStatement();

            reOrderLevelLists = new ArrayList<>();

            if (categoryId.isEmpty()) {
                categoryId = null;
            }
            if (subCategoryId.isEmpty()) {
                subCategoryId = null;
            }


            ResultSet resultSet1 = stmt.executeQuery("SELECT distinct\n" +
                    "  ITEM_DTL.ITEM_REORDER_LEVEL\n" +
                    "FROM ITEM_DTL,\n" +
                    "  ITEM_MST,\n" +
                    "  ITEM_SUBCAT,\n" +
                    "  RETAILER_MST,\n" +
                    "  SUBCAT_MST\n" +
                    "WHERE ( ITEM_DTL.ITEM_ISC_ID       = ITEM_SUBCAT.ISC_ID )\n" +
                    "AND ( ITEM_SUBCAT.ISC_IM_ID        = ITEM_MST.IM_ID )\n" +
                    "AND ITEM_DTL.ITEM_RETAILER_ID     = RETAILER_MST.RETAILER_ID(+)\n" +
                    "AND ITEM_SUBCAT.ISC_SUBCATM_ID    = SUBCAT_MST.SUBCATM_ID\n" +
                    "AND ( NVL(ITEM_PACK.GET_ITEM_STOCK_QTY (ITEM_DTL.ITEM_ID ,null,null,SYSDATE), 0) <= NVL(ITEM_DTL.ITEM_REORDER_LEVEL, 0) )\n" +
                    "AND ("+categoryId+" IS NULL OR ITEM_MST.IM_ID = "+categoryId+")\n" +
                    "AND ("+subCategoryId+" IS NULL OR ITEM_SUBCAT.ISC_ID = "+subCategoryId+")\n" +
                    "ORDER BY 1 ASC");

            while (resultSet1.next()) {
                reOrderLevelLists.add(resultSet1.getString(1));
            }

            if (categoryId == null) {
                categoryId = "";
            }
            if (subCategoryId == null) {
                subCategoryId = "";
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



    //getting Item Information Query
    public void ItemData() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            reorderItemLists = new ArrayList<>();

            Statement stmt = connection.createStatement();

            if (subCategoryId.isEmpty()) {
                subCategoryId = null;
            }
            if (categoryId.isEmpty()) {
                categoryId = null;
            }

            CallableStatement callableStatement = connection.prepareCall("begin ? := GET_ITEM_REORDER_LEVEL(?,?); end;");
            callableStatement.setString(2,(categoryId));
            callableStatement.setString(3,(subCategoryId));
            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement.execute();

            ResultSet resultSet = (ResultSet) callableStatement.getObject(1);

            while (resultSet.next()) {
                reorderItemLists.add(new StockItemList("Re-Order",resultSet.getString(1),resultSet.getString(2),
                        resultSet.getString(3),resultSet.getString(6),resultSet.getString(7)));
            }

            callableStatement.close();
            connected = true;



            if (subCategoryId == null) {
                subCategoryId = "";
            }
            if (categoryId == null) {
                categoryId = "";
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
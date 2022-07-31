package ttit.com.shuvo.icomerp.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.ColorUtils;
import androidx.core.widget.NestedScrollView;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rosemaryapp.amazingspinner.AmazingSpinner;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import oracle.jdbc.driver.OracleTypes;
import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.WaitProgress;
import ttit.com.shuvo.icomerp.adapters.ItemAdapter;
import ttit.com.shuvo.icomerp.adapters.ItemDetailsAdapter;
import ttit.com.shuvo.icomerp.arrayList.ChartValue;
import ttit.com.shuvo.icomerp.arrayList.ItemDetaisList;
import ttit.com.shuvo.icomerp.arrayList.ItemQtyList;
import ttit.com.shuvo.icomerp.arrayList.ItemViewList;
import ttit.com.shuvo.icomerp.arrayList.ReceiveTypeList;
import ttit.com.shuvo.icomerp.arrayList.SupplierList;

import static ttit.com.shuvo.icomerp.OracleConnection.createConnection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StockFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StockFragment extends Fragment implements ItemDetailsAdapter.ClickedItem {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    NestedScrollView scrollView;

    LinearLayout afterCatSelect;
    LinearLayout afterSubCatSelect;

    TextInputLayout wareLay;
    AmazingSpinner wareSpinner;

    TextView toSeCat;
    TextView totalStock;
    TextView selectedCatName;
    TextView selectedCatStockValue;
    ImageView catColorChange;
    TextView toSeSubCat;
    TextView selectedSubCName;
    TextView selectedSubCatStockValue;
    ImageView colorChangeSub;

    TextInputEditText searchItem;
    TextInputLayout searchItemLay;
    RecyclerView itemView;
    ItemDetailsAdapter itemAdapter;
    RecyclerView.LayoutManager layoutManager;


    PieChart pieChart;
    ArrayList<PieEntry> NoOfEmp;
    ArrayList<ChartValue> categoryValues;

    BarChart barChart;
    ArrayList<ChartValue> subCategoryValues;
    ArrayList<String> barItem;
    ArrayList<String> barValue;
    ArrayList<BarEntry> barEntries;

    String toSeCatMsg = "Category View (Select Category from Chart)";
    String toSeSubCatMSG = "Sub Category View (Select Sub Category from Chart)";

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
    Boolean isfiltered = false;

    private Connection connection;

    ArrayList<ReceiveTypeList> wareHouseLists;

    String warehouseId = "";
    String categoryId = "";
    String subCatrgoryId = "";
    String selectedCategoryName = "";
    String selectedSubCatName = "";
    int cc = 0;
    int cc1 = 0;

    ArrayList<ItemDetaisList> stockItemLists;
    ArrayList<ItemDetaisList> filteredList;

    Context mContext;

    public StockFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StockFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StockFragment newInstance(String param1, String param2) {
        StockFragment fragment = new StockFragment();
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
        View view = inflater.inflate(R.layout.fragment_stock, container, false);

        scrollView = view.findViewById(R.id.scrollview_stock);

        afterCatSelect = view.findViewById(R.id.after_selecting_category_stock);
        afterCatSelect.setVisibility(View.GONE);
        afterSubCatSelect = view.findViewById(R.id.after_selecting_sub_category_stock);
        afterSubCatSelect.setVisibility(View.GONE);

        wareLay = view.findViewById(R.id.spinner_layout_stock);
        wareSpinner = view.findViewById(R.id.warehouse_type_spinner);
        toSeCat = view.findViewById(R.id.to_select_category_msg_stock);
        totalStock = view.findViewById(R.id.total_stock_amount);
        selectedCatName = view.findViewById(R.id.selected_cat_name_stock);
        selectedCatStockValue = view.findViewById(R.id.selected_cat_amount_stock);
        catColorChange = view.findViewById(R.id.color_change_image_stock);

        toSeSubCat = view.findViewById(R.id.sub_category_selected_msg_stock);

        selectedSubCName = view.findViewById(R.id.selected_sub_cat_name_stock);
        colorChangeSub = view.findViewById(R.id.color_change_image_for_sub_stock);
        selectedSubCatStockValue = view.findViewById(R.id.selected_sub_cat_amount_stock);

        searchItem = view.findViewById(R.id.search_item_name_stock);
        searchItemLay = view.findViewById(R.id.search_item_name_lay_stock);
        itemView = view.findViewById(R.id.item_overview_relation_stock);


        pieChart = view.findViewById(R.id.stock_category_overview);
        barChart = view.findViewById(R.id.stock_sub_category_overview);

        wareHouseLists = new ArrayList<>();
        NoOfEmp = new ArrayList<>();
        categoryValues = new ArrayList<>();
        subCategoryValues = new ArrayList<>();
        barItem = new ArrayList<>();
        barValue = new ArrayList<>();
        barEntries = new ArrayList<>();
        stockItemLists = new ArrayList<>();
        filteredList = new ArrayList<>();

        Drawable background = catColorChange.getBackground();
        Drawable drawable = colorChangeSub.getBackground();

        itemView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        itemView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(itemView.getContext(),DividerItemDecoration.VERTICAL);
        itemView.addItemDecoration(dividerItemDecoration);



        // Selecting warehouse type
        wareSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                System.out.println(position+": "+name);
                wareLay.setHint("Warehouse");
                for (int i = 0; i <wareHouseLists.size(); i++) {
                    if (name.equals(wareHouseLists.get(i).getType())) {
                        warehouseId = wareHouseLists.get(i).getId();
                    }
                }
                System.out.println(warehouseId);
                new CategoryCheck().execute();
                afterCatSelect.setVisibility(View.GONE);
                afterSubCatSelect.setVisibility(View.GONE);
                toSeCat.setText(toSeCatMsg);
                toSeSubCat.setText(toSeSubCatMSG);
//                afterItem.setVisibility(View.GONE);
//                toSeItem.setText(toSeItemMSG);
            }
        });

        PieChartInit();

        pieChart.setClickable(true);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {


                float value = e.getY();

                categoryId = e.getData().toString();
                System.out.println(categoryId);

                int dataset = h.getDataSetIndex();
                float x = h.getX();

                cc = pieChart.getData().getDataSet().getColor((int) x);
                System.out.println(cc);


                System.out.println("dataSet: "+dataset);
                System.out.println("X: "+x);

                PieEntry pe = (PieEntry) e;
                selectedCategoryName =  pe.getLabel();
                if (!selectedCategoryName.equals("No Receive")) {
                    selectedCatName.setText(selectedCategoryName);
                    selectedCatName.setTextColor(cc);

                    System.out.println(selectedCategoryName);
                    if (background instanceof ShapeDrawable) {
                        ((ShapeDrawable)background).getPaint().setColor(cc);
                    } else if (background instanceof GradientDrawable) {
                        ((GradientDrawable)background).setColor(cc);
                    } else if (background instanceof ColorDrawable) {
                        ((ColorDrawable)background).setColor(cc);
                    }

                    selectedCatStockValue.setText(value+" BDT");
                    toSeCat.setText("Category View ("+selectedCategoryName+" Selected)");
                    toSeSubCat.setText(toSeSubCatMSG);
                    //toSeItem.setText(toSeItemMSG);

                    new SubCategoryCheck().execute();
                }

            }

            @Override
            public void onNothingSelected() {

                //pieChart.highlightValue(3.0f,0);
            }
        });

        BarChartInit();

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                float value = e.getY();

                subCatrgoryId = e.getData().toString();
                System.out.println(subCatrgoryId);

                float x = h.getX();

                cc1 = barChart.getBarData().getDataSetByIndex(0).getColor((int) x);
                System.out.println(cc1);
                cc1 = ColorUtils.blendARGB(cc1,Color.WHITE,0.3f);

                BarEntry br = (BarEntry) e;
                float ss = br.getX();
                selectedSubCatName = barItem.get((int)ss);
                System.out.println(selectedSubCatName);

                selectedSubCName.setText(selectedSubCatName);
                selectedSubCName.setTextColor(cc1);

                if (drawable instanceof ShapeDrawable) {
                    ((ShapeDrawable)drawable).getPaint().setColor(cc1);
                } else if (drawable instanceof GradientDrawable) {
                    ((GradientDrawable)drawable).setColor(cc1);
                } else if (drawable instanceof ColorDrawable) {
                    ((ColorDrawable)drawable).setColor(cc1);
                }

                selectedSubCatStockValue.setText(value+" BDT");
                toSeSubCat.setText("Sub Category View ("+selectedSubCatName+" Selected)");
//                toSeItem.setText(toSeItemMSG);
//
                new ItemCheck().execute();

            }

            @Override
            public void onNothingSelected() {

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
        for (ItemDetaisList item : stockItemLists) {
            if (item.getItem_name().toLowerCase().contains(text.toLowerCase()) || item.getItem_code().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add((item));
                isfiltered = true;
            }
        }
        itemAdapter.filterList(filteredList);
    }

    public void PieChartInit() {

        pieChart.setDrawEntryLabels(true);
        pieChart.setCenterTextSize(14);
        pieChart.setHoleRadius(40);
        pieChart.setTransparentCircleRadius(40);

        pieChart.setEntryLabelTextSize(11);
        pieChart.setEntryLabelColor(Color.DKGRAY);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setXOffset(10);
        l.setTextSize(12);
        l.setYOffset(50);
        l.setWordWrapEnabled(false);
        l.setDrawInside(false);
        l.setYOffset(5f);

    }

    public void BarChartInit() {

        subCategoryValues = new ArrayList<>();
        barItem = new ArrayList<>();
        barValue = new ArrayList<>();
        barEntries = new ArrayList<>();

        barChart.getDescription().setEnabled(false);
        barChart.setPinchZoom(false);

        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false);

        barChart.getAxisLeft().setDrawGridLines(true);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setDrawGridLines(false);

        barChart.getLegend().setFormToTextSpace(10);
        barChart.getLegend().setStackSpace(10);
        barChart.getLegend().setYOffset(10);
        barChart.setExtraOffsets(0,0,0,20);

        // zoom and touch disabled
        barChart.setScaleEnabled(false);
        barChart.setTouchEnabled(true);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setClickable(true);
        barChart.setHighlightPerTapEnabled(true);
        barChart.setHighlightPerDragEnabled(false);
        //salaryChart.setOnTouchListener(null);

        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setAxisMinValue(0);
        barChart.getLegend().setEnabled(false);


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

                SupplierData();
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
                for(int i = 0; i < wareHouseLists.size(); i++) {
                    type.add(wareHouseLists.get(i).getType());
                }

                ArrayAdapter<String> adapter =
                        new ArrayAdapter<String>(
                                mContext,
                                R.layout.dropdown_menu_popup_item,
                                type);

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                wareSpinner.setAdapter(arrayAdapter);

                new CategoryCheck().execute();

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

    public class CategoryCheck extends AsyncTask<Void, Void, Void> {

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

                NoOfEmp = new ArrayList<>();
                for (int i = 0 ; i < categoryValues.size(); i++) {
                    if (!categoryValues.get(i).getValue().equals("0")) {
                        NoOfEmp.add(new PieEntry(Float.parseFloat(categoryValues.get(i).getValue()),categoryValues.get(i).getType(),categoryValues.get(i).getId()));
                    }
                }
                if (NoOfEmp.size() == 0) {
                    NoOfEmp.add(new PieEntry(1,"No Receive",12403));
                }
//                NoOfEmp.add(new PieEntry(5500f,"Fluids",10));
//                NoOfEmp.add(new PieEntry(3000f,"Spare Parts",11));
//                NoOfEmp.add(new PieEntry(8000f,"Filter",12));
//                NoOfEmp.add(new PieEntry(7000f,"Printers",13));

                final int[] piecolors = new int[]{
                        Color.parseColor("#f1c40f"),
                        Color.parseColor("#9b59b6"),
                        Color.parseColor("#3498db"),
                        Color.parseColor("#2ecc71"),
                        Color.parseColor("#b71540"),
                        Color.parseColor("#0a3d62"),
                        Color.parseColor("#636e72"),
                        Color.parseColor("#01a3a4")};
                pieChart.animateXY(1000, 1000);

                PieDataSet dataSet = new PieDataSet(NoOfEmp, "");
                pieChart.animateXY(1000, 1000);
                pieChart.setEntryLabelColor(Color.TRANSPARENT);
                pieChart.highlightValues(null);
                pieChart.setExtraRightOffset(20);

                PieData data = new PieData(dataSet);
                String label = dataSet.getValues().get(0).getLabel();
                System.out.println(label);
                if (label.equals("No Receive")) {
                    dataSet.setValueTextColor(Color.TRANSPARENT);
                } else {
                    dataSet.setValueTextColor(Color.WHITE);
                }
                dataSet.setHighlightEnabled(true);
                dataSet.setValueTextSize(12);
                dataSet.setColors(ColorTemplate.createColors(piecolors));


                pieChart.setData(data);
                pieChart.invalidate();
                int totalValue =  0;
                for (int i = 0; i < NoOfEmp.size(); i++) {
                    if (NoOfEmp.get(i).getLabel().equals("No Receive")) {
                        totalValue = 0;
                    } else {
                        totalValue = totalValue + (int) NoOfEmp.get(i).getValue();
                    }
                }
                totalStock.setText(totalValue+" BDT");


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

                        new CategoryCheck().execute();
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

                for (int i = 0; i < subCategoryValues.size(); i++) {
                    barItem.add(subCategoryValues.get(i).getType());
                }

                for (int i = 0; i < subCategoryValues.size(); i++) {
                    barEntries.add(new BarEntry(i,Float.parseFloat(subCategoryValues.get(i).getValue()), subCategoryValues.get(i).getId()));
                }


                BarDataSet bardataset = new BarDataSet(barEntries, "Months");
                barChart.animateY(1000);
                barChart.highlightValues(null);

                BarData data1 = new BarData(bardataset);
                if (cc == 0) {
                    bardataset.setColors(ColorTemplate.VORDIPLOM_COLORS);
                } else {
                    //int newCC = darkenColor(cc,0.8f);

                    bardataset.setColor(cc);
                }

                bardataset.setBarBorderColor(Color.DKGRAY);
                bardataset.setValueTextSize(12);
                barChart.setData(data1);
//                barChart.getXAxis().setValueFormatter(new MyAxisValueFormatter(barItem));
                barChart.getXAxis().setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getAxisLabel(float value, AxisBase axis) {
                        //System.out.println(value);
                        float size = barItem.size() - 1;
                        if (value <= size) {
                            return (barItem.get((int) value));
                        } else {
                            return super.getAxisLabel(value, axis);
                        }

                    }
                });

                afterCatSelect.setVisibility(View.VISIBLE);

                afterSubCatSelect.setVisibility(View.GONE);

                scrollView.fullScroll(View.FOCUS_DOWN);
                //scrollView.smoothScrollTo(0, scrollView.getBottom());

                scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        scrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        scrollView.fullScroll(View.FOCUS_DOWN);
//                        scrollView.pageScroll(View.FOCUS_DOWN);
                    }
                });
//                afterItem.setVisibility(View.GONE);

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

                afterSubCatSelect.setVisibility(View.VISIBLE);

                itemAdapter = new ItemDetailsAdapter(stockItemLists, getContext(),StockFragment.this);
                itemView.setAdapter(itemAdapter);
                searchItem.setText("");


                scrollView.fullScroll(View.FOCUS_DOWN);

//                System.out.println(searchItem.getBaseline()+" : "+ searchItem.getBottom());
//                scrollView.scrollTo(searchItem.getBottom(),searchItem.getBottom());

                scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        scrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        scrollView.fullScroll(View.FOCUS_DOWN);
                        searchItem.clearFocus();
//                        scrollView.scrollTo(searchItem.getBottom(),searchItem.getBottom());

//                        scrollView.pageScroll(View.FOCUS_DOWN);
                    }
                });

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

    public void SupplierData() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            Statement stmt = connection.createStatement();

            wareHouseLists = new ArrayList<>();



            ResultSet resultSet1 = stmt.executeQuery("SELECT DISTINCT\n" +
                    "          COM_PACK.GET_WHM_NAME (SL_WHM_ID)\n" +
                    "       \n" +
                    "          AS WHD_RACK_NAME,\n" +
                    "       SL_WHM_ID\n" +
                    "  FROM STOCK_LEDGER");

            while (resultSet1.next()) {
                wareHouseLists.add(new ReceiveTypeList(resultSet1.getString(2),resultSet1.getString(1)));
            }
            wareHouseLists.add(new ReceiveTypeList("","All Warehouse"));


            connected = true;

            connection.close();

        }
        catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void CategoryData() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            categoryValues = new ArrayList<>();
            Statement stmt = connection.createStatement();

            if (warehouseId.isEmpty()) {
                warehouseId = null;
            }

            CallableStatement callableStatement = connection.prepareCall("begin ? := GET_WH_WISE_CATEGORY_STOCK (?); end;");
            callableStatement.setString(2,warehouseId);

            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement.execute();

            ResultSet resultSet = (ResultSet) callableStatement.getObject(1);


            while (resultSet.next()) {
                categoryValues.add(new ChartValue(resultSet.getString(3), resultSet.getString(4),resultSet.getString(2)));
            }

            callableStatement.close();



            connected = true;


            if (warehouseId == null) {
                warehouseId = "";
            }

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

            subCategoryValues = new ArrayList<>();
            barItem = new ArrayList<>();
            barValue = new ArrayList<>();
            barEntries = new ArrayList<>();
            Statement stmt = connection.createStatement();

            if (warehouseId.isEmpty()) {
                warehouseId = null;
            }
            if (categoryId.isEmpty()) {
                categoryId = null;
            }


            CallableStatement callableStatement = connection.prepareCall("begin ? := GET_WH_WISE_SUBCATEGORY_STOCK(?,?); end;");
            callableStatement.setString(2,warehouseId);
            callableStatement.setInt(3,Integer.parseInt(categoryId));
            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement.execute();

            ResultSet resultSet = (ResultSet) callableStatement.getObject(1);



            while (resultSet.next()) {
                subCategoryValues.add(new ChartValue(resultSet.getString(3), resultSet.getString(4),resultSet.getString(2)));
            }
            callableStatement.close();
            connected = true;


            if (warehouseId == null) {
                warehouseId = "";
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

    public void ItemData() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            stockItemLists = new ArrayList<>();

            Statement stmt = connection.createStatement();
            if (warehouseId.isEmpty()) {
                warehouseId = null;
            }
            if (subCatrgoryId.isEmpty()) {
                subCatrgoryId = null;
            }

            CallableStatement callableStatement = connection.prepareCall("begin ? := GET_WH_WISE_ITEM_STOCK(?,?); end;");
            callableStatement.setString(2,warehouseId);
            callableStatement.setString(3,subCatrgoryId);
            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement.execute();

            ResultSet resultSet = (ResultSet) callableStatement.getObject(1);

            int i = 0;
            while (resultSet.next()) {
                i++;
                stockItemLists.add(new ItemDetaisList(String.valueOf(i),resultSet.getString(8),resultSet.getString(4),resultSet.getString(5),
                        resultSet.getString(1),resultSet.getString(6),resultSet.getString(2),resultSet.getString(7),resultSet.getString(9)));
            }

            callableStatement.close();
            connected = true;


            if (warehouseId == null) {
                warehouseId = "";
            }
            if (subCatrgoryId == null) {
                subCatrgoryId = "";
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
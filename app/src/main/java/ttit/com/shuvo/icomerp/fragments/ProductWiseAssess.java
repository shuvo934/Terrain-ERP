package ttit.com.shuvo.icomerp.fragments;

import android.app.DatePickerDialog;
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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.textfield.TextInputEditText;
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
import ttit.com.shuvo.icomerp.adapters.SalesOrderAdapter;
import ttit.com.shuvo.icomerp.arrayList.ChartValue;
import ttit.com.shuvo.icomerp.arrayList.ReceiveTypeList;
import ttit.com.shuvo.icomerp.arrayList.SalesOrderList;
import ttit.com.shuvo.icomerp.extra.MyMarkerView;

import static ttit.com.shuvo.icomerp.OracleConnection.createConnection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductWiseAssess#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductWiseAssess extends Fragment {

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

    ArrayList<ReceiveTypeList> categoryLists;

    ArrayList<ChartValue> sortedValues;
    ArrayList<ChartValue> categoryValues;

    ArrayList<Entry> dataValue;
    ArrayList<String> monthName;
    LineChart lineChart;

    ArrayList<ChartValue> cateValuesDD;
    ArrayList<ChartValue> sortedCateValuesDD;

    ArrayList<Entry> dataValueDD;
    ArrayList<String> monthNameDD;
    LineChart lineChartDD;

    ArrayList<ChartValue> relationSales;
    ArrayList<ChartValue> relationDelivery;

    ArrayList<Entry> dataValueRS;
    ArrayList<Entry> dataValueRS1;
    ArrayList<String> monthNameRS;
    LineChart lineChartRS;

    ArrayList<ChartValue> sortedRelateSales;
    ArrayList<ChartValue> sortedRelateDelv;

    String firstDate = "";
    String lastDate = "";
    String categoryId = "";
    private int mYear, mMonth, mDay;

    double total_sales_order_amnt = 0.0;
    TextView totalSalesOrderAmnt;

    double total_delivery_order_amnt = 0.0;
    TextView totalDeliveryOrderAmnt;

    Context mContext;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext=context;
    }

    public ProductWiseAssess() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductWiseAssess.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductWiseAssess newInstance(String param1, String param2) {
        ProductWiseAssess fragment = new ProductWiseAssess();
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
        View view = inflater.inflate(R.layout.fragment_product_wise_assess, container, false);

        beginDate = view.findViewById(R.id.begin_date_pwa);
        endDate = view.findViewById(R.id.end_date_pwa);
        daterange = view.findViewById(R.id.date_range_msg_pwa);
        categorySpinner = view.findViewById(R.id.category_name_pwa_spinner);
        lineChart = view.findViewById(R.id.sales_order_category_overview_pwa);
        totalSalesOrderAmnt = view.findViewById(R.id.total_sales_order_amount_pwa);

        lineChartDD = view.findViewById(R.id.deliverry_challan_category_overview_pwa);
        totalDeliveryOrderAmnt = view.findViewById(R.id.total_delivery_challan_amount_pwa);

        lineChartRS = view.findViewById(R.id.deliverry_sales_category_overview_pwa);

        categoryLists = new ArrayList<>();
        categoryValues = new ArrayList<>();
        sortedValues = new ArrayList<>();

        cateValuesDD = new ArrayList<>();
        sortedCateValuesDD = new ArrayList<>();

        relationSales = new ArrayList<>();
        relationDelivery = new ArrayList<>();
        sortedRelateDelv = new ArrayList<>();
        sortedRelateSales = new ArrayList<>();

        LineChartInit();

        MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view);
        mv.setChartView(lineChart);
        lineChart.setMarker(mv);

        MyMarkerView mv1 = new MyMarkerView(getContext(), R.layout.custom_marker_view);
        mv1.setChartView(lineChartDD);
        lineChartDD.setMarker(mv1);

        MyMarkerView mv2 = new MyMarkerView(getContext(), R.layout.custom_marker_view);
        mv2.setChartView(lineChartRS);
        lineChartRS.setMarker(mv2);

        // Selecting Client
        categorySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                System.out.println(position+": "+name);
                for (int i = 0; i < categoryLists.size(); i++) {
                    if (name.equals(categoryLists.get(i).getType())) {
                        categoryId = categoryLists.get(i).getId();
                    }
                }
                System.out.println("CLIENT ID: "+ categoryId);

                SelectingCategory();

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
                                new Check().execute();

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

                                        new Check().execute();
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
                                new Check().execute();

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

                                        new Check().execute();

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

        return view;
    }


    public void SelectingCategory() {


        DecimalFormat formatter = new DecimalFormat("##,##,##,###");

        total_sales_order_amnt = 0.0;
        sortedValues = new ArrayList<>();

        for (int i = 0; i < categoryValues.size(); i++) {
            if (categoryId.isEmpty()) {
                if (categoryValues.get(i).getValue() != null) {
                    sortedValues.add(new ChartValue(categoryValues.get(i).getId(),categoryValues.get(i).getType(),categoryValues.get(i).getValue()));
                    total_sales_order_amnt = total_sales_order_amnt + Double.parseDouble(categoryValues.get(i).getValue());
                }
            } else if (categoryId.equals(categoryValues.get(i).getId())){

                if (categoryValues.get(i).getValue() != null) {
                    sortedValues.add(new ChartValue("0","0","0"));
                    sortedValues.add(new ChartValue(categoryValues.get(i).getId(),categoryValues.get(i).getType(),categoryValues.get(i).getValue()));
                    total_sales_order_amnt = total_sales_order_amnt + Double.parseDouble(categoryValues.get(i).getValue());
                }
            }
        }

        dataValue = new ArrayList<>();


        for (int i = 0; i < sortedValues.size(); i++) {
            dataValue.add(new Entry(i,Float.parseFloat(sortedValues.get(i).getValue()), sortedValues.get(i).getId()));
        }

        monthName = new ArrayList<>();

        for (int i = 0; i < sortedValues.size(); i++) {
            monthName.add(sortedValues.get(i).getType());
        }


        lineChart.animateXY(1000,1000);
        LineDataSet lineDataSet = new LineDataSet(dataValue,"set1");
        lineDataSet.setValueFormatter(new MyValueFormatter());
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData data1 = new LineData(lineDataSet);
        lineDataSet.setCircleColor(Color.parseColor("#b2bec3"));
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setColor(Color.parseColor("#ffc048"));
        lineDataSet.setDrawFilled(true);
        lineDataSet.setValueTextSize(9f);
        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.sales_chart_fill);
        lineDataSet.setFillDrawable(drawable);

        lineDataSet.setDrawCircleHole(false);
        lineChart.getXAxis().setValueFormatter(new MyAxisValueFormatter(monthName));
        lineChart.setData(data1);
        lineChart.invalidate();

        System.out.println(total_sales_order_amnt);

        String formatted = formatter.format(total_sales_order_amnt);
        totalSalesOrderAmnt.setText(formatted + " BDT");



        // Delivery Assess
        total_delivery_order_amnt = 0.0;
        sortedCateValuesDD = new ArrayList<>();

        for (int i = 0; i < cateValuesDD.size(); i++) {
            if (categoryId.isEmpty()) {
                if (cateValuesDD.get(i).getValue() != null) {
                    sortedCateValuesDD.add(new ChartValue(cateValuesDD.get(i).getId(),cateValuesDD.get(i).getType(),cateValuesDD.get(i).getValue()));
                    total_delivery_order_amnt = total_delivery_order_amnt + Double.parseDouble(cateValuesDD.get(i).getValue());
                }
            } else if (categoryId.equals(cateValuesDD.get(i).getId())){

                if (cateValuesDD.get(i).getValue() != null) {
                    sortedCateValuesDD.add(new ChartValue("0","0","0"));
                    sortedCateValuesDD.add(new ChartValue(cateValuesDD.get(i).getId(),cateValuesDD.get(i).getType(),cateValuesDD.get(i).getValue()));
                    total_delivery_order_amnt = total_delivery_order_amnt + Double.parseDouble(cateValuesDD.get(i).getValue());
                }
            }
        }

        dataValueDD = new ArrayList<>();



        for (int i = 0; i < sortedCateValuesDD.size(); i++) {
            dataValueDD.add(new Entry(i,Float.parseFloat(sortedCateValuesDD.get(i).getValue()), sortedCateValuesDD.get(i).getId()));
        }

        monthNameDD = new ArrayList<>();

        for (int i = 0; i < sortedCateValuesDD.size(); i++) {
            monthNameDD.add(sortedCateValuesDD.get(i).getType());
        }


        lineChartDD.animateXY(1000,1000);
        LineDataSet lineDataSetDD = new LineDataSet(dataValueDD,"set1");
        lineDataSetDD.setValueFormatter(new MyValueFormatterDD());
        ArrayList<ILineDataSet> dataSetsDD = new ArrayList<>();
        dataSetsDD.add(lineDataSetDD);

        LineData data1DD = new LineData(lineDataSetDD);
        lineDataSetDD.setCircleColor(Color.parseColor("#b2bec3"));
        lineDataSetDD.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSetDD.setColor(Color.parseColor("#CC66BE"));
        lineDataSetDD.setDrawFilled(true);
        lineDataSetDD.setValueTextSize(9f);
        Drawable drawableDD = ContextCompat.getDrawable(mContext, R.drawable.delivery_chart_fill);
        lineDataSetDD.setFillDrawable(drawableDD);

        lineDataSetDD.setDrawCircleHole(false);
        lineChartDD.getXAxis().setValueFormatter(new MyAxisValueFormatterDD(monthNameDD));
        lineChartDD.setData(data1DD);
        lineChartDD.invalidate();

        System.out.println(total_delivery_order_amnt);

        formatted = formatter.format(total_delivery_order_amnt);
        totalDeliveryOrderAmnt.setText(formatted + " BDT");


        //

        sortedRelateSales = new ArrayList<>();
        sortedRelateDelv = new ArrayList<>();

        for (int i = 0; i < relationSales.size(); i++) {
            if (categoryId.isEmpty()) {
                if (relationSales.get(i).getValue() != null) {
                    sortedRelateSales.add(new ChartValue(relationSales.get(i).getId(),relationSales.get(i).getType(),relationSales.get(i).getValue()));
                    //total_sales_order_amnt = total_sales_order_amnt + Double.parseDouble(relationSales.get(i).getValue());
                }
            } else if (categoryId.equals(relationSales.get(i).getId())){

                if (relationSales.get(i).getValue() != null) {
                    sortedRelateSales.add(new ChartValue("0","0","0"));
                    sortedRelateSales.add(new ChartValue(relationSales.get(i).getId(),relationSales.get(i).getType(),relationSales.get(i).getValue()));
                    //total_sales_order_amnt = total_sales_order_amnt + Double.parseDouble(relationSales.get(i).getValue());
                }
            }
        }

        for (int i = 0; i < relationDelivery.size(); i++) {
            if (categoryId.isEmpty()) {
                if (relationDelivery.get(i).getValue() != null) {
                    sortedRelateDelv.add(new ChartValue(relationDelivery.get(i).getId(),relationDelivery.get(i).getType(),relationDelivery.get(i).getValue()));
                    //total_delivery_order_amnt = total_delivery_order_amnt + Double.parseDouble(relationDelivery.get(i).getValue());
                }
            } else if (categoryId.equals(relationDelivery.get(i).getId())){

                if (relationDelivery.get(i).getValue() != null) {
                    sortedRelateDelv.add(new ChartValue("0","0","0"));
                    sortedRelateDelv.add(new ChartValue(relationDelivery.get(i).getId(),relationDelivery.get(i).getType(),relationDelivery.get(i).getValue()));
                    //total_delivery_order_amnt = total_delivery_order_amnt + Double.parseDouble(relationDelivery.get(i).getValue());
                }
            }
        }

        dataValueRS = new ArrayList<>();
        dataValueRS1 = new ArrayList<>();


        for (int i = 0; i < sortedRelateSales.size(); i++) {
            dataValueRS.add(new Entry(i,Float.parseFloat(sortedRelateSales.get(i).getValue()), sortedRelateSales.get(i).getId()));
        }

        for (int i = 0; i < sortedRelateDelv.size(); i++) {
            dataValueRS1.add(new Entry(i,Float.parseFloat(sortedRelateDelv.get(i).getValue()), sortedRelateDelv.get(i).getId()));
        }

        monthNameRS = new ArrayList<>();

        for (int i = 0; i < sortedRelateSales.size(); i++) {
            monthNameRS.add(sortedRelateSales.get(i).getType());
        }

        lineChartRS.animateXY(1000,1000);
        LineDataSet lineDataSetRS = new LineDataSet(dataValueRS,"SALES");
        LineDataSet lineDataSetRS1 = new LineDataSet(dataValueRS1,"DELIVERY");
        lineDataSetRS.setValueFormatter(new MyValueFormatter());
        lineDataSetRS1.setValueFormatter(new MyValueFormatterDD());
        ArrayList<ILineDataSet> dataSetsRS = new ArrayList<>();
        dataSetsRS.add(lineDataSetRS);
        dataSetsRS.add(lineDataSetRS1);

        LineData data1RS = new LineData(dataSetsRS);
        lineDataSetRS.setCircleColor(Color.parseColor("#b2bec3"));
        lineDataSetRS.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSetRS.setColor(Color.parseColor("#ffc048"));
        lineDataSetRS.setDrawFilled(false);
        lineDataSetRS.setValueTextSize(9f);
        Drawable drawableRS = ContextCompat.getDrawable(mContext, R.drawable.sales_chart_fill);
        lineDataSetRS.setFillDrawable(drawableRS);

        lineDataSetRS.setDrawCircleHole(false);


        lineDataSetRS1.setCircleColor(Color.parseColor("#b2bec3"));
        lineDataSetRS1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSetRS1.setColor(Color.parseColor("#CC66BE"));
        lineDataSetRS1.setDrawFilled(false);
        lineDataSetRS1.setValueTextSize(9f);
        Drawable drawableRS1 = ContextCompat.getDrawable(mContext, R.drawable.delivery_chart_fill);
        lineDataSetRS1.setFillDrawable(drawableRS1);

        lineDataSetRS1.setDrawCircleHole(false);

        lineChartRS.getXAxis().setValueFormatter(new MyAxisValueFormatterDD(monthNameRS));
        lineChartRS.setData(data1RS);
        lineChartRS.invalidate();


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

    public void LineChartInit() {


        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.GRAY);
        lineChart.getDescription().setEnabled(false);
        lineChart.setPinchZoom(false);

        lineChart.setDrawGridBackground(false);
        lineChart.setExtraLeftOffset(30);
        lineChart.setExtraRightOffset(30);
        lineChart.getAxisLeft().setDrawGridLines(true);
        lineChart.getAxisLeft().setEnabled(false);
        lineChart.setScaleEnabled(true);
        lineChart.setTouchEnabled(true);
        lineChart.setDoubleTapToZoomEnabled(false);

        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setAxisMinimum(0);
        lineChart.getLegend().setEnabled(false);
        //lineChart.getAxisLeft().mAxisMinimum = 5000f;
        lineChart.getAxisLeft().setTextColor(Color.GRAY);
        lineChart.getAxisLeft().setXOffset(10f);
        lineChart.getAxisLeft().setTextSize(8);
        lineChart.getAxisLeft().setDrawGridLines(false);


        //
        XAxis xAxiDD = lineChartDD.getXAxis();
        xAxiDD.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxiDD.setGranularity(1);
        xAxiDD.setDrawGridLines(false);
        xAxiDD.setTextColor(Color.GRAY);
        lineChartDD.getDescription().setEnabled(false);
        lineChartDD.setPinchZoom(false);

        lineChartDD.setDrawGridBackground(false);

        lineChartDD.setExtraLeftOffset(30);
        lineChartDD.setExtraRightOffset(30);
        lineChartDD.getAxisLeft().setDrawGridLines(true);
        lineChartDD.getAxisLeft().setEnabled(false);
        lineChartDD.setScaleEnabled(true);
        lineChartDD.setTouchEnabled(true);
        lineChartDD.setDoubleTapToZoomEnabled(false);

        lineChartDD.getAxisRight().setEnabled(false);
        lineChartDD.getAxisLeft().setAxisMinimum(0);
        lineChartDD.getLegend().setEnabled(false);
        //lineChart.getAxisLeft().mAxisMinimum = 5000f;
        lineChartDD.getAxisLeft().setTextColor(Color.GRAY);
        lineChartDD.getAxisLeft().setXOffset(10f);
        lineChartDD.getAxisLeft().setTextSize(8);
        lineChartDD.getAxisLeft().setDrawGridLines(false);


        //
        XAxis xAxiRS = lineChartRS.getXAxis();
        xAxiRS.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxiRS.setGranularity(1);
        xAxiRS.setDrawGridLines(false);
        xAxiRS.setTextColor(Color.GRAY);
        lineChartRS.getDescription().setEnabled(false);
        lineChartRS.setPinchZoom(true);

        lineChartRS.setDrawGridBackground(false);

        lineChartRS.setExtraLeftOffset(10);
        lineChartRS.setExtraRightOffset(30);
        lineChartRS.setExtraBottomOffset(10);
        lineChartRS.getAxisLeft().setDrawGridLines(true);
        lineChartRS.getAxisLeft().setEnabled(true);
        lineChartRS.setScaleEnabled(true);
        lineChartRS.setTouchEnabled(true);
        lineChartRS.setDoubleTapToZoomEnabled(true);

        lineChartRS.getAxisRight().setEnabled(false);
        lineChartRS.getAxisLeft().setAxisMinimum(0);
        lineChartRS.getLegend().setEnabled(true);
        lineChartRS.getLegend().setOrientation(Legend.LegendOrientation.HORIZONTAL);
        lineChartRS.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        lineChartRS.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        //lineChartRS.getLegend().setYOffset(10);

        //lineChart.getAxisLeft().mAxisMinimum = 5000f;
        lineChartRS.getAxisLeft().setTextColor(Color.GRAY);
        lineChartRS.getAxisLeft().setXOffset(10f);
        lineChartRS.getAxisLeft().setTextSize(8);
        lineChartRS.getAxisLeft().setDrawGridLines(false);



    }

    public static class MyAxisValueFormatter extends ValueFormatter {
        private ArrayList<String> mvalues = new ArrayList<>();
        public MyAxisValueFormatter(ArrayList<String> mvalues) {
            this.mvalues = mvalues;
        }

        @Override
        public String getAxisLabel(float value, AxisBase axis) {

            if (mvalues != null) {
                if (value < 0 || value >= mvalues.size()) {
                    return "";
                } else {
//                    System.out.println(value);
//                    System.out.println(axis);
//                    System.out.println(mvalues.get((int) value));
                    return (mvalues.get((int) value));
                }
            } else {
                return "";
            }
//            return (mvalues.get((int) value));
        }
    }

    public static class MyAxisValueFormatterDD extends ValueFormatter {
        private ArrayList<String> mvalues = new ArrayList<>();
        public MyAxisValueFormatterDD(ArrayList<String> mvalues) {
            this.mvalues = mvalues;
        }

        @Override
        public String getAxisLabel(float value, AxisBase axis) {

            if (mvalues != null) {
//                System.out.println(mvalues.size());
//                System.out.println(value);
                if (value < 0 || value >= mvalues.size()) {
                    return "";
                } else {
//                    System.out.println(value);
//                    System.out.println(axis);
//                    System.out.println(mvalues.get((int) value));
                    return (mvalues.get((int) value));
                }
            } else {
                return "";
            }
//            return (mvalues.get((int) value));
        }
    }

    public class MyValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {

            Double value1 = (value/total_sales_order_amnt) * 100;

            DecimalFormat formatter = new DecimalFormat("###.##");

            String formatted = formatter.format(value1);

            formatted = formatted + "%";

            return formatted;
        }
    }

    public class MyValueFormatterDD extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {

            Double value1 = (value/total_delivery_order_amnt) * 100;

            DecimalFormat formatter = new DecimalFormat("###.##");

            String formatted = formatter.format(value1);

            formatted = formatted + "%";

            return formatted;
        }
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

                ClientData();
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

                categorySpinner.setText("");

                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < categoryLists.size(); i++) {
                    type.add(categoryLists.get(i).getType());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                categorySpinner.setAdapter(arrayAdapter);


                DecimalFormat formatter = new DecimalFormat("##,##,##,###");

                dataValue = new ArrayList<>();
                monthName = new ArrayList<>();

                for (int i = 0; i < categoryValues.size(); i++) {
                    if (categoryValues.get(i).getValue() != null) {
                        total_sales_order_amnt = total_sales_order_amnt + Double.parseDouble(categoryValues.get(i).getValue());
                    }
                }

                if (categoryValues.size() == 1) {
                    dataValue.add(new Entry(0,0,"0"));
                    dataValue.add(new Entry(1,Float.parseFloat(categoryValues.get(0).getValue()),categoryValues.get(0).getId()));
                    monthName.add("0");
                    monthName.add(categoryValues.get(0).getType());
                } else {
                    for (int i = 0; i < categoryValues.size(); i++) {
                        dataValue.add(new Entry(i,Float.parseFloat(categoryValues.get(i).getValue()), categoryValues.get(i).getId()));
                    }

                    for (int i = 0; i < categoryValues.size(); i++) {
                        monthName.add(categoryValues.get(i).getType());
                    }
                }



                lineChart.animateXY(1000,1000);
                LineDataSet lineDataSet = new LineDataSet(dataValue,"set1");
                lineDataSet.setValueFormatter(new MyValueFormatter());
                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(lineDataSet);

                LineData data1 = new LineData(lineDataSet);
                lineDataSet.setCircleColor(Color.parseColor("#b2bec3"));
                lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
                lineDataSet.setColor(Color.parseColor("#ffc048"));
                lineDataSet.setDrawFilled(true);
                lineDataSet.setValueTextSize(9f);
                Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.sales_chart_fill);
                lineDataSet.setFillDrawable(drawable);

                lineDataSet.setDrawCircleHole(false);
                lineChart.getXAxis().setValueFormatter(new MyAxisValueFormatter(monthName));
                lineChart.setData(data1);
                lineChart.invalidate();

                System.out.println(total_sales_order_amnt);

                String formatted = formatter.format(total_sales_order_amnt);
                totalSalesOrderAmnt.setText(formatted + " BDT");


                dataValueDD = new ArrayList<>();
                monthNameDD = new ArrayList<>();

                for (int i = 0; i < cateValuesDD.size(); i++) {
                    if (cateValuesDD.get(i).getValue() != null) {
                        total_delivery_order_amnt = total_delivery_order_amnt + Double.parseDouble(cateValuesDD.get(i).getValue());
                    }
                }

                if (cateValuesDD.size() == 1) {
                    dataValueDD.add(new Entry(0,0,"0"));
                    dataValueDD.add(new Entry(1,Float.parseFloat(cateValuesDD.get(0).getValue()),cateValuesDD.get(0).getId()));
                    monthNameDD.add("0");
                    monthNameDD.add(cateValuesDD.get(0).getType());
                } else {
                    for (int i = 0; i < cateValuesDD.size(); i++) {
                        dataValueDD.add(new Entry(i,Float.parseFloat(cateValuesDD.get(i).getValue()), cateValuesDD.get(i).getId()));
                    }

                    for (int i = 0; i < cateValuesDD.size(); i++) {
                        monthNameDD.add(cateValuesDD.get(i).getType());
                    }
                }

                for (int i = 0; i <monthNameDD.size(); i++) {
                    System.out.println("CAT Name: "+ monthNameDD.get(i));
                }



                lineChartDD.animateXY(1000,1000);
                LineDataSet lineDataSetDD = new LineDataSet(dataValueDD,"set1");
                lineDataSetDD.setValueFormatter(new MyValueFormatterDD());
                ArrayList<ILineDataSet> dataSetsDD = new ArrayList<>();
                dataSetsDD.add(lineDataSetDD);

                LineData data1DD = new LineData(lineDataSetDD);
                lineDataSetDD.setCircleColor(Color.parseColor("#b2bec3"));
                lineDataSetDD.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
                lineDataSetDD.setColor(Color.parseColor("#CC66BE"));
                lineDataSetDD.setDrawFilled(true);
                lineDataSetDD.setValueTextSize(9f);
                Drawable drawableDD = ContextCompat.getDrawable(mContext, R.drawable.delivery_chart_fill);
                lineDataSetDD.setFillDrawable(drawableDD);

                lineDataSetDD.setDrawCircleHole(false);
                lineChartDD.getXAxis().setValueFormatter(new MyAxisValueFormatterDD(monthNameDD));
                lineChartDD.setData(data1DD);
                lineChartDD.invalidate();

                System.out.println(total_delivery_order_amnt);

                formatted = formatter.format(total_delivery_order_amnt);
                totalDeliveryOrderAmnt.setText(formatted + " BDT");


                //
                relationSales = new ArrayList<>();
                relationDelivery = new ArrayList<>();

                for (int i = 0; i < categoryValues.size(); i++) {
                    relationSales.add(new ChartValue(categoryValues.get(i).getId(),categoryValues.get(i).getType(),categoryValues.get(i).getValue()));
                    boolean found = false;
                    for (int j = 0; j < cateValuesDD.size(); j++) {
                        if (categoryValues.get(i).getId().equals(cateValuesDD.get(j).getId())) {
                            found = true;
                            relationDelivery.add(new ChartValue(cateValuesDD.get(j).getId(),cateValuesDD.get(j).getType(),cateValuesDD.get(j).getValue()));
                        }
                    }
                    if (!found) {
                        relationDelivery.add(new ChartValue(categoryValues.get(i).getId(),categoryValues.get(i).getType(),"0"));
                    }
                }

                for (int i = 0; i < cateValuesDD.size(); i++) {
                    boolean found = false;
                    for (int j = 0; j < relationDelivery.size(); j++) {
                        if (cateValuesDD.get(i).getId().equals(relationDelivery.get(j).getId())) {
                            found = true;
                        }
                    }
                    if (!found) {
                        relationDelivery.add(new ChartValue(cateValuesDD.get(i).getId(),cateValuesDD.get(i).getType(),cateValuesDD.get(i).getValue()));
                        relationSales.add(new ChartValue(cateValuesDD.get(i).getId(),cateValuesDD.get(i).getType(),"0"));
                    }
                }

                for (int i = 0; i < relationSales.size(); i++) {
                    System.out.println("SALES: "+relationSales.get(i).getType()+" : "+ relationSales.get(i).getValue());
                }

                for (int i = 0; i < relationDelivery.size(); i++) {
                    System.out.println("DELIVERY: "+relationDelivery.get(i).getType()+" : "+ relationDelivery.get(i).getValue());
                }


                dataValueRS = new ArrayList<>();
                dataValueRS1 = new ArrayList<>();
                monthNameRS = new ArrayList<>();

                if (relationSales.size() == 1) {
                    dataValueRS.add(new Entry(0,0,"0"));
                    dataValueRS.add(new Entry(1,Float.parseFloat(relationSales.get(0).getValue()),relationSales.get(0).getId()));
                    monthNameRS.add("0");
                    monthNameRS.add(relationSales.get(0).getType());
                } else {
                    for (int i = 0; i < relationSales.size(); i++) {
                        dataValueRS.add(new Entry(i,Float.parseFloat(relationSales.get(i).getValue()), relationSales.get(i).getId()));
                    }
                    for (int i = 0; i < relationSales.size(); i++) {
                        monthNameRS.add(relationSales.get(i).getType());
                    }
                }

                if (relationDelivery.size() == 1) {
                    dataValueRS1.add(new Entry(0,0,"0"));
                    dataValueRS1.add(new Entry(1,Float.parseFloat(relationDelivery.get(0).getValue()),relationDelivery.get(0).getId()));
                } else {
                    for (int i = 0; i < relationDelivery.size(); i++) {
                        dataValueRS1.add(new Entry(i,Float.parseFloat(relationDelivery.get(i).getValue()), relationDelivery.get(i).getId()));
                    }
                }


                lineChartRS.animateXY(1000,1000);
                LineDataSet lineDataSetRS = new LineDataSet(dataValueRS,"SALES ORDER");
                LineDataSet lineDataSetRS1 = new LineDataSet(dataValueRS1,"DELIVERY CHALLAN");
                lineDataSetRS.setValueFormatter(new MyValueFormatter());
                lineDataSetRS1.setValueFormatter(new MyValueFormatterDD());
                ArrayList<ILineDataSet> dataSetsRS = new ArrayList<>();
                dataSetsRS.add(lineDataSetRS);
                dataSetsRS.add(lineDataSetRS1);

                LineData data1RS = new LineData(dataSetsRS);
                lineDataSetRS.setCircleColor(Color.parseColor("#b2bec3"));
                lineDataSetRS.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                lineDataSetRS.setColor(Color.parseColor("#ffc048"));
                lineDataSetRS.setDrawFilled(false);
                lineDataSetRS.setValueTextSize(9f);
                lineDataSetRS.setLineWidth(2f);
                Drawable drawableRS = ContextCompat.getDrawable(mContext, R.drawable.sales_chart_fill);
                lineDataSetRS.setFillDrawable(drawableRS);

                lineDataSetRS.setDrawCircleHole(false);


                lineDataSetRS1.setCircleColor(Color.parseColor("#b2bec3"));
                lineDataSetRS1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                lineDataSetRS1.setColor(Color.parseColor("#CC66BE"));
                lineDataSetRS1.setDrawFilled(false);
                lineDataSetRS1.setValueTextSize(9f);
                lineDataSetRS1.setLineWidth(2f);
                Drawable drawableRS1 = ContextCompat.getDrawable(mContext, R.drawable.delivery_chart_fill);
                lineDataSetRS1.setFillDrawable(drawableRS1);

                lineDataSetRS1.setDrawCircleHole(false);

                lineChartRS.getXAxis().setValueFormatter(new MyAxisValueFormatterDD(monthNameRS));
                lineChartRS.setData(data1RS);
                lineChartRS.invalidate();

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


    public void ClientData() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            categoryLists = new ArrayList<>();
            categoryId = "";
            total_sales_order_amnt = 0.0;
            total_delivery_order_amnt = 0.0;

            categoryValues = new ArrayList<>();
            cateValuesDD = new ArrayList<>();

            Statement stmt = connection.createStatement();
            if (firstDate.isEmpty()) {
                firstDate = null;
            }
            if (lastDate.isEmpty()) {
                lastDate = null;
            }


            ResultSet resultSet = stmt.executeQuery("SELECT DISTINCT V.IM_ID, V.IM_NAME\n" +
                    "  FROM ITEM_DETAILS_V V");

            while (resultSet.next()) {
                categoryLists.add(new ReceiveTypeList(resultSet.getString(1),resultSet.getString(2)));
            }
            categoryLists.add(new ReceiveTypeList("","All Categories"));


            CallableStatement callableStatement1 = connection.prepareCall("begin ? := GET_CATEGORY_WISE_ORDER(?,?,?); end;");
            callableStatement1.setString(2, null);
            callableStatement1.setString(3,firstDate);
            callableStatement1.setString(4,lastDate);
            callableStatement1.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement1.execute();

            ResultSet resultSet2 = (ResultSet) callableStatement1.getObject(1);

            while (resultSet2.next()) {
                categoryValues.add(new ChartValue(resultSet2.getString(1),resultSet2.getString(2),resultSet2.getString(3)));
            }


            callableStatement1.close();


            CallableStatement callableStatement12 = connection.prepareCall("begin ? := GET_CAT_WISE_SALES_RET_INV(?,?,?); end;");
            callableStatement12.setString(4,null);
            callableStatement12.setString(2,firstDate);
            callableStatement12.setString(3,lastDate);
            callableStatement12.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement12.execute();

            ResultSet resultSet21 = (ResultSet) callableStatement12.getObject(1);

            while (resultSet21.next()) {
                cateValuesDD.add(new ChartValue(resultSet21.getString(1),resultSet21.getString(2),resultSet21.getString(3)));
            }


            callableStatement12.close();

            if (firstDate == null) {
                firstDate = "";
            }
            if (lastDate == null) {
                lastDate = "";
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
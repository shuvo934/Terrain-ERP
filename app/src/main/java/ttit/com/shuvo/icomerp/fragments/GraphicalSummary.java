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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dewinjm.monthyearpicker.MonthFormat;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
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
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import oracle.jdbc.driver.OracleTypes;
import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.WaitProgress;
import ttit.com.shuvo.icomerp.adapters.TopNItemAdapter;
import ttit.com.shuvo.icomerp.arrayList.ChartValue;
import ttit.com.shuvo.icomerp.arrayList.ReceiveTypeList;
import ttit.com.shuvo.icomerp.arrayList.TopNItemLists;
import ttit.com.shuvo.icomerp.extra.MyMarkerView;

import static ttit.com.shuvo.icomerp.OracleConnection.createConnection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GraphicalSummary#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GraphicalSummary extends Fragment {

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

    ArrayList<ReceiveTypeList> categoryLists;
    ArrayList<ReceiveTypeList> subCategoryLists;

    ArrayList<ChartValue> categoryValues;

    ArrayList<Entry> dataValue;
    ArrayList<String> monthName;
    LineChart lineChart;

    ArrayList<ChartValue> cateValuesDD;

    ArrayList<Entry> dataValueDD;
    ArrayList<String> monthNameDD;
    LineChart lineChartDD;

    double total_delivery_order_amnt = 0.0;
    TextView totalDeliveryOrderAmnt;

    private int mYear, mMonth, mDay;

    String firstDate = "";
    String lastDate = "";
    String categoryId = "";
    String subCategoryId = "";

    double total_sales_order_amnt = 0.0;
    TextView totalSalesOrderAmnt;

    public GraphicalSummary() {
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
     * @return A new instance of fragment GraphicalSummary.
     */
    // TODO: Rename and change types and number of parameters
    public static GraphicalSummary newInstance(String param1, String param2) {
        GraphicalSummary fragment = new GraphicalSummary();
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
        View view = inflater.inflate(R.layout.fragment_graphical_summary, container, false);

        beginDate = view.findViewById(R.id.begin_date_graph_summary);
        endDate = view.findViewById(R.id.end_date_graph_summary);
        daterange = view.findViewById(R.id.date_range_msg_graph_summary);
        categorySpinner = view.findViewById(R.id.cat_type_spinner_item_gs);
        subCatSpinner = view.findViewById(R.id.sub_cat_type_spinner_item_gs);
        lineChart = view.findViewById(R.id.sales_order_category_overview_gs);
        totalSalesOrderAmnt = view.findViewById(R.id.total_sales_order_amount_gs);
        lineChartDD = view.findViewById(R.id.deliverry_challan_category_overview_gs);
        totalDeliveryOrderAmnt = view.findViewById(R.id.total_delivery_challan_amount_gs);



        categoryLists = new ArrayList<>();
        subCategoryLists = new ArrayList<>();
        categoryValues = new ArrayList<>();
        cateValuesDD = new ArrayList<>();

        LineChartInit();

        MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view);
        mv.setChartView(lineChart);
        lineChart.setMarker(mv);

        MyMarkerView mv1 = new MyMarkerView(getContext(), R.layout.custom_marker_view);
        mv1.setChartView(lineChartDD);
        lineChartDD.setMarker(mv1);

        // Selecting Category
        categorySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                System.out.println(position+": "+name);
                subCatSpinner.setText("");
                //searchingSubCate = "";
                for (int i = 0; i <categoryLists.size(); i++) {
                    if (name.equals(categoryLists.get(i).getType())) {
                        categoryId = categoryLists.get(i).getId();
//                        if (categoryId.isEmpty()) {
//                            searchingCate = "";
//                        } else {
//                            searchingCate = categoryLists.get(i).getType();
//                        }

                    }
                }
                System.out.println(categoryId);
                subCategoryId = "";
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
                for (int i = 0; i <subCategoryLists.size(); i++) {
                    if (name.equals(subCategoryLists.get(i).getType())) {
                        subCategoryId = subCategoryLists.get(i).getId();
//                        if (subCategoryId.isEmpty()) {
//                            searchingSubCate = "";
//                        } else {
//                            searchingSubCate = subCategoryLists.get(i).getType();
//                        }
                    }
                }
//                filterSubCate(searchingSubCate);
                new ItemCheck().execute();
                System.out.println(subCategoryId);
            }
        });

        // Getting Date
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM-yy",Locale.getDefault());
        SimpleDateFormat datetoShow = new SimpleDateFormat("MMM-yyyy", Locale.getDefault());

        String dasda = datetoShow.format(c);
        dasda = dasda.toUpperCase();
        System.out.println(dasda);

        String normalDate = simpleDateFormat.format(c);
        firstDate = "01-"+normalDate;
        System.out.println(firstDate);

        Date today = null;
        try {
            today = df.parse(firstDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar1 = Calendar.getInstance();
        if (today != null) {
            calendar1.setTime(today);
            calendar1.add(Calendar.MONTH, 1);
            calendar1.set(Calendar.DAY_OF_MONTH, 1);
            calendar1.add(Calendar.DATE, -1);

            Date lastDayOfMonth = calendar1.getTime();

            SimpleDateFormat sdff = new SimpleDateFormat("dd",Locale.getDefault());
            String llll = sdff.format(lastDayOfMonth);
            lastDate =  llll+ "-" + normalDate;
            System.out.println(lastDate);
        }

        beginDate.setText(dasda);
        endDate.setText(dasda);

        beginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date c = Calendar.getInstance().getTime();

                String formattedYear = "";
                String monthValue = "";
                String lastformattedYear = "";
                String lastdateView = "";

                SimpleDateFormat df = new SimpleDateFormat("yyyy", Locale.getDefault());
                SimpleDateFormat sdf = new SimpleDateFormat("MM",Locale.getDefault());

                formattedYear = df.format(c);
                monthValue = sdf.format(c);
                int nowMonNumb = Integer.parseInt(monthValue);
                int nowYearNumb = Integer.parseInt(formattedYear);
                nowMonNumb = nowMonNumb - 1;
                int lastYearNumb = nowYearNumb - 10;

                lastformattedYear = String.valueOf(lastYearNumb);

                Date today = new Date();

                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(today);

                calendar1.add(Calendar.MONTH, 1);
                calendar1.set(Calendar.DAY_OF_MONTH, 1);
                calendar1.add(Calendar.DATE, -1);

                Date lastDayOfMonth = calendar1.getTime();

                SimpleDateFormat sdff = new SimpleDateFormat("dd",Locale.getDefault());
                lastdateView = sdff.format(lastDayOfMonth);

                int yearSelected;
                int monthSelected;
                MonthFormat monthFormat = MonthFormat.LONG;
                String customTitle = "Select Month";
// Use the calendar for create ranges
                Calendar calendar = Calendar.getInstance();
                yearSelected = calendar.get(Calendar.YEAR);
                monthSelected = calendar.get(Calendar.MONTH);
                calendar.clear();
                calendar.set(Integer.parseInt(lastformattedYear), 0, 1); // Set minimum date to show in dialog
                long minDate = calendar.getTimeInMillis(); // Get milliseconds of the modified date

                calendar.clear();
                calendar.set(Integer.parseInt(formattedYear), nowMonNumb, Integer.parseInt(lastdateView)); // Set maximum date to show in dialog
                long maxDate = calendar.getTimeInMillis(); // Get milliseconds of the modified date

// Create instance with date ranges values
                MonthYearPickerDialogFragment dialogFragment =  MonthYearPickerDialogFragment
                        .getInstance(monthSelected, yearSelected, minDate, maxDate, customTitle, monthFormat);



                dialogFragment.show(getActivity().getSupportFragmentManager(), null);

                dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int monthOfYear) {
                        System.out.println(year);
                        System.out.println(monthOfYear);

                        int month = monthOfYear + 1;
                        String monthName = "";
                        String mon = "";
                        String yearName = "";

                        if (month == 1) {
                            monthName = "JANUARY";
                            mon = "JAN";
                        } else if (month == 2) {
                            monthName = "FEBRUARY";
                            mon = "FEB";
                        } else if (month == 3) {
                            monthName = "MARCH";
                            mon = "MAR";
                        } else if (month == 4) {
                            monthName = "APRIL";
                            mon = "APR";
                        } else if (month == 5) {
                            monthName = "MAY";
                            mon = "MAY";
                        } else if (month == 6) {
                            monthName = "JUNE";
                            mon = "JUN";
                        } else if (month == 7) {
                            monthName = "JULY";
                            mon = "JUL";
                        } else if (month == 8) {
                            monthName = "AUGUST";
                            mon = "AUG";
                        } else if (month == 9) {
                            monthName = "SEPTEMBER";
                            mon = "SEP";
                        } else if (month == 10) {
                            monthName = "OCTOBER";
                            mon = "OCT";
                        } else if (month == 11) {
                            monthName = "NOVEMBER";
                            mon = "NOV";
                        } else if (month == 12) {
                            monthName = "DECEMBER";
                            mon = "DEC";
                        }

                        yearName  = String.valueOf(year);
                        yearName = yearName.substring(yearName.length()-2);

                        firstDate = "01-"+mon+"-"+yearName;
                        System.out.println(firstDate);
                        beginDate.setText(mon + "-" + year);

                        if (lastDate.isEmpty()) {
                            daterange.setVisibility(View.GONE);
                            new ItemCheck().execute();

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

                                }
                                else {
                                    daterange.setVisibility(View.VISIBLE);
                                    beginDate.setText("");
                                }
                            }
                        }

                    }
                });

            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date c = Calendar.getInstance().getTime();

                String formattedYear = "";
                String monthValue = "";
                String lastformattedYear = "";
                String lastdateView = "";

                SimpleDateFormat df = new SimpleDateFormat("yyyy", Locale.getDefault());
                SimpleDateFormat sdf = new SimpleDateFormat("MM",Locale.getDefault());

                formattedYear = df.format(c);
                monthValue = sdf.format(c);
                int nowMonNumb = Integer.parseInt(monthValue);
                int nowYearNumb = Integer.parseInt(formattedYear);
                nowMonNumb = nowMonNumb - 1;
                int lastYearNumb = nowYearNumb - 10;

                lastformattedYear = String.valueOf(lastYearNumb);

                Date today = new Date();

                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(today);

                calendar1.add(Calendar.MONTH, 1);
                calendar1.set(Calendar.DAY_OF_MONTH, 1);
                calendar1.add(Calendar.DATE, -1);

                Date lastDayOfMonth = calendar1.getTime();

                SimpleDateFormat sdff = new SimpleDateFormat("dd",Locale.getDefault());
                lastdateView = sdff.format(lastDayOfMonth);

                int yearSelected;
                int monthSelected;
                MonthFormat monthFormat = MonthFormat.LONG;
                String customTitle = "Select Month";
// Use the calendar for create ranges
                Calendar calendar = Calendar.getInstance();
                yearSelected = calendar.get(Calendar.YEAR);
                monthSelected = calendar.get(Calendar.MONTH);
                calendar.clear();
                calendar.set(Integer.parseInt(lastformattedYear), 0, 1); // Set minimum date to show in dialog
                long minDate = calendar.getTimeInMillis(); // Get milliseconds of the modified date

                calendar.clear();
                calendar.set(Integer.parseInt(formattedYear), nowMonNumb, Integer.parseInt(lastdateView)); // Set maximum date to show in dialog
                long maxDate = calendar.getTimeInMillis(); // Get milliseconds of the modified date

// Create instance with date ranges values
                MonthYearPickerDialogFragment dialogFragment =  MonthYearPickerDialogFragment
                        .getInstance(monthSelected, yearSelected, minDate, maxDate, customTitle, monthFormat);



                dialogFragment.show(getActivity().getSupportFragmentManager(), null);

                dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int monthOfYear) {
                        System.out.println(year);
                        System.out.println(monthOfYear);

                        int month = monthOfYear + 1;
                        String monthName = "";
                        String mon = "";
                        String yearName = "";

                        if (month == 1) {
                            monthName = "JANUARY";
                            mon = "JAN";
                        } else if (month == 2) {
                            monthName = "FEBRUARY";
                            mon = "FEB";
                        } else if (month == 3) {
                            monthName = "MARCH";
                            mon = "MAR";
                        } else if (month == 4) {
                            monthName = "APRIL";
                            mon = "APR";
                        } else if (month == 5) {
                            monthName = "MAY";
                            mon = "MAY";
                        } else if (month == 6) {
                            monthName = "JUNE";
                            mon = "JUN";
                        } else if (month == 7) {
                            monthName = "JULY";
                            mon = "JUL";
                        } else if (month == 8) {
                            monthName = "AUGUST";
                            mon = "AUG";
                        } else if (month == 9) {
                            monthName = "SEPTEMBER";
                            mon = "SEP";
                        } else if (month == 10) {
                            monthName = "OCTOBER";
                            mon = "OCT";
                        } else if (month == 11) {
                            monthName = "NOVEMBER";
                            mon = "NOV";
                        } else if (month == 12) {
                            monthName = "DECEMBER";
                            mon = "DEC";
                        }

                        yearName  = String.valueOf(year);
                        yearName = yearName.substring(yearName.length()-2);

                        SimpleDateFormat sss = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                        String first = "01-"+mon+"-"+yearName;

                        Date today = null;
                        try {
                            today = sss.parse(first);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Calendar calendar1 = Calendar.getInstance();
                        if (today != null) {
                            calendar1.setTime(today);
                            calendar1.add(Calendar.MONTH, 1);
                            calendar1.set(Calendar.DAY_OF_MONTH, 1);
                            calendar1.add(Calendar.DATE, -1);

                            Date lastDayOfMonth = calendar1.getTime();

                            SimpleDateFormat sdff = new SimpleDateFormat("dd",Locale.getDefault());
                            String llll = sdff.format(lastDayOfMonth);
                            lastDate =  llll+ "-" + mon +"-"+ yearName;
                            System.out.println(lastDate);
                        }
                        endDate.setText(mon + "-" + year);

                        if (firstDate.isEmpty()) {
                            daterange.setVisibility(View.GONE);
                            new ItemCheck().execute();
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

                                }
                                else {
                                    daterange.setVisibility(View.VISIBLE);
                                    endDate.setText("");

                                }
                            }
                        }

                    }
                });
            }
        });

        new Check().execute();

        return view;
    }

    public void LineChartInit() {

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.GRAY);
        //xAxis.setAxisMaximum(10f);
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

        // Delivery Challan
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

    public class MyValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {

            Double value1 = (value/total_sales_order_amnt) * 100;

            DecimalFormat formatter = new DecimalFormat("###.##");

            String formatted = formatter.format(value1);
            //System.out.println(formatted);

            if (formatted.equals("0") || formatted.equals("NaN")) {
                formatted = "";
            } else {
                formatted = formatted + "%";
            }

            return formatted;
        }
    }

    public class MyValueFormatterDD extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {

            Double value1 = (value/total_delivery_order_amnt) * 100;

            DecimalFormat formatter = new DecimalFormat("###.##");

            String formatted = formatter.format(value1);

            if (formatted.equals("0") || formatted.equals("NaN")) {
                formatted = "";
            } else {
                formatted = formatted + "%";
            }

            return formatted;
        }
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

                new ItemCheck().execute();


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

                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < subCategoryLists.size(); i++) {
                    type.add(subCategoryLists.get(i).getType());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                subCatSpinner.setAdapter(arrayAdapter);
                new ItemCheck().execute();



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

                DecimalFormat formatter = new DecimalFormat("##,##,##,###");

                for (int i = 0; i < categoryValues.size(); i++) {
                    if (categoryValues.get(i).getValue() != null) {
                        total_sales_order_amnt = total_sales_order_amnt + Double.parseDouble(categoryValues.get(i).getValue());
                    }
                }

                dataValue = new ArrayList<>();

                if (categoryValues.size() == 1) {
                    dataValue.add(new Entry(0,0,0));
                    for (int i = 0; i < categoryValues.size(); i++) {
                        dataValue.add(new Entry(i+1,Float.parseFloat(categoryValues.get(i).getValue()), categoryValues.get(i).getId()));
                    }
                } else {
                    for (int i = 0; i < categoryValues.size(); i++) {
                        dataValue.add(new Entry(i,Float.parseFloat(categoryValues.get(i).getValue()), categoryValues.get(i).getId()));
                    }
                }


                monthName = new ArrayList<>();

                if (categoryValues.size() == 1) {
                    monthName.add(categoryValues.get(0).getType());
                    for (int i = 0; i < categoryValues.size(); i++) {
                        monthName.add(categoryValues.get(i).getType());
                    }
                } else {
                    for (int i = 0; i < categoryValues.size(); i++) {
                        monthName.add(categoryValues.get(i).getType());
                    }
                }
                lineChart.animateXY(1000,1000);
                LineDataSet lineDataSet = new LineDataSet(dataValue,"set1");
                lineDataSet.setValueFormatter(new MyValueFormatter());
//                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//                dataSets.add(lineDataSet);

                LineData data1 = new LineData(lineDataSet);
                lineDataSet.setCircleColor(Color.parseColor("#b2bec3"));
                lineDataSet.setCircleRadius(2f);
                lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
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

                //

                for (int i = 0; i < cateValuesDD.size(); i++) {
                    if (cateValuesDD.get(i).getValue() != null) {
                        total_delivery_order_amnt = total_delivery_order_amnt + Double.parseDouble(cateValuesDD.get(i).getValue());
                    }
                }

                dataValueDD = new ArrayList<>();

                if (cateValuesDD.size() == 1) {
                    dataValueDD.add(new Entry(0,0,0));
                    for (int i = 0; i < cateValuesDD.size(); i++) {
                        dataValueDD.add(new Entry(i+1,Float.parseFloat(cateValuesDD.get(i).getValue()), cateValuesDD.get(i).getId()));
                    }
                } else {
                    for (int i = 0; i < cateValuesDD.size(); i++) {
                        dataValueDD.add(new Entry(i,Float.parseFloat(cateValuesDD.get(i).getValue()), cateValuesDD.get(i).getId()));
                    }
                }


                monthNameDD = new ArrayList<>();

                if (cateValuesDD.size() == 1) {
                    monthNameDD.add(cateValuesDD.get(0).getType());
                    for (int i = 0; i < cateValuesDD.size(); i++) {
                        monthNameDD.add(cateValuesDD.get(i).getType());
                    }
                } else {
                    for (int i = 0; i < cateValuesDD.size(); i++) {
                        monthNameDD.add(cateValuesDD.get(i).getType());
                    }
                }

                lineChartDD.animateXY(1000,1000);
                LineDataSet lineDataSetDD = new LineDataSet(dataValueDD,"set1");
                lineDataSetDD.setValueFormatter(new MyValueFormatterDD());
//                ArrayList<ILineDataSet> dataSetsDD = new ArrayList<>();
//                dataSetsDD.add(lineDataSetDD);

                LineData data1DD = new LineData(lineDataSetDD);
                lineDataSetDD.setCircleColor(Color.parseColor("#b2bec3"));
                lineDataSetDD.setCircleRadius(2f);
                lineDataSetDD.setMode(LineDataSet.Mode.CUBIC_BEZIER);
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



            categoryLists.add(new ReceiveTypeList("","ALL"));
            ResultSet resultSet1 = stmt.executeQuery("SELECT DISTINCT V.IM_ID, V.IM_NAME\n" +
                    "  FROM ITEM_DETAILS_V V");

            while (resultSet1.next()) {
                categoryLists.add(new ReceiveTypeList(resultSet1.getString(1),resultSet1.getString(2)));
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

    public void SubCategoryData() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            Statement stmt = connection.createStatement();

            subCategoryLists = new ArrayList<>();

            if (categoryId != null) {
                if (categoryId.isEmpty()) {
                    categoryId = null;
                }
            }


            subCategoryLists.add(new ReceiveTypeList("","ALL"));
            ResultSet resultSet1 = stmt.executeQuery("SELECT DISTINCT V.SUBCATM_ID, V.SUBCATM_NAME\n" +
                    "  FROM ITEM_DETAILS_V V where V.IM_ID = "+categoryId+"");

            while (resultSet1.next()) {
                subCategoryLists.add(new ReceiveTypeList(resultSet1.getString(1),resultSet1.getString(2)));
                System.out.println(resultSet1.getString(1));
            }


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

            categoryValues = new ArrayList<>();
            total_sales_order_amnt = 0.0;
            cateValuesDD = new ArrayList<>();
            total_delivery_order_amnt = 0.0;

            //Statement stmt = connection.createStatement();

            if (firstDate != null) {
                if (firstDate.isEmpty()) {
                    firstDate = null;
                }
            }
            if (lastDate != null) {
                if (lastDate.isEmpty()) {
                    lastDate = null;
                }
            }
            if (categoryId != null) {
                if (categoryId.isEmpty()) {
                    categoryId = null;
                }
            }
            if (subCategoryId != null) {
                if (subCategoryId.isEmpty()) {
                    subCategoryId = null;
                }
            }

            CallableStatement callableStatement = connection.prepareCall("begin ? := GET_MONTH_WISE_SALES(?,?,?,?,?); end;");
            callableStatement.setString(2,firstDate);
            callableStatement.setString(3,lastDate);
            callableStatement.setString(4,categoryId);
            callableStatement.setString(5,subCategoryId);
            callableStatement.setInt(6,2);

            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement.execute();

            ResultSet resultSet = (ResultSet) callableStatement.getObject(1);

            int i = 0;
            while (resultSet.next()) {
                categoryValues.add(new ChartValue(resultSet.getString(2),resultSet.getString(1),resultSet.getString(3)));

            }

            callableStatement.close();


            CallableStatement callableStatement1 = connection.prepareCall("begin ? := GET_MONTH_WISE_SALES(?,?,?,?,?); end;");
            callableStatement1.setString(2,firstDate);
            callableStatement1.setString(3,lastDate);
            callableStatement1.setString(4,categoryId);
            callableStatement1.setString(5,subCategoryId);
            callableStatement1.setInt(6,1);

            callableStatement1.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement1.execute();

            ResultSet resultSet1 = (ResultSet) callableStatement1.getObject(1);


            while (resultSet1.next()) {
                cateValuesDD.add(new ChartValue(resultSet1.getString(2),resultSet1.getString(1),resultSet1.getString(3)));

            }

            callableStatement1.close();


            connected = true;

            if (firstDate == null) {
                firstDate = "";
            }
            if (lastDate == null) {
                lastDate = "";
            }
            if (categoryId == null) {
                categoryId = "";
            }
            if (subCategoryId == null) {
                subCategoryId = "";
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
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import oracle.jdbc.driver.OracleTypes;
import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.WaitProgress;
import ttit.com.shuvo.icomerp.arrayList.ChartValue;
import ttit.com.shuvo.icomerp.extra.MyMarkerView;
import ttit.com.shuvo.icomerp.mainBoard.MainMenu;

import static ttit.com.shuvo.icomerp.OracleConnection.createConnection;
import static ttit.com.shuvo.icomerp.login.Login.userInfoLists;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link DashboardFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class DashboardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Context mContext;
    PieChart pieChart;
    ArrayList<PieEntry> NoOfEmp;
    ArrayList<Entry> dataValue;
    ArrayList<String> monthName;
    LineChart lineChart;
    ArrayList<ChartValue> cateValuesDD;

    TextView userName;

    String firstDate = "";
    String lastDate = "";

    TextView selectYear;

    String workOrderTotal = "";
    int work_order_total = 0;

    String receiveTotal = "";
    int receive_total = 0;

    String salesOrderTotal = "";
    int sales_order_total = 0;

    String deliveryTotal = "";
    int delivery_total = 0;

    String supplierTotal = "";
    int supplier_total = 0;

    String customerTotal = "";
    int customer_total = 0;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
    private Connection connection;

    TextView wot;
    TextView rt;
    TextView sot;
    TextView dt;
    TextView ct;
    TextView st;

    public DashboardFragment() {
        // Required empty public constructor

    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment DashboardFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static DashboardFragment newInstance(String param1, String param2) {
//        DashboardFragment fragment = new DashboardFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

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
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        pieChart = view.findViewById(R.id.company_overview);
        lineChart = view.findViewById(R.id.salary_growth);
        userName = view.findViewById(R.id.user_full_name);
        selectYear = view.findViewById(R.id.select_year_for_all_dashboard);

        wot = view.findViewById(R.id.work_order_total_dashboard);
        rt = view.findViewById(R.id.receive_total_dashboard);
        sot = view.findViewById(R.id.sales_order_total_dashboard);
        dt = view.findViewById(R.id.delivery_total_dashboard);
        ct = view.findViewById(R.id.customer_total_dashboard);
        st = view.findViewById(R.id.supplier_total_dashboard);

        cateValuesDD = new ArrayList<>();

        PieChartInit();
        LineChartInit();

//        MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view);
//        mv.setChartView(lineChart);
//        lineChart.setMarker(mv);

        if (userInfoLists.size() != 0) {
            String firstname = userInfoLists.get(0).getUser_fname();
            String lastName = userInfoLists.get(0).getUser_lname();
            if (firstname == null) {
                firstname = "";
            }
            if (lastName == null) {
                lastName = "";
            }
            String empFullName = firstname+" "+lastName;
            userName.setText(empFullName);
        }

        Calendar today = Calendar.getInstance();
        today.get(Calendar.YEAR);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy", Locale.getDefault());


        String formattedYear = "";
        formattedYear = df.format(c);

        String yy = simpleDateFormat.format(c);

        firstDate = "01-JAN-"+yy;
        lastDate = "31-DEC-"+yy;

        AlertDialog dialog;

        int year = Integer.parseInt(formattedYear);

        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(mContext, new MonthPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int selectedMonth, int selectedYear) {
                System.out.println("Selected Year: "+selectedYear);
                selectYear.setText("Year: "+ String.valueOf(selectedYear));

                String syy = String.valueOf(selectedYear);
                syy = syy.substring(2);
                firstDate = "01-JAN-"+syy;
                lastDate = "31-DEC-"+syy;
                new Check().execute();
            }
        },today.get(Calendar.YEAR),today.get(Calendar.MONTH));

        builder.setActivatedYear(Integer.parseInt(formattedYear))
                .setMinYear(2000)
                .setMaxYear(year)
                .showYearOnly()
                .setTitle("Selected Year")
                .setOnYearChangedListener(new MonthPickerDialog.OnYearChangedListener() {
                    @Override
                    public void onYearChanged(int year) {

                    }
                });

        dialog = builder.build();


        selectYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();


            }
        });

        new Check().execute();

        return view;
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
    public void LineChartInit() {


        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.GRAY);
        lineChart.getDescription().setEnabled(false);
        lineChart.setPinchZoom(false);

        lineChart.setDrawGridBackground(false);

        lineChart.getAxisLeft().setDrawGridLines(true);
        lineChart.setScaleEnabled(true);
        lineChart.setTouchEnabled(true);
        lineChart.setHighlightPerTapEnabled(false);
        lineChart.setDoubleTapToZoomEnabled(false);

        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setAxisMinValue(0);
        lineChart.getLegend().setEnabled(false);
        lineChart.getAxisLeft().mAxisMinimum = 5000f;
        lineChart.getAxisLeft().setTextColor(Color.GRAY);
        lineChart.getAxisLeft().setXOffset(10f);
        lineChart.getAxisLeft().setTextSize(8);
        lineChart.getAxisLeft().setDrawGridLines(false);


    }

    public static class MyAxisValueFormatter extends ValueFormatter {
        private final ArrayList<String> mvalues;
        public MyAxisValueFormatter(ArrayList<String> mvalues) {
            this.mvalues = mvalues;
        }

        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            return (mvalues.get((int) value));
        }
    }

    public boolean isConnected () {
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

    public boolean isOnline () {

        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

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

                SummaryData();
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


                DecimalFormat formatter = new DecimalFormat("###,##,##,###");
                String formatted = formatter.format(work_order_total);

                wot.setText(formatted + " BDT");

                formatted = formatter.format(sales_order_total);
                sot.setText(formatted + " BDT");

                formatted = formatter.format(delivery_total);
                dt.setText(formatted + " BDT");

                formatted = formatter.format(receive_total);
                rt.setText(formatted + " BDT");

//                workOrderTotal = String.valueOf(work_order_total);
//                salesOrderTotal = String.valueOf(sales_order_total);
//                deliveryTotal = String.valueOf(delivery_total);
//                receiveTotal = String.valueOf(receive_total);
                customerTotal = String.valueOf(customer_total);
                supplierTotal = String.valueOf(supplier_total);

//                wot.setText(workOrderTotal);
//                sot.setText(salesOrderTotal);
//                dt.setText(deliveryTotal);
//                rt.setText(receiveTotal);
                ct.setText(customerTotal);
                st.setText(supplierTotal);

                //pie chart
                NoOfEmp = new ArrayList<>();
                NoOfEmp.add(new PieEntry(work_order_total,"Work Order",0));
                NoOfEmp.add(new PieEntry(receive_total,"Receive",1));
                NoOfEmp.add(new PieEntry(sales_order_total,"Sales Order",2));
                NoOfEmp.add(new PieEntry(delivery_total,"Delivery Challan",3));

                final int[] piecolors = new int[]{
                        Color.parseColor("#47C198"),
                        Color.parseColor("#4A6DE5"),
                        Color.parseColor("#ffc048"),
                        Color.parseColor("#CC66BE")};
                pieChart.animateXY(1000, 1000);

                PieDataSet dataSet = new PieDataSet(NoOfEmp, "");
                pieChart.animateXY(1000, 1000);
                pieChart.setEntryLabelColor(Color.TRANSPARENT);

                pieChart.setExtraRightOffset(20);

                PieData data = new PieData(dataSet);
                String label = dataSet.getValues().get(0).getLabel();
                System.out.println(label);
                if (label.equals("No Data Found")) {
                    dataSet.setValueTextColor(Color.TRANSPARENT);
                } else {
                    dataSet.setValueTextColor(Color.WHITE);
                }
                dataSet.setHighlightEnabled(true);
                dataSet.setValueTextSize(12);
                dataSet.setColors(ColorTemplate.createColors(piecolors));

                pieChart.setData(data);
                pieChart.invalidate();


                //line chart
                dataValue = new ArrayList<>();
//                dataValue.add(new Entry(0,20000,0));
//                dataValue.add(new Entry(1,30000,1));
//                dataValue.add(new Entry(2,15000,2));
//                dataValue.add(new Entry(3,40000,3));
//                dataValue.add(new Entry(4,45000,4));
//                dataValue.add(new Entry(5,15000,5));

                for (int i = 0; i < cateValuesDD.size(); i++) {
                    dataValue.add(new Entry(i,Float.parseFloat(cateValuesDD.get(i).getValue()), cateValuesDD.get(i).getId()));
                }


                monthName = new ArrayList<>();
//                monthName.add("JAN");
//                monthName.add("FEB");
//                monthName.add("MAR");
//                monthName.add("APR");
//                monthName.add("MAY");
//                monthName.add("JUN");

                for (int i = 0; i < cateValuesDD.size(); i++) {
                    monthName.add(cateValuesDD.get(i).getType());
                }

                lineChart.animateX(1000);
                LineDataSet lineDataSet = new LineDataSet(dataValue,"set1");
                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(lineDataSet);

                LineData data1 = new LineData(lineDataSet);
                lineDataSet.setCircleColor(Color.parseColor("#b2bec3"));
                lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
                lineDataSet.setColor(Color.parseColor("#00cec9"));
                lineDataSet.setDrawFilled(true);
                Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.chart_fill);
                lineDataSet.setFillDrawable(drawable);

                lineDataSet.setDrawCircleHole(false);
                lineChart.getXAxis().setValueFormatter(new MyAxisValueFormatter(monthName));
                lineChart.setData(data1);
                lineChart.invalidate();


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

    public void SummaryData () {

        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
            Statement stmt = connection.createStatement();

            workOrderTotal = "";
            work_order_total = 0;
            receiveTotal = "";
            receive_total = 0;
            salesOrderTotal = "";
            sales_order_total = 0;
            deliveryTotal = "";
            delivery_total = 0;
            supplierTotal = "";
            supplier_total = 0;
            customerTotal = "";
            customer_total = 0;

            cateValuesDD = new ArrayList<>();

            if (firstDate.isEmpty()) {
                firstDate = null;
            }
            if (lastDate.isEmpty()) {
                lastDate = null;
            }

            ResultSet resultSet = stmt.executeQuery("WITH JOB\n" +
                    "     AS (  SELECT J.WOJ_IM_ID, NVL (SUM (J.WOJ_JOB_AMT), 0) TOTAL_WOJ_JOB_AMT\n" +
                    "             FROM WORK_ORDER_MST M, WORK_ORDER_JOB J, ACC_DTL A\n" +
                    "            WHERE     M.WOM_ID = J.WOJ_WOM_ID\n" +
                    "                   AND m.p_disapproved_flag <> 2\n"+
                    "                  AND M.WOM_AD_ID = A.AD_ID\n" +
                    "                  AND (   TRUNC(M.WOM_DATE) BETWEEN case when TRUNC(TO_DATE('"+firstDate+"')) is null then TRUNC(M.WOM_DATE) else TRUNC(TO_DATE('"+firstDate+"')) end AND case when TRUNC(TO_DATE('"+lastDate+"')) is null then TRUNC(M.WOM_DATE) else TRUNC(TO_DATE('"+lastDate+"')) end )\n" +
                    "                  AND (M.WOM_AD_ID = null OR null IS NULL)\n" +
                    "                  AND (   M.P_FINAL_FLAG = null\n" +
                    "                       OR null IS NULL)\n" +
                    "         GROUP BY J.WOJ_IM_ID)\n" +
                    "SELECT ITEM.IM_ID,ITEM.IM_NAME category_name, NVL (JOB.TOTAL_WOJ_JOB_AMT, 0) TOTAL_JOB_AMT\n" +
                    "  FROM JOB, ITEM_MST ITEM\n" +
                    " WHERE JOB.WOJ_IM_ID(+) = ITEM.IM_ID\n" +
                    " ORDER BY ITEM.IM_ID asc");

            while (resultSet.next()) {
                if (resultSet.getString(3) != null) {
                    if (!resultSet.getString(3).isEmpty()) {
                        work_order_total = work_order_total + resultSet.getInt(3);
                    }
                }
            }

            resultSet.close();

            CallableStatement callableStatement = connection.prepareCall("begin ? := GET_CATEGORY_WISE_RCV_AMT(?,?,?,?); end;");
            callableStatement.setString(2,firstDate);
            callableStatement.setString(3,lastDate);
            callableStatement.setString(4,null);
            callableStatement.setString(5,null);
            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement.execute();

            ResultSet resultSet1 = (ResultSet) callableStatement.getObject(1);

            while (resultSet1.next()) {
                if (resultSet1.getString(3) != null) {
                    if (!resultSet1.getString(3).isEmpty()) {
                        receive_total = receive_total + resultSet1.getInt(3);
                    }
                }
            }

            resultSet1.close();

            callableStatement.close();

            CallableStatement callableStatement1 = connection.prepareCall("begin ? := GET_CATEGORY_WISE_ORDER(?,?,?); end;");
            callableStatement1.setString(2,null);
            callableStatement1.setString(3,firstDate);
            callableStatement1.setString(4,lastDate);
            callableStatement1.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement1.execute();

            ResultSet resultSet2 = (ResultSet) callableStatement1.getObject(1);

            while (resultSet2.next()) {

                if (resultSet2.getString(3) != null) {
                    if (!resultSet2.getString(3).isEmpty()) {
                        sales_order_total = sales_order_total + resultSet2.getInt(3);
                    }
                }
            }

            resultSet2.close();

            callableStatement1.close();

            CallableStatement callableStatement2 = connection.prepareCall("begin ? := GET_CATEGORY_WISE_SALES_INV(?,?,?); end;");
            callableStatement2.setString(4,null);
            callableStatement2.setString(2,firstDate);
            callableStatement2.setString(3,lastDate);
            callableStatement2.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement2.execute();

            ResultSet resultSet3 = (ResultSet) callableStatement2.getObject(1);

            while (resultSet3.next()) {

                if (resultSet3.getString(3) != null) {
                    if (!resultSet3.getString(3).isEmpty()) {
                        delivery_total = delivery_total + resultSet3.getInt(3);
                    }
                }
            }

            resultSet3.close();

            callableStatement2.close();

            ResultSet resultSet4 = stmt.executeQuery("select count(*) from acc_dtl where ad_flag = 7");

            while (resultSet4.next()) {
                supplier_total = resultSet4.getInt(1);
            }

            resultSet4.close();

            ResultSet resultSet5 = stmt.executeQuery("select count(*) from acc_dtl where ad_flag = 6");

            while (resultSet5.next()) {
                customer_total = resultSet5.getInt(1);
            }

            resultSet5.close();

            CallableStatement callableStatement3 = connection.prepareCall("begin ? := GET_MONTH_WISE_SALES(?,?,?,?,?); end;");
            callableStatement3.setString(2,firstDate);
            callableStatement3.setString(3,lastDate);
            callableStatement3.setString(4,null);
            callableStatement3.setString(5,null);
            callableStatement3.setInt(6,1);

            callableStatement3.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement3.execute();

            ResultSet resultSet6 = (ResultSet) callableStatement3.getObject(1);


            while (resultSet6.next()) {
                cateValuesDD.add(new ChartValue(resultSet6.getString(2),resultSet6.getString(1),resultSet6.getString(3)));

            }

            callableStatement3.close();

            stmt.close();

            if (firstDate == null) {
                firstDate = "";
            }
            if (lastDate == null) {
                lastDate = "";
            }

            System.out.println(work_order_total);
            System.out.println(receive_total);
            System.out.println(sales_order_total);
            System.out.println(delivery_total);


            connected = true;

            connection.close();

        } catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }

    }
}
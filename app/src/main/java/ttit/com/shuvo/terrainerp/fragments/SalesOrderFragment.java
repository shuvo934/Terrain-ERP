package ttit.com.shuvo.terrainerp.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.textfield.TextInputEditText;
import com.rosemaryapp.amazingspinner.AmazingSpinner;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.WaitProgress;
import ttit.com.shuvo.terrainerp.adapters.SalesOrderAdapter;
import ttit.com.shuvo.terrainerp.arrayList.ChartValue;
import ttit.com.shuvo.terrainerp.arrayList.ReceiveTypeList;
import ttit.com.shuvo.terrainerp.arrayList.SalesOrderList;
import ttit.com.shuvo.terrainerp.dialogues.SalesOrderDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SalesOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SalesOrderFragment extends Fragment implements SalesOrderAdapter.ClickedItem {

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

//    private Connection connection;

    TextInputEditText beginDate;
    TextInputEditText endDate;
    TextView daterange;
    AmazingSpinner clientSpinner;
    TextView noOrderMsg;

    TextView totalDisAmnt;
    TextView totalVatAmnt;
    TextView totalOrder;
    TextView totalAdvance;

    TextView totalSalesOrderAmount;

    BarChart barChart;
    ArrayList<ChartValue> categoryValues;
    ArrayList<String> barItem;
    ArrayList<String> barValue;
    ArrayList<BarEntry> barEntries;

    String firstDate = "";
    String lastDate = "";
    String clientId = "";
    private int mYear, mMonth, mDay;

    ArrayList<ReceiveTypeList> clientLists;
    ArrayList<ReceiveTypeList> sortedClientLists;
    ArrayList<SalesOrderList> salesOrderLists;

    RecyclerView itemView;
    SalesOrderAdapter salesOrderAdapter;
    RecyclerView.LayoutManager layoutManager;

    double disc_amnt = 0.0;
    double vat_amnt = 0.0;
    double order_amnt = 0.0;
    double adv_amnt = 0.0;
    double total_sales_order_amnt = 0.0;

    public static String ORDER_NO = "";
    public static String ORDER_DATE = "";
    public static String CLIENT_NAME = "";
    public static String CLIENT_CODE = "";
    public static String TARGET_ADDRESS = "";
    public static String ED_DATE = "";
    public static String CONTACT_NO = "";
    public static String CLIENT_EMAIL = "";
    public static String SOM_ID = "";
    public static String CONTACT_PERSON = "";
    public static String ADVANCE_AMOUNT = "";
    public static String VAT_AMNT = "";
    public static int fromSO = 0;

    public SalesOrderFragment() {
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
     * @return A new instance of fragment SalesOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SalesOrderFragment newInstance(String param1, String param2) {
        SalesOrderFragment fragment = new SalesOrderFragment();
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
        View view = inflater.inflate(R.layout.fragment_sales_order, container, false);
        beginDate = view.findViewById(R.id.begin_date_sales_order);
        endDate = view.findViewById(R.id.end_date_sales_order);
        daterange = view.findViewById(R.id.date_range_msg_sales_order);
        clientSpinner = view.findViewById(R.id.client_name_so_spinner);
        itemView = view.findViewById(R.id.sales_order_report_view);
        noOrderMsg = view.findViewById(R.id.no_sales_order_msg);
        totalDisAmnt = view.findViewById(R.id.total_discount_amnt_so);
        totalVatAmnt = view.findViewById(R.id.total_vat_amnt_so);
        totalAdvance = view.findViewById(R.id.total_advance_amnt_so);
        totalOrder = view.findViewById(R.id.total_amnt_so);
        totalSalesOrderAmount = view.findViewById(R.id.total_sales_order_amount);
        barChart = view.findViewById(R.id.sales_order_category_overview);

        clientLists = new ArrayList<>();
        sortedClientLists = new ArrayList<>();

        BarChartInit();

        categoryValues = new ArrayList<>();
        barItem = new ArrayList<>();
        barValue = new ArrayList<>();
        barEntries = new ArrayList<>();

        itemView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        itemView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(itemView.getContext(),DividerItemDecoration.VERTICAL);
        itemView.addItemDecoration(dividerItemDecoration);


        // Selecting Client
        clientSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                System.out.println(position+": "+name);
                for (int i = 0; i < sortedClientLists.size(); i++) {
                    if (name.equals(sortedClientLists.get(i).getType())) {
                        clientId = sortedClientLists.get(i).getId();
                    }
                }
                System.out.println("CLIENT ID: "+ clientId);

//                new CheckReport().execute();
                getReportData();

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
//                                new Check().execute();
                                getClient();

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

//                                        new Check().execute();
                                        getClient();
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
//                                new Check().execute();
                                getClient();
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

//                                        new Check().execute();
                                        getClient();
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
        getClient();

        return view;
    }

    public void BarChartInit() {

        categoryValues = new ArrayList<>();
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
        barChart.setTouchEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setClickable(true);
        barChart.setHighlightPerTapEnabled(false);
        barChart.setHighlightPerDragEnabled(false);
        //salaryChart.setOnTouchListener(null);

        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setAxisMinValue(0);
        barChart.getLegend().setEnabled(false);


    }

    private void declareValue() {
         disc_amnt = 0.0;
         vat_amnt = 0.0;
         order_amnt = 0.0;
         adv_amnt = 0.0;
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

        ORDER_NO = salesOrderLists.get(CategoryPosition).getOrder_no();
        ORDER_DATE = salesOrderLists.get(CategoryPosition).getOrder_date();
        CLIENT_NAME = salesOrderLists.get(CategoryPosition).getAd_name();
        CLIENT_CODE = salesOrderLists.get(CategoryPosition).getClient_code();
        TARGET_ADDRESS = salesOrderLists.get(CategoryPosition).getTarget_address();
        ED_DATE = salesOrderLists.get(CategoryPosition).getEdd();
        CONTACT_NO = salesOrderLists.get(CategoryPosition).getContact_number();
        CLIENT_EMAIL = salesOrderLists.get(CategoryPosition).getPerson_email();
        SOM_ID = salesOrderLists.get(CategoryPosition).getOrder_id();
        CONTACT_PERSON = salesOrderLists.get(CategoryPosition).getContact_person();
        ADVANCE_AMOUNT = salesOrderLists.get(CategoryPosition).getAdvance_amnt();
        VAT_AMNT = salesOrderLists.get(CategoryPosition).getVat_amnt();
        fromSO = 1;

        SalesOrderDetails salesOrderDetails = new SalesOrderDetails(mContext);
        salesOrderDetails.show(getActivity().getSupportFragmentManager(),"SOD");


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
//                ClientData();
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
//                clientSpinner.setText("");
//                sortedClientLists = new ArrayList<>();
//                boolean adFound = false;
//                sortedClientLists.add(new ReceiveTypeList("","All Clients"));
//                for (int i = 0; i < clientLists.size(); i++) {
//                    String ad_id = clientLists.get(i).getId();
//                    for (int j = 0 ; j < salesOrderLists.size(); j++) {
//                        if (ad_id.equals(salesOrderLists.get(j).getAd_id())) {
//                            adFound = false;
//                            if (sortedClientLists.size() != 0) {
//                                for (int k = 0; k < sortedClientLists.size(); k++) {
//                                    if (ad_id.equals(sortedClientLists.get(k).getId())) {
//                                        adFound = true;
//                                    }
//                                }
//                            }
//                            if (!adFound) {
//
//                                sortedClientLists.add(new ReceiveTypeList(salesOrderLists.get(j).getAd_id(),salesOrderLists.get(j).getAd_name()));
//                            }
//                        }
//                    }
//                }
//
//                ArrayList<String> type = new ArrayList<>();
//                for(int i = 0; i < sortedClientLists.size(); i++) {
//                    type.add(sortedClientLists.get(i).getType());
//                }
//                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);
//
//                clientSpinner.setAdapter(arrayAdapter);
//
////                String ad_id = "";
////                boolean first = true;
////                for (int i = 0; i < salesOrderLists.size(); i++) {
////                    ad_id = salesOrderLists.get(i).getAd_id();
////                    first = true;
////                    for (int j = 0 ; j < salesOrderLists.size(); j++) {
////
////                        if (ad_id.equals(salesOrderLists.get(j).getAd_id())) {
////                            if (first) {
////                                first = false;
////                            }
////                            else {
////                                salesOrderLists.get(j).setAd_name("||");
////                            }
////                        }
////                    }
////                }
//                if (salesOrderLists.size() ==0) {
//                    noOrderMsg.setVisibility(View.VISIBLE);
//                }else {
//                    noOrderMsg.setVisibility(View.GONE);
//                }
//
//                DecimalFormat formatter = new DecimalFormat("##,##,##,###");
//                String formatted = formatter.format(order_amnt);
//
//                totalOrder.setText(formatted);
//
//                formatted = formatter.format(vat_amnt);
//                totalVatAmnt.setText(formatted);
//
//                formatted = formatter.format(disc_amnt);
//                totalDisAmnt.setText(formatted);
//
//                formatted = formatter.format(adv_amnt);
//                totalAdvance.setText(formatted);
//
//                salesOrderAdapter = new SalesOrderAdapter(salesOrderLists, getContext(),SalesOrderFragment.this);
//                itemView.setAdapter(salesOrderAdapter);
//
//                for (int i = 0; i < categoryValues.size(); i++) {
//                    if (categoryValues.get(i).getValue() != null) {
//                        total_sales_order_amnt = total_sales_order_amnt + Double.parseDouble(categoryValues.get(i).getValue());
//                    }
//                }
//
//                for (int i = 0; i < categoryValues.size(); i++) {
//                    barItem.add(categoryValues.get(i).getType());
//                }
//
//                for (int i = 0; i < categoryValues.size(); i++) {
//                    barEntries.add(new BarEntry(i,Float.parseFloat(categoryValues.get(i).getValue()), categoryValues.get(i).getId()));
//                }
//
//                BarDataSet bardataset = new BarDataSet(barEntries, "Months");
//                barChart.animateY(1000);
//                barChart.highlightValues(null);
//
//                BarData data1 = new BarData(bardataset);
//                //bardataset.setColors(ColorTemplate.VORDIPLOM_COLORS);
//                int cc = Color.parseColor("#ffc048");
//                bardataset.setColor(cc);
////                if (cc == 0) {
////
////                } else {
////                    //int newCC = darkenColor(cc,0.8f);
////
////                    bardataset.setColor(cc);
////                }
//                bardataset.setBarBorderColor(Color.DKGRAY);
//                bardataset.setValueTextSize(12);
//                barChart.setData(data1);
////                barChart.getXAxis().setValueFormatter(new MyAxisValueFormatter(barItem));
//                barChart.getXAxis().setValueFormatter(new ValueFormatter() {
//                    @Override
//                    public String getAxisLabel(float value, AxisBase axis) {
//                        //System.out.println(value);
//                        float size = barItem.size() - 1;
//                        if (value <= size) {
//                            return (barItem.get((int) value));
//                        } else {
//                            return super.getAxisLabel(value, axis);
//                        }
//
//                    }
//                });
//                formatted = formatter.format(total_sales_order_amnt);
//                totalSalesOrderAmount.setText(formatted + " BDT");
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

//    public class CheckReport extends AsyncTask<Void, Void, Void> {
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
//                OnlyReportData();
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
//                String ad_id = "";
//                boolean first = true;
//                for (int i = 0; i < salesOrderLists.size(); i++) {
//                    ad_id = salesOrderLists.get(i).getAd_id();
//                    first = true;
//                    for (int j = 0 ; j < salesOrderLists.size(); j++) {
//
//                        if (ad_id.equals(salesOrderLists.get(j).getAd_id())) {
//                            if (first) {
//                                first = false;
//                            }
//                            else {
//                                salesOrderLists.get(j).setAd_name("||");
//                            }
//                        }
//                    }
//                }
//
//                if (salesOrderLists.size() ==0) {
//                    noOrderMsg.setVisibility(View.VISIBLE);
//                }else {
//                    noOrderMsg.setVisibility(View.GONE);
//                }
//
//                DecimalFormat formatter = new DecimalFormat("##,##,##,###");
//                String formatted = formatter.format(order_amnt);
//
//                totalOrder.setText(formatted);
//
//                formatted = formatter.format(vat_amnt);
//                totalVatAmnt.setText(formatted);
//
//                formatted = formatter.format(disc_amnt);
//                totalDisAmnt.setText(formatted);
//
//                formatted = formatter.format(adv_amnt);
//                totalAdvance.setText(formatted);
//
//                salesOrderAdapter = new SalesOrderAdapter(salesOrderLists, getContext(),SalesOrderFragment.this);
//                itemView.setAdapter(salesOrderAdapter);
//
//
//                for (int i = 0; i < categoryValues.size(); i++) {
//                    if (categoryValues.get(i).getValue() != null) {
//                        total_sales_order_amnt = total_sales_order_amnt + Double.parseDouble(categoryValues.get(i).getValue());
//                    }
//                }
//
//                for (int i = 0; i < categoryValues.size(); i++) {
//                    barItem.add(categoryValues.get(i).getType());
//                }
//
//                for (int i = 0; i < categoryValues.size(); i++) {
//                    barEntries.add(new BarEntry(i,Float.parseFloat(categoryValues.get(i).getValue()), categoryValues.get(i).getId()));
//                }
//
//                BarDataSet bardataset = new BarDataSet(barEntries, "Months");
//                barChart.animateY(1000);
//                barChart.highlightValues(null);
//
//                BarData data1 = new BarData(bardataset);
//                //bardataset.setColors(ColorTemplate.VORDIPLOM_COLORS);
//                int cc = Color.parseColor("#ffc048");
//                bardataset.setColor(cc);
////                if (cc == 0) {
////
////                } else {
////                    //int newCC = darkenColor(cc,0.8f);
////
////                    bardataset.setColor(cc);
////                }
//                bardataset.setBarBorderColor(Color.DKGRAY);
//                bardataset.setValueTextSize(12);
//                barChart.setData(data1);
////                barChart.getXAxis().setValueFormatter(new MyAxisValueFormatter(barItem));
//                barChart.getXAxis().setValueFormatter(new ValueFormatter() {
//                    @Override
//                    public String getAxisLabel(float value, AxisBase axis) {
//                        //System.out.println(value);
//                        float size = barItem.size() - 1;
//                        if (value <= size) {
//                            return (barItem.get((int) value));
//                        } else {
//                            return super.getAxisLabel(value, axis);
//                        }
//
//                    }
//                });
//                formatted = formatter.format(total_sales_order_amnt);
//                totalSalesOrderAmount.setText(formatted + " BDT");
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
//                        new CheckReport().execute();
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

//    public void ClientData() {
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//
//            clientLists = new ArrayList<>();
//            salesOrderLists = new ArrayList<>();
//            clientId = "";
//            declareValue();
//            total_sales_order_amnt = 0.0;
//
//            categoryValues = new ArrayList<>();
//            barItem = new ArrayList<>();
//            barValue = new ArrayList<>();
//            barEntries = new ArrayList<>();
//
//            Statement stmt = connection.createStatement();
//            if (firstDate.isEmpty()) {
//                firstDate = null;
//            }
//            if (lastDate.isEmpty()) {
//                lastDate = null;
//            }
//            if (clientId != null) {
//                if (clientId.isEmpty()) {
//                    clientId = null;
//                }
//            }
//
//
////            ResultSet resultSet = stmt.executeQuery("select ad_id, ad_name from acc_dtl where ad_flag = 6");
////
////            while (resultSet.next()) {
////                clientLists.add(new ReceiveTypeList(resultSet.getString(1),resultSet.getString(2)));
////            }
//
////            CallableStatement callableStatement = connection.prepareCall("begin ? := GET_SALES_ORDER_LIST(?,?,?); end;");
////            callableStatement.setString(2,firstDate);
////            callableStatement.setString(3,lastDate);
////            callableStatement.setString(4,clientId);
////            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
////            callableStatement.execute();
////
////            ResultSet resultSet1 = (ResultSet) callableStatement.getObject(1);
////
////
////
////            while (resultSet1.next()) {
////                salesOrderLists.add(new SalesOrderList(resultSet1.getString(1), resultSet1.getString(2),resultSet1.getString(3),
////                        resultSet1.getString(4), resultSet1.getString(5),resultSet1.getString(6),
////                        resultSet1.getString(7),resultSet1.getString(8), resultSet1.getString(9),
////                        resultSet1.getString(10),resultSet1.getString(11),resultSet1.getString(12),
////                        resultSet1.getString(13),resultSet1.getString(14),resultSet1.getString(15),
////                        resultSet1.getString(16),resultSet1.getString(17),resultSet1.getString(18)));
////            }
////            callableStatement.close();
//
//
////            CallableStatement callableStatement1 = connection.prepareCall("begin ? := GET_CATEGORY_WISE_ORDER(?,?,?); end;");
////            callableStatement1.setString(2,clientId);
////            callableStatement1.setString(3,firstDate);
////            callableStatement1.setString(4,lastDate);
////            callableStatement1.registerOutParameter(1, OracleTypes.CURSOR);
////            callableStatement1.execute();
////
////            ResultSet resultSet2 = (ResultSet) callableStatement1.getObject(1);
////
////            while (resultSet2.next()) {
////                categoryValues.add(new ChartValue(resultSet2.getString(1),resultSet2.getString(2),resultSet2.getString(3)));
////            }
////
////
////            callableStatement1.close();
//
//            if (firstDate == null) {
//                firstDate = "";
//            }
//            if (lastDate == null) {
//                lastDate = "";
//            }
//            if (clientId == null) {
//                clientId = "";
//            }
//
////            for (int i = 0; i < salesOrderLists.size(); i++) {
////                if (salesOrderLists.get(i).getDiscount_amnt() != null) {
////                    disc_amnt = disc_amnt + Double.parseDouble(salesOrderLists.get(i).getDiscount_amnt());
////                }
////                if (salesOrderLists.get(i).getVat_amnt() != null) {
////                    vat_amnt = vat_amnt + Double.parseDouble(salesOrderLists.get(i).getVat_amnt());
////                }
////                if (salesOrderLists.get(i).getTotal_order_amnt() != null) {
////                    order_amnt = order_amnt + Double.parseDouble(salesOrderLists.get(i).getTotal_order_amnt());
////                }
////                if (salesOrderLists.get(i).getAdvance_amnt() != null) {
////                    adv_amnt = adv_amnt + Double.parseDouble(salesOrderLists.get(i).getAdvance_amnt());
////                }
////            }
//
//
//            connected = true;
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

    public void getClient() {
        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;
        clientLists = new ArrayList<>();
        clientId = "";
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

                        clientLists.add(new ReceiveTypeList(ad_id,ad_name,""));
                    }
                }
                getData(1);
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

        requestQueue.add(clientReq);
    }

    private void updateFragment() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                clientSpinner.setText("");
                sortedClientLists = new ArrayList<>();
                boolean adFound = false;
                sortedClientLists.add(new ReceiveTypeList("","All Clients",""));
                for (int i = 0; i < clientLists.size(); i++) {
                    String ad_id = clientLists.get(i).getId();
                    for (int j = 0 ; j < salesOrderLists.size(); j++) {
                        if (ad_id.equals(salesOrderLists.get(j).getAd_id())) {
                            adFound = false;
                            if (sortedClientLists.size() != 0) {
                                for (int k = 0; k < sortedClientLists.size(); k++) {
                                    if (ad_id.equals(sortedClientLists.get(k).getId())) {
                                        adFound = true;
                                    }
                                }
                            }
                            if (!adFound) {

                                sortedClientLists.add(new ReceiveTypeList(salesOrderLists.get(j).getAd_id(),salesOrderLists.get(j).getAd_name(),""));
                            }
                        }
                    }
                }

                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < sortedClientLists.size(); i++) {
                    type.add(sortedClientLists.get(i).getType());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                clientSpinner.setAdapter(arrayAdapter);

//                String ad_id = "";
//                boolean first = true;
//                for (int i = 0; i < salesOrderLists.size(); i++) {
//                    ad_id = salesOrderLists.get(i).getAd_id();
//                    first = true;
//                    for (int j = 0 ; j < salesOrderLists.size(); j++) {
//
//                        if (ad_id.equals(salesOrderLists.get(j).getAd_id())) {
//                            if (first) {
//                                first = false;
//                            }
//                            else {
//                                salesOrderLists.get(j).setAd_name("||");
//                            }
//                        }
//                    }
//                }
                updateData();
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

                    getClient();
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

                getClient();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }

    public void getData(int parser) {
        salesOrderLists = new ArrayList<>();
        declareValue();
        total_sales_order_amnt = 0.0;

        categoryValues = new ArrayList<>();
        barItem = new ArrayList<>();
        barValue = new ArrayList<>();
        barEntries = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        String salesListUrl = "http://103.56.208.123:8001/apex/tterp/salesOrder/getSalesOrderList?st_date="+firstDate+"&end_date="+lastDate+"&ad_id="+clientId+"";
        String catOrderUrl = "http://103.56.208.123:8001/apex/tterp/salesOrder/getCatWiseOrder?st_date="+firstDate+"&end_date="+lastDate+"&ad_id="+clientId+"";

        StringRequest catOrderReq = new StringRequest(Request.Method.GET, catOrderUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        String category_wise_order = info.getString("category_wise_order");
                        JSONArray orderArray = new JSONArray(category_wise_order);
                        for (int j = 0; j < orderArray.length(); j++) {
                            JSONObject sales_info = orderArray.getJSONObject(j);

                            String im_id = sales_info.getString("im_id");
                            String im_name = sales_info.getString("im_name");
                            String total_order = sales_info.getString("total_order");

                            categoryValues.add(new ChartValue(im_id,im_name,total_order));
                        }
                    }
                }
                connected = true;
                if (parser == 1) {
                    updateFragment();
                }
                else if (parser == 2) {
                    updateLayout();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                if (parser == 1) {
                    updateFragment();
                }
                else if (parser == 2) {
                    updateLayout();
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
                updateLayout();
            }
        });

        StringRequest salesListReq = new StringRequest(Request.Method.GET, salesListUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        String sales_order_list = info.getString("sales_order_list");
                        JSONArray salesArray = new JSONArray(sales_order_list);
                        for (int j = 0; j < salesArray.length(); j++) {
                            JSONObject sales_info = salesArray.getJSONObject(j);

                            String som_id_new = sales_info.getString("som_id");
                            String ad_id_new = sales_info.getString("ad_id");
                            String ad_name_new = sales_info.getString("ad_name");
                            String ad_code = sales_info.getString("ad_code");
                            String som_order_no = sales_info.getString("som_order_no");
                            String som_date = sales_info.getString("som_date");
                            String som_user = sales_info.getString("som_user");

                            String target_delivery = sales_info.getString("target_delivery")
                                    .equals("null") ? "" : sales_info.getString("target_delivery");
                            String target_address = sales_info.getString("target_address")
                                    .equals("null") ? "" : sales_info.getString("target_address");
                            String order_type = sales_info.getString("order_type")
                                    .equals("null") ? "" : sales_info.getString("order_type");
                            String edd = sales_info.getString("edd");
                            String som_discount_value = sales_info.getString("som_discount_value")
                                    .equals("null") ? "" : sales_info.getString("som_discount_value");
                            String som_vat_amt = sales_info.getString("som_vat_amt")
                                    .equals("null") ? "" : sales_info.getString("som_vat_amt");
                            String aad_contact_person = sales_info.getString("aad_contact_person")
                                    .equals("null") ? "" : sales_info.getString("aad_contact_person");
                            String aad_contact_no = sales_info.getString("aad_contact_no")
                                    .equals("null") ? "" : sales_info.getString("aad_contact_no");
                            String aad_email = sales_info.getString("aad_email")
                                    .equals("null") ? "" : sales_info.getString("aad_email");
                            String total_amt = sales_info.getString("total_amt")
                                    .equals("null") ? "" : sales_info.getString("total_amt");
                            String som_advance_amt = sales_info.getString("som_advance_amt")
                                    .equals("null") ? "" : sales_info.getString("som_advance_amt");

                            salesOrderLists.add(new SalesOrderList(som_id_new, ad_id_new,ad_name_new,
                                    ad_code, som_order_no,som_date, som_user,target_delivery, target_address,
                                    order_type,edd,som_discount_value, som_vat_amt,aad_contact_person,aad_contact_no,
                                    aad_email,total_amt,som_advance_amt));
                        }
                    }
                    for (int i = 0; i < salesOrderLists.size(); i++) {
                        if (salesOrderLists.get(i).getDiscount_amnt() != null) {
                            if (!salesOrderLists.get(i).getDiscount_amnt().isEmpty()) {
                                disc_amnt = disc_amnt + Double.parseDouble(salesOrderLists.get(i).getDiscount_amnt());
                            }
                        }
                        if (salesOrderLists.get(i).getVat_amnt() != null) {
                            if (!salesOrderLists.get(i).getVat_amnt().isEmpty()) {
                                vat_amnt = vat_amnt + Double.parseDouble(salesOrderLists.get(i).getVat_amnt());
                            }
                        }
                        if (salesOrderLists.get(i).getTotal_order_amnt() != null) {
                            if (!salesOrderLists.get(i).getTotal_order_amnt().isEmpty()) {
                                order_amnt = order_amnt + Double.parseDouble(salesOrderLists.get(i).getTotal_order_amnt());
                            }
                        }
                        if (salesOrderLists.get(i).getAdvance_amnt() != null) {
                            if (!salesOrderLists.get(i).getAdvance_amnt().isEmpty()) {
                                adv_amnt = adv_amnt + Double.parseDouble(salesOrderLists.get(i).getAdvance_amnt());
                            }
                        }
                    }
                }

                requestQueue.add(catOrderReq);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                if (parser == 1) {
                    updateFragment();
                }
                else if (parser == 2) {
                    updateLayout();
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
                updateLayout();
            }
        });

        requestQueue.add(salesListReq);
    }

    private void updateData() {
        if (salesOrderLists.size() ==0) {
            noOrderMsg.setVisibility(View.VISIBLE);
        }else {
            noOrderMsg.setVisibility(View.GONE);
        }

        DecimalFormat formatter = new DecimalFormat("##,##,##,###");
        String formatted = formatter.format(order_amnt);

        totalOrder.setText(formatted);

        formatted = formatter.format(vat_amnt);
        totalVatAmnt.setText(formatted);

        formatted = formatter.format(disc_amnt);
        totalDisAmnt.setText(formatted);

        formatted = formatter.format(adv_amnt);
        totalAdvance.setText(formatted);

        salesOrderAdapter = new SalesOrderAdapter(salesOrderLists, getContext(),SalesOrderFragment.this);
        itemView.setAdapter(salesOrderAdapter);

        for (int i = 0; i < categoryValues.size(); i++) {
            if (categoryValues.get(i).getValue() != null) {
                total_sales_order_amnt = total_sales_order_amnt + Double.parseDouble(categoryValues.get(i).getValue());
            }
        }

        for (int i = 0; i < categoryValues.size(); i++) {
            barItem.add(categoryValues.get(i).getType());
        }

        for (int i = 0; i < categoryValues.size(); i++) {
            barEntries.add(new BarEntry(i,Float.parseFloat(categoryValues.get(i).getValue()), categoryValues.get(i).getId()));
        }

        BarDataSet bardataset = new BarDataSet(barEntries, "Months");
        barChart.animateY(1000);
        barChart.highlightValues(null);

        BarData data1 = new BarData(bardataset);
        //bardataset.setColors(ColorTemplate.VORDIPLOM_COLORS);
        int cc = Color.parseColor("#ffc048");
        bardataset.setColor(cc);
//                if (cc == 0) {
//
//                } else {
//                    //int newCC = darkenColor(cc,0.8f);
//
//                    bardataset.setColor(cc);
//                }
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
        formatted = formatter.format(total_sales_order_amnt);
        totalSalesOrderAmount.setText(formatted + " BDT");
    }

//    public void OnlyReportData() {
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//
//            declareValue();
//            salesOrderLists = new ArrayList<>();
//
//            total_sales_order_amnt = 0.0;
//
//            categoryValues = new ArrayList<>();
//            barItem = new ArrayList<>();
//            barValue = new ArrayList<>();
//            barEntries = new ArrayList<>();
//
//            if (firstDate.isEmpty()) {
//                firstDate = null;
//            }
//            if (lastDate.isEmpty()) {
//                lastDate = null;
//            }
//            if (clientId != null) {
//                if (clientId.isEmpty()) {
//                    clientId = null;
//                }
//            }
//
//
//            CallableStatement callableStatement = connection.prepareCall("begin ? := GET_SALES_ORDER_LIST(?,?,?); end;");
//            callableStatement.setString(2,firstDate);
//            callableStatement.setString(3,lastDate);
//            callableStatement.setString(4,clientId);
//            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
//            callableStatement.execute();
//
//            ResultSet resultSet1 = (ResultSet) callableStatement.getObject(1);
//
//            while (resultSet1.next()) {
//                salesOrderLists.add(new SalesOrderList(resultSet1.getString(1), resultSet1.getString(2),resultSet1.getString(3),
//                        resultSet1.getString(4), resultSet1.getString(5),resultSet1.getString(6),
//                        resultSet1.getString(7),resultSet1.getString(8), resultSet1.getString(9),
//                        resultSet1.getString(10),resultSet1.getString(11),resultSet1.getString(12),
//                        resultSet1.getString(13),resultSet1.getString(14),resultSet1.getString(15),
//                        resultSet1.getString(16),resultSet1.getString(17),resultSet1.getString(18)));
//            }
//            callableStatement.close();
//
//            CallableStatement callableStatement1 = connection.prepareCall("begin ? := GET_CATEGORY_WISE_ORDER(?,?,?); end;");
//            callableStatement1.setString(2,clientId);
//            callableStatement1.setString(3,firstDate);
//            callableStatement1.setString(4,lastDate);
//            callableStatement1.registerOutParameter(1, OracleTypes.CURSOR);
//            callableStatement1.execute();
//
//            ResultSet resultSet2 = (ResultSet) callableStatement1.getObject(1);
//
//            while (resultSet2.next()) {
//                categoryValues.add(new ChartValue(resultSet2.getString(1),resultSet2.getString(2),resultSet2.getString(3)));
//            }
//
//
//            callableStatement1.close();
//
//            if (firstDate == null) {
//                firstDate = "";
//            }
//            if (lastDate == null) {
//                lastDate = "";
//            }
//            if (clientId == null) {
//                clientId = "";
//            }
//
//            for (int i = 0; i < salesOrderLists.size(); i++) {
//                if (salesOrderLists.get(i).getDiscount_amnt() != null) {
//                    disc_amnt = disc_amnt + Double.parseDouble(salesOrderLists.get(i).getDiscount_amnt());
//                }
//                if (salesOrderLists.get(i).getVat_amnt() != null) {
//                    vat_amnt = vat_amnt + Double.parseDouble(salesOrderLists.get(i).getVat_amnt());
//                }
//                if (salesOrderLists.get(i).getTotal_order_amnt() != null) {
//                    order_amnt = order_amnt + Double.parseDouble(salesOrderLists.get(i).getTotal_order_amnt());
//                }
//                if (salesOrderLists.get(i).getAdvance_amnt() != null) {
//                    adv_amnt = adv_amnt + Double.parseDouble(salesOrderLists.get(i).getAdvance_amnt());
//                }
//            }
//
//
//            connected = true;
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

    public void getReportData() {
        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;
        getData(2);
    }

    private void updateLayout() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;


                String ad_id = "";
                boolean first = true;
                for (int i = 0; i < salesOrderLists.size(); i++) {
                    ad_id = salesOrderLists.get(i).getAd_id();
                    first = true;
                    for (int j = 0 ; j < salesOrderLists.size(); j++) {

                        if (ad_id.equals(salesOrderLists.get(j).getAd_id())) {
                            if (first) {
                                first = false;
                            }
                            else {
                                salesOrderLists.get(j).setAd_name("||");
                            }
                        }
                    }
                }

                updateData();
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

                    getReportData();
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

                getReportData();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }
}
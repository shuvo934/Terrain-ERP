package ttit.com.shuvo.icomerp.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
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
import ttit.com.shuvo.icomerp.adapters.DeliveryChallanAdapter;
import ttit.com.shuvo.icomerp.adapters.SalesOrderAdapter;
import ttit.com.shuvo.icomerp.arrayList.ChartValue;
import ttit.com.shuvo.icomerp.arrayList.DeliveryChallanList;
import ttit.com.shuvo.icomerp.arrayList.ReceiveTypeList;
import ttit.com.shuvo.icomerp.arrayList.SalesOrderList;
import ttit.com.shuvo.icomerp.dialogues.DeliveryChallanDetails;
import ttit.com.shuvo.icomerp.dialogues.SalesOrderDetails;

import static ttit.com.shuvo.icomerp.OracleConnection.createConnection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeliveryFragement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeliveryFragement extends Fragment implements DeliveryChallanAdapter.ClickedItem {

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
    AmazingSpinner clientSpinner;
    TextView noOrderMsg;

    TextView totalInvoiceAmnt;
    TextView totalVatAmntInvoice;


    TextView totalDeliveryChallanAmount;

    double inv_amnt = 0.0;
    double vat_amnt_inv = 0.0;
    double total_delv_chal_amnt = 0.0;

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
    ArrayList<DeliveryChallanList> deliveryChallanLists;

    RecyclerView itemView;
    DeliveryChallanAdapter deliveryChallanAdapter;
    RecyclerView.LayoutManager layoutManager;

    public static String INV_NO = "";
    public static String INV_DATE = "";
    public static String SO_NO = "";
    public static String SO_DATE = "";
    public static String C_NAME = "";
    public static String C_CODE = "";
    public static String ADDS = "";
    public static String EDD = "";
    public static String CONTACT = "";
    public static String C_EMAIL = "";
    public static String PERSON = "";
    public static String SM_ID = "";
    public static String VAT_AMNT_DC = "";
    public static int fromDC = 0;

    Context mContext;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext=context;
    }

    public DeliveryFragement() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeliveryFragement.
     */
    // TODO: Rename and change types and number of parameters
    public static DeliveryFragement newInstance(String param1, String param2) {
        DeliveryFragement fragment = new DeliveryFragement();
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
        View view =  inflater.inflate(R.layout.fragment_delivery_fragement, container, false);

        beginDate = view.findViewById(R.id.begin_date_delivery_challan);
        endDate = view.findViewById(R.id.end_date_delivery_challan);
        daterange = view.findViewById(R.id.date_range_msg_delivery_challan);
        clientSpinner = view.findViewById(R.id.client_name_dc_spinner);
        itemView = view.findViewById(R.id.delivery_challan_report_view);
        noOrderMsg = view.findViewById(R.id.no_delivery_challan_msg);
        totalInvoiceAmnt = view.findViewById(R.id.total_invoice_amnt);
        totalVatAmntInvoice = view.findViewById(R.id.total_vat_amount_invoice);
        totalDeliveryChallanAmount = view.findViewById(R.id.total_delivery_challan_amount);

        barChart = view.findViewById(R.id.delivery_challan_category_overview);

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

        // Selecting Sub Category
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

                    new CheckReport().execute();

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
        total_delv_chal_amnt = 0.0;
        inv_amnt = 0.0;
        vat_amnt_inv = 0.0;
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

        INV_NO = deliveryChallanLists.get(CategoryPosition).getDelivery_no();
        INV_DATE = deliveryChallanLists.get(CategoryPosition).getDelivery_date();
        SO_NO = deliveryChallanLists.get(CategoryPosition).getOrder_no();
        SO_DATE = deliveryChallanLists.get(CategoryPosition).getOrder_date();
        C_NAME = deliveryChallanLists.get(CategoryPosition).getClient_name();
        C_CODE = deliveryChallanLists.get(CategoryPosition).getAd_code();
        ADDS = deliveryChallanLists.get(CategoryPosition).getTarget_address();
        EDD = deliveryChallanLists.get(CategoryPosition).getEdd();
        C_EMAIL = deliveryChallanLists.get(CategoryPosition).getContact_email();
        PERSON = deliveryChallanLists.get(CategoryPosition).getContact_person();
        CONTACT = deliveryChallanLists.get(CategoryPosition).getContact_number();
        SM_ID = deliveryChallanLists.get(CategoryPosition).getSm_id();
        VAT_AMNT_DC = deliveryChallanLists.get(CategoryPosition).getVat_amnt();
        fromDC = 1;

        DeliveryChallanDetails deliveryChallanDetails = new DeliveryChallanDetails();
        deliveryChallanDetails.show(getActivity().getSupportFragmentManager(),"DCD");
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

                clientSpinner.setText("");
                sortedClientLists = new ArrayList<>();
                boolean adFound = false;
                sortedClientLists.add(new ReceiveTypeList("","All Clients"));
                for (int i = 0; i < clientLists.size(); i++) {
                    String ad_id = clientLists.get(i).getId();
                    for (int j = 0 ; j < deliveryChallanLists.size(); j++) {
                        if (ad_id.equals(deliveryChallanLists.get(j).getAd_id())) {
                            adFound = false;
                            if (sortedClientLists.size() != 0) {
                                for (int k = 0; k < sortedClientLists.size(); k++) {
                                    if (ad_id.equals(sortedClientLists.get(k).getId())) {
                                        adFound = true;
                                    }
                                }
                            }
                            if (!adFound) {

                                sortedClientLists.add(new ReceiveTypeList(deliveryChallanLists.get(j).getAd_id(),deliveryChallanLists.get(j).getClient_name()));
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
//                for (int i = 0; i < deliveryChallanLists.size(); i++) {
//                    ad_id = deliveryChallanLists.get(i).getAd_id();
//                    first = true;
//                    for (int j = 0 ; j < deliveryChallanLists.size(); j++) {
//
//                        if (ad_id.equals(deliveryChallanLists.get(j).getAd_id())) {
//                            if (first) {
//                                first = false;
//                            }
//                            else {
//                                deliveryChallanLists.get(j).setClient_name("||");
//                            }
//                        }
//                    }
//                }
                if (deliveryChallanLists.size() ==0) {
                    noOrderMsg.setVisibility(View.VISIBLE);
                }else {
                    noOrderMsg.setVisibility(View.GONE);
                }

                DecimalFormat formatter = new DecimalFormat("##,##,##,###");
                String formatted = formatter.format(inv_amnt);

                totalInvoiceAmnt.setText(formatted);

                formatted = formatter.format(vat_amnt_inv);
                totalVatAmntInvoice.setText(formatted);


                deliveryChallanAdapter = new DeliveryChallanAdapter(deliveryChallanLists, getContext(),DeliveryFragement.this);
                itemView.setAdapter(deliveryChallanAdapter);

                for (int i = 0; i < categoryValues.size(); i++) {
                    if (categoryValues.get(i).getValue() != null) {
                        total_delv_chal_amnt = total_delv_chal_amnt + Double.parseDouble(categoryValues.get(i).getValue());
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
                int cc = Color.parseColor("#CC66BE");
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
                formatted = formatter.format(total_delv_chal_amnt);
                totalDeliveryChallanAmount.setText(formatted);

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

    public class CheckReport extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                OnlyReportData();
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

                if (deliveryChallanLists.size() ==0) {
                    noOrderMsg.setVisibility(View.VISIBLE);
                }else {
                    noOrderMsg.setVisibility(View.GONE);
                }

                DecimalFormat formatter = new DecimalFormat("##,##,##,###");
                String formatted = formatter.format(inv_amnt);

                totalInvoiceAmnt.setText(formatted);

                formatted = formatter.format(vat_amnt_inv);
                totalVatAmntInvoice.setText(formatted);

                deliveryChallanAdapter = new DeliveryChallanAdapter(deliveryChallanLists, getContext(),DeliveryFragement.this);
                itemView.setAdapter(deliveryChallanAdapter);


                for (int i = 0; i < categoryValues.size(); i++) {
                    if (categoryValues.get(i).getValue() != null) {
                        total_delv_chal_amnt = total_delv_chal_amnt + Double.parseDouble(categoryValues.get(i).getValue());
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
                int cc = Color.parseColor("#CC66BE");
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
                formatted = formatter.format(total_delv_chal_amnt);
                totalDeliveryChallanAmount.setText(formatted);


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

                        new CheckReport().execute();
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

            clientLists = new ArrayList<>();
            deliveryChallanLists = new ArrayList<>();
            clientId = "";
            declareValue();
            total_delv_chal_amnt = 0.0;

            categoryValues = new ArrayList<>();
            barItem = new ArrayList<>();
            barValue = new ArrayList<>();
            barEntries = new ArrayList<>();

            Statement stmt = connection.createStatement();
            if (firstDate.isEmpty()) {
                firstDate = null;
            }
            if (lastDate.isEmpty()) {
                lastDate = null;
            }
            if (clientId != null) {
                if (clientId.isEmpty()) {
                    clientId = null;
                }
            }


            ResultSet resultSet = stmt.executeQuery("select ad_id, ad_name from acc_dtl where ad_flag = 6");

            while (resultSet.next()) {
                clientLists.add(new ReceiveTypeList(resultSet.getString(1),resultSet.getString(2)));
            }

            CallableStatement callableStatement = connection.prepareCall("begin ? := GET_INVOICE_REGISTER_LIST(?,?,?); end;");
            callableStatement.setString(2,firstDate);
            callableStatement.setString(3,lastDate);
            callableStatement.setString(4,clientId);
            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement.execute();

            ResultSet resultSet1 = (ResultSet) callableStatement.getObject(1);



            while (resultSet1.next()) {
                deliveryChallanLists.add(new DeliveryChallanList(resultSet1.getString(1), resultSet1.getString(2),resultSet1.getString(3),
                        resultSet1.getString(4), resultSet1.getString(5),resultSet1.getString(6),
                        resultSet1.getString(7),resultSet1.getString(8), resultSet1.getString(9),
                        resultSet1.getString(10),resultSet1.getString(11),resultSet1.getString(12),
                        resultSet1.getString(13),resultSet1.getString(14),resultSet1.getString(15),
                        resultSet1.getString(16)));
            }
            callableStatement.close();


            CallableStatement callableStatement1 = connection.prepareCall("begin ? := GET_CATEGORY_WISE_SALES_INV(?,?,?); end;");
            callableStatement1.setString(4,clientId);
            callableStatement1.setString(2,firstDate);
            callableStatement1.setString(3,lastDate);
            callableStatement1.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement1.execute();

            ResultSet resultSet2 = (ResultSet) callableStatement1.getObject(1);

            while (resultSet2.next()) {
                categoryValues.add(new ChartValue(resultSet2.getString(1),resultSet2.getString(2),resultSet2.getString(3)));
            }


            callableStatement1.close();

            if (firstDate == null) {
                firstDate = "";
            }
            if (lastDate == null) {
                lastDate = "";
            }
            if (clientId == null) {
                clientId = "";
            }

            for (int i = 0; i < deliveryChallanLists.size(); i++) {
                if (deliveryChallanLists.get(i).getInvoice_amnt() != null) {
                    inv_amnt = inv_amnt + Double.parseDouble(deliveryChallanLists.get(i).getInvoice_amnt());
                }
                if (deliveryChallanLists.get(i).getVat_amnt() != null) {
                    vat_amnt_inv = vat_amnt_inv + Double.parseDouble(deliveryChallanLists.get(i).getVat_amnt());
                }
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

    public void OnlyReportData() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            declareValue();
            deliveryChallanLists = new ArrayList<>();

            total_delv_chal_amnt = 0.0;

            categoryValues = new ArrayList<>();
            barItem = new ArrayList<>();
            barValue = new ArrayList<>();
            barEntries = new ArrayList<>();

            if (firstDate.isEmpty()) {
                firstDate = null;
            }
            if (lastDate.isEmpty()) {
                lastDate = null;
            }
            if (clientId != null) {
                if (clientId.isEmpty()) {
                    clientId = null;
                }
            }


            CallableStatement callableStatement = connection.prepareCall("begin ? := GET_INVOICE_REGISTER_LIST(?,?,?); end;");
            callableStatement.setString(2,firstDate);
            callableStatement.setString(3,lastDate);
            callableStatement.setString(4,clientId);
            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement.execute();

            ResultSet resultSet1 = (ResultSet) callableStatement.getObject(1);

            while (resultSet1.next()) {
                deliveryChallanLists.add(new DeliveryChallanList(resultSet1.getString(1), resultSet1.getString(2),resultSet1.getString(3),
                        resultSet1.getString(4), resultSet1.getString(5),resultSet1.getString(6),
                        resultSet1.getString(7),resultSet1.getString(8), resultSet1.getString(9),
                        resultSet1.getString(10),resultSet1.getString(11),resultSet1.getString(12),
                        resultSet1.getString(13),resultSet1.getString(14),resultSet1.getString(15),
                        resultSet1.getString(16)));
            }
            callableStatement.close();

            CallableStatement callableStatement1 = connection.prepareCall("begin ? := GET_CATEGORY_WISE_SALES_INV(?,?,?); end;");
            callableStatement1.setString(4,clientId);
            callableStatement1.setString(2,firstDate);
            callableStatement1.setString(3,lastDate);
            callableStatement1.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement1.execute();

            ResultSet resultSet2 = (ResultSet) callableStatement1.getObject(1);

            while (resultSet2.next()) {
                categoryValues.add(new ChartValue(resultSet2.getString(1),resultSet2.getString(2),resultSet2.getString(3)));
            }


            callableStatement1.close();

            if (firstDate == null) {
                firstDate = "";
            }
            if (lastDate == null) {
                lastDate = "";
            }
            if (clientId == null) {
                clientId = "";
            }

            for (int i = 0; i < deliveryChallanLists.size(); i++) {
                if (deliveryChallanLists.get(i).getInvoice_amnt() != null) {
                    inv_amnt = inv_amnt + Double.parseDouble(deliveryChallanLists.get(i).getInvoice_amnt());
                }
                if (deliveryChallanLists.get(i).getVat_amnt() != null) {
                    vat_amnt_inv = vat_amnt_inv + Double.parseDouble(deliveryChallanLists.get(i).getVat_amnt());
                }
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
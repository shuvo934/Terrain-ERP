package ttit.com.shuvo.terrainerp.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.dewinjm.monthyearpicker.MonthFormat;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.WaitProgress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerformanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerformanceFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Context mContext;
    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
//    private Connection connection;

    TextView currentMonth;
    TextView vsMonth;

    TextView workOrderCurrent;
    TextView workOrderPercentage;
    TextView recvCurrent;
    TextView recvPercentage;
    TextView billRecvCurrent;
    TextView billRecvPercentage;
    TextView billPaidCurrent;
    TextView billPaidPercentage;

    TextView salesOrderCurrent;
    TextView salesOrderPercentage;
    TextView deliveryCurrent;
    TextView deliveryPercentage;
    TextView collectionCurrent;
    TextView collectionPercentage;

    String firstDate = "";
    String lastDate = "";

    String lastMfirstDate = "";
    String lastMlastdate = "";

    double workOrderTotal = 0.0;
    double workOrderTotalPrv = 0.0;
    double receiveTotal = 0.0;
    double receiveTotalPrv = 0.0;
    double billRcvSTotal = 0.0;
    double billRcvTotalPrv = 0.0;
    double billPaid = 0.0;
    double billpaidPrv = 0.0;

    double salesOrderTotal = 0.0;
    double salesOrderPrv = 0.0;
    double deliveryTotal = 0.0;
    double deliveryPrv = 0.0;
    double collection = 0.0;
    double collectionPrv = 0.0;


    public PerformanceFragment() {
        // Required empty public constructor
    }

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
     * @return A new instance of fragment PerformanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerformanceFragment newInstance(String param1, String param2) {
        PerformanceFragment fragment = new PerformanceFragment();
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
        View view = inflater.inflate(R.layout.fragment_performance, container, false);

        currentMonth = view.findViewById(R.id.current_month_perf_report);
        vsMonth = view.findViewById(R.id.vs_month_perf_report);

        workOrderCurrent = view.findViewById(R.id.work_order_total_performance);
        workOrderPercentage = view.findViewById(R.id.work_order_total_percentage_prv);
        recvCurrent = view.findViewById(R.id.receive_total_performance);
        recvPercentage = view.findViewById(R.id.receive_total_percentage_prv);
        billRecvCurrent = view.findViewById(R.id.bill_receive_total_performance);
        billRecvPercentage = view.findViewById(R.id.bill_receive_total_percentage_prv);
        billPaidCurrent = view.findViewById(R.id.bill_paid_total_performance);
        billPaidPercentage = view.findViewById(R.id.bill_paid_total_percentage_prv);

        salesOrderCurrent = view.findViewById(R.id.sales_order_total_performance);
        salesOrderPercentage = view.findViewById(R.id.sales_order_total_percentage_prv);
        deliveryCurrent = view.findViewById(R.id.delivery_total_performance);
        deliveryPercentage = view.findViewById(R.id.delivery_total_percentage_prv);
        collectionCurrent = view.findViewById(R.id.bill_collection_total_performance);
        collectionPercentage = view.findViewById(R.id.bill_collection_total_percentage_prv);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MMM, yyyy", Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat("MMM-yy", Locale.getDefault());

        String month = df.format(c);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-1);

        Date d = calendar.getTime();

        String vsmonth = df.format(d);

        currentMonth.setText(month);
        vsMonth.setText("(vs "+vsmonth+")");

        //
        String date1 = sdf.format(c);

        String prM = sdf.format(d);

        firstDate = "01-"+date1;
        lastMfirstDate = "01-"+prM;

        lastDate = gettingLastDate(firstDate);
        lastMlastdate = gettingLastDate(lastMfirstDate);

        System.out.println(firstDate);
        System.out.println(lastDate);

        System.out.println(lastMfirstDate);
        System.out.println(lastMlastdate);


        currentMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date c = Calendar.getInstance().getTime();

                String formattedYear = "";
                String monthValue = "";
                String lastformattedYear = "";
                String lastdateView = "";

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM",Locale.getDefault());

                formattedYear = simpleDateFormat.format(c);
                monthValue = simpleDateFormat1.format(c);
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
                        lastDate = gettingLastDate(firstDate);
                        System.out.println(firstDate);
                        currentMonth.setText(mon + ", " + year);

                        Calendar calendar2 = Calendar.getInstance();

                        calendar2.set(Calendar.MONTH, monthOfYear);
                        calendar2.set(Calendar.YEAR,year);

                        calendar2.add(Calendar.MONTH,-1);

                        Date d = calendar2.getTime();

                        SimpleDateFormat df = new SimpleDateFormat("MMM, yyyy", Locale.getDefault());
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM-yy", Locale.getDefault());

                        String prmonthText = df.format(d);
                        vsMonth.setText("(vs "+prmonthText+")");

                        String prMonth = sdf.format(d);
                        lastMfirstDate = "01-"+prMonth;
                        lastMlastdate = gettingLastDate(lastMfirstDate);

//                        new Check().execute();
                        getSummaryData();

                    }
                });

            }
        });

//        new Check().execute();
        getSummaryData();

        return  view;
    }

    private String gettingLastDate(String fdate) {
        SimpleDateFormat sss = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());

        String lDate = "";

        Date today = null;
        try {
            today = sss.parse(fdate);
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

            SimpleDateFormat sdff = new SimpleDateFormat("dd-MMM-yy",Locale.getDefault());
            lDate = sdff.format(lastDayOfMonth);

        }

        return lDate;
    }

//    public boolean isConnected () {
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
//    public boolean isOnline () {
//
//        Runtime runtime = Runtime.getRuntime();
//        try {
//            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
//            int exitValue = ipProcess.waitFor();
//            return (exitValue == 0);
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }

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
//                SummaryData();
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
//                DecimalFormat formatter = new DecimalFormat("###,##,##,###");
//                DecimalFormat formatterPer = new DecimalFormat("###,##,##,###.#");
//                String formatted = formatter.format(workOrderTotal);
//                workOrderCurrent.setText(formatted + " BDT");
//
//                double dividend = workOrderTotal - workOrderTotalPrv;
//                double result = 0.0;
//
//                if (workOrderTotal == 0.0 && workOrderTotalPrv == 0.0) {
//                    result = 0.0;
//                }
//                else if (workOrderTotal == 0.0 && workOrderTotalPrv != 0.0) {
//                    result = -100.0;
//                }
//                else {
//                    result = (dividend / workOrderTotalPrv) * 100;
//                }
//
//
//                System.out.println("RESSSS: "+dividend);
//
//                formatted = formatterPer.format(result);
//                if (formatted.contains("-")) {
//                    workOrderPercentage.setTextColor(Color.parseColor("#ffff4444"));
//                    workOrderPercentage.setText("("+formatted+"%)");
//                } else if (formatted.equals("0")) {
//                    workOrderPercentage.setTextColor(Color.parseColor("#FF000000"));
//                    workOrderPercentage.setText("("+formatted+"%)");
//                } else {
//                    workOrderPercentage.setTextColor(Color.parseColor("#FF018786"));
//                    workOrderPercentage.setText("(+"+formatted+"%)");
//                }
//
//                //
//                formatted = formatter.format(receiveTotal);
//                recvCurrent.setText(formatted + " BDT");
//
//                dividend = receiveTotal - receiveTotalPrv;
//                if (receiveTotal == 0.0 && receiveTotalPrv == 0.0) {
//                    result = 0.0;
//                }
//                else if (receiveTotal == 0.0 && receiveTotalPrv != 0.0) {
//                    result = -100.0;
//                }
//                else {
//                    result = (dividend / receiveTotalPrv) * 100;
//                }
//
//
//                formatted = formatterPer.format(result);
//                if (formatted.contains("-")) {
//                    recvPercentage.setTextColor(Color.parseColor("#ffff4444"));
//                    recvPercentage.setText("("+formatted+"%)");
//                } else if (formatted.equals("0")) {
//                    recvPercentage.setTextColor(Color.parseColor("#FF000000"));
//                    recvPercentage.setText("("+formatted+"%)");
//                } else {
//                    recvPercentage.setTextColor(Color.parseColor("#FF018786"));
//                    recvPercentage.setText("(+"+formatted+"%)");
//                }
//
//                //
//                formatted = formatter.format(salesOrderTotal);
//                salesOrderCurrent.setText(formatted + " BDT");
//
//                dividend = salesOrderTotal - salesOrderPrv;
//                if (salesOrderTotal == 0.0 && salesOrderPrv == 0.0) {
//                    result = 0.0;
//                }
//                else if (salesOrderTotal == 0.0 && salesOrderPrv != 0.0) {
//                    result = -100.0;
//                }
//                else {
//                    result = (dividend / salesOrderPrv) * 100;
//                }
//
//
//                formatted = formatterPer.format(result);
//                if (formatted.contains("-")) {
//                    salesOrderPercentage.setTextColor(Color.parseColor("#ffff4444"));
//                    salesOrderPercentage.setText("("+formatted+"%)");
//                } else if (formatted.equals("0")) {
//                    salesOrderPercentage.setTextColor(Color.parseColor("#FF000000"));
//                    salesOrderPercentage.setText("("+formatted+"%)");
//                } else {
//                    salesOrderPercentage.setTextColor(Color.parseColor("#FF018786"));
//                    salesOrderPercentage.setText("(+"+formatted+"%)");
//                }
//
//                //
//                formatted = formatter.format(deliveryTotal);
//                deliveryCurrent.setText(formatted + " BDT");
//
//                dividend = deliveryTotal - deliveryPrv;
//
//                if (deliveryTotal == 0.0 && deliveryPrv == 0.0) {
//                    result = 0.0;
//                }
//                else if (deliveryTotal == 0.0 && deliveryPrv != 0.0) {
//                    result = -100.0;
//                }
//                else {
//                    result = (dividend / deliveryPrv) * 100;
//                }
//
//
//                formatted = formatterPer.format(result);
//                if (formatted.contains("-")) {
//                    deliveryPercentage.setTextColor(Color.parseColor("#ffff4444"));
//                    deliveryPercentage.setText("("+formatted+"%)");
//                } else if (formatted.equals("0")) {
//                    deliveryPercentage.setTextColor(Color.parseColor("#FF000000"));
//                    deliveryPercentage.setText("("+formatted+"%)");
//                } else {
//                    deliveryPercentage.setTextColor(Color.parseColor("#FF018786"));
//                    deliveryPercentage.setText("(+"+formatted+"%)");
//                }
//
//                //
//                formatted = formatter.format(billPaid);
//                billPaidCurrent.setText(formatted + " BDT");
//
//                dividend = billPaid - billpaidPrv;
//
//                if (billPaid == 0.0 && billpaidPrv == 0.0) {
//                    result = 0.0;
//                }
//                else if (billPaid == 0.0 && billpaidPrv != 0.0) {
//                    result = -100.0;
//                }
//                else {
//                    result = (dividend / billpaidPrv) * 100;
//                }
//
//
//                formatted = formatterPer.format(result);
//                if (formatted.contains("-")) {
//                    billPaidPercentage.setTextColor(Color.parseColor("#ffff4444"));
//                    billPaidPercentage.setText("("+formatted+"%)");
//                } else if (formatted.equals("0")) {
//                    billPaidPercentage.setTextColor(Color.parseColor("#FF000000"));
//                    billPaidPercentage.setText("("+formatted+"%)");
//                } else {
//                    billPaidPercentage.setTextColor(Color.parseColor("#FF018786"));
//                    billPaidPercentage.setText("(+"+formatted+"%)");
//                }
//
//                //
//                formatted = formatter.format(billRcvSTotal);
//                billRecvCurrent.setText(formatted + " BDT");
//
//                dividend = billRcvSTotal - billRcvTotalPrv;
//
//                if (billRcvSTotal == 0.0 && billRcvTotalPrv == 0.0) {
//                    result = 0.0;
//                }
//                else if (billRcvSTotal == 0.0 && billRcvTotalPrv != 0.0) {
//                    result = -100.0;
//                }
//                else {
//                    result = (dividend / billRcvTotalPrv) * 100;
//                }
//
//
//                formatted = formatterPer.format(result);
//                if (formatted.contains("-")) {
//                    billRecvPercentage.setTextColor(Color.parseColor("#ffff4444"));
//                    billRecvPercentage.setText("("+formatted+"%)");
//                } else if (formatted.equals("0")) {
//                    billRecvPercentage.setTextColor(Color.parseColor("#FF000000"));
//                    billRecvPercentage.setText("("+formatted+"%)");
//                } else {
//                    billRecvPercentage.setTextColor(Color.parseColor("#FF018786"));
//                    billRecvPercentage.setText("(+"+formatted+"%)");
//                }
//
//                //
//                formatted = formatter.format(collection);
//                collectionCurrent.setText(formatted + " BDT");
//
//                dividend = collection - collectionPrv;
//
//                if (collection == 0.0 && collectionPrv == 0.0) {
//                    result = 0.0;
//                }
//                else if (collection == 0.0 && collectionPrv != 0.0) {
//                    result = -100.0;
//                }
//                else {
//                    result = (dividend / collectionPrv) * 100;
//                }
//
//
//                formatted = formatterPer.format(result);
//                if (formatted.contains("-")) {
//                    collectionPercentage.setTextColor(Color.parseColor("#ffff4444"));
//                    collectionPercentage.setText("("+formatted+"%)");
//                } else if (formatted.equals("0")) {
//                    collectionPercentage.setTextColor(Color.parseColor("#FF000000"));
//                    collectionPercentage.setText("("+formatted+"%)");
//                } else {
//                    collectionPercentage.setTextColor(Color.parseColor("#FF018786"));
//                    collectionPercentage.setText("(+"+formatted+"%)");
//                }
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

//    public void SummaryData () {
//
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//            Statement stmt = connection.createStatement();
//
////             workOrderTotal = 0.0;
////             workOrderTotalPrv = 0.0;
////             receiveTotal = 0.0;
////             receiveTotalPrv = 0.0;
////             billRcvSTotal = 0.0;
////             billRcvTotalPrv = 0.0;
////             billPaid = 0.0;
////             billpaidPrv = 0.0;
////
////             salesOrderTotal = 0.0;
////             salesOrderPrv = 0.0;
////             deliveryTotal = 0.0;
////             deliveryPrv = 0.0;
////             collection = 0.0;
////             collectionPrv = 0.0;
//
//
//            if (firstDate.isEmpty()) {
//                firstDate = null;
//            }
//            if (lastDate.isEmpty()) {
//                lastDate = null;
//            }
//
////            ResultSet resultSet = stmt.executeQuery("WITH JOB\n" +
////                    "     AS (  SELECT J.WOJ_IM_ID, NVL (SUM (J.WOJ_JOB_AMT), 0) TOTAL_WOJ_JOB_AMT\n" +
////                    "             FROM WORK_ORDER_MST M, WORK_ORDER_JOB J, ACC_DTL A\n" +
////                    "            WHERE     M.WOM_ID = J.WOJ_WOM_ID\n" +
////                    "                   AND m.p_disapproved_flag <> 2\n"+
////                    "                  AND M.WOM_AD_ID = A.AD_ID\n" +
////                    "                  AND (   TRUNC(M.WOM_DATE) BETWEEN case when TRUNC(TO_DATE('"+firstDate+"')) is null then TRUNC(M.WOM_DATE) else TRUNC(TO_DATE('"+firstDate+"')) end AND case when TRUNC(TO_DATE('"+lastDate+"')) is null then TRUNC(M.WOM_DATE) else TRUNC(TO_DATE('"+lastDate+"')) end )\n" +
////                    "                  AND (M.WOM_AD_ID = null OR null IS NULL)\n" +
////                    "                  AND (   M.P_FINAL_FLAG = null\n" +
////                    "                       OR null IS NULL)\n" +
////                    "         GROUP BY J.WOJ_IM_ID)\n" +
////                    "SELECT ITEM.IM_ID,ITEM.IM_NAME category_name, NVL (JOB.TOTAL_WOJ_JOB_AMT, 0) TOTAL_JOB_AMT\n" +
////                    "  FROM JOB, ITEM_MST ITEM\n" +
////                    " WHERE JOB.WOJ_IM_ID(+) = ITEM.IM_ID\n" +
////                    " ORDER BY ITEM.IM_ID asc");
////
////            while (resultSet.next()) {
////                if (resultSet.getString(3) != null) {
////                    if (!resultSet.getString(3).isEmpty()) {
////                        workOrderTotal = workOrderTotal + resultSet.getDouble(3);
////                    }
////                }
////            }
////
////            resultSet.close();
//
////            ResultSet resultSetPrv = stmt.executeQuery("WITH JOB\n" +
////                    "     AS (  SELECT J.WOJ_IM_ID, NVL (SUM (J.WOJ_JOB_AMT), 0) TOTAL_WOJ_JOB_AMT\n" +
////                    "             FROM WORK_ORDER_MST M, WORK_ORDER_JOB J, ACC_DTL A\n" +
////                    "            WHERE     M.WOM_ID = J.WOJ_WOM_ID\n" +
////                    "                   AND m.p_disapproved_flag <> 2\n"+
////                    "                  AND M.WOM_AD_ID = A.AD_ID\n" +
////                    "                  AND (   TRUNC(M.WOM_DATE) BETWEEN case when TRUNC(TO_DATE('"+lastMfirstDate+"')) is null then TRUNC(M.WOM_DATE) else TRUNC(TO_DATE('"+lastMfirstDate+"')) end AND case when TRUNC(TO_DATE('"+lastMlastdate+"')) is null then TRUNC(M.WOM_DATE) else TRUNC(TO_DATE('"+lastMlastdate+"')) end )\n" +
////                    "                  AND (M.WOM_AD_ID = null OR null IS NULL)\n" +
////                    "                  AND (   M.P_FINAL_FLAG = null\n" +
////                    "                       OR null IS NULL)\n" +
////                    "         GROUP BY J.WOJ_IM_ID)\n" +
////                    "SELECT ITEM.IM_ID,ITEM.IM_NAME category_name, NVL (JOB.TOTAL_WOJ_JOB_AMT, 0) TOTAL_JOB_AMT\n" +
////                    "  FROM JOB, ITEM_MST ITEM\n" +
////                    " WHERE JOB.WOJ_IM_ID(+) = ITEM.IM_ID\n" +
////                    " ORDER BY ITEM.IM_ID asc");
////
////            while (resultSetPrv.next()) {
////                if (resultSetPrv.getString(3) != null) {
////                    if (!resultSetPrv.getString(3).isEmpty()) {
////                        workOrderTotalPrv = workOrderTotalPrv + resultSetPrv.getDouble(3);
////                    }
////                }
////            }
////
////            resultSetPrv.close();
//
////            CallableStatement callableStatement = connection.prepareCall("begin ? := GET_CATEGORY_WISE_RCV_AMT(?,?,?,?); end;");
////            callableStatement.setString(2,firstDate);
////            callableStatement.setString(3,lastDate);
////            callableStatement.setString(4,null);
////            callableStatement.setString(5,null);
////            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
////            callableStatement.execute();
////
////            ResultSet resultSet1 = (ResultSet) callableStatement.getObject(1);
////
////            while (resultSet1.next()) {
////                if (resultSet1.getString(3) != null) {
////                    if (!resultSet1.getString(3).isEmpty()) {
////                        receiveTotal = receiveTotal + resultSet1.getDouble(3);
////                    }
////                }
////            }
////
////            resultSet1.close();
////
////            callableStatement.close();
//
////            CallableStatement callableStatementPrv = connection.prepareCall("begin ? := GET_CATEGORY_WISE_RCV_AMT(?,?,?,?); end;");
////            callableStatementPrv.setString(2,lastMfirstDate);
////            callableStatementPrv.setString(3,lastMlastdate);
////            callableStatementPrv.setString(4,null);
////            callableStatementPrv.setString(5,null);
////            callableStatementPrv.registerOutParameter(1, OracleTypes.CURSOR);
////            callableStatementPrv.execute();
////
////            ResultSet resultSetPrv1 = (ResultSet) callableStatementPrv.getObject(1);
////
////            while (resultSetPrv1.next()) {
////                if (resultSetPrv1.getString(3) != null) {
////                    if (!resultSetPrv1.getString(3).isEmpty()) {
////                        receiveTotalPrv = receiveTotalPrv + resultSetPrv1.getDouble(3);
////                    }
////                }
////            }
////
////            resultSetPrv1.close();
////
////            callableStatementPrv.close();
//
////            CallableStatement callableStatement1 = connection.prepareCall("begin ? := GET_CATEGORY_WISE_ORDER(?,?,?); end;");
////            callableStatement1.setString(2,null);
////            callableStatement1.setString(3,firstDate);
////            callableStatement1.setString(4,lastDate);
////            callableStatement1.registerOutParameter(1, OracleTypes.CURSOR);
////            callableStatement1.execute();
////
////            ResultSet resultSet2 = (ResultSet) callableStatement1.getObject(1);
////
////            while (resultSet2.next()) {
////
////                if (resultSet2.getString(3) != null) {
////                    if (!resultSet2.getString(3).isEmpty()) {
////                        salesOrderTotal = salesOrderTotal + resultSet2.getDouble(3);
////                    }
////                }
////            }
////
////            resultSet2.close();
////
////            callableStatement1.close();
//
////            CallableStatement callableStatementPrv1 = connection.prepareCall("begin ? := GET_CATEGORY_WISE_ORDER(?,?,?); end;");
////            callableStatementPrv1.setString(2,null);
////            callableStatementPrv1.setString(3,lastMfirstDate);
////            callableStatementPrv1.setString(4,lastMlastdate);
////            callableStatementPrv1.registerOutParameter(1, OracleTypes.CURSOR);
////            callableStatementPrv1.execute();
////
////            ResultSet resultSetPrv2 = (ResultSet) callableStatementPrv1.getObject(1);
////
////            while (resultSetPrv2.next()) {
////
////                if (resultSetPrv2.getString(3) != null) {
////                    if (!resultSetPrv2.getString(3).isEmpty()) {
////                        salesOrderPrv = salesOrderPrv + resultSetPrv2.getDouble(3);
////                    }
////                }
////            }
////
////            resultSetPrv2.close();
////
////            callableStatementPrv1.close();
//
////            CallableStatement callableStatement2 = connection.prepareCall("begin ? := GET_CATEGORY_WISE_SALES_INV(?,?,?); end;");
////            callableStatement2.setString(4,null);
////            callableStatement2.setString(2,firstDate);
////            callableStatement2.setString(3,lastDate);
////            callableStatement2.registerOutParameter(1, OracleTypes.CURSOR);
////            callableStatement2.execute();
////
////            ResultSet resultSet3 = (ResultSet) callableStatement2.getObject(1);
////
////            while (resultSet3.next()) {
////
////                if (resultSet3.getString(3) != null) {
////                    if (!resultSet3.getString(3).isEmpty()) {
////                        deliveryTotal = deliveryTotal + resultSet3.getDouble(3);
////                    }
////                }
////            }
////
////            resultSet3.close();
////
////            callableStatement2.close();
//
////            CallableStatement callableStatementPrv2 = connection.prepareCall("begin ? := GET_CATEGORY_WISE_SALES_INV(?,?,?); end;");
////            callableStatementPrv2.setString(4,null);
////            callableStatementPrv2.setString(2,lastMfirstDate);
////            callableStatementPrv2.setString(3,lastMlastdate);
////            callableStatementPrv2.registerOutParameter(1, OracleTypes.CURSOR);
////            callableStatementPrv2.execute();
////
////            ResultSet resultSetPrv3 = (ResultSet) callableStatementPrv2.getObject(1);
////
////            while (resultSetPrv3.next()) {
////
////                if (resultSetPrv3.getString(3) != null) {
////                    if (!resultSetPrv3.getString(3).isEmpty()) {
////                        deliveryPrv = deliveryPrv + resultSetPrv3.getDouble(3);
////                    }
////                }
////            }
////
////            resultSetPrv3.close();
////
////            callableStatementPrv2.close();
//
//
////            ResultSet resultSet4 = stmt.executeQuery("SELECT NVL (SUM (DISTINCT M.WOM_TOT_AMT), 0) TOT_WO_AMT,\n" +
////                    "                  CASE\n" +
////                    "                     WHEN M.WOM_WO_TYPE = 1\n" +
////                    "                     THEN\n" +
////                    "                        NVL (SUM (BILL.BRB_BILL_AMT), 0)\n" +
////                    "                     WHEN M.WOM_WO_TYPE = 2\n" +
////                    "                     THEN\n" +
////                    "                        NVL (SUM (BILL2.BRB_BILL_AMT), 0)\n" +
////                    "                     ELSE\n" +
////                    "                        NULL\n" +
////                    "                  END\n" +
////                    "                     BILL_AMT_WO,\n" +
////                    "                  CASE\n" +
////                    "                     WHEN M.WOM_WO_TYPE = 1 THEN NVL (SUM (BILL.RM_AMT), 0)\n" +
////                    "                     WHEN M.WOM_WO_TYPE = 2 THEN NVL (SUM (BILL2.RM_AMT), 0)\n" +
////                    "                     ELSE NULL\n" +
////                    "                  END\n" +
////                    "                     TOTAL_RM_AMT,\n" +
////                    "                  CASE\n" +
////                    "                     WHEN M.WOM_WO_TYPE = 1\n" +
////                    "                     THEN\n" +
////                    "                        NVL (SUM (BILL.BRB_PAYABLE_VAT), 0)\n" +
////                    "                     WHEN M.WOM_WO_TYPE = 2\n" +
////                    "                     THEN\n" +
////                    "                        NVL (SUM (BILL2.BRB_PAYABLE_VAT), 0)\n" +
////                    "                     ELSE\n" +
////                    "                        NULL\n" +
////                    "                  END\n" +
////                    "                     WOM_VAT_AMT_MARK,\n" +
////                    "                  CASE\n" +
////                    "                     WHEN M.WOM_WO_TYPE = 1\n" +
////                    "                     THEN\n" +
////                    "                        NVL (SUM (BILL.TOT_PAID_MARK), 0)\n" +
////                    "                     WHEN M.WOM_WO_TYPE = 2\n" +
////                    "                     THEN\n" +
////                    "                        NVL (SUM (BILL2.TOT_PAID_MARK), 0)\n" +
////                    "                     ELSE\n" +
////                    "                        NULL\n" +
////                    "                  END\n" +
////                    "                     TOT_PAID_MARK\n" +
////                    "             FROM WORK_ORDER_MST M,\n" +
////                    "                  --ACC_DTL        A,\n" +
////                    "                  (SELECT BRB_WOM_ID,\n" +
////                    "                          BRB_BILL_AMT,\n" +
////                    "                          TOT_PAID_MARK,\n" +
////                    "                          BRB_PAYABLE_VAT,\n" +
////                    "                          RM.RM_AMT RM_AMT\n" +
////                    "                     FROM (  SELECT BRB.BRB_WOM_ID,\n" +
////                    "                                    SUM (NVL (BRB.BRB_BILL_AMT, 0)) BRB_BILL_AMT,\n" +
////                    "                                    SUM (\n" +
////                    "                                       (  NVL (BRB.BRB_AC_PAID_MARK, 0)\n" +
////                    "                                        + NVL (BRB.BRB_AC_VAT_PAID_MARK, 0)))\n" +
////                    "                                       TOT_PAID_MARK,\n" +
////                    "                                    SUM (BRB.BRB_PAYABLE_VAT)\n" +
////                    "                                       BRB_PAYABLE_VAT\n" +
////                    "                               FROM BILL_RECEIVE_BILL BRB, BILL_RECEIVE_MST M\n" +
////                    "                              WHERE     M.BRM_ID = BRB.BRB_BRM_ID\n" +
////                    "                                    AND M.BRM_TYPE_FLAG IN (2, 3)\n" +
////                    "                           GROUP BY BRB.BRB_WOM_ID) BR,\n" +
////                    "                          (  SELECT RM_WOM_ID, SUM (NVL (RM_AMT, 0)) RM_AMT\n" +
////                    "                               FROM RECEIVE_MST\n" +
////                    "                              WHERE RM_WOM_ID IS NOT NULL\n" +
////                    "                           GROUP BY RM_WOM_ID) RM\n" +
////                    "                    WHERE BR.BRB_WOM_ID = RM.RM_WOM_ID(+)) BILL,\n" +
////                    "                  (SELECT BRB_WOM_ID,\n" +
////                    "                          BRB_BILL_AMT,\n" +
////                    "                          TOT_PAID_MARK,\n" +
////                    "                          BRB_PAYABLE_VAT,\n" +
////                    "                          RM.RM_AMT RM_AMT\n" +
////                    "                     FROM (  SELECT BRB.BRB_WOM_ID,\n" +
////                    "                                    SUM (NVL (BRB.BRB_BILL_AMT, 0)) BRB_BILL_AMT,\n" +
////                    "                                    SUM (\n" +
////                    "                                       (  NVL (BRB.BRB_AC_PAID_MARK, 0)\n" +
////                    "                                        + NVL (BRB.BRB_AC_VAT_PAID_MARK, 0)))\n" +
////                    "                                       TOT_PAID_MARK,\n" +
////                    "                                    SUM (BRB.BRB_PAYABLE_VAT)\n" +
////                    "                                       BRB_PAYABLE_VAT\n" +
////                    "                               FROM BILL_RECEIVE_BILL BRB, BILL_RECEIVE_MST M\n" +
////                    "                              WHERE     M.BRM_ID = BRB.BRB_BRM_ID\n" +
////                    "                                    AND M.BRM_TYPE_FLAG = 1\n" +
////                    "                           GROUP BY BRB.BRB_WOM_ID) BR,\n" +
////                    "                          (  SELECT RM_WOM_ID, SUM (NVL (RM_AMT, 0)) RM_AMT\n" +
////                    "                               FROM RECEIVE_MST\n" +
////                    "                              WHERE RM_WOM_ID IS NOT NULL\n" +
////                    "                           GROUP BY RM_WOM_ID) RM\n" +
////                    "                    WHERE BR.BRB_WOM_ID = RM.RM_WOM_ID(+)) BILL2\n" +
////                    "            WHERE M.WOM_ID = BILL.BRB_WOM_ID(+)\n" +
////                    "                  AND M.WOM_ID = BILL2.BRB_WOM_ID(+)\n" +
////                    "                  AND (TRUNC (M.WOM_DATE) BETWEEN CASE\n" +
////                    "                                                     WHEN TRUNC (TO_DATE ('"+firstDate+"', 'DD-MON-RR'))\n" +
////                    "                                                             IS NULL\n" +
////                    "                                                     THEN\n" +
////                    "                                                        TRUNC (M.WOM_DATE)\n" +
////                    "                                                     ELSE\n" +
////                    "                                                        TRUNC (TO_DATE ('"+firstDate+"', 'DD-MON-RR'))\n" +
////                    "                                                  END\n" +
////                    "                                              AND CASE\n" +
////                    "                                                     WHEN TRUNC (TO_DATE ('"+lastDate+"', 'DD-MON-RR'))\n" +
////                    "                                                             IS NULL\n" +
////                    "                                                     THEN\n" +
////                    "                                                        TRUNC (M.WOM_DATE)\n" +
////                    "                                                     ELSE\n" +
////                    "                                                        TRUNC (TO_DATE ('"+lastDate+"', 'DD-MON-RR'))\n" +
////                    "                                                  END)\n" +
////                    "                  AND (M.WOM_AD_ID = NULL OR NULL IS NULL)\n" +
////                    "                  AND M.P_FINAL_FLAG = '"+1+"'\n" +
////                    "                  --AND M.WOM_WO_TYPE = :P_WOM_WO_TYPE\n" +
////                    "         GROUP BY M.WOM_WO_TYPE");
////
////            while (resultSet4.next()) {
////                if (resultSet4.getString(5) != null) {
////                    if (!resultSet4.getString(5).isEmpty()) {
////                        billPaid = billPaid + resultSet4.getDouble(5);
////                    }
////                }
////
////                if (resultSet4.getString(2) != null) {
////                    if (!resultSet4.getString(2).isEmpty()) {
////                        billRcvSTotal = billRcvSTotal + resultSet4.getDouble(2);
////                    }
////                }
////
////            }
////
////            resultSet4.close();
//
////            ResultSet resultSet5 = stmt.executeQuery("SELECT NVL (SUM (DISTINCT M.WOM_TOT_AMT), 0) TOT_WO_AMT,\n" +
////                    "                  CASE\n" +
////                    "                     WHEN M.WOM_WO_TYPE = 1\n" +
////                    "                     THEN\n" +
////                    "                        NVL (SUM (BILL.BRB_BILL_AMT), 0)\n" +
////                    "                     WHEN M.WOM_WO_TYPE = 2\n" +
////                    "                     THEN\n" +
////                    "                        NVL (SUM (BILL2.BRB_BILL_AMT), 0)\n" +
////                    "                     ELSE\n" +
////                    "                        NULL\n" +
////                    "                  END\n" +
////                    "                     BILL_AMT_WO,\n" +
////                    "                  CASE\n" +
////                    "                     WHEN M.WOM_WO_TYPE = 1 THEN NVL (SUM (BILL.RM_AMT), 0)\n" +
////                    "                     WHEN M.WOM_WO_TYPE = 2 THEN NVL (SUM (BILL2.RM_AMT), 0)\n" +
////                    "                     ELSE NULL\n" +
////                    "                  END\n" +
////                    "                     TOTAL_RM_AMT,\n" +
////                    "                  CASE\n" +
////                    "                     WHEN M.WOM_WO_TYPE = 1\n" +
////                    "                     THEN\n" +
////                    "                        NVL (SUM (BILL.BRB_PAYABLE_VAT), 0)\n" +
////                    "                     WHEN M.WOM_WO_TYPE = 2\n" +
////                    "                     THEN\n" +
////                    "                        NVL (SUM (BILL2.BRB_PAYABLE_VAT), 0)\n" +
////                    "                     ELSE\n" +
////                    "                        NULL\n" +
////                    "                  END\n" +
////                    "                     WOM_VAT_AMT_MARK,\n" +
////                    "                  CASE\n" +
////                    "                     WHEN M.WOM_WO_TYPE = 1\n" +
////                    "                     THEN\n" +
////                    "                        NVL (SUM (BILL.TOT_PAID_MARK), 0)\n" +
////                    "                     WHEN M.WOM_WO_TYPE = 2\n" +
////                    "                     THEN\n" +
////                    "                        NVL (SUM (BILL2.TOT_PAID_MARK), 0)\n" +
////                    "                     ELSE\n" +
////                    "                        NULL\n" +
////                    "                  END\n" +
////                    "                     TOT_PAID_MARK\n" +
////                    "             FROM WORK_ORDER_MST M,\n" +
////                    "                  --ACC_DTL        A,\n" +
////                    "                  (SELECT BRB_WOM_ID,\n" +
////                    "                          BRB_BILL_AMT,\n" +
////                    "                          TOT_PAID_MARK,\n" +
////                    "                          BRB_PAYABLE_VAT,\n" +
////                    "                          RM.RM_AMT RM_AMT\n" +
////                    "                     FROM (  SELECT BRB.BRB_WOM_ID,\n" +
////                    "                                    SUM (NVL (BRB.BRB_BILL_AMT, 0)) BRB_BILL_AMT,\n" +
////                    "                                    SUM (\n" +
////                    "                                       (  NVL (BRB.BRB_AC_PAID_MARK, 0)\n" +
////                    "                                        + NVL (BRB.BRB_AC_VAT_PAID_MARK, 0)))\n" +
////                    "                                       TOT_PAID_MARK,\n" +
////                    "                                    SUM (BRB.BRB_PAYABLE_VAT)\n" +
////                    "                                       BRB_PAYABLE_VAT\n" +
////                    "                               FROM BILL_RECEIVE_BILL BRB, BILL_RECEIVE_MST M\n" +
////                    "                              WHERE     M.BRM_ID = BRB.BRB_BRM_ID\n" +
////                    "                                    AND M.BRM_TYPE_FLAG IN (2, 3)\n" +
////                    "                           GROUP BY BRB.BRB_WOM_ID) BR,\n" +
////                    "                          (  SELECT RM_WOM_ID, SUM (NVL (RM_AMT, 0)) RM_AMT\n" +
////                    "                               FROM RECEIVE_MST\n" +
////                    "                              WHERE RM_WOM_ID IS NOT NULL\n" +
////                    "                           GROUP BY RM_WOM_ID) RM\n" +
////                    "                    WHERE BR.BRB_WOM_ID = RM.RM_WOM_ID(+)) BILL,\n" +
////                    "                  (SELECT BRB_WOM_ID,\n" +
////                    "                          BRB_BILL_AMT,\n" +
////                    "                          TOT_PAID_MARK,\n" +
////                    "                          BRB_PAYABLE_VAT,\n" +
////                    "                          RM.RM_AMT RM_AMT\n" +
////                    "                     FROM (  SELECT BRB.BRB_WOM_ID,\n" +
////                    "                                    SUM (NVL (BRB.BRB_BILL_AMT, 0)) BRB_BILL_AMT,\n" +
////                    "                                    SUM (\n" +
////                    "                                       (  NVL (BRB.BRB_AC_PAID_MARK, 0)\n" +
////                    "                                        + NVL (BRB.BRB_AC_VAT_PAID_MARK, 0)))\n" +
////                    "                                       TOT_PAID_MARK,\n" +
////                    "                                    SUM (BRB.BRB_PAYABLE_VAT)\n" +
////                    "                                       BRB_PAYABLE_VAT\n" +
////                    "                               FROM BILL_RECEIVE_BILL BRB, BILL_RECEIVE_MST M\n" +
////                    "                              WHERE     M.BRM_ID = BRB.BRB_BRM_ID\n" +
////                    "                                    AND M.BRM_TYPE_FLAG = 1\n" +
////                    "                           GROUP BY BRB.BRB_WOM_ID) BR,\n" +
////                    "                          (  SELECT RM_WOM_ID, SUM (NVL (RM_AMT, 0)) RM_AMT\n" +
////                    "                               FROM RECEIVE_MST\n" +
////                    "                              WHERE RM_WOM_ID IS NOT NULL\n" +
////                    "                           GROUP BY RM_WOM_ID) RM\n" +
////                    "                    WHERE BR.BRB_WOM_ID = RM.RM_WOM_ID(+)) BILL2\n" +
////                    "            WHERE M.WOM_ID = BILL.BRB_WOM_ID(+)\n" +
////                    "                  AND M.WOM_ID = BILL2.BRB_WOM_ID(+)\n" +
////                    "                  AND (TRUNC (M.WOM_DATE) BETWEEN CASE\n" +
////                    "                                                     WHEN TRUNC (TO_DATE ('"+lastMfirstDate+"', 'DD-MON-RR'))\n" +
////                    "                                                             IS NULL\n" +
////                    "                                                     THEN\n" +
////                    "                                                        TRUNC (M.WOM_DATE)\n" +
////                    "                                                     ELSE\n" +
////                    "                                                        TRUNC (TO_DATE ('"+lastMfirstDate+"', 'DD-MON-RR'))\n" +
////                    "                                                  END\n" +
////                    "                                              AND CASE\n" +
////                    "                                                     WHEN TRUNC (TO_DATE ('"+lastMlastdate+"', 'DD-MON-RR'))\n" +
////                    "                                                             IS NULL\n" +
////                    "                                                     THEN\n" +
////                    "                                                        TRUNC (M.WOM_DATE)\n" +
////                    "                                                     ELSE\n" +
////                    "                                                        TRUNC (TO_DATE ('"+lastMlastdate+"', 'DD-MON-RR'))\n" +
////                    "                                                  END)\n" +
////                    "                  AND (M.WOM_AD_ID = NULL OR NULL IS NULL)\n" +
////                    "                  AND M.P_FINAL_FLAG = '"+1+"'\n" +
////                    "                  --AND M.WOM_WO_TYPE = :P_WOM_WO_TYPE\n" +
////                    "         GROUP BY M.WOM_WO_TYPE");
////
////            while (resultSet5.next()) {
////                if (resultSet5.getString(5) != null) {
////                    if (!resultSet5.getString(5).isEmpty()) {
////                        billpaidPrv = billpaidPrv + resultSet5.getDouble(5);
////                    }
////                }
////
////                if (resultSet5.getString(2) != null) {
////                    if (!resultSet5.getString(2).isEmpty()) {
////                        billRcvTotalPrv = billRcvTotalPrv + resultSet5.getDouble(2);
////                    }
////                }
////
////            }
////
////            resultSet5.close();
//
//
//            ResultSet resultSet6 = stmt.executeQuery("SELECT SUM (  NVL (SM_PAID_AMT, 0)\n" +
//                    "            + NVL ( (SELECT SALES_ORDER_MST.SOM_ADVANCE_AMT\n" +
//                    "                       FROM SALES_ORDER_MST\n" +
//                    "                      WHERE SALES_ORDER_MST.SOM_ID = SM_SOM_ID),\n" +
//                    "                   0))\n" +
//                    "          COLLECTION_AMT\n" +
//                    "  FROM SALES_MST\n" +
//                    " WHERE SALES_MST.SM_DATE BETWEEN TO_DATE ('"+firstDate+"', 'DD-MON-RR') AND TO_DATE ('"+lastDate+"', 'DD-MON-RR')");
//
//            while (resultSet6.next()) {
//                if (resultSet6.getString(1) != null) {
//                    if (!resultSet6.getString(1).isEmpty()) {
//                        collection = collection + resultSet6.getDouble(1);
//                    }
//                }
//            }
//
//            resultSet6.close();
//
//            ResultSet resultSet7 = stmt.executeQuery("SELECT SUM (  NVL (SM_PAID_AMT, 0)\n" +
//                    "            + NVL ( (SELECT SALES_ORDER_MST.SOM_ADVANCE_AMT\n" +
//                    "                       FROM SALES_ORDER_MST\n" +
//                    "                      WHERE SALES_ORDER_MST.SOM_ID = SM_SOM_ID),\n" +
//                    "                   0))\n" +
//                    "          COLLECTION_AMT\n" +
//                    "  FROM SALES_MST\n" +
//                    " WHERE SALES_MST.SM_DATE BETWEEN TO_DATE ('"+lastMfirstDate+"', 'DD-MON-RR') AND TO_DATE ('"+lastMlastdate+"', 'DD-MON-RR')");
//
//            while (resultSet7.next()) {
//                if (resultSet7.getString(1) != null) {
//                    if (!resultSet7.getString(1).isEmpty()) {
//                        collectionPrv = collectionPrv + resultSet7.getDouble(1);
//                    }
//                }
//            }
//
//            resultSet7.close();
//
//
//            stmt.close();
//
//            if (firstDate == null) {
//                firstDate = "";
//            }
//            if (lastDate == null) {
//                lastDate = "";
//            }
//
//            System.out.println(workOrderTotal);
//            System.out.println(workOrderTotalPrv);
//            System.out.println(receiveTotal);
//            System.out.println(receiveTotalPrv);
//            System.out.println(salesOrderTotal);
//            System.out.println(salesOrderPrv);
//            System.out.println(deliveryTotal);
//            System.out.println(deliveryPrv);
//
//
//            connected = true;
//
//            connection.close();
//
//        } catch (Exception e) {
//
//            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
//            Log.i("ERRRRR", e.getLocalizedMessage());
//            e.printStackTrace();
//        }
//
//    }

    public void getSummaryData() {
        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        workOrderTotal = 0.0;
        workOrderTotalPrv = 0.0;
        receiveTotal = 0.0;
        receiveTotalPrv = 0.0;
        billRcvSTotal = 0.0;
        billRcvTotalPrv = 0.0;
        billPaid = 0.0;
        billpaidPrv = 0.0;

        salesOrderTotal = 0.0;
        salesOrderPrv = 0.0;
        deliveryTotal = 0.0;
        deliveryPrv = 0.0;
        collection = 0.0;
        collectionPrv = 0.0;

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        String woUrl = "http://103.56.208.123:8001/apex/tterp/workOrder/getCatAmnt?st_date="+firstDate+"&end_date="+lastDate+"&ad_id=&status=";
        String woPrvUrl = "http://103.56.208.123:8001/apex/tterp/workOrder/getCatAmnt?st_date="+lastMfirstDate+"&end_date="+lastMlastdate+"&ad_id=&status=";
        String rcvUrl = "http://103.56.208.123:8001/apex/tterp/recieve/getRcvAmnt?st_date="+firstDate+"&end_date="+lastDate+"&ad_id=&status=";
        String rcvPrvUrl = "http://103.56.208.123:8001/apex/tterp/recieve/getRcvAmnt?st_date="+lastMfirstDate+"&end_date="+lastMlastdate+"&ad_id=&status=";
        String catOrderUrl = "http://103.56.208.123:8001/apex/tterp/salesOrder/getCatWiseOrder?st_date="+firstDate+"&end_date="+lastDate+"&ad_id=";
        String catPrvOrderUrl = "http://103.56.208.123:8001/apex/tterp/salesOrder/getCatWiseOrder?st_date="+lastMfirstDate+"&end_date="+lastMlastdate+"&ad_id=";
        String catWisInvUrl = "http://103.56.208.123:8001/apex/tterp/deliveryChallan/getCatWiseSaleInv?st_date="+firstDate+"&end_date="+lastDate+"&ad_id=";
        String catWisPrvInvUrl = "http://103.56.208.123:8001/apex/tterp/deliveryChallan/getCatWiseSaleInv?st_date="+lastMfirstDate+"&end_date="+lastMlastdate+"&ad_id=";
        String billPaidUrl = "http://103.56.208.123:8001/apex/tterp/performance/getBillInfo?st_date="+firstDate+"&end_date="+lastDate+"";
        String billPrvPaidUrl = "http://103.56.208.123:8001/apex/tterp/performance/getBillInfo?st_date="+lastMfirstDate+"&end_date="+lastMlastdate+"";
        String collAmntUrl = "http://103.56.208.123:8001/apex/tterp/performance/getCollectionAmnt?st_date="+firstDate+"&end_date="+lastDate+"";
        String collPrvAmntUrl = "http://103.56.208.123:8001/apex/tterp/performance/getCollectionAmnt?st_date="+lastMfirstDate+"&end_date="+lastMlastdate+"";

        StringRequest collPrvAmntReq = new StringRequest(Request.Method.GET, collPrvAmntUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        String collection_amt = info.getString("collection_amt")
                                .equals("null") ? "" : info.getString("collection_amt");

                        if (!collection_amt.isEmpty()) {
                            collectionPrv = collectionPrv + info.getDouble("collection_amt");
                        }
                    }
                }
                connected = true;
                updateFragment();
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

        StringRequest collAmntReq = new StringRequest(Request.Method.GET, collAmntUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        String collection_amt = info.getString("collection_amt")
                                .equals("null") ? "" : info.getString("collection_amt");

                        if (!collection_amt.isEmpty()) {
                            collection = collection + info.getDouble("collection_amt");
                        }
                    }
                }
                requestQueue.add(collPrvAmntReq);
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

        StringRequest billPrvPaidReq = new StringRequest(Request.Method.GET, billPrvPaidUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        String bill_amt_wo = info.getString("bill_amt_wo")
                                .equals("null") ? "" : info.getString("bill_amt_wo");
                        String tot_paid_mark = info.getString("tot_paid_mark")
                                .equals("null") ? "" : info.getString("tot_paid_mark");

                        if (!tot_paid_mark.isEmpty()) {
                            billpaidPrv = billpaidPrv + info.getDouble("tot_paid_mark");
                        }
                        if (!bill_amt_wo.isEmpty()) {
                            billRcvTotalPrv = billRcvTotalPrv + info.getDouble("bill_amt_wo");
                        }
                    }
                }
                requestQueue.add(collAmntReq);
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

        StringRequest billPaidReq = new StringRequest(Request.Method.GET, billPaidUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        String bill_amt_wo = info.getString("bill_amt_wo")
                                .equals("null") ? "" : info.getString("bill_amt_wo");
                        String tot_paid_mark = info.getString("tot_paid_mark")
                                .equals("null") ? "" : info.getString("tot_paid_mark");

                        if (!tot_paid_mark.isEmpty()) {
                            billPaid = billPaid + info.getDouble("tot_paid_mark");
                        }
                        if (!bill_amt_wo.isEmpty()) {
                            billRcvSTotal = billRcvSTotal + info.getDouble("bill_amt_wo");
                        }
                    }
                }
                requestQueue.add(billPrvPaidReq);
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

        StringRequest catInvPrvReq = new StringRequest(Request.Method.GET, catWisPrvInvUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        String cat_sales_inv = info.getString("cat_sales_inv");
                        JSONArray invoiceArray = new JSONArray(cat_sales_inv);
                        for (int j = 0; j < invoiceArray.length(); j++) {
                            JSONObject inv_info = invoiceArray.getJSONObject(j);

                            String total_sales_without_vat = inv_info.getString("total_sales_without_vat")
                                    .equals("null") ? "" : inv_info.getString("total_sales_without_vat");

                            if (!total_sales_without_vat.isEmpty()) {
                                deliveryPrv = deliveryPrv + inv_info.getDouble("total_sales_without_vat");
                            }
                        }
                    }
                }

                requestQueue.add(billPaidReq);
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

        StringRequest catInvReq = new StringRequest(Request.Method.GET, catWisInvUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        String cat_sales_inv = info.getString("cat_sales_inv");
                        JSONArray invoiceArray = new JSONArray(cat_sales_inv);
                        for (int j = 0; j < invoiceArray.length(); j++) {
                            JSONObject inv_info = invoiceArray.getJSONObject(j);

                            String total_sales_without_vat = inv_info.getString("total_sales_without_vat")
                                    .equals("null") ? "" : inv_info.getString("total_sales_without_vat");

                            if (!total_sales_without_vat.isEmpty()) {
                                deliveryTotal = deliveryTotal + inv_info.getDouble("total_sales_without_vat");
                            }
                        }
                    }
                }

                requestQueue.add(catInvPrvReq);
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

        StringRequest catPrvOrderReq = new StringRequest(Request.Method.GET, catPrvOrderUrl, response -> {
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

                            String total_order = sales_info.getString("total_order")
                                    .equals("null") ? "" : sales_info.getString("total_order");

                            if (!total_order.isEmpty()) {
                                salesOrderPrv = salesOrderPrv + sales_info.getDouble("total_order");
                            }
                        }
                    }
                }
                requestQueue.add(catInvReq);
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

                            String total_order = sales_info.getString("total_order")
                                    .equals("null") ? "" : sales_info.getString("total_order");

                            if (!total_order.isEmpty()) {
                                salesOrderTotal = salesOrderTotal + sales_info.getDouble("total_order");
                            }
                        }
                    }
                }
                requestQueue.add(catPrvOrderReq);
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

        StringRequest rcvPrvReq = new StringRequest(Request.Method.GET, rcvPrvUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        String category_wise_rcv_amt = info.getString("category_wise_rcv_amt");
                        JSONArray rcvArray = new JSONArray(category_wise_rcv_amt);
                        for (int j = 0; j < rcvArray.length(); j++) {
                            JSONObject rcv_info = rcvArray.getJSONObject(j);

                            String total_amt = rcv_info.getString("total_amt")
                                    .equals("null") ? "" : rcv_info.getString("total_amt");

                            if (!total_amt.isEmpty()) {
                                receiveTotalPrv = receiveTotalPrv + rcv_info.getDouble("total_amt");
                            }
                        }
                    }
                }
                requestQueue.add(catOrderReq);
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

        StringRequest rcvReq = new StringRequest(Request.Method.GET, rcvUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        String category_wise_rcv_amt = info.getString("category_wise_rcv_amt");
                        JSONArray rcvArray = new JSONArray(category_wise_rcv_amt);
                        for (int j = 0; j < rcvArray.length(); j++) {
                            JSONObject rcv_info = rcvArray.getJSONObject(j);

                            String total_amt = rcv_info.getString("total_amt")
                                    .equals("null") ? "" : rcv_info.getString("total_amt");

                            if (!total_amt.isEmpty()) {
                                receiveTotal = receiveTotal + rcv_info.getDouble("total_amt");
                            }
                        }
                    }
                }
                requestQueue.add(rcvPrvReq);
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

        StringRequest woPrvRequest = new StringRequest(Request.Method.GET, woPrvUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);

                        String total_job_amt = info.getString("total_job_amt")
                                .equals("null") ? "" : info.getString("total_job_amt");

                        if (!total_job_amt.isEmpty()) {
                            workOrderTotalPrv = workOrderTotalPrv + info.getDouble("total_job_amt");
                        }
                    }
                }
                requestQueue.add(rcvReq);
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

        StringRequest woRequest = new StringRequest(Request.Method.GET, woUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);

                        String total_job_amt = info.getString("total_job_amt")
                                .equals("null") ? "" : info.getString("total_job_amt");

                        if (!total_job_amt.isEmpty()) {
                            workOrderTotal = workOrderTotal + info.getDouble("total_job_amt");
                        }
                    }
                }
                requestQueue.add(woPrvRequest);
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

        requestQueue.add(woRequest);
    }

    private void updateFragment() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;


                DecimalFormat formatter = new DecimalFormat("###,##,##,###");
                DecimalFormat formatterPer = new DecimalFormat("###,##,##,###.#");
                String formatted = formatter.format(workOrderTotal);
                workOrderCurrent.setText(formatted + " BDT");

                double dividend = workOrderTotal - workOrderTotalPrv;
                double result = 0.0;

                if (workOrderTotal == 0.0 && workOrderTotalPrv == 0.0) {
                    result = 0.0;
                }
                else if (workOrderTotal == 0.0 && workOrderTotalPrv != 0.0) {
                    result = -100.0;
                }
                else {
                    result = (dividend / workOrderTotalPrv) * 100;
                }


                System.out.println("RESSSS: "+dividend);

                formatted = formatterPer.format(result);
                if (formatted.contains("-")) {
                    workOrderPercentage.setTextColor(Color.parseColor("#ffff4444"));
                    workOrderPercentage.setText("("+formatted+"%)");
                } else if (formatted.equals("0")) {
                    workOrderPercentage.setTextColor(Color.parseColor("#FF000000"));
                    workOrderPercentage.setText("("+formatted+"%)");
                } else {
                    workOrderPercentage.setTextColor(Color.parseColor("#FF018786"));
                    workOrderPercentage.setText("(+"+formatted+"%)");
                }

                //
                formatted = formatter.format(receiveTotal);
                recvCurrent.setText(formatted + " BDT");

                dividend = receiveTotal - receiveTotalPrv;
                if (receiveTotal == 0.0 && receiveTotalPrv == 0.0) {
                    result = 0.0;
                }
                else if (receiveTotal == 0.0 && receiveTotalPrv != 0.0) {
                    result = -100.0;
                }
                else {
                    result = (dividend / receiveTotalPrv) * 100;
                }


                formatted = formatterPer.format(result);
                if (formatted.contains("-")) {
                    recvPercentage.setTextColor(Color.parseColor("#ffff4444"));
                    recvPercentage.setText("("+formatted+"%)");
                } else if (formatted.equals("0")) {
                    recvPercentage.setTextColor(Color.parseColor("#FF000000"));
                    recvPercentage.setText("("+formatted+"%)");
                } else {
                    recvPercentage.setTextColor(Color.parseColor("#FF018786"));
                    recvPercentage.setText("(+"+formatted+"%)");
                }

                //
                formatted = formatter.format(salesOrderTotal);
                salesOrderCurrent.setText(formatted + " BDT");

                dividend = salesOrderTotal - salesOrderPrv;
                if (salesOrderTotal == 0.0 && salesOrderPrv == 0.0) {
                    result = 0.0;
                }
                else if (salesOrderTotal == 0.0 && salesOrderPrv != 0.0) {
                    result = -100.0;
                }
                else {
                    result = (dividend / salesOrderPrv) * 100;
                }


                formatted = formatterPer.format(result);
                if (formatted.contains("-")) {
                    salesOrderPercentage.setTextColor(Color.parseColor("#ffff4444"));
                    salesOrderPercentage.setText("("+formatted+"%)");
                } else if (formatted.equals("0")) {
                    salesOrderPercentage.setTextColor(Color.parseColor("#FF000000"));
                    salesOrderPercentage.setText("("+formatted+"%)");
                } else {
                    salesOrderPercentage.setTextColor(Color.parseColor("#FF018786"));
                    salesOrderPercentage.setText("(+"+formatted+"%)");
                }

                //
                formatted = formatter.format(deliveryTotal);
                deliveryCurrent.setText(formatted + " BDT");

                dividend = deliveryTotal - deliveryPrv;

                if (deliveryTotal == 0.0 && deliveryPrv == 0.0) {
                    result = 0.0;
                }
                else if (deliveryTotal == 0.0 && deliveryPrv != 0.0) {
                    result = -100.0;
                }
                else {
                    result = (dividend / deliveryPrv) * 100;
                }


                formatted = formatterPer.format(result);
                if (formatted.contains("-")) {
                    deliveryPercentage.setTextColor(Color.parseColor("#ffff4444"));
                    deliveryPercentage.setText("("+formatted+"%)");
                } else if (formatted.equals("0")) {
                    deliveryPercentage.setTextColor(Color.parseColor("#FF000000"));
                    deliveryPercentage.setText("("+formatted+"%)");
                } else {
                    deliveryPercentage.setTextColor(Color.parseColor("#FF018786"));
                    deliveryPercentage.setText("(+"+formatted+"%)");
                }

                //
                formatted = formatter.format(billPaid);
                billPaidCurrent.setText(formatted + " BDT");

                dividend = billPaid - billpaidPrv;

                if (billPaid == 0.0 && billpaidPrv == 0.0) {
                    result = 0.0;
                }
                else if (billPaid == 0.0 && billpaidPrv != 0.0) {
                    result = -100.0;
                }
                else {
                    result = (dividend / billpaidPrv) * 100;
                }


                formatted = formatterPer.format(result);
                if (formatted.contains("-")) {
                    billPaidPercentage.setTextColor(Color.parseColor("#ffff4444"));
                    billPaidPercentage.setText("("+formatted+"%)");
                } else if (formatted.equals("0")) {
                    billPaidPercentage.setTextColor(Color.parseColor("#FF000000"));
                    billPaidPercentage.setText("("+formatted+"%)");
                } else {
                    billPaidPercentage.setTextColor(Color.parseColor("#FF018786"));
                    billPaidPercentage.setText("(+"+formatted+"%)");
                }

                //
                formatted = formatter.format(billRcvSTotal);
                billRecvCurrent.setText(formatted + " BDT");

                dividend = billRcvSTotal - billRcvTotalPrv;

                if (billRcvSTotal == 0.0 && billRcvTotalPrv == 0.0) {
                    result = 0.0;
                }
                else if (billRcvSTotal == 0.0 && billRcvTotalPrv != 0.0) {
                    result = -100.0;
                }
                else {
                    result = (dividend / billRcvTotalPrv) * 100;
                }


                formatted = formatterPer.format(result);
                if (formatted.contains("-")) {
                    billRecvPercentage.setTextColor(Color.parseColor("#ffff4444"));
                    billRecvPercentage.setText("("+formatted+"%)");
                } else if (formatted.equals("0")) {
                    billRecvPercentage.setTextColor(Color.parseColor("#FF000000"));
                    billRecvPercentage.setText("("+formatted+"%)");
                } else {
                    billRecvPercentage.setTextColor(Color.parseColor("#FF018786"));
                    billRecvPercentage.setText("(+"+formatted+"%)");
                }

                //
                formatted = formatter.format(collection);
                collectionCurrent.setText(formatted + " BDT");

                dividend = collection - collectionPrv;

                if (collection == 0.0 && collectionPrv == 0.0) {
                    result = 0.0;
                }
                else if (collection == 0.0 && collectionPrv != 0.0) {
                    result = -100.0;
                }
                else {
                    result = (dividend / collectionPrv) * 100;
                }


                formatted = formatterPer.format(result);
                if (formatted.contains("-")) {
                    collectionPercentage.setTextColor(Color.parseColor("#ffff4444"));
                    collectionPercentage.setText("("+formatted+"%)");
                } else if (formatted.equals("0")) {
                    collectionPercentage.setTextColor(Color.parseColor("#FF000000"));
                    collectionPercentage.setText("("+formatted+"%)");
                } else {
                    collectionPercentage.setTextColor(Color.parseColor("#FF018786"));
                    collectionPercentage.setText("(+"+formatted+"%)");
                }
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

                    getSummaryData();
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

                getSummaryData();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }
}
package ttit.com.shuvo.icomerp.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

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
import ttit.com.shuvo.icomerp.adapters.ClientSDSAdapter;
import ttit.com.shuvo.icomerp.adapters.ItemRcvIssRegAdapter;
import ttit.com.shuvo.icomerp.arrayList.ClientAllSDSList;
import ttit.com.shuvo.icomerp.arrayList.ClientSDSList;
import ttit.com.shuvo.icomerp.arrayList.DeliveryAllSDSList;
import ttit.com.shuvo.icomerp.arrayList.DeliverySDSList;
import ttit.com.shuvo.icomerp.arrayList.ItemRcvIssItemDescList;
import ttit.com.shuvo.icomerp.arrayList.ItemRcvIssReglist;
import ttit.com.shuvo.icomerp.arrayList.ItemRcvIssWarehouseList;
import ttit.com.shuvo.icomerp.arrayList.OrderAllSDSList;
import ttit.com.shuvo.icomerp.arrayList.OrderSDSList;
import ttit.com.shuvo.icomerp.arrayList.ReceiveTypeList;
import ttit.com.shuvo.icomerp.arrayList.VoucherAllSDSList;
import ttit.com.shuvo.icomerp.arrayList.VoucherSDSList;

import static ttit.com.shuvo.icomerp.OracleConnection.createConnection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SalesOrderDeliverySummary#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SalesOrderDeliverySummary extends Fragment implements ClientSDSAdapter.ClickedItem {

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

    AmazingSpinner dateBetweenSpinner;

    String firstDate = "";
    String lastDate = "";
    int dateBetween = 1;
    ArrayList<ReceiveTypeList> dateBetweenLists;

    ArrayList<ClientSDSList> clientSDSLists;

    ArrayList<ClientAllSDSList> clientAllSDSLists;
    ArrayList<OrderAllSDSList> orderAllSDSLists;
    ArrayList<DeliveryAllSDSList> deliveryAllSDSLists;
    ArrayList<VoucherAllSDSList> voucherAllSDSLists;

    RecyclerView itemView;
    ClientSDSAdapter clientSDSAdapter;
    RecyclerView.LayoutManager layoutManager;

    public static int sods_selected_position = -1;


    boolean firsttime = true;

    private int mYear, mMonth, mDay;

    double order_amnt = 0.0;
    double advance_amnt = 0.0;
    double total_paid = 0.0;
    double order_balance = 0.0;

    double delivery_amnt = 0.0;
    double paid_amnt = 0.0;
    double delivery_balance = 0.0;

    double voucher_amnt = 0.0;

    TextView orderAmnt;
    TextView advanceAmnt;
    TextView totalPaid;
    TextView orderBalance;
    TextView deliveryAmnt;
    TextView paidAmnt;
    TextView deliveryBalance;
    TextView voucherAmnt;

    public SalesOrderDeliverySummary() {
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
     * @return A new instance of fragment SalesOrderDeliverySummary.
     */
    // TODO: Rename and change types and number of parameters
    public static SalesOrderDeliverySummary newInstance(String param1, String param2) {
        SalesOrderDeliverySummary fragment = new SalesOrderDeliverySummary();
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
        View view = inflater.inflate(R.layout.fragment_sales_order_delivery_summary, container, false);

        beginDate = view.findViewById(R.id.begin_date_sales_order_delivery_sum);
        endDate = view.findViewById(R.id.end_date_sales_order_delivery_sum);
        daterange = view.findViewById(R.id.date_range_msg_sales_delivery_sum);
        dateBetweenSpinner = view.findViewById(R.id.date_between_spinner);
        itemView = view.findViewById(R.id.sales_delivery_summary_report_view);
        orderAmnt = view.findViewById(R.id.total_order_amnt);
        advanceAmnt = view.findViewById(R.id.total_order_advance_amnt);
        totalPaid = view.findViewById(R.id.total_order_paid);
        orderBalance = view.findViewById(R.id.total_order_balance);
        deliveryAmnt = view.findViewById(R.id.total_delivery_amnt);
        paidAmnt = view.findViewById(R.id.total_paid_amnt);
        deliveryBalance = view.findViewById(R.id.total_delivery_balance);
        voucherAmnt = view.findViewById(R.id.total_voucher_amnt);

        clientSDSLists = new ArrayList<>();
        dateBetweenLists = new ArrayList<>();

        clientAllSDSLists= new ArrayList<>();
        orderAllSDSLists= new ArrayList<>();
        deliveryAllSDSLists= new ArrayList<>();
        voucherAllSDSLists= new ArrayList<>();

        dateBetweenLists.add(new ReceiveTypeList("1","Sales Order Date"));
        dateBetweenLists.add(new ReceiveTypeList("2","Sales Delivery Date"));
        dateBetweenLists.add(new ReceiveTypeList("3","Voucher Date"));

        firsttime = true;

        itemView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        itemView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(itemView.getContext(),DividerItemDecoration.VERTICAL);
        itemView.addItemDecoration(dividerItemDecoration);


        dateBetweenSpinner.setText("Sales Order Date");
        ArrayList<String> type = new ArrayList<>();
        for(int i = 0; i < dateBetweenLists.size(); i++) {
            type.add(dateBetweenLists.get(i).getType());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

        dateBetweenSpinner.setAdapter(arrayAdapter);

        // Selecting Sub Category
        dateBetweenSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                System.out.println(position+": "+name);
                for (int i = 0; i < dateBetweenLists.size(); i++) {
                    if (name.equals(dateBetweenLists.get(i).getType())) {
                        dateBetween = Integer.parseInt(dateBetweenLists.get(i).getId());
                    }
                }

                new Check().execute();

            }
        });
//        dateBetweenSpinner.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (firsttime) {
//                    ArrayList<String> type = new ArrayList<>();
//                    for(int i = 0; i < dateBetweenLists.size(); i++) {
//                        type.add(dateBetweenLists.get(i).getType());
//                    }
//                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);
//
//                    dateBetweenSpinner.setAdapter(arrayAdapter);
//                    firsttime = false;
//                }
//
//                return false;
//            }
//        });


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

    private void declareValue() {
         order_amnt = 0.0;
         advance_amnt = 0.0;
         total_paid = 0.0;
         order_balance = 0.0;

         delivery_amnt = 0.0;
         paid_amnt = 0.0;
         delivery_balance = 0.0;

         voucher_amnt = 0.0;
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
//        if (sods_selected_position == CategoryPosition) {
//            sods_selected_position = -1;
//        } else {
//            sods_selected_position=CategoryPosition;
//        }
//
//        clientSDSAdapter.notifyDataSetChanged();
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

                ReportData();
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
                clientSDSAdapter = new ClientSDSAdapter(clientSDSLists, getContext(), SalesOrderDeliverySummary.this);
                itemView.setAdapter(clientSDSAdapter);

//                String number = String.valueOf((int)order_amnt);
//                double amount = Double.parseDouble(number);
                DecimalFormat formatter = new DecimalFormat("##,##,##,###");
                String formatted = formatter.format(order_amnt);

                orderAmnt.setText(formatted);

//                number = String.valueOf((int)advance_amnt);
//                amount = Double.parseDouble(number);
                formatted = formatter.format(advance_amnt);

                advanceAmnt.setText(formatted);

//                number = String.valueOf((int)total_paid);
//                amount = Double.parseDouble(number);
                formatted = formatter.format(total_paid);

                totalPaid.setText(formatted);

//                number = String.valueOf((int)order_balance);
//                amount = Double.parseDouble(number);
                formatted = formatter.format(order_balance);

                orderBalance.setText(formatted);

//                number = String.valueOf((int)delivery_amnt);
//                amount = Double.parseDouble(number);
                formatted = formatter.format(delivery_amnt);

                deliveryAmnt.setText(formatted);

//                number = String.valueOf((int)paid_amnt);
//                amount = Double.parseDouble(number);
                formatted = formatter.format(paid_amnt);

                paidAmnt.setText(formatted);

//                number = String.valueOf((int)delivery_balance);
//                amount = Double.parseDouble(number);
                formatted = formatter.format(delivery_balance);

                deliveryBalance.setText(formatted);

//                number = String.valueOf((int)voucher_amnt);
//                amount = Double.parseDouble(number);
                formatted = formatter.format(voucher_amnt);

                voucherAmnt.setText(formatted);

                //orderAmnt.setText(String.valueOf((int)order_amnt));
                //advanceAmnt.setText(String.valueOf((int)advance_amnt));
                //totalPaid.setText(String.valueOf((int)total_paid));
                //orderBalance.setText(String.valueOf((int)order_balance));
                //deliveryAmnt.setText(String.valueOf((int)delivery_amnt));
                //paidAmnt.setText(String.valueOf((int)paid_amnt));
                //deliveryBalance.setText(String.valueOf((int)delivery_balance));
                //voucherAmnt.setText(String.valueOf((int)voucher_amnt));


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

    public void ReportData() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            clientSDSLists = new ArrayList<>();

            clientAllSDSLists= new ArrayList<>();
            orderAllSDSLists= new ArrayList<>();
            deliveryAllSDSLists= new ArrayList<>();
            voucherAllSDSLists= new ArrayList<>();
            declareValue();
            sods_selected_position = -1;


            if (firstDate.isEmpty()) {
                firstDate = null;
            }
            if (lastDate.isEmpty()) {
                lastDate = null;
            }

            CallableStatement callableStatement = connection.prepareCall("begin P_GET_SALES_ORDER_SUMMARY(?,?,?,?,?,?,?); end;");
            callableStatement.setString(1,firstDate);
            callableStatement.setString(2,lastDate);
            callableStatement.setInt(3,dateBetween);

            callableStatement.registerOutParameter(4, OracleTypes.CURSOR);
            callableStatement.registerOutParameter(5, OracleTypes.CURSOR);
            callableStatement.registerOutParameter(6, OracleTypes.CURSOR);
            callableStatement.registerOutParameter(7, OracleTypes.CURSOR);
            callableStatement.execute();

            ResultSet resultSet = (ResultSet) callableStatement.getObject(4);
            ResultSet resultSet1 = (ResultSet) callableStatement.getObject(5);
            ResultSet resultSet2 = (ResultSet) callableStatement.getObject(6);
            ResultSet resultSet3 = (ResultSet) callableStatement.getObject(7);

            //int i = 0;

            while (resultSet.next()) {
                clientAllSDSLists.add(new ClientAllSDSList(resultSet.getString(1),resultSet.getString(2)));
            }
            while (resultSet1.next()) {
                orderAllSDSLists.add(new OrderAllSDSList(resultSet1.getString(1),resultSet1.getString(2),resultSet1.getString(3),
                        resultSet1.getString(4),resultSet1.getString(5),resultSet1.getString(6),resultSet1.getString(7),
                        resultSet1.getString(8)));
            }
            while (resultSet2.next()) {
                deliveryAllSDSLists.add(new DeliveryAllSDSList(resultSet2.getString(1),resultSet2.getString(2),resultSet2.getString(3),
                        resultSet2.getString(4),resultSet2.getString(5),resultSet2.getString(6),
                        resultSet2.getString(7),resultSet2.getString(8),resultSet2.getString(9)));
            }
            while (resultSet3.next()) {
                voucherAllSDSLists.add(new VoucherAllSDSList(resultSet3.getString(1),resultSet3.getString(2),resultSet3.getString(3),
                        resultSet3.getString(4),resultSet3.getString(5)));
            }

            for (int i = 0 ; i < clientAllSDSLists.size(); i++) {
                String ad_id = clientAllSDSLists.get(i).getAd_id();
                ArrayList<OrderSDSList> orderSDSLists = new ArrayList<>();

                for (int j = 0; j < orderAllSDSLists.size(); j++) {

                    if (ad_id.equals(orderAllSDSLists.get(j).getAd_id())) {
                        String som_id = orderAllSDSLists.get(j).getSom_id();
                        String orderNo = orderAllSDSLists.get(j).getOrder_no();
                        System.out.println("FOR "+ ad_id+ " order no: "+ orderNo);

                        order_amnt = order_amnt + Double.parseDouble(orderAllSDSLists.get(j).getOrder_amnt());
                        advance_amnt = advance_amnt + Double.parseDouble(orderAllSDSLists.get(j).getAdv_amnt());
                        total_paid = total_paid + Double.parseDouble(orderAllSDSLists.get(j).getAcc_paid());
                        order_balance = order_balance + Double.parseDouble(orderAllSDSLists.get(j).getBalance());

                        ArrayList<DeliverySDSList> deliverySDSLists = new ArrayList<>();

                        for (int k = 0; k < deliveryAllSDSLists.size(); k++) {

                            if (som_id.equals(deliveryAllSDSLists.get(k).getSom_id())) {
                                String sm_id = deliveryAllSDSLists.get(k).getSm_id();

                                if (deliveryAllSDSLists.get(k).getDeliver_paid() != null) {
                                    paid_amnt = paid_amnt + Double.parseDouble(deliveryAllSDSLists.get(k).getDeliver_paid());
                                }
                                delivery_amnt = delivery_amnt + Double.parseDouble(deliveryAllSDSLists.get(k).getDeliver_total_amnt());

                                delivery_balance = delivery_balance + Double.parseDouble(deliveryAllSDSLists.get(k).getDeliver_balance());

                                System.out.println("FOR "+ ad_id+ " order no: "+ orderNo+ " deliveryNo : "+ deliveryAllSDSLists.get(k).getDelivery_no());

                                ArrayList<VoucherSDSList> voucherSDSLists = new ArrayList<>();

                                for (int l = 0; l < voucherAllSDSLists.size(); l++) {

                                    if (sm_id.equals(voucherAllSDSLists.get(l).getSm_id())) {

                                        voucher_amnt = voucher_amnt + Double.parseDouble(voucherAllSDSLists.get(l).getV_amnt());

                                        voucherSDSLists.add(new VoucherSDSList(voucherAllSDSLists.get(l).getV_no(),voucherAllSDSLists.get(l).getV_date(),
                                                voucherAllSDSLists.get(l).getV_amnt()));
                                    }
                                }
                                if (voucherSDSLists.size() == 0) {
                                    voucherSDSLists.add(new VoucherSDSList("","","0"));
                                }

                                deliverySDSLists.add(new DeliverySDSList(deliveryAllSDSLists.get(k).getDelivery_no(),deliveryAllSDSLists.get(k).getDeliver_date(),
                                        deliveryAllSDSLists.get(k).getDeliver_total_amnt(),deliveryAllSDSLists.get(k).getDeliver_paid(),deliveryAllSDSLists.get(k).getDeliver_balance(),
                                        voucherSDSLists));
                            }
                        }

                        if (deliverySDSLists.size() == 0) {
                            ArrayList<VoucherSDSList> voucherSDSLists = new ArrayList<>();
                            voucherSDSLists.add(new VoucherSDSList("","","0"));

                            deliverySDSLists.add(new DeliverySDSList("","","0","0","0",
                                    voucherSDSLists));
                        }
                        orderSDSLists.add(new OrderSDSList(orderAllSDSLists.get(j).getOrder_no(),orderAllSDSLists.get(j).getOrder_date(),orderAllSDSLists.get(j).getOrder_amnt(),
                                orderAllSDSLists.get(j).getAdv_amnt(),orderAllSDSLists.get(j).getAcc_paid(),orderAllSDSLists.get(j).getBalance(),deliverySDSLists));
                    }
                }
                if (orderSDSLists.size() == 0) {
                    ArrayList<VoucherSDSList> voucherSDSLists = new ArrayList<>();
                    voucherSDSLists.add(new VoucherSDSList("","","0"));

                    ArrayList<DeliverySDSList> deliverySDSLists = new ArrayList<>();
                    deliverySDSLists.add(new DeliverySDSList("","","0","0","0",
                            voucherSDSLists));

                    orderSDSLists.add(new OrderSDSList("","","0","0","0","0",
                            deliverySDSLists));
                }
                clientSDSLists.add(new ClientSDSList(clientAllSDSLists.get(i).getAd_name(),orderSDSLists));
            }

//            for (int i = 0; i < orderAllSDSLists.size(); i++) {
//                order_amnt = order_amnt + Double.parseDouble(orderAllSDSLists.get(i).getOrder_amnt());
//                advance_amnt = advance_amnt + Double.parseDouble(orderAllSDSLists.get(i).getAdv_amnt());
//                total_paid = total_paid + Double.parseDouble(orderAllSDSLists.get(i).getAcc_paid());
//                order_balance = order_balance + Double.parseDouble(orderAllSDSLists.get(i).getBalance());
//            }
//
//            for (int i = 0 ; i < deliveryAllSDSLists.size(); i++) {
//                if (deliveryAllSDSLists.get(i).getDeliver_paid() != null) {
//                    paid_amnt = paid_amnt + Double.parseDouble(deliveryAllSDSLists.get(i).getDeliver_paid());
//                }
//                delivery_amnt = delivery_amnt + Double.parseDouble(deliveryAllSDSLists.get(i).getDeliver_amnt());
//
//                delivery_balance = delivery_balance + Double.parseDouble(deliveryAllSDSLists.get(i).getDeliver_balance());
//            }
//
//            for (int i = 0; i < voucherAllSDSLists.size(); i++) {
//                voucher_amnt = voucher_amnt + Double.parseDouble(voucherAllSDSLists.get(i).getV_amnt());
//            }


            System.out.println(order_amnt);
            System.out.println(advance_amnt);
            System.out.println(total_paid);
            System.out.println(order_balance);
            System.out.println(delivery_amnt);
            System.out.println(paid_amnt);
            System.out.println(delivery_balance);
            System.out.println(voucher_amnt);

//            while (resultSet.next()) {
//                System.out.println(resultSet.getString(2));
//                String ad_ID = resultSet.getString(1);
//                ArrayList<OrderSDSList> orderSDSLists = new ArrayList<>();
//
//                while (resultSet1.next()) {
//                    System.out.println("ASHE NA");
//                    if (ad_ID.equals(resultSet1.getString(1))) {
//                        String som_id = resultSet1.getString(2);
//                        String orderNo = resultSet1.getString(3);
//                        System.out.println("FOR "+ ad_ID+ " order no: "+ orderNo);
//
//                        ArrayList<DeliverySDSList> deliverySDSLists = new ArrayList<>();
//
//                        while (resultSet2.next()) {
//                            if (som_id.equals(resultSet2.getString(1))) {
//                                String sm_id = resultSet2.getString(2);
//
//                                System.out.println("FOR "+ ad_ID+ " order no: "+ orderNo+ " deliveryNo : "+ resultSet2.getString(3));
//
//                                ArrayList<VoucherSDSList> voucherSDSLists = new ArrayList<>();
//
//                                while (resultSet3.next()) {
//                                    if (sm_id.equals(resultSet3.getString(1))) {
//                                        voucherSDSLists.add(new VoucherSDSList(resultSet3.getString(3),resultSet3.getString(4),
//                                                resultSet3.getString(5)));
//                                    }
//
//                                }
//
//                                if (voucherSDSLists.size() == 0) {
//                                    voucherSDSLists.add(new VoucherSDSList("","","0"));
//                                }
//                                deliverySDSLists.add(new DeliverySDSList(resultSet2.getString(3),resultSet2.getString(4),
//                                        resultSet2.getString(7),resultSet2.getString(8),resultSet2.getString(9),
//                                        voucherSDSLists));
//                            }
//
//                        }
//
//                        if (deliverySDSLists.size() == 0) {
//                            ArrayList<VoucherSDSList> voucherSDSLists = new ArrayList<>();
//                            voucherSDSLists.add(new VoucherSDSList("","","0"));
//
//                            deliverySDSLists.add(new DeliverySDSList("","","0","0","0",
//                                    voucherSDSLists));
//                        }
//                        orderSDSLists.add(new OrderSDSList(resultSet1.getString(3),resultSet1.getString(4),resultSet1.getString(5),
//                                resultSet1.getString(6), resultSet1.getString(7),resultSet1.getString(8),deliverySDSLists));
//                    }
//                }
//
//                if (orderSDSLists.size() == 0) {
//                    ArrayList<VoucherSDSList> voucherSDSLists = new ArrayList<>();
//                    voucherSDSLists.add(new VoucherSDSList("","","0"));
//
//                    ArrayList<DeliverySDSList> deliverySDSLists = new ArrayList<>();
//                    deliverySDSLists.add(new DeliverySDSList("","","0","0","0",
//                            voucherSDSLists));
//
//                    orderSDSLists.add(new OrderSDSList("","","0","0","0","0",
//                            deliverySDSLists));
//                }
//
//                clientSDSLists.add(new ClientSDSList(resultSet.getString(2),orderSDSLists));
//            }

            callableStatement.close();
            connected = true;

            if (firstDate == null) {
                firstDate = "";
            }
            if (lastDate == null) {
                lastDate = "";
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
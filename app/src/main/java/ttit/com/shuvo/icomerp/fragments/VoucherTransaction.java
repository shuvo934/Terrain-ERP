package ttit.com.shuvo.icomerp.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
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

import com.google.android.material.textfield.TextInputEditText;
import com.rosemaryapp.amazingspinner.AmazingSpinner;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import oracle.jdbc.driver.OracleTypes;
import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.WaitProgress;
import ttit.com.shuvo.icomerp.adapters.VouchTrans1Adapter;
import ttit.com.shuvo.icomerp.arrayList.DeliveryChallanList;
import ttit.com.shuvo.icomerp.arrayList.ReceiveTypeList;
import ttit.com.shuvo.icomerp.arrayList.SalesOrderList;
import ttit.com.shuvo.icomerp.arrayList.VoucherLists1;
import ttit.com.shuvo.icomerp.arrayList.VoucherLists2;
import ttit.com.shuvo.icomerp.arrayList.VoucherLists3;

import static ttit.com.shuvo.icomerp.OracleConnection.createConnection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VoucherTransaction#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VoucherTransaction extends Fragment implements VouchTrans1Adapter.ClickedItem{

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

    private int mYear, mMonth, mDay;
    String firstDate = "";
    String lastDate = "";

    AmazingSpinner voucherSpinner;
    ArrayList<ReceiveTypeList> voucherTypeLists;

    public static ArrayList<SalesOrderList> salesOrderListsVT;
    public ArrayList<String> salesOrderNo;

    public static ArrayList<DeliveryChallanList> deliveryChallanListsVT;
    public ArrayList<String> deliveryChallanNo;

    String voucherType = "";

    CardView vCard;

    RecyclerView itemView;
    VouchTrans1Adapter vouchTrans1Adapter;
    RecyclerView.LayoutManager layoutManager;
    TextView noVoucherMsg;

    ArrayList<VoucherLists1> voucherLists1s;

    public VoucherTransaction() {
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
     * @return A new instance of fragment VoucherTransaction.
     */
    // TODO: Rename and change types and number of parameters
    public static VoucherTransaction newInstance(String param1, String param2) {
        VoucherTransaction fragment = new VoucherTransaction();
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
        View view = inflater.inflate(R.layout.fragment_voucher_transaction, container, false);

        beginDate = view.findViewById(R.id.begin_date_voucher_transaction);
        endDate = view.findViewById(R.id.end_date_voucher_transaction);
        daterange = view.findViewById(R.id.date_range_msg_voucher_transaction);

        voucherSpinner = view.findViewById(R.id.voucher_type_vt_spinner);

        vCard = view.findViewById(R.id.voucher_transaction_report_card);
        vCard.setVisibility(View.GONE);
        itemView = view.findViewById(R.id.voucher_trans_report_view);
        noVoucherMsg = view.findViewById(R.id.no_voucher_trans_msg);

        itemView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        itemView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(itemView.getContext(),DividerItemDecoration.VERTICAL);
        itemView.addItemDecoration(dividerItemDecoration);

        voucherTypeLists = new ArrayList<>();
        voucherLists1s = new ArrayList<>();
        salesOrderListsVT = new ArrayList<>();
        salesOrderNo = new ArrayList<>();
        deliveryChallanListsVT = new ArrayList<>();
        deliveryChallanNo = new ArrayList<>();

        voucherTypeLists.add(new ReceiveTypeList("1","CV"));
        voucherTypeLists.add(new ReceiveTypeList("2","DV"));
        voucherTypeLists.add(new ReceiveTypeList("3","JV"));

        ArrayList<String> type = new ArrayList<>();
        for(int i = 0; i < voucherTypeLists.size(); i++) {
            type.add(voucherTypeLists.get(i).getType());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

        voucherSpinner.setAdapter(arrayAdapter);

        voucherSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                System.out.println(position+": "+name);

                voucherType = name;

                vCard.setVisibility(View.GONE);
                new Check().execute();

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
                                if (!voucherType.isEmpty()) {
                                    new Check().execute();
                                }

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
                                        daterange.setVisibility(View.GONE);

                                        if (!voucherType.isEmpty()) {
                                            new Check().execute();
                                        }

                                    }
                                    else {
                                        daterange.setVisibility(View.VISIBLE);
                                        beginDate.setText("");
                                        firstDate = "";
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
                                if (!voucherType.isEmpty()) {
                                    new Check().execute();
                                }


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

                                        if (!voucherType.isEmpty()) {
                                            new Check().execute();
                                        }

                                    }
                                    else {
                                        daterange.setVisibility(View.VISIBLE);
                                        endDate.setText("");
                                        lastDate = "";

                                    }
                                }
                            }

                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });

        return view;
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


            if (conn) {

                conn = false;
                connected = false;
                vCard.setVisibility(View.VISIBLE);
                if (voucherLists1s.size() == 0 ) {
                    noVoucherMsg.setVisibility(View.VISIBLE);
                } else {
                    noVoucherMsg.setVisibility(View.GONE);
                }

                vouchTrans1Adapter = new VouchTrans1Adapter(voucherLists1s, getContext(), VoucherTransaction.this);
                itemView.setAdapter(vouchTrans1Adapter);
                waitProgress.dismiss();

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

            voucherLists1s = new ArrayList<>();
            salesOrderListsVT = new ArrayList<>();
            salesOrderNo = new ArrayList<>();
            deliveryChallanNo = new ArrayList<>();
            deliveryChallanListsVT = new ArrayList<>();

            Statement stmt = connection.createStatement();

            if (firstDate.isEmpty()) {
                firstDate = null;
            }
            if (lastDate.isEmpty()) {
                lastDate = null;
            }


            CallableStatement callableStatement1 = connection.prepareCall("begin ? := GET_VOUCHER_TRANSACTION_LIST(?,?,?,?,?,?); end;");
            callableStatement1.setString(2, firstDate);
            callableStatement1.setString(3,lastDate);
            callableStatement1.setString(4,voucherType);
            callableStatement1.setString(5,null);
            callableStatement1.setString(6,null);
            callableStatement1.setInt(7,1);
            callableStatement1.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement1.execute();

            ResultSet resultSet = (ResultSet) callableStatement1.getObject(1);

            while (resultSet.next()) {
                String date = resultSet.getString(2);
                ArrayList<VoucherLists2> voucherLists2s = new ArrayList<>();

                CallableStatement callableStatement2 = connection.prepareCall("begin ? := GET_VOUCHER_TRANSACTION_LIST(?,?,?,?,?,?); end;");
                callableStatement2.setString(2, firstDate);
                callableStatement2.setString(3,lastDate);
                callableStatement2.setString(4,voucherType);
                callableStatement2.setString(5,date);
                callableStatement2.setString(6,null);
                callableStatement2.setInt(7,2);
                callableStatement2.registerOutParameter(1, OracleTypes.CURSOR);
                callableStatement2.execute();

                ResultSet resultSet1 = (ResultSet) callableStatement2.getObject(1);

                while (resultSet1.next()) {
                    String vNo = resultSet1.getString(2);
                    String purchase = resultSet1.getString(4);
                    String type = resultSet1.getString(5);
                    if (type.equals("SOM")) {
                        salesOrderNo.add(purchase);
                    }
                    if (type.equals("SM")) {
                        deliveryChallanNo.add(purchase);
                    }

                    System.out.println(vNo+" "+purchase+" "+" "+type);
                    ArrayList<VoucherLists3> voucherLists3s = new ArrayList<>();

                    CallableStatement callableStatement3 = connection.prepareCall("begin ? := GET_VOUCHER_TRANSACTION_LIST(?,?,?,?,?,?); end;");
                    callableStatement3.setString(2, firstDate);
                    callableStatement3.setString(3,lastDate);
                    callableStatement3.setString(4,voucherType);
                    callableStatement3.setString(5,date);
                    callableStatement3.setString(6,vNo);
                    callableStatement3.setInt(7,3);
                    callableStatement3.registerOutParameter(1, OracleTypes.CURSOR);
                    callableStatement3.execute();

                    ResultSet resultSet2 = (ResultSet) callableStatement3.getObject(1);

                    while (resultSet2.next()) {
//                        System.out.println(resultSet2.getString(2)+" "+ resultSet2.getString(3));
                        voucherLists3s.add(new VoucherLists3(resultSet2.getString(2),resultSet2.getString(3),resultSet2.getString(4)));
                    }

                    resultSet2.close();
                    callableStatement3.close();


                    voucherLists2s.add(new VoucherLists2(vNo,resultSet1.getString(3),voucherLists3s,type,purchase));
                }

                resultSet1.close();
                callableStatement2.close();

                voucherLists1s.add(new VoucherLists1(resultSet.getString(2),voucherLists2s));

            }

            resultSet.close();

            callableStatement1.close();

            for (int i = 0; i < salesOrderNo.size(); i++) {
                ResultSet rsSOM = stmt.executeQuery("SELECT SOM_ID,\n" +
                        "               AD_ID,\n" +
                        "               AD_NAME,\n" +
                        "               AD_CODE,\n" +
                        "               SOM_ORDER_NO,\n" +
                        "               SOM_DATE,\n" +
                        "               SOM_USER,\n" +
                        "               TARGET_DELIVERY,\n" +
                        "               TARGET_ADDRESS,\n" +
                        "               ORDER_TYPE,\n" +
                        "               EDD,\n" +
                        "               SOM_DISCOUNT_VALUE,\n" +
                        "               SOM_VAT_AMT,\n" +
                        "               AAD_CONTACT_PERSON,\n" +
                        "               AAD_CONTACT_NO,\n" +
                        "               AAD_EMAIL,\n" +
                        "               SUM (AMT) TOTAL_AMT,\n" +
                        "               SOM_ADVANCE_AMT\n" +
                        "          FROM (SELECT SALES_ORDER_MST.SOM_ID,\n" +
                        "                       ACC_DTL.AD_ID,\n" +
                        "                       ACC_DTL.AD_NAME,\n" +
                        "                       ACC_DTL.AD_CODE,\n" +
                        "                       SALES_ORDER_MST.SOM_ORDER_NO,\n" +
                        "                       to_char(SALES_ORDER_MST.SOM_DATE,'DD-Mon-RR') SOM_DATE,\n" +
                        "                       SALES_ORDER_MST.SOM_USER,\n" +
                        "                       CASE\n" +
                        "                          WHEN SALES_ORDER_MST.SOM_DELIVERY_TARGET = 1\n" +
                        "                          THEN\n" +
                        "                             'Client Address'\n" +
                        "                          WHEN SALES_ORDER_MST.SOM_DELIVERY_TARGET = 2\n" +
                        "                          THEN\n" +
                        "                             'Factory Address'\n" +
                        "                          WHEN SALES_ORDER_MST.SOM_DELIVERY_TARGET = 3\n" +
                        "                          THEN\n" +
                        "                             'Alternative Address'\n" +
                        "                          ELSE\n" +
                        "                             NULL\n" +
                        "                       END\n" +
                        "                          AS TARGET_DELIVERY,\n" +
                        "                       CASE\n" +
                        "                          WHEN SALES_ORDER_MST.SOM_DELIVERY_TARGET = 1\n" +
                        "                          THEN\n" +
                        "                             ACC_DTL.AD_ADDRESS\n" +
                        "                          WHEN SALES_ORDER_MST.SOM_DELIVERY_TARGET = 2\n" +
                        "                          THEN\n" +
                        "                             ACC_FACTORY_DTL.AFD_FACTORY_ADDRESS\n" +
                        "                          WHEN SALES_ORDER_MST.SOM_DELIVERY_TARGET = 3\n" +
                        "                          THEN\n" +
                        "                             SALES_ORDER_MST.SOM_DELIVERY_ADDRESS\n" +
                        "                          ELSE\n" +
                        "                             NULL\n" +
                        "                       END\n" +
                        "                          AS TARGET_ADDRESS,\n" +
                        "                       CASE\n" +
                        "                          WHEN SALES_ORDER_MST.SOM_ORDER_TYPE = 1\n" +
                        "                          THEN\n" +
                        "                             'Sales Order'\n" +
                        "                          WHEN SALES_ORDER_MST.SOM_ORDER_TYPE = 2\n" +
                        "                          THEN\n" +
                        "                             'Sample Order'\n" +
                        "                          WHEN SALES_ORDER_MST.SOM_ORDER_TYPE = 3\n" +
                        "                          THEN\n" +
                        "                             'Service Order'\n" +
                        "                          ELSE\n" +
                        "                             NULL\n" +
                        "                       END\n" +
                        "                          AS ORDER_TYPE,\n" +
                        "                       to_char(SALES_ORDER_MST.SOM_DATE,'DD-Mon-RR') EDD,\n" +
                        "                       SALES_ORDER_MST.SOM_DISCOUNT_VALUE,\n" +
                        "                       SALES_ORDER_MST.SOM_VAT_AMT,\n" +
                        "                       SALES_ORDER_MST.SOM_ADVANCE_AMT,\n" +
                        "                       ACC_ATTENTION_DTL.AAD_CONTACT_PERSON,\n" +
                        "                       ACC_ATTENTION_DTL.AAD_CONTACT_NO,\n" +
                        "                       ACC_ATTENTION_DTL.AAD_EMAIL,\n" +
                        "                         (CASE\n" +
                        "                             WHEN     SOD_WEIGHTED_DISCOUNT_TYPE IS NOT NULL\n" +
                        "                                  AND SOD_WEIGHTED_DISCOUNT_TYPE = '%'\n" +
                        "                             THEN\n" +
                        "                                  NVL (SOD_ORDER_RATE, 0)\n" +
                        "                                - (  (  CASE\n" +
                        "                                           WHEN     SOD_DISCOUNT_TYPE\n" +
                        "                                                       IS NOT NULL\n" +
                        "                                                AND SOD_DISCOUNT_TYPE = '%'\n" +
                        "                                           THEN\n" +
                        "                                              (  (NVL (SOD_ORDER_RATE, 0))\n" +
                        "                                               - (  (  NVL (SOD_ORDER_RATE, 0)\n" +
                        "                                                     / 100)\n" +
                        "                                                  * NVL (SOD_DISCOUNT_VALUE, 0)))\n" +
                        "                                           WHEN     SOD_DISCOUNT_TYPE\n" +
                        "                                                       IS NOT NULL\n" +
                        "                                                AND SOD_DISCOUNT_TYPE = 'SR'\n" +
                        "                                           THEN\n" +
                        "                                              (  (NVL (SOD_ORDER_RATE, 0))\n" +
                        "                                               - NVL (SOD_DISCOUNT_VALUE, 0))\n" +
                        "                                           ELSE\n" +
                        "                                              NVL (SOD_ORDER_RATE, 0)\n" +
                        "                                        END\n" +
                        "                                      / 100)\n" +
                        "                                   * NVL (SOD_WEIGHTED_DISCOUNT_VALUE, 0))\n" +
                        "                             WHEN     SOD_WEIGHTED_DISCOUNT_TYPE IS NOT NULL\n" +
                        "                                  AND SOD_WEIGHTED_DISCOUNT_TYPE = 'SR'\n" +
                        "                             THEN\n" +
                        "                                (  (CASE\n" +
                        "                                       WHEN     SOD_DISCOUNT_TYPE IS NOT NULL\n" +
                        "                                            AND SOD_DISCOUNT_TYPE = '%'\n" +
                        "                                       THEN\n" +
                        "                                          (  (NVL (SOD_ORDER_RATE, 0))\n" +
                        "                                           - (  (NVL (SOD_ORDER_RATE, 0) / 100)\n" +
                        "                                              * NVL (SOD_DISCOUNT_VALUE, 0)))\n" +
                        "                                       WHEN     SOD_DISCOUNT_TYPE IS NOT NULL\n" +
                        "                                            AND SOD_DISCOUNT_TYPE = 'SR'\n" +
                        "                                       THEN\n" +
                        "                                          (  (NVL (SOD_ORDER_RATE, 0))\n" +
                        "                                           - NVL (SOD_DISCOUNT_VALUE, 0))\n" +
                        "                                       ELSE\n" +
                        "                                          NVL (SOD_ORDER_RATE, 0)\n" +
                        "                                    END)\n" +
                        "                                 - NVL (SOD_WEIGHTED_DISCOUNT_VALUE, 0))\n" +
                        "                             ELSE\n" +
                        "                                NVL (SOD_ORDER_RATE, 0)\n" +
                        "                          END)\n" +
                        "                       * NVL (SALES_ORDER_DTL.SOD_QTY, 0)\n" +
                        "                          AS AMT\n" +
                        "                  FROM SALES_ORDER_MST,\n" +
                        "                       ACC_DTL,\n" +
                        "                       ACC_FACTORY_DTL,\n" +
                        "                       ACC_ATTENTION_DTL,\n" +
                        "                       SALES_ORDER_DTL,\n" +
                        "                       SALES_ORDER_MST SALES_ORDER_MST1\n" +
                        "                 WHERE     ACC_DTL.AD_ID = SALES_ORDER_MST.SOM_AD_ID\n" +
                        "                       AND ACC_FACTORY_DTL.AFD_ID(+) =\n" +
                        "                              SALES_ORDER_MST.SOM_CLIENT_AFD_ID\n" +
                        "                       AND ACC_ATTENTION_DTL.AAD_ID(+) =\n" +
                        "                              SALES_ORDER_MST.SOM_CLIENT_AAD_ID\n" +
                        "                       AND SALES_ORDER_MST.SOM_ID = SALES_ORDER_DTL.SOD_SOM_ID\n" +
                        "                       AND SALES_ORDER_MST.SOM_PREVIOUS_SOM_ID =\n" +
                        "                              SALES_ORDER_MST1.SOM_ID(+)\n" +
                        "                       AND  SALES_ORDER_MST.SOM_ORDER_NO = '"+salesOrderNo.get(i)+"')\n" +
                        "      GROUP BY SOM_ID,\n" +
                        "               AD_ID,\n" +
                        "               AD_NAME,\n" +
                        "               AD_CODE,\n" +
                        "               SOM_ORDER_NO,\n" +
                        "               SOM_DATE,\n" +
                        "               SOM_USER,\n" +
                        "               TARGET_DELIVERY,\n" +
                        "               TARGET_ADDRESS,\n" +
                        "               ORDER_TYPE,\n" +
                        "               EDD,\n" +
                        "               SOM_DISCOUNT_VALUE,\n" +
                        "               SOM_VAT_AMT,\n" +
                        "               AAD_CONTACT_PERSON,\n" +
                        "               AAD_CONTACT_NO,\n" +
                        "               AAD_EMAIL,\n" +
                        "               SOM_ADVANCE_AMT\n" +
                        "      ORDER BY AD_NAME, SOM_ID");

                while (rsSOM.next()) {
                    salesOrderListsVT.add(new SalesOrderList(rsSOM.getString(1), rsSOM.getString(2),rsSOM.getString(3),
                            rsSOM.getString(4), rsSOM.getString(5),rsSOM.getString(6),
                            rsSOM.getString(7),rsSOM.getString(8), rsSOM.getString(9),
                            rsSOM.getString(10),rsSOM.getString(11),rsSOM.getString(12),
                            rsSOM.getString(13),rsSOM.getString(14),rsSOM.getString(15),
                            rsSOM.getString(16),rsSOM.getString(17),rsSOM.getString(18)));
                }

                rsSOM.close();
            }



            for (int i = 0; i < deliveryChallanNo.size(); i++) {
                ResultSet resDC = stmt.executeQuery("SELECT ALL\n" +
                        "               SALES_MST.SM_ID,\n" +
                        "               SALES_MST.SM_DELIVERY_NO,\n" +
                        "               TO_CHAR (SALES_MST.SM_DATE, 'DD-Mon-RR')      SM_DATE,\n" +
                        "               ACC_DTL.AD_NAME,\n" +
                        "               CASE\n" +
                        "                  WHEN SOM_ORDER_TYPE = 1 THEN 'Sales Order'\n" +
                        "                  WHEN SOM_ORDER_TYPE = 2 THEN 'Sample Order'\n" +
                        "                  WHEN SOM_ORDER_TYPE = 3 THEN 'Service Order'\n" +
                        "                  ELSE 'N/A'\n" +
                        "               END\n" +
                        "                  AS ORDER_TYPE,\n" +
                        "               SOM_ORDER_NO                                  OM_ORDER_NO,\n" +
                        "               TO_CHAR (SOM_DATE, 'DD-Mon-RR')               OM_ODR_DATE,\n" +
                        "               SM_INVOICE_AMT,\n" +
                        "               CASE WHEN COM_PACK.GET_VAT_CALC_FLAG(SM_CID_ID) = 1 THEN SM_VAT_AMT ELSE\n" +
                        "                CASE WHEN SM_VAT_PERCENTAGE>0 AND SM_INVOICE_AMT> 0 THEN NVL(SM_INVOICE_AMT,0)*(NVL(SM_VAT_PERCENTAGE,0)/100) ELSE 0 END\n" +
                        "                END SM_VAT_AMT,\n" +
                        "               ACC_DTL.AD_ID,\n" +
                        "               TO_CHAR (SALES_ORDER_MST.SOM_DATE, 'DD-Mon-RR') EDD,\n" +
                        "               ACC_DTL.AD_CODE,\n" +
                        "               CASE\n" +
                        "                  WHEN SALES_ORDER_MST.SOM_DELIVERY_TARGET = 1\n" +
                        "                  THEN\n" +
                        "                     ACC_DTL.AD_ADDRESS\n" +
                        "                  WHEN SALES_ORDER_MST.SOM_DELIVERY_TARGET = 2\n" +
                        "                  THEN\n" +
                        "                     ACC_FACTORY_DTL.AFD_FACTORY_ADDRESS\n" +
                        "                  WHEN SALES_ORDER_MST.SOM_DELIVERY_TARGET = 3\n" +
                        "                  THEN\n" +
                        "                     SALES_ORDER_MST.SOM_DELIVERY_ADDRESS\n" +
                        "                  ELSE\n" +
                        "                     NULL\n" +
                        "               END\n" +
                        "                  AS TARGET_ADDRESS,\n" +
                        "               ACC_ATTENTION_DTL.AAD_CONTACT_PERSON,\n" +
                        "               ACC_ATTENTION_DTL.AAD_CONTACT_NO,\n" +
                        "               ACC_ATTENTION_DTL.AAD_EMAIL\n" +
                        "          FROM SALES_MST,\n" +
                        "               ACC_DTL,\n" +
                        "               SALES_ORDER_MST,\n" +
                        "               ACC_FACTORY_DTL,\n" +
                        "               ACC_ATTENTION_DTL\n" +
                        "         WHERE     SALES_ORDER_MST.SOM_ID = SALES_MST.SM_SOM_ID\n" +
                        "               AND (SALES_MST.SM_AD_ID = ACC_DTL.AD_ID)\n" +
                        "               AND ACC_FACTORY_DTL.AFD_ID(+) =\n" +
                        "                      SALES_ORDER_MST.SOM_CLIENT_AFD_ID\n" +
                        "               AND ACC_ATTENTION_DTL.AAD_ID(+) =\n" +
                        "                      SALES_ORDER_MST.SOM_CLIENT_AAD_ID\n" +
                        "               AND SALES_MST.SM_DELIVERY_NO = '"+deliveryChallanNo.get(i)+"'");

                while (resDC.next()) {
                    deliveryChallanListsVT.add(new DeliveryChallanList(resDC.getString(1), resDC.getString(2),resDC.getString(3),
                            resDC.getString(4), resDC.getString(5),resDC.getString(6),
                            resDC.getString(7),resDC.getString(8), resDC.getString(9),
                            resDC.getString(10),resDC.getString(11),resDC.getString(12),
                            resDC.getString(13),resDC.getString(14),resDC.getString(15),
                            resDC.getString(16)));
                }
                resDC.close();
            }


            stmt.close();

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
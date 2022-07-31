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
import ttit.com.shuvo.icomerp.adapters.PartSummaryLedAdapter;
import ttit.com.shuvo.icomerp.adapters.VouchTrans1Adapter;
import ttit.com.shuvo.icomerp.arrayList.PartySummaryLedgerLists;
import ttit.com.shuvo.icomerp.arrayList.ReceiveTypeList;
import ttit.com.shuvo.icomerp.arrayList.SalesOrderList;
import ttit.com.shuvo.icomerp.arrayList.VoucherLists1;
import ttit.com.shuvo.icomerp.arrayList.VoucherLists2;
import ttit.com.shuvo.icomerp.arrayList.VoucherLists3;

import static ttit.com.shuvo.icomerp.OracleConnection.createConnection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PartySummaryLedger#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PartySummaryLedger extends Fragment implements PartSummaryLedAdapter.ClickedItem{

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

    AmazingSpinner partySpinner;
    ArrayList<ReceiveTypeList> partyTypeLists;

    int partyType = 0;

    CardView pslCard;

    RecyclerView itemView;
    PartSummaryLedAdapter partSummaryLedAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<PartySummaryLedgerLists> partySummaryLedgerLists;

    TextView totalBf;
    TextView totalDebit;
    TextView totalCredit;
    TextView totalDueBalance;

    double total_bf = 0.0;
    double total_debit = 0.0;
    double total_credit = 0.0;
    double total_due_balance = 0.0;

    public PartySummaryLedger() {
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
     * @return A new instance of fragment PartySummaryLedger.
     */
    // TODO: Rename and change types and number of parameters
    public static PartySummaryLedger newInstance(String param1, String param2) {
        PartySummaryLedger fragment = new PartySummaryLedger();
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
        View view = inflater.inflate(R.layout.fragment_party_summary_ledger, container, false);

        beginDate = view.findViewById(R.id.begin_date_party_summary_ledger);
        endDate = view.findViewById(R.id.end_date_party_summary_ledger);
        daterange = view.findViewById(R.id.date_range_msg_party_summary_ledger);

        partySpinner = view.findViewById(R.id.party_type_psl_spinner);

        pslCard = view.findViewById(R.id.party_summary_ledger_report_card);
        pslCard.setVisibility(View.GONE);
        itemView = view.findViewById(R.id.party_summary_ledger_report_view);

        totalBf = view.findViewById(R.id.total_bf_psl);
        totalDebit = view.findViewById(R.id.total_debit_psl);
        totalCredit = view.findViewById(R.id.total_credit_psl);
        totalDueBalance = view.findViewById(R.id.total_due_balance_psl);

        itemView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        itemView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(itemView.getContext(),DividerItemDecoration.VERTICAL);
        itemView.addItemDecoration(dividerItemDecoration);

        partyTypeLists = new ArrayList<>();
        partySummaryLedgerLists = new ArrayList<>();

        partyTypeLists.add(new ReceiveTypeList("6","Customer"));
        partyTypeLists.add(new ReceiveTypeList("7","Supplier"));

        ArrayList<String> type = new ArrayList<>();
        for(int i = 0; i < partyTypeLists.size(); i++) {
            type.add(partyTypeLists.get(i).getType());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

        partySpinner.setAdapter(arrayAdapter);

        partySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                System.out.println(position+": "+name);

                for (int i = 0; i < partyTypeLists.size(); i++) {
                    if (name.equals(partyTypeLists.get(i).getType())) {
                        partyType = Integer.parseInt(partyTypeLists.get(i).getId());
                    }
                }

                pslCard.setVisibility(View.GONE);
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
                                if (partyType > 0) {
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

                                        if (partyType > 0) {
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
                                if (partyType > 0) {
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

                                        if (partyType > 0) {
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
                pslCard.setVisibility(View.VISIBLE);

                 total_bf = 0.0;
                 total_debit = 0.0;
                 total_credit = 0.0;
                 total_due_balance = 0.0;


                partSummaryLedAdapter = new PartSummaryLedAdapter(partySummaryLedgerLists, getContext(), PartySummaryLedger.this);
                itemView.setAdapter(partSummaryLedAdapter);
                waitProgress.dismiss();

                for (int i = 0 ; i < partySummaryLedgerLists.size(); i++) {
                    if (partySummaryLedgerLists.get(i).getBf() != null) {
                        total_bf = total_bf + Double.parseDouble(partySummaryLedgerLists.get(i).getBf());
                    }
                    if (partySummaryLedgerLists.get(i).getDebit() != null) {
                        total_debit = total_debit + Double.parseDouble(partySummaryLedgerLists.get(i).getDebit());
                    }
                    if (partySummaryLedgerLists.get(i).getCredit() != null) {
                        total_credit = total_credit + Double.parseDouble(partySummaryLedgerLists.get(i).getCredit());
                    }
                    if (partySummaryLedgerLists.get(i).getDueBalance() != null) {
                        total_due_balance = total_due_balance + Double.parseDouble(partySummaryLedgerLists.get(i).getDueBalance());
                    }
                }

                DecimalFormat formatter = new DecimalFormat("##,##,##,###.##");

                String formatted = formatter.format(total_bf);

                if (formatted.contains("-")) {
                    formatted = formatted.substring(1);
                    formatted = "(-)  "+ formatted;
                }
                totalBf.setText(formatted);


                formatted = formatter.format(total_debit);

                if (formatted.contains("-")) {
                    formatted = formatted.substring(1);
                    formatted = "(-)  "+ formatted;
                }
                totalDebit.setText(formatted);

                formatted = formatter.format(total_credit);

                if (formatted.contains("-")) {
                    formatted = formatted.substring(1);
                    formatted = "(-)  "+ formatted;
                }
                totalCredit.setText(formatted);

                formatted = formatter.format(total_due_balance);

                if (formatted.contains("-")) {
                    formatted = formatted.substring(1);
                    formatted = "(-)  "+ formatted;
                }
                totalDueBalance.setText(formatted);


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

            partySummaryLedgerLists = new ArrayList<>();

            if (firstDate.isEmpty()) {
                firstDate = null;
            }
            if (lastDate.isEmpty()) {
                lastDate = null;
            }


            CallableStatement callableStatement1 = connection.prepareCall("begin ? := GET_PARTY_SUMMARY_LEDGER_LIST(?,?,?,?); end;");
            callableStatement1.setString(2, firstDate);
            callableStatement1.setString(3,lastDate);
            callableStatement1.setInt(4,partyType);
            callableStatement1.setString(5,null);
            callableStatement1.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement1.execute();

            ResultSet resultSet = (ResultSet) callableStatement1.getObject(1);

            while (resultSet.next()) {

                partySummaryLedgerLists.add(new PartySummaryLedgerLists(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),
                        resultSet.getString(4),resultSet.getString(5),resultSet.getString(6),
                        resultSet.getString(7),resultSet.getString(8)));

            }

            resultSet.close();

            callableStatement1.close();

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
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

import android.text.Editable;
import android.text.TextWatcher;
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
import ttit.com.shuvo.icomerp.adapters.AccountLedgerAdapter;
import ttit.com.shuvo.icomerp.adapters.VouchTrans1Adapter;
import ttit.com.shuvo.icomerp.arrayList.AccountLedgerLists;
import ttit.com.shuvo.icomerp.arrayList.ReceiveTypeList;
import ttit.com.shuvo.icomerp.dialogues.AccountsDialogue;

import static ttit.com.shuvo.icomerp.OracleConnection.createConnection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountLedger#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountLedger extends Fragment implements AccountLedgerAdapter.ClickedItem{

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

    public static TextInputEditText accLedgerSpinner;
    public static ArrayList<ReceiveTypeList> accLists;

    public static String ad_id = "";

    CardView vCard;

    RecyclerView itemView;
    AccountLedgerAdapter accountLedgerAdapter;
    RecyclerView.LayoutManager layoutManager;
    TextView openingDebit;
    TextView openingCredit;
    TextView closingDebit;
    TextView closingCredit;
    TextView closingBalance;
    TextView openingDate;

    String openD = "";
    String openC = "";


    ArrayList<AccountLedgerLists> accountLedgerLists;

    public AccountLedger() {
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
     * @return A new instance of fragment AccountLedger.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountLedger newInstance(String param1, String param2) {
        AccountLedger fragment = new AccountLedger();
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
        View view = inflater.inflate(R.layout.fragment_account_ledger, container, false);

        beginDate = view.findViewById(R.id.begin_date_account_ledger);
        endDate = view.findViewById(R.id.end_date_account_ledger);
        daterange = view.findViewById(R.id.date_range_msg_account_ledger);

        accLedgerSpinner = view.findViewById(R.id.acc_no_account_ledger_spinner);

        vCard = view.findViewById(R.id.account_ledger_report_card);
        vCard.setVisibility(View.GONE);

        openingCredit = view.findViewById(R.id.opening_credit_balance);
        openingDebit = view.findViewById(R.id.opening_debit_balance);
        closingBalance = view.findViewById(R.id.closing_balance);
        closingDebit = view.findViewById(R.id.closing_debit_balance);
        closingCredit = view.findViewById(R.id.closing_credit_balance);
        openingDate = view.findViewById(R.id.opening_date_acc_ledger);

        itemView = view.findViewById(R.id.acc_ledger_report_view);

        itemView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        itemView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(itemView.getContext(),DividerItemDecoration.VERTICAL);
        itemView.addItemDecoration(dividerItemDecoration);

        accLists = new ArrayList<>();
        accountLedgerLists = new ArrayList<>();

//        accLedgerSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String name = parent.getItemAtPosition(position).toString();
//                System.out.println(position+": "+name);
//                for (int i = 0; i < accLists.size(); i++) {
//                    if (name.equals(accLists.get(i).getType())) {
//                        ad_id = accLists.get(i).getId();
//                    }
//                }
//                System.out.println("AD ID: "+ ad_id);
//
//                vCard.setVisibility(View.GONE);
//                new ReportCheck().execute();
//
//            }
//        });

        accLedgerSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountsDialogue accountsDialogue = new AccountsDialogue();
                accountsDialogue.show(getActivity().getSupportFragmentManager(),"ACCOUNTS");
            }
        });

        accLedgerSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                vCard.setVisibility(View.GONE);
                new ReportCheck().execute();
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
                                if (!ad_id.isEmpty()) {
                                    new ReportCheck().execute();
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

                                        if (!ad_id.isEmpty()) {
                                            new ReportCheck().execute();
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
                                if (!ad_id.isEmpty()) {
                                    new ReportCheck().execute();
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

                                        if (!ad_id.isEmpty()) {
                                            new ReportCheck().execute();
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

        new Check().execute();

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

                AccData();
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
//                ArrayList<String> type = new ArrayList<>();
//                for(int i = 0; i < accLists.size(); i++) {
//                    type.add(accLists.get(i).getType());
//                }
//                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);
//
//                accLedgerSpinner.setAdapter(arrayAdapter);
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

    public class ReportCheck extends AsyncTask<Void, Void, Void> {

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

                accountLedgerAdapter = new AccountLedgerAdapter(accountLedgerLists, getContext(), AccountLedger.this);
                itemView.setAdapter(accountLedgerAdapter);

                if (accountLedgerLists.size() != 0) {

                    String openD = accountLedgerLists.get(0).getOpeningDebit();
                    if (openD.contains("-")) {
                        openD = openD.substring(1);
                        openD = "(-)  "+ openD;
                    }
                    openingDebit.setText(openD);

                    String openC = accountLedgerLists.get(0).getOpeningCredit();
                    if (openC.contains("-")) {
                        openC = openC.substring(1);
                        openC = "(-)  "+ openC;
                    }

                    openingCredit.setText(openC);

                    double totalD = 0;
                    double totalC = 0;
                    for (int i = 0; i < accountLedgerLists.size(); i++) {
                        if (accountLedgerLists.get(i).getDebit() != null) {
                            totalD = totalD + Double.parseDouble(accountLedgerLists.get(i).getDebit());
                        }
                        if (accountLedgerLists.get(i).getCredit() != null) {
                            totalC = totalC + Double.parseDouble(accountLedgerLists.get(i).getCredit());
                        }
                    }

                    double opD = Double.parseDouble(accountLedgerLists.get(0).getOpeningDebit());
                    double opC = Double.parseDouble(accountLedgerLists.get(0).getOpeningCredit());

                    totalD = totalD + opD;
                    totalC = totalC + opC;

                    String closeD = String.valueOf(totalD);
                    String closeC = String.valueOf(totalC);

                    if (closeD.contains("-")) {
                        closeD = closeD.substring(1);
                        closeD = "(-)  "+ closeD;
                    }

                    closingDebit.setText(closeD);

                    if (closeC.contains("-")) {
                        closeC = closeC.substring(1);
                        closeC = "(-)  "+ closeC;
                    }

                    closingCredit.setText(closeC);

                    String closeBal = accountLedgerLists.get(accountLedgerLists.size() - 1).getBalance();

                    if (closeBal.contains("-")) {
                        closeBal = closeBal.substring(1);
                        closeBal = "(-)  "+ closeBal;
                    }

                    closingBalance.setText(closeBal);
                }
                else  {

                    double opD = Double.parseDouble(openD);
                    double opC = Double.parseDouble(openC);
                    if (openD.contains("-")) {
                        openD = openD.substring(1);
                        openD = "(-)  "+ openD;
                    }
                    openingDebit.setText(openD);


                    if (openC.contains("-")) {
                        openC = openC.substring(1);
                        openC = "(-)  "+ openC;
                    }

                    openingCredit.setText(openC);



//                    String closeD = String.valueOf(opD);
//                    String closeC = String.valueOf(opC);
//
//                    if (closeD.contains("-")) {
//                        closeD = closeD.substring(1);
//                        closeD = "(-)  "+ closeD;
//                    }
//
                    closingDebit.setText(openD);
//
//                    if (closeC.contains("-")) {
//                        closeC = closeC.substring(1);
//                        closeC = "(-)  "+ closeC;
//                    }

                    closingCredit.setText(openC);

                    double cloB = opD+ opC;
                    String closeBal = String.valueOf(cloB);

                    if (closeBal.contains("-")) {
                        closeBal = closeBal.substring(1);
                        closeBal = "(-)  "+ closeBal;
                    }

                    closingBalance.setText(closeBal);

                }


                openingDate.setText(firstDate);
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

                        new ReportCheck().execute();
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

    public void AccData() {
        try {
            this.connection = createConnection();

            Statement stmt = connection.createStatement();

            accLists = new ArrayList<>();

            ResultSet resultSet2 = stmt.executeQuery("select ad_id, ad_name from acc_dtl");
            while (resultSet2.next()) {
                accLists.add(new ReceiveTypeList(resultSet2.getString(1),resultSet2.getString(2)));
            }

            resultSet2.close();

            connected = true;

            connection.close();

        }
        catch (Exception e) {

            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void ReportData() {
        try {
            this.connection = createConnection();
            accountLedgerLists = new ArrayList<>();

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
            if (ad_id != null) {
                if (ad_id.isEmpty()) {
                    ad_id = null;
                }
            }

            int adId = 0;
            if (ad_id != null) {
                adId = Integer.parseInt(ad_id);
            }
            CallableStatement callableStatement1 = connection.prepareCall("begin ? := GET_ACCOUNT_LEDGER_LIST(?,?,?); end;");
            callableStatement1.setString(2, firstDate);
            callableStatement1.setString(3,lastDate);
            callableStatement1.setInt(4,adId);
            callableStatement1.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement1.execute();

            ResultSet resultSet = (ResultSet) callableStatement1.getObject(1);


            int i = 0;
            int balanceAmnt = 0;
            while (resultSet.next()) {

                if (!resultSet.getString(1).equals("0")) {

                    int debitAmnt = resultSet.getInt(6);
                    int creditAmnt = resultSet.getInt(8);
                    String ahCode = resultSet.getString(13);

                    if (i == 0) {
                        int openDebit = resultSet.getInt(9);
                        int openCredit = resultSet.getInt(10);
                        if (openDebit == 0 && openCredit == 0) {
                            balanceAmnt = 0;
                        }
                        else if (openDebit == 0) {
                            balanceAmnt = openCredit;
                        }
                        else if (openCredit == 0) {
                            balanceAmnt = openDebit;
                        }
                    }

                    System.out.println("Previous Balance: "+balanceAmnt);
                    i++;

                    CallableStatement callableStatement2 = connection.prepareCall("begin ? := GET_LEDGER_BALANCE(?,?,?,?,?,?); end;");
                    callableStatement2.setString(2, firstDate);
                    callableStatement2.setInt(3,adId);
                    callableStatement2.setInt(4,debitAmnt);
                    callableStatement2.setInt(5,creditAmnt);
                    callableStatement2.setString(6,ahCode);
                    callableStatement2.setInt(7,balanceAmnt);
                    callableStatement2.registerOutParameter(1, OracleTypes.VARCHAR);
                    callableStatement2.execute();

                    balanceAmnt = callableStatement2.getInt(1);

                    System.out.println("New Balance: "+balanceAmnt);

                    callableStatement2.close();

                    accountLedgerLists.add(new AccountLedgerLists(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),
                            resultSet.getString(4),resultSet.getString(5),resultSet.getString(6),
                            resultSet.getString(7),resultSet.getString(8),resultSet.getString(9),
                            resultSet.getString(10),resultSet.getString(13),String.valueOf(balanceAmnt)));

                }
                else {
                    openD = resultSet.getString(9);
                    openC = resultSet.getString(10);
                }

            }

            resultSet.close();
            callableStatement1.close();


            if (firstDate == null) {
                firstDate = "";
            }
            if (lastDate == null) {
                lastDate = "";
            }
            if (ad_id == null) {
                ad_id = "";
            }

            connected = true;
            connection.close();


        }
        catch (Exception e) {
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}
package ttit.com.shuvo.terrainerp.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.rosemaryapp.amazingspinner.AmazingSpinner;

import java.text.DecimalFormat;
import java.util.ArrayList;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.WaitProgress;
import ttit.com.shuvo.terrainerp.adapters.BankBalanceAdapter;
import ttit.com.shuvo.terrainerp.arrayList.ChartValue;
import ttit.com.shuvo.terrainerp.arrayList.PayRcvSummAccountsList;
import ttit.com.shuvo.terrainerp.arrayList.ReceiveTypeList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BankBalance#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BankBalance extends Fragment implements BankBalanceAdapter.ClickedItem{

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

    AmazingSpinner payRcvSpinner;
    ArrayList<ReceiveTypeList> payRcvList;

    String payRcv = "";

    LinearLayout afterPayRcv;

    PieChart pieChart;
    ArrayList<PieEntry> pieEntries;
    ArrayList<ChartValue> chartValues;

    String toSePAYRCVMSG = "Party Wise View (Select Party Type from Chart)";
    String selectedTypeName = "";

    TextView selectedTypeText;
    TextView totalPayRcvText;
    TextView totalPayRcvBalance;

    TextView selectedPartyNameText;
    TextView selectedPartyNameAmnt;
    ImageView colorChange;
    LinearLayout afterSelectingParty;

    String partyID = "";
    int cc = 0;

    RecyclerView itemView;
    BankBalanceAdapter bankBalanceAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<PayRcvSummAccountsList> payRcvSummAccountsLists;
    TextView typeWiseTextChange;

    public BankBalance() {
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
     * @return A new instance of fragment BankBalance.
     */
    // TODO: Rename and change types and number of parameters
    public static BankBalance newInstance(String param1, String param2) {
        BankBalance fragment = new BankBalance();
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
        View view = inflater.inflate(R.layout.fragment_bank_balance, container, false);

        payRcvSpinner = view.findViewById(R.id.bank_balance_type_bb_spinner);
        afterPayRcv = view.findViewById(R.id.after_selecting_bb);
        afterPayRcv.setVisibility(View.GONE);

        pieChart = view.findViewById(R.id.pay_rcv_party_type_overview_bb);
        totalPayRcvText = view.findViewById(R.id.pay_or_rcv_text_changed_bb);
        totalPayRcvBalance = view.findViewById(R.id.total_pay_or_rcvble_amount_bb);
        selectedTypeText = view.findViewById(R.id.to_select_party_type_msg_bb);

        selectedPartyNameText = view.findViewById(R.id.selected_party_name_bb);
        selectedPartyNameAmnt = view.findViewById(R.id.selected_party_type_amount_bb);
        colorChange = view.findViewById(R.id.color_change_image_bb);
        afterSelectingParty = view.findViewById(R.id.after_selecting_party_type_bb);
        afterSelectingParty.setVisibility(View.GONE);
        Drawable background = colorChange.getBackground();

        itemView = view.findViewById(R.id.party_type_pay_rcv_report_view_bb);
        typeWiseTextChange = view.findViewById(R.id.type_wise_pay_or_rcv_text_bb);

        itemView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        itemView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(itemView.getContext(),DividerItemDecoration.VERTICAL);
        itemView.addItemDecoration(dividerItemDecoration);

        payRcvList = new ArrayList<>();
        payRcvSummAccountsLists = new ArrayList<>();

        payRcvList.add(new ReceiveTypeList("PAYABLE","Payable",""));
        payRcvList.add(new ReceiveTypeList("RECEIVABLE","Receivable",""));

        ArrayList<String> type = new ArrayList<>();
        for(int i = 0; i < payRcvList.size(); i++) {
            type.add(payRcvList.get(i).getType());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

        payRcvSpinner.setAdapter(arrayAdapter);

        payRcvSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                System.out.println(position+": "+name);

                for (int i = 0; i < payRcvList.size(); i++) {
                    if (name.equals(payRcvList.get(i).getType())) {
                        payRcv = payRcvList.get(i).getId();
                    }
                }

                afterPayRcv.setVisibility(View.GONE);
                selectedTypeText.setText(toSePAYRCVMSG);
                afterSelectingParty.setVisibility(View.GONE);
//                new Check().execute();
                getChartData();

            }
        });

        PieChartInit();

        pieChart.setClickable(true);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {


                float value = e.getY();

                partyID = e.getData().toString();
                System.out.println(partyID);

                int dataset = h.getDataSetIndex();
                float x = h.getX();

                cc = pieChart.getData().getDataSet().getColor((int) x);
                System.out.println(cc);


                System.out.println("dataSet: "+dataset);
                System.out.println("X: "+x);

                PieEntry pe = (PieEntry) e;
                selectedTypeName =  pe.getLabel();
                if (!selectedTypeName.equals("No Work Order")) {
                    selectedPartyNameText.setText(selectedTypeName);
                    selectedPartyNameText.setTextColor(cc);

                    System.out.println(selectedTypeName);
                    if (background instanceof ShapeDrawable) {
                        ((ShapeDrawable)background).getPaint().setColor(cc);
                    } else if (background instanceof GradientDrawable) {
                        ((GradientDrawable)background).setColor(cc);
                    } else if (background instanceof ColorDrawable) {
                        ((ColorDrawable)background).setColor(cc);
                    }

                    DecimalFormat formatter = new DecimalFormat("##,##,##,###.##");
                    String formatted = formatter.format(value);
                    selectedPartyNameAmnt.setText(formatted+" BDT");
                    selectedTypeText.setText("Party Wise View ("+selectedTypeName+" Selected)");
                    typeWiseTextChange.setText("TOTAL BANK "+payRcv+ " BALANCE");
                    afterSelectingParty.setVisibility(View.GONE);
//                    new ListCheck().execute();
                    getReportData();
                }



                //Toast.makeText(getContext(),String.valueOf(value),Toast.LENGTH_SHORT).show();




            }

            @Override
            public void onNothingSelected() {

                //pieChart.highlightValue(3.0f,0);
            }
        });

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
    @Override
    public void onCategoryClicked(int CategoryPosition) {

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
//                ChartData();
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
//                afterPayRcv.setVisibility(View.VISIBLE);
//                pieEntries = new ArrayList<>();
//                for (int i = 0 ; i < chartValues.size(); i++) {
//                    if (!chartValues.get(i).getValue().equals("0")) {
//                        pieEntries.add(new PieEntry(Float.parseFloat(chartValues.get(i).getValue()),chartValues.get(i).getType(),chartValues.get(i).getId()));
//                    }
//                }
//                if (pieEntries.size() == 0) {
//                    pieEntries.add(new PieEntry(1,"No Work Order",12403));
//                }
//
//                final int[] piecolors = new int[]{
//                        Color.parseColor("#0abde3"),
//                        Color.parseColor("#5f27cd"),
//                        Color.parseColor("#ff7979"),
//                        Color.parseColor("#7F8FA6"),
//                        Color.parseColor("#fdcb6e"),
//                        Color.parseColor("#2ecc71"),
//                        Color.parseColor("#b71540"),
//                        Color.parseColor("#0a3d62"),
//                        Color.parseColor("#636e72"),
//                        Color.parseColor("#01a3a4")};
//                pieChart.animateXY(1000, 1000);
//
//                PieDataSet dataSet = new PieDataSet(pieEntries, "");
//                pieChart.animateXY(1000, 1000);
//                pieChart.setEntryLabelColor(Color.TRANSPARENT);
//                pieChart.highlightValues(null);
//                pieChart.setExtraRightOffset(20);
//
//                PieData data = new PieData(dataSet);
//                String label = dataSet.getValues().get(0).getLabel();
//                System.out.println(label);
//                if (label.equals("No Work Order")) {
//                    dataSet.setValueTextColor(Color.TRANSPARENT);
//                } else {
//                    dataSet.setValueTextColor(Color.WHITE);
//                }
//                dataSet.setHighlightEnabled(true);
//                dataSet.setValueTextSize(12);
//                dataSet.setColors(ColorTemplate.createColors(piecolors));
//
//
//                pieChart.setData(data);
//                pieChart.invalidate();
//                double totalValue =  0;
//                for (int i = 0; i < pieEntries.size(); i++) {
//                    if (pieEntries.get(i).getLabel().equals("No Work Order")) {
//                        totalValue = 0;
//                    } else {
//                        totalValue = totalValue + (double) pieEntries.get(i).getValue();
//                    }
//                }
//
//                DecimalFormat formatter = new DecimalFormat("##,##,##,###.##");
//                String formatted = formatter.format(totalValue);
//
//                totalPayRcvBalance.setText(formatted+" BDT");
//                totalPayRcvText.setText("TOTAL BANK "+payRcv+ " AMOUNT");
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

//    public class ListCheck extends AsyncTask<Void, Void, Void> {
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
//                ReportData();
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
//                afterSelectingParty.setVisibility(View.VISIBLE);
//                bankBalanceAdapter = new BankBalanceAdapter(payRcvSummAccountsLists,getContext(),BankBalance.this);
//                itemView.setAdapter(bankBalanceAdapter);
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
//                        new ListCheck().execute();
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

//    public void ChartData() {
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//
//            chartValues = new ArrayList<>();
//
//
//            CallableStatement callableStatement1 = connection.prepareCall("begin ? := GET_BANK_BALANCE_LEDGER(?,?,?,?,?); end;");
//            callableStatement1.setString(2, payRcv);
//            callableStatement1.setString(3,null);
//            callableStatement1.setString(4,null);
//            callableStatement1.setString(5,null);
//            callableStatement1.setInt(6,1);
//            callableStatement1.registerOutParameter(1, OracleTypes.CURSOR);
//            callableStatement1.execute();
//
//            ResultSet resultSet = (ResultSet) callableStatement1.getObject(1);
//
//            while (resultSet.next()) {
//                chartValues.add(new ChartValue(resultSet.getString(1), resultSet.getString(2),resultSet.getString(3)));
//            }
//
//            resultSet.close();
//
//            callableStatement1.close();
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

    public void getChartData() {
        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;
        chartValues = new ArrayList<>();

        String url = "http://103.56.208.123:8001/apex/tterp/bankBalance/getBankLedger?bal_type="+payRcv+"&ad_flag=&unit=&project_id=&options=1";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        String bank_ledger = info.getString("bank_ledger");
                        JSONArray ledgerArray = new JSONArray(bank_ledger);
                        for (int j = 0; j < ledgerArray.length(); j++) {
                            JSONObject ledger_info = ledgerArray.getJSONObject(j);
                            String ad_flag = ledger_info.getString("ad_flag");
                            String party_type = ledger_info.getString("party_type");
                            String total_balance = ledger_info.getString("total_balance");

                            chartValues.add(new ChartValue(ad_flag, party_type,total_balance));
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

        requestQueue.add(stringRequest);
    }

    private void updateFragment() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                afterPayRcv.setVisibility(View.VISIBLE);
                pieEntries = new ArrayList<>();
                for (int i = 0 ; i < chartValues.size(); i++) {
                    if (!chartValues.get(i).getValue().equals("0")) {
                        pieEntries.add(new PieEntry(Float.parseFloat(chartValues.get(i).getValue()),chartValues.get(i).getType(),chartValues.get(i).getId()));
                    }
                }
                if (pieEntries.size() == 0) {
                    pieEntries.add(new PieEntry(1,"No Work Order",12403));
                }

                final int[] piecolors = new int[]{
                        Color.parseColor("#0abde3"),
                        Color.parseColor("#5f27cd"),
                        Color.parseColor("#ff7979"),
                        Color.parseColor("#7F8FA6"),
                        Color.parseColor("#fdcb6e"),
                        Color.parseColor("#2ecc71"),
                        Color.parseColor("#b71540"),
                        Color.parseColor("#0a3d62"),
                        Color.parseColor("#636e72"),
                        Color.parseColor("#01a3a4")};
                pieChart.animateXY(1000, 1000);

                PieDataSet dataSet = new PieDataSet(pieEntries, "");
                pieChart.animateXY(1000, 1000);
                pieChart.setEntryLabelColor(Color.TRANSPARENT);
                pieChart.highlightValues(null);
                pieChart.setExtraRightOffset(20);

                PieData data = new PieData(dataSet);
                String label = dataSet.getValues().get(0).getLabel();
                System.out.println(label);
                if (label.equals("No Work Order")) {
                    dataSet.setValueTextColor(Color.TRANSPARENT);
                } else {
                    dataSet.setValueTextColor(Color.WHITE);
                }
                dataSet.setHighlightEnabled(true);
                dataSet.setValueTextSize(12);
                dataSet.setColors(ColorTemplate.createColors(piecolors));


                pieChart.setData(data);
                pieChart.invalidate();
                double totalValue =  0;
                for (int i = 0; i < pieEntries.size(); i++) {
                    if (pieEntries.get(i).getLabel().equals("No Work Order")) {
                        totalValue = 0;
                    } else {
                        totalValue = totalValue + (double) pieEntries.get(i).getValue();
                    }
                }

                DecimalFormat formatter = new DecimalFormat("##,##,##,###.##");
                String formatted = formatter.format(totalValue);

                totalPayRcvBalance.setText(formatted+" BDT");
                totalPayRcvText.setText("TOTAL BANK "+payRcv+ " AMOUNT");
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

                    getChartData();
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

                getChartData();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }

//    public void ReportData() {
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//
//            payRcvSummAccountsLists = new ArrayList<>();
//
//
//            CallableStatement callableStatement1 = connection.prepareCall("begin ? := GET_BANK_BALANCE_LEDGER(?,?,?,?,?); end;");
//            callableStatement1.setString(2, payRcv);
//            callableStatement1.setString(3,partyID);
//            callableStatement1.setString(4,null);
//            callableStatement1.setString(5,null);
//            callableStatement1.setInt(6,2);
//            callableStatement1.registerOutParameter(1, OracleTypes.CURSOR);
//            callableStatement1.execute();
//
//            ResultSet resultSet = (ResultSet) callableStatement1.getObject(1);
//
//            int i = 0;
//            while (resultSet.next()) {
//                i++;
//                payRcvSummAccountsLists.add(new PayRcvSummAccountsList(String.valueOf(i),resultSet.getString(1), resultSet.getString(3),resultSet.getString(4),
//                        resultSet.getString(5),resultSet.getString(2),resultSet.getString(6)));
//            }
//
//            resultSet.close();
//
//            callableStatement1.close();
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

        payRcvSummAccountsLists = new ArrayList<>();

        String url = "http://103.56.208.123:8001/apex/tterp/bankBalance/getBankLedger?bal_type="+payRcv+"&ad_flag="+partyID+"&unit=&project_id=&options=2";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        String bank_ledger = info.getString("bank_ledger");
                        JSONArray ledgerArray = new JSONArray(bank_ledger);
                        for (int j = 0; j < ledgerArray.length(); j++) {
                            JSONObject ledger_info = ledgerArray.getJSONObject(j);

                            String lg_ad_id = ledger_info.getString("lg_ad_id");
                            String ad_code = ledger_info.getString("ad_code");
                            String ad_name = ledger_info.getString("ad_name");
                            String bal = ledger_info.getString("bal");
                            String balance_type = ledger_info.getString("balance_type");
                            String ad_flag = ledger_info.getString("ad_flag");

                            payRcvSummAccountsLists.add(new PayRcvSummAccountsList(String.valueOf(j+1),lg_ad_id,
                                    ad_name,bal,balance_type,ad_code,ad_flag));
                        }
                    }
                }
                connected = true;
                updateLayout();
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateLayout();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateLayout();
        });

        requestQueue.add(stringRequest);
    }

    private void updateLayout() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                afterSelectingParty.setVisibility(View.VISIBLE);
                bankBalanceAdapter = new BankBalanceAdapter(payRcvSummAccountsLists,getContext(),BankBalance.this);
                itemView.setAdapter(bankBalanceAdapter);
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
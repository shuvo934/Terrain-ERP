package ttit.com.shuvo.icomerp.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
import java.util.List;
import java.util.Locale;

import oracle.jdbc.driver.OracleTypes;
import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.WaitProgress;
import ttit.com.shuvo.icomerp.arrayList.ChartValue;
import ttit.com.shuvo.icomerp.arrayList.ItemQtyList;
import ttit.com.shuvo.icomerp.arrayList.ReceiveTypeList;
import ttit.com.shuvo.icomerp.arrayList.SupplierList;
import ttit.com.shuvo.icomerp.dialogues.SupplierDialogue;

import static ttit.com.shuvo.icomerp.OracleConnection.createConnection;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link ReceiveFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class ReceiveFragment extends Fragment {

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

    LinearLayout afterCat;
    LinearLayout afterSubCat;
    LinearLayout afterItem;

    TextView selectedCatName;
    TextView selectedSubCName;
    TextView selectedItemName;
    ImageView colorChangeSub;
    ImageView colorChange;
    ImageView colorChangeItemAmnt;
    ImageView colorChangeItemQty;
    TextView selectedCatAmnt;
    TextView selectedSubCatAmnt;
    TextView selectedItemAmnt;
    TextView selectedItemQty;
    TextInputEditText beginDate;
    TextInputEditText endDate;
    public static TextInputLayout supplierLayRe;
    public static TextInputEditText supplierRe;
    TextView daterange;
    TextView toSeCat;
    TextView toSeSubCat;
    TextView toSeItem;
    TextView totalRA;

    CardView qtyCard;

    AmazingSpinner spinner;
    TextInputLayout spinnerLayout;

    PieChart pieChart;
    ArrayList<PieEntry> NoOfEmp;
    ArrayList<ChartValue> categoryValues;

    BarChart barChart;
    ArrayList<ChartValue> subCategoryValues;
    ArrayList<String> barItem;
    ArrayList<String> barValue;
    ArrayList<BarEntry> barEntries;

    BarChart itemBarChart;
    ArrayList<ChartValue> itemValues;
    ArrayList<String> itemBarItem;
    ArrayList<String> itemBarValue;
    ArrayList<BarEntry> itemBarEntries;
    ArrayList<ItemQtyList> itemQtyLists;

    private int mYear, mMonth, mDay;

    String firstDate = "";
    String lastDate = "";
    String selectedCategoryName = "";
    String selectedSubCatName = "";
    String selectedItName = "";
    String selectedItQty = "";
    String status_flag = "";
    String categoryId = "";
    String subCatrgoryId = "";
    String itemId = "";
    String itemType = "";
    int cc = 0;
    int cc1 = 0;
    int cc2 = 0;

    public static int fromRE_supp = 0;
    public static String supplier_ad_id_re = "";
    public static ArrayList<SupplierList> supplierListsRe;

    public ArrayList<ReceiveTypeList> receiveTypeLists;

    private Connection connection;

    String toSeCatMSG = "Category View (Select Category from Chart)";
    String toSeSubCatMSG = "Sub Category View (Select Sub Category from Chart)";
    String toSeItemMSG = "Item View (Select Item from Chart)";

    public ReceiveFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment ReceiveFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static ReceiveFragment newInstance(String param1, String param2) {
//        ReceiveFragment fragment = new ReceiveFragment();
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
        View view = inflater.inflate(R.layout.fragment_receive, container, false);

        afterCat = view.findViewById(R.id.after_selecting_category_re);
        afterSubCat = view.findViewById(R.id.after_selecting_sub_category_re);
        afterItem = view.findViewById(R.id.after_selecting_item_re);

        beginDate = view.findViewById(R.id.begin_date_re);
        endDate = view.findViewById(R.id.end_date_re);
        daterange = view.findViewById(R.id.date_range_msg_re);
        supplierLayRe = view.findViewById(R.id.select_supplier_layout_re);
        supplierRe = view.findViewById(R.id.select_supplier_re);
        spinner = view.findViewById(R.id.receive_type_spinner);
        spinnerLayout = view.findViewById(R.id.spinner_layout_re);

        toSeCat = view.findViewById(R.id.to_select_category_msg_re);
        pieChart = view.findViewById(R.id.receive_category_overview);
        totalRA = view.findViewById(R.id.total_receive_amount);

        selectedCatName = view.findViewById(R.id.selected_cat_name_re);
        selectedCatAmnt = view.findViewById(R.id.selected_cat_amount_re);
        colorChange = view.findViewById(R.id.color_change_image_re);
        toSeSubCat = view.findViewById(R.id.sub_category_selected_msg_re);

        selectedSubCName = view.findViewById(R.id.selected_sub_cat_name_re);
        colorChangeSub = view.findViewById(R.id.color_change_image_for_sub_re);
        selectedSubCatAmnt = view.findViewById(R.id.selected_sub_cat_amount_re);
        toSeItem = view.findViewById(R.id.item_selected_msg_re);

        selectedItemName = view.findViewById(R.id.selected_item_name_re);
        selectedItemAmnt = view.findViewById(R.id.selected_item_amount_re);
        selectedItemQty = view.findViewById(R.id.selected_item_qty_re);
        colorChangeItemAmnt = view.findViewById(R.id.color_change_image_for_item_amnt_re);
        colorChangeItemQty = view.findViewById(R.id.color_change_image_for_item_qty_re);

        qtyCard = view.findViewById(R.id.qty_card_receive);

        barChart = view.findViewById(R.id.receive_sub_category_overview);
        itemBarChart = view.findViewById(R.id.receive_item_overview);

        supplierListsRe = new ArrayList<>();
        categoryValues = new ArrayList<>();
        receiveTypeLists = new ArrayList<>();
        itemQtyLists = new ArrayList<>();

        Drawable background = colorChange.getBackground();
        Drawable drawable = colorChangeSub.getBackground();
        Drawable itemAmntDraw = colorChangeItemAmnt.getBackground();
        Drawable itemQtyDraw = colorChangeItemQty.getBackground();

        // Getting Date
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM-yy",Locale.getDefault());

        firstDate = simpleDateFormat.format(c);
        firstDate = "01-"+firstDate;
        lastDate = df.format(c);

        beginDate.setText(firstDate);
        endDate.setText(lastDate);

        //selecting Supplier
        supplierRe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromRE_supp = 1;
                SupplierDialogue supplierDialogue = new SupplierDialogue();
                supplierDialogue.show(getActivity().getSupportFragmentManager(),"SS" );
            }
        });

        supplierRe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                new CategoryCheck().execute();
                afterCat.setVisibility(View.GONE);
                afterSubCat.setVisibility(View.GONE);

                toSeCat.setText(toSeCatMSG);
                toSeSubCat.setText(toSeSubCatMSG);

                afterItem.setVisibility(View.GONE);
                toSeItem.setText(toSeItemMSG);
            }
        });

        // Selecting receive type
        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                System.out.println(position+": "+name);
                spinnerLayout.setHint("Receive Type");
                for (int i = 0; i <receiveTypeLists.size(); i++) {
                    if (name.equals(receiveTypeLists.get(i).getType())) {
                        status_flag = receiveTypeLists.get(i).getId();
                    }
                }
                System.out.println(status_flag);
                new CategoryCheck().execute();
                afterCat.setVisibility(View.GONE);
                afterSubCat.setVisibility(View.GONE);
                toSeCat.setText(toSeCatMSG);
                toSeSubCat.setText(toSeSubCatMSG);
                afterItem.setVisibility(View.GONE);
                toSeItem.setText(toSeItemMSG);
            }
        });

        // pie chart initialization
        PieChartInit();

        pieChart.setClickable(true);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {


                float value = e.getY();

                categoryId = e.getData().toString();
                System.out.println(categoryId);

                int dataset = h.getDataSetIndex();
                float x = h.getX();

                cc = pieChart.getData().getDataSet().getColor((int) x);
                System.out.println(cc);


                System.out.println("dataSet: "+dataset);
                System.out.println("X: "+x);

                PieEntry pe = (PieEntry) e;
                selectedCategoryName =  pe.getLabel();
                if (!selectedCategoryName.equals("No Receive")) {
                    selectedCatName.setText(selectedCategoryName);
                    selectedCatName.setTextColor(cc);

                    System.out.println(selectedCategoryName);
                    if (background instanceof ShapeDrawable) {
                        ((ShapeDrawable)background).getPaint().setColor(cc);
                    } else if (background instanceof GradientDrawable) {
                        ((GradientDrawable)background).setColor(cc);
                    } else if (background instanceof ColorDrawable) {
                        ((ColorDrawable)background).setColor(cc);
                    }

                    selectedCatAmnt.setText(value+" BDT");
                    toSeCat.setText("Category View ("+selectedCategoryName+" Selected)");
                    toSeSubCat.setText(toSeSubCatMSG);
                    toSeItem.setText(toSeItemMSG);

                    new SubCategoryCheck().execute();
                }

            }

            @Override
            public void onNothingSelected() {

                //pieChart.highlightValue(3.0f,0);
            }
        });

        // sub category chart
        BarChartInit();

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                float value = e.getY();

                subCatrgoryId = e.getData().toString();
                System.out.println(subCatrgoryId);

                float x = h.getX();

                cc1 = barChart.getBarData().getDataSetByIndex(0).getColor((int) x);
                System.out.println(cc1);
                cc1 = ColorUtils.blendARGB(cc1,Color.WHITE,0.3f);

                BarEntry br = (BarEntry) e;
                float ss = br.getX();
                selectedSubCatName = barItem.get((int)ss);
                System.out.println(selectedSubCatName);

                selectedSubCName.setText(selectedSubCatName);
                selectedSubCName.setTextColor(cc1);

                if (drawable instanceof ShapeDrawable) {
                    ((ShapeDrawable)drawable).getPaint().setColor(cc1);
                } else if (drawable instanceof GradientDrawable) {
                    ((GradientDrawable)drawable).setColor(cc1);
                } else if (drawable instanceof ColorDrawable) {
                    ((ColorDrawable)drawable).setColor(cc1);
                }

                selectedSubCatAmnt.setText(value+" BDT");
                toSeSubCat.setText("Sub Category View ("+selectedSubCatName+" Selected)");
                afterSubCat.setVisibility(View.VISIBLE);
                toSeItem.setText(toSeItemMSG);

                new ItemCheck().execute();

            }

            @Override
            public void onNothingSelected() {

            }
        });

        ItemBarChartInit();

        itemBarChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                float value = e.getY();

                itemId = e.getData().toString();
                System.out.println(itemId);

                float x = h.getX();

                cc2 = itemBarChart.getBarData().getDataSetByIndex(0).getColor((int) x);
                System.out.println(cc2);
                cc2 = ColorUtils.blendARGB(cc1,Color.WHITE,0.2f);

                BarEntry br = (BarEntry) e;
                float ss = br.getX();
                selectedItName = itemBarItem.get((int)ss);
                System.out.println(selectedItName);

                selectedItemName.setText(selectedItName);
                selectedItemName.setTextColor(cc2);

                if (itemAmntDraw instanceof ShapeDrawable) {
                    ((ShapeDrawable)itemAmntDraw).getPaint().setColor(cc2);
                } else if (itemAmntDraw instanceof GradientDrawable) {
                    ((GradientDrawable)itemAmntDraw).setColor(cc2);
                } else if (itemAmntDraw instanceof ColorDrawable) {
                    ((ColorDrawable)itemAmntDraw).setColor(cc2);
                }

                if (itemQtyDraw instanceof ShapeDrawable) {
                    ((ShapeDrawable)itemQtyDraw).getPaint().setColor(cc2);
                } else if (itemQtyDraw instanceof GradientDrawable) {
                    ((GradientDrawable)itemQtyDraw).setColor(cc2);
                } else if (itemQtyDraw instanceof ColorDrawable) {
                    ((ColorDrawable)itemQtyDraw).setColor(cc2);
                }

                selectedItemAmnt.setText(value+" BDT");
                toSeItem.setText("Item View ("+selectedItName+" Selected)");

                afterItem.setVisibility(View.VISIBLE);
                for (int i = 0; i < itemQtyLists.size(); i++) {
                    if (itemId.equals(itemQtyLists.get(i).getId())) {
                        selectedItQty = itemQtyLists.get(i).getQty();
                        itemType = itemQtyLists.get(i).getType();
                    }
                }
                selectedItemQty.setText(selectedItQty);
                if (itemType.equals("1")) {
                    qtyCard.setVisibility(View.VISIBLE);
                } else if (itemType.equals("2")) {
                    qtyCard.setVisibility(View.GONE);
                }




            }

            @Override
            public void onNothingSelected() {

            }
        });

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
                                new CategoryCheck().execute();
                                afterCat.setVisibility(View.GONE);
                                afterSubCat.setVisibility(View.GONE);
                                toSeCat.setText(toSeCatMSG);
                                toSeSubCat.setText(toSeSubCatMSG);
                                afterItem.setVisibility(View.GONE);
                                toSeItem.setText(toSeItemMSG);
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

                                        new CategoryCheck().execute();
                                        afterCat.setVisibility(View.GONE);
                                        afterSubCat.setVisibility(View.GONE);
                                        toSeCat.setText(toSeCatMSG);
                                        toSeSubCat.setText(toSeSubCatMSG);
                                        afterItem.setVisibility(View.GONE);
                                        toSeItem.setText(toSeItemMSG);

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
                                new CategoryCheck().execute();
                                afterCat.setVisibility(View.GONE);
                                afterSubCat.setVisibility(View.GONE);
                                toSeCat.setText(toSeCatMSG);
                                toSeSubCat.setText(toSeSubCatMSG);
                                afterItem.setVisibility(View.GONE);
                                toSeItem.setText(toSeItemMSG);
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

                                        new CategoryCheck().execute();
                                        afterCat.setVisibility(View.GONE);
                                        afterSubCat.setVisibility(View.GONE);
                                        toSeCat.setText(toSeCatMSG);
                                        toSeSubCat.setText(toSeSubCatMSG);
                                        afterItem.setVisibility(View.GONE);
                                        toSeItem.setText(toSeItemMSG);

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

        return  view;

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

    public void BarChartInit() {

        subCategoryValues = new ArrayList<>();
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
        barChart.setTouchEnabled(true);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setClickable(true);
        barChart.setHighlightPerTapEnabled(true);
        barChart.setHighlightPerDragEnabled(false);
        //salaryChart.setOnTouchListener(null);

        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setAxisMinValue(0);
        barChart.getLegend().setEnabled(false);


    }

    public void ItemBarChartInit() {

        itemValues = new ArrayList<>();
        itemBarItem = new ArrayList<>();
        itemBarValue = new ArrayList<>();
        itemBarEntries = new ArrayList<>();

        itemBarChart.getDescription().setEnabled(false);
        itemBarChart.setPinchZoom(false);

        itemBarChart.setDrawBarShadow(false);
        itemBarChart.setDrawGridBackground(false);

        itemBarChart.getAxisLeft().setDrawGridLines(true);

        XAxis xAxis = itemBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);

        xAxis.setDrawGridLines(false);

        itemBarChart.getLegend().setFormToTextSpace(10);
        itemBarChart.getLegend().setStackSpace(10);
        itemBarChart.getLegend().setYOffset(10);
        itemBarChart.setExtraOffsets(0,0,0,20);

        // zoom and touch disabled
        itemBarChart.setScaleEnabled(false);
        itemBarChart.setTouchEnabled(true);
        itemBarChart.setDoubleTapToZoomEnabled(false);
        itemBarChart.setClickable(true);
        itemBarChart.setHighlightPerTapEnabled(true);
        itemBarChart.setHighlightPerDragEnabled(false);
        //salaryChart.setOnTouchListener(null);

        itemBarChart.getAxisRight().setEnabled(false);
        itemBarChart.getAxisLeft().setAxisMinValue(0);
        itemBarChart.getLegend().setEnabled(false);


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

                SupplierData();
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

                //String[] type = new String[] {"Approved", "Pending","Both"};
                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < receiveTypeLists.size(); i++) {
                    type.add(receiveTypeLists.get(i).getType());
                }

                ArrayAdapter<String> adapter =
                        new ArrayAdapter<String>(
                                mContext,
                                R.layout.dropdown_menu_popup_item,
                                type);

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                spinner.setAdapter(arrayAdapter);

                new CategoryCheck().execute();

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

    public class CategoryCheck extends AsyncTask<Void, Void, Void> {

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

                NoOfEmp = new ArrayList<>();
                for (int i = 0 ; i < categoryValues.size(); i++) {
                    if (!categoryValues.get(i).getValue().equals("0")) {
                        NoOfEmp.add(new PieEntry(Float.parseFloat(categoryValues.get(i).getValue()),categoryValues.get(i).getType(),categoryValues.get(i).getId()));
                    }
                }
                if (NoOfEmp.size() == 0) {
                    NoOfEmp.add(new PieEntry(1,"No Receive",12403));
                }
//                NoOfEmp.add(new PieEntry(5500f,"Fluids",10));
//                NoOfEmp.add(new PieEntry(3000f,"Spare Parts",11));
//                NoOfEmp.add(new PieEntry(8000f,"Filter",12));
//                NoOfEmp.add(new PieEntry(7000f,"Printers",13));

                final int[] piecolors = new int[]{
                        Color.parseColor("#f1c40f"),
                        Color.parseColor("#9b59b6"),
                        Color.parseColor("#3498db"),
                        Color.parseColor("#2ecc71"),
                        Color.parseColor("#b71540"),
                        Color.parseColor("#0a3d62"),
                        Color.parseColor("#636e72"),
                        Color.parseColor("#01a3a4")};
                pieChart.animateXY(1000, 1000);

                PieDataSet dataSet = new PieDataSet(NoOfEmp, "");
                pieChart.animateXY(1000, 1000);
                pieChart.setEntryLabelColor(Color.TRANSPARENT);
                pieChart.highlightValues(null);
                pieChart.setExtraRightOffset(20);

                PieData data = new PieData(dataSet);
                String label = dataSet.getValues().get(0).getLabel();
                System.out.println(label);
                if (label.equals("No Receive")) {
                    dataSet.setValueTextColor(Color.TRANSPARENT);
                } else {
                    dataSet.setValueTextColor(Color.WHITE);
                }
                dataSet.setHighlightEnabled(true);
                dataSet.setValueTextSize(12);
                dataSet.setColors(ColorTemplate.createColors(piecolors));


                pieChart.setData(data);
                pieChart.invalidate();
                int totalValue =  0;
                for (int i = 0; i < NoOfEmp.size(); i++) {
                    if (NoOfEmp.get(i).getLabel().equals("No Receive")) {
                        totalValue = 0;
                    } else {
                        totalValue = totalValue + (int) NoOfEmp.get(i).getValue();
                    }
                }
                totalRA.setText(totalValue+" BDT");

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

                        new CategoryCheck().execute();
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

                for (int i = 0; i < subCategoryValues.size(); i++) {
                    barItem.add(subCategoryValues.get(i).getType());
                }

                for (int i = 0; i < subCategoryValues.size(); i++) {
                    barEntries.add(new BarEntry(i,Float.parseFloat(subCategoryValues.get(i).getValue()), subCategoryValues.get(i).getId()));
                }


                BarDataSet bardataset = new BarDataSet(barEntries, "Months");
                barChart.animateY(1000);
                barChart.highlightValues(null);

                BarData data1 = new BarData(bardataset);
                if (cc == 0) {
                    bardataset.setColors(ColorTemplate.VORDIPLOM_COLORS);
                } else {
                    //int newCC = darkenColor(cc,0.8f);

                    bardataset.setColor(cc);
                }

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

                afterCat.setVisibility(View.VISIBLE);
                afterSubCat.setVisibility(View.GONE);
                afterItem.setVisibility(View.GONE);

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

                for (int i = 0; i < itemValues.size(); i++) {
                    itemBarItem.add(itemValues.get(i).getType());
                }

                for (int i = 0; i < itemValues.size(); i++) {
                    itemBarEntries.add(new BarEntry(i,Float.parseFloat(itemValues.get(i).getValue()), itemValues.get(i).getId()));
                }

                System.out.println("Label Size " +itemBarItem.size());

                BarDataSet bardataset = new BarDataSet(itemBarEntries, "Monthss");
                itemBarChart.animateY(1000);
                itemBarChart.highlightValues(null);

                BarData data1 = new BarData(bardataset);
                if (cc1 == 0) {
                    bardataset.setColors(ColorTemplate.VORDIPLOM_COLORS);
                } else {
                    //int newCC = darkenColor(cc,0.8f);

                    bardataset.setColor(cc1);
                }

                bardataset.setBarBorderColor(Color.DKGRAY);
                bardataset.setValueTextSize(12);
                if (itemBarItem.size() > 7) {
                    itemBarChart.getXAxis().setLabelRotationAngle(90);
                }
                itemBarChart.getXAxis().setTextSize(8);

                itemBarChart.setData(data1);
                itemBarChart.invalidate();

                //itemBarChart.getXAxis().setValueFormatter(new MyAxisValueFormatterItem(itemBarItem));
                itemBarChart.getXAxis().setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getAxisLabel(float value, AxisBase axis) {
                        //System.out.println(value);
                        float size = itemBarItem.size() - 1;
                        if (value <= size) {
                            return (itemBarItem.get((int) value));
                        } else {
                            return super.getAxisLabel(value, axis);
                        }

                    }
                });


                afterSubCat.setVisibility(View.VISIBLE);
                afterItem.setVisibility(View.GONE);

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

    public void SupplierData() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();



            Statement stmt = connection.createStatement();

            supplierListsRe = new ArrayList<>();
            receiveTypeLists = new ArrayList<>();


            ResultSet resultSet = stmt.executeQuery("SELECT ALL ACC_DTL.AD_ID,\n" +
                    "           ACC_DTL.AD_CODE,\n" +
                    "           ACC_DTL.AD_NAME,\n" +
                    "           ACC_DTL.AD_SHORT_NAME\n" +
                    "  FROM ACC_DTL\n" +
                    " WHERE ACC_DTL.AD_FLAG = 7 AND AD_PHARMACY_FLAG = 0");

            while (resultSet.next()) {
                supplierListsRe.add(new SupplierList(resultSet.getString(1), resultSet.getString(2),resultSet.getString(3),resultSet.getString(4)));
            }

            supplierListsRe.add(new SupplierList("","1234567890","All Suppliers","All"));

            ResultSet resultSet1 = stmt.executeQuery("SELECT DISTINCT\n" +
                    "       CASE RECEIVE_MST.RM_TYPE_FLAG\n" +
                    "          WHEN 1 THEN 'N/A'\n" +
                    "          WHEN 2 THEN 'PO Rcv'\n" +
                    "          WHEN 3 THEN 'CLS'\n" +
                    "          WHEN 4 THEN 'In Trans. Rcv'\n" +
                    "          WHEN 5 THEN 'Out Trans. Rcv'\n" +
                    "          WHEN 6 THEN 'Disposal Rcv'\n" +
                    "          WHEN 8 THEN 'Direct Rcv'\n" +
                    "          ELSE NULL\n" +
                    "       END\n" +
                    "          RCV_TYPE,\n" +
                    "       RECEIVE_MST.RM_TYPE_FLAG\n" +
                    "  FROM RECEIVE_MST");

            while (resultSet1.next()) {
                receiveTypeLists.add(new ReceiveTypeList(resultSet1.getString(2),resultSet1.getString(1)));
            }
            receiveTypeLists.add(new ReceiveTypeList("","All Types"));


            connected = true;

            connection.close();

        }
        catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void CategoryData() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            categoryValues = new ArrayList<>();
            Statement stmt = connection.createStatement();
            if (firstDate.isEmpty()) {
                firstDate = null;
            }
            if (lastDate.isEmpty()) {
                lastDate = null;
            }
            if (status_flag.isEmpty()) {
                status_flag = null;
            }
            if (supplier_ad_id_re.isEmpty()) {
                supplier_ad_id_re = null;
            }

            CallableStatement callableStatement = connection.prepareCall("begin ? := GET_CATEGORY_WISE_RCV_AMT(?,?,?,?); end;");
            callableStatement.setString(2,firstDate);
            callableStatement.setString(3,lastDate);
            callableStatement.setString(4,supplier_ad_id_re);
            callableStatement.setString(5,status_flag);
            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement.execute();

            ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
//            while (resultSet.next()) {
//                String id = resultSet.getString(1);
//                String code = resultSet.getString(2);
//                String name = resultSet.getString(3);
//                String basic = resultSet.getString(4);
//
//                System.out.println("ID: "+ id);
//                System.out.println("Code: "+ code);
//                System.out.println("Name: "+name);
//                System.out.println("Salary: "+basic);
//
//            }

            while (resultSet.next()) {
                categoryValues.add(new ChartValue(resultSet.getString(1), resultSet.getString(2),resultSet.getString(3)));
            }



            callableStatement.close();



            connected = true;


            if (firstDate == null) {
                firstDate = "";
            }
            if (lastDate == null) {
                lastDate = "";
            }
            if (status_flag == null) {
                status_flag = "";
            }
            if (supplier_ad_id_re == null) {
                supplier_ad_id_re = "";
            }

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

            subCategoryValues = new ArrayList<>();
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
            if (status_flag.isEmpty()) {
                status_flag = null;
            }
            if (supplier_ad_id_re.isEmpty()) {
                supplier_ad_id_re = null;
            }

            CallableStatement callableStatement = connection.prepareCall("begin ? := GET_SUBCATEGORY_WISE_RCV_AMT(?,?,?,?,?); end;");
            callableStatement.setInt(2,Integer.parseInt(categoryId));
            callableStatement.setString(3,firstDate);
            callableStatement.setString(4,lastDate);
            callableStatement.setString(5,supplier_ad_id_re);
            callableStatement.setString(6,status_flag);
            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement.execute();

            ResultSet resultSet = (ResultSet) callableStatement.getObject(1);



            while (resultSet.next()) {
                subCategoryValues.add(new ChartValue(resultSet.getString(1), resultSet.getString(2),resultSet.getString(3)));
            }
            callableStatement.close();
            connected = true;


            if (firstDate == null) {
                firstDate = "";
            }
            if (lastDate == null) {
                lastDate = "";
            }
            if (status_flag == null) {
                status_flag = "";
            }
            if (supplier_ad_id_re == null) {
                supplier_ad_id_re = "";
            }

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

            itemValues = new ArrayList<>();
            itemBarItem = new ArrayList<>();
            itemBarValue = new ArrayList<>();
            itemBarEntries = new ArrayList<>();
            itemQtyLists = new ArrayList<>();

            Statement stmt = connection.createStatement();
            if (firstDate.isEmpty()) {
                firstDate = null;
            }
            if (lastDate.isEmpty()) {
                lastDate = null;
            }
            if (status_flag.isEmpty()) {
                status_flag = null;
            }
            if (supplier_ad_id_re.isEmpty()) {
                supplier_ad_id_re = null;
            }


            CallableStatement callableStatement = connection.prepareCall("begin ? := GET_ITEM_WISE_RCV_AMT(?,?,?,?,?); end;");
            callableStatement.setInt(2,Integer.parseInt(subCatrgoryId));
            callableStatement.setString(3,firstDate);
            callableStatement.setString(4,lastDate);
            callableStatement.setString(5,supplier_ad_id_re);
            callableStatement.setString(6,status_flag);
            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement.execute();

            ResultSet resultSet = (ResultSet) callableStatement.getObject(1);

            while (resultSet.next()) {
                itemValues.add(new ChartValue(resultSet.getString(1), resultSet.getString(2),resultSet.getString(3)));
                itemQtyLists.add(new ItemQtyList(resultSet.getString(1),resultSet.getString(4)+" "+resultSet.getString(5),resultSet.getString(6)));
            }

            callableStatement.close();
            connected = true;


            if (firstDate == null) {
                firstDate = "";
            }
            if (lastDate == null) {
                lastDate = "";
            }
            if (status_flag == null) {
                status_flag = "";
            }
            if (supplier_ad_id_re == null) {
                supplier_ad_id_re = "";
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
package ttit.com.shuvo.icomerp.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import oracle.jdbc.driver.OracleTypes;
import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.WaitProgress;
import ttit.com.shuvo.icomerp.adapters.ItemAdapter;
import ttit.com.shuvo.icomerp.adapters.WoPoAdapter;
import ttit.com.shuvo.icomerp.arrayList.ChartValue;
import ttit.com.shuvo.icomerp.arrayList.ItemViewList;
import ttit.com.shuvo.icomerp.arrayList.SupplierList;
import ttit.com.shuvo.icomerp.arrayList.WoPoLists;
import ttit.com.shuvo.icomerp.dialogues.ItemDetailsRelation;
import ttit.com.shuvo.icomerp.dialogues.SupplierDialogue;
import ttit.com.shuvo.icomerp.dialogues.WoPoDetails;

import static ttit.com.shuvo.icomerp.OracleConnection.createConnection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RelationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RelationsFragment extends Fragment implements ItemAdapter.ClickedItem, WoPoAdapter.ClickedItem {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int mYear, mMonth, mDay;

    CardView prRecvCard;
    CardView woPoAmntCard;

    CardView woPoBillRela;
    CardView woPoRcvRela;

    CardView woPoCard;
    CardView mrReceiveCard;
    CardView balanceCard;

    CardView billPayCard;
    CardView billPayableCard;
    CardView vatCard;
    CardView billAllCard;

    LinearLayout afterRelation;
    LinearLayout afterCatSelection;
    LinearLayout aftercatSelectionItem;
    LinearLayout afterWoPoSelect;
    LinearLayout afterWopOlistSelect;

    AmazingSpinner woPoType;

    AmazingSpinner relationType;
    TextInputEditText beginDate;
    TextInputEditText endDate;
    TextView daterange;
    public static TextInputLayout supplierLayRelation;
    public static TextInputEditText supplierRelation;
    public static int fromRela_supp = 0;
    public static String supplier_ad_id_relation = "";
    ImageView workOrderCir;
    ImageView receCircl;
    TextView workOrderTotal;
    TextView receTotal;
    TextView noItem;
    TextView whoseItem;
    TextView receiveName;
    TextView orderName;
    TextView noWOPO;

    TextInputEditText searchItem;
    TextInputLayout searchItemLay;
    RecyclerView itemView;
    ItemAdapter itemAdapter;
    RecyclerView.LayoutManager layoutManager;

    TextInputEditText searchWOPO;
    RecyclerView woPoView;
    RecyclerView.LayoutManager woPolayoutManager;
    WoPoAdapter woPoAdapter;

    ArrayList<ItemViewList> itemViewLists;
    ArrayList<ItemViewList> filteredList;

    ImageView billPayCircle;
    ImageView billPayableCircle;
    ImageView vatCircle;
    ImageView billAllCircle;

    ImageView workOrderCirCat;
    ImageView receCirclCat;
    ImageView balanceCircle;
    ImageView purchaseCircle;
    TextView workOrderTotalCat;
    TextView receTotalCat;
    TextView selectedCat;
    TextView balanceTotal;
    TextView purchaseReTotal;
    TextView billAll;

    TextView billpay;
    TextView billPayable;
    TextView vat;

    TextView toSeCat;
    TextView toSeSubCat;

    PieChart pieChart;
    ArrayList<PieEntry> NoOfEmp;

    BarChart categoryMultiChart;
    ArrayList<BarEntry> workOrders;
    ArrayList<BarEntry> receives;
    ArrayList<String> shortCode;

    BarChart subCatMultiChart;
    ArrayList<BarEntry> subWorkOrders;
    ArrayList<BarEntry> subReceives;
    ArrayList<String> subShortCode;

    public static ArrayList<SupplierList> supplierListsRelations;


    ArrayList<ChartValue> categoryValuesWO;
    ArrayList<ChartValue> categoryValuesRE;

    ArrayList<WoPoLists> woPoLists;
    ArrayList<WoPoLists> filteredListWoPo;

    ArrayList<ChartValue> subCategoryValuesWO;
    ArrayList<ChartValue> subCategoryValuesRE;

    String selectedRelations = "";
    public static String firstDate = "";
    public static String lastDate = "";
    String catrgoryId = "";
    String selectedCatName = "";
    String subCategoryId = "";
    String selectedSubCatName = "";
    String labelForItem = "";
    public static String selectedWOPO = "";
    public static String wo_id = "";
    String totalvat = "";
    String billpaidTotal = "";

    float workOval = 0;
    float recVal = 0;


    public static String itemNameList = "";
    public static String w_o_date = "";
    public static String w_o_qty = "";
    public static String w_o_rate = "";
    public static String w_o_amnt = "";
    public static String re_qty = "";
    public static String re_amnt = "";
    public static String rebl_qty = "";
    public static String rebl_amnt = "";


    public static String wo_po_amnt = "";
    public static String rcv_amnt = "";
    public static String bill_amnt = "";
    public static String wo_po_no = "";

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
    Boolean isfiltered = false;
    Boolean isfilteredWoPo = false;

    private Connection connection;

    String toSeCatMSG = "Category View (Select Category from Chart)";
    String toSeSubCatMsg = "Sub Category View (Select Sub Category from Chart)";

    Context mContext;
    public RelationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RelationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RelationsFragment newInstance(String param1, String param2) {
        RelationsFragment fragment = new RelationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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
        View view = inflater.inflate(R.layout.fragment_relations, container, false);

        afterRelation = view.findViewById(R.id.after_relation_type_selecting);
        afterRelation.setVisibility(View.GONE);
        afterCatSelection = view.findViewById(R.id.after_selecting_category_relation);
        afterCatSelection.setVisibility(View.GONE);
        aftercatSelectionItem = view.findViewById(R.id.after_selecting_category_for_item);
        aftercatSelectionItem.setVisibility(View.GONE);
        afterWoPoSelect = view.findViewById(R.id.after_selecting_wo_po);
        afterWoPoSelect.setVisibility(View.GONE);
        afterWopOlistSelect = view.findViewById(R.id.after_selecting_wo_po_relation_type);
        afterWopOlistSelect.setVisibility(View.GONE);

        woPoCard = view.findViewById(R.id.wo_po_card_view);
        woPoCard.setVisibility(View.GONE);

        mrReceiveCard = view.findViewById(R.id.mr_receive_card);
        balanceCard = view.findViewById(R.id.balance_card);

        mrReceiveCard.setVisibility(View.GONE);
        balanceCard.setVisibility(View.GONE);

        woPoBillRela = view.findViewById(R.id.card_of_wo_po_bill_relation);
        woPoBillRela.setVisibility(View.GONE);

        woPoRcvRela = view.findViewById(R.id.card_of_wo_po_rcv_relation);
        woPoRcvRela.setVisibility(View.GONE);

        billPayableCard = view.findViewById(R.id.bill_payable_card);
        billPayableCard.setVisibility(View.GONE);

        billPayCard = view.findViewById(R.id.mr_bill_paid_card);
        billPayCard.setVisibility(View.GONE);

        vatCard = view.findViewById(R.id.mr_total_vat_card);
        vatCard.setVisibility(View.GONE);

        billAllCard = view.findViewById(R.id.mr_total_bill_card);
        billAllCard.setVisibility(View.GONE);

        prRecvCard = view.findViewById(R.id.purchase_receive_card);
        woPoAmntCard = view.findViewById(R.id.wo_po_amnt_card);

        orderName = view.findViewById(R.id.order_name_change);
        receiveName = view.findViewById(R.id.receive_name_change);
        woPoType = view.findViewById(R.id.wo_po_type_spinner);
        relationType = view.findViewById(R.id.relation_type_spinner);
        beginDate = view.findViewById(R.id.begin_date_relation);
        endDate = view.findViewById(R.id.end_date_relations);
        supplierLayRelation = view.findViewById(R.id.select_supplier_layout_relations);
        supplierRelation = view.findViewById(R.id.select_supplier_relations);
        toSeCat = view.findViewById(R.id.category_selected_msg_relations);
        toSeSubCat = view.findViewById(R.id.sub_category_selected_msg_relations);
        daterange = view.findViewById(R.id.date_range_msg_relations);
        workOrderCir = view.findViewById(R.id.w_o_rela_circle);
        receCircl = view.findViewById(R.id.receive_relation_circle);
        balanceCircle = view.findViewById(R.id.balance_wo_po_circle);
        workOrderTotal = view.findViewById(R.id.work_order_relation_value);
        receTotal = view.findViewById(R.id.receive_relation_value);
        noItem = view.findViewById(R.id.no_item_msg);
        noWOPO = view.findViewById(R.id.no_wo_po_msg);

        billPayCircle = view.findViewById(R.id.mr_bill_paid_circle);
        billPayableCircle = view.findViewById(R.id.bill_payable_circle);
        vatCircle = view.findViewById(R.id.mr_total_vat_circle);
        billAllCircle = view.findViewById(R.id.mr_total_bill_circle);

        noWOPO.setVisibility(View.GONE);

        whoseItem = view.findViewById(R.id.selected_all_name);
        searchItem = view.findViewById(R.id.search_item_name);
        searchItemLay = view.findViewById(R.id.search_item_name_lay);
        itemView = view.findViewById(R.id.item_overview_relation);

        searchWOPO = view.findViewById(R.id.search_wo_no);
        woPoView = view.findViewById(R.id.wo_po_overview_relation);

        balanceTotal = view.findViewById(R.id.balance_po_wo_value);
        purchaseCircle = view.findViewById(R.id.mr_receive_circle);
        purchaseReTotal = view.findViewById(R.id.mr_receive_value);
        billPayable = view.findViewById(R.id.bill_payable);
        billpay = view.findViewById(R.id.mr_bill_paid);
        vat = view.findViewById(R.id.mr_total_vat);
        billAll = view.findViewById(R.id.mr_total_bill);


        workOrderCirCat = view.findViewById(R.id.w_o_rela_circle_cate);
        receCirclCat = view.findViewById(R.id.receive_relation_circle_cate);

        workOrderTotalCat = view.findViewById(R.id.work_order_relation_value_cate);
        receTotalCat = view.findViewById(R.id.receive_relation_value_cate);

        selectedCat = view.findViewById(R.id.selected_cat_name_w_r_relation);

        categoryMultiChart = view.findViewById(R.id.relation_category_overview);
        subCatMultiChart = view.findViewById(R.id.relation_sub_category_overview);
        pieChart = view.findViewById(R.id.wo_po_bill_rcv_overview);

        itemViewLists = new ArrayList<>();
        filteredList = new ArrayList<>();
        filteredListWoPo = new ArrayList<>();
        NoOfEmp = new ArrayList<>();
        woPoLists = new ArrayList<>();

        itemView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        itemView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(itemView.getContext(),DividerItemDecoration.VERTICAL);
        itemView.addItemDecoration(dividerItemDecoration);

        woPoView.setHasFixedSize(true);
        woPolayoutManager = new LinearLayoutManager(getContext());
        woPoView.setLayoutManager(woPolayoutManager);
        DividerItemDecoration dividerItemDecoration1 = new DividerItemDecoration(woPoView.getContext(),DividerItemDecoration.VERTICAL);
        woPoView.addItemDecoration(dividerItemDecoration1);



        Drawable background = workOrderCir.getBackground();
        Drawable drawable = receCircl.getBackground();

        Drawable background2 = workOrderCirCat.getBackground();
        Drawable drawable2 = receCirclCat.getBackground();

        Drawable balanceBack = balanceCircle.getBackground();

        Drawable purchaseBack = purchaseCircle.getBackground();

        Drawable billpaaid = billPayCircle.getBackground();

        Drawable billpayablee = billPayableCircle.getBackground();

        Drawable vatt = vatCircle.getBackground();

        Drawable billll = billAllCircle.getBackground();


        int bc = darkenColor(Color.parseColor("#01a3a4"),0.8f);

        if (billll instanceof ShapeDrawable) {
            ((ShapeDrawable)billll).getPaint().setColor(bc);
        } else if (billll instanceof GradientDrawable) {
            ((GradientDrawable)billll).setColor(bc);
        } else if (billll instanceof ColorDrawable) {
            ((ColorDrawable)billll).setColor(bc);
        }


        int bpc = -4088564;

        if (billpayablee instanceof ShapeDrawable) {
            ((ShapeDrawable)billpayablee).getPaint().setColor(bpc);
        } else if (billpayablee instanceof GradientDrawable) {
            ((GradientDrawable)billpayablee).setColor(bpc);
        } else if (billpayablee instanceof ColorDrawable) {
            ((ColorDrawable)billpayablee).setColor(bpc);
        }

        int bpac = -8632430;

        if (billpaaid instanceof ShapeDrawable) {
            ((ShapeDrawable)billpaaid).getPaint().setColor(bpac);
        } else if (billpaaid instanceof GradientDrawable) {
            ((GradientDrawable)billpaaid).setColor(bpac);
        } else if (billpaaid instanceof ColorDrawable) {
            ((ColorDrawable)billpaaid).setColor(bpac);
        }

        int vc = -13993297;

        if (vatt instanceof ShapeDrawable) {
            ((ShapeDrawable)vatt).getPaint().setColor(vc);
        } else if (vatt instanceof GradientDrawable) {
            ((GradientDrawable)vatt).setColor(vc);
        } else if (vatt instanceof ColorDrawable) {
            ((ColorDrawable)vatt).setColor(vc);
        }

        int wCc = -3365264;

        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(wCc);
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(wCc);
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(wCc);
        }

        if (background2 instanceof ShapeDrawable) {
            ((ShapeDrawable)background2).getPaint().setColor(wCc);
        } else if (background2 instanceof GradientDrawable) {
            ((GradientDrawable)background2).setColor(wCc);
        } else if (background2 instanceof ColorDrawable) {
            ((ColorDrawable)background2).setColor(wCc);
        }

        int prCc = -3379074;
        int rCc = -9389108;
        if (drawable instanceof ShapeDrawable) {
            ((ShapeDrawable)drawable).getPaint().setColor(rCc);
        } else if (drawable instanceof GradientDrawable) {
            ((GradientDrawable)drawable).setColor(rCc);
        } else if (drawable instanceof ColorDrawable) {
            ((ColorDrawable)drawable).setColor(rCc);
        }

        if (drawable2 instanceof ShapeDrawable) {
            ((ShapeDrawable)drawable2).getPaint().setColor(rCc);
        } else if (drawable2 instanceof GradientDrawable) {
            ((GradientDrawable)drawable2).setColor(rCc);
        } else if (drawable2 instanceof ColorDrawable) {
            ((ColorDrawable)drawable2).setColor(rCc);
        }

        int color = getResources().getColor(R.color.teal_200);
        if (balanceBack instanceof ShapeDrawable) {
            ((ShapeDrawable)balanceBack).getPaint().setColor(color);
        } else if (balanceBack instanceof GradientDrawable) {
            ((GradientDrawable)balanceBack).setColor(color);
        } else if (balanceBack instanceof ColorDrawable) {
            ((ColorDrawable)balanceBack).setColor(color);
        }



        if (purchaseBack instanceof ShapeDrawable) {
            ((ShapeDrawable)purchaseBack).getPaint().setColor(prCc);
        } else if (purchaseBack instanceof GradientDrawable) {
            ((GradientDrawable)purchaseBack).setColor(prCc);
        } else if (purchaseBack instanceof ColorDrawable) {
            ((ColorDrawable)purchaseBack).setColor(prCc);
        }


        CateMultiInit();

        categoryMultiChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                float value = e.getY();

                subCategoryId = "";
                catrgoryId = e.getData().toString();
                System.out.println(catrgoryId);

                float x = h.getX();
                int index = h.getDataSetIndex();

                int cc1 = categoryMultiChart.getBarData().getDataSetByIndex(index).getColor((int) x);

                System.out.println(cc1);

                //System.out.println(value);
                //cc1 = ColorUtils.blendARGB(cc1,Color.WHITE,0.3f);

                BarEntry br = (BarEntry) e;
                float ss = br.getX();
                selectedCatName = shortCode.get((int)ss);
                System.out.println(selectedCatName);

                workOval = categoryMultiChart.getBarData().getDataSets().get(0).getEntryForIndex((int) x).getY();
                recVal = categoryMultiChart.getBarData().getDataSets().get(1).getEntryForIndex((int) x).getY();

                float valueSelect = categoryMultiChart.getBarData().getDataSetByIndex(index).getEntryForIndex((int)x).getY();

                System.out.println("Value Selected: "+valueSelect);

                labelForItem = categoryMultiChart.getBarData().getDataSetByIndex(index).getLabel();

                System.out.println(workOval);
                System.out.println(recVal);

                selectedCat.setText(selectedCatName);

                String number = String.valueOf(workOval);
                double amount = Double.parseDouble(number);
                DecimalFormat formatter = new DecimalFormat("#,##,##,###");
                String formatted = formatter.format(amount);

                workOrderTotalCat.setText(formatted+" BDT");

                number = String.valueOf(recVal);
                amount = Double.parseDouble(number);

                formatted = formatter.format(amount);
                receTotalCat.setText(formatted+" BDT");


                toSeCat.setText("Category View ("+selectedCatName+" Selected)");
                toSeSubCat.setText(toSeSubCatMsg);
                System.out.println("label: "+ labelForItem);

                if (selectedRelations.equals("1")) {
                    if (valueSelect != 0 && labelForItem.equals("WO/PO")) {
                        new SubCategoryCheck().execute();
                    } else if (valueSelect != 0 && labelForItem.equals("Receive")) {
                        new SubCategoryCheck().execute();
                    }
                }


//                if (workOval == 0 && recVal == 0) {
//                    // do nothing
//                } else {
//                    new SubCategoryCheck().execute();
//                }


            }

            @Override
            public void onNothingSelected() {

            }
        });

        SubCateMultiInit();

        subCatMultiChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                subCategoryId = e.getData().toString();
                System.out.println(subCategoryId);

                float x = h.getX();
                int index = h.getDataSetIndex();

                BarEntry br = (BarEntry) e;
                float ss = br.getX();
                selectedSubCatName = subShortCode.get((int)ss);

                workOval = subCatMultiChart.getBarData().getDataSets().get(index).getEntryForIndex((int) x).getY();
                recVal = subCatMultiChart.getBarData().getDataSets().get(index).getEntryForIndex((int) x).getY();

                labelForItem = subCatMultiChart.getBarData().getDataSets().get(index).getLabel();

                System.out.println("Label: "+workOval);
                System.out.println("Label: "+recVal);
                System.out.println("Label: "+labelForItem);

                float valueSelect = subCatMultiChart.getBarData().getDataSetByIndex(index).getEntryForIndex((int)x).getY();

                System.out.println("Value Selected: "+valueSelect);

                whoseItem.setText(selectedSubCatName+" ("+labelForItem+")");
                toSeSubCat.setText("Sub Category View (" + selectedSubCatName + " Selected)");

                if (valueSelect != 0 && labelForItem.equals("WO/PO")) {
                    new ItemCheck().execute();
                    noItem.setVisibility(View.GONE);
                } else if (valueSelect != 0 && labelForItem.equals("Receive")) {
                    new ItemCheck().execute();
                    noItem.setVisibility(View.GONE);
                } else {
                    noItem.setVisibility(View.VISIBLE);
                    itemViewLists = new ArrayList<>();
                    itemAdapter = new ItemAdapter(itemViewLists, getContext(),RelationsFragment.this);
                    itemView.setAdapter(itemAdapter);
                    searchItem.setText("");
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        PieChartInit();

        pieChart.setClickable(false);
        pieChart.setTouchEnabled(false);


        // WO/PO Type
        String[] type1 = new String[] {"Work Order", "Purchase Order"};

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type1);

        woPoType.setAdapter(arrayAdapter1);

        woPoType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                System.out.println(position+": "+name);
                if (name.equals("Work Order")) {
                    selectedWOPO = "2";
                    //selectedRelations = "1";
//                    afterRelation.setVisibility(View.VISIBLE);
//                    toSeCat.setText(toSeCatMSG);
//                    toSeSubCat.setText(toSeSubCatMsg);
//                    afterCatSelection.setVisibility(View.GONE);
//                    aftercatSelectionItem.setVisibility(View.GONE);
//                    new CheckSupplier().execute();
                    balanceCard.setVisibility(View.VISIBLE);
                    mrReceiveCard.setVisibility(View.VISIBLE);
                    prRecvCard.setVisibility(View.GONE);
                    afterWoPoSelect.setVisibility(View.VISIBLE);
                    orderName.setText("Work Order");
                    System.out.println("Part2: billW");

                    int sizeInDP = 5;

                    int marginInDp = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, sizeInDP, getResources()
                                    .getDisplayMetrics());

                    ViewGroup.MarginLayoutParams layoutParams =
                            (ViewGroup.MarginLayoutParams) woPoAmntCard.getLayoutParams();
                    layoutParams.setMargins(marginInDp, 0, marginInDp, marginInDp);
                    woPoAmntCard.requestLayout();


                    new CategoryCheck().execute();

                } else if (name.equals("Purchase Order")){
                    //selectedRelations = "2";
                    selectedWOPO = "1";
                    balanceCard.setVisibility(View.VISIBLE);
                    mrReceiveCard.setVisibility(View.VISIBLE);
                    prRecvCard.setVisibility(View.VISIBLE);

                    afterWoPoSelect.setVisibility(View.VISIBLE);
                    orderName.setText("Purchase Order");
                    System.out.println("Part2: billP");

                    int sizeInDP = 5;
                    double minSizeInDp = 2.5;

                    int marginInDp = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, sizeInDP, getResources()
                                    .getDisplayMetrics());

                    int marginInDp1 = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, (float) minSizeInDp, getResources()
                                    .getDisplayMetrics());

                    ViewGroup.MarginLayoutParams layoutParams =
                            (ViewGroup.MarginLayoutParams) woPoAmntCard.getLayoutParams();
                    layoutParams.setMargins((int) marginInDp, 0, marginInDp1, marginInDp);
                    woPoAmntCard.requestLayout();


                    new CategoryCheck().execute();


                }

            }
        });


        //Relation Type
        String[] type = new String[] {"WO/PO - Receive", "WO/PO - Bill Details"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        mContext,
                        R.layout.dropdown_menu_popup_item,
                        type);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

        relationType.setAdapter(arrayAdapter);

        relationType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                System.out.println(position+": "+name);
                if (name.equals("WO/PO - Receive")) {
                    selectedRelations = "1";

                    afterRelation.setVisibility(View.VISIBLE);

                    vatCard.setVisibility(View.GONE);
                    billPayableCard.setVisibility(View.GONE);
                    billPayCard.setVisibility(View.GONE);
                    billAllCard.setVisibility(View.GONE);

                    woPoBillRela.setVisibility(View.GONE);
                    woPoRcvRela.setVisibility(View.VISIBLE);
                    prRecvCard.setVisibility(View.VISIBLE);

                    toSeCat.setText(toSeCatMSG);
                    toSeSubCat.setText(toSeSubCatMsg);

                    afterCatSelection.setVisibility(View.GONE);
                    aftercatSelectionItem.setVisibility(View.GONE);

                    receiveName.setText("Receive");
                    orderName.setText("WO/PO");
                    System.out.println("Part1: rcv");

                    afterWopOlistSelect.setVisibility(View.GONE);

                    balanceCard.setVisibility(View.GONE);
                    mrReceiveCard.setVisibility(View.GONE);

                    woPoCard.setVisibility(View.GONE);
                    afterWoPoSelect.setVisibility(View.VISIBLE);

//                    int rCc = -9389108;
//                    if (drawable instanceof ShapeDrawable) {
//                        ((ShapeDrawable)drawable).getPaint().setColor(rCc);
//                    } else if (drawable instanceof GradientDrawable) {
//                        ((GradientDrawable)drawable).setColor(rCc);
//                    } else if (drawable instanceof ColorDrawable) {
//                        ((ColorDrawable)drawable).setColor(rCc);
//                    }

                    new CategoryCheck().execute();
                    System.out.println("Part1: rcv");
                    System.out.println("Relations: "+selectedRelations);

                } else if (name.equals("WO/PO - Bill Details")){
                    selectedRelations = "2";

                    woPoCard.setVisibility(View.VISIBLE);
                    afterWoPoSelect.setVisibility(View.GONE);
                    receiveName.setText("Purchase Receive");

                    vatCard.setVisibility(View.VISIBLE);
                    billPayableCard.setVisibility(View.VISIBLE);
                    billPayCard.setVisibility(View.VISIBLE);
                    billAllCard.setVisibility(View.VISIBLE);

                    woPoRcvRela.setVisibility(View.GONE);
                    woPoBillRela.setVisibility(View.VISIBLE);
                    System.out.println("Part1: bill");

                    afterWopOlistSelect.setVisibility(View.VISIBLE);
                    afterRelation.setVisibility(View.VISIBLE);
                    toSeCat.setText(toSeCatMSG);
                    toSeSubCat.setText(toSeSubCatMsg);
                    afterCatSelection.setVisibility(View.GONE);
                    aftercatSelectionItem.setVisibility(View.GONE);
                    woPoType.setText("");

//                    int prCc = -3379074;
//                    if (drawable instanceof ShapeDrawable) {
//                        ((ShapeDrawable)drawable).getPaint().setColor(prCc);
//                    } else if (drawable instanceof GradientDrawable) {
//                        ((GradientDrawable)drawable).setColor(prCc);
//                    } else if (drawable instanceof ColorDrawable) {
//                        ((ColorDrawable)drawable).setColor(prCc);
//                    }

//                    new CategoryCheck().execute();
                    System.out.println("Relations: "+selectedRelations);

                }

            }
        });




        //selecting Supplier
        supplierRelation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromRela_supp = 1;
                SupplierDialogue supplierDialogue = new SupplierDialogue();
                supplierDialogue.show(getActivity().getSupportFragmentManager(),"SS" );
            }
        });

        supplierRelation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                if (selectedRelations.equals("1")) {
                    new CategoryCheck().execute();
                    afterCatSelection.setVisibility(View.GONE);
                    toSeCat.setText(toSeCatMSG);
                    toSeSubCat.setText(toSeSubCatMsg);
                    aftercatSelectionItem.setVisibility(View.GONE);
                } else if (selectedRelations.equals("2") && !selectedWOPO.isEmpty()) {
                    new CategoryCheck().execute();
                    afterCatSelection.setVisibility(View.GONE);
                    toSeCat.setText(toSeCatMSG);
                    toSeSubCat.setText(toSeSubCatMsg);
                    aftercatSelectionItem.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getContext(),"Please Select WO/PO Type first",Toast.LENGTH_SHORT).show();
                }

//                new CategoryCheck().execute();
//                afterCatSelection.setVisibility(View.GONE);
//                aftercatSelectionItem.setVisibility(View.GONE);
//
//                toSeCat.setText(toSeCatMSG);
//                toSeSubCat.setText(toSeSubCatMsg);

            }
        });

        //Getting Date
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
                                if (selectedRelations.equals("1")) {
                                    System.out.println("FirstDATE");
                                    new CategoryCheck().execute();
                                    afterCatSelection.setVisibility(View.GONE);
                                    toSeCat.setText(toSeCatMSG);
                                    toSeSubCat.setText(toSeSubCatMsg);
                                    aftercatSelectionItem.setVisibility(View.GONE);
                                } else if (selectedRelations.equals("2") && !selectedWOPO.isEmpty()) {
                                    System.out.println("FirstDATE1");
                                    new CategoryCheck().execute();
                                    afterCatSelection.setVisibility(View.GONE);
                                    toSeCat.setText(toSeCatMSG);
                                    toSeSubCat.setText(toSeSubCatMsg);
                                    aftercatSelectionItem.setVisibility(View.GONE);
                                } else {
                                    Toast.makeText(getContext(),"Please Select WO/PO Type first",Toast.LENGTH_SHORT).show();
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
                                        //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                                        daterange.setVisibility(View.GONE);

                                        if (selectedRelations.equals("1")) {
                                            System.out.println("FirstDATE2");
                                            new CategoryCheck().execute();
                                            afterCatSelection.setVisibility(View.GONE);
                                            toSeCat.setText(toSeCatMSG);
                                            toSeSubCat.setText(toSeSubCatMsg);
                                            aftercatSelectionItem.setVisibility(View.GONE);
                                        } else if (selectedRelations.equals("2") && !selectedWOPO.isEmpty()) {
                                            System.out.println("FirstDATE3");
                                            new CategoryCheck().execute();
                                            afterCatSelection.setVisibility(View.GONE);
                                            toSeCat.setText(toSeCatMSG);
                                            toSeSubCat.setText(toSeSubCatMsg);
                                            aftercatSelectionItem.setVisibility(View.GONE);
                                        } else {
                                            Toast.makeText(getContext(),"Please Select WO/PO Type first",Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                    else {
                                        daterange.setVisibility(View.VISIBLE);
                                        beginDate.setText("");
                                        firstDate = beginDate.getText().toString();
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

                                if (selectedRelations.equals("1")) {
                                    new CategoryCheck().execute();
                                    afterCatSelection.setVisibility(View.GONE);
                                    toSeCat.setText(toSeCatMSG);
                                    toSeSubCat.setText(toSeSubCatMsg);
                                    aftercatSelectionItem.setVisibility(View.GONE);
                                } else if (selectedRelations.equals("2") && !selectedWOPO.isEmpty()) {
                                    new CategoryCheck().execute();
                                    afterCatSelection.setVisibility(View.GONE);
                                    toSeCat.setText(toSeCatMSG);
                                    toSeSubCat.setText(toSeSubCatMsg);
                                    aftercatSelectionItem.setVisibility(View.GONE);
                                } else {
                                    Toast.makeText(getContext(),"Please Select WO/PO Type first",Toast.LENGTH_SHORT).show();
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
                                        //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                                        daterange.setVisibility(View.GONE);

                                        if (selectedRelations.equals("1")) {
                                            new CategoryCheck().execute();
                                            afterCatSelection.setVisibility(View.GONE);
                                            toSeCat.setText(toSeCatMSG);
                                            toSeSubCat.setText(toSeSubCatMsg);
                                            aftercatSelectionItem.setVisibility(View.GONE);
                                        } else if (selectedRelations.equals("2") && !selectedWOPO.isEmpty()) {
                                            new CategoryCheck().execute();
                                            afterCatSelection.setVisibility(View.GONE);
                                            toSeCat.setText(toSeCatMSG);
                                            toSeSubCat.setText(toSeSubCatMsg);
                                            aftercatSelectionItem.setVisibility(View.GONE);
                                        } else {
                                            Toast.makeText(getContext(),"Please Select WO/PO Type first",Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                    else {
                                        daterange.setVisibility(View.VISIBLE);
                                        endDate.setText("");
                                        lastDate = endDate.getText().toString();

                                    }
                                }
                            }

                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });

        //Search Item
        searchItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());
            }
        });

        searchItem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                        event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        Log.i("Let see", "Come here");
                        closeKeyBoard();



                        return false; // consume.
                    }
                }
                return false;
            }
        });

        //Search WO PO
        searchWOPO.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filterWoPo(s.toString());
            }
        });

        searchWOPO.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                        event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        Log.i("Let see", "Come here");
                        closeKeyBoard();

                        return false; // consume.
                    }
                }
                return false;
            }
        });

        new CheckSupplier().execute();

        return  view;
    }

    private void closeKeyBoard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void CateMultiInit() {

        workOrders = new ArrayList<>();
        receives = new ArrayList<>();
        shortCode = new ArrayList<>();

        categoryMultiChart.getDescription().setEnabled(false);
        categoryMultiChart.setPinchZoom(false);
        categoryMultiChart.getAxisLeft().setDrawGridLines(true);
        categoryMultiChart.getLegend().setStackSpace(20);
        categoryMultiChart.getLegend().setYOffset(10);
        categoryMultiChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        categoryMultiChart.setExtraOffsets(0,0,0,20);
        categoryMultiChart.setScaleEnabled(false);
        categoryMultiChart.setTouchEnabled(true);
        categoryMultiChart.setDoubleTapToZoomEnabled(false);
        categoryMultiChart.setHighlightPerTapEnabled(true);
        categoryMultiChart.setHighlightPerDragEnabled(false);

        categoryMultiChart.getAxisRight().setEnabled(false);
    }

    public void SubCateMultiInit() {
        subWorkOrders = new ArrayList<>();
        subReceives = new ArrayList<>();
        subShortCode = new ArrayList<>();

        subCatMultiChart.getDescription().setEnabled(false);
        subCatMultiChart.setPinchZoom(false);
        subCatMultiChart.getAxisLeft().setDrawGridLines(true);
        subCatMultiChart.getLegend().setStackSpace(20);
        subCatMultiChart.getLegend().setYOffset(10);
        subCatMultiChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        subCatMultiChart.setExtraOffsets(0,0,0,20);
        subCatMultiChart.setScaleEnabled(false);
        subCatMultiChart.setTouchEnabled(true);
        subCatMultiChart.setDoubleTapToZoomEnabled(false);
        subCatMultiChart.setHighlightPerTapEnabled(true);
        subCatMultiChart.setHighlightPerDragEnabled(false);
        subCatMultiChart.getAxisRight().setEnabled(false);
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

    public static int darkenColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r,255),
                Math.min(g,255),
                Math.min(b,255));
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


//        String name = "";
//        String id = "";
        if (isfiltered) {
            itemNameList = filteredList.get(CategoryPosition).getItem_name();
            w_o_date = filteredList.get(CategoryPosition).getWom_date();
            w_o_qty = filteredList.get(CategoryPosition).getItem_wo_qty();
            w_o_rate = filteredList.get(CategoryPosition).getItem_wo_rate();
            w_o_amnt = filteredList.get(CategoryPosition).getItem_wo_amnt();
            re_qty = filteredList.get(CategoryPosition).getItem_re_qty();
            re_amnt = filteredList.get(CategoryPosition).getItem_re_amnt();
            rebl_amnt = filteredList.get(CategoryPosition).getItem_rebl_amnt();
            rebl_qty = filteredList.get(CategoryPosition).getItem_rebl_qty();

            ItemDetailsRelation itemDetailsRelation = new ItemDetailsRelation();
            itemDetailsRelation.show(getActivity().getSupportFragmentManager(),"Details");
//            name = filteredList.get(CategoryPosition).getItem_name();
//            id = filteredList.get(CategoryPosition).getItem_wo_amnt();
//            System.out.println(name);
//            System.out.println(id);
        } else {
            itemNameList = itemViewLists.get(CategoryPosition).getItem_name();
            w_o_date = itemViewLists.get(CategoryPosition).getWom_date();
            w_o_qty = itemViewLists.get(CategoryPosition).getItem_wo_qty();
            w_o_rate = itemViewLists.get(CategoryPosition).getItem_wo_rate();
            w_o_amnt = itemViewLists.get(CategoryPosition).getItem_wo_amnt();
            re_qty = itemViewLists.get(CategoryPosition).getItem_re_qty();
            re_amnt = itemViewLists.get(CategoryPosition).getItem_re_amnt();
            rebl_amnt = itemViewLists.get(CategoryPosition).getItem_rebl_amnt();
            rebl_qty = itemViewLists.get(CategoryPosition).getItem_rebl_qty();

            ItemDetailsRelation itemDetailsRelation = new ItemDetailsRelation();
            itemDetailsRelation.show(getActivity().getSupportFragmentManager(),"Details");
//            name = itemViewLists.get(CategoryPosition).getItem_name();
//            id = itemViewLists.get(CategoryPosition).getItem_wo_amnt();
//            System.out.println(name);
//            System.out.println(id);
        }
    }

    private void filter(String text) {

        filteredList = new ArrayList<>();
        for (ItemViewList item : itemViewLists) {
            if (item.getItem_name().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add((item));
                isfiltered = true;
            }
        }
        itemAdapter.filterList(filteredList);
    }

    @Override
    public void onWoPoClicked(int CategoryPosition) {

        if (isfilteredWoPo) {

            wo_po_amnt = filteredListWoPo.get(CategoryPosition).getWo_amnt();
            rcv_amnt = filteredListWoPo.get(CategoryPosition).getRcv_amnt();
            bill_amnt = filteredListWoPo.get(CategoryPosition).getBill_amnt();
            wo_id = filteredListWoPo.get(CategoryPosition).getWo_id();
            wo_po_no = filteredListWoPo.get(CategoryPosition).getWo_po_no();

            WoPoDetails woPoDetails = new WoPoDetails();
            woPoDetails.show(getActivity().getSupportFragmentManager(),"Details");

        } else {
            wo_po_amnt = woPoLists.get(CategoryPosition).getWo_amnt();
            rcv_amnt = woPoLists.get(CategoryPosition).getRcv_amnt();
            bill_amnt = woPoLists.get(CategoryPosition).getBill_amnt();
            wo_id = woPoLists.get(CategoryPosition).getWo_id();
            wo_po_no = woPoLists.get(CategoryPosition).getWo_po_no();

            WoPoDetails woPoDetails = new WoPoDetails();
            woPoDetails.show(getActivity().getSupportFragmentManager(),"Details");

        }

        wo_id = woPoLists.get(CategoryPosition).getWo_id();
    }

    private void filterWoPo(String text) {

        filteredListWoPo = new ArrayList<>();
        for (WoPoLists item : woPoLists) {
            if (item.getWo_po_no().toLowerCase().contains(text.toLowerCase())) {
                filteredListWoPo.add((item));
                isfilteredWoPo = true;
            }
        }
        woPoAdapter.filterList(filteredListWoPo);
    }

    public class CheckSupplier extends AsyncTask<Void, Void, Void> {

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

//                if (selectedRelations.equals("1")) {
//                    System.out.println("Part2: rcv");
//                    new CategoryCheck().execute();
//                }

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

                        new CheckSupplier().execute();
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

                workOrders = new ArrayList<>();
                receives = new ArrayList<>();
                shortCode = new ArrayList<>();

                if (selectedRelations.equals("1")) {
                    System.out.println("Part3: rcv");
                    if (categoryValuesRE.size() != 0 || categoryValuesWO.size() != 0) {

                        System.out.println("Paise Data");

                        if (categoryValuesRE.size() != 0 && categoryValuesWO.size() != 0) {
                            int k = 0;
                            for (int i = 0; i < categoryValuesWO.size(); i++) {
                                String wCat = categoryValuesWO.get(i).getType();
                                boolean isit = false;

                                for (int j = 0; j < categoryValuesRE.size(); j++) {
                                    if (wCat.equals(categoryValuesRE.get(j).getType())) {
                                        isit = true;
                                        workOrders.add(new BarEntry(k, Float.parseFloat(categoryValuesWO.get(i).getValue()), categoryValuesWO.get(i).getId()));
                                        receives.add(new BarEntry(k, Float.parseFloat(categoryValuesRE.get(j).getValue()), categoryValuesRE.get(j).getId()));
                                        shortCode.add(categoryValuesWO.get(i).getType());
                                        k++;
                                    }
                                }
                                if (!isit) {
                                    workOrders.add(new BarEntry(k, Float.parseFloat(categoryValuesWO.get(i).getValue()), categoryValuesWO.get(i).getId()));
                                    receives.add(new BarEntry(k, 0f, categoryValuesWO.get(i).getId()));
                                    shortCode.add(categoryValuesWO.get(i).getType());
                                    k++;
                                }
                            }

                            for (int i = 0; i < categoryValuesRE.size(); i++) {
                                String reCat = categoryValuesRE.get(i).getType();
                                boolean isit = false;
                                for (int j = 0; j < categoryValuesWO.size(); j++) {
                                    if (reCat.equals(categoryValuesWO.get(j).getType())) {
                                        isit = true;
                                        boolean isShortcode = false;
                                        for (int l = 0; l < shortCode.size(); l++) {
                                            if (reCat.equals(shortCode.get(l))) {
                                                isShortcode = true;
                                            }
                                        }
                                        if (!isShortcode) {
                                            workOrders.add(new BarEntry(k, Float.parseFloat(categoryValuesWO.get(j).getValue()), categoryValuesWO.get(j).getId()));
                                            receives.add(new BarEntry(k, Float.parseFloat(categoryValuesRE.get(i).getValue()), categoryValuesRE.get(i).getId()));
                                            shortCode.add(categoryValuesRE.get(i).getType());
                                            k++;
                                        }
                                    }
                                }
                                if (!isit) {
                                    workOrders.add(new BarEntry(k, 0f, categoryValuesRE.get(i).getId()));
                                    receives.add(new BarEntry(k, Float.parseFloat(categoryValuesRE.get(i).getValue()), categoryValuesRE.get(i).getId()));
                                    shortCode.add(categoryValuesRE.get(i).getType());
                                    k++;
                                }
                            }
                        }
                        else if (categoryValuesRE.size() != 0 && categoryValuesWO.size() == 0) {

                            int k = 0;
                            for (int i = 0; i < categoryValuesRE.size(); i++) {
                                workOrders.add(new BarEntry(k, 0f, categoryValuesRE.get(i).getId()));
                                receives.add(new BarEntry(k, Float.parseFloat(categoryValuesRE.get(i).getValue()), categoryValuesRE.get(i).getId()));
                                shortCode.add(categoryValuesRE.get(i).getType());
                                k++;
                            }
                        } else if (categoryValuesRE.size() == 0 && categoryValuesWO.size() != 0) {

                            int k = 0;
                            for (int i = 0; i < categoryValuesWO.size(); i++) {
                                workOrders.add(new BarEntry(k, Float.parseFloat(categoryValuesWO.get(i).getValue()), categoryValuesWO.get(i).getId()));
                                receives.add(new BarEntry(k, 0f, categoryValuesWO.get(i).getId()));
                                shortCode.add(categoryValuesWO.get(i).getType());
                                k++;
                            }
                        }

                        System.out.println("Work: "+ workOrders.size());
                        System.out.println("RecV: "+ receives.size());
                        System.out.println("Short: "+ shortCode.size());

                        XAxis xAxis = categoryMultiChart.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setDrawGridLines(false);
                        xAxis.setCenterAxisLabels(true);
                        xAxis.setAxisMinimum(0);
                        xAxis.setAxisMaximum(workOrders.size());
                        xAxis.setGranularity(1);
                        categoryMultiChart.getAxisLeft().setAxisMinimum(0);
                        categoryMultiChart.highlightValues(null);

                        BarDataSet set1 = new BarDataSet(receives, "Receive");
                        int cc = ColorTemplate.VORDIPLOM_COLORS[3];
                        int newCC = darkenColor(cc,0.8f);

                        set1.setColor(newCC);
//                        set1.setValueFormatter(new ValueFormatter() {
//                            @Override
//                            public String getFormattedValue(float value) {
//                                return String.valueOf((int) Math.floor(value));
//                            }
//                        });
                        BarDataSet set2 = new BarDataSet(workOrders, "WO/PO");
//                        set2.setValueFormatter(new ValueFormatter() {
//                            @Override
//                            public String getFormattedValue(float value) {
//                                return String.valueOf((int) Math.floor(value));
//                            }
//                        });

                        int cc1 = ColorTemplate.VORDIPLOM_COLORS[2];
                        int newCC1 = darkenColor(cc1,0.8f);

                        set2.setColor(newCC1);

                        float groupSpace = 0.04f;
                        float barSpace = 0.02f; // x2 dataset
                        float barWidth = 0.46f;

                        BarData leavedata = new BarData(set2, set1);
                        leavedata.setValueTextSize(12);
                        leavedata.setBarWidth(barWidth); // set the width of each bar
                        categoryMultiChart.animateY(1000);
                        categoryMultiChart.setData(leavedata);
                        categoryMultiChart.groupBars(0, groupSpace, barSpace); // perform the "explicit" grouping
                        categoryMultiChart.invalidate();

                        xAxis.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getAxisLabel(float value, AxisBase axis) {

//                                System.out.println(value);
////                                System.out.println(axis);
//                                for (int i = 0; i < shortCode.size(); i++) {
//                                    System.out.println(shortCode.get(i));
//                                }
                                //System.out.println(shortCode.get((int) value));
                                if (shortCode != null) {
                                    if (value < 0 || value >= shortCode.size()) {
                                        return "";
                                    } else {
                                        System.out.println(value);
                                        System.out.println(axis);
                                        System.out.println(shortCode.get((int) value));
                                        return (shortCode.get((int) value));
                                    }
                                } else {
                                    return "";
                                }


                            }
                        });

                        int totalValue =  0;
                        for (int i = 0; i < workOrders.size(); i++) {

                            totalValue = totalValue + (int) workOrders.get(i).getY();

                        }

                        String number = String.valueOf(totalValue);
                        double amount = Double.parseDouble(number);
                        DecimalFormat formatter = new DecimalFormat("#,##,##,###");
                        String formatted = formatter.format(amount);

                        workOrderTotal.setText(formatted+" BDT");

                        int totalValue1 =  0;
                        for (int i = 0; i < receives.size(); i++) {

                            totalValue1 = totalValue1 + (int) receives.get(i).getY();

                        }

                        number = String.valueOf(totalValue1);
                        amount = Double.parseDouble(number);
                        formatted = formatter.format(amount);

                        receTotal.setText(formatted+" BDT");
                        noItem.setVisibility(View.GONE);



                    }
                    else {
                        workOrders.add(new BarEntry(0,0,1234567));
                        receives.add(new BarEntry(0,0,1234567));
                        shortCode.add("No Data Found");

                        XAxis xAxis = categoryMultiChart.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setDrawGridLines(false);
                        xAxis.setCenterAxisLabels(true);
                        xAxis.setAxisMinimum(0);
                        xAxis.setAxisMaximum(workOrders.size());
                        xAxis.setGranularity(1);
                        categoryMultiChart.getAxisLeft().setAxisMinimum(0);
                        categoryMultiChart.highlightValues(null);

                        BarDataSet set1 = new BarDataSet(receives, "Receive");
                        int cc = ColorTemplate.VORDIPLOM_COLORS[3];
                        int newCC = darkenColor(cc,0.8f);

                        set1.setColor(newCC);
//                        set1.setValueFormatter(new ValueFormatter() {
//                            @Override
//                            public String getFormattedValue(float value) {
//                                return String.valueOf((int) Math.floor(value));
//                            }
//                        });
                        BarDataSet set2 = new BarDataSet(workOrders, "WO/PO");
//                        set2.setValueFormatter(new ValueFormatter() {
//                            @Override
//                            public String getFormattedValue(float value) {
//                                return String.valueOf((int) Math.floor(value));
//                            }
//                        });

                        int cc1 = ColorTemplate.VORDIPLOM_COLORS[2];
                        int newCC1 = darkenColor(cc1,0.8f);

                        set2.setColor(newCC1);

                        float groupSpace = 0.04f;
                        float barSpace = 0.02f; // x2 dataset
                        float barWidth = 0.46f;

                        BarData leavedata = new BarData(set2, set1);
                        leavedata.setValueTextSize(12);
                        leavedata.setBarWidth(barWidth); // set the width of each bar
                        categoryMultiChart.animateY(1000);
                        categoryMultiChart.setData(leavedata);
                        categoryMultiChart.groupBars(0, groupSpace, barSpace); // perform the "explicit" grouping
                        categoryMultiChart.invalidate();

                        xAxis.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getAxisLabel(float value, AxisBase axis) {
                                if (value < 0 || value >= shortCode.size()) {
                                    return "";
                                } else {
//                                System.out.println(value);
//                                System.out.println(axis);
//                                System.out.println(shortCode.get((int) value));
                                    return (shortCode.get((int) value));
                                }

                            }
                        });

                        int totalValue =  0;
                        for (int i = 0; i < workOrders.size(); i++) {

                            totalValue = totalValue + (int) workOrders.get(i).getY();

                        }

                        String number = String.valueOf(totalValue);
                        double amount = Double.parseDouble(number);
                        DecimalFormat formatter = new DecimalFormat("#,##,##,###");
                        String formatted = formatter.format(amount);

                        workOrderTotal.setText(formatted+" BDT");

                        int totalValue1 =  0;
                        for (int i = 0; i < receives.size(); i++) {

                            totalValue1 = totalValue1 + (int) receives.get(i).getY();

                        }

                        number = String.valueOf(totalValue1);
                        amount = Double.parseDouble(number);
                        formatted = formatter.format(amount);

                        receTotal.setText(formatted+" BDT");

                        noItem.setVisibility(View.VISIBLE);
                    }
                } else {

                    System.out.println("Part3: bill");
                    if (NoOfEmp.size() == 0) {
                        NoOfEmp.add(new PieEntry(1,"No Work Order",6));
                    }
//                NoOfEmp.add(new PieEntry(5500f,"Fluids",10));
//                NoOfEmp.add(new PieEntry(3000f,"Spare Parts",11));
//                NoOfEmp.add(new PieEntry(8000f,"Filter",12));
//                NoOfEmp.add(new PieEntry(7000f,"Printers",13));

                    int cc1 = ColorTemplate.VORDIPLOM_COLORS[2];
                    int newCC1 = darkenColor(cc1,0.8f);
                    int cc2 = ColorTemplate.VORDIPLOM_COLORS[3];
                    int newCC2 = darkenColor(cc2,0.8f);
                    int cc3 = ColorTemplate.VORDIPLOM_COLORS[4];
                    int newCC3 = darkenColor(cc3,0.8f);
                    int cc4 = Color.parseColor("#f1c40f");
                    int newCC4 = darkenColor(cc4,0.8f);
                    int cc5 = Color.parseColor("#9b59b6");
                    int newCC5 = darkenColor(cc5, 0.8f);
                    int cc6 = Color.parseColor("#3498db");
                    int newCC6 = darkenColor(cc6,0.8f);

                    System.out.println(newCC4);
                    System.out.println(newCC5);
                    System.out.println(newCC6);
                    final int[] piecolors = new int[]{ newCC1, newCC2, newCC3,
                            newCC5,
                            newCC4,
                            newCC6,
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
                    if (label.equals("No Work Order")) {
                        dataSet.setValueTextColor(Color.TRANSPARENT);
                    } else {
                        dataSet.setValueTextColor(Color.WHITE);
                    }
                    dataSet.setHighlightEnabled(true);
                    dataSet.setValueTextSize(12);
                    //dataSet.setColors(ColorTemplate.createColors(piecolors));

                    int[] num = new int[NoOfEmp.size()];
                    for (int i = 0; i < NoOfEmp.size(); i++) {
                        int neki = (int) NoOfEmp.get(i).getData();
                        num[i] = piecolors[neki];
                    }

                    dataSet.setColors(ColorTemplate.createColors(num));

                    pieChart.setData(data);
                    pieChart.invalidate();
                    int totalValue =  0;
                    int totalvalue1 = 0;
                    int totalvalue2 = 0;


                    totalValue = (int) NoOfEmp.get(0).getValue();
                    if ( totalValue == 1) {
                        String text = "0 BDT";
                        workOrderTotal.setText(text);
                        receTotal.setText(text);
                        purchaseReTotal.setText(text);
                        balanceTotal.setText(text);
                        vat.setText(text);
                        billpay.setText(text);
                        billPayable.setText(text);
                        billAll.setText(text);


                    } else {

                        if (selectedWOPO.equals("1")) {
                            totalvalue1 = (int) NoOfEmp.get(2).getValue();
                            int bal = totalValue - totalvalue1;
                            totalvalue2 = (int) NoOfEmp.get(1).getValue();
                            String number = String.valueOf(totalvalue2);
                            double amount = Double.parseDouble(number);
                            DecimalFormat formatter = new DecimalFormat("#,##,##,###");
                            String formatted = formatter.format(amount);
                            receTotal.setText(formatted+ " BDT");

                            number = String.valueOf(totalvalue1);
                            amount = Double.parseDouble(number);
                            String formatted2 = formatter.format(amount);
                            purchaseReTotal.setText(formatted2+ " BDT");

                            number = String.valueOf(bal);
                            amount = Double.parseDouble(number);
                            String formatted3 = formatter.format(amount);

                            balanceTotal.setText(formatted3 + " BDT");

                        } else {
                            totalvalue1 = (int) NoOfEmp.get(1).getValue();
                            int bal = totalValue - totalvalue1;

                            String number = String.valueOf(totalvalue1);
                            double amount = Double.parseDouble(number);
                            DecimalFormat formatter = new DecimalFormat("#,##,##,###");
                            String formatted2 = formatter.format(amount);
                            purchaseReTotal.setText(formatted2+ " BDT");

                            number = String.valueOf(bal);
                            amount = Double.parseDouble(number);
                            String formatted3 = formatter.format(amount);

                            balanceTotal.setText(formatted3 + " BDT");
                        }

                        String number = String.valueOf(totalValue);
                        double amount = Double.parseDouble(number);

                        DecimalFormat formatter = new DecimalFormat("#,##,##,###");
                        String formatted1 = formatter.format(amount);

                        workOrderTotal.setText(formatted1+ " BDT");




                        DecimalFormat formatter1 = new DecimalFormat("#,##,##,###.##");
                        number = String.valueOf(totalvat);
                        amount = Double.parseDouble(number);
                        String formatted4 = formatter1.format(amount);

                        vat.setText(formatted4 + " BDT");


                        number = String.valueOf(billpaidTotal);
                        amount = Double.parseDouble(number);
                        String formatted5 = formatter1.format(amount);

                        billpay.setText(formatted5 +" BDT");


                        double totalBill = Double.parseDouble(String.valueOf(totalvalue1)) + Double.parseDouble(totalvat);

                        String formatter7 = formatter1.format(totalBill);

                        billAll.setText(formatter7+ " BDT");


                        Double billtoPay = totalBill - Double.parseDouble(billpaidTotal);

                        String formatted6 = formatter1.format(billtoPay);

                        billPayable.setText(formatted6 + " BDT");


                    }

                    if (woPoLists.size() == 0) {
                        noWOPO.setVisibility(View.VISIBLE);

                        woPoAdapter = new WoPoAdapter(woPoLists, getContext(),RelationsFragment.this);
                        woPoView.setAdapter(woPoAdapter);
                        searchWOPO.setText("");
                    } else {
                        noWOPO.setVisibility(View.GONE);

                        woPoAdapter = new WoPoAdapter(woPoLists, getContext(),RelationsFragment.this);
                        woPoView.setAdapter(woPoAdapter);
                        searchWOPO.setText("");

                    }


                }


            }
            else {
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

                subWorkOrders = new ArrayList<>();
                subReceives = new ArrayList<>();
                subShortCode = new ArrayList<>();

                if (subCategoryValuesRE.size() != 0 || subCategoryValuesWO.size() != 0) {

                    System.out.println("Paise Data");


                    if (subCategoryValuesRE.size() != 0 && subCategoryValuesWO.size() != 0) {
                        int k = 0;
                        for (int i = 0; i < subCategoryValuesWO.size(); i++) {
                            String wCat = subCategoryValuesWO.get(i).getType();
                            boolean isit = false;

                            for (int j = 0; j < subCategoryValuesRE.size(); j++) {
                                if (wCat.equals(subCategoryValuesRE.get(j).getType())) {
                                    isit = true;
                                    subWorkOrders.add(new BarEntry(k, Float.parseFloat(subCategoryValuesWO.get(i).getValue()), subCategoryValuesWO.get(i).getId()));
                                    subReceives.add(new BarEntry(k, Float.parseFloat(subCategoryValuesRE.get(j).getValue()), subCategoryValuesRE.get(j).getId()));
                                    subShortCode.add(subCategoryValuesWO.get(i).getType());
                                    k++;
                                }
                            }
                            if (!isit) {
                                subWorkOrders.add(new BarEntry(k, Float.parseFloat(subCategoryValuesWO.get(i).getValue()), subCategoryValuesWO.get(i).getId()));
                                subReceives.add(new BarEntry(k, 0f, subCategoryValuesWO.get(i).getId()));
                                subShortCode.add(subCategoryValuesWO.get(i).getType());
                                k++;
                            }
                        }

                        for (int i = 0; i < subCategoryValuesRE.size(); i++) {
                            String reCat = subCategoryValuesRE.get(i).getType();
                            boolean isit = false;
                            for (int j = 0; j < subCategoryValuesWO.size(); j++) {
                                if (reCat.equals(subCategoryValuesWO.get(j).getType())) {
                                    isit = true;
                                    boolean isShortcode = false;
                                    for (int l = 0; l < subShortCode.size(); l++) {
                                        if (reCat.equals(subShortCode.get(l))) {
                                            isShortcode = true;
                                        }
                                    }
                                    if (!isShortcode) {
                                        subWorkOrders.add(new BarEntry(k, Float.parseFloat(subCategoryValuesWO.get(j).getValue()), subCategoryValuesWO.get(j).getId()));
                                        subReceives.add(new BarEntry(k, Float.parseFloat(subCategoryValuesRE.get(i).getValue()), subCategoryValuesRE.get(i).getId()));
                                        subShortCode.add(subCategoryValuesRE.get(i).getType());
                                        k++;
                                    }
                                }
                            }
                            if (!isit) {
                                subWorkOrders.add(new BarEntry(k, 0f, subCategoryValuesRE.get(i).getId()));
                                subReceives.add(new BarEntry(k, Float.parseFloat(subCategoryValuesRE.get(i).getValue()), subCategoryValuesRE.get(i).getId()));
                                subShortCode.add(subCategoryValuesRE.get(i).getType());
                                k++;
                            }
                        }
                    } else if (subCategoryValuesRE.size() != 0 && subCategoryValuesWO.size() == 0) {

                        int k = 0;
                        for (int i = 0; i < subCategoryValuesRE.size(); i++) {
                            subWorkOrders.add(new BarEntry(k, 0f, subCategoryValuesRE.get(i).getId()));
                            subReceives.add(new BarEntry(k, Float.parseFloat(subCategoryValuesRE.get(i).getValue()), subCategoryValuesRE.get(i).getId()));
                            subShortCode.add(subCategoryValuesRE.get(i).getType());
                            k++;
                        }
                    } else if (subCategoryValuesRE.size() == 0 && subCategoryValuesWO.size() != 0) {

                        int k = 0;
                        for (int i = 0; i < subCategoryValuesWO.size(); i++) {
                            subWorkOrders.add(new BarEntry(k, Float.parseFloat(subCategoryValuesWO.get(i).getValue()), subCategoryValuesWO.get(i).getId()));
                            subReceives.add(new BarEntry(k, 0f, subCategoryValuesWO.get(i).getId()));
                            subShortCode.add(subCategoryValuesWO.get(i).getType());
                        }
                    }


                    XAxis xAxis = subCatMultiChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setDrawGridLines(false);
                    xAxis.setCenterAxisLabels(true);
                    xAxis.setAxisMinimum(0);
                    xAxis.setAxisMaximum(subWorkOrders.size());
                    xAxis.setGranularity(1);
                    subCatMultiChart.getAxisLeft().setAxisMinimum(0);
                    subCatMultiChart.highlightValues(null);

                    BarDataSet set1 = new BarDataSet(subReceives, "Receive");
                    int cc = ColorTemplate.VORDIPLOM_COLORS[3];
                    int newCC = darkenColor(cc,0.8f);

                    set1.setColor(newCC);
//                    set1.setValueFormatter(new ValueFormatter() {
//                        @Override
//                        public String getFormattedValue(float value) {
//                            return String.valueOf((int) Math.floor(value));
//                        }
//                    });
                    BarDataSet set2 = new BarDataSet(subWorkOrders, "WO/PO");
//                    set2.setValueFormatter(new ValueFormatter() {
//                        @Override
//                        public String getFormattedValue(float value) {
//                            return String.valueOf((int) Math.floor(value));
//                        }
//                    });

                    int cc1 = ColorTemplate.VORDIPLOM_COLORS[2];
                    int newCC1 = darkenColor(cc1,0.8f);

                    set2.setColor(newCC1);

                    float groupSpace = 0.04f;
                    float barSpace = 0.02f; // x2 dataset
                    float barWidth = 0.46f;

                    BarData leavedata = new BarData(set2, set1);
                    leavedata.setValueTextSize(12);
                    leavedata.setBarWidth(barWidth); // set the width of each bar
                    subCatMultiChart.animateY(1000);
                    subCatMultiChart.setData(leavedata);
                    subCatMultiChart.groupBars(0, groupSpace, barSpace); // perform the "explicit" grouping
                    subCatMultiChart.invalidate();

                    xAxis.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getAxisLabel(float value, AxisBase axis) {
                            if (value < 0 || value >= subShortCode.size()) {
                                return "";
                            } else {
//                                System.out.println(value);
//                                System.out.println(axis);
//                                System.out.println(shortCode.get((int) value));
                                return (subShortCode.get((int) value));
                            }

                        }
                    });


                    afterCatSelection.setVisibility(View.VISIBLE);


                    whoseItem.setText(selectedCatName+" ("+labelForItem+")" );
                    new ItemCheck().execute();


                } else {
                    subWorkOrders.add(new BarEntry(0,0,1234567));
                    subReceives.add(new BarEntry(0,0,1234567));
                    subShortCode.add("No Data Found");

                    XAxis xAxis = subCatMultiChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setDrawGridLines(false);
                    xAxis.setCenterAxisLabels(true);
                    xAxis.setAxisMinimum(0);
                    xAxis.setAxisMaximum(subWorkOrders.size());
                    xAxis.setGranularity(1);
                    subCatMultiChart.getAxisLeft().setAxisMinimum(0);
                    subCatMultiChart.highlightValues(null);

                    BarDataSet set1 = new BarDataSet(subReceives, "Receive");
                    int cc = ColorTemplate.VORDIPLOM_COLORS[3];
                    int newCC = darkenColor(cc,0.8f);

                    set1.setColor(newCC);
//                    set1.setValueFormatter(new ValueFormatter() {
//                        @Override
//                        public String getFormattedValue(float value) {
//                            return String.valueOf((int) Math.floor(value));
//                        }
//                    });
                    BarDataSet set2 = new BarDataSet(subWorkOrders, "W0/PO");
//                    set2.setValueFormatter(new ValueFormatter() {
//                        @Override
//                        public String getFormattedValue(float value) {
//                            return String.valueOf((int) Math.floor(value));
//                        }
//                    });

                    int cc1 = ColorTemplate.VORDIPLOM_COLORS[2];
                    int newCC1 = darkenColor(cc1,0.8f);

                    set2.setColor(newCC1);

                    float groupSpace = 0.04f;
                    float barSpace = 0.02f; // x2 dataset
                    float barWidth = 0.46f;

                    BarData leavedata = new BarData(set2, set1);
                    leavedata.setValueTextSize(12);
                    leavedata.setBarWidth(barWidth); // set the width of each bar
                    subCatMultiChart.animateY(1000);
                    subCatMultiChart.setData(leavedata);
                    subCatMultiChart.groupBars(0, groupSpace, barSpace); // perform the "explicit" grouping
                    subCatMultiChart.invalidate();

                    xAxis.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getAxisLabel(float value, AxisBase axis) {
                            if (value < 0 || value >= subShortCode.size()) {
                                return "";
                            } else {
//                                System.out.println(value);
//                                System.out.println(axis);
//                                System.out.println(shortCode.get((int) value));
                                return (subShortCode.get((int) value));
                            }

                        }
                    });

                }
            }
            else {
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

                aftercatSelectionItem.setVisibility(View.VISIBLE);
                noItem.setVisibility(View.GONE);

                itemAdapter = new ItemAdapter(itemViewLists, getContext(),RelationsFragment.this);
                itemView.setAdapter(itemAdapter);
                searchItem.setText("");



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

            supplierListsRelations = new ArrayList<>();


            ResultSet resultSet = stmt.executeQuery("SELECT ALL ACC_DTL.AD_ID,\n" +
                    "           ACC_DTL.AD_CODE,\n" +
                    "           ACC_DTL.AD_NAME,\n" +
                    "           ACC_DTL.AD_SHORT_NAME\n" +
                    "  FROM ACC_DTL\n" +
                    " WHERE ACC_DTL.AD_FLAG = 7 AND AD_PHARMACY_FLAG = 0");

            supplierListsRelations.add(new SupplierList("","1234567890","All Suppliers","All"));

            while (resultSet.next()) {
                supplierListsRelations.add(new SupplierList(resultSet.getString(1), resultSet.getString(2),resultSet.getString(3),resultSet.getString(4)));
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

    public void CategoryData() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            workOrders = new ArrayList<>();
            receives = new ArrayList<>();
            shortCode = new ArrayList<>();
            categoryValuesWO = new ArrayList<>();
            categoryValuesRE = new ArrayList<>();
            NoOfEmp = new ArrayList<>();
            woPoLists = new ArrayList<>();

            Statement stmt = connection.createStatement();
            if (firstDate.isEmpty()) {
                firstDate = null;
            }
            if (lastDate.isEmpty()) {
                lastDate = null;
            }
            if (supplier_ad_id_relation.isEmpty()) {
                supplier_ad_id_relation = null;
            }

            System.out.println(selectedRelations);
            System.out.println(selectedWOPO);

            if (selectedRelations.equals("1")) {
                ResultSet resultSet = stmt.executeQuery("WITH JOB\n" +
                        "     AS (  SELECT J.WOJ_IM_ID, NVL (SUM (J.WOJ_JOB_AMT), 0) TOTAL_WOJ_JOB_AMT\n" +
                        "             FROM WORK_ORDER_MST M, WORK_ORDER_JOB J, ACC_DTL A\n" +
                        "            WHERE     M.WOM_ID = J.WOJ_WOM_ID\n" +
                        "                  AND M.WOM_AD_ID = A.AD_ID\n" +
                        "                  AND (   TRUNC(M.WOM_DATE) BETWEEN case when TRUNC(TO_DATE('"+firstDate+"')) is null then TRUNC(M.WOM_DATE) else TRUNC(TO_DATE('"+firstDate+"')) end AND case when TRUNC(TO_DATE('"+lastDate+"')) is null then TRUNC(M.WOM_DATE) else TRUNC(TO_DATE('"+lastDate+"')) end )\n" +
                        "                  AND (M.WOM_AD_ID = "+supplier_ad_id_relation+" OR "+supplier_ad_id_relation+" IS NULL)\n" +
                        "                  AND (   M.P_FINAL_FLAG = 1\n" +
                        "                       OR 1 IS NULL)\n" +
                        "         GROUP BY J.WOJ_IM_ID)\n" +
                        "SELECT ITEM.IM_ID,ITEM.IM_NAME category_name, NVL (JOB.TOTAL_WOJ_JOB_AMT, 0) TOTAL_JOB_AMT\n" +
                        "  FROM JOB, ITEM_MST ITEM\n" +
                        " WHERE JOB.WOJ_IM_ID(+) = ITEM.IM_ID\n" +
                        " ORDER BY ITEM.IM_ID asc");

                while (resultSet.next()) {
                    if (!resultSet.getString(3).equals("0")) {
                        categoryValuesWO.add(new ChartValue(resultSet.getString(1), resultSet.getString(2),resultSet.getString(3)));
                    }
                }

                CallableStatement callableStatement = connection.prepareCall("begin ? := GET_CATEGORY_WISE_RCV_AMT(?,?,?,?); end;");
                callableStatement.setString(2,firstDate);
                callableStatement.setString(3,lastDate);
                callableStatement.setString(4,supplier_ad_id_relation);
                callableStatement.setString(5,"2");
                callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
                callableStatement.execute();

                ResultSet resultSet1 = (ResultSet) callableStatement.getObject(1);

                while (resultSet1.next()) {
                    if (!resultSet1.getString(3).equals("0")) {
                        categoryValuesRE.add(new ChartValue(resultSet1.getString(1), resultSet1.getString(2),resultSet1.getString(3)));
                    }
                }

                callableStatement.close();

                connected = true;

            } else if (selectedRelations.equals("2")) {

                CallableStatement callableStatement = connection.prepareCall("begin ? := GET_WO_PO_LIST(?,?,?,?,?,?,?); end;");
                callableStatement.setString(2,firstDate);
                callableStatement.setString(3,lastDate);
                callableStatement.setString(4,supplier_ad_id_relation);
                callableStatement.setString(5,"1");
                callableStatement.setString(6,selectedWOPO);
                callableStatement.setString(7,null);
                callableStatement.setInt(8,1);
                callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
                callableStatement.execute();

                ResultSet resultSet = (ResultSet) callableStatement.getObject(1);

                while (resultSet.next()) {

                    if (!resultSet.getString(3).equals("0") || !resultSet.getString(2).equals("0") || !resultSet.getString(1).equals("0")) {

                        if (selectedWOPO.equals("2")) {
                            NoOfEmp.add(new PieEntry(Float.parseFloat(resultSet.getString(1)),"Work Order",0));
                        } else {
                            NoOfEmp.add(new PieEntry(Float.parseFloat(resultSet.getString(1)),"Purchase Order",0));
                        }


                        if (selectedWOPO.equals("1")) {

                            NoOfEmp.add(new PieEntry(Float.parseFloat(resultSet.getString(3)),"Purchase Receive",1));

                        }
                        NoOfEmp.add(new PieEntry(Float.parseFloat(resultSet.getString(2)),"Bill Receive",2));
                        NoOfEmp.add(new PieEntry(Float.parseFloat(resultSet.getString(5)),"Bill Paid",3));


                        totalvat = resultSet.getString(4);
                        billpaidTotal = resultSet.getString(5);
                    }


                }

                callableStatement.close();

                CallableStatement callableStatement1 = connection.prepareCall("begin ? := GET_WO_PO_LIST(?,?,?,?,?,?,?); end;");
                callableStatement1.setString(2,firstDate);
                callableStatement1.setString(3,lastDate);
                callableStatement1.setString(4,supplier_ad_id_relation);
                callableStatement1.setString(5,"1");
                callableStatement1.setString(6,selectedWOPO);
                callableStatement1.setString(7,null);
                callableStatement1.setInt(8,2);
                callableStatement1.registerOutParameter(1, OracleTypes.CURSOR);
                callableStatement1.execute();

                ResultSet resultSet1 = (ResultSet) callableStatement1.getObject(1);


                while (resultSet1.next()) {

                    System.out.println(resultSet1.getString(1));
                    double totalBill = resultSet1.getDouble(6) + resultSet1.getDouble(4);
                    double billPayable = totalBill - resultSet1.getDouble(7);
                    woPoLists.add(new WoPoLists(resultSet1.getString(1), resultSet1.getString(2),resultSet1.getString(3),resultSet1.getString(5),resultSet1.getString(4),resultSet1.getString(6),String.valueOf(totalBill),resultSet1.getString(7),String.valueOf(billPayable)));

                }


                callableStatement1.close();

                connected = true;
            }


            if (firstDate == null) {
                firstDate = "";
            }
            if (lastDate == null) {
                lastDate = "";
            }
            if (supplier_ad_id_relation == null) {
                supplier_ad_id_relation = "";
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

            subCategoryValuesWO = new ArrayList<>();
            subCategoryValuesRE = new ArrayList<>();

            Statement stmt = connection.createStatement();
            if (firstDate.isEmpty()) {
                firstDate = null;
            }
            if (lastDate.isEmpty()) {
                lastDate = null;
            }
            if (supplier_ad_id_relation.isEmpty()) {
                supplier_ad_id_relation = null;
            }

            if (selectedRelations.equals("1")) {

                ResultSet resultSet = stmt.executeQuery("WITH WORK_ORDER\n" +
                        "     AS (  SELECT D.WOD_ITEM_ID,\n" +
                        "                  NVL (\n" +
                        "                     SUM (ROUND (NVL (D.WOD_QTY, 0) * NVL (D.WOD_RATE, 0), 2)),\n" +
                        "                     0)\n" +
                        "                     TOTAL_WO_AMT\n" +
                        "             FROM WORK_ORDER_MST M,\n" +
                        "                  WORK_ORDER_JOB J,\n" +
                        "                  WORK_ORDER_DTL D,\n" +
                        "                  ACC_DTL      A\n" +
                        "            WHERE     M.WOM_ID = J.WOJ_WOM_ID\n" +
                        "                  AND D.WOD_WOJ_ID = J.WOJ_ID\n" +
                        "                  AND M.WOM_AD_ID = A.AD_ID\n" +
                        "                  AND (TRUNC(M.WOM_DATE) BETWEEN CASE\n" +
                        "                                             WHEN TRUNC(TO_DATE('"+firstDate+"')) IS NULL\n" +
                        "                                             THEN\n" +
                        "                                                TRUNC(M.WOM_DATE)\n" +
                        "                                             ELSE\n" +
                        "                                                TRUNC(TO_DATE('"+firstDate+"'))\n" +
                        "                                          END\n" +
                        "                                      AND CASE\n" +
                        "                                             WHEN TRUNC(TO_DATE('"+lastDate+"')) IS NULL\n" +
                        "                                             THEN\n" +
                        "                                                TRUNC(M.WOM_DATE)\n" +
                        "                                             ELSE\n" +
                        "                                                TRUNC(TO_DATE('"+lastDate+"'))\n" +
                        "                                          END)\n" +
                        "                  AND (M.WOM_AD_ID = "+supplier_ad_id_relation+" OR "+supplier_ad_id_relation+" IS NULL)\n" +
                        "                  AND (   M.P_FINAL_FLAG = 1 \n" +
                        "                       OR 1 IS NULL)\n" +
                        "         GROUP BY D.WOD_ITEM_ID)\n" +
                        "  SELECT ITEM.SUBCATM_NAME            SUBCATEGORY_NAME,\n" +
                        "         ITEM.ISC_ID,\n" +
                        "         NVL (SUM (WO.TOTAL_WO_AMT), 0) TOTAL_WO_AMT\n" +
                        "    FROM WORK_ORDER WO, ITEM_DETAILS_V ITEM\n" +
                        "   WHERE WO.WOD_ITEM_ID = ITEM.ITEM_ID AND ITEM.IM_ID = "+catrgoryId+"\n" +
                        "GROUP BY ITEM.SUBCATM_NAME, ITEM.ISC_ID\n");

                while (resultSet.next()) {
                    if (!resultSet.getString(3).equals("0")) {
                        subCategoryValuesWO.add(new ChartValue(resultSet.getString(2), resultSet.getString(1),resultSet.getString(3)));
                    }
                }

                CallableStatement callableStatement = connection.prepareCall("begin ? := GET_SUBCATEGORY_WISE_RCV_AMT(?,?,?,?,?); end;");
                callableStatement.setInt(2,Integer.parseInt(catrgoryId));
                callableStatement.setString(3,firstDate);
                callableStatement.setString(4,lastDate);
                callableStatement.setString(5,supplier_ad_id_relation);
                callableStatement.setString(6,"2");
                callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
                callableStatement.execute();

                ResultSet resultSet1 = (ResultSet) callableStatement.getObject(1);



                while (resultSet1.next()) {
                    subCategoryValuesRE.add(new ChartValue(resultSet1.getString(1), resultSet1.getString(2),resultSet1.getString(3)));
                }
                callableStatement.close();

                connected = true;

            }


            if (firstDate == null) {
                firstDate = "";
            }
            if (lastDate == null) {
                lastDate = "";
            }
            if (supplier_ad_id_relation == null) {
                supplier_ad_id_relation = "";
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

            itemViewLists = new ArrayList<>();

            Statement stmt = connection.createStatement();
            if (firstDate.isEmpty()) {
                firstDate = null;
            }
            if (lastDate.isEmpty()) {
                lastDate = null;
            }
            if (supplier_ad_id_relation.isEmpty()) {
                supplier_ad_id_relation = null;
            }
            if (catrgoryId.isEmpty()) {
                catrgoryId = null;
            }
            if (subCategoryId.isEmpty()) {
                subCategoryId = null;
            }

            if (labelForItem.equals("WO/PO")) {

                CallableStatement callableStatement = connection.prepareCall("begin ? := GET_WO_RCV_DETAILS(?,?,?,?,?); end;");
                callableStatement.setInt(2,Integer.parseInt(catrgoryId));
                callableStatement.setString(3,subCategoryId);
                callableStatement.setString(4,firstDate);
                callableStatement.setString(5,lastDate);
                callableStatement.setString(6,supplier_ad_id_relation);
                callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
                callableStatement.execute();

                ResultSet resultSet1 = (ResultSet) callableStatement.getObject(1);



                while (resultSet1.next()) {
                    itemViewLists.add(new ItemViewList(resultSet1.getString(1), resultSet1.getString(2),resultSet1.getString(3),resultSet1.getString(4),
                            resultSet1.getString(5),resultSet1.getString(6),resultSet1.getString(7),resultSet1.getString(8),
                            resultSet1.getString(9),resultSet1.getString(10),resultSet1.getString(11),resultSet1.getString(12),
                            resultSet1.getString(13),resultSet1.getString(14),resultSet1.getString(15),resultSet1.getString(16)));
                }
                callableStatement.close();
            } else if (labelForItem.equals("Receive")) {

                CallableStatement callableStatement = connection.prepareCall("begin ? := GET_RCV_WISE_WO_DETAILS(?,?,?,?,?); end;");
                callableStatement.setInt(2,Integer.parseInt(catrgoryId));
                callableStatement.setString(3,subCategoryId);
                callableStatement.setString(4,firstDate);
                callableStatement.setString(5,lastDate);
                callableStatement.setString(6,supplier_ad_id_relation);
                callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
                callableStatement.execute();

                ResultSet resultSet1 = (ResultSet) callableStatement.getObject(1);



                while (resultSet1.next()) {
                    itemViewLists.add(new ItemViewList(resultSet1.getString(1), resultSet1.getString(2),resultSet1.getString(3),resultSet1.getString(4),
                            resultSet1.getString(5),resultSet1.getString(6),resultSet1.getString(7),resultSet1.getString(8),
                            resultSet1.getString(9),resultSet1.getString(10),resultSet1.getString(11),resultSet1.getString(12),
                            resultSet1.getString(13),resultSet1.getString(14),resultSet1.getString(15),resultSet1.getString(16)));
                }
                callableStatement.close();
            }



            connected = true;


            if (firstDate == null) {
                firstDate = "";
            }
            if (lastDate == null) {
                lastDate = "";
            }
            if (supplier_ad_id_relation == null) {
                supplier_ad_id_relation = "";
            }
            if (catrgoryId == null) {
                catrgoryId = "";
            }
            if (subCategoryId == null) {
                subCategoryId = "";
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
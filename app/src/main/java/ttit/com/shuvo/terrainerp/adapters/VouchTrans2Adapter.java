package ttit.com.shuvo.terrainerp.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.arrayList.VoucherLists2;
import ttit.com.shuvo.terrainerp.arrayList.VoucherLists3;
import ttit.com.shuvo.terrainerp.dialogues.DebitCreditVoucher;
import ttit.com.shuvo.terrainerp.dialogues.DeliveryChallanDetails;
import ttit.com.shuvo.terrainerp.dialogues.DirectPurchase;
import ttit.com.shuvo.terrainerp.dialogues.SalesOrderDetails;

import static ttit.com.shuvo.terrainerp.fragments.VoucherTransaction.deliveryChallanListsVT;
import static ttit.com.shuvo.terrainerp.fragments.VoucherTransaction.salesOrderListsVT;

public class VouchTrans2Adapter extends RecyclerView.Adapter<VouchTrans2Adapter.VT2Holder> {

    private ArrayList<VoucherLists2> mCategoryItem;
    private final Context myContext;
    public VouchTrans3Adapter vouchTrans3Adapter;
    public static int fromVTSO = 0;

    public static String VM_NO = "";
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

    int selectedPosition = -1;

    public VouchTrans2Adapter(ArrayList<VoucherLists2> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    public class VT2Holder extends RecyclerView.ViewHolder {
        public TextView voucherNo;
        public TextView particulars;

        public RecyclerView itemDetails;

        public LinearLayout bottomborder;


        public VT2Holder(@NonNull View itemView) {
            super(itemView);
            voucherNo = itemView.findViewById(R.id.voucher_no_v_trans);
            particulars = itemView.findViewById(R.id.particulars_v_trans);

            itemDetails = itemView.findViewById(R.id.voucher_trans_report_view3);
            bottomborder = itemView.findViewById(R.id.layout_bottom_border_v_trans);

            voucherNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    if (mCategoryItem.get(getAdapterPosition()).getType().equals("SOM")) {
                        for (int i = 0; i < salesOrderListsVT.size(); i++) {
                            if (mCategoryItem.get(getAdapterPosition()).getOrderNo().equals(salesOrderListsVT.get(i).getOrder_no())) {

                                ORDER_NO = salesOrderListsVT.get(i).getOrder_no();
                                ORDER_DATE = salesOrderListsVT.get(i).getOrder_date();
                                CLIENT_NAME = salesOrderListsVT.get(i).getAd_name();
                                CLIENT_CODE = salesOrderListsVT.get(i).getClient_code();
                                TARGET_ADDRESS = salesOrderListsVT.get(i).getTarget_address();
                                ED_DATE = salesOrderListsVT.get(i).getEdd();
                                CONTACT_NO = salesOrderListsVT.get(i).getContact_number();
                                CLIENT_EMAIL = salesOrderListsVT.get(i).getPerson_email();
                                SOM_ID = salesOrderListsVT.get(i).getOrder_id();
                                CONTACT_PERSON = salesOrderListsVT.get(i).getContact_person();
                                ADVANCE_AMOUNT = salesOrderListsVT.get(i).getAdvance_amnt();
                                VAT_AMNT = salesOrderListsVT.get(i).getVat_amnt();
                                fromVTSO = 1;

                                SalesOrderDetails salesOrderDetails = new SalesOrderDetails(myContext);
                                salesOrderDetails.show(activity.getSupportFragmentManager(),"VTSO");
                                break;
                            }
                        }

                    }
                    else if (mCategoryItem.get(getAdapterPosition()).getType().equals("AV")) {
                        VM_NO = mCategoryItem.get(getAdapterPosition()).getOrderNo();

                        DebitCreditVoucher debitCreditVoucher = new DebitCreditVoucher(myContext);
                        debitCreditVoucher.show(activity.getSupportFragmentManager(),"VTDV");

                    }
                    else if (mCategoryItem.get(getAdapterPosition()).getType().equals("DPRCV")) {
                        VM_NO = mCategoryItem.get(getAdapterPosition()).getOrderNo();

                        DirectPurchase directPurchase = new DirectPurchase(myContext);
                        directPurchase.show(activity.getSupportFragmentManager(),"DPR");
                    }
                    else if (mCategoryItem.get(getAdapterPosition()).getType().equals("SM")) {
                        for (int i = 0; i < deliveryChallanListsVT.size(); i++) {
                            if (mCategoryItem.get(getAdapterPosition()).getOrderNo().equals(deliveryChallanListsVT.get(i).getDelivery_no())) {

                                INV_NO = deliveryChallanListsVT.get(i).getDelivery_no();
                                INV_DATE = deliveryChallanListsVT.get(i).getDelivery_date();
                                SO_NO = deliveryChallanListsVT.get(i).getOrder_no();
                                SO_DATE = deliveryChallanListsVT.get(i).getOrder_date();
                                C_NAME = deliveryChallanListsVT.get(i).getClient_name();
                                C_CODE = deliveryChallanListsVT.get(i).getAd_code();
                                ADDS = deliveryChallanListsVT.get(i).getTarget_address();
                                EDD = deliveryChallanListsVT.get(i).getEdd();
                                C_EMAIL = deliveryChallanListsVT.get(i).getContact_email();
                                PERSON = deliveryChallanListsVT.get(i).getContact_person();
                                CONTACT = deliveryChallanListsVT.get(i).getContact_number();
                                SM_ID = deliveryChallanListsVT.get(i).getSm_id();
                                VAT_AMNT_DC = deliveryChallanListsVT.get(i).getVat_amnt();
                                fromVTSO = 1;

                                DeliveryChallanDetails deliveryChallanDetails = new DeliveryChallanDetails(myContext);
                                deliveryChallanDetails.show(activity.getSupportFragmentManager(),"DCDVT");
                                break;
                            }
                        }
                    }
                }
            });


        }
    }

    @NonNull
    @Override
    public VT2Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.v_trans_view_2, parent, false);
        return new VT2Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VT2Holder holder, int position) {

        VoucherLists2 voucherLists2 = mCategoryItem.get(position);

        holder.voucherNo.setText(voucherLists2.getvNo());
        holder.particulars.setText(voucherLists2.getParticulars());

        ArrayList<VoucherLists3> voucherLists3s = voucherLists2.getVoucherLists3s();

        holder.itemDetails.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(myContext);
        holder.itemDetails.setLayoutManager(layoutManager);
        vouchTrans3Adapter = new VouchTrans3Adapter(voucherLists3s,myContext);
        holder.itemDetails.setAdapter(vouchTrans3Adapter);

        if (position == mCategoryItem.size()-1) {
            holder.bottomborder.setVisibility(View.GONE);
        } else {
            holder.bottomborder.setVisibility(View.VISIBLE);
        }
        ColorStateList color = holder.particulars.getTextColors();

        if (voucherLists2.getType().equals("SOM") || voucherLists2.getType().equals("AV") || voucherLists2.getType().equals("DPRCV") || voucherLists2.getType().equals("SM")) {
            holder.voucherNo.setTextColor(Color.parseColor("#3A9FEE"));
        }  else {
            holder.voucherNo.setTextColor(color);
        }
//        if (sods_selected_position == positionFromFirstAdapter) {
//            holder.itemView.setBackgroundColor(Color.parseColor("#81ecec"));
//        }
//        else {
//            holder.itemView.setBackgroundColor(Color.parseColor("#f5f6fa"));
//        }

    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }
}

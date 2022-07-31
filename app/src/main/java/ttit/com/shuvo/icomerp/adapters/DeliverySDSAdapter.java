package ttit.com.shuvo.icomerp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.arrayList.DeliverySDSList;
import ttit.com.shuvo.icomerp.arrayList.VoucherSDSList;

import static ttit.com.shuvo.icomerp.adapters.ClientSDSAdapter.positionFromFirstAdapter;
import static ttit.com.shuvo.icomerp.fragments.SalesOrderDeliverySummary.sods_selected_position;

public class DeliverySDSAdapter extends RecyclerView.Adapter<DeliverySDSAdapter.DeliveryHolder> {

    private final ArrayList<DeliverySDSList> mCategoryItem;
    private final Context myContext;
    public VoucherSDSAdapter voucherSDSAdapter;

    public DeliverySDSAdapter(ArrayList<DeliverySDSList> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    public static class DeliveryHolder extends RecyclerView.ViewHolder {
        public TextView deliveryNo;
        public TextView deliveryDate;
        public TextView deliveryAmnt;
        public TextView deliveryPaid;
        public TextView deliveryBalance;

        public RecyclerView itemDetails;

        public LinearLayout bottomborder;


        public DeliveryHolder(@NonNull View itemView) {
            super(itemView);
            deliveryNo = itemView.findViewById(R.id.delivery_no_sds);
            deliveryDate = itemView.findViewById(R.id.delivery_date_sds);
            deliveryAmnt = itemView.findViewById(R.id.delivery_amnt_sds);
            deliveryPaid = itemView.findViewById(R.id.paid_amnt_sds);
            deliveryBalance = itemView.findViewById(R.id.delivery_balance_sds);

            itemDetails = itemView.findViewById(R.id.voucher_no_view_sds);
            bottomborder = itemView.findViewById(R.id.layout_bottom_border_delivery);

        }
    }

    @NonNull
    @Override
    public DeliverySDSAdapter.DeliveryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.delivery_no_sales_deliver_sum, parent, false);
        return new DeliveryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliverySDSAdapter.DeliveryHolder holder, int position) {

        DeliverySDSList deliverySDSList = mCategoryItem.get(position);

        holder.deliveryNo.setText(deliverySDSList.getDeliveryNo());
        holder.deliveryDate.setText(deliverySDSList.getDeliveryDate());
        holder.deliveryAmnt.setText(deliverySDSList.getDeliveryAmnt());
        holder.deliveryPaid.setText(deliverySDSList.getPaidAmnt());
        holder.deliveryBalance.setText(deliverySDSList.getDeliveryBalance());

        ArrayList<VoucherSDSList> voucherSDSLists = deliverySDSList.getVoucherSDSLists();

        holder.itemDetails.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(myContext);
        holder.itemDetails.setLayoutManager(layoutManager);
        voucherSDSAdapter = new VoucherSDSAdapter(voucherSDSLists,myContext);
        holder.itemDetails.setAdapter(voucherSDSAdapter);

        if (position == mCategoryItem.size()-1) {
            holder.bottomborder.setVisibility(View.GONE);
        } else {
            holder.bottomborder.setVisibility(View.VISIBLE);
        }
        if (sods_selected_position == positionFromFirstAdapter) {
            holder.itemView.setBackgroundColor(Color.parseColor("#81ecec"));
        }
        else {
            holder.itemView.setBackgroundColor(Color.parseColor("#f5f6fa"));
        }
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }
}

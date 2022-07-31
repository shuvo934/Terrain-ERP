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
import ttit.com.shuvo.icomerp.arrayList.OrderSDSList;

import static ttit.com.shuvo.icomerp.adapters.ClientSDSAdapter.positionFromFirstAdapter;
import static ttit.com.shuvo.icomerp.fragments.SalesOrderDeliverySummary.sods_selected_position;

public class OrderSDSAdapter extends RecyclerView.Adapter<OrderSDSAdapter.OrderHolder> {
    private final ArrayList<OrderSDSList> mCategoryItem;
    private final Context myContext;
    public DeliverySDSAdapter deliverySDSAdapter;

    public OrderSDSAdapter(ArrayList<OrderSDSList> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    public static class OrderHolder extends RecyclerView.ViewHolder {

        public TextView orderNo;
        public TextView orderDate;
        public TextView orderAmnt;
        public TextView advanceAmnt;
        public TextView accountPaid;
        public TextView orderBalance;

        public RecyclerView itemDetails;

        public LinearLayout bottomBorder;

        public OrderHolder(@NonNull View itemView) {
            super(itemView);

            orderNo = itemView.findViewById(R.id.order_no_sds);
            orderDate = itemView.findViewById(R.id.order_date_sds);
            orderAmnt = itemView.findViewById(R.id.order_amount_sds);
            advanceAmnt = itemView.findViewById(R.id.advance_amount_sds);
            accountPaid = itemView.findViewById(R.id.total_paid_sds);
            orderBalance = itemView.findViewById(R.id.order_baalnce_sds);

            itemDetails = itemView.findViewById(R.id.delivery_no_view_sds);
            bottomBorder = itemView.findViewById(R.id.layout_bottom_border_order);
        }
    }

    @NonNull
    @Override
    public OrderSDSAdapter.OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.order_no_sales_deliver_sum, parent, false);
        return new OrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderSDSAdapter.OrderHolder holder, int position) {

        OrderSDSList orderSDSList = mCategoryItem.get(position);

        holder.orderNo.setText(orderSDSList.getOrderNo());
        holder.orderDate.setText(orderSDSList.getOrderDate());
        holder.orderAmnt.setText(orderSDSList.getOrderAmnt());
        holder.advanceAmnt.setText(orderSDSList.getAdvanceAmnt());
        holder.accountPaid.setText(orderSDSList.getTotalPaid());
        holder.orderBalance.setText(orderSDSList.getOrderBalance());

        ArrayList<DeliverySDSList> deliverySDSLists = orderSDSList.getDeliverySDSLists();

        holder.itemDetails.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(myContext);
        holder.itemDetails.setLayoutManager(layoutManager);
        deliverySDSAdapter = new DeliverySDSAdapter(deliverySDSLists,myContext);
        holder.itemDetails.setAdapter(deliverySDSAdapter);

        if (position == mCategoryItem.size()-1) {
            holder.bottomBorder.setVisibility(View.GONE);
        } else {
            holder.bottomBorder.setVisibility(View.VISIBLE);
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

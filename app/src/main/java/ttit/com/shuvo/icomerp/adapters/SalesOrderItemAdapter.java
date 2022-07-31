package ttit.com.shuvo.icomerp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.arrayList.SalesOrderItemLists;

public class SalesOrderItemAdapter extends RecyclerView.Adapter<SalesOrderItemAdapter.SOIHolder> {

    private final ArrayList<SalesOrderItemLists> mCategoryItem;
    private final Context myContext;


    public SalesOrderItemAdapter(ArrayList<SalesOrderItemLists> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public SOIHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.sales_order_item_details_view, parent, false);
        return new SOIHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SOIHolder holder, int position) {

        SalesOrderItemLists salesOrderItemLists = mCategoryItem.get(position);
        holder.itemName.setText(salesOrderItemLists.getItemName());
        holder.hsCode.setText(salesOrderItemLists.getHsCode());
        holder.partNo.setText(salesOrderItemLists.getPartNo());
        holder.itemQuantity.setText(salesOrderItemLists.getQty());
        holder.itemUnit.setText(salesOrderItemLists.getUnit());
        holder.unitPrice.setText(salesOrderItemLists.getUnitPrice());
        holder.discountType.setText(salesOrderItemLists.getDiscountType());
        holder.disValueUnit.setText(salesOrderItemLists.getDiscountValueUnit());
        holder.amnt.setText(salesOrderItemLists.getOrderAmnt());
        holder.delQty.setText(salesOrderItemLists.getDeliverdQty());
        holder.rtnQty.setText(salesOrderItemLists.getRtnQty());
        holder.balQty.setText(salesOrderItemLists.getBalanceQty());
        holder.samQty.setText(salesOrderItemLists.getSampleQty());
        holder.delSamQty.setText(salesOrderItemLists.getDeliverdSampQty());
        holder.balSamQty.setText(salesOrderItemLists.getBalancedSampQty());
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public static class SOIHolder extends RecyclerView.ViewHolder {

        public TextView itemName;
        public TextView hsCode;
        public TextView partNo;
        public TextView itemQuantity;
        public TextView itemUnit;
        public TextView unitPrice;
        public TextView discountType;
        public TextView disValueUnit;
        public TextView amnt;
        public TextView delQty;
        public TextView rtnQty;
        public TextView balQty;
        public TextView samQty;
        public TextView delSamQty;
        public TextView balSamQty;

        public SOIHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.item_name_sod);
            hsCode = itemView.findViewById(R.id.hs_code_sod);
            partNo = itemView.findViewById(R.id.part_number_sod);
            itemQuantity = itemView.findViewById(R.id.quantity_sod);
            itemUnit = itemView.findViewById(R.id.unit_sod);
            unitPrice = itemView.findViewById(R.id.unit_price_sod);
            discountType = itemView.findViewById(R.id.discount_type_sod);
            disValueUnit = itemView.findViewById(R.id.dis_value_unit_sod);
            amnt = itemView.findViewById(R.id.amount_sod);
            delQty = itemView.findViewById(R.id.deliverd_qty_sod);
            rtnQty = itemView.findViewById(R.id.return_qty_sod);
            balQty = itemView.findViewById(R.id.balance_qty_sod);
            samQty = itemView.findViewById(R.id.sample_qty_sod);
            delSamQty = itemView.findViewById(R.id.delivered_samp_qty_sod);
            balSamQty = itemView.findViewById(R.id.balance_samp_qty_sod);

        }
    }
}

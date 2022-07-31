package ttit.com.shuvo.icomerp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.arrayList.PurchaseOrderAppItemDetailsLists;

public class PurchaseOrderAppItemAdapter extends RecyclerView.Adapter<PurchaseOrderAppItemAdapter.POAIHolder> {

    private final ArrayList<PurchaseOrderAppItemDetailsLists> mCategoryItem;
    private final Context myContext;

    public PurchaseOrderAppItemAdapter(ArrayList<PurchaseOrderAppItemDetailsLists> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public POAIHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.purchase_order_item_details_view, parent, false);
        return new POAIHolder(view);
    }

    public static class POAIHolder extends RecyclerView.ViewHolder {

        public TextView itemName;
        public TextView hsCode;
        public TextView partNo;
        public TextView quntity;
        public TextView itemUnit;
        public TextView itemCode;
        public TextView balaQty;
        public TextView reqQty;
        public TextView unitPrice;
        public TextView itemAmount;

        public POAIHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.item_name_poaid);
            hsCode = itemView.findViewById(R.id.hs_code_poaid);
            partNo = itemView.findViewById(R.id.part_no_poaid);
            quntity = itemView.findViewById(R.id.quantity_poaid);
            itemUnit = itemView.findViewById(R.id.unit_poaid);
            itemCode = itemView.findViewById(R.id.item_code_poaid);
            balaQty = itemView.findViewById(R.id.req_bala_qty_poaid);
            reqQty = itemView.findViewById(R.id.req_qty_poaid);
            unitPrice = itemView.findViewById(R.id.unit_price_poaid);
            itemAmount = itemView.findViewById(R.id.amount_poaid);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull POAIHolder holder, int position) {

        PurchaseOrderAppItemDetailsLists purchaseReqAppItemlists = mCategoryItem.get(position);
        holder.itemName.setText(purchaseReqAppItemlists.getItemName());
        holder.hsCode.setText(purchaseReqAppItemlists.getItemHSCode());
        holder.partNo.setText(purchaseReqAppItemlists.getItemPartNo());
        holder.quntity.setText(purchaseReqAppItemlists.getWodQty());
        holder.itemUnit.setText(purchaseReqAppItemlists.getItemUnit());
        holder.itemCode.setText(purchaseReqAppItemlists.getItemCode());
        holder.balaQty.setText(purchaseReqAppItemlists.getBalanceQty());
        holder.reqQty.setText(purchaseReqAppItemlists.getReqQty());
        holder.unitPrice.setText(purchaseReqAppItemlists.getWodRate());
        holder.itemAmount.setText(purchaseReqAppItemlists.getAmount());

    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }
}

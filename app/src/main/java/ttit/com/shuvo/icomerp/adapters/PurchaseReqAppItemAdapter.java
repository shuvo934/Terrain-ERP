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
import ttit.com.shuvo.icomerp.arrayList.PurchaseReqAppItemlists;

public class PurchaseReqAppItemAdapter extends RecyclerView.Adapter<PurchaseReqAppItemAdapter.PRAIHolder> {

    private final ArrayList<PurchaseReqAppItemlists> mCategoryItem;
    private final Context myContext;

    public PurchaseReqAppItemAdapter(ArrayList<PurchaseReqAppItemlists> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public PRAIHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.pra_item_details_details_view, parent, false);
        return new PRAIHolder(view);
    }

    public static class PRAIHolder extends RecyclerView.ViewHolder {

        public TextView itemName;
        public TextView hsCode;
        public TextView partNo;
        public TextView itemStockQuantity;
        public TextView itemUnit;
        public TextView itemCode;

        public PRAIHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.item_name_praidd);
            hsCode = itemView.findViewById(R.id.hs_code_praidd);
            partNo = itemView.findViewById(R.id.part_no_praidd);
            itemStockQuantity = itemView.findViewById(R.id.stock_qty_praidd);
            itemUnit = itemView.findViewById(R.id.unit_praidd);
            itemCode = itemView.findViewById(R.id.item_code_praidd);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull PRAIHolder holder, int position) {

        PurchaseReqAppItemlists purchaseReqAppItemlists = mCategoryItem.get(position);
        holder.itemName.setText(purchaseReqAppItemlists.getItemName());
        holder.hsCode.setText(purchaseReqAppItemlists.getItemHSCode());
        holder.partNo.setText(purchaseReqAppItemlists.getItemPartNo());
        holder.itemStockQuantity.setText(purchaseReqAppItemlists.getStockQty());
        holder.itemUnit.setText(purchaseReqAppItemlists.getUnit());
        holder.itemCode.setText(purchaseReqAppItemlists.getItemCode());
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }
}

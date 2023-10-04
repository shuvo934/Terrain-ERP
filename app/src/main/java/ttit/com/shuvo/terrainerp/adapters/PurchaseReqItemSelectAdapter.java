package ttit.com.shuvo.terrainerp.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.arrayList.PurchaseReqItemList;

public class PurchaseReqItemSelectAdapter extends RecyclerView.Adapter<PurchaseReqItemSelectAdapter.PRISHolder> {
    private ArrayList<PurchaseReqItemList> mCategoryItem;
    private final Context myContext;
    private final ClickedItem myClickedItem;

    public PurchaseReqItemSelectAdapter(ArrayList<PurchaseReqItemList> mCategoryItem, Context myContext, ClickedItem myClickedItem) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
        this.myClickedItem = myClickedItem;
    }

    @NonNull
    @Override
    public PRISHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.pr_item_details_view, parent, false);
        return new PRISHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull PRISHolder holder, int position) {
        PurchaseReqItemList categoryItem = mCategoryItem.get(position);

        holder.itemCode.setText(categoryItem.getItem_code());
        holder.itemName.setText(categoryItem.getItem_reference_name());
        holder.hsCode.setText(categoryItem.getItem_hs_code());
        holder.partNumber.setText(categoryItem.getItem_part_number());
        holder.itemUnit.setText(categoryItem.getItem_unit());
        holder.stockQty.setText(categoryItem.getStock());
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public  class PRISHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView itemCode;
        public TextView itemName;
        public TextView hsCode;
        public TextView partNumber;
        public TextView itemUnit;
        public TextView stockQty;
        ClickedItem mClickedItem;

        public PRISHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            itemCode = itemView.findViewById(R.id.item_code_for_item_select_purchase_requisition);
            itemName = itemView.findViewById(R.id.item_name_for_item_select_purchase_requisition);
            hsCode = itemView.findViewById(R.id.hs_code_for_item_select_purchase_requisition);
            partNumber = itemView.findViewById(R.id.part_number_for_item_select_purchase_requisition);
            itemUnit = itemView.findViewById(R.id.unit_for_item_select_purchase_requisition);
            stockQty = itemView.findViewById(R.id.stock_qty_for_item_select_purchase_requisition);
            this.mClickedItem = ci;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            mClickedItem.onItemClicked(getAdapterPosition());
            //Log.i("Client Name", mCategoryItem.get(getAdapterPosition()).getvDate());
        }
    }

    public interface ClickedItem {
        void onItemClicked(int CategoryPosition);
    }

    public void filterList(ArrayList<PurchaseReqItemList> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }
}

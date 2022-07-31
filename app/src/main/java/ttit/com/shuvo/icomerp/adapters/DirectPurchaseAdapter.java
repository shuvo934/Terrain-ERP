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
import ttit.com.shuvo.icomerp.arrayList.DirectPurchaseLists;
import ttit.com.shuvo.icomerp.arrayList.SalesOrderItemLists;

public class DirectPurchaseAdapter extends RecyclerView.Adapter<DirectPurchaseAdapter.DPRHolder> {

    private final ArrayList<DirectPurchaseLists> mCategoryItem;
    private final Context myContext;

    public DirectPurchaseAdapter(ArrayList<DirectPurchaseLists> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public DPRHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.direct_purchase_item_details_view, parent, false);
        return new DPRHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DPRHolder holder, int position) {

        DirectPurchaseLists directPurchaseLists = mCategoryItem.get(position);
        holder.itemName.setText(directPurchaseLists.getItemName());
        holder.hsCode.setText(directPurchaseLists.getHsCode());
        holder.partNo.setText(directPurchaseLists.getPartNo());
        holder.itemCode.setText(directPurchaseLists.getItemCode());
        holder.itemQuantity.setText(directPurchaseLists.getQty());
        holder.itemUnit.setText(directPurchaseLists.getUnit());
        holder.unitPrice.setText(directPurchaseLists.getPrice());
        holder.color.setText(directPurchaseLists.getColorName());
        holder.size.setText(directPurchaseLists.getSizeName());
        holder.amnt.setText(directPurchaseLists.getTotal_amnt());
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public static class DPRHolder extends RecyclerView.ViewHolder {

        public TextView itemName;
        public TextView hsCode;
        public TextView itemCode;
        public TextView partNo;
        public TextView itemQuantity;
        public TextView itemUnit;
        public TextView unitPrice;
        public TextView color;
        public TextView size;
        public TextView amnt;

        public DPRHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.item_name_dpr);
            hsCode = itemView.findViewById(R.id.hs_code_dpr);
            itemCode = itemView.findViewById(R.id.item_code_dpr);
            partNo = itemView.findViewById(R.id.item_part_no_dpr);
            itemQuantity = itemView.findViewById(R.id.item_qty_dpr);
            itemUnit = itemView.findViewById(R.id.item_unit_dpr);
            unitPrice = itemView.findViewById(R.id.item_price_dpr);
            color = itemView.findViewById(R.id.item_color_dpr);
            size = itemView.findViewById(R.id.item_size_dpr);
            amnt = itemView.findViewById(R.id.item_total_amnt_dpr);



        }
    }
}

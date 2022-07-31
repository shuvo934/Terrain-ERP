package ttit.com.shuvo.icomerp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.arrayList.ItemDetaisList;
import ttit.com.shuvo.icomerp.arrayList.StockItemList;

public class ItemDetailsAdapter extends RecyclerView.Adapter<ItemDetailsAdapter.ItemHolder> {

    private ArrayList<ItemDetaisList> mCategoryItem;
    private final ClickedItem myClickedItem;
    private final Context myContext;


    public ItemDetailsAdapter(ArrayList<ItemDetaisList> categoryItems, Context context, ClickedItem cli) {
        this.mCategoryItem = categoryItems;
        this.myClickedItem = cli;
        this.myContext = context;
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView sl_no;
        public TextView itemCode;
        public TextView itemName;
        public TextView unit;
        public TextView qty;
        public TextView vat;
        public TextView amnt;
        public TextView sales;
        public TextView hsCode;

        ClickedItem mClickedItem;

        public ItemHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            sl_no = itemView.findViewById(R.id.serial_no);
            itemCode = itemView.findViewById(R.id.item_code);
            itemName = itemView.findViewById(R.id.item_reference_name);
            unit = itemView.findViewById(R.id.item_unit);
            qty = itemView.findViewById(R.id.item_quantity);
            vat = itemView.findViewById(R.id.item_vat_amnt);
            amnt = itemView.findViewById(R.id.item_purchase_amount);
            sales = itemView.findViewById(R.id.item_sales_price);
            hsCode = itemView.findViewById(R.id.item_hs_code);


            this.mClickedItem = ci;

            itemView.setOnClickListener(this::onClick);

        }

        @Override
        public void onClick(View v) {
            mClickedItem.onCategoryClicked(getAdapterPosition());
            Log.i("Category Name", mCategoryItem.get(getAdapterPosition()).getItem_name());
        }
    }

    public interface ClickedItem {
        void onCategoryClicked(int CategoryPosition);
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.item_details_view, parent, false);
        ItemHolder categoryViewHolder = new ItemHolder(view, myClickedItem);
        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {

        ItemDetaisList categoryItem = mCategoryItem.get(position);

        holder.sl_no.setText(categoryItem.getSlNo());
        holder.itemCode.setText(categoryItem.getItem_code());
        holder.itemName.setText(categoryItem.getItem_name());
        holder.unit.setText(categoryItem.getItem_unit());
        holder.qty.setText(categoryItem.getItem_qty());
        holder.vat.setText(categoryItem.getVat());
        holder.amnt.setText(categoryItem.getPurchase_amnt());
        holder.sales.setText(categoryItem.getSales_price());
        holder.hsCode.setText(categoryItem.getHs_code());


    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public void filterList(ArrayList<ItemDetaisList> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }
}

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
import ttit.com.shuvo.terrainerp.arrayList.ItemAllLists;

public class AllItemSubItemAdapter extends RecyclerView.Adapter<AllItemSubItemAdapter.AISIHolder> {

    private ArrayList<ItemAllLists> mCategoryItem;
    private final Context myContext;

    public AllItemSubItemAdapter(ArrayList<ItemAllLists> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    public class AISIHolder extends RecyclerView.ViewHolder{
        public TextView slNo;
        public TextView item_code;
        public TextView part_no;
        public TextView item_name;
        public TextView item_unit;
        public TextView item_color;
        public TextView item_size;
        public TextView item_sales_price;
        public TextView item_stck_qty;
        public TextView item_stck_val;


        public AISIHolder(@NonNull View itemView) {
            super(itemView);
            slNo = itemView.findViewById(R.id.serial_no_sub_item_ail);
            item_code = itemView.findViewById(R.id.item_code_sub_item_ail);
            part_no = itemView.findViewById(R.id.part_no_sub_item_ail);
            item_name = itemView.findViewById(R.id.item_name_sub_item_ail);
            item_unit = itemView.findViewById(R.id.item_unit_sub_item_ail);
            item_color = itemView.findViewById(R.id.item_color_sub_item_ail);
            item_size = itemView.findViewById(R.id.item_size_sub_item_ail);
            item_sales_price = itemView.findViewById(R.id.item_sales_price_sub_item_ail);
            item_stck_qty = itemView.findViewById(R.id.stock_qty_sub_item_ail);
            item_stck_val = itemView.findViewById(R.id.stock_value_sub_item_ail);


        }

    }

    @NonNull
    @Override
    public AISIHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.all_item_lists_item_view, parent, false);
        return new AISIHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AISIHolder holder, int position) {

        ItemAllLists categoryItem = mCategoryItem.get(position);

        holder.slNo.setText(categoryItem.getSlNo());
        holder.item_code.setText(categoryItem.getItemCode());
        holder.part_no.setText(categoryItem.getItemPartNumber());
        holder.item_name.setText(categoryItem.getItemName());
        holder.item_unit.setText(categoryItem.getItemUnit());
        holder.item_color.setText(categoryItem.getColorName());
        holder.item_size.setText(categoryItem.getSizeName());
        holder.item_sales_price.setText(categoryItem.getItemSellingPrice());
        holder.item_stck_qty.setText(categoryItem.getStock());
        holder.item_stck_val.setText(categoryItem.getStockVal());

    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public void filterList(ArrayList<ItemAllLists> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }
}

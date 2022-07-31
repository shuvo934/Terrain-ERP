package ttit.com.shuvo.icomerp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.arrayList.WareHouseQtyLists;

public class WarehouseItemQtyAdapter extends RecyclerView.Adapter<WarehouseItemQtyAdapter.WareHouseHolder> {

    private ArrayList<WareHouseQtyLists> mCategoryItem;
    private final Context myContext;


    public WarehouseItemQtyAdapter(ArrayList<WareHouseQtyLists> categoryItems, Context context) {
        this.mCategoryItem = categoryItems;

        this.myContext = context;
    }

    public class WareHouseHolder extends RecyclerView.ViewHolder  {
        public TextView warehouse;
        public TextView qty;
        public LinearLayout bottomLay;


        public WareHouseHolder(@NonNull View itemView) {
            super(itemView);
            warehouse = itemView.findViewById(R.id.warehouse_name_item_stock);
            qty = itemView.findViewById(R.id.warehouse_item_qty);
            bottomLay = itemView.findViewById(R.id.bottom_line_lay);

        }

    }

    @NonNull
    @Override
    public WareHouseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.warehouse_item_qty_view, parent, false);
        WareHouseHolder categoryViewHolder = new WareHouseHolder(view);
        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WareHouseHolder holder, int position) {

        WareHouseQtyLists categoryItem = mCategoryItem.get(position);

        holder.warehouse.setText(categoryItem.getWarehouse());
        holder.qty.setText(categoryItem.getQty());
        int serial = Integer.parseInt(categoryItem.getSerial_no());
        if (serial == mCategoryItem.size()) {
            holder.bottomLay.setVisibility(View.INVISIBLE);
        } else {
            holder.bottomLay.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public void filterList(ArrayList<WareHouseQtyLists> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }
}

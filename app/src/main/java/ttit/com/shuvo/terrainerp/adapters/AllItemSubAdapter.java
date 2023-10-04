package ttit.com.shuvo.terrainerp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.arrayList.ItemAllLists;
import ttit.com.shuvo.terrainerp.arrayList.ItemAllSubLists;

public class AllItemSubAdapter extends RecyclerView.Adapter<AllItemSubAdapter.AISSHolder> {

    private ArrayList<ItemAllSubLists> mCategoryItem;
    private final Context myContext;
    public AllItemSubItemAdapter allItemSubItemAdapter;

    public AllItemSubAdapter(Context myContext, ArrayList<ItemAllSubLists> mCategoryItem) {
        this.myContext = myContext;
        this.mCategoryItem = mCategoryItem;
    }

    public class AISSHolder extends RecyclerView.ViewHolder {


        public TextView subCatName;
        public TextView totalQty;
        public TextView totalVal;

        public RecyclerView itemDetails;

        public AISSHolder(@NonNull View itemView) {
            super(itemView);

            subCatName = itemView.findViewById(R.id.sub_category_name_ail);
            totalQty = itemView.findViewById(R.id.total_stock_qty_sub_ail);
            totalVal = itemView.findViewById(R.id.total_stock_value_sub_ail);

            itemDetails = itemView.findViewById(R.id.all_item_list_item_report_view);


        }


    }

    @NonNull
    @Override
    public AISSHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.all_item_lists_sub_category_view, parent, false);
        return new AISSHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AISSHolder holder, int position) {

        ItemAllSubLists categoryItem = mCategoryItem.get(position);


        holder.subCatName.setText(categoryItem.getSubName());
        holder.totalQty.setText(categoryItem.getTotalQty());
        holder.totalVal.setText(categoryItem.getTotalVal());

        ArrayList<ItemAllLists> voucherLists2s = categoryItem.getAllLists();

        holder.itemDetails.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(myContext);
        holder.itemDetails.setLayoutManager(layoutManager);
        allItemSubItemAdapter = new AllItemSubItemAdapter(voucherLists2s,myContext);
        holder.itemDetails.setAdapter(allItemSubItemAdapter);



    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public void filterList(ArrayList<ItemAllSubLists> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }

}

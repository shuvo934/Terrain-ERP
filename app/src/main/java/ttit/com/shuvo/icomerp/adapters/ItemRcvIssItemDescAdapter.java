package ttit.com.shuvo.icomerp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.arrayList.ItemRcvIssItemDescList;
import ttit.com.shuvo.icomerp.arrayList.ItemRcvIssWarehouseList;

public class ItemRcvIssItemDescAdapter extends RecyclerView.Adapter<ItemRcvIssItemDescAdapter.ITRIIDHolder> {

    private ArrayList<ItemRcvIssItemDescList> mCategoryItem;
    private final Context myContext;

    public ItemRcvIssWHAdapter itemRcvIssWHAdapter;

    int selectedPosition = -1;

    public ItemRcvIssItemDescAdapter(ArrayList<ItemRcvIssItemDescList> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    public class ITRIIDHolder extends RecyclerView.ViewHolder {

        public TextView itemName;
        public TextView itemUnit;

        public RecyclerView itemDetails;


        public ITRIIDHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name_irir);
            itemUnit = itemView.findViewById(R.id.unit_irir);

            itemDetails = itemView.findViewById(R.id.warehouse_desc_irir);



        }

    }

    @NonNull
    @Override
    public ITRIIDHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.item_rcv_iss_item_des_view, parent, false);
        return new ITRIIDHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ITRIIDHolder holder, int position) {

        ItemRcvIssItemDescList categoryItem = mCategoryItem.get(position);


        holder.itemName.setText(categoryItem.getItemName());
        holder.itemUnit.setText(categoryItem.getUnit());


        ArrayList<ItemRcvIssWarehouseList> itemRcvIssWarehouseLists = categoryItem.getItemRcvIssWarehouseLists();

        holder.itemDetails.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(myContext);
        holder.itemDetails.setLayoutManager(layoutManager);
        itemRcvIssWHAdapter = new ItemRcvIssWHAdapter(itemRcvIssWarehouseLists,myContext);
        holder.itemDetails.setAdapter(itemRcvIssWHAdapter);

//        if(selectedPosition == position) {
//            holder.itemView.setBackgroundColor(Color.parseColor("#81ecec"));
//        }
//        else {
//            holder.itemView.setBackgroundColor(Color.parseColor("#f5f6fa"));
//        }
//
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (selectedPosition == position) {
//                    selectedPosition = -1;
//                } else {
//                    selectedPosition=position;
//                }
//
//                notifyDataSetChanged();
//
//            }
//        });



    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }
}

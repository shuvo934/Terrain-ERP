package ttit.com.shuvo.terrainerp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.arrayList.ItemRcvIssWarehouseList;

public class ItemRcvIssWHAdapter extends RecyclerView.Adapter<ItemRcvIssWHAdapter.ITRIWHHolder> {

    private ArrayList<ItemRcvIssWarehouseList> mCategoryItem;
    private final Context myContext;

    int selectedPosition = -1;


    public ItemRcvIssWHAdapter(ArrayList<ItemRcvIssWarehouseList> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    public class ITRIWHHolder extends RecyclerView.ViewHolder {

        public TextView whName;
        public TextView whRack;
        public TextView whRcvQty;
        public TextView rcvRate;
        public TextView rcvAmnt;
        public TextView whIssQty;
        public TextView issRate;
        public TextView issAmnt;



        public ITRIWHHolder(@NonNull View itemView) {
            super(itemView);
            whName = itemView.findViewById(R.id.warehouse_name_irir);
            whRack = itemView.findViewById(R.id.warehouse_rack_irir);
            whRcvQty = itemView.findViewById(R.id.warehouse_rcv_qty_irir);
            rcvRate = itemView.findViewById(R.id.rcv_rate_irir);
            rcvAmnt = itemView.findViewById(R.id.rcv_amnt_irir);
            whIssQty = itemView.findViewById(R.id.warehouse_issue_qty_irir);
            issRate = itemView.findViewById(R.id.issue_rate_irir);
            issAmnt = itemView.findViewById(R.id.issue_amnt_irir);




        }

    }

    @NonNull
    @Override
    public ITRIWHHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.item_rcv_iss_warehouse_view, parent, false);
        return new ITRIWHHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ITRIWHHolder holder, int position) {

        ItemRcvIssWarehouseList categoryItem = mCategoryItem.get(position);

        holder.whName.setText(categoryItem.getWhName());
        holder.whRack.setText(categoryItem.getWhRack());
        holder.whRcvQty.setText(categoryItem.getWhRcvQty());
        holder.rcvRate.setText(categoryItem.getWhRcvRate());
        holder.rcvAmnt.setText(categoryItem.getWhRcvAmnt());
        holder.whIssQty.setText(categoryItem.getWhIssQty());
        holder.issRate.setText(categoryItem.getWhIssRate());
        holder.issAmnt.setText(categoryItem.getWhIssAmnt());

        if(selectedPosition == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#81ecec"));
        }
        else {
            holder.itemView.setBackgroundColor(Color.parseColor("#f5f6fa"));
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition == position) {
                    selectedPosition = -1;
                } else {
                    selectedPosition=position;
                }

                notifyDataSetChanged();

            }
        });


    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }
}

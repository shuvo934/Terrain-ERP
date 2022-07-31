package ttit.com.shuvo.icomerp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.arrayList.DCColorWiseItemList;
import ttit.com.shuvo.icomerp.arrayList.DeliveryChallanDetailsList;

public class DCItemDetailsAdapter extends RecyclerView.Adapter<DCItemDetailsAdapter.DCIDHolder> {

    private ArrayList<DeliveryChallanDetailsList> mCategoryItem;
    private final Context myContext;
    public DCColorWiseItemAdapter dcColorWiseItemAdapter;

    public DCItemDetailsAdapter(ArrayList<DeliveryChallanDetailsList> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public DCIDHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.delivery_challan_item_details_view, parent, false);
        return new DCIDHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DCIDHolder holder, int position) {

        DeliveryChallanDetailsList dcdl = mCategoryItem.get(position);

        holder.itemName.setText(dcdl.getItem_name());
        holder.partNo.setText(dcdl.getPart_no());
        holder.qty.setText(dcdl.getItem_qty());
        holder.amnt.setText(dcdl.getItem_amnt());

        ArrayList<DCColorWiseItemList> dcColorWiseItemLists = dcdl.getDcColorWiseItemLists();
        holder.itemDetails.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(myContext);
        holder.itemDetails.setLayoutManager(layoutManager);
        dcColorWiseItemAdapter = new DCColorWiseItemAdapter(dcColorWiseItemLists,myContext);
        holder.itemDetails.setAdapter(dcColorWiseItemAdapter);

    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public static class DCIDHolder extends RecyclerView.ViewHolder {


        public TextView itemName;
        public TextView partNo;
        public TextView qty;
        public TextView amnt;

        public RecyclerView itemDetails;


        public DCIDHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name_dc);
            partNo = itemView.findViewById(R.id.part_number_dc);
            qty = itemView.findViewById(R.id.qty_dc);
            amnt = itemView.findViewById(R.id.amnt_dc);

            itemDetails = itemView.findViewById(R.id.delivery_challan_item_wise_report_view);

        }

    }
}

package ttit.com.shuvo.terrainerp.adapters;

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

import ttit.com.shuvo.terrainerp.R;

import ttit.com.shuvo.terrainerp.arrayList.ItemRcvIssItemDescList;
import ttit.com.shuvo.terrainerp.arrayList.ItemRcvIssReglist;

public class ItemRcvIssRegAdapter extends RecyclerView.Adapter<ItemRcvIssRegAdapter.ITTIRholder> {

    private ArrayList<ItemRcvIssReglist> mCategoryItem;
    private final ClickedItem myClickedItem;
    private final Context myContext;

    public ItemRcvIssItemDescAdapter itemRcvIssItemDescAdapter;

    int selectedPosition = -1;

    public ItemRcvIssRegAdapter(ArrayList<ItemRcvIssReglist> categoryItems, Context context, ClickedItem cli) {
        this.mCategoryItem = categoryItems;
        this.myClickedItem = cli;
        this.myContext = context;
    }

    public class ITTIRholder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView sldate;
        public TextView rcvNo;
        public TextView issueNo;
        public TextView transFlag;
        public TextView transSrc;
        public TextView supplierName;
        public TextView customerName;

        public RecyclerView itemDetails;


        ClickedItem mClickedItem;

        public ITTIRholder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            sldate = itemView.findViewById(R.id.sl_date_irir);
            rcvNo = itemView.findViewById(R.id.rcv_no_irir);
            issueNo = itemView.findViewById(R.id.issue_no_irir);
            transFlag = itemView.findViewById(R.id.trans_flag_irir);
            transSrc = itemView.findViewById(R.id.trans_src_irir);
            supplierName = itemView.findViewById(R.id.supplier_irir);
            customerName = itemView.findViewById(R.id.customer_irir);

            itemDetails = itemView.findViewById(R.id.item_description_irir);

            this.mClickedItem = ci;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mClickedItem.onCategoryClicked(getAdapterPosition());
            Log.i("Category Name", mCategoryItem.get(getAdapterPosition()).getSupplier());
        }
    }

    public interface ClickedItem {
        void onCategoryClicked(int CategoryPosition);
    }

    @NonNull
    @Override
    public ITTIRholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.item_rcv_iss_reg_view, parent, false);
        return new ITTIRholder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ITTIRholder holder, int position) {

        ItemRcvIssReglist categoryItem = mCategoryItem.get(position);

        if (categoryItem.getSlDate() != null) {
            holder.sldate.setText(categoryItem.getSlDate());
        }

        holder.rcvNo.setText(categoryItem.getRcvNo());
        holder.issueNo.setText(categoryItem.getIssNo());
        holder.transFlag.setText(categoryItem.getTransFlag());
        holder.transSrc.setText(categoryItem.getTransSrc());
        holder.supplierName.setText(categoryItem.getSupplier());
        holder.customerName.setText(categoryItem.getCustomer());

        ArrayList<ItemRcvIssItemDescList> itemRcvIssItemDescLists = categoryItem.getItemRcvIssItemDescLists();

        holder.itemDetails.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(myContext);
        holder.itemDetails.setLayoutManager(layoutManager);
        itemRcvIssItemDescAdapter = new ItemRcvIssItemDescAdapter(itemRcvIssItemDescLists,myContext);
        holder.itemDetails.setAdapter(itemRcvIssItemDescAdapter);

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

    public void filterList(ArrayList<ItemRcvIssReglist> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }

}

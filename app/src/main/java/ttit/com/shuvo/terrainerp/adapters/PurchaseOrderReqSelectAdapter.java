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
import ttit.com.shuvo.terrainerp.arrayList.PurchaseOrderRequisitionLists;

public class PurchaseOrderReqSelectAdapter extends RecyclerView.Adapter<PurchaseOrderReqSelectAdapter.PORSHolder>{

    private ArrayList<PurchaseOrderRequisitionLists> mCategoryItem;
    private final Context myContext;
    private final ClickedItem myClickedItem;

    public PurchaseOrderReqSelectAdapter(ArrayList<PurchaseOrderRequisitionLists> mCategoryItem, Context myContext, ClickedItem myClickedItem) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
        this.myClickedItem = myClickedItem;
    }

    @NonNull
    @Override
    public PORSHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.po_pr_details_view, parent, false);
        return new PORSHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull PORSHolder holder, int position) {
        PurchaseOrderRequisitionLists categoryItem = mCategoryItem.get(position);

        holder.reqNo.setText(categoryItem.getPrm_req_no());
        holder.reqDate.setText(categoryItem.getPr_date());
        holder.catName.setText(categoryItem.getIm_name());
        holder.reqQty.setText(categoryItem.getReq_qty());
        holder.reqAppQty.setText(categoryItem.getApp_qty());
        holder.reqBalQty.setText(categoryItem.getApp_balance());
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public class PORSHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView reqNo;
        public TextView reqDate;
        public TextView catName;
        public TextView reqQty;
        public TextView reqAppQty;
        public TextView reqBalQty;
        ClickedItem mClickedItem;

        public PORSHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            reqNo = itemView.findViewById(R.id.requisition_no_for_pr_select_po);
            reqDate = itemView.findViewById(R.id.requisition_date_for_pr_select_po);
            catName = itemView.findViewById(R.id.category_for_pr_select_po);
            reqQty = itemView.findViewById(R.id.requisition_qty_for_pr_select_po);
            reqAppQty = itemView.findViewById(R.id.requisition_approved_qty_for_pr_select_po);
            reqBalQty = itemView.findViewById(R.id.requisition_balance_qty_for_pr_select_po);
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
        void onItemClicked(int Position);
    }

    public void filterList(ArrayList<PurchaseOrderRequisitionLists> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }
}

package ttit.com.shuvo.icomerp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.arrayList.StoreLedgerList;

public class StoreLedgerAdapter extends RecyclerView.Adapter<StoreLedgerAdapter.StoreLedgerHolder> {

    private ArrayList<StoreLedgerList> mCategoryItem;
    private final ClickedItem myClickedItem;
    private final Context myContext;
    public WarehouseItemQtyAdapter warehouseItemQtyAdapter;

    int selectedPosition = -1;

    public StoreLedgerAdapter(ArrayList<StoreLedgerList> categoryItems, Context context, ClickedItem cli) {
        this.mCategoryItem = categoryItems;
        this.myClickedItem = cli;
        this.myContext = context;
    }

    public class StoreLedgerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView sl_no;
        private TextView itemName;
        private TextView itemCode;
        private TextView hsCode;
        private TextView unit;
        private TextView opQty;
        private TextView opVal;
        private TextView rcvQty;
        private TextView rcvVal;
        private TextView salesRtnQty;
        private TextView salesRtnVal;
        private TextView proUpdQty;
        private TextView proUpdVal;
        private TextView issueRtnQty;
        private TextView issueRtnVal;
        private TextView tranRcvQty;
        private TextView tranRcvVal;
        private TextView genIssRtnQty;
        private TextView genIssRtnVal;
        private TextView salesQty;
        private TextView salesVal;
        private TextView purRtnQty;
        private TextView purRtnVal;
        private TextView storAdjQty;
        private TextView storAdjVal;
        private TextView rawIssueQty;
        private TextView rawissueVal;
        private TextView genIssueItemQty;
        private TextView genIssueItemVal;
        private TextView genAssetQty;
        private TextView genAssetVal;
        private TextView transIssueQty;
        private TextView transIssueVal;
        private TextView closingQty;
        private TextView closingVal;

        ClickedItem mClickedItem;

        public StoreLedgerHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            sl_no = itemView.findViewById(R.id.sl_no_store_ledger);
            itemCode = itemView.findViewById(R.id.item_code_store_ledger);
            itemName = itemView.findViewById(R.id.item_name_store_ledger);
            unit = itemView.findViewById(R.id.unit_store_ledger);
            hsCode = itemView.findViewById(R.id.hs_code_store_ledger);
            opQty = itemView.findViewById(R.id.opening_qty);
            opVal = itemView.findViewById(R.id.opening_val);
            rcvQty = itemView.findViewById(R.id.rcv_qty);
            rcvVal = itemView.findViewById(R.id.rcv_val);
            salesRtnQty = itemView.findViewById(R.id.sales_rtn_qty);
            salesRtnVal = itemView.findViewById(R.id.sales_rtn_val);
            proUpdQty = itemView.findViewById(R.id.pro_upd_qty);
            proUpdVal = itemView.findViewById(R.id.pro_upd_val);
            issueRtnQty = itemView.findViewById(R.id.issue_rtn_qty);
            issueRtnVal = itemView.findViewById(R.id.issue_rtn_val);
            tranRcvQty = itemView.findViewById(R.id.transfer_rcv_qty);
            tranRcvVal = itemView.findViewById(R.id.transfer_rcv_val);
            genIssRtnQty = itemView.findViewById(R.id.gen_iss_rtn_qty);
            genIssRtnVal = itemView.findViewById(R.id.gen_iss_rtn_val);
            salesQty = itemView.findViewById(R.id.sales_qty);
            salesVal = itemView.findViewById(R.id.sales_val);
            purRtnQty = itemView.findViewById(R.id.pur_rtn_qty);
            purRtnVal = itemView.findViewById(R.id.pur_rtn_val);
            storAdjQty = itemView.findViewById(R.id.store_adj_qty);
            storAdjVal = itemView.findViewById(R.id.store_adj_val);
            rawIssueQty = itemView.findViewById(R.id.raw_issue_qty);
            rawissueVal = itemView.findViewById(R.id.raw_issue_val);
            genIssueItemQty = itemView.findViewById(R.id.gen_item_issue_qty);
            genIssueItemVal = itemView.findViewById(R.id.gen_item_issue_val);
            genAssetQty = itemView.findViewById(R.id.gen_asset_qty);
            genAssetVal = itemView.findViewById(R.id.gen_asset_val);
            transIssueQty = itemView.findViewById(R.id.transfer_issue_qty);
            transIssueVal = itemView.findViewById(R.id.transfer_issue_val);
            closingQty = itemView.findViewById(R.id.closing_qty);
            closingVal = itemView.findViewById(R.id.closing_val);


            this.mClickedItem = ci;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mClickedItem.onCategoryClicked(getAdapterPosition());
            Log.i("Category Name", mCategoryItem.get(getAdapterPosition()).getItem_name_2());
        }
    }

    public interface ClickedItem {
        void onCategoryClicked(int CategoryPosition);
    }

    @NonNull
    @Override
    public StoreLedgerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.store_ledger_item_view, parent, false);
        return new StoreLedgerHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreLedgerHolder holder, int position) {

        StoreLedgerList categoryItem = mCategoryItem.get(position);


        holder.sl_no.setText(categoryItem.getSl_no());
        holder.itemCode.setText(categoryItem.getItem_code_1());
        holder.itemName.setText(categoryItem.getItem_name_2());
        holder.unit.setText(categoryItem.getUnit_7());
        holder.hsCode.setText(categoryItem.getHs_code_5());
        holder.opQty.setText(categoryItem.getOp_qty_9());
        holder.opVal.setText(categoryItem.getOp_val_8());
        holder.rcvQty.setText(categoryItem.getRcv_qty_10());
        holder.rcvVal.setText(categoryItem.getRcv_val_11());
        holder.salesRtnQty.setText(categoryItem.getSales_rtn_qty_12());
        holder.salesRtnVal.setText(categoryItem.getSales_rtn_val_13());
        holder.proUpdQty.setText(categoryItem.getPro_update_qty_14());
        holder.proUpdVal.setText(categoryItem.getPro_update_val_15());
        holder.issueRtnQty.setText(categoryItem.getIssue_rtn_qty_16());
        holder.issueRtnVal.setText(categoryItem.getIssue_rtn_val_17());
        holder.tranRcvQty.setText(categoryItem.getTransfer_rcv_qty_18());
        holder.tranRcvVal.setText(categoryItem.getTransfer_rcv_val_19());
        holder.genIssRtnQty.setText(categoryItem.getGen_issue_rtn_qty_20());
        holder.genIssRtnVal.setText(categoryItem.getGen_issue_rtn_val_21());
        holder.salesQty.setText(categoryItem.getSales_qty_22());
        holder.salesVal.setText(categoryItem.getSales_val_23());
        holder.purRtnQty.setText(categoryItem.getPur_rtn_qty_26());
        holder.purRtnVal.setText(categoryItem.getPur_rtn_val_27());
        holder.storAdjQty.setText(categoryItem.getStore_adj_qty_28());
        holder.storAdjVal.setText(categoryItem.getStore_adj_val_29());
        holder.rawIssueQty.setText(categoryItem.getRaw_issue_qty_30());
        holder.rawissueVal.setText(categoryItem.getRaw_issue_val_31());
        holder.genIssueItemQty.setText(categoryItem.getGen_item_issue_qty_32());
        holder.genIssueItemVal.setText(categoryItem.getGen_item_issue_val_33());
        holder.genAssetQty.setText(categoryItem.getGen_asset_qty_34());
        holder.genAssetVal.setText(categoryItem.getGen_asset_val_35());
        holder.transIssueQty.setText(categoryItem.getTransfer_issue_r_qty_36());
        holder.transIssueVal.setText(categoryItem.getTransfer_issue_r_val_37());
        holder.closingQty.setText(categoryItem.getClosing_qty_38());
        holder.closingVal.setText(categoryItem.getClosing_val_39());




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

    public void filterList(ArrayList<StoreLedgerList> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }
}

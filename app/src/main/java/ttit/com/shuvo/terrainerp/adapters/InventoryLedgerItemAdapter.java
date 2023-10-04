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
import ttit.com.shuvo.terrainerp.arrayList.InventoryLedgerItemList;

public class InventoryLedgerItemAdapter extends RecyclerView.Adapter<InventoryLedgerItemAdapter.InventoryItemHolder> {

    private ArrayList<InventoryLedgerItemList> mCategoryItem;
    private final Context myContext;

    public InventoryLedgerItemAdapter(ArrayList<InventoryLedgerItemList> categoryItems, Context context) {
        this.mCategoryItem = categoryItems;
        this.myContext = context;
    }

    public class InventoryItemHolder extends RecyclerView.ViewHolder  {
        public TextView slDate;
        public TextView rcvNo;
        public TextView issueNo;
        public TextView transFlag;
        public TextView transSrc;
        public TextView supplier;
        public TextView rcvQty;
        public TextView issueQty;
        public TextView balance;


        public InventoryItemHolder(@NonNull View itemView) {
            super(itemView);
            slDate = itemView.findViewById(R.id.sl_date_inventory_ledger);
            rcvNo = itemView.findViewById(R.id.rcv_no_inventory_ledger);
            issueNo = itemView.findViewById(R.id.issue_no_inventory_ledger);
            transFlag = itemView.findViewById(R.id.trans_flag_inventory_ledger);
            transSrc = itemView.findViewById(R.id.trans_src_inventory_ledger);
            supplier = itemView.findViewById(R.id.supplier_inventory_ledger);
            rcvQty = itemView.findViewById(R.id.rcv_qty_inventory_ledger);
            issueQty = itemView.findViewById(R.id.issue_qty_inventory_ledger);
            balance = itemView.findViewById(R.id.balance_inventory_ledger);

        }

    }

    @NonNull
    @Override
    public InventoryItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.inventory_ledger_item_details_view, parent, false);
        InventoryItemHolder categoryViewHolder = new InventoryItemHolder(view);
        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryItemHolder holder, int position) {

        InventoryLedgerItemList categoryItem = mCategoryItem.get(position);

        holder.slDate.setText(categoryItem.getSl_date());
        holder.rcvNo.setText(categoryItem.getRcv_no());
        holder.issueNo.setText(categoryItem.getIssue_no());
        holder.transFlag.setText(categoryItem.getTrans_flag());
        holder.transSrc.setText(categoryItem.getTrans_src());
        holder.supplier.setText(categoryItem.getSupplier());
        holder.rcvQty.setText(categoryItem.getRcv_qty());
        holder.issueQty.setText(categoryItem.getIssue_qty());
        holder.balance.setText(categoryItem.getBalance());

    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

}

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
import ttit.com.shuvo.icomerp.arrayList.InventoryLedgerItemList;
import ttit.com.shuvo.icomerp.arrayList.InventoryLedgerList;
import ttit.com.shuvo.icomerp.arrayList.ItemWiseStockLists;
import ttit.com.shuvo.icomerp.arrayList.WareHouseQtyLists;

public class InventoryLedgerAdapter extends RecyclerView.Adapter<InventoryLedgerAdapter.InventoryHolder> {

    private ArrayList<InventoryLedgerList> mCategoryItem;
    private final ClickedItem myClickedItem;
    private final Context myContext;

    public InventoryLedgerItemAdapter inventoryLedgerItemAdapter;

    int selectedPosition = -1;

    public InventoryLedgerAdapter(ArrayList<InventoryLedgerList> categoryItems, Context context, ClickedItem cli) {
        this.mCategoryItem = categoryItems;
        this.myClickedItem = cli;
        this.myContext = context;
    }

    public class InventoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView cateName;
        public TextView subCatName;
        public TextView itemCode;
        public TextView itemName;
        public TextView unit;
        public TextView hsCode;
        public TextView opening_sl_date;
        public TextView opening_balance;
        public TextView totalRcvQty;
        public TextView totalIssueQty;
        public TextView totalBalance;
        public RecyclerView itemDetails;


        ClickedItem mClickedItem;

        public InventoryHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            opening_sl_date = itemView.findViewById(R.id.opening_date_inventory_ledger);
            cateName = itemView.findViewById(R.id.cate_name_inventory_ledger);
            subCatName = itemView.findViewById(R.id.sub_cat_name_inventory_ledger);
            itemCode = itemView.findViewById(R.id.item_code_inventory_ledger);
            itemName = itemView.findViewById(R.id.item_name_inventory_ledger);
            unit = itemView.findViewById(R.id.unit_inventory_ledger);
            opening_balance = itemView.findViewById(R.id.opening_qty_inventory_ledger);
            totalRcvQty = itemView.findViewById(R.id.total_rcv_qty_inventory_ledger);
            totalIssueQty = itemView.findViewById(R.id.total_issue_qty_inventory_ledger);
            totalBalance = itemView.findViewById(R.id.total_balance_inventory_ledger);
            hsCode = itemView.findViewById(R.id.hs_code_inventory_ledger);
            itemDetails = itemView.findViewById(R.id.item_overview_relation_inventory_ledger);

            this.mClickedItem = ci;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mClickedItem.onCategoryClicked(getAdapterPosition());
            Log.i("Category Name", mCategoryItem.get(getAdapterPosition()).getItem_name());
        }
    }

    public interface ClickedItem {
        void onCategoryClicked(int CategoryPosition);
    }

    @NonNull
    @Override
    public InventoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.inventory_ledger_item_view, parent, false);
        return new InventoryHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryHolder holder, int position) {

        InventoryLedgerList categoryItem = mCategoryItem.get(position);

        if (categoryItem.getOpening_date() != null) {
            holder.opening_sl_date.setText(categoryItem.getOpening_date());
        }

        holder.itemCode.setText(categoryItem.getItem_code());
        holder.itemName.setText(categoryItem.getItem_name());
        holder.unit.setText(categoryItem.getUnit());
        holder.opening_balance.setText(categoryItem.getOpening_balance());
        holder.totalRcvQty.setText(categoryItem.getTotal_rcv_qty());
        holder.totalIssueQty.setText(categoryItem.getTotal_issue_qty());
        holder.hsCode.setText(categoryItem.getHs_code());
        holder.cateName.setText(categoryItem.getCat_name());
        holder.subCatName.setText(categoryItem.getSubCat_name());
        holder.totalBalance.setText(categoryItem.getTotal_balance());
        ArrayList<InventoryLedgerItemList> inventoryLedgerItemLists = categoryItem.getInventoryLedgerItemLists();

        holder.itemDetails.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(myContext);
        holder.itemDetails.setLayoutManager(layoutManager);
        inventoryLedgerItemAdapter = new InventoryLedgerItemAdapter(inventoryLedgerItemLists,myContext);
        holder.itemDetails.setAdapter(inventoryLedgerItemAdapter);

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

    public void filterList(ArrayList<InventoryLedgerList> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }
}

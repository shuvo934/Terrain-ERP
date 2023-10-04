package ttit.com.shuvo.terrainerp.adapters;

import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.selectedItemPosition;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.arrayList.PurchaseRcvItemDetailsList;
import ttit.com.shuvo.terrainerp.dialogues.PRcvSingleItemDetailsDial;

public class PurchaseRcvItemDetailsAdapter extends RecyclerView.Adapter<PurchaseRcvItemDetailsAdapter.PRIDHolder> {
    private ArrayList<PurchaseRcvItemDetailsList> mCategoryItem;
    private final Context myContext;
    private final ClickedItem myClickedItem;

    public PurchaseRcvItemDetailsAdapter(ArrayList<PurchaseRcvItemDetailsList> mCategoryItem, Context myContext, ClickedItem myClickedItem) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
        this.myClickedItem = myClickedItem;
    }

    @NonNull
    @Override
    public PRIDHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.purchase_rcv_item_details_view, parent, false);
        return new PRIDHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull PRIDHolder holder, int position) {
        PurchaseRcvItemDetailsList list = mCategoryItem.get(position);

        holder.itemName.setText(list.getItem());
        holder.itemUnit.setText(list.getItem_unit());
        holder.poQty.setText(list.getWod_qty());
        holder.rcvQty.setText(list.getRcv_qty());
        holder.balanceQty.setText(list.getAfter_rcv_balance_qty());
        holder.unitPrice.setText(list.getWod_rate());
        holder.itemAmount.setText(list.getItem_wise_rcv_total_amnt());
        holder.vatPercentage.setText(list.getWod_vat_pct());
        holder.itemVat.setText(list.getWod_vat_pct_amt());
        holder.vatAmount.setText(list.getItem_wise_rcv_total_vat());

        if(selectedItemPosition == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#81ecec"));
        }
        else {
            holder.itemView.setBackgroundColor(Color.parseColor("#f5f6fa"));
        }
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public class PRIDHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView itemName;
        public TextView itemUnit;
        public TextView poQty;
        public TextView rcvQty;
        public TextView balanceQty;
        public TextView unitPrice;
        public TextView itemAmount;
        public TextView vatPercentage;
        public TextView itemVat;
        public TextView vatAmount;

        ClickedItem mClickedItem;

        public PRIDHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name_selected_item_for_p_rcv);
            itemUnit = itemView.findViewById(R.id.item_unit_selected_item_for_p_rcv);
            poQty = itemView.findViewById(R.id.po_qty_selected_item_for_p_rcv);
            rcvQty = itemView.findViewById(R.id.rcv_qty_selected_item_for_p_rcv);
            balanceQty = itemView.findViewById(R.id.balance_qty_selected_item_for_p_rcv);
            unitPrice = itemView.findViewById(R.id.unit_price_selected_item_for_p_rcv);
            itemAmount = itemView.findViewById(R.id.amount_selected_item_for_p_rcv);
            vatPercentage = itemView.findViewById(R.id.vat_percentage_selected_item_for_p_rcv);
            itemVat = itemView.findViewById(R.id.vat_selected_item_for_p_rcv);
            vatAmount = itemView.findViewById(R.id.vat_amount_selected_item_for_p_rcv);
            this.mClickedItem = ci;

            itemView.setOnClickListener(this);

            itemName.setOnClickListener(v -> {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                String item_name = mCategoryItem.get(getAdapterPosition()).getItem();
                String item_code = mCategoryItem.get(getAdapterPosition()).getItem_code();
                String hs_code = mCategoryItem.get(getAdapterPosition()).getItem_hs_code();
                String part_number = mCategoryItem.get(getAdapterPosition()).getItem_part_number();
                String item_unit = mCategoryItem.get(getAdapterPosition()).getItem_unit();
                String size_name = mCategoryItem.get(getAdapterPosition()).getSize_name();
                String color_name = mCategoryItem.get(getAdapterPosition()).getColor_name();
                String actual_rate = mCategoryItem.get(getAdapterPosition()).getWod_actual_rate();

                PRcvSingleItemDetailsDial pRcvSingleItemDetailsDial = new PRcvSingleItemDetailsDial(item_name,item_code,hs_code,part_number,
                        item_unit,size_name,color_name,actual_rate,myContext);
                pRcvSingleItemDetailsDial.show(activity.getSupportFragmentManager(),"PRCVSIS");
            });

        }

        @Override
        public void onClick(View v) {
            mClickedItem.onItemClicked(getAdapterPosition());
        }
    }

    public interface ClickedItem {
        void onItemClicked(int Position);
    }
}

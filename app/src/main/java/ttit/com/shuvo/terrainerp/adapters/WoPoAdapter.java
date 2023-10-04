package ttit.com.shuvo.terrainerp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.arrayList.WoPoLists;

public class WoPoAdapter extends RecyclerView.Adapter<WoPoAdapter.WoPoHolder> {

    private ArrayList<WoPoLists> mCategoryItem;
    private final ClickedItem myClickedItem;
    private final Context myContext;


    public WoPoAdapter(ArrayList<WoPoLists> categoryItems, Context context, ClickedItem cli) {
        this.mCategoryItem = categoryItems;
        this.myClickedItem = cli;
        this.myContext = context;
    }

    public class WoPoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView ftext;
        public TextView stext;
        public TextView ttext;
        public TextView fotext;
        public TextView vatAmnt;
        public TextView totalBill;
        public TextView billPaid;
        public TextView billPayable;

        ClickedItem mClickedItem;

        public WoPoHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            ftext = itemView.findViewById(R.id.wo_po_no_from_list);
            stext = itemView.findViewById(R.id.wo_po_amnt_from_list);
            ttext = itemView.findViewById(R.id.receive_amnt_from_list);
            fotext = itemView.findViewById(R.id.bill_rcv_amnt_from_list);
            vatAmnt = itemView.findViewById(R.id.vat_amnt_from_list);
            totalBill = itemView.findViewById(R.id.total_bill_amnt_from_list);
            billPaid = itemView.findViewById(R.id.bill_paid_amnt_from_list);
            billPayable = itemView.findViewById(R.id.payable_amnt_from_list);


            this.mClickedItem = ci;

            itemView.setOnClickListener(this::onClick);

        }

        @Override
        public void onClick(View v) {
            mClickedItem.onWoPoClicked(getAdapterPosition());
            Log.i("Category Name", mCategoryItem.get(getAdapterPosition()).getWo_amnt());
        }
    }

    public interface ClickedItem {
        void onWoPoClicked(int CategoryPosition);
    }

    @NonNull
    @Override
    public WoPoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.wopo_wise_bill_details, parent, false);
        WoPoHolder categoryViewHolder = new WoPoHolder(view, myClickedItem);
        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WoPoHolder holder, int position) {

        WoPoLists categoryItem = mCategoryItem.get(position);

        String bdt = " BDT";

        holder.ftext.setText(categoryItem.getWo_po_no());
        holder.stext.setText(categoryItem.getWo_amnt()+bdt);
        holder.ttext.setText(categoryItem.getRcv_amnt()+bdt);
        holder.fotext.setText(categoryItem.getBill_amnt()+bdt);
        holder.vatAmnt.setText(categoryItem.getVat_amnt()+ bdt);
        holder.totalBill.setText(categoryItem.getTotal_bill()+bdt);
        holder.billPaid.setText(categoryItem.getBill_paid()+bdt);
        holder.billPayable.setText(categoryItem.getBill_payable()+bdt);

    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public void filterList(ArrayList<WoPoLists> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }
}

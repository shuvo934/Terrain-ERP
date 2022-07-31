package ttit.com.shuvo.icomerp.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.arrayList.PartySummaryLedgerLists;

public class PartSummaryLedAdapter extends RecyclerView.Adapter<PartSummaryLedAdapter.PSLHolder> {

    private ArrayList<PartySummaryLedgerLists> mCategoryItem;
    private final Context myContext;
    private final ClickedItem myClickedItem;

    public PartSummaryLedAdapter(ArrayList<PartySummaryLedgerLists> categoryItems, Context context, ClickedItem myClickedItem) {
        this.mCategoryItem = categoryItems;
        this.myContext = context;
        this.myClickedItem = myClickedItem;
    }

    public class PSLHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView accountNo;
        public TextView partyName;
        public TextView bfPsl;
        public TextView debitPsl;
        public TextView creditPsl;
        public TextView dueBalancePsl;
        ClickedItem mClickedItem;

        public PSLHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            accountNo = itemView.findViewById(R.id.account_no_psl);
            partyName = itemView.findViewById(R.id.party_name_psl);
            bfPsl = itemView.findViewById(R.id.bf_psl);
            debitPsl = itemView.findViewById(R.id.debit_psl);
            creditPsl = itemView.findViewById(R.id.credit_psl);
            dueBalancePsl = itemView.findViewById(R.id.due_balance_psl);

            this.mClickedItem = ci;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mClickedItem.onCategoryClicked(getAdapterPosition());
            //Log.i("Client Name", mCategoryItem.get(getAdapterPosition()).getvDate());
        }

    }
    public interface ClickedItem {
        void onCategoryClicked(int CategoryPosition);
    }

    @NonNull
    @Override
    public PSLHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.party_summary_ledger_item_view, parent, false);
        return new PSLHolder(view,myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull PSLHolder holder, int position) {

        PartySummaryLedgerLists categoryItem = mCategoryItem.get(position);

        //positionFromFirstAdapter = position;


        holder.accountNo.setText(categoryItem.getAdCode());
        holder.partyName.setText(categoryItem.getAdName());

        String bf = categoryItem.getBf();

        if (bf.contains("-")) {
            bf = bf.substring(1);
            bf = "(-)  "+ bf;
        }
        holder.bfPsl.setText(bf);

        String dr = categoryItem.getDebit();

        if (dr.contains("-")) {
            dr = dr.substring(1);
            dr = "(-)  "+ dr;
        }
        holder.debitPsl.setText(dr);

        String cr = categoryItem.getCredit();

        if (cr.contains("-")) {
            cr = cr.substring(1);
            cr = "(-)  "+ cr;
        }
        holder.creditPsl.setText(cr);

        String bal = categoryItem.getDueBalance();

        if (bal.contains("-")) {
            bal = bal.substring(1);
            bal = "(-)  "+ bal;
        }
        holder.dueBalancePsl.setText(bal);

        ColorStateList color = holder.partyName.getTextColors();

        if (categoryItem.getAdFlag().equals("6") && categoryItem.getDueBalance().contains("-")) {
            holder.dueBalancePsl.setTextColor(Color.parseColor("#CC0000"));
        } else if (!categoryItem.getAdFlag().equals("6") && !categoryItem.getDueBalance().contains("-")) {
            holder.dueBalancePsl.setTextColor(Color.parseColor("#CC0000"));
        } else {
            holder.dueBalancePsl.setTextColor(color);
        }


//        if(sods_selected_position == position) {
//            holder.itemView.setBackgroundColor(Color.parseColor("#81ecec"));
//        }
//        else {
//            holder.itemView.setBackgroundColor(Color.parseColor("#f5f6fa"));
//        }


//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (sods_selected_position == position) {
//                    sods_selected_position = -1;
//                } else {
//                    sods_selected_position=position;
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

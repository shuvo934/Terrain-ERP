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
import ttit.com.shuvo.icomerp.arrayList.PayRcvSummAccountsList;

public class PartyPayRcvSumAdapter extends RecyclerView.Adapter<PartyPayRcvSumAdapter.PPRSLHolder> {

    private ArrayList<PayRcvSummAccountsList> mCategoryItem;
    private final Context myContext;
    private final ClickedItem myClickedItem;

    public PartyPayRcvSumAdapter(ArrayList<PayRcvSummAccountsList> categoryItems, Context context, ClickedItem myClickedItem) {
        this.mCategoryItem = categoryItems;
        this.myContext = context;
        this.myClickedItem = myClickedItem;
    }

    public class PPRSLHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView slNo;
        public TextView partyName;
        public TextView accountNo;
        public TextView balance;
        ClickedItem mClickedItem;

        public PPRSLHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            accountNo = itemView.findViewById(R.id.account_no_pprsl);
            partyName = itemView.findViewById(R.id.party_name_pprsl);
            slNo = itemView.findViewById(R.id.serial_no_pprsl);
            balance = itemView.findViewById(R.id.balance_pprsl);

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
    public PPRSLHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.party_pay_rcv_accnt_sum_view, parent, false);
        return new PPRSLHolder(view,myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull PPRSLHolder holder, int position) {

        PayRcvSummAccountsList categoryItem = mCategoryItem.get(position);

        //positionFromFirstAdapter = position;


        holder.accountNo.setText(categoryItem.getAccountNo());
        holder.partyName.setText(categoryItem.getPartyName());
        holder.slNo.setText(categoryItem.getSlNo());

        String bal = categoryItem.getBalance();

        if (bal.contains("-")) {
            bal = bal.substring(1);
            bal = "(-)  "+ bal;
        }
        holder.balance.setText(bal);



        ColorStateList color = holder.partyName.getTextColors();

        if (categoryItem.getBalanceType().equals("PAYABLE")) {
            holder.balance.setTextColor(Color.parseColor("#CC0000"));
        } else {
            holder.balance.setTextColor(color);
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

package ttit.com.shuvo.icomerp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.arrayList.CashTransactionLists;

public class CashTransactionAdapter extends RecyclerView.Adapter<CashTransactionAdapter.CSHolder> {

    private ArrayList<CashTransactionLists> mCategoryItem;
    private final Context myContext;
    private final ClickedItem myClickedItem;

    public CashTransactionAdapter(ArrayList<CashTransactionLists> categoryItems, Context context, ClickedItem myClickedItem) {
        this.mCategoryItem = categoryItems;
        this.myContext = context;
        this.myClickedItem = myClickedItem;
    }

    public class CSHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView vNo;
        public TextView particul;
        public TextView debitCS;
        public TextView creditCS;
        ClickedItem mClickedItem;

        public CSHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            vNo = itemView.findViewById(R.id.voucher_no_cs);
            particul = itemView.findViewById(R.id.particulars_cs);
            debitCS = itemView.findViewById(R.id.debit_cs);
            creditCS = itemView.findViewById(R.id.credit_cs);

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
    public CSHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.cash_transaction_item_view, parent, false);
        return new CSHolder(view,myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull CSHolder holder, int position) {

        CashTransactionLists categoryItem = mCategoryItem.get(position);

        //positionFromFirstAdapter = position;


        holder.vNo.setText(categoryItem.getVoucherNo());
        holder.particul.setText(categoryItem.getParticulars());
        holder.debitCS.setText(categoryItem.getDebit());
        holder.creditCS.setText(categoryItem.getCredit());

        if (categoryItem.getType().equals("AV") || categoryItem.getType().equals("DPRCV") || categoryItem.getType().equals("SOM") || categoryItem.getType().equals("SM")) {
            holder.vNo.setTextColor(Color.parseColor("#3A9FEE"));
        }



//        ColorStateList color = holder.partyName.getTextColors();

//        if (categoryItem.getBalanceType().equals("PAYABLE")) {
//            holder.balance.setTextColor(Color.parseColor("#CC0000"));
//        } else {
//            holder.balance.setTextColor(color);
//        }



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

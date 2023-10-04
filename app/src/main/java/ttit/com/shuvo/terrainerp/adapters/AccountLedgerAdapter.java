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
import ttit.com.shuvo.terrainerp.arrayList.AccountLedgerLists;

public class AccountLedgerAdapter extends RecyclerView.Adapter<AccountLedgerAdapter.ALHolder> {

    private ArrayList<AccountLedgerLists> mCategoryItem;
    private final Context myContext;
    private final ClickedItem myClickedItem;

    public AccountLedgerAdapter(ArrayList<AccountLedgerLists> categoryItems, Context context, ClickedItem myClickedItem) {
        this.mCategoryItem = categoryItems;
        this.myContext = context;
        this.myClickedItem = myClickedItem;
    }

    public class ALHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView vDate;
        public TextView voucherNo;
        public TextView particulars;
        public TextView alDebit;
        public TextView alCredit;
        public TextView alBalance;
        ClickedItem mClickedItem;

        public ALHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            vDate = itemView.findViewById(R.id.acc_led_date);
            voucherNo = itemView.findViewById(R.id.voucher_no_acc_led);
            particulars = itemView.findViewById(R.id.particulars_acc_led);
            alDebit = itemView.findViewById(R.id.debit_acc_led);
            alCredit = itemView.findViewById(R.id.credit_acc_led);
            alBalance = itemView.findViewById(R.id.balance_acc_led);

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
    public ALHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.account_ledger_item_view, parent, false);
        return new ALHolder(view,myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ALHolder holder, int position) {

        AccountLedgerLists categoryItem = mCategoryItem.get(position);

        //positionFromFirstAdapter = position;


        holder.vDate.setText(categoryItem.getDate());
        holder.voucherNo.setText(categoryItem.getVoucherNo());
        holder.particulars.setText(categoryItem.getParticulars());
        holder.alDebit.setText(categoryItem.getDebit());
        holder.alCredit.setText(categoryItem.getCredit());
        String bal = categoryItem.getBalance();

        if (bal.contains("-")) {
            bal = bal.substring(1);
            bal = "(-)  "+ bal;
        }
        holder.alBalance.setText(bal);


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

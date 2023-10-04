package ttit.com.shuvo.terrainerp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.arrayList.DebitVABillLists;
import ttit.com.shuvo.terrainerp.arrayList.DebitVoucherApprovedList;

public class DebitVoucherApprovedAdapter extends RecyclerView.Adapter<DebitVoucherApprovedAdapter.DVAHolder> {

    private ArrayList<DebitVoucherApprovedList> mCategoryItem;
    private final Context myContext;
    private final ClickedItem myClickedItem;
    public DebitVoucherBillAdapter debitVoucherBillAdapter;

    public DebitVoucherApprovedAdapter(ArrayList<DebitVoucherApprovedList> categoryItems, Context context, ClickedItem myClickedItem) {
        this.mCategoryItem = categoryItems;
        this.myContext = context;
        this.myClickedItem = myClickedItem;

    }

    public static class DVAHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView acNo;
        public TextView acDetails;
        public TextView partyCode;
        public TextView partyName;
        public TextView chequeNo;
        public TextView chequeDate;
        public TextView vDebit;
        public TextView vCredit;
        public RecyclerView itemDetails;
        ClickedItem mClickedItem;

        public DVAHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            acNo = itemView.findViewById(R.id.ac_no_debit_voucher_approved);
            acDetails = itemView.findViewById(R.id.ac_details_debit_voucher_approved);
            partyCode = itemView.findViewById(R.id.party_code_debit_voucher_approved);
            partyName = itemView.findViewById(R.id.party_name_debit_voucher_approved);
            chequeNo = itemView.findViewById(R.id.cheque_no_debit_voucher_approved);
            chequeDate = itemView.findViewById(R.id.cheque_date_debit_voucher_approved);
            vDebit = itemView.findViewById(R.id.debit_amount_debit_voucher_approved);
            vCredit = itemView.findViewById(R.id.credit_amount_debit_voucher_approved);
            itemDetails = itemView.findViewById(R.id.debit_v_a_bill_view);

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
    public DVAHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.debit_voucher_approved_item_view, parent, false);
        return new DVAHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull DVAHolder holder, int position) {

        DebitVoucherApprovedList categoryItem = mCategoryItem.get(position);


        holder.acNo.setText(categoryItem.getAdCode());
        holder.acDetails.setText(categoryItem.getAdName());
        holder.partyCode.setText(categoryItem.getPartyCode());
        holder.partyName.setText(categoryItem.getPartyName());
        holder.chequeNo.setText(categoryItem.getVdChequeNo());
        holder.chequeDate.setText(categoryItem.getVdChequeDate());
        holder.vDebit.setText(categoryItem.getVdDebit());
        holder.vCredit.setText(categoryItem.getVdCredit());

        ArrayList<DebitVABillLists> voucherLists2s = categoryItem.getDebitVABillLists();

        holder.itemDetails.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(myContext);
        holder.itemDetails.setLayoutManager(layoutManager);
        debitVoucherBillAdapter = new DebitVoucherBillAdapter(voucherLists2s,myContext);
        holder.itemDetails.setAdapter(debitVoucherBillAdapter);

    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

}

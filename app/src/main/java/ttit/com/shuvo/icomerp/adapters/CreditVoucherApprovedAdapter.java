package ttit.com.shuvo.icomerp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.arrayList.CreditVoucherApprovedLists;

public class CreditVoucherApprovedAdapter extends RecyclerView.Adapter<CreditVoucherApprovedAdapter.CVAHolder> {

    private ArrayList<CreditVoucherApprovedLists> mCategoryItem;
    private final Context myContext;
    private final ClickedItem myClickedItem;

    public CreditVoucherApprovedAdapter(ArrayList<CreditVoucherApprovedLists> categoryItems, Context context, ClickedItem myClickedItem) {
        this.mCategoryItem = categoryItems;
        this.myContext = context;
        this.myClickedItem = myClickedItem;
    }

    public class CVAHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView acNo;
        public TextView acDetails;
        public TextView partyCode;
        public TextView partyName;
        public TextView chequeNo;
        public TextView chequeDate;
        public TextView vDebit;
        public TextView vCredit;
        ClickedItem mClickedItem;

        public CVAHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            acNo = itemView.findViewById(R.id.ac_no_voucher_approved);
            acDetails = itemView.findViewById(R.id.ac_details_voucher_approved);
            partyCode = itemView.findViewById(R.id.party_code_voucher_approved);
            partyName = itemView.findViewById(R.id.party_name_voucher_approved);
            chequeNo = itemView.findViewById(R.id.cheque_no_voucher_approved);
            chequeDate = itemView.findViewById(R.id.cheque_date_voucher_approved);
            vDebit = itemView.findViewById(R.id.debit_amount_voucher_approved);
            vCredit = itemView.findViewById(R.id.credit_amount_voucher_approved);

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
    public CVAHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.voucher_debit_credit_item_list_approved_view, parent, false);
        return new CVAHolder(view,myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull CVAHolder holder, int position) {

        CreditVoucherApprovedLists categoryItem = mCategoryItem.get(position);


        holder.acNo.setText(categoryItem.getAdCode());
        holder.acDetails.setText(categoryItem.getAdName());
        holder.partyCode.setText(categoryItem.getPartyCode());
        holder.partyName.setText(categoryItem.getPartyName());
        holder.chequeNo.setText(categoryItem.getVdChequeNo());
        holder.chequeDate.setText(categoryItem.getVdChequeDate());
        holder.vDebit.setText(categoryItem.getVdDebit());
        holder.vCredit.setText(categoryItem.getVdCredit());


    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }
}

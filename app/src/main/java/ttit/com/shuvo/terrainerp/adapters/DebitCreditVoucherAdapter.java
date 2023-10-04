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
import ttit.com.shuvo.terrainerp.arrayList.DebitCreditVoucherLists;

public class DebitCreditVoucherAdapter extends RecyclerView.Adapter<DebitCreditVoucherAdapter.DVHolder> {

    private final ArrayList<DebitCreditVoucherLists> mCategoryItem;
    private final Context myContext;

    public DebitCreditVoucherAdapter(ArrayList<DebitCreditVoucherLists> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }
    @NonNull
    @Override
    public DVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.debit_credit_voucher_item_view, parent, false);
        return new DVHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DVHolder holder, int position) {

        DebitCreditVoucherLists debitCreditVoucherLists = mCategoryItem.get(position);
//        String totalNarration = "";
//        if (debitVoucherLists.getNarration() != null) {
//             totalNarration =  debitVoucherLists.getNarration()+"\n"+debitVoucherLists.getBillInfo();
//        } else {
//            totalNarration = debitVoucherLists.getBillInfo();
//        }

        //holder.narration.setText(totalNarration);
        holder.ac_no.setText(debitCreditVoucherLists.getAdCode());

        String totalHead = "";
        if (debitCreditVoucherLists.getAdDate() != null) {
            totalHead = debitCreditVoucherLists.getAdName() + "\n" + debitCreditVoucherLists.getAdDate();
        } else {
            totalHead = debitCreditVoucherLists.getAdName();
        }
        holder.headAc.setText(totalHead);
        holder.payeeName.setText(debitCreditVoucherLists.getPayeeName());
        holder.debit.setText(debitCreditVoucherLists.getDebit());
        holder.credit.setText(debitCreditVoucherLists.getCredit());

    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public static class DVHolder extends RecyclerView.ViewHolder {

        //public TextView narration;
        public TextView ac_no;
        public TextView headAc;
        public TextView payeeName;
        public TextView debit;
        public TextView credit;

        public DVHolder(@NonNull View itemView) {
            super(itemView);

            //narration = itemView.findViewById(R.id.narration_item_dv);
            ac_no = itemView.findViewById(R.id.ac_no_item_dv);
            headAc = itemView.findViewById(R.id.head_of_acc_dv);
            payeeName = itemView.findViewById(R.id.payee_name_dv);
            debit = itemView.findViewById(R.id.debit_dv);
            credit = itemView.findViewById(R.id.credit_dv);
        }
    }
}

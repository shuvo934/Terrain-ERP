package ttit.com.shuvo.terrainerp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.arrayList.DebitVABillDetailsLists;
import ttit.com.shuvo.terrainerp.arrayList.DebitVABillLists;

public class DebitVoucherBillAdapter extends RecyclerView.Adapter<DebitVoucherBillAdapter.DVBHolder> {

    private ArrayList<DebitVABillLists> mCategoryItem;
    private final Context myContext;
    public DebitVoucherBillDetailsAdapter debitVoucherBillDetailsAdapter;

    public DebitVoucherBillAdapter(ArrayList<DebitVABillLists> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    public class DVBHolder extends RecyclerView.ViewHolder {
        public TextView billNo;
        public TextView billType;
        public TextView remarks;

        public RecyclerView itemDetails;

        public LinearLayout bottomborder;


        public DVBHolder(@NonNull View itemView) {
            super(itemView);
            billNo = itemView.findViewById(R.id.bill_no_debit_voucher_approved);
            billType = itemView.findViewById(R.id.bill_type_debit_voucher_approved);
            remarks = itemView.findViewById(R.id.remarks_debit_voucher_approved);

            itemDetails = itemView.findViewById(R.id.debit_v_a_bill_details_view);
            bottomborder = itemView.findViewById(R.id.layout_bottom_border_debit_bill_view);

        }
    }

    @NonNull
    @Override
    public DVBHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.debit_voucher_app_item_bill_view, parent, false);
        return new DVBHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DVBHolder holder, int position) {

        DebitVABillLists voucherLists2 = mCategoryItem.get(position);

        holder.billNo.setText(voucherLists2.getBillNo());
        holder.billType.setText(voucherLists2.getBillType());
        holder.remarks.setText(voucherLists2.getRemarks());

        ArrayList<DebitVABillDetailsLists> vaBillDetailsLists = voucherLists2.getDebitVABillDetailsLists();

        holder.itemDetails.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(myContext);
        holder.itemDetails.setLayoutManager(layoutManager);
        debitVoucherBillDetailsAdapter = new DebitVoucherBillDetailsAdapter(vaBillDetailsLists,myContext);
        holder.itemDetails.setAdapter(debitVoucherBillDetailsAdapter);

        if (position == mCategoryItem.size()-1) {
            holder.bottomborder.setVisibility(View.GONE);
        } else {
            holder.bottomborder.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }
}

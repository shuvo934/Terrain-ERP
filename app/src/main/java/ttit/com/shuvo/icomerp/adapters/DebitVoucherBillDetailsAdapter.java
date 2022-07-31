package ttit.com.shuvo.icomerp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.arrayList.DebitVABillDetailsLists;

public class DebitVoucherBillDetailsAdapter extends RecyclerView.Adapter<DebitVoucherBillDetailsAdapter.DVBDHolder> {

    private final ArrayList<DebitVABillDetailsLists> mCategoryItem;
    private final Context myContext;

    public DebitVoucherBillDetailsAdapter(ArrayList<DebitVABillDetailsLists> categoryItems, Context context) {
        this.mCategoryItem = categoryItems;
        this.myContext = context;
    }

    public static class DVBDHolder extends RecyclerView.ViewHolder {

        public TextView poWoNo;
        public TextView invoiceNo;
        public TextView invoiceAmnt;
        public TextView vatAmnt;
        public LinearLayout bottomBorder;

        public DVBDHolder(@NonNull View itemView) {
            super(itemView);
            poWoNo = itemView.findViewById(R.id.po_wo_no_debit_voucher_approved);
            invoiceNo = itemView.findViewById(R.id.invoice_no_debit_voucher_approved);
            invoiceAmnt = itemView.findViewById(R.id.invoice_amnt_debit_voucher_approved);
            vatAmnt = itemView.findViewById(R.id.vat_amnt_debit_voucher_approved);
            bottomBorder = itemView.findViewById(R.id.layout_bottom_border_debit_bill_details);

        }

    }

    @NonNull
    @Override
    public DVBDHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.debit_voucher_app_item_bill_details_view, parent, false);
        return new DVBDHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DVBDHolder holder, int position) {

        DebitVABillDetailsLists voucherLists3 = mCategoryItem.get(position);

        holder.poWoNo.setText(voucherLists3.getPoWoNo());
        holder.invoiceNo.setText(voucherLists3.getInvoiceNo());
        holder.invoiceAmnt.setText(voucherLists3.getInvoiceAmnt());
        holder.vatAmnt.setText(voucherLists3.getVatAmnt());

        if (position == mCategoryItem.size()-1) {
            holder.bottomBorder.setVisibility(View.GONE);
        } else {
            holder.bottomBorder.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

}

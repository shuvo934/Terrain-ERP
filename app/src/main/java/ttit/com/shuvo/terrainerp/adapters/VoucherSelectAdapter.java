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
import ttit.com.shuvo.terrainerp.arrayList.VoucherSelectionList;

public class VoucherSelectAdapter extends RecyclerView.Adapter<VoucherSelectAdapter.VSHolder> {

    private ArrayList<VoucherSelectionList> mCategoryItem;
    private final Context myContext;
    private final ClickedItem myClickedItem;

    public VoucherSelectAdapter(ArrayList<VoucherSelectionList> categoryItems, Context context, ClickedItem myClickedItem) {
        this.mCategoryItem = categoryItems;
        this.myContext = context;
        this.myClickedItem = myClickedItem;
    }

    public class VSHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView vDate;
        public TextView voucherNo;
        public TextView billrefNo;
        public TextView status;
        public TextView billrefDate;
        public TextView vAmount;
        ClickedItem mClickedItem;

        public VSHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            vDate = itemView.findViewById(R.id.voucher_date_svncva);
            voucherNo = itemView.findViewById(R.id.voucher_no_svncva);
            billrefNo = itemView.findViewById(R.id.bill_ref_no_svncva);
            billrefDate = itemView.findViewById(R.id.bill_ref_date_svncva);
            vAmount = itemView.findViewById(R.id.amount_svncva);
            status = itemView.findViewById(R.id.status_svncva);
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
    public VSHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.voucher_select_item_view, parent, false);
        return new VSHolder(view,myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull VSHolder holder, int position) {

        VoucherSelectionList categoryItem = mCategoryItem.get(position);


        holder.vDate.setText(categoryItem.getVm_date());
        holder.voucherNo.setText(categoryItem.getVm_no());
        holder.billrefNo.setText(categoryItem.getVm_bill_ref_no());
        holder.billrefDate.setText(categoryItem.getVm_bill_ref_date());
        holder.vAmount.setText(categoryItem.getAmount());
        holder.status.setText(categoryItem.getStatus());


    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public void filterList(ArrayList<VoucherSelectionList> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }
}

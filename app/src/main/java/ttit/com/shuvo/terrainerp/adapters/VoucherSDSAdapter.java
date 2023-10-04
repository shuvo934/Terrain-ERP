package ttit.com.shuvo.terrainerp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.arrayList.VoucherSDSList;

import static ttit.com.shuvo.terrainerp.adapters.ClientSDSAdapter.positionFromFirstAdapter;
import static ttit.com.shuvo.terrainerp.fragments.SalesOrderDeliverySummary.sods_selected_position;

public class VoucherSDSAdapter extends RecyclerView.Adapter<VoucherSDSAdapter.VoucherHolder> {

    private final ArrayList<VoucherSDSList> mCategoryItem;
    private final Context myContext;

    public VoucherSDSAdapter(ArrayList<VoucherSDSList> categoryItems, Context context) {
        this.mCategoryItem = categoryItems;
        this.myContext = context;
    }

    public static class VoucherHolder extends RecyclerView.ViewHolder {

        public TextView vNo;
        public TextView vDate;
        public TextView vAmnt;
        public LinearLayout bottomBorder;

        public VoucherHolder(@NonNull View itemView) {
            super(itemView);
            vNo = itemView.findViewById(R.id.voucher_no_sds);
            vDate = itemView.findViewById(R.id.voucher_date_sds);
            vAmnt = itemView.findViewById(R.id.voucher_amnt_sds);
            bottomBorder = itemView.findViewById(R.id.layout_bottom_border_voucher);

        }

    }

    @NonNull
    @Override
    public VoucherSDSAdapter.VoucherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.voucher_no_sales_deliver_sum, parent, false);
        return new VoucherHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherSDSAdapter.VoucherHolder holder, int position) {

        VoucherSDSList voucherSDSList = mCategoryItem.get(position);

        holder.vNo.setText(voucherSDSList.getVoucherNo());
        holder.vDate.setText(voucherSDSList.getVoucherDate());
        holder.vAmnt.setText(voucherSDSList.getVoucherAmnt());

        if (position == mCategoryItem.size()-1) {
            holder.bottomBorder.setVisibility(View.GONE);
        } else {
            holder.bottomBorder.setVisibility(View.VISIBLE);
        }

        if (sods_selected_position == positionFromFirstAdapter) {
            holder.itemView.setBackgroundColor(Color.parseColor("#81ecec"));
        }
        else {
            holder.itemView.setBackgroundColor(Color.parseColor("#f5f6fa"));
        }
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }
}

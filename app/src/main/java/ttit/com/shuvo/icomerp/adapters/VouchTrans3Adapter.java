package ttit.com.shuvo.icomerp.adapters;

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

import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.arrayList.VoucherLists3;


public class VouchTrans3Adapter extends RecyclerView.Adapter<VouchTrans3Adapter.VT3Holder> {

    private final ArrayList<VoucherLists3> mCategoryItem;
    private final Context myContext;

    public VouchTrans3Adapter(ArrayList<VoucherLists3> categoryItems, Context context) {
        this.mCategoryItem = categoryItems;
        this.myContext = context;
    }

    public static class VT3Holder extends RecyclerView.ViewHolder {

        public TextView acD;
        public TextView debit;
        public TextView credit;
        public LinearLayout bottomBorder;

        public VT3Holder(@NonNull View itemView) {
            super(itemView);
            acD = itemView.findViewById(R.id.account_details_v_trans);
            debit = itemView.findViewById(R.id.debit_v_trans);
            credit = itemView.findViewById(R.id.credit_v_trans);
            bottomBorder = itemView.findViewById(R.id.layout_bottom_border_v_trans_ac_d_c);

        }

    }

    @NonNull
    @Override
    public VT3Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.v_trans_view_3, parent, false);
        return new VT3Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VT3Holder holder, int position) {

        VoucherLists3 voucherLists3 = mCategoryItem.get(position);

        holder.acD.setText(voucherLists3.getAccountDetails());
        holder.debit.setText(voucherLists3.getDebit());
        holder.credit.setText(voucherLists3.getCredit());

        if (position == mCategoryItem.size()-1) {
            holder.bottomBorder.setVisibility(View.GONE);
        } else {
            holder.bottomBorder.setVisibility(View.VISIBLE);
        }

//        if (sods_selected_position == positionFromFirstAdapter) {
//            holder.itemView.setBackgroundColor(Color.parseColor("#81ecec"));
//        }
//        else {
//            holder.itemView.setBackgroundColor(Color.parseColor("#f5f6fa"));
//        }
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }
}

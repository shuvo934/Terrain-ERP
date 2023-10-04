package ttit.com.shuvo.terrainerp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.arrayList.DCColorWiseItemList;

public class DCColorWiseItemAdapter extends RecyclerView.Adapter<DCColorWiseItemAdapter.DCCWIHolder> {

    private final ArrayList<DCColorWiseItemList> mCategoryItem;
    private final Context myContext;

    public DCColorWiseItemAdapter(ArrayList<DCColorWiseItemList> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public DCCWIHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.dc_color_wise_item_details_view, parent, false);
        return new DCCWIHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DCCWIHolder holder, int position) {

        DCColorWiseItemList dcc = mCategoryItem.get(position);

        holder.colorName.setText(dcc.getColor_name());
        holder.sizeName.setText(dcc.getSize());
        holder.colorQty.setText(dcc.getQty());
        holder.cUnit.setText(dcc.getUnit());
        holder.price.setText(dcc.getPrice());
        holder.discType.setText(dcc.getDisc_type());
        holder.discValueUnit.setText(dcc.getDisc_value_unit());
        holder.wDiscType.setText(dcc.getW_disc_type());
        holder.wDiscValueUnit.setText(dcc.getW_disc_value_unit());
        holder.totalDiscAmnt.setText(dcc.getTotal_disc());
        holder.discountPriceUnit.setText(dcc.getDisc_price_unit());
        holder.amnt.setText(dcc.getAmnt());
        holder.sampQty.setText(dcc.getSample_qty());
        holder.vatAmnt.setText(dcc.getVat_amnt_unit());
        holder.totalVatAmnt.setText(dcc.getTotal_vat_amnt());
        holder.retQty.setText(dcc.getRet_qty());
        holder.retAmnt.setText(dcc.getRet_amnt());
        holder.totalQty.setText(dcc.getTotal_qty());
        holder.totalAmnt.setText(dcc.getTotal_amnt());

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

    public static class DCCWIHolder extends RecyclerView.ViewHolder {

        public TextView colorName;
        public TextView sizeName;
        public TextView colorQty;
        public TextView cUnit;
        public TextView price;
        public TextView discType;
        public TextView discValueUnit;
        public TextView wDiscType;
        public TextView wDiscValueUnit;
        public TextView totalDiscAmnt;
        public TextView discountPriceUnit;
        public TextView amnt;
        public TextView sampQty;
        public TextView vatAmnt;
        public TextView totalVatAmnt;
        public TextView retQty;
        public TextView retAmnt;
        public TextView totalQty;
        public TextView totalAmnt;

        public LinearLayout bottomBorder;

        public DCCWIHolder(@NonNull View itemView) {
            super(itemView);
            colorName = itemView.findViewById(R.id.color_dc);
            sizeName = itemView.findViewById(R.id.size_dc);
            colorQty = itemView.findViewById(R.id.color_qty_dc);
            cUnit = itemView.findViewById(R.id.color_unit_dc);
            price = itemView.findViewById(R.id.color_price_dc);
            discType = itemView.findViewById(R.id.color_disc_type_dc);
            discValueUnit = itemView.findViewById(R.id.color_disc_value_unit_dc);
            wDiscType = itemView.findViewById(R.id.color_w_disc_type_dc);
            wDiscValueUnit = itemView.findViewById(R.id.color_w_disc_value_unit_dc);
            totalDiscAmnt = itemView.findViewById(R.id.color_total_disc_dc);
            discountPriceUnit = itemView.findViewById(R.id.color_disc_price_unit);
            amnt = itemView.findViewById(R.id.color_amnt_dc);
            sampQty = itemView.findViewById(R.id.color_samp_qty_dc);
            vatAmnt = itemView.findViewById(R.id.color_vat_amnt_unit_dc);
            totalVatAmnt = itemView.findViewById(R.id.color_total_vat_amnt_dc);
            retQty = itemView.findViewById(R.id.color_ret_qty_dc);
            retAmnt = itemView.findViewById(R.id.color_ret_amnt_dc);
            totalQty = itemView.findViewById(R.id.color_total_qty_dc);
            totalAmnt = itemView.findViewById(R.id.color_total_amnt_dc);

            bottomBorder = itemView.findViewById(R.id.layout_bottom_border_dc);

        }

    }
}

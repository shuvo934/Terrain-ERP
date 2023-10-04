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
import ttit.com.shuvo.terrainerp.arrayList.PurchaseOrderSelectionList;

public class PurchaseOrderForPRCVAdapter extends RecyclerView.Adapter<PurchaseOrderForPRCVAdapter.POPRCVHoLder> {

    private ArrayList<PurchaseOrderSelectionList> mCategoryItem;
    private final Context myContext;
    private final ClickedItem myClickedItem;

    public PurchaseOrderForPRCVAdapter(ArrayList<PurchaseOrderSelectionList> mCategoryItem, Context myContext, ClickedItem myClickedItem) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
        this.myClickedItem = myClickedItem;
    }

    @NonNull
    @Override
    public POPRCVHoLder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.purchase_order_item_list_view, parent, false);
        return new POPRCVHoLder(view,myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull POPRCVHoLder holder, int position) {
        PurchaseOrderSelectionList categoryItem = mCategoryItem.get(position);

        holder.OrderNo.setText(categoryItem.getWom_no());
        holder.Orderdate.setText(categoryItem.getWom_date());
        holder.Orderuser.setText(categoryItem.getWom_user());
        holder.OrderSupplier.setText(categoryItem.getAd_name());
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public static class POPRCVHoLder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView OrderNo;
        public TextView Orderdate;
        public TextView Orderuser;
        public TextView OrderSupplier;
        ClickedItem mClickedItem;

        public POPRCVHoLder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            OrderNo = itemView.findViewById(R.id.order_no_poa);
            Orderdate = itemView.findViewById(R.id.order_date_poa);
            Orderuser = itemView.findViewById(R.id.order_user_poa);
            OrderSupplier = itemView.findViewById(R.id.supplier_name_poa);
            this.mClickedItem = ci;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mClickedItem.onPOClicked(getAdapterPosition());
        }
    }

    public interface ClickedItem {
        void onPOClicked(int Position);
    }

    public void filterList(ArrayList<PurchaseOrderSelectionList> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }
}

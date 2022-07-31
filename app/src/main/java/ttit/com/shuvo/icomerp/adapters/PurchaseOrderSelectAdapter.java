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
import ttit.com.shuvo.icomerp.arrayList.PurchaseOrderSelectList;
import ttit.com.shuvo.icomerp.arrayList.PurchaseRequisionLists;

public class PurchaseOrderSelectAdapter extends RecyclerView.Adapter<PurchaseOrderSelectAdapter.POSHolder> {

    private ArrayList<PurchaseOrderSelectList> mCategoryItem;
    private final Context myContext;
    private final ClickedItem myClickedItem;

    public PurchaseOrderSelectAdapter(ArrayList<PurchaseOrderSelectList> categoryItems, Context context, ClickedItem myClickedItem) {
        this.mCategoryItem = categoryItems;
        this.myContext = context;
        this.myClickedItem = myClickedItem;
    }

    public static class POSHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView OrderNo;
        public TextView Orderdate;
        public TextView Orderuser;
        public TextView OrderSupplier;

        ClickedItem mClickedItem;

        public POSHolder(@NonNull View itemView, ClickedItem ci) {
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
            mClickedItem.onCategoryClicked(getAdapterPosition());
            //Log.i("Client Name", mCategoryItem.get(getAdapterPosition()).getvDate());
        }

    }
    public interface ClickedItem {
        void onCategoryClicked(int CategoryPosition);
    }

    @NonNull
    @Override
    public POSHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.purchase_order_item_list_view, parent, false);
        return new POSHolder(view,myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull POSHolder holder, int position) {

        PurchaseOrderSelectList categoryItem = mCategoryItem.get(position);


        holder.OrderNo.setText(categoryItem.getWom_no());
        holder.Orderdate.setText(categoryItem.getDate());
        holder.Orderuser.setText(categoryItem.getUserName());
        holder.OrderSupplier.setText(categoryItem.getSupplierName());


    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public void filterList(ArrayList<PurchaseOrderSelectList> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }
}

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
import ttit.com.shuvo.icomerp.arrayList.PurchaseRequisionLists;
import ttit.com.shuvo.icomerp.arrayList.VoucherSelectionList;

public class PurchaseRequisitionSelectAdapter extends RecyclerView.Adapter<PurchaseRequisitionSelectAdapter.PRSHolder> {

    private ArrayList<PurchaseRequisionLists> mCategoryItem;
    private final Context myContext;
    private final ClickedItem myClickedItem;

    public PurchaseRequisitionSelectAdapter(ArrayList<PurchaseRequisionLists> categoryItems, Context context, ClickedItem myClickedItem) {
        this.mCategoryItem = categoryItems;
        this.myContext = context;
        this.myClickedItem = myClickedItem;
    }

    public static class PRSHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView reqNo;
        public TextView reqdate;
        public TextView requser;

        ClickedItem mClickedItem;

        public PRSHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            reqNo = itemView.findViewById(R.id.req_no_pra);
            reqdate = itemView.findViewById(R.id.req_date_pra);
            requser = itemView.findViewById(R.id.req_user_pra);
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
    public PRSHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.purchase_requisition_item_list_view, parent, false);
        return new PRSHolder(view,myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull PRSHolder holder, int position) {

        PurchaseRequisionLists categoryItem = mCategoryItem.get(position);


        holder.reqNo.setText(categoryItem.getReqNo());
        holder.reqdate.setText(categoryItem.getPrDate());
        holder.requser.setText(categoryItem.getUser());


    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public void filterList(ArrayList<PurchaseRequisionLists> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }
}

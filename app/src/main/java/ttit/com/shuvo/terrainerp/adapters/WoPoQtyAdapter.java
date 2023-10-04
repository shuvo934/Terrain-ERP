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
import ttit.com.shuvo.terrainerp.arrayList.WoPoQtyLists;

public class WoPoQtyAdapter extends RecyclerView.Adapter<WoPoQtyAdapter.WoPoQtyHolder> {

    private ArrayList<WoPoQtyLists> mCategoryItem;
    private final Context myContext;


    public WoPoQtyAdapter(ArrayList<WoPoQtyLists> categoryItems, Context context) {
        this.mCategoryItem = categoryItems;
        this.myContext = context;
    }

    public class WoPoQtyHolder extends RecyclerView.ViewHolder {
        public TextView ftext;
        public TextView stext;
        public TextView ttext;



        public WoPoQtyHolder(@NonNull View itemView) {
            super(itemView);
            ftext = itemView.findViewById(R.id.wo_po_item_name);
            stext = itemView.findViewById(R.id.wo_po_item_qty);
            ttext = itemView.findViewById(R.id.wo_po_item_rate);

        }

    }


    @NonNull
    @Override
    public WoPoQtyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.wo_po_bill_details_item_view, parent, false);
        WoPoQtyHolder categoryViewHolder = new WoPoQtyHolder(view);
        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WoPoQtyHolder holder, int position) {

        WoPoQtyLists categoryItem = mCategoryItem.get(position);

        holder.ftext.setText(categoryItem.getItemName());
        holder.stext.setText(categoryItem.getQty());
        holder.ttext.setText(categoryItem.getRate()+" BDT");

    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

}

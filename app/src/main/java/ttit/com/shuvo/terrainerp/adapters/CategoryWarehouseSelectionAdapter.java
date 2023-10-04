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
import ttit.com.shuvo.terrainerp.arrayList.ReceiveTypeList;

public class CategoryWarehouseSelectionAdapter extends RecyclerView.Adapter<CategoryWarehouseSelectionAdapter.CWSAHolder> {

    private ArrayList<ReceiveTypeList> mCategoryItem;
    private final Context myContext;
    private final ClickedItem myClickedItem;

    public CategoryWarehouseSelectionAdapter(ArrayList<ReceiveTypeList> mCategoryItem, Context myContext, ClickedItem myClickedItem) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
        this.myClickedItem = myClickedItem;
    }

    @NonNull
    @Override
    public CWSAHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.category_warehouse_item_view, parent, false);
        return new CWSAHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull CWSAHolder holder, int position) {
        ReceiveTypeList list = mCategoryItem.get(position);
        holder.itemName.setText(list.getType());
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public static class CWSAHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView itemName;
        ClickedItem mClickedItem;

        public CWSAHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            itemName = itemView.findViewById(R.id.category_warehouse_name_for_p_rcv_to_select);
            this.mClickedItem = ci;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mClickedItem.onItemClicked(getAdapterPosition());
        }
    }

    public interface ClickedItem {
        void onItemClicked(int Position);
    }

    public void filterList(ArrayList<ReceiveTypeList> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }
}

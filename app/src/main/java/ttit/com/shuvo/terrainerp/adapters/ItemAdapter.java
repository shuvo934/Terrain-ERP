package ttit.com.shuvo.terrainerp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.arrayList.ItemViewList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {

    private ArrayList<ItemViewList> mCategoryItem;
    private final ClickedItem myClickedItem;
    private final Context myContext;


    public ItemAdapter(ArrayList<ItemViewList> categoryItems, Context context, ClickedItem cli) {
        this.mCategoryItem = categoryItems;
        this.myClickedItem = cli;
        this.myContext = context;
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView ftext;
        public TextView stext;
        public TextView ttext;
        public TextView fotext;

        ClickedItem mClickedItem;

        public ItemHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            ftext = itemView.findViewById(R.id.first_item);
            stext = itemView.findViewById(R.id.second_item);
            ttext = itemView.findViewById(R.id.third_item);
            fotext = itemView.findViewById(R.id.fourth_item);


            this.mClickedItem = ci;

            itemView.setOnClickListener(this::onClick);

        }

        @Override
        public void onClick(View v) {
            mClickedItem.onCategoryClicked(getAdapterPosition());
            Log.i("Category Name", mCategoryItem.get(getAdapterPosition()).getItem_name());
        }
    }

    public interface ClickedItem {
        void onCategoryClicked(int CategoryPosition);
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.item_list_view, parent, false);
        ItemHolder categoryViewHolder = new ItemHolder(view, myClickedItem);
        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {

        ItemViewList categoryItem = mCategoryItem.get(position);

        holder.ftext.setText(categoryItem.getItem_name());
        holder.stext.setText(categoryItem.getItem_wo_amnt()+" BDT");
        holder.ttext.setText(categoryItem.getItem_re_amnt()+" BDT");
        holder.fotext.setText(categoryItem.getItem_rebl_amnt()+" BDT");

    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public void filterList(ArrayList<ItemViewList> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }
}

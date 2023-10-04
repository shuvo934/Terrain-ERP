package ttit.com.shuvo.terrainerp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.arrayList.StockItemList;

public class StockItemAdapter extends RecyclerView.Adapter<StockItemAdapter.StockItemHolder>{

    private ArrayList<StockItemList> mCategoryItem;
    private final ClickedItem myClickedItem;
    private final Context myContext;


    public StockItemAdapter(ArrayList<StockItemList> categoryItems, Context context, ClickedItem cli) {
        this.mCategoryItem = categoryItems;
        this.myClickedItem = cli;
        this.myContext = context;
    }

    public class StockItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView ftext;
        public TextView stext;
        public TextView ttext;
        public TextView fotext;
        public TextView fifText;

        ClickedItem mClickedItem;

        public StockItemHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            ftext = itemView.findViewById(R.id.first_item_stock);
            stext = itemView.findViewById(R.id.second_item_stock);
            ttext = itemView.findViewById(R.id.third_item_stock);
            fotext = itemView.findViewById(R.id.fourth_item_stock);
            fifText = itemView.findViewById(R.id.fifth_item_stock);


            this.mClickedItem = ci;

            itemView.setOnClickListener(this::onClick);

        }

        @Override
        public void onClick(View v) {
            mClickedItem.onCategoryClicked(getAdapterPosition());
            Log.i("Category Name", mCategoryItem.get(getAdapterPosition()).getItemName());
        }
    }

    public interface ClickedItem {
        void onCategoryClicked(int CategoryPosition);
    }

    @NonNull
    @Override
    public StockItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.stock_item_list_view, parent, false);
        StockItemHolder categoryViewHolder = new StockItemHolder(view, myClickedItem);
        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StockItemHolder holder, int position) {

        StockItemList categoryItem = mCategoryItem.get(position);

        holder.ftext.setText(categoryItem.getItemName());
        holder.stext.setText(categoryItem.getUnit());
        holder.ttext.setText(categoryItem.getQty());
        if (categoryItem.getItemId().equals("Re-Order")) {
            holder.fotext.setText(categoryItem.getValue());
            holder.fotext.setTextSize(14);
            holder.fotext.setTextColor(Color.RED);
        } else {
            holder.fotext.setText(categoryItem.getValue()+" BDT");
        }
        holder.fifText.setText(categoryItem.getStock_qty());


    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public void filterList(ArrayList<StockItemList> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }

}

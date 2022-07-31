package ttit.com.shuvo.icomerp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.arrayList.SupplierList;

public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.SupplierHolder> {

    private ArrayList<SupplierList> mCategoryItem;
    private final ClickedItem myClickedItem;
    private final Context myContext;


    public SupplierAdapter(ArrayList<SupplierList> categoryItems, Context context, ClickedItem cli) {
        this.mCategoryItem = categoryItems;
        this.myClickedItem = cli;
        this.myContext = context;
    }

    public class SupplierHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;

        ClickedItem mClickedItem;

        public SupplierHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            name = itemView.findViewById(R.id.supplier_name);

            this.mClickedItem = ci;

            itemView.setOnClickListener(this::onClick);

        }

        @Override
        public void onClick(View v) {
            mClickedItem.onCategoryClicked(getAdapterPosition());
            Log.i("Category Name", mCategoryItem.get(getAdapterPosition()).getAd_name());
        }
    }

    public interface ClickedItem {
        void onCategoryClicked(int CategoryPosition);
    }

    @NonNull
    @Override
    public SupplierHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.supplier_item_view, parent, false);
        return new SupplierHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull SupplierHolder holder, int position) {

        SupplierList categoryItem = mCategoryItem.get(position);

        holder.name.setText(categoryItem.getAd_name());


    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

}

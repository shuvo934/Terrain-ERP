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
import ttit.com.shuvo.terrainerp.arrayList.ReceiveTypeList;

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.ACCHolder> {
    private ArrayList<ReceiveTypeList> mCategoryItem;
    private final ClickedItem myClickedItem;
    private final Context myContext;

    public AccountsAdapter(ArrayList<ReceiveTypeList> categoryItems, Context context, ClickedItem cli) {
        this.mCategoryItem = categoryItems;
        this.myClickedItem = cli;
        this.myContext = context;
    }

    public class ACCHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;

        ClickedItem mClickedItem;

        public ACCHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            name = itemView.findViewById(R.id.supplier_name);

            this.mClickedItem = ci;

            itemView.setOnClickListener(this::onClick);

        }

        @Override
        public void onClick(View v) {
            mClickedItem.onCategoryClicked(getAdapterPosition());
            Log.i("Category Name", mCategoryItem.get(getAdapterPosition()).getType());
        }
    }

    public interface ClickedItem {
        void onCategoryClicked(int CategoryPosition);
    }

    @NonNull
    @Override
    public ACCHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.supplier_item_view, parent, false);
        return new ACCHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ACCHolder holder, int position) {

        ReceiveTypeList categoryItem = mCategoryItem.get(position);

        holder.name.setText(categoryItem.getType());

    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public void filterList(ArrayList<ReceiveTypeList> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }
}

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
import ttit.com.shuvo.terrainerp.arrayList.UserSetupUserList;

public class UserSelectionAdapter extends RecyclerView.Adapter<UserSelectionAdapter.USHolder> {

    private ArrayList<UserSetupUserList> mCategoryItem;
    private final Context myContext;
    private final ClickedItem myClickedItem;

    public UserSelectionAdapter(ArrayList<UserSetupUserList> mCategoryItem, Context myContext, ClickedItem myClickedItem) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
        this.myClickedItem = myClickedItem;
    }

    @NonNull
    @Override
    public USHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.supplier_item_view, parent, false);
        return new USHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull USHolder holder, int position) {
        UserSetupUserList categoryItem = mCategoryItem.get(position);

        String fName = categoryItem.getUser_f_name();
        String lname = categoryItem.getUser_l_name();
        String u_name = categoryItem.getUser_name();
        String text = fName + " " + lname + " ("+u_name+")";
        holder.name.setText(text);
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public static class USHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;

        ClickedItem mClickedItem;

        public USHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            name = itemView.findViewById(R.id.supplier_name);

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

    public void filterList(ArrayList<UserSetupUserList> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }
}

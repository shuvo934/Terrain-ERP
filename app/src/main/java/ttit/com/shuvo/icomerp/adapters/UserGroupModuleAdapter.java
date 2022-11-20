package ttit.com.shuvo.icomerp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.arrayList.UserGroupModuleList;

public class UserGroupModuleAdapter extends RecyclerView.Adapter<UserGroupModuleAdapter.UGMHolder> {

    private final ArrayList<UserGroupModuleList> mCategoryItem;
    private final Context mContext;

    public UserGroupModuleAdapter(ArrayList<UserGroupModuleList> mCategoryItem, Context mContext) {
        this.mCategoryItem = mCategoryItem;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public UGMHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.choose_group_module_layout, parent, false);
        return new UGMHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UGMHolder holder, int position) {

        UserGroupModuleList userGroupModuleList = mCategoryItem.get(position);

        holder.checkBox.setChecked(userGroupModuleList.isActive());
        holder.checkBox.setText(userGroupModuleList.getMsma_name());

        if (userGroupModuleList.getMsma_id().equals("13")) {
            holder.checkBox.setEnabled(false);
            holder.checkBox.setTextColor(Color.parseColor("#95a5a6"));
            holder.linearLayout.setBackgroundColor(Color.BLACK);
            holder.linearLayout.setAlpha((float) 0.1);
        }
        else {
            holder.checkBox.setEnabled(true);
            holder.checkBox.setTextColor(Color.parseColor("#031A5F"));
            holder.linearLayout.setBackgroundColor(Color.WHITE);
            holder.linearLayout.setAlpha(1);
        }

    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public class UGMHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        LinearLayout linearLayout;

        public UGMHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.user_group_module_access_check_box);
            linearLayout = itemView.findViewById(R.id.layout_of_group_module_access);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (checkBox.isChecked()) {
                        mCategoryItem.get(getAdapterPosition()).setActive(true);
                        System.out.println("ACTIVE");
                    }
                    else {
                        mCategoryItem.get(getAdapterPosition()).setActive(false);
                        System.out.println("NOT ACTIVE");
                    }
                }
            });
        }
    }
}

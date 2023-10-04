package ttit.com.shuvo.terrainerp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.arrayList.UserGroupList;
import ttit.com.shuvo.terrainerp.arrayList.UserGroupModuleList;
import ttit.com.shuvo.terrainerp.dialogues.UserGroupModuleDialogue;

public class UserGroupAdapter extends RecyclerView.Adapter<UserGroupAdapter.UGHolder> {

    private final ArrayList<UserGroupList> mCategoryItem;
    private Context mContext;
    public static ArrayList<UserGroupModuleList> groupModuleLists;
    public static int selectedPosition;

    public UserGroupAdapter(ArrayList<UserGroupList> mCategoryItem, Context mContext) {
        this.mCategoryItem = mCategoryItem;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public UGHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_group_access_layout, parent, false);
        return new UGHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UGHolder holder, int position) {

        UserGroupList userGroupList = mCategoryItem.get(position);

        holder.checkBox.setChecked(userGroupList.isActivated());
        holder.groupName.setText(userGroupList.getGroup_name());
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public class UGHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        TextView groupName;
        MaterialCardView details;

        public UGHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.user_group_access_check_box);
            groupName = itemView.findViewById(R.id.name_of_the_group);
            details = itemView.findViewById(R.id.group_access_details);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkBox.isChecked()) {
                        System.out.println("ACTIVATED");
                        mCategoryItem.get(getAdapterPosition()).setActivated(true);
                        ArrayList<UserGroupModuleList> userGroupModuleLists = mCategoryItem.get(getAdapterPosition()).getUserGroupModuleLists();

                        for (int i = 0 ; i < userGroupModuleLists.size(); i++) {
                            userGroupModuleLists.get(i).setActive(true);
                        }
                    }
                    else {
                        System.out.println("NOT ACTIVE");
                        mCategoryItem.get(getAdapterPosition()).setActivated(false);
                        ArrayList<UserGroupModuleList> userGroupModuleLists = mCategoryItem.get(getAdapterPosition()).getUserGroupModuleLists();

                        for (int i = 0 ; i < userGroupModuleLists.size(); i++) {
                            userGroupModuleLists.get(i).setActive(false);
                        }
                    }
                }
            });

            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    groupModuleLists = new ArrayList<>();
                    groupModuleLists = mCategoryItem.get(getAdapterPosition()).getUserGroupModuleLists();
                    selectedPosition = getAdapterPosition();
                    System.out.println(selectedPosition);

                    if (checkBox.isChecked()) {
                        System.out.println("ACTIVATED");
                        UserGroupModuleDialogue userGroupModuleDialogue = new UserGroupModuleDialogue();
                        userGroupModuleDialogue.show(activity.getSupportFragmentManager(),"UGMD");

                    }
                    else {
                        System.out.println("NOT ACTIVE");
                        Toast.makeText(mContext,"This Group is not selected.",Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

}

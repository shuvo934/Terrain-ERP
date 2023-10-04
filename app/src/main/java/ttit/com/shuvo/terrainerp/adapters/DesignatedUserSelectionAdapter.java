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
import ttit.com.shuvo.terrainerp.arrayList.DesignatedUserList;

public class DesignatedUserSelectionAdapter extends RecyclerView.Adapter<DesignatedUserSelectionAdapter.DUSHolder>{
    private ArrayList<DesignatedUserList> mCategoryItem;
    private final Context myContext;
    private final ClickedItem myClickedItem;

    public DesignatedUserSelectionAdapter(ArrayList<DesignatedUserList> mCategoryItem, Context myContext, ClickedItem myClickedItem) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
        this.myClickedItem = myClickedItem;
    }

    @NonNull
    @Override
    public DUSHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.designated_user_select_details_view, parent, false);
        return new DUSHolder(view,myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull DUSHolder holder, int position) {

        DesignatedUserList categoryItem = mCategoryItem.get(position);

        holder.userName.setText(categoryItem.getUser_name());
        holder.userEmail.setText(categoryItem.getUsr_email());
        holder.userContact.setText(categoryItem.getUsr_contact());
        holder.empName.setText(categoryItem.getEmp_name());
        holder.empCode.setText(categoryItem.getEmp_code());
        holder.userTitle.setText(categoryItem.getJob_calling_title());
        holder.userDep.setText(categoryItem.getDept_name());
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public class DUSHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView userName;
        public TextView userEmail;
        public TextView userContact;
        public TextView empName;
        public TextView empCode;
        public TextView userTitle;
        public TextView userDep;
        ClickedItem mClickedItem;

        public DUSHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name_designated_user_select);
            userEmail = itemView.findViewById(R.id.email_designated_user_select);
            userContact = itemView.findViewById(R.id.contact_designated_user_select);
            empName = itemView.findViewById(R.id.emp_name_designated_user_select);
            empCode = itemView.findViewById(R.id.emp_code_designated_user_select);
            userTitle = itemView.findViewById(R.id.designation_designated_user_select);
            userDep = itemView.findViewById(R.id.department_designated_user_select);
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

    public void filterList(ArrayList<DesignatedUserList> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }
}

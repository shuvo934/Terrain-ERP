package ttit.com.shuvo.icomerp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.arrayList.PartyContactList;

public class PartyContactAdapter extends RecyclerView.Adapter<PartyContactAdapter.PCAHolder> {

    private final ArrayList<PartyContactList> mCategoryItem;
    private final Context myContext;

    public PartyContactAdapter(ArrayList<PartyContactList> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }
    @NonNull
    @Override
    public PCAHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.contact_item_view, parent, false);
        return new PCAHolder(view);
    }

    public static class PCAHolder extends RecyclerView.ViewHolder {

        public TextView contactPerson;
        public TextView desg;
        public TextView dept;
        public TextView conNo;
        public TextView emAdd;

        public PCAHolder(@NonNull View itemView) {
            super(itemView);

            contactPerson = itemView.findViewById(R.id.contact_person_pal);
            desg = itemView.findViewById(R.id.designation_pal);
            dept = itemView.findViewById(R.id.department_pal);
            conNo = itemView.findViewById(R.id.contact_no_pal);
            emAdd = itemView.findViewById(R.id.contact_email_pal);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull PCAHolder holder, int position) {

        PartyContactList partyContactList = mCategoryItem.get(position);

        holder.contactPerson.setText(partyContactList.getContactPerson());
        holder.desg.setText(partyContactList.getDesignation());
        holder.dept.setText(partyContactList.getDepartment());
        holder.conNo.setText(partyContactList.getContactNo());
        holder.emAdd.setText(partyContactList.getEmailAddress());

    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }
}

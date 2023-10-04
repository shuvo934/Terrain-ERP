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
import ttit.com.shuvo.terrainerp.arrayList.PartyFactoryList;

public class PartyFactoryAdapter extends RecyclerView.Adapter<PartyFactoryAdapter.PFAHolder> {

    private final ArrayList<PartyFactoryList> mCategoryItem;
    private final Context myContext;

    public PartyFactoryAdapter(ArrayList<PartyFactoryList> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }
    @NonNull
    @Override
    public PFAHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.factory_item_view, parent, false);
        return new PFAHolder(view);
    }

    public static class PFAHolder extends RecyclerView.ViewHolder {

        public TextView factoryName;
        public TextView facAdd;
        public TextView facCon;

        public PFAHolder(@NonNull View itemView) {
            super(itemView);

            factoryName = itemView.findViewById(R.id.factory_name_pal);
            facAdd = itemView.findViewById(R.id.factory_address_pal);
            facCon = itemView.findViewById(R.id.factory_contact_pal);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull PFAHolder holder, int position) {

        PartyFactoryList partyFactoryList = mCategoryItem.get(position);

        holder.factoryName.setText(partyFactoryList.getFactoryName());
        holder.facAdd.setText(partyFactoryList.getFactoryAddress());
        holder.facCon.setText(partyFactoryList.getFactoryContact());


    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }
}

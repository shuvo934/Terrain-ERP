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
import ttit.com.shuvo.terrainerp.arrayList.AllPartyLists;

public class AllPartyAdapter extends RecyclerView.Adapter<AllPartyAdapter.APAHolder> {

    private ArrayList<AllPartyLists> mCategoryItem;
    private final Context myContext;
    private final ClickedItem myClickedItem;

    public AllPartyAdapter(ArrayList<AllPartyLists> categoryItems, Context context, ClickedItem myClickedItem) {
        this.mCategoryItem = categoryItems;
        this.myContext = context;
        this.myClickedItem = myClickedItem;
    }

    public static class APAHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView sNo;
        public TextView ad_name;
        public TextView ad_code;
        public TextView ad_address;
        public TextView ad_email;
        public TextView ad_phone;

        ClickedItem mClickedItem;

        public APAHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            sNo = itemView.findViewById(R.id.item_serial_pal);
            ad_name = itemView.findViewById(R.id.name_pal);
            ad_code = itemView.findViewById(R.id.a_c_no_pal);
            ad_address = itemView.findViewById(R.id.address_pal);
            ad_email = itemView.findViewById(R.id.email_pal);
            ad_phone = itemView.findViewById(R.id.phone_pal);

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

    @NonNull
    @Override
    public APAHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.party_view_list, parent, false);
        return new APAHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull APAHolder holder, int position) {

        AllPartyLists categoryItem = mCategoryItem.get(position);

        holder.sNo.setText(categoryItem.getsNo());
        holder.ad_name.setText(categoryItem.getAd_name());
        holder.ad_code.setText(categoryItem.getAd_code());
        holder.ad_address.setText(categoryItem.getAd_address());
        holder.ad_email.setText(categoryItem.getAd_email());
        holder.ad_phone.setText(categoryItem.getAd_phone());
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public void filterList(ArrayList<AllPartyLists> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }
}

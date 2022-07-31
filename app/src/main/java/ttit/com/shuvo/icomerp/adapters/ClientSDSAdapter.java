package ttit.com.shuvo.icomerp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.arrayList.ClientSDSList;
import ttit.com.shuvo.icomerp.arrayList.ItemRcvIssItemDescList;
import ttit.com.shuvo.icomerp.arrayList.ItemRcvIssReglist;
import ttit.com.shuvo.icomerp.arrayList.OrderSDSList;

import static ttit.com.shuvo.icomerp.fragments.SalesOrderDeliverySummary.sods_selected_position;

public class ClientSDSAdapter extends RecyclerView.Adapter<ClientSDSAdapter.ClientHolder> {

    private ArrayList<ClientSDSList> mCategoryItem;
    private final Context myContext;
    private final ClickedItem myClickedItem;
    public OrderSDSAdapter orderSDSAdapter;

    public static int positionFromFirstAdapter = -1;

    public ClientSDSAdapter(ArrayList<ClientSDSList> categoryItems, Context context, ClickedItem myClickedItem) {
        this.mCategoryItem = categoryItems;
        this.myContext = context;
        this.myClickedItem = myClickedItem;

    }

    public class ClientHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView clients;


        public RecyclerView itemDetails;
        ClickedItem mClickedItem;

        public ClientHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            clients = itemView.findViewById(R.id.client_name_sds);

            itemDetails = itemView.findViewById(R.id.order_no_view_sds);
            this.mClickedItem = ci;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mClickedItem.onCategoryClicked(getAdapterPosition());
            Log.i("Client Name", mCategoryItem.get(getAdapterPosition()).getClientName());
        }

    }
    public interface ClickedItem {
        void onCategoryClicked(int CategoryPosition);
    }

    @NonNull
    @Override
    public ClientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.clientname_sales_deliver_sum, parent, false);
        return new ClientHolder(view,myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientHolder holder, int position) {

        ClientSDSList categoryItem = mCategoryItem.get(position);

        positionFromFirstAdapter = position;


        holder.clients.setText(categoryItem.getClientName());

        ArrayList<OrderSDSList> orderSDSLists = categoryItem.getOrderSDSLists();

        holder.itemDetails.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(myContext);
        holder.itemDetails.setLayoutManager(layoutManager);
        orderSDSAdapter = new OrderSDSAdapter(orderSDSLists,myContext);
        holder.itemDetails.setAdapter(orderSDSAdapter);

        if(sods_selected_position == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#81ecec"));
        }
        else {
            holder.itemView.setBackgroundColor(Color.parseColor("#f5f6fa"));
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sods_selected_position == position) {
                    sods_selected_position = -1;
                } else {
                    sods_selected_position=position;
                }

                notifyDataSetChanged();

            }
        });


    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

}

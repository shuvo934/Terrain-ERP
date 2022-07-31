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
import ttit.com.shuvo.icomerp.arrayList.ItemRcvIssItemDescList;
import ttit.com.shuvo.icomerp.arrayList.ItemRcvIssReglist;
import ttit.com.shuvo.icomerp.arrayList.SalesOrderList;

public class SalesOrderAdapter extends RecyclerView.Adapter<SalesOrderAdapter.SalesorderHolder> {

    private ArrayList<SalesOrderList> mCategoryItem;
    private final ClickedItem myClickedItem;
    private final Context myContext;

    public SalesOrderAdapter(ArrayList<SalesOrderList> mCategoryItem, Context myContext, ClickedItem myClickedItem) {
        this.mCategoryItem = mCategoryItem;
        this.myClickedItem = myClickedItem;
        this.myContext = myContext;
    }

    public class SalesorderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView clientName;
        public TextView orderNo;
        public TextView orderDate;
        public TextView orderType;
        public TextView edd;
        public TextView discountAmnt;
        public TextView vatAmnt;
        public TextView orderAmnt;
        public TextView advanceAmnt;

        ClickedItem mClickedItem;

        public SalesorderHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            clientName = itemView.findViewById(R.id.client_name_so);
            orderNo = itemView.findViewById(R.id.order_no_so);
            orderDate = itemView.findViewById(R.id.order_date_so);
            orderType = itemView.findViewById(R.id.order_type_so);
            edd = itemView.findViewById(R.id.edd_so);
            discountAmnt = itemView.findViewById(R.id.discount_amnt_so);
            vatAmnt = itemView.findViewById(R.id.vat_amnt_so);
            orderAmnt = itemView.findViewById(R.id.order_amnt_so);
            advanceAmnt = itemView.findViewById(R.id.advance_amnt_so);

            this.mClickedItem = ci;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mClickedItem.onCategoryClicked(getAdapterPosition());
            Log.i("Client Name", mCategoryItem.get(getAdapterPosition()).getAd_name());
        }
    }

    public interface ClickedItem {
        void onCategoryClicked(int CategoryPosition);
    }

    @NonNull
    @Override
    public SalesorderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.sales_order_list_view, parent, false);
        return new SalesorderHolder(view, myClickedItem);
    }
    @Override
    public void onBindViewHolder(@NonNull SalesorderHolder holder, int position) {

        SalesOrderList categoryItem = mCategoryItem.get(position);


        holder.clientName.setText(categoryItem.getAd_name());
        holder.orderNo.setText(categoryItem.getOrder_no());
        holder.orderDate.setText(categoryItem.getOrder_date());
        holder.orderType.setText(categoryItem.getOrder_type());
        holder.edd.setText(categoryItem.getEdd());
        holder.discountAmnt.setText(categoryItem.getDiscount_amnt());
        holder.vatAmnt.setText(categoryItem.getVat_amnt());
        holder.advanceAmnt.setText(categoryItem.getAdvance_amnt());
        holder.orderAmnt.setText(categoryItem.getTotal_order_amnt());

//        ArrayList<ItemRcvIssItemDescList> itemRcvIssItemDescLists = categoryItem.getItemRcvIssItemDescLists();
//
//        holder.itemDetails.setHasFixedSize(true);
//
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(myContext);
//        holder.itemDetails.setLayoutManager(layoutManager);
//        itemRcvIssItemDescAdapter = new ItemRcvIssItemDescAdapter(itemRcvIssItemDescLists,myContext);
//        holder.itemDetails.setAdapter(itemRcvIssItemDescAdapter);
//
//        if(selectedPosition == position) {
//            holder.itemView.setBackgroundColor(Color.parseColor("#81ecec"));
//        }
//        else {
//            holder.itemView.setBackgroundColor(Color.parseColor("#f5f6fa"));
//        }
//
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (selectedPosition == position) {
//                    selectedPosition = -1;
//                } else {
//                    selectedPosition=position;
//                }
//
//                notifyDataSetChanged();
//
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }


}

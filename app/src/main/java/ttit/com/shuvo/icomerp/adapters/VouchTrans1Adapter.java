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
import ttit.com.shuvo.icomerp.arrayList.VoucherLists1;
import ttit.com.shuvo.icomerp.arrayList.VoucherLists2;

import static ttit.com.shuvo.icomerp.fragments.SalesOrderDeliverySummary.sods_selected_position;

public class VouchTrans1Adapter extends RecyclerView.Adapter<VouchTrans1Adapter.VT1Holder> {

    private ArrayList<VoucherLists1> mCategoryItem;
    private final Context myContext;
    private final ClickedItem myClickedItem;
    public VouchTrans2Adapter vouchTrans2Adapter;

    public VouchTrans1Adapter(ArrayList<VoucherLists1> categoryItems, Context context, ClickedItem myClickedItem) {
        this.mCategoryItem = categoryItems;
        this.myContext = context;
        this.myClickedItem = myClickedItem;

    }

    public class VT1Holder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView vDate;

        public RecyclerView itemDetails;
        ClickedItem mClickedItem;

        public VT1Holder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            vDate = itemView.findViewById(R.id.v_trans_date);

            itemDetails = itemView.findViewById(R.id.v_trans_report_view2);
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
    public VT1Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.v_trans_view_1, parent, false);
        return new VT1Holder(view,myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull VT1Holder holder, int position) {

        VoucherLists1 categoryItem = mCategoryItem.get(position);

        //positionFromFirstAdapter = position;


        holder.vDate.setText(categoryItem.getvDate());

        ArrayList<VoucherLists2> voucherLists2s = categoryItem.getVoucherLists2s();

        holder.itemDetails.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(myContext);
        holder.itemDetails.setLayoutManager(layoutManager);
        vouchTrans2Adapter = new VouchTrans2Adapter(voucherLists2s,myContext);
        holder.itemDetails.setAdapter(vouchTrans2Adapter);

//        if(sods_selected_position == position) {
//            holder.itemView.setBackgroundColor(Color.parseColor("#81ecec"));
//        }
//        else {
//            holder.itemView.setBackgroundColor(Color.parseColor("#f5f6fa"));
//        }


//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (sods_selected_position == position) {
//                    sods_selected_position = -1;
//                } else {
//                    sods_selected_position=position;
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

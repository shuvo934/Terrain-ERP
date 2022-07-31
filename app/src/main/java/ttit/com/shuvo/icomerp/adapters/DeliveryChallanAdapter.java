package ttit.com.shuvo.icomerp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.arrayList.DeliveryChallanList;
import ttit.com.shuvo.icomerp.arrayList.SalesOrderList;

public class DeliveryChallanAdapter extends RecyclerView.Adapter<DeliveryChallanAdapter.DeliveryHolder> {

    private ArrayList<DeliveryChallanList> mCategoryItem;
    private final ClickedItem myClickedItem;
    private final Context myContext;

    public DeliveryChallanAdapter(ArrayList<DeliveryChallanList> mCategoryItem, Context myContext, ClickedItem myClickedItem) {
        this.mCategoryItem = mCategoryItem;
        this.myClickedItem = myClickedItem;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public DeliveryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.delivery_challan_list_view, parent, false);
        return new DeliveryHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryHolder holder, int position) {

        DeliveryChallanList deliveryChallanList = mCategoryItem.get(position);

        holder.challanNo.setText(deliveryChallanList.getDelivery_no());
        holder.challanDate.setText(deliveryChallanList.getDelivery_date());
        holder.clientName.setText(deliveryChallanList.getClient_name());
        holder.salesType.setText(deliveryChallanList.getOrder_type());
        holder.orderNo.setText(deliveryChallanList.getOrder_no());
        holder.orderDate.setText(deliveryChallanList.getOrder_date());
        holder.invAmnt.setText(deliveryChallanList.getInvoice_amnt());
        holder.vatAmntInv.setText(deliveryChallanList.getVat_amnt());
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public class DeliveryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView challanNo;
        public TextView challanDate;
        public TextView clientName;
        public TextView salesType;
        public TextView orderNo;
        public TextView orderDate;
        public TextView invAmnt;
        public TextView vatAmntInv;

        ClickedItem mClickedItem;

        public DeliveryHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            challanNo = itemView.findViewById(R.id.challan_no_dc);
            challanDate = itemView.findViewById(R.id.challan_date_dc);
            clientName = itemView.findViewById(R.id.client_name_dc);
            salesType = itemView.findViewById(R.id.sales_type_dc);
            orderDate = itemView.findViewById(R.id.order_date_dc);
            orderNo = itemView.findViewById(R.id.order_no_dc);
            invAmnt = itemView.findViewById(R.id.invoice_amnt_dc);
            vatAmntInv = itemView.findViewById(R.id.vat_amnt_inv_dc);

            this.mClickedItem = ci;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mClickedItem.onCategoryClicked(getAdapterPosition());
            Log.i("Client Name", mCategoryItem.get(getAdapterPosition()).getClient_name());
        }
    }

    public interface ClickedItem {
        void onCategoryClicked(int CategoryPosition);
    }


}

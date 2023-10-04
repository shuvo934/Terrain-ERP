package ttit.com.shuvo.terrainerp.adapters;

import static ttit.com.shuvo.terrainerp.fragments.PurchaseOrder.itemCardView;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseOrder.itemUpdate;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseOrder.prCardView;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseOrder.prm_id_in_po;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseOrder.purchaseOrderSelectedRequisitionLists;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseOrder.reqItemHsv;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseOrder.reqItemMissing;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseOrder.selectedRequisition;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.arrayList.PurchaseOrderSelectedRequisitionLists;
import ttit.com.shuvo.terrainerp.fragments.PurchaseOrder;

public class PurchaseOrderReqSelectedAdapter extends RecyclerView.Adapter<PurchaseOrderReqSelectedAdapter.PORSAHolder>{

    private ArrayList<PurchaseOrderSelectedRequisitionLists> mCategoryItem;
    private final Context myContext;
    private final ClickedItem myClickedItem;

    public PurchaseOrderReqSelectedAdapter(ArrayList<PurchaseOrderSelectedRequisitionLists> mCategoryItem, Context myContext, ClickedItem myClickedItem) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
        this.myClickedItem = myClickedItem;
    }

    @NonNull
    @Override
    public PORSAHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.pr_selected_po_view, parent, false);
        return new PORSAHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull PORSAHolder holder, int position) {
        PurchaseOrderSelectedRequisitionLists selectedItemLists = mCategoryItem.get(position);

        holder.reqNo.setText(selectedItemLists.getPrm_req_no());
        holder.catName.setText(selectedItemLists.getIm_name());

        if(PurchaseOrder.selectedRequisition == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#81ecec"));
        }
        else {
            holder.itemView.setBackgroundColor(Color.parseColor("#f5f6fa"));
        }
        if (selectedItemLists.isCanDelete()) {
            holder.deleteItem.setVisibility(View.VISIBLE);
        }
        else {
            holder.deleteItem.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public class PORSAHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView reqNo;
        public TextView catName;
        public ImageView deleteItem;
        ClickedItem mClickedItem;

        public PORSAHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            reqNo = itemView.findViewById(R.id.req_no_selected_pr_for_po);
            catName = itemView.findViewById(R.id.category_selected_pr_for_po);
            deleteItem = itemView.findViewById(R.id.delete_selected_pr_for_po);
            this.mClickedItem = ci;

            itemView.setOnClickListener(this);

            deleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(myContext);
                    builder.setTitle("Remove Requisition!")
                            .setMessage("Are you want to remove this requisition?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    purchaseOrderSelectedRequisitionLists.remove(getAdapterPosition());

                                    if (purchaseOrderSelectedRequisitionLists.size() == 0) {
                                        prCardView.setVisibility(View.GONE);
                                        itemCardView.setVisibility(View.GONE);
                                        reqItemHsv.setVisibility(View.GONE);
                                        reqItemMissing.setVisibility(View.GONE);
                                    }
                                    if (purchaseOrderSelectedRequisitionLists.size() != 0) {
                                        selectedRequisition = 0;
                                        prm_id_in_po = purchaseOrderSelectedRequisitionLists.get(0).getPrm_id();
                                        itemUpdate(prm_id_in_po);
                                    }

                                    notifyDataSetChanged();

                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });

        }

        @Override
        public void onClick(View v) {
            mClickedItem.onReqClicked(getAdapterPosition());
            //Log.i("Name", mCategoryItem.get(getAdapterPosition()).getName());
        }
    }

    public interface ClickedItem {
        void onReqClicked(int Position);
    }
}

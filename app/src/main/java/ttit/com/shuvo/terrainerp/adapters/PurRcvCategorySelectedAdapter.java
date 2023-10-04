package ttit.com.shuvo.terrainerp.adapters;

import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.catItemHsv;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.catItemMissing;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.catItemUpdate;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.im_id_in_p_rcv;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.prcvCatCardView;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.prcvItemCardView;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.prcvWareCardView;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.purchaseReceiveAllSelectedLists;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.selectedCategoryPosition;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.selectedItemPosition;

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
import ttit.com.shuvo.terrainerp.arrayList.PurchaseReceiveAllSelectedLists;

public class PurRcvCategorySelectedAdapter extends RecyclerView.Adapter<PurRcvCategorySelectedAdapter.PRCASHolder> {
    private ArrayList<PurchaseReceiveAllSelectedLists> mCategoryItem;
    private final Context myContext;
    private final ClickedItem myClickedItem;

    public PurRcvCategorySelectedAdapter(ArrayList<PurchaseReceiveAllSelectedLists> mCategoryItem, Context myContext, ClickedItem myClickedItem) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
        this.myClickedItem = myClickedItem;
    }

    @NonNull
    @Override
    public PRCASHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.cat_selected_p_rcv_view, parent, false);
        return new PRCASHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull PRCASHolder holder, int position) {
        PurchaseReceiveAllSelectedLists lists = mCategoryItem.get(position);

        holder.catName.setText(lists.getCat_name());

        if(selectedCategoryPosition == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#81ecec"));
        }
        else {
            holder.itemView.setBackgroundColor(Color.parseColor("#f5f6fa"));
        }

    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public class PRCASHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView catName;
        public ImageView deleteItem;
        ClickedItem mClickedItem;

        public PRCASHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            catName = itemView.findViewById(R.id.category_selected_for_p_rcv);
            deleteItem = itemView.findViewById(R.id.delete_category_selected_for_p_rcv);
            this.mClickedItem = ci;

            itemView.setOnClickListener(this);

            deleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(myContext);
                    builder.setTitle("Remove Category!")
                            .setMessage("Are you want to remove this Category?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    purchaseReceiveAllSelectedLists.remove(getAdapterPosition());

                                    if (purchaseReceiveAllSelectedLists.size() == 0) {
                                        prcvCatCardView.setVisibility(View.GONE);
                                        prcvItemCardView.setVisibility(View.GONE);
                                        prcvWareCardView.setVisibility(View.GONE);
                                        catItemHsv.setVisibility(View.GONE);
                                        catItemMissing.setVisibility(View.GONE);
                                        selectedCategoryPosition = 0;
                                        selectedItemPosition = 0;
                                    }
                                    if (purchaseReceiveAllSelectedLists.size() != 0) {
                                        selectedCategoryPosition = 0;
                                        selectedItemPosition = 0;
                                        im_id_in_p_rcv = purchaseReceiveAllSelectedLists.get(0).getCat_id();
                                        catItemUpdate(im_id_in_p_rcv);
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
            mClickedItem.onCatClicked(getAdapterPosition());
        }
    }

    public interface ClickedItem {
        void onCatClicked(int Position);
    }
}

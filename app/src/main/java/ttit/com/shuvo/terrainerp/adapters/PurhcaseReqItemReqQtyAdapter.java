package ttit.com.shuvo.terrainerp.adapters;

import static ttit.com.shuvo.terrainerp.fragments.PurchaseRequisition.categoryLay;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseRequisition.itemSelectLay;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseRequisition.prSelectedItemLists;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseRequisition.requisition_date;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseRequisition.totalReqQty;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseRequisition.total_req_qty_pr;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.arrayList.PRSelectedItemLists;
import ttit.com.shuvo.terrainerp.dialogues.SingleItemShow;

public class PurhcaseReqItemReqQtyAdapter extends RecyclerView.Adapter<PurhcaseReqItemReqQtyAdapter.PRIRQHolder> {
    private ArrayList<PRSelectedItemLists> mCategoryItem;
    private final Context myContext;

    String searchingName = "";
    boolean textChanged = false;

    public PurhcaseReqItemReqQtyAdapter(ArrayList<PRSelectedItemLists> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public PRIRQHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.purchase_req_selected_item_req_qty_view, parent, false);
        return new PRIRQHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PRIRQHolder holder, int position) {
        PRSelectedItemLists selectedItemLists = mCategoryItem.get(position);

        holder.item_Name.setText(selectedItemLists.getItem_reference_name());
        holder.itemUnit.setText(selectedItemLists.getItem_unit());
        if (textChanged) {
            textChanged = false;
        }
        holder.reqQty.setText(selectedItemLists.getReqQty());
        if (!textChanged) {
            textChanged = true;
        }

        System.out.println(textChanged);
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public class PRIRQHolder extends RecyclerView.ViewHolder {
        public TextView item_Name;
        public EditText reqQty;
        public TextView itemUnit;
        public ImageView deleteItem;
        public PRIRQHolder(@NonNull View itemView) {
            super(itemView);
            item_Name = itemView.findViewById(R.id.item_name_pr_item_selection_req);
            reqQty = itemView.findViewById(R.id.req_qty_pr_item_selection_req);
            itemUnit = itemView.findViewById(R.id.item_unit_pr_item_selection_req);
            deleteItem = itemView.findViewById(R.id.delete_item_pr_item_selection_req);

            reqQty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        System.out.println("Focus ase");
                        System.out.println(getAdapterPosition() + " no position e");
                        if (getAdapterPosition() < 0) {
                            v.clearFocus();
                            closeKeyBoard();
                        }
                    }
                    else {
                        System.out.println("Focus nai");
                        System.out.println(getAdapterPosition() + " no position e");
                        if (textChanged) {
                            searchingName = reqQty.getText().toString();
                            total_req_qty_pr = 0;
                            if (!searchingName.isEmpty()) {
                                mCategoryItem.get(getAdapterPosition()).setReqQty(searchingName);
                                textChanged = false;
                                notifyDataSetChanged();
                            }



                            if (prSelectedItemLists.size() != 0) {
                                for (int i = 0; i < prSelectedItemLists.size() ; i++) {
                                    if (prSelectedItemLists.get(i).getReqQty() != null) {
                                        if (!prSelectedItemLists.get(i).getReqQty().isEmpty()) {
                                            total_req_qty_pr = total_req_qty_pr + Integer.parseInt(prSelectedItemLists.get(i).getReqQty());
                                        }

                                    }
                                }
                            }
                            System.out.println(total_req_qty_pr);
                            totalReqQty.setText(String.valueOf(total_req_qty_pr));
                        }

                    }
                }
            });

            reqQty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                            actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                            event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT ) {
                        if (event == null || !event.isShiftPressed()) {
                            // the user is done typing.
                            Log.i("Let see", "Come here");
                            reqQty.clearFocus();
                            closeKeyBoard();

                            return false; // consume.
                        }
                        else if (event.getAction() == KeyEvent.ACTION_UP || event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                            System.out.println("ON EDITOR BACK PRESSED");
                            return false; // consume.
                        }
                    }
                    return false;
                }
            });

            deleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(myContext);
                    builder.setTitle("Delete Item!")
                            .setMessage("Are you want to remove this item?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    prSelectedItemLists.remove(getAdapterPosition());

                                    total_req_qty_pr = 0;
                                    if (prSelectedItemLists.size() != 0) {
                                        for (int i = 0; i < prSelectedItemLists.size() ; i++) {
                                            if (prSelectedItemLists.get(i).getReqQty() != null) {
                                                if (!prSelectedItemLists.get(i).getReqQty().isEmpty()) {
                                                    total_req_qty_pr = total_req_qty_pr + Integer.parseInt(prSelectedItemLists.get(i).getReqQty());
                                                }

                                            }
                                        }
                                    }
                                    if (prSelectedItemLists.size() == 0) {
                                        itemSelectLay.setVisibility(View.GONE);
                                        categoryLay.setEnabled(true);
                                    }
                                    System.out.println(total_req_qty_pr);
                                    totalReqQty.setText(String.valueOf(total_req_qty_pr));

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

            item_Name.setOnClickListener(v -> {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                String cat_id = mCategoryItem.get(getAdapterPosition()).getIm_id();
                String item_id = mCategoryItem.get(getAdapterPosition()).getItem_id();

                SingleItemShow singleItemShow = new SingleItemShow(cat_id,requisition_date,item_id,myContext);
                singleItemShow.show(activity.getSupportFragmentManager(),"SIS");
            });
        }
    }

    private void closeKeyBoard() {
        AppCompatActivity activity = (AppCompatActivity) myContext;
        View view = activity.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

package ttit.com.shuvo.terrainerp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.arrayList.PurchaseReqAppQtyLists;

import static ttit.com.shuvo.terrainerp.fragments.PurchaseReqApproved.totalAppReqQty;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReqApproved.total_app_qty;

public class PurchaseReqAppQtyAdapter extends RecyclerView.Adapter<PurchaseReqAppQtyAdapter.PRAQHolder> {

    private ArrayList<PurchaseReqAppQtyLists> mCategoryItem;
    private final Context myContext;

    String searchingName = "";
    boolean textChanged = false;

    public PurchaseReqAppQtyAdapter(ArrayList<PurchaseReqAppQtyLists> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    public class PRAQHolder extends RecyclerView.ViewHolder {

        public TextView item_Name;
        public TextView reqQty;
        public EditText appQty;

        public PRAQHolder(@NonNull View itemView) {
            super(itemView);

            item_Name = itemView.findViewById(R.id.item_name_praq);
            reqQty = itemView.findViewById(R.id.req_qty_praq);
            appQty = itemView.findViewById(R.id.app_qty_praq);


            appQty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        System.out.println("Focus ase");
                        System.out.println(getAdapterPosition() + " no position e");
                        if (getAdapterPosition() < 0) {
                            v.clearFocus();
                            closeKeyBoard();
                        }
                    } else {
                        System.out.println("Focus nai");
                        System.out.println(getAdapterPosition() + " no position e");
                        if (textChanged) {
                            searchingName = appQty.getText().toString();
                            total_app_qty = 0;

                            int qty = 0;
                            if (mCategoryItem.get(getAdapterPosition()).getReqQty() != null) {
                                if (!mCategoryItem.get(getAdapterPosition()).getReqQty().isEmpty()) {
                                    qty = Integer.parseInt(mCategoryItem.get(getAdapterPosition()).getReqQty());
                                }
                            }

                            int approvedQty = 0;
                            if (searchingName != null) {
                                if (!searchingName.isEmpty()) {
                                    approvedQty = Integer.parseInt(searchingName);
                                }
                            }

                            if (approvedQty > qty) {
                                Toast.makeText(myContext, "Approved Quantity is greater than Requisition Quantity",Toast.LENGTH_SHORT).show();
                                appQty.setText(mCategoryItem.get(getAdapterPosition()).getApprovedQty());
                            } else {
                                mCategoryItem.get(getAdapterPosition()).setApprovedQty(searchingName);
                                textChanged = false;
                                notifyDataSetChanged();
                            }



                            if (mCategoryItem.size() != 0) {
                                for (int i = 0; i < mCategoryItem.size() ; i++) {
                                    if (mCategoryItem.get(i).getApprovedQty() != null) {
                                        if (!mCategoryItem.get(i).getApprovedQty().isEmpty()) {
                                            total_app_qty = total_app_qty + Integer.parseInt(mCategoryItem.get(i).getApprovedQty());
                                        }

                                    }
                                }
                            }
                            System.out.println(total_app_qty);
                            totalAppReqQty.setText(String.valueOf(total_app_qty));
//                            appQty.clearFocus();
//                            closeKeyBoard();
                        }

                    }
                }
            });
            appQty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                            actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                            event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT ) {
                        if (event == null || !event.isShiftPressed()) {
                            // the user is done typing.
                            Log.i("Let see", "Come here");

//                            searchingName = appQty.getText().toString();
//                            total_app_qty = 0;
//
//                            int qty = 0;
//                            if (mCategoryItem.get(getAdapterPosition()).getReqQty() != null) {
//                                if (!mCategoryItem.get(getAdapterPosition()).getReqQty().isEmpty()) {
//                                    qty = Integer.parseInt(mCategoryItem.get(getAdapterPosition()).getReqQty());
//                                }
//                            }
//
//                            int approvedQty = 0;
//                            if (searchingName != null) {
//                                if (!searchingName.isEmpty()) {
//                                    approvedQty = Integer.parseInt(searchingName);
//                                }
//                            }
//
//                            if (approvedQty > qty) {
//                                Toast.makeText(myContext, "Approved Quantity is greater than Requisition Quantity",Toast.LENGTH_SHORT).show();
//                            } else {
//                                mCategoryItem.get(getAdapterPosition()).setApprovedQty(searchingName);
//                                textChanged = false;
//                                notifyDataSetChanged();
//                            }
//
//                            if (mCategoryItem.size() != 0) {
//                                for (int i = 0; i < mCategoryItem.size() ; i++) {
//                                    if (mCategoryItem.get(i).getApprovedQty() != null) {
//                                        if (!mCategoryItem.get(i).getApprovedQty().isEmpty()) {
//                                            total_app_qty = total_app_qty + Integer.parseInt(mCategoryItem.get(i).getApprovedQty());
//                                        }
//
//                                    }
//                                }
//                            }
//                            System.out.println(total_app_qty);
//                            totalAppReqQty.setText(String.valueOf(total_app_qty));
                            appQty.clearFocus();
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

        }
    }

    @NonNull
    @Override
    public PRAQHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.purchase_req_approved_qty_view, parent, false);
        return new PRAQHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PRAQHolder holder, int position) {

        PurchaseReqAppQtyLists categoryItem = mCategoryItem.get(position);


        System.out.println("Position: " + position);
        holder.item_Name.setText(categoryItem.getItemName());
        holder.reqQty.setText(categoryItem.getReqQty());
        if (textChanged) {
            textChanged = false;
        }
        holder.appQty.setText(categoryItem.getApprovedQty());
        if (!textChanged) {
            textChanged = true;
        }

        System.out.println(textChanged);

    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public void filterList(ArrayList<PurchaseReqAppQtyLists> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
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

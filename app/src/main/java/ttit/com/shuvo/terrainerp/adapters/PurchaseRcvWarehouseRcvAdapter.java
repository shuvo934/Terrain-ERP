package ttit.com.shuvo.terrainerp.adapters;

import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.im_id_in_p_rcv;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.purchaseRcvItemDetailsAdapter;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.purchaseRcvItemDetailsLists;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.purchaseRcvWarehouseRcvLists;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.purchaseReceiveAllSelectedLists;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.totalAmountPRCV;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.totalRcvQtyPRCV;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.totalVatAmountPRCV;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.totalWarehouseWiseRcvQty;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.total_amount_prcv;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.total_rcv_qty_prcv;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.total_vat_amnt_prcv;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.total_ware_rcv_qty;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.warehouseLay;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.warehouseSelectSugg;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.DecimalFormat;
import java.util.ArrayList;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.arrayList.PurchaseRcvItemDetailsList;
import ttit.com.shuvo.terrainerp.arrayList.PurchaseRcvWarehouseRcvLists;

public class PurchaseRcvWarehouseRcvAdapter extends RecyclerView.Adapter<PurchaseRcvWarehouseRcvAdapter.PRWRHolder> {
    private ArrayList<PurchaseRcvWarehouseRcvLists> mCategoryItem;
    private final Context myContext;

    String searchingName = "";
    boolean textChanged = false;

    public PurchaseRcvWarehouseRcvAdapter(ArrayList<PurchaseRcvWarehouseRcvLists> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public PRWRHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.warehouse_selected_cv_item_view, parent, false);
        return new PRWRHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PRWRHolder holder, int position) {
        PurchaseRcvWarehouseRcvLists lists = mCategoryItem.get(position);

        holder.warehouse_rack_name.setText(lists.getWh_rack_name());

        if (textChanged) {
            textChanged = false;
        }
        holder.rcvQty.setText(lists.getRcv_qty());
        if (!textChanged) {
            textChanged = true;
        }
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public class PRWRHolder extends RecyclerView.ViewHolder {
        public TextView warehouse_rack_name;
        public EditText rcvQty;
        public ImageView deleteItem;
        public PRWRHolder(@NonNull View itemView) {
            super(itemView);
            warehouse_rack_name = itemView.findViewById(R.id.warehouse_selected_for_p_rcv);
            rcvQty = itemView.findViewById(R.id.rcv_qty_warehouse_wise_for_p_rcv);
            deleteItem = itemView.findViewById(R.id.delete_warehouse_selected_for_p_rcv);

            rcvQty.setOnFocusChangeListener((v, hasFocus) -> {
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
                    if (getAdapterPosition() >= 0) {
                        if (textChanged) {
                            searchingName = rcvQty.getText().toString();
                            total_ware_rcv_qty = 0;
                            String item_id = mCategoryItem.get(getAdapterPosition()).getItem_id();
                            String whd_id = mCategoryItem.get(getAdapterPosition()).getWhd_id();
                            int bal_qty = 0;
                            int index = 0;
                            for (int i = 0; i < purchaseRcvItemDetailsLists.size(); i++) {
                                if (item_id.equals(purchaseRcvItemDetailsLists.get(i).getWod_item_id())) {
                                    index = i;
                                    break;
                                }
                            }

                            if (!purchaseRcvItemDetailsLists.get(index).getBalance_qty().isEmpty()) {
                                bal_qty = Integer.parseInt(purchaseRcvItemDetailsLists.get(index).getBalance_qty());
                            }

                            if (!searchingName.isEmpty()) {
                                int rcv_qty = Integer.parseInt(searchingName);
                                if (mCategoryItem.size() != 0) {
                                    for (int i = 0; i < mCategoryItem.size() ; i++) {
                                        if (mCategoryItem.get(i).getRcv_qty() != null) {
                                            if (!mCategoryItem.get(i).getRcv_qty().isEmpty()) {
                                                if (!whd_id.equals(mCategoryItem.get(i).getWhd_id())) {
                                                    rcv_qty = rcv_qty + Integer.parseInt(mCategoryItem.get(i).getRcv_qty());
                                                }
                                            }
                                        }
                                    }
                                }
                                if (rcv_qty <= 0 || rcv_qty > bal_qty) {
                                    Toast.makeText(myContext,"Invalid Receive Quantity Typed",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    mCategoryItem.get(getAdapterPosition()).setRcv_qty(searchingName);

                                    if (mCategoryItem.size() != 0) {
                                        for (int i = 0; i < mCategoryItem.size() ; i++) {
                                            if (mCategoryItem.get(i).getRcv_qty() != null) {
                                                if (!mCategoryItem.get(i).getRcv_qty().isEmpty()) {
                                                    total_ware_rcv_qty = total_ware_rcv_qty + Integer.parseInt(mCategoryItem.get(i).getRcv_qty());
                                                }

                                            }
                                        }
                                    }
                                    totalWarehouseWiseRcvQty.setText(String.valueOf(total_ware_rcv_qty));
                                    int after_rcv_bal = bal_qty - total_ware_rcv_qty;

                                    purchaseRcvItemDetailsLists.get(index).setRcv_qty(String.valueOf(total_ware_rcv_qty));
                                    purchaseRcvItemDetailsLists.get(index).setAfter_rcv_balance_qty(String.valueOf(after_rcv_bal));

                                    DecimalFormat decimalFormat = new DecimalFormat("#.#");

                                    double unit_price =  Double.parseDouble(purchaseRcvItemDetailsLists.get(index).getWod_rate());
                                    double item_per_amount = total_ware_rcv_qty * unit_price;

                                    purchaseRcvItemDetailsLists.get(index).setItem_wise_rcv_total_amnt(decimalFormat.format(item_per_amount));

                                    double vat_amnt = Double.parseDouble(purchaseRcvItemDetailsLists.get(index).getWod_vat_pct_amt());

                                    double total_vat_per_item_amnt = vat_amnt * total_ware_rcv_qty;
                                    purchaseRcvItemDetailsLists.get(index).setItem_wise_rcv_total_vat(decimalFormat.format(total_vat_per_item_amnt));

                                    purchaseRcvItemDetailsAdapter.notifyDataSetChanged();
                                    total_rcv_qty_prcv = 0;
                                    total_amount_prcv = 0.0;
                                    total_vat_amnt_prcv = 0.0;
                                    for (int i = 0; i < purchaseRcvItemDetailsLists.size(); i++) {
                                        if (!purchaseRcvItemDetailsLists.get(i).getRcv_qty().isEmpty()) {
                                            total_rcv_qty_prcv = total_rcv_qty_prcv + Integer.parseInt(purchaseRcvItemDetailsLists.get(i).getRcv_qty());
                                        }
                                        if (!purchaseRcvItemDetailsLists.get(i).getItem_wise_rcv_total_amnt().isEmpty()) {
                                            total_amount_prcv = total_amount_prcv + Double.parseDouble(purchaseRcvItemDetailsLists.get(i).getItem_wise_rcv_total_amnt());
                                        }
                                        if (!purchaseRcvItemDetailsLists.get(i).getItem_wise_rcv_total_vat().isEmpty()) {
                                            total_vat_amnt_prcv = total_vat_amnt_prcv + Double.parseDouble(purchaseRcvItemDetailsLists.get(i).getItem_wise_rcv_total_vat());
                                        }
                                    }


                                    totalRcvQtyPRCV.setText(String.valueOf(total_rcv_qty_prcv));
                                    totalAmountPRCV.setText(decimalFormat.format(total_amount_prcv));
                                    totalVatAmountPRCV.setText(decimalFormat.format(total_vat_amnt_prcv));

                                }
                            }
                            else {
                                Toast.makeText(myContext,"Please Provide Receive Quantity",Toast.LENGTH_SHORT).show();
                            }
                            textChanged = false;
                            notifyDataSetChanged();
                        }
                    }
                    else {
                        v.clearFocus();
                        closeKeyBoard();
                    }
                }
            });

            rcvQty.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                        event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT ) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        Log.i("Let see", "Come here");
                        rcvQty.clearFocus();
                        closeKeyBoard();

                        return false; // consume.
                    }
                    else if (event.getAction() == KeyEvent.ACTION_UP || event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                        System.out.println("ON EDITOR BACK PRESSED");
                        return false; // consume.
                    }
                }
                return false;
            });

            deleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(myContext);
                    builder.setTitle("Delete Warehouse!")
                            .setMessage("Do you want to remove this Warehouse?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String rcv_qty = mCategoryItem.get(getAdapterPosition()).getRcv_qty();
                                    total_ware_rcv_qty = 0;
                                    String item_id = mCategoryItem.get(getAdapterPosition()).getItem_id();
                                    int bal_qty = 0;
                                    int index = 0;

                                    for (int i = 0; i < purchaseRcvItemDetailsLists.size(); i++) {
                                        if (item_id.equals(purchaseRcvItemDetailsLists.get(i).getWod_item_id())) {
                                            index = i;
                                            break;
                                        }
                                    }
                                    if (!purchaseRcvItemDetailsLists.get(index).getAfter_rcv_balance_qty().isEmpty()) {
                                        bal_qty = Integer.parseInt(purchaseRcvItemDetailsLists.get(index).getAfter_rcv_balance_qty());
                                    }

                                    if (rcv_qty.isEmpty()) {
                                        purchaseRcvWarehouseRcvLists.remove(getAdapterPosition());
                                    }
                                    else {
                                        purchaseRcvWarehouseRcvLists.remove(getAdapterPosition());
                                        if (mCategoryItem.size() != 0) {
                                            for (int i = 0; i < mCategoryItem.size() ; i++) {
                                                if (mCategoryItem.get(i).getRcv_qty() != null) {
                                                    if (!mCategoryItem.get(i).getRcv_qty().isEmpty()) {
                                                        total_ware_rcv_qty = total_ware_rcv_qty + Integer.parseInt(mCategoryItem.get(i).getRcv_qty());
                                                    }

                                                }
                                            }
                                        }
                                        totalWarehouseWiseRcvQty.setText(String.valueOf(total_ware_rcv_qty));
                                        int after_deleting_rcv_bal = bal_qty + Integer.parseInt(rcv_qty);

                                        purchaseRcvItemDetailsLists.get(index).setRcv_qty(String.valueOf(total_ware_rcv_qty));
                                        purchaseRcvItemDetailsLists.get(index).setAfter_rcv_balance_qty(String.valueOf(after_deleting_rcv_bal));

                                        DecimalFormat decimalFormat = new DecimalFormat("#.#");

                                        double unit_price =  Double.parseDouble(purchaseRcvItemDetailsLists.get(index).getWod_rate());
                                        double item_per_amount = total_ware_rcv_qty * unit_price;

                                        purchaseRcvItemDetailsLists.get(index).setItem_wise_rcv_total_amnt(decimalFormat.format(item_per_amount));

                                        double vat_amnt = Double.parseDouble(purchaseRcvItemDetailsLists.get(index).getWod_vat_pct_amt());

                                        double total_vat_per_item_amnt = vat_amnt * total_ware_rcv_qty;
                                        purchaseRcvItemDetailsLists.get(index).setItem_wise_rcv_total_vat(decimalFormat.format(total_vat_per_item_amnt));

                                        purchaseRcvItemDetailsAdapter.notifyDataSetChanged();

                                        total_rcv_qty_prcv = 0;
                                        total_amount_prcv = 0.0;
                                        total_vat_amnt_prcv = 0.0;
                                        for (int i = 0; i < purchaseRcvItemDetailsLists.size(); i++) {
                                            if (!purchaseRcvItemDetailsLists.get(i).getRcv_qty().isEmpty()) {
                                                total_rcv_qty_prcv = total_rcv_qty_prcv + Integer.parseInt(purchaseRcvItemDetailsLists.get(i).getRcv_qty());
                                            }
                                            if (!purchaseRcvItemDetailsLists.get(i).getItem_wise_rcv_total_amnt().isEmpty()) {
                                                total_amount_prcv = total_amount_prcv + Double.parseDouble(purchaseRcvItemDetailsLists.get(i).getItem_wise_rcv_total_amnt());
                                            }
                                            if (!purchaseRcvItemDetailsLists.get(i).getItem_wise_rcv_total_vat().isEmpty()) {
                                                total_vat_amnt_prcv = total_vat_amnt_prcv + Double.parseDouble(purchaseRcvItemDetailsLists.get(i).getItem_wise_rcv_total_vat());
                                            }
                                        }


                                        totalRcvQtyPRCV.setText(String.valueOf(total_rcv_qty_prcv));
                                        totalAmountPRCV.setText(decimalFormat.format(total_amount_prcv));
                                        totalVatAmountPRCV.setText(decimalFormat.format(total_vat_amnt_prcv));

                                    }

                                    ArrayList<PurchaseRcvItemDetailsList> lists = new ArrayList<>();
                                    for (int i = 0; i < purchaseReceiveAllSelectedLists.size(); i++) {
                                        if (im_id_in_p_rcv.equals(purchaseReceiveAllSelectedLists.get(i).getCat_id())) {
                                            lists = purchaseReceiveAllSelectedLists.get(i).getPurchaseRcvItemDetailsLists();
                                        }
                                    }
                                    ArrayList<PurchaseRcvWarehouseRcvLists> lists1 = new ArrayList<>();
                                    for (int i = 0; i < lists.size(); i++) {
                                        if (item_id.equals(lists.get(i).getWod_item_id())) {
                                            lists1 = lists.get(i).getPurchaseRcvWarehouseRcvLists();
                                        }
                                    }

                                    lists1.remove(getAdapterPosition());
                                    purchaseRcvWarehouseRcvLists.clear();
                                    purchaseRcvWarehouseRcvLists.addAll(lists1);

                                    notifyDataSetChanged();

                                    if (purchaseRcvWarehouseRcvLists.size() == 0) {
                                        warehouseSelectSugg.setVisibility(View.VISIBLE);
                                        warehouseLay.setVisibility(View.GONE);
                                    }

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

package ttit.com.shuvo.terrainerp.adapters;

import static ttit.com.shuvo.terrainerp.fragments.PurchaseOrder.*;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseOrder.totalOrderQtyPO;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseOrder.total_order_qty_po;

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

import java.text.DecimalFormat;
import java.util.ArrayList;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.arrayList.PurchaseOrderItemDetailsLists;

public class PurchaseOrderSelectedItemAdpater extends RecyclerView.Adapter<PurchaseOrderSelectedItemAdpater.POSIHolder> {
    private ArrayList<PurchaseOrderItemDetailsLists> mCategoryItem;
    private final Context myContext;

    String searchingName = "";
    boolean textChanged = false;

    public PurchaseOrderSelectedItemAdpater(ArrayList<PurchaseOrderItemDetailsLists> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public POSIHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.purchase_order_item_edited_details_view, parent, false);
        return new POSIHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull POSIHolder holder, int position) {
        PurchaseOrderItemDetailsLists purchaseOrderItemDetailsLists = mCategoryItem.get(position);

        holder.itemCode.setText(purchaseOrderItemDetailsLists.getItem_code());
        holder.hsCode.setText(purchaseOrderItemDetailsLists.getItem_hs_code());
        holder.itemName.setText(purchaseOrderItemDetailsLists.getItem_reference_name());
        holder.partNumber.setText(purchaseOrderItemDetailsLists.getItem_part_number());
        holder.itemUnit.setText(purchaseOrderItemDetailsLists.getItem_unit());
        holder.approvedQty.setText(purchaseOrderItemDetailsLists.getPrd_qty());

        if (textChanged) {
            textChanged = false;
        }
        holder.orderQty.setText(purchaseOrderItemDetailsLists.getOrder_qty());
        holder.balanceQty.setText(purchaseOrderItemDetailsLists.getAfter_order_balance_qty());
        holder.unitPrice.setText(purchaseOrderItemDetailsLists.getCostprice());

//        int balance_qty = Integer.parseInt(purchaseOrderItemDetailsLists.getBalance_qty());
//        int unit_price = Integer.parseInt(purchaseOrderItemDetailsLists.getCostprice());
//        int item_per_amount = balance_qty * unit_price;
        holder.itemAmount.setText(purchaseOrderItemDetailsLists.getAmount());

        holder.vatPercentage.setText(purchaseOrderItemDetailsLists.getItem_vat_percentage());

//        double vat_amnt = (Double.parseDouble(purchaseOrderItemDetailsLists.getItem_vat_percentage()) * (double) unit_price) / 100;

        holder.itemVat.setText(purchaseOrderItemDetailsLists.getItem_vat());

//        double total_vat_per_item_amnt = vat_amnt * (double) item_per_amount;

        holder.vatAmount.setText(purchaseOrderItemDetailsLists.getItem_vat_amnt());

        if (!textChanged) {
            textChanged = true;
        }

    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public class POSIHolder extends RecyclerView.ViewHolder {
        public TextView itemCode;
        public TextView hsCode;
        public TextView itemName;
        public TextView partNumber;
        public TextView itemUnit;
        public TextView approvedQty;
        public EditText orderQty;
        public TextView balanceQty;
        public EditText unitPrice;
        public TextView itemAmount;
        public EditText vatPercentage;
        public TextView itemVat;
        public TextView vatAmount;

        public POSIHolder(@NonNull View itemView) {
            super(itemView);
            itemCode = itemView.findViewById(R.id.item_code_selected_item_for_po);
            hsCode = itemView.findViewById(R.id.hs_code_selected_item_for_po);
            itemName = itemView.findViewById(R.id.item_name_selected_item_for_po);
            partNumber = itemView.findViewById(R.id.part_number_selected_item_for_po);
            itemUnit = itemView.findViewById(R.id.item_unit_selected_item_for_po);
            approvedQty = itemView.findViewById(R.id.app_qty_selected_item_for_po);
            orderQty = itemView.findViewById(R.id.order_qty_selected_item_for_po);
            balanceQty = itemView.findViewById(R.id.balance_qty_selected_item_for_po);
            unitPrice = itemView.findViewById(R.id.unit_price_selected_item_for_po);
            itemAmount = itemView.findViewById(R.id.amount_selected_item_for_po);
            vatPercentage = itemView.findViewById(R.id.vat_percentage_selected_item_for_po);
            itemVat = itemView.findViewById(R.id.vat_selected_item_for_po);
            vatAmount = itemView.findViewById(R.id.vat_amount_selected_item_for_po);

            orderQty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
                            System.out.println(textChanged);
                            searchingName = orderQty.getText().toString();
                            total_order_qty_po = 0;
                            total_amount_po = 0.0;
                            total_vat_amnt_po = 0.0;
                            int bal_qty = 0;
                            if (!mCategoryItem.get(getAdapterPosition()).getBalance_qty().isEmpty()) {
                                bal_qty = Integer.parseInt(mCategoryItem.get(getAdapterPosition()).getBalance_qty());
                            }
                            if (!searchingName.isEmpty()) {
                                int order_qty = Integer.parseInt(searchingName);
                                if (order_qty <= 0 || order_qty > bal_qty) {
                                    Toast.makeText(myContext,"Invalid order amount typed",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    mCategoryItem.get(getAdapterPosition()).setOrder_qty(searchingName);
                                    int after_order_bal = bal_qty - order_qty;
                                    mCategoryItem.get(getAdapterPosition()).setAfter_order_balance_qty(String.valueOf(after_order_bal));

                                    DecimalFormat decimalFormat = new DecimalFormat("#.#");

                                    double unit_price =  Double.parseDouble(mCategoryItem.get(getAdapterPosition()).getCostprice());
                                    double item_per_amount = order_qty * unit_price;
                                    mCategoryItem.get(getAdapterPosition()).setAmount(decimalFormat.format(item_per_amount));

                                    double vat_amnt = (Double.parseDouble(mCategoryItem.get(getAdapterPosition()).getItem_vat_percentage()) * unit_price) / 100;
                                    mCategoryItem.get(getAdapterPosition()).setItem_vat(decimalFormat.format(vat_amnt));

                                    double total_vat_per_item_amnt = vat_amnt * order_qty;
                                    mCategoryItem.get(getAdapterPosition()).setItem_vat_amnt(decimalFormat.format(total_vat_per_item_amnt));

                                    if (mCategoryItem.size() != 0) {
                                        for (int i = 0; i < mCategoryItem.size() ; i++) {
                                            if (mCategoryItem.get(i).getOrder_qty() != null) {
                                                if (!mCategoryItem.get(i).getOrder_qty().isEmpty()) {
                                                    total_order_qty_po = total_order_qty_po + Integer.parseInt(mCategoryItem.get(i).getOrder_qty());
                                                }
                                            }
                                            if (mCategoryItem.get(i).getAmount() != null) {
                                                if (!mCategoryItem.get(i).getAmount().isEmpty()) {
                                                    total_amount_po = total_amount_po + Double.parseDouble(mCategoryItem.get(i).getAmount());
                                                }
                                            }
                                            if (mCategoryItem.get(i).getItem_vat_amnt() != null) {
                                                if (!mCategoryItem.get(i).getItem_vat_amnt().isEmpty()) {
                                                    total_vat_amnt_po = total_vat_amnt_po + Double.parseDouble(mCategoryItem.get(i).getItem_vat_amnt());
                                                }
                                            }
                                        }
                                    }


                                    totalOrderQtyPO.setText(String.valueOf(total_order_qty_po));
                                    totalAmountPO.setText(decimalFormat.format(total_amount_po));
                                    totalVatAmountPO.setText(decimalFormat.format(total_vat_amnt_po));
                                }
                            }
                            else {
                                Toast.makeText(myContext,"Order amount can not be empty",Toast.LENGTH_SHORT).show();
                            }

                            textChanged = false;
                            notifyDataSetChanged();
                        }

                    }
                }
            });
            orderQty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                            actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                            event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT ) {
                        if (event == null || !event.isShiftPressed()) {
                            // the user is done typing.
                            Log.i("Let see", "Come here");
                            orderQty.clearFocus();
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

            unitPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
                            System.out.println(textChanged);
                            searchingName = unitPrice.getText().toString();
                            total_amount_po = 0.0;
                            total_vat_amnt_po = 0.0;

                            if (!searchingName.isEmpty()) {
                                double unit_price = Double.parseDouble(searchingName);
                                if (unit_price < 0) {
                                    Toast.makeText(myContext,"Invalid unit price typed",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    DecimalFormat decimalFormat = new DecimalFormat("#.#");

                                    mCategoryItem.get(getAdapterPosition()).setCostprice(decimalFormat.format(unit_price));

                                    int order_qty = Integer.parseInt(mCategoryItem.get(getAdapterPosition()).getOrder_qty());
                                    double item_per_amount = order_qty * unit_price;
                                    mCategoryItem.get(getAdapterPosition()).setAmount(decimalFormat.format(item_per_amount));

                                    double vat_amnt = (Double.parseDouble(mCategoryItem.get(getAdapterPosition()).getItem_vat_percentage()) * unit_price) / 100;
                                    mCategoryItem.get(getAdapterPosition()).setItem_vat(decimalFormat.format(vat_amnt));

                                    double total_vat_per_item_amnt = vat_amnt * order_qty;
                                    mCategoryItem.get(getAdapterPosition()).setItem_vat_amnt(decimalFormat.format(total_vat_per_item_amnt));

                                    if (mCategoryItem.size() != 0) {
                                        for (int i = 0; i < mCategoryItem.size() ; i++) {

                                            if (mCategoryItem.get(i).getAmount() != null) {
                                                if (!mCategoryItem.get(i).getAmount().isEmpty()) {
                                                    total_amount_po = total_amount_po + Double.parseDouble(mCategoryItem.get(i).getAmount());
                                                }
                                            }
                                            if (mCategoryItem.get(i).getItem_vat_amnt() != null) {
                                                if (!mCategoryItem.get(i).getItem_vat_amnt().isEmpty()) {
                                                    total_vat_amnt_po = total_vat_amnt_po + Double.parseDouble(mCategoryItem.get(i).getItem_vat_amnt());
                                                }
                                            }
                                        }
                                    }


                                    totalAmountPO.setText(decimalFormat.format(total_amount_po));
                                    totalVatAmountPO.setText(decimalFormat.format(total_vat_amnt_po));
                                }
                            }
                            else {
                                Toast.makeText(myContext,"Unit price can not be empty",Toast.LENGTH_SHORT).show();
                            }

                            textChanged = false;
                            notifyDataSetChanged();
                        }

                    }
                }
            });
            unitPrice.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                            actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                            event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT ) {
                        if (event == null || !event.isShiftPressed()) {
                            // the user is done typing.
                            Log.i("Let see", "Come here");
                            unitPrice.clearFocus();
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

            vatPercentage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
                            System.out.println(textChanged);
                            searchingName = vatPercentage.getText().toString();
                            total_vat_amnt_po = 0.0;

                            if (!searchingName.isEmpty()) {
                                double vat_percentage = Double.parseDouble(searchingName);
                                if (vat_percentage < 0) {
                                    Toast.makeText(myContext,"Invalid Vat typed",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    DecimalFormat decimalFormat = new DecimalFormat("#.#");

                                    mCategoryItem.get(getAdapterPosition()).setItem_vat_percentage(decimalFormat.format(vat_percentage));

                                    double unit_price = Double.parseDouble(mCategoryItem.get(getAdapterPosition()).getCostprice());

                                    double vat_amnt = (vat_percentage * unit_price) / 100;
                                    mCategoryItem.get(getAdapterPosition()).setItem_vat(decimalFormat.format(vat_amnt));

                                    int order_qty  = Integer.parseInt(mCategoryItem.get(getAdapterPosition()).getOrder_qty());
                                    double total_vat_per_item_amnt = vat_amnt * order_qty;
                                    mCategoryItem.get(getAdapterPosition()).setItem_vat_amnt(decimalFormat.format(total_vat_per_item_amnt));

                                    if (mCategoryItem.size() != 0) {
                                        for (int i = 0; i < mCategoryItem.size() ; i++) {

                                            if (mCategoryItem.get(i).getItem_vat_amnt() != null) {
                                                if (!mCategoryItem.get(i).getItem_vat_amnt().isEmpty()) {
                                                    total_vat_amnt_po = total_vat_amnt_po + Double.parseDouble(mCategoryItem.get(i).getItem_vat_amnt());
                                                }
                                            }
                                        }
                                    }

                                    totalVatAmountPO.setText(decimalFormat.format(total_vat_amnt_po));
                                }
                            }
                            else {
                                Toast.makeText(myContext,"Vat Percentage can not be empty",Toast.LENGTH_SHORT).show();
                            }

                            textChanged = false;
                            notifyDataSetChanged();
                        }

                    }
                }
            });
            vatPercentage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                            actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                            event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT ) {
                        if (event == null || !event.isShiftPressed()) {
                            // the user is done typing.
                            Log.i("Let see", "Come here");
                            vatPercentage.clearFocus();
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

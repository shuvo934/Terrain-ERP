package ttit.com.shuvo.terrainerp.dialogues;

import static ttit.com.shuvo.terrainerp.fragments.PurchaseRequisition.categoryLay;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseRequisition.itemSelectLay;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseRequisition.prSelectedItemLists;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseRequisition.purhcaseReqItemReqQtyAdapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.WaitProgress;
import ttit.com.shuvo.terrainerp.adapters.PurchaseReqItemSelectAdapter;
import ttit.com.shuvo.terrainerp.arrayList.PRSelectedItemLists;
import ttit.com.shuvo.terrainerp.arrayList.PurchaseReqItemList;


public class ItemSelectionForPRDial extends AppCompatDialogFragment implements PurchaseReqItemSelectAdapter.ClickedItem{
    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
//    private Connection connection;

    RecyclerView recyclerView;
    PurchaseReqItemSelectAdapter purchaseReqItemSelectAdapter;
    RecyclerView.LayoutManager layoutManager;

    TextInputEditText search;

    AlertDialog alertDialog;

    String searchingName = "";

    ArrayList<PurchaseReqItemList> purchaseReqItemLists;
    ArrayList<PurchaseReqItemList> filteredList;

    Boolean isfiltered = false;

    AppCompatActivity activity;

    Context mContext;
    String cat_id;
    String req_date;
    public ItemSelectionForPRDial(Context mContext, String cat_id, String req_date) {
        this.mContext = mContext;
        this.cat_id = cat_id;
        this.req_date= req_date;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.purchase_requistion_item_selection_view, null);

        activity = (AppCompatActivity) view.getContext();

        purchaseReqItemLists = new ArrayList<>();
        filteredList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.select_item_list_for_pr_view);
        search = view.findViewById(R.id.search_items_for_pr_dialogue);

        builder.setView(view);

        alertDialog = builder.create();

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                searchingName = s.toString();
                filter(s.toString());
            }
        });

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                        event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        Log.i("Let see", "Come here");
                        search.clearFocus();
                        InputMethodManager mgr = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        closeKeyBoard();

                        return false; // consume.
                    }
                }
                return false;
            }
        });

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        getItemList();

        return alertDialog;
    }

    private void closeKeyBoard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void filter(String text) {

        filteredList = new ArrayList<>();
        for (PurchaseReqItemList item : purchaseReqItemLists) {
            if (item.getItem_reference_name().toLowerCase().contains(text.toLowerCase()) || item.getItem_code().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add((item));
                isfiltered = true;
            }
        }
        purchaseReqItemSelectAdapter.filterList(filteredList);
    }

    public void getItemList() {
        waitProgress.show(activity.getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;
        purchaseReqItemLists = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        String url = "http://103.56.208.123:8001/apex/tterp/purchaseReq/getItems?PRM_IM_ID="+cat_id+"&PRM_REQ_DATE="+req_date+"&PRM_CCPP_ID=";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        String im_name = info.getString("im_name")
                                .equals("null") ? "" : info.getString("im_name");
                        String item_reference_name = info.getString("item_reference_name")
                                .equals("null") ? "" : info.getString("item_reference_name");
                        String item_barcode_no = info.getString("item_barcode_no")
                                .equals("null") ? "" : info.getString("item_barcode_no");
                        String item_hs_code = info.getString("item_hs_code")
                                .equals("null") ? "" : info.getString("item_hs_code");
                        String item_part_number = info.getString("item_part_number")
                                .equals("null") ? "" : info.getString("item_part_number");
                        String item_id = info.getString("item_id")
                                .equals("null") ? "" : info.getString("item_id");
                        String im_id = info.getString("im_id")
                                .equals("null") ? "" : info.getString("im_id");
                        String item_unit = info.getString("item_unit")
                                .equals("null") ? "" : info.getString("item_unit");
                        String item_code = info.getString("item_code")
                                .equals("null") ? "" : info.getString("item_code");
                        String item_color_id = info.getString("item_color_id")
                                .equals("null") ? "" : info.getString("item_color_id");
                        String item_size_id = info.getString("item_size_id")
                                .equals("null") ? "" : info.getString("item_size_id");
                        String stock = info.getString("stock")
                                .equals("null") ? "0" : info.getString("stock");
                        String ccppd_id = info.getString("ccppd_id")
                                .equals("null") ? "" : info.getString("ccppd_id");


                        purchaseReqItemLists.add(new PurchaseReqItemList(im_name, item_reference_name, item_barcode_no,
                                item_hs_code,item_part_number,item_id,
                                im_id,item_unit,item_code,
                                item_color_id,item_size_id,stock,ccppd_id));
                    }
                }
                connected = true;
                updateFragment();
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateFragment();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateFragment();
        });

        requestQueue.add(stringRequest);
    }

    private void updateFragment() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                if (prSelectedItemLists.size() == 0) {
                    purchaseReqItemSelectAdapter = new PurchaseReqItemSelectAdapter(purchaseReqItemLists,getContext(),ItemSelectionForPRDial.this);
                    recyclerView.setAdapter(purchaseReqItemSelectAdapter);
                    purchaseReqItemSelectAdapter.notifyDataSetChanged();
                }
                else {
                    for (int i=0 ; i < prSelectedItemLists.size(); i++) {
                        String item_id = prSelectedItemLists.get(i).getItem_id();
                        int index = 0;
                        boolean found = false;
                        for (int j = 0; j < purchaseReqItemLists.size(); j++) {
                            if (item_id.equals(purchaseReqItemLists.get(j).getItem_id())) {
                                index = j;
                                found = true;
                                break;
                            }
                        }
                        if (found) {
                            purchaseReqItemLists.remove(index);
                        }
                    }

                    purchaseReqItemSelectAdapter = new PurchaseReqItemSelectAdapter(purchaseReqItemLists,getContext(),ItemSelectionForPRDial.this);
                    recyclerView.setAdapter(purchaseReqItemSelectAdapter);
                    purchaseReqItemSelectAdapter.notifyDataSetChanged();
                }

            }
            else {
                AlertDialog dialog;
                dialog = new AlertDialog.Builder(mContext)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getItemList();
                    dialog.dismiss();
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> {
                    dialog.dismiss();
                    alertDialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog;
            dialog = new AlertDialog.Builder(mContext)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getItemList();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                alertDialog.dismiss();
            });
        }
    }

    @Override
    public void onItemClicked(int CategoryPosition) {
        String im_name;
        String item_reference_name;
        String item_barcode_no;
        String item_hs_code;
        String item_part_number;
        String item_id;
        String im_id;
        String item_unit;
        String item_code;
        String item_color_id;
        String item_size_id;
        String stock;
        String ccppd_id;
        if (isfiltered) {
            im_name = filteredList.get(CategoryPosition).getIm_name();
            item_reference_name = filteredList.get(CategoryPosition).getItem_reference_name();
            item_barcode_no = filteredList.get(CategoryPosition).getItem_barcode_no();
            item_hs_code = filteredList.get(CategoryPosition).getItem_hs_code();
            item_part_number = filteredList.get(CategoryPosition).getItem_part_number();
            item_id = filteredList.get(CategoryPosition).getItem_id();
            im_id = filteredList.get(CategoryPosition).getIm_id();
            item_unit = filteredList.get(CategoryPosition).getItem_unit();
            item_code = filteredList.get(CategoryPosition).getItem_code();
            item_color_id = filteredList.get(CategoryPosition).getItem_color_id();
            item_size_id = filteredList.get(CategoryPosition).getItem_size_id();
            stock = filteredList.get(CategoryPosition).getStock();
            ccppd_id = filteredList.get(CategoryPosition).getCcppd_id();
        }
        else {
            im_name = purchaseReqItemLists.get(CategoryPosition).getIm_name();
            item_reference_name = purchaseReqItemLists.get(CategoryPosition).getItem_reference_name();
            item_barcode_no = purchaseReqItemLists.get(CategoryPosition).getItem_barcode_no();
            item_hs_code = purchaseReqItemLists.get(CategoryPosition).getItem_hs_code();
            item_part_number = purchaseReqItemLists.get(CategoryPosition).getItem_part_number();
            item_id = purchaseReqItemLists.get(CategoryPosition).getItem_id();
            im_id = purchaseReqItemLists.get(CategoryPosition).getIm_id();
            item_unit = purchaseReqItemLists.get(CategoryPosition).getItem_unit();
            item_code = purchaseReqItemLists.get(CategoryPosition).getItem_code();
            item_color_id = purchaseReqItemLists.get(CategoryPosition).getItem_color_id();
            item_size_id = purchaseReqItemLists.get(CategoryPosition).getItem_size_id();
            stock = purchaseReqItemLists.get(CategoryPosition).getStock();
            ccppd_id = purchaseReqItemLists.get(CategoryPosition).getCcppd_id();
        }


        prSelectedItemLists.add(new PRSelectedItemLists(im_name,item_reference_name,item_barcode_no,item_hs_code,item_part_number,item_id,im_id,
                item_unit,item_code,item_color_id,item_size_id,stock,ccppd_id,"","",false));

        if (prSelectedItemLists.size() == 1) {
            itemSelectLay.setVisibility(View.VISIBLE);
            categoryLay.setEnabled(false);
        }
        purhcaseReqItemReqQtyAdapter.notifyDataSetChanged();
        alertDialog.dismiss();
    }
}

package ttit.com.shuvo.terrainerp.dialogues;

import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.categorySelection;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.im_id_in_p_rcv;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.prcvCatCardView;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.purRcvCategorySelectedAdapter;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.purchaseRcvWarehouseRcvAdapter;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.purchaseRcvWarehouseRcvLists;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.purchaseReceiveAllSelectedLists;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.selectedCategoryPosition;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.warehouseLay;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.warehouseSelectSugg;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.warehouseSelection;

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
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.WaitProgress;
import ttit.com.shuvo.terrainerp.adapters.CategoryWarehouseSelectionAdapter;
import ttit.com.shuvo.terrainerp.arrayList.PurchaseRcvItemDetailsList;
import ttit.com.shuvo.terrainerp.arrayList.PurchaseRcvWarehouseRcvLists;
import ttit.com.shuvo.terrainerp.arrayList.PurchaseReceiveAllSelectedLists;
import ttit.com.shuvo.terrainerp.arrayList.ReceiveTypeList;

public class CategoryWarehouseSelectionDial extends AppCompatDialogFragment implements CategoryWarehouseSelectionAdapter.ClickedItem {
    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
    TextView dial_name;
    TextInputLayout searchLay;
    TextInputEditText search;
    TextView nameOfList;
    RecyclerView recyclerView;
    CategoryWarehouseSelectionAdapter categoryWarehouseSelectionAdapter;
    RecyclerView.LayoutManager layoutManager;
    AlertDialog alertDialog;

    String searchingName = "";
    ArrayList<ReceiveTypeList> receiveTypeLists;
    ArrayList<ReceiveTypeList> filteredList;
    Boolean isfiltered = false;

    AppCompatActivity activity;
    Context mContext;
    int selection;
    String req_id;

    public CategoryWarehouseSelectionDial(Context mContext, int selection, String req_id) {
        this.mContext = mContext;
        this.selection = selection;
        this.req_id = req_id;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.purchase_rcv_category_warehouse_selection_view, null);

        activity = (AppCompatActivity) view.getContext();

        receiveTypeLists = new ArrayList<>();
        filteredList = new ArrayList<>();

        dial_name = view.findViewById(R.id.what_is_selected_name_p_rcv);
        searchLay = view.findViewById(R.id.search_category_warehouse_layout_for_p_rcv_dialogue);
        search = view.findViewById(R.id.search_category_warehouse_for_p_rcv_dialogue);
        nameOfList = view.findViewById(R.id.list_category_warehouse_name_in_p_rcv_view);
        recyclerView = view.findViewById(R.id.select_category_warehouse_list_for_p_rcv_view);

        builder.setView(view);

        alertDialog = builder.create();

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        if (selection == 1) {
            dial_name.setText("Select Category:");
            searchLay.setHint("Search Category");
            nameOfList.setText("Category Name");
        }
        else if (selection == 2) {
            dial_name.setText("Select Warehouse:");
            searchLay.setHint("Search Warehouse");
            nameOfList.setText("Warehouse Name");
        }

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

        getList();

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
        for (ReceiveTypeList item : receiveTypeLists) {
            if (item.getType().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add((item));
                isfiltered = true;
            }
        }
        categoryWarehouseSelectionAdapter.filterList(filteredList);
    }

    public void getList() {
        waitProgress.show(activity.getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;
        receiveTypeLists = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        if (selection == 1) {
            String url = "http://103.56.208.123:8001/apex/tterp/purchaseReceive/getPOCategory?wom_id="+req_id+"";

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
                            String im_id = info.getString("im_id")
                                    .equals("null") ? "" : info.getString("im_id");


                            receiveTypeLists.add(new ReceiveTypeList(im_id, im_name,""));
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
        else if (selection == 2) {
            String url = "http://103.56.208.123:8001/apex/tterp/purchaseReceive/getWarehouses";

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
                            String whd_reck = info.getString("whd_reck")
                                    .equals("null") ? "" : info.getString("whd_reck");
                            String whd_id = info.getString("whd_id")
                                    .equals("null") ? "" : info.getString("whd_id");
                            String whm_id = info.getString("whm_id")
                                    .equals("null") ? "" : info.getString("whm_id");


                            receiveTypeLists.add(new ReceiveTypeList(whd_id, whd_reck,whm_id));
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
    }

    private void updateFragment() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                if (selection == 1) {
                    if (purchaseReceiveAllSelectedLists.size() != 0) {
                        for (int i = 0; i < purchaseReceiveAllSelectedLists.size(); i++) {
                            String cat_id = purchaseReceiveAllSelectedLists.get(i).getCat_id();
                            int index = 0;
                            boolean found = false;
                            for (int j = 0; j < receiveTypeLists.size(); j++) {
                                if (cat_id.equals(receiveTypeLists.get(j).getId())) {
                                    index = j;
                                    found = true;
                                    break;
                                }
                            }
                            if (found) {
                                receiveTypeLists.remove(index);
                            }
                        }

                    }
                    categoryWarehouseSelectionAdapter = new CategoryWarehouseSelectionAdapter(receiveTypeLists,getContext(),CategoryWarehouseSelectionDial.this);
                    recyclerView.setAdapter(categoryWarehouseSelectionAdapter);
                    categoryWarehouseSelectionAdapter.notifyDataSetChanged();
                }
                else if (selection == 2) {
                    if (purchaseRcvWarehouseRcvLists.size() != 0) {
                        for (int i = 0; i < purchaseRcvWarehouseRcvLists.size(); i++) {
                            String whd_id = purchaseRcvWarehouseRcvLists.get(i).getWhd_id();
                            int index = 0;
                            boolean found = false;
                            for (int j = 0; j < receiveTypeLists.size(); j++) {
                                if (whd_id.equals(receiveTypeLists.get(j).getId())) {
                                    index = j;
                                    found = true;
                                    break;
                                }
                            }
                            if (found) {
                                receiveTypeLists.remove(index);
                            }
                        }
                    }
                    categoryWarehouseSelectionAdapter = new CategoryWarehouseSelectionAdapter(receiveTypeLists,getContext(),CategoryWarehouseSelectionDial.this);
                    recyclerView.setAdapter(categoryWarehouseSelectionAdapter);
                    categoryWarehouseSelectionAdapter.notifyDataSetChanged();
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

                    getList();
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

                getList();
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
    public void onItemClicked(int Position) {
        if (selection == 1) {
            String im_id = "";
            String im_name = "";
            if (isfiltered) {
                im_id = filteredList.get(Position).getId();
                im_name = filteredList.get(Position).getType();
            }
            else {
                im_id = receiveTypeLists.get(Position).getId();
                im_name = receiveTypeLists.get(Position).getType();
            }
            im_id_in_p_rcv = im_id;
            purchaseReceiveAllSelectedLists.add(new PurchaseReceiveAllSelectedLists(im_id, im_name,new ArrayList<>(),false));

            if (purchaseReceiveAllSelectedLists.size() == 1) {
                prcvCatCardView.setVisibility(View.VISIBLE);
            }
            selectedCategoryPosition = purchaseReceiveAllSelectedLists.size() - 1;
            purRcvCategorySelectedAdapter.notifyDataSetChanged();

            categorySelection.setText("SELECT CATEGORY >>");

        }
        else if (selection == 2) {
            String whd_id = "";
            String wh_rack = "";
            String whm_id = "";
            if (isfiltered) {
                whd_id = filteredList.get(Position).getId();
                wh_rack = filteredList.get(Position).getType();
                whm_id = filteredList.get(Position).getExtra_id();
            }
            else {
                whd_id = receiveTypeLists.get(Position).getId();
                wh_rack = receiveTypeLists.get(Position).getType();
                whm_id = receiveTypeLists.get(Position).getExtra_id();
            }

//            for (int i = 0; i < purchaseRcvItemDetailsLists.size(); i++) {
//                if (req_id.equals(purchaseRcvItemDetailsLists.get(i).getWod_item_id())) {
//                    purchaseRcvItemDetailsLists.get(i).setPurchaseRcvWarehouseRcvLists(purchaseRcvWarehouseRcvLists);
//                }
//            }
            ArrayList<PurchaseRcvItemDetailsList> lists = new ArrayList<>();
            for (int i = 0; i < purchaseReceiveAllSelectedLists.size(); i++) {
                if (im_id_in_p_rcv.equals(purchaseReceiveAllSelectedLists.get(i).getCat_id())) {
                    lists = purchaseReceiveAllSelectedLists.get(i).getPurchaseRcvItemDetailsLists();
                }
            }
            ArrayList<PurchaseRcvWarehouseRcvLists> lists1 = new ArrayList<>();
            for (int i = 0; i < lists.size(); i++) {
                if (req_id.equals(lists.get(i).getWod_item_id())) {
                    lists1 = lists.get(i).getPurchaseRcvWarehouseRcvLists();
                }
            }

            lists1.add(new PurchaseRcvWarehouseRcvLists(wh_rack,whd_id,whm_id,"",req_id,false));

//            purchaseRcvWarehouseRcvLists.add(new PurchaseRcvWarehouseRcvLists(wh_rack,whd_id,whm_id,"",req_id,false));
            for (int i = 0; i < lists.size(); i++) {
                if (req_id.equals(lists.get(i).getWod_item_id())) {
                    lists.get(i).setPurchaseRcvWarehouseRcvLists(lists1);
                }
            }
            for (int i = 0; i < purchaseReceiveAllSelectedLists.size(); i++) {
                if (im_id_in_p_rcv.equals(purchaseReceiveAllSelectedLists.get(i).getCat_id())) {
                    purchaseReceiveAllSelectedLists.get(i).setPurchaseRcvItemDetailsLists(lists);
                }
            }
            purchaseRcvWarehouseRcvLists.clear();
            purchaseRcvWarehouseRcvLists.addAll(lists1);
            if (purchaseRcvWarehouseRcvLists.size() == 1) {
                warehouseLay.setVisibility(View.VISIBLE);
                warehouseSelectSugg.setVisibility(View.GONE);
            }

            purchaseRcvWarehouseRcvAdapter.notifyDataSetChanged();
            warehouseSelection.setText("SELECT WAREHOUSE >>");
        }
        alertDialog.dismiss();

    }
}

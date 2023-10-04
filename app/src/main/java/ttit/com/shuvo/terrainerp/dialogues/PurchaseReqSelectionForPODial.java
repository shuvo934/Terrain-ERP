package ttit.com.shuvo.terrainerp.dialogues;

import static ttit.com.shuvo.terrainerp.fragments.PurchaseOrder.im_id_in_po;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseOrder.prCardView;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseOrder.prm_id_in_po;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseOrder.purchaseOrderReqSelectedAdapter;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseOrder.purchaseOrderSelectedRequisitionLists;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseOrder.purchaseReqSelection;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseOrder.selectedRequisition;

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
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.cardview.widget.CardView;
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
import ttit.com.shuvo.terrainerp.adapters.PurchaseOrderReqSelectAdapter;
import ttit.com.shuvo.terrainerp.arrayList.PurchaseOrderSelectedRequisitionLists;
import ttit.com.shuvo.terrainerp.arrayList.PurchaseOrderRequisitionLists;

public class PurchaseReqSelectionForPODial extends AppCompatDialogFragment implements PurchaseOrderReqSelectAdapter.ClickedItem {
    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
    RecyclerView recyclerView;
    PurchaseOrderReqSelectAdapter purchaseOrderReqSelectAdapter;
    RecyclerView.LayoutManager layoutManager;
    TextInputEditText search;

    AlertDialog alertDialog;

    String searchingName = "";
    ArrayList<PurchaseOrderRequisitionLists> requisitionLists;
    ArrayList<PurchaseOrderRequisitionLists> filteredList;
    Boolean isfiltered = false;

    AppCompatActivity activity;
    Context mContext;
    TextView noReqMsg;
    CardView searchCard;
    HorizontalScrollView horizontalScrollView;

    public PurchaseReqSelectionForPODial(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.purchase_order_requisition_selection_view, null);

        activity = (AppCompatActivity) view.getContext();

        requisitionLists = new ArrayList<>();
        filteredList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.select_pr_list_for_po_view);
        search = view.findViewById(R.id.search_pr_for_po_dialogue);
        noReqMsg = view.findViewById(R.id.no_req_found_msg_pr_select);
        noReqMsg.setVisibility(View.GONE);
        searchCard = view.findViewById(R.id.search_pr_for_po_card_view);
        searchCard.setVisibility(View.VISIBLE);
        horizontalScrollView = view.findViewById(R.id.horizontal_scroll_pr_search_po);
        horizontalScrollView.setVisibility(View.VISIBLE);

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

        getReqList();

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
        for (PurchaseOrderRequisitionLists item : requisitionLists) {
            if (item.getPrm_req_no().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add((item));
                isfiltered = true;
            }
        }
        purchaseOrderReqSelectAdapter.filterList(filteredList);
    }

    public void getReqList() {
        waitProgress.show(activity.getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;
        requisitionLists = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        String url = "http://103.56.208.123:8001/apex/tterp/purchaseOrder/getRequisitions";

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
                        String prm_req_no = info.getString("prm_req_no")
                                .equals("null") ? "" : info.getString("prm_req_no");
                        String pr_date = info.getString("pr_date")
                                .equals("null") ? "" : info.getString("pr_date");
                        String prm_id = info.getString("prm_id")
                                .equals("null") ? "" : info.getString("prm_id");
                        String im_id = info.getString("im_id")
                                .equals("null") ? "" : info.getString("im_id");
                        String im_name = info.getString("im_name")
                                .equals("null") ? "" : info.getString("im_name");
                        String req_qty = info.getString("req_qty")
                                .equals("null") ? "0" : info.getString("req_qty");
                        String app_qty = info.getString("app_qty")
                                .equals("null") ? "0" : info.getString("app_qty");
                        String app_balance = info.getString("app_balance")
                                .equals("null") ? "0" : info.getString("app_balance");


                        requisitionLists.add(new PurchaseOrderRequisitionLists(prm_req_no, pr_date, prm_id,
                                im_id,im_name,req_qty,
                                app_qty,app_balance));
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

                if (requisitionLists.size() != 0) {
                    noReqMsg.setVisibility(View.GONE);
                    searchCard.setVisibility(View.VISIBLE);
                    horizontalScrollView.setVisibility(View.VISIBLE);

                    if (purchaseOrderSelectedRequisitionLists.size() != 0) {
                        for (int i = 0; i < purchaseOrderSelectedRequisitionLists.size(); i++) {
                            String prm_id = purchaseOrderSelectedRequisitionLists.get(i).getPrm_id();
                            int index = 0;
                            boolean found = false;
                            for (int j = 0; j < requisitionLists.size(); j++) {
                                if (prm_id.equals(requisitionLists.get(j).getPrm_id())) {
                                    index = j;
                                    found = true;
                                    break;
                                }
                            }
                            if (found) {
                                requisitionLists.remove(index);
                            }
                        }

                    }
                    purchaseOrderReqSelectAdapter = new PurchaseOrderReqSelectAdapter(requisitionLists,getContext(),PurchaseReqSelectionForPODial.this);
                    recyclerView.setAdapter(purchaseOrderReqSelectAdapter);
                    purchaseOrderReqSelectAdapter.notifyDataSetChanged();
                }
                else {
                    noReqMsg.setVisibility(View.VISIBLE);
                    searchCard.setVisibility(View.GONE);
                    horizontalScrollView.setVisibility(View.GONE);
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

                    getReqList();
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

                getReqList();
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
        String prm_req_no;
        String pr_date;
        String prm_id;
        String im_id;
        String im_name;
        String req_qty;
        String app_qty;
        String app_balance;

        if (isfiltered) {
            prm_req_no = filteredList.get(Position).getPrm_req_no();
            pr_date = filteredList.get(Position).getPr_date();
            prm_id = filteredList.get(Position).getPrm_id();
            im_id = filteredList.get(Position).getIm_id();
            im_name = filteredList.get(Position).getIm_name();
            req_qty = filteredList.get(Position).getReq_qty();
            app_qty = filteredList.get(Position).getApp_qty();
            app_balance = filteredList.get(Position).getApp_balance();
        }
        else {
            prm_req_no = requisitionLists.get(Position).getPrm_req_no();
            pr_date = requisitionLists.get(Position).getPr_date();
            prm_id = requisitionLists.get(Position).getPrm_id();
            im_id = requisitionLists.get(Position).getIm_id();
            im_name = requisitionLists.get(Position).getIm_name();
            req_qty = requisitionLists.get(Position).getReq_qty();
            app_qty = requisitionLists.get(Position).getApp_qty();
            app_balance = requisitionLists.get(Position).getApp_balance();
        }

        prm_id_in_po = prm_id;
        im_id_in_po = im_id;
        purchaseOrderSelectedRequisitionLists.add(new PurchaseOrderSelectedRequisitionLists("",prm_req_no,pr_date,prm_id,im_id,im_name,req_qty,app_qty,app_balance,
                new ArrayList<>(),true,false,false));

        if (purchaseOrderSelectedRequisitionLists.size() == 1) {
            prCardView.setVisibility(View.VISIBLE);
        }
        selectedRequisition = purchaseOrderSelectedRequisitionLists.size() - 1;
        purchaseOrderReqSelectedAdapter.notifyDataSetChanged();

        purchaseReqSelection.setText("SELECT PURCHASE REQUISITION >>");
        alertDialog.dismiss();
    }
}

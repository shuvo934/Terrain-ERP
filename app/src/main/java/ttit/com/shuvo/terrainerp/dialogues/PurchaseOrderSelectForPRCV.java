package ttit.com.shuvo.terrainerp.dialogues;

import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.purOrderSelection;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseReceive.wom_id_for_p_rcv;

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
import ttit.com.shuvo.terrainerp.adapters.PurchaseOrderForPRCVAdapter;
import ttit.com.shuvo.terrainerp.arrayList.PurchaseOrderSelectionList;
import ttit.com.shuvo.terrainerp.fragments.PurchaseReceive;

public class PurchaseOrderSelectForPRCV extends AppCompatDialogFragment implements PurchaseOrderForPRCVAdapter.ClickedItem {
    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    RecyclerView accountsView;
    PurchaseOrderForPRCVAdapter purchaseOrderForPRCVAdapter;
    RecyclerView.LayoutManager layoutManager;

    TextInputEditText search;

    AlertDialog alertDialog;
    String searchingName = "";
    ArrayList<PurchaseOrderSelectionList> purchaseOrderSelectionLists;
    ArrayList<PurchaseOrderSelectionList> filteredList;
    Boolean isfiltered = false;

    AppCompatActivity activity;
    Context mContext;

    public PurchaseOrderSelectForPRCV(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.purchase_order_list_view, null);

        activity = (AppCompatActivity) view.getContext();

        purchaseOrderSelectionLists = new ArrayList<>();
        filteredList = new ArrayList<>();
        accountsView = view.findViewById(R.id.select_order_list_view);
        search = view.findViewById(R.id.search_order_no_poa_dialogue);

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

        accountsView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        accountsView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(accountsView.getContext(),DividerItemDecoration.VERTICAL);
        accountsView.addItemDecoration(dividerItemDecoration);

        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

//        new Check().execute();
        getPurchaseOrder();

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
        for (PurchaseOrderSelectionList item : purchaseOrderSelectionLists) {
            if (item.getWom_no().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add((item));
                isfiltered = true;
            }
        }
        purchaseOrderForPRCVAdapter.filterList(filteredList);
    }

    public void getPurchaseOrder() {
        waitProgress.show(activity.getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;
        purchaseOrderSelectionLists = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        String url = "http://103.56.208.123:8001/apex/tterp/purchaseReceive/getPoDetails";

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
                        String ad_name = info.getString("ad_name")
                                .equals("null") ? "" : info.getString("ad_name");
                        String ad_address = info.getString("ad_address")
                                .equals("null") ? "" : info.getString("ad_address");
                        String ad_code = info.getString("ad_code")
                                .equals("null") ? "" : info.getString("ad_code");
                        String wom_id = info.getString("wom_id")
                                .equals("null") ? "" : info.getString("wom_id");
                        String wom_no = info.getString("wom_no")
                                .equals("null") ? "" : info.getString("wom_no");
                        String wom_date = info.getString("wom_date")
                                .equals("null") ? "" : info.getString("wom_date");
                        String wom_user = info.getString("wom_user")
                                .equals("null") ? "" : info.getString("wom_user");
                        String wom_ad_id = info.getString("wom_ad_id")
                                .equals("null") ? "" : info.getString("wom_ad_id");
                        String wom_aad_id = info.getString("wom_aad_id")
                                .equals("null") ? "" : info.getString("wom_aad_id");
                        String wom_cid_id = info.getString("wom_cid_id")
                                .equals("null") ? "" : info.getString("wom_cid_id");
                        String wom_type_flag = info.getString("wom_type_flag")
                                .equals("null") ? "" : info.getString("wom_type_flag");
                        String aad_contact_person = info.getString("aad_contact_person")
                                .equals("null") ? "" : info.getString("aad_contact_person");
                        String desig_name = info.getString("desig_name")
                                .equals("null") ? "" : info.getString("desig_name");
                        String balance_qty = info.getString("balance_qty")
                                .equals("null") ? "" : info.getString("balance_qty");
                        String replacement_balance = info.getString("replacement_balance")
                                .equals("null") ? "" : info.getString("replacement_balance");
                        String wom_payment_type = info.getString("wom_payment_type")
                                .equals("null") ? "" : info.getString("wom_payment_type");
                        String wom_supplier_type = info.getString("wom_supplier_type")
                                .equals("null") ? "" : info.getString("wom_supplier_type");
                        String supp_type = info.getString("supp_type")
                                .equals("null") ? "" : info.getString("supp_type");


                        purchaseOrderSelectionLists.add(new PurchaseOrderSelectionList(ad_name, ad_address,ad_code,wom_id,
                                wom_no,wom_date,wom_user,wom_ad_id,wom_aad_id,wom_cid_id,wom_type_flag,aad_contact_person,
                                desig_name,balance_qty,replacement_balance,wom_payment_type,wom_supplier_type,supp_type));
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

                purchaseOrderForPRCVAdapter = new PurchaseOrderForPRCVAdapter(purchaseOrderSelectionLists,getContext(),PurchaseOrderSelectForPRCV.this);
                accountsView.setAdapter(purchaseOrderForPRCVAdapter);
                purchaseOrderForPRCVAdapter.notifyDataSetChanged();
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
                    getPurchaseOrder();
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
                getPurchaseOrder();
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
    public void onPOClicked(int Position) {
        String name = "";
        String id = "";
        String s_name = "";
        String s_code = "";
        String s_adds = "";
        String s_type = "";
        String p_type = "";
        String ad_id = "";
        String aad_id = "";
        String p_type_id = "";
        String supp_id = "";
        String wom_type_flag = "";
        if (isfiltered) {
            name = filteredList.get(Position).getWom_no();
            id = filteredList.get(Position).getWom_id();
            s_name = filteredList.get(Position).getAd_name();
            s_code = filteredList.get(Position).getAd_code();
            s_adds = filteredList.get(Position).getAd_address();
            s_type = filteredList.get(Position).getSupp_type();
            p_type = filteredList.get(Position).getWom_payment_type();
            ad_id = filteredList.get(Position).getWom_ad_id();
            aad_id = filteredList.get(Position).getWom_aad_id();
            p_type_id = filteredList.get(Position).getWom_payment_type();
            supp_id = filteredList.get(Position).getWom_supplier_type();
            wom_type_flag = filteredList.get(Position).getWom_type_flag();
        } else {
            name = purchaseOrderSelectionLists.get(Position).getWom_no();
            id = purchaseOrderSelectionLists.get(Position).getWom_id();
            s_name = purchaseOrderSelectionLists.get(Position).getAd_name();
            s_code = purchaseOrderSelectionLists.get(Position).getAd_code();
            s_adds = purchaseOrderSelectionLists.get(Position).getAd_address();
            s_type = purchaseOrderSelectionLists.get(Position).getSupp_type();
            p_type = purchaseOrderSelectionLists.get(Position).getWom_payment_type();
            ad_id = purchaseOrderSelectionLists.get(Position).getWom_ad_id();
            aad_id = purchaseOrderSelectionLists.get(Position).getWom_aad_id();
            p_type_id = purchaseOrderSelectionLists.get(Position).getWom_payment_type();
            supp_id = purchaseOrderSelectionLists.get(Position).getWom_supplier_type();
            wom_type_flag = purchaseOrderSelectionLists.get(Position).getWom_type_flag();
        }
        if (!p_type.isEmpty()) {
            if (p_type.equals("1")) {
                p_type = "Cash/Cheque";
            }
            else if (p_type.equals("2")) {
                p_type = "Credit/LC";
            }
        }

        PurchaseReceive.supplier_name = s_name;
        PurchaseReceive.supplier_code = s_code;
        PurchaseReceive.supplier_address = s_adds;
        PurchaseReceive.supplier_type = s_type;
        PurchaseReceive.payment_type = p_type;
        PurchaseReceive.ad_id_for_p_rcv = ad_id;
        PurchaseReceive.aad_id_for_p_rcv = aad_id;
        PurchaseReceive.p_type_id_for_p_rcv = p_type_id;
        PurchaseReceive.supplier_type_Id_for_p_rcv = supp_id;
        PurchaseReceive.wom_type_flag_for_p_rcv = wom_type_flag;
        wom_id_for_p_rcv = id;
        purOrderSelection.setText(name);
        alertDialog.dismiss();
    }
}

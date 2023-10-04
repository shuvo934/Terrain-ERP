package ttit.com.shuvo.terrainerp.dialogues;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.WaitProgress;

public class SingleItemShow extends AppCompatDialogFragment {

    TextView itemName;
    TextView itemCode;
    TextView hsCode;
    TextView partNumber;
    TextView itemUnit;
    TextView stockQty;

    String item_name = "";
    String item_code = "";
    String hs_code = "";
    String part_number = "";
    String item_unit = "";
    String stk_qty = "";

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    AlertDialog alertDialog;
    AppCompatActivity activity;

    String cat_id;
    String req_date;
    String itemId;
    Context mContext;

    public SingleItemShow(String cat_id, String req_date, String itemId, Context mContext) {
        this.cat_id = cat_id;
        this.req_date = req_date;
        this.itemId = itemId;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.item_details_select_to_show, null);

        activity = (AppCompatActivity) view.getContext();

        itemName = view.findViewById(R.id.item_name_single_selection_pr);
        itemCode = view.findViewById(R.id.item_code_single_selection_pr);
        hsCode = view.findViewById(R.id.hs_code_single_selection_pr);
        partNumber = view.findViewById(R.id.part_number_single_selection_pr);
        itemUnit = view.findViewById(R.id.unit_single_selection_pr);
        stockQty = view.findViewById(R.id.stock_qty_single_selection_pr);

        getSingleItem();

        builder.setView(view);

        alertDialog = builder.create();

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", (dialog, which) -> alertDialog.dismiss());

        return alertDialog;
    }

    public void getSingleItem() {
        waitProgress.show(activity.getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;
        item_name = "";
        item_code = "";
        hs_code = "";
        part_number = "";
        item_unit = "";
        stk_qty = "";
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        String url = "http://103.56.208.123:8001/apex/tterp/purchaseReq/getSingleItemDetails?PRM_IM_ID="+cat_id+"&PRM_REQ_DATE="+req_date+"&P_ITEM_ID="+itemId+"&PRM_CCPP_ID=";

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
                        item_name = info.getString("item_reference_name")
                                .equals("null") ? "" : info.getString("item_reference_name");
                        hs_code = info.getString("item_hs_code")
                                .equals("null") ? "" : info.getString("item_hs_code");
                        part_number = info.getString("item_part_number")
                                .equals("null") ? "" : info.getString("item_part_number");
                        item_unit = info.getString("item_unit")
                                .equals("null") ? "" : info.getString("item_unit");
                        item_code = info.getString("item_code")
                                .equals("null") ? "" : info.getString("item_code");
                        stk_qty = info.getString("stock")
                                .equals("null") ? "0" : info.getString("stock");

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

                itemName.setText(item_name);
                itemCode.setText(item_code);
                hsCode.setText(hs_code);
                partNumber.setText(part_number);
                itemUnit.setText(item_unit);
                stockQty.setText(stk_qty);
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

                    getSingleItem();
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

                getSingleItem();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                alertDialog.dismiss();
            });
        }
    }
}

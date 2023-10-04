package ttit.com.shuvo.terrainerp.dialogues;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ttit.com.shuvo.terrainerp.R;

import static ttit.com.shuvo.terrainerp.fragments.RelationsFragment.itemNameList;
import static ttit.com.shuvo.terrainerp.fragments.RelationsFragment.re_amnt;
import static ttit.com.shuvo.terrainerp.fragments.RelationsFragment.re_qty;
import static ttit.com.shuvo.terrainerp.fragments.RelationsFragment.rebl_amnt;
import static ttit.com.shuvo.terrainerp.fragments.RelationsFragment.rebl_qty;
import static ttit.com.shuvo.terrainerp.fragments.RelationsFragment.w_o_amnt;
import static ttit.com.shuvo.terrainerp.fragments.RelationsFragment.w_o_date;
import static ttit.com.shuvo.terrainerp.fragments.RelationsFragment.w_o_qty;
import static ttit.com.shuvo.terrainerp.fragments.RelationsFragment.w_o_rate;

public class ItemDetailsRelation extends AppCompatDialogFragment {

    TextView itemName;
    TextView wODate;
    TextView wOQty;
    TextView wORate;
    TextView wOAmnt;
    TextView reQty;
    TextView reAmnt;
    TextView reblQty;
    TextView reblAmnt;

    AlertDialog alertDialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.item_dialogue_details, null);

        itemName = view.findViewById(R.id.item_name_from_list);
        wODate = view.findViewById(R.id.w_o_date);
        wOQty = view.findViewById(R.id.w_o_qty);
        wORate = view.findViewById(R.id.w_o_rate);
        wOAmnt = view.findViewById(R.id.w_o_amount);
        reQty = view.findViewById(R.id.r_o_qty);
        reAmnt = view.findViewById(R.id.r_o_amnt);
        reblQty = view.findViewById(R.id.r_o_bl_qty);
        reblAmnt = view.findViewById(R.id.r_o_bl_amnt);

        builder.setView(view);

        alertDialog = builder.create();

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy, hh:mm a",Locale.getDefault());
        Date date = null;

        try {
            date = sd.parse(w_o_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            w_o_date = sdf.format(date);
        }


        itemName.setText(itemNameList);
        wODate.setText(w_o_date);
        wOQty.setText(w_o_qty);
        wORate.setText(w_o_rate + " BDT");
        wOAmnt.setText(w_o_amnt + " BDT");
        reQty.setText(re_qty);
        reAmnt.setText(re_amnt + " BDT");
        reblQty.setText(rebl_qty);
        reblAmnt.setText(rebl_amnt + " BDT");


        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        return alertDialog;
    }
}

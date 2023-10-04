package ttit.com.shuvo.terrainerp.dialogues;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import ttit.com.shuvo.terrainerp.R;

public class PRcvSingleItemDetailsDial extends AppCompatDialogFragment {
    TextView itemName;
    TextView itemCode;
    TextView hsCode;
    TextView partNumber;
    TextView itemUnit;
    TextView sizeName;
    TextView colorName;
    TextView actualRate;

    AlertDialog alertDialog;
    AppCompatActivity activity;

    String item_name;
    String item_code;
    String hs_code;
    String part_number;
    String item_unit;
    String size_name;
    String color_name;
    String actual_rate;
    Context mContext;

    public PRcvSingleItemDetailsDial(String item_name, String item_code, String hs_code, String part_number, String item_unit, String size_name, String color_name, String actual_rate, Context mContext) {
        this.item_name = item_name;
        this.item_code = item_code;
        this.hs_code = hs_code;
        this.part_number = part_number;
        this.item_unit = item_unit;
        this.size_name = size_name;
        this.color_name = color_name;
        this.actual_rate = actual_rate;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.p_rcv_item_details_show_dial_view, null);

        activity = (AppCompatActivity) view.getContext();

        itemName = view.findViewById(R.id.item_name_selected_to_show_prcv);
        itemCode = view.findViewById(R.id.item_code_selected_to_show_prcv);
        hsCode = view.findViewById(R.id.hs_code_selected_to_show_prcv);
        partNumber = view.findViewById(R.id.part_number_selected_to_show_prcv);
        itemUnit = view.findViewById(R.id.unit_selected_to_show_prcv);
        sizeName = view.findViewById(R.id.size_name_selected_to_show_prcv);
        colorName = view.findViewById(R.id.color_name_selected_to_show_prcv);
        actualRate = view.findViewById(R.id.actual_rate_selected_to_show_prcv);


        itemName.setText(item_name);
        itemCode.setText(item_code);
        hsCode.setText(hs_code);
        partNumber.setText(part_number);
        itemUnit.setText(item_unit);
        sizeName.setText(size_name);
        colorName.setText(color_name);
        actualRate.setText(actual_rate);

        builder.setView(view);

        alertDialog = builder.create();

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", (dialog, which) -> alertDialog.dismiss());

        return alertDialog;
    }
}

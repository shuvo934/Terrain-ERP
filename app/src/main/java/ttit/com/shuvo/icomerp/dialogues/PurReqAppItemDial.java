package ttit.com.shuvo.icomerp.dialogues;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.adapters.PurchaseReqAppItemAdapter;
import ttit.com.shuvo.icomerp.arrayList.PurchaseReqAppItemlists;
import ttit.com.shuvo.icomerp.fragments.PurchaseReqApproved;

public class PurReqAppItemDial extends AppCompatDialogFragment {

    TextView purReqNo;

    RecyclerView itemView;
    RecyclerView.LayoutManager layoutManager;
    PurchaseReqAppItemAdapter purchaseReqAppItemAdapter;

    AlertDialog alertDialog;
    AppCompatActivity activity;

    ArrayList<PurchaseReqAppItemlists> purchaseReqAppItemlistsNew;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.pra_item_details_view, null);

        activity = (AppCompatActivity) view.getContext();
        purReqNo = view.findViewById(R.id.purchase_requisiton_no_item_details);

        itemView = view.findViewById(R.id.pra_item_details_report_view);
        purchaseReqAppItemlistsNew = new ArrayList<>();

        purchaseReqAppItemlistsNew = PurchaseReqApproved.purchaseReqAppItemlists;

        purReqNo.setText(Objects.requireNonNull(PurchaseReqApproved.purReqSelectSpinner.getText()).toString());

        itemView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        itemView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(itemView.getContext(),DividerItemDecoration.VERTICAL);
        itemView.addItemDecoration(dividerItemDecoration);

        purchaseReqAppItemAdapter = new PurchaseReqAppItemAdapter(purchaseReqAppItemlistsNew,getContext());
        itemView.setAdapter(purchaseReqAppItemAdapter);

        builder.setView(view);

        alertDialog = builder.create();

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        return alertDialog;
    }
}

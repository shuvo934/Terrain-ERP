package ttit.com.shuvo.icomerp.dialogues;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.adapters.SupplierAdapter;
import ttit.com.shuvo.icomerp.arrayList.SupplierList;

import static ttit.com.shuvo.icomerp.fragments.ReceiveFragment.fromRE_supp;
import static ttit.com.shuvo.icomerp.fragments.ReceiveFragment.supplierLayRe;
import static ttit.com.shuvo.icomerp.fragments.ReceiveFragment.supplierListsRe;
import static ttit.com.shuvo.icomerp.fragments.ReceiveFragment.supplierRe;
import static ttit.com.shuvo.icomerp.fragments.ReceiveFragment.supplier_ad_id_re;
import static ttit.com.shuvo.icomerp.fragments.RelationsFragment.fromRela_supp;
import static ttit.com.shuvo.icomerp.fragments.RelationsFragment.supplierLayRelation;
import static ttit.com.shuvo.icomerp.fragments.RelationsFragment.supplierListsRelations;
import static ttit.com.shuvo.icomerp.fragments.RelationsFragment.supplierRelation;
import static ttit.com.shuvo.icomerp.fragments.RelationsFragment.supplier_ad_id_relation;
import static ttit.com.shuvo.icomerp.fragments.WorkOrderFragment.fromWO_supp;
import static ttit.com.shuvo.icomerp.fragments.WorkOrderFragment.supplier;
import static ttit.com.shuvo.icomerp.fragments.WorkOrderFragment.supplierLay;
import static ttit.com.shuvo.icomerp.fragments.WorkOrderFragment.supplierLists;
import static ttit.com.shuvo.icomerp.fragments.WorkOrderFragment.supplier_ad_id;

public class SupplierDialogue extends AppCompatDialogFragment implements SupplierAdapter.ClickedItem {

    RecyclerView supplierView;
    SupplierAdapter supplierAdapter;
    RecyclerView.LayoutManager layoutManager;

    AlertDialog dialog;

    ArrayList<SupplierList> selectedSupplierList;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.supplier_dialogue, null);

        selectedSupplierList = new ArrayList<>();
        supplierView = view.findViewById(R.id.supplier_name_recycler);

        builder.setView(view);

        dialog = builder.create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        if (fromWO_supp == 1) {
            selectedSupplierList = supplierLists;
        } else if (fromRE_supp == 1) {
            selectedSupplierList = supplierListsRe;
        } else if (fromRela_supp == 1) {
            selectedSupplierList = supplierListsRelations;
        }



        supplierView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        supplierView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(supplierView.getContext(),DividerItemDecoration.VERTICAL);
        supplierView.addItemDecoration(dividerItemDecoration);
        supplierAdapter = new SupplierAdapter(selectedSupplierList, getContext(),this);
        supplierView.setAdapter(supplierAdapter);

        dialog.setButton(Dialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                fromWO_supp = 0;
                fromRE_supp = 0;
                fromRela_supp = 0;
                dialog.dismiss();
            }
        });

        return dialog;

    }

    @Override
    public void onCategoryClicked(int CategoryPosition) {
        String name = "";
        String id = "";
        name = selectedSupplierList.get(CategoryPosition).getAd_name();
        id = selectedSupplierList.get(CategoryPosition).getAd_id();

        if (fromWO_supp == 1) {
            supplierLay.setHint("Supplier");
            supplier.setText(name);
            supplier_ad_id = id;

        } else if (fromRE_supp == 1) {
            supplierLayRe.setHint("Supplier");
            supplierRe.setText(name);
            supplier_ad_id_re = id;
        } else if (fromRela_supp == 1) {
            supplierLayRelation.setHint("Supplier");
            supplierRelation.setText(name);
            supplier_ad_id_relation = id;
        }
        fromWO_supp = 0;
        fromRE_supp = 0;
        fromRela_supp = 0;
        dialog.dismiss();
    }
}

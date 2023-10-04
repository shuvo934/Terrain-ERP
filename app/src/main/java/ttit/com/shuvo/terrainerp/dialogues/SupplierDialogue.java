package ttit.com.shuvo.terrainerp.dialogues;

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

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.adapters.SupplierAdapter;
import ttit.com.shuvo.terrainerp.arrayList.SupplierList;

import static ttit.com.shuvo.terrainerp.fragments.PurchaseOrder.contact_person_needed;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseOrder.selected_ad_id_for_po;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseOrder.fromforPO_supp;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseOrder.supplierPO;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseOrder.supplierPOLay;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseOrder.supplier_address;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseOrder.supplier_code;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseOrder.supplierforPOLists;
import static ttit.com.shuvo.terrainerp.fragments.ReceiveFragment.fromRE_supp;
import static ttit.com.shuvo.terrainerp.fragments.ReceiveFragment.supplierLayRe;
import static ttit.com.shuvo.terrainerp.fragments.ReceiveFragment.supplierListsRe;
import static ttit.com.shuvo.terrainerp.fragments.ReceiveFragment.supplierRe;
import static ttit.com.shuvo.terrainerp.fragments.ReceiveFragment.supplier_ad_id_re;
import static ttit.com.shuvo.terrainerp.fragments.RelationsFragment.fromRela_supp;
import static ttit.com.shuvo.terrainerp.fragments.RelationsFragment.supplierLayRelation;
import static ttit.com.shuvo.terrainerp.fragments.RelationsFragment.supplierListsRelations;
import static ttit.com.shuvo.terrainerp.fragments.RelationsFragment.supplierRelation;
import static ttit.com.shuvo.terrainerp.fragments.RelationsFragment.supplier_ad_id_relation;
import static ttit.com.shuvo.terrainerp.fragments.WorkOrderFragment.fromWO_supp;
import static ttit.com.shuvo.terrainerp.fragments.WorkOrderFragment.supplier;
import static ttit.com.shuvo.terrainerp.fragments.WorkOrderFragment.supplierLay;
import static ttit.com.shuvo.terrainerp.fragments.WorkOrderFragment.supplierLists;
import static ttit.com.shuvo.terrainerp.fragments.WorkOrderFragment.supplier_ad_id;

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
        else if (fromforPO_supp == 1) {
            selectedSupplierList = supplierforPOLists;
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
                fromforPO_supp = 0;
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
        String code = selectedSupplierList.get(CategoryPosition).getAd_code();
        String address = selectedSupplierList.get(CategoryPosition).getAd_address();

        if (fromWO_supp == 1) {
            supplierLay.setHint("Supplier");
            supplier_ad_id = id;
            supplier.setText(name);
        } else if (fromRE_supp == 1) {
            supplierLayRe.setHint("Supplier");
            supplier_ad_id_re = id;
            supplierRe.setText(name);
        } else if (fromRela_supp == 1) {
            supplierLayRelation.setHint("Supplier");
            supplier_ad_id_relation = id;
            supplierRelation.setText(name);
        }
        else if (fromforPO_supp == 1) {
            contact_person_needed = true;
            supplierPOLay.setHint("Supplier");
            selected_ad_id_for_po = id;
            supplier_code = code;
            supplier_address = address;
            supplierPO.setText(name);
        }
        fromWO_supp = 0;
        fromRE_supp = 0;
        fromRela_supp = 0;
        fromforPO_supp = 0;
        dialog.dismiss();
    }
}

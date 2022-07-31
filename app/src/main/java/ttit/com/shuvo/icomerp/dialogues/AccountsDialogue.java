package ttit.com.shuvo.icomerp.dialogues;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.adapters.AccountsAdapter;
import ttit.com.shuvo.icomerp.arrayList.ItemRcvIssReglist;
import ttit.com.shuvo.icomerp.arrayList.ReceiveTypeList;

import static ttit.com.shuvo.icomerp.fragments.AccountLedger.accLedgerSpinner;
import static ttit.com.shuvo.icomerp.fragments.AccountLedger.accLists;
import static ttit.com.shuvo.icomerp.fragments.AccountLedger.ad_id;


public class AccountsDialogue extends AppCompatDialogFragment implements AccountsAdapter.ClickedItem {

    RecyclerView accountsView;
    AccountsAdapter accountsAdapter;
    RecyclerView.LayoutManager layoutManager;

    TextInputEditText search;

    AlertDialog dialog;

    String searchingName = "";

    ArrayList<ReceiveTypeList> accountsLists;
    ArrayList<ReceiveTypeList> filteredList;

    Boolean isfiltered = false;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.accounts_dialogue, null);

        accountsLists = new ArrayList<>();
        filteredList = new ArrayList<>();
        accountsView = view.findViewById(R.id.accounts_list_view);
        search = view.findViewById(R.id.search_ac_no_acc_dialogue);

        builder.setView(view);

        dialog = builder.create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        accountsLists = accLists;

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

        accountsAdapter = new AccountsAdapter(accountsLists, getContext(),this);
        accountsView.setAdapter(accountsAdapter);

        dialog.setButton(Dialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        return dialog;
    }

    @Override
    public void onCategoryClicked(int CategoryPosition) {
        String name = "";
        String id = "";
        if (isfiltered) {
            name = filteredList.get(CategoryPosition).getType();
            id = filteredList.get(CategoryPosition).getId();
        } else {
            name = accountsLists.get(CategoryPosition).getType();
            id = accountsLists.get(CategoryPosition).getId();
        }

        System.out.println(name);

        accLedgerSpinner.setText(name);
        ad_id = id;
        dialog.dismiss();
    }

    private void closeKeyBoard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void filter(String text) {

        filteredList = new ArrayList<>();
        for (ReceiveTypeList item : accountsLists) {
            if (item.getType().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add((item));
                isfiltered = true;
            }
        }
        accountsAdapter.filterList(filteredList);
    }
}

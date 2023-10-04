package ttit.com.shuvo.terrainerp.dialogues;

import static ttit.com.shuvo.terrainerp.fragments.PurchaseOrder.designatedUser;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseOrder.designated_user_id;
import static ttit.com.shuvo.terrainerp.fragments.PurchaseOrder.designated_user_name;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.adapters.DesignatedUserSelectionAdapter;
import ttit.com.shuvo.terrainerp.arrayList.DesignatedUserList;

public class DesignatedUserSelectDial extends AppCompatDialogFragment implements DesignatedUserSelectionAdapter.ClickedItem {
    RecyclerView recyclerView;
    DesignatedUserSelectionAdapter designatedUserSelectionAdapter;
    RecyclerView.LayoutManager layoutManager;
    TextInputEditText search;
    AlertDialog alertDialog;
    String searchingName = "";

    ArrayList<DesignatedUserList> designatedUserLists;
    ArrayList<DesignatedUserList> filteredList;
    Boolean isfiltered = false;

    AppCompatActivity activity;

    Context mContext;

    public DesignatedUserSelectDial(ArrayList<DesignatedUserList> designatedUserLists, Context mContext) {
        this.designatedUserLists = designatedUserLists;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.designated_user_select_list_dial, null);

        activity = (AppCompatActivity) view.getContext();

        filteredList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.select_designated_user_list_view);
        search = view.findViewById(R.id.search_user_name_for_desig_user_dialogue);

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

        designatedUserSelectionAdapter = new DesignatedUserSelectionAdapter(designatedUserLists,getContext(),DesignatedUserSelectDial.this);
        recyclerView.setAdapter(designatedUserSelectionAdapter);

        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

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
        for (DesignatedUserList item : designatedUserLists) {
            if (item.getUser_name().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add((item));
                isfiltered = true;
            }
        }
        designatedUserSelectionAdapter.filterList(filteredList);
    }

    @Override
    public void onCategoryClicked(int CategoryPosition) {
        String name = "";
        String id = "";
        name = designatedUserLists.get(CategoryPosition).getUser_name();
        id = designatedUserLists.get(CategoryPosition).getUsr_name();

        designated_user_id = id;
        designated_user_name = name;
        designatedUser.setText(name);
        alertDialog.dismiss();
    }
}

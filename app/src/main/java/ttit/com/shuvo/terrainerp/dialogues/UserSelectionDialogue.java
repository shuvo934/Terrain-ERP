package ttit.com.shuvo.terrainerp.dialogues;

import static ttit.com.shuvo.terrainerp.fragments.UserSetupFragment.selected_user_id;
import static ttit.com.shuvo.terrainerp.fragments.UserSetupFragment.userSelectSpinner;
import static ttit.com.shuvo.terrainerp.fragments.UserSetupFragment.userSetupUserLists;

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
import ttit.com.shuvo.terrainerp.adapters.UserSelectionAdapter;
import ttit.com.shuvo.terrainerp.arrayList.UserSetupUserList;

public class UserSelectionDialogue extends AppCompatDialogFragment implements UserSelectionAdapter.ClickedItem {

    TextInputEditText search;

    AlertDialog alertDialog;

    RecyclerView userView;
    UserSelectionAdapter userSelectionAdapter;
    RecyclerView.LayoutManager layoutManager;

    String searchingName = "";

    ArrayList<UserSetupUserList> userLists;
    ArrayList<UserSetupUserList> filteredList;

    Boolean isfiltered = false;

    AppCompatActivity activity;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.user_select_list_view, null);

        activity = (AppCompatActivity) view.getContext();

        userLists = new ArrayList<>();
        filteredList = new ArrayList<>();
        userView = view.findViewById(R.id.select_user_list_view);
        search = view.findViewById(R.id.search_user_name_dialogue);

        builder.setView(view);

        alertDialog = builder.create();

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        userLists = userSetupUserLists;
        userView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        userView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(userView.getContext(),DividerItemDecoration.VERTICAL);
        userView.addItemDecoration(dividerItemDecoration);
        userSelectionAdapter = new UserSelectionAdapter(userLists,getContext(),UserSelectionDialogue.this);
        userView.setAdapter(userSelectionAdapter);

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
        for (UserSetupUserList item : userLists) {
            String fName = item.getUser_f_name();
            String lname = item.getUser_l_name();
            String u_name = item.getUser_name();
            String fullName = fName + " " + lname + " ("+u_name+")";
            if (fullName.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add((item));
                isfiltered = true;
            }
        }
        userSelectionAdapter.filterList(filteredList);
    }

    @Override
    public void onCategoryClicked(int CategoryPosition) {
        String name = "";
        String id = "";
        if (isfiltered) {
            String fName = filteredList.get(CategoryPosition).getUser_f_name();
            String lname = filteredList.get(CategoryPosition).getUser_l_name();
            String u_name = filteredList.get(CategoryPosition).getUser_name();
            name = fName + " " + lname + " ("+u_name+")";
            id = filteredList.get(CategoryPosition).getUser_id();
        }
        else {
            String fName = userLists.get(CategoryPosition).getUser_f_name();
            String lname = userLists.get(CategoryPosition).getUser_l_name();
            String u_name = userLists.get(CategoryPosition).getUser_name();
            name = fName + " " + lname + " ("+u_name+")";
            id = userLists.get(CategoryPosition).getUser_id();
        }

        selected_user_id = id;
        userSelectSpinner.setText(name);
        alertDialog.dismiss();
    }
}

package ttit.com.shuvo.icomerp.dialogues;

import static ttit.com.shuvo.icomerp.adapters.UserGroupAdapter.groupModuleLists;
import static ttit.com.shuvo.icomerp.adapters.UserGroupAdapter.selectedPosition;
import static ttit.com.shuvo.icomerp.fragments.UserSetupFragment.userGroupLists;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.adapters.UserGroupModuleAdapter;

public class UserGroupModuleDialogue extends AppCompatDialogFragment {

    AlertDialog alertDialog;

    RecyclerView moduleView;
    UserGroupModuleAdapter userGroupModuleAdapter;
    RecyclerView.LayoutManager layoutManager;

    AppCompatActivity activity;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.user_group_module_selection_dialogue, null);

        activity = (AppCompatActivity) view.getContext();

        moduleView = view.findViewById(R.id.choose_group_module_view);

        builder.setView(view);

        alertDialog = builder.create();

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        moduleView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        moduleView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(moduleView.getContext(),DividerItemDecoration.VERTICAL);
        moduleView.addItemDecoration(dividerItemDecoration);

        userGroupModuleAdapter = new UserGroupModuleAdapter(groupModuleLists,getContext());
        moduleView.setAdapter(userGroupModuleAdapter);

        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                userGroupLists.get(selectedPosition).setUserGroupModuleLists(groupModuleLists);
                dialog.dismiss();
            }
        });

        return alertDialog;
    }
}

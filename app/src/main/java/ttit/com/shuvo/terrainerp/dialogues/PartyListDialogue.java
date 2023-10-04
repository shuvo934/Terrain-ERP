package ttit.com.shuvo.terrainerp.dialogues;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.WaitProgress;
import ttit.com.shuvo.terrainerp.adapters.PartyContactAdapter;
import ttit.com.shuvo.terrainerp.adapters.PartyFactoryAdapter;
import ttit.com.shuvo.terrainerp.arrayList.PartyContactList;
import ttit.com.shuvo.terrainerp.arrayList.PartyFactoryList;
import static ttit.com.shuvo.terrainerp.fragments.PartyAll.ad_id_pal;
import static ttit.com.shuvo.terrainerp.fragments.PartyAll.ad_name_pal;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PartyListDialogue extends AppCompatDialogFragment {

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
//    private Connection connection;

    TextView anyName;
    TextView noFactoryMsg;
    TextView noContactMsg;

    RecyclerView factoryView;
    RecyclerView.LayoutManager layoutManagerFac;
    PartyFactoryAdapter partyFactoryAdapter;

    RecyclerView contactView;
    RecyclerView.LayoutManager layoutManagerCon;
    PartyContactAdapter partyContactAdapter;

    AlertDialog alertDialog;
    AppCompatActivity activity;
    String ad_id = "";

    ArrayList<PartyContactList> partyContactLists;
    ArrayList<PartyFactoryList> partyFactoryLists;
    Context mContext;
    public PartyListDialogue(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.party_details_view_pal, null);

        activity = (AppCompatActivity) view.getContext();

        anyName = view.findViewById(R.id.party_name_from_list);
        noFactoryMsg = view.findViewById(R.id.no_factory_found_msg);
        noContactMsg = view.findViewById(R.id.no_contact_found_msg);
        factoryView = view.findViewById(R.id.factory_details_report_view);
        contactView = view.findViewById(R.id.contact_details_report_view);

        partyContactLists = new ArrayList<>();
        partyFactoryLists = new ArrayList<>();

        factoryView.setHasFixedSize(true);
        layoutManagerFac = new LinearLayoutManager(getContext());
        factoryView.setLayoutManager(layoutManagerFac);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(factoryView.getContext(),DividerItemDecoration.VERTICAL);
        factoryView.addItemDecoration(dividerItemDecoration);

        contactView.setHasFixedSize(true);
        layoutManagerCon = new LinearLayoutManager(getContext());
        contactView.setLayoutManager(layoutManagerCon);

        DividerItemDecoration dividerItemDecoration1 =
                new DividerItemDecoration(contactView.getContext(),DividerItemDecoration.VERTICAL);
        contactView.addItemDecoration(dividerItemDecoration1);

        ad_id = ad_id_pal;
        String name = ad_name_pal;
        anyName.setText(name);

//        new CheckFORLISt().execute();
        getPartyList();

        builder.setView(view);

        alertDialog = builder.create();

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        return alertDialog;
    }

//    public boolean isConnected() {
//        boolean connected = false;
//        boolean isMobile = false;
//        try {
//            ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo nInfo = cm.getActiveNetworkInfo();
//            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
//            return connected;
//        } catch (Exception e) {
//            Log.e("Connectivity Exception", e.getMessage());
//        }
//        return connected;
//    }
//
//    public boolean isOnline() {
//
//        Runtime runtime = Runtime.getRuntime();
//        try {
//            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
//            int     exitValue = ipProcess.waitFor();
//            return (exitValue == 0);
//        }
//        catch (IOException | InterruptedException e)          { e.printStackTrace(); }
//
//        return false;
//    }
//
//    public class CheckFORLISt extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            waitProgress.show(activity.getSupportFragmentManager(),"WaitBar");
//            waitProgress.setCancelable(false);
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            if (isConnected() && isOnline()) {
//
//                ItemData();
//                if (connected) {
//                    conn = true;
//                }
//
//            } else {
//                conn = false;
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//
//
//            if (conn) {
//
//                conn = false;
//                connected = false;
//
//                partyContactAdapter = new PartyContactAdapter(partyContactLists,getContext());
//                contactView.setAdapter(partyContactAdapter);
//                partyContactAdapter.notifyDataSetChanged();
//
//                partyFactoryAdapter = new PartyFactoryAdapter(partyFactoryLists,getContext());
//                factoryView.setAdapter(partyFactoryAdapter);
//                partyFactoryAdapter.notifyDataSetChanged();
//
//                if (partyContactLists.size() == 0) {
//                    noContactMsg.setVisibility(View.VISIBLE);
//                } else {
//                    noContactMsg.setVisibility(View.GONE);
//                }
//
//                if (partyFactoryLists.size() == 0) {
//                    noFactoryMsg.setVisibility(View.VISIBLE);
//                } else {
//                    noFactoryMsg.setVisibility(View.GONE);
//                }
//
//
//                waitProgress.dismiss();
//
//            }
//            else {
//                waitProgress.dismiss();
//                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog;
//                dialog = new AlertDialog.Builder(getContext())
//                        .setMessage("Please Check Your Internet Connection")
//                        .setPositiveButton("Retry", null)
//                        .setNegativeButton("Cancel",null)
//                        .show();
//
//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
//                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                positive.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        new CheckFORLISt().execute();
//                        dialog.dismiss();
//                    }
//                });
//
//                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                negative.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                        alertDialog.dismiss();
//
//                    }
//                });
//            }
//        }
//
//    }
//
//    public void ItemData() {
//        try {
//            this.connection = createConnection();
//
//            partyContactLists = new ArrayList<>();
//            partyFactoryLists = new ArrayList<>();
//
//            Statement statement = connection.createStatement();
//
//            ResultSet resultSet = statement.executeQuery("select afd_factory_name, afd_factory_address, afd_contact_person \n" +
//                    "from acc_factory_dtl \n" +
//                    "where afd_ad_id = "+ad_id+"");
//
//            while (resultSet.next()) {
//                partyFactoryLists.add(new PartyFactoryList(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3)));
//            }
//
//            resultSet.close();
//
//            ResultSet resultSet1 = statement.executeQuery("select aad_contact_person, \n" +
//                    "(select desig_name from desig_mst where desig_id = aad_desig_id) as DESIGNATION,\n" +
//                    "(select dept_name from dept_mst where dept_id = aad_dept_id) as DEPARTMENT,\n" +
//                    "aad_contact_no, aad_email\n" +
//                    "from acc_attention_dtl \n" +
//                    "where aad_ad_id = "+ad_id+"");
//
//            while (resultSet1.next()) {
//                partyContactLists.add(new PartyContactList(resultSet1.getString(1),resultSet1.getString(2),resultSet1.getString(3),
//                        resultSet1.getString(4),resultSet1.getString(5)));
//            }
//
//            resultSet1.close();
//
//            statement.close();
//
//            connected = true;
//
//            connection.close();
//
//        }
//        catch (Exception e) {
//
//            Log.i("ERRRRR", e.getLocalizedMessage());
//            e.printStackTrace();
//        }
//    }

    public void getPartyList() {
        waitProgress.show(activity.getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;
        partyContactLists = new ArrayList<>();
        partyFactoryLists = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        String partyFacUrl = "http://103.56.208.123:8001/apex/tterp/dialogs/getpartyFactory?ad_id="+ad_id+"";
        String partyContactUrl = "http://103.56.208.123:8001/apex/tterp/dialogs/getPartyContact?ad_id="+ad_id+"";

        StringRequest partyContactReq = new StringRequest(Request.Method.GET, partyContactUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        String aad_contact_person = info.getString("aad_contact_person")
                                .equals("null") ? "" : info.getString("aad_contact_person");
                        String designation = info.getString("designation")
                                .equals("null") ? "" : info.getString("designation");
                        String department = info.getString("department")
                                .equals("null") ? "" : info.getString("department");
                        String aad_contact_no = info.getString("aad_contact_no")
                                .equals("null") ? "" : info.getString("aad_contact_no");
                        String aad_email = info.getString("aad_email")
                                .equals("null") ? "" : info.getString("aad_email");
                        partyContactLists.add(new PartyContactList(aad_contact_person,designation,department,
                                aad_contact_no,aad_email));
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

        StringRequest partyFacReq = new StringRequest(Request.Method.GET, partyFacUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        String afd_factory_name = info.getString("afd_factory_name")
                                .equals("null") ? "" : info.getString("afd_factory_name");
                        String afd_factory_address = info.getString("afd_factory_address")
                                .equals("null") ? "" : info.getString("afd_factory_address");
                        String afd_contact_person = info.getString("afd_contact_person")
                                .equals("null") ? "" : info.getString("afd_contact_person");
                        partyFactoryLists.add(new PartyFactoryList(afd_factory_name,afd_factory_address,afd_contact_person));
                    }
                }
                requestQueue.add(partyContactReq);
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

        requestQueue.add(partyFacReq);

    }

    private void updateFragment() {
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                partyContactAdapter = new PartyContactAdapter(partyContactLists,getContext());
                contactView.setAdapter(partyContactAdapter);
                partyContactAdapter.notifyDataSetChanged();

                partyFactoryAdapter = new PartyFactoryAdapter(partyFactoryLists,getContext());
                factoryView.setAdapter(partyFactoryAdapter);
                partyFactoryAdapter.notifyDataSetChanged();

                if (partyContactLists.size() == 0) {
                    noContactMsg.setVisibility(View.VISIBLE);
                } else {
                    noContactMsg.setVisibility(View.GONE);
                }

                if (partyFactoryLists.size() == 0) {
                    noFactoryMsg.setVisibility(View.VISIBLE);
                } else {
                    noFactoryMsg.setVisibility(View.GONE);
                }


                waitProgress.dismiss();
            }
            else {
                waitProgress.dismiss();
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

                    getPartyList();
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
            waitProgress.dismiss();
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

                getPartyList();
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

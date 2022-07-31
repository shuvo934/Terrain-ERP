package ttit.com.shuvo.icomerp.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.rosemaryapp.amazingspinner.AmazingSpinner;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.WaitProgress;
import ttit.com.shuvo.icomerp.adapters.LevelOneAdapter;
import ttit.com.shuvo.icomerp.adapters.LevelThreeAdapter;
import ttit.com.shuvo.icomerp.adapters.LevelTwoAdapter;
import ttit.com.shuvo.icomerp.arrayList.LevelThreeLists;
import ttit.com.shuvo.icomerp.arrayList.LevelWiseLists;
import ttit.com.shuvo.icomerp.arrayList.ReceiveTypeList;

import static ttit.com.shuvo.icomerp.OracleConnection.createConnection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChartAccounts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChartAccounts extends Fragment implements LevelOneAdapter.ClickedItem, LevelTwoAdapter.ClickedItem2 {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
    Boolean isfiltered = false;
    private Connection connection;

    public static int selectedPosition = 0;
    public static int selectedPosition2 = 0;

    AmazingSpinner typeSpinner;

    RecyclerView levelOneView;
    LevelOneAdapter levelOneAdapter;
    RecyclerView.LayoutManager layoutManager1;

    RecyclerView levelTwoView;
    LevelTwoAdapter levelTwoAdapter;
    RecyclerView.LayoutManager layoutManager2;

    RecyclerView levelThreeView;
    LevelThreeAdapter levelThreeAdapter;
    RecyclerView.LayoutManager layoutManager3;

    ArrayList<ReceiveTypeList> typeLists;

    ArrayList<LevelWiseLists> levelOneLists;
    ArrayList<LevelWiseLists> levelTwoLists;
    ArrayList<LevelThreeLists> levelThreeLists;

    LinearLayout afterType;

    String typeId = "";
    String levelOneId = "";
    String levelTwoId = "";
    String selected_al1_id = "";
    String selected_al2_id = "";

    public ChartAccounts() {
        // Required empty public constructor
    }

    Context mContext;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext=context;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChartAccounts.
     */
    // TODO: Rename and change types and number of parameters
    public static ChartAccounts newInstance(String param1, String param2) {
        ChartAccounts fragment = new ChartAccounts();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chart_accounts, container, false);

        typeSpinner = view.findViewById(R.id.type_name_ca_spinner);
        levelOneView = view.findViewById(R.id.level_one_ca_report_view);
        levelTwoView = view.findViewById(R.id.level_two_ca_report_view);
        levelThreeView = view.findViewById(R.id.level_three_ca_report_view);
        afterType = view.findViewById(R.id.after_selecting_type_ca);
        afterType.setVisibility(View.GONE);

        typeLists = new ArrayList<>();
        levelOneLists = new ArrayList<>();
        levelTwoLists = new ArrayList<>();
        levelThreeLists = new ArrayList<>();

        levelOneView.setHasFixedSize(true);
        layoutManager1 = new LinearLayoutManager(getContext());
        levelOneView.setLayoutManager(layoutManager1);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(levelOneView.getContext(),DividerItemDecoration.VERTICAL);
        levelOneView.addItemDecoration(dividerItemDecoration);

        levelTwoView.setHasFixedSize(true);
        layoutManager2 = new LinearLayoutManager(getContext());
        levelTwoView.setLayoutManager(layoutManager2);
        DividerItemDecoration dividerItemDecoration1 = new DividerItemDecoration(levelTwoView.getContext(),DividerItemDecoration.VERTICAL);
        levelTwoView.addItemDecoration(dividerItemDecoration1);

        levelThreeView.setHasFixedSize(true);
        layoutManager3 = new LinearLayoutManager(getContext());
        levelThreeView.setLayoutManager(layoutManager3);
        DividerItemDecoration dividerItemDecoration2 = new DividerItemDecoration(levelThreeView.getContext(),DividerItemDecoration.VERTICAL);
        levelThreeView.addItemDecoration(dividerItemDecoration2);

        typeSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                System.out.println(position+": "+name);
                for (int i = 0; i < typeLists.size(); i++) {
                    if (name.equals(typeLists.get(i).getType())) {
                        typeId = (typeLists.get(i).getId());
                    }
                }

                selectedPosition = 0;
                selectedPosition2 = 0;

                System.out.println(typeId);
                new AllCheck().execute();

            }
        });


        new Check().execute();
        return  view;

    }

    public boolean isConnected() {
        boolean connected = false;
        boolean isMobile = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    public boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException | InterruptedException e)          { e.printStackTrace(); }

        return false;
    }

    @Override
    public void onCategoryClicked2(int CategoryPosition2) {

        selected_al2_id = levelTwoLists.get(CategoryPosition2).getId();

        selectedPosition2 = CategoryPosition2;

        levelTwoAdapter.notifyDataSetChanged();

        new Level3Check().execute();

    }

    @Override
    public void onCategoryClicked(int CategoryPosition) {

        selected_al1_id = levelOneLists.get(CategoryPosition).getId();
//        if (selectedPosition == CategoryPosition) {
//            selectedPosition = -1;
//        } else {
        System.out.println(CategoryPosition);
        selectedPosition = CategoryPosition;
        selectedPosition2 = 0;

        levelOneAdapter.notifyDataSetChanged();

        new Level2Check().execute();
    }

    public class Check extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                TypeData();
                if (connected) {
                    conn = true;
                }

            } else {
                conn = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            waitProgress.dismiss();
            if (conn) {

                conn = false;
                connected = false;

                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < typeLists.size(); i++) {
                    type.add(typeLists.get(i).getType());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                typeSpinner.setAdapter(arrayAdapter);


            }else {
                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new Check().execute();
                        dialog.dismiss();
                    }
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public class AllCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                AfterTypeSelectedData();
                if (connected) {
                    conn = true;
                }

            } else {
                conn = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            waitProgress.dismiss();
            if (conn) {

                conn = false;
                connected = false;

                afterType.setVisibility(View.VISIBLE);

                levelOneAdapter = new LevelOneAdapter(levelOneLists, getContext(),ChartAccounts.this);
                levelOneView.setAdapter(levelOneAdapter);

                levelTwoAdapter = new LevelTwoAdapter(levelTwoLists,getContext(),ChartAccounts.this);
                levelTwoView.setAdapter(levelTwoAdapter);

                levelThreeAdapter = new LevelThreeAdapter(levelThreeLists,getContext());
                levelThreeView.setAdapter(levelThreeAdapter);


            }else {
                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new AllCheck().execute();
                        dialog.dismiss();
                    }
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public class Level2Check extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                Level2Data();
                if (connected) {
                    conn = true;
                }

            } else {
                conn = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            waitProgress.dismiss();
            if (conn) {

                conn = false;
                connected = false;

                levelTwoAdapter = new LevelTwoAdapter(levelTwoLists,getContext(),ChartAccounts.this);
                levelTwoView.setAdapter(levelTwoAdapter);

                levelThreeAdapter = new LevelThreeAdapter(levelThreeLists,getContext());
                levelThreeView.setAdapter(levelThreeAdapter);


            }else {
                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new Level2Check().execute();
                        dialog.dismiss();
                    }
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public class Level3Check extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                Level3Data();
                if (connected) {
                    conn = true;
                }

            } else {
                conn = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            waitProgress.dismiss();
            if (conn) {

                conn = false;
                connected = false;

                levelThreeAdapter = new LevelThreeAdapter(levelThreeLists,getContext());
                levelThreeView.setAdapter(levelThreeAdapter);


            }else {
                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new Level3Check().execute();
                        dialog.dismiss();
                    }
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public void TypeData() {
        try {
            this.connection = createConnection();

            Statement stmt = connection.createStatement();

            typeLists = new ArrayList<>();

            ResultSet resultSet1 = stmt.executeQuery("Select ah_id, ah_code, ah_name from acc_head order by ah_id");

            while (resultSet1.next()) {
                typeLists.add(new ReceiveTypeList(resultSet1.getString(1),resultSet1.getString(3)+" ("+resultSet1.getString(2)+")"));
            }

            connected = true;

            connection.close();

        }
        catch (Exception e) {

            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void AfterTypeSelectedData() {
        try {
            this.connection = createConnection();

            Statement stmt = connection.createStatement();

            levelOneLists = new ArrayList<>();
            levelTwoLists = new ArrayList<>();
            levelThreeLists = new ArrayList<>();
            String al1_id = "";
            String al2_id = "";

            if (typeId != null) {
                if (typeId.isEmpty()) {
                    typeId = null;
                }
            }

            ResultSet resultSet = stmt.executeQuery("select al1_id, al1_code, al1_name from acc_level1 where al1_ah_id = "+typeId+" order by al1_code");

            int first = 0;
            while (resultSet.next()) {
                if (first == 0) {
                    al1_id = resultSet.getString(1);
                    first++;
                }
                levelOneLists.add(new LevelWiseLists(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3)));
            }

            int second = 0;
            ResultSet resultSet1 = stmt.executeQuery("select al2_id, al2_code, al2_name from acc_level2 where al2_al1_id = "+al1_id+" order by al2_code");
            while (resultSet1.next()) {
                if (second == 0 ) {
                    al2_id = resultSet1.getString(1);
                    second++;
                }
                levelTwoLists.add(new LevelWiseLists(resultSet1.getString(1),resultSet1.getString(2),resultSet1.getString(3)));
            }

            ResultSet resultSet2 = stmt.executeQuery("select ad_id, ad_code, ad_short_name, ad_name from acc_dtl where ad_al2_id = "+al2_id+" order by ad_code");
            while (resultSet2.next()) {
                levelThreeLists.add(new LevelThreeLists(resultSet2.getString(1),resultSet2.getString(2),resultSet2.getString(3),resultSet2.getString(4)));
            }

            if (typeId == null) {
                typeId = "";
            }

            connected = true;

            connection.close();

        }
        catch (Exception e) {

            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void Level2Data() {
        try {
            this.connection = createConnection();

            Statement stmt = connection.createStatement();

            levelTwoLists = new ArrayList<>();
            levelThreeLists = new ArrayList<>();
            String al2_id = "";


            int second = 0;
            ResultSet resultSet1 = stmt.executeQuery("select al2_id, al2_code, al2_name from acc_level2 where al2_al1_id = "+selected_al1_id+" order by al2_code");
            while (resultSet1.next()) {
                if (second == 0 ) {
                    al2_id = resultSet1.getString(1);
                    second++;
                }
                levelTwoLists.add(new LevelWiseLists(resultSet1.getString(1),resultSet1.getString(2),resultSet1.getString(3)));
            }

            ResultSet resultSet2 = stmt.executeQuery("select ad_id, ad_code, ad_short_name, ad_name from acc_dtl where ad_al2_id = "+al2_id+" order by ad_code");
            while (resultSet2.next()) {
                levelThreeLists.add(new LevelThreeLists(resultSet2.getString(1),resultSet2.getString(2),resultSet2.getString(3),resultSet2.getString(4)));
            }



            connected = true;

            connection.close();

        }
        catch (Exception e) {

            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void Level3Data() {
        try {
            this.connection = createConnection();

            Statement stmt = connection.createStatement();

            levelThreeLists = new ArrayList<>();

            ResultSet resultSet2 = stmt.executeQuery("select ad_id, ad_code, ad_short_name, ad_name from acc_dtl where ad_al2_id = "+selected_al2_id+" order by ad_code");
            while (resultSet2.next()) {
                levelThreeLists.add(new LevelThreeLists(resultSet2.getString(1),resultSet2.getString(2),resultSet2.getString(3),resultSet2.getString(4)));
            }


            connected = true;

            connection.close();

        }
        catch (Exception e) {

            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}
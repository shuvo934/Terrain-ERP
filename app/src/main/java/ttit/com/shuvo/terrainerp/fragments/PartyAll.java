package ttit.com.shuvo.terrainerp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rosemaryapp.amazingspinner.AmazingSpinner;

import java.util.ArrayList;

import ttit.com.shuvo.terrainerp.R;
import ttit.com.shuvo.terrainerp.WaitProgress;
import ttit.com.shuvo.terrainerp.adapters.AllPartyAdapter;
import ttit.com.shuvo.terrainerp.arrayList.AllPartyLists;
import ttit.com.shuvo.terrainerp.arrayList.ReceiveTypeList;
import ttit.com.shuvo.terrainerp.dialogues.PartyListDialogue;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PartyAll#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PartyAll extends Fragment implements AllPartyAdapter.ClickedItem{

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
//    private Connection connection;

    AmazingSpinner partySpinner;
    ArrayList<ReceiveTypeList> partyTypeLists;

    String partyType = "";

    public static String ad_id_pal = "";
    public static String ad_name_pal = "";

    LinearLayout afterSelectingParty;

    TextView totalNameText;
    TextView totalNumberText;
    ImageView changeImage;

    TextInputEditText searchItem;
    TextInputLayout searchItemLay;

    String searchingName = "";

    ArrayList<AllPartyLists> filteredList;
    ArrayList<AllPartyLists> allPartyLists;

    RecyclerView itemView;
    AllPartyAdapter allPartyAdapter;
    RecyclerView.LayoutManager layoutManager;

    String total_party = "";

    public PartyAll() {
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
     * @return A new instance of fragment PartyAll.
     */
    // TODO: Rename and change types and number of parameters
    public static PartyAll newInstance(String param1, String param2) {
        PartyAll fragment = new PartyAll();
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
        View view = inflater.inflate(R.layout.fragment_party_all, container, false);

        partySpinner = view.findViewById(R.id.party_type_pal_spinner);

        afterSelectingParty = view.findViewById(R.id.after_selecting_party_type_pal);
        afterSelectingParty.setVisibility(View.GONE);

        totalNameText = view.findViewById(R.id.supplier_or_customer_text_changed_pal);
        totalNumberText = view.findViewById(R.id.total_customer_or_supplier_no_pal);
        changeImage = view.findViewById(R.id.supplier_or_customer_icon);

        searchItem = view.findViewById(R.id.search_item_name_party_all_list);
        searchItemLay = view.findViewById(R.id.search_item_name_lay_party_all_list);

        itemView = view.findViewById(R.id.party_all_list_report_view);

        itemView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        itemView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(itemView.getContext(),DividerItemDecoration.VERTICAL);
        itemView.addItemDecoration(dividerItemDecoration);

        partyTypeLists = new ArrayList<>();

        filteredList = new ArrayList<>();
        allPartyLists = new ArrayList<>();

        partyTypeLists.add(new ReceiveTypeList("6","Customer",""));
        partyTypeLists.add(new ReceiveTypeList("7","Supplier",""));

        ArrayList<String> type = new ArrayList<>();
        for(int i = 0; i < partyTypeLists.size(); i++) {
            type.add(partyTypeLists.get(i).getType());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

        partySpinner.setAdapter(arrayAdapter);

        partySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                System.out.println(position+": "+name);

                for (int i = 0; i < partyTypeLists.size(); i++) {
                    if (name.equals(partyTypeLists.get(i).getType())) {
                        partyType = (partyTypeLists.get(i).getId());
                    }
                }

                afterSelectingParty.setVisibility(View.GONE);

                totalNameText.setText("Total " + name);
                if (partyType.equals("6")) {
                    changeImage.setImageResource(R.drawable.cus_circle);
                }
                else if (partyType.equals("7")) {
                    changeImage.setImageResource(R.drawable.sup_circle);
                }
//                new Check().execute();
                getPartyList();

            }
        });

        searchItem.addTextChangedListener(new TextWatcher() {
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

        searchItem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                        event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        Log.i("Let see", "Come here");
                        closeKeyBoard();



                        return false; // consume.
                    }
                }
                return false;
            }
        });

        return view;
    }

//    public boolean isConnected() {
//        boolean connected = false;
//        boolean isMobile = false;
//        try {
//            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
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

    private void closeKeyBoard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void filter(String text) {

        filteredList = new ArrayList<>();
        for (AllPartyLists item : allPartyLists) {
            if (item.getAd_name().toLowerCase().contains(text.toLowerCase()) || item.getAd_code().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add((item));
                isfiltered = true;
            }
        }
        allPartyAdapter.filterList(filteredList);

    }

    @Override
    public void onCategoryClicked(int CategoryPosition) {

        if (isfiltered) {
            ad_name_pal = filteredList.get(CategoryPosition).getAd_name();
            ad_id_pal = filteredList.get(CategoryPosition).getAd_id();
        }
        else {
            ad_name_pal = allPartyLists.get(CategoryPosition).getAd_name();
            ad_id_pal = allPartyLists.get(CategoryPosition).getAd_id();
        }

        PartyListDialogue partyListDialogue = new PartyListDialogue(mContext);
        partyListDialogue.show(getActivity().getSupportFragmentManager(),"PARTY");
    }

//    public class Check extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
//            waitProgress.setCancelable(false);
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            if (isConnected() && isOnline()) {
//
//                PartyData();
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
//                afterSelectingParty.setVisibility(View.VISIBLE);
//
//                allPartyAdapter = new AllPartyAdapter(allPartyLists,getContext(),PartyAll.this);
//                itemView.setAdapter(allPartyAdapter);
//
//                totalNumberText.setText(total_party);
//
//                waitProgress.dismiss();
//
//            }
//            else {
//                waitProgress.dismiss();
//                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(getContext())
//                        .setMessage("Please Check Your Internet Connection")
//                        .setPositiveButton("Retry", null)
//                        .setNegativeButton("Cancel", null)
//                        .show();
//
//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
//                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                positive.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        new Check().execute();
//                        dialog.dismiss();
//                    }
//                });
//
//                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                negative.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//            }
//        }
//    }

//    public void PartyData() {
//        try {
//            this.connection = createConnection();
//
//            allPartyLists = new ArrayList<>();
//
//            total_party = "";
//
//            Statement statement = connection.createStatement();
//
//            ResultSet resultSet = statement.executeQuery("select ad_id, ad_name, ad_code, ad_address, ad_phone, ad_email,\n" +
//                    "(select count(*) from acc_dtl where ad_flag = "+partyType+") as total\n" +
//                    "from acc_dtl \n" +
//                    "where ad_flag = "+partyType+"\n" +
//                    "order by ad_name");
//
//            int i = 0;
//            while (resultSet.next()) {
//
//                i++;
//                total_party = resultSet.getString(7);
//                allPartyLists.add(new AllPartyLists(String.valueOf(i),resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),
//                        resultSet.getString(4),resultSet.getString(5),resultSet.getString(6)));
//            }
//
//            resultSet.close();
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
        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;
        allPartyLists = new ArrayList<>();

        total_party = "";

        String url = "http://103.56.208.123:8001/apex/tterp/partyAll/getPartyList?partyType="+partyType+"";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);

                        String ad_id = info.getString("ad_id");
                        String ad_name = info.getString("ad_name")
                                .equals("null") ? "" : info.getString("ad_name");
                        String ad_code = info.getString("ad_code")
                                .equals("null") ? "" : info.getString("ad_code");
                        String ad_address = info.getString("ad_address")
                                .equals("null") ? "" : info.getString("ad_address");
                        String ad_phone = info.getString("ad_phone")
                                .equals("null") ? "" : info.getString("ad_phone");
                        String ad_email = info.getString("ad_email")
                                .equals("null") ? "" : info.getString("ad_email");
                        total_party = info.getString("total")
                                .equals("null") ? "" : info.getString("total");


                        allPartyLists.add(new AllPartyLists(String.valueOf(i+1),ad_id,ad_name,ad_code,
                                ad_address,ad_phone,ad_email));
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

        requestQueue.add(request);
    }

    private void updateFragment() {
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                afterSelectingParty.setVisibility(View.VISIBLE);

                allPartyAdapter = new AllPartyAdapter(allPartyLists,getContext(),PartyAll.this);
                itemView.setAdapter(allPartyAdapter);

                totalNumberText.setText(total_party);

                waitProgress.dismiss();
            }
            else {
                waitProgress.dismiss();
                AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getPartyList();
                    dialog.dismiss();
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> dialog.dismiss());
            }
        }
        else {
            waitProgress.dismiss();
            AlertDialog dialog = new AlertDialog.Builder(mContext)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getPartyList();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }
}
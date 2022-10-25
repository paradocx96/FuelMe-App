package com.example.fuelme.ui.mainscreen.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fuelme.R;
import com.example.fuelme.commonconstants.CommonConstants;
import com.example.fuelme.models.FuelStation;
import com.example.fuelme.ui.mainscreen.adapters.AllStationsRecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllStationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * IT19014128
 * A.M.W.W.R.L. Watkaetiya
 *
 * Fragment for displaying al stations
 */
public class AllStationsFragment extends Fragment {

    private final String TAG = "demo";
    ArrayList<FuelStation> fuelStations = new ArrayList<>(); //array list for fuel stations
    ArrayList<FuelStation> sampleFuelStations = new ArrayList<>(); //array list for sample fuel stations
    private final OkHttpClient client = new OkHttpClient(); //okhttp client instance
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    RecyclerView recyclerView;
    AllStationsRecyclerViewAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllStationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllStationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllStationsFragment newInstance(String param1, String param2) {
        AllStationsFragment fragment = new AllStationsFragment();
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
        View view =  inflater.inflate(R.layout.fragment_all_stations, container, false);

        //setup the sample fuel station list
        //setupSampleFuelStations();

        //fetch and assign the fuel station list
        fetchFuelStationsAsync();


        //assign recycler view
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_allStations);


        adapter = new AllStationsRecyclerViewAdapter(getActivity(), fuelStations);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        //set the swipe refresh layout
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_all_stations);
        //set the listener for swipe refresh layout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fuelStations.clear();
                adapter.notifyDataSetChanged();
                fetchFuelStationsAsync();
            }
        });

        return view;
    }



    //fetch all the stations from remote asynchronously
    public void fetchFuelStationsAsync(){
        String baseUrl = CommonConstants.REMOTE_URL;

        //build the url using Url builder
        HttpUrl url = HttpUrl.parse(baseUrl).newBuilder()
                .addPathSegment("api")
                .addPathSegment("FuelStations")
                .build();

        //build the request
        Request request = new Request.Builder()
                .url(url)
                .build();

        //make the client call using okhttp
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "Failed to make call");

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false); //stop displaying refreshing indicator
                        getAlertDialog("Error in making call", "Check your network connection").show();
                    }
                });

                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    //handle successful response
                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();

                    //get the body to a JSON object
                    try {
                        JSONArray jsonArray = new JSONArray(body); //get the json array
                        //iterate through the JSON array
                        for (int i = 0; i < jsonArray.length(); i++){
                            //JSONObject jsonObject = new JSONObject(body);
                            JSONObject jsonObject = jsonArray.getJSONObject(i); //get the json object by index

                            FuelStation fuelStation = new FuelStation();//instantiate fuel station object

                            //assign attributes to the fuel station object
                            fuelStation.setId(jsonObject.getString("id"));
                            fuelStation.setLicense(jsonObject.getString("license"));
                            fuelStation.setOwnerUsername(jsonObject.getString("ownerUsername"));
                            fuelStation.setStationName(jsonObject.getString("stationName"));
                            fuelStation.setStationAddress(jsonObject.getString("stationAddress"));
                            fuelStation.setStationPhoneNumber(jsonObject.getString("stationPhoneNumber"));
                            fuelStation.setStationEmail(jsonObject.getString("stationEmail"));
                            fuelStation.setStationWebsite(jsonObject.getString("stationWebsite"));
                            fuelStation.setOpenStatus(jsonObject.getString("openStatus"));
                            fuelStation.setPetrolQueueLength(jsonObject.getInt("petrolQueueLength"));
                            fuelStation.setDieselQueueLength(jsonObject.getInt("dieselQueueLength"));
                            fuelStation.setPetrolStatus(jsonObject.getString("petrolStatus"));
                            fuelStation.setDieselStatus(jsonObject.getString("dieselStatus"));
                            fuelStation.setLocationLatitude(jsonObject.getInt("locationLatitude"));
                            fuelStation.setLocationLongitude(jsonObject.getInt("locationLongitude"));

                            //add the fuel station to fuel stations list
                            fuelStations.add(fuelStation);
                        }

                        Log.d(TAG, "Successfully added fuel station array list");
                        Log.d(TAG, "Fuel station 1 name : " + fuelStations.get(0).getStationName());

                        //update the UI on UI thread
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                swipeRefreshLayout.setRefreshing(false); //stop displaying refreshing indicator
                                if (fuelStations.isEmpty()){
                                    Toast.makeText(getActivity(), "No stations found", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    adapter = new AllStationsRecyclerViewAdapter(getActivity(), fuelStations);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                }

                            }
                        });


                    }catch (JSONException e){
                        Log.d(TAG, "JSON Exception : " + e);
                        e.printStackTrace();
                    }

                }
                else {
                    //handle unsuccessful response

                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false); //stop displaying refreshing indicator
                            getAlertDialog("Error in response" , " Message: " + body).show();
                        }
                    });

                    Log.d(TAG, "onResponse failure : " + body);
                }
            }
        });

        Log.d(TAG, "URL : " +url);

    }

    public AlertDialog.Builder getAlertDialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        return builder;
    }


    //method for fetching data and assigning to fuel stations array list
    public void setupSampleFuelStations(){
        FuelStation fuelStation1 = new FuelStation("0001","l001" ,"eheliyagoda",
                "Eheliyagoda Assotiates","41/8, Sangabo Mawatha, Colombo", "01144552",
                "ehe@gmail.com","ehe.com","open",
                10, 2, "available", "available",
                0, 0 );

        FuelStation fuelStation2 = new FuelStation("0002","l089" ,"madura",
                "Madura Assotiates","78, Kandy Road, Kiribathgoda", "01144552",
                "madura@gmail.com","madura.com","open",
                100, 20, "unavailable", "available",
                0, 0 );

        FuelStation fuelStation3 = new FuelStation("0003","l052" ,"wije",
                "Wije Assotiates","65/8/9, Wihara Road, Gampaha", "01144552",
                "wije@gmail.com","wije.com","closed",
                50, 72, "available", "unavailable",
                0, 0 );

        sampleFuelStations.add(fuelStation1);
        sampleFuelStations.add(fuelStation2);
        sampleFuelStations.add(fuelStation3);
    }
}
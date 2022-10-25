package com.example.fuelme.ui.owner_dashboard_screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.fuelme.R;
import com.example.fuelme.commonconstants.CommonConstants;
import com.example.fuelme.models.FuelStation;
import com.example.fuelme.ui.owner_dashboard_screen.adapters.OwnerStationsRecyclerViewAdapter;

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

public class OwnerStationsActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient(); //okhttp client instance
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private final String TAG = "demo";
    ArrayList<FuelStation> fuelStations = new ArrayList<>(); //array list for fuel stations
    SharedPreferences preferences;

    RecyclerView recyclerView;
    OwnerStationsRecyclerViewAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_stations);

        //setup the fuel station list
        //setupSampleFuelStations();

        //fetch the owner's fuel stations from the list
        fetchOwnerFuelStationsAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.owner_stations_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //assign recycler view
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_ownerStations);

        adapter = new OwnerStationsRecyclerViewAdapter(this, fuelStations);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //assign swipe refresh view
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_owner_stations);
        //set listener for swipe refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fuelStations.clear();
                fetchOwnerFuelStationsAsync(OwnerStationsActivity.this);
            }
        });

    }

    //fetch owner's stations from remote
    public void fetchOwnerFuelStationsAsync(Context context){

        //get the current user's username
        preferences = getSharedPreferences("login_data", MODE_PRIVATE); //assign preferences for login data
        String currentUsername = preferences.getString("user_username", ""); //get the username from shared preferences

        String baseUrl = CommonConstants.REMOTE_URL;

        //build the url using Url builder
        HttpUrl url = HttpUrl.parse(baseUrl).newBuilder()
                .addPathSegment("api")
                .addPathSegment("FuelStations")
                .addPathSegment("GetAllStationsByOwnerUsername")
                .addPathSegment(currentUsername)
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false); //stop displaying refreshing indicator
                        getAlertDialog("Error when getting data", "Check your network connection").show();
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


                        //update the UI on UI thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                swipeRefreshLayout.setRefreshing(false); //stop displaying refreshing indicator
                                if (fuelStations.isEmpty()){
                                    Toast.makeText(context, "You do not have registered any stations yet",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    adapter = new OwnerStationsRecyclerViewAdapter(context, fuelStations);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                    Log.d(TAG, "Fuel station 1 name : " + fuelStations.get(0).getStationName());
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
                    Log.d(TAG, "onResponse failure : " + body);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false); //stop displaying refreshing indicator
                            getAlertDialog("Error in response", "Message : " + body).show();
                        }
                    });
                }
            }
        });

        Log.d(TAG, "URL : " +url);

    }

    //generic alert dialog builder
    public AlertDialog.Builder getAlertDialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        return builder;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

        fuelStations.add(fuelStation1);
        fuelStations.add(fuelStation2);
        fuelStations.add(fuelStation3);
    }
}
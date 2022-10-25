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
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fuelme.R;
import com.example.fuelme.commonconstants.CommonConstants;
import com.example.fuelme.models.FuelStationLogItem;
import com.example.fuelme.ui.customer_dashboard.CustomerRefuelHistoryActivity;
import com.example.fuelme.ui.customer_dashboard.adapters.CustomerRefuelHistoryRecyclerViewAdapter;
import com.example.fuelme.ui.owner_dashboard_screen.adapters.StationHistoryRecyclerViewAdapter;

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

public class StationHistoryActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient(); //okhttp client instance
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    String TAG = "demo";

    ArrayList<FuelStationLogItem> fuelStationLogItems = new ArrayList<>();

    RecyclerView recyclerView;
    StationHistoryRecyclerViewAdapter adapter;

    AlertDialog.Builder progressDialogBuilder;
    AlertDialog progressDialog;

    SwipeRefreshLayout swipeRefreshLayout;

    String stationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_history);

        //get the extras
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            stationId = extras.getString("station_id");
        }

        //fetch the log items from remote
        getStationLogItems(stationId, this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.station_history_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        //assign the recycler view
        recyclerView = (RecyclerView) findViewById(R.id.station_history_recyclerview);

        adapter = new StationHistoryRecyclerViewAdapter(this, fuelStationLogItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //set swipe refresh layout
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_station_history);
        //set listener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //clear the log item list
                fuelStationLogItems.clear();
                //get the log items from remote
                getStationLogItems(stationId, StationHistoryActivity.this);
            }
        });
    }

    //method to get station log items from remote
    public void getStationLogItems(String stationId, Context context){
        String baseUrl = CommonConstants.REMOTE_URL;

        //build the url using Url builder
        HttpUrl url = HttpUrl.parse(baseUrl).newBuilder()
                .addPathSegment("api")
                .addPathSegment("FuelStationLogs")
                .addPathSegment("GetByStationId")
                .addPathSegment(stationId)
                .build();

        //build the request
        Request request = new Request.Builder()
                .url(url)
                .build();

        //make the client call using okhttp
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false); //stop displaying refreshing indicator
                        getAlertDialog("Error", "Check your network connection").show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    //handle successful response
                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();

                    try {
                        JSONArray jsonArray = new JSONArray(body); //get the json array

                        //iterate through the JSON array
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i); //get the json object by index

                            FuelStationLogItem fuelStationLogItem = new FuelStationLogItem();

                            fuelStationLogItem.setId(jsonObject.getString("id"));
                            fuelStationLogItem.setStationId(jsonObject.getString("stationId"));
                            fuelStationLogItem.setFuelType(jsonObject.getString("fuelType"));
                            fuelStationLogItem.setFuelStatus(jsonObject.getString("fuelStatus"));
                            fuelStationLogItem.setYear(jsonObject.getInt("year"));
                            fuelStationLogItem.setMonth(jsonObject.getInt("month"));
                            fuelStationLogItem.setDayNumber(jsonObject.getInt("dayNumber"));
                            fuelStationLogItem.setHour(jsonObject.getInt("hour"));
                            fuelStationLogItem.setMinute(jsonObject.getInt("minute"));
                            fuelStationLogItem.setSecond(jsonObject.getInt("second"));

                            //add the log item to the list
                            fuelStationLogItems.add(fuelStationLogItem);


                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                swipeRefreshLayout.setRefreshing(false); //stop displaying refreshing indicator
                                if (fuelStationLogItems.isEmpty()){
                                    //if the station logs are empty show a toast
                                    Log.d(TAG, "No log items");
                                    Toast.makeText(StationHistoryActivity.this, "No logs found", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    //if not populate adapter
                                    Log.d(TAG, "Log items are there");
                                    adapter = new StationHistoryRecyclerViewAdapter(context, fuelStationLogItems);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
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

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false); //stop displaying refreshing indicator
                            getAlertDialog("Error", "Message : " + body).show();
                        }
                    });
                }
            }
        });
    }


    //progress bar in an alert dialog
    public AlertDialog.Builder getDialogProgressBar(){
        if (progressDialogBuilder == null){
            progressDialogBuilder = new AlertDialog.Builder(this);
            progressDialogBuilder.setCancelable(false);
            progressDialogBuilder.setTitle("Registering Station");
            progressDialogBuilder.setMessage("Please wait");

            final ProgressBar progressBar = new ProgressBar(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            progressBar.setLayoutParams(layoutParams);
            progressDialogBuilder.setView(progressBar);

        }
        return progressDialogBuilder;
    }

    //generic alert dialog with OK button
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
        return  true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
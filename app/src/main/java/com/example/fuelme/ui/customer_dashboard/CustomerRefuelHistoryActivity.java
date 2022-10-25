package com.example.fuelme.ui.customer_dashboard;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fuelme.R;
import com.example.fuelme.commonconstants.CommonConstants;
import com.example.fuelme.models.FuelStation;
import com.example.fuelme.models.QueueLogItem;
import com.example.fuelme.ui.customer_dashboard.adapters.CustomerRefuelHistoryRecyclerViewAdapter;

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


public class CustomerRefuelHistoryActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient(); //okhttp client instance
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    String TAG = "demo";

    ArrayList<QueueLogItem> queueLogItems = new ArrayList<>(); //array list for fuel stations
    SharedPreferences preferences;

    RecyclerView recyclerView;
    CustomerRefuelHistoryRecyclerViewAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    AlertDialog.Builder progressDialogBuilder;
    AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_refuel_history);

        //get the logs from remote
        getQueueLogItems(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.customer_refuel_history_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //assign the recycler view
        recyclerView = (RecyclerView) findViewById(R.id.customer_refuel_history_recyclerview);

        adapter = new CustomerRefuelHistoryRecyclerViewAdapter(this, queueLogItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //set swipe refresh layout
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_customer_refuel_history);
        //set the listener for on refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //clear the log item list
                queueLogItems.clear();
                //notify the recyclerview adapter that the dataset has changed
                adapter.notifyDataSetChanged();
                //get the log items from remote
                getQueueLogItems(CustomerRefuelHistoryActivity.this);
            }
        });
    }

    //method to get queue log items from remote
    public void getQueueLogItems(Context context){

        //get the current user's username
        preferences = getSharedPreferences("login_data", MODE_PRIVATE); //assign preferences for login data
        String currentUsername = preferences.getString("user_username", ""); //get the username from shared preferences

        String baseUrl = CommonConstants.REMOTE_URL;

        //build the url using Url builder
        HttpUrl url = HttpUrl.parse(baseUrl).newBuilder()
                .addPathSegment("api")
                .addPathSegment("QueueLogs")
                .addPathSegment("GetQueueLogItemsByUsername")
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

                            QueueLogItem queueLogItem = new QueueLogItem();

                            //assign attributes to the queue log item object
                            queueLogItem.setId(jsonObject.getString("id"));
                            queueLogItem.setCustomerUsername(jsonObject.getString("customerUsername"));
                            queueLogItem.setStationId(jsonObject.getString("stationId"));
                            queueLogItem.setStationLicense(jsonObject.getString("stationLicense"));
                            queueLogItem.setStationName(jsonObject.getString("stationName"));
                            queueLogItem.setQueue(jsonObject.getString("queue"));
                            queueLogItem.setAction(jsonObject.getString("action"));
                            queueLogItem.setRefuelStatus(jsonObject.getString("refuelStatus"));
                            queueLogItem.setYear(jsonObject.getInt("year"));
                            queueLogItem.setMonth(jsonObject.getInt("month"));
                            queueLogItem.setDayNumber(jsonObject.getInt("dayNumber"));
                            queueLogItem.setHour(jsonObject.getInt("hour"));
                            queueLogItem.setMinute(jsonObject.getInt("minute"));
                            queueLogItem.setSecond(jsonObject.getInt("second"));

                            //add the log item to the list
                            queueLogItems.add(queueLogItem);


                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                swipeRefreshLayout.setRefreshing(false); //stop displaying refreshing indicator
                                if (queueLogItems.isEmpty()){
                                    //if the queue logs are empty show a toast
                                    Toast.makeText(CustomerRefuelHistoryActivity.this, "No logs found", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    //if not populate adapter
                                    adapter = new CustomerRefuelHistoryRecyclerViewAdapter(context, queueLogItems);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                }
                            }
                        });
                    }
                    catch (JSONException e){
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
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
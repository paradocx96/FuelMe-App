package com.example.fuelme.ui.station_edit_screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.fuelme.R;
import com.example.fuelme.commonconstants.CommonConstants;
import com.example.fuelme.models.FuelStation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/*
* IT19014128
* A.M.W.W.R.L. Wataketiya
* Activity class to view more details of station for the owner
* The owner can then go to the edit screen from here
* */

public class StationMoreDetailsActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient(); //okhttp client instance
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private final String TAG = "demo";

    String stationId;
    FuelStation fuelStation = new FuelStation();
    TextView textViewStationName, textViewStationAddress, textViewLicense, textViewStationPhoneNumber, textViewStationEmail, textViewStationWebsite;
    AlertDialog.Builder progressDialogBuilder;
    AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_more_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.station_more_details_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //set the views
        textViewStationName = findViewById(R.id.txtView_stationName_station_more_details);
        textViewStationAddress = findViewById(R.id.txtView_stationAddress_station_more_details);
        textViewLicense = findViewById(R.id.txtView_license_station_more_details);
        textViewStationPhoneNumber = findViewById(R.id.txtView_stationPhoneNumber_station_more_details);
        textViewStationEmail = findViewById(R.id.txtView_stationEmail_station_more_details);
        textViewStationWebsite = findViewById(R.id.txtView_stationWebsite_station_more_details);

        setTextViewToLoading();

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            this.stationId = extras.getString("station_id"); //get the station id and set it to class attribute

            //make remote call to get station details by id
            getStation();
        }
    }

    public void editButtonClick(View view){
        Intent intent = new Intent(StationMoreDetailsActivity.this, EditStationActivity.class);
        intent.putExtra("selected_fuel_station",fuelStation);
        startActivity(intent);
    }

    //set every textview to loading
    public void  setTextViewToLoading(){
        textViewStationName.setText("Loading");
        textViewStationAddress.setText("Loading");
        textViewLicense.setText("Loading");
        textViewStationPhoneNumber.setText("Loading");
        textViewStationEmail.setText("Loading");
        textViewStationWebsite.setText("Loading");
    }

    //update the text views with fuelStation object details
    public void setTextViewsToFuelStationObjectDetails(){
        textViewStationName.setText(fuelStation.getStationName());
        textViewStationAddress.setText(fuelStation.getStationAddress());
        textViewLicense.setText(fuelStation.getLicense());
        textViewStationPhoneNumber.setText(fuelStation.getStationPhoneNumber());
        textViewStationEmail.setText(fuelStation.getStationEmail());
        textViewStationWebsite.setText(fuelStation.getStationWebsite());
    }

    //makes the remote call and gets the station by id
    public void getStation(){
        String baseUrl = CommonConstants.REMOTE_URL;

        //build the url using Url builder
        HttpUrl url = HttpUrl.parse(baseUrl).newBuilder()
                .addPathSegment("api")
                .addPathSegment("FuelStations")
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
                //handle failure to make call
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getAlertDialog("Error", "Check your network connection");
                    }
                });

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                //handle the received response
                if (response.isSuccessful()){
                    //handle successful response

                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();

                    try {
                        JSONObject jsonObject = new JSONObject(body);
                        //set the received details to the fuel station object
                        fuelStation.setId(jsonObject.getString("id"));
                        fuelStation.setStationName(jsonObject.getString("stationName"));
                        fuelStation.setLicense(jsonObject.getString("license"));
                        fuelStation.setOwnerUsername(jsonObject.getString("ownerUsername"));
                        fuelStation.setStationAddress(jsonObject.getString("stationAddress"));
                        fuelStation.setStationPhoneNumber(jsonObject.getString("stationPhoneNumber"));
                        fuelStation.setStationEmail(jsonObject.getString("stationEmail"));
                        fuelStation.setStationWebsite(jsonObject.getString("stationWebsite"));
                        fuelStation.setPetrolQueueLength(jsonObject.getInt("petrolQueueLength"));
                        fuelStation.setDieselQueueLength(jsonObject.getInt("dieselQueueLength"));
                        fuelStation.setPetrolStatus(jsonObject.getString("petrolStatus"));
                        fuelStation.setDieselStatus(jsonObject.getString("dieselStatus"));
                        fuelStation.setLocationLatitude(jsonObject.getInt("locationLatitude"));
                        fuelStation.setLocationLongitude(jsonObject.getInt("locationLongitude"));
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                        Log.d(TAG, "JSON Exception :  " +e);
                    }

                    //update the UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setTextViewsToFuelStationObjectDetails();
                        }
                    });
                }
                else {
                    //handle unsuccessful response
                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getAlertDialog("Error", "Response says : " + body);
                        }
                    });
                }
            }
        });
    }

    //progress bar in an alert dialog
    public AlertDialog.Builder getDialogProgressBar() {
        if (progressDialogBuilder == null) {
            progressDialogBuilder = new AlertDialog.Builder(this);
            progressDialogBuilder.setTitle("Joining Queue");

            final ProgressBar progressBar = new ProgressBar(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            progressBar.setLayoutParams(layoutParams);
            progressDialogBuilder.setView(progressBar);

        }
        return progressDialogBuilder;
    }

    //generic alert dialog with ok button
    public AlertDialog.Builder getAlertDialog(String title, String message) {
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
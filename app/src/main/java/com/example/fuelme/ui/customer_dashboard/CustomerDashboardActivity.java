package com.example.fuelme.ui.customer_dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fuelme.R;
import com.example.fuelme.commonconstants.CommonConstants;
import com.example.fuelme.commonconstants.StationCommonConstants;
import com.example.fuelme.models.FuelStation;
import com.example.fuelme.ui.mainscreen.StationSingleViewActivity;

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

/**
*  IT19014128 - A.M.W.W.R.L. Wtaketiya
*
* Activity for customer dashboard
 *
 * References:
 * https://developer.android.com/docs
 * https://square.github.io/okhttp/
 * https://youtu.be/RGQ3_UpDzO0
* */
public class CustomerDashboardActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient(); //okhttp client instance
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    String TAG = "demo";

    SharedPreferences sharedPreferences, userSharedPreferences;
    FuelStation fuelStation = new FuelStation();

    TextView textViewStationName, textViewUserFullName, textViewEmail;
    Button viewStationButton;
    AlertDialog.Builder progressDialogBuilder;
    AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.customer_dashboard_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //register the views
        textViewStationName = findViewById(R.id.txtView_stationName_customer_dashboard);
        viewStationButton = findViewById(R.id.btn_viewStation_customer_dashboard);
        textViewUserFullName = findViewById(R.id.txtView_fullName_customer_dashboard);
        textViewEmail = findViewById(R.id.txtView_email_customer_dashboard);

        setTextViewsToLoading();
        setButtonToLoading();

        //get the currently joined queue
        sharedPreferences = getSharedPreferences(StationCommonConstants.STATION_SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String currentlyJoinedQueueStationId = sharedPreferences.getString(StationCommonConstants.IN_QUEUE_STATION_ID, "");

        if (currentlyJoinedQueueStationId.isEmpty()){
            //user is not in a queue currently
            setViewsToNotInAQueue();
        }
        else {
            //user is currently in a queue
            //get the current station by id from remote
            getCurrentStation(currentlyJoinedQueueStationId);
        }

        //get full name and email for the current user
        userSharedPreferences = getSharedPreferences("login_data", MODE_PRIVATE);
        String fullName = userSharedPreferences.getString("user_full_name","");
        String email = userSharedPreferences.getString("user_email", "");

        //set the full name and email

        if (!fullName.isEmpty()){
            textViewUserFullName.setText(fullName);
        }
        if (!email.isEmpty()){
            textViewEmail.setText(email);
        }

    }

    //button click method for view station button
    public  void  viewStationButtonClick(View view){
        Intent intent = new Intent(this, StationSingleViewActivity.class);
        intent.putExtra("selected_fuel_station", fuelStation);
        startActivity(intent);
    }

    //navigate to history view
    public void navigateToViewHistory(View view){
        Intent intent  = new Intent(this, CustomerRefuelHistoryActivity.class);
        startActivity(intent);
    }

    //set text views to loading
    public void setTextViewsToLoading(){
        textViewStationName.setText("Loading");
    }

    public void setButtonToLoading(){
        viewStationButton.setEnabled(false);
    }

    //method to set the text view and disable the button when not in a queue
    public void setViewsToNotInAQueue(){
        textViewStationName.setText("You are currently not in a queue");
        viewStationButton.setEnabled(false);
    }

    //method to set the text view to current fuel station name and activate the view station button
    public void setViewsForReceivedFuelStation(){
        textViewStationName.setText(fuelStation.getStationName());
        viewStationButton.setEnabled(true);
    }

    //method to get current station from remote
    public void getCurrentStation(String currentStationId){
        String baseUrl = CommonConstants.REMOTE_URL;

        //build the url using Url builder
        HttpUrl url = HttpUrl.parse(baseUrl).newBuilder()
                .addPathSegment("api")
                .addPathSegment("FuelStations")
                .addPathSegment(currentStationId)
                .build();

        //build the request
        Request request = new Request.Builder()
                .url(url)
                .build();

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
                        fuelStation.setOpenStatus(jsonObject.getString("openStatus"));
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
                            setViewsForReceivedFuelStation();
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
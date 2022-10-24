package com.example.fuelme.ui.update_station_screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuelme.R;
import com.example.fuelme.commonconstants.CommonConstants;
import com.example.fuelme.models.FuelStation;
import com.example.fuelme.ui.delete_station_screen.DeleteStationActivity;
import com.example.fuelme.ui.mainscreen.StationSingleViewActivity;
import com.example.fuelme.ui.notice.NoticeCreateActivity;
import com.example.fuelme.ui.notice.NoticeListCustomerActivity;
import com.example.fuelme.ui.notice.NoticeListStationActivity;
import com.example.fuelme.ui.station_edit_screens.StationMoreDetailsActivity;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/*
* IT19014128
* A.M.W.W.R.L. Wataketiya
* Activity class for updating a station
*
* */

public class UpdateStationActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient(); //okhttp client instance
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private final String TAG = "demo";

    TextView textViewStationName,  textViewOpenStatus, textViewPetrolAvailability, textViewPetrolQueueLength, textViewDieselAvailability, textViewDieselQueueLength;

    Button editButton, petrolStatusUpdateButton, dieselStatusUpdateButton, stationOpenStatusUpdateButton, postNoticeButton, viewNoticesButton, viewFeedbackButton, deleteStationButton;
    FuelStation fuelStation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_station);

        //instantiate toolbar and set the back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.update_station_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //set the views by id
        textViewStationName = findViewById(R.id.txtView_stationName_update_station);
        textViewOpenStatus = findViewById(R.id.txtView_openStatus_update_station);
        textViewPetrolAvailability = findViewById(R.id.txtView_petrol_availability_update_station);
        textViewPetrolQueueLength = findViewById(R.id.txtView_petrol_queueLength_update_station);
        textViewDieselAvailability = findViewById(R.id.txtView_diesel_availability_update_station);
        textViewDieselQueueLength = findViewById(R.id.txtView_diesel_queueLength_update_station);
        editButton = findViewById(R.id.btn_edit_update_station);
        petrolStatusUpdateButton = findViewById(R.id.btn_changePetrolStatus_update_station);
        dieselStatusUpdateButton = findViewById(R.id.btn_changeDieselStatus_update_station);
        stationOpenStatusUpdateButton = findViewById(R.id.btn_updateStationOpenStatus_update_station);
        postNoticeButton = findViewById(R.id.btn_postNotice_update_station);
        viewNoticesButton = findViewById(R.id.btn_viewAllNotices_update_station);
        viewFeedbackButton = findViewById(R.id.btn_viewFeedback_update_station);
        deleteStationButton = findViewById(R.id.btnDelete_update_station);

        //get the extras
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            FuelStation fuelStation = (FuelStation) extras.getSerializable("selected_fuel_station"); //get the serializable and cast into fuel station object
            this.fuelStation = fuelStation; //assign the fuel station to view's fuel station object
        }
        syncAllViews();
    }

    //button click method for post notice button
    public void postNoticeButtonClick(View view){
        Intent intent = new Intent(UpdateStationActivity.this, NoticeCreateActivity.class);
        intent.putExtra("station_id", fuelStation.getId());
        startActivity(intent);
    }

    //button click method for view notices button
    public  void viewNoticesInUpdateStationButtonClick(View view){
        Intent intent = new Intent(UpdateStationActivity.this, NoticeListStationActivity.class);
        intent.putExtra("station_id", fuelStation.getId());
        startActivity(intent);
    }

    //button click method for view feedback button
    public void viewFeedbackInUpdateStationButtonClick(View view){
        Log.d(TAG, "View Feedback button click");
        //handle logic here
    }

    //button click for delete station button click
    public void deleteStationButtonClick(View view){
        Intent intent = new Intent(UpdateStationActivity.this, DeleteStationActivity.class);
        intent.putExtra("selected_fuel_station", fuelStation);
        startActivity(intent);
    }

    //button click to go to more view
    public void  moreButtonClick(View view){
        Intent intent = new Intent(UpdateStationActivity.this, StationMoreDetailsActivity.class);
        intent.putExtra("station_id",fuelStation.getId());
        startActivity(intent);
    }

    //method to sync all the views
    public void syncAllViews(){
        syncTextViews();
        syncOpenStatusButton();
        syncPetrolStatusButton();
        syncDieselStatusButton();
    }

    //method to change station open status
    public void stationOpenStatusUpdateButtonClick(View view){
        //check whether the station is currently open or closed
        if (fuelStation.getOpenStatus().equalsIgnoreCase("open")){
            //station is currently open
            //user is requesting to close the station

            //make the remote call to update the open status as closed
            updateStationsStatusAsClosed();
        }
        else if (fuelStation.getOpenStatus().equalsIgnoreCase("closed")){
            //station is currently closed
            //user is requesting to close the station

            //make the remote call to update the open status as open
            updateStationAsOpen();
        }
    }

    //method to update petrol status
    public void petrolStatusUpdateButtonClick(View view){
        //check whether petrol is currently available or not
        if (fuelStation.getPetrolStatus().equalsIgnoreCase("available")){
            //petrol is currently available
            //user is requesting to mark as unavailable

            //make the remote call to update the petrol status as unavailable
            updatePetrolStatusAsUnavailable();
        }
        else if (fuelStation.getPetrolStatus().equalsIgnoreCase("unavailable")){
            //petrol is currently unavailable
            //user is requesting to mark as available

            //make the remote call to update the petrol status as available
            updatePetrolStatusAsAvailable();
        }
    }

    //method to update diesel status
    public void dieselStatusUpdateButtonClick(View view){
        //check whether diesel is currently available or not
        if (fuelStation.getDieselStatus().equalsIgnoreCase("available")){
            //diesel is currently available
            //user is requesting to mark as unavailable

            //make the remote call to update the diesel status as unavailable
            updateDieselStatusAsUnavailable();
        }
        else if (fuelStation.getDieselStatus().equalsIgnoreCase("unavailable")){
            //diesel is currently unavailable
            //user is requesting to mark as available

            //make the remote call to update the diesel status as available
            updateDieselStatusAsAvailable();
        }
    }

    //method for remote call to mark station as closed
    public void updateStationsStatusAsClosed(){
        //create an instance of HTTPUrl
        HttpUrl url = HttpUrl.parse(CommonConstants.REMOTE_URL)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("FuelStations")
                .addPathSegment("MarkStationAsClosed")
                .addPathSegment(fuelStation.getId()) //set this view's station id to path
                .build();

        String sampleString = "sample";
        //dummy request body since okhttp requires one for put requests
        RequestBody requestBody = RequestBody.create(sampleString, JSON);

        //build the request
        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //show failure alert dialog
                        getAlertDialog("Error", "Failed to make the call").show();
                    }
                });

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){

                    //update the open status of fuel station object as closed
                    fuelStation.setOpenStatus("closed");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            syncTextViews();
                            syncOpenStatusButton();
                            Toast.makeText(UpdateStationActivity.this, "Closed the station", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    //handle failure response logic

                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //show the response error
                            getAlertDialog("Failure in response", "Message : " + body);
                        }
                    });
                }

            }
        });


    }

    //method for remote call to mark station as open
    public void updateStationAsOpen(){
        //create an instance of HTTPUrl
        HttpUrl url = HttpUrl.parse(CommonConstants.REMOTE_URL)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("FuelStations")
                .addPathSegment("MarkStationAsOpen")
                .addPathSegment(fuelStation.getId()) //set this view's station id to path
                .build();

        String sampleString = "sample";
        //dummy request body since okhttp requires one for put requests
        RequestBody requestBody = RequestBody.create(sampleString, JSON);

        //build the request
        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //show failure alert dialog
                        getAlertDialog("Error", "Failed to make the call").show();
                    }
                });

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){

                    //update the open status of fuel station object as closed
                    fuelStation.setOpenStatus("open");

                    //perform related UI updates
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            syncTextViews();
                            syncOpenStatusButton();
                            Toast.makeText(UpdateStationActivity.this, "Opened the station", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    //handle failure response logic

                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //show the response error
                            getAlertDialog("Failure in response", "Message : " + body);
                        }
                    });
                }

            }
        });
    }

    //method for remote call to mark petrol available
    public void updatePetrolStatusAsAvailable(){
        //create an instance of HTTPUrl
        HttpUrl url = HttpUrl.parse(CommonConstants.REMOTE_URL)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("FuelStations")
                .addPathSegment("MarkPetrolAsAvailable")
                .addPathSegment(fuelStation.getId()) //set this view's station id to path
                .build();

        String sampleString = "sample";
        //dummy request body since okhttp requires one for put requests
        RequestBody requestBody = RequestBody.create(sampleString, JSON);

        //build the request
        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //show failure alert dialog
                        getAlertDialog("Error", "Failed to make the call").show();
                    }
                });

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){

                    //update the open status of fuel station object as closed
                    fuelStation.setPetrolStatus("available");

                    //perform related UI updates
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            syncTextViews();
                            syncPetrolStatusButton();
                            Toast.makeText(UpdateStationActivity.this, "Updated petrol status", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    //handle failure response logic

                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //show the response error
                            getAlertDialog("Failure in response", "Message : " + body);
                        }
                    });
                }

            }
        });
    }

    //method for remote call to mark petrol unavailable
    public void updatePetrolStatusAsUnavailable(){
        //create an instance of HTTPUrl
        HttpUrl url = HttpUrl.parse(CommonConstants.REMOTE_URL)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("FuelStations")
                .addPathSegment("MarkPetrolAsUnavailable")
                .addPathSegment(fuelStation.getId()) //set this view's station id to path
                .build();

        String sampleString = "sample";
        //dummy request body since okhttp requires one for put requests
        RequestBody requestBody = RequestBody.create(sampleString, JSON);

        //build the request
        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //show failure alert dialog
                        getAlertDialog("Error", "Failed to make the call").show();
                    }
                });

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){

                    //update the open status of fuel station object as closed
                    fuelStation.setPetrolStatus("unavailable");

                    //perform related UI updates
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            syncTextViews();
                            syncPetrolStatusButton();
                            Toast.makeText(UpdateStationActivity.this, "Updated petrol status", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    //handle failure response logic

                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //show the response error
                            getAlertDialog("Failure in response", "Message : " + body);
                        }
                    });
                }

            }
        });
    }

    //method for remote call to mark diesel available
    public void updateDieselStatusAsAvailable(){
        //create an instance of HTTPUrl
        HttpUrl url = HttpUrl.parse(CommonConstants.REMOTE_URL)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("FuelStations")
                .addPathSegment("MarkDieselAsAvailable")
                .addPathSegment(fuelStation.getId()) //set this view's station id to path
                .build();

        String sampleString = "sample";
        //dummy request body since okhttp requires one for put requests
        RequestBody requestBody = RequestBody.create(sampleString, JSON);

        //build the request
        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //show failure alert dialog
                        getAlertDialog("Error", "Failed to make the call").show();
                    }
                });

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){

                    //update the open status of fuel station object as closed
                    fuelStation.setDieselStatus("available");

                    //perform related UI updates
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            syncTextViews();
                            syncDieselStatusButton();
                            Toast.makeText(UpdateStationActivity.this, "Updated diesel status", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    //handle failure response logic

                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //show the response error
                            getAlertDialog("Failure in response", "Message : " + body);
                        }
                    });
                }

            }
        });
    }

    //method for remote call to mark diesel unavailable
    public void updateDieselStatusAsUnavailable(){
        //create an instance of HTTPUrl
        HttpUrl url = HttpUrl.parse(CommonConstants.REMOTE_URL)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("FuelStations")
                .addPathSegment("MarkDieselAsUnavailable")
                .addPathSegment(fuelStation.getId()) //set this view's station id to path
                .build();

        String sampleString = "sample";
        //dummy request body since okhttp requires one for put requests
        RequestBody requestBody = RequestBody.create(sampleString, JSON);

        //build the request
        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //show failure alert dialog
                        getAlertDialog("Error", "Failed to make the call").show();
                    }
                });

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){

                    //update the open status of fuel station object as closed
                    fuelStation.setDieselStatus("unavailable");

                    //perform related UI updates
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            syncTextViews();
                            syncDieselStatusButton();
                            Toast.makeText(UpdateStationActivity.this, "Updated diesel status", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    //handle failure response logic

                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //show the response error
                            getAlertDialog("Failure in response", "Message : " + body);
                        }
                    });
                }

            }
        });
    }

    //sync the text views
    public void syncTextViews(){
        //get string values of queue lengths
        String petrolQueueLengthString = String.valueOf(fuelStation.getPetrolQueueLength());
        String dieselQueueLengthString = String.valueOf(fuelStation.getDieselQueueLength());

        //open status
        String openStatus = "Not Assigned";

        //Handle the station open status
        if (fuelStation.getOpenStatus().equalsIgnoreCase("open")){
            openStatus = "Open";
        }
        else if (fuelStation.getOpenStatus().equalsIgnoreCase("closed")){
            openStatus = "Closed";
        }

        //availability strings
        String petrolAvailabilityStatus = "Not Assigned";
        String dieselAvailabilityStatus  = "Not Assigned";

        //set availability strings based on the availability to maintain capitalization
        if (fuelStation.getPetrolStatus().equalsIgnoreCase("available")){
            petrolAvailabilityStatus = "Available";
        }
        else if(fuelStation.getPetrolStatus().equalsIgnoreCase("unavailable")){
            petrolAvailabilityStatus = "Unavailable";
        }

        if (fuelStation.getDieselStatus().equalsIgnoreCase("available")){
            dieselAvailabilityStatus = "Available";
        }
        else if(fuelStation.getDieselStatus().equalsIgnoreCase("unavailable")){
            dieselAvailabilityStatus = "Unavailable";
        }

        //set the details of the station in the views
        textViewStationName.setText(fuelStation.getStationName());
        textViewOpenStatus.setText(openStatus);
        textViewPetrolAvailability.setText(petrolAvailabilityStatus);
        textViewPetrolQueueLength.setText(petrolQueueLengthString);
        textViewDieselAvailability.setText(dieselAvailabilityStatus);
        textViewDieselQueueLength.setText(dieselQueueLengthString);

        //change station open text view color based on open status
        if (fuelStation.getOpenStatus().equalsIgnoreCase("open")){
            //change color to green
            textViewOpenStatus.setTextColor(Color.parseColor("#0E8921"));
        }
        else if (fuelStation.getOpenStatus().equalsIgnoreCase("closed")){
            //change color to red
            textViewOpenStatus.setTextColor(Color.parseColor("#FF0000"));
        }

        //change petrol status text view color based on petrol availability
        if (fuelStation.getPetrolStatus().equalsIgnoreCase("available")){
            //change color to green
            textViewPetrolAvailability.setTextColor(Color.parseColor("#0E8921"));
        }
        else if (fuelStation.getPetrolStatus().equalsIgnoreCase("unavailable")){
            //change color to red
            textViewPetrolAvailability.setTextColor(Color.parseColor("#FF0000"));
        }

        //change diesel status text view color based on diesel availability
        if (fuelStation.getDieselStatus().equalsIgnoreCase("available")){
            //change color to green
            textViewDieselAvailability.setTextColor(Color.parseColor("#0E8921"));
        }
        else if (fuelStation.getDieselStatus().equalsIgnoreCase("unavailable")){
            //change color to red
            textViewDieselAvailability.setTextColor(Color.parseColor("#FF0000"));
        }
    }

    //changes the open status button based on the open status of the station
    public void syncOpenStatusButton(){
        if (fuelStation != null){
            if (fuelStation.getOpenStatus().equalsIgnoreCase("open")){
                stationOpenStatusUpdateButton.setText("Close the station");
            }
            else if (fuelStation.getOpenStatus().equalsIgnoreCase("closed")){
                stationOpenStatusUpdateButton.setText("Open the station");
            }
        }
        else {
            Log.d(TAG, "FuelStation object is null");
        }
    }

    //changes the petrol status button based on petrol availability
    public void syncPetrolStatusButton(){
        if (fuelStation != null){
            if (fuelStation.getPetrolStatus().equalsIgnoreCase("available")){
                petrolStatusUpdateButton.setText("Mark Petrol Unavailable");
            }
            else if(fuelStation.getPetrolStatus().equalsIgnoreCase("unavailable")){
                petrolStatusUpdateButton.setText("Mark Petrol Available");
            }
        }
        else {
            Log.d(TAG, "FuelStation object is null");
        }

    }

    //changes the diesel status button based on petrol availability
    public void syncDieselStatusButton(){
        if (fuelStation != null){
            if (fuelStation.getDieselStatus().equalsIgnoreCase("available")){
                dieselStatusUpdateButton.setText("Mark Diesel Unavailable");
            }
            else if (fuelStation.getDieselStatus().equalsIgnoreCase("unavailable")){
                dieselStatusUpdateButton.setText("Mark Diesel Available");
            }

            //station_id = fuelStation.getId();
        }

        else {
            Log.d(TAG, "FuelStation object is null");
        }
    }

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
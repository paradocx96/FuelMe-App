package com.example.fuelme.ui.mainscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;

import android.content.DialogInterface;

import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuelme.R;
import com.example.fuelme.commonconstants.CommonConstants;
import com.example.fuelme.commonconstants.StationCommonConstants;
import com.example.fuelme.models.FuelStation;
import com.example.fuelme.ui.feedback.FeedbackList;
import com.example.fuelme.ui.notice.NoticeListCustomerActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class StationSingleViewActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient(); //okhttp client instance
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    String TAG = "demo";
    TextView textViewStationName, textViewStationAddress, textViewOpenStatus,
            textViewPetrolAvailabilityStatus, textViewDieselAvailabilityStatus, textViewPetrolQueueLength, textViewDieselQueueLength;
    Button petrolQueueUpdateButton, dieselQueueUpdateButton, stationPhoneNumberButton, stationEmailButton, websiteButton,
            viewFeedbackButton, viewNoticesButton, favouriteButton;
    SharedPreferences sharedPreferences, sharedPreferencesForUser;
    SharedPreferences.Editor editor;

    FuelStation fuelStation;

    AlertDialog.Builder progressDialogBuilder;
    AlertDialog progressDialog;

    String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_single_view);

        //instantiate toolbar and set the back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.station_single_view_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //set the views by id
        textViewStationName = findViewById(R.id.txtView_stationName_station_single_view);
        textViewStationAddress = findViewById(R.id.txtView_stationAddress_station_single_view);
        textViewOpenStatus = findViewById(R.id.txtView_openStatus_station_single_view);
        textViewPetrolAvailabilityStatus = findViewById(R.id.txtView_petrol_availability_status_station_single_viw);
        textViewPetrolQueueLength = findViewById(R.id.txtView_petrol_queue_length_station_single_viw);
        textViewDieselAvailabilityStatus = findViewById(R.id.txtView_diesel_availability_status_station_single_viw);
        textViewDieselQueueLength = findViewById(R.id.txtView_diesel_queue_length_station_single_viw);
        petrolQueueUpdateButton = findViewById(R.id.btn_petrolQueueUpdate_station_single_view);
        dieselQueueUpdateButton = findViewById(R.id.btn_dieselQueueUpdate_station_single_view);
        stationPhoneNumberButton = findViewById(R.id.btn_phoneNumber_station_single_view);
        stationEmailButton = findViewById(R.id.btn_email_station_single_view);
        websiteButton = findViewById(R.id.btn_website_station_single_view);
        viewFeedbackButton = findViewById(R.id.btnViewFeedback_station_single_view);
        viewNoticesButton = findViewById(R.id.btnViewNotices_station_single_view);
        favouriteButton = findViewById(R.id.btnFavourite_station_single_view);
        favouriteButton.setEnabled(true);

        //instantiate shared preferences
        //sharedPreferences = getSharedPreferences(StationCommonConstants.STATION_SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //get the extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            FuelStation fuelStation = (FuelStation) extras.getSerializable("selected_fuel_station"); //get the serializable and cast into fuel station object
            this.fuelStation = fuelStation;

            //get string values of queue lengths
            String petrolQueueLengthString = String.valueOf(fuelStation.getPetrolQueueLength());
            String dieselQueueLengthString = String.valueOf(fuelStation.getDieselQueueLength());

            //open status
            String openStatus = "Not Assigned";

            if (fuelStation.getOpenStatus().equalsIgnoreCase("open")) {
                openStatus = "Open";
            } else if (fuelStation.getOpenStatus().equalsIgnoreCase("closed")) {
                openStatus = "Closed";
            }

            //availability strings
            String petrolAvailabilityStatus = "Not Assigned";
            String dieselAvailabilityStatus = "Not Assigned";

            //set availability strings based on the availability to maintain capitalization
            if (fuelStation.getPetrolStatus().equalsIgnoreCase("available")) {
                petrolAvailabilityStatus = "Available";
            } else if (fuelStation.getPetrolStatus().equalsIgnoreCase("unavailable")) {
                petrolAvailabilityStatus = "Unavailable";
            }

            if (fuelStation.getDieselStatus().equalsIgnoreCase("available")) {
                dieselAvailabilityStatus = "Available";
            } else if (fuelStation.getDieselStatus().equalsIgnoreCase("unavailable")) {
                dieselAvailabilityStatus = "Unavailable";
            }

            //set the details of the station in the views
            textViewStationName.setText(fuelStation.getStationName());
            textViewStationAddress.setText(fuelStation.getStationAddress());
            textViewOpenStatus.setText(openStatus);
            textViewPetrolAvailabilityStatus.setText(petrolAvailabilityStatus);
            textViewPetrolQueueLength.setText(petrolQueueLengthString);
            textViewDieselAvailabilityStatus.setText(dieselAvailabilityStatus);
            textViewDieselQueueLength.setText(dieselQueueLengthString);
            stationPhoneNumberButton.setText(fuelStation.getStationPhoneNumber());
            stationEmailButton.setText(fuelStation.getStationEmail());
            websiteButton.setText(fuelStation.getStationWebsite());

            //change station open text view color based on open status
            if (fuelStation.getOpenStatus().equalsIgnoreCase("open")) {
                //change color to green
                textViewOpenStatus.setTextColor(Color.parseColor("#0E8921"));
            } else if (fuelStation.getOpenStatus().equalsIgnoreCase("closed")) {
                //change color to red
                textViewOpenStatus.setTextColor(Color.parseColor("#FF0000"));
            }

            //change petrol status text view color based on petrol availability
            if (fuelStation.getPetrolStatus().equalsIgnoreCase("available")) {
                //change color to green
                textViewPetrolAvailabilityStatus.setTextColor(Color.parseColor("#0E8921"));
            } else if (fuelStation.getPetrolStatus().equalsIgnoreCase("unavailable")) {
                //change color to red
                textViewPetrolAvailabilityStatus.setTextColor(Color.parseColor("#FF0000"));
            }

            //change diesel status text view color based on diesel availability
            if (fuelStation.getDieselStatus().equalsIgnoreCase("available")) {
                //change color to green
                textViewDieselAvailabilityStatus.setTextColor(Color.parseColor("#0E8921"));
            } else if (fuelStation.getDieselStatus().equalsIgnoreCase("unavailable")) {
                //change color to red
                textViewDieselAvailabilityStatus.setTextColor(Color.parseColor("#FF0000"));
            }

            sharedPreferences = getSharedPreferences(StationCommonConstants.STATION_SHARED_PREF_NAME, Context.MODE_PRIVATE);
            String currentlyJoinedQueueStationId = sharedPreferences.getString(StationCommonConstants.IN_QUEUE_STATION_ID, "");
            String queueType = sharedPreferences.getString(StationCommonConstants.QUEUE, "");

            Log.d(TAG, "User is in queue in station with id : " + currentlyJoinedQueueStationId);
            Log.d(TAG, "User is in queue type : " + queueType);
            Log.d(TAG, "User queue station is empty : " + currentlyJoinedQueueStationId.isEmpty());

            updateQueueButtons(currentlyJoinedQueueStationId, queueType, fuelStation.getId());

            //Get current logged username
            sharedPreferencesForUser = getSharedPreferences("login_data", MODE_PRIVATE);
            username = sharedPreferencesForUser.getString("user_username", "");

            sharedPreferencesForUser = getSharedPreferences("feedback_data", MODE_PRIVATE);
            editor = sharedPreferencesForUser.edit();

            editor.putString("feedback_station_id", fuelStation.getId());
            editor.apply();
        }


    }

    //button click for favourite button
    public void favouriteButtonClick(View view) {
        Log.d(TAG, "Favourite Button Clicked");

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("stationId", fuelStation.getId());
            jsonObject.put("username", username);
            jsonObject.put("stationName", fuelStation.getStationName());
            jsonObject.put("stationAddress", fuelStation.getStationAddress());
            jsonObject.put("createAt", currentDateTimeString);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String jsonString = jsonObject.toString();

        RequestBody requestBody = RequestBody.create(jsonString, JSON);

        HttpUrl baseUrl = HttpUrl.parse(CommonConstants.REMOTE_URL).newBuilder()
                .addPathSegment("api")
                .addPathSegment("Favourite")
                .build();

        Request request = new Request.Builder()
                .url(baseUrl)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "onResponse success : " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {

                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        String responseString = responseBody.string();

                        Log.d(TAG, "onResponse success : " + responseString);

                        try {
                            JSONObject jsonResponse = new JSONObject(responseString);

                            String message = jsonResponse.getString("message");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    favouriteButton.setEnabled(false);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    Log.d(TAG, "onResponse failure : " + response.message());
                }
            }
        });

    }


    //button click for view feedback button
    public void feedbackButtonClick(View view) {
        Log.d(TAG, "Feedback Button Clicked");
        Intent intent = new Intent(this, FeedbackList.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //button click for view notices button
    public void viewNoticesButtonClick(View view) {
        Log.d(TAG, "Notices Button Clicked");
        Intent intent = new Intent(StationSingleViewActivity.this, NoticeListCustomerActivity.class);
        intent.putExtra("station_id", fuelStation.getId());
        startActivity(intent);
    }

    //button click for phone number button
    public void phoneNumberButtonClick(View view){
        //get the phone number
        String phoneNumber = fuelStation.getStationPhoneNumber();
        //set the intent with ACTION_DIAL since this does not require additional permissions
        Intent intent  = new Intent(Intent.ACTION_DIAL);
        //set phone number to intent data with tel prefix
        intent.setData(Uri.parse("tel:"+phoneNumber));
        startActivity(intent);

    }

    //button click for email button
    public void emailButtonClick(View view){
        //get the email address of the station
        String email = fuelStation.getStationEmail();
        String subject = "Subject"; //set the subject
        String body = "Body"; //set the body
        //set the intent with ACTION_VIEW
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //parse and set the URI with email address, subject and body
        Uri data = Uri.parse("mailto:" + email + "?subject=" + Uri.encode(subject) + "&body=" + Uri.encode(body));
        intent.setData(data);//set the data to the intent
        startActivity(intent); //start the activity
    }

    //button click for website button
    public void websiteButtonClick(View view){
        //get the station website
        String website = fuelStation.getStationWebsite();
        //set the intent with ACTION_VIEW
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //parse and set the URI with the web address
        Uri data = Uri.parse("http://www." +website);
        intent.setData(data); //set the data to the intent
        startActivity(intent); //start activity
    }

    //update the queue buttons based on the shared preferences
    public void updateQueueButtons(String currentlyJoinedQueueStationId, String queueType, String viewFuelStationId) {

        //check if the currently joined queue's station id is this view's station id
        if (viewFuelStationId.equalsIgnoreCase(currentlyJoinedQueueStationId)) {
            //check which is the joined queue

            if (queueType.equalsIgnoreCase("petrol")) {
                //user is in this station's petrol queue
                //change the petrol queue button attributes
                petrolQueueUpdateButton.setText("Leave the queue");
                petrolQueueUpdateButton.setBackgroundColor(Color.parseColor("#F3AD25"));

                //disable the diesel queue update button
                dieselQueueUpdateButton.setEnabled(false);
            } else if (queueType.equalsIgnoreCase("diesel")) {
                //user is in this station's diesel queue
                //change the diesel queue button attributes
                dieselQueueUpdateButton.setText("Leave the queue");
                dieselQueueUpdateButton.setBackgroundColor(Color.parseColor("#F3AD25"));

                //disabled the petrol queue update button
                petrolQueueUpdateButton.setEnabled(false);
            }
        } else {
            // if the currently joined queue's station id is not this view's station id, user is in a different queue
            //then the user cannot join these queues
            //disable both queue update buttons

            //check if currentlyJoinedQueueStationId is empty
            if (currentlyJoinedQueueStationId.isEmpty()) {
                //if the currentlyJoinedQueueStationId is empty user is not in a queue

                //reset button attributes
                //reset the queue button attributes
                petrolQueueUpdateButton.setText("Join the queue");
                petrolQueueUpdateButton.setBackgroundColor(Color.parseColor("#0E8921")); //color green
                dieselQueueUpdateButton.setText("Join the Queue");
                dieselQueueUpdateButton.setBackgroundColor(Color.parseColor("#0E8921")); //color green

                //enable both the buttons
                petrolQueueUpdateButton.setEnabled(true);
                dieselQueueUpdateButton.setEnabled(true);
            } else {
                //if the currentlyJoinedQueueStationId is not empty user is in a queue of different station
                //disable both the buttons
                petrolQueueUpdateButton.setEnabled(false);
                dieselQueueUpdateButton.setEnabled(false);
            }

        }


    }

    //button click to update the petrol queue
    public void petrolQueueUpdateButtonClick(View view) {
        if (isUserNotInAQueue()) {
            //if user is not in a queue user is requesting to join this queue (the user can join the queue)
            sharedPreferences = getSharedPreferences(StationCommonConstants.STATION_SHARED_PREF_NAME, Context.MODE_PRIVATE);

            //set the station id and queue type in ths shared preferences
            SharedPreferences.Editor editor = sharedPreferences.edit(); //get the editor
            editor.putString(StationCommonConstants.IN_QUEUE_STATION_ID, this.fuelStation.getId()); //get this view's fuel station id
            editor.putString(StationCommonConstants.QUEUE, "petrol"); //set the queue type
            editor.apply(); //apply the changes

            String currentlyJoinedQueueStationId = sharedPreferences.getString(StationCommonConstants.IN_QUEUE_STATION_ID, "");
            String queueType = sharedPreferences.getString(StationCommonConstants.QUEUE, "");

            //make the remote call to increment the petrol queue length
            incrementPetrolQueue();

            updateQueueButtons(currentlyJoinedQueueStationId, queueType, fuelStation.getId());
        } else {
            //the user is already in a queue
            //check whether it is this station's this queue
            //if it is this station's this queue, user is leaving
            //handle the leaving logic
            sharedPreferences = getSharedPreferences(StationCommonConstants.STATION_SHARED_PREF_NAME, Context.MODE_PRIVATE);
            final String[] currentlyJoinedQueueStationId = {sharedPreferences.getString(StationCommonConstants.IN_QUEUE_STATION_ID, "")};
            final String[] queueType = {sharedPreferences.getString(StationCommonConstants.QUEUE, "")};

            Log.d(TAG, "User is in queue in station with id : " + currentlyJoinedQueueStationId[0]);
            Log.d(TAG, "User is in queue type : " + queueType[0]);
            if (currentlyJoinedQueueStationId[0].equalsIgnoreCase(this.fuelStation.getId())) {
                //user is in a queue of this station

                //check whether the user in this queue
                queueType[0] = sharedPreferences.getString(StationCommonConstants.QUEUE, "");
                if (queueType[0].equalsIgnoreCase("petrol")) {
                    //user is in this queue
                    //user is leaving the queue


                    AlertDialog.Builder petrolQueueLeaveDialogBuilder = new AlertDialog.Builder(this);
                    petrolQueueLeaveDialogBuilder.setCancelable(true);
                    petrolQueueLeaveDialogBuilder.setTitle("Have you refueled?");

                    //positive button for leaving after refueling
                    petrolQueueLeaveDialogBuilder.setPositiveButton(
                            "Yes, I Have refueled",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit(); //get the editor
                                    editor.remove(StationCommonConstants.IN_QUEUE_STATION_ID); //remove station id
                                    editor.remove(StationCommonConstants.QUEUE); //remove queue
                                    editor.apply(); //apply the changes

                                    currentlyJoinedQueueStationId[0] = sharedPreferences.getString(StationCommonConstants.IN_QUEUE_STATION_ID, "");
                                    queueType[0] = sharedPreferences.getString(StationCommonConstants.QUEUE, "");

                                    //make the remote call to decrement the petrol queue length
                                    decrementPetrolQueue();

                                    updateQueueButtons(currentlyJoinedQueueStationId[0], queueType[0], fuelStation.getId());
                                }
                            }
                    );

                    petrolQueueLeaveDialogBuilder.setNegativeButton(
                            "No, But I am leaving",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit(); //get the editor
                                    editor.remove(StationCommonConstants.IN_QUEUE_STATION_ID); //remove station id
                                    editor.remove(StationCommonConstants.QUEUE); //remove queue
                                    editor.apply(); //apply the changes

                                    currentlyJoinedQueueStationId[0] = sharedPreferences.getString(StationCommonConstants.IN_QUEUE_STATION_ID, "");
                                    queueType[0] = sharedPreferences.getString(StationCommonConstants.QUEUE, "");

                                    //make the remote call to decrement the petrol queue length
                                    decrementPetrolQueue();

                                    updateQueueButtons(currentlyJoinedQueueStationId[0], queueType[0], fuelStation.getId());
                                }
                            }
                    );

                    petrolQueueLeaveDialogBuilder.setNeutralButton(
                            "Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            }
                    );

                    AlertDialog petrolQueueLeaveAlert = petrolQueueLeaveDialogBuilder.create(); //create the alert
                    petrolQueueLeaveAlert.show(); //show the alert

                    /*SharedPreferences.Editor editor = sharedPreferences.edit(); //get the editor
                    editor.remove(StationCommonConstants.IN_QUEUE_STATION_ID); //remove station id
                    editor.remove(StationCommonConstants.QUEUE); //remove queue
                    editor.apply(); //apply the changes

                    currentlyJoinedQueueStationId = sharedPreferences.getString(StationCommonConstants.IN_QUEUE_STATION_ID,"");
                    queueType = sharedPreferences.getString(StationCommonConstants.QUEUE, "");

                    //make the remote call to decrement the petrol queue length
                    decrementPetrolQueue();

                    updateQueueButtons(currentlyJoinedQueueStationId, queueType, fuelStation.getId());*/
                }
            } else {
                //if the user is not in this station's this queue, user cannot join this queue too
                //display the related messages
                Toast.makeText(this, "You are already in a different queue", Toast.LENGTH_SHORT).show();
            }


        }

    }

    public void updateSharedPreferences() {

    }

    //button click to update the diesel queue
    public void dieselQueueUpdateButtonClick(View view) {

        if (isUserNotInAQueue()) {
            //if user is not in a queue user is requesting to and can join this queue
            sharedPreferences = getSharedPreferences(StationCommonConstants.STATION_SHARED_PREF_NAME, Context.MODE_PRIVATE);

            //set the station id and queue type in ths shared preferences
            SharedPreferences.Editor editor = sharedPreferences.edit(); //get the editor
            editor.putString(StationCommonConstants.IN_QUEUE_STATION_ID, this.fuelStation.getId()); //get this view's fuel station id
            editor.putString(StationCommonConstants.QUEUE, "diesel"); //set the queue type
            editor.apply(); //apply the changes

            String currentlyJoinedQueueStationId = sharedPreferences.getString(StationCommonConstants.IN_QUEUE_STATION_ID, "");
            String queueType = sharedPreferences.getString(StationCommonConstants.QUEUE, "");

            //make the remote call to increment the diesel queue length
            incrementDieselQueue();

            updateQueueButtons(currentlyJoinedQueueStationId, queueType, fuelStation.getId());
        } else {
            //the user is already in a queue
            //check whether it is this station's this queue
            //if it is this station's this queue, user is leaving
            //handle the leaving logic
            sharedPreferences = getSharedPreferences(StationCommonConstants.STATION_SHARED_PREF_NAME, Context.MODE_PRIVATE);
            final String[] currentlyJoinedQueueStationId = {sharedPreferences.getString(StationCommonConstants.IN_QUEUE_STATION_ID, "")};
            final String[] queueType = {sharedPreferences.getString(StationCommonConstants.QUEUE, "")};

            Log.d(TAG, "User is in queue in station with id : " + currentlyJoinedQueueStationId[0]);
            Log.d(TAG, "User is in queue type : " + queueType[0]);
            if (currentlyJoinedQueueStationId[0].equalsIgnoreCase(this.fuelStation.getId())) {
                //user is in a queue of this station

                //check whether the user in this queue
                queueType[0] = sharedPreferences.getString(StationCommonConstants.QUEUE, "");
                if (queueType[0].equalsIgnoreCase("diesel")) {
                    //user is in this queue
                    //user is leaving the queue


                    //set the alert dialog
                    AlertDialog.Builder dieselQueueLeaveDialogBuilder = new AlertDialog.Builder(this);
                    dieselQueueLeaveDialogBuilder.setCancelable(true);
                    dieselQueueLeaveDialogBuilder.setTitle("Have you refueled?"); //set the title

                    //set the button for leaving after refueling
                    dieselQueueLeaveDialogBuilder.setPositiveButton(
                            "Yes. I have refueled",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit(); //get the editor
                                    editor.remove(StationCommonConstants.IN_QUEUE_STATION_ID); //remove station id
                                    editor.remove(StationCommonConstants.QUEUE); //remove queue
                                    editor.apply(); //apply the changes

                                    currentlyJoinedQueueStationId[0] = sharedPreferences.getString(StationCommonConstants.IN_QUEUE_STATION_ID, "");
                                    queueType[0] = sharedPreferences.getString(StationCommonConstants.QUEUE, "");

                                    //make the remote call to decrement the diesel queue length
                                    decrementDieselQueue();

                                    updateQueueButtons(currentlyJoinedQueueStationId[0], queueType[0], fuelStation.getId());

                                }
                            }
                    );

                    //set the button for leaving without refueling
                    dieselQueueLeaveDialogBuilder.setNegativeButton(
                            "No. But I am leaving",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit(); //get the editor
                                    editor.remove(StationCommonConstants.IN_QUEUE_STATION_ID); //remove station id
                                    editor.remove(StationCommonConstants.QUEUE); //remove queue
                                    editor.apply(); //apply the changes

                                    currentlyJoinedQueueStationId[0] = sharedPreferences.getString(StationCommonConstants.IN_QUEUE_STATION_ID, "");
                                    queueType[0] = sharedPreferences.getString(StationCommonConstants.QUEUE, "");

                                    //make the remote call to decrement the diesel queue length
                                    decrementDieselQueue();

                                    updateQueueButtons(currentlyJoinedQueueStationId[0], queueType[0], fuelStation.getId());

                                }
                            }
                    );


                    //set the button for cancelling
                    dieselQueueLeaveDialogBuilder.setNeutralButton(
                            "Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            }
                    );

                    AlertDialog dieselQueueLeaveAlert = dieselQueueLeaveDialogBuilder.create(); //create the alert
                    dieselQueueLeaveAlert.show(); //show the alert


                }
            } else {
                //if the user is not in this station's this queue, user cannot join this queue too
                //display the related messages
                Toast.makeText(this, "You are already in a different queue", Toast.LENGTH_SHORT).show();
            }
        }

    }

    //method to remote call to create logs when joining and leaving the queues
    public void createLogEntry(String refuelStatus){

    }

    //method to remote increment petrol queue
    public void incrementPetrolQueue() {

        //create an instance of HTTPUrl
        HttpUrl url = HttpUrl.parse(CommonConstants.REMOTE_URL)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("FuelStations")
                .addPathSegment("IncrementPetrolQueueLength")
                .addPathSegment(fuelStation.getId()) //set this view's station id to path
                .build();


        //create JSON object for queue log request
        JSONObject queueLogRequestJsonObject = new JSONObject();
        try {
            queueLogRequestJsonObject.put("customerUsername", username);
            queueLogRequestJsonObject.put("stationId", fuelStation.getId());
            queueLogRequestJsonObject.put("stationLicense", fuelStation.getLicense());
            queueLogRequestJsonObject.put("stationName", fuelStation.getStationName());
            queueLogRequestJsonObject.put("refuelStatus", "not-applicable");
        }catch (JSONException e){
            e.printStackTrace();
        }

        String queueLogRequestString = queueLogRequestJsonObject.toString();
        //empty request body
        RequestBody requestBody = RequestBody.create(queueLogRequestString, JSON);

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
                if (response.isSuccessful()) {
                    //if response is successful handle success logic
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(StationSingleViewActivity.this, "Successfully joined the queue", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    //handle failure response logic

                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //show the response error
                            getAlertDialog("Failure", "Message : " + body);
                        }
                    });
                }

            }
        });

    }

    //method to remote decrement petrol queue
    public void decrementPetrolQueue() {
        //create an instance of HTTPUrl
        HttpUrl url = HttpUrl.parse(CommonConstants.REMOTE_URL)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("FuelStations")
                .addPathSegment("DecrementPetrolQueueLength")
                .addPathSegment(fuelStation.getId()) //set this view's station id to path
                .build();

        String sampleString = "sample";
        //empty request body
        RequestBody requestBody = RequestBody.create(sampleString, JSON);

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
                if (response.isSuccessful()) {
                    //if response is successful handle success logic
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(StationSingleViewActivity.this, "Successfully left the queue", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    //handle failure response logic

                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //show the response error
                            getAlertDialog("Failure", "Message : " + body);
                        }
                    });
                }

            }
        });
    }

    //method to remote increment diesel queue
    public void incrementDieselQueue() {
        //create an instance of HTTPUrl
        HttpUrl url = HttpUrl.parse(CommonConstants.REMOTE_URL)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("FuelStations")
                .addPathSegment("IncrementDieselQueueLength")
                .addPathSegment(fuelStation.getId()) //set this view's station id to path
                .build();

        //create JSON object for queue log request
        JSONObject queueLogRequestJsonObject = new JSONObject();
        try {
            queueLogRequestJsonObject.put("customerUsername", username);
            queueLogRequestJsonObject.put("stationId", fuelStation.getId());
            queueLogRequestJsonObject.put("stationLicense", fuelStation.getLicense());
            queueLogRequestJsonObject.put("stationName", fuelStation.getStationName());
            queueLogRequestJsonObject.put("refuelStatus", "not-applicable");
        }catch (JSONException e){
            e.printStackTrace();
        }

        String queueLogRequestString = queueLogRequestJsonObject.toString();
        //empty request body
        RequestBody requestBody = RequestBody.create(queueLogRequestString, JSON);

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
                if (response.isSuccessful()) {
                    //if response is successful handle success logic
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(StationSingleViewActivity.this, "Successfully joined the queue", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    //handle failure response logic

                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //show the response error
                            getAlertDialog("Failure", "Message : " + body);
                        }
                    });
                }

            }
        });
    }

    //method to remote decrement diesel queue
    public void decrementDieselQueue() {
        //create an instance of HTTPUrl
        HttpUrl url = HttpUrl.parse(CommonConstants.REMOTE_URL)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("FuelStations")
                .addPathSegment("DecrementDieselQueueLength")
                .addPathSegment(fuelStation.getId()) //set this view's station id to path
                .build();

        String sampleString = "sample";
        //empty request body
        RequestBody requestBody = RequestBody.create(sampleString, JSON);

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
                if (response.isSuccessful()) {
                    //if response is successful handle success logic
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(StationSingleViewActivity.this, "Successfully left the queue", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    //handle failure response logic

                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //show the response error
                            getAlertDialog("Failure", "Message : " + body);
                        }
                    });
                }

            }
        });
    }

    //method to check whether the user is not in a queue
    //returns true of user is not in a queue
    //returns false if user is in a queue
    public boolean isUserNotInAQueue() {
        sharedPreferences = getSharedPreferences(StationCommonConstants.STATION_SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String currentlyJoinedQueueStationId = sharedPreferences.getString(StationCommonConstants.IN_QUEUE_STATION_ID, "");

        if (currentlyJoinedQueueStationId.isEmpty()) {
            return true;
        } else {
            return false;
        }
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

    //method called when toolbar back button is clicked
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //handle back press
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
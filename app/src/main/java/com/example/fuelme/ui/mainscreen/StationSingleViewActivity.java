package com.example.fuelme.ui.mainscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fuelme.R;
import com.example.fuelme.commonconstants.StationCommonConstants;
import com.example.fuelme.models.FuelStation;
import com.example.fuelme.ui.notice.NoticeListCustomerActivity;

public class StationSingleViewActivity extends AppCompatActivity {

    String TAG = "demo";
    TextView textViewStationName, textViewStationAddress, textViewOpenStatus,
            textViewPetrolAvailabilityStatus, textViewDieselAvailabilityStatus, textViewPetrolQueueLength, textViewDieselQueueLength;
    Button petrolQueueUpdateButton, dieselQueueUpdateButton, stationPhoneNumberButton, stationEmailButton, websiteButton,
            viewFeedbackButton, viewNoticesButton;
    SharedPreferences sharedPreferences;
    String station_id;

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

        //instantiate shared preferences
        //sharedPreferences = getSharedPreferences(StationCommonConstants.STATION_SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //get the extras
        Bundle extras  = getIntent().getExtras();
        if (extras != null){
            FuelStation fuelStation = (FuelStation) extras.getSerializable("selected_fuel_station"); //get the serializable and cast into fuel station object

            //get string values of queue lengths
            String petrolQueueLengthString = String.valueOf(fuelStation.getPetrolQueueLength());
            String dieselQueueLengthString = String.valueOf(fuelStation.getDieselQueueLength());

            //open status
            String openStatus = "Not Assigned";

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
                textViewPetrolAvailabilityStatus.setTextColor(Color.parseColor("#0E8921"));
            }
            else if (fuelStation.getPetrolStatus().equalsIgnoreCase("unavailable")){
                //change color to red
                textViewPetrolAvailabilityStatus.setTextColor(Color.parseColor("#FF0000"));
            }

            //change diesel status text view color based on diesel availability
            if (fuelStation.getDieselStatus().equalsIgnoreCase("available")){
                //change color to green
                textViewDieselAvailabilityStatus.setTextColor(Color.parseColor("#0E8921"));
            }
            else if (fuelStation.getDieselStatus().equalsIgnoreCase("unavailable")){
                //change color to red
                textViewDieselAvailabilityStatus.setTextColor(Color.parseColor("#FF0000"));
            }

            //set shared preferences listener here
            SharedPreferences.OnSharedPreferenceChangeListener sharedPreferencesChangedListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                    sharedPreferences = getSharedPreferences(StationCommonConstants.STATION_SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //get the station id of the joined queue from shared preferences
                    String currentlyJoinedQueueStationId = sharedPreferences.getString(StationCommonConstants.IN_QUEUE_STATION_ID,"");

                    //check if the current station id of the joined queue is not empty
                    if (!currentlyJoinedQueueStationId.isEmpty()){
                        //get the queue type
                        String queueType = sharedPreferences.getString(StationCommonConstants.QUEUE,"");
                        updateQueueButtons(currentlyJoinedQueueStationId,queueType, fuelStation.getId());
                    }
                }
            };

            station_id = fuelStation.getId();

            viewNoticesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(StationSingleViewActivity.this, NoticeListCustomerActivity.class);
                    intent.putExtra("station_id", station_id);
                    startActivity(intent);
                }
            });
        }


    }

    //update the queue buttons based on the shared preferences
    public void updateQueueButtons(String currentlyJoinedQueueStationId, String queueType, String viewFuelStationId){

        //check if the currently joined queue's station id is this view's station id
        if (viewFuelStationId.equalsIgnoreCase(currentlyJoinedQueueStationId)){
            //check which is the joined queue

            if (queueType.equalsIgnoreCase("petrol")){
                //user is in this station's petrol queue
                //change the petrol queue button attributes
                petrolQueueUpdateButton.setText("Leave the queue");
                petrolQueueUpdateButton.setBackgroundColor(Color.parseColor("#F3AD25"));
            }
            else if (queueType.equalsIgnoreCase("diesel")){
                //user is in this station's diesel queue
                //change the diesel queue button attributes
                dieselQueueUpdateButton.setText("Leave the queue");
                dieselQueueUpdateButton.setBackgroundColor(Color.parseColor("#F3AD25"));
            }
        }

        // if the currently joined queue's station id is not this view's station id, user is in a different queue

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
package com.example.fuelme.ui.update_station_screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fuelme.R;
import com.example.fuelme.models.FuelStation;
import com.example.fuelme.ui.notice.NoticeCreateActivity;
import com.example.fuelme.ui.notice.NoticeListStationActivity;
import com.google.android.material.tabs.TabLayout;

public class UpdateStationActivity extends AppCompatActivity {

    TextView textViewStationName,  textViewOpenStatus, textViewPetrolAvailability, textViewPetrolQueueLength, textViewDieselAvailability, textViewDieselQueueLength;
    Button editButton, petrolStatusUpdateButton, dieselStatusUpdateButton, stationOpenStatusUpdateButton, postNoticeButton, viewNoticesButton, viewFeedbackButton;
    String station_id;

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

        station_id = "default";

        //get the extras
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            FuelStation fuelStation = (FuelStation) extras.getSerializable("selected_fuel_station"); //get the serializable and cast into fuel station object

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

            station_id = fuelStation.getId();
        }

        postNoticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateStationActivity.this, NoticeCreateActivity.class);
                intent.putExtra("station_id", station_id);
                startActivity(intent);
            }
        });

        viewNoticesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateStationActivity.this, NoticeListStationActivity.class);
                intent.putExtra("station_id", station_id);
                startActivity(intent);
            }
        });
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
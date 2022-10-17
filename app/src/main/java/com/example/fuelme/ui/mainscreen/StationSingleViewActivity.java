package com.example.fuelme.ui.mainscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.fuelme.R;
import com.example.fuelme.models.FuelStation;

public class StationSingleViewActivity extends AppCompatActivity {

    String TAG = "demo";
    TextView textViewStationName, textViewStationAddress, textViewOpenStatus,
            textViewPetrolAvailabilityStatus, textViewDieselAvailabilityStatus, textViewPetrolQueueLength, textViewDieselQueueLength;
    Button petrolQueueUpdateButton, dieselQueueUpdateButton, stationPhoneNumberButton, stationEmailButton, websiteButton;

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

        //get the extras
        Bundle extras  = getIntent().getExtras();
        if (extras != null){
            FuelStation fuelStation = (FuelStation) extras.getSerializable("selected_fuel_station"); //get the serializable and cast into fuel station object

            //get string values of queue lengths
            String petrolQueueLengthString = String.valueOf(fuelStation.getPetrolQueueLength());
            String dieselQueueLengthString = String.valueOf(fuelStation.getDieselQueueLength());

            //set the details of the station in the views
            textViewStationName.setText(fuelStation.getStationName());
            textViewStationAddress.setText(fuelStation.getStationAddress());
            textViewOpenStatus.setText(fuelStation.getOpenStatus());
            textViewPetrolAvailabilityStatus.setText(fuelStation.getPetrolStatus());
            textViewPetrolQueueLength.setText(petrolQueueLengthString);
            textViewDieselAvailabilityStatus.setText(fuelStation.getDieselStatus());
            textViewDieselQueueLength.setText(dieselQueueLengthString);
            stationPhoneNumberButton.setText(fuelStation.getStationPhoneNumber());
            stationEmailButton.setText(fuelStation.getStationEmail());
            websiteButton.setText(fuelStation.getStationWebsite());

            //change station open text view color based on open status
            if (fuelStation.getOpenStatus().equalsIgnoreCase("open")){
                //textViewOpenStatus.setTextColor();
                //change color to green
            }
            else if (fuelStation.getOpenStatus().equalsIgnoreCase("closed")){
                //change color to red
            }

            //change petrol status text view color based on petrol availability
            if (fuelStation.getPetrolStatus().equalsIgnoreCase("available")){
                //change color to green
            }
            else if (fuelStation.getPetrolStatus().equalsIgnoreCase("unavailable")){
                //change color to red
            }

            //change diesel status text view color based on diesel availability
            if (fuelStation.getDieselStatus().equalsIgnoreCase("available")){
                //change color to green
            }
            else if (fuelStation.getDieselStatus().equalsIgnoreCase("unavailable")){
                //change color to red
            }
        }


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
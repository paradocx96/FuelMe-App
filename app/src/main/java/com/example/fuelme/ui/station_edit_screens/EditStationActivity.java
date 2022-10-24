package com.example.fuelme.ui.station_edit_screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.EditText;

import com.example.fuelme.R;
import com.example.fuelme.models.FuelStation;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class EditStationActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient(); //okhttp client instance
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    final String TAG = "demo";//debug tag

    EditText editTextLicense, editTextStationName, editTextStationAddress, editTextStationEmail, editTextPhoneNumber, editTextStationWebsite;
    FuelStation fuelStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_station);

        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_station_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //set the views
        editTextLicense = findViewById(R.id.edtText_license_edit_station);
        editTextStationName = findViewById(R.id.edtText_stationName_edit_station);
        editTextStationAddress = findViewById(R.id.edtText_stationEmail_edit_station);
        editTextStationEmail = findViewById(R.id.edtText_stationEmail_edit_station);
        editTextPhoneNumber = findViewById(R.id.edtText_stationPhone_edit_station);
        editTextStationWebsite = findViewById(R.id.edtText_stationWebsite_edit_station);

        //get the extras and set to fuel station
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            fuelStation = (FuelStation) extras.getSerializable("selected_fuel_station");

            //set the edit texts with fuel station object details
            setEditTextsWithFuelStationObject();
        }
    }

    //set the edit texts with fuel station current fuel station details
    public void setEditTextsWithFuelStationObject(){
        editTextLicense.setText(fuelStation.getLicense());
        editTextStationName.setText(fuelStation.getStationName());
        editTextStationAddress.setText(fuelStation.getStationAddress());
        editTextStationEmail.setText(fuelStation.getStationEmail());
        editTextPhoneNumber.setText(fuelStation.getStationPhoneNumber());
        editTextStationWebsite.setText(fuelStation.getStationWebsite());
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
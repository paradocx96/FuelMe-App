package com.example.fuelme.ui.delete_station_screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fuelme.R;

public class DeleteStationActivity extends AppCompatActivity {

    private final String TAG = "demo";

    TextView txtViewStationName, txtViewStationAddress, txtViewLicense, txtViewStationId;
    EditText editTextStationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_station);

        //instantiate toolbar and set the back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.delete_station_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //setup the views
        txtViewStationName = findViewById(R.id.txtView_stationName_delete_station);
        txtViewStationAddress = findViewById(R.id.txtView_stationAddress_delete_station);
        txtViewLicense = findViewById(R.id.txtView_license_delete_station);
        txtViewStationId = findViewById(R.id.txtVIew_stationId_delete_station);
        editTextStationId = findViewById(R.id.edtTxt_stationId_delete_station);
    }

    //button click method for delete station
    public void deleteStationInDeleteActivityButtonClick(View view){

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
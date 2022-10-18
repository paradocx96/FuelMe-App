package com.example.fuelme.ui.owner_dashboard_screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fuelme.R;
import com.example.fuelme.ui.register_station_screen.RegisterStationActivity;
import com.example.fuelme.ui.update_station_screen.UpdateStationActivity;

public class OwnerDashboardActivity extends AppCompatActivity {

    Button btnRegisterStation, btnUpdateStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_dashboard);

        //instantiate toolbar and set the back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.owner_dashboard_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnRegisterStation = findViewById(R.id.btn_registerStation);
        btnUpdateStation = findViewById(R.id.btn_updateStation);


    }

    //method to navigate to register station activity
    public void navigateToRegisterStation(View view){
        Intent intent = new Intent(this, RegisterStationActivity.class);
        startActivity(intent);
    }

    //method to navigate to update station activity
    public void  navigateToUpdateStation(View view){
        Intent intent = new Intent(this, UpdateStationActivity.class);
        startActivity(intent);
    }

    public void navigateToViewAllOwnedStations(View view){
        Intent intent = new Intent(this, OwnerStationsActivity.class);
        startActivity(intent);
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
package com.example.fuelme.ui.mainscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.example.fuelme.R;

public class StationSingleViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_single_view);

        //instantiate toolbar and set the back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.station_single_view_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
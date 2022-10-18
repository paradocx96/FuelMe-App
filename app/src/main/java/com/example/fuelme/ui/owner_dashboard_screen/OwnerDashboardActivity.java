package com.example.fuelme.ui.owner_dashboard_screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fuelme.R;

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

    public void navigateToRegisterStation(View view){
        //Intent intent = new Intent(this, )
    }

    public void  navigateToUpdate(View view){

    }
}
package com.example.fuelme.ui.owner_dashboard_screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fuelme.R;
import com.example.fuelme.ui.customer_dashboard.CustomerDashboardActivity;
import com.example.fuelme.ui.register_station_screen.RegisterStationActivity;
import com.example.fuelme.ui.update_station_screen.UpdateStationActivity;

/**
* IT19014128
* A.M.W.W.R.L. Wataketiya
*
* Activity class for Owner Dashboard
 *
 * References:
 *  https://developer.android.com/docs
 *  https://square.github.io/okhttp/
* */
public class OwnerDashboardActivity extends AppCompatActivity {

    TextView textViewUserFullName, textViewUserEmail;
    Button btnRegisterStation;
    SharedPreferences sharedPreferences;

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
        textViewUserFullName = findViewById(R.id.txtView_fullName_owner_dashboard);
        textViewUserEmail = findViewById(R.id.txtView_email_owner_dashboard);

        //get full name and email for the current user
        sharedPreferences = getSharedPreferences("login_data", MODE_PRIVATE);
        String fullName = sharedPreferences.getString("user_full_name","");
        String email = sharedPreferences.getString("user_email", "");

        //set the full name and email

        if (!fullName.isEmpty()){
            textViewUserFullName.setText(fullName);
        }
        if (!email.isEmpty()){
            textViewUserEmail.setText(email);
        }


    }

    //method to navigate to register station activity
    public void navigateToRegisterStation(View view){
        Intent intent = new Intent(this, RegisterStationActivity.class);
        startActivity(intent);
    }

    //method to navigate to view all the stations owned by the current user
    public void navigateToViewAllOwnedStations(View view){
        Intent intent = new Intent(this, OwnerStationsActivity.class);
        startActivity(intent);
    }

    //temporary method to navigate to the customer dashboard
    public void navigateToCustomerDashboard(View view){
        Intent intent = new Intent(this, CustomerDashboardActivity.class);
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
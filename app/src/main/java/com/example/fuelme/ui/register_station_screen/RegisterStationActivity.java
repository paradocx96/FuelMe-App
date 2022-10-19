package com.example.fuelme.ui.register_station_screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fuelme.R;

import java.lang.reflect.Method;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

/*
IT19014128
A.M.W.W.R.L. Wataketiya
Activity for registering a new station
*/



public class RegisterStationActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient(); //okhttp client instance
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    final String TAG = "demo";//debug tag

    TextView textViewLicense, textViewStationName, textViewStationAddress, textViewStationEmail, textViewStationWebsite;
    EditText editTextLicense, editTextStationName, editTextStationAddress, editTextStationEmail, editTextStationWebsite;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_station);

        //instantiate toolbar and set the back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.register_station_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);




        //register the views
        textViewLicense = findViewById(R.id.txtView_license_register_station);
        textViewStationName = findViewById(R.id.txtView_stationName_register_station);
        textViewStationAddress = findViewById(R.id.txtView_stationAddress_register_station);
        textViewStationEmail = findViewById(R.id.txtView_email_register_station);
        textViewStationWebsite = findViewById(R.id.txtView_website_register_station);
        editTextLicense = findViewById(R.id.edtText_license_register_station);
        editTextStationName = findViewById(R.id.edtText_stationName_register_station);
        editTextStationAddress = findViewById(R.id.edtText_stationAddress_register_station);
        editTextStationEmail = findViewById(R.id.edtText_stationEmail_register_station);
        editTextStationWebsite = findViewById(R.id.edtText_stationWebsite_register_station);

    }

    //button click for register station button
    public void registerStationButtonClick(View view){
        validateLicense();
    }

    //validate the license
    public void validateLicense(){
        String licenseString= editTextLicense.getText().toString();
        if (licenseString.isEmpty()){
            //the text field is empty
            //handle empty logic
            textViewLicense.setTextColor(Color.parseColor("#FF0000")); //set the text color to red
        }
        else {
            //the text field is not empty


            int nightModeFlags = getApplicationContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK; //set night mode mask
            //set the text color based on night mode
            switch (nightModeFlags){
                case Configuration.UI_MODE_NIGHT_NO:
                    //night mode is not enabled. handle black text
                    textViewLicense.setTextColor(Color.parseColor("#FF000000"));
                    break;
                case Configuration.UI_MODE_NIGHT_YES:
                    //night mode is enabled handle white text
                    textViewLicense.setTextColor(Color.parseColor("#FFFFFFFF"));
                    break;
                default:
                    //not defined
                    textViewLicense.setTextColor(Color.parseColor("#FF000000"));
                    break;
            }
            //textViewLicense.setTextColor();
        }
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
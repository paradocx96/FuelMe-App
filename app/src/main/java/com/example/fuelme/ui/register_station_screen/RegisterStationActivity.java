package com.example.fuelme.ui.register_station_screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fuelme.R;
import com.example.fuelme.helpers.NightModeHelper;

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
        validateStationName();
        validateStationAddress();
        validateStationEmail();
        validateStationWebsite();
    }

    //validate the license
    public void validateLicense(){
        isLicenseNotEmpty();
    }

    //validate the station name
    public void validateStationName(){
        isStationNameNotEmpty();
    }

    //validate the station address
    public void validateStationAddress(){
        isStationAddressNotEmpty();
    }

    //validate the station email
    public void validateStationEmail(){
        isStationEmailNotEmpty();
    }

    //validate the website
    public void validateStationWebsite(){
        isStationWebsiteNotEmpty();
    }

    //returns whether the license field is empty and performs UI related changes
    public boolean isLicenseNotEmpty(){
        String licenseString= editTextLicense.getText().toString();
        if (licenseString.isEmpty()){
            //the text field is empty
            //handle empty logic
            setTextViewColorToRed(textViewLicense);
            return false;
        }
        else {
            //the text field is not empty
            //set the default colors for text

            setTextViewColorToDefault(textViewLicense);
            return true;
        }
    }

    //check whether the station name is empty and perform related UI changes
    public boolean isStationNameNotEmpty(){
        String editTextString= editTextStationName.getText().toString();
        if (editTextString.isEmpty()){
            //the text field is empty
            //handle empty logic
            setTextViewColorToRed(textViewStationName);
            return false;
        }
        else {
            //the text field is not empty
            //set the default colors for text

            setTextViewColorToDefault(textViewStationName);
            return true;
        }
    }

    //check whether the station address is empty and perform related UI changes
    public boolean isStationAddressNotEmpty(){
        String editTextString= editTextStationAddress.getText().toString();
        if (editTextString.isEmpty()){
            //the text field is empty
            //handle empty logic
            setTextViewColorToRed(textViewStationAddress);
            return false;
        }
        else {
            //the text field is not empty
            //set the default colors for text

            setTextViewColorToDefault(textViewStationAddress);
            return true;
        }
    }

    //check whether the station email is empty and perform related UI changes
    public boolean isStationEmailNotEmpty(){
        String editTextString= editTextStationEmail.getText().toString();
        if (editTextString.isEmpty()){
            //the text field is empty
            //handle empty logic
            setTextViewColorToRed(textViewStationEmail);
            return false;
        }
        else {
            //the text field is not empty
            //set the default colors for text

            setTextViewColorToDefault(textViewStationEmail);
            return true;
        }
    }

    //check whether the station website is empty and perform related UI changes
    public boolean isStationWebsiteNotEmpty(){
        String editTextString= editTextStationEmail.getText().toString();
        if (editTextString.isEmpty()){
            //the text field is empty
            //handle empty logic
            //currently website is not mandatory
            //therefore do not change color
            return false;
        }
        else {
            //the text field is not empty
            //set the default colors for text
            setTextViewColorToDefault(textViewStationEmail);
            return true;
        }
    }

    //takes an edit text and returns whether it is empty
    public boolean isTextViewEmpty(EditText editText){
        String editTextString = editText.getText().toString();
        return editTextString.isEmpty();
    }

    //takes a text view and sets its color to red
    public void setTextViewColorToRed(TextView textView){
        textView.setTextColor(Color.parseColor("#FF0000")); //set the text color to red
    }

    //takes a text view and sets its color to default text colors
    public void setTextViewColorToDefault(TextView textView){
        String theme = NightModeHelper.getMode(this);
        switch (theme){
            case "light":
                //night mode is not enabled. handle black text
                textView.setTextColor(Color.parseColor("#FF000000"));
                break;
            case "dark":
                //night mode is enabled handle white text
                textView.setTextColor(Color.parseColor("#FFFFFFFF"));
                break;
            default:
                //not defined
                textView.setTextColor(Color.parseColor("#FF000000"));
                break;
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
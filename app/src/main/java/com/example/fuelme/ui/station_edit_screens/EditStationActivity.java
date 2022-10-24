package com.example.fuelme.ui.station_edit_screens;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuelme.R;
import com.example.fuelme.helpers.NightModeHelper;
import com.example.fuelme.models.FuelStation;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class EditStationActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient(); //okhttp client instance
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    final String TAG = "demo";//debug tag

    TextView textViewLicense, textViewStationName, textViewStationAddress, textViewStationEmail, textViewPhoneNumber, textViewStationWebsite;
    EditText editTextLicense, editTextStationName, editTextStationAddress, editTextStationEmail, editTextPhoneNumber, editTextStationWebsite;
    FuelStation fuelStation;
    AlertDialog.Builder progressDialogBuilder;
    AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_station);

        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_station_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //set the views
        //text views
        textViewLicense = findViewById(R.id.txtView_license_edit_station);
        textViewStationName = findViewById(R.id.txtView_stationName_edit_station);
        textViewStationAddress = findViewById(R.id.txtView_stationAddress_edit_station);
        textViewStationEmail = findViewById(R.id.txtView_email_edit_station);
        textViewPhoneNumber = findViewById(R.id.txtView_stationPhone_edit_station);
        textViewStationWebsite = findViewById(R.id.txtView_website_edit_station);
        //edit texts
        editTextLicense = findViewById(R.id.edtText_license_edit_station);
        editTextStationName = findViewById(R.id.edtText_stationName_edit_station);
        editTextStationAddress = findViewById(R.id.edtText_stationAddress_edit_station);
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

    public void saveChangesButtonClick(View view){
        boolean validStatus = validateAllFields();
    }

    //method to validate all the fields
    public boolean validateAllFields(){
        boolean licenseNotEmpty = validateLicense();
        boolean nameNotEmpty = validateStationName();
        boolean addressNotEmpty = validateStationAddress();
        boolean phoneNotEmpty = validateStationPhoneNumber();
        boolean emailNotEmpty = isStationEmailNotEmpty();


        if (licenseNotEmpty && nameNotEmpty && addressNotEmpty && phoneNotEmpty && emailNotEmpty){
            //all the mandatory fields are filled
            //validate email
            boolean isEmailValid = validateStationEmail();
            return isEmailValid; //if email is valid the form is valid
        }
        else {
            //one or more mandatory fields are missing
            Toast.makeText(this, "One or more mandatory fields are missing",Toast.LENGTH_SHORT).show(); //show a toast with message
            return false;
        }
    }

    //validate the license
    public boolean validateLicense(){
        return isLicenseNotEmpty();
    }

    //validate the station name
    public boolean validateStationName(){
        return isStationNameNotEmpty();
    }

    //validate the station address
    public boolean validateStationAddress(){
        return isStationAddressNotEmpty();
    }

    //validate phone number
    public boolean validateStationPhoneNumber(){
        return isStationPhoneNumberNotEmpty();
    }

    //validate the station email
    //returns true if valid, false if not
    public boolean validateStationEmail(){

        //validate that email is of valid pattern
        String emailString = editTextStationEmail.getText().toString();
        if (Patterns.EMAIL_ADDRESS.matcher(emailString).matches()){
            //the string is a matching email pattern
            return true;
        }
        else {
            //the string is not a matching email pattern
            Toast.makeText(this, "Email format is invalid",Toast.LENGTH_SHORT).show(); //show a toast with message
            return false;
        }


    }


    //progress bar in an alert dialog
    public AlertDialog.Builder getDialogProgressBar(){
        if (progressDialogBuilder == null){
            progressDialogBuilder = new AlertDialog.Builder(this);
            progressDialogBuilder.setCancelable(false);
            progressDialogBuilder.setTitle("Registering Station");
            progressDialogBuilder.setMessage("Please wait");

            final ProgressBar progressBar = new ProgressBar(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            progressBar.setLayoutParams(layoutParams);
            progressDialogBuilder.setView(progressBar);

        }
        return progressDialogBuilder;
    }

    //generic alert dialog with OK button
    public AlertDialog.Builder getAlertDialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        return builder;
    }

    //returns whether the license is empty. Return true if not empty and false if empty
    public boolean isLicenseNotEmpty(){
        String licenseString = editTextLicense.getText().toString();
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
            return  true;
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

    //check whether the station phone number is empty and perform related UI changes
    public boolean isStationPhoneNumberNotEmpty(){
        String editTextString= editTextPhoneNumber.getText().toString();
        if (editTextString.isEmpty()){
            //the text field is empty
            //handle empty logic
            setTextViewColorToRed(textViewPhoneNumber);
            return false;
        }
        else {
            //the text field is not empty
            //set the default colors for text

            setTextViewColorToDefault(textViewPhoneNumber);
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
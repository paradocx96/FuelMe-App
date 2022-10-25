package com.example.fuelme.ui.station_edit_screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuelme.R;
import com.example.fuelme.commonconstants.CommonConstants;
import com.example.fuelme.helpers.NightModeHelper;
import com.example.fuelme.models.FuelStation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

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

        //set the edit text background colors
        setEditTextColors();
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

        if (validStatus){
            // if the status is valid
            //get the strings from edit texts
            String licenseString = editTextLicense.getText().toString();
            String stationNameString = editTextStationName.getText().toString();
            String stationAddressString = editTextStationAddress.getText().toString();
            String stationPhoneNumberString = editTextPhoneNumber.getText().toString();
            String stationEmailString = editTextStationEmail.getText().toString();
            String stationWebsiteString = editTextStationWebsite.getText().toString();

            //if website is empty reassign it
            if (stationWebsiteString.isEmpty()){
                stationWebsiteString = "not-given";
            }

            //instantiate and set the fuel station instance
            FuelStation fuelStationToUpdate = new FuelStation();
            //set fuel station attributes
            fuelStationToUpdate.setId(fuelStation.getId());

            //get items from edit texts
            fuelStationToUpdate.setLicense(licenseString);
            fuelStationToUpdate.setStationName(stationNameString);
            fuelStationToUpdate.setStationAddress(stationAddressString);
            fuelStationToUpdate.setStationPhoneNumber(stationPhoneNumberString);
            fuelStationToUpdate.setStationEmail(stationEmailString);
            fuelStationToUpdate.setStationWebsite(stationWebsiteString);

            //get items from object
            fuelStationToUpdate.setOwnerUsername(fuelStation.getOwnerUsername());
            fuelStationToUpdate.setOpenStatus(fuelStation.getOpenStatus());
            fuelStationToUpdate.setPetrolQueueLength(fuelStation.getPetrolQueueLength());
            fuelStationToUpdate.setDieselQueueLength(fuelStation.getDieselQueueLength());
            fuelStationToUpdate.setPetrolStatus(fuelStation.getPetrolStatus());
            fuelStationToUpdate.setDieselStatus(fuelStation.getDieselStatus());
            fuelStationToUpdate.setLocationLatitude(fuelStation.getLocationLatitude());
            fuelStationToUpdate.setLocationLongitude(fuelStation.getLocationLongitude());


            //setting other data is not necessary since they are manually set in JSON object

            Log.d(TAG, "All fields are valid. Fuel station object is set.");

            updateStation(fuelStationToUpdate); //call remote with created station object
        }
    }

    //method to call the remote to update the station
    public void updateStation(FuelStation fuelStation){
        //instantiate a JSON object
        JSONObject stationJsonObject = new JSONObject();
        try {
            //put the data in the JSON object
            stationJsonObject.put("id", "dummy");
            stationJsonObject.put("license", fuelStation.getLicense());
            stationJsonObject.put("ownerUsername", fuelStation.getOwnerUsername());
            stationJsonObject.put("stationName", fuelStation.getStationName());
            stationJsonObject.put("stationAddress", fuelStation.getStationAddress());
            stationJsonObject.put("stationPhoneNumber", fuelStation.getStationPhoneNumber());
            stationJsonObject.put("stationEmail", fuelStation.getStationEmail());
            stationJsonObject.put("stationWebsite", fuelStation.getStationWebsite());
            stationJsonObject.put("openStatus", fuelStation.getOpenStatus());
            stationJsonObject.put("petrolQueueLength", fuelStation.getPetrolQueueLength());
            stationJsonObject.put("dieselQueueLength", fuelStation.getDieselQueueLength());
            stationJsonObject.put("petrolStatus", fuelStation.getPetrolStatus());
            stationJsonObject.put("dieselStatus", fuelStation.getDieselStatus());
            stationJsonObject.put("locationLatitude", fuelStation.getLocationLatitude());
            stationJsonObject.put("locationLongitude", fuelStation.getLocationLongitude());

        }catch (JSONException e){
            e.printStackTrace();
        }

        String stationJsonString = stationJsonObject.toString(); //get the string of the json object
        RequestBody requestBody = RequestBody.create(stationJsonString, JSON); //add the json string to request body

        String baseUrl = CommonConstants.REMOTE_URL;

        //build the url using Url builder
        HttpUrl url = HttpUrl.parse(baseUrl).newBuilder()
                .addPathSegment("api")
                .addPathSegment("FuelStations")
                .addPathSegment(fuelStation.getId())
                .build();

        //build the request
        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .build();

        //create and show progress dialog
        progressDialog = getDialogProgressBar().create();
        progressDialog.show();

        //make the call with okhttp client
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        //show failure alert dialog
                        getAlertDialog("Error", "Failed to make the call. Check your network connection").show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(EditStationActivity.this);
                            builder.setTitle("Success");
                            builder.setMessage("Saved the changes");
                            builder.setCancelable(false);
                            builder.setPositiveButton(
                                    "OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            navigateToMoreDetails(); //navigate to more details
                                            dialogInterface.dismiss(); // dismiss the dialog interface
                                        }
                                    }
                            );
                            builder.show();
                        }
                    });
                }
                else {
                    //handle failed response
                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            //show the response error
                            getAlertDialog("Failure in response", "Message : " + body).show();
                        }
                    });
                }
            }
        });
    }

    //method to navigate back to more details
    public void navigateToMoreDetails(){
        Intent intent = new Intent(EditStationActivity.this, StationMoreDetailsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //clear the top
        intent.putExtra("station_id", fuelStation.getId());
        startActivity(intent);
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
            progressDialogBuilder.setTitle("Saving Your Changes");
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

    //set the colors of edit texts
    public void setEditTextColors(){
        changeEditTextColorBasedOnTheme(editTextLicense);
        changeEditTextColorBasedOnTheme(editTextStationName);
        changeEditTextColorBasedOnTheme(editTextStationAddress);
        changeEditTextColorBasedOnTheme(editTextStationEmail);
        changeEditTextColorBasedOnTheme(editTextPhoneNumber);
        changeEditTextColorBasedOnTheme(editTextStationWebsite);
    }

    //method takes in an edit text and sets its background color to a grey tone of dark mode is enabled
    public void changeEditTextColorBasedOnTheme(EditText editText){
        String theme = NightModeHelper.getMode(this);
        switch (theme){
            case "light":
                //night mode is not enabled.
                //leave default background colors
                break;
            case "dark":
                //night mode is enabled
                //set the background color to a grey tone
                editText.setBackgroundColor(Color.parseColor("#606478"));
                break;
            default:
                //not defined
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
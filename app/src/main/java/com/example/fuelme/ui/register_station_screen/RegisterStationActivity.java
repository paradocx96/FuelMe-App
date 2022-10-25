package com.example.fuelme.ui.register_station_screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
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

/**
 * IT19014128
 * A.M.W.W.R.L. Wataketiya
 * Activity for registering a new station
 *
 * References:
 * https://developer.android.com/docs
 * https://square.github.io/okhttp/
*/



public class RegisterStationActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient(); //okhttp client instance
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    final String TAG = "demo";//debug tag
    SharedPreferences preferences;

    TextView textViewLicense, textViewStationName, textViewStationAddress, textViewStationEmail, textViewPhoneNumber, textViewStationWebsite;
    EditText editTextLicense, editTextStationName, editTextStationAddress, editTextStationEmail, editTextPhoneNumber, editTextStationWebsite;
    Button submitButton;
    AlertDialog.Builder progressDialogBuilder;
    AlertDialog progressDialog;

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
        textViewPhoneNumber = findViewById(R.id.txtView_stationPhone_register_station);
        textViewStationEmail = findViewById(R.id.txtView_email_register_station);
        textViewStationWebsite = findViewById(R.id.txtView_website_register_station);
        editTextLicense = findViewById(R.id.edtText_license_register_station);
        editTextStationName = findViewById(R.id.edtText_stationName_register_station);
        editTextStationAddress = findViewById(R.id.edtText_stationAddress_register_station);
        editTextPhoneNumber = findViewById(R.id.edtText_stationPhone_register_station);
        editTextStationEmail = findViewById(R.id.edtText_stationEmail_register_station);
        editTextStationWebsite = findViewById(R.id.edtText_stationWebsite_register_station);

        //set the edit text background colors
        setEditTextColors();

    }

    //button click for register station button
    public void registerStationButtonClick(View view){

        boolean validStatus = validateAllFields();

        if (validStatus){
            //get the currently logged in user
            preferences = getSharedPreferences("login_data", MODE_PRIVATE); //assign preferences for login data
            String currentUsername = preferences.getString("user_username", ""); //get the username from shared preferences

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
            FuelStation fuelStation = new FuelStation();
            //set fuel station attributes
            fuelStation.setId("dummy");
            fuelStation.setLicense(licenseString);
            fuelStation.setOwnerUsername(currentUsername);
            fuelStation.setStationName(stationNameString);
            fuelStation.setStationAddress(stationAddressString);
            fuelStation.setStationPhoneNumber(stationPhoneNumberString);
            fuelStation.setStationEmail(stationEmailString);
            fuelStation.setStationWebsite(stationWebsiteString);

            //setting other data is not necessary since they are manually set in JSON object

            Log.d(TAG, "All fields are valid. Fuel station object is set.");


            //call addStation record method
            addStationRecord(fuelStation);
        }

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

    //uses okhttp client to call the remote web API to add the fuel station record
    public void addStationRecord(FuelStation fuelStation){

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
            stationJsonObject.put("openStatus", "closed");
            stationJsonObject.put("petrolQueueLength", 0);
            stationJsonObject.put("dieselQueueLength", 0);
            stationJsonObject.put("petrolStatus", "available");
            stationJsonObject.put("dieselStatus", "available");
            stationJsonObject.put("locationLatitude", 0);
            stationJsonObject.put("locationLongitude", 0);

        }catch (JSONException e){
            e.printStackTrace();
        }

        String stationJsonString = stationJsonObject.toString(); //get the string of the json object
        RequestBody requestBody = RequestBody.create(stationJsonString, JSON); //add the json string to request body

        //create an instance of HTTPUrl
        HttpUrl url = HttpUrl.parse(CommonConstants.REMOTE_URL+"api/FuelStations").newBuilder().build();

        //build the request
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        //create and show progress dialog
        progressDialog = getDialogProgressBar().create();
        progressDialog.show();

        //make the call with okhttp client
        client.newCall(request).enqueue(new Callback() {

            //handle the call failure
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss(); //dismiss the progress dialog on failure

                        //show failure alert dialog
                        getAlertDialog("Error", "Failed to make the call").show();
                    }
                });

                Log.d(TAG, "onFailure: Creating station record failed." );
                e.printStackTrace();
            }

            //handle the received responses for the call
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (response.isSuccessful()){
                    //handle successful response

                    //run UI updates on UI threads
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss(); //dismiss the progress dialog on successful response
                            //show success
                            getAlertDialog("Success", "Successfully registered the station").show();
                            //clear all the edit text fields
                            clearAllEditTextFields();
                        }
                    });


                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();
                    Log.d(TAG,"Successfully created station");
                    Log.d(TAG,"onResponse : " + body);
                }
                else {
                    //handle failed response

                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();
                    Log.d(TAG, "failed response: " + body);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss(); //dismiss the progress dialog on failed response

                            //show the response error
                            getAlertDialog("Failure", "Message : " + body);
                        }
                    });


                }

            }
        });

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

    public void clearAllEditTextFields(){
        editTextLicense.setText("");
        editTextStationName.setText("");
        editTextStationAddress.setText("");
        editTextStationEmail.setText("");
        editTextPhoneNumber.setText("");
        editTextStationWebsite.setText("");
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
        //validate that email field is not empty
        if (isStationEmailNotEmpty()){
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
        else {
            return false;
        }

    }

    //validate the website
    public boolean validateStationWebsite(){
        return isStationWebsiteNotEmpty();
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
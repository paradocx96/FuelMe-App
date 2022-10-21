package com.example.fuelme.ui.delete_station_screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuelme.R;
import com.example.fuelme.commonconstants.CommonConstants;
import com.example.fuelme.models.FuelStation;
import com.example.fuelme.ui.owner_dashboard_screen.OwnerStationsActivity;

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

public class DeleteStationActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient(); //okhttp client instance
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private final String TAG = "demo";
    FuelStation fuelStation;

    TextView txtViewStationName, txtViewStationAddress, txtViewLicense, txtViewStationId;
    EditText editTextStationId;
    AlertDialog.Builder progressDialogBuilder;
    AlertDialog progressDialog;

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
        editTextStationId = findViewById(R.id.edtTxt_license_delete_station);

        //get the extras
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            FuelStation fuelStation = (FuelStation) extras.getSerializable("selected_fuel_station"); //get the serializable and cast into fuel station object
            this.fuelStation = fuelStation; //assign the fuel station to view's fuel station object
            updateTextViews(); //update the text views with new data
        }
    }

    //button click method for delete station
    public void deleteStationInDeleteActivityButtonClick(View view){

        if (editTextStationId.getText().toString().isEmpty()){
            //the edit text field is empty
            // show message
            showEmptyLicenseAlert();
        }
        else {
            //license field is not empty
            //check if license field matches with station license
            if (isLicenseMatching()){
                //the license is matching
                //confirm and go through confirmation alert
                showDeleteConfirmationAlert();
            }
            else {
                //license is not matching
                //show mismatch alert
                showLicenseMismatchAlert();
            }
        }

    }

    //show the alert for empty edit text
    public void showEmptyLicenseAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("The license is empty");
        builder.setMessage("Please fill the license field");
        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }
        );
        builder.show();
    }

    //show license mismatch alert
    public void showLicenseMismatchAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("The license does not match");
        builder.setMessage("Please carefully check and fill the license field");
        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }
        );
        builder.show();
    }

    public void showDeleteConfirmationAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Delete the Station?");
        builder.setMessage("This action cannot be undone");
        builder.setPositiveButton(
                "Yes, Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //call the remote
                        deleteStation();
                    }
                }
        );
        builder.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }
        );
        builder.show();
    }

    //if the license matches returns true
    public boolean isLicenseMatching(){
        return editTextStationId.getText().toString().equalsIgnoreCase(fuelStation.getLicense());
    }

    //method to navigate to owner stations screen
    public void navigateToOwnerStationsScreen(){
        Intent intent = new Intent(DeleteStationActivity.this, OwnerStationsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //clear the top
        startActivity(intent);
    }

    //method to make remote call to delete the station
    public void deleteStation(){
        //create an instance of HTTPUrl
        HttpUrl url = HttpUrl.parse(CommonConstants.REMOTE_URL)
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("FuelStations")
                .addPathSegment(fuelStation.getId()) //set this view's station id to path
                .build();

        String sampleString = "sample";
        //dummy request body since okhttp requires one for put requests
        RequestBody requestBody = RequestBody.create(sampleString, JSON);

        //build the request
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        //create and show progress dialog
        progressDialog = getDialogProgressBar().create();
        progressDialog.show();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss(); //dismiss the progress dialog
                        showErrorAlertWithMessage("Error", "Could not make the delete call. Check your network connection"); //show the error alert
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss(); //dismiss the progress dialog
                            navigateToOwnerStationsScreen(); //navigate to owner stations screen
                            Toast.makeText(DeleteStationActivity.this, "Delted the station", Toast.LENGTH_SHORT).show(); //show toast
                        }
                    });
                }
                else {
                    //handle failure response
                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss(); //dismiss the progress dialog
                            showErrorAlertWithMessage("Failure in response",body); //show the error alert
                        }
                    });

                }
            }
        });
    }

    //error alert dialog
    public void showErrorAlertWithMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }
        );
        builder.show();
    }

    //progress bar in an alert dialog
    public AlertDialog.Builder getDialogProgressBar(){
        if (progressDialogBuilder == null){
            progressDialogBuilder = new AlertDialog.Builder(this);
            progressDialogBuilder.setCancelable(false);
            progressDialogBuilder.setTitle("Deleting Station");
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

    //method to update the text views
    public void updateTextViews(){
        txtViewStationName.setText(fuelStation.getStationName());
        txtViewStationAddress.setText(fuelStation.getStationAddress());
        txtViewLicense.setText(fuelStation.getLicense());
        txtViewStationId.setText(fuelStation.getId());
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
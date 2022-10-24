package com.example.fuelme.ui.feedback;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuelme.R;
import com.example.fuelme.commonconstants.CommonConstants;
import com.example.fuelme.models.Feedback;
import com.example.fuelme.ui.mainscreen.MainActivity;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * @author H.G. Malwatta - IT19240848
 * This class is used to view single feedback in detail and edit,delete it
 */
public class ViewFeedback extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient();
    private static String DELETE_FEEDBACK_URL;

    TextView txtSubjectView, txtDescriptionView, txtUsernameView, txtDateTimeView;
    String id, stationId, subject, description, username, dateTime;
    Button btnEditFeedback, btnDeleteFeedback;

    SharedPreferences sharedPreferences;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_feedback);

        //instantiate toolbar and set the back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.view_single_feedback_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //get current logged username
        sharedPreferences = getSharedPreferences("login_data", MODE_PRIVATE);
        currentUsername = sharedPreferences.getString("user_username", "");

        //instantiate the text views and buttons
        txtSubjectView = findViewById(R.id.txtSubjectView);
        txtDescriptionView = findViewById(R.id.txtDescriptionView);
        txtUsernameView = findViewById(R.id.txtUsernameView);
        txtDateTimeView = findViewById(R.id.txtDateTimeView);
        btnEditFeedback = findViewById(R.id.btn_editFeedback);
        btnDeleteFeedback = findViewById(R.id.btn_deleteFeedback);

        //get the feedback data from the intent
        Intent intent = getIntent();
        id = intent.getStringExtra("feedback_id"); //1 - Get feedback id from intent
        stationId = intent.getStringExtra("feedback_stationId");
        subject = intent.getStringExtra("feedback_subject"); //2 - Get feedback subject from intent
        description = intent.getStringExtra("feedback_description"); //3 - Get feedback description from intent
        username = intent.getStringExtra("feedback_username"); //4 - Get feedback username from intent
        dateTime = intent.getStringExtra("feedback_dateTime"); //5 - Get feedback dateTime from intent

        //set station Id to the URI
        DELETE_FEEDBACK_URL = CommonConstants.REMOTE_URL_DELETE_FEEDBACK_STATIONS + id;

        //check the feedback id is null or not
        if (id != null) {

            //set the feedback data to the text views
            txtSubjectView.setText(subject);
            txtDescriptionView.setText(description);
            txtUsernameView.setText(username.toUpperCase());
            txtDateTimeView.setText(dateTime);
        } else {

            //if feedback id is null, then show error message
            txtSubjectView.setText("Error");
            txtDescriptionView.setText("Error");
            txtUsernameView.setText("Error");
            txtDateTimeView.setText("Error");
        }

        //if the current logged user is not the owner of the feedback, then hide the edit and delete buttons
        if (!currentUsername.equals(username)) {
            btnEditFeedback.setVisibility(View.GONE);
            btnDeleteFeedback.setVisibility(View.GONE);
        }

    }

    /**
     * This method is used to edit feedback by clicking the edit button
     *
     * @param view
     */
    public void editFeedbackButtonClick(View view) {

        //open the edit feedback activity
        Intent intent = new Intent(ViewFeedback.this, EditFeedback.class);

        //pass the feedback data to the edit feedback activity
        intent.putExtra("feedback_id", id);
        intent.putExtra("feedback_stationId", stationId);
        intent.putExtra("feedback_subject", subject);
        intent.putExtra("feedback_description", description);
        intent.putExtra("feedback_username", username);
        intent.putExtra("feedback_dateTime", dateTime);

        //start the activity and clear the previous activity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    /**
     * This method is used to delete feedback by clicking the delete button
     *
     * @param view
     */
    public void deleteFeedbackButtonClick(View view) {

        //create an alert dialog to confirm the delete action
        AlertDialog alertDialog = new AlertDialog.Builder(ViewFeedback.this)
                .setIcon(R.drawable.ic_warning)
                .setTitle("Are you sure ?")
                .setMessage("This action cannot be undone! If you click 'Yes', this feedback will be deleted permanently.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // delete feedback
                        deleteFeedbackById();
                        Toast.makeText(getApplicationContext(), "Successfully deleted!", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what should happen when negative button is clicked
                        Toast.makeText(getApplicationContext(), "Feedback delete cancelled!", Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }

    /**
     * This method is used to delete feedback by id
     */
    private void deleteFeedbackById() {

        //set the delete feedback url
        HttpUrl url = HttpUrl.parse(DELETE_FEEDBACK_URL).newBuilder().build();

        //create a request
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        //create a client call
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, java.io.IOException e) {
                e.printStackTrace();
                Log.d("API_CALL_DELETE", "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws java.io.IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    Log.d("API_CALL_DELETE", "onFailure: " + response);

                    ViewFeedback.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ViewFeedback.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ViewFeedback.this, FeedbackList.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
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
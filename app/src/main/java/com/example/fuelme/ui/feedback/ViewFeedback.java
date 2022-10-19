package com.example.fuelme.ui.feedback;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuelme.R;
import com.example.fuelme.commonconstants.CommonConstants;
import com.example.fuelme.models.Feedback;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ViewFeedback extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient();
    private static String DELETE_FEEDBACK_URL;

    TextView txtSubjectView, txtDescriptionView, txtUsernameView, txtDateTimeView;
    String id, stationId, subject, description, username, dateTime;

    Button btnEditFeedback, btnDeleteFeedback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_feedback);
        txtSubjectView = findViewById(R.id.txtSubjectView);
        txtDescriptionView = findViewById(R.id.txtDescriptionView);
        txtUsernameView = findViewById(R.id.txtUsernameView);
        txtDateTimeView = findViewById(R.id.txtDateTimeView);
        btnEditFeedback = findViewById(R.id.btn_editFeedback);
        btnDeleteFeedback = findViewById(R.id.btn_deleteFeedback);

        Intent intent = getIntent();
        id = intent.getStringExtra("feedback_id"); //1 - Get feedback id from intent
        stationId = intent.getStringExtra("feedback_stationId");
        subject = intent.getStringExtra("feedback_subject"); //2 - Get feedback subject from intent
        description = intent.getStringExtra("feedback_description"); //3 - Get feedback description from intent
        username = intent.getStringExtra("feedback_username"); //4 - Get feedback username from intent
        dateTime = intent.getStringExtra("feedback_dateTime"); //5 - Get feedback dateTime from intent


        if (id != null) {
            //2 - Get feedback from database
            txtSubjectView.setText(subject);
            txtDescriptionView.setText(description);
            txtUsernameView.setText(username);
            txtDateTimeView.setText(dateTime);
        } else {
            //3 - If feedback id is null, then show error message
            txtSubjectView.setText("Error");
            txtDescriptionView.setText("Error");
            txtUsernameView.setText("Error");
            txtDateTimeView.setText("Error");
        }

        btnEditFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewFeedback.this, EditFeedback.class);
                intent.putExtra("feedback_id", id);
                intent.putExtra("feedback_stationId", stationId);
                intent.putExtra("feedback_subject", subject);
                intent.putExtra("feedback_description", description);
                intent.putExtra("feedback_username", username);
                intent.putExtra("feedback_dateTime", dateTime);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btnDeleteFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //4 - Delete feedback from database

                AlertDialog alertDialog = new AlertDialog.Builder(ViewFeedback.this)
                        .setIcon(R.drawable.ic_warning)
                        .setTitle("Are you sure ?")
                        .setMessage("This action cannot be undone! If you click 'Yes', this feedback will be deleted permanently.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Delete feedback
                                deleteFeedbackById(id);
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
        });

    }

    private void deleteFeedbackById(String id) {
        //6 - Set DELETE_FEEDBACK_URL
        DELETE_FEEDBACK_URL = CommonConstants.REMOTE_URL + "api/Feedback/" + id;

        HttpUrl url = HttpUrl.parse(DELETE_FEEDBACK_URL).newBuilder().build();

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

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


}
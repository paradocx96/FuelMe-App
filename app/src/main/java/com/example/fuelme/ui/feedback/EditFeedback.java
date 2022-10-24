package com.example.fuelme.ui.feedback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuelme.R;
import com.example.fuelme.commonconstants.CommonConstants;
import com.example.fuelme.models.Feedback;
import com.example.fuelme.ui.mainscreen.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author H.G. Malwatta - IT19240848
 * This class is used to edit feedback(s) to respective stations
 */

public class EditFeedback extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static String UPDATE_FEEDBACK_URL;

    EditText txtSubjectEdit, txtDescriptionEdit;
    Button btnUpdateFeedback;

    private String id, stationId, username, subject, description, dateTime;
    private String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_feedback);

        //instantiate toolbar and set the back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_feedback_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //instantiate the edit text and button
        txtSubjectEdit = findViewById(R.id.editTxt_subject_edit);
        txtDescriptionEdit = findViewById(R.id.editTxt_description_edit);
        btnUpdateFeedback = findViewById(R.id.btn_submit_edit);

        //get the feedback details from the intent
        Intent intent = getIntent();
        id = intent.getStringExtra("feedback_id");
        stationId = intent.getStringExtra("feedback_stationId");
        subject = intent.getStringExtra("feedback_subject");
        description = intent.getStringExtra("feedback_description");
        username = intent.getStringExtra("feedback_username");
        dateTime = intent.getStringExtra("feedback_dateTime");

        //set the station id to the URI
        UPDATE_FEEDBACK_URL = CommonConstants.REMOTE_URL_UPDATE_FEEDBACK_STATIONS + id;

        //set the feedback details to the edit text
        txtSubjectEdit.setText(subject);
        txtDescriptionEdit.setText(description);


        btnUpdateFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Feedback feedback = new Feedback();
                feedback.setId(id);
                feedback.setStationId(stationId);
                feedback.setUsername(username);
                feedback.setCreateAt(currentDateTimeString);

                feedback.setSubject(txtSubjectEdit.getText().toString());
                feedback.setDescription(txtDescriptionEdit.getText().toString());

                if (validateFeedbackForm(feedback)) {
                    updateFeedback(feedback);
                    Intent intent = new Intent(EditFeedback.this, FeedbackList.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

    }

    /**
     * This method is used to validate the feedback form
     *
     * @param view
     * @throws Exception
     */
    public void editFeedbackButtonClick(View view) {

        try {
            Feedback feedback = new Feedback();
            feedback.setId(id);
            feedback.setStationId(stationId);
            feedback.setUsername(username);
            feedback.setCreateAt(currentDateTimeString);

            feedback.setSubject(txtSubjectEdit.getText().toString());
            feedback.setDescription(txtDescriptionEdit.getText().toString());

            if (validateFeedbackForm(feedback)) {
                updateFeedback(feedback);
                Intent intent = new Intent(EditFeedback.this, FeedbackList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to validate the feedback form
     *
     * @param feedback
     * @return true if the form is valid
     */
    private boolean validateFeedbackForm(Feedback feedback) {
        if (feedback.getSubject().isEmpty()) {
            txtSubjectEdit.setError("Subject is required");
            txtSubjectEdit.requestFocus();
            return false;
        }
        if (feedback.getDescription().isEmpty()) {
            txtDescriptionEdit.setError("Description is required");
            txtDescriptionEdit.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * This method is used to update the feedback
     *
     * @param feedback
     */
    private void updateFeedback(Feedback feedback) {
        //create a JSON object
        JSONObject jsonObject = new JSONObject();

        try {
            //add the feedback details to the JSON object
            jsonObject.put("id", feedback.getId());
            jsonObject.put("stationId", feedback.getStationId());
            jsonObject.put("subject", feedback.getSubject());
            jsonObject.put("username", feedback.getUsername());
            jsonObject.put("description", feedback.getDescription());
            jsonObject.put("createAt", feedback.getCreateAt());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String jsonString = jsonObject.toString();

        //create a request body
        RequestBody body = RequestBody.create(jsonString, JSON);

        //create a request and set the URL with the update feedback details
        HttpUrl url = HttpUrl.parse(UPDATE_FEEDBACK_URL).newBuilder()
                .addQueryParameter("id", feedback.getId())
                .addQueryParameter("stationId", feedback.getStationId())
                .addQueryParameter("subject", feedback.getSubject())
                .addQueryParameter("username", feedback.getUsername())
                .addQueryParameter("description", feedback.getDescription())
                .addQueryParameter("createAt", feedback.getCreateAt())
                .build();

        //create a request
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();

        //create a call
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
                Log.d("API_CALL_EDIT", "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    String myResponse = response.body().string();
                    System.out.println(myResponse);
                    Log.d("API_CALL_EDIT", "onResponse: " + myResponse);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(EditFeedback.this, myResponse, Toast.LENGTH_SHORT).show();
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
/**
 * EAD - FuelMe Mobile App
 *
 * @author H.G. Malwatta - IT19240848
 * @version 1.0.0
 */

package com.example.fuelme.ui.feedback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fuelme.R;
import com.example.fuelme.commonconstants.CommonConstants;
import com.example.fuelme.models.Feedback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

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
 * @author H.G. Malwatta - IT19240848
 * This class is used to add feedback(s) to respective stations
 */

public class AddFeedback extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    SharedPreferences sharedPreferences;
    private String username;
    private String stationId;

    String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());

    EditText editTxtSubject, editTxtDescription;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feedback);

        //get the username from the intent
        Intent intent = getIntent();
        stationId = intent.getStringExtra("stationId");

        //get current logged username
        sharedPreferences = getSharedPreferences("login_data", MODE_PRIVATE);
        username = sharedPreferences.getString("user_username", "");

        //instantiate toolbar and set the back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.add_feedback_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //instantiate the edit text and button
        editTxtSubject = findViewById(R.id.editTxt_subject);
        editTxtDescription = findViewById(R.id.editTxt_description);
        btnSubmit = findViewById(R.id.btn_submit);
    }

    /**
     * This method is used to add feedback(s) to respective stations when clicking the submit button
     * @param view
     * @throws Exception
     */
    public void addFeedbackButtonClick(View view){
        try {

            Feedback feedback = new Feedback();
            feedback.setSubject(editTxtSubject.getText().toString());
            feedback.setDescription(editTxtDescription.getText().toString());
            feedback.setStationId(stationId);
            feedback.setUsername(username);
            feedback.setCreateAt(currentDateTimeString);

            if(validateFeedback(feedback)){
                addFeedback(feedback);
                Intent intent = new Intent(AddFeedback.this, FeedbackList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to validate the feedback
     * @param feedback
     * @return true if the form is valid
     */
    private boolean validateFeedback(Feedback feedback) {
        if (feedback.getSubject().isEmpty()) {
            editTxtSubject.setError("Subject is required");
            editTxtSubject.requestFocus();
            return false;
        }
        if (feedback.getDescription().isEmpty()) {
            editTxtDescription.setError("Description is required");
            editTxtDescription.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * This method is used to add feedback(s) to respective stations
     * @param feedback
     * @throws JSONException
     */
    private void addFeedback(Feedback feedback){

        //create a json object
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("stationId", feedback.getStationId());
            jsonObject.put("subject", feedback.getSubject());
            jsonObject.put("username", feedback.getUsername());
            jsonObject.put("description", feedback.getDescription());
            jsonObject.put("createAt", feedback.getCreateAt());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String jsonString = jsonObject.toString();

        //create the request body
        RequestBody requestBody = RequestBody.create(jsonString, JSON);

        //create the request for add feedback
        HttpUrl url = HttpUrl.parse(CommonConstants.REMOTE_URL_ADD_FEEDBACK_STATIONS).newBuilder().build();

        //create the request
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        //create the call
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("API_CALL","OnFailure : " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        String responseString = responseBody.string();
                        Log.d("API_CALL","Add_Feedback : " + responseString);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AddFeedback.this, "Successfully Added!", Toast.LENGTH_LONG).show();
                                editTxtSubject.setText("");
                                editTxtDescription.setText("");
                            }
                        });
                    }
                }else{
                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();
                    Log.d("API_CALL", "Failed response: " + body);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddFeedback.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
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
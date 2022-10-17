package com.example.fuelme.ui.feedback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class AddFeedback extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static String ADD_FEEDBACK_URL = CommonConstants.REMOTE_URL + "api/Feedback";

    String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());

    EditText editTxtSubject, editTxtDescription;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feedback);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTxtSubject = findViewById(R.id.editTxt_subject);
        editTxtDescription = findViewById(R.id.editTxt_description);

        btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(v -> {
            try {

                Feedback feedback = new Feedback();
                feedback.setSubject(editTxtSubject.getText().toString());
                feedback.setDescription(editTxtDescription.getText().toString());
                feedback.setStationId("3");
                feedback.setUsername("Billa");
                feedback.setCreateAt(currentDateTimeString);

                addFeedback(feedback);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }



    private void addFeedback(Feedback feedback){

        FormBody formBody = new FormBody.Builder()
                .add("stationId", feedback.getStationId())
                .add("subject", feedback.getSubject())
                .add("username", feedback.getUsername())
                .add("description", feedback.getDescription())
                .add("createAt", feedback.getCreateAt())
                .build();

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
        //Log.d("API_CALL","Add_Feedback : " + jsonString);

        RequestBody requestBody = RequestBody.create(jsonString, JSON);

        HttpUrl url = HttpUrl.parse(ADD_FEEDBACK_URL).newBuilder().build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
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
                               // txtId.setText(responseString);
//                                responseMessage.setText(responseString);
//                                responseMessage.show();
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
                            //textView1.setText("Failed posting");
//                            responseMessage.setText(body);
//                            responseMessage.show();
                        }
                    });
                }
            }
        });

    }

//    void getEmployees() {
//
//
//
//        HttpUrl url = HttpUrl.parse(CommonConstants.BASE_URL).newBuilder().build();
//
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        //client.hostnameVerifier(new NullHostNameVerifier())
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    ResponseBody responseBody = response.body();
//                    String body = responseBody.string();
//                    Log.d("API_CALL", "onResponse: " + body);
//
//                    try {
//                        JSONObject jsonObject = new JSONObject(body);
//                        Feedback feedback = new Feedback();
//                        feedback.setId(jsonObject.getString("id"));
//                        feedback.setStationId(jsonObject.getString("stationId"));
//                        feedback.setUsername(jsonObject.getString("username"));
//                        feedback.setDescription(jsonObject.getString("description"));
//                        feedback.setCreateAt(jsonObject.getString("createAt"));
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                //textView1.setText("Employee Last Name : " +employee.getLastName() );
//                                txtId.setText(feedback.getId());
//                                txtStationId.setText(feedback.getStationId());
//                                txtUsername.setText(feedback.getUsername());
//                                txtDescription.setText(feedback.getDescription());
//                                txtCreateAt.setText(feedback.getCreateAt());
//                            }
//                        });
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//
//                } else {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            //textView1.setText("Error getting employee.");
//                            txtId.setText("Error getting employee.");
//                            txtStationId.setText("Error getting employee.");
//                            txtUsername.setText("Error getting employee.");
//                            txtDescription.setText("Error getting employee.");
//                            txtCreateAt.setText("Error getting employee.");
//                        }
//                    });
//                }
//            }
//        });
//    }


}
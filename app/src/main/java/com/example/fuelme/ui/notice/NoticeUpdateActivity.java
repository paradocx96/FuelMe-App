/**
 * FuelMe APP
 * Enterprise Application Development - SE4040
 *
 * @author IT19180526 - S.A.N.L.D. Chandrasiri
 * @version 1.0
 */

package com.example.fuelme.ui.notice;

import static com.example.fuelme.commonconstants.CommonConstants.REMOTE_URL_NOTICE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuelme.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Notice Update UI for FuelMe Application
 *
 * @author IT19180526 - S.A.N.L.D. Chandrasiri
 * @version 1.0
 */
public class NoticeUpdateActivity extends AppCompatActivity {

    // Defined object and variables
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String logged_username;
    String current_date_time;
    TextView textView_title, textView_description;
    String id, stationId, title, description, author, created;
    Button btnUpdate;
    Toolbar toolbar;
    private final OkHttpClient client = new OkHttpClient();
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * This method used for bind the layout UI element with defined local object.
     * Also defined onclick listener for button update.
     * Then call updateNotice() method to update notice data from API.
     *
     * @param savedInstanceState - Bundle
     * @throws JSONException - Handle the Exception produced by JSON manipulation operations.
     * @see #onCreate(Bundle savedInstanceState)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Binding relevant Layout UI with this class
        setContentView(R.layout.activity_notice_update);

        // Define UI objects
        textView_title = findViewById(R.id.notice_update_title);
        textView_description = findViewById(R.id.notice_update_description);
        btnUpdate = findViewById(R.id.notice_update_button_update);
        toolbar = (Toolbar) findViewById(R.id.toolbar_notice_update);

        // Get logged user's data
        preferences = getSharedPreferences("login_data", MODE_PRIVATE);
        editor = preferences.edit();
        logged_username = preferences.getString("user_username", "");

        // Setup back button for toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Defined intent data
        Intent intent = getIntent();
        id = intent.getStringExtra("notice_id");
        stationId = intent.getStringExtra("notice_station_id");
        title = intent.getStringExtra("notice_title");
        description = intent.getStringExtra("notice_description");
        author = intent.getStringExtra("notice_author");
        created = intent.getStringExtra("notice_created");

        // Checking intent data and set text
        if (id != null) {
            textView_title.setText(title);
            textView_description.setText(description);
        } else {
            textView_title.setText("Error");
            textView_description.setText("Error");
        }

        // Setup on click listener for update button
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get current date and time and assign to new string
                current_date_time = DateFormat.getDateTimeInstance().format(new Date());

                // Create JSON object using API response
                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("stationId", stationId);
                    jsonObject.put("title", textView_title.getText().toString());
                    jsonObject.put("description", textView_description.getText().toString());
                    jsonObject.put("author", logged_username);
                    jsonObject.put("createAt", current_date_time);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Convert JSON object to String and Make Response body
                String jsonString = jsonObject.toString();
                RequestBody body = RequestBody.create(jsonString, JSON);

                // Calling updateNotice method for update notice data.
                updateNotice(id, body);
            }
        });
    }

    /**
     * This method used for getting updated notice data and notice id as parameter.
     * Then execute HTTP request to update the relevant notice data.
     *
     * @param id       - String
     * @param jsonBody - RequestBody
     * @throws IOException - Handle the Exception produced by failed or interrupted I/O operations.
     * @see #updateNotice(String, RequestBody)
     */
    void updateNotice(String id, RequestBody jsonBody) {
        // Create HTTP request for update notice data from API
        Request request = new Request.Builder()
                .url(REMOTE_URL_NOTICE + id)
                .put(jsonBody)
                .build();

        // Execute HTTP request and check whether request response
        client.newCall(request).enqueue(new Callback() {
            // HTTP Request when failed this will execute.
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NoticeUpdateActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            // HTTP Request when response got from API this will execute.
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // Check response is success or not
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Make toast message
                            Toast.makeText(NoticeUpdateActivity.this, "Notice Updated!", Toast.LENGTH_SHORT).show();

                            // Change UI into Station owner notice list view
                            Intent intent = new Intent(NoticeUpdateActivity.this, NoticeListStationActivity.class);
                            intent.putExtra("station_id", stationId);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NoticeUpdateActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    /**
     * This method used for handle the toolbar
     *
     * @see #onSupportNavigateUp()
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * This method used for handle the toolbar
     *
     * @see #onBackPressed()
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
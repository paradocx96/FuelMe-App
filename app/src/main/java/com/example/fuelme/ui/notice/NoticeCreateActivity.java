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
import okhttp3.ResponseBody;

/**
 * Notice create UI for FuelMe Application
 *
 * @author IT19180526 - S.A.N.L.D. Chandrasiri
 * @version 1.0
 *
 * Reference:
 * https://square.github.io/okhttp/
 * https://www.digitalocean.com/community/tutorials/retrofit-android-example-tutorial
 * https://developer.android.com/docs
 * https://www.youtube.com/watch?v=lBzzL7ZLT7c
 */
public class NoticeCreateActivity extends AppCompatActivity {

    // Defined object and variables
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String logged_username, station_id, current_date_time;
    TextView textView_title, textView_description;
    Button btnNoticeCreate;
    Toolbar toolbar;
    private final OkHttpClient client = new OkHttpClient();
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * This method used for bind the layout UI element with defined local object.
     * Also defined onclick listener for button create
     *
     * @param savedInstanceState - Bundle
     * @throws JSONException - Handle the Exception produced by JSON manipulation operations.
     * @see #onCreate(Bundle savedInstanceState)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Binding relevant Layout UI with this class
        setContentView(R.layout.activity_notice_create);

        // Define UI objects
        textView_title = findViewById(R.id.notice_create_title);
        textView_description = findViewById(R.id.notice_create_description);
        btnNoticeCreate = findViewById(R.id.notice_create_button_add);
        toolbar = (Toolbar) findViewById(R.id.toolbar_notice_create);

        // Get logged user's data
        preferences = getSharedPreferences("login_data", MODE_PRIVATE);
        editor = preferences.edit();
        logged_username = preferences.getString("user_username", "");

        // Defined intent data
        Intent intent = getIntent();
        station_id = intent.getStringExtra("station_id");

        // Setup back button for toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Setup on click listener for create notice button
        btnNoticeCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get current date and time and assign to new string
                current_date_time = DateFormat.getDateTimeInstance().format(new Date());

                // Create JSON object to send HTTP request
                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("stationId", station_id);
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

                // Calling createNotice method for create new notice
                createNotice(body);
            }
        });
    }

    /**
     * This method used for getting new notice data and create new notice
     * Then execute HTTP request to create new notice
     *
     * @param body - RequestBody
     * @throws IOException - Handle the Exception produced by failed or interrupted I/O operations.
     * @see #createNotice(RequestBody)
     */
    void createNotice(RequestBody body) {
        // Create HTTP request for create new notice
        Request request = new Request.Builder()
                .url(REMOTE_URL_NOTICE)
                .post(body)
                .build();

        // Execute HTTP request and check whether request response
        client.newCall(request).enqueue(new Callback() {
            // HTTP Request when failed this will execute.
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NoticeCreateActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            // HTTP Request when response got from API this will execute.
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // Check response is success or not
                if (response.isSuccessful()) {
                    // Create JSON object using API response
                    ResponseBody responseBody = response.body();

                    // Check response body is empty or not
                    if (responseBody != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Clear text fields
                                clearTextView();

                                // Make toast response for notice creation success
                                Toast.makeText(NoticeCreateActivity.this, "Successfully created!", Toast.LENGTH_LONG).show();

                                // Navigate into new Notice List view
                                Intent intent = new Intent(NoticeCreateActivity.this, NoticeListStationActivity.class);
                                intent.putExtra("station_id", station_id);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        });
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NoticeCreateActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    /**
     * This method used for clear text fields
     *
     * @see #clearTextView()
     */
    void clearTextView() {
        textView_title.setText(null);
        textView_description.setText(null);
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
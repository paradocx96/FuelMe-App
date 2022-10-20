package com.example.fuelme.ui.notice;

import static com.example.fuelme.commonconstants.CommonConstants.REMOTE_URL_NOTICE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

public class NoticeUpdateActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current_date_time = DateFormat.getDateTimeInstance().format(new Date());
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

                String jsonString = jsonObject.toString();
                RequestBody body = RequestBody.create(jsonString, JSON);

                Log.d("API_CALL", "NOTICE_UPDATE JSON_STRING: " + jsonString);
                Log.d("API_CALL", "NOTICE_UPDATE REQUEST_BODY: " + body);

                updateNotice(id, body);
            }
        });
    }

    void updateNotice(String id, RequestBody jsonBody) {
        Request request = new Request.Builder()
                .url(REMOTE_URL_NOTICE + id)
                .put(jsonBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("API_CALL", "NOTICE_UPDATE onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String res = response.body().string();
                    Log.d("API_CALL_EDIT", "NOTICE_UPDATE onResponse: " + res);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NoticeUpdateActivity.this, "Notice Updated!", Toast.LENGTH_SHORT).show();
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
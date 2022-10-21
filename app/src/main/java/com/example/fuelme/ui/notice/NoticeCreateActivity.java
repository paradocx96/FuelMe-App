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

public class NoticeCreateActivity extends AppCompatActivity {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String logged_username, station_id, current_date_time;

    TextView textView_title, textView_description;
    Button btnNoticeCreate;
    Toolbar toolbar;

    private final OkHttpClient client = new OkHttpClient();
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        btnNoticeCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current_date_time = DateFormat.getDateTimeInstance().format(new Date());
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

                String jsonString = jsonObject.toString();
                RequestBody body = RequestBody.create(jsonString, JSON);
                createNotice(body);
            }
        });
    }

    void createNotice(RequestBody body) {
        Request request = new Request.Builder()
                .url(REMOTE_URL_NOTICE)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NoticeCreateActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                clearTextView();
                                Toast.makeText(NoticeCreateActivity.this, "Successfully created!", Toast.LENGTH_LONG).show();
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

    void clearTextView() {
        textView_title.setText(null);
        textView_description.setText(null);
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
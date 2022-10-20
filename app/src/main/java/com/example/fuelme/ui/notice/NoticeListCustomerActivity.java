package com.example.fuelme.ui.notice;

import static com.example.fuelme.commonconstants.CommonConstants.REMOTE_URL_NOTICE;
import static com.example.fuelme.commonconstants.CommonConstants.REMOTE_URL_NOTICE_STATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.fuelme.R;
import com.example.fuelme.models.notice.Notice;
import com.example.fuelme.ui.notice.adapter.NoticeListCustomerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class NoticeListCustomerActivity extends AppCompatActivity {

    RecyclerView recyclerViewNotice;
    NoticeListCustomerAdapter adapter;
    String station_id;

    private final OkHttpClient client = new OkHttpClient();
    ArrayList<Notice> noticeArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Defined Layout objects of this activity
        setContentView(R.layout.activity_notice_list_customer);
        recyclerViewNotice = findViewById(R.id.notice_list_customer);

        // Defined toolbar and set the back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_notice_list_customer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Defined intent data
        Intent intent = getIntent();
        station_id = intent.getStringExtra("station_id");
        Log.d("API_CALL", "STATION_ID => " + station_id);

        noticeArrayList = new ArrayList<>();

        adapter = new NoticeListCustomerAdapter(getApplicationContext(), noticeArrayList);
        recyclerViewNotice.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        getNotices();
    }

    private void getNotices() {
        Request request = new Request.Builder()
                .url(REMOTE_URL_NOTICE_STATION + station_id)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(NoticeListCustomerActivity.this, "No Notice", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    String body = responseBody.string();

                    JSONArray jsonArr = new JSONArray(body);
                    Notice notice;

                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject object = jsonArr.getJSONObject(i);

                        notice = new Notice(
                                object.getString("id"),
                                object.getString("stationId"),
                                object.getString("title"),
                                object.getString("description"),
                                object.getString("author"),
                                object.getString("createAt")
                        );

                        noticeArrayList.add(notice);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerViewNotice.setAdapter(adapter);
                            }
                        });
                    }
                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
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
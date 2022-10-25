/**
 * FuelMe APP
 * Enterprise Application Development - SE4040
 *
 * @author IT19180526 - S.A.N.L.D. Chandrasiri
 * @version 1.0
 */

package com.example.fuelme.ui.notice;

import static com.example.fuelme.commonconstants.CommonConstants.REMOTE_URL_NOTICE_STATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
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

/**
 * Notice List View for Customer UI for FuelMe Application
 *
 * @author IT19180526 - S.A.N.L.D. Chandrasiri
 * @version 1.0
 */
public class NoticeListCustomerActivity extends AppCompatActivity {

    // Defined object and variables
    RecyclerView recyclerViewNotice;
    NoticeListCustomerAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    String station_id;
    private final OkHttpClient client = new OkHttpClient();
    ArrayList<Notice> noticeArrayList;

    /**
     * This method used for bind the layout UI element with defined local object.
     * Also defined onclick refresh listener for swipe refresh.
     * Then call getNotices() method to get notice data from API.
     *
     * @param savedInstanceState - Bundle
     * @see #onCreate(Bundle savedInstanceState)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Defined Layout objects of this activity
        setContentView(R.layout.activity_notice_list_customer);
        recyclerViewNotice = findViewById(R.id.notice_list_customer);
        swipeRefreshLayout = findViewById(R.id.swipe_notice_list_customer);

        // Defined toolbar and set the back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_notice_list_customer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Defined intent data
        Intent intent = getIntent();
        station_id = intent.getStringExtra("station_id");

        // Defined and initialize object need for view List view
        noticeArrayList = new ArrayList<>();
        adapter = new NoticeListCustomerAdapter(getApplicationContext(), noticeArrayList);
        recyclerViewNotice.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        // Calling notice retrieve method for get notice from database
        getNotices();

        // Initialize swipe refresh list view
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                noticeArrayList.clear();
                getNotices();
            }
        });
    }

    /**
     * This method used for getting notice data from database and view in list view
     * Execute HTTP request to get notice from database
     *
     * @throws IOException          - Handle the Exception produced by failed or interrupted I/O operations.
     * @throws JSONException        - Handle the Exception produced by JSON manipulation operations.
     * @throws NullPointerException - Handle the Exception produced by null values operating.
     * @see #getNotices()
     */
    private void getNotices() {
        // Create HTTP request for get notice by station id
        Request request = new Request.Builder()
                .url(REMOTE_URL_NOTICE_STATION + station_id)
                .build();

        // Execute HTTP request and check whether request response
        client.newCall(request).enqueue(new Callback() {
            // HTTP Request when failed this will execute.
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NoticeListCustomerActivity.this, "No Notice", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            // HTTP Request when response got from API this will execute.
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // Define JSON object using API response
                try (ResponseBody responseBody = response.body()) {
                    // Check response is success or not
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    // Define string by response body
                    String body = responseBody.string();

                    // Define JSON array using string response body
                    JSONArray jsonArr = new JSONArray(body);

                    // Define notice object
                    Notice notice;

                    for (int i = 0; i < jsonArr.length(); i++) {
                        // Create JSON object using JSON array object
                        JSONObject object = jsonArr.getJSONObject(i);

                        // Create notice object with database data
                        notice = new Notice(
                                object.getString("id"),
                                object.getString("stationId"),
                                object.getString("title"),
                                object.getString("description"),
                                object.getString("author"),
                                object.getString("createAt")
                        );

                        // Add notice data to notice arraylist
                        noticeArrayList.add(notice);

                        // Call List view adapter to view the list view
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

    /**
     * This method used for handle the toolbar
     *
     * @see #onBackPressed()
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
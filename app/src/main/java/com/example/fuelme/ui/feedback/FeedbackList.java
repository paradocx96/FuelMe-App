/**
 * EAD - FuelMe Mobile App
 *
 * @author H.G. Malwatta - IT19240848
 * @version 1.0.0
 * @references
 *  - https://square.github.io/okhttp/
 *  - https://www.digitalocean.com/community/tutorials/retrofit-android-example-tutorial
 *  - https://developer.android.com/docs
 *  - https://www.youtube.com/watch?v=GIDTH6Y0qds
 *  - https://www.youtube.com/watch?v=1D4XCJegizs&t=59s
 */

package com.example.fuelme.ui.feedback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.fuelme.R;
import com.example.fuelme.commonconstants.CommonConstants;
import com.example.fuelme.models.Feedback;
import com.example.fuelme.ui.feedback.adapters.FeedbackListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author H.G. Malwatta - IT19240848
 * This class is used to display the feedback(s) of respective stations
 */
public class FeedbackList extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient();
    private String GET_FEEDBACK_URL;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private HttpUrl url;

    private FloatingActionButton addFeedbackFab;
    RecyclerView recyclerViewFeedback;
    SwipeRefreshLayout swipeRefreshLayout;
    private FeedbackListAdapter adapter;
    private ArrayList<Feedback> feedbackArrayList;
    private String stationId;

    SharedPreferences fuelStationIdSharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_list);

        //get the station id from the shared preferences
        fuelStationIdSharedPref = getSharedPreferences("feedback_data", MODE_PRIVATE);
        stationId = fuelStationIdSharedPref.getString("feedback_station_id", "");

        //set the url with station id
        GET_FEEDBACK_URL = CommonConstants.REMOTE_URL_GET_FEEDBACK_STATIONS + stationId;

        //instantiate toolbar and set the back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.view_feedbackList_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //instantiate the recycler view
        recyclerViewFeedback = findViewById(R.id.feedbackList_recycle_view);
        recyclerViewFeedback.setLayoutManager(new LinearLayoutManager(this));
        feedbackArrayList = new ArrayList<>();

        //instantiate the swipe refresh layout
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        addFeedbackFab = findViewById(R.id.add_feedback_fab);

        //passing the arraylist to the adapter
        adapter = new FeedbackListAdapter(FeedbackList.this, feedbackArrayList);
        //setting the adapter to the recyclerview
        recyclerViewFeedback.setAdapter(adapter);
        //adding a divider line between items
        recyclerViewFeedback.addItemDecoration(new DividerItemDecoration(FeedbackList.this, LinearLayoutManager.VERTICAL));

        //get the feedbacks from the database
        createListFeedback();

        //set the swipe refresh layout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                feedbackArrayList.clear();
                url = HttpUrl.parse(GET_FEEDBACK_URL).newBuilder().build();
                createListFeedback();
            }
        });
    }

    /**
     * This method is used to navigate add feedback(s) activity
     *
     * @param view
     */
    public void addFeedbackFabButtonClick(View view) {
        Intent intent = new Intent(FeedbackList.this, AddFeedback.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("stationId", stationId);
        startActivity(intent);
    }

    /**
     * This method is used to get the feedback(s) from the database
     */
    private void createListFeedback() {

        //set the url
        url = HttpUrl.parse(GET_FEEDBACK_URL).newBuilder().build();
        Request request = new Request.Builder()
                .url(url)
                .build();

        //create the call
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                //check the response is successful or not
                if (response.isSuccessful()) {

                    //get the response body
                    ResponseBody responseBody = response.body();

                    //get the response body string
                    String body = responseBody.string();

                    Log.d("API_CALL", "on-Response: " + body);

                    try {

                        //create the json array
                        JSONArray feedbackArray = new JSONArray(body);
                        Feedback feedback;

                        //iterate the json array
                        for (int i = 0; i < feedbackArray.length(); i++) {

                            //get the json object one by one
                            JSONObject object = feedbackArray.getJSONObject(i);

                            //set the feedback object
                            feedback = new Feedback(
                                    object.getString("id"),
                                    object.getString("stationId"),
                                    object.getString("username"),
                                    object.getString("subject"),
                                    object.getString("description"),
                                    object.getString("createAt"));

                            //add the feedback object to the array list
                            feedbackArrayList.add(feedback);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //notify the adapter
                            adapter.notifyDataSetChanged();
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

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
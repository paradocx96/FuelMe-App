package com.example.fuelme.ui.feedback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
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


public class FeedbackList extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient();
    private String GET_FEEDBACK_URL = CommonConstants.REMOTE_URL + "api/Feedback/station/1";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private HttpUrl url;

    private FloatingActionButton addFeedbackFab;

    RecyclerView recyclerViewFeedback;
    SwipeRefreshLayout swipeRefreshLayout;
    private FeedbackListAdapter adapter;
    private ArrayList<Feedback> feedbackArrayList;

    TextView txtToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_list);

        //instantiate toolbar and set the back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.view_feedbackList_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txtToolbarTitle = (TextView) toolbar.findViewById(R.id.txtToolbar_title_feedbackList);
        txtToolbarTitle.setText("All Feedbacks");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        recyclerViewFeedback = findViewById(R.id.feedbackList_recycle_view);
        recyclerViewFeedback.setLayoutManager(new LinearLayoutManager(this));
        feedbackArrayList = new ArrayList<>();

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        addFeedbackFab = findViewById(R.id.add_feedback_fab);

        adapter = new FeedbackListAdapter(FeedbackList.this, feedbackArrayList); //passing the arraylist to the adapter
        recyclerViewFeedback.setAdapter(adapter); //setting the adapter to the recyclerview
        recyclerViewFeedback.addItemDecoration(new DividerItemDecoration(FeedbackList.this, LinearLayoutManager.VERTICAL));

        createListFeedback();


        addFeedbackFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FeedbackList.this, AddFeedback.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

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


    private void createListFeedback() {

        url = HttpUrl.parse(GET_FEEDBACK_URL).newBuilder().build();
        Request request = new Request.Builder()
                .url(url)
                .build();

        //client.hostnameVerifier(new NullHostNameVerifier())
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();

                    Log.d("API_CALL", "on-Response: " + body);

                    try {
                        JSONArray feedbackArray = new JSONArray(body);
                        Feedback feedback;

                        for (int i = 0; i < feedbackArray.length(); i++) {
                            JSONObject object = feedbackArray.getJSONObject(i);

                            feedback = new Feedback(
                                    object.getString("id"),
                                    object.getString("stationId"),
                                    object.getString("username"),
                                    object.getString("subject"),
                                    object.getString("description"),
                                    object.getString("createAt"));
                            feedbackArrayList.add(feedback);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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
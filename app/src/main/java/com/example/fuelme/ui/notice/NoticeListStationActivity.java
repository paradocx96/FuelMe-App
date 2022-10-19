package com.example.fuelme.ui.notice;

import static com.example.fuelme.commonconstants.CommonConstants.REMOTE_URL_NOTICE_STATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.fuelme.R;
import com.example.fuelme.models.notice.Notice;
import com.example.fuelme.ui.notice.adapter.NoticeListStationAdapter;

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

public class NoticeListStationActivity extends AppCompatActivity {

    ArrayList<Notice> noticeArrayList;
    RecyclerView recyclerViewNotice;
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_list_station);
        recyclerViewNotice = findViewById(R.id.notice_list_station);
        noticeArrayList = new ArrayList<>();

        getNotices();
    }

    private void getNotices() {
        Request request = new Request.Builder()
                .url(REMOTE_URL_NOTICE_STATION + "002")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    String body = responseBody.string();

                    Log.d("API_CALL", "JSON RESPONSE => " + body);

                    JSONArray jsonArr = new JSONArray(body);
                    Notice notice = new Notice();

                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject object = jsonArr.getJSONObject(i);

                        notice.setId(object.getString("id"));
                        notice.setStationId(object.getString("stationId"));
                        notice.setTitle(object.getString("title"));
                        notice.setDescription(object.getString("description"));
                        notice.setAuthor(object.getString("author"));
                        notice.setCreateAt(object.getString("createAt"));

                        noticeArrayList.add(notice);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                NoticeListStationAdapter adapter = new NoticeListStationAdapter(getApplicationContext(), noticeArrayList);
                                recyclerViewNotice.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerViewNotice.setAdapter(adapter);
                            }
                        });
                    }

                    Log.d("API_CALL", "Array Size onResponse: " + noticeArrayList.size());
                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });

        Log.d("API_CALL", "Array Size getNotices: " + noticeArrayList.size());
    }
}
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuelme.R;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Notice Single view for Station owner UI for FuelMe Application
 *
 * @author IT19180526 - S.A.N.L.D. Chandrasiri
 * @version 1.0
 */
public class NoticeViewStationActivity extends AppCompatActivity {

    // Defined object and variables
    TextView textView_title, textView_description, textView_author, textView_created;
    String id, stationId, title, description, author, created;
    Button btnUpdate, btnDelete;
    Toolbar toolbar;
    private final OkHttpClient client = new OkHttpClient();

    /**
     * This method used for bind the layout UI element with defined local object.
     * Also defined onclick listener for button update and button delete.
     *
     * @param savedInstanceState - Bundle
     * @throws JSONException - Handle the Exception produced by JSON manipulation operations.
     * @see #onCreate(Bundle savedInstanceState)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Binding relevant Layout UI with this class
        setContentView(R.layout.activity_notice_single_station);

        // Define UI objects
        textView_title = findViewById(R.id.notice_single_station_title);
        textView_description = findViewById(R.id.notice_single_station_description);
        textView_author = findViewById(R.id.notice_single_station_author);
        textView_created = findViewById(R.id.notice_single_station_created);
        btnUpdate = findViewById(R.id.notice_single_station_button_update);
        btnDelete = findViewById(R.id.notice_single_station_button_delete);
        toolbar = (Toolbar) findViewById(R.id.toolbar_notice_single_station);

        // Setup back button for toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Get intent and assign to local variable
        Intent intent = getIntent();
        id = intent.getStringExtra("notice_id");
        stationId = intent.getStringExtra("notice_station_id");
        title = intent.getStringExtra("notice_title");
        description = intent.getStringExtra("notice_description");
        author = intent.getStringExtra("notice_author");
        created = intent.getStringExtra("notice_created");

        // Check intent data is null or not
        if (id != null) {
            textView_title.setText(title);
            textView_description.setText(description);
            textView_author.setText(author);
            textView_created.setText(created);
        } else {
            textView_title.setText("Error");
            textView_description.setText("Error");
            textView_author.setText("Error");
            textView_created.setText("Error");
        }

        // Setup on click listener for update button
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Make intent with intent data to be passed into Notice Update UI
                Intent intent = new Intent(NoticeViewStationActivity.this, NoticeUpdateActivity.class);
                intent.putExtra("notice_id", id);
                intent.putExtra("notice_station_id", stationId);
                intent.putExtra("notice_title", title);
                intent.putExtra("notice_description", description);
                intent.putExtra("notice_author", author);
                intent.putExtra("notice_created", created);
                startActivity(intent);
            }
        });

        // Setup on click listener for delete button
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Defined Dialog box to confirm delete action
                AlertDialog alertDialog = new AlertDialog.Builder(NoticeViewStationActivity.this)
                        .setIcon(R.drawable.ic_warning)
                        .setTitle("Are you sure ?")
                        .setMessage("This action cannot be undone! If you click 'Yes', this notice will be deleted permanently.")

                        // If user select 'Yes' action call deleteNotice method to delete notice
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteNotice(id);
                            }
                        })

                        // If user select 'No' action cancel operation
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getApplicationContext(), "Delete cancelled!", Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();
            }
        });
    }

    /**
     * This method used for getting notice id as parameter and delete notice.
     * Then execute HTTP request to delete notice by notice id.
     *
     * @param id - String
     * @throws IOException - Handle the Exception produced by failed or interrupted I/O operations.
     * @see #deleteNotice(String)
     */
    void deleteNotice(String id) {
        // Create HTTP request for delete notice from API
        Request request = new Request.Builder()
                .url(REMOTE_URL_NOTICE + id)
                .delete()
                .build();

        // Execute HTTP request and check whether request response
        client.newCall(request).enqueue(new Callback() {
            // HTTP Request when failed this will execute.
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(NoticeViewStationActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(NoticeViewStationActivity.this, "Notice deleted!", Toast.LENGTH_SHORT).show();

                            // Change UI into Notice list view
                            Intent intent = new Intent(NoticeViewStationActivity.this, NoticeListStationActivity.class);
                            intent.putExtra("station_id", stationId);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
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
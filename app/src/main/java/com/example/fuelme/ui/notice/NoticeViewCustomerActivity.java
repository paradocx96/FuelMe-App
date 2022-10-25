/**
 * FuelMe APP
 * Enterprise Application Development - SE4040
 *
 * @author IT19180526 - S.A.N.L.D. Chandrasiri
 * @version 1.0
 */

package com.example.fuelme.ui.notice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.fuelme.R;

import org.json.JSONException;

/**
 * Notice Single view for Customer UI for FuelMe Application
 *
 * @author IT19180526 - S.A.N.L.D. Chandrasiri
 * @version 1.0
 */
public class NoticeViewCustomerActivity extends AppCompatActivity {

    // Defined object and variables
    TextView textView_title, textView_description, textView_author, textView_created;
    String id, stationId, title, description, author, created;
    Toolbar toolbar;

    /**
     * This method used for bind the layout UI element with defined local object.
     *
     * @param savedInstanceState - Bundle
     * @see #onCreate(Bundle savedInstanceState)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Binding relevant Layout UI with this class
        setContentView(R.layout.activity_notice_single_customer);

        // Define UI objects
        textView_title = findViewById(R.id.notice_single_title);
        textView_description = findViewById(R.id.notice_single_description);
        textView_author = findViewById(R.id.notice_single_author);
        textView_created = findViewById(R.id.notice_single_created);
        toolbar = (Toolbar) findViewById(R.id.toolbar_notice_single);

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
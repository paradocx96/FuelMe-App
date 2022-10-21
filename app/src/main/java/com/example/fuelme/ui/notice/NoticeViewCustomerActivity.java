package com.example.fuelme.ui.notice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.fuelme.R;

public class NoticeViewCustomerActivity extends AppCompatActivity {

    TextView textView_title, textView_description, textView_author, textView_created;
    String id, stationId, title, description, author, created;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        Intent intent = getIntent();
        id = intent.getStringExtra("notice_id");
        stationId = intent.getStringExtra("notice_station_id");
        title = intent.getStringExtra("notice_title");
        description = intent.getStringExtra("notice_description");
        author = intent.getStringExtra("notice_author");
        created = intent.getStringExtra("notice_created");

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
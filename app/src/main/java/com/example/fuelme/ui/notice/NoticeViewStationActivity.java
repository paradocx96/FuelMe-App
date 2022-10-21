package com.example.fuelme.ui.notice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fuelme.R;

public class NoticeViewStationActivity extends AppCompatActivity {

    TextView textView_title, textView_description, textView_author, textView_created;
    String id, stationId, title, description, author, created;
    Button btnUpdate, btnDelete;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
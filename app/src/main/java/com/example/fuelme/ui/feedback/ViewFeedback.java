package com.example.fuelme.ui.feedback;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.fuelme.R;
import com.example.fuelme.models.Feedback;

public class ViewFeedback extends AppCompatActivity {

    TextView txtSubjectView, txtDescriptionView, txtUsernameView, txtDateTimeView;
    String id, stationId, subject, description, username, dateTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_feedback);
        txtSubjectView = findViewById(R.id.txtSubjectView);
        txtDescriptionView = findViewById(R.id.txtDescriptionView);
        txtUsernameView = findViewById(R.id.txtUsernameView);
        txtDateTimeView = findViewById(R.id.txtDateTimeView);

        Intent intent = getIntent();
        id = intent.getStringExtra("feedback_id"); //1 - Get feedback id from intent
        stationId = intent.getStringExtra("feedback_stationId");
        subject = intent.getStringExtra("feedback_subject"); //2 - Get feedback subject from intent
        description = intent.getStringExtra("feedback_description"); //3 - Get feedback description from intent
        username = intent.getStringExtra("feedback_username"); //4 - Get feedback username from intent
        dateTime = intent.getStringExtra("feedback_dateTime"); //5 - Get feedback dateTime from intent

        if(id != null){
            //2 - Get feedback from database
           txtSubjectView.setText(subject);
           txtDescriptionView.setText(description);
           txtUsernameView.setText(username);
           txtDateTimeView.setText(dateTime);
        }else{
            //3 - If feedback id is null, then show error message
            txtSubjectView.setText("Error");
            txtDescriptionView.setText("Error");
            txtUsernameView.setText("Error");
            txtDateTimeView.setText("Error");
        }

    }
}
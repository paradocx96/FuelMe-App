package com.example.fuelme.ui.feedback;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuelme.R;
import com.example.fuelme.models.Feedback;

public class ViewFeedback extends AppCompatActivity {

    TextView txtSubjectView, txtDescriptionView, txtUsernameView, txtDateTimeView;
    String id, stationId, subject, description, username, dateTime;

    Button btnEditFeedback, btnDeleteFeedback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_feedback);
        txtSubjectView = findViewById(R.id.txtSubjectView);
        txtDescriptionView = findViewById(R.id.txtDescriptionView);
        txtUsernameView = findViewById(R.id.txtUsernameView);
        txtDateTimeView = findViewById(R.id.txtDateTimeView);
        btnEditFeedback = findViewById(R.id.btn_editFeedback);
        btnDeleteFeedback = findViewById(R.id.btn_deleteFeedback);

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

        btnEditFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewFeedback.this, EditFeedback.class);
                intent.putExtra("feedback_id", id);
                intent.putExtra("feedback_stationId", stationId);
                intent.putExtra("feedback_subject", subject);
                intent.putExtra("feedback_description", description);
                intent.putExtra("feedback_username", username);
                intent.putExtra("feedback_dateTime", dateTime);
                startActivity(intent);
            }
        });
        
        btnDeleteFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //4 - Delete feedback from database
//                Feedback feedback = new Feedback();
//                feedback.setId(id);
//                feedback.delete();
//                finish();
                Toast.makeText(ViewFeedback.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
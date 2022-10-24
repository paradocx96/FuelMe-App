/*
 * IT19180526
 * Chandrasiri S.A.N.L.D.
 * Enterprise Application Development - SE4040
 * Update User Profile UI for FuelMe Application
 * */

package com.example.fuelme.ui.auth;

import static com.example.fuelme.commonconstants.CommonConstants.REMOTE_URL_USER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuelme.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileUpdateActivity extends AppCompatActivity {

    // Defined object and variables
    TextView textViewFullName, textViewEmail;
    ImageView viewProfileImage;
    Button btnUpdate, btnCancel;
    String logged_id, logged_name, logged_email;
    private final OkHttpClient client = new OkHttpClient();
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * This method used for bind the layout UI element with defined local object.
     * Also defined onclick listener for button update and button cancel.
     *
     * @throws JSONException - Handle the Exception produced by JSON manipulation operations.
     * @see #onCreate(Bundle savedInstanceState)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Binding relevant Layout UI with this class
        setContentView(R.layout.activity_auth_profile_update);

        // Define UI objects
        viewProfileImage = findViewById(R.id.auth_profile_update_image);
        textViewFullName = findViewById(R.id.auth_profile_update_fullname);
        textViewEmail = findViewById(R.id.auth_profile_update_email);
        btnUpdate = findViewById(R.id.auth_profile_update_button);
        btnCancel = findViewById(R.id.auth_profile_update_button_cancel);

        // Defined intent data
        Intent intent = getIntent();
        logged_id = intent.getStringExtra("user_id");
        logged_name = intent.getStringExtra("user_full_name");
        logged_email = intent.getStringExtra("user_email");

        // Setup User Data
        textViewFullName.setText(logged_name);
        textViewEmail.setText(logged_email);

        // Setup on click listener for update button
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create JSON object using API response
                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("fullName", textViewFullName.getText().toString());
                    jsonObject.put("email", textViewEmail.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Convert JSON object to String and Make Response body
                String jsonString = jsonObject.toString();
                RequestBody body = RequestBody.create(jsonString, JSON);

                // Calling updateUserData method for update user data.
                updateUserData(logged_id, body);
            }
        });

        // Setup on click listener for cancel button
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileUpdateActivity.this, "Cancel!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProfileUpdateActivity.this, ProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    /**
     * This method used for getting updated user data and logged user id as parameter.
     * Then execute HTTP request to update the relevant user data.
     *
     * @throws IOException - Handle the Exception produced by failed or interrupted I/O operations.
     * @see #updateUserData(String id, RequestBody body)
     */
    void updateUserData(String id, RequestBody body) {
        // Create HTTP request for update user data from API
        Request request = new Request.Builder()
                .url(REMOTE_URL_USER + id)
                .put(body)
                .build();

        // Execute HTTP request and check whether request response
        client.newCall(request).enqueue(new Callback() {
            // HTTP Request when failed this will execute.
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(ProfileUpdateActivity.this, "User Update Error!", Toast.LENGTH_SHORT).show();
            }

            // HTTP Request when response got from API this will execute.
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // Check response is success or not
                if (response.isSuccessful()) {
                    // Display user update success message and going back to user profile.
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Make toast message
                            Toast.makeText(ProfileUpdateActivity.this, "User Updated!", Toast.LENGTH_SHORT).show();

                            // Change UI into User profile
                            Intent intent = new Intent(ProfileUpdateActivity.this, ProfileActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                } else {
                    // if response got error, this will show error message.
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Make toast message
                            Toast.makeText(ProfileUpdateActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
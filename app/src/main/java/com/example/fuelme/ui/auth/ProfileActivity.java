/**
 * FuelMe APP
 * Enterprise Application Development - SE4040
 *
 * @author IT19180526 - S.A.N.L.D. Chandrasiri
 * @version 1.0
 */

package com.example.fuelme.ui.auth;

import static com.example.fuelme.commonconstants.CommonConstants.REMOTE_URL_USER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuelme.R;
import com.example.fuelme.models.auth.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * User Profile UI for FuelMe Application
 *
 * @author IT19180526 - S.A.N.L.D. Chandrasiri
 * @version 1.0
 */
public class ProfileActivity extends AppCompatActivity {

    // Defined object and variables
    SharedPreferences preferences;
    String logged_id, logged_name, logged_username, logged_role, logged_email;
    TextView textViewFullName, textViewUsername, textViewRole, textViewEmail;
    ImageView viewProfileImage;
    Toolbar toolbar;
    Button btnUpdate;
    private final OkHttpClient client = new OkHttpClient();

    /**
     * This method used for bind the layout UI element with defined local object.
     * Also defined onclick listener for button update.
     * Then call getUserData() method to get user data from API.
     *
     * @see #onCreate(Bundle savedInstanceState)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Binding relevant Layout UI with this class
        setContentView(R.layout.activity_auth_profile);

        // Define UI objects
        viewProfileImage = findViewById(R.id.auth_profile_image);
        textViewFullName = findViewById(R.id.auth_profile_name);
        textViewUsername = findViewById(R.id.auth_profile_username);
        textViewRole = findViewById(R.id.auth_profile_role);
        textViewEmail = findViewById(R.id.auth_profile_email);
        btnUpdate = findViewById(R.id.auth_profile_button_update);
        toolbar = findViewById(R.id.toolbar_auth_profile);

        // Setup back button for toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Get logged user's data
        preferences = getSharedPreferences("login_data", MODE_PRIVATE);
        logged_id = preferences.getString("user_id", "");
        logged_name = preferences.getString("user_full_name", "");
        logged_username = preferences.getString("user_username", "");
        logged_role = preferences.getString("user_role", "");
        logged_email = preferences.getString("user_email", "");

        // Setup User Data
        textViewFullName.setText(logged_name);
        textViewUsername.setText(logged_username);
        textViewRole.setText(logged_role);
        textViewEmail.setText(logged_email);

        // Setup on click listener for update button
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to User profile update UI
                Intent intent = new Intent(ProfileActivity.this, ProfileUpdateActivity.class);
                intent.putExtra("user_id", logged_id);
                intent.putExtra("user_full_name", textViewFullName.getText().toString());
                intent.putExtra("user_email", textViewEmail.getText().toString());
                startActivity(intent);
            }
        });

        // Calling getUserData method for get relevant data from database
        getUserData();
    }

    /**
     * This method used for getting user data by logged user id.
     * Then call the reassign user data method for set user data to be display in layout.
     *
     * @throws IOException   - Handle the Exception produced by failed or interrupted I/O operations.
     * @throws JSONException - Handle the Exception produced by JSON manipulation operations.
     * @see #getUserData()
     */
    private void getUserData() {
        // Create HTTP request for getting user data from API
        Request request = new Request.Builder()
                .url(REMOTE_URL_USER + logged_id)
                .get()
                .build();

        // Execute HTTP request and check whether request response
        client.newCall(request).enqueue(new Callback() {
            // HTTP Request when failed this will execute.
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(ProfileActivity.this, "User Profile Error!", Toast.LENGTH_LONG).show();
            }

            // HTTP Request when response got from API this will execute.
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // Check response is success or not
                if (response.isSuccessful()) {
                    // Create response body with API response
                    ResponseBody responseBody = response.body();

                    // Check response body is empty or not
                    if (responseBody != null) {
                        // Convert API response to String
                        String responseString = responseBody.string();

                        try {
                            // Create JSON object using API response
                            JSONObject jsonResponse = new JSONObject(responseString);

                            // Create user object with database data
                            User user = new User(
                                    logged_id,
                                    jsonResponse.getString("username"),
                                    jsonResponse.getString("fullName"),
                                    jsonResponse.getString("email"),
                                    null,
                                    jsonResponse.getString("role")
                            );

                            // Calling reassign method for view user data
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    reLoadUserData(user);
                                }
                            });
                        } catch (JSONException e) {
                            Log.d("API_CALL", "JSON Error : " + e.getMessage());
                        }
                    }
                }
            }
        });
    }

    /**
     * This method used for Re-Assign user data after retrieve from API
     *
     * @see #reLoadUserData(User user)
     */
    void reLoadUserData(User user) {
        // Setup User Data
        textViewFullName.setText(user.getFullName());
        textViewUsername.setText(user.getUsername());
        textViewRole.setText(user.getRole());
        textViewEmail.setText(user.getEmail());
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
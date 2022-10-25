/**
 * FuelMe APP
 * Enterprise Application Development - SE4040
 *
 * @author IT19180526 - S.A.N.L.D. Chandrasiri
 * @version 1.0
 */

package com.example.fuelme.ui.auth;

import static com.example.fuelme.commonconstants.CommonConstants.REMOTE_URL_AUTH_REGISTER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuelme.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * User Registration UI for FuelMe Application
 *
 * @author IT19180526 - S.A.N.L.D. Chandrasiri
 * @version 1.0
 *
 * Reference:
 * https://square.github.io/okhttp/
 * https://www.digitalocean.com/community/tutorials/retrofit-android-example-tutorial
 * https://developer.android.com/docs
 * https://www.youtube.com/watch?v=lBzzL7ZLT7c
 */
public class RegistrationActivity extends AppCompatActivity {

    // Defined object and variables
    TextView textViewFullName, textViewUsername, textViewPassword, textViewRePassword, textViewEmail;
    Button btnRegister, btnCancel;
    Spinner spinnerRole;
    Toolbar toolbar;
    String response_message;
    private final OkHttpClient client = new OkHttpClient();
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * This method used for bind the layout UI element with defined local object.
     * Also defined onclick listener for button register and button cancel.
     *
     * @param savedInstanceState - Bundle
     * @throws JSONException - Handle the Exception produced by JSON manipulation operations.
     * @see #onCreate(Bundle savedInstanceState)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Binding relevant Layout UI with this class
        setContentView(R.layout.activity_auth_registration);

        // Define UI objects
        btnRegister = findViewById(R.id.auth_registration_button_reg);
        btnCancel = findViewById(R.id.auth_registration_button_cancel);
        textViewFullName = findViewById(R.id.auth_registration_fullname);
        textViewUsername = findViewById(R.id.auth_registration_username);
        textViewPassword = findViewById(R.id.auth_registration_password);
        textViewRePassword = findViewById(R.id.auth_registration_re_password);
        textViewEmail = findViewById(R.id.auth_registration_email);
        spinnerRole = findViewById(R.id.auth_registration_role_select);
        toolbar = findViewById(R.id.toolbar_auth_reg);

        // Setup back button for toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Spinner Setup
        ArrayList<String> role_list = new ArrayList<>();
        role_list.add("Customer");
        role_list.add("Owner");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_dropdown_item_1line, role_list);
        spinnerRole.setAdapter(spinnerAdapter);

        // Setup on click listener for register button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get password and re-password field data and assign to string
                String pass1 = textViewPassword.getText().toString();
                String pass2 = textViewRePassword.getText().toString();

                // Check whether is passwords match or not
                if (pass1.equals(pass2)) {
                    // Create JSON object to send HTTP request
                    JSONObject jsonObject = new JSONObject();

                    try {
                        jsonObject.put("username", textViewUsername.getText().toString());
                        jsonObject.put("fullName", textViewFullName.getText().toString());
                        jsonObject.put("password", textViewPassword.getText().toString());
                        jsonObject.put("email", textViewEmail.getText().toString());
                        jsonObject.put("role", spinnerRole.getSelectedItem().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Convert JSON object to String and Make Response body
                    String jsonString = jsonObject.toString();
                    RequestBody body = RequestBody.create(jsonString, JSON);

                    // Calling registerUser method for register new user
                    registerUser(body);
                } else {
                    Toast.makeText(RegistrationActivity.this, "Password Not Match!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Setup on click listener for cancel button
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear text fields
                clearTextView();

                // Change UI to Login UI
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    /**
     * This method used for getting new user data and register new user.
     * Then execute HTTP request to register the new user.
     *
     * @param body - RequestBody
     * @throws IOException   - Handle the Exception produced by failed or interrupted I/O operations.
     * @throws JSONException - Handle the Exception produced by JSON manipulation operations.
     * @see #registerUser(RequestBody body)
     */
    void registerUser(RequestBody body) {
        // Create HTTP request for register new user
        Request request = new Request.Builder()
                .url(REMOTE_URL_AUTH_REGISTER)
                .post(body)
                .build();

        // Execute HTTP request and check whether request response
        client.newCall(request).enqueue(new Callback() {
            // HTTP Request when failed this will execute.
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("API_CALL", "USER_REGISTRATION onFailure: " + e.getMessage());
            }

            // HTTP Request when response got from API this will execute.
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // Check response is success or not
                if (response.isSuccessful()) {
                    // Create JSON object using API response
                    ResponseBody responseBody = response.body();

                    // Check response body is empty or not
                    if (responseBody != null) {
                        // Convert API response to String
                        String responseString = responseBody.string();

                        try {
                            // Create JSON object using API response
                            JSONObject jsonResponse = new JSONObject(responseString);
                            response_message = jsonResponse.getString("message");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    responseHandler(response_message);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegistrationActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    /**
     * This method used for Clear text fields and change UI into Login UI
     *
     * @param message - String
     * @see #responseHandler(String message)
     */
    void responseHandler(String message) {
        if ("User Registration Success".equals(message)) {
            clearTextView();
            Toast.makeText(RegistrationActivity.this, "Registration Successfully!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * This method used for clear text fields
     *
     * @see #clearTextView()
     */
    void clearTextView() {
        textViewFullName.setText(null);
        textViewUsername.setText(null);
        textViewPassword.setText(null);
        textViewRePassword.setText(null);
        textViewEmail.setText(null);
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
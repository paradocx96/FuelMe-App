/**
 * FuelMe APP
 * Enterprise Application Development - SE4040
 *
 * @author IT19180526 - S.A.N.L.D. Chandrasiri
 * @version 1.0
 */

package com.example.fuelme.ui.auth;

import static com.example.fuelme.commonconstants.CommonConstants.REMOTE_URL_AUTH_LOGIN;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fuelme.R;
import com.example.fuelme.ui.mainscreen.MainActivity;

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
import okhttp3.ResponseBody;

/**
 * User Login UI for FuelMe Application
 *
 * @author IT19180526 - S.A.N.L.D. Chandrasiri
 * @version 1.0
 */
public class LoginActivity extends AppCompatActivity {

    // Defined object and variables
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    EditText username, password;
    Button loginButton, registerButton;
    Context context;
    String login_response_message;
    private final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * This method used for bind the layout UI element with defined local object.
     * Also defined onclick listener for button login and register.
     * Also check the user logged or not and if logged redirect to Home UI.
     *
     * @param savedInstanceState - Bundle
     * @throws JSONException - Handle the Exception produced by JSON manipulation operations.
     * @see #onCreate(Bundle savedInstanceState)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Binding relevant Layout UI with this class
        setContentView(R.layout.activity_auth_login);

        // Defined Shared preference data configuration
        preferences = getSharedPreferences("login_data", MODE_PRIVATE);
        editor = preferences.edit();

        // Check user logged or not
        if (preferences.contains("user_id")) {
            // Redirect to Home UI
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else {
            // Define UI objects
            username = findViewById(R.id.auth_login_username);
            password = findViewById(R.id.auth_login_password);
            loginButton = findViewById(R.id.auth_login_button_login);
            registerButton = findViewById(R.id.auth_login_button_register);
            login_response_message = "default";
            context = getApplicationContext();

            // Setup on click listener for login button
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        // Create JSON object
                        JSONObject jsonObject = new JSONObject();

                        // Add data to JSON object
                        jsonObject.put("username", (username.getText().toString()).trim());
                        jsonObject.put("password", (password.getText().toString()).trim());

                        // Convert JSON object to String and Make Response body
                        String jsonString = jsonObject.toString();
                        RequestBody requestBody = RequestBody.create(jsonString, JSON);

                        // Calling loginUser method for login user
                        loginUser(requestBody);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            // Setup on click listener for register button
            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Redirect to Register UI
                    Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    /**
     * This method used for validate user credentials and retrieve user data if user is valid.
     * Then call the responseHandler method to display the toast messages and redirect to Home UI.
     *
     * @param requestBody - RequestBody
     * @throws IOException   - Handle the Exception produced by failed or interrupted I/O operations.
     * @throws JSONException - Handle the Exception produced by JSON manipulation operations.
     * @see #loginUser(RequestBody requestBody)
     */
    void loginUser(RequestBody requestBody) {
        // Create HTTP request for validate user data
        Request request = new Request.Builder()
                .url(REMOTE_URL_AUTH_LOGIN)
                .post(requestBody)
                .build();

        // Execute HTTP request and check whether request response
        client.newCall(request).enqueue(new Callback() {
            // HTTP Request when failed this will execute.
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("API_CALL", "OnFailure : " + e.getMessage());
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

                            // Check the response message in JSON object
                            // and assign to login_response_message if any error or not
                            switch (jsonResponse.getString("message")) {
                                case "User or Username Not Found":
                                case "Incorrect Password":
                                    login_response_message = jsonResponse.getString("message");
                                    break;
                                case "Correct Username and Password":
                                    login_response_message = jsonResponse.getString("message");

                                    // Store logged user data in Shared Preferences
                                    editor.putString("user_id", jsonResponse.getString("Id"));
                                    editor.putString("user_username", jsonResponse.getString("Username"));
                                    editor.putString("user_full_name", jsonResponse.getString("FullName"));
                                    editor.putString("user_email", jsonResponse.getString("Email"));
                                    editor.putString("user_role", jsonResponse.getString("Role"));
                                    editor.apply();
                                    break;
                            }

                            // Calling responseHandler method for view the response in UI
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    responseHandler(login_response_message);
                                }
                            });
                        } catch (JSONException e) {
                            Log.d("API_CALL", "JSON Error : " + e.getMessage());
                        }
                    }
                } else {
                    Log.d("API_CALL", "onResponse Failed");
                }
            }
        });
    }

    /**
     * This method used to display error responses (toast messages).
     * Also change UI to Home UI.
     *
     * @param message - String
     * @see #responseHandler(String message)
     */
    void responseHandler(String message) {
        switch (message) {
            case "User or Username Not Found":
                Toast.makeText(LoginActivity.this, "Incorrect Username!", Toast.LENGTH_LONG).show();
                break;
            case "Incorrect Password":
                Toast.makeText(LoginActivity.this, "Incorrect Password!", Toast.LENGTH_LONG).show();
                break;
            case "Correct Username and Password":
                Toast.makeText(LoginActivity.this, "Welcome!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case "default":
                Toast.makeText(LoginActivity.this, "Error!", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
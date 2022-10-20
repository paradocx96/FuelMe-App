package com.example.fuelme.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button loginButton, registerButton;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    Context context;
    String login_response_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_login);

        preferences = getSharedPreferences("login_data", MODE_PRIVATE);
        editor = preferences.edit();

        if (preferences.contains("user_id")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);

        } else {
            username = findViewById(R.id.auth_login_username);
            password = findViewById(R.id.auth_login_password);
            loginButton = findViewById(R.id.auth_login_button_login);
            registerButton = findViewById(R.id.auth_login_button_register);

            login_response_message = "default";
            context = getApplicationContext();


            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        JSONObject jsonObject = new JSONObject();

                        jsonObject.put("username", username.getText().toString());
                        jsonObject.put("password", password.getText().toString());

                        String jsonString = jsonObject.toString();
                        RequestBody requestBody = RequestBody.create(jsonString, JSON);

                        loginUser(requestBody);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    void loginUser(RequestBody requestBody) {
        String REMOTE_URL_AUTH_LOGIN = "https://fuelme.azurewebsites.net/api/Auth/login";

        Request request = new Request.Builder()
                .url(REMOTE_URL_AUTH_LOGIN)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("API_CALL", "OnFailure : " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        String responseString = responseBody.string();

                        try {
                            JSONObject jsonResponse = new JSONObject(responseString);

                            switch (jsonResponse.getString("message")) {
                                case "User or Username Not Found":
                                case "Incorrect Password":
                                    login_response_message = jsonResponse.getString("message");
                                    break;
                                case "Correct Username and Password":
                                    login_response_message = jsonResponse.getString("message");

                                    editor.putString("user_id", jsonResponse.getString("Id"));
                                    editor.putString("user_username", jsonResponse.getString("Username"));
                                    editor.putString("user_full_name", jsonResponse.getString("FullName"));
                                    editor.putString("user_email", jsonResponse.getString("Email"));
                                    editor.putString("user_role", jsonResponse.getString("Role"));
                                    editor.commit();
                                    break;
                            }

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
                startActivity(intent);
                break;
            case "default":
                Toast.makeText(LoginActivity.this, "Error!", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
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

public class RegistrationActivity extends AppCompatActivity {

    TextView textViewFullName, textViewUsername, textViewPassword, textViewRePassword, textViewEmail;
    Button btnRegister, btnCancel;
    Spinner spinnerRole;
    Toolbar toolbar;
    String response_message;

    private final OkHttpClient client = new OkHttpClient();
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass1 = textViewPassword.getText().toString();
                String pass2 = textViewRePassword.getText().toString();

                if (pass1.equals(pass2)) {
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

                    String jsonString = jsonObject.toString();
                    RequestBody body = RequestBody.create(jsonString, JSON);

                    registerUser(body);
                } else {
                    Toast.makeText(RegistrationActivity.this, "Password Not Match!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearTextView();
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    void registerUser(RequestBody body) {
        Request request = new Request.Builder()
                .url(REMOTE_URL_AUTH_REGISTER)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("API_CALL", "USER_REGISTRATION onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        String responseString = responseBody.string();

                        try {
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

    void responseHandler(String message) {
        if ("User Registration Success".equals(message)) {
            clearTextView();
            Toast.makeText(RegistrationActivity.this, "Registration Successfully!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_LONG).show();
        }
    }

    void clearTextView() {
        textViewFullName.setText(null);
        textViewUsername.setText(null);
        textViewPassword.setText(null);
        textViewRePassword.setText(null);
        textViewEmail.setText(null);
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
package com.example.sportsclub.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.sportsclub.Constants;
import com.example.sportsclub.R;

import org.json.JSONException;
import org.json.JSONObject;

//import static com.example.sportsclub.activities.SignInActivity.ADDRESS;
import static com.example.sportsclub.activities.SignInActivity.EMAIL;
import static com.example.sportsclub.activities.SignInActivity.ID;
//import static com.example.sportsclub.activities.SignInActivity.NAME;
import static com.example.sportsclub.activities.SignInActivity.PHONE;
import static com.example.sportsclub.activities.SignInActivity.SHARED_PREFS;
import static com.example.sportsclub.activities.SignInActivity.TOKEN;
import static com.example.sportsclub.activities.SignInActivity.USERNAME;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = SignUpActivity.class.getSimpleName();
    private ProgressBar progressBar;
    private Button btnSignUp;
    private EditText edtUsername, edtPhoneNumber, edtEmail, edtPassword, edtConfirmPassword;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnSignUp = findViewById(R.id.btn_sign_up);
        edtUsername = findViewById(R.id.edt_username);
//        edtName = findViewById(R.id.edt_name);
//        edtAddress = findViewById(R.id.edt_address);
        edtPhoneNumber = findViewById(R.id.edt_phone_number);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        edtConfirmPassword = findViewById(R.id.edt_confirm_password);
        progressBar = findViewById(R.id.progressBar);

        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_sign_up) {
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            token = sharedPreferences.getString(TOKEN, "");

            String inputUsername = edtUsername.getText().toString().trim();
//            String inputName = edtName.getText().toString().trim();
//            String inputAddress = edtAddress.getText().toString().trim();
            String inputPhoneNumber = edtPhoneNumber.getText().toString().trim();
            String inputEmail = edtEmail.getText().toString().trim();
            String inputPassword = edtPassword.getText().toString().trim();
            String inputConfirmPassword = edtConfirmPassword.getText().toString().trim();

            boolean isEmpty = false;
            boolean isInvalidPassword = false;

            if (inputUsername.isEmpty()) {
                isEmpty = true;
                edtUsername.setError("Username harus diisi");
            }

//            if (inputName.isEmpty()) {
//                isEmpty = true;
//                edtName.setError("Name harus diisi");
//            }
//
//            if (inputAddress.isEmpty()) {
//                isEmpty = true;
//                edtAddress.setError("Address harus diisi");
//            }

            if (inputPhoneNumber.isEmpty()) {
                isEmpty = true;
                edtPhoneNumber.setError("Phone Number harus diisi");
            }

            if (inputEmail.isEmpty()) {
                isEmpty = true;
                edtEmail.setError("Email harus diisi");
            }

            if (inputPassword.isEmpty()) {
                isEmpty = true;
                edtPassword.setError("Password harus diisi");
            }

            if (inputConfirmPassword.isEmpty()) {
                isEmpty = true;
                edtConfirmPassword.setError("Confirm Password harus diisi");
            }

            if (!inputPassword.equals(inputConfirmPassword)) {
                isInvalidPassword = true;
                edtConfirmPassword.setError("Password & Confirm Password don't match");
            }

            if (!isEmpty && !isInvalidPassword) {
                btnSignUp.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                AndroidNetworking.post(Constants.BASE_URL + "/api/register")
                        .addBodyParameter("username", inputUsername)
//                        .addBodyParameter("name", inputName)
//                        .addBodyParameter("address", inputAddress)
                        .addBodyParameter("phone", inputPhoneNumber)
                        .addBodyParameter("email", inputEmail)
                        .addBodyParameter("password", inputPassword)
                        .addBodyParameter("token", token)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // do anything with response
                                try {
                                    String status = response.getString("status");
                                    String message = response.getString("message");
                                    if (status.equals("success")) {
                                        JSONObject data = response.getJSONObject("data");
                                        String id = data.getString("id");
                                        String email = data.getString("email");
                                        String username = data.getString("username");
                                        String phoneNumber = data.getString("phone");
                                        System.out.println("data : " + data);
                                        System.out.println("username : " + username);

                                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();

                                        editor.putString(ID, id);
                                        editor.putString(EMAIL, email);
                                        editor.putString(USERNAME, username);
                                        editor.putString(PHONE, phoneNumber);
                                        editor.apply();

                                        Intent intent = new Intent(SignUpActivity.this, VerifyActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);

                                        Toast.makeText(SignUpActivity.this, "Selamat datang " + username, Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(ANError error) {
                                // handle error
                                Log.d("RBA", "onError: " + error.getErrorBody());
                                Log.d("RBA", "onError: " + error.getLocalizedMessage());
                                Log.d("RBA", "onError: " + error.getErrorDetail());
                                Log.d("RBA", "onError: " + error.getResponse());
                                Log.d("RBA", "onError: " + error.getErrorCode());
                                progressBar.setVisibility(View.GONE);
                                btnSignUp.setVisibility(View.VISIBLE);
                            }
                        });
            }
        }
    }
}

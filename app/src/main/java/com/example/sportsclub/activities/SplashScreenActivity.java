package com.example.sportsclub.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.sportsclub.R;

import static com.example.sportsclub.activities.SignInActivity.ID;
import static com.example.sportsclub.activities.SignInActivity.SHARED_PREFS;

public class SplashScreenActivity extends AppCompatActivity {
    private int waktu_loading = 1500;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        id = sharedPreferences.getString(ID, "");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (id.isEmpty()) {
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashScreenActivity.this, MainDashboardActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, waktu_loading);
    }
}

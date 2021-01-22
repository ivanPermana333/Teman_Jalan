package com.example.sportsclub;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

import static com.example.sportsclub.activities.SignInActivity.SHARED_PREFS;
import static com.example.sportsclub.activities.SignInActivity.TOKEN;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    public MyFirebaseMessagingService() {
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TOKEN, token);
        editor.apply();
    }
}

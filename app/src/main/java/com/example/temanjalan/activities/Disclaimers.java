package com.example.temanjalan.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.temanjalan.R;

import static com.example.temanjalan.activities.SignInActivity.ID;
import static com.example.temanjalan.activities.SignInActivity.SHARED_PREFS;

public class Disclaimers extends AppCompatActivity {
    private Button btnnext;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimers);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        id = sharedPreferences.getString(ID, "");

        btnnext =findViewById(R.id.btn_next);
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Disclaimers.this, MainActivity.class);
                intent.putExtra("Title", "Disclemer");
                startActivity(intent);
            }
        });
    }
}
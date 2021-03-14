package com.example.temanjalan.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.temanjalan.Constants;
import com.example.temanjalan.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.temanjalan.activities.SignInActivity.ADDRESS;
import static com.example.temanjalan.activities.SignInActivity.EMAIL;
import static com.example.temanjalan.activities.SignInActivity.ID;
import static com.example.temanjalan.activities.SignInActivity.NAME;
import static com.example.temanjalan.activities.SignInActivity.PHONE;
import static com.example.temanjalan.activities.SignInActivity.SHARED_PREFS;
import static com.example.temanjalan.activities.SignInActivity.USERNAME;

public class ProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView mTitle, tvLogOut;
    private EditText et_username, et_nama, et_phone, et_alamat, et_email;
    private String id;
    private ImageView iv_info;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        binding();

//        String title = getIntent().getStringExtra("Title");
//        mTitle.setText(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        id = sharedPreferences.getString(ID, "");

        fetchProfile();
    }

    private void binding (){
        toolbar = findViewById(R.id.toolbar);
//        mTitle = toolbar.findViewById(R.id.toolbar_title);

        et_username = findViewById(R.id.et_username);
        et_nama = findViewById(R.id.et_nama);
        et_phone = findViewById(R.id.et_phone);
        et_alamat = findViewById(R.id.et_alamat);
        et_email = findViewById(R.id.et_email);
        tvLogOut = findViewById(R.id.tvLogOut);
        tvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ProfileActivity.this)
                        .setMessage("Apakah anda yakin ingin keluar ?")
                        .setNegativeButton("Tidak", null)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply();
                                Intent intent = new Intent(ProfileActivity.this, Disclaimers.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }).create().show();

            }
        });


        iv_info = findViewById(R.id.iV_info);
        iv_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ProfileActivity.this)
                        .setMessage("Apakah anda ingin mendapatkan Penghasilan dari aplikasi ini?")
                        .setNegativeButton("Tidak", null)
                        .setPositiveButton("Ya  ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
//                                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
//                                SharedPreferences.Editor editor = sharedPreferences.edit();
//                                editor.clear();
//                                editor.apply();
                                String url = "http://192.168.43.92:8000/registerteman";
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
//                                finish();
                            }
                        }).create().show();
            }
        });
    }

    public void fetchProfile() {
        AndroidNetworking.get(Constants.BASE_URL + "/api/profile/{id}")
                .addPathParameter("id", id)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            String name = data.getString("name");
                            String username = data.getString("username");
                            String email = data.getString("email");
                            String phoneNumber = data.getString("phone");
                            String address = data.getString("address");
                            String avatar = data.getString("avatar");
                            if (avatar != "null") {
                                avatar = Constants.BASE_URL + "/storage/" + avatar;
                            }

                            et_nama.setText(name);
                            et_username.setText(username);
                            et_email.setText(email);
                            et_phone.setText(phoneNumber);
                            et_alamat.setText(address);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        et_nama.setText(sharedPreferences.getString(NAME, ""));
                        et_username.setText(sharedPreferences.getString(USERNAME, ""));
                        et_email.setText(sharedPreferences.getString(EMAIL, ""));
                        et_phone.setText(sharedPreferences.getString(PHONE, ""));
                        et_alamat.setText(sharedPreferences.getString(ADDRESS, ""));
                    }
                });
    }



    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
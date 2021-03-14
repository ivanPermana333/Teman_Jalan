package com.example.temanjalan.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.temanjalan.R;
import com.example.temanjalan.model.Teman;

import cn.pedant.SweetAlert.SweetAlertDialog;

//import static com.example.sportsclub.activities.SignInActivity.NAME;
import static com.example.temanjalan.activities.SignInActivity.USERNAME;
import static com.example.temanjalan.activities.SignInActivity.SHARED_PREFS;

public class DetailFieldActivity extends AppCompatActivity{
    private Toolbar toolbar;
//    private GoogleMap mMap;
    private Button btnbooking;
    private ImageView imgteman;
    private TextView Tvprice;
    private EditText et_username, et_name, et_alamat, et_jamopsional,et_umur;
    private String location;
    private ImageView divClose;
    private Button btnProkesNext;
    private RelativeLayout divProkes;
    private String id;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_field);

        toolbar = findViewById(R.id.toolbar);
//        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        btnbooking = findViewById(R.id.btn_booking);
        imgteman = findViewById(R.id.detail_temans_img);
        et_username = findViewById(R.id.et_username);
        et_name = findViewById(R.id.et_name);
        Tvprice = findViewById(R.id.tv_price);
        et_alamat= findViewById(R.id.et_alamat);
        et_jamopsional= findViewById(R.id.et_jamopsional);
        et_umur= findViewById(R.id.et_umur);
        divClose = findViewById(R.id.divClose);
        btnProkesNext = findViewById(R.id.btnProkesNext);
        divProkes = findViewById(R.id.divProkes);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        mTitle.setText("Detail Teman");

//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        final Teman itemData = intent.getParcelableExtra("Item Data");

        final String idTeman = itemData.getId();
        Glide.with(this)
                .load(itemData.getPhoto())
                .into(imgteman);
        et_name.setText(itemData.getName());
        et_username.setText(itemData.getUsername());
        et_alamat.setText(itemData.getAddress());
        et_umur.setText(itemData.getUmur());
        Tvprice.setText(itemData.getPrice());
//        location = itemData.getLocation();
        et_jamopsional.setText(itemData.getOpen() + " - " + itemData.getClose());

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        if (!sharedPreferences.getString(USERNAME, "").isEmpty()){
            btnbooking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    divProkes.setVisibility(View.VISIBLE);
                }
            });
        }else{
            btnbooking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SweetAlertDialog(DetailFieldActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setContentText("Anda belum login, Silahkan login untuk akses fitur lebih")
                            .setConfirmText("Login")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    Intent intent = new Intent(DetailFieldActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .setCancelButton("Tidak", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }
            });
        }


        btnProkesNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailFieldActivity.this, BookingActivity.class);
                intent.putExtra("ID Teman", itemData);
                startActivity(intent);
                divProkes.setVisibility(View.GONE);
            }
        });

        divClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                divProkes.setVisibility(View.GONE);
            }
        });
    }


//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        String[] parts = location.split(",");
//        String part1 = parts[0];
//        String part2 = parts[1];
//
//        double latitude = Double.valueOf(part1);
//        double longitude = Double.valueOf(part2);
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(latitude, longitude);
//        mMap.addMarker(new MarkerOptions().position(sydney));
//        float zoomLevel = 16.0f;
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));
//    }

    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}

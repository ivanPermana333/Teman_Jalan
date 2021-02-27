package com.example.sportsclub.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sportsclub.R;
import com.example.sportsclub.model.Field;
import com.example.sportsclub.model.Teman;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import cn.pedant.SweetAlert.SweetAlertDialog;

//import static com.example.sportsclub.activities.SignInActivity.NAME;
import static com.example.sportsclub.activities.SignInActivity.USERNAME;
import static com.example.sportsclub.activities.SignInActivity.SHARED_PREFS;

public class DetailFieldActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Toolbar toolbar;
    private GoogleMap mMap;
    private Button btnbooking;
    private ImageView imgteman;
    private TextView tvTitle, tvPrice, tvAddress, tvJamOperasional;
    private String location;
    //gi

    private ImageView divClose;
    private Button btnProkesNext;
    private RelativeLayout divProkes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_field);

        toolbar = findViewById(R.id.toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        btnbooking = findViewById(R.id.btn_booking);
        imgteman = findViewById(R.id.detail_teman_img);
        tvTitle = findViewById(R.id.detail_teman_title);
        tvPrice = findViewById(R.id.item_teman_price);
        tvAddress = findViewById(R.id.item_teman_address);
        tvJamOperasional = findViewById(R.id.tvJamOperasional);
        divClose = findViewById(R.id.divClose);
        btnProkesNext = findViewById(R.id.btnProkesNext);
        divProkes = findViewById(R.id.divProkes);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitle.setText("Detail Teman");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        final Teman itemData = intent.getParcelableExtra("Item Data");

        final String idTeman = itemData.getId();
        Glide.with(this)
                .load(itemData.getPhoto())
                .into(imgteman);
        tvTitle.setText(itemData.getName());
        tvPrice.setText(itemData.getPrice());
        tvAddress.setText(itemData.getAddress());
        location = itemData.getLocation();
        tvJamOperasional.setText(itemData.getOpen() + " - " + itemData.getClose());

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
                intent.putExtra("ID Field", itemData);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        String[] parts = location.split(",");
        String part1 = parts[0];
        String part2 = parts[1];

        double latitude = Double.valueOf(part1);
        double longitude = Double.valueOf(part2);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(sydney));
        float zoomLevel = 16.0f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));
    }

    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}

package com.example.temanjalan.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.example.temanjalan.Constants;
import com.example.temanjalan.R;
import com.example.temanjalan.adapters.BookingAdapter;
import com.example.temanjalan.model.Booking;
import com.example.temanjalan.model.Teman;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;

import static com.example.temanjalan.activities.SignInActivity.ID;
import static com.example.temanjalan.activities.SignInActivity.SHARED_PREFS;

public class DetailHistoryActivity extends AppCompatActivity {
    public static final String TAG = DetailHistoryActivity.class.getSimpleName();
    private TextView tvTotalPrice,tvname, tvtime,tvdate,tvteman,tvstatus,tvcode;
    private Button btnupload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history);


//        tvname = findViewById(R.id.tv_user);
        tvTotalPrice = findViewById(R.id.tv_total_harga);
        tvtime = findViewById(R.id.tv_time);
        tvteman = findViewById(R.id.tv_teman);
        tvdate = findViewById(R.id.tv_date);
        tvstatus = findViewById(R.id.tv_status);
        tvcode = findViewById(R.id.tv_code);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String id = sharedPreferences.getString(ID, "");

//        fetchUserBookings(id);
        Intent intent = getIntent();
        final Booking data = intent.getParcelableExtra("EXTRA_CUSTOMER");

        final String idBooking = data.getIdUser();
//        tvname.setText(itemData.getUsername());
        tvteman.setText(data.getTeman());
        tvdate.setText(data.getDate());
        tvstatus.setText(data.getStatus());
        tvTotalPrice.setText(data.getTotalPrice());
        tvTotalPrice.setText(data.getCode());
//        Log.d("IVN", "onError: " + data.getTotalPrice());
//        location = itemData.getLocation();
        tvtime.setText(data.getTime());

        btnupload = findViewById(R.id.btn_upload);
        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://192.168.43.92:8000/bukti_pembayaran/" + idBooking ;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }

}
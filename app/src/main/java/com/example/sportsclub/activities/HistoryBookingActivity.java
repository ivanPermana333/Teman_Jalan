package com.example.sportsclub.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.sportsclub.Constants;
import com.example.sportsclub.R;
import com.example.sportsclub.adapters.BookingAdapter;
import com.example.sportsclub.fragments.BookingFragment;
import com.example.sportsclub.model.Booking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.sportsclub.activities.SignInActivity.ID;
import static com.example.sportsclub.activities.SignInActivity.SHARED_PREFS;

public class HistoryBookingActivity extends AppCompatActivity {
    public static final String TAG = HistoryBookingActivity.class.getSimpleName();
    private Toolbar toolbar;
    private RecyclerView rvBooking;
    private TextView mTitle;
    private ArrayList<Booking> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_booking);
        binding();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        String title = getIntent().getStringExtra("Title");
        mTitle.setText(title);
        rvBooking.setHasFixedSize(true);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String id = sharedPreferences.getString(ID, "");

        fetchUserBookings(id);
    }

    private void binding(){
        toolbar = findViewById(R.id.toolbar);
        rvBooking = findViewById(R.id.rv_booking);
        mTitle = toolbar.findViewById(R.id.toolbar_title);
    }

    public void showRecyclerList() {
        rvBooking.setLayoutManager(new LinearLayoutManager(this));
        BookingAdapter bookingAdapter = new BookingAdapter(list);
        rvBooking.setAdapter(bookingAdapter);
    }

    public void fetchUserBookings(String id) {
        AndroidNetworking.get(Constants.BASE_URL + "/api/bookings/{id}")
                .addPathParameter("id", id)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("data");
                            System.out.println("Data : " + data);
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject item = data.getJSONObject(i);
                                JSONObject field = item.getJSONObject("field");
                                Booking booking = new Booking();
                                booking.setStatus(item.getString("status"));
                                booking.setDate(item.getString("date"));
                                booking.setField(field.getString("name"));
                                booking.setTotalPrice(item.getString("total_price"));
                                String realTime1 = item.getString("time").replace("[", "");
                                String realTime2 = realTime1.replace("]", "");
                                String realTime3 = realTime2.replace("\"", "");
                                String[] ary = realTime3.split(",");
                                String realTime4 = realTime3.replace(",", " - ");
                                if (ary.length > 1){
                                    realTime4 = ary[0] + " - " + ary[ary.length - 1];
                                }
                                booking.setTime(realTime4);
                                booking.setCode(item.getString("code"));
                                list.add(booking);
                                System.out.println("index cek " + ary[0] + ary[ary.length - 1]);
                            }
                            showRecyclerList();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "onError: " + anError.getLocalizedMessage());
                    }
                });
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
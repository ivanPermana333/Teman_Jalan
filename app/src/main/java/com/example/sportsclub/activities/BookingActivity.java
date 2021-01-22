package com.example.sportsclub.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsclub.Constants;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.sportsclub.R;
import com.example.sportsclub.adapters.TimeAdapter;
import com.example.sportsclub.model.Time;
import com.example.sportsclub.model.Field;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.sportsclub.activities.SignInActivity.ID;
import static com.example.sportsclub.activities.SignInActivity.SHARED_PREFS;

public class BookingActivity extends AppCompatActivity {
    public static final String TAG = BookingActivity.class.getSimpleName();
    private Toolbar toolbar;
    private RecyclerView rvTime;
    private ArrayList<Time> list = new ArrayList<>();
    private TextView mTitle, tvTotalPrice;
    private Button btnbook;
    private CalendarView calendarView;
    private String date, idField, open, close, text, id;
    private int countSelected, price, totalPrice;
    private String time[], bookedTime[], bookingTime[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        toolbar = findViewById(R.id.toolbar);
        mTitle = toolbar.findViewById(R.id.toolbar_title);
        rvTime = findViewById(R.id.rv_Time);
        btnbook = findViewById(R.id.btn_book);
        calendarView = findViewById(R.id.calendar_view);
        tvTotalPrice = findViewById(R.id.tv_total_price);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitle.setText("Booking Field");

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        id = sharedPreferences.getString(ID, "");

        btnbook.setEnabled(false);
        btnbook.setBackgroundColor(Color.WHITE);

        Intent intent = getIntent();
        Field itemData = intent.getParcelableExtra("ID Field");

        idField = itemData.getId();
        open = itemData.getOpen();
        close = itemData.getClose();
        price = Integer.parseInt(itemData.getPrice());

        generateTime();

        /*for (String a : test) {
            System.out.println("Jam Booking : " + a);
            for (String b : time) {
                System.out.println("Jam Operasional : " + b);
                if (a.equals(b)) {
                    System.out.println("Sudah Terbooking : " + a);

                }
            }
        }*/

        btnbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONArray json_array = new JSONArray();
                for (int i = 0; i < bookingTime.length; i++) {
                    json_array.put(bookingTime[i]);
                }

                System.out.println("Data send : " + id + " " + idField + " " + date + " " + json_array.length() + " " + totalPrice);
                AndroidNetworking.post(Constants.BASE_URL + "/api/booking")
                        .addBodyParameter("user_id", id)
                        .addBodyParameter("field_id", idField)
                        .addBodyParameter("date", date)
                        .addBodyParameter("time", json_array.toString())
                        .addBodyParameter("total_price", String.valueOf(totalPrice))
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // do anything with response
                                try {
                                    String status = response.getString("status");
                                    Log.d(TAG, "onResponse: " + response);
                                    if (status.equals("success")) {
                                        Toast.makeText(BookingActivity.this, "Sukses booking", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(BookingActivity.this, MainDashboardActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(ANError error) {
                                // handle error
                                Log.e(TAG, "onError: " + error.getLocalizedMessage());
                            }
                        });
            }
        });

        rvTime.setHasFixedSize(true);

        System.out.println("Open : " + open + " Close : " + close);

        if (date == null) {
            date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            fetchUnavailableTime(idField, date);
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = year + "-" + (month + 1) + "-" + dayOfMonth;
                fetchUnavailableTime(idField, date);
                System.out.println("Date :" + date);
            }
        });
    }

    private void showRecylerTime() {
        rvTime.setLayoutManager(new GridLayoutManager(this, 4));
        TimeAdapter timeAdapter = new TimeAdapter(list);
        rvTime.setAdapter(timeAdapter);

        timeAdapter.setOnItemClickCallback(new TimeAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Time data) {
                showSelectedTime(data);
            }
        });
    }

    private void showSelectedTime(Time time) {
//        Toast.makeText(this, time.getTime(), Toast.LENGTH_SHORT).show();
        getTimeSelected();
        totalPrice = bookingTime.length * price;
        tvTotalPrice.setText(String.valueOf(totalPrice));

        if (bookingTime.length > 0) {
            btnbook.setEnabled(true);
            btnbook.setBackground(getDrawable(R.drawable.roundedbutton));
        } else {
            btnbook.setEnabled(false);
            btnbook.setBackgroundColor(Color.WHITE);
        }
    }

    public void fetchUnavailableTime(String id, final String date) {
        AndroidNetworking.get(Constants.BASE_URL + "/api/unavailable/{id}/{date}")
                .addPathParameter("id", id)
                .addPathParameter("date", date)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            list.clear();
                            generateTime();
                            JSONArray data = response.getJSONArray("data");

                            bookedTime = jsonArrayToStringArray(data);

                            for (String a : bookedTime) {
                                System.out.println("Jam Booked : " + a);
                            }

                            for (String b : time) {
                                System.out.println("Jam Operasional : " + b);
                            }

                            for (int i = 0; i < bookedTime.length; i++) {
                                for (int j = 0; j < time.length; j++) {
                                    if (bookedTime[i].equals(time[j])) {
                                        Time time = list.get(j);
                                        time.setClickable(false);
                                    }
                                }
                            }
                            showRecylerTime();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    public String[] jsonArrayToStringArray(JSONArray jsonArray) {
        int arraySize = jsonArray.length();
        String[] stringArray = new String[arraySize];

        for (int i = 0; i < arraySize; i++) {
            try {
                stringArray[i] = (String) jsonArray.get(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return stringArray;
    }

    public void generateTime() {
        open = open.replaceAll("[^1-9]+", " ");
        close = close.replaceAll("[^1-9]+", " ");
        int mOpen = Integer.parseInt(open.trim());
        int mClose = Integer.parseInt(close.trim());

        String exampleTime = "07:00";
        int hour = mClose - mOpen + 1;
        time = new String[hour];
        int position = 0;
        for (int i = mOpen; i <= mClose; i++) {
            if (position < hour) {
                if (i < 10) {
                    time[position] = exampleTime.replace("7", Integer.toString(i));
                } else {
                    time[position] = exampleTime.replace("07", Integer.toString(i));
                }
            }
            position++;
            System.out.println("Open : " + i);
        }

        for (int i = 0; i < time.length; i++) {
            Time time = new Time();
            time.setTime(this.time[i]);
            list.add(time);
        }
    }

    public void getTimeSelected() {
        text = "";
        countSelected = 0;
        for (Time availableTim : list) {
            if (availableTim.isSelected()) {
                text += availableTim.getTime() + " ";
                countSelected++;
            }
        }

        Log.d("TAG", "Output : " + text);

        bookingTime = new String[countSelected];
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            Time item = list.get(i);
            if (item.isSelected()) {
                bookingTime[count] = item.getTime();
                count++;
            }
        }
        System.out.println("testtt " + bookingTime.length);
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

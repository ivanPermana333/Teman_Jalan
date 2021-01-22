package com.example.sportsclub.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.sportsclub.Constants;
import com.example.sportsclub.R;
import com.example.sportsclub.activities.BookingActivity;
import com.example.sportsclub.adapters.BookingAdapter;
import com.example.sportsclub.model.Booking;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.sportsclub.activities.SignInActivity.ID;
import static com.example.sportsclub.activities.SignInActivity.SHARED_PREFS;

public class BookingFragment extends Fragment {
    public static final String TAG = BookingFragment.class.getSimpleName();
    private RecyclerView rvBooking;
    private ArrayList<Booking> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);
        rvBooking = view.findViewById(R.id.rv_booking);
        rvBooking.setHasFixedSize(true);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String id = sharedPreferences.getString(ID, "");

        fetchUserBookings(id);
        return view;
    }

    public void showRecyclerList() {
        rvBooking.setLayoutManager(new LinearLayoutManager(getContext()));
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
                                booking.setTotalPrice(field.getString("total_price"));
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
}

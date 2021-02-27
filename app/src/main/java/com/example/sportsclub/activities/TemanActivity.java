package com.example.sportsclub.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.sportsclub.Constants;
import com.example.sportsclub.R;
import com.example.sportsclub.adapters.FieldAdapter;
import com.example.sportsclub.adapters.TemanAdapter;
import com.example.sportsclub.model.Field;
import com.example.sportsclub.model.Teman;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TemanActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView rvTeman;
    private ArrayList<Teman> list = new ArrayList<>();
    private TemanAdapter temanAdapter;
    private TextView mTitle;
    private ShimmerFrameLayout mShimmerViewContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teman);
        rvTeman = findViewById(R.id.rv_Temans);
        toolbar = findViewById(R.id.toolbar);
        mTitle = toolbar.findViewById(R.id.toolbar_title);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

        String title = getIntent().getStringExtra("Title");
        mTitle.setText(title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        rvTeman.setHasFixedSize(true);

        fetchFields(title);

        showRecycler();
    }

    private void showRecycler() {
        rvTeman.setLayoutManager(new LinearLayoutManager(this));
        temanAdapter = new TemanAdapter(list);
        rvTeman.setAdapter(temanAdapter);

        temanAdapter.setOnItemClickCallback(new TemanAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Teman data) {
                showSelectedTeman(data);
            }
        });
    }

    private void showSelectedTeman(Teman teman) {
//        Toast.makeText(this, "Kamu memilih " + field.getName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DetailFieldActivity.class);
        intent.putExtra("test", list);
        intent.putExtra("Item Data", teman);
        startActivity(intent);
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void fetchFields(String category) {
        AndroidNetworking.get(Constants.BASE_URL + "/api/fields/{category}")
                .addPathParameter("category", category)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("data");
                            System.out.println("Debug : " + data);
                            for (int position = 0; position < data.length(); position++) {
                                JSONObject teman = data.getJSONObject(position);
                                Teman item = new Teman();
                                item.setId(teman.getString("id"));
                                item.setName(teman.getString("name"));
                                item.setAddress(teman.getString("address"));
                                item.setPrice(teman.getString("price"));
                                item.setPhoto(Constants.BASE_URL + "/storage/" + teman.getString("picture"));
                                item.setLocation(teman.getString("location"));
                                item.setOpen(teman.getString("open"));
                                item.setClose(teman.getString("close"));
                                list.add(item);
                            }
                            temanAdapter.notifyDataSetChanged();

                            mShimmerViewContainer.stopShimmer();
                            mShimmerViewContainer.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmer();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmer();
        super.onPause();
    }

}
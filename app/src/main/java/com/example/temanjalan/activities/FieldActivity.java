package com.example.temanjalan.activities;

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
import com.example.temanjalan.Constants;
import com.example.temanjalan.R;
import com.example.temanjalan.adapters.FieldAdapter;
import com.example.temanjalan.model.Field;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FieldActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView rvField;
    private ArrayList<Field> list = new ArrayList<>();
    private FieldAdapter fieldAdapter;
    private TextView mTitle;
    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field);

        rvField = findViewById(R.id.rv_Temans);
        toolbar = findViewById(R.id.toolbar);
        mTitle = toolbar.findViewById(R.id.toolbar_title);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

        String title = getIntent().getStringExtra("Title");
        mTitle.setText(title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        rvField.setHasFixedSize(true);

        fetchFields(title);

        showRecycler();
    }

    private void showRecycler() {
        rvField.setLayoutManager(new LinearLayoutManager(this));
        fieldAdapter = new FieldAdapter(list);
        rvField.setAdapter(fieldAdapter);

        fieldAdapter.setOnItemClickCallback(new FieldAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Field data) {
                showSelectedField(data);
            }
        });
    }

    private void showSelectedField(Field field) {
//        Toast.makeText(this, "Kamu memilih " + field.getName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DetailFieldActivity.class);
        intent.putExtra("test", list);
        intent.putExtra("Item Data", field);
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
                                JSONObject field = data.getJSONObject(position);
                                Field item = new Field();
                                item.setId(field.getString("id"));
                                item.setName(field.getString("name"));
                                item.setAddress(field.getString("address"));
                                item.setPrice(field.getString("price"));
                                item.setPhoto(Constants.BASE_URL + "/storage/" + field.getString("picture"));
                                item.setLocation(field.getString("location"));
                                item.setOpen(field.getString("open"));
                                item.setClose(field.getString("close"));
                                list.add(item);
                            }
                            fieldAdapter.notifyDataSetChanged();

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

package com.example.temanjalan.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.temanjalan.Constants;
import com.example.temanjalan.R;
import com.example.temanjalan.adapters.TemanAdapter;
import com.example.temanjalan.model.Teman;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static xdroid.toaster.Toaster.toast;

public class SeeAllActivity extends AppCompatActivity {
    private ShimmerFrameLayout mShimmerViewContainer;
    private RecyclerView rv_Temans;
    private String sResultByTop = "";
    private TextView  tvResultFriends, tvName, tvEmail, tvCondition;
    private ImageView ivReload;
    private String sResultAll = "";



    private ArrayList<Teman> list = new ArrayList<>();
    private TemanAdapter temanAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all);
        binding();

        sResultAll = Constants.CATEGORY_All;
        tvResultFriends.setText(sResultAll);

        fetchFriends(sResultAll);

        ivReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShimmerViewContainer.startShimmer();
                mShimmerViewContainer.setVisibility(View.VISIBLE);
                rv_Temans.setVisibility(View.GONE);
                tvCondition.setVisibility(View.GONE);
                ivReload.setVisibility(View.GONE);
                fetchFriends(sResultAll);
            }
        });

        rv_Temans.setHasFixedSize(true);
        rv_Temans.setLayoutManager(new LinearLayoutManager(this));
        rv_Temans.setNestedScrollingEnabled(false);
    }

    private void binding() {
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvResultFriends = findViewById(R.id.tvResult);
        tvCondition = findViewById(R.id.tvCondition);
        ivReload = findViewById(R.id.ivReload);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        rv_Temans = findViewById(R.id.rv_Temans);
    }
    private void Result (String Result){
        sResultAll = Result;
        tvResultFriends.setText(Result);
        fetchFriends(Result);
    }
    private void showRecycler() {
        rv_Temans.setLayoutManager(new LinearLayoutManager(this));
        temanAdapter = new TemanAdapter(list);
        rv_Temans.setAdapter(temanAdapter);

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

    private void fetchFriends(String sResultByTop) {
        AndroidNetworking.get(Constants.BASE_URL + "/api/temans")
//                .addPathParameter("category", category)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (temanAdapter != null) {
                                temanAdapter.clearData();
                                temanAdapter.notifyDataSetChanged();
                            }
                            if (list != null)  list.clear();

                            JSONArray data = response.getJSONArray("data");
                            if(data == null) {
                                toast("Tidak ada data");
                                return;
                            }
                            System.out.println("Debug : " + data);
                            if (data.length() <= 0){
                                tvCondition.setVisibility(View.VISIBLE);
                                tvCondition.setText(Constants.TEXT_LOAD_NOL);
                                ivReload.setVisibility(View.GONE);
                                rv_Temans.setVisibility(View.GONE);
                                mShimmerViewContainer.stopShimmer();
                                mShimmerViewContainer.setVisibility(View.GONE);
                            }else{
                                for (int position = 0; position < data.length(); position++) {
                                    JSONObject teman = data.getJSONObject(position);
                                    Teman item = new Teman();
                                    item.setId(teman.getString("id"));
                                    item.setName(teman.getString("name"));
                                    item.setUmur(teman.getString("umur"));
                                    item.setAddress(teman.getString("address"));
                                    item.setUsername(teman.getString("username"));
                                    item.setPrice(teman.getString("price"));
                                    item.setPhoto(Constants.BASE_URL + "/storage/" + teman.getString("picture"));
                                    item.setLocation(teman.getString("location"));
                                    item.setOpen(teman.getString("open"));
                                    item.setClose(teman.getString("close"));
                                    list.add(item);
                                }
                                showRecycler();

                                mShimmerViewContainer.stopShimmer();
                                mShimmerViewContainer.setVisibility(View.GONE);
                                rv_Temans.setVisibility(View.VISIBLE);
                                tvCondition.setVisibility(View.GONE);
                                ivReload.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        rv_Temans.setVisibility(View.GONE);
                        tvCondition.setVisibility(View.VISIBLE);
                        tvCondition.setText(Constants.TEXT_GAGAL_MEMUAT);
                        ivReload.setVisibility(View.VISIBLE);
                        Log.d("RBA", "onError: " + anError.getErrorBody());
                        Log.d("RBA", "onError: " + anError.getLocalizedMessage());
                        Log.d("RBA", "onError: " + anError.getErrorDetail());
                        Log.d("RBA", "onError: " + anError.getResponse());
                        Log.d("RBA", "onError: " + anError.getErrorCode());
                    }
                });

    }
}
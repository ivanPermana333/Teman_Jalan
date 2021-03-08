package com.example.temanjalan.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.example.temanjalan.activities.SignInActivity.EMAIL;
//import static com.example.sportsclub.activities.SignInActivity.NAME;
import static com.example.temanjalan.activities.SignInActivity.USERNAME;
import static com.example.temanjalan.activities.SignInActivity.SHARED_PREFS;
import static xdroid.toaster.Toaster.toast;

public class MainDashboardActivity extends AppCompatActivity {
    private ImageButton imgbtn;
    private Button divBooking;
    private TextView tvDateToday, tvResultFriends, tvName, tvEmail, tvCondition, see_all;
    private LinearLayout divAllMatch, divMyMatch,  divFutsal, divBuluTangkis, divVolley;
//    private CircleImageView ivFutsal, ivBuluTangkis, ivVolley;
    private ImageView ivReload, divProfile;
    private ShimmerFrameLayout mShimmerViewContainer;
    private RecyclerView rv_Temans;
    private String sResultByTop = "";

    private ArrayList<Teman> list = new ArrayList<>();
    private TemanAdapter temanAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);
        binding();

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        if (!sharedPreferences.getString(USERNAME, "").isEmpty()){
            tvName.setText(sharedPreferences.getString(USERNAME, ""));
            tvEmail.setText(sharedPreferences.getString(EMAIL, ""));
        }else {
            tvName.setText("Anda belum login, Silahkan login untuk akses fitur lebih, Klik untuk login");
            divProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertNotLogin();
                }
            });
            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertNotLogin();
                }
            });
//            divAllMatch.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    alertNotLogin();
//                }
//            });
//            divMyMatch.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    alertNotLogin();
//                }
//            });
            divBooking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertNotLogin();
                }
            });
        }



        sResultByTop = Constants.CATEGORY_TOP_5;
        tvResultFriends.setText(sResultByTop);

        fetchFriends(sResultByTop);

        ivReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShimmerViewContainer.startShimmer();
                mShimmerViewContainer.setVisibility(View.VISIBLE);
                rv_Temans.setVisibility(View.GONE);
                tvCondition.setVisibility(View.GONE);
                ivReload.setVisibility(View.GONE);
                fetchFriends(sResultByTop);
            }
        });

        rv_Temans.setHasFixedSize(true);
        rv_Temans.setLayoutManager(new LinearLayoutManager(this));
        rv_Temans.setNestedScrollingEnabled(false);

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
                                for (int position = 0; position < 5; position++) {
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

    private void alertNotLogin(){
        new SweetAlertDialog(MainDashboardActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setContentText("Anda belum login, Silahkan login untuk akses fitur lebih")
                .setConfirmText("Login")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        Intent intent = new Intent(MainDashboardActivity.this, MainActivity.class);
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

    private void binding (){
//        tvDateToday = findViewById(R.id.tvDateToday);
//        tvDateToday.setText(new SimpleDateFormat("EEE, dd-MM-yyyy", Locale.getDefault()).format(new Date()));
//        ivFutsal = findViewById(R.id.ivFutsal);
//        Glide.with(this).load(R.drawable.item_photo2).into(ivFutsal);
//        ivBuluTangkis = findViewById(R.id.ivBuluTangkis);
//        Glide.with(this).load(R.drawable.coming_soon).into(ivBuluTangkis);
//        ivVolley = findViewById(R.id.ivVolley);
//        Glide.with(this).load(R.drawable.coming_soon).into(ivVolley);

//        divAllMatch = findViewById(R.id.divAllMatch);
//        divAllMatch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainDashboardActivity.this, AllMatchActivity.class);
//                startActivity(intent);
//            }
//        });
//        divMyMatch = findViewById(R.id.divMyMatch);
//        divMyMatch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainDashboardActivity.this, MyMatchActivity.class);
//                startActivity(intent);
//            }
//        });
        divProfile = findViewById(R.id.divProfile);
        divProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainDashboardActivity.this, ProfileActivity.class);
                intent.putExtra("Title", "Profile");
                startActivity(intent);
            }
        });

        divBooking = findViewById(R.id.divHistory);
        divBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainDashboardActivity.this, HistoryBookingActivity.class);
                intent.putExtra("Title", "Booking");
                startActivity(intent);
            }
        });

//        divFutsal = findViewById(R.id.divFutsal);
//        divFutsal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Result(Constants.CATEGORY_TOP_5);
//            }
//        });
//        divBuluTangkis = findViewById(R.id.divBuluTangkis);
//        divBuluTangkis.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                categoryBadminton(Constants.CATEGORY_BADMINTON);
//            }
//        });
//        divVolley = findViewById(R.id.divVolley);
//        divVolley.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                categoryVolley(Constants.CATEGORY_VOLLEY);
//            }
//        });


        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvResultFriends = findViewById(R.id.tvResult);
        tvCondition = findViewById(R.id.tvCondition);
        ivReload = findViewById(R.id.ivReload);
        see_all = findViewById(R.id.see_all);
        see_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getBaseContext(), "nearby comingsoon", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainDashboardActivity.this, SeeAllActivity.class);
                startActivity(intent);
            }
        });
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        rv_Temans = findViewById(R.id.rv_Temans);


//        ImageButton imgbtn = findViewById(R.id.btnsearch);

    }

    private void Result (String Result){
        sResultByTop = Result;
        tvResultFriends.setText(Result);
        fetchFriends(Result);
    }

//    private void categoryVolley (String category){
////        sResultByCategory = category;
////        tvResultCategory.setText(category);
////        fetchFields(category);
//        toast("Fitur sedang dalam pengembangan");
//    }
//
//    private void categoryBadminton (String category){
////        sResultByCategory = category;
////        tvResultCategory.setText(category);
////        fetchFields(category);
//        toast("Fitur sedang dalam pengembangan");
//
//    }

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
}
package com.example.sportsclub.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.sportsclub.Constants;
import com.example.sportsclub.R;
import com.example.sportsclub.adapters.DatingAdapter;
import com.example.sportsclub.adapters.MatchAdapter;
import com.example.sportsclub.model.Dating;
import com.example.sportsclub.model.Match;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.sportsclub.activities.SignInActivity.SHARED_PREFS;

public class MyDatingActivity extends AppCompatActivity {
    public static final String TAG = MyMatchActivity.class.getSimpleName();
    public static final String ID = "id";
    private String id;
    private Toolbar toolbar;
    private TextView mTitle;
    private RecyclerView rvDating;
    private FloatingActionButton fabDating;
    private ArrayList<Dating> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dating);
        binding();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitle.setText("My Match");

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        id = sharedPreferences.getString(ID, "");

        fetchDating(id);

        fabDating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyDatingActivity.this, RequestActivity.class);
                intent.putExtra("Title", "Request Match");
                startActivity(intent);
            }
        });
    }

    private void binding(){
        toolbar = findViewById(R.id.toolbar);
        mTitle = toolbar.findViewById(R.id.toolbar_title);
        rvDating = findViewById(R.id.rv_dating);
        fabDating = findViewById(R.id.fab_dating);
    }

    public void showRecyclerView() {
        rvDating.setLayoutManager(new LinearLayoutManager(this));
        DatingAdapter datingAdapter = new DatingAdapter(list);
        rvDating.setAdapter(datingAdapter);

        datingAdapter.setOnItemClickCallback(new DatingAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Dating data) {
                if (id.equals(data.getIdUser())){
                    return;
                }else {
                    showSelectedDating(data);
                }
            }
        });
    }

    public void fetchDating(String idUser) {
        AndroidNetworking.get(Constants.BASE_URL + "/api/mymatch/{id}")
                .addPathParameter("id", idUser)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("data");
                            System.out.println("Isiiii " + data);
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject item = data.getJSONObject(i);
                                Dating dating = new Dating();
                                dating.setIdMatch(item.getString("id"));
                                dating.setStatus(item.getString("status"));
                                dating.setDate(item.getString("date"));
                                dating.setTeamReq(item.getString("teamReq"));
                                dating.setTeamAcc(item.getString("teamAcc"));

                                JSONObject field = item.getJSONObject("field");
                                dating.setField(field.getString("name"));

                                JSONObject user = item.getJSONObject("user");
                                dating.setIdUser(user.getString("id"));
                                dating.setName(user.getString("name"));
                                dating.setPhone(user.getString("phone"));
                                list.add(dating);
                            }
                            showRecyclerView();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        if (error.getErrorCode() != 0) {
                            // received error from server
                            // error.getErrorCode() - the error code from server
                            // error.getErrorBody() - the error body from server
                            // error.getErrorDetail() - just an error detail
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                            // get parsed error object (If ApiError is your class)
                            // ApiError apiError = error.getErrorAsObject(ApiError.class);
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });
    }

    public void showSelectedDating(Dating dating) {
        Toast.makeText(this, dating.getName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MyDatingActivity.this, AcceptActivity.class);
        intent.putExtra(ID, dating.getIdMatch());
        startActivity(intent);
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
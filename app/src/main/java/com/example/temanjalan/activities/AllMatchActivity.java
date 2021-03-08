package com.example.temanjalan.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.temanjalan.Constants;
import com.example.temanjalan.R;
import com.example.temanjalan.adapters.MatchAdapter;
import com.example.temanjalan.model.Match;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.temanjalan.activities.SignInActivity.SHARED_PREFS;

public class AllMatchActivity extends AppCompatActivity {
    public static final String TAG = AllMatchActivity.class.getSimpleName();
    public static final String ID = "id";
    private Toolbar toolbar;
    private TextView mTitle;
    private RecyclerView rvMatch;
    private ArrayList<Match> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_match);
        binding();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitle.setText("All Match");

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String id = sharedPreferences.getString(ID, "");

        fetchMatch(id);
    }

    private void binding(){
        toolbar = findViewById(R.id.toolbar);
        mTitle = toolbar.findViewById(R.id.toolbar_title);
        rvMatch = findViewById(R.id.rv_match);
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void showRecyclerView() {
        rvMatch.setLayoutManager(new LinearLayoutManager(this));
        MatchAdapter matchAdapter = new MatchAdapter(list);
        rvMatch.setAdapter(matchAdapter);

        matchAdapter.setOnItemClickCallback(new MatchAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Match data) {
                showSelectedMatch(data);
            }
        });
    }

    public void fetchMatch(String idUser) {
        AndroidNetworking.get(Constants.BASE_URL + "/api/matchs/{id}")
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
                                Match match = new Match();
                                match.setIdMatch(item.getString("id"));
                                match.setStatus(item.getString("status"));
                                match.setDate(item.getString("date"));
                                match.setTeamReq(item.getString("teamReq"));
                                match.setTeamAcc(item.getString("teamAcc"));

                                JSONObject field = item.getJSONObject("field");
                                match.setField(field.getString("name"));

                                JSONObject user = item.getJSONObject("user");
                                match.setName(user.getString("name"));
                                match.setPhone(user.getString("phone"));
                                list.add(match);
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

    public void showSelectedMatch(Match match) {
        Intent intent = new Intent(AllMatchActivity.this, AcceptActivity.class);
        intent.putExtra(ID, match.getIdMatch());
        startActivity(intent);
    }
}
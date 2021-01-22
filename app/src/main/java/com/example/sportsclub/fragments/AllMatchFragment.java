package com.example.sportsclub.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.sportsclub.Constants;
import com.example.sportsclub.R;
import com.example.sportsclub.activities.AcceptActivity;
import com.example.sportsclub.activities.RequestActivity;
import com.example.sportsclub.adapters.MatchAdapter;
import com.example.sportsclub.model.Match;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.sportsclub.activities.SignInActivity.SHARED_PREFS;

public class AllMatchFragment extends Fragment {
    public static final String ID = "id";
    private RecyclerView rvMatch;
    private ArrayList<Match> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_match, container, false);
        rvMatch = view.findViewById(R.id.rv_match);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String id = sharedPreferences.getString(ID, "");

        fetchMatch(id);

        return view;
    }

    public void showRecyclerView() {
        rvMatch.setLayoutManager(new LinearLayoutManager(getContext()));
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
                    public void onError(ANError anError) {

                    }
                });
    }

    public void showSelectedMatch(Match match) {
//        Toast.makeText(getContext(), match.getName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), AcceptActivity.class);
        System.out.println("id flied : " + match.getIdMatch());
        intent.putExtra(ID, match.getIdMatch());
        startActivity(intent);
    }
}

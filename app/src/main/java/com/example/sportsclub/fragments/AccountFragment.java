package com.example.sportsclub.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.example.sportsclub.Constants;
import com.example.sportsclub.R;
import com.example.sportsclub.activities.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.sportsclub.activities.SignInActivity.ID;
import static com.example.sportsclub.activities.SignInActivity.SHARED_PREFS;

public class AccountFragment extends Fragment {
    private TextView tvName, tvUsername, tvEmail, tvPhoneNumber, tvAddress;
    private Button btnLogout;
    private ImageView imgAvatar;
    private String id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        tvName = view.findViewById(R.id.tv_name);
        tvUsername = view.findViewById(R.id.tv_username);
        tvEmail = view.findViewById(R.id.tv_email);
        tvPhoneNumber = view.findViewById(R.id.tv_phonenumber);
        tvAddress = view.findViewById(R.id.tv_address);
        btnLogout = view.findViewById(R.id.btn_logout);
        imgAvatar = view.findViewById(R.id.img_avatar);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        id = sharedPreferences.getString(ID, "");

        fetchProfile();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setMessage("Apakah anda yakin ingin keluar ?")
                        .setNegativeButton("Tidak", null)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply();
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }).create().show();
            }
        });

        return view;
    }

    public void fetchProfile() {
        AndroidNetworking.get(Constants.BASE_URL + "/api/profile/{id}")
                .addPathParameter("id", id)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            String name = data.getString("name");
                            String username = data.getString("username");
                            String email = data.getString("email");
                            String phoneNumber = data.getString("phone");
                            String address = data.getString("address");
                            String avatar = data.getString("avatar");
                            if (avatar != "null") {
                                avatar = Constants.BASE_URL + "/storage/" + avatar;
                            }

                            tvName.setText(name);
                            tvUsername.setText(username);
                            tvEmail.setText(email);
                            tvPhoneNumber.setText(phoneNumber);
                            tvAddress.setText(address);
                            if (avatar != "null") {
                                Glide.with(getActivity())
                                        .load(avatar)
                                        .into(imgAvatar);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}

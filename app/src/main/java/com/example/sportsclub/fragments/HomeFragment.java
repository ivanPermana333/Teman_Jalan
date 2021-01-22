package com.example.sportsclub.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.sportsclub.Constants;
import com.example.sportsclub.R;
import com.example.sportsclub.activities.FieldActivity;

public class HomeFragment extends Fragment {
    private CardView cardFutsal, cardVolly, cardBadmiton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        cardFutsal = view.findViewById(R.id.card_futsal);
        cardVolly = view.findViewById(R.id.card_volly);
        cardBadmiton = view.findViewById(R.id.card_badminton);

        cardFutsal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FieldActivity.class);
                intent.putExtra("Title", Constants.CATEGORY_FUTSAL);
                startActivity(intent);
            }
        });

        cardVolly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FieldActivity.class);
                intent.putExtra("Title", Constants.CATEGORY_VOLLEY);
                startActivity(intent);
            }
        });

        cardBadmiton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FieldActivity.class);
                intent.putExtra("Title", Constants.CATEGORY_BADMINTON);
                startActivity(intent);
            }
        });

        return view;
    }
}

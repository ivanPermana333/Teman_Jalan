package com.example.sportsclub.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsclub.R;
import com.example.sportsclub.model.Match;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {
    private ArrayList<Match> listMatch;
    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public MatchAdapter(ArrayList<Match> list) {
        this.listMatch = list;
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match, parent, false);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        Match match = listMatch.get(position);

        holder.tvStatus.setText(match.getStatus());
        holder.tvDate.setText(match.getDate());
        holder.tvField.setText(match.getField());
        holder.tvPhone.setText(match.getPhone());
        holder.tvName.setText(match.getName());
        holder.tvTeamReq.setText(match.getTeamReq());

        if (match.getTeamAcc() != ("null")) {
            holder.tvTeamAcc.setText(match.getTeamAcc());
            holder.tvStatus.setBackgroundResource(R.drawable.bg_green);
        }

        holder.tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumberWithCountryCode = "+62" + match.getPhone();
                String message = "Mari Bermain Futsal Dengan Tim Kami";

                v.getContext().startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(
                                String.format("https://api.whatsapp.com/send?phone=%s&text=%s",
                                        phoneNumberWithCountryCode, message))));
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (match.getTeamAcc() == "null") {
                    onItemClickCallback.onItemClicked(listMatch.get(holder.getAdapterPosition()));
                } else {
                    return;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listMatch.size();
    }

    public class MatchViewHolder extends RecyclerView.ViewHolder {
        private TextView tvStatus, tvDate, tvField, tvTeamReq, tvTeamAcc, tvPhone, tvName;

        public MatchViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvField = itemView.findViewById(R.id.tv_field);
            tvPhone = itemView.findViewById(R.id.tv_contact);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTeamReq = itemView.findViewById(R.id.tv_team_request);
            tvTeamAcc = itemView.findViewById(R.id.tv_team_accept);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Match data);
    }
}

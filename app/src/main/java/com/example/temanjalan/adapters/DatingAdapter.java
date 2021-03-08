package com.example.temanjalan.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.temanjalan.R;
import com.example.temanjalan.model.Dating;

import java.util.ArrayList;

public class DatingAdapter extends RecyclerView.Adapter<DatingAdapter.DatingViewHolder> {
    private ArrayList<Dating> listDating;
    private DatingAdapter.OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(DatingAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public DatingAdapter(ArrayList<Dating> list) {
        this.listDating = list;
    }

    @NonNull
    @Override
    public DatingAdapter.DatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match, parent, false);
        return new DatingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DatingAdapter.DatingViewHolder holder, int position) {
        Dating dating = listDating.get(position);

        holder.tvStatus.setText(dating.getStatus());
        holder.tvDate.setText(dating.getDate());
        holder.tvTeman.setText(dating.getTeman());
        holder.tvPhone.setText(dating.getPhone());
        holder.tvName.setText(dating.getName());
        holder.tvTeamReq.setText(dating.getTeamReq());

        if (dating.getTeamAcc() != ("null")) {
            holder.tvTeamAcc.setText(dating.getTeamAcc());
            holder.tvStatus.setBackgroundResource(R.drawable.bg_green);
        }

        holder.tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumberWithCountryCode = "+62" + dating.getPhone();
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
                if (dating.getTeamAcc() == "null") {
                    onItemClickCallback.onItemClicked(listDating.get(holder.getAdapterPosition()));
                } else {
                    return;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listDating.size();
    }

    public class DatingViewHolder extends RecyclerView.ViewHolder {
        private TextView tvStatus, tvDate, tvTeman, tvTeamReq, tvTeamAcc, tvPhone, tvName;

        public DatingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvTeman = itemView.findViewById(R.id.tv_teman);
//            tvPhone = itemView.findViewById(R.id.tv_contact);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTeamReq = itemView.findViewById(R.id.tv_team_request);
            tvTeamAcc = itemView.findViewById(R.id.tv_team_accept);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Dating data);
    }
}

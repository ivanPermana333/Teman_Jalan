package com.example.temanjalan.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.temanjalan.R;
import com.example.temanjalan.model.Teman;

import java.util.ArrayList;

public class TemanAdapter extends RecyclerView.Adapter<TemanAdapter.TemanViewHolder> {
    private ArrayList<Teman> listTeman;
    private TemanAdapter.OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(TemanAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public TemanAdapter(ArrayList<Teman> listTeman) {
        this.listTeman = listTeman;
    }

    @NonNull
    @Override
    public TemanAdapter.TemanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teman, parent, false);
        return new TemanAdapter.TemanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TemanAdapter.TemanViewHolder holder, final int position) {
        Teman teman = listTeman.get(position);
        Glide.with(holder.itemView.getContext())
                .load(teman.getPhoto())
                .into(holder.imgPhoto);
        holder.tvUsername.setText(teman.getUsername());
        holder.tvAddress.setText(teman.getAddress());
        holder.tvPrice.setText(teman.getPrice());
        Log.d("RBA", "onError: " +teman.getUsername());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listTeman.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTeman.size();
    }

//    public void clearData() {
//        int size = this.listTeman.size();
//        if (size > 0) {
//            for (int i = 0; i < size; i++) {
//                this.listTeman.remove(0);
//            }
//        }
//    }

    public class TemanViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView tvUsername, tvAddress, tvPrice;

        public TemanViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.item_teman_img);
            tvUsername = itemView.findViewById(R.id.item_teman_title);
            tvAddress = itemView.findViewById(R.id.item_teman_address);
            tvPrice = itemView.findViewById(R.id.item_teman_price);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Teman data);
    }
}

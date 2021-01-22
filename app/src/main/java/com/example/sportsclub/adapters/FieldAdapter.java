package com.example.sportsclub.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sportsclub.R;
import com.example.sportsclub.model.Field;

import java.util.ArrayList;

public class FieldAdapter extends RecyclerView.Adapter<FieldAdapter.FieldViewHolder> {
    private ArrayList<Field> listField;
    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public FieldAdapter(ArrayList<Field> listField) {
        this.listField = listField;
    }

    @NonNull
    @Override
    public FieldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_field, parent, false);
        return new FieldViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FieldViewHolder holder, final int position) {
        Field field = listField.get(position);
        Glide.with(holder.itemView.getContext())
                .load(field.getPhoto())
                .into(holder.imgPhoto);
        holder.tvName.setText(field.getName());
        holder.tvAddress.setText(field.getAddress());
        holder.tvPrice.setText(field.getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listField.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listField.size();
    }

    public void clearData() {
        int size = this.listField.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.listField.remove(0);
            }
        }
    }

    public class FieldViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView tvName, tvAddress, tvPrice;

        public FieldViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.item_field_img);
            tvName = itemView.findViewById(R.id.item_field_title);
            tvAddress = itemView.findViewById(R.id.item_field_address);
            tvPrice = itemView.findViewById(R.id.item_field_price);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Field data);
    }
}

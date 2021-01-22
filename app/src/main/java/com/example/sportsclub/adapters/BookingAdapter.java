package com.example.sportsclub.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsclub.R;
import com.example.sportsclub.model.Booking;

import java.util.ArrayList;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private ArrayList<Booking> listBooking;

    public BookingAdapter(ArrayList<Booking> list) {
        this.listBooking = list;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = listBooking.get(position);

        holder.tvStatus.setText(booking.getStatus());
        holder.tvDate.setText(booking.getDate());
        holder.tvField.setText(booking.getField());
        holder.tvTime.setText(booking.getTime());
        holder.tvTotalPrice.setText(booking.getTotalPrice());
        if(booking.getCode() != "null"){
            holder.tvCode.setText(booking.getCode());
            holder.tvStatus.setBackgroundResource(R.drawable.bg_green);
        }
        if(booking.getStatus().equals("REJECT")){
            holder.tvStatus.setBackgroundResource(R.drawable.bg_red);
        }
    }

    @Override
    public int getItemCount() {
        return listBooking.size();
    }

    public class BookingViewHolder extends RecyclerView.ViewHolder {
        private TextView tvStatus, tvDate, tvField, tvTime, tvCode, tvTotalPrice;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvField = itemView.findViewById(R.id.tv_field);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvCode = itemView.findViewById(R.id.tv_code);
            tvTotalPrice = itemView.findViewById(R.id.tv_total_harga);
        }
    }
}

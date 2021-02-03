package com.example.sportsclub.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsclub.R;
import com.example.sportsclub.activities.DetailHistoryActivity;
import com.example.sportsclub.model.Booking;

import java.util.ArrayList;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private ArrayList<Booking> listBooking;
//    private OnItemClickCallback onItemClickCallback;
        private Context mCtx;

//    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
//        this.onItemClickCallback = onItemClickCallback;
//    }

    public BookingAdapter(Context mCtx, ArrayList<Booking> list) {
        this.listBooking = list;
        this.mCtx = mCtx;
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
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (booking.getStatus() == "null") {
//                    onItemClickCallback.onItemClicked(listBooking.get(holder.getAdapterPosition()));
//                } else {
//                    return;
//                }
//            }
//        });
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mCtx, DetailHistoryActivity.class);
                    intent.putExtra("EXTRA_CUSTOMER", listBooking.get(getAdapterPosition()));
                    mCtx.startActivity(intent);
                }
            });
        }
    }
//    public interface OnItemClickCallback {
//        void onItemClicked(Booking data);
//    }
}

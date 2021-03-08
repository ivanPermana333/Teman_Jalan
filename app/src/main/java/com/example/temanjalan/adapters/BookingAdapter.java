package com.example.temanjalan.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.temanjalan.R;
import com.example.temanjalan.model.Booking;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.example.temanjalan.activities.SignInActivity.SHARED_PREFS;
import static com.example.temanjalan.activities.SignInActivity.USERNAME;

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
        holder.tvTeman.setText(booking.getTeman());
        holder.tvTime.setText(booking.getTime());
        holder.tvTotalPrice.setText(booking.getTotalPrice());

        SharedPreferences pref = mCtx.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String userObject = pref.getString(USERNAME, "");
        holder.tvName.setText(userObject);


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
        private TextView tvStatus, tvDate, tvTeman, tvTime, tvCode, tvTotalPrice, tvName;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvTeman = itemView.findViewById(R.id.tv_teman);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvCode = itemView.findViewById(R.id.tv_code);
            tvTotalPrice = itemView.findViewById(R.id.tv_total_harga);
            tvName = itemView.findViewById(R.id.tvName  );

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(mCtx, DetailHistoryActivity.class);
//                    intent.putExtra("EXTRA_CUSTOMER", listBooking.get(getAdapterPosition()));
//                    mCtx.startActivity(intent);
//                }
//            });
        }
    }
//    public interface OnItemClickCallback {
//        void onItemClicked(Booking data);
//    }
}

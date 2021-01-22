package com.example.sportsclub.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsclub.R;
import com.example.sportsclub.model.Time;

import java.util.ArrayList;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.TimeViewHolder> {
    private ArrayList<Time> listTime;
    private OnItemClickCallback onItemClickCallback;
    private boolean isFirstSelected = true;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public TimeAdapter(ArrayList<Time> listTime) {
        this.listTime = listTime;
    }

    @NonNull
    @Override
    public TimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time, parent, false);
        return new TimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TimeViewHolder holder, final int position) {
        final Time time = listTime.get(position);

        holder.itemTime.setText(time.getTime());

        if (!time.isClickable()) {
            holder.itemTime.setTextColor(Color.parseColor("#EBEBE4"));
        }

        holder.itemTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!time.isClickable()) return;

                if (position == 0) {
                    Time kanan = listTime.get(position + 1);
                    if (!kanan.isSelected() && isFirstSelected) {
                        isFirstSelected = false;
                        time.setSelected(true);
                    } else if (kanan.isSelected()) {
                        time.setSelected(!time.isSelected());
                    } else if (time.isSelected() && !kanan.isSelected()) {
                        isFirstSelected = true;
                        time.setSelected(false);
                    }
                } else if (position == listTime.size() - 1) {
                    Time kiri = listTime.get(position - 1);
                    if (!kiri.isSelected() && isFirstSelected) {
                        isFirstSelected = false;
                        time.setSelected(true);
                    } else if (kiri.isSelected()) {
                        time.setSelected(!time.isSelected());
                    } else if (time.isSelected() && !kiri.isSelected()) {
                        isFirstSelected = true;
                        time.setSelected(false);
                    }
                } else {
                    Time kanan = listTime.get(position + 1);
                    Time kiri = listTime.get(position - 1);
                    if (!kanan.isSelected() && !kiri.isSelected() && isFirstSelected) {
                        isFirstSelected = false;
                        time.setSelected(true);
                    } else if (kanan.isSelected() && kiri.isSelected()) {
                        return;
                    } else if (kanan.isSelected() || kiri.isSelected()) {
                        time.setSelected(!time.isSelected());
                    } else if (time.isSelected() && !kanan.isSelected() && !kiri.isSelected()) {
                        isFirstSelected = true;
                        time.setSelected(false);
                    }
                }

                if (time.isSelected()) {
                    holder.cardTime.setCardBackgroundColor(Color.parseColor("#32AAFF"));
                    holder.itemTime.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    holder.cardTime.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    holder.itemTime.setTextColor(Color.parseColor("#000000"));
                }

                onItemClickCallback.onItemClicked(listTime.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTime.size();
    }

    public class TimeViewHolder extends RecyclerView.ViewHolder {
        private TextView itemTime;
        private CardView cardTime;

        public TimeViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTime = itemView.findViewById(R.id.item_time);
            cardTime = itemView.findViewById(R.id.card_time);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Time data);
    }
}

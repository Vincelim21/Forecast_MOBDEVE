package com.example.forecast.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forecast.R;
import com.example.forecast.model.Hour;

import java.util.ArrayList;

public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {
    private ArrayList<Hour> hourList;

    public static class HourViewHolder extends RecyclerView.ViewHolder{
        public TextView hour, aveTemp;
        public ImageView conditionIcon;

        public HourViewHolder(@NonNull View itemView) {
            super(itemView);
            hour = itemView.findViewById(R.id.hour_time);
            aveTemp = itemView.findViewById(R.id.hour_degree);
            conditionIcon = itemView.findViewById(R.id.hour_condition_icon);
        }
    }

    public HourAdapter(ArrayList<Hour> hourList) { this.hourList = hourList; }

    @NonNull
    @Override
    public HourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hour_card, parent, false);
        HourViewHolder hvh = new HourViewHolder(v);
        return hvh;
    }

    @Override
    public void onBindViewHolder(@NonNull HourViewHolder holder, int position) {
        Hour currentHour = hourList.get(position);

        holder.hour.setText(currentHour.getHour());
        holder.conditionIcon.setImageResource(currentHour.getConditionIconId());
        holder.aveTemp.setText(currentHour.getAveTemp() + "\u00B0");
    }

    @Override
    public int getItemCount() { return hourList.size(); }

}

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
import com.squareup.picasso.Picasso;

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
        System.out.println("DISPLAY HOUR: " +currentHour.getHour());
        holder.hour.setText(currentHour.getCondition());

        String extraIcon = currentHour.getHour();
        String iconUrl = "http://openweathermap.org/img/w/" + extraIcon + ".png";
        Picasso.get().load(iconUrl).into(holder.conditionIcon);

        holder.aveTemp.setText(Math.round(currentHour.getAveTemp()) + "\u00B0");
    }

    @Override
    public int getItemCount() {
        return hourList.size();
    }

}

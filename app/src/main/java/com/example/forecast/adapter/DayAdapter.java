package com.example.forecast.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forecast.R;
import com.example.forecast.model.Day;

import java.util.ArrayList;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder>{
    private ArrayList<Day> dayList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) { mListener = listener; }

    public static class DayViewHolder extends RecyclerView.ViewHolder{
        public TextView dayName, aveTemp, maxTemp, minTemp;
        public ImageView conditionIcon;
        public ConstraintLayout dayBG;
        public CardView dayCard;

        public DayViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            dayName = itemView.findViewById(R.id.day_name);
            conditionIcon = itemView.findViewById(R.id.day_condition_icon);
            aveTemp = itemView.findViewById(R.id.day_degree);
            maxTemp = itemView.findViewById(R.id.day_max_temp);
            minTemp = itemView.findViewById(R.id.day_min_temp);
            dayBG = itemView.findViewById(R.id.day_background);
            dayCard = itemView.findViewById(R.id.day_card);

            dayCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAbsoluteAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public DayAdapter(ArrayList<Day> dayList) {
        this.dayList = dayList;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_card, parent, false);
        DayViewHolder dvh = new DayViewHolder(v, mListener);
        return dvh;
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        Day currentDay = dayList.get(position);

        holder.dayName.setText(currentDay.getDayName());
        //holder.conditionIcon.setImageResource(currentDay.getConditionIconId());
        holder.aveTemp.setText(currentDay.getAveTemp() + "\u00B0");
        holder.maxTemp.setText(currentDay.getMaxTemp() + "\u00B0");
        holder.minTemp.setText(currentDay.getMinTemp() + "\u00B0");

        switch (position) {
            case 1:
                holder.dayBG.setBackgroundResource(R.drawable.day_background_pink);
                break;
            case 2:
                holder.dayBG.setBackgroundResource(R.drawable.day_background_green);
                break;
            case 3:
                holder.dayBG.setBackgroundResource(R.drawable.day_background_blue);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }
}

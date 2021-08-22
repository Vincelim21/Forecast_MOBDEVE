package com.example.forecast.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forecast.R;
import com.example.forecast.model.City;
import com.example.forecast.model.CityList;

import java.util.ArrayList;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {
    private static final int TYPE = 1;
    private ArrayList<CityList> cityList;
    private OnItemClickListener mlistener;

    public interface  OnItemClickListener {
        void onItemClick(int position);
        void onPreferClick(int position);
    }

    public void setOnItemCLickListener(OnItemClickListener listener){
        mlistener = listener;
    }

    public static class CityViewHolder extends RecyclerView.ViewHolder{
        public TextView cityName, countryCode;
        public ImageButton preferredButton;


        public CityViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            cityName = itemView.findViewById(R.id.city_name);
            countryCode = itemView.findViewById(R.id.country_code);
            preferredButton = itemView.findViewById(R.id.city_is_preferred);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAbsoluteAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            preferredButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAbsoluteAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onPreferClick(position);
                        }
                    }
                }
            });
        }
    }

    public CityAdapter(ArrayList<CityList> cityList) {
        this.cityList = cityList;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch(viewType) {
            case TYPE:
            default:
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_card, parent, false);
                CityViewHolder cvh = new CityViewHolder(v, mlistener);
                return cvh;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        CityList currentCity = cityList.get(position);

        holder.cityName.setText(currentCity.getCity());
        holder.countryCode.setText(currentCity.getCountry());
    }

    @Override
    public int getItemCount() { return cityList.size(); }

}

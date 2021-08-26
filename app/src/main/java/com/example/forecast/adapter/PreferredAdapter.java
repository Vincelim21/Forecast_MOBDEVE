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

public class PreferredAdapter extends RecyclerView.Adapter<PreferredAdapter.PreferredViewHolder> {
    private ArrayList<CityList> preferredList;
    private OnPreferredClickListener mlistener;

    public interface OnPreferredClickListener {
        void onPreferredCityClick(int position);
        void onPreferenceClick(int position);
    }

    public void setOnPreferredCLickListener(OnPreferredClickListener listener){
        mlistener = listener;
    }

    public static class PreferredViewHolder extends RecyclerView.ViewHolder{
        public TextView cityName;
        public ImageButton preferredButton;


        public PreferredViewHolder(@NonNull View itemView, OnPreferredClickListener listener) {
            super(itemView);
            cityName = itemView.findViewById(R.id.preferred_name);
            preferredButton = itemView.findViewById(R.id.is_preferred);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAbsoluteAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onPreferredCityClick(position);
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
                            listener.onPreferenceClick(position);
                        }
                    }
                }
            });
        }
    }

    public PreferredAdapter(ArrayList<CityList> preferredList) {
        this.preferredList = preferredList;
    }

    @NonNull
    @Override
    public PreferredAdapter.PreferredViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.preferred_city_card, parent, false);
        PreferredAdapter.PreferredViewHolder pvh = new PreferredAdapter.PreferredViewHolder(v, mlistener);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull PreferredAdapter.PreferredViewHolder holder, int position) {
        CityList currentPreferredCity = preferredList.get(position);

        holder.cityName.setText(currentPreferredCity.getCity());

        if(currentPreferredCity.getPreferred()) {
            holder.preferredButton.setImageResource(R.drawable.star_on);
        }else {
            holder.preferredButton.setImageResource(R.drawable.star_off);
        }

    }

    @Override
    public int getItemCount() { return preferredList.size(); }

}

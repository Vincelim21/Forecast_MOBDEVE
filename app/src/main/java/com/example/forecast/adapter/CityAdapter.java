package com.example.forecast.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forecast.R;
import com.example.forecast.model.City;
import com.example.forecast.model.CityList;

import java.util.ArrayList;
import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder>{
    //private static final int TYPE = 1;
    private ArrayList<CityList> cityList;
    private ArrayList<CityList> cityListFull;
    private OnItemClickListener mlistener;

    public interface  OnItemClickListener {
        void onItemClick(int position);
        void onPreferClick(int position);
    }

    public void setOnItemCLickListener(OnItemClickListener listener){
        mlistener = listener;
    }

    public static class CityViewHolder extends RecyclerView.ViewHolder{
        public TextView cityCountry;
        public ImageButton preferredButton;


        public CityViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            cityCountry = itemView.findViewById(R.id.city_name);
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
        cityListFull = new ArrayList<>(cityList);
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        switch(viewType) {
//            case TYPE:
//            default:
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_card, parent, false);
                CityViewHolder cvh = new CityViewHolder(v, mlistener);
                return cvh;
        //}
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        CityList currentCity = cityList.get(position);

        holder.cityCountry.setText(currentCity.getCityCountry());
    }

    @Override
    public int getItemCount() { return cityList.size(); }

    public void filterList(ArrayList<CityList> filteredList) {
        cityList = filteredList;
        notifyDataSetChanged();
    }

    /*
    @Override
    public Filter getFilter() {
        return cityFilter;
    }

    private Filter cityFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CityList> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(cityListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (CityList city : cityListFull) {
                    if (city.getCityCountry().toLowerCase().contains(filterPattern)) {
                        filteredList.add(city);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return  results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            cityList.clear();
            cityList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };*/
}

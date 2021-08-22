package com.example.forecast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.forecast.adapter.CityAdapter;
import com.example.forecast.model.City;


import java.util.ArrayList;

public class PickLocationActivity extends AppCompatActivity {

    private RecyclerView cityRecyclerView;
    private CityAdapter cityAdapter;
    private RecyclerView.LayoutManager cityLayoutManager;
    private ArrayList<City> cities = new ArrayList<>();
    private SearchView searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_location);

        //load cities data
        loadCityData();

        //setup city recyclerview
        setUpCityRecyclerView();

    }

    public void loadCityData() {
        cities.add(new City("Manila", R.drawable.clear, "32", "sunny"));
        cities.add(new City("Cebu", R.drawable.light_rain, "29", "light rains"));
        cities.add(new City("London", R.drawable.rain, "22", "rainy"));
        cities.add(new City("Brighton", R.drawable.partly_cloudy, "19", "partly cloudy"));
        cities.add(new City("Tokyo", R.drawable.overcast, "22", "cloudy"));
        cities.add(new City("Sapporo", R.drawable.mostly_cloudy, "20", "mostly cloudy"));
        cities.add(new City("Ontario", R.drawable.heavy_rain, "24", "heavy rains"));
        cities.add(new City("Vancouver", R.drawable.rain, "19", "rainy"));
    }

    public void setAsCurrentCity(int position) {
        String cityName = cities.get(position).getCityName();

        Toast.makeText(this, "Now using "+ cityName + " as current city.",
                Toast.LENGTH_LONG).show();
        //cityAdapter.notifyItemChanged(position);
    }

    public void preferCity(int position) {
        cities.remove(position);
        cityAdapter.notifyItemRemoved(position);
    }

     public void setUpCityRecyclerView() {

         cityRecyclerView = findViewById(R.id.city_recyclerview);
         cityRecyclerView.setHasFixedSize(true);
         cityLayoutManager = new LinearLayoutManager(PickLocationActivity.this, LinearLayoutManager.VERTICAL, false);
         //cityLayoutManager = new GridLayoutManager(this, NUM_COLS);

         cityAdapter = new CityAdapter(cities);

         cityRecyclerView.setLayoutManager(cityLayoutManager);
         cityRecyclerView.setAdapter(cityAdapter);

         //cityRecyclerView.addItemDecoration(new SpacesCityDecoration(15));

         cityAdapter.setOnItemCLickListener(new CityAdapter.OnItemClickListener() {
             @Override
             public void onItemClick(int position) {
                 setAsCurrentCity(position);
             }

             @Override
             public void onPreferClick(int position) {
                 preferCity(position);
             }
         });
     }


}
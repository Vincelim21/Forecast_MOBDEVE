package com.example.forecast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.forecast.adapter.CityAdapter;
import com.example.forecast.adapter.PreferredAdapter;
import com.example.forecast.model.City;

import java.util.ArrayList;

public class PreferredActivity extends AppCompatActivity {

    private ArrayList<City> preferredCities = new ArrayList<>();
    private RecyclerView preferredRecyclerView;
    private PreferredAdapter preferredAdapter;
    private RecyclerView.LayoutManager preferredLayoutManager;
    private static int NUM_COLS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferred_cities);

        loadPreferredCityData();

        setUpPreferredRecyclerView();
    }

    public void loadPreferredCityData() {
        preferredCities.add(new City("Manila", R.drawable.clear, "32", "sunny"));
        preferredCities.add(new City("Cebu", R.drawable.light_rain, "29", "light rains"));
        preferredCities.add(new City("London", R.drawable.rain, "22", "rainy"));
        preferredCities.add(new City("Brighton", R.drawable.partly_cloudy, "19", "partly cloudy"));
        preferredCities.add(new City("Tokyo", R.drawable.overcast, "22", "cloudy"));
        preferredCities.add(new City("Sapporo", R.drawable.mostly_cloudy, "20", "mostly cloudy"));
        preferredCities.add(new City("Ontario", R.drawable.heavy_rain, "24", "heavy rains"));
        preferredCities.add(new City("Vancouver", R.drawable.rain, "19", "rainy"));
    }

    public void setAsCurrentCity(int position) {
        String cityName = preferredCities.get(position).getCityName();

        Toast.makeText(this, "Now using "+ cityName + " as current city.",
                Toast.LENGTH_LONG).show();
        //cityAdapter.notifyItemChanged(position);
    }

    public void preferCity(int position) {
        preferredCities.remove(position);
        preferredAdapter.notifyItemRemoved(position);
    }

    public void setUpPreferredRecyclerView() {

        preferredRecyclerView = findViewById(R.id.preferred_cities_rv);
        preferredRecyclerView.setHasFixedSize(true);
        preferredLayoutManager = new GridLayoutManager(this, NUM_COLS);

        preferredAdapter = new PreferredAdapter(preferredCities);

        preferredRecyclerView.setLayoutManager(preferredLayoutManager);
        preferredRecyclerView.setAdapter(preferredAdapter);

        preferredRecyclerView.addItemDecoration(new SpacesCityDecoration(15));

        preferredAdapter.setOnPreferredCLickListener(new PreferredAdapter.OnPreferredClickListener() {
            @Override
            public void onPreferredCityClick(int position) {
                setAsCurrentCity(position);
            }

            @Override
            public void onPreferenceClick(int position) {
                preferCity(position);
            }
        });
    }
}
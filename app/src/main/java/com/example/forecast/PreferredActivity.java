package com.example.forecast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.forecast.adapter.CityAdapter;
import com.example.forecast.adapter.PreferredAdapter;
import com.example.forecast.model.City;
import com.example.forecast.model.CityList;

import java.util.ArrayList;

public class PreferredActivity extends AppCompatActivity {

    private ArrayList<CityList> preferredCities = new ArrayList<>();
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
        preferredCities.add(new CityList("Manila", "MM", true));
        preferredCities.add(new CityList("Cebu", "CEB", true));
        preferredCities.add(new CityList("Davao", "DAO", true));
        preferredCities.add(new CityList("Baguio", "BEN", true));
        preferredCities.add(new CityList("Tagaytay", "CAV", true));
        preferredCities.add(new CityList("Coron", "PLW", true));
        preferredCities.add(new CityList("Mexico", "PAM", true));
    }

    public void setAsCurrentCity(int position) {
        String cityName = preferredCities.get(position).getCity();

        Toast.makeText(this, "Now using "+ cityName + " as current city.",
                Toast.LENGTH_LONG).show();

        Intent intent = new Intent(PreferredActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void preferCity(int position) {
        boolean isPreferred = preferredCities.get(position).getPreferred();

        if(isPreferred) {
            preferredCities.get(position).setPreferred(false);
            preferredAdapter.notifyItemChanged(position);

            preferredCities.remove(position);
            preferredAdapter.notifyItemRemoved(position);
        } else {
            preferredCities.get(position).setPreferred(true);
        }

    }

    public void setUpPreferredRecyclerView() {

        preferredRecyclerView = findViewById(R.id.preferred_cities_rv);
        preferredRecyclerView.setHasFixedSize(true);
        preferredLayoutManager = new LinearLayoutManager(PreferredActivity.this, LinearLayoutManager.VERTICAL, false);

        //preferredLayoutManager = new GridLayoutManager(this, NUM_COLS);

        preferredAdapter = new PreferredAdapter(preferredCities);

        preferredRecyclerView.setLayoutManager(preferredLayoutManager);
        preferredRecyclerView.setAdapter(preferredAdapter);

        //preferredRecyclerView.addItemDecoration(new SpacesCityDecoration(15));

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
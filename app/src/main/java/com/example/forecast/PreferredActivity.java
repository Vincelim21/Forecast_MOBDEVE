package com.example.forecast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forecast.adapter.PreferredAdapter;
import com.example.forecast.data.DBHelper;
import com.example.forecast.model.CityList;
import com.example.forecast.model.Keys;

import java.util.ArrayList;

public class PreferredActivity extends AppCompatActivity {

    private ArrayList<CityList> preferredCities = new ArrayList<>();
    private RecyclerView preferredRecyclerView;
    private PreferredAdapter preferredAdapter;
    private RecyclerView.LayoutManager preferredLayoutManager;
    private static int NUM_COLS = 2;

    private DBHelper db = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferred_cities);

        setUpPreferredRecyclerView();

        loadPreferredCityData();
    }

    public void loadPreferredCityData() {
        if (db.getPrefCities() == null){
            Toast.makeText(PreferredActivity.this, "Seems like you don't have any preferred cities!", Toast.LENGTH_SHORT).show();
        }else {
            for (int i = 0; i < db.getPrefCities().size(); i++) {
                preferredCities.add(new CityList(db.getPrefCities().get(i), "PH", true));
            }
        }


        /*preferredCities.add(new CityList("Manila", "MM", true));
        preferredCities.add(new CityList("Cebu", "CEB", true));
        preferredCities.add(new CityList("Davao", "DAO", true));
        preferredCities.add(new CityList("Baguio", "BEN", true));
        preferredCities.add(new CityList("Tagaytay", "CAV", true));
        preferredCities.add(new CityList("Coron", "PLW", true));
        preferredCities.add(new CityList("Mexico", "PAM", true));*/
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

            db.deleteCity(preferredCities.get(position).getCity());
            preferredCities.remove(preferredCities.indexOf(preferredCities.get(position)));

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
                saveCity(position);
                setAsCurrentCity(position);
            }

            @Override
            public void onPreferenceClick(int position) {
                preferCity(position);
            }
        });
    }

    public void saveCity(int position){
        SharedPreferences sp = getSharedPreferences(Keys.KEY_SP.name(), MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sp.edit();

        spEditor.putString(Keys.KEY_SELECTED_CITY.name(), preferredCities.get(position).getCity());

        spEditor.apply();
    }
}
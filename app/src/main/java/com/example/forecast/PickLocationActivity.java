package com.example.forecast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.forecast.adapter.CityAdapter;
import com.example.forecast.model.City;
import com.example.forecast.model.CityList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PickLocationActivity extends AppCompatActivity {

    private RecyclerView cityRecyclerView;
    private CityAdapter cityAdapter;
    private RecyclerView.LayoutManager cityLayoutManager;
    private ArrayList<CityList> cities = new ArrayList<>();
    private SearchView searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_location);

        searchBar = findViewById(R.id.search_bar);

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                cityAdapter.getFilter().filter(newText);
                return false;
            }
        });

        //setup city recyclerview
        setUpCityRecyclerView();

    }

    public void loadCityData() {
        try {

            String jsonDataString = readJSONDataFromFile();
            JSONArray jsonArray = new JSONArray(jsonDataString);

            for (int i = 0; i < jsonArray.length(); ++i) {

                JSONObject itemObj = jsonArray.getJSONObject(i);

                String name = itemObj.getString("name");
                String province = itemObj.getString("province");

                CityList City = new CityList(name, province);
                cities.add(City);
            }

        } catch (JSONException | IOException e) {
            Log.d(null, "loadCityData: ", e);
        }
    }

    private String readJSONDataFromFile() throws IOException{

        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();

        try {

            String jsonString = null;
            inputStream = getResources().openRawResource(R.raw.citylist);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, "UTF-8"));

            while ((jsonString = bufferedReader.readLine()) != null) {
                builder.append(jsonString);
            }

        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return new String(builder);
    }
//        cities.add(new City("Manila", R.drawable.clear, "32", "sunny"));
//        cities.add(new City("Cebu", R.drawable.light_rain, "29", "light rains"));
//        cities.add(new City("London", R.drawable.rain, "22", "rainy"));
//        cities.add(new City("Brighton", R.drawable.partly_cloudy, "19", "partly cloudy"));
//        cities.add(new City("Tokyo", R.drawable.overcast, "22", "cloudy"));
//        cities.add(new City("Sapporo", R.drawable.mostly_cloudy, "20", "mostly cloudy"));
//        cities.add(new City("Ontario", R.drawable.heavy_rain, "24", "heavy rains"));
//        cities.add(new City("Vancouver", R.drawable.rain, "19", "rainy"));


    public void setAsCurrentCity(int position) {
        String cityName = cities.get(position).getCity();

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

         //load cities data
         loadCityData();

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
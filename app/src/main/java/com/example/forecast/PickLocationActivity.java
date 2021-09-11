package com.example.forecast;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forecast.adapter.CityAdapter;
import com.example.forecast.data.DBHelper;
import com.example.forecast.model.CityList;
import com.example.forecast.model.Keys;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PickLocationActivity extends AppCompatActivity {

    private RecyclerView cityRecyclerView;
    private CityAdapter cityAdapter;
    private RecyclerView.LayoutManager cityLayoutManager;
    private ArrayList<CityList> cities = new ArrayList<>();
    private EditText searchBar;
    private ImageButton ib_currentlocation;
    FusedLocationProviderClient FLPC;

    private DBHelper db = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_location);

        initUseLocation();

        //setup city recyclerview
        setUpCityRecyclerView();

        initSearchBar();
    }

    private void initUseLocation(){
        ib_currentlocation = findViewById(R.id.use_current_location);

        this.FLPC = LocationServices.getFusedLocationProviderClient(this);

        ib_currentlocation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //checks for permission
                if (ActivityCompat.checkSelfPermission(PickLocationActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    FLPC.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            //Initialize location
                            Location location = task.getResult();
                            if (location != null){
                                try {
                                    //initialize geocoder
                                    Geocoder g = new Geocoder(PickLocationActivity.this,
                                            Locale.getDefault());
                                    //initialize address list
                                    List<Address> add = g.getFromLocation(
                                            location.getLatitude(), location.getLongitude(), 1
                                    );

                                    String currCity = add.get(0).getLocality();
                                    //Stores current location in shared preferences
                                    SharedPreferences sp = getSharedPreferences(Keys.KEY_SP.name(), MODE_PRIVATE);
                                    SharedPreferences.Editor spEditor = sp.edit();

                                    spEditor.putString(Keys.KEY_SELECTED_CITY.name(), add.get(0).getLocality().toString());
                                    spEditor.apply();

                                    setAsCurrentCity(currCity);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });
                }
                else{
                    System.out.println("Null loc");
                    ActivityCompat.requestPermissions(PickLocationActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                }
            }
        });

    }


    private void initSearchBar() {
        searchBar = findViewById(R.id.search_bar);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    private void filter(String text) {
        ArrayList<CityList> filteredList = new ArrayList<>();

        for (CityList city: cities) {
            if (city.getCity().toLowerCase().startsWith(text.toLowerCase())) {
                filteredList.add(city);
            }
        }

        cityAdapter.filterList(filteredList);
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


    public void setAsCurrentCity(String cityName) {

        Toast.makeText(this, "Now using "+ cityName + " as current city.",
                Toast.LENGTH_LONG).show();

        Intent intent = new Intent(PickLocationActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void preferCity(int position) {
        boolean isPreferred = cities.get(position).getPreferred();


        if(isPreferred) {
            cities.get(position).setPreferred(false);
            db.deleteCity(cities.get(position).getCity());
        } else {
            cities.get(position).setPreferred(true);
            Toast.makeText(this, "Now preferring "+ cities.get(position).getCity() + " city.",
                    Toast.LENGTH_LONG).show();
            db.addCity(cities.get(position).getCity());
        }

        cityAdapter.notifyItemChanged(position);
        //cities.remove(position);
        //cityAdapter.notifyItemRemoved(position);
    }

     public void setUpCityRecyclerView() {
        System.out.println("City: "+cities.size());

         cityRecyclerView = findViewById(R.id.city_recyclerview);
         cityRecyclerView.setHasFixedSize(true);
         cityLayoutManager = new LinearLayoutManager(PickLocationActivity.this, LinearLayoutManager.VERTICAL, false);

         cityAdapter = new CityAdapter(cities);

         cityRecyclerView.setLayoutManager(cityLayoutManager);
         cityRecyclerView.setAdapter(cityAdapter);

         //load cities data
         loadCityData();

         cityAdapter.setOnItemCLickListener(new CityAdapter.OnItemClickListener() {
             @Override
             public void onItemClick(int position) {

                 saveCity(position);
                 setAsCurrentCity(cities.get(position).getCity());
             }

             @Override
             public void onPreferClick(int position) {
                 preferCity(position);
             }
         });
     }

    public void saveCity(int position){
        SharedPreferences sp = getSharedPreferences(Keys.KEY_SP.name(), MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sp.edit();

        spEditor.putString(Keys.KEY_SELECTED_CITY.name(), cities.get(position).getCity());

        spEditor.apply();
    }
}
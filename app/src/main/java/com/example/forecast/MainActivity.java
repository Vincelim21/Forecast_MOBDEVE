package com.example.forecast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.forecast.adapter.DayAdapter;
import com.example.forecast.model.Day;
import com.example.forecast.model.Hour;
import com.example.forecast.model.Keys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView fourDayRecyclerView;
    private DayAdapter fourDayAdapter;
    private RecyclerView.LayoutManager fourDayLayoutManager;
    private ArrayList<Day> days = new ArrayList<>();
    private DecimalFormat df2 = new DecimalFormat("#.##");

    private TextView tv_temp, tv_hum, tv_wind, tv_cond, tv_city;

    private final String url = "http://api.openweathermap.org/data/2.5/forecast";
    private final String appid = "f0e85b0c89d7444ae43d1e802809975f";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView cityCountry;

        readyTodayWeather();
        //Test Commit
        //Push Carlo
        //Push Victor

        //load days data
        loadDaysData();

        //setup day recyclerview
        setUpFourDayRecyclerView();
    }
    public void loadDaysData() {
        SharedPreferences sp = getSharedPreferences(Keys.KEY_SP.name(), MODE_PRIVATE);
        String selectedCity = sp.getString(Keys.KEY_SELECTED_CITY.name(),null);

        String temp = url + "?q=" + selectedCity + ",PH&appid=" + appid;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, temp,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("City: " + selectedCity);
                        Log.d("response", response.toString());

//                String output = "";
                        try{
                            JSONArray weather_list = response.getJSONArray("list");
                            System.out.println(weather_list.length());

                            int conditionIconId, ctr=0;
                            String date, time, dayName, hour, condition;
                            double max=0, min=0, aveTemp=0, aveHum=0, aveWind=0;
                            double minTemp, maxTemp, hTemp, wind, humidity;
                            String currentDate = "";
                            boolean day1 =false;

                            int i = 0, day=1;
                            while (i < weather_list.length() && day1==false) {
                                System.out.println(i);
                                JSONObject forecast = (JSONObject) weather_list.get(i);

                                hTemp = forecast.getJSONObject("main").getDouble("temp") - 273.15;
                                maxTemp = forecast.getJSONObject("main").getDouble("temp_max") - 273.15;
                                minTemp = forecast.getJSONObject("main").getDouble("temp_min") - 273.15;
                                humidity = forecast.getJSONObject("main").getDouble("humidity");
                                wind = forecast.getJSONObject("wind").getDouble("speed");
                                JSONObject hi = (JSONObject) forecast.getJSONArray("weather").get(0);
                                condition = hi.getString("description");
                                String dt = forecast.getString("dt_txt");
                                String[] dateTime = dt.split(" ");
                                date = dateTime[0];
                                time = dateTime[1];

                                System.out.println("humidity: "+humidity);

                                if (i == 0){
                                    currentDate = date;
                                }

                                if (currentDate.equals(date) && day!=1){
                                    aveTemp += hTemp;
                                    aveHum += humidity;
                                    aveWind += wind;

                                    if (minTemp < min)
                                        min = minTemp;
                                    if (maxTemp > max)
                                        max = maxTemp;
                                    ctr++;
                                }

                                if (!currentDate.equals(date)){
                                    if(day !=1) {
                                        aveTemp /= (ctr);
                                        aveHum /= ctr;
                                        aveWind /= ctr;
                                        days.add(new Day(currentDate, condition, aveTemp, max, min, aveHum, aveWind));

                                        currentDate = date;
                                        ctr = 0;
                                        aveTemp += hTemp;
                                        aveHum += humidity;
                                        aveWind += wind;
                                        if (minTemp < min)
                                            min = minTemp;
                                        if (maxTemp > max)
                                            max = maxTemp;
                                        ctr++;
                                    }
                                    day++;
                                }
                                i++;
                            }


                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        rq.add(req);
    }

    public void setUpFourDayRecyclerView() {
        fourDayRecyclerView = findViewById(R.id.four_day_forecast_container);
        fourDayRecyclerView.setHasFixedSize(true);
        fourDayLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        fourDayAdapter = new DayAdapter(days);

        fourDayRecyclerView.setLayoutManager(fourDayLayoutManager);
        fourDayRecyclerView.setAdapter(fourDayAdapter);

        fourDayAdapter.setOnItemClickListener(new DayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                goDetailedView(position);
            }
        });
    }

    public void goDetailedView(int position){
        String dayName = days.get(position).getDayName();
        String aveTemp = Double.toString(days.get(position).getAveTemp());
        String minTemp = Double.toString(days.get(position).getMinTemp());
        String maxTemp = Double.toString(days.get(position).getMaxTemp());

        Intent i = new Intent(getBaseContext(), DetailedWeatherActivity.class);
        i.putExtra("EXTRA_DAY_NAME", dayName);
        i.putExtra("EXTRA_AVE_TEMP", aveTemp);
        i.putExtra("EXTRA_MIN_TEMP", minTemp);
        i.putExtra("EXTRA_MAX_TEMP", maxTemp);

        switch (position) {
            case 0:
                i.putExtra("EXTRA_DAY_BG", R.drawable.day_background);
                break;
            case 1:
                i.putExtra("EXTRA_DAY_BG", R.drawable.day_background_pink);
                break;
            case 2:
                i.putExtra("EXTRA_DAY_BG", R.drawable.day_background_green);
                break;
            case 3:
                i.putExtra("EXTRA_DAY_BG", R.drawable.day_background_blue);
                break;
        }

        startActivity(i);
    }

    @Override
    protected void onResume(){
        super.onResume();

        readyTodayWeather();
    }

    private void readyTodayWeather() {
        SharedPreferences sp = getSharedPreferences(Keys.KEY_SP.name(), MODE_PRIVATE);
        String selectedCity = sp.getString(Keys.KEY_SELECTED_CITY.name(),null);

        String temp = url + "?q=" + selectedCity + ",PH&appid=" + appid;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, temp,null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("City: " + selectedCity);
                Log.d("response", response.toString());

//                String output = "";
                try{
                    JSONArray weather_list = response.getJSONArray("list");
                    System.out.println(weather_list.length());

                    int conditionIconId, ctr=0;
                    String date, time, dayName, hour, condition;
                    double max=0, min=0, aveTemp=0, aveHum=0, aveWind=0;
                    double minTemp, maxTemp, hTemp, wind, humidity;
                    String currentDate = "";

                    int i = 0, day=1;
                    while (i < weather_list.length() && day==1) {
                        System.out.println(i);
                        JSONObject forecast = (JSONObject) weather_list.get(i);

                        hTemp = forecast.getJSONObject("main").getDouble("temp") - 273.15;
                        maxTemp = forecast.getJSONObject("main").getDouble("temp_max") - 273.15;
                        minTemp = forecast.getJSONObject("main").getDouble("temp_min") - 273.15;
                        humidity = forecast.getJSONObject("main").getDouble("humidity");
                        wind = forecast.getJSONObject("wind").getDouble("speed");
                        JSONObject hi = (JSONObject) forecast.getJSONArray("weather").get(0);
                        condition = hi.getString("description");
                        String dt = forecast.getString("dt_txt");
                        String[] dateTime = dt.split(" ");
                        date = dateTime[0];
                        time = dateTime[1];

                        System.out.println("humidity: "+humidity);

                        if (i == 0){
                            currentDate = date;
                        }

                        if (currentDate.equals(date)){
                            aveTemp += hTemp;
                            aveHum += humidity;
                            aveWind += wind;

                            if (minTemp < min)
                                min = minTemp;
                            if (maxTemp > max)
                                max = maxTemp;
                            ctr++;
                        }
                        else{
                            aveTemp/=ctr;
                            aveHum/=ctr;
                            aveWind/=ctr;

                            tv_cond = findViewById(R.id.current_condition);
                            tv_hum = findViewById(R.id.current_humidity);
                            tv_temp = findViewById(R.id.current_degree);
                            tv_wind = findViewById(R.id.current_windspeed);
                            tv_city = findViewById(R.id.preferred_name);

                            tv_cond.setText(condition);
                            tv_city.setText(selectedCity + ", Philippines");
                            tv_hum.setText(df2.format(aveHum) + "%");
                            tv_temp.setText(df2.format(aveTemp) + "°C");
                            tv_wind.setText(df2.format(aveWind) + "m/s");

                            day=999;
                        }
                        i++;
                    }

                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        rq.add(req);
    }



    /**
     * OnClick listener function for city_name TextView. Once clicked, the app will go to the PickLocationActivity.
     *
     * @param v is the TextView named city_name which holds the name of the current city being used for the weather data
     */
    public void cityClick(View v) {
            Intent intent = new Intent(MainActivity.this, PickLocationActivity.class);
            startActivity(intent);
    }

    public void detailedClick(View v){
            Intent intent = new Intent(MainActivity.this, DetailedWeatherActivity.class);
            startActivity(intent);
    }

    public void preferredClick (View v) {
        Intent intent = new Intent(MainActivity.this, PreferredActivity.class);
        startActivity(intent);
    }
}
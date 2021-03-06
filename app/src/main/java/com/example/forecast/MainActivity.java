package com.example.forecast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.forecast.adapter.DayAdapter;
import com.example.forecast.data.DBHelper;
import com.example.forecast.model.Day;
import com.example.forecast.model.Hour;
import com.example.forecast.model.Keys;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView fourDayRecyclerView;
    private DayAdapter fourDayAdapter;
    private RecyclerView.LayoutManager fourDayLayoutManager;
    private ArrayList<Day> days = new ArrayList<>();
    private DecimalFormat df2 = new DecimalFormat("#.#");

    private TextView tv_temp, tv_hum, tv_wind, tv_cond, tv_city;
    private ImageView iv_icon;

    private final String url = "http://api.openweathermap.org/data/2.5/forecast";
    private final String appid = "f0e85b0c89d7444ae43d1e802809975f";

    private DBHelper db = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView cityCountry;

        db.DeleteForecast();

        this.readyTodayWeather();

        loadDaysData();
    }
    public void loadDaysData() {
        SharedPreferences sp = getSharedPreferences(Keys.KEY_SP.name(), MODE_PRIVATE);
        String selectedCity = sp.getString(Keys.KEY_SELECTED_CITY.name(),null);
        System.out.println(selectedCity);

        String temp = url + "?q=" + selectedCity + ",PH&appid=" + appid;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, temp,null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("City: " + selectedCity);
                        Log.d("response", response.toString());

//                String output = "";
                        try{
                            JSONArray weather_list = response.getJSONArray("list");
                            System.out.println(weather_list.length());

                            int conditionIconId;
                            String date, time, dayName, hour, condition, icon;
                            double ctr = 0, max=0, min=0, aveTemp=0, aveHum=0, aveWind=0, minTemp, maxTemp, hTemp, wind, humidity;
                            String currentDate = "", dayOfWeek;

                            int i = 0, day=1;
                            while (i < weather_list.length()){

                                JSONObject forecast = (JSONObject) weather_list.get(i);

                                hTemp = forecast.getJSONObject("main").getDouble("temp") - 273.15;
                                maxTemp = forecast.getJSONObject("main").getDouble("temp_max") - 273.15;
                                minTemp = forecast.getJSONObject("main").getDouble("temp_min") - 273.15;
                                humidity = forecast.getJSONObject("main").getDouble("humidity");
                                wind = forecast.getJSONObject("wind").getDouble("speed");
                                JSONObject hi = (JSONObject) forecast.getJSONArray("weather").get(0);
                                condition = hi.getString("description");
                                icon = hi.getString("icon");
                                String dt = forecast.getString("dt_txt");
                                String[] dateTime = dt.split(" ");
                                date = dateTime[0];
                                time = dateTime[1];
                                System.out.println(date);
                                if (i == 0){
                                    currentDate = date;
                                }
                                if (day != 1 && ctr ==1){
                                    min = minTemp;
                                    max = maxTemp;
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
                                    if(day != 1) {
                                        aveTemp /= (ctr);
                                        aveHum /= ctr;
                                        aveWind /= ctr;
                                        dayOfWeek = LocalDate.parse(currentDate).getDayOfWeek().name();
                                        Day newDay = new Day(dayOfWeek, condition, icon, Double.parseDouble(df2.format(aveTemp)),
                                                Double.parseDouble(df2.format(max)),  Double.parseDouble(df2.format(min)),
                                                Double.parseDouble(df2.format(aveHum)), Double.parseDouble(df2.format(aveWind)));
                                        db.AddDayForecast(newDay);

                                        ctr = 0;
                                        aveTemp = 0;
                                        aveHum = 0;
                                        aveWind = 0;
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
                                    currentDate = date;
                                }
                                i++;
                            }
                            setUpFourDayRecyclerView();

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



//        days.add(new Day("Monday", "fair", 32, 34, 29, 56, 1.5));
//        days.add(new Day("Tuesday", "fair", 32, 34, 29, 56, 1.5));
//        days.add(new Day("Wednesday", "fair", 32, 34, 29, 56, 1.5));
//        days.add(new Day("Thursday", "fair", 32, 34, 29, 56, 1.5));

    }

    public void setUpFourDayRecyclerView() {
        if (this.days != null)
            this.days.clear();
        if(db.getFourDayForecast() != null) {
            this.days.add(db.getFourDayForecast().get(db.getFourDayForecast().size() - 4));
            this.days.add(db.getFourDayForecast().get(db.getFourDayForecast().size() - 3));
            this.days.add(db.getFourDayForecast().get(db.getFourDayForecast().size() - 2));
            this.days.add(db.getFourDayForecast().get(db.getFourDayForecast().size() - 1));
        }

        fourDayRecyclerView = findViewById(R.id.four_day_forecast_container);
        fourDayRecyclerView.setHasFixedSize(true);
        fourDayLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);

        fourDayAdapter = new DayAdapter(this.days);

        fourDayRecyclerView.setLayoutManager(fourDayLayoutManager);
        fourDayRecyclerView.setAdapter(fourDayAdapter);

        //load days data
        //this.loadDaysData();

        //System.out.println("Days: "+days.size());

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
        String condition = days.get(position).getCondition();
        String icon = days.get(position).getConditionIcon();

        Intent i = new Intent(getBaseContext(), DetailedWeatherActivity.class);
        i.putExtra("EXTRA_DAY_NAME", dayName);
        i.putExtra("EXTRA_AVE_TEMP", aveTemp);
        i.putExtra("EXTRA_MIN_TEMP", minTemp);
        i.putExtra("EXTRA_MAX_TEMP", maxTemp);
        i.putExtra("EXTRA_COND", condition);
        i.putExtra("EXTRA_ICON", icon);

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
        System.out.println("resume");
        this.readyTodayWeather();
        this.setUpFourDayRecyclerView();
    }

    public void readyTodayWeather() {

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

                            int conditionIconId, ctr=0, day=1;
                            String date, time, condition, icon;
                            double max=0, min=0, aveTemp=0, aveHum=0, aveWind=0;
                            double minTemp, maxTemp, hTemp, wind, humidity;
                            String currentDate = "";

                            ArrayList<Hour> hours = new ArrayList<>();
                            ArrayList<Day> days = new ArrayList<>();
                            int i = 0;
                            while (i < weather_list.length() && day==1) {
                                JSONObject forecast = (JSONObject) weather_list.get(i);

                                hTemp = forecast.getJSONObject("main").getDouble("temp") - 273.15;
                                maxTemp = forecast.getJSONObject("main").getDouble("temp_max") - 273.15;
                                minTemp = forecast.getJSONObject("main").getDouble("temp_min") - 273.15;
                                humidity = forecast.getJSONObject("main").getDouble("humidity");
                                wind = forecast.getJSONObject("wind").getDouble("speed");
                                JSONObject hi = (JSONObject) forecast.getJSONArray("weather").get(0);
                                condition = hi.getString("description");
                                icon = hi.getString("icon");
                                String dt = forecast.getString("dt_txt");
                                String[] dateTime = dt.split(" ");
                                date = dateTime[0];
                                time = dateTime[1];


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
                                    iv_icon = findViewById(R.id.current_condition_icon);


                                    String iconUrl = "http://openweathermap.org/img/w/" + icon + ".png";
                                    Picasso.get().load(iconUrl).into(iv_icon);

                                    tv_cond.setText(condition);
                                    tv_city.setText(selectedCity + ", Philippines");
                                    tv_hum.setText("Humidity " + df2.format(aveHum) + "%");
                                    tv_temp.setText(df2.format(aveTemp) + "??C");
                                    tv_wind.setText("Wind " + df2.format(aveWind) + " m/s");

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

        this.loadDaysData();
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

        SharedPreferences sp = getSharedPreferences(Keys.KEY_SP.name(), MODE_PRIVATE);
        String selectedCity = sp.getString(Keys.KEY_SELECTED_CITY.name(),null);

        String temp = url + "?q=" + selectedCity + ",PH&appid=" + appid;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, temp,null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("City: " + selectedCity);
                        Log.d("response", response.toString());

//                String output = "";
                        try{
                            JSONArray weather_list = response.getJSONArray("list");
                            System.out.println(weather_list.length());

                            int conditionIconId, ctr=0, day=1;
                            String date, time, condition, icon;
                            double max=0, min=0, aveTemp=0, aveHum=0, aveWind=0;
                            double minTemp, maxTemp, hTemp, wind, humidity;
                            String currentDate = "";

                            ArrayList<Hour> hours = new ArrayList<>();
                            ArrayList<Day> days = new ArrayList<>();
                            int i = 0;
                            while (i < weather_list.length() && day==1) {
                                JSONObject forecast = (JSONObject) weather_list.get(i);

                                hTemp = forecast.getJSONObject("main").getDouble("temp") - 273.15;
                                maxTemp = forecast.getJSONObject("main").getDouble("temp_max") - 273.15;
                                minTemp = forecast.getJSONObject("main").getDouble("temp_min") - 273.15;
                                humidity = forecast.getJSONObject("main").getDouble("humidity");
                                wind = forecast.getJSONObject("wind").getDouble("speed");
                                JSONObject hi = (JSONObject) forecast.getJSONArray("weather").get(0);
                                condition = hi.getString("description");
                                icon = hi.getString("icon");
                                String dt = forecast.getString("dt_txt");
                                String[] dateTime = dt.split(" ");
                                date = dateTime[0];
                                time = dateTime[1];


                                if (i == 0){
                                    currentDate = date;
                                    min = minTemp;
                                    max = maxTemp;
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

                                    Intent intent = new Intent(MainActivity.this, DetailedWeatherActivity.class);
                                    intent.putExtra("EXTRA_DAY_NAME", LocalDate.parse(currentDate).getDayOfWeek().name());
                                    intent.putExtra("EXTRA_AVE_TEMP", String.valueOf(df2.format(aveTemp)));
                                    intent.putExtra("EXTRA_MIN_TEMP", String.valueOf(df2.format(min)));
                                    intent.putExtra("EXTRA_MAX_TEMP", String.valueOf(df2.format(max)));
                                    intent.putExtra("EXTRA_COND", condition);
                                    intent.putExtra("EXTRA_ICON", icon);
                                    intent.putExtra("EXTRA_DAY_BG", R.drawable.current_weather_container);

                                    startActivity(intent);
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

    public void preferredClick (View v) {
        Intent intent = new Intent(MainActivity.this, PreferredActivity.class);
        startActivity(intent);
    }
}
package com.example.forecast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.forecast.adapter.HourAdapter;
import com.example.forecast.model.Day;
import com.example.forecast.model.Hour;
import com.example.forecast.model.Keys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailedWeatherActivity extends Activity {
    private RecyclerView hourlyRecyclerView;
    private RecyclerView.Adapter hourlyAdapter;
    private RecyclerView.LayoutManager hourlyLayoutManager;
    private ArrayList<Hour> hours = new ArrayList<>();
    private final String url = "http://api.openweathermap.org/data/2.5/forecast";
    private final String appid = "f0e85b0c89d7444ae43d1e802809975f";
    private String extraDayName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_layout);

        unpackPassedData();

        //load hour data
        loadHourData();

        //setup recyclerview
        setUpHourlyRecyclerView();

        //set pop-up
        setUpWindowManager();
    }

    public void unpackPassedData() {
        Bundle bundle = getIntent().getExtras();
        //if one of the next days cards clicked, unpack passed data
        if (bundle != null){
            TextView dayName = findViewById(R.id.day_name);
            this.extraDayName = getIntent().getStringExtra("EXTRA_DAY_NAME");
            dayName.setText(extraDayName);

            ConstraintLayout dayBG = findViewById(R.id.day_background);
            int bg = bundle.getInt("EXTRA_DAY_BG");
            dayBG.setBackgroundResource(bg);

            TextView aveTemp = findViewById(R.id.degree);
            String extraAveTemp = getIntent().getStringExtra("EXTRA_AVE_TEMP");
            aveTemp.setText(extraAveTemp);

            TextView minTemp = findViewById(R.id.day_min_temp);
            String extraMinTemp = getIntent().getStringExtra("EXTRA_MIN_TEMP");
            minTemp.setText(extraMinTemp);

            TextView maxTemp = findViewById(R.id.day_max_temp);
            String extraMaxTemp = getIntent().getStringExtra("EXTRA_MAX_TEMP");
            maxTemp.setText(extraMaxTemp);
        }
    }

    public void loadHourData() {
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
                            String date, time, hour, condition;
                            double max=0, min=0, aveTemp=0, aveHum=0, aveWind=0;
                            double minTemp, maxTemp, hTemp, wind, humidity;
                            String currentDate = "";
                            String[] next;

                            int i = 0;
                            do {
                                JSONObject forecast = (JSONObject) weather_list.get(i);

                                hTemp = forecast.getJSONObject("main").getDouble("temp");
                                condition = forecast.getJSONArray("weather").get(0).toString();
                                String dt = forecast.getString("dt_txt");
                                String[] dateTime = dt.split(" ");
                                date = dateTime[0];
                                time = dateTime[1];

                                JSONObject nextO = (JSONObject) weather_list.get(i+1);
                                next = nextO.getString("dt_txt").split(" ");

                                hours.add(new Hour(time, condition, hTemp));

                                System.out.println(condition);
                                i++;
                            } while (getExtraDayName().equals(next[0]) || i != 39);


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
//        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
//        rq.add(req);
    }

    public void setUpHourlyRecyclerView() {
        hourlyRecyclerView = findViewById(R.id.hour_container);
        hourlyRecyclerView.setHasFixedSize(true);
        hourlyLayoutManager = new LinearLayoutManager(DetailedWeatherActivity.this, LinearLayoutManager.HORIZONTAL, false);
        hourlyAdapter = new HourAdapter(hours);

        hourlyRecyclerView.setLayoutManager(hourlyLayoutManager);
        hourlyRecyclerView.setAdapter(hourlyAdapter);
    }

    public void setUpWindowManager() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.9), (int)(height*.6));
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        WindowManager.LayoutParams params = getWindow().getAttributes();


        /*WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
         */

        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = 0;

        getWindow().setAttributes(params);
    }

    public String getExtraDayName() {
        return extraDayName;
    }
}


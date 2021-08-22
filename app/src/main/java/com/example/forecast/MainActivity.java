package com.example.forecast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.forecast.adapter.DayAdapter;
import com.example.forecast.model.Day;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView fourDayRecyclerView;
    private DayAdapter fourDayAdapter;
    private RecyclerView.LayoutManager fourDayLayoutManager;
    private ArrayList<Day> days = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Test Commit

        //load days data
        loadDaysData();

        //setup day recyclerview
        setUpFourDayRecyclerView();
    }
    public void loadDaysData() {
        days.add(new Day("Thursday", R.drawable.overcast, "33", "35", "28"));
        days.add(new Day("Friday", R.drawable.fog, "4", "9", "-4"));
        days.add(new Day("Saturday", R.drawable.rain, "25", "28", "22"));
        days.add(new Day("Sunday", R.drawable.thunderstorms, "30", "33", "27"));
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
        String aveTemp = days.get(position).getAveTemp();
        String minTemp = days.get(position).getMinTemp();
        String maxTemp = days.get(position).getMaxTemp();

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
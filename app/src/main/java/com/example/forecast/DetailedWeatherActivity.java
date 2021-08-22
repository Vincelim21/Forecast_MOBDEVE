package com.example.forecast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.forecast.adapter.HourAdapter;
import com.example.forecast.model.Hour;

import java.util.ArrayList;

public class DetailedWeatherActivity extends Activity {
    private RecyclerView hourlyRecyclerView;
    private RecyclerView.Adapter hourlyAdapter;
    private RecyclerView.LayoutManager hourlyLayoutManager;
    private ArrayList<Hour> hours = new ArrayList<>();

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
            String extraDayName = getIntent().getStringExtra("EXTRA_DAY_NAME");
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
        hours.add(new Hour("09:00", R.drawable.overcast,"32"));
        hours.add(new Hour("12:00", R.drawable.overcast,"32"));
        hours.add(new Hour("15:00", R.drawable.overcast,"31"));
        hours.add(new Hour("18:00", R.drawable.overcast,"28"));
        hours.add(new Hour("21:00", R.drawable.overcast,"27"));
        hours.add(new Hour("00:00", R.drawable.overcast,"27"));
        hours.add(new Hour("03:00", R.drawable.overcast,"26"));
        hours.add(new Hour("06:00", R.drawable.overcast,"27"));
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
}


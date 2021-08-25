package com.example.forecast.model;

import android.widget.Toast;

public class Day {
    private int conditionIconId;
    private double humidity, wind_speed, aveTemp, maxTemp, minTemp;
    private String dayName, condition;

    public Day(String dayName, String condition, double aveTemp, double maxTemp, double minTemp, double humidity, double speed) {
        this.condition = condition;
        this.humidity = humidity;
        this.wind_speed = speed;
        this.dayName = dayName;
        this.aveTemp = aveTemp;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        System.out.println("Constructor called");
    }

    public double getHumidity() {
        return humidity;
    }

    public double getWind_speed() {
        return wind_speed;
    }

    public String getDayName() {
        return dayName;
    }

    public int getConditionIconId() {
        return conditionIconId;
    }

    public double getAveTemp() {
        return aveTemp;
    }

    public double getMaxTemp() { return maxTemp; }

    public double getMinTemp() {
        return minTemp;
    }

}

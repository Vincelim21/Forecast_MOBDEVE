package com.example.forecast.model;

import android.widget.Toast;

public class Day {
    private int conditionIconId;
    private String dayName, aveTemp, maxTemp, minTemp;

    public Day(String dayName, int conditionIconId, String aveTemp, String maxTemp, String minTemp) {
        this.dayName = dayName;
        this.conditionIconId = conditionIconId;
        this.aveTemp = aveTemp;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
    }

    public String getDayName() {
        return dayName;
    }

    public int getConditionIconId() {
        return conditionIconId;
    }

    public String getAveTemp() {
        return aveTemp;
    }

    public String getMaxTemp() { return maxTemp; }

    public String getMinTemp() {
        return minTemp;
    }

}

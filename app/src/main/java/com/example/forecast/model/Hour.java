package com.example.forecast.model;

public class Hour {

    private int conditionIconId;
    private String hour, aveTemp;

    public Hour(String hour, int conditionIconId, String aveTemp) {
        this.hour = hour;
        this.conditionIconId = conditionIconId;
        this.aveTemp = aveTemp;
    }

    public String getHour() { return hour; }

    public int getConditionIconId() {
        return conditionIconId;
    }

    public String getAveTemp() {
        return aveTemp;
    }

}

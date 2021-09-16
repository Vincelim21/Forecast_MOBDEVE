package com.example.forecast.model;

public class Day {
    private double humidity, wind_speed, aveTemp, maxTemp, minTemp;
    private String dayName, condition, icon;

    public Day(String dayName, String condition, String icon, double aveTemp, double maxTemp, double minTemp, double humidity, double speed) {
        this.condition = condition;
        this.humidity = humidity;
        this.wind_speed = speed;
        this.dayName = dayName;
        this.aveTemp = aveTemp;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.icon = icon;
        System.out.println("Constructor called");
    }

    public String getConditionIcon() {
        return icon;
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

    public String getCondition() {
        return condition;
    }

    public double getAveTemp() {
        return aveTemp;
    }

    public double getMaxTemp() { return maxTemp; }

    public double getMinTemp() {
        return minTemp;
    }

}

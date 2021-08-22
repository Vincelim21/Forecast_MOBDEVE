package com.example.forecast.model;

public class City {
    private int conditionIconId;
    private String cityName, curTemp, weatherDesc;

    public City(String cityName, int conditionIconId, String curTemp, String weather_desc) {
        this.cityName = cityName;
        this.conditionIconId = conditionIconId;
        this.curTemp = curTemp;
        this.weatherDesc = weather_desc;
    }

    public City(String cityName, int conditionIconId, String curTemp) {
        this.cityName = cityName;
        this.conditionIconId = conditionIconId;
        this.curTemp = curTemp;
    }

    public String getCityName() { return cityName; }

    public int getConditionIconId() {
        return conditionIconId;
    }

    public String getCurTemp() {
        return curTemp;
    }

    public String getWeatherDesc() { return weatherDesc; }
}

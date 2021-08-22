package com.example.forecast.model;

public class CityList {
    private String city;
    private String cityCountry;

    public CityList(String city, String province){
        this.city = city;
        this.cityCountry = city + ", " + province;
    }

    public String getCity() {
        return city;
    }

    public String getCityCountry() {
        return cityCountry;
    }

}

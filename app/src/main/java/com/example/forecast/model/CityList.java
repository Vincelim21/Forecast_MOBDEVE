package com.example.forecast.model;

public class CityList {
    private String city, country;

    public CityList(String city, String country){
        this.city = city;
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }
}

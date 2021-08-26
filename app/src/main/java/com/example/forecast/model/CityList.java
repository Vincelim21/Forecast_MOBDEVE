package com.example.forecast.model;

public class CityList {
    private String city;
    private String cityCountry;
    private boolean preferred;

    public CityList(String city, String province){
        this.city = city;
        this.cityCountry = city + ", " + province;
        this.preferred = false;
    }

    public String getCity() {
        return city;
    }

    public String getCityCountry() { return cityCountry; }

    public boolean getPreferred() {
        return preferred;
    }

    public void setPreferred(boolean preferred) {
        this.preferred = preferred;
    }
}

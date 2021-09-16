package com.example.forecast.model;

public class Hour {

    private int conditionIconId;
    private String condition, day;
    private String hour;
    private double aveTemp;

    public Hour(String day, String hour, String condition, double aveTemp) {
        this.hour = hour;
        this.day = day;
        this.condition = condition;
//        this.conditionIconId = conditionIconId;
        this.aveTemp = aveTemp;
    }

    public Hour(){}

    public String getDay() {
        return day;
    }

    public String getHour() { return hour; }

    public String getCondition() {
        return condition;
    }

    public void setConditionIconId(int conditionIconId) {
        this.conditionIconId = conditionIconId;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setAveTemp(double aveTemp) {
        this.aveTemp = aveTemp;
    }

    public double getAveTemp() {
        return aveTemp;
    }

}

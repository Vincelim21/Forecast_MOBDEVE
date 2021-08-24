package com.example.forecast.model;

public class Hour {

    private int conditionIconId;
    private String condition;
    private String hour;
    private double aveTemp;

    public Hour(String hour, String condition, double aveTemp) {
        this.hour = hour;
        this.condition = condition;
//        this.conditionIconId = conditionIconId;
        this.aveTemp = aveTemp;
    }

    public Hour(){}

    public String getHour() { return hour; }

    public int getConditionIconId() {
        return conditionIconId;
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

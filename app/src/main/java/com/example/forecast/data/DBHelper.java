package com.example.forecast.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.forecast.model.Day;
import com.example.forecast.model.Hour;

import java.util.ArrayList;

/*
 * Supply the information based on what you think is appropriate.
 * */

public class DBHelper extends SQLiteOpenHelper {
    // DB information
    private static final String DATABASE_NAME = "Preferred.db";
    private static final int DATABASE_VERSION = 1;

    // Column names
    public static final String CITY_TABLE = "CityTable";
    public static final String CITY_NAME = "city";

    public static final String DAILY_TABLE = "DailyTable";
    public static final String D_COND = "Condition";
    public static final String D_CICON = "Icon";
    public static final String D_HUM = "Humidity";
    public static final String D_SPEED = "Speed";
    public static final String D_DAY = "Day_of_Week";
    public static final String D_TEMP = "Ave_Temp";
    public static final String D_MAX = "Max_Temp";
    public static final String D_MIN = "Min_Temp";

    public static final String HOUR_TABLE = "HourlyTable";
    public static final String H_HOUR = "Hour";
    public static final String H_COND = "Condition";
    public static final String H_TEMP = "hTemp";

    private String create_query = "CREATE TABLE " + CITY_TABLE +
            " ("+ CITY_NAME + " TEXT);\n";
    private String create2 = "CREATE TABLE " + DAILY_TABLE +
            " (" + D_COND + " TEXT, "+ D_CICON + " TEXT, "+
            D_HUM + " DOUBLE, "+ D_SPEED + " DOUBLE, "+
            D_DAY + " TEXT, "+ D_TEMP + " DOUBLE, "+
            D_MAX + " DOUBLE, "+ D_MIN + " DOUBLE);";
    private String create3 = "CREATE TABLE " + HOUR_TABLE +
            " (" + H_HOUR + " TEXT, "+ H_COND + " TEXT, "+
            H_TEMP + " DOUBLE, "+ D_DAY+" TEXT"+");";

    private String upgrade_query = "DROP TABLE IF EXISTS " + CITY_TABLE +";";
    private String upgrade2 = "DROP TABLE IF EXISTS " + DAILY_TABLE + ";";
    private String upgrade3 = "DROP TABLE IF EXISTS " + HOUR_TABLE + ";";
    // Table information
    private static final String CREATE_POST_TABLE = "";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(create_query);
        db.execSQL(create2);
        db.execSQL(create3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(upgrade_query);
        db.execSQL(upgrade2);
        db.execSQL(upgrade3);

        onCreate(db);
    }

    public  boolean DeleteForecast(){
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(DAILY_TABLE, null,null);
        if (result != -1 )
            return true;
        else
            return false;
    }

    public  boolean DeleteHours(){
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(HOUR_TABLE, null,null);
        if (result != -1 )
            return true;
        else
            return false;
    }

    public boolean AddDayForecast(Day day) {
        long result;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(D_COND, day.getCondition());
        cv.put(D_CICON, day.getConditionIcon());
        cv.put(D_HUM, day.getHumidity());
        cv.put(D_SPEED,day.getWind_speed());
        cv.put(D_DAY,day.getDayName());
        cv.put(D_TEMP,day.getAveTemp());
        cv.put(D_MAX,day.getMaxTemp());
        cv.put(D_MIN,day.getMinTemp());

        result = db.insert(DAILY_TABLE, null, cv);
        System.out.println(result);
        if (result != -1) {
            return true;
        }
        else
            return false;

    }

    public boolean AddHoursForDay(Hour hour) {
        long result;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(H_COND, hour.getCondition());
        cv.put(H_HOUR, hour.getHour());
        cv.put(H_TEMP, hour.getAveTemp());
        cv.put(D_DAY, hour.getDay());

        result = db.insert(HOUR_TABLE, null, cv);
        if (result != -1)
            return true;
        else
            return false;

    }
    public ArrayList<Hour> getHoursByDay(String extra_day_name) {
        ArrayList<Hour> forecast = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();

        String query = "SELECT * FROM " + HOUR_TABLE + " WHERE "+
                D_DAY +" = '"+extra_day_name+ "';";

        Cursor cursor=null;
        if (db != null)
            cursor = db.rawQuery(query, null);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String condition = cursor.getString(0);
                String hour = cursor.getString(1);
                double aveTemp = cursor.getDouble(2);
                String day = cursor.getString(3);

                forecast.add(new Hour(day, hour, condition, aveTemp));
            }
            cursor.close();
            return forecast;
        }
        cursor.close();
        return null;
    }


    public ArrayList<Day> getFourDayForecast () {
        ArrayList<Day> forecast = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();

        String query = "SELECT * FROM " + this.DAILY_TABLE + ";";

        Cursor cursor=null;
        if (db != null)
            cursor = db.rawQuery(query, null);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String condition = cursor.getString(0);
                String icon = cursor.getString(1);
                double humidity = cursor.getDouble(2);
                double wind_speed = cursor.getDouble(3);
                String dayName = cursor.getString(4);
                double aveTemp = cursor.getDouble(5);
                double maxTemp = cursor.getDouble(6);
                double minTemp = cursor.getDouble(7);

                forecast.add(new Day(dayName, condition, icon, aveTemp,
                        maxTemp, minTemp, humidity, wind_speed));
            }
            cursor.close();
            return forecast;
        }
        cursor.close();
        return null;
    }

    public boolean addCity(String city) {
        long result;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(CITY_NAME, city);

        result = db.insert(CITY_TABLE, null, cv);
        System.out.println(result);
        if (result != -1)
            return true;
        else
            return false;
    }

    public void deleteCity(String city) {

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "DELETE FROM "+DAILY_TABLE;
        db.execSQL(query);
    }

    public ArrayList<String> getPrefCities(){
        ArrayList<String> pCities = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();

        String query = "SELECT * FROM " + this.CITY_TABLE + ";";


        Cursor cursor=null;
        if (db != null)
            cursor = db.rawQuery(query, null);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String city = cursor.getString(0);

                pCities.add(city);
            }
            cursor.close();
            return pCities;
        }
        cursor.close();
        return null;
    }


}

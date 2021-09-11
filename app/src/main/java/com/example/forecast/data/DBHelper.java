package com.example.forecast.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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


    // Table information
    private static final String CREATE_POST_TABLE = "";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_query = "CREATE TABLE " + CITY_TABLE +
                " ("+ CITY_NAME + " TEXT);";

        db.execSQL(create_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String upgrade_query = "DROP TABLE IF EXISTS " + CITY_TABLE +";";
        db.execSQL(upgrade_query);
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

        db.delete(CITY_TABLE, "city = ?", new String[]{city});

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

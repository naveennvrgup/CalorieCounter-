package com.example.caloriecounter.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

import com.example.caloriecounter.model.Food;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(@Nullable Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table = String.format("create table %s ( " +
                "%s integer primary key," +
                "%s text" +
                "%s int" +
                "%d long);", Constants.KEY_ID, Constants.TABLE_NAME, Constants.FOOD_NAME, Constants.FOOD_CALORIES_NAME, Constants.DATE_NAME);
        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + Constants.TABLE_NAME);
    }

    public int getTotalItems() {
        SQLiteDatabase dbs = this.getReadableDatabase();
        Cursor cursor = dbs.rawQuery("select * from " + Constants.TABLE_NAME, null);
        return cursor.getCount();
    }

    public int totalCalories() {
        Cursor cursor = this.getReadableDatabase()
                .rawQuery("select sum("+Constants.FOOD_CALORIES_NAME+") " +
                        "from "+ Constants.TABLE_NAME,null);

        int total_calories=0;
        if(cursor.moveToFirst()){
            total_calories = cursor.getInt(0);
        }
        cursor.close();
        return  total_calories;
    }

    public void deleteFood(int id) {
        SQLiteDatabase dbw = getWritableDatabase();
        dbw.delete(Constants.TABLE_NAME, Constants.KEY_ID+" =? ", new String[]{String.valueOf(id)});
        dbw.close();
    }

    public void addFood(Food food) {
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(Constants.FOOD_NAME,food.getFoodName());
        values.put(Constants.FOOD_CALORIES_NAME,food.getCalories());
        values.put(Constants.DATE_NAME,System.currentTimeMillis());
        db.insert(Constants.TABLE_NAME,null,values);
        Log.v("db","added food item");
        db.close();
    }

    public ArrayList<Food> getAllFoods() {
        SQLiteDatabase db=getReadableDatabase();
        ArrayList<Food> foodList = new ArrayList<>();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{
                Constants.KEY_ID,
                Constants.FOOD_NAME,
                Constants.FOOD_CALORIES_NAME,
                Constants.DATABASE_NAME
        },null,null,null,null,Constants.DATE_NAME+" desc ");

        if(cursor.moveToFirst()){
            do{
                Food f=new Food();
                f.setFoodName(cursor.getString(cursor.getColumnIndex(Constants.FOOD_NAME)));
                f.setCalories(cursor.getInt(cursor.getColumnIndex(Constants.FOOD_CALORIES_NAME)));
                f.setFoodId(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));

                DateFormat dateFormat = DateFormat.getDateInstance();
                String date =dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.DATE_NAME))));
                f.setRecordDate(date);

                foodList.add(f);
            }while (cursor.moveToNext());
        }
        db.close();
        cursor.close();

        return foodList;
    }

}

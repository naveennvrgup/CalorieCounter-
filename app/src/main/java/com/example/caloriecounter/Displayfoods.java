package com.example.caloriecounter;

import android.os.Bundle;

import com.example.caloriecounter.data.CustomListViewAdapter;
import com.example.caloriecounter.model.Food;
import com.example.caloriecounter.util.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Displayfoods extends AppCompatActivity {
    private data.DatabaseHandler dba;
    private ArrayList<Food> dbFoods = new ArrayList<>();
    private CustomListViewAdapter foodAdapter;
    private ListView listView;

    private Food myFood;
    private TextView totalCals, totalFoods;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayfoods);

        listView = findViewById(R.id.list);
        totalCals = findViewById(R.id.totalAmountTextView);
        totalFoods = findViewById(R.id.totalItemsTextView);

        refreshData();
    }

    private void refreshData(){
        dbFoods.clear();

        dba=new data.DatabaseHandler(getApplicationContext());

        ArrayList<Food> foodsFromDB = dba.getFoods();

        String calsValue=String.valueOf(dba.totalCalories());
        String totalItems= String.valueOf(dba.getTotalItems());

        totalCals.setText("Total Calories: "+calsValue);
        totalFoods.setText("Total Foods: "+totalItems);

        for(int i=0;i<foodsFromDB.size();i++){
            myFood = new Food();
            myFood.setFoodName(foodsFromDB.get(i).getFoodName());
            myFood.setRecordDate(foodsFromDB.get(i).getRecordDate());
            myFood.setCalories(foodsFromDB.get(i).getCalories());
            myFood.setFoodId(foodsFromDB.get(i).getFoodId());

            Log.v("food ids: ", String.valueOf(foodsFromDB.get(i).getFoodId()));
            dbFoods.add(myFood);
        }

        dba.close();

        foodAdapter=new CustomListViewAdapter(Displayfoods.this,R.layout.list_row,dbFoods);
        listView.setAdapter(foodAdapter);
        foodAdapter.notifyDataSetChanged();
    }

}

package com.example.caloriecounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.caloriecounter.data.DatabaseHandler;
import com.example.caloriecounter.model.Food;

public class MainActivity extends AppCompatActivity {
    private EditText foodName, foodCals;
    private Button submitButton;
    private  DatabaseHandler dba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dba = new DatabaseHandler(MainActivity.this);

        foodName=findViewById(R.id.foodEditText);
        foodCals=findViewById(R.id.caloriesEditText);
        submitButton=findViewById(R.id.addFoodBtn);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToDB();
//                startActivity(new Intent(MainActivity.this,Displayfoods.class));
            }
        });
    }

    private  void saveDataToDB(){
        Food food = new Food();
        String name = foodName.getText().toString().trim();
        String calsString = foodCals.getText().toString().trim();

        int cals = Integer.parseInt(calsString);

        if (name.equals("") || calsString.equals("")) {

            Toast.makeText(getApplicationContext(), "No empty fields allowed", Toast.LENGTH_LONG).show();

        }else {

            food.setFoodName(name);
            food.setCalories(cals);

            dba.addFood(food);
            dba.close();


            //clear the form
            foodName.setText("");
            foodCals.setText("");

            //take users to next screen (display all entered items)
            startActivity(new Intent(MainActivity.this, Displayfoods.class));
        }

    }
}

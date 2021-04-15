package com.example.ucfgo_mobile_app;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class CarFinder extends AppCompatActivity {

    ImageView garageImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_finder);
        setTitle("Car Finder");

        garageImg = findViewById(R.id.garageImage);


    }
}

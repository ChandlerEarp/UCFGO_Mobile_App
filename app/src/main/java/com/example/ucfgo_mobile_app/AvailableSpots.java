package com.example.ucfgo_mobile_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AvailableSpots extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_spots);
        setTitle("Available Spots");
    }

    public void launchGarageA(View v){
        Intent i = new Intent(this, GarageA.class);
        startActivity(i);
    }

    public void launchGarageB(View v){
        Intent i = new Intent(this, GarageB.class);
        startActivity(i);
    }

    public void launchGarageC(View v){
        Intent i = new Intent(this, GarageC.class);
        startActivity(i);
    }

    public void launchGarageD(View v){
        Intent i = new Intent(this, GarageD.class);
        startActivity(i);
    }

//    public void launchGarageE(View v){
//        Intent i = new Intent(this, GarageE.class);
//        startActivity(i);
//    }
//    public void launchGarageF(View v){
//        Intent i = new Intent(this, GarageF.class);
//        startActivity(i);
//    }
//    public void launchGarageG(View v){
//        Intent i = new Intent(this, GarageG.class);
//        startActivity(i);
//    }
//    public void launchGarageH(View v){
//        Intent i = new Intent(this, GarageH.class);
//        startActivity(i);
//    }
//    public void launchGarageI(View v){
//        Intent i = new Intent(this, GarageI.class);
//        startActivity(i);
//    }
//    public void launchGarageLibra(View v){
//        Intent i = new Intent(this, GarageLibra.class);
//        startActivity(i);
//    }

}
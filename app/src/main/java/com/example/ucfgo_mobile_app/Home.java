package com.example.ucfgo_mobile_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
    public void launchSettings(View v){
        //Open the Settings Page
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }
}
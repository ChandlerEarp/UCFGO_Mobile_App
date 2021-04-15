package com.example.ucfgo_mobile_app;

import com.google.gson.annotations.SerializedName;

public class Post {
    private int userID;
    private char garage;
    private int spotNum;
    private boolean occupied;

    public int getUserID() {
        return userID;
    }

    public char getGarage() {
        return garage;
    }

    public int getSpotNum() {
        return spotNum;
    }

    public boolean isOccupied() {
        return occupied;
    }
}


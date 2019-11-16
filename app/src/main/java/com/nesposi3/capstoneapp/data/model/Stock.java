package com.nesposi3.capstoneapp.data.model;

import com.google.gson.annotations.SerializedName;

public class Stock {

    @SerializedName("Name")
    private String name;

    @SerializedName("Price")
    private int price;

    @SerializedName("NumShares")
    private int numShares;

    @SerializedName("PreviousNumShares")
    private int previousNumShares;

    @SerializedName("Trend")
    private boolean trend;

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getNumShares() {
        return numShares;
    }

    public int getPreviousNumShares() {
        return previousNumShares;
    }

    public boolean getTrend() {
        return trend;
    }
}

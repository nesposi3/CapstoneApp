package com.nesposi3.capstoneapp.data.model;

import com.google.gson.annotations.SerializedName;

public class Player {
    public boolean isDeleted() {
        return deleted;
    }

    public Dividend[] getPortfolio() {
        return portfolio;
    }

    public int getTotalCash() {
        return totalCash;
    }

    @SerializedName("Name")
    private String name;

    @SerializedName("Deleted")
    private boolean deleted;

    @SerializedName("Portfolio")
    private Dividend[] portfolio;

    @SerializedName("TotalCash")
    private int totalCash;

    public String getName() {
        return name;
    }
}

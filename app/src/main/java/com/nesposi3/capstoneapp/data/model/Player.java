package com.nesposi3.capstoneapp.data.model;

import com.google.gson.annotations.SerializedName;
import com.nesposi3.capstoneapp.data.StaticUtils;

public class Player implements Comparable<Player> {
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
    public int getPortfolioValue(){
        int i = 0;
        for (Dividend d :portfolio) {
            i+= d.getValue();
        }
        return i;
    }
    public String getPortfolioDollarValue(){
        return StaticUtils.centsToDolars(getPortfolioValue());
    }
    @Override
    public int compareTo(Player o) {
        if(this.getPortfolioValue() > o.getPortfolioValue()){
            return 1;
        } else if(this.getPortfolioValue()< o.getPortfolioValue()){
            return  -1;
        }else{
            return 0;
        }
    }
}

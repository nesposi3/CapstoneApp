package com.nesposi3.capstoneapp.data.model;

import com.google.gson.annotations.SerializedName;
import com.nesposi3.capstoneapp.data.StaticUtils;

public class Dividend {
    @SerializedName("BoughtStock")
    private Stock boughtStock;

    @SerializedName("Name")
    private String name;

    @SerializedName("NumShares")
    private int numShares;

    public Stock getBoughtStock() {
        return boughtStock;
    }

    public String getName() {
        return name;
    }

    public int getNumShares() {
        return numShares;
    }
    public int getValue(){
        return (numShares * boughtStock.getPrice());
    }
    public String getDollarValue(){
        return StaticUtils.centsToDolars(getValue());
    }
}

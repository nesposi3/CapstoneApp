package com.nesposi3.capstoneapp.data.model;

import com.google.gson.annotations.SerializedName;

public class GameState {
    @SerializedName("GameID")
    private String gameID;

    @SerializedName("Players")
    private Player[] players;

    @SerializedName("Stocks")
    private Stock[] stocks;

    @SerializedName("TicksLeft")
    private int ticksLeft;

    @SerializedName("Done")
    private boolean done;

    public String getGameID() {
        return gameID;
    }

    public Player[] getPlayers() {
        return players;
    }

    public Stock[] getStocks() {
        return stocks;
    }

    public int getTicksLeft() {
        return ticksLeft;
    }

    public boolean isDone() {
        return done;
    }
}

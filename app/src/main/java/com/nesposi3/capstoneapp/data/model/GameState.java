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
    public Stock getStock(String name){
        for (Stock s: stocks
             ) {
            if(s.getName().equals(name)){
                return s;
            }
        }
        return null;
    }
    public Player getPlayer(String name){
        for (Player p: players
             ) {
            if(p.getName().equals(name)){
                return p;
            }
        }
        return null;
    }
}

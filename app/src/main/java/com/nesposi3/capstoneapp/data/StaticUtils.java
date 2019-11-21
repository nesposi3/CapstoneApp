package com.nesposi3.capstoneapp.data;

public class StaticUtils {
    public static String minutesToTimeLeft(int minutes){
        int minsLeft = minutes % 60;
        minutes -=minsLeft;
        minutes /=60;
        int hoursLeft = minutes %24;
        minutes -=hoursLeft;
        int days = minutes/24;
        return days + " Days, " + hoursLeft + " Hours, " + minsLeft + " Minutes left.";
    }
    public static String centsToDolars(int cents){
        int dollars = (cents - (cents%100));
        int centsOut = cents%100;
        dollars /=100;
        return "$" + dollars + "." + String.format("%02d",centsOut);
    }
}

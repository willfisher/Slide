package com.retrochicken.slide.Scenes;

/**
 * Created by Will on 8/16/2016.
 */
public class CentralData {
    public static int GOLD = 0;

    public static int gamesPlayed;
    public static int score;

    private static int startGold = 0;
    public static void setStartGold(int startGoldNew) {
        startGold = startGoldNew;
    }
    public static int getStartGold() {
        return startGold;
    }
}

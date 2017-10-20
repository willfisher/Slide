package com.retrochicken.slide.Scenes;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;

import com.retrochicken.slide.Constants;
import com.retrochicken.slide.MenuButton;

/**
 * Created by Will on 2/5/2017.
 */
public class OptionsScene implements Scene {

    private float x1, y;
    private float slideX;

    private boolean slideOut;
    private MenuButton blackOrange, fadeTiles, back;

    public static boolean fadeTilesOn = true;
    public static boolean blackOrangeMode = false;

    private long startSlide;

    private final float buttonsStartX = 0;

    private static SharedPreferences optionData;

    public OptionsScene() {
        optionData = Constants.CURRENT_CONTEXT.getSharedPreferences("OPTIONS", Context.MODE_PRIVATE);
        fadeTilesOn = optionData.getBoolean("FADE_TILES", true);
        blackOrangeMode = optionData.getBoolean("BLACK_ORANGE", false);

        fadeTiles = new MenuButton(Color.rgb(116, 125, 125), Color.rgb(135, 206, 235), buttonsStartX, Constants.SCREEN_HEIGHT/3f, 4*Constants.SCREEN_WIDTH/5f, Constants.SCREEN_HEIGHT/15f, "TILE FADE: " + (fadeTilesOn ? "ON" : "OFF"), 60);
        blackOrange = new MenuButton(Color.rgb(116, 125, 125), Color.rgb(173, 255, 47), buttonsStartX, Constants.SCREEN_HEIGHT/3f + Constants.SCREEN_HEIGHT/10f, 2*Constants.SCREEN_WIDTH/3f, Constants.SCREEN_HEIGHT/15f, "B/O MODE: " + (blackOrangeMode ? "ON" : "OFF"), 80);
        back = new MenuButton(Color.rgb(116, 125, 125), Color.rgb(255, 127, 80), buttonsStartX, Constants.SCREEN_HEIGHT - Constants.SCREEN_HEIGHT/15f, Constants.SCREEN_WIDTH/2f, Constants.SCREEN_HEIGHT/15f, "BACK", 1000*0.25f);
    }

    @Override
    public void recieveTouch(MotionEvent event) {
        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                slideX = x1;
                y = event.getY();
                if(!slideOut) {
                    if(fadeTiles.isClicked(x1, y)) {
                        fadeTilesOn = !fadeTilesOn;
                        fadeTiles.setText("TILE FADE: " + (fadeTilesOn ? "ON" : "OFF"));
                        updateOptions();
                    } else if(blackOrange.isClicked(x1, y)) {
                        blackOrangeMode = !blackOrangeMode;
                        blackOrange.setText("B/O MODE: " + (blackOrangeMode ? "ON" : "OFF"));
                        updateOptions();
                    } else if(back.isClicked(x1, y)) {
                        slideOut = true;
                        startSlide = System.currentTimeMillis();
                    }
                }
                break;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        fadeTiles.draw(canvas);
        blackOrange.draw(canvas);
        back.draw(canvas);
    }

    @Override
    public void update(long timeMillis) {
        if (slideOut) {
            blackOrange.setX(blackOrange.getX() - blackOrange.getSlideSpeed());
            fadeTiles.setX(fadeTiles.getX() - fadeTiles.getSlideSpeed());
            back.setX(buttonsStartX - (int)(back.getWidth()*(System.currentTimeMillis() - startSlide)/back.getSlideSpeed()));
            if (blackOrange.getX() + blackOrange.getWidth() <= 0) {
                slideOut = false;
                blackOrange.setX(buttonsStartX);
                fadeTiles.setX(buttonsStartX);
                back.setX(buttonsStartX);
                SceneManager.ACTIVE_SCENE = 0;
            }
        }
    }

    private void updateOptions() {
        SharedPreferences.Editor editor = optionData.edit();
        editor.putBoolean("FADE_TILES", fadeTilesOn);
        editor.putBoolean("BLACK_ORANGE", blackOrangeMode);
        editor.apply();
    }
}

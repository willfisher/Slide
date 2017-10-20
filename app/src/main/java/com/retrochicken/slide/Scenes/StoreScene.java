package com.retrochicken.slide.Scenes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;

import com.retrochicken.slide.ColorStore;
import com.retrochicken.slide.Constants;
import com.retrochicken.slide.MenuButton;

/**
 * Created by Will on 2/5/2017.
 */
public class StoreScene implements Scene {
    private boolean slideOut;
    private MenuButton back;

    private long startSlide;

    private float x1, y;
    private float slideX;

    private final float buttonsStartX = 0;

    public static ColorStore backgroundColors;

    public StoreScene() {
        back = new MenuButton(Color.rgb(116, 125, 125), Color.rgb(255, 127, 80), buttonsStartX, Constants.SCREEN_HEIGHT - Constants.SCREEN_HEIGHT/15f, Constants.SCREEN_WIDTH/2f, Constants.SCREEN_HEIGHT/15f, "BACK", 1000*0.25f);
        backgroundColors = new ColorStore(Constants.CURRENT_CONTEXT.getSharedPreferences("STORE", Context.MODE_PRIVATE), "BG_STORE", "0,-1 0 true,-26215 100 false,-103 100 false,-4980839 100 false,-6684673 150 false,-6710785 150 false,-26113 200 false", "Background Store");
    }

    @Override
    public void recieveTouch(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                slideX = x1;
                y = event.getY();
                if(!slideOut) {
                    if (back.isClicked(x1, y)) {
                        slideOut = true;
                        startSlide = System.currentTimeMillis();
                        break;
                    }
                    backgroundColors.analyzeClick(event);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                backgroundColors.doSwipe(event.getX() - slideX);
                slideX = event.getX();
                break;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        backgroundColors.draw(canvas);
        back.draw(canvas);
    }

    @Override
    public void update(long timeMillis) {
        if(slideOut) {
            back.setX(buttonsStartX - (int)(back.getWidth()*(System.currentTimeMillis() - startSlide)/back.getSlideSpeed()));
            if (back.getX() + back.getWidth() <= 0) {
                slideOut = false;
                back.setX(buttonsStartX);
                SceneManager.ACTIVE_SCENE = 0;
            }
        }
    }
}

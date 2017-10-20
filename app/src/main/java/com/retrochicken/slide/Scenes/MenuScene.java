package com.retrochicken.slide.Scenes;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.retrochicken.slide.Constants;
import com.retrochicken.slide.MenuButton;
import com.retrochicken.slide.SlideManager;

/**
 * Created by Will on 8/16/2016.
 */
public class MenuScene implements Scene {
    private MenuButton play, store, options, howToPlay, stats;
    private boolean slideOut = false, option = false, htp = false, statistics = false, storeView = false;

    private final float buttonsStartX = 0;

    private long startSlide;

    public MenuScene() {
        play = new MenuButton(Color.rgb(116, 125, 125), Color.rgb(135, 206, 235), buttonsStartX, Constants.SCREEN_HEIGHT/3f, 2f*Constants.SCREEN_WIDTH/3f, Constants.SCREEN_HEIGHT/15f, "PLAY", 1000*0.2f);
        store = new MenuButton(Color.rgb(116, 125, 125), Color.RED, buttonsStartX, Constants.SCREEN_HEIGHT/3f + Constants.SCREEN_HEIGHT/10f, 3*Constants.SCREEN_WIDTH/4f, Constants.SCREEN_HEIGHT/15f, "STORE", 1000*0.25f);
        options = new MenuButton(Color.rgb(116, 125, 125), Color.rgb(173, 255, 47), buttonsStartX, Constants.SCREEN_HEIGHT/3f + 2*Constants.SCREEN_HEIGHT/10f, Constants.SCREEN_WIDTH/2f, Constants.SCREEN_HEIGHT/15f, "OPTIONS", 1000*0.3f);
        stats = new MenuButton(Color.rgb(116, 125, 125), Color.YELLOW, buttonsStartX, Constants.SCREEN_HEIGHT/3f + 3*Constants.SCREEN_HEIGHT/10f, 4f*Constants.SCREEN_WIDTH/5f, Constants.SCREEN_HEIGHT/15f, "STATS", 1000*0.2f);
        howToPlay = new MenuButton(Color.rgb(116, 125, 125), Color.rgb(255, 127, 80), buttonsStartX, Constants.SCREEN_HEIGHT/3f + 4*Constants.SCREEN_HEIGHT/10f, 2f*Constants.SCREEN_WIDTH/3f, Constants.SCREEN_HEIGHT/15f, "HOW TO PLAY", 1000*0.15f);
    }

    @Override
    public void recieveTouch(MotionEvent event) {
        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                float x1 = event.getX();
                float y = event.getY();
                if(!slideOut) {
                    if(play.isClicked(x1, y)) {
                        CentralData.setStartGold(CentralData.GOLD);
                        slideOut = true;
                        startSlide = System.currentTimeMillis();
                    } else if(options.isClicked(x1, y)) {
                        slideOut = true;
                        option = true;
                        startSlide = System.currentTimeMillis();
                    } else if(howToPlay.isClicked(x1, y)) {
                        slideOut = true;
                        htp = true;
                        startSlide = System.currentTimeMillis();
                    } else if(stats.isClicked(x1, y)) {
                        slideOut = true;
                        statistics = true;
                        startSlide = System.currentTimeMillis();
                    } else if(store.isClicked(x1, y)) {
                        slideOut = true;
                        storeView = true;
                        startSlide = System.currentTimeMillis();
                    }
                }
                break;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        play.draw(canvas);
        options.draw(canvas);
        howToPlay.draw(canvas);
        stats.draw(canvas);
        store.draw(canvas);
    }

    @Override
    public void update(long timeMillis) {
        if (slideOut) {
                    /*
                    play.setX(play.getX() - play.getSlideSpeed());
                    options.setX(options.getX() - options.getSlideSpeed());
                    howToPlay.setX(howToPlay.getX() - howToPlay.getSlideSpeed());
                    stats.setX(stats.getX() - stats.getSlideSpeed());
                    store.setX(store.getX() - store.getSlideSpeed());
                    */
            long currTime = System.currentTimeMillis();
            play.setX(buttonsStartX - (int)(play.getWidth()*(currTime - startSlide)/play.getSlideSpeed()));
            options.setX(buttonsStartX - (int)(options.getWidth()*(currTime - startSlide)/options.getSlideSpeed()));
            howToPlay.setX(buttonsStartX - (int)(howToPlay.getWidth()*(currTime - startSlide)/howToPlay.getSlideSpeed()));
            stats.setX(buttonsStartX - (int)(stats.getWidth()*(currTime - startSlide)/stats.getSlideSpeed()));
            store.setX(buttonsStartX - (int)(store.getWidth()*(currTime - startSlide)/store.getSlideSpeed()));
            if (play.getX() + play.getWidth() <= 0 && options.getX() + options.getWidth() <= 0
                    && howToPlay.getX() + howToPlay.getWidth() <= 0 && stats.getX() + stats.getWidth() <= 0) {
                slideOut = false;
                if(option)
                    SceneManager.ACTIVE_SCENE = 3;
                else if(htp)
                    SceneManager.ACTIVE_SCENE = 4;
                else if(statistics)
                    SceneManager.ACTIVE_SCENE = 5;
                else if(storeView)
                    SceneManager.ACTIVE_SCENE = 6;
                else {
                    ((GameplayScene)SceneManager.getScene(1)).refreshManager();
                    SceneManager.ACTIVE_SCENE = 1;
                }
                play.setX(buttonsStartX);
                options.setX(buttonsStartX);
                howToPlay.setX(buttonsStartX);
                stats.setX(buttonsStartX);
                store.setX(buttonsStartX);
                option = false;
                htp = false;
                statistics = false;
                storeView = false;
            }
        }
    }
}

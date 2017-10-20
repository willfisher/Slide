package com.retrochicken.slide.Scenes;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.retrochicken.slide.Constants;
import com.retrochicken.slide.R;
import com.retrochicken.slide.SlideManager;
import com.retrochicken.slide.Tile;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Will on 8/16/2016.
 */
public class GameplayScene implements Scene {
    private SlideManager slideManager;
    public void refreshManager() {
        CentralData.setStartGold(CentralData.GOLD);
        slideManager = new SlideManager(GOOD_COLOR_SLIDE.getPixel((int)(Math.random()*15.99), 0), currencies, values);
    }
    public static Bitmap GOOD_COLOR_SLIDE, BAD_COLOR_SLIDE;

    private Bitmap[] currencies;
    private int[][] values;

    public static int highScore;
    public static boolean newHighScore = false;

    private static SharedPreferences highScoreData, money;

    private HashMap<Integer, float[]> multiTouch;

    private ArrayList<Tile> deleteList;
    private static float deltaXPerFrame;
    private float x1, y;

    public static long deadTime;

    public GameplayScene() {
        multiTouch = new HashMap<>();

        deleteList = new ArrayList<>();
        deltaXPerFrame = Constants.SCREEN_WIDTH/30;

        GOOD_COLOR_SLIDE = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.safecolors);
        BAD_COLOR_SLIDE = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.badcolors);

        currencies = new Bitmap[]{BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.silvercoin),
                BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.goldcoin),
                BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.silverbar),
                BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.goldbar),
                BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.dollar),
                BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.dollarstack)};
        values = new int[][]{new int[]{1, 0}, new int[]{2, 1}, new int[]{3, 2}, new int[]{5, 3}, new int[]{10, 4}, new int[]{25, 6}};

        slideManager = new SlideManager(GOOD_COLOR_SLIDE.getPixel((int)(Math.random()*15.99), 0), currencies, values);

        money = Constants.CURRENT_CONTEXT.getSharedPreferences("MONEY", Context.MODE_PRIVATE);
        CentralData.GOLD = money.getInt("GOLD", 0);

        highScoreData = Constants.CURRENT_CONTEXT.getSharedPreferences("HIGH_SCORE", Context.MODE_PRIVATE);
        highScore = highScoreData.getInt("HIGH SCORE", 0);
    }

    @Override
    public void recieveTouch(MotionEvent event) {
        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y = event.getY();
                captureDown(event);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                captureDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                for(int i = 0; i < event.getPointerCount(); i++) {
                    processMove(event, i);
                }
                break;
            case MotionEvent.ACTION_UP:
                captureUp(event);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                captureUp(event);
                break;
        }
    }

    private void captureDown(MotionEvent event) {
        int index = event.getActionIndex();
        int id = event.getPointerId(index);
        float x = event.getX(index);
        float y = event.getY(index);
        int deleteID = slideManager.swipeID(x, y);
        if(deleteID >= 0)
            multiTouch.put(id, new float[]{x, y, deleteID});
    }

    private void captureUp(MotionEvent event) {
        int index = event.getActionIndex();
        int id = event.getPointerId(index);
        if(multiTouch.containsKey(id)) {
            float x2 = event.getX(index);
            float deltX = x2 - multiTouch.get(id)[0];
            Tile t = slideManager.getTileByID((int)multiTouch.get(id)[2]);
            if (t != null) {
                if (Math.abs(deltX) < t.getWidth() / 2f)
                    t.reset();
                else
                    deleteList.add(t);
            }
            multiTouch.remove(id);
        }
    }

    private void processMove(MotionEvent event, int index) {
        int id = event.getPointerId(index);
        float x2 = event.getX(index);
        if(multiTouch.containsKey(id)) {
            float deltaX = x2 - multiTouch.get(id)[0];
            Tile t = slideManager.getTileByID((int)multiTouch.get(id)[2]);
            if (t != null) {
                t.slideFade(deltaX);
                slideManager.checkCoins(t);
            }
        } else {
            float y = event.getY(index);
            int deleteID = slideManager.swipeID(x2, y);
            if(deleteID >= 0)
                multiTouch.put(id, new float[]{x2, y, deleteID});
        }
    }

    private void updateFadeTileWDelete(Tile t, float deltaX) {
        t.slideFade(deltaX);
        if (t.getAlpha() <= 0) {
            slideManager.destroyTileByID(t.getID());
        }
    }

    @Override
    public void draw(Canvas canvas) {
        slideManager.draw(canvas);
    }

    @Override
    public void update(long timeMillis) {
        for (int i = deleteList.size() - 1; i >= 0; i--) {
            Tile t = deleteList.get(i);
            float prevDelt = t.getX() - t.getStartX();
            boolean isNeg = prevDelt < 0;
            updateFadeTileWDelete(t, prevDelt + (isNeg ? -deltaXPerFrame : deltaXPerFrame));
            if (t.getAlpha() <= 0)
                deleteList.remove(i);
        }
        if(slideManager.getDead()) {
            SceneManager.ACTIVE_SCENE = 2;
            deadTime = System.currentTimeMillis();
            CentralData.score = slideManager.getScore();
            if(slideManager.getScore() > highScore) {
                newHighScore = true;
                highScore = slideManager.getScore();
                SharedPreferences.Editor editor = highScoreData.edit();
                editor.putInt("HIGH SCORE", highScore);
                editor.apply();
            }
        }
        slideManager.update(timeMillis);
    }

    public static void updateGold() {
        SharedPreferences.Editor editor = money.edit();
        editor.putInt("GOLD", CentralData.GOLD);
        editor.apply();
    }
}

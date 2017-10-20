package com.retrochicken.slide.Scenes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.retrochicken.slide.Constants;
import com.retrochicken.slide.GamePanel;
import com.retrochicken.slide.MenuButton;
import com.retrochicken.slide.SlideManager;

/**
 * Created by Will on 2/5/2017.
 */
public class HTPScene implements Scene {

    private boolean slideOut;
    private MenuButton back;

    private long startSlide;

    private float x1, y;
    private float slideX;

    private final float buttonsStartX = 0;

    private Paint htpText;

    public HTPScene() {
        back = new MenuButton(Color.rgb(116, 125, 125), Color.rgb(255, 127, 80), buttonsStartX, Constants.SCREEN_HEIGHT - Constants.SCREEN_HEIGHT/15f, Constants.SCREEN_WIDTH/2f, Constants.SCREEN_HEIGHT/15f, "BACK", 1000*0.25f);
        htpText = new Paint();
        htpText.setTextAlign(Paint.Align.CENTER);
        SlideManager.setTextSizeForDim(htpText, 4 * Constants.SCREEN_WIDTH / 5, 0, "Gain points when you swipe the bad tiles");
        htpText.setTypeface(GamePanel.GLOBAL_TYPEFACE);
    }

    @Override
    public void recieveTouch(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                slideX = x1;
                y = event.getY();
                if(!slideOut) {
                    if(back.isClicked(x1, y)) {
                        slideOut = true;
                        startSlide = System.currentTimeMillis();
                    }
                }
                break;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        String text = "Swipe the colors that stand out.\nGain points when you swipe the bad tiles\nand when good tiles reach the bottom.\nDon't swipe good tiles.\nTo swipe a tile your finger must\nstart on the tile.";
        float x = Constants.SCREEN_WIDTH/2, y = Constants.SCREEN_HEIGHT/3f;
        for (String line : text.split("\n")) {
            canvas.drawText(line, x, y, htpText);
            y += htpText.descent() - htpText.ascent();
        }
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

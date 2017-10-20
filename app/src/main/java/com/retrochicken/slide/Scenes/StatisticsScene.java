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
public class StatisticsScene implements Scene {

    private boolean slideOut;
    private MenuButton back;

    private long startSlide;

    private float x1, y;
    private float slideX;

    private final float buttonsStartX = 0;

    public StatisticsScene() {
        back = new MenuButton(Color.rgb(116, 125, 125), Color.rgb(255, 127, 80), buttonsStartX, Constants.SCREEN_HEIGHT - Constants.SCREEN_HEIGHT/15f, Constants.SCREEN_WIDTH/2f, Constants.SCREEN_HEIGHT/15f, "BACK", 1000*0.25f);
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
        Paint paint = new Paint();
        SlideManager.setTextSizeForDim(paint, 4 * Constants.SCREEN_WIDTH / 5, Constants.SCREEN_HEIGHT / 40, "Games Played: ");
        paint.setTypeface(GamePanel.GLOBAL_TYPEFACE);
        canvas.drawText("Games Played: " + CentralData.gamesPlayed, 10, Constants.SCREEN_HEIGHT / 3f, paint);
        canvas.drawText("High Score: " + GameplayScene.highScore, 10, Constants.SCREEN_HEIGHT/3f + 2*(paint.descent() - paint.ascent()), paint);
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

package com.retrochicken.slide;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.retrochicken.slide.Scenes.SceneManager;
import com.retrochicken.slide.Scenes.StoreScene;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Will on 2/22/2016.
 * Manages the entire game, basically handles "scenes" and touch events.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;

    public static final int BG_COLOR = Color.WHITE;//Color.rgb(240, 240, 240);

    public static Typeface GLOBAL_TYPEFACE;

    private Paint titlePaint;

    public GamePanel(Context context) {
        super(context);

        Constants.CURRENT_CONTEXT = context;

        GLOBAL_TYPEFACE = Typeface.createFromAsset(context.getAssets(), "NixieOne.ttf");

        //in order to intercept events
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        titlePaint = new Paint();
        titlePaint.setTypeface(GLOBAL_TYPEFACE);
        SlideManager.setTextSizeForDim(titlePaint, Constants.SCREEN_WIDTH - Constants.SCREEN_WIDTH / 4, Constants.SCREEN_HEIGHT / 6, "SLIDE");
        titlePaint.setTextAlign(Paint.Align.CENTER);

        //make gamePanel focusable so it can handle events
        setFocusable(true);

        SceneManager.initScenes();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while(retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch(InterruptedException e) {e.printStackTrace();}
            retry = false;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(), this);
        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        SceneManager.recieveTouch(event);
        return true;
    }

    public void update(long timeMillis) {
        SceneManager.update(timeMillis);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(((StoreScene)SceneManager.getScene(6)).backgroundColors != null)
            canvas.drawColor(((StoreScene)SceneManager.getScene(6)).backgroundColors.getActiveColor());
        else
            canvas.drawColor(BG_COLOR);
        if(SceneManager.ACTIVE_SCENE == 0 || SceneManager.ACTIVE_SCENE == 3 || SceneManager.ACTIVE_SCENE == 4 || SceneManager.ACTIVE_SCENE == 5)
            canvas.drawText("SLIDE", Constants.SCREEN_WIDTH / 2, titlePaint.descent() - titlePaint.ascent(), titlePaint);
        SceneManager.draw(canvas);
    }
}
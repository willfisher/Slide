package com.retrochicken.slide.Scenes;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by Will on 8/16/2016.
 */
public interface Scene {
    public void recieveTouch(MotionEvent event);
    public void draw(Canvas canvas);
    public void update(long timeMillis);
}

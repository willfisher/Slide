package com.retrochicken.slide;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Will on 3/6/2016.
 * Class that can take the form of any currency to be displayed.
 */
public class Coin {
    private long totalTime;
    private static final int bobTime = 1000;
    private float originalY;
    private float bobHeight;
    Bitmap image;

    private float x, y;
    private int value;

    public Coin(float x, float y, Bitmap image, int value) {
        this.x = x;
        originalY = y;
        this.y = y;
        this.value = value;
        this.image = image;
        bobHeight = 2*image.getHeight()/3.0f;
        totalTime = 0;
    }

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }

    public void update(long timeMillis) {
        totalTime += timeMillis;
        y = originalY + bobHeight*(float)Math.abs(Math.sin(Math.PI*totalTime/bobTime));
    }

    public int getWidth() {
        return image.getWidth();
    }
    public int getHeight() {
        return image.getHeight();
    }

    public int getValue() {
        return value;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y - image.getHeight(), null);
    }

    public long getTotalTime() {
        return totalTime;
    }
}

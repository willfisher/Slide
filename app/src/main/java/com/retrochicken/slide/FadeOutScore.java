package com.retrochicken.slide;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Will on 2/25/2016.
 * Allows for a +number to be displayed and fade upwards.
 */
public class FadeOutScore {
    private int color;
    private int value;
    public int getValue() {
        return value;
    }

    private float x;
    private float y;
    private float width;
    private float height;

    private float startY;

    private float totalMoveUp;

    private int alpha;
    public int getAlpha() {
        return alpha;
    }
    private double deltaAlpha;

    private Paint textPaint;

    private static final int alphaFrames = 30;

    public FadeOutScore(int color, int value, float x, float y, float width, float height, float totalMoveUp) {
        this.color = color;
        this.value = value;
        deltaAlpha = 255.0/alphaFrames;
        this.x = x;
        startY = y;
        this.y = y;
        this.width = width;
        this.height = height;
        textPaint = new Paint();
        textPaint.setTypeface(GamePanel.GLOBAL_TYPEFACE);
        textPaint.setColor(color);
        SlideManager.setTextSizeForDim(textPaint, width, height, "+" + value);
        this.totalMoveUp = totalMoveUp;
        this.alpha = 255;
        if(value == 0)
            this.alpha = 0;
    }

    public void update() {
        //TODO establish frame independence
        alpha = (int)(alpha - deltaAlpha);
        y -= totalMoveUp/alphaFrames;
        if(alpha > 0)
            textPaint.setColor(Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color)));
    }

    public void reset(int value) {
        this.value = value;
        alpha = 255;
        y = startY;
    }

    public void draw(Canvas canvas) {
        if(alpha > 0)
            canvas.drawText("+" + value, x, y, textPaint);
    }
}

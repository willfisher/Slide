package com.retrochicken.slide;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Tile {
    private float x;
    private float startX;
    public float getStartX() {
        return startX;
    }
    private float y;

    private float width;
    private float height;

    private Paint paint;
    private int darkColor;
    private int rgbColor;

    private int alpha;
    public int getAlpha() {
        return alpha;
    }

    private boolean isBad;

    private int ID;

    public Tile(float x, float y, float width, float height, int rgbColor, boolean isBad, int ID) {
        this.x = x;
        startX = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rgbColor = rgbColor;
        this.isBad = isBad;
        this.ID = ID;
        float[] hsv = new float[3];
        Color.colorToHSV(rgbColor, hsv);
        hsv[2] *= 0.8f; // value component
        darkColor = Color.HSVToColor(hsv);
        paint = new Paint();
        alpha = 255;
    }

    public void setX(float x) {
        this.x = x;
    }
    public void setY(float y) {
        this.y = y;
    }
    public void setWidth(float width) {
        this.width = width;
    }
    public void setHeight(float height) {
        this.height = height;
    }
    public void setColor(int rgbColor) {
        this.rgbColor = rgbColor;
    }
    public int getColor() {
        return rgbColor;
    }
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    public float getWidth() {
        return width;
    }
    public float getHeight() {
        return height;
    }
    public int getRGBColor() {
        return rgbColor;
    }
    public boolean getIsBad() {
        return isBad;
    }
    public int getID() {
        return ID;
    }

    public void slideFade(float deltaX) {
        x = startX + deltaX;
        alpha = (int)(255 - (255.0/width)*Math.abs(deltaX));
        if(alpha > 0) {
            updateColor();
        }
    }

    private void updateColor() {
        rgbColor = Color.argb(alpha, Color.red(rgbColor), Color.green(rgbColor), Color.blue(rgbColor));
        darkColor = Color.argb(alpha, Color.red(darkColor), Color.green(darkColor), Color.blue(darkColor));
    }

    public void reset() {
        alpha = 255;
        updateColor();
        x = startX;
    }

    public void updateDarkColor() {
        float[] hsv = new float[3];
        Color.colorToHSV(rgbColor, hsv);
        hsv[2] *= 0.8f; // value component
        darkColor = Color.HSVToColor(hsv);
    }

    public void draw(Canvas canvas) {
        if(alpha > 0) {
            paint.setColor(rgbColor);
            canvas.drawRect(x, y - height, x + width, y, paint);
            paint.setColor(darkColor);
            canvas.drawRect(x + width, y - height, x + width + width / 20, y, paint);
        }
    }
}

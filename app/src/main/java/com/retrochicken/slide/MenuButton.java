package com.retrochicken.slide;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Will on 2/24/2016.
 */
public class MenuButton {
    private int color;
    private int tabColor;

    private float x;
    private float y;
    private float width;
    private float height;

    private float slideSpeed;
    public float getSlideSpeed() {
        return slideSpeed;
    }

    public int getColor() {
        return color;
    }
    public int getTabColor() {
        return tabColor;
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
    public Paint getTextPaint() {
        return textPaint;
    }
    public void setColor(int color) {
        this.color = color;
    }
    public void setTabColor(int tabColor) {
        this.tabColor = tabColor;
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
    public void setTextPaint(Paint textPaint) {
        this.textPaint = textPaint;
    }

    private Paint textPaint;
    private Paint mainPaint;
    private Paint tabPaint;

    private String text;

    public void setText(String text) {
        this.text = text;
        SlideManager.setTextSizeForDim(textPaint, 2*width/3f, 13*height/30f, text);
    }

    public MenuButton(int color, int tabColor, float x, float y, float width, float height, String text, float slideSpeed) {
        this.color = color;
        this.tabColor = tabColor;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        textPaint = new Paint();
        SlideManager.setTextSizeForDim(textPaint, 2*width/3f, 13*height/30f, text);
        textPaint.setColor(GamePanel.BG_COLOR);
        textPaint.setTypeface(GamePanel.GLOBAL_TYPEFACE);
        mainPaint = new Paint();
        mainPaint.setColor(color);
        tabPaint = new Paint();
        tabPaint.setColor(tabColor);
        this.slideSpeed = slideSpeed;
    }

    public boolean isClicked(float x, float y) {
        return x >= this.x && x <= this.x + width && y >= this.y - height && y <= this.y;
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(x, y - height, x + width, y, mainPaint);
        canvas.drawRect(x + width, y - height, x + width + Constants.SCREEN_WIDTH/50f, y, tabPaint);
        canvas.drawText(text, x + width/20f, y - height/4f, textPaint);
    }
}

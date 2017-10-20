package com.retrochicken.slide;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class StoreColor {
    private int color;

    private float x, y, width, height;

    private int cost;
    private boolean bought;

    private Paint tilePaint = new Paint();

    private boolean isSelected = false;

    private boolean isActive = false;

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
    public boolean getIsSelected() {
        return isSelected;
    }
    public boolean getBought() {
        return bought;
    }

    public int getCost() {
        return cost;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public float getY() {
        return y;
    }
    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }
    public float getHeight() {
        return height;
    }
    public float getWidth() {
        return width;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }

    public StoreColor(int color, int cost, boolean bought, float width, float height, float x, float y, boolean isActive) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.cost = cost;
        this.isActive = isActive;
        tilePaint.setColor(color);
    }

    public int getColor() {
        return color;
    }

    public StoreColor(String data, float width, float height, float x, float y, boolean isActive) {
        String[] parsed = data.split(" ");
        color = Integer.parseInt(parsed[0]);
        cost = Integer.parseInt(parsed[1]);
        bought = parsed[2].toLowerCase().equals("true");
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.isActive = isActive;
        tilePaint.setColor(color);
    }

    public String toString() {
        return "" + color + " " + cost + " " + (bought ? "true" : "false");
    }

    public void draw(Canvas canvas) {
        if(isActive || isSelected) {
            Paint p = new Paint();
            if(isActive)
                p.setColor(Color.BLACK);
            else if(isSelected)
                p.setColor(Color.GREEN);
            canvas.drawRect(x - width/10, y - height - height/10, x + width + width/10, y + height/10, p);
        }
        canvas.drawRect(x, y - height, x + width, y, tilePaint);
    }

    public boolean inBounds(float x, float y) {
        return this.x <= x && this.x + width >= x && this.y >= y && this.y - height <= y;
    }
}

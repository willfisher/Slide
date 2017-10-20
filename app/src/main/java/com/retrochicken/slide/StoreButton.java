package com.retrochicken.slide;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class StoreButton {
    private float x;
    private float y;
    private float width;
    private float height;
    private String text;
    private int color;
    private int tabColor;
    private Paint textPaint = new Paint();

    public StoreButton(float x, float y, float width, float height, int color, int tabColor, String text) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.tabColor = tabColor;
        this.text = text;
        textPaint.setTypeface(GamePanel.GLOBAL_TYPEFACE);
        textPaint.setFakeBoldText(true);
        SlideManager.setTextSizeForDim(textPaint, 2*width/3f, 13*height/30f, text);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setText(String text) {
        this.text = text;
        SlideManager.setTextSizeForDim(textPaint, 2*width/3f, 13*height/30f, text);
    }

    public void draw(Canvas canvas) {
        float tabSize = width/20;
        Paint p = new Paint();
        p.setColor(tabColor);
        canvas.drawRect(x, y - height, x + tabSize, y, p);
        canvas.drawRect(x + width - tabSize, y - height, x + width, y, p);
        p.setColor(color);
        canvas.drawRect(x + tabSize, y - height, x + width - tabSize, y, p);
        canvas.drawText(text, x + width/2f, y - height/4f, textPaint);
    }

    public boolean isClicked(float x, float y) {
        return this.x <= x && this.x + width >= x && this.y >= y && this.y - height <= y;
    }
}
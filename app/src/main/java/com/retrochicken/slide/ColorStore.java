package com.retrochicken.slide;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.retrochicken.slide.Scenes.CentralData;
import com.retrochicken.slide.Scenes.GameplayScene;

import java.util.ArrayList;

public class ColorStore {
    private ArrayList<StoreColor> colors;

    private int activeIndex = -1;

    private boolean canSlide = false;

    private int selectedIndex = -1;

    private StoreButton buy, equip;

    private int numPerRow;
    private float paddingRL;

    private SharedPreferences storeData;
    private String storeID;

    private String storeName;

    private static Paint titlePaint;

    public ColorStore(SharedPreferences storeData, String storeID, String defaultStr, String storeName) {
        this.storeData = storeData;
        colors = new ArrayList<>();
        this.storeID = storeID;
        String colorData = storeData.getString(storeID, defaultStr);
        initializeColors(colorData);
        this.storeName = storeName;
        titlePaint = new Paint();
        titlePaint.setTypeface(GamePanel.GLOBAL_TYPEFACE);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        SlideManager.setTextSizeForDim(titlePaint, 3 * Constants.SCREEN_WIDTH / 4, Constants.SCREEN_HEIGHT / 13, storeName);
        float width = Math.min(Constants.X_PIXELS_TO_INCH, Constants.SCREEN_WIDTH/2);
        float x = (Constants.SCREEN_WIDTH - width)/2;
        float height = Math.min(Constants.SCREEN_HEIGHT / 12, Constants.Y_PIXELS_TO_INCH / 3);
        float y = colors.get(0).getY() - colors.get(0).getHeight() - height/2;
        buy = new StoreButton(x, y, width, height, Color.rgb(116, 125, 125), Color.GREEN, "BUY");
        equip = new StoreButton(x, y, width, height, Color.rgb(116, 125, 125), Color.GREEN, "EQUIP");
    }

    public void analyzeClick(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if(selectedIndex >= 0) {
            if(colors.get(selectedIndex).getBought() && equip.isClicked(x, y)) {
                if(activeIndex >= 0)
                    colors.get(activeIndex).setActive(false);
                activeIndex = selectedIndex;
                colors.get(activeIndex).setActive(true);
                update();
                return;
            } else if(!colors.get(selectedIndex).getBought() && buy.isClicked(x, y)) {
                if(CentralData.GOLD >= colors.get(selectedIndex).getCost()) {
                    CentralData.GOLD -= colors.get(selectedIndex).getCost();
                    colors.get(selectedIndex).setBought(true);
                    GameplayScene.updateGold();
                    update();
                }
                return;
            }
        }
        for (int i = 0; i < colors.size(); i++) {
            if (colors.get(i).inBounds(x, y)) {
                boolean wasSelected = colors.get(i).getIsSelected();
                colors.get(i).setSelected(!wasSelected);
                if (selectedIndex >= 0)
                    colors.get(selectedIndex).setSelected(false);
                if (wasSelected)
                    selectedIndex = -1;
                else {
                    selectedIndex = i;
                }
                break;
            }
        }
    }

    private void initializeColors(String colorData) {
        String[] data = colorData.split(",");
        if(data.length > 1) {
            float width = Constants.X_PIXELS_TO_INCH/3f;
            float height = Constants.Y_PIXELS_TO_INCH/3f;
            int numPerRow = (int)((Constants.SCREEN_WIDTH - width/4f)/(width + width/4f));
            float paddingRL = (Constants.SCREEN_WIDTH - numPerRow*width)/(float)(numPerRow + 1);
            int rows = (int)((Constants.SCREEN_HEIGHT/2f + height/4f)/(height + height/4f));
            float paddingTB = (Constants.SCREEN_HEIGHT/2f - rows*height)/(float)(rows - 1);
            paddingTB = Math.min(paddingTB, height/2);
            activeIndex = Integer.parseInt(data[0]);
            float y;
            float x;
            int count = 0;
            if(rows * numPerRow < data.length - 1) {
                numPerRow += (int)Math.ceil((data.length - 1 - rows*numPerRow)/(double)rows);
                canSlide = true;
            }
            this.numPerRow = numPerRow;
            this.paddingRL = paddingRL;
            OUTER:
            for(int i = 0; i < rows; i++) {
                for(int e = 0; e < numPerRow; e++) {
                    if(count + 1 < data.length) {
                        y = Constants.SCREEN_HEIGHT / 3 + height + i * (height + paddingTB);
                        x = paddingRL + (count % numPerRow) * (width + paddingRL);
                        colors.add(new StoreColor(data[count + 1], width, height, x, y, count == activeIndex));
                        count++;
                    } else
                        break OUTER;
                }
            }
        }
    }

    public void draw(Canvas canvas) {
        titlePaint.setFakeBoldText(true);
        canvas.drawText(storeName, Constants.SCREEN_WIDTH/2, (titlePaint.descent() - titlePaint.ascent()), titlePaint);
        titlePaint.setFakeBoldText(false);
        canvas.drawText("Gold: " + CentralData.GOLD, Constants.SCREEN_WIDTH/2, 2*(titlePaint.descent() - titlePaint.ascent()), titlePaint);
        if(selectedIndex != -1 && !colors.get(selectedIndex).getBought())
            canvas.drawText("Cost: " + colors.get(selectedIndex).getCost(), Constants.SCREEN_WIDTH/2, 3*(titlePaint.descent() - titlePaint.ascent()), titlePaint);
        for(StoreColor col : colors)
            col.draw(canvas);
        if(selectedIndex >= 0 && selectedIndex < colors.size()) {
            if(!colors.get(selectedIndex).getBought())
                buy.draw(canvas);
            else if(activeIndex != selectedIndex)
                equip.draw(canvas);
        }
    }

    public void doSwipe(float deltaX) {
        if(canSlide) {
            if(deltaX < 0) {
                float val = colors.get(numPerRow - 1).getX() + colors.get(numPerRow - 1).getWidth() + paddingRL;
                if(val + deltaX < Constants.SCREEN_WIDTH)
                    deltaX = Constants.SCREEN_WIDTH - val;
                moveAll(deltaX);
            } else if(deltaX > 0) {
                if(colors.get(0).getX() - paddingRL + deltaX > 0)
                    deltaX = 0 - colors.get(0).getX() + paddingRL;
                moveAll(deltaX);
            }
        }
    }
    private void moveAll(float deltaX) {
        for(StoreColor sc : colors)
            sc.setX(sc.getX() + deltaX);
    }

    private void update() {
        SharedPreferences.Editor editor = storeData.edit();
        editor.putString(storeID, toString());
        editor.apply();
    }

    public String toString() {
        String result = activeIndex + ",";
        for(StoreColor col : colors) {
            result += col.toString() + ",";
        }
        return result.substring(0, result.length() - 1);
    }

    public int getActiveColor() {
        return colors.get(activeIndex).getColor();
    }
}

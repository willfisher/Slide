package com.retrochicken.slide;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.retrochicken.slide.Scenes.GameplayScene;
import com.retrochicken.slide.Scenes.OptionsScene;

import java.util.ArrayList;

/**
 * Created by Will, I forgot when.
 * Manages the CoinManager and the Tiles, basically the manager of the entire game play.
 */

public class SlideManager {
    private Paint textPaint;

    private CoinManager coinManager;

    private int IDCount = 0;

    private int score = 0;
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }

    private ArrayList<Tile> tiles;

    private float sizeGoal;

    private double speedIncrement = 0.05; //per 0.25 seconds
    //private final double SPEED_CAP = 40;
    private double speed = 1;
    private static double SPEED_MAX;

    private static double badChanceIncrement = 0.00025;
    private static final double BAD_CHANCE_CAP = 0.3;
    private double badChance = 0.2;

    private int badCount = 0;
    private static final int BAD_MAX_IN_A_ROW = 2;

    private double maxTileHeight;
    private double minTileHeight;
    private double heightRandomScale;
    private float tileWidth;

    private static final double targetTime = 1000.0/30.0;
    //private static final double speedScale = 0.9;

    private float size;

    private int safeColor;
    //private final double COLOR_CHANGE_THRESHOLD = 0;

    private int scoreTime;
    private int totalTime;

    private float tileXPos;

    private boolean dead = false;
    public boolean getDead() {
        return dead;
    }
    public void setDead(boolean dead) {
        this.dead = dead;
    }

    private FadeOutScore swipeBonus;

    public SlideManager(int safeColor, Bitmap[] currencies, int[][] values) {
        tiles = new ArrayList<>();
        sizeGoal = (float)(3.0*Constants.SCREEN_HEIGHT/2.0);
        maxTileHeight = 5*Constants.Y_PIXELS_TO_INCH/8;
        minTileHeight = Constants.Y_PIXELS_TO_INCH/2;
        heightRandomScale = maxTileHeight - minTileHeight;
        size = 0;
        SPEED_MAX = Constants.Y_PIXELS_TO_INCH/10;
        this.safeColor = safeColor;
        tileWidth = Constants.SCREEN_WIDTH/3.0f;
        tileXPos = Constants.SCREEN_WIDTH/2.0f - Constants.SCREEN_WIDTH/12.0f;
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.LEFT);
        setTextSizeForDim(textPaint, Constants.SCREEN_WIDTH / 3f, Constants.SCREEN_HEIGHT / 10f, "1000");
        textPaint.setTypeface(GamePanel.GLOBAL_TYPEFACE);
        swipeBonus = new FadeOutScore(Color.BLACK, 0, Constants.SCREEN_WIDTH / 25f, Constants.SCREEN_HEIGHT / 3, Constants.SCREEN_WIDTH / 3f, Constants.SCREEN_HEIGHT / 20f, Constants.SCREEN_HEIGHT/9);
        for(int i = 0; i < 2; i++) {
            tiles.add(generateSafeTile());
            updateSize();
        }
        coinManager = new CoinManager(currencies, values, tileXPos, tileWidth);
        populateTiles();
    }

    public void update(long timeMillis) {
        coinManager.update(timeMillis);
        double scale = (double)timeMillis/targetTime;

        totalTime += timeMillis;
        scoreTime += timeMillis;
        if(scoreTime >= 500) {
            score++;
            scoreTime -= 500;
        }

        swipeBonus.update();
        double x = 2.0*totalTime/targetTime;
        speed = (2.5*Math.pow(x, 1.35))/(x+2);
        if(speed > SPEED_MAX)
            speed = SPEED_MAX;
        if(badChance + badChanceIncrement * scale <= BAD_CHANCE_CAP)
            badChance += badChanceIncrement * scale;
        for(Tile t : tiles)
            t.setY(t.getY() + (float)(speed*scale));

        //assuming bottom anchoring on rectangles
        if(tiles.get(0).getY() - tiles.get(0).getHeight() > Constants.SCREEN_HEIGHT)
            destroyTile(0);
        populateTiles();
    }
    public void draw(Canvas canvas) {
        coinManager.draw(canvas);
        canvas.drawText("" + score, Constants.SCREEN_WIDTH / 25f, Constants.SCREEN_HEIGHT / 9, textPaint);
        for(Tile t : tiles) {
            t.draw(canvas);
        }
        swipeBonus.draw(canvas);
    }
    public static void setTextSizeForDim(Paint paint, float desiredWidth, float desiredHeight,
                                            String text) {
        if(desiredHeight == 0) {
            final float testTextSize = 48f;

            // Get the bounds of the text, using our testTextSize.
            paint.setTextSize(testTextSize);
            Rect bounds = new Rect();
            paint.getTextBounds(text, 0, text.length(), bounds);

            // Calculate the desired size as a proportion of our testTextSize.
            float desiredTextSize = testTextSize * desiredWidth / bounds.width();

            // Set the paint for that size.
            paint.setTextSize(desiredTextSize);
        } else if(desiredWidth == 0) {
            final float testTextSize = 48f;

            // Get the bounds of the text, using our testTextSize.
            paint.setTextSize(testTextSize);
            Rect bounds = new Rect();
            paint.getTextBounds(text, 0, text.length(), bounds);

            // Calculate the desired size as a proportion of our testTextSize.
            float desiredTextSize = testTextSize * desiredHeight / bounds.height();

            // Set the paint for that size.
            paint.setTextSize(desiredTextSize);
        } else {
            final float testTextSize = 48f;

            // Get the bounds of the text, using our testTextSize.
            paint.setTextSize(testTextSize);
            Rect bounds = new Rect();
            paint.getTextBounds(text, 0, text.length(), bounds);

            // Calculate the desired size as a proportion of our testTextSize.
            float desiredTextSize = testTextSize * desiredWidth / bounds.width();
            float desiredHeightSize = testTextSize * desiredHeight / bounds.height();

            // Set the paint for that size.
            paint.setTextSize(Math.min(desiredTextSize, desiredHeightSize));
        }
    }

    private void populateTiles() {
        updateSize();
        while(size < sizeGoal) {
            updateSize();
            if(badCount < BAD_MAX_IN_A_ROW && Math.random() <= badChance) {
                badCount++;
                tiles.add(generateBadTile());
            } else {
                tiles.add(generateSafeTile());
                badCount = 0;
            }
        }
    }

    public void destroyTile(int index) {
        float tileSize = tiles.get(index).getHeight();
        if(index != 0) {
            for(int i = index + 1; i < tiles.size(); i++) {
                tiles.get(i).setY(tiles.get(i).getY() + tileSize);
            }
            if(tiles.get(index).getIsBad()) {
                score++;
                if(swipeBonus.getAlpha() > 0)
                    swipeBonus.reset(swipeBonus.getValue() + 1);
                else
                    swipeBonus.reset(1);
            } else {
                GameplayScene.updateGold();
                dead = true;
            }
        } else {
            if(tiles.get(index).getIsBad())
                dead = true;
            /*else
                score++;*/
        }
        tiles.remove(index);
        populateTiles();
    }
    public void destroyTileByID(int ID) {
        int tileIndex = -1;
        for(int i = 0; i < tiles.size(); i++) {
            if(tiles.get(i).getID() == ID) {
                tileIndex = i;
                break;
            }
        }
        if(tileIndex != -1)
            destroyTile(tileIndex);

    }

    private Tile generateSafeTile() {
        IDCount++;
        float blockHeight = (float)(Math.random()*heightRandomScale + minTileHeight);
        int randColor = safeColor;
        if(OptionsScene.blackOrangeMode)
            randColor = Color.BLACK;
        return new Tile(tileXPos, tiles.size() > 0 ? tiles.get(0).getY() - size : Constants.SCREEN_HEIGHT, tileWidth, blockHeight, randColor, false, IDCount);
    }
    private Tile generateBadTile() {
        IDCount++;
        float blockHeight = (float)(Math.random()*heightRandomScale + minTileHeight);
        int randColor;
        if(OptionsScene.blackOrangeMode)
            randColor = Color.rgb(255, 127, 80);
        else {
            randColor = GameplayScene.BAD_COLOR_SLIDE.getPixel((int)(Math.random()*15.99), 0);
        }
        return new Tile(tileXPos, tiles.size() > 0 ? tiles.get(0).getY() - size : Constants.SCREEN_HEIGHT, tileWidth, blockHeight, randColor, true, IDCount);
    }

    private int generateRandomColor(int minChange, double changeThreshold, int currColor) {
        double tempColorChange = changeThreshold;
        boolean isNeg = Math.random() <= 0.5;
        int red = Color.red(currColor);
        tempColorChange = modColorChange(isNeg, red + (isNeg ? -minChange : minChange), changeThreshold);
        red = DeadBandRGB(red + (isNeg ? -(minChange + Math.random()*tempColorChange) : (minChange + Math.random()*tempColorChange)));
        int green = Color.green(currColor);
        isNeg = Math.random() <= 0.5;
        tempColorChange = modColorChange(isNeg, green + (isNeg ? -minChange : minChange), changeThreshold);
        green = DeadBandRGB(green + (isNeg ? -(minChange + Math.random()*tempColorChange) : (minChange + Math.random()*tempColorChange)));
        int blue = Color.blue(currColor);
        isNeg = Math.random() <= 0.5;
        tempColorChange = modColorChange(isNeg, blue + (isNeg ? -minChange : minChange), changeThreshold);
        blue = DeadBandRGB(blue + (isNeg ? -(minChange + Math.random()*tempColorChange) : (minChange + Math.random()*tempColorChange)));
        return Color.rgb(red, green, blue);
    }
    private int generateRandomColorHSV(int minChange, double changeThreshold, int currColor) {
        boolean isNeg = Math.random() <= 0.5;
        float[] hsv = new float[3];
        Color.colorToHSV(currColor, hsv);
        float h = hsv[0];
        h += isNeg ? (-(minChange + Math.random()*changeThreshold)) : (minChange + Math.random()*changeThreshold);
        if(h < 0)
            h += 359;
        hsv[0] = h;
        return Color.HSVToColor(hsv);
    }
    private double modColorChange(boolean isNeg, int rgb, double currThreshold) {
        if(isNeg) {
            if(rgb - currThreshold < 0)
                return rgb;
        } else {
            if(255 - rgb - currThreshold < 0)
                return 255 - rgb;
        }
        return currThreshold;
    }
    private int DeadBandRGB(double rgb) {
        if(rgb < 0)
            return 0;
        else if(rgb > 255)
            return 255;
        return (int)rgb;
    }

    //returns index of tile that was swipped out
    public int swipeID(float x, float y) {
        if(x >= tileXPos && x <= tileXPos + tileWidth) {
            //assumes bottom anchoring on rectangles
            for(Tile temp : tiles) {
                if(y <= temp.getY() && y >= temp.getY() - temp.getHeight()) {
                    if(temp.getY() < Constants.SCREEN_HEIGHT)
                        return temp.getID();
                    else
                        return -1;
                }
            }
        }
        return -1;
    }
    public int swipeIndex(float x, float y) {
        if(x >= tileXPos && x <= tileXPos + tileWidth) {
            //assumes bottom anchoring on rectangles
            for(int i = 0; i < tiles.size(); i++) {
                Tile temp  = tiles.get(i);
                if(y <= temp.getY() && y >= temp.getY() - temp.getHeight())
                    return i;
            }
        }
        return -1;
    }

    private void updateSize() {
        size = 0;
        if(tiles.size() > 0) {
            Tile temp  = tiles.get(tiles.size() - 1);
            size = Math.abs(tiles.get(0).getY() - temp.getY() + temp.getHeight());
        }
    }

    public void checkCoins(Tile t) {
        coinManager.overCoin(t.getX(), t.getY(), t.getWidth(), t.getHeight());
    }

    public Tile getTileByID(int ID) {
        for(Tile t : tiles) {
            if(t.getID() == ID)
                return t;
        }
        return null;
    }
}
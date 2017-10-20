package com.retrochicken.slide;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import com.retrochicken.slide.Scenes.CentralData;

import java.util.ArrayList;

/**
 * Created by Will on 3/9/2016.
 * Manages the coins being instantiated on the game field.
 */
public class CoinManager {
    private ArrayList<FadeOutScore> coinBonuses;
    private ArrayList<Coin> coins;
    private static Bitmap[] currencies;
    private int[][] values;
    private float tileX, tileWidth;

    private long totalTime = 0;

    private static final int COIN_LIFETIME = 3000;

    public CoinManager(Bitmap[] currencies, int[][] values, float tileX, float tileWidth) {
        coins = new ArrayList<>();
        this.values = values;
        this.tileWidth = tileWidth;
        this.tileX = tileX;
        float coinWidth = tileWidth/3.5f;
        float scaleFactor = coinWidth/currencies[0].getWidth();
        if(CoinManager.currencies == null) {
            CoinManager.currencies = currencies;
            for (int i = 0; i < currencies.length; i++)
                CoinManager.currencies[i] = Bitmap.createScaledBitmap(currencies[i], (int) (currencies[i].getWidth() * scaleFactor), (int) (currencies[i].getHeight() * scaleFactor), false);
        }
        coinBonuses = new ArrayList<>();
    }

    public void update(long timeMillis) {
        for(int i = coinBonuses.size() - 1; i >= 0; i--) {
            coinBonuses.get(i).update();
            if(coinBonuses.get(i).getAlpha() <= 0)
                coinBonuses.remove(i);
        }
        totalTime += timeMillis;
        if(Math.random() * totalTime/3000 > 0.9) {
            generateCoin();
            totalTime = 0;
        }
        for(int i = coins.size() - 1; i >= 0; i--) {
            coins.get(i).update(timeMillis);
            if(coins.get(i).getTotalTime() >= COIN_LIFETIME)
                coins.remove(i);
        }
    }

    private void generateCoin() {
        int index = 0;
        int count = 0;
        while(count < values[values.length - 1][1] && Math.random() >= 0.5) {
            count++;
        }
        for(int i = values.length - 1; i >= 0; i--) {
            if(values[i][1] <= count) {
                index = i;
                break;
            }
        }
        Bitmap image = currencies[index];
        int value = values[index][0];
        float xInc, y;
        int imgWidth = image.getWidth();
        xInc = (float)Math.random()*Math.min(2 * tileWidth/3f - imgWidth, tileX - imgWidth);
        y = (float)Math.random() * (Constants.SCREEN_HEIGHT - image.getHeight() - 2*image.getHeight()/3.0f);
        coins.add(new Coin(tileX - imgWidth - xInc, image.getHeight() + 2*image.getHeight()/3.0f + y, image, value));
    }

    public void overCoin(float x, float y, float width, float height) {
        for(int i = coins.size() - 1; i >= 0; i--) {
            Coin coin = coins.get(i);
            if(x <= coin.getX() && x + width >= coin.getX() + coin.getWidth() && y >= coin.getY() && y - height <= coin.getY() - coin.getHeight()) {
                CentralData.GOLD += coin.getValue();
                coinBonuses.add(new FadeOutScore(Color.rgb(255, 165, 0), coin.getValue(), Constants.SCREEN_WIDTH / 25f, Constants.SCREEN_HEIGHT / 2, Constants.SCREEN_WIDTH / 3f, Constants.SCREEN_HEIGHT / 25f, Constants.SCREEN_HEIGHT/9));
                coins.remove(i);
            }
        }
    }

    public void draw(Canvas canvas) {
        for(Coin coin : coins)
            coin.draw(canvas);
        for(FadeOutScore fd : coinBonuses)
            fd.draw(canvas);
    }
}

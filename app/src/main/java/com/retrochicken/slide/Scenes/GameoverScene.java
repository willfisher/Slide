package com.retrochicken.slide.Scenes;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.retrochicken.slide.Constants;
import com.retrochicken.slide.GamePanel;
import com.retrochicken.slide.SlideManager;

/**
 * Created by Will on 2/5/2017.
 */
public class GameoverScene implements Scene {

    private static SharedPreferences statData;

    public GameoverScene() {
        statData = Constants.CURRENT_CONTEXT.getSharedPreferences("STATS", Context.MODE_PRIVATE);
        CentralData.gamesPlayed = statData.getInt("GAMES_PLAYED", 0);
    }

    @Override
    public void recieveTouch(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if(System.currentTimeMillis() - GameplayScene.deadTime > 500) {
                    CentralData.gamesPlayed++;
                    SharedPreferences.Editor editor = statData.edit();
                    editor.putInt("GAMES_PLAYED", CentralData.gamesPlayed);
                    editor.apply();
                    GameplayScene.newHighScore = false;
                    SceneManager.ACTIVE_SCENE = 0;
                }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        SlideManager.setTextSizeForDim(p, Constants.SCREEN_WIDTH - Constants.SCREEN_WIDTH / 4, 0, "You Reached " + CentralData.score);
        Paint p1 = new Paint();
        p1.setColor(Color.BLACK);
        SlideManager.setTextSizeForDim(p1, (Constants.SCREEN_WIDTH - Constants.SCREEN_WIDTH / 4) / 3, 0, "High Score");
        p.setTypeface(GamePanel.GLOBAL_TYPEFACE);
        p1.setTypeface(GamePanel.GLOBAL_TYPEFACE);
        canvas.drawText("You Reached " + CentralData.score, Constants.SCREEN_WIDTH/10, Constants.SCREEN_HEIGHT/2, p);
        if(GameplayScene.newHighScore) {
            canvas.drawText("New High Score!", Constants.SCREEN_WIDTH/10, Constants.SCREEN_HEIGHT/2 + 5*(p.descent() - p.ascent())/4, p1);
        } else {
            canvas.drawText("High Score: " + GameplayScene.highScore, Constants.SCREEN_WIDTH/10, Constants.SCREEN_HEIGHT/2 + 5*(p.descent() - p.ascent())/4, p1);
        }
        canvas.drawText("Gold Earned: " + (CentralData.GOLD - CentralData.getStartGold()), Constants.SCREEN_WIDTH/10, Constants.SCREEN_HEIGHT/2 + 5*(p.descent() - p.ascent())/4 + 5*(p1.descent() - p1.ascent())/4, p1);
    }

    @Override
    public void update(long timeMillis) {

    }
}

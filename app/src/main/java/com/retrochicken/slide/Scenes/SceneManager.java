package com.retrochicken.slide.Scenes;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by Will on 8/16/2016.
 */
public class SceneManager {
    private static Scene[] scenes;
    public static int ACTIVE_SCENE = 0;
    public static Scene getScene(int index) {
        return scenes[index];
    }

    public static void initScenes() {
        scenes = new Scene[]{new MenuScene(), new GameplayScene(), new GameoverScene(), new OptionsScene(), new HTPScene(), new StatisticsScene(), new StoreScene()};
    }

    public static void recieveTouch(MotionEvent event) {
        scenes[ACTIVE_SCENE].recieveTouch(event);
    }

    public static void update(long timeMillis) {
        scenes[ACTIVE_SCENE].update(timeMillis);
    }

    public static void draw(Canvas canvas) {
        scenes[ACTIVE_SCENE].draw(canvas);
    }
}

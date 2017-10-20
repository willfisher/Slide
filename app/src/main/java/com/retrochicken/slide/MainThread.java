package com.retrochicken.slide;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by Will on 2/22/2016.
 */
public class MainThread extends Thread {
    public static final int maxFPS = 30;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    public static Canvas canvas;

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        long startTime;
        long timeMillis = 1000/maxFPS;
        long waitTime;
        int frameCount = 0;
        long totalTime = 0;
        long targetTime = 1000/maxFPS;

        while(running) {
            startTime = System.nanoTime();
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gamePanel.update(timeMillis);
                    this.gamePanel.draw(canvas);
                }
            } catch(Exception e) {

            } finally {
                if(canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch(Exception e) {e.printStackTrace();}
                }
            }
            timeMillis = (System.nanoTime() - startTime)/1000000;
            waitTime = targetTime - timeMillis;
            try {
                if (waitTime > 0) {
                    this.sleep(waitTime);
                    timeMillis = targetTime;
                }
            } catch(Exception e) {}

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if(frameCount == maxFPS) {
                averageFPS = 1000/(totalTime/frameCount)/1000000;
                frameCount = 0;
                totalTime = 0;
                //System.out.println(averageFPS);
            }
        }
    }

    public double getAverageFPS() {
        return averageFPS;
    }

    public boolean getRunning() {
        return running;
    }
    public void setRunning(boolean running) {
        this.running = running;
    }
}

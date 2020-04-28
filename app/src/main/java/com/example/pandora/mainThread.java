package com.example.pandora;

import android.os.SystemClock;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

public class mainThread extends Thread {
    SurfaceHolder surfaceHolder;//class that handles the surface functions
    gameView game;
    boolean isRunning;//current state of the thread

    mainThread(SurfaceHolder surfaceHolder, gameView game) {
        super();
        Log.i("print", "mainThread()");
        this.surfaceHolder = surfaceHolder;
        this.game = game;
    }

    @Override
    public void run() {
        Log.i("print", "run()");
        while (isRunning) {
            long pTime= SystemClock.elapsedRealtime();
            game.canvas = null;

            try {
                game.canvas = this.surfaceHolder.lockCanvas();
                game.canvasHeight=game.canvas.getHeight();
                game.canvasWidth=game.canvas.getWidth();
                synchronized (surfaceHolder) {

                    this.game.update(game.canvas);

                   this.game.draw(game.canvas);
                }
            } catch (Exception e) {
            } finally {
                if (game.canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(game.canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            long cTime=SystemClock.elapsedRealtime();
            long fps=1000/(cTime-pTime);
            if(Math.abs(fps-game.fps)>=5){
                game.fps=fps;
            }

        }
    }

    public void setRunning(boolean isRunning) {
        Log.i("print", "setRunning()");
        this.isRunning = isRunning;
    }
}

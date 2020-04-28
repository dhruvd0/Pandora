package com.example.pandora;

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
            game.canvas = null;

            try {
                game.canvas = this.surfaceHolder.lockCanvas();
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
        }
    }

    public void setRunning(boolean isRunning) {
        Log.i("print", "setRunning()");
        this.isRunning = isRunning;
    }
}

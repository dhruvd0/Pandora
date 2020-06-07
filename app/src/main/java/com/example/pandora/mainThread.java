package com.example.pandora;

import android.os.SystemClock;
import android.util.Log;

import android.view.SurfaceHolder;

public class mainThread extends Thread {
    final SurfaceHolder surfaceHolder;//class that handles the surface functions
    gameView game;
    private boolean isRunning;//current state of the thread
    static int threadCount=0;
    mainThread(SurfaceHolder surfaceHolder, gameView game) {

        super();
        threadCount++;
        setName("GameThread:"+threadCount);
        Log.i("print", "mainThread()");
        this.surfaceHolder = surfaceHolder;
        this.game = game;
        setPriority(10);
    }

    @Override
    public void run() {

        while (isRunning) {
            long pTime = SystemClock.elapsedRealtime();
            gameView.canvas = null;

            try {
                gameView.canvas = this.surfaceHolder.lockCanvas();
                game.canvasHeight = gameView.canvas.getHeight();
                game.canvasWidth = gameView.canvas.getWidth();
                if (game.spaceship.x <= 0 && game.spaceship.y <= 0) {
                    game.spaceship.setPos(game.canvasWidth / 2, game.canvasHeight);
                }
                synchronized (surfaceHolder) {


                    game.draw(gameView.canvas);
                    game.update(gameView.canvas);

                }
            } catch (Exception ignored) {
            } finally {
                if (gameView.canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(gameView.canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            long cTime = SystemClock.elapsedRealtime();
            long fps = 1000 / (cTime - pTime);
            if (Math.abs(fps - game.fps) >= 5) {
                game.fps = fps;
            }

        }
        if(!isRunning){
           isRunning=true;
        }
    }

    void setRunning(boolean isRunning) {

        this.isRunning = isRunning;
    }
}

package com.example.pandora;

import android.os.SystemClock;
import android.util.Log;

import android.view.SurfaceHolder;

public class mainThread extends Thread {
    final SurfaceHolder surfaceHolder;//class that handles the surface functions
    gameView game;
    private boolean isRunning;//current state of the thread

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
            gameView.canvas = null;

            try {
                gameView.canvas = this.surfaceHolder.lockCanvas();
                game.canvasHeight= gameView.canvas.getHeight();
                game.canvasWidth= gameView.canvas.getWidth();
                if(game.spaceship.x<=0 && game.spaceship.y<=0){
                    game.spaceship.setPos(game.canvasWidth/2,game.canvasHeight);
                }
                synchronized (surfaceHolder) {

                    this.game.update(gameView.canvas);

                   this.game.draw(gameView.canvas);
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
            long cTime=SystemClock.elapsedRealtime();
            long fps=1000/(cTime-pTime);
            if(Math.abs(fps-game.fps)>=5){
                game.fps=fps;
            }

        }
    }

    void setRunning(boolean isRunning) {
        Log.i("print", "setRunning()");
        this.isRunning = isRunning;
    }
}

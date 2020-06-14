package com.example.pandora;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.Window;
import android.view.WindowManager;

public class TutorialActivity extends Activity implements Runnable {
    Tutorial game;

    SurfaceHolder surfaceHolder;
    boolean isRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isRunning = true;
        game = new Tutorial(this, this);

        surfaceHolder = game.getHolder();
        setFullScreen();
        setContentView(game);
    }

    public void setFullScreen() {//sets the view to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }


    @Override
    public void run() {

        while (isRunning) {
            long pTime = SystemClock.elapsedRealtime();
            game.canvas = null;

            try {

                game.canvas = this.surfaceHolder.lockCanvas();

                game.canvasHeight = game.canvas.getHeight();
                game.canvasWidth = game.canvas.getWidth();
                if (game.spaceship.x <= 0 && game.spaceship.y <= 0) {
                    game.spaceship.setPos(game.canvasWidth / 2, game.canvasHeight);
                }

                synchronized (surfaceHolder) {


                    game.draw(game.canvas);
                    game.update(game.canvas);
                    isRunning = game.isPlaying;


                }
            } catch (Exception ignored) {
            } finally {
                if (game.canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(game.canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            long cTime = SystemClock.elapsedRealtime();
            if ((cTime - pTime) > 0) {
                long fps = 1000 / (cTime - pTime);
                if (Math.abs(fps - game.fps) >= 5) {
                    game.fps = fps;
                }
            }


        }
        if (!game.isPlaying) {

            quit();

        }

    }

    void quit() {


        game.gameThread.interrupt();

        game.gameThread = new Thread(this);
        startActivity(new Intent(TutorialActivity.this, MainActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("tut", "destroy");
        quit();
    }
}

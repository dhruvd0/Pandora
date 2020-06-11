package com.example.pandora;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;


public class GameActivity extends Activity implements Runnable {
    gameView game;

    SurfaceHolder surfaceHolder;
    boolean isRunning;
    Scores scores;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scores = new Scores();
        isRunning = true;
        game = new gameView(this, this);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        surfaceHolder = game.getHolder();
        setFullScreen();
        if(readScoreFromFile()==-1){
            openDialog();
        }
        else {
            setContentView(game);
        }

    }

    public void setFullScreen() {//sets the view to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    int readScoreFromFile() {
        int highScore = sharedPref.getInt("score", -1);
        String name = sharedPref.getString("name", "null");
        Log.i("log", "file:" + name);
        return highScore;
    }

    void saveScoreToFile(String userName, int userMaxScore) {
        editor = sharedPref.edit();
        editor.putInt("score", userMaxScore);
        editor.putString("name", userName);
        editor.apply();
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
            if (cTime - pTime != 0) {


                long fps = 1000 / (cTime - pTime);
                if (Math.abs(fps - game.fps) >= 5) {
                    game.fps = fps;
                }
            }


        }
        if (!game.isPlaying) {

            scores.pushScoreToFireStore("testUser", game.score);
            game.gameThread.interrupt();
            game.gameThread = new Thread(this);
            startActivity(new Intent(GameActivity.this, MainActivity.class));


        }

    }

    public void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_username, null);

        builder.setView(view)
                .setTitle("Enter Name")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        game.gameThread.interrupt();
        game.gameThread = new Thread(this);
        startActivity(new Intent(GameActivity.this, MainActivity.class));
    }
}

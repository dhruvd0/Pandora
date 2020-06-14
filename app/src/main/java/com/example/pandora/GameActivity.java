package com.example.pandora;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import android.view.LayoutInflater;
import android.view.SurfaceHolder;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;


public class GameActivity extends Activity implements Runnable {
    gameView game;

    SurfaceHolder surfaceHolder;
    boolean isRunning;
    FireStoreHandler fireStoreHandler;

    Map<String, Object> user;
    EditText editText;


    public void openDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_username, null);
        editText = view.findViewById(R.id.name);
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

                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });
                        String userName = editText.getText().toString();

                        user.put("name", userName);
                        setContentView(game);

                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isRunning = true;
        fireStoreHandler = new FireStoreHandler(this);

        game = new gameView(this, this);
        surfaceHolder = game.getHolder();
        setFullScreen();
        user = new HashMap<>();
        MainActivity.log(user + "");
        openDialog();


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
            if (cTime - pTime != 0) {


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
        user.put("score", game.score);
        try {
            MainActivity.fireStoreHandler.pushScoreToFireStore(user.get("name").toString(), game.score);
        } catch (NullPointerException n) {
            user.put("name", "null");
        }

        MainActivity.fireStoreHandler.getScores();
        game.gameThread.interrupt();
        game.gameThread = new Thread(this);
        startActivity(new Intent(GameActivity.this, MainActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        quit();
    }
}

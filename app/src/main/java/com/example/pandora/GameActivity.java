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
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;


public class GameActivity extends Activity implements Runnable {
    gameView game;

    SurfaceHolder surfaceHolder;
    boolean isRunning;
    Scores fireStoreHandler;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    Map<String, Object> user;
    EditText editText;

    Map<String, Object> readUserFromFile() {
        int highScore = sharedPref.getInt("score", -1);
        String name = sharedPref.getString("name", "null");
        Log.i("log", "file:" + name);
        Map<String, Object> saveUser = new HashMap<String, Object>();
        saveUser.put("name", name);
        saveUser.put("score", highScore);
        return saveUser;

    }

    public void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);

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


                        String userName = editText.getText().toString();
                        user.put("name", userName);
                        saveUserToFile(userName, 0);
                        setContentView(game);

                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    void saveUserToFile(String userName, int userMaxScore) {
        editor = sharedPref.edit();
        editor.putInt("score", userMaxScore);
        editor.putString("name", userName);
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fireStoreHandler = new Scores();
        isRunning = true;
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        game = new gameView(this, this);
        surfaceHolder = game.getHolder();
        setFullScreen();
        user = readUserFromFile();

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

    int getUserPersonalBest(String name) {
        for (Map<String, Object> u : fireStoreHandler.fireBaseScores) {

            try {
                if (u.get("name").equals(name)) {
                    return (int) u.get("score");
                }
            } catch (NullPointerException nullPointer) {
                return -1;
            }
        }
        return -1;
    }

    void UpdateScore(int score) {
        if (score > getUserPersonalBest(user.get("name").toString())) {
            saveUserToFile(user.get("name").toString(), score);

            fireStoreHandler.pushScoreToFireStore(user.get("name").toString(), score);
        } else {
            //dont update;
        }

    }

    void quit() {
        UpdateScore(game.score);
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

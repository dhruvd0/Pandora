package com.example.pandora;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {
    static FireStoreHandler fireStoreHandler;
    static int highScore = 0;
    static Map<String, Object> user;
    static String hint = "Name";

    public void setFullScreen() {//sets the view to full screen
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
     /*   getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public void openDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
        final EditText editText;
        user = new HashMap<>();

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_username, null);
        editText = view.findViewById(R.id.name);
        editText.setHint(hint);
        builder.setView(view)
                .setTitle("Type Your Name:")

                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNeutralButton("Start", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        String userName = editText.getText().toString();
                        if (userName.equals("")) {
                            hint = "Your Name Cant Be Empty";


                        } else {
                            user.put("name", userName);
                            startActivity(new Intent(MainActivity.this, GameActivity.class));
                        }
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });


                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    void mainMenu() {
        setFullScreen();
        setContentView(R.layout.activity_main);
        Button startButton = (Button) findViewById(R.id.play);
        Button startTutorial = (Button) findViewById(R.id.tutorial);
        Button startLeaderBoard = (Button) findViewById(R.id.leaderboard);
        startLeaderBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, Leaderboard.class));


            }
        });
        startButton.getBackground().setAlpha(0);
        startTutorial.getBackground().setAlpha(0);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog();


            }
        });
        startTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TutorialActivity.class));
            }
        });

    }

    static void logFireBaseEvent(String eventName, Context context) {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle b = new Bundle();
        b.putString(FirebaseAnalytics.Param.ITEM_ID, "test ID");
        mFirebaseAnalytics.logEvent(eventName, b);
    }

    static void log(String msg) {
        Log.i("log", msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        logFireBaseEvent(FirebaseAnalytics.Event.APP_OPEN, this);
        fireStoreHandler = new FireStoreHandler(this);
        fireStoreHandler.getScores();
        setFullScreen();
        mainMenu();

    }

}

package com.example.pandora;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends Activity {
    static FireStoreHandler fireStoreHandler;

    public void setFullScreen() {//sets the view to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
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

                startActivity(new Intent(MainActivity.this, GameActivity.class));


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
        mainMenu();

    }

}

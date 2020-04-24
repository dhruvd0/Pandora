package com.example.pandora;


import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity {

    public void setFullScreen() {//sets the view to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public void startGame() {
        setContentView(new gameView(this));//sets the content of the activity to game View which creates a surface
    }

    void mainMenu() {
        setContentView(R.layout.activity_main);
        Button startButton = (Button) findViewById(R.id.play);
        startButton.getBackground().setAlpha(0);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setFullScreen();

        startGame();
    }
}

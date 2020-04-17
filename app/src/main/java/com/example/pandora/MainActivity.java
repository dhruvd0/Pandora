package com.example.pandora;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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

    public void startGame(View view) {
        setContentView(new gameView(this));//sets the content of the activity to game View which creates a surface
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_main);
        Button startButton = (Button) findViewById(R.id.play);
        startButton.getBackground().setAlpha(0);

        Log.i("print",Integer.toString(R.drawable.planet1));
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(v);
            }
        });

    }
}

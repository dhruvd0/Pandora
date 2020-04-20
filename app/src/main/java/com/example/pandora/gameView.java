package com.example.pandora;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceControl;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;

public class gameView extends SurfaceView implements SurfaceHolder.Callback {
    //basic surface class where we would create canvas and draw
    public static Canvas canvas;
    mainThread thread;//start a thread when the surface is created;
    Sprite PlayerSprite;
    Planet[] planets = new Planet[5];
    Bitmap space;

    public gameView(Context context) {
        super(context);
        Log.i("print", "gameView()");
        thread = new mainThread(getHolder(), this);
        getHolder().addCallback(this);

        setFocusable(true);
        loadSprites();
    }

    public void loadSprites() {
        space = BitmapFactory.decodeResource(getResources(), R.drawable.space);
        PlayerSprite = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.spaceship));
        PlayerSprite.setPos(500, 900);
        int id = R.drawable.planet1;
        for (int i = 0; i < 5; i++) {
            planets[i] = new Planet(BitmapFactory.decodeResource(getResources(), id));
            id++;
            planets[i].setPos(0, i * 300);
        }
    }

    public void update() {


        PlayerSprite.move(0, -5);
        planets[0].rotate(canvas,2);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("print", "surfaceCreated()");
        thread.setRunning(true);

        thread.start();//start the thread

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("print", "changed");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);//stops the thread when user quits
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public void draw(Canvas canvas) {

        super.draw(canvas);
        //graphics and drawing

        canvas.drawBitmap(space, 0, 0, null);

        PlayerSprite.draw(canvas);
        Planet.drawPlanets(this.planets, canvas);

    }


}

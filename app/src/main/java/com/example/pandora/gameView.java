package com.example.pandora;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.SurfaceView;
import android.view.SurfaceHolder;


public class gameView extends SurfaceView implements SurfaceHolder.Callback {
    //basic surface class where we would create canvas and draw
    public static Canvas canvas;
    mainThread thread;//start a thread when the surface is created;
    Sprite spaceship;
    Planet[] planets = new Planet[5];
    Bitmap space;
    Display display;
    Point point;
    int scr_wid, scr_hei;
    Rect rect;
    Paint paint;

    public gameView(Context context) {
        super(context);
        Log.i("print", "gameView()");
        thread = new mainThread(getHolder(), this);
        getHolder().addCallback(this);

        setFocusable(true);
        loadSprites();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setColor(Color.BLACK);
        display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        scr_wid = point.x;
        scr_hei = point.y;
        rect = new Rect(0, 0, scr_wid, scr_hei);
    }

    public void loadSprites() {
        space = BitmapFactory.decodeResource(getResources(), R.drawable.space);

        spaceship = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.spaceship));


        Planet.loadPlanets(planets, getResources());

        spaceship.setPos(500, 900);
    }

    public void update(Canvas canvas) {

        spaceship.move(0,-5);

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
        canvas.drawBitmap(space, null, rect, null);
        Planet.drawPlanets(planets,canvas);
        spaceship.draw(canvas);


    }


}

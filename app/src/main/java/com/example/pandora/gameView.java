package com.example.pandora;

import android.annotation.SuppressLint;
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
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;


public class gameView extends SurfaceView implements SurfaceHolder.Callback {
    //basic surface class where we would create canvas and draw
    public static Canvas canvas;
    mainThread thread;//start a thread when the surface is created;
    Sprite spaceship;
    Planet[] planets = new Planet[3];
    Bitmap space;
    Display display;
    Point point;
    int scr_wid, scr_hei;
    Rect rect;
    Paint paint;
    long fps;
    int starCount;
    float canvasHeight, canvasWidth;
    Star[] stars;
    collisionThread spaceshipCollision;

    static boolean isDecreasing;

    public gameView(Context context) {

        super(context);


        Log.i("print", "gameView()");
        thread = new mainThread(getHolder(), this);
        getHolder().addCallback(this);

        setFocusable(true);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setColor(Color.GREEN);
        display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        scr_wid = point.x;
        scr_hei = point.y;
        rect = new Rect(0, 0, scr_wid, scr_hei);
        starCount = 300;
        stars = new Star[starCount];
        loadSprites();

    }

    public void loadSprites() {
        space = BitmapFactory.decodeResource(getResources(), R.drawable.space);

        spaceship = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.spaceship2));
        Planet.loadPlanets(planets, getResources());
        spaceship.ySpeed = -5;

        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(5000, 5000);
        }
        Star.selectRandomStars(stars);
        spaceshipCollision=new collisionThread(this);
    }

    public void update(Canvas canvas) {

        spaceshipCollision.start();
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

    void displayText(Canvas canvas, String text, float x, float y) {
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        textPaint.setColor(Color.GREEN);
        textPaint.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));

        canvas.drawText(text, x, y, textPaint);
    }


    void drawBackground(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        Star.drawStars(stars, canvas);
    }

    void showSpaceShipStats(Canvas canvas) {
        displayText(canvas, "Speed:" + spaceship.ySpeed, spaceship.x + 200, spaceship.y);
        displayText(canvas, "Angle:" + spaceship.circleAngle, spaceship.x + 200, spaceship.y + 50);
        displayText(canvas, "X:" + spaceship.x, spaceship.x + 200, spaceship.y + 100);
        displayText(canvas, "Y:" + spaceship.y, spaceship.x + 200, spaceship.y + 150);
        displayText(canvas, "FPS:" + Float.toString(fps), spaceship.x + 200, spaceship.y + 200);
    }

    void drawSprites(Canvas canvas) {
        Star.drawStars(stars, canvas);
        Planet.drawPlanets(planets, canvas);

        spaceship.draw(canvas);


    }

    void showCenter(Sprite sprite) {
        canvas.drawCircle(sprite.cx, sprite.cy, 10, paint);
    }

    public void draw(Canvas canvas) {

        super.draw(canvas);

        spaceship.draw(canvas);
        planets[0].draw(canvas);


    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int e = event.getAction();
        if (e == MotionEvent.ACTION_DOWN) {
            isDecreasing = true;

            update(canvas);
        } else if (e == MotionEvent.ACTION_UP) {
            spaceship.ySpeed = -5;
            isDecreasing = false;
            update((canvas));
        }


        return true;
    }
}

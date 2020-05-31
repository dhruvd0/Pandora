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
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;


public class gameView extends SurfaceView implements SurfaceHolder.Callback {
    //basic surface class where we would create canvas and draw
    public static Canvas canvas;
    mainThread thread;//start a thread when the surface is created;
    Spaceship spaceship;
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


    static boolean isDecreasing;

    public gameView(Context context) {

        super(context);


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


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        thread.setRunning(true);
        // spaceshipCollisionThread.start();//thread checks spaceship's collision distance and displays message
        thread.start();//start the main game loop


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

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

    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        int e = event.getAction();
        if (e == MotionEvent.ACTION_DOWN) {
            isDecreasing = true;

            update(canvas);
        } else if (e == MotionEvent.ACTION_UP) {
            spaceship.unhook();

            spaceship.ySpeed = -5;
            isDecreasing = false;
            update((canvas));
        }


        return true;
    }


    public void loadSprites() {
        space = BitmapFactory.decodeResource(getResources(), R.drawable.space);

        spaceship = new Spaceship(BitmapFactory.decodeResource(getResources(), R.drawable.spaceship2));
        Planet.loadPlanets(planets, getResources());
        spaceship.ySpeed = -5;

        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(5000, 5000);
        }
        Star.selectRandomStars(stars);

    }


    public boolean collisionHandler() {//handles spaceship collision and resets the position

        int minCollideDistance = 300;

        for (Planet p : planets) {

            if (p.active) {
                if (spaceship.collisionDist(p) < minCollideDistance) {
                    if (!spaceship.isHooked) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    void showSpaceShipStats(Canvas canvas) {
        // displayText(canvas, "Speed:" + spaceship.ySpeed, spaceship.x + 200, spaceship.y);
        displayText(canvas, "Circle Angle:" + spaceship.circleAngle, spaceship.x + 200, spaceship.y + 50);
        displayText(canvas, "Rotate angle:" + spaceship.rotateAngle, spaceship.x + 200, spaceship.y + 100);
        displayText(canvas, "isHooked:" + spaceship.isHooked, spaceship.x + 200, spaceship.y + 150);

    }

    void drawSprites(Canvas canvas) {
        //Star.drawStars(stars, canvas);
        //Planet.drawPlanets(planets, canvas);
        planets[0].draw(canvas);
        spaceship.draw(canvas);
    }

    void showCenter(Sprite sprite) {
        canvas.drawCircle(sprite.cx, sprite.cy, 10, paint);
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

    public void draw(Canvas canvas) {

        super.draw(canvas);
        drawSprites(canvas);
        showSpaceShipStats(canvas);
        displayText(canvas, Float.toString(fps), canvasWidth - 150, 50);
    }

    public void update(Canvas canvas) {


        planets[0].setPos(canvasWidth / 2, canvasHeight / 2);
        if (isDecreasing) {//decrease speed on touch
            if (spaceship.ySpeed >= 0) {
                spaceship.ySpeed = 0;
            } else {
                spaceship.ySpeed += 0.1;
            }
        }
        if (spaceship.y < 0) {//spaceship reaches end of canvas
           /* Star.setStars(stars, canvas);
            Planet.loadPlanets(planets, getResources());*/
            spaceship.y = canvasHeight;
        } else if (collisionHandler() || spaceship.isHooked) {

            spaceship.revolve(planets[0]);
        } else {
            spaceship.move();
        }
        //collisionHandler();


    }


}

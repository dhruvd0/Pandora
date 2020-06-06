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
import android.widget.Button;


public class gameView extends SurfaceView implements SurfaceHolder.Callback {
    //basic surface class where we would create canvas and draw
    public static Canvas canvas;
    mainThread thread = new mainThread(getHolder(), this);//start a thread when the surface is created;
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
    boolean spaceshipNearPlanet;
    Bitmap spaceshipImg, SpaceshipImgGreen;
    static boolean touchDown;
    Obstacles[] meteors = new Obstacles[3];
    boolean isPlaying;
    Button pauseButton;
    public gameView(Context context) {

        super(context);
        spaceshipNearPlanet = false;
        spaceshipImg = BitmapFactory.decodeResource(getResources(), R.drawable.spaceship);
        SpaceshipImgGreen = BitmapFactory.decodeResource(getResources(), R.drawable.green);
        getHolder().addCallback(this);
        isPlaying=true;
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

     void loadSprites() {
        space = BitmapFactory.decodeResource(getResources(), R.drawable.space);

        for (int i = 0; i < meteors.length; i++) {
            meteors[i] = new Obstacles(BitmapFactory.decodeResource(getResources(), R.drawable.meteor));
        }
        spaceship = new Spaceship(spaceshipImg);
        Planet.loadPlanets(planets, getResources(), scr_wid, scr_hei);
        spaceship.ySpeed = -5;

        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(5000, 5000);
        }
        Star.selectRandomStars(stars);

    }

    boolean obstacleCollision() {

        for (Obstacles m : meteors) {

            if (m.active) {
                if (spaceship.collisionDist(m) <= 100) {

                  return true;
                }
            }
        }
        return false;
    }

    boolean planetCollision() {//handles spaceship collision and resets the position


        for (Planet p : planets) {

            if (p.active) {
                if (spaceship.collisionDist(p) <= spaceship.minCollideDistance) {
                    if (!spaceship.isHooked) {

                        spaceship.image = SpaceshipImgGreen;
                        spaceship.hookedPLanet = p;
                        return true;
                    }

                }
            }
        }
        return false;
    }

    void pause(){
       isPlaying=false;
       thread.setRunning(false);

    }
    void resume(){
        isPlaying=true;
        thread=new mainThread(getHolder(),this);
        thread.setRunning(true);
        thread.start();

    }
    public void update(Canvas canvas) {


        Obstacles.update(meteors);

        if (touchDown) {//decrease speed on touch down
            if (spaceship.energy > 0) {
                spaceship.ySpeed += 0.1;
                spaceship.xSpeed -= 0.1;
            }

            if (spaceship.ySpeed >= 0 || spaceship.xSpeed <= 0) {
                spaceship.ySpeed = 0;
                spaceship.xSpeed = 0;
            }
            if(!spaceship.isHooked){
                spaceship.energy -= 0.1;
            }

        }

        if (spaceship.reachedBounds(canvas)) {//spaceship reaches end of canvas
            Star.setStars(stars, canvas);
            Planet.loadPlanets(planets, getResources(), scr_wid, scr_hei);

        } else if (spaceship.isHooked) {//handles collision with a planet
            spaceship.revolve();

        } else {//move with ySpeed,xSpeed and rotateAngle

            if(obstacleCollision()){
                spaceship.health-=10;
                if(spaceship.health<0){
                    spaceship.health=0;
                }
            }
            if (planetCollision()) {
                spaceshipNearPlanet = true;
            } else {
                spaceshipNearPlanet = false;
            }
            spaceship.move();

            if (spaceship.afterUnhookAngle != -1) {
                spaceship.rotate(spaceship.rotateAngle - spaceship.afterUnhookAngle);
            }
            if (spaceship.collisionDist(spaceship.hookedPLanet) > spaceship.minCollideDistance) {
                spaceship.image = spaceshipImg;
            }


        }
        if(spaceship.health<=0){

            pause();
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    void drawSprites(Canvas canvas) {
        Star.drawStars(stars, canvas);
        Planet.drawPlanets(planets, canvas);

        spaceship.draw(canvas);
        Obstacles.drawObstacles(meteors, canvas);
    }

    public void draw(Canvas canvas) {

        super.draw(canvas);

        drawSprites(canvas);
        displayText(canvas, Float.toString(fps), canvasWidth - 150, 50);
        displayText(canvas, "Energy:" + spaceship.energy, 100, 100);
        displayText(canvas, "Health:" + spaceship.health, 300, 100);

    }

    @SuppressLint("ClickableViewAccessibility")
     public boolean onTouchEvent(MotionEvent event) {
        int e = event.getAction();
        if (e == MotionEvent.ACTION_DOWN) {
            touchDown = true;

        } else if (e == MotionEvent.ACTION_UP) {
            if(isPlaying){
                pause();
            }
            else{
                resume();
            }
            touchDown = false;
            if (spaceshipNearPlanet) {

                spaceship.isHooked = true;
                spaceship.image = spaceshipImg;
                spaceshipNearPlanet = false;


            } else if (spaceship.isHooked) {

                spaceship.hookedPLanet = null;
                spaceship.unhook();
                spaceshipNearPlanet = false;
            }
            spaceship.xSpeed = 5;
            spaceship.ySpeed = -5;
        }


        return true;
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

    void showSpaceShipStats(Canvas canvas) {
        /* displayText(canvas, "Speed:" + spaceship.ySpeed, spaceship.x + 200, spaceship.y);*/
        displayText(canvas, "X:" + spaceship.x, spaceship.x + 200, spaceship.y + 50);
        displayText(canvas, "Y:" + spaceship.y, spaceship.x + 200, spaceship.y + 100);
        displayText(canvas, "Rotate angle:" + spaceship.rotateAngle, spaceship.x + 200, spaceship.y + 150);
        displayText(canvas, "Skyhook angle:" + planets[0].skyhook.rotateAngle, spaceship.x + 200, spaceship.y + 200);
    }

    void displayText(Canvas canvas, String text, float x, float y) {
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        textPaint.setColor(Color.GREEN);
        textPaint.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));

        canvas.drawText(text, x, y, textPaint);
    }
}

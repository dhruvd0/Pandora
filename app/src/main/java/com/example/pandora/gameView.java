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

import androidx.appcompat.app.AppCompatActivity;


public class gameView extends SurfaceView implements SurfaceHolder.Callback {
    //basic surface class where we would create canvas and draw
    public Canvas canvas;
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
    int score;
    Wormwhole wormwhole_in, wormhole_out;
    Thread gameThread;
    boolean tap;

    void initView() {
        spaceshipNearPlanet = false;
        spaceshipImg = BitmapFactory.decodeResource(getResources(), R.drawable.spaceship);
        SpaceshipImgGreen = BitmapFactory.decodeResource(getResources(), R.drawable.green);
        getHolder().addCallback(this);
        isPlaying = true;
        wormwhole_in = new Wormwhole(BitmapFactory.decodeResource(getResources(), R.drawable.wormhole_in));
        wormhole_out = new Wormwhole(BitmapFactory.decodeResource(getResources(), R.drawable.wormhole_out));

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

    public gameView(Context context, GameActivity gameActivity) {

        super(context);
        initView();
        gameThread = new Thread(gameActivity);
    }

    public gameView(Context context, TutorialActivity tutorialActivity) {

        super(context);
        initView();
        gameThread = new Thread(tutorialActivity);
    }


    void loadSprites() {
        space = BitmapFactory.decodeResource(getResources(), R.drawable.space);

        for (int i = 0; i < meteors.length; i++) {
            meteors[i] = new Obstacles(BitmapFactory.decodeResource(getResources(), R.drawable.meteor));
        }
        spaceship = new Spaceship(spaceshipImg);
        Planet.loadPlanets(planets, getResources(), scr_wid, scr_hei);


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
                        spaceship.hookedPlanet = p;
                        return true;
                    }

                }
            }
        }
        return false;
    }

    public void update(Canvas canvas) {

        Obstacles.update(meteors);

        if (touchDown) {//decrease speed on touch down
            if (spaceship.energy > 0) {
                spaceship.ySpeed += 0.5;
                spaceship.xSpeed -= 0.5;
            }

            if (spaceship.ySpeed >= 0 || spaceship.xSpeed <= 0) {
                spaceship.ySpeed = 0;
                spaceship.xSpeed = 0;
            }
            if (!spaceship.isHooked) {
                spaceship.energy -= 0.1;
            }


        }
        if (spaceship.health <= 0) {
            displayText(canvas, "Game Over Your Health is 0", canvasWidth / 2, canvasHeight / 2);
            isPlaying = false;
        }
        if (spaceship.reachedBounds(canvas)) {//spaceship reaches end of canvas
            Star.setStars(stars, canvas);
            Planet.loadPlanets(planets, getResources(), scr_wid, scr_hei);


        } else if (spaceship.isHooked) {//handles collision with a planet
            spaceship.revolve();

        } else {//move with ySpeed,xSpeed and rotateAngle

            if (obstacleCollision()) {
                spaceship.health -= 2;
                if (spaceship.health < 0) {
                    spaceship.health = 0;
                }
            }
            spaceshipNearPlanet = planetCollision();
            spaceship.move();
            score++;
            if (spaceship.afterUnhookAngle != -1) {
                spaceship.rotate(spaceship.rotateAngle - spaceship.afterUnhookAngle);
            }
            if (spaceship.collisionDist(spaceship.hookedPlanet) > spaceship.minCollideDistance) {
                spaceship.image = spaceshipImg;
            }


        }


    }

    void drawSprites(Canvas canvas) {
        Star.drawStars(stars, canvas);
        Planet.drawPlanets(planets, canvas);

        spaceship.draw(canvas);
        Obstacles.drawObstacles(meteors, canvas);

    }

    public void draw(Canvas canvas) {

        super.draw(canvas);

        drawSprites(canvas);

        displayText(canvas, "Energy:" + spaceship.energy, canvasWidth - 150, 100);
        displayText(canvas, "Health:" + spaceship.health, canvasWidth - 150, 200);
        displayText(canvas, "Score:" + score, canvasWidth - 150, 300);


    }


    public boolean onTouchEvent(MotionEvent event) {
        int e = event.getAction();
        if (e == MotionEvent.ACTION_DOWN) {
            touchDown = true;
            tap = false;

        } else if (e == MotionEvent.ACTION_UP) {

            tap = true;
            touchDown = false;
            if (spaceshipNearPlanet) {

                spaceship.isHooked = true;
                spaceship.image = spaceshipImg;
                spaceshipNearPlanet = false;


            } else if (spaceship.isHooked) {

                spaceship.hookedPlanet = null;
                spaceship.unhook();
                spaceshipNearPlanet = false;
            }

            spaceship.xSpeed = 10;
            spaceship.ySpeed = -10;
        }


        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        gameThread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    void displayText(Canvas canvas, String text, float x, float y) {
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.GREEN);
        textPaint.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));

        canvas.drawText(text, x, y, textPaint);
    }
}
